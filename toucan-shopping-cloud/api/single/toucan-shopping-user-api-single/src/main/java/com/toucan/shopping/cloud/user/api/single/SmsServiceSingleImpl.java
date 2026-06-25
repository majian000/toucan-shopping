package com.toucan.shopping.cloud.user.api.single;

import com.toucan.shopping.cloud.user.api.SmsServiceAPI;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.user.business.service.SmsBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceSingleImpl implements SmsServiceAPI {

    @Autowired
    private SmsBusinessService smsBusinessService;

    @Override
    public ResultObjectVO send(RequestJsonVO requestJsonVO) {
        return smsBusinessService.send(requestJsonVO);
    }
}
