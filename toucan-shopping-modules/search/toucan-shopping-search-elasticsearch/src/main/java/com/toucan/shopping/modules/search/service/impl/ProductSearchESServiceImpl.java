package com.toucan.shopping.modules.search.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.search.es.index.ProductIndex;
import com.toucan.shopping.modules.search.service.ProductSearchService;
import com.toucan.shopping.modules.search.vo.ProductSearchResultVO;
import com.toucan.shopping.modules.search.vo.ProductSearchVO;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("productSearchESServiceImpl")
public class ProductSearchESServiceImpl implements ProductSearchService {


    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public void createIndex() {
        try {
            CreateIndexRequest request = new CreateIndexRequest(ProductIndex.PRODUCT_INDEX);
            restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
    }

    @Override
    public PageInfo<ProductSearchResultVO> search(ProductSearchVO productSearchVO) {
        try {
            SearchRequest request = new SearchRequest();
            request.indices(ProductIndex.PRODUCT_INDEX);
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.query(QueryBuilders.fuzzyQuery("name","name").fuzziness(Fuzziness.AUTO));

            sourceBuilder.from(productSearchVO.getPage()-1);
            sourceBuilder.size(productSearchVO.getSize());
            request.source(sourceBuilder);

            SearchResponse searchResponse = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            SearchHits searchHits = searchResponse.getHits();
            SearchHit[] searchHitsHits = searchHits.getHits();
            for (SearchHit searchHit : searchHitsHits) {
                String sourceString = searchHit.getSourceAsString();
                if (StringUtils.isNotEmpty(sourceString)) {
                }
            }
        }catch(Exception e)
        {
            logger.error(e.getMessage(),e);
        }
        return null;
    }
}
