package com.toucan.shopping.cloud.user.api.single;

import com.toucan.shopping.cloud.user.api.feign.service.FeignUserService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.user.business.service.UserBusinessService;
import com.toucan.shopping.modules.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceSingleImpl implements FeignUserService {

    @Autowired
    private UserBusinessService userBusinessService;

    @Override
    public ResultObjectVO registByMobilePhone(String signHeader, RequestJsonVO requestJsonVO) {
        return userBusinessService.registByMobilePhone(requestJsonVO);
    }

    @Override
    public ResultObjectVO resetPassword(String signHeader, RequestJsonVO requestJsonVO) {
        return userBusinessService.resetPassword(requestJsonVO);
    }

    @Override
    public ResultObjectVO connectUsername(String signHeader, RequestJsonVO requestJsonVO) {
        return userBusinessService.connectUsername(requestJsonVO);
    }

    @Override
    public ResultObjectVO connectEmail(String signHeader, RequestJsonVO requestJsonVO) {
        return userBusinessService.connectEmail(requestJsonVO);
    }

    @Override
    public ResultObjectVO connectMobilePhone(String signHeader, RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        resultObjectVO.setCode(ResultObjectVO.FAILD);
        resultObjectVO.setMsg("单机模式暂不支持此服务");
        return resultObjectVO;
    }

    @Override
    public ResultObjectVO updateDetail(String signHeader, RequestJsonVO requestJsonVO) {
        return userBusinessService.updateDetail(requestJsonVO);
    }

    @Override
    public ResultObjectVO registByUsername(String signHeader, RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        resultObjectVO.setCode(ResultObjectVO.FAILD);
        resultObjectVO.setMsg("单机模式暂不支持此服务");
        return resultObjectVO;
    }

    @Override
    public ResultObjectVO loginByPassword(String signHeader, RequestJsonVO requestJsonVO) {
        return userBusinessService.loginByPassword(requestJsonVO);
    }

    @Override
    public ResultObjectVO logout(String signHeader, RequestJsonVO requestVo) {
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
    public ResultObjectVO isOnline(String signHeader, RequestJsonVO requestVo) {
        return userBusinessService.isOnline(requestVo);
    }

    @Override
    public ResultObjectVO verifyLoginToken(String signHeader, RequestJsonVO requestVo) {
        return userBusinessService.verifyLoginToken(requestVo);
    }

    @Override
    public ResultObjectVO verifyLoginTokenAndIsOnline(String signHeader, RequestJsonVO requestVo) {
        return userBusinessService.verifyLoginTokenAndIsOnline(requestVo);
    }

    @Override
    public ResultObjectVO queryLoginInfo(String signHeader, RequestJsonVO requestVo) {
        return userBusinessService.queryLoginInfo(requestVo);
    }

    @Override
    public ResultObjectVO findByUserMainIdForCacheOrDB(String signHeader, RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        resultObjectVO.setCode(ResultObjectVO.FAILD);
        resultObjectVO.setMsg("单机模式暂不支持此服务");
        return resultObjectVO;
    }

    @Override
    public ResultObjectVO verifyRealName(String signHeader, RequestJsonVO requestVo) {
        return userBusinessService.verifyRealName(requestVo);
    }

    @Override
    public ResultObjectVO findByMobilePhone(String signHeader, RequestJsonVO requestJsonVO) {
        return userBusinessService.findByMobilePhone(requestJsonVO);
    }

    @Override
    public ResultObjectVO list(String signHeader, RequestJsonVO requestVo) {
        return userBusinessService.list(requestVo);
    }

    @Override
    public ResultObjectVO mobilePhoneList(String signHeader, RequestJsonVO requestVo) {
        return userBusinessService.mobilePhoneList(requestVo);
    }

    @Override
    public ResultObjectVO emailList(String signHeader, RequestJsonVO requestVo) {
        return userBusinessService.emailList(requestVo);
    }

    @Override
    public ResultObjectVO usernameList(String signHeader, RequestJsonVO requestVo) {
        return userBusinessService.usernameList(requestVo);
    }

    @Override
    public ResultObjectVO findUsernameListByUsername(String signHeader, RequestJsonVO requestJsonVO) {
        return userBusinessService.findUsernameListByUsername(requestJsonVO);
    }

    @Override
    public ResultObjectVO findEmailListByEmail(String signHeader, RequestJsonVO requestJsonVO) {
        return userBusinessService.findEmailListByEmail(requestJsonVO);
    }

    @Override
    public ResultObjectVO disabledEnabledById(String signHeader, RequestJsonVO requestVo) {
        return userBusinessService.disabledEnabledById(requestVo);
    }

    @Override
    public ResultObjectVO disabledEnabledMobilePhone(String signHeader, RequestJsonVO requestVo) {
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
    public ResultObjectVO disabledEnabledEmail(String signHeader, RequestJsonVO requestVo) {
        return userBusinessService.disabledEnabledEmail(requestVo);
    }

    @Override
    public ResultObjectVO disabledEnabledUsernameByUserMainIdAndUsername(String signHeader, RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        resultObjectVO.setCode(ResultObjectVO.FAILD);
        resultObjectVO.setMsg("单机模式暂不支持此服务");
        return resultObjectVO;
    }

    @Override
    public ResultObjectVO disabledByIds(String signHeader, RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        resultObjectVO.setCode(ResultObjectVO.FAILD);
        resultObjectVO.setMsg("单机模式暂不支持此服务");
        return resultObjectVO;
    }

    @Override
    public ResultObjectVO findByUserMainId(String signHeader, RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        resultObjectVO.setCode(ResultObjectVO.FAILD);
        resultObjectVO.setMsg("单机模式暂不支持此服务");
        return resultObjectVO;
    }

    @Override
    public ResultObjectVO flushCache(String signHeader, RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        resultObjectVO.setCode(ResultObjectVO.FAILD);
        resultObjectVO.setMsg("单机模式暂不支持此服务");
        return resultObjectVO;
    }

    @Override
    public ResultObjectVO editInfo(String signHeader, RequestJsonVO requestJsonVO) {
        return userBusinessService.editInfo(requestJsonVO);
    }

    @Override
    public ResultObjectVO updateIsShop(String signHeader, RequestJsonVO requestJsonVO) {
        return userBusinessService.updateIsShop(requestJsonVO);
    }

    @Override
    public ResultObjectVO updateHeadsculpture(String signHeader, RequestJsonVO requestJsonVO) {
        return userBusinessService.updateHeadsculpture(requestJsonVO);
    }

    @Override
    public ResultObjectVO findByUsername(RequestJsonVO requestJsonVO) {
        return userBusinessService.findByUsername(requestJsonVO);
    }
}
