package com.dafy.elasticsearch.base;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.lucene.search.Query;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.ToXContent.Params;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryShardContext;

public class ElasticDAOImpl implements ElasticDAO{

	//根据ID获取数据
	@Override
	public GetResponse getById(TransportClient client, String index, String type, String id) {
		GetResponse response = client.prepareGet(index, type, id)
                .setOperationThreaded(false)
                .get();
		return response;
	}

	//根据type获取数据集合
	@Override
	public SearchResponse elasticGet(TransportClient client, String index, String type) {
		SearchResponse response = client.prepareSearch(index).setTypes(type).get();
		return response;
	}
	

	//添加Document
	@Override
	public String addIndex(TransportClient client, String index, String type, String id,
			XContentBuilder sourceBuilder) {
		IndexResponse response = null;
    	if(id!=null && !"".equals(id)){
				response = client.prepareIndex(index, type, id)
		        	.setSource(sourceBuilder)
		        	.get();

		}else{
				response = client.prepareIndex(index, type)
		    	.setSource(sourceBuilder)
		    	.get();
		}
		return response.status().toString();
	}

	//根据ID删除Document
	@Override
	public String delete(TransportClient client, String index, String type, String id) {
		DeleteResponse response = client.prepareDelete(index, type, id).get();
		return response.status().toString();
	}

	//根据ID修改Document
	@Override
	public String update(TransportClient client, String index, String type, String id,XContentBuilder sourceBuilder) {
		UpdateResponse updateResponse = null;
    	UpdateRequest updateRequest = new UpdateRequest();
    	updateRequest.index(index);
    	updateRequest.type(type);
    	updateRequest.id(id);
    	try {
			updateRequest.doc(sourceBuilder);
		
			updateResponse = client.update(updateRequest).get();
    	} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return updateResponse.status().toString();
	}

	//新增或修改Document
	@Override
	public String upsert(TransportClient client, String index, String type, String id,XContentBuilder sourceBuilder,XContentBuilder updateBuilder) {
		IndexRequest indexRequest;
    	UpdateResponse updateResponse = null;
		try {
			indexRequest = new IndexRequest(index, type, id)
			        .source(sourceBuilder);
		
			UpdateRequest updateRequest = new UpdateRequest(index, type, id)
    	        .doc(updateBuilder)
    	        .upsert(indexRequest);              
			updateResponse = client.update(updateRequest).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return updateResponse.status().toString();
	}

	//创建index结构
	@Override
	public boolean createIndex(TransportClient client, String index, XContentBuilder mapping) {
		CreateIndexRequestBuilder  cib=client.admin().indices().prepareCreate("megacorp");
		cib.addMapping("pointdata", mapping);   
		CreateIndexResponse indexResponse = cib.execute().actionGet();
		return indexResponse.isAcknowledged();
	}

}
