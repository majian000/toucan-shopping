package com.toucan.shopping.cloud.product.api.single;

import com.toucan.shopping.cloud.product.api.AttributeKeyServiceAPI;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.product.business.service.AttributeKeyBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttributeKeyServiceSingleImpl implements AttributeKeyServiceAPI {

    @Autowired
    private AttributeKeyBusinessService attributeKeyBusinessService;

    @Override
    public ResultObjectVO queryListPage(RequestJsonVO requestJsonVO) {
        return attributeKeyBusinessService.queryListPage(requestJsonVO);
    }

    @Override
    public ResultObjectVO save(RequestJsonVO requestVo) {
        return attributeKeyBusinessService.save(requestVo);
    }

    @Override
    public ResultObjectVO update(RequestJsonVO requestVo) {
        return attributeKeyBusinessService.update(requestVo);
    }

    @Override
    public ResultObjectVO findById(RequestJsonVO requestVo) {
        return attributeKeyBusinessService.findById(requestVo);
    }

    @Override
    public ResultObjectVO queryTreeTableByPid(RequestJsonVO requestJsonVO) {
        return attributeKeyBusinessService.queryTreeTableByPid(requestJsonVO);
    }

    @Override
    public ResultObjectVO deleteById(RequestJsonVO requestVo) {
        return attributeKeyBusinessService.deleteById(requestVo);
    }

    @Override
    public ResultObjectVO deleteByIds(RequestJsonVO requestVo) {
        return attributeKeyBusinessService.deleteByIds(requestVo);
    }

    @Override
    public ResultObjectVO queryTreeByCategoryId(RequestJsonVO requestJsonVO) {
        return attributeKeyBusinessService.queryTreeByCategoryId(requestJsonVO);
    }

    @Override
    public ResultObjectVO querySearchList(RequestJsonVO requestJsonVO) {
        return attributeKeyBusinessService.querySearchList(requestJsonVO);
    }
}
