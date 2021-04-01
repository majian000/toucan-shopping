package com.toucan.shopping.center.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.center.user.export.constant.UserCacheElasticSearchConstant;
import com.toucan.shopping.center.user.export.vo.UserElasticSearchVO;
import com.toucan.shopping.center.user.service.UserElasticSearchService;
import com.toucan.shopping.center.user.vo.SearchAfterPage;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
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
    public SearchAfterPage queryListForSearchAfter(UserElasticSearchVO esUserVo, int size, Object[] searchAfter) throws Exception {
        SearchAfterPage searchAfterPage = new SearchAfterPage();
        List<UserElasticSearchVO> userElasticSearchVOS = new ArrayList<UserElasticSearchVO>();
        searchAfterPage.setUserElasticSearchVOS(userElasticSearchVOS);

        //创建查询对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(size);
        //设置模糊查询条件
        if(StringUtils.isNotEmpty(esUserVo.getKeyword())) {
            searchSourceBuilder.query(QueryBuilders.multiMatchQuery(esUserVo.getKeyword(),
                    "_id","mobilePhone","nickName","email","username","idCard"));
        }
        //设置邮箱条件查询条件
        if(StringUtils.isNotEmpty(esUserVo.getEmail()))
        {
            searchSourceBuilder.query(QueryBuilders.termQuery("email", esUserVo.getEmail()));
        }
        //设置手机号查询条件
        if(StringUtils.isNotEmpty(esUserVo.getMobilePhone()))
        {
            searchSourceBuilder.query(QueryBuilders.termQuery("mobilePhone", esUserVo.getMobilePhone()));
        }

        //设置昵称查询条件
        if(StringUtils.isNotEmpty(esUserVo.getNickName()))
        {
            searchSourceBuilder.query(QueryBuilders.termQuery("nickName", esUserVo.getNickName()));
        }

        //设置用户ID查询条件
        if(esUserVo.getId()!=null)
        {
            searchSourceBuilder.query(QueryBuilders.termQuery("_id", esUserVo.getId()));
        }


        //根据ID降序
        searchSourceBuilder.sort("_id", SortOrder.DESC);
        //设置after
        if(searchAfter!=null) {
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
}
