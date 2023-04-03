package com.toucan.shopping.modules.search.service.impl;

import com.toucan.shopping.modules.search.es.index.ProductIndex;
import com.toucan.shopping.modules.search.service.ProductSearchService;
import com.toucan.shopping.modules.search.vo.ProductSearchResultVO;
import com.toucan.shopping.modules.search.vo.ProductSearchVO;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
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
    public List<ProductSearchResultVO> search(ProductSearchVO productSearchVO) {
        return null;
    }
}
