package com.toucan.shopping.cloud.message.api.single;

import com.toucan.shopping.cloud.message.api.MessageUserServiceAPI;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.message.business.service.MessageUserBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageUserServiceSingleImpl implements MessageUserServiceAPI {

    @Autowired
    private MessageUserBusinessService messageUserBusinessService;

    @Override
    public ResultObjectVO send(RequestJsonVO requestJsonVO) {
        return messageUserBusinessService.send(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryListPage(RequestJsonVO requestJsonVO) {
        return messageUserBusinessService.queryListPage(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryListPageByUserMianId(RequestJsonVO requestJsonVO) {
        return messageUserBusinessService.queryListPageByUserMianId(requestJsonVO);
    }

    @Override
    public ResultObjectVO update(RequestJsonVO requestVo) {
        return messageUserBusinessService.update(requestVo);
    }

    @Override
    public ResultObjectVO findById(RequestJsonVO requestJsonVO) {
        return messageUserBusinessService.findById(requestJsonVO);
    }

    @Override
    public ResultObjectVO deleteById(RequestJsonVO requestJsonVO) {
        return messageUserBusinessService.deleteById(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryUnreadCountByUserMainId(RequestJsonVO requestJsonVO) {
        return messageUserBusinessService.queryUnreadCountByUserMainId(requestJsonVO);
    }

    @Override
    public ResultObjectVO updateReadStatus(RequestJsonVO requestJsonVO) {
        return messageUserBusinessService.updateReadStatus(requestJsonVO);
    }

    @Override
    public ResultObjectVO updateAllReadStatus(RequestJsonVO requestJsonVO) {
        return messageUserBusinessService.updateAllReadStatus(requestJsonVO);
    }
}
