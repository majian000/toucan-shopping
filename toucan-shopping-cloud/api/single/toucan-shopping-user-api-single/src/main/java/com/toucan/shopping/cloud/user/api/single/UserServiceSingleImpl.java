package com.toucan.shopping.cloud.user.api.single;

import com.toucan.shopping.cloud.user.api.UserServiceAPI;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.user.business.service.UserBusinessService;
import com.toucan.shopping.modules.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceSingleImpl implements UserServiceAPI {

    @Autowired
    private UserBusinessService userBusinessService;

    @Override
    public ResultObjectVO registByMobilePhone(RequestJsonVO requestJsonVO) {
        return userBusinessService.registByMobilePhone(requestJsonVO);
    }

    @Override
    public ResultObjectVO resetPassword(RequestJsonVO requestJsonVO) {
        return userBusinessService.resetPassword(requestJsonVO);
    }

    @Override
    public ResultObjectVO connectUsername(RequestJsonVO requestJsonVO) {
        return userBusinessService.connectUsername(requestJsonVO);
    }

    @Override
    public ResultObjectVO connectEmail(RequestJsonVO requestJsonVO) {
        return userBusinessService.connectEmail(requestJsonVO);
    }

    @Override
    public ResultObjectVO connectMobilePhone(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        resultObjectVO.setCode(ResultObjectVO.FAILD);
        resultObjectVO.setMsg("单机模式暂不支持此服务");
        return resultObjectVO;
    }

    @Override
    public ResultObjectVO updateDetail(RequestJsonVO requestJsonVO) {
        return userBusinessService.updateDetail(requestJsonVO);
    }

    @Override
    public ResultObjectVO registByUsername(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        resultObjectVO.setCode(ResultObjectVO.FAILD);
        resultObjectVO.setMsg("单机模式暂不支持此服务");
        return resultObjectVO;
    }

    @Override
    public ResultObjectVO loginByPassword(RequestJsonVO requestJsonVO) {
        return userBusinessService.loginByPassword(requestJsonVO);
    }

    @Override
    public ResultObjectVO logout(RequestJsonVO requestVo) {
        return userBusinessService.logout(requestVo);
    }

    @Override
    public ResultObjectVO loginByVCode(User user) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        resultObjectVO.setCode(ResultObjectVO.FAILD);
        resultObjectVO.setMsg("单机模式暂不支持此服务");
        return resultObjectVO;
    }

    @Override
    public ResultObjectVO isOnline(RequestJsonVO requestVo) {
        return userBusinessService.isOnline(requestVo);
    }

    @Override
    public ResultObjectVO verifyLoginToken(RequestJsonVO requestVo) {
        return userBusinessService.verifyLoginToken(requestVo);
    }

    @Override
    public ResultObjectVO verifyLoginTokenAndIsOnline(RequestJsonVO requestVo) {
        return userBusinessService.verifyLoginTokenAndIsOnline(requestVo);
    }

    @Override
    public ResultObjectVO queryLoginInfo(RequestJsonVO requestVo) {
        return userBusinessService.queryLoginInfo(requestVo);
    }

    @Override
    public ResultObjectVO findByUserMainIdForCacheOrDB(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        resultObjectVO.setCode(ResultObjectVO.FAILD);
        resultObjectVO.setMsg("单机模式暂不支持此服务");
        return resultObjectVO;
    }

    @Override
    public ResultObjectVO verifyRealName(RequestJsonVO requestVo) {
        return userBusinessService.verifyRealName(requestVo);
    }

    @Override
    public ResultObjectVO findByMobilePhone(RequestJsonVO requestJsonVO) {
        return userBusinessService.findByMobilePhone(requestJsonVO);
    }

    @Override
    public ResultObjectVO list(RequestJsonVO requestVo) {
        return userBusinessService.list(requestVo);
    }

    @Override
    public ResultObjectVO mobilePhoneList(RequestJsonVO requestVo) {
        return userBusinessService.mobilePhoneList(requestVo);
    }

    @Override
    public ResultObjectVO emailList(RequestJsonVO requestVo) {
        return userBusinessService.emailList(requestVo);
    }

    @Override
    public ResultObjectVO usernameList(RequestJsonVO requestVo) {
        return userBusinessService.usernameList(requestVo);
    }

    @Override
    public ResultObjectVO findUsernameListByUsername(RequestJsonVO requestJsonVO) {
        return userBusinessService.findUsernameListByUsername(requestJsonVO);
    }

    @Override
    public ResultObjectVO findEmailListByEmail(RequestJsonVO requestJsonVO) {
        return userBusinessService.findEmailListByEmail(requestJsonVO);
    }

    @Override
    public ResultObjectVO disabledEnabledById(RequestJsonVO requestVo) {
        return userBusinessService.disabledEnabledById(requestVo);
    }

    @Override
    public ResultObjectVO disabledEnabledMobilePhone(RequestJsonVO requestVo) {
        return userBusinessService.disabledEnabledMobilePhone(requestVo);
    }

    @Override
    public ResultObjectVO updateConnectEmail(RequestJsonVO requestJsonVO) {
        return userBusinessService.updateConnectEmail(requestJsonVO);
    }

    @Override
    public ResultObjectVO updateConnectMobilePhone(RequestJsonVO requestJsonVO) {
        return userBusinessService.updateConnectMobilePhone(requestJsonVO);
    }

    @Override
    public ResultObjectVO disabledEnabledEmail(RequestJsonVO requestVo) {
        return userBusinessService.disabledEnabledEmail(requestVo);
    }

    @Override
    public ResultObjectVO disabledEnabledUsernameByUserMainIdAndUsername(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        resultObjectVO.setCode(ResultObjectVO.FAILD);
        resultObjectVO.setMsg("单机模式暂不支持此服务");
        return resultObjectVO;
    }

    @Override
    public ResultObjectVO disabledByIds(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        resultObjectVO.setCode(ResultObjectVO.FAILD);
        resultObjectVO.setMsg("单机模式暂不支持此服务");
        return resultObjectVO;
    }

    @Override
    public ResultObjectVO findByUserMainId(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        resultObjectVO.setCode(ResultObjectVO.FAILD);
        resultObjectVO.setMsg("单机模式暂不支持此服务");
        return resultObjectVO;
    }

    @Override
    public ResultObjectVO flushCache(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        resultObjectVO.setCode(ResultObjectVO.FAILD);
        resultObjectVO.setMsg("单机模式暂不支持此服务");
        return resultObjectVO;
    }

    @Override
    public ResultObjectVO editInfo(RequestJsonVO requestJsonVO) {
        return userBusinessService.editInfo(requestJsonVO);
    }

    @Override
    public ResultObjectVO updateIsShop(RequestJsonVO requestJsonVO) {
        return userBusinessService.updateIsShop(requestJsonVO);
    }

    @Override
    public ResultObjectVO updateHeadsculpture(RequestJsonVO requestJsonVO) {
        return userBusinessService.updateHeadsculpture(requestJsonVO);
    }

    @Override
    public ResultObjectVO findByUsername(RequestJsonVO requestJsonVO) {
        return userBusinessService.findByUsername(requestJsonVO);
    }
}
