package com.toucan.shopping.modules.user.es.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.user.constant.UserCacheElasticSearchConstant;
import com.toucan.shopping.modules.user.vo.UserElasticSearchVO;
import com.toucan.shopping.modules.user.es.service.UserElasticSearchService;
import com.toucan.shopping.modules.user.es.vo.SearchAfterPage;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
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

@Service("esUserService")
public class UserElasticSearchServiceImpl implements UserElasticSearchService {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public void save(UserElasticSearchVO esUserVO) {
        try {
            IndexRequest request = new IndexRequest(UserCacheElasticSearchConstant.USER_INDEX).id(String.valueOf(esUserVO.getId())).source(JSONObject.toJSONString(esUserVO), XContentType.JSON);
            restHighLevelClient.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            logger.warn(e.getMessage(),e);
        }

    }

    @Override
    public void update(UserElasticSearchVO esUserVO) {
        try {
            UpdateRequest request = new UpdateRequest(UserCacheElasticSearchConstant.USER_INDEX,String.valueOf(esUserVO.getId()));
            XContentBuilder updateBody = XContentFactory.jsonBuilder().startObject();
            updateBody.field("nickName",esUserVO.getNickName());
            updateBody.endObject();
            request.doc(updateBody);
            restHighLevelClient.update(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            logger.warn(e.getMessage(),e);
        }

    }

    @Override
    public boolean existsIndex() {
        try {
            GetAliasesRequest request = new GetAliasesRequest();
            GetAliasesResponse getAliasesResponse = restHighLevelClient.indices().getAlias(request, RequestOptions.DEFAULT);
            Map<String, Set<AliasMetadata>> map = getAliasesResponse.getAliases();
            Set<String> indices = map.keySet();
            for (String key : indices) {
                if(key.toLowerCase().equals(UserCacheElasticSearchConstant.USER_INDEX))
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
            CreateIndexRequest request = new CreateIndexRequest(UserCacheElasticSearchConstant.USER_INDEX);
            restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
    }

    @Override
    public List<UserElasticSearchVO> queryListForFormSize(UserElasticSearchVO esUserVo,int size) throws Exception{
        List<UserElasticSearchVO> userElasticSearchVOS = new ArrayList<UserElasticSearchVO>();
        //创建请求对象
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(UserCacheElasticSearchConstant.USER_INDEX);
        searchRequest.types("_doc");
        //创建查询对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //设置查询条件
        if(StringUtils.isNotEmpty(esUserVo.getMobilePhone())) {
            searchSourceBuilder.query(QueryBuilders.termQuery("mobilePhone", esUserVo.getMobilePhone()));
        }
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(size);
        //设置查询条件到请求对象中
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest,  RequestOptions.DEFAULT);
        SearchHits searchHits = searchResponse.getHits();
        SearchHit[] searchHitsHits = searchHits.getHits();
        for(SearchHit searchHit:searchHitsHits) {
            String sourceString = searchHit.getSourceAsString();
            if (StringUtils.isNotEmpty(sourceString)){
                logger.info("UserElasticSearchService queryListForFormSize {}", sourceString);
                userElasticSearchVOS.add(JSONObject.parseObject(sourceString,UserElasticSearchVO.class));
            }
        }
        return userElasticSearchVOS;
    }

    @Override
    public List<UserElasticSearchVO> queryById(Long id) throws Exception{
        List<UserElasticSearchVO> userElasticSearchVOS = new ArrayList<UserElasticSearchVO>();
        //创建请求对象
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(UserCacheElasticSearchConstant.USER_INDEX);
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
                userElasticSearchVOS.add(JSONObject.parseObject(sourceString,UserElasticSearchVO.class));
            }
        }
        return userElasticSearchVOS;
    }


    @Override
    public List<UserElasticSearchVO> queryByUserMainId(Long userMainId) throws Exception{
        List<UserElasticSearchVO> userElasticSearchVOS = new ArrayList<UserElasticSearchVO>();
        //创建请求对象
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(UserCacheElasticSearchConstant.USER_INDEX);
        //创建查询对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(10);
        //设置查询条件
        searchSourceBuilder.query(QueryBuilders.termQuery("userMainId", userMainId));
        //设置查询条件到请求对象中
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest,  RequestOptions.DEFAULT);
        SearchHits searchHits = searchResponse.getHits();
        SearchHit[] searchHitsHits = searchHits.getHits();
        for(SearchHit searchHit:searchHitsHits) {
            String sourceString = searchHit.getSourceAsString();
            if (StringUtils.isNotEmpty(sourceString)){
                logger.info("UserElasticSearchService queryById {}", sourceString);
                userElasticSearchVOS.add(JSONObject.parseObject(sourceString,UserElasticSearchVO.class));
            }
        }
        return userElasticSearchVOS;
    }

    @Override
    public SearchAfterPage queryListForSearchAfter(UserElasticSearchVO esUserVo, int size, Object[] searchAfter) throws Exception {
        SearchAfterPage searchAfterPage = new SearchAfterPage();
        List<UserElasticSearchVO> userElasticSearchVOS = new ArrayList<UserElasticSearchVO>();
        searchAfterPage.setUserElasticSearchVOS(userElasticSearchVOS);

        //创建查询对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        searchSourceBuilder.size(size);
        //设置模糊查询条件
//        if(StringUtils.isNotEmpty(esUserVo.getKeyword())) {
//            searchSourceBuilder.query(QueryBuilders.multiMatchQuery(esUserVo.getKeyword(),
//                    "_id","mobilePhone","nickName","email","username","idCard"));
//        }
        //设置邮箱条件查询条件
        if(StringUtils.isNotEmpty(esUserVo.getEmail()))
        {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("email",esUserVo.getEmail()));
        }
        //设置手机号查询条件
        if(StringUtils.isNotEmpty(esUserVo.getMobilePhone()))
        {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("mobilePhone", esUserVo.getMobilePhone()));
        }

        //设置昵称查询条件
        if(StringUtils.isNotEmpty(esUserVo.getNickName()))
        {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("nickName", esUserVo.getNickName()));
        }

        //设置用户ID查询条件
        if(esUserVo.getId()!=null)
        {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("id", esUserVo.getId()));
        }

        //设置查询条件组
        searchSourceBuilder.query(boolQueryBuilder);

        //根据ID降序
        searchSourceBuilder.sort("_id", SortOrder.DESC);
        //设置after
        if(searchAfter!=null&&searchAfter.length>0&&StringUtils.isNotEmpty(String.valueOf(searchAfter[0]))) {
            searchSourceBuilder.searchAfter(searchAfter);
        }

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(UserCacheElasticSearchConstant.USER_INDEX);
        //设置查询条件到请求对象中
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest,  RequestOptions.DEFAULT);
        SearchHits searchHits = searchResponse.getHits();
        //设置总条数
        searchAfterPage.setTotal(searchHits.getTotalHits().value);
        SearchHit[] searchHitsHits = searchHits.getHits();
        if(searchHitsHits!=null&&searchHitsHits.length>0) {
            for (SearchHit searchHit : searchHitsHits) {
                String sourceString = searchHit.getSourceAsString();
                if (StringUtils.isNotEmpty(sourceString)) {
                    logger.info("UserElasticSearchService queryListForSearchAfter {}", sourceString);
                    userElasticSearchVOS.add(JSONObject.parseObject(sourceString, UserElasticSearchVO.class));
                }
            }
            //设置sort values
            searchAfterPage.setSortValues(searchHitsHits[searchHitsHits.length-1].getSortValues());
        }
        return searchAfterPage;
    }

    @Override
    public Long queryCount(UserElasticSearchVO esUserVo)  throws Exception {
        CountRequest countRequest=new CountRequest(UserCacheElasticSearchConstant.USER_INDEX);
        //创建查询对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        //设置邮箱条件查询条件
        if(StringUtils.isNotEmpty(esUserVo.getEmail()))
        {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("email",esUserVo.getEmail()));
        }
        //设置手机号查询条件
        if(StringUtils.isNotEmpty(esUserVo.getMobilePhone()))
        {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("mobilePhone", esUserVo.getMobilePhone()));
        }

        //设置昵称查询条件
        if(StringUtils.isNotEmpty(esUserVo.getNickName()))
        {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("nickName", esUserVo.getNickName()));
        }

        //设置用户ID查询条件
        if(esUserVo.getId()!=null)
        {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("id", esUserVo.getId()));
        }

        //设置查询条件组
        searchSourceBuilder.query(boolQueryBuilder);

        CountResponse response=restHighLevelClient.count(countRequest,RequestOptions.DEFAULT);
        return response.getCount();
    }
}
