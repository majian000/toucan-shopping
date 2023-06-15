package com.toucan.shopping.modules.search.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.util.DateUtils;
import com.toucan.shopping.modules.search.es.index.ProductIndex;
import com.toucan.shopping.modules.search.service.ProductSearchService;
import com.toucan.shopping.modules.search.vo.ProductSearchAttributeVO;
import com.toucan.shopping.modules.search.vo.ProductSearchResultVO;
import com.toucan.shopping.modules.search.vo.ProductSearchVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.GetAliasesResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.cluster.metadata.AliasMetadata;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.awt.image.ImageWatched;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Stream;


@Service("productSearchESServiceImpl")
public class ProductSearchESServiceImpl implements ProductSearchService {


    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public void createIndex() {
        try {
            CreateIndexRequest request = new CreateIndexRequest(ProductIndex.PRODUCT_SKU_INDEX);

            //配置IK分词器
            XContentBuilder builder = XContentFactory.jsonBuilder();
            builder.startObject();
            {
                builder.startObject("properties");
                {
                    builder.startObject("id");
                    {
                        builder.field("type", "long");
                    }
                    builder.endObject();

                    builder.startObject("skuId");
                    {
                        builder.field("type", "long");
                    }
                    builder.endObject();

                    //商品名称
                    builder.startObject("name");
                    {
                        builder.field("type", "text")
                                //插入时分词
                                .field("analyzer", "ik_max_word")
                                //搜索时分词
                                .field("search_analyzer", "ik_smart");
                    }
                    builder.endObject();

                    //价格
                    builder.startObject("price");
                    {
                        builder.field("type", "double");
                    }
                    builder.endObject();

                    //品牌名称
                    builder.startObject("brandName");
                    {
                        builder.field("type", "text")
                                //插入时分词
                                .field("analyzer", "ik_max_word")
                                //搜索时分词
                                .field("search_analyzer", "ik_smart");
                    }
                    builder.endObject();


                    //品牌中文名称
                    builder.startObject("brandNameCN");
                    {
                        builder.field("type", "text")
                                //插入时分词
                                .field("analyzer", "ik_max_word")
                                //搜索时分词
                                .field("search_analyzer", "ik_smart");
                    }
                    builder.endObject();

                    //品牌英文名称
                    builder.startObject("brandNameEN");
                    {
                        builder.field("type", "text")
                                //插入时分词
                                .field("analyzer", "ik_max_word")
                                //搜索时分词
                                .field("search_analyzer", "ik_smart");
                    }
                    builder.endObject();

                    //分类名称
                    builder.startObject("categoryName");
                    {
                        builder.field("type", "text")
                                //插入时分词
                                .field("analyzer", "ik_max_word")
                                //搜索时分词
                                .field("search_analyzer", "ik_smart");
                    }
                    builder.endObject();


                    //分类ID数组
                    builder.startObject("categoryIds");
                    {
                        builder.field("type", "text");
                    }
                    builder.endObject();

                    //属性数组
                    builder.startObject("attributes");
                    {
                        builder.field("type", "nested");

                        builder.startObject("properties");
                        {
                            builder.startObject("name");
                            {
                                builder.field("type", "keyword");
                            }
                            builder.endObject();

                            builder.startObject("value");
                            {
                                builder.field("type", "keyword");
                            }
                            builder.endObject();
                        }
                        builder.endObject();
                    }
                    builder.endObject();

                    //搜索属性
                    builder.startObject("searchAttributes");
                    {
                        builder.field("type", "nested");

                        builder.startObject("properties");
                        {
                            builder.startObject("nameId");
                            {
                                builder.field("type", "long");
                            }
                            builder.endObject();

                            builder.startObject("name");
                            {
                                builder.field("type", "keyword");
                            }
                            builder.endObject();

                            builder.startObject("valueId");
                            {
                                builder.field("type", "long");
                            }
                            builder.endObject();

                            builder.startObject("value");
                            {
                                builder.field("type", "keyword");
                            }
                            builder.endObject();
                        }
                        builder.endObject();
                    }
                    builder.endObject();

                    //品牌ID
                    builder.startObject("brandId");
                    {
                        builder.field("type", "long");
                    }
                    builder.endObject();

                    //权重值
                    builder.startObject("randk");
                    {
                        builder.field("type", "double");
                    }
                    builder.endObject();

                }
                builder.endObject();
            }
            builder.endObject();
            request.mapping(builder);
            restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
    }

    @Override
    public PageInfo<ProductSearchResultVO> search(ProductSearchVO productSearchVO) throws Exception {

        List<ProductSearchResultVO> queryResult = new LinkedList<>();

        SearchRequest request = new SearchRequest(ProductIndex.PRODUCT_SKU_INDEX);

        //名称查询
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        if(StringUtils.isNotEmpty(productSearchVO.getKeyword())) {
            boolQueryBuilder.must(QueryBuilders
                    .multiMatchQuery(productSearchVO.getKeyword(), new String[]{"name", "brandName","brandNameCN","brandNameEN", "categoryName"})
            );
        }
        //分类查询
        if(StringUtils.isNotEmpty(productSearchVO.getCid())) {
            boolQueryBuilder.must(QueryBuilders.termQuery("categoryIds",productSearchVO.getCid()));
        }
        //商品名称查询
        if(StringUtils.isNotEmpty(productSearchVO.getProductName()))
        {
            boolQueryBuilder.must(QueryBuilders
                    .multiMatchQuery(productSearchVO.getProductName(), new String[]{"name"})
            );
        }
        //品牌名称查询
        if(StringUtils.isNotEmpty(productSearchVO.getBn()))
        {
            boolQueryBuilder.must(QueryBuilders
                    .multiMatchQuery(productSearchVO.getBn(), new String[]{"brandName","brandNameCN","brandNameEN"})
            );
        }
        //分类名称查询
        if(StringUtils.isNotEmpty(productSearchVO.getCategoryName()))
        {
            boolQueryBuilder.must(QueryBuilders
                    .multiMatchQuery(productSearchVO.getCategoryName(), new String[]{"categoryName"})
            );
        }
        //品牌查询
        if(CollectionUtils.isNotEmpty(productSearchVO.getBrandIds())) {
            boolQueryBuilder.must(QueryBuilders.termsQuery("brandId",productSearchVO.getBrandIds()));
        }
        //品牌查询
        if(StringUtils.isNotEmpty(productSearchVO.getBid()))
        {
            boolQueryBuilder.must(QueryBuilders.termQuery("brandId",productSearchVO.getBid()));
        }
        //属性查询
        if(CollectionUtils.isNotEmpty(productSearchVO.getSearchAttributes()))
        {
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            for(ProductSearchAttributeVO productSearchAttributeVO:productSearchVO.getSearchAttributes()) {
                BoolQueryBuilder attributeQuery = QueryBuilders.boolQuery();
                attributeQuery.must(QueryBuilders.termQuery("searchAttributes.name", productSearchAttributeVO.getName()));
                attributeQuery.must(QueryBuilders.termQuery("searchAttributes.value", productSearchAttributeVO.getValue()));
                NestedQueryBuilder nestedQuery = QueryBuilders.nestedQuery("searchAttributes", attributeQuery, ScoreMode.None);
                boolQuery.filter(nestedQuery);
            }
            boolQueryBuilder.must(boolQuery);

        }

        if(productSearchVO.getPsd() != null||productSearchVO.getPed() != null) {
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            /**
             gt：大于 >
             gte：大于等于 >=
             lt：小于  <
             lte：小于等于  <=
             */
            //价格查询 大于等于
            if (productSearchVO.getPsd() != null) {
                boolQuery.filter(QueryBuilders.rangeQuery("price").gte(productSearchVO.getPsd()));
            }
            //价格查询 小于等于
            if (productSearchVO.getPed() != null) {
                boolQuery.filter(QueryBuilders.rangeQuery("price").lte(productSearchVO.getPed()));
            }
            boolQueryBuilder.must(boolQuery);
        }

        sourceBuilder.query(boolQueryBuilder);
        sourceBuilder.from(productSearchVO.getPage()==1?productSearchVO.getPage()-1:((productSearchVO.getPage()-1)*productSearchVO.getSize()));
        sourceBuilder.size(productSearchVO.getSize());
        request.source(sourceBuilder);

        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        SearchHits searchHits = response.getHits();
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
        pageInfo.setTotal(queryCount(sourceBuilder).longValue());
        pageInfo.setPageTotal(pageInfo.getTotal()%pageInfo.getSize()==0?(pageInfo.getTotal()/pageInfo.getSize()):((pageInfo.getTotal()/pageInfo.getSize())+1));
        return pageInfo;
    }


    public Long queryCount(SearchSourceBuilder searchSourceBuilder)  throws Exception {
        CountRequest countRequest=new CountRequest(ProductIndex.PRODUCT_SKU_INDEX);
        countRequest.source(searchSourceBuilder);
        CountResponse response=restHighLevelClient.count(countRequest,RequestOptions.DEFAULT);
        return response.getCount();
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
        productSearchResultVO.setCreateDate(DateUtils.FORMATTER_SS.get().format(DateUtils.currentDate()));
        IndexRequest request = new IndexRequest(ProductIndex.PRODUCT_SKU_INDEX).id(String.valueOf(productSearchResultVO.getSkuId())).source(JSONObject.toJSONString(productSearchResultVO), XContentType.JSON);

        restHighLevelClient.index(request, RequestOptions.DEFAULT);
    }

    @Override
    public void update(ProductSearchResultVO productSearchResultVO) throws Exception {
        List<Long> deleteFaildList = new ArrayList<>();
        this.removeById(productSearchResultVO.getSkuId(),deleteFaildList);
        this.save(productSearchResultVO);
    }

    @Override
    public boolean removeById(Long id,List<Long> deleteFaildIdList) throws Exception {
        //创建请求对象
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(ProductIndex.PRODUCT_SKU_INDEX);
        //创建查询对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //设置查询条件
        searchSourceBuilder.query(QueryBuilders.termQuery("skuId", id));
        searchSourceBuilder.size(queryCount(searchSourceBuilder).intValue());
        //设置查询条件到请求对象中
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest,  RequestOptions.DEFAULT);
        SearchHits searchHits = searchResponse.getHits();
        SearchHit[] searchHitsHits = searchHits.getHits();
        for(SearchHit searchHit:searchHitsHits) {
            DeleteRequest deleteRequest = new DeleteRequest(ProductIndex.PRODUCT_SKU_INDEX);
            deleteRequest.id(searchHit.getId());
            DeleteResponse deleteResponse =restHighLevelClient.delete(deleteRequest,RequestOptions.DEFAULT);
            if(RestStatus.OK.getStatus() == deleteResponse.status().getStatus())
            {
                //强制刷新
                deleteResponse.forcedRefresh();
            }else{
                //保存删除失败ID
                deleteFaildIdList.add(Long.parseLong(searchHit.getId()));
            }
        }
        //没有删除失败的ID
        if(CollectionUtils.isEmpty(deleteFaildIdList))
        {
            return true;
        }
        return false;
    }
}
