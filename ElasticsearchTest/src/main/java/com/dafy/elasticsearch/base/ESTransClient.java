package com.dafy.elasticsearch.base;

import static com.dafy.elasticsearch.util.Constants.ELASTIC_PROPERTY;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import com.dafy.elasticsearch.util.ObjectTools;
import com.dafy.elasticsearch.util.PropertyManager;


public class ESTransClient {

	private PropertyManager propManager = PropertyManager.instance();
	
	private String clusterName;
	
	private String host1;
	
	private String host2;
	
	private Integer port1;
	
	private Integer port2;
	
	private static TransportClient client = null;
	
	public ESTransClient(){
		this.clusterName = propManager.getValue(ELASTIC_PROPERTY, "cluster.name");
		this.host1 = propManager.getValue(ELASTIC_PROPERTY, "elastic.host1");
		this.port1 = Integer.parseInt(propManager.getValue(ELASTIC_PROPERTY, "elastic.port1"));
		this.host2 = propManager.getValue(ELASTIC_PROPERTY, "elastic.host2");
		this.port2 = Integer.parseInt(propManager.getValue(ELASTIC_PROPERTY, "elastic.port2"));
	}
	
	@SuppressWarnings("resource")
	public TransportClient getClient() throws UnknownHostException {
		if(ObjectTools.isNullByObject(client)){
	        // 开启client.transport.sniff功能，探测集群所有节点
	        Settings settings = Settings.builder()
	                .put("cluster.name", clusterName)
	                .put("client.transport.sniff", true).build();
	        // on startup
	        // 获取TransportClient
	        client = new PreBuiltTransportClient(settings)
	                .addTransportAddress(
	                        new InetSocketTransportAddress(InetAddress
	                                .getByName(host1), port1))
	                .addTransportAddress(
	                        new InetSocketTransportAddress(InetAddress
	                                .getByName(host2), port2));
        
		}
        List<DiscoveryNode> connectedNodes = client.connectedNodes();
        for (DiscoveryNode discoveryNode : connectedNodes) {
            System.out.println("集群节点："+discoveryNode.getHostName()+","+discoveryNode.getAddress());
        }
//        this.elasticIndex(client, "megacorp", "employee","4");
//        this.elasticGet(client,"megacorp", "employee", "1");
//        this.elasticDetete(client, "megacorp", "employee", "AV5LkWIIquVuxjNcCCFS");
//        this.elasticUpdate(client, "megacorp", "employee", "AV5Lsx1UquVuxjNcCCFT");
//        this.elasticUpsert(client, "megacorp", "employee", "5");
//        this.elasticGet(client,"megacorp","employee");

        return client;
    }
	
	public void closeClient(){
		if(client!=null){
			// on shutdown
	        client.close();
		}
	}
	
	public static void main(String[] args){
		ESTransClient connector = new ESTransClient();
		TransportClient client = null;
		try {
			client = connector.getClient();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ElasticDAO elasticDAO = new ElasticDAOImpl();
		GetResponse response = elasticDAO.getById(client, "megacorp", "employee", "5");
		System.out.println(response.getSourceAsString());
		if(!ObjectTools.isNullByObject(client)){
			connector.closeClient();
		}
	}
}
