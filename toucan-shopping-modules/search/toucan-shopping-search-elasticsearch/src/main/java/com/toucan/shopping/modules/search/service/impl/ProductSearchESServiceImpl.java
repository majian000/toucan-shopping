package com.toucan.shopping.modules.search.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.search.es.index.ProductIndex;
import com.toucan.shopping.modules.search.service.ProductSearchService;
import com.toucan.shopping.modules.search.vo.ProductSearchResultVO;
import com.toucan.shopping.modules.search.vo.ProductSearchVO;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
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
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;


@Service("productSearchESServiceImpl")
public class ProductSearchESServiceImpl implements ProductSearchService {


    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public void createIndex() {
        try {
            CreateIndexRequest request = new CreateIndexRequest(ProductIndex.PRODUCT_SKU_INDEX);
            restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
    }

    @Override
    public PageInfo<ProductSearchResultVO> search(ProductSearchVO productSearchVO) throws IOException {
        List<ProductSearchResultVO> queryResult= new LinkedList<>();

        SearchRequest request = new SearchRequest();
        request.indices(ProductIndex.PRODUCT_SKU_INDEX);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder productNameIKBoolBuilder = QueryBuilders.boolQuery();
        productNameIKBoolBuilder.must(QueryBuilders.matchQuery("name.keyword_ik", productSearchVO.getKeyword()));
        sourceBuilder.query(productNameIKBoolBuilder);

        sourceBuilder.from(productSearchVO.getPage()-1);
        sourceBuilder.size(productSearchVO.getSize());
        request.source(sourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        SearchHits searchHits = searchResponse.getHits();
        SearchHit[] searchHitsHits = searchHits.getHits();
        for (SearchHit searchHit : searchHitsHits) {
            String sourceString = searchHit.getSourceAsString();
            if (StringUtils.isNotEmpty(sourceString)) {
                queryResult.add(JSONObject.parseObject(sourceString,ProductSearchResultVO.class));
            }
        }
        PageInfo pageInfo = new PageInfo();
        pageInfo.setList(queryResult);
        pageInfo.setPage(productSearchVO.getPage());
        pageInfo.setSize(productSearchVO.getSize());
        return pageInfo;
    }


    @Override
    public boolean existsIndex() {
        try {
            GetAliasesRequest request = new GetAliasesRequest();
            GetAliasesResponse getAliasesResponse = restHighLevelClient.indices().getAlias(request, RequestOptions.DEFAULT);
            Map<String, Set<AliasMetadata>> map = getAliasesResponse.getAliases();
            Set<String> indices = map.keySet();
            for (String key : indices) {
                if(key.toLowerCase().equals(ProductIndex.PRODUCT_SKU_INDEX))
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
    public List<ProductSearchResultVO> queryBySkuId(Long id) throws Exception {
        List<ProductSearchResultVO> productSearchResultVOS = new ArrayList<ProductSearchResultVO>();
        //创建请求对象
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(ProductIndex.PRODUCT_SKU_INDEX);
        //创建查询对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(10);
        //设置查询条件
        searchSourceBuilder.query(QueryBuilders.termQuery("skuId", id));
        //设置查询条件到请求对象中
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest,  RequestOptions.DEFAULT);
        SearchHits searchHits = searchResponse.getHits();
        SearchHit[] searchHitsHits = searchHits.getHits();
        for(SearchHit searchHit:searchHitsHits) {
            String sourceString = searchHit.getSourceAsString();
            if (StringUtils.isNotEmpty(sourceString)){
                productSearchResultVOS.add(JSONObject.parseObject(sourceString,ProductSearchResultVO.class));
            }
        }
        return productSearchResultVOS;
    }

    @Override
    public void save(ProductSearchResultVO productSearchResultVO) throws IOException {
        IndexRequest request = new IndexRequest(ProductIndex.PRODUCT_SKU_INDEX).id(String.valueOf(productSearchResultVO.getSkuId())).source(JSONObject.toJSONString(productSearchResultVO), XContentType.JSON);
        restHighLevelClient.index(request, RequestOptions.DEFAULT);
    }

    @Override
    public void update(ProductSearchResultVO productSearchResultVO) throws IOException, IllegalAccessException {
        UpdateRequest request = new UpdateRequest(ProductIndex.PRODUCT_SKU_INDEX,String.valueOf(productSearchResultVO.getId()));
        XContentBuilder updateBody = XContentFactory.jsonBuilder().startObject();
        Field[] declaredFields = productSearchResultVO.getClass().getDeclaredFields();
        for(Field field:declaredFields){
            field.setAccessible(true);
            updateBody.field(field.getName(),field.get(productSearchResultVO));
        }
        updateBody.endObject();
        request.doc(updateBody);
        UpdateResponse updateResponse = restHighLevelClient.update(request, RequestOptions.DEFAULT);
        //强制刷新
        updateResponse.forcedRefresh();
    }
}
