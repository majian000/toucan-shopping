package com.toucan.shopping.cloud.user.api.single;

import com.toucan.shopping.cloud.user.api.feign.service.FeignConsigneeAddressService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.user.business.service.ConsigneeAddressBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConsigneeAddressServiceSingleImpl implements FeignConsigneeAddressService {

    @Autowired
    private ConsigneeAddressBusinessService consigneeAddressBusinessService;

    @Override
    public ResultObjectVO save(RequestJsonVO requestJsonVO) {
        return consigneeAddressBusinessService.save(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryListPage(RequestJsonVO requestVo) {
        return consigneeAddressBusinessService.queryListPage(requestVo);
    }

    @Override
    public ResultObjectVO update(RequestJsonVO requestJsonVO) {
        return consigneeAddressBusinessService.update(requestJsonVO);
    }

    @Override
    public ResultObjectVO deleteByIdAndUserMainIdAndAppCode(RequestJsonVO requestVo) {
        return consigneeAddressBusinessService.deleteByIdAndUserMainIdAndAppCode(requestVo);
    }

    @Override
    public ResultObjectVO setDefaultByIdAndUserMainId(RequestJsonVO requestVo) {
        return consigneeAddressBusinessService.setDefaultByIdAndUserMainId(requestVo);
    }

    @Override
    public ResultObjectVO findByIdAndUserMainIdAndAppcode(RequestJsonVO requestVo) {
        return consigneeAddressBusinessService.findByIdAndUserMainIdAndAppcode(requestVo);
    }

    @Override
    public ResultObjectVO findDefaultByUserMainIdAndAppcode(RequestJsonVO requestVo) {
        return consigneeAddressBusinessService.findDefaultByUserMainIdAndAppcode(requestVo);
    }
}
