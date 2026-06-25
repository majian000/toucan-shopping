package com.toucan.shopping.cloud.product.api.single;

import com.toucan.shopping.cloud.product.api.feign.service.FeignAttributeValueService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.product.business.service.AttributeValueBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttributeValueServiceSingleImpl implements FeignAttributeValueService {

    @Autowired
    private AttributeValueBusinessService attributeValueBusinessService;

    @Override
    public ResultObjectVO queryListPage(String signHeader, RequestJsonVO requestJsonVO) {
        return attributeValueBusinessService.queryListPage(requestJsonVO);
    }

    @Override
    public ResultObjectVO save(String signHeader, RequestJsonVO requestVo) {
        return attributeValueBusinessService.save(requestVo);
    }

    @Override
    public ResultObjectVO update(String signHeader, RequestJsonVO requestVo) {
        return attributeValueBusinessService.update(requestVo);
    }

    @Override
    public ResultObjectVO findById(String signHeader, RequestJsonVO requestVo) {
        return attributeValueBusinessService.findById(requestVo);
    }

    @Override
    public ResultObjectVO deleteById(String signHeader, RequestJsonVO requestVo) {
        return attributeValueBusinessService.deleteById(requestVo);
    }

    @Override
    public ResultObjectVO deleteByIds(String signHeader, RequestJsonVO requestVo) {
        return attributeValueBusinessService.deleteByIds(requestVo);
    }
}
