package com.toucan.shopping.cloud.product.api.single;

import com.toucan.shopping.cloud.product.api.feign.service.FeignAttributeKeyService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.product.business.service.AttributeKeyBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttributeKeyServiceSingleImpl implements FeignAttributeKeyService {

    @Autowired
    private AttributeKeyBusinessService attributeKeyBusinessService;

    @Override
    public ResultObjectVO queryListPage(String signHeader, RequestJsonVO requestJsonVO) {
        return attributeKeyBusinessService.queryListPage(requestJsonVO);
    }

    @Override
    public ResultObjectVO save(String signHeader, RequestJsonVO requestVo) {
        return attributeKeyBusinessService.save(requestVo);
    }

    @Override
    public ResultObjectVO update(String signHeader, RequestJsonVO requestVo) {
        return attributeKeyBusinessService.update(requestVo);
    }

    @Override
    public ResultObjectVO findById(String signHeader, RequestJsonVO requestVo) {
        return attributeKeyBusinessService.findById(requestVo);
    }

    @Override
    public ResultObjectVO queryTreeTableByPid(RequestJsonVO requestJsonVO) {
        return attributeKeyBusinessService.queryTreeTableByPid(requestJsonVO);
    }

    @Override
    public ResultObjectVO deleteById(String signHeader, RequestJsonVO requestVo) {
        return attributeKeyBusinessService.deleteById(requestVo);
    }

    @Override
    public ResultObjectVO deleteByIds(String signHeader, RequestJsonVO requestVo) {
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
