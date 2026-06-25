package com.toucan.shopping.cloud.product.api.single;

import com.toucan.shopping.cloud.product.api.feign.service.FeignAttributeKeyValueService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.product.business.service.AttributeKeyValueBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttributeKeyValueServiceSingleImpl implements FeignAttributeKeyValueService {

    @Autowired
    private AttributeKeyValueBusinessService attributeKeyValueBusinessService;

    @Override
    public ResultObjectVO findByCategoryId(RequestJsonVO requestVo) {
        return attributeKeyValueBusinessService.findByCategoryId(requestVo);
    }

    @Override
    public ResultObjectVO queryAttributeTreePage(RequestJsonVO requestVo) {
        return attributeKeyValueBusinessService.queryAttributeTreePage(requestVo);
    }

    @Override
    public ResultObjectVO querySearchAttributeList(RequestJsonVO requestVo) {
        return attributeKeyValueBusinessService.querySearchAttributeList(requestVo);
    }
}
