package com.toucan.shopping.modules.admin.auth.es.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.admin.auth.constant.AdminRoleCacheElasticSearchConstant;
import com.toucan.shopping.modules.admin.auth.es.service.AdminRoleElasticSearchService;
import com.toucan.shopping.modules.admin.auth.vo.AdminRoleElasticSearchVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.admin.indices.cache.clear.ClearIndicesCacheRequest;
import org.elasticsearch.action.admin.indices.cache.clear.ClearIndicesCacheResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.GetAliasesResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.cluster.metadata.AliasMetadata;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service("esAdminRoleService")
public class AdminRoleElasticSearchServiceImpl implements AdminRoleElasticSearchService {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public void save(AdminRoleElasticSearchVO esVO) throws Exception{
        IndexRequest request = new IndexRequest(AdminRoleCacheElasticSearchConstant.ADMIN_ROLE_INDEX).id(String.valueOf(esVO.getId())).source(JSONObject.toJSONString(esVO), XContentType.JSON);
        restHighLevelClient.index(request, RequestOptions.DEFAULT);

    }

    @Override
    public void update(AdminRoleElasticSearchVO esVO) throws Exception {
        UpdateRequest request = new UpdateRequest(AdminRoleCacheElasticSearchConstant.ADMIN_ROLE_INDEX,String.valueOf(esVO.getId()));
        XContentBuilder updateBody = XContentFactory.jsonBuilder().startObject();
        updateBody.field("id",esVO.getId());
        updateBody.field("adminId",esVO.getAdminId());
        updateBody.field("roleId",esVO.getRoleId());
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
                if(key.toLowerCase().equals(AdminRoleCacheElasticSearchConstant.ADMIN_ROLE_INDEX))
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
            CreateIndexRequest request = new CreateIndexRequest(AdminRoleCacheElasticSearchConstant.ADMIN_ROLE_INDEX);
            restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
    }


    @Override
    public List<AdminRoleElasticSearchVO> queryById(Long id) throws Exception{
        List<AdminRoleElasticSearchVO> AdminRoleElasticSearchVOS = new ArrayList<AdminRoleElasticSearchVO>();
        //创建请求对象
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(AdminRoleCacheElasticSearchConstant.ADMIN_ROLE_INDEX);
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
                AdminRoleElasticSearchVOS.add(JSONObject.parseObject(sourceString,AdminRoleElasticSearchVO.class));
            }
        }
        return AdminRoleElasticSearchVOS;
    }

    @Override
    public List<AdminRoleElasticSearchVO> queryByEntity(AdminRoleElasticSearchVO query) throws Exception {
        List<AdminRoleElasticSearchVO> AdminRoleElasticSearchVOS = new ArrayList<AdminRoleElasticSearchVO>();
        //创建请求对象
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(AdminRoleCacheElasticSearchConstant.ADMIN_ROLE_INDEX);
        //创建查询对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //设置查询条件
        if(query.getId()!=null) {
            searchSourceBuilder.query(QueryBuilders.termQuery("_id", query.getId()));
        }
        if(query.getAdminId()!=null)
        {
            searchSourceBuilder.query(QueryBuilders.termQuery("adminId", query.getAdminId()));
        }
        if(query.getRoleId()!=null)
        {
            searchSourceBuilder.query(QueryBuilders.termQuery("roleId", query.getRoleId()));
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
                AdminRoleElasticSearchVOS.add(JSONObject.parseObject(sourceString,AdminRoleElasticSearchVO.class));
            }
        }
        return AdminRoleElasticSearchVOS;
    }


    @Override
    public boolean deleteById(String id) throws Exception {
        //创建请求对象
        DeleteRequest deleteRequest = new DeleteRequest(AdminRoleCacheElasticSearchConstant.ADMIN_ROLE_INDEX);
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

    @Override
    public boolean deleteIndex() throws Exception {
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(AdminRoleCacheElasticSearchConstant.ADMIN_ROLE_INDEX);
        deleteIndexRequest.indicesOptions(IndicesOptions.LENIENT_EXPAND_OPEN);
        AcknowledgedResponse delete = restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
        return delete.isAcknowledged();
    }



    @Override
    public boolean deleteByAdminIdAndAppCodes(String adminId,String appCode,List<String> deleteFaildIdList) throws Exception {
        List<AdminRoleElasticSearchVO> AdminRoleElasticSearchVOS = new ArrayList<AdminRoleElasticSearchVO>();
        //创建请求对象
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(AdminRoleCacheElasticSearchConstant.ADMIN_ROLE_INDEX);
        //创建查询对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //设置查询条件
        searchSourceBuilder.query(QueryBuilders.termQuery("adminId", adminId));
        searchSourceBuilder.query(QueryBuilders.termQuery("appCode", appCode));
        //设置查询条件到请求对象中
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest,  RequestOptions.DEFAULT);
        SearchHits searchHits = searchResponse.getHits();
        SearchHit[] searchHitsHits = searchHits.getHits();
        for(SearchHit searchHit:searchHitsHits) {
            DeleteRequest deleteRequest = new DeleteRequest(AdminRoleCacheElasticSearchConstant.ADMIN_ROLE_INDEX);
            deleteRequest.id(searchHit.getId());
            DeleteResponse deleteResponse =restHighLevelClient.delete(deleteRequest,RequestOptions.DEFAULT);
            if(RestStatus.OK.getStatus() == deleteResponse.status().getStatus())
            {
                //强制刷新
                deleteResponse.forcedRefresh();
            }else{
                //保存删除失败ID
                deleteFaildIdList.add(searchHit.getId());
            }
        }
        //没有删除失败的ID
        if(CollectionUtils.isEmpty(deleteFaildIdList))
        {
            return true;
        }
        return false;
    }

    @Override
    public void saves(AdminRoleElasticSearchVO[] adminRoleElasticSearchVOS) throws Exception {

        for(AdminRoleElasticSearchVO adminRoleElasticSearchVO:adminRoleElasticSearchVOS)
        {
            save(adminRoleElasticSearchVO);
        }

    }




}
