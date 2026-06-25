package com.toucan.shopping.cloud.seller.api.single;

import com.toucan.shopping.cloud.seller.api.feign.service.FeignFreightTemplateService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.seller.business.service.FreightTemplateBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FreightTemplateServiceSingleImpl implements FeignFreightTemplateService {

    @Autowired
    private FreightTemplateBusinessService freightTemplateBusinessService;

    @Override
    public ResultObjectVO queryListPage(RequestJsonVO requestVo) {
        return freightTemplateBusinessService.queryListPage(requestVo);
    }

    @Override
    public ResultObjectVO save(RequestJsonVO requestJsonVO) {
        return freightTemplateBusinessService.save(requestJsonVO);
    }

    @Override
    public ResultObjectVO findByIdList(RequestJsonVO requestVo) {
        return freightTemplateBusinessService.findByIdList(requestVo);
    }

    @Override
    public ResultObjectVO update(RequestJsonVO requestJsonVO) {
        return freightTemplateBusinessService.update(requestJsonVO);
    }

    @Override
    public ResultObjectVO findByIdAndUserMainId(RequestJsonVO requestVo) {
        return freightTemplateBusinessService.findByIdAndUserMainId(requestVo);
    }

    @Override
    public ResultObjectVO deleteById(String signHeader, RequestJsonVO requestJsonVO) {
        return freightTemplateBusinessService.deleteById(signHeader, requestJsonVO);
    }

    @Override
    public ResultObjectVO findById(RequestJsonVO requestVo) {
        return freightTemplateBusinessService.findById(requestVo);
    }

}
