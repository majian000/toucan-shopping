package com.toucan.shopping.cloud.user.api.single;

import com.toucan.shopping.cloud.user.api.UserHeadSculptureApproveServiceAPI;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.user.business.service.UserHeadSculptureApproveBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserHeadSculptureApproveServiceSingleImpl implements UserHeadSculptureApproveServiceAPI {

    @Autowired
    private UserHeadSculptureApproveBusinessService userHeadSculptureApproveBusinessService;

    @Override
    public ResultObjectVO save( RequestJsonVO requestJsonVO) {
        return userHeadSculptureApproveBusinessService.save(requestJsonVO);
    }

    @Override
    public ResultObjectVO update( RequestJsonVO requestJsonVO) {
        return userHeadSculptureApproveBusinessService.update(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryByUserMainId( RequestJsonVO requestJsonVO) {
        return userHeadSculptureApproveBusinessService.queryByUserMainId(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryAliveByUserMainId( RequestJsonVO requestJsonVO) {
        return userHeadSculptureApproveBusinessService.queryAliveByUserMainId(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryListByUserMainIdAndOrderByUpdateDateDesc( RequestJsonVO requestJsonVO) {
        return userHeadSculptureApproveBusinessService.queryListByUserMainIdAndOrderByUpdateDateDesc(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryListPage( RequestJsonVO requestVo) {
        return userHeadSculptureApproveBusinessService.queryListPage(requestVo);
    }

    @Override
    public ResultObjectVO passById( RequestJsonVO requestVo) {
        return userHeadSculptureApproveBusinessService.passById(requestVo);
    }

    @Override
    public ResultObjectVO deleteByIds( RequestJsonVO requestVo) {
        return userHeadSculptureApproveBusinessService.deleteByIds(requestVo);
    }

    @Override
    public ResultObjectVO queryById( RequestJsonVO requestJsonVO) {
        return userHeadSculptureApproveBusinessService.queryById(requestJsonVO);
    }

    @Override
    public ResultObjectVO rejectById( RequestJsonVO requestVo) {
        return userHeadSculptureApproveBusinessService.rejectById(requestVo);
    }
}
