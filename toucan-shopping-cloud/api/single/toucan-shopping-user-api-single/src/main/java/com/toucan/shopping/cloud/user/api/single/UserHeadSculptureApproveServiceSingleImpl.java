package com.toucan.shopping.cloud.user.api.single;

import com.toucan.shopping.cloud.user.api.feign.service.FeignUserHeadSculptureApproveService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.user.business.service.UserHeadSculptureApproveBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserHeadSculptureApproveServiceSingleImpl implements FeignUserHeadSculptureApproveService {

    @Autowired
    private UserHeadSculptureApproveBusinessService userHeadSculptureApproveBusinessService;

    @Override
    public ResultObjectVO save(String signHeader, RequestJsonVO requestJsonVO) {
        return userHeadSculptureApproveBusinessService.save(requestJsonVO);
    }

    @Override
    public ResultObjectVO update(String signHeader, RequestJsonVO requestJsonVO) {
        return userHeadSculptureApproveBusinessService.update(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryByUserMainId(String signHeader, RequestJsonVO requestJsonVO) {
        return userHeadSculptureApproveBusinessService.queryByUserMainId(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryAliveByUserMainId(String signHeader, RequestJsonVO requestJsonVO) {
        return userHeadSculptureApproveBusinessService.queryAliveByUserMainId(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryListByUserMainIdAndOrderByUpdateDateDesc(String signHeader, RequestJsonVO requestJsonVO) {
        return userHeadSculptureApproveBusinessService.queryListByUserMainIdAndOrderByUpdateDateDesc(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryListPage(String signHeader, RequestJsonVO requestVo) {
        return userHeadSculptureApproveBusinessService.queryListPage(requestVo);
    }

    @Override
    public ResultObjectVO passById(String signHeader, RequestJsonVO requestVo) {
        return userHeadSculptureApproveBusinessService.passById(requestVo);
    }

    @Override
    public ResultObjectVO deleteByIds(String signHeader, RequestJsonVO requestVo) {
        return userHeadSculptureApproveBusinessService.deleteByIds(requestVo);
    }

    @Override
    public ResultObjectVO queryById(String signHeader, RequestJsonVO requestJsonVO) {
        return userHeadSculptureApproveBusinessService.queryById(requestJsonVO);
    }

    @Override
    public ResultObjectVO rejectById(String signHeader, RequestJsonVO requestVo) {
        return userHeadSculptureApproveBusinessService.rejectById(requestVo);
    }
}
