package com.toucan.shopping.cloud.user.api.single;

import com.toucan.shopping.cloud.user.api.feign.service.FeignUserTrueNameApproveService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.user.business.service.UserTrueNameApproveBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserTrueNameApproveServiceSingleImpl implements FeignUserTrueNameApproveService {

    @Autowired
    private UserTrueNameApproveBusinessService userTrueNameApproveBusinessService;

    @Override
    public ResultObjectVO save(String signHeader, RequestJsonVO requestJsonVO) {
        return userTrueNameApproveBusinessService.save(requestJsonVO);
    }

    @Override
    public ResultObjectVO update(String signHeader, RequestJsonVO requestJsonVO) {
        return userTrueNameApproveBusinessService.update(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryByUserMainId(String signHeader, RequestJsonVO requestJsonVO) {
        return userTrueNameApproveBusinessService.queryByUserMainId(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryListByUserMainIdAndOrderByUpdateDateDesc(String signHeader, RequestJsonVO requestJsonVO) {
        return userTrueNameApproveBusinessService.queryListByUserMainIdAndOrderByUpdateDateDesc(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryListPage(String signHeader, RequestJsonVO requestVo) {
        return userTrueNameApproveBusinessService.queryListPage(requestVo);
    }

    @Override
    public ResultObjectVO passById(String signHeader, RequestJsonVO requestVo) {
        return userTrueNameApproveBusinessService.passById(requestVo);
    }

    @Override
    public ResultObjectVO deleteByIds(String signHeader, RequestJsonVO requestVo) {
        return userTrueNameApproveBusinessService.deleteByIds(requestVo);
    }

    @Override
    public ResultObjectVO queryById(String signHeader, RequestJsonVO requestJsonVO) {
        return userTrueNameApproveBusinessService.queryById(requestJsonVO);
    }

    @Override
    public ResultObjectVO rejectById(String signHeader, RequestJsonVO requestVo) {
        return userTrueNameApproveBusinessService.rejectById(requestVo);
    }
}
