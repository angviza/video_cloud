package com.hdvon.client.es;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkProcessor.Listener;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.AbstractQueryBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hdvon.client.exception.EsException;
import com.hdvon.client.vo.EsPage;
import com.hdvon.nmp.common.SystemConstant;
/**
 * es公共操作类
 * @author wanshaojian
 *
 */
@Component
public class ElasticsearchUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(ElasticsearchUtils.class);

	@Autowired
	private TransportClient transportClient;

	private static TransportClient client;

	@PostConstruct
	public void init() {
		client = this.transportClient;
	}
	/**
	 * 创建索引以及设置其内容
	 * @param index
	 * @param indexType
	 */
	public static void createIndex(String index,String indexType) throws EsException{
		try {
			String fileName ="";
			if(index.equals(SystemConstant.es_user_cameragroup_index)) {
				fileName = SystemConstant.user_cameragroup_mapping_file;
			}else if(index.equals(SystemConstant.es_cameragroup_index)) {
				fileName = SystemConstant.cameragroup_mapping_file;
			}else if(index.equals(SystemConstant.es_cameraPermission_index)) {
				fileName = SystemConstant.cameraPermission_mapping_file;
			}else {
				fileName = SystemConstant.camera_mapping_file;
			}
			StringBuffer strBuf = new StringBuffer();
			//解析json配置
			ClassPathResource resource = new ClassPathResource("es/"+fileName);
			InputStream inputStream = resource.getInputStream();
			
			int len = 0;
			byte[] buf = new byte[1024];
			while((len=inputStream.read(buf)) != -1) {
				strBuf.append(new String(buf, 0, len, "utf-8"));
			}
			inputStream.close();
			//创建索引
			createIndex(index);
			//设置索引元素
			putMapping(index, indexType, strBuf.toString());
		} catch (Exception e) {
			throw new EsException(e.getMessage());
		}

	}
	/**
	 * 创建索引
	 *
	 * @param index 索引名称
	 * @param indexType 索引类型
	 * @param mappingjson 创建的mapping结构
	 * @return
	 */
	public static boolean createIndex(String index) throws EsException{
		
		try {
			if (isIndexExist(index)) {
				//索引库存在则删除索引
				deleteIndex(index);
			}
			CreateIndexResponse indexresponse = client.admin().indices().prepareCreate(index).setSettings(Settings.builder().put("index.number_of_shards", 5)
	                .put("index.number_of_replicas", 1)
	        )
	        .get();               
			LOGGER.info("创建索引 {} 执行状态 {}", index , indexresponse.isAcknowledged());

			return indexresponse.isAcknowledged();
		}catch (Exception e) {
			throw new EsException(e.getMessage());
		}

	}

	/**
	 * 创建索引
	 *
	 * @param index 索引名称
	 * @param indexType 索引类型
	 * @param mappingjson 创建的mapping结构
	 * @return
	 */
	public static boolean putMapping(String index,String indexType,String mapping) throws EsException {
		if (!isIndexExist(index)) {
			throw new EsException("创建索引库"+index+"mapping"+mapping+"结构失败,索引库不存在!");
		}        
		try {
			PutMappingResponse indexresponse = client.admin().indices().preparePutMapping(index).setType(indexType).setSource(mapping, XContentType.JSON).get();  
		        
			LOGGER.info("索引 {} 设置 mapping {} 执行状态 {}", index ,indexType, indexresponse.isAcknowledged());

			return indexresponse.isAcknowledged();
		}catch (Exception e) {
			throw new EsException(e.getMessage());
		}
      

	}
	/**
	 * 删除索引
	 *
	 * @param index
	 * @return
	 */
	public static boolean deleteIndex(String index) throws EsException{
		if (!isIndexExist(index)) {
			return true;		
		}
		try {
			DeleteIndexResponse dResponse = client.admin().indices().prepareDelete(index).execute().actionGet();
			if (dResponse.isAcknowledged()) {
				LOGGER.info("delete index " + index + "  successfully!");
			} else {
				LOGGER.info("Fail to delete index " + index);
			}
			return dResponse.isAcknowledged();
		} catch (Exception e) {
			
			throw new EsException(e.getMessage());
		}
	}

	/**
	 * 判断索引是否存在
	 *
	 * @param index
	 * @return
	 */
	public static boolean isIndexExist(String index) {
		IndicesExistsResponse inExistsResponse = client.admin().indices().exists(new IndicesExistsRequest(index))
				.actionGet();
		return inExistsResponse.isExists();
	}

	/**
	 * 数据添加，正定ID
	 *
	 * @param jsonObject
	 *            要增加的数据
	 * @param index
	 *            索引，类似数据库
	 * @param type
	 *            类型，类似表
	 * @param id
	 *            数据ID
	 * @return
	 */
	public static String addData(JSONObject jsonObject, String index, String type, String id)throws EsException {
		try {
			IndexResponse response = client.prepareIndex(index, type, id).setSource(jsonObject).get();

			LOGGER.info("addData response status:{},id:{}", response.status().getStatus(), response.getId());

			return response.getId();
		} catch (Exception e) {
			
			throw new EsException(e.getMessage());
		}

	}


	/**
	 * 数据添加
	 *
	 * @param jsonObject
	 *            要增加的数据
	 * @param index
	 *            索引，类似数据库
	 * @param type
	 *            类型，类似表
	 * @return
	 */
	public static String addData(JSONObject jsonObject, String index, String type) {
		return addData(jsonObject, index, type, UUID.randomUUID().toString().replaceAll("-", "").toUpperCase());
	}

	/**
	 * 批量数据添加，
	 *
	 * @param list
	 *            要增加的数据
	 * @param pkName
	 *            主键id
	 * @param index
	 *            索引，类似数据库
	 * @param type
	 *            类型，类似表
	 * @param id
	 *            数据ID
	 * @return
	 */
	public static <T> void addBatchData(List<T> list, String pkName, String index, String type) {
		if(list == null || list.isEmpty()) {
			return;
		}
		// 创建BulkPorcessor对象
		BulkProcessor bulkProcessor = BulkProcessor.builder(client, new Listener() {
		    @Override
			public void beforeBulk(long paramLong, BulkRequest paramBulkRequest) {
				// TODO Auto-generated method stub
			}

			// 执行出错时执行
		    @Override
			public void afterBulk(long paramLong, BulkRequest paramBulkRequest, Throwable paramThrowable) {
				// TODO Auto-generated method stub
			}
		    @Override
			public void afterBulk(long paramLong, BulkRequest paramBulkRequest, BulkResponse paramBulkResponse) {
				// TODO Auto-generated method stub
			}
		})
				// 1w次请求执行一次bulk
				.setBulkActions(1000)
				// 1gb的数据刷新一次bulk
				// .setBulkSize(new ByteSizeValue(1, ByteSizeUnit.GB))
				// 固定5s必须刷新一次
				.setFlushInterval(TimeValue.timeValueSeconds(5))
				// 并发请求数量, 0不并发, 1并发允许执行
				.setConcurrentRequests(1)
				// 设置退避, 100ms后执行, 最大请求3次
				.setBackoffPolicy(BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3)).build();

		for (T vo : list) {
			if(getPkValueByName(vo, pkName)!= null) {
				String id = getPkValueByName(vo, pkName).toString();
				bulkProcessor.add(new IndexRequest(index, type, id).source(JSON.toJSONString(vo), XContentType.JSON));
			}

		}
		bulkProcessor.close();
	}

	/**
	 * 根据主键名称获取实体类主键属性值
	 * 
	 * @param clazz
	 * @param pkName
	 * @return
	 */
	private static Object getPkValueByName(Object clazz, String pkName) {
		try {
			String firstLetter = pkName.substring(0, 1).toUpperCase();
			String getter = "get" + firstLetter + pkName.substring(1);
			Method method = clazz.getClass().getMethod(getter, new Class[] {});
			Object value = method.invoke(clazz, new Object[] {});
			return value;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 通过ID删除数据
	 *
	 * @param index
	 *            索引，类似数据库
	 * @param type
	 *            类型，类似表
	 * @param id
	 *            数据ID
	 */
	public static void deleteDataById(String index, String type, String id) throws EsException{

		try{
			DeleteResponse response = client.prepareDelete(index, type, id).execute().actionGet();

			LOGGER.info("deleteDataById response status:{},id:{}", response.status().getStatus(), response.getId());
		} catch (Exception e) {
			
			throw new EsException(e.getMessage());
		}
	}

	/**
	 * 批量数据添加，
	 *
	 * @param list
	 *            要增加的数据
	 * @param pkName
	 *            主键id
	 * @param index
	 *            索引，类似数据库
	 * @param type
	 *            类型，类似表
	 * @param id
	 *            数据ID
	 * @return
	 */
	public static void deleteBatchData(List<String> ids, String index, String type) {
		// 创建BulkPorcessor对象
		BulkProcessor bulkProcessor = BulkProcessor.builder(client, new Listener() {
		    @Override
			public void beforeBulk(long paramLong, BulkRequest paramBulkRequest) {
				// TODO Auto-generated method stub
			}

			// 执行出错时执行
		    @Override
			public void afterBulk(long paramLong, BulkRequest paramBulkRequest, Throwable paramThrowable) {
				// TODO Auto-generated method stub
			}
		    @Override
			public void afterBulk(long paramLong, BulkRequest paramBulkRequest, BulkResponse paramBulkResponse) {
				// TODO Auto-generated method stub
			}
		})
				// 1w次请求执行一次bulk
				.setBulkActions(1000)
				// 1gb的数据刷新一次bulk
				// .setBulkSize(new ByteSizeValue(1, ByteSizeUnit.GB))
				// 固定5s必须刷新一次
				.setFlushInterval(TimeValue.timeValueSeconds(5))
				// 并发请求数量, 0不并发, 1并发允许执行
				.setConcurrentRequests(1)
				// 设置退避, 100ms后执行, 最大请求3次
				.setBackoffPolicy(BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3)).build();

		for (String id : ids) {
			bulkProcessor.add(new DeleteRequest(index, type, id));
		}
		bulkProcessor.close();
	}

	/**
	 * 通过ID 更新数据
	 *
	 * @param jsonObject
	 *            要增加的数据
	 * @param index
	 *            索引，类似数据库
	 * @param type
	 *            类型，类似表
	 * @param id
	 *            数据ID
	 * @return
	 */
	public static void updateDataById(JSONObject jsonObject, String index, String type, String id) throws EsException {

		try{
			UpdateRequest updateRequest = new UpdateRequest();

			updateRequest.index(index).type(type).id(id).doc(jsonObject);

			client.update(updateRequest);
		} catch (Exception e) {
			throw new EsException(e.getMessage());
		}
	}

	/**
	 * 批量数据添加，
	 *
	 * @param list
	 *            要增加的数据
	 * @param pkName
	 *            主键id
	 * @param index
	 *            索引，类似数据库
	 * @param type
	 *            类型，类似表
	 * @param id
	 *            数据ID
	 * @return
	 */
	public static <T> void updateBatchData(List<T> list, String pkName, String index, String type) {
		// 创建BulkPorcessor对象
		BulkProcessor bulkProcessor = BulkProcessor.builder(client, new Listener() {
		    @Override
			public void beforeBulk(long paramLong, BulkRequest paramBulkRequest) {
				// TODO Auto-generated method stub
			}

			// 执行出错时执行
		    @Override
			public void afterBulk(long paramLong, BulkRequest paramBulkRequest, Throwable paramThrowable) {
				// TODO Auto-generated method stub
			}
		    @Override
			public void afterBulk(long paramLong, BulkRequest paramBulkRequest, BulkResponse paramBulkResponse) {
				// TODO Auto-generated method stub
			}
		})
				// 1w次请求执行一次bulk
				.setBulkActions(1000)
				// 1gb的数据刷新一次bulk
				// .setBulkSize(new ByteSizeValue(1, ByteSizeUnit.GB))
				// 固定5s必须刷新一次
				.setFlushInterval(TimeValue.timeValueSeconds(5))
				// 并发请求数量, 0不并发, 1并发允许执行
				.setConcurrentRequests(1)
				// 设置退避, 100ms后执行, 最大请求3次
				.setBackoffPolicy(BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3)).build();

		for (T vo : list) {
			String id = getPkValueByName(vo, pkName).toString();
			bulkProcessor.add(new UpdateRequest(index, type, id).doc(JSON.toJSONString(vo), XContentType.JSON));
		}
		bulkProcessor.close();
	}

	/**
	 * 通过ID获取数据
	 *
	 * @param index
	 *            索引，类似数据库
	 * @param type
	 *            类型，类似表
	 * @param id
	 *            数据ID
	 * @param fields
	 *            需要显示的字段，逗号分隔（缺省为全部字段）
	 * @return
	 */
	public static Map<String, Object> searchDataById(String index, String type, String id, String fields) {

		GetRequestBuilder getRequestBuilder = client.prepareGet(index, type, id);

		if (StringUtils.isNotEmpty(fields)) {
			getRequestBuilder.setFetchSource(fields.split(","), null);
		}

		GetResponse getResponse = getRequestBuilder.execute().actionGet();

		return getResponse.getSource();
	}

	/**
	 * 使用分词查询
	 *
	 * @param index
	 *            索引名称
	 * @param type
	 *            类型名称,可传入多个type逗号分隔
	 * @param clz
	 *            数据对应实体类
	 * @param fields
	 *            需要显示的字段，逗号分隔（缺省为全部字段）
	 * @param boolQuery
	 *            查询条件
	 * @return
	 */
	public static <T> List<T> searchListData(String index, String type, Class<T> clz, String fields,BoolQueryBuilder boolQuery) {
		return searchListData(index, type, clz, 0, fields, null,  null,boolQuery);
	}




	/**
	 * 使用分词查询
	 *
	 * @param index
	 *            索引名称
	 * @param type
	 *            类型名称,可传入多个type逗号分隔
	 * @param clz
	 *            数据对应实体类
	 * @param size
	 *            文档大小限制
	 * @param fields
	 *            需要显示的字段，逗号分隔（缺省为全部字段）
	 * @param sortField
	 *            排序字段
	 * @param highlightField
	 *            高亮字段
	 * @param boolQuery
	 *            查询条件
	 * @return
	 */
	public static <T> List<T> searchListData(String index, String type, Class<T> clz, 
			Integer size, String fields, String sortField, String highlightField,BoolQueryBuilder boolQuery) throws EsException{

		SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index);
		if (StringUtils.isNotEmpty(type)) {
			searchRequestBuilder.setTypes(type.split(","));
		}
		// 高亮（xxx=111,aaa=222）
		if (StringUtils.isNotEmpty(highlightField)) {
			HighlightBuilder highlightBuilder = new HighlightBuilder();
			// 设置高亮字段
			highlightBuilder.field(highlightField);
			searchRequestBuilder.highlighter(highlightBuilder);
		}		
		searchRequestBuilder.setQuery(boolQuery);
		if (StringUtils.isNotEmpty(fields)) {
			searchRequestBuilder.setFetchSource(fields.split(","), null);
		}
		searchRequestBuilder.setFetchSource(true);

		if (StringUtils.isNotEmpty(sortField)) {
			searchRequestBuilder.addSort(sortField, SortOrder.DESC);
		}
		if (size != null && size > 0) {
			searchRequestBuilder.setSize(size);
		}
		searchRequestBuilder.setScroll(new TimeValue(1000));
		searchRequestBuilder.setSize(10000);
		// 打印的内容 可以在 Elasticsearch head 和 Kibana 上执行查询
		LOGGER.info("\n{}", searchRequestBuilder);

		SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();

		long totalHits = searchResponse.getHits().totalHits;
		if(LOGGER.isDebugEnabled()) {
			long length = searchResponse.getHits().getHits().length;

			LOGGER.info("共查询到[{}]条数据,处理数据条数[{}]", totalHits, length);
		}


		if (searchResponse.status().getStatus() == SystemConstant.success_stauts) {
			// 解析对象
			return setSearchResponse(clz, searchResponse, highlightField);
		}

		return null;

	}

	/**
	 * 使用分词查询,并分页
	 *
	 * @param index
	 *            索引名称
	 * @param type
	 *            类型名称,可传入多个type逗号分隔
	 * @param clz
	 *            数据对应实体类
	 * @param currentPage
	 *            当前页
	 * @param pageSize
	 *            每页显示条数
	 * @param fields
	 *            需要显示的字段，逗号分隔（缺省为全部字段）
	 * @param sortField
	 *            排序字段
	 * @param matchPhrase
	 *            true 使用，短语精准匹配
	 * @param highlightField
	 *            高亮字段
	 * @param boolQuery
	 *            查询条件
	 * @return
	 */
	public static <T> EsPage<T> searchDataPage(String index, String type, Class<T> clz, int currentPage, int pageSize,
			String fields, String sortField, boolean matchPhrase, String highlightField, BoolQueryBuilder boolQuery) throws EsException{
		SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index);
		if (StringUtils.isNotEmpty(type)) {
			searchRequestBuilder.setTypes(type.split(","));
		}
		searchRequestBuilder.setSearchType(SearchType.QUERY_THEN_FETCH);

		// 需要显示的字段，逗号分隔（缺省为全部字段）
		if (StringUtils.isNotEmpty(fields)) {
			searchRequestBuilder.setFetchSource(fields.split(","), null);
		}

		// 排序字段
		if (StringUtils.isNotEmpty(sortField)) {
			searchRequestBuilder.addSort(sortField, SortOrder.DESC);
		}
		// 高亮
		if (StringUtils.isNotEmpty(highlightField)) {
			HighlightBuilder highlightBuilder = new HighlightBuilder();

			// 设置高亮字段
			highlightBuilder.field(highlightField);
			searchRequestBuilder.highlighter(highlightBuilder);
		}

//		searchRequestBuilder.setQuery(QueryBuilders.matchAllQuery());
		searchRequestBuilder.setQuery(boolQuery);

		// 分页应用
		int firstSize = (currentPage - 1) * pageSize;
		searchRequestBuilder.setFrom(firstSize).setSize(pageSize);

		// 设置是否按查询匹配度排序
		searchRequestBuilder.setExplain(true);

		// 打印的内容 可以在 Elasticsearch head 和 Kibana 上执行查询
		LOGGER.info("\n{}", searchRequestBuilder);

		// 执行搜索,返回搜索响应信息
		SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();

		long totalHits = searchResponse.getHits().totalHits;
		if(LOGGER.isDebugEnabled()) {
			long length = searchResponse.getHits().getHits().length;

			LOGGER.info("共查询到[{}]条数据,处理数据条数[{}]", totalHits, length);
		}

		if (searchResponse.status().getStatus() == SystemConstant.success_stauts) {
			// 解析对象
			List<T> sourceList = setSearchResponse(clz, searchResponse, highlightField);

			return new EsPage<T>(currentPage, pageSize, (int) totalHits, sourceList);
		}
		return null;
	}

	/**
	 * 使用分词查询,并分页
	 *
	 * @param index
	 *            索引名称
	 * @param type
	 *            类型名称,可传入多个type逗号分隔
	 * @param clz
	 *            数据对应实体类
	 * @param currentPage
	 *            当前页
	 * @param pageSize
	 *            每页显示条数
	 * @param fields
	 *            需要显示的字段，逗号分隔（缺省为全部字段）
	 * @param sortField
	 *            排序字段
	 * @param highlightField
	 *            高亮字段
	 * @param boolQuery
	 *            查询条件
	 * @return
	 */
	public static <T> EsPage<T> searchDataPage(String index, String type, Class<T> clz, int currentPage, int pageSize,
			String fields, String sortField, String highlightField, BoolQueryBuilder boolQuery) throws EsException{
		SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index);
		if (StringUtils.isNotEmpty(type)) {
			searchRequestBuilder.setTypes(type.split(","));
		}
		searchRequestBuilder.setSearchType(SearchType.QUERY_THEN_FETCH);

		// 需要显示的字段，逗号分隔（缺省为全部字段）
		if (StringUtils.isNotEmpty(fields)) {
			searchRequestBuilder.setFetchSource(fields.split(","), null);
		}

		// 排序字段
		if (StringUtils.isNotEmpty(sortField)) {
			searchRequestBuilder.addSort(sortField, SortOrder.DESC);
		}
		// 高亮（xxx=111,aaa=222）
		if (StringUtils.isNotEmpty(highlightField)) {
			HighlightBuilder highlightBuilder = new HighlightBuilder();
			// 设置高亮字段
			highlightBuilder.field(highlightField);
			searchRequestBuilder.highlighter(highlightBuilder);
		}
		searchRequestBuilder.setQuery(boolQuery);

		// 分页应用
		int firstSize = (currentPage - 1) * pageSize;
		searchRequestBuilder.setFrom(firstSize).setSize(pageSize);

		// 设置是否按查询匹配度排序
		searchRequestBuilder.setExplain(true);

		// 打印的内容 可以在 Elasticsearch head 和 Kibana 上执行查询
		LOGGER.info("\n{}", searchRequestBuilder);

		// 执行搜索,返回搜索响应信息
		SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();

		long totalHits = searchResponse.getHits().totalHits;
		if(LOGGER.isDebugEnabled()) {
			long length = searchResponse.getHits().getHits().length;

			LOGGER.info("共查询到[{}]条数据,处理数据条数[{}]", totalHits, length);
		}

		if (searchResponse.status().getStatus() == SystemConstant.success_stauts) {
			// 解析对象
			List<T> sourceList = setSearchResponse(clz, searchResponse, highlightField);

			return new EsPage<T>(currentPage, pageSize, (int) totalHits, sourceList);
		}
		return null;
	}
	/**
	 * 使用分词查询,并分页
	 *
	 * @param index
	 *            索引名称
	 * @param type
	 *            类型名称,可传入多个type逗号分隔
	 * @param clz
	 *            数据对应实体类
	 * @param currentPage
	 *            当前页
	 * @param pageSize
	 *            每页显示条数
	 * @param fields
	 *            需要显示的字段，逗号分隔（缺省为全部字段）
	 * @param avgfields 
	 *            聚合字段           
	 * @param sortField
	 *            排序字段
	 * @param highlightField
	 *            高亮字段
	 * @param boolQuery
	 *            查询条件
	 * @return
	 */
	public static SearchResponse searchAvgDataPage(String index, String type, String fields, String sortField,String avgfields,
			String highlightField, BoolQueryBuilder boolQuery) throws EsException{
		
		SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index);
		
		if(StringUtils.isNotEmpty(avgfields)) {
			AggregationBuilder agg = AggregationBuilders.terms("terms").field(avgfields).size(5000);
			searchRequestBuilder.addAggregation(agg);
		}
		
		if (StringUtils.isNotEmpty(type)) {
			searchRequestBuilder.setTypes(type.split(","));
		}
		// 高亮（xxx=111,aaa=222）
		if (StringUtils.isNotEmpty(highlightField)) {
			HighlightBuilder highlightBuilder = new HighlightBuilder();
			// 设置高亮字段
			highlightBuilder.field(highlightField);
			searchRequestBuilder.highlighter(highlightBuilder);
		}

		if (StringUtils.isNotEmpty(fields)) {
			searchRequestBuilder.setFetchSource(fields.split(","), null);
		}

		if (StringUtils.isNotEmpty(sortField)) {
			searchRequestBuilder.addSort(sortField, SortOrder.DESC);
		}


		// 需要显示的字段，逗号分隔（缺省为全部字段）
		if (StringUtils.isNotEmpty(fields)) {
			searchRequestBuilder.setFetchSource(fields.split(","), null);
		}
		searchRequestBuilder.setScroll(new TimeValue(1000));
		searchRequestBuilder.setSize(10000);
		searchRequestBuilder.setQuery(boolQuery);

		// 设置是否按查询匹配度排序
		searchRequestBuilder.setExplain(true);

		// 打印的内容 可以在 Elasticsearch head 和 Kibana 上执行查询
		LOGGER.info("\n{}", searchRequestBuilder);

		// 执行搜索,返回搜索响应信息
		SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
		
		return searchResponse;
	}
	/**
	 * 高亮结果集 特殊处理
	 * 
	 * @param clz
	 *            数据对应实体类
	 * @param searchResponse
	 * 
	 * @param highlightField
	 *            高亮字段
	 */
	public static <T> List<T> setSearchResponse(Class<T> clz, SearchResponse searchResponse, String highlightField) {
		List<T> sourceList = new ArrayList<T>();
		SearchHit[] hits = searchResponse.getHits().getHits();
		for (SearchHit searchHit : hits) {		
			searchHit.getSourceAsMap().put(IndexField.ID, searchHit.getId());
			StringBuffer stringBuffer = new StringBuffer();
			if (StringUtils.isNotEmpty(highlightField)) {

				// System.out.println("遍历 高亮结果集，覆盖 正常结果集" + searchHit.getSourceAsMap());
				HighlightField highlight = searchHit.getHighlightFields().get(highlightField);

				if(highlight != null) {//不过滤非高亮
					Text[] text = highlight.getFragments();
					if (text != null) {
						for (Text str : text) {
							stringBuffer.append(str.string());
						}
						// 遍历 高亮结果集，覆盖 正常结果集
						searchHit.getSourceAsMap().put(highlightField, stringBuffer.toString());
					}
				}
			}

			T t = JSON.parseObject(JSON.toJSONString(searchHit.getSourceAsMap()), clz);
			sourceList.add(t);
		}

		return sourceList;
	}

	/**
	 * 查询附近的摄像机信息
	 * 
	 * @param index
	 *            索引名称
	 * @param type
	 *            类型
	 * @param clz
	 *            数据对应实体类
	 * @param lat
	 *            纬度
	 * @param lon
	 *            经度
	 * @param distance
	 *            距离 千米
	 * @param highlightField
	 *            高亮字段
	 * @param boolQuery
	 *            查询条件
	 * @return
	 */
	public static <T> EsPage<T> geoDistanceList(String index, String type, Class<T> clz, int currentPage, int pageSize, double lat, double lon,
			int distance,String highlightField, BoolQueryBuilder boolQuery) throws EsException{
		SearchRequestBuilder srb = client.prepareSearch(index).setTypes(type);
		srb.setQuery(boolQuery);

		srb.setPostFilter(AbstractEsFilter.geoDistanceFilter(lat, lon, distance));
		// 获取距离多少公里 这个才是获取点与点之间的距离的
		srb.addSort(AbstractEsFilter.geoDistanceSortBuilderFilter(lat, lon));

		// 分页应用
		int firstSize = (currentPage - 1) * pageSize;
		srb.setFrom(firstSize).setSize(pageSize);

		// 设置是否按查询匹配度排序
		srb.setExplain(true);
		// 高亮
		if (StringUtils.isNotEmpty(highlightField)) {
			HighlightBuilder highlightBuilder = new HighlightBuilder();

			// 设置高亮字段
			highlightBuilder.field(highlightField);
			srb.highlighter(highlightBuilder);
		}
		// 打印的内容 可以在 Elasticsearch head 和 Kibana 上执行查询
		LOGGER.info("\n{}", srb);

		// 执行搜索,返回搜索响应信息
		SearchResponse searchResponse = srb.execute().actionGet();

		long totalHits = searchResponse.getHits().totalHits;
		if(LOGGER.isDebugEnabled()) {
			long length = searchResponse.getHits().getHits().length;

			LOGGER.info("共查询到[{}]条数据,处理数据条数[{}]", totalHits, length);
		}

		if (searchResponse.status().getStatus() == SystemConstant.success_stauts) {
			// 解析对象
			List<T> sourceList = setSearchResponse(clz, searchResponse, highlightField);

			return new EsPage<T>(currentPage, pageSize, (int) totalHits, sourceList);
		}
		return null;
	}
	
	


	/**
	 * 查询附近的摄像机信息
	 * 
	 * @param index
	 *            索引名称
	 * @param type
	 *            类型
	 * @param clz
	 *            数据对应实体类
	 * @param filter
	 *            过滤对象
	 * @param highlightField
	 *            高亮字段
	 * @param boolQuery
	 *            查询条件
	 * @return
	 */
	public static <T> EsPage<T> geoSearchDataPage(String index, String type, Class<T> clz,int currentPage, int pageSize, AbstractQueryBuilder<?> filter,String highlightField,
			BoolQueryBuilder boolQuery) throws EsException{
		SearchRequestBuilder srb = client.prepareSearch(index).setTypes(type);
		srb.setQuery(boolQuery);

//		srb.setPostFilter(EsFilter.geoPolygonFilter(points));
		srb.setPostFilter(filter);

		// 分页应用
		int firstSize = (currentPage - 1) * pageSize;
		srb.setFrom(firstSize).setSize(pageSize);

		// 设置是否按查询匹配度排序
		srb.setExplain(true);
		// 高亮
		if (StringUtils.isNotEmpty(highlightField)) {
			HighlightBuilder highlightBuilder = new HighlightBuilder();

			// 设置高亮字段
			highlightBuilder.field(highlightField);
			srb.highlighter(highlightBuilder);
		}
		// 打印的内容 可以在 Elasticsearch head 和 Kibana 上执行查询
		LOGGER.info("\n{}", srb);

		// 执行搜索,返回搜索响应信息
		SearchResponse searchResponse = srb.execute().actionGet();

		long totalHits = searchResponse.getHits().totalHits;
		if(LOGGER.isDebugEnabled()) {
			long length = searchResponse.getHits().getHits().length;

			LOGGER.info("共查询到[{}]条数据,处理数据条数[{}]", totalHits, length);
		}

		if (searchResponse.status().getStatus() == SystemConstant.success_stauts) {
			// 解析对象
			List<T> sourceList = setSearchResponse(clz, searchResponse, highlightField);

			return new EsPage<T>(currentPage, pageSize, (int) totalHits, sourceList);
		}
		return null;

	}
}
