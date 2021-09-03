package com.toucan.shopping.cloud.user.controller;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import com.toucan.shopping.modules.user.constant.UserRegistConstant;
import com.toucan.shopping.modules.user.entity.*;
import com.toucan.shopping.modules.user.page.UserTrueNameApprovePageInfo;
import com.toucan.shopping.modules.user.redis.UserCenterTrueNameApproveKey;
import com.toucan.shopping.modules.user.service.*;
import com.toucan.shopping.modules.user.vo.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 用户实名制审核
 */
@RestController
@RequestMapping("/user/true/name/approve")
public class UserTrueNameApproveController {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private UserService userService;

    @Autowired
    private UserTrueNameApproveService userTrueNameApproveService;

    @Autowired
    private UserTrueNameApproveRecordService userTrueNameApproveRecordService;

    @Autowired
    private UserDetailService userDetailService;


    @Autowired
    private UserMobilePhoneService userMobilePhoneService;

    @Autowired
    private UserUserNameService userUserNameService;

    @Autowired
    private UserEmailService userEmailService;

    @Autowired
    private Toucan toucan;

    @Autowired
    private SkylarkLock skylarkLock;

    @RequestMapping(value="/save",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO save(@RequestBody RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("请求失败,没有找到请求对象");
            return resultObjectVO;
        }
        if (StringUtils.isEmpty(requestJsonVO.getAppCode())) {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("请求失败,没有找到应用编码");
            return resultObjectVO;
        }
        UserTrueNameApprove userTrueNameApprove = JSONObject.parseObject(requestJsonVO.getEntityJson(),UserTrueNameApprove.class);
        if(StringUtils.isEmpty(userTrueNameApprove.getTrueName()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请求失败,真实姓名不能为空");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(userTrueNameApprove.getIdCard()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请求失败,证件号码不能为空");
            return resultObjectVO;
        }
        if(userTrueNameApprove.getIdcardType()==null)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请求失败,证件类型不能为空");
            return resultObjectVO;
        }
        if(userTrueNameApprove.getUserMainId()==null)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请求失败,用户ID不能为空");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(userTrueNameApprove.getIdcardImg1()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请求失败,证件正面照片不能为空");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(userTrueNameApprove.getIdcardImg2()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请求失败,证件背面照片不能为空");
            return resultObjectVO;
        }
        String userMainId = String.valueOf(userTrueNameApprove.getUserMainId());
        try {
            boolean lockStatus = skylarkLock.lock(UserCenterTrueNameApproveKey.getSaveApproveLockKeyForService(userMainId), userMainId);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求失败,请稍候重试");
                return resultObjectVO;
            }
            //查询是否存在审核中
            UserTrueNameApprove queryUserTrueNameApprove = new UserTrueNameApprove();
            queryUserTrueNameApprove.setUserMainId(userTrueNameApprove.getUserMainId());
            queryUserTrueNameApprove.setApproveStatus(userTrueNameApprove.getApproveStatus());
            List<UserTrueNameApprove> userTrueNameApproves = userTrueNameApproveService.findListByEntity(queryUserTrueNameApprove);
            if(CollectionUtils.isNotEmpty(userTrueNameApproves))
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("保存失败,实名认证正在审核中");
                return resultObjectVO;
            }

            userTrueNameApprove.setId(idGenerator.id());
            userTrueNameApprove.setDeleteStatus((short)0);
            int ret = userTrueNameApproveService.save(userTrueNameApprove);
            if(ret<=0)
            {
                logger.warn("保存用户实名审核记录失败 requestJson{} id{}",requestJsonVO.getEntityJson(),userTrueNameApprove.getId());
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,请稍后重试");
            }
            resultObjectVO.setData(userTrueNameApprove);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }finally{
            skylarkLock.unLock(UserCenterTrueNameApproveKey.getSaveApproveLockKeyForService(userMainId), userMainId);
        }
        return resultObjectVO;
    }


    @RequestMapping(value="/update",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO update(@RequestBody RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("请求失败,没有找到请求对象");
            return resultObjectVO;
        }
        if (StringUtils.isEmpty(requestJsonVO.getAppCode())) {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("请求失败,没有找到应用编码");
            return resultObjectVO;
        }
        UserTrueNameApprove userTrueNameApprove = JSONObject.parseObject(requestJsonVO.getEntityJson(),UserTrueNameApprove.class);
        if(userTrueNameApprove.getId()==null)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请求失败,ID为空");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(userTrueNameApprove.getTrueName()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请求失败,真实姓名不能为空");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(userTrueNameApprove.getIdCard()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请求失败,证件号码不能为空");
            return resultObjectVO;
        }
        if(userTrueNameApprove.getIdcardType()==null)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请求失败,证件类型不能为空");
            return resultObjectVO;
        }
        if(userTrueNameApprove.getUserMainId()==null)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请求失败,用户ID不能为空");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(userTrueNameApprove.getIdcardImg1()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请求失败,证件正面照片不能为空");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(userTrueNameApprove.getIdcardImg2()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请求失败,证件背面照片不能为空");
            return resultObjectVO;
        }

        String userMainId = String.valueOf(userTrueNameApprove.getUserMainId());
        try {
            boolean lockStatus = skylarkLock.lock(UserCenterTrueNameApproveKey.getUpdateApproveLockKeyForService(userMainId), userMainId);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求失败,请稍候重试");
                return resultObjectVO;
            }
            //查询是否存在审核中
            UserTrueNameApprove queryUserTrueNameApprove = new UserTrueNameApprove();
            queryUserTrueNameApprove.setUserMainId(userTrueNameApprove.getUserMainId());
            List<UserTrueNameApprove> userTrueNameApproves = userTrueNameApproveService.findListByEntity(queryUserTrueNameApprove);
            if(CollectionUtils.isNotEmpty(userTrueNameApproves))
            {
                for(int i=0;i<userTrueNameApproves.size();i++) {
                    if(userTrueNameApproves.get(i).getApproveStatus().intValue()==1) {
                        resultObjectVO.setCode(ResultObjectVO.FAILD);
                        resultObjectVO.setMsg("请求失败,实名认证正在审核中");
                        return resultObjectVO;
                    }else if(userTrueNameApproves.get(i).getApproveStatus().intValue()==2) {
                        resultObjectVO.setCode(ResultObjectVO.FAILD);
                        resultObjectVO.setMsg("请求失败,实名认证已完成");
                        return resultObjectVO;
                    }
                }
            }

            userTrueNameApprove.setDeleteStatus((short)0);
            int ret = userTrueNameApproveService.update(userTrueNameApprove);
            if(ret<=0)
            {
                logger.warn("保存用户实名审核记录失败 requestJson{} id{}",requestJsonVO.getEntityJson(),userTrueNameApprove.getId());
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,请稍后重试");
            }
            resultObjectVO.setData(userTrueNameApprove);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }finally{
            skylarkLock.unLock(UserCenterTrueNameApproveKey.getUpdateApproveLockKeyForService(userMainId), userMainId);
        }
        return resultObjectVO;
    }


    /**
     * 查询列表页
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/list/page",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,没有找到实体对象");
            return resultObjectVO;
        }

        try {
            UserTrueNameApprovePageInfo queryPageInfo = JSONObject.parseObject(requestVo.getEntityJson(), UserTrueNameApprovePageInfo.class);

            if(StringUtils.isEmpty(requestVo.getAppCode()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到应用编码");
                return resultObjectVO;
            }

            //查询列表页
            resultObjectVO.setData(userTrueNameApproveService.queryListPage(queryPageInfo));

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }



    @RequestMapping(value="/queryByUserMainId",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryByUserMainId(@RequestBody RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("请求失败,没有找到请求对象");
            return resultObjectVO;
        }
        UserTrueNameApprove userTrueNameApprove = JSONObject.parseObject(requestJsonVO.getEntityJson(),UserTrueNameApprove.class);
        if(userTrueNameApprove.getUserMainId()==null)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请求失败,用户ID不能为空");
            return resultObjectVO;
        }
        try {
            UserTrueNameApprove queryUserTrueNameApprove = new UserTrueNameApprove();
            queryUserTrueNameApprove.setUserMainId(userTrueNameApprove.getUserMainId());
            if(userTrueNameApprove.getApproveStatus()!=null) {
                queryUserTrueNameApprove.setApproveStatus(userTrueNameApprove.getApproveStatus());
            }
            List<UserTrueNameApprove> userTrueNameApproves = userTrueNameApproveService.findListByEntity(queryUserTrueNameApprove);
            resultObjectVO.setData(userTrueNameApproves);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }






    /**
     * 通过指定
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/pass/id",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO passById(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,没有找到实体对象");
            return resultObjectVO;
        }

        try {
            UserTrueNameApproveVO userTrueNameApproveVO = JSONObject.parseObject(requestVo.getEntityJson(),UserTrueNameApproveVO.class);
            if(userTrueNameApproveVO.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,没有找到ID");
                return resultObjectVO;
            }

            UserTrueNameApprove queryUserTrueNameApprove= new UserTrueNameApprove();
            queryUserTrueNameApprove.setId(userTrueNameApproveVO.getId());
            List<UserTrueNameApprove> userTrueNameApproves = userTrueNameApproveService.findListByEntity(queryUserTrueNameApprove);

            if(CollectionUtils.isNotEmpty(userTrueNameApproves))
            {
                UserTrueNameApprove userTrueNameApprove =  userTrueNameApproves.get(0);
                userTrueNameApprove.setApproveStatus(2); //设置审核通过
                int ret = userTrueNameApproveService.update(userTrueNameApprove);
                if(ret>0) {

                    if (ret <= 0) {
                        logger.warn("实名审核失败 {} ", JSONObject.toJSONString(userTrueNameApproves));
                        resultObjectVO.setCode(ResultVO.FAILD);
                        resultObjectVO.setMsg("请求失败,请稍后重试");
                        return resultObjectVO;
                    }


                    //保存审核记录
                    UserTrueNameApproveRecord userTrueNameApproveRecord = new UserTrueNameApproveRecord();
                    userTrueNameApproveRecord.setId(idGenerator.id());
                    userTrueNameApproveRecord.setApproveId(userTrueNameApprove.getId()); //审核主表ID
                    userTrueNameApproveRecord.setUserMainId(userTrueNameApprove.getUserMainId());
                    userTrueNameApproveRecord.setApproveStatus(1);
                    userTrueNameApproveRecord.setCreateAdminId(userTrueNameApproveVO.getApproveAdminId()); //审核管理员
                    userTrueNameApproveRecord.setCreateDate(new Date());
                    userTrueNameApproveRecord.setDeleteStatus((short)0);
                    userTrueNameApproveRecordService.save(userTrueNameApproveRecord);

                    //设置姓名和身份证号码
                    List<UserDetail> userDetails = userDetailService.findByUserMainId(userTrueNameApprove.getUserMainId());
                    if(CollectionUtils.isNotEmpty(userDetails))
                    {
                        UserDetail userDetail = userDetails.get(0);
                        userDetail.setTrueName(userTrueNameApprove.getTrueName());
                        userDetail.setIdCard(userTrueNameApprove.getIdCard());
                        userDetail.setIdcardType(userTrueNameApprove.getIdcardType());
                        userDetail.setIdcardImg1(userTrueNameApprove.getIdcardImg1());
                        userDetail.setIdcardImg2(userTrueNameApprove.getIdcardImg2());
                        userDetail.setTrueNameStatus(1); //已实名
                        ret = userDetailService.update(userDetail);
                        if (ret <= 0) {
                            logger.warn("修改姓名和身份证失败 {} ", JSONObject.toJSONString(userDetail));
                            resultObjectVO.setCode(ResultVO.FAILD);
                            resultObjectVO.setMsg("请求失败,请稍后重试");
                            return resultObjectVO;
                        }
                    }

                    resultObjectVO.setData(userTrueNameApproves);
                    return resultObjectVO;
                }
            }

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
            return resultObjectVO;
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 批量删除
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/ids",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteByIds(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,没有找到实体对象");
            return resultObjectVO;
        }

        try {
            List<UserTrueNameApprove> userTrueNameApproves = JSONObject.parseArray(requestVo.getEntityJson(),UserTrueNameApprove.class);
            if(CollectionUtils.isEmpty(userTrueNameApproves))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,没有找到ID");
                return resultObjectVO;
            }
            List<ResultObjectVO> resultObjectVOList = new ArrayList<ResultObjectVO>();
            for(UserTrueNameApprove userTrueNameApprove:userTrueNameApproves) {
                if(userTrueNameApprove.getId()!=null) {
                    ResultObjectVO appResultObjectVO = new ResultObjectVO();
                    appResultObjectVO.setData(userTrueNameApprove);

                    int row = userTrueNameApproveService.deleteById(userTrueNameApprove.getId());
                    if (row < 1) {
                        logger.warn("删除失败，id:{}",userTrueNameApprove.getId());
                        resultObjectVO.setCode(ResultVO.FAILD);
                        resultObjectVO.setMsg("请求失败,请重试!");
                        continue;
                    }

                }
            }
            resultObjectVO.setData(resultObjectVOList);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }



}
