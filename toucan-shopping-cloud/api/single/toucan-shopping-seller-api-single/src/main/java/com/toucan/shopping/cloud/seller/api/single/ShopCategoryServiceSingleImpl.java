package com.toucan.shopping.cloud.seller.api.single;

import com.toucan.shopping.cloud.seller.api.feign.service.FeignShopCategoryService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.seller.business.service.ShopCategoryBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShopCategoryServiceSingleImpl implements FeignShopCategoryService {

    @Autowired
    private ShopCategoryBusinessService shopCategoryBusinessService;

    @Override
    public ResultObjectVO save(String signHeader, RequestJsonVO requestJsonVO) {
        return shopCategoryBusinessService.save(signHeader, requestJsonVO);
    }

    @Override
    public ResultObjectVO saveForAdmin(RequestJsonVO requestJsonVO) {
        return shopCategoryBusinessService.saveForAdmin(requestJsonVO);
    }

    @Override
    public ResultObjectVO update(RequestJsonVO requestJsonVO) {
        return shopCategoryBusinessService.update(requestJsonVO);
    }

    @Override
    public ResultObjectVO updateForAdmin(RequestJsonVO requestJsonVO) {
        return shopCategoryBusinessService.updateForAdmin(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryAllList(RequestJsonVO requestJsonVO) {
        return shopCategoryBusinessService.queryAllList(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryById(RequestJsonVO requestJsonVO) {
        return shopCategoryBusinessService.queryById(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryByIdList(String signHeader, RequestJsonVO requestJsonVO) {
        return shopCategoryBusinessService.queryByIdList(signHeader, requestJsonVO);
    }

    @Override
    public ResultObjectVO flushCache(RequestJsonVO requestVo) {
        return shopCategoryBusinessService.flushCache(requestVo);
    }

    @Override
    public ResultObjectVO clearCache(RequestJsonVO requestVo) {
        return shopCategoryBusinessService.clearCache(requestVo);
    }

    @Override
    public ResultObjectVO findById(RequestJsonVO requestVo) {
        return shopCategoryBusinessService.findById(requestVo);
    }

    @Override
    public ResultObjectVO moveTop(RequestJsonVO requestJsonVO) {
        return shopCategoryBusinessService.moveTop(requestJsonVO);
    }

    @Override
    public ResultObjectVO moveTopForAdmin(RequestJsonVO requestJsonVO) {
        return shopCategoryBusinessService.moveTopForAdmin(requestJsonVO);
    }

    @Override
    public ResultObjectVO moveBottom(RequestJsonVO requestJsonVO) {
        return shopCategoryBusinessService.moveBottom(requestJsonVO);
    }

    @Override
    public ResultObjectVO moveBottomForAdmin(RequestJsonVO requestJsonVO) {
        return shopCategoryBusinessService.moveBottomForAdmin(requestJsonVO);
    }

    @Override
    public ResultObjectVO moveUp(RequestJsonVO requestJsonVO) {
        return shopCategoryBusinessService.moveUp(requestJsonVO);
    }

    @Override
    public ResultObjectVO moveUpForAdmin(RequestJsonVO requestJsonVO) {
        return shopCategoryBusinessService.moveUpForAdmin(requestJsonVO);
    }

    @Override
    public ResultObjectVO moveDown(RequestJsonVO requestJsonVO) {
        return shopCategoryBusinessService.moveDown(requestJsonVO);
    }

    @Override
    public ResultObjectVO moveDownForAdmin(RequestJsonVO requestJsonVO) {
        return shopCategoryBusinessService.moveDownForAdmin(requestJsonVO);
    }

    @Override
    public ResultObjectVO findByIdArray(RequestJsonVO requestVo) {
        return shopCategoryBusinessService.findByIdArray(requestVo);
    }

    @Override
    public ResultObjectVO queryTree(RequestJsonVO requestJsonVO) {
        return shopCategoryBusinessService.queryTree(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryWebIndexTree(RequestJsonVO requestJsonVO) {
        return shopCategoryBusinessService.queryWebIndexTree(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryTreeTable(RequestJsonVO requestJsonVO) {
        return shopCategoryBusinessService.queryTreeTable(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryListByPid(RequestJsonVO requestJsonVO) {
        return shopCategoryBusinessService.queryListByPid(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryTreeTableByPid(RequestJsonVO requestJsonVO) {
        return shopCategoryBusinessService.queryTreeTableByPid(requestJsonVO);
    }

    @Override
    public ResultObjectVO deleteById(RequestJsonVO requestJsonVO) {
        return shopCategoryBusinessService.deleteById(requestJsonVO);
    }

    @Override
    public ResultObjectVO deleteByIdForAdmin(RequestJsonVO requestJsonVO) {
        return shopCategoryBusinessService.deleteByIdForAdmin(requestJsonVO);
    }

    @Override
    public ResultObjectVO deleteByIds(RequestJsonVO requestVo) {
        return shopCategoryBusinessService.deleteByIds(requestVo);
    }

    @Override
    public ResultObjectVO findIdPathById(RequestJsonVO requestVo) {
        return shopCategoryBusinessService.findIdPathById(requestVo);
    }

    @Override
    public ResultObjectVO queryListByShopId(RequestJsonVO requestJsonVO) {
        return shopCategoryBusinessService.queryListByShopId(requestJsonVO);
    }

}
