package com.toucan.shopping.cloud.message.api.single;

import com.toucan.shopping.cloud.message.api.feign.service.FeignMessageTypeService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.message.business.service.MessageTypeBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageTypeServiceSingleImpl implements FeignMessageTypeService {

    @Autowired
    private MessageTypeBusinessService messageTypeBusinessService;

    @Override
    public ResultObjectVO save(RequestJsonVO requestJsonVO) {
        return messageTypeBusinessService.save(requestJsonVO);
    }

    @Override
    public ResultObjectVO deleteById(RequestJsonVO requestJsonVO) {
        return messageTypeBusinessService.deleteById(requestJsonVO);
    }

    @Override
    public ResultObjectVO deleteByIds(RequestJsonVO requestVo) {
        return messageTypeBusinessService.deleteByIds(requestVo);
    }

    @Override
    public ResultObjectVO update(RequestJsonVO requestVo) {
        return messageTypeBusinessService.update(requestVo);
    }

    @Override
    public ResultObjectVO queryListPage(RequestJsonVO requestJsonVO) {
        return messageTypeBusinessService.queryListPage(requestJsonVO);
    }

    @Override
    public ResultObjectVO findById(RequestJsonVO requestJsonVO) {
        return messageTypeBusinessService.findById(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryList(RequestJsonVO requestJsonVO) {
        return messageTypeBusinessService.queryList(requestJsonVO);
    }

    @Override
    public ResultObjectVO findCacheByCode(RequestJsonVO requestVo) {
        return messageTypeBusinessService.findCacheByCode(requestVo);
    }

    @Override
    public ResultObjectVO flushCache(RequestJsonVO requestJsonVO) {
        return messageTypeBusinessService.flushCache(requestJsonVO);
    }
}
