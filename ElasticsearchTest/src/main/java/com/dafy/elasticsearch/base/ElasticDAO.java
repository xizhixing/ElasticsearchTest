package com.dafy.elasticsearch.base;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;

public interface ElasticDAO {

	//根据ID获取数据
	public GetResponse getById(TransportClient client,String index,String type,String id);
	
	//根据type获取数据集合
	public SearchResponse elasticGet(TransportClient client,String index,String type);
	
	//创建index结构
	public boolean createIndex(TransportClient client,String index,XContentBuilder mapping);
	
	//添加Document
    public String addIndex(TransportClient client,String index,String type,String id,XContentBuilder sourceBuilder);
    
    //根据ID删除Document
    public String delete(TransportClient client,String index,String type,String id);
    
    //根据ID修改Document
    public String update(TransportClient client,String index,String type,String id,XContentBuilder sourceBuilder);
    
    //新建或修改Document
    public String upsert(TransportClient client,String index,String type,String id,XContentBuilder sourceBuilder,XContentBuilder updateBuilder);
}
