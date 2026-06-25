package com.toucan.shopping.cloud.user.api.single;

import com.toucan.shopping.cloud.user.api.UserTrueNameApproveServiceAPI;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.user.business.service.UserTrueNameApproveBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserTrueNameApproveServiceSingleImpl implements UserTrueNameApproveServiceAPI {

    @Autowired
    private UserTrueNameApproveBusinessService userTrueNameApproveBusinessService;

    @Override
    public ResultObjectVO save(RequestJsonVO requestJsonVO) {
        return userTrueNameApproveBusinessService.save(requestJsonVO);
    }

    @Override
    public ResultObjectVO update(RequestJsonVO requestJsonVO) {
        return userTrueNameApproveBusinessService.update(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryByUserMainId(RequestJsonVO requestJsonVO) {
        return userTrueNameApproveBusinessService.queryByUserMainId(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryListByUserMainIdAndOrderByUpdateDateDesc(RequestJsonVO requestJsonVO) {
        return userTrueNameApproveBusinessService.queryListByUserMainIdAndOrderByUpdateDateDesc(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryListPage(RequestJsonVO requestVo) {
        return userTrueNameApproveBusinessService.queryListPage(requestVo);
    }

    @Override
    public ResultObjectVO passById(RequestJsonVO requestVo) {
        return userTrueNameApproveBusinessService.passById(requestVo);
    }

    @Override
    public ResultObjectVO deleteByIds(RequestJsonVO requestVo) {
        return userTrueNameApproveBusinessService.deleteByIds(requestVo);
    }

    @Override
    public ResultObjectVO queryById(RequestJsonVO requestJsonVO) {
        return userTrueNameApproveBusinessService.queryById(requestJsonVO);
    }

    @Override
    public ResultObjectVO rejectById(RequestJsonVO requestVo) {
        return userTrueNameApproveBusinessService.rejectById(requestVo);
    }
}
