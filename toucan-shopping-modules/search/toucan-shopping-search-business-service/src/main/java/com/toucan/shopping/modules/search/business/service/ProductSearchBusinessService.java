package com.toucan.shopping.modules.search.business.service;

import com.toucan.shopping.modules.common.annotation.RequestCheck;
import com.toucan.shopping.modules.common.exception.BusinessValidationException;
import com.toucan.shopping.modules.common.util.Check;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.annotation.RequestCheck;
import com.toucan.shopping.modules.common.exception.BusinessValidationException;
import com.toucan.shopping.modules.common.util.Check;
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
import java.util.concurrent.Callable;

@Service
public class ProductSearchBusinessService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ProductSearchService productSearchService;

    /**
     * 安全执行业务逻辑并返回带数据的ResultObjectVO。
     * 自动捕获异常，记录日志并设置失败状态码。
     */
    private ResultObjectVO safeCall(Callable<?> callable) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            resultObjectVO.setData(callable.call());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
        }
        return resultObjectVO;
    }

    /**
     * 安全执行业务逻辑（无返回数据的操作）。
     * 自动捕获异常，记录日志并设置失败状态码。
     */
    @FunctionalInterface
    private interface ThrowingRunnable {
        void run() throws Exception;
    }

    private ResultObjectVO safeRun(ThrowingRunnable action) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            action.run();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
        }
        return resultObjectVO;
    }

    /**
     * 搜索商品
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO search(RequestJsonVO requestJsonVO) {
        return safeCall(() -> {
            ProductSearchVO productSearch = requestJsonVO.formatEntity(ProductSearchVO.class);
            if (productSearch.getPage() < 1) {
                productSearch.setPage(1);
            }
            return productSearchService.search(productSearch);
        });
    }

    /**
     * 搜索商品数量
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO count(RequestJsonVO requestJsonVO) {
        return safeCall(() -> {
            ProductSearchVO productSearch = requestJsonVO.formatEntity(ProductSearchVO.class);
            return productSearchService.queryCount(productSearch);
        });
    }

    /**
     * 保存到搜索
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO save(RequestJsonVO requestJsonVO) {
        return safeRun(() -> {
            ProductSearchResultVO productSearchResultVO = requestJsonVO.formatEntity(ProductSearchResultVO.class);
            productSearchService.save(productSearchResultVO);
        });
    }

    /**
     * 根据SKUID查询
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryBySkuId(RequestJsonVO requestJsonVO) {
        return safeCall(() -> {
            Long skuId = requestJsonVO.formatEntity(Long.class);
            return productSearchService.queryBySkuId(skuId);
        });
    }

    /**
     * 更新到搜索
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO update(RequestJsonVO requestJsonVO) {
        return safeRun(() -> {
            ProductSearchResultVO productSearchResultVO = requestJsonVO.formatEntity(ProductSearchResultVO.class);
            productSearchService.update(productSearchResultVO);
        });
    }

    /**
     * 从搜索中删除
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO removeById(RequestJsonVO requestJsonVO) {
        return safeCall(() -> {
            Long skuId = requestJsonVO.formatEntity(Long.class);
            List<Long> deleteFaildList = new ArrayList<>();
            productSearchService.removeById(skuId, deleteFaildList);
            return deleteFaildList;
        });
    }

    /**
     * 清空索引并重建
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO clear(RequestJsonVO requestJsonVO) {
        return safeRun(() -> {
            productSearchService.deleteIndex();
            productSearchService.createIndex();
            productSearchService.setMaxResultWindow(ProductIndex.MAX_RESULT_WINDOW);
        });
    }

}
