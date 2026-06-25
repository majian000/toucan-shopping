package com.toucan.shopping.cloud.search.api.single;

import com.toucan.shopping.cloud.search.api.feign.service.FeignProductSearchService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.search.es.index.ProductIndex;
import com.toucan.shopping.modules.search.service.ProductSearchService;
import com.toucan.shopping.modules.search.vo.ProductSearchResultVO;
import com.toucan.shopping.modules.search.vo.ProductSearchVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductSearchServiceAPISingleImpl implements FeignProductSearchService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ProductSearchService productSearchService;

    @Override
    public ResultObjectVO search(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        ProductSearchVO productSearch = requestJsonVO.formatEntity(ProductSearchVO.class);
        try {
            if (productSearch.getPage() < 1) {
                productSearch.setPage(1);
            }
            resultObjectVO.setData(productSearchService.search(productSearch));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
        }
        return resultObjectVO;
    }

    @Override
    public ResultObjectVO save(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        ProductSearchResultVO productSearchResultVO = requestJsonVO.formatEntity(ProductSearchResultVO.class);
        try {
            productSearchService.save(productSearchResultVO);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
        }
        return resultObjectVO;
    }

    @Override
    public ResultObjectVO queryBySkuId(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        Long skuId = requestJsonVO.formatEntity(Long.class);
        try {
            resultObjectVO.setData(productSearchService.queryBySkuId(skuId));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
        }
        return resultObjectVO;
    }

    @Override
    public ResultObjectVO update(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        ProductSearchResultVO productSearchResultVO = requestJsonVO.formatEntity(ProductSearchResultVO.class);
        try {
            productSearchService.update(productSearchResultVO);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
        }
        return resultObjectVO;
    }

    @Override
    public ResultObjectVO removeById(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        Long skuId = requestJsonVO.formatEntity(Long.class);
        try {
            List<Long> deleteFaildList = new ArrayList<>();
            productSearchService.removeById(skuId, deleteFaildList);
            resultObjectVO.setData(deleteFaildList);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
        }
        return resultObjectVO;
    }

    @Override
    public ResultObjectVO clear(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            productSearchService.deleteIndex();
            productSearchService.createIndex();
            productSearchService.setMaxResultWindow(ProductIndex.MAX_RESULT_WINDOW);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
        }
        return resultObjectVO;
    }

    @Override
    public ResultObjectVO count(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        ProductSearchVO productSearch = requestJsonVO.formatEntity(ProductSearchVO.class);
        try {
            resultObjectVO.setData(productSearchService.queryCount(productSearch));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
        }
        return resultObjectVO;
    }

}
