package com.toucan.shopping.cloud.common.data.api.single;

import com.toucan.shopping.cloud.common.data.api.CategoryServiceAPI;
import com.toucan.shopping.modules.category.business.service.CategoryBusinessService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceSingleImpl implements CategoryServiceAPI {

    @Autowired
    private CategoryBusinessService categoryBusinessService;

    @Override
    public ResultObjectVO save(RequestJsonVO requestJsonVO) {
        return categoryBusinessService.save("",requestJsonVO);
    }

    @Override
    public ResultObjectVO queryById(RequestJsonVO requestJsonVO) {
        return categoryBusinessService.queryById(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryByIdList(RequestJsonVO requestJsonVO) {
        return categoryBusinessService.queryByIdList(requestJsonVO);
    }

    @Override
    public ResultObjectVO findByIdArray(RequestJsonVO requestJsonVO) {
        return categoryBusinessService.findByIdArray(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryTreeChildByPid(RequestJsonVO requestJsonVO) {
        return categoryBusinessService.queryTreeChildByPid(requestJsonVO);
    }

    @Override
    public ResultObjectVO findIdPathById(RequestJsonVO requestVo) {
        return categoryBusinessService.findIdPathById(requestVo);
    }

    @Override
    public ResultObjectVO findById(RequestJsonVO requestVo) {
        return categoryBusinessService.findById(requestVo);
    }

    @Override
    public ResultObjectVO queryTreeTable(RequestJsonVO requestJsonVO) {
        return categoryBusinessService.queryTreeTable(requestJsonVO);
    }

    @Override
    public ResultObjectVO update(RequestJsonVO requestVo) {
        return categoryBusinessService.update(requestVo);
    }

    @Override
    public ResultObjectVO queryTreeTableByPid(RequestJsonVO requestJsonVO) {
        return categoryBusinessService.queryTreeTableByPid(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryTree(RequestJsonVO requestJsonVO) {
        return categoryBusinessService.queryTree(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryMiniTree(RequestJsonVO requestJsonVO) {
        return categoryBusinessService.queryMiniTree(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryWebIndexTree(RequestJsonVO requestJsonVO) {
        return categoryBusinessService.queryWebIndexTree(requestJsonVO);
    }

    @Override
    public ResultObjectVO flushWebIndexCache(RequestJsonVO requestVo) {
        return categoryBusinessService.flushWebIndexCache(requestVo);
    }

    @Override
    public ResultObjectVO flushAllCache(RequestJsonVO requestVo) {
        return categoryBusinessService.flushAllCache(requestVo);
    }

    @Override
    public ResultObjectVO flushWMiniTreeCache(RequestJsonVO requestVo) {
        return categoryBusinessService.flushWMiniTreeCache(requestVo);
    }

    @Override
    public ResultObjectVO flushNavigationMiniTreeCache(RequestJsonVO requestVo) {
        return categoryBusinessService.flushNavigationMiniTreeCache(requestVo);
    }

    @Override
    public ResultObjectVO clearWebIndexCache(RequestJsonVO requestVo) {
        return categoryBusinessService.clearWebIndexCache(requestVo);
    }

    @Override
    public ResultObjectVO deleteByIds(RequestJsonVO requestVo) {
        return categoryBusinessService.deleteByIds(requestVo);
    }

    @Override
    public ResultObjectVO deleteById(RequestJsonVO requestVo) {
        return categoryBusinessService.deleteById("",requestVo);
    }

    @Override
    public ResultObjectVO queryListByPid(RequestJsonVO requestJsonVO) {
        return categoryBusinessService.queryListByPid(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryChildListByPid(RequestJsonVO requestJsonVO) {
        return categoryBusinessService.queryChildListByPid(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryNextOneLevelChildListByPid(RequestJsonVO requestJsonVO) {
        return categoryBusinessService.queryNextOneLevelChildListByPid(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryAllList(RequestJsonVO requestJsonVO) {
        return categoryBusinessService.queryAllList(requestJsonVO);
    }
}
