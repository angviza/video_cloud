package com.hdvon.client.es.search.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hdvon.client.config.redis.BaseRedisDao;
import com.hdvon.client.es.IndexField;
import com.hdvon.client.service.CameraHelper;
import com.hdvon.client.service.TreeNodeService;
import com.hdvon.nmp.common.SystemConstant;

/**
 * es服务基类
 * @author wanshaojian
 *
 * @param <T>
 */
public abstract class BaseSearchServiceImpl<T> {

	protected static final Logger log = LoggerFactory.getLogger(BaseSearchServiceImpl.class);
	
    @Autowired
    protected TransportClient esClient;
    
    @Autowired
    protected ObjectMapper objectMapper;
    
    @Resource
	BaseRedisDao redisDao;
    
    @Resource
	TreeNodeService treeNodeService;
    
    
    public abstract void setTreeName(List<T> list);


	
	/**
     * 索引跟新
     * @param index 索引名称
     * @param mapping 索引type
     * @param boolQuery
     * @param list list集合
     * @param ids list的主键集合
     * @return
     */
    public void updateIndex(String index,String mapping,List<T> list) {
       
    	//然后在更新索引
        list.stream().forEach(t->{
        	String id = CameraHelper.getFieldValueByFieldName(IndexField.ID, t);
        	addOrUpdate(id, t, index, mapping);
        });
    }
    
    
	/**
     * 索引跟新
     * @param id 索引主键id
     * @param indexTemplate
     * @param index 索引名称
     * @param mapping 索引type
     * @param boolQuery
     * @return
     */
    public boolean addOrUpdate(String id,T indexTemplate,String index,String mapping) {
    	//然后在更新索引
    	boolean success = create(indexTemplate,index,mapping);
//        SearchRequestBuilder builder = this.esClient.prepareSearch(index).setTypes(mapping)
//        		.setQuery(QueryBuilders.termQuery(IndexField.ID,id));
//        if(log.isDebugEnabled()) {
//        	log.debug(builder.toString());
//        }
//        SearchResponse response = builder.execute().actionGet();
//        long totalHits = response.getHits().totalHits;
//        boolean success = false;
//        if(totalHits == 0){
//            success = create(indexTemplate,index,mapping);
//        }else if(totalHits == 1){
//            success = update(id, indexTemplate,index,mapping);
//        }
//        if(success){
//        	log.debug("Index success this camera " + id);
//        }
        return success;
    }

	/**
	 * 新增索引
	 * @param indexTemplate
	 * @param index
	 * @param mapping
	 * @return
	 */
    public boolean create(T indexTemplate, String index, String mapping){
        try {
        	System.out.println(objectMapper.writeValueAsBytes(indexTemplate));
        	String id = CameraHelper.getFieldValueByFieldName(IndexField.ID, indexTemplate);
            IndexResponse response = this.esClient.prepareIndex(index, mapping,id)
                    .setSource(objectMapper.writeValueAsBytes(indexTemplate), XContentType.JSON).get();
            log.debug("Create index with camear: {}" + JSON.toJSONString(indexTemplate));
            return response.status() == RestStatus.CREATED;
        } catch (Exception e) {
            log.error("Error to Index camear: {}" + JSON.toJSONString(indexTemplate), e);
            return false;
        }
    }
	/**
	 * 修改索引
	 * @param esId
	 * @param indexTemplate
	 * @param index
	 * @param mapping
	 * @return
	 */
	private boolean update(String esId, T indexTemplate, String index, String mapping) {
        try {
            UpdateResponse response = this.esClient.prepareUpdate(index, mapping, esId)
                    .setDoc(objectMapper.writeValueAsBytes(indexTemplate), XContentType.JSON).get();
            log.debug("Update index with camear: {}" ,JSON.toJSONString(indexTemplate));
            return response.status() == RestStatus.OK;
        } catch (Exception e) {
            log.error("Error to index camear {}", JSON.toJSONString(indexTemplate), e);
            return false;
        }
    }
	/**
	 * 删除索引
	 * @param index 索引名称
	 * @param mapping 索引type
	 * @param delList 删除id集合
	 */
	public void delete(String index, String mapping,BoolQueryBuilder delQuery) {

		if(delQuery!=null) {
			SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(index);
			searchRequestBuilder.setScroll(new TimeValue(1000));
			searchRequestBuilder.setSize(10000);
			searchRequestBuilder.setQuery(delQuery);
			searchRequestBuilder.setFetchSource(IndexField.ID, null);

			// 打印的内容 可以在 Elasticsearch head 和 Kibana 上执行查询
			log.info("\n{}", searchRequestBuilder);
			// 执行搜索,返回搜索响应信息
			SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
			long totalHits = searchResponse.getHits().totalHits;
			if(totalHits > 0) {
				//获取主键id
				BulkRequestBuilder bulkRequest = esClient.prepareBulk(); 
				boolean flag = false;
				for (SearchHit searchHit : searchResponse.getHits().getHits()) {
					flag = true;
					String id= searchHit.getId();
					bulkRequest.add(esClient.prepareDelete(index, mapping, id).request());
				}
				if(flag) {
					BulkResponse bulkResponse = bulkRequest.get();  
					if (bulkResponse.hasFailures()) {  
						for(BulkItemResponse item : bulkResponse.getItems()){  
							log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>{}", item.getFailureMessage());  
						}  
					} else {  
						log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>delete ok");  
					}
				}

			}
//			DeleteByQueryRequestBuilder builder  = DeleteByQueryAction.INSTANCE.newRequestBuilder(esClient).source(index)
//	                .filter(delQuery);
//	        BulkByScrollResponse response = builder.get();
//	        long deleted = response.getDeleted();
//	        log.debug("Delete total :" + deleted);
		}
	}


    /**
     * 删除索引
     * @param index 索引名称
     * @param mapping 索引type
     * @param boolQuery
     * @param ids
     */
	public void remove(String index, String mapping,BoolQueryBuilder boolQuery,List<String> ids)  {
		// TODO Auto-generated method stub
		if(boolQuery == null) {
			return;
		}

		SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(index);
		searchRequestBuilder.setScroll(new TimeValue(1000));
		searchRequestBuilder.setSize(10000);
		searchRequestBuilder.setQuery(boolQuery);
		searchRequestBuilder.setFetchSource(IndexField.ID, null);

		// 打印的内容 可以在 Elasticsearch head 和 Kibana 上执行查询
		log.info("\n{}", searchRequestBuilder);
		// 执行搜索,返回搜索响应信息
		SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
		
		long totalHits = searchResponse.getHits().totalHits;
		if(totalHits > 0) {
			//获取主键id
			BulkRequestBuilder bulkRequest = esClient.prepareBulk(); 
			boolean flag = false;
			for (SearchHit searchHit : searchResponse.getHits().getHits()) {	
				String id= searchHit.getId();
				if(!ids.contains(id)) {
					bulkRequest.add(esClient.prepareDelete(index, mapping, id).request());
					flag = true;
				}
			}
			if(flag) {
				try {
					BulkResponse bulkResponse = bulkRequest.get();  
					if (bulkResponse.hasFailures()) {  
						for(BulkItemResponse item : bulkResponse.getItems()){  
							log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>{}", item.getFailureMessage());  
						}  
					} else {  
						log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>delete ok");  
					}
				} catch (Exception e) {
					// TODO: handle exception
					log.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>删除索引数据失败：{}",e.getMessage());  
				}

			}

		}
	}

    /**
     * 删除索引
     * @param index 索引名称
     * @param mapping 索引type
     * @param boolQuery
     * @param ids
     */
	public List<String> search(String index, String mapping,BoolQueryBuilder boolQuery)  {
		// TODO Auto-generated method stub
		if(boolQuery == null) {
			return Collections.emptyList();
		}

		SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(index);
		searchRequestBuilder.setScroll(new TimeValue(1000));
		searchRequestBuilder.setSize(10000);
		searchRequestBuilder.setQuery(boolQuery);
		searchRequestBuilder.setFetchSource(IndexField.ID, null);

		// 打印的内容 可以在 Elasticsearch head 和 Kibana 上执行查询
		log.info("\n{}", searchRequestBuilder);
		// 执行搜索,返回搜索响应信息
		SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
		
		long totalHits = searchResponse.getHits().totalHits;
		if(totalHits == 0) {
			return Collections.emptyList();
		}
		List<String> idList = new ArrayList<String>();
		for (SearchHit searchHit : searchResponse.getHits().getHits()) {	
			String id= searchHit.getId();
			idList.add(id);
		}
		return idList;
	}
    
    /**
     * 判断redids树节点信息是否存在
     */
   public void treeKeyExists() {
	   String addressKey=SystemConstant.TREENODE_ADDRESSNODE_KEY;
	   String orgKey=SystemConstant.TREENODE_ORGNODE_KEY;
	   String projectKey=SystemConstant.TREENODE_PROJECTNODE_KEY;
	   String groupKey=SystemConstant.TREENODE_GROUPNODE_KEY;
	   if(! redisDao.exists(addressKey)) {
		   treeNodeService.getAddressTree();
	   }
	   if(! redisDao.exists(orgKey)) {
		   treeNodeService.getOrgTree();
	   }
	   if(! redisDao.exists(projectKey)) {
		   treeNodeService.getPojectTree();
	   }
	   if(! redisDao.exists(groupKey)) {
		   treeNodeService.getGrroupTree();
	   }
	   
   }
    

}
