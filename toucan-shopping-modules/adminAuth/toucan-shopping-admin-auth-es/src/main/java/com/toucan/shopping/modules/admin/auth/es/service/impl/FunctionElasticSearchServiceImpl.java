package com.toucan.shopping.modules.admin.auth.es.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.admin.auth.constant.FunctionCacheElasticSearchConstant;
import com.toucan.shopping.modules.admin.auth.constant.RoleFunctionCacheElasticSearchConstant;
import com.toucan.shopping.modules.admin.auth.es.service.FunctionElasticSearchService;
import com.toucan.shopping.modules.admin.auth.es.service.RoleFunctionElasticSearchService;
import com.toucan.shopping.modules.admin.auth.vo.FunctionElasticSearchVO;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.GetAliasesResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.cluster.metadata.AliasMetadata;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service("esFunctionService")
public class FunctionElasticSearchServiceImpl implements FunctionElasticSearchService {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public void save(FunctionElasticSearchVO esVO) throws Exception{
        IndexRequest request = new IndexRequest(FunctionCacheElasticSearchConstant.FUNCTION_INDEX).id(String.valueOf(esVO.getId())).source(JSONObject.toJSONString(esVO), XContentType.JSON);
        restHighLevelClient.index(request, RequestOptions.DEFAULT);

    }

    @Override
    public void update(FunctionElasticSearchVO esVO) throws Exception {
        UpdateRequest request = new UpdateRequest(FunctionCacheElasticSearchConstant.FUNCTION_INDEX,String.valueOf(esVO.getId()));
        XContentBuilder updateBody = XContentFactory.jsonBuilder().startObject();
        updateBody.field("id",esVO.getId());
        updateBody.field("functionId",esVO.getFunctionId());
        updateBody.field("name",esVO.getName());
        updateBody.field("url",esVO.getUrl());
        updateBody.field("permission",esVO.getPermission());
        updateBody.field("type",esVO.getType());
        updateBody.field("functionText",esVO.getFunctionText());
        updateBody.field("pid",esVO.getPid());
        updateBody.field("enableStatus",esVO.getEnableStatus());
        updateBody.field("icon",esVO.getIcon());
        updateBody.field("remark",esVO.getRemark());
        updateBody.field("functionSort",esVO.getFunctionSort());
        updateBody.field("appCode",esVO.getAppCode());
        updateBody.field("createAdminId",esVO.getCreateAdminId());
        updateBody.field("deleteStatus",esVO.getDeleteStatus());
        if(esVO.getCreateDate()!=null) {
            updateBody.field("createDate", esVO.getCreateDate().getTime());
        }
        updateBody.endObject();
        request.doc(updateBody);
        UpdateResponse updateResponse = restHighLevelClient.update(request, RequestOptions.DEFAULT);
        //强制刷新
        updateResponse.forcedRefresh();

    }

    @Override
    public boolean existsIndex() {
        try {
            GetAliasesRequest request = new GetAliasesRequest();
            GetAliasesResponse getAliasesResponse = restHighLevelClient.indices().getAlias(request, RequestOptions.DEFAULT);
            Map<String, Set<AliasMetadata>> map = getAliasesResponse.getAliases();
            Set<String> indices = map.keySet();
            for (String key : indices) {
                if(key.toLowerCase().equals(FunctionCacheElasticSearchConstant.FUNCTION_INDEX))
                {
                    return true;
                }
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return false;
    }

    @Override
    public void createIndex() {
        try {
            CreateIndexRequest request = new CreateIndexRequest(FunctionCacheElasticSearchConstant.FUNCTION_INDEX);
            restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
    }


    @Override
    public List<FunctionElasticSearchVO> queryById(Long id) throws Exception{
        List<FunctionElasticSearchVO> FunctionElasticSearchVOS = new ArrayList<FunctionElasticSearchVO>();
        //创建请求对象
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(FunctionCacheElasticSearchConstant.FUNCTION_INDEX);
        //创建查询对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(10);
        //设置查询条件
        searchSourceBuilder.query(QueryBuilders.termQuery("_id", id));
        //设置查询条件到请求对象中
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest,  RequestOptions.DEFAULT);
        SearchHits searchHits = searchResponse.getHits();
        SearchHit[] searchHitsHits = searchHits.getHits();
        for(SearchHit searchHit:searchHitsHits) {
            String sourceString = searchHit.getSourceAsString();
            if (StringUtils.isNotEmpty(sourceString)){
                logger.info("UserElasticSearchService queryById {}", sourceString);
                FunctionElasticSearchVOS.add(JSONObject.parseObject(sourceString,FunctionElasticSearchVO.class));
            }
        }
        return FunctionElasticSearchVOS;
    }

    @Override
    public List<FunctionElasticSearchVO> queryByEntity(FunctionElasticSearchVO query) throws Exception {
        List<FunctionElasticSearchVO> FunctionElasticSearchVOS = new ArrayList<FunctionElasticSearchVO>();
        //创建请求对象
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(FunctionCacheElasticSearchConstant.FUNCTION_INDEX);
        //创建查询对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //设置查询条件
        if(query.getId()!=null) {
            searchSourceBuilder.query(QueryBuilders.termQuery("_id", query.getId()));
        }
        if(query.getFunctionId()!=null) {
            searchSourceBuilder.query(QueryBuilders.termQuery("functionId", query.getFunctionId()));
        }
        if(query.getAppCode()!=null)
        {
            searchSourceBuilder.query(QueryBuilders.termQuery("appCode", query.getAppCode()));
        }
        //设置查询条件到请求对象中
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest,  RequestOptions.DEFAULT);
        SearchHits searchHits = searchResponse.getHits();
        SearchHit[] searchHitsHits = searchHits.getHits();
        for(SearchHit searchHit:searchHitsHits) {
            String sourceString = searchHit.getSourceAsString();
            if (StringUtils.isNotEmpty(sourceString)){
                logger.info("UserElasticSearchService queryById {}", sourceString);
                FunctionElasticSearchVOS.add(JSONObject.parseObject(sourceString,FunctionElasticSearchVO.class));
            }
        }
        return FunctionElasticSearchVOS;
    }


    @Override
    public boolean deleteById(String id) throws Exception {
        //创建请求对象
        DeleteRequest deleteRequest = new DeleteRequest(FunctionCacheElasticSearchConstant.FUNCTION_INDEX);
        deleteRequest.id(id);
        DeleteResponse deleteResponse =restHighLevelClient.delete(deleteRequest,RequestOptions.DEFAULT);
        if(RestStatus.OK.getStatus() == deleteResponse.status().getStatus())
        {
            //强制刷新
            deleteResponse.forcedRefresh();
            return true;
        }
        return false;
    }



}
