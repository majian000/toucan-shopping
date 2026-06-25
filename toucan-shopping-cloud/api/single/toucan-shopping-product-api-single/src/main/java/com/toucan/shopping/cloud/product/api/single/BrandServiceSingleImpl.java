package com.toucan.shopping.cloud.product.api.single;

import com.toucan.shopping.cloud.product.api.BrandServiceAPI;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.product.business.service.BrandBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BrandServiceSingleImpl implements BrandServiceAPI {

    @Autowired
    private BrandBusinessService brandBusinessService;

    @Override
    public ResultObjectVO queryListPage(RequestJsonVO requestJsonVO) {
        return brandBusinessService.queryListPage(requestJsonVO);
    }

    @Override
    public ResultObjectVO save(RequestJsonVO requestVo) {
        return brandBusinessService.save(requestVo);
    }

    @Override
    public ResultObjectVO update(RequestJsonVO requestVo) {
        return brandBusinessService.update(requestVo);
    }

    @Override
    public ResultObjectVO findById(RequestJsonVO requestVo) {
        return brandBusinessService.findById(requestVo);
    }

    @Override
    public ResultObjectVO findByIdList(RequestJsonVO requestVo) {
        return brandBusinessService.findByIdList(requestVo);
    }

    @Override
    public ResultObjectVO deleteById(RequestJsonVO requestVo) {
        return brandBusinessService.deleteById(requestVo);
    }

    @Override
    public ResultObjectVO deleteByIds(RequestJsonVO requestVo) {
        return brandBusinessService.deleteByIds(requestVo);
    }

    @Override
    public ResultObjectVO findListByNameAndCategoryIdAndEnabled(RequestJsonVO requestVo) {
        return brandBusinessService.findListByNameAndCategoryIdAndEnabled(requestVo);
    }

    @Override
    public ResultObjectVO queryListByCategoryId(RequestJsonVO requestJsonVO) {
        return brandBusinessService.queryListByCategoryId(requestJsonVO);
    }
}
