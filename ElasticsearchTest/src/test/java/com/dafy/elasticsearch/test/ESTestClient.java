package com.dafy.elasticsearch.test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequestBuilder;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.support.WriteRequest.RefreshPolicy;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.DocValueFormat.DateTime;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Test;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;;

public class ESTestClient {
	
	@Test
	public void addBorrowIntentExt(){
		System.out.println("dd");
	}
	
	/**
     * 测试使用Java API 连接ElasticSearch 集群
     * 
     * @throws UnknownHostException
     */
    @Test
    public void test1() throws UnknownHostException {
        // on startup
        // 获取TransportClient
        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(
                        new InetSocketTransportAddress(InetAddress
                                .getByName("192.168.245.128"), 9300));
        
        List<DiscoveryNode> connectedNodes = client.connectedNodes();
        for (DiscoveryNode discoveryNode : connectedNodes) {
            System.out.println("集群节点："+discoveryNode.getHostName());
        }
        // on shutdown
        client.close();
    }

    /**
     * 生产环境下，ElasticSearch集群名称非默认需要显示设置
     * 
     * @throws UnknownHostException
     */
    @Test
    public void test2() throws UnknownHostException {
        Settings settings = Settings.builder()
                .put("cluster.name", "xzx1-elasticsearch").build();
        // on startup
        // 获取TransportClient
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(
                        new InetSocketTransportAddress(InetAddress
                                .getByName("192.168.245.128"), 9300));
        
        List<DiscoveryNode> connectedNodes = client.connectedNodes();
        for (DiscoveryNode discoveryNode : connectedNodes) {
            System.out.println("集群节点："+discoveryNode.getHostName());
        }

        // on shutdown
        client.close();
    }

    /**
     * 1、启动应用程序之后，client.transport.sniff能保证即使master挂掉也能连接上集群
     * 2、master节点挂机同时应用程序重启，则无法连接ElasticSearch集群
     * 
     * @throws UnknownHostException
     */
    @Test
    public void test3() throws UnknownHostException {

        // 开启client.transport.sniff功能，探测集群所有节点
        Settings settings = Settings.builder()
                .put("cluster.name", "xzx1-elasticsearch")
                .put("client.transport.sniff", true).build();
        // on startup
        // 获取TransportClient
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(
                        new InetSocketTransportAddress(InetAddress
                                .getByName("192.168.245.128"), 9300));

        List<DiscoveryNode> connectedNodes = client.connectedNodes();
        for (DiscoveryNode discoveryNode : connectedNodes) {
            System.out.println("集群节点："+discoveryNode.getHostName()+","+discoveryNode.getAddress());
        }

        // on shutdown
        client.close();
    }

    /**
     * 1、启动应用程序之后，client.transport.sniff能保证即使master挂掉也能连接上集群
     * 2、设置多节点，防止其中一个节点挂机同时应用程序重启，无法连接ElasticSearch集群问题
     * 
     * @throws UnknownHostException
     */
    @Test
    public void test4() throws UnknownHostException {

        // 开启client.transport.sniff功能，探测集群所有节点
        Settings settings = Settings.builder()
                .put("cluster.name", "xzx1-elasticsearch")
                .put("client.transport.sniff", true).build();
        // on startup
        // 获取TransportClient
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(
                        new InetSocketTransportAddress(InetAddress
                                .getByName("192.168.245.128"), 9300))
                .addTransportAddress(
                        new InetSocketTransportAddress(InetAddress
                                .getByName("192.168.245.128"), 9301));
        
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
        this.elasticSearch(client, "megacorp", "employee");

        // on shutdown
        client.close();
    }
    
    //根据ID查询指定数据
    private void elasticGet(TransportClient client,String index,String type,String id){
    	GetResponse response = client.prepareGet(index, type, id)
                .setOperationThreaded(false)
                .get();
        System.out.println(response.getSourceAsString());
    }
    
    //根据index、type查询多条数据
    private void elasticGet(TransportClient client,String index,String type){
    	SearchResponse response = client.prepareSearch(index).setTypes(type).get();
        SearchHits hits = response.getHits();
        SearchHit[] hitsArr = hits.getHits();
        for(SearchHit hit : hitsArr){
        	System.out.print("id: "+hit.getId());
        	System.out.println(hit.getSourceAsString());
        }
    }
    
    //创建索引结构
    private void CreateIndex(TransportClient client){
        CreateIndexRequestBuilder  cib=client.admin()
                .indices().prepareCreate("megacorp");
        XContentBuilder mapping;
		try {
			mapping = XContentFactory.jsonBuilder()
			     .startObject()
			        .startObject("employee")
				        .startObject("_all")//关闭_all字段,可选项，它用于不知道查询值属于哪个字段时可用该命令，_all字段在查询时占用更多的CPU和占用更多的磁盘空间，如果确实不需要它可以完全的关闭它或者基于每字段定制
		                    .field("enabled", false)
	                    .endObject()
			            .startObject("properties") //设置之定义字段
			              .startObject("first_name")
			                .field("type","string") //设置数据类型
			              .endObject()
			              .startObject("last_name")
			                 .field("type","string")
			              .endObject()
			              .startObject("age")
			                 .field("type","integer")
			              .endObject()
			              .startObject("about")
			                 .field("type","string")
			              .endObject()
			              .startObject("interests")
			                 .field("type","string")
			              .endObject()
			              .startObject("inputtime")
			                 .field("type","date")  //设置Date类型
			                 .field("format","yyyy-MM-dd HH:mm:ss") //设置Date的格式
			              .endObject()
			          .endObject()
			        .endObject()
			      .endObject();
		
			cib.addMapping("pointdata", mapping);   
			cib.execute().actionGet();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    //添加记录
    private void elasticIndex(TransportClient client,String index,String type,String id) {
    	IndexResponse response = null;
    	List interestList = Arrays.asList("swim","travel");
    	try {
    			if(id!=null && !"".equals(id)){
    					response = client.prepareIndex(index, type, id)
				        	.setSource(jsonBuilder()
				                    .startObject()
				                        .field("first_name", "assmark")
				                        .field("last_name", "tasha")
				                        .field("age", 29)
				                        .field("about", "trying out Elasticsearch")
				                        .field("interests",interestList)
				                    .endObject()
				                  )
				        	.get();
			
    			}else{
	    				response = client.prepareIndex(index, type)
	    	        	.setSource(jsonBuilder()
	    	                    .startObject()
	    	                    	.field("first_name", "assmark")
	    	                    	.field("last_name", "tasha")
	    	                    	.field("age", 29)
	    	                        .field("about", "trying out Elasticsearch")
	    	                        .field("interests",interestList)
	    	                    .endObject()
	    	                  )
	    	        	.get();
    			}
    	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	// Index name
    	String _index = response.getIndex();
    	System.out.println("index: "+_index);
    	// Type name
    	String _type = response.getType();
    	System.out.println("type: "+_type);
    	// Document ID (generated or not)
    	String _id = response.getId();
    	System.out.println("id: "+_id);
    	// Version (if it's the first time you index this document, you will get: 1)
    	long _version = response.getVersion();
    	System.out.println("version: "+_version);
    	// status has stored current instance statement.
    	RestStatus status = response.status();
    	System.out.println("status: "+status);
    }
    
    //根据ID删除信息
    private void elasticDetete(TransportClient client,String index,String type,String id){
    	DeleteResponse response = client.prepareDelete(index, type, id).get();
    	System.out.println(response.status());
    }
    
    //根据ID修改信息
    private void elasticUpdate(TransportClient client,String index,String type,String id){
    	UpdateResponse updateResponse = null;
    	UpdateRequest updateRequest = new UpdateRequest();
    	updateRequest.index(index);
    	updateRequest.type(type);
    	updateRequest.id(id);
    	try {
			updateRequest.doc(jsonBuilder()
			        .startObject()
			            .field("age", 35)
			        .endObject());
		
			updateResponse = client.update(updateRequest).get();
    	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	System.out.println(updateResponse.status());
    }
    
    //不存在ID则新建否则修改
    private void elasticUpsert(TransportClient client,String index,String type,String id){
    	IndexRequest indexRequest;
    	UpdateResponse updateResponse = null;
		try {
			indexRequest = new IndexRequest(index, type, id)
			        .source(jsonBuilder()
			            .startObject()
				            .field("first_name", "Smith")
	                    	.field("last_name", "yaho")
	                    	.field("age", 34)
	                        .field("about", "trying out Elasticsearch")
	                        .field("interests","swim")
			            .endObject());
		
			UpdateRequest updateRequest = new UpdateRequest(index, type, id)
    	        .doc(jsonBuilder()
    	            .startObject()
    	                .field("age", 34)
    	            .endObject())
    	        .upsert(indexRequest);              
			updateResponse = client.update(updateRequest).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(updateResponse.status());
    }
    
    //根据多条件查询
    private void elasticMultiGet(TransportClient client,String index,String type,String id){
    		MultiGetResponse multiGetItemResponses = client.prepareMultiGet()
    		    .add("twitter", "tweet", "1")           
    		    .add("twitter", "tweet", "2", "3", "4") 
    		    .add("another", "type", "foo")          
    		    .get();

    		for (MultiGetItemResponse itemResponse : multiGetItemResponses) { 
    		    GetResponse response = itemResponse.getResponse();
    		    if (response.isExists()) {                      
    		        String json = response.getSourceAsString(); 
    		    }
    		}
    }
    
    //条件查询及分页等
    public SearchResponse elasticSearch(TransportClient client, String index, String type) {
		SearchResponse response = client.prepareSearch(index).setTypes(type)
//				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH) 1.SearchType.DFS_QUERY_THEN_FETCH = 精确查询 2.SearchType.SCAN = 扫描查询,无序
				.setQuery(QueryBuilders.matchQuery("about", "like"))				// Query
				.setQuery(QueryBuilders.matchQuery("last_name", "Fir"))
//		        .setQuery(QueryBuilders.termQuery("multi", "test"))                 
		        .setPostFilter(QueryBuilders.rangeQuery("age").from(12).to(35))     // Filter
		        .setFrom(0).setSize(60)//分页
		        .setExplain(true)// 设置是否按查询匹配度排序
		        .get();
//		SearchRequestBuilder requestBuilder = client.prepareSearch(index).setTypes(type);
		
		SearchHits hits = response.getHits();
        SearchHit[] hitsArr = hits.getHits();
        for(SearchHit hit : hitsArr){
        	System.out.print("id: "+hit.getId());
        	System.out.println(hit.getSourceAsString());
        }
		return response;
	}
    
    //条件查询及分页2
    public void elasticSearch2(TransportClient client){
        //时间范围的设定
        RangeQueryBuilder rangequerybuilder = QueryBuilders
                    .rangeQuery("inputtime")
                    .from("2016-7-21 00:00:01").to("2016-7-21 00:00:03");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(rangequerybuilder);

        //System.out.println(sourceBuilder.toString());

        //查询建立
        SearchRequestBuilder responsebuilder = client
                                .prepareSearch("pointdata")
                                .setTypes("pointdata");
        SearchResponse myresponse=responsebuilder
                    .setQuery(QueryBuilders.boolQuery() 
                    .must(QueryBuilders.matchPhraseQuery //must表示and 、should标识or、must_not标识 not                
                        ("pointid","W3.UNIT1.10LBG01CP301")
                    )
                    .must(rangequerybuilder))
                    .setFrom(0).setSize(50) //分页
                    .setExplain(true)// 设置是否按查询匹配度排序
                    .execute()
                    .actionGet();
        SearchHits hits = myresponse.getHits();
        for(int i = 0; i < hits.getHits().length; i++) {
            System.out.println(hits.getHits()[i].getSourceAsString());
        }
    }
    
    
    //根据聚合类查询
    public void searchAggregation(TransportClient client){
        AggregationBuilder aggregation = AggregationBuilders
            .dateRange("agg")
            .field("inputtime")
            .format("yyyy-MM-dd HH:mm:ss")
            // from 1950 to 1960 (excluded)
            .addRange("2016-7-21 00:00:01", "2016-7-21 00:00:02");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.aggregation(aggregation);

        System.out.println(sourceBuilder.toString());

        SearchResponse response =  client
                    .prepareSearch("pointdata")
                    .setTypes("pointdata")
                    .setQuery(QueryBuilders.queryStringQuery
                        ("W3.UNIT1.10LBG01CP301"))
                    .setSource(sourceBuilder)
                    .execute()
                    .actionGet();

        SearchHits hits = response.getHits();
        for(int i = 0; i < hits.getHits().length; i++) {
            System.out.println(i+":"+hits.getHits()[i].getSourceAsString());
        }

        // sr is here your SearchResponse object
        Range agg = response.getAggregations().get("agg");

        // For each entry
        for (Range.Bucket entry : agg.getBuckets()) {
             // Date range as key
             String key = entry.getKeyAsString(); 
             // Date bucket from as a Date  引用import org.joda.time.DateTime;            
             DateTime fromAsDate = (DateTime) entry.getFrom(); 
             // Date bucket to as a Date  
             DateTime toAsDate = (DateTime) entry.getTo(); 
             // Doc count      
             long docCount = entry.getDocCount();                

             System.out.println("key [{"+key+"}], from [{"+fromAsDate+"}], "+
                 "to [{"+toAsDate+"}], doc_count [{"+docCount+"}]");
            }
    }
    
    /*
     * 进行大数据量查询时，往往因为设备、网络传输问题影响查询数据的效率；Elasticsearch中提供了Scroll（游标）的方式对数据进行少量多批次的滚动查询，来提高查询效率
     */
    //滚动查询
    public void searchScroll(TransportClient client) {

        try{
            long startTime = System.currentTimeMillis();

            List<String> result = new ArrayList<>();

            String scrollId = "";

            //第一次请求
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();


            //TODO: 设置查询条件
            RangeQueryBuilder rangequerybuilder = QueryBuilders
                .rangeQuery("inputtime")
                .from("2016-12-14 02:00:00").to("2016-12-14 07:59:59");
            sourceBuilder.query(QueryBuilders.boolQuery()
                .must(QueryBuilders
                        .matchPhraseQuery("pointid","W3.UNIT1.10HFC01CT013"))
                    .must(rangequerybuilder))
                    .size(100)//如果开启游标，则滚动获取
                    .sort("inputtime", SortOrder.ASC);
            //查询
            SearchRequest request = Requests.searchRequest("pointdata");
                request.scroll("2m");
                request.source(sourceBuilder);
            SearchResponse response = client.search(request).actionGet();
            //TODO:处理数据
            SearchHits hits = response.getHits();
            for(int i = 0; i < hits.getHits().length; i++) {
                //System.out.println(hits.getHits()[i].getSourceAsString());
                result.add(hits.getHits()[i].getSourceAsString());
            }
            //记录滚动ID
            scrollId = response.getScrollId();


            while(true){
                //后续的请求
                //scrollId = query.getScollId();
                SearchScrollRequestBuilder searchScrollRequestBuilder = client
                    .prepareSearchScroll(scrollId);            
                // 重新设定滚动时间            
                //TimeValue timeValue = new TimeValue(30000);
                searchScrollRequestBuilder.setScroll("2m");
                // 请求            
                SearchResponse response1 = searchScrollRequestBuilder.get();

                //TODO:处理数据
                SearchHits hits2 = response1.getHits();
                if(hits2.getHits().length == 0){
                    break;
                }
                for(int i = 0; i < hits2.getHits().length; i++) {
                    result.add(hits2.getHits()[i].getSourceAsString());
                }
                //下一批处理
                scrollId = response1.getScrollId();
            }

            System.out.println(result.size());
            long endTime = System.currentTimeMillis();
            System.out.println("Java程序运行时间：" + (endTime - startTime) + "ms");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    
    //数据批量上传
    public static void WriteBulkRequest(TransportClient client) {
        String globalname = "";
        String valuep = "";
        String time = "";
        //数据条数记步
        int count = 0;
        //数据上传条数设定
        int BULK_SIZE = 100;
        try{
            
            for(int i = 0; i<10; i++){
                BulkRequestBuilder bulkRequest = client.prepareBulk();
                bulkRequest.setRefreshPolicy(RefreshPolicy.IMMEDIATE);
                XContentBuilder builder=XContentFactory.jsonBuilder()
                                    .startObject();
                //TODO: 设置属性和值
                builder.field("pointid",globalname);
                builder.field("pointvalue",valuep);
                builder.field("inputtime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time));
                builder.endObject();
                //添加索引数据        
                IndexRequestBuilder requestBuilder = client
                        .prepareIndex("pointdata", "pointdata")
                        .setSource(builder);
                bulkRequest.add(requestBuilder);
                count++;
                if(count % BULK_SIZE == 0){
                    BulkResponse bulkResponse = bulkRequest
                            .execute().actionGet();
                    if (bulkResponse.hasFailures()) {
//                      LOGGER.error("导入索引数据失败: "+"pointdata");
//                      LOGGER.error("导入索引数据失败: "+bulkResponse
//                          .buildFailureMessage());
                        System.out.println("导入索引数据失败");
                    }

                    bulkRequest = client.prepareBulk();
                    bulkRequest.setRefreshPolicy(RefreshPolicy.IMMEDIATE);
                    count = 0;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }


    }

  
}
