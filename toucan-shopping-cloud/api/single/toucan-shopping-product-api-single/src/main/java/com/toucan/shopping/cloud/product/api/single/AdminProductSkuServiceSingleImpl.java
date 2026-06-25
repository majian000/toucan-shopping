package com.toucan.shopping.cloud.product.api.single;

import com.toucan.shopping.cloud.product.api.feign.service.FeignAdminProductSkuService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.product.business.service.ProductSkuBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminProductSkuServiceSingleImpl implements FeignAdminProductSkuService {

    @Autowired
    private ProductSkuBusinessService productSkuBusinessService;

    @Override
    public ResultObjectVO saveSku(String signHeader, RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        resultObjectVO.setCode(ResultObjectVO.FAILD);
        resultObjectVO.setMsg("请稍后重试!");
        return resultObjectVO;
    }
}
