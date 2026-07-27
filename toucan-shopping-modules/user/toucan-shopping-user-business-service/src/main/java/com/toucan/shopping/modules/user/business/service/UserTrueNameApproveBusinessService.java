package com.toucan.shopping.modules.user.business.service;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.annotation.RequestCheck;
import com.toucan.shopping.modules.common.exception.BusinessValidationException;
import com.toucan.shopping.modules.user.constant.AppCodeEnum;
import com.toucan.shopping.modules.user.service.UserRedisService;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.Check;
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
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 用户实名制审核
 */
@Service
public class UserTrueNameApproveBusinessService {


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
    private UserRedisService userRedisService;

    @Autowired
    private Toucan toucan;

    @Autowired
    private SkylarkLock skylarkLock;

    @RequestCheck(requireEntity = true)
    public ResultObjectVO save(RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        UserTrueNameApprove userTrueNameApprove = JSONObject.parseObject(requestJsonVO.getEntityJson(),UserTrueNameApprove.class);
        Check.notEmpty(userTrueNameApprove.getTrueName(), ResultObjectVO.FAILD, "真实姓名不能为空");
        Check.notEmpty(userTrueNameApprove.getIdCard(), ResultObjectVO.FAILD, "证件号码不能为空");
        Check.notNull(userTrueNameApprove.getIdcardType(), ResultObjectVO.FAILD, "证件类型不能为空");
        Check.notNull(userTrueNameApprove.getUserMainId(), ResultObjectVO.FAILD, "用户ID不能为空");
        Check.notEmpty(userTrueNameApprove.getIdcardImg1(), ResultObjectVO.FAILD, "证件正面照片不能为空");
        Check.notEmpty(userTrueNameApprove.getIdcardImg2(), ResultObjectVO.FAILD, "证件背面照片不能为空");
        String userMainId = String.valueOf(userTrueNameApprove.getUserMainId());
        try {
            boolean lockStatus = skylarkLock.lock(UserCenterTrueNameApproveKey.getSaveApproveLockKeyForService(userMainId), userMainId);
            if (!lockStatus) {
                return ResultObjectVO.fail(ResultObjectVO.FAILD, "请稍后重试");
            }
            //查询是否存在审核中
            UserTrueNameApprove queryUserTrueNameApprove = new UserTrueNameApprove();
            queryUserTrueNameApprove.setUserMainId(userTrueNameApprove.getUserMainId());
            queryUserTrueNameApprove.setApproveStatus(userTrueNameApprove.getApproveStatus());
            List<UserTrueNameApprove> userTrueNameApproves = userTrueNameApproveService.findListByEntity(queryUserTrueNameApprove);
            if(CollectionUtils.isNotEmpty(userTrueNameApproves))
            {
                return ResultObjectVO.fail(ResultObjectVO.FAILD, "实名认证正在审核中");
            }

            userTrueNameApprove.setId(idGenerator.id());
            userTrueNameApprove.setDeleteStatus((short)0);
            int ret = userTrueNameApproveService.save(userTrueNameApprove);
            if(ret<=0)
            {
                logger.warn("保存用户实名审核记录失败 requestJson{} id{}",requestJsonVO.getEntityJson(),userTrueNameApprove.getId());
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
            }
            resultObjectVO.setData(userTrueNameApprove);
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            return ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        }finally{
            skylarkLock.unLock(UserCenterTrueNameApproveKey.getSaveApproveLockKeyForService(userMainId), userMainId);
        }
        return resultObjectVO;
    }


    @RequestCheck(requireEntity = true)
    public ResultObjectVO update(RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        UserTrueNameApprove userTrueNameApprove = JSONObject.parseObject(requestJsonVO.getEntityJson(),UserTrueNameApprove.class);
        Check.notNull(userTrueNameApprove.getId(), ResultObjectVO.FAILD, "ID为空");
        Check.notEmpty(userTrueNameApprove.getTrueName(), ResultObjectVO.FAILD, "真实姓名不能为空");
        Check.notEmpty(userTrueNameApprove.getIdCard(), ResultObjectVO.FAILD, "证件号码不能为空");
        Check.notNull(userTrueNameApprove.getIdcardType(), ResultObjectVO.FAILD, "证件类型不能为空");
        Check.notNull(userTrueNameApprove.getUserMainId(), ResultObjectVO.FAILD, "用户ID不能为空");
        Check.notEmpty(userTrueNameApprove.getIdcardImg1(), ResultObjectVO.FAILD, "证件正面照片不能为空");
        Check.notEmpty(userTrueNameApprove.getIdcardImg2(), ResultObjectVO.FAILD, "证件背面照片不能为空");

        String userMainId = String.valueOf(userTrueNameApprove.getUserMainId());
        try {
            boolean lockStatus = skylarkLock.lock(UserCenterTrueNameApproveKey.getUpdateApproveLockKeyForService(userMainId), userMainId);
            if (!lockStatus) {
                return ResultObjectVO.fail(ResultObjectVO.FAILD, "请稍后重试");
            }

            userTrueNameApprove.setDeleteStatus((short)0);
            int ret = userTrueNameApproveService.update(userTrueNameApprove);
            if(ret<=0)
            {
                logger.warn("保存用户实名审核记录失败 requestJson{} id{}",requestJsonVO.getEntityJson(),userTrueNameApprove.getId());
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
            }
            resultObjectVO.setData(userTrueNameApprove);
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            return ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryListPage(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            UserTrueNameApprovePageInfo queryPageInfo = JSONObject.parseObject(requestVo.getEntityJson(), UserTrueNameApprovePageInfo.class);

            //查询列表页
            resultObjectVO.setData(userTrueNameApproveService.queryListPage(queryPageInfo));

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            return ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        }
        return resultObjectVO;
    }



    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryByUserMainId(RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        UserTrueNameApprove userTrueNameApprove = JSONObject.parseObject(requestJsonVO.getEntityJson(),UserTrueNameApprove.class);
        Check.notNull(userTrueNameApprove.getUserMainId(), ResultObjectVO.FAILD, "用户ID不能为空");
        try {
            UserTrueNameApprove queryUserTrueNameApprove = new UserTrueNameApprove();
            queryUserTrueNameApprove.setUserMainId(userTrueNameApprove.getUserMainId());
            if(userTrueNameApprove.getApproveStatus()!=null) {
                queryUserTrueNameApprove.setApproveStatus(userTrueNameApprove.getApproveStatus());
            }
            List<UserTrueNameApprove> userTrueNameApproves = userTrueNameApproveService.findListByEntity(queryUserTrueNameApprove);
            resultObjectVO.setData(userTrueNameApproves);
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            return ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        }
        return resultObjectVO;
    }


    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryListByUserMainIdAndOrderByUpdateDateDesc(RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        UserTrueNameApprove userTrueNameApprove = JSONObject.parseObject(requestJsonVO.getEntityJson(),UserTrueNameApprove.class);
        Check.notNull(userTrueNameApprove.getUserMainId(), ResultObjectVO.FAILD, "用户ID不能为空");
        try {
            UserTrueNameApprove queryUserTrueNameApprove = new UserTrueNameApprove();
            queryUserTrueNameApprove.setUserMainId(userTrueNameApprove.getUserMainId());
            if(userTrueNameApprove.getApproveStatus()!=null) {
                queryUserTrueNameApprove.setApproveStatus(userTrueNameApprove.getApproveStatus());
            }
            List<UserTrueNameApprove> userTrueNameApproves = userTrueNameApproveService.findListByEntityOrderByUpdateDateDesc(queryUserTrueNameApprove);
            resultObjectVO.setData(userTrueNameApproves);
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            return ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        }
        return resultObjectVO;
    }



    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryById(RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        UserTrueNameApprove userTrueNameApprove = JSONObject.parseObject(requestJsonVO.getEntityJson(),UserTrueNameApprove.class);
        Check.notNull(userTrueNameApprove.getId(), ResultObjectVO.FAILD, "查询失败,ID不能为空");
        try {
            UserTrueNameApprove queryUserTrueNameApprove = new UserTrueNameApprove();
            queryUserTrueNameApprove.setId(userTrueNameApprove.getId());
            List<UserTrueNameApprove> userTrueNameApproves = userTrueNameApproveService.findListByEntity(queryUserTrueNameApprove);
            resultObjectVO.setData(userTrueNameApproves);
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            return ResultObjectVO.fail(ResultVO.FAILD, "查询失败,请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 通过指定
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO passById(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            UserTrueNameApproveVO userTrueNameApproveVO = JSONObject.parseObject(requestVo.getEntityJson(),UserTrueNameApproveVO.class);
            Check.notNull(userTrueNameApproveVO.getId(), ResultVO.FAILD, "操作失败,没有找到ID");

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
                        return ResultObjectVO.fail(ResultVO.FAILD, "操作失败,请稍后重试");
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
                            resultObjectVO.setMsg("操作失败,请稍后重试");

                            //开始回滚数据
                            userTrueNameApprove.setApproveStatus(1); //设置审核中
                            ret = userTrueNameApproveService.update(userTrueNameApprove);
                            if (ret <= 0) {
                                logger.warn("回滚实名审核失败 {} ", JSONObject.toJSONString(userTrueNameApproves));
                                resultObjectVO.setCode(ResultVO.FAILD);
                                resultObjectVO.setMsg("操作失败,请稍后重试");
                            }


                            return resultObjectVO;
                        }



                        try {
                            //更新商城用户缓存
                            userRedisService.flushLoginCache(String.valueOf(userTrueNameApprove.getUserMainId()), AppCodeEnum.SHOPPING_WEB.value());
                        }catch(Exception e)
                        {
                            logger.warn(e.getMessage(),e);
                        }

                    }

                    resultObjectVO.setData(userTrueNameApproves);
                    return resultObjectVO;
                }
            }

            return ResultObjectVO.fail(ResultVO.FAILD, "操作失败,请稍后重试");
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            return ResultObjectVO.fail(ResultVO.FAILD, "操作失败,请稍后重试");
        }
    }






    /**
     * 驳回指定
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO rejectById(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            UserTrueNameApproveVO userTrueNameApproveVO = JSONObject.parseObject(requestVo.getEntityJson(),UserTrueNameApproveVO.class);
            Check.notNull(userTrueNameApproveVO.getId(), ResultVO.FAILD, "操作失败,没有找到ID");

            UserTrueNameApprove queryUserTrueNameApprove= new UserTrueNameApprove();
            queryUserTrueNameApprove.setId(userTrueNameApproveVO.getId());
            List<UserTrueNameApprove> userTrueNameApproves = userTrueNameApproveService.findListByEntity(queryUserTrueNameApprove);

            if(CollectionUtils.isNotEmpty(userTrueNameApproves))
            {
                UserTrueNameApprove userTrueNameApprove =  userTrueNameApproves.get(0);
                userTrueNameApprove.setApproveStatus(3); //设置审核驳回
                userTrueNameApprove.setRejectText(userTrueNameApproveVO.getRejectText()); //驳回原因
                int ret = userTrueNameApproveService.update(userTrueNameApprove);
                if(ret>0) {

                    if (ret <= 0) {
                        logger.warn("实名审核失败 {} ", JSONObject.toJSONString(userTrueNameApproves));
                        return ResultObjectVO.fail(ResultVO.FAILD, "操作失败,请稍后重试");
                    }


                    //保存审核记录
                    UserTrueNameApproveRecord userTrueNameApproveRecord = new UserTrueNameApproveRecord();
                    userTrueNameApproveRecord.setId(idGenerator.id());
                    userTrueNameApproveRecord.setApproveId(userTrueNameApprove.getId()); //审核主表ID
                    userTrueNameApproveRecord.setUserMainId(userTrueNameApprove.getUserMainId());
                    userTrueNameApproveRecord.setApproveStatus(2);
                    userTrueNameApproveRecord.setRejectText(userTrueNameApproveVO.getRejectText()); //驳回原因
                    userTrueNameApproveRecord.setCreateAdminId(userTrueNameApproveVO.getApproveAdminId()); //审核管理员
                    userTrueNameApproveRecord.setCreateDate(new Date());
                    userTrueNameApproveRecord.setDeleteStatus((short)0);
                    userTrueNameApproveRecordService.save(userTrueNameApproveRecord);


                    resultObjectVO.setData(userTrueNameApproves);
                    return resultObjectVO;
                }
            }

            return ResultObjectVO.fail(ResultVO.FAILD, "操作失败,请稍后重试");
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            return ResultObjectVO.fail(ResultVO.FAILD, "操作失败,请稍后重试");
        }
    }



    /**
     * 批量删除
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO deleteByIds(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            List<UserTrueNameApprove> userTrueNameApproves = JSONObject.parseArray(requestVo.getEntityJson(),UserTrueNameApprove.class);
            if(CollectionUtils.isEmpty(userTrueNameApproves))
            {
                return ResultObjectVO.fail(ResultVO.FAILD, "没有找到ID");
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
                        resultObjectVO.setMsg("请重试!");
                        continue;
                    }

                }
            }
            resultObjectVO.setData(resultObjectVOList);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            return ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        }
        return resultObjectVO;
    }



}
