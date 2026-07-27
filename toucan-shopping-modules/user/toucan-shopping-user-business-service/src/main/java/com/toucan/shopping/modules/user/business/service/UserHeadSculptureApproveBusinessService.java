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
import com.toucan.shopping.modules.user.entity.UserDetail;
import com.toucan.shopping.modules.user.entity.UserHeadSculptureApprove;
import com.toucan.shopping.modules.user.entity.UserHeadSculptureApproveRecord;
import com.toucan.shopping.modules.user.page.UserHeadSculptureApprovePageInfo;
import com.toucan.shopping.modules.user.redis.UserCenterHeadSculptureApproveKey;
import com.toucan.shopping.modules.user.service.*;
import com.toucan.shopping.modules.user.vo.UserHeadSculptureApproveVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserHeadSculptureApproveBusinessService {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IdGenerator idGenerator;


    @Autowired
    private UserHeadSculptureApproveService userHeadSculptureApproveService;

    @Autowired
    private UserHeadSculptureApproveRecordService userHeadSculptureApproveRecordService;

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
        UserHeadSculptureApprove userHeadSculptureApprove = JSONObject.parseObject(requestJsonVO.getEntityJson(),UserHeadSculptureApprove.class);
        Check.notEmpty(userHeadSculptureApprove.getHeadSculpture(), ResultObjectVO.FAILD, "真实姓名不能为空");
        Check.notNull(userHeadSculptureApprove.getUserMainId(), ResultObjectVO.FAILD, "用户ID不能为空");
        String userMainId = String.valueOf(userHeadSculptureApprove.getUserMainId());
        try {
            boolean lockStatus = skylarkLock.lock(UserCenterHeadSculptureApproveKey.getSaveApproveLockKeyForService(userMainId), userMainId);
            if (!lockStatus) {
                return ResultObjectVO.fail(ResultObjectVO.FAILD, "请稍后重试");
            }
            //查询是否存在审核中
            UserHeadSculptureApprove queryUserHeadSculptureApprove = new UserHeadSculptureApprove();
            queryUserHeadSculptureApprove.setUserMainId(userHeadSculptureApprove.getUserMainId());
            queryUserHeadSculptureApprove.setApproveStatus(userHeadSculptureApprove.getApproveStatus());
            List<UserHeadSculptureApprove> userHeadSculptureApproves = userHeadSculptureApproveService.findListByEntity(queryUserHeadSculptureApprove);
            if(CollectionUtils.isNotEmpty(userHeadSculptureApproves))
            {
                return ResultObjectVO.fail(ResultObjectVO.FAILD, "头像认证正在审核中");
            }


            userHeadSculptureApprove.setId(idGenerator.id());
            userHeadSculptureApprove.setDeleteStatus((short)0);
            int ret = userHeadSculptureApproveService.save(userHeadSculptureApprove);
            if(ret<=0)
            {
                logger.warn("保存用户头像审核记录失败 requestJson{} id{}",requestJsonVO.getEntityJson(),userHeadSculptureApprove.getId());
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
            }
            resultObjectVO.setData(userHeadSculptureApprove);
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            return ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        }finally{
            skylarkLock.unLock(UserCenterHeadSculptureApproveKey.getSaveApproveLockKeyForService(userMainId), userMainId);
        }
        return resultObjectVO;
    }


    @RequestCheck(requireEntity = true)
    public ResultObjectVO update(RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        UserHeadSculptureApprove userHeadSculptureApprove = JSONObject.parseObject(requestJsonVO.getEntityJson(),UserHeadSculptureApprove.class);
        Check.notNull(userHeadSculptureApprove.getId(), ResultObjectVO.FAILD, "ID为空");
        Check.notEmpty(userHeadSculptureApprove.getHeadSculpture(), ResultObjectVO.FAILD, "真实姓名不能为空");
        Check.notNull(userHeadSculptureApprove.getUserMainId(), ResultObjectVO.FAILD, "用户ID不能为空");

        String userMainId = String.valueOf(userHeadSculptureApprove.getUserMainId());
        try {
            boolean lockStatus = skylarkLock.lock(UserCenterHeadSculptureApproveKey.getUpdateApproveLockKeyForService(userMainId), userMainId);
            if (!lockStatus) {
                return ResultObjectVO.fail(ResultObjectVO.FAILD, "请稍后重试");
            }

            userHeadSculptureApprove.setDeleteStatus((short)0);
            int ret = userHeadSculptureApproveService.update(userHeadSculptureApprove);
            if(ret<=0)
            {
                logger.warn("保存用户头像审核记录失败 requestJson{} id{}",requestJsonVO.getEntityJson(),userHeadSculptureApprove.getId());
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
            }
            resultObjectVO.setData(userHeadSculptureApprove);
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            return ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        }finally{
            skylarkLock.unLock(UserCenterHeadSculptureApproveKey.getUpdateApproveLockKeyForService(userMainId), userMainId);
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
            UserHeadSculptureApprovePageInfo queryPageInfo = JSONObject.parseObject(requestVo.getEntityJson(), UserHeadSculptureApprovePageInfo.class);

            //查询列表页
            resultObjectVO.setData(userHeadSculptureApproveService.queryListPage(queryPageInfo));

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
        UserHeadSculptureApprove userHeadSculptureApprove = JSONObject.parseObject(requestJsonVO.getEntityJson(),UserHeadSculptureApprove.class);
        Check.notNull(userHeadSculptureApprove.getUserMainId(), ResultObjectVO.FAILD, "用户ID不能为空");
        try {
            UserHeadSculptureApprove queryUserHeadSculptureApprove = new UserHeadSculptureApprove();
            queryUserHeadSculptureApprove.setUserMainId(userHeadSculptureApprove.getUserMainId());
            if(userHeadSculptureApprove.getApproveStatus()!=null) {
                queryUserHeadSculptureApprove.setApproveStatus(userHeadSculptureApprove.getApproveStatus());
            }
            List<UserHeadSculptureApprove> userHeadSculptureApproves = userHeadSculptureApproveService.findListByEntity(queryUserHeadSculptureApprove);
            resultObjectVO.setData(userHeadSculptureApproves);
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
    public ResultObjectVO queryAliveByUserMainId(RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        UserHeadSculptureApprove userHeadSculptureApprove = JSONObject.parseObject(requestJsonVO.getEntityJson(),UserHeadSculptureApprove.class);
        Check.notNull(userHeadSculptureApprove.getUserMainId(), ResultObjectVO.FAILD, "用户ID不能为空");
        try {
            UserHeadSculptureApprove queryUserHeadSculptureApprove = new UserHeadSculptureApprove();
            queryUserHeadSculptureApprove.setUserMainId(userHeadSculptureApprove.getUserMainId());
            if(userHeadSculptureApprove.getApproveStatus()!=null) {
                queryUserHeadSculptureApprove.setApproveStatus(userHeadSculptureApprove.getApproveStatus());
            }
            List<UserHeadSculptureApprove> userHeadSculptureApproves = userHeadSculptureApproveService.findListByEntityOrderByCreateDateDesc(queryUserHeadSculptureApprove);
            if(CollectionUtils.isNotEmpty(userHeadSculptureApproves)) {
                resultObjectVO.setData(userHeadSculptureApproves.get(0));
            }
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
        UserHeadSculptureApprove userHeadSculptureApprove = JSONObject.parseObject(requestJsonVO.getEntityJson(),UserHeadSculptureApprove.class);
        Check.notNull(userHeadSculptureApprove.getUserMainId(), ResultObjectVO.FAILD, "用户ID不能为空");
        try {
            UserHeadSculptureApprove queryUserHeadSculptureApprove = new UserHeadSculptureApprove();
            queryUserHeadSculptureApprove.setUserMainId(userHeadSculptureApprove.getUserMainId());
            if(userHeadSculptureApprove.getApproveStatus()!=null) {
                queryUserHeadSculptureApprove.setApproveStatus(userHeadSculptureApprove.getApproveStatus());
            }
            List<UserHeadSculptureApprove> userHeadSculptureApproves = userHeadSculptureApproveService.findListByEntityOrderByUpdateDateDesc(queryUserHeadSculptureApprove);
            resultObjectVO.setData(userHeadSculptureApproves);
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
        UserHeadSculptureApprove userHeadSculptureApprove = JSONObject.parseObject(requestJsonVO.getEntityJson(),UserHeadSculptureApprove.class);
        Check.notNull(userHeadSculptureApprove.getId(), ResultObjectVO.FAILD, "查询失败,ID不能为空");
        try {
            UserHeadSculptureApprove queryUserHeadSculptureApprove = new UserHeadSculptureApprove();
            queryUserHeadSculptureApprove.setId(userHeadSculptureApprove.getId());
            List<UserHeadSculptureApprove> userHeadSculptureApproves = userHeadSculptureApproveService.findListByEntity(queryUserHeadSculptureApprove);
            resultObjectVO.setData(userHeadSculptureApproves);
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
            UserHeadSculptureApproveVO userHeadSculptureApproveVO = JSONObject.parseObject(requestVo.getEntityJson(),UserHeadSculptureApproveVO.class);
            Check.notNull(userHeadSculptureApproveVO.getId(), ResultVO.FAILD, "操作失败,没有找到ID");

            UserHeadSculptureApprove queryUserHeadSculptureApprove= new UserHeadSculptureApprove();
            queryUserHeadSculptureApprove.setId(userHeadSculptureApproveVO.getId());
            List<UserHeadSculptureApprove> userHeadSculptureApproves = userHeadSculptureApproveService.findListByEntity(queryUserHeadSculptureApprove);

            if(CollectionUtils.isNotEmpty(userHeadSculptureApproves))
            {
                UserHeadSculptureApprove userHeadSculptureApprove =  userHeadSculptureApproves.get(0);
                userHeadSculptureApprove.setApproveStatus(2); //设置审核通过
                int ret = userHeadSculptureApproveService.update(userHeadSculptureApprove);
                if(ret>0) {

                    if (ret <= 0) {
                        logger.warn("头像审核失败 {} ", JSONObject.toJSONString(userHeadSculptureApproves));
                        return ResultObjectVO.fail(ResultVO.FAILD, "操作失败,请稍后重试");
                    }


                    //保存审核记录
                    UserHeadSculptureApproveRecord userHeadSculptureApproveRecord = new UserHeadSculptureApproveRecord();
                    userHeadSculptureApproveRecord.setId(idGenerator.id());
                    userHeadSculptureApproveRecord.setApproveId(userHeadSculptureApprove.getId()); //审核主表ID
                    userHeadSculptureApproveRecord.setUserMainId(userHeadSculptureApprove.getUserMainId());
                    userHeadSculptureApproveRecord.setApproveStatus(1);
                    userHeadSculptureApproveRecord.setCreateAdminId(userHeadSculptureApproveVO.getApproveAdminId()); //审核管理员
                    userHeadSculptureApproveRecord.setCreateDate(new Date());
                    userHeadSculptureApproveRecord.setDeleteStatus((short)0);
                    userHeadSculptureApproveRecordService.save(userHeadSculptureApproveRecord);

                    //设置用户头像
                    List<UserDetail> userDetails = userDetailService.findByUserMainId(userHeadSculptureApprove.getUserMainId());
                    if(CollectionUtils.isNotEmpty(userDetails))
                    {
                        UserDetail userDetail = userDetails.get(0);
                        userDetail.setHeadSculpture(userHeadSculptureApprove.getHeadSculpture());
                        ret = userDetailService.update(userDetail);
                        if (ret <= 0) {
                            logger.warn("修改头像失败 {} ", JSONObject.toJSONString(userDetail));
                            resultObjectVO.setCode(ResultVO.FAILD);
                            resultObjectVO.setMsg("操作失败,请稍后重试");

                            //开始回滚数据
                            userHeadSculptureApprove.setApproveStatus(1); //设置审核中
                            ret = userHeadSculptureApproveService.update(userHeadSculptureApprove);
                            if (ret <= 0) {
                                logger.warn("回滚头像审核失败 {} ", JSONObject.toJSONString(userHeadSculptureApproves));
                                resultObjectVO.setCode(ResultVO.FAILD);
                                resultObjectVO.setMsg("操作失败,请稍后重试");
                            }

                            return resultObjectVO;
                        }

                        try {
                            //更新商城用户缓存
                            userRedisService.flushLoginCache(String.valueOf(userHeadSculptureApprove.getUserMainId()), AppCodeEnum.SHOPPING_WEB.value());
                        }catch(Exception e)
                        {
                            logger.warn(e.getMessage(),e);
                        }

                    }

                    resultObjectVO.setData(userHeadSculptureApproves);
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
            UserHeadSculptureApproveVO userHeadSculptureApproveVO = JSONObject.parseObject(requestVo.getEntityJson(),UserHeadSculptureApproveVO.class);
            Check.notNull(userHeadSculptureApproveVO.getId(), ResultVO.FAILD, "操作失败,没有找到ID");

            UserHeadSculptureApprove queryUserHeadSculptureApprove= new UserHeadSculptureApprove();
            queryUserHeadSculptureApprove.setId(userHeadSculptureApproveVO.getId());
            List<UserHeadSculptureApprove> userHeadSculptureApproves = userHeadSculptureApproveService.findListByEntity(queryUserHeadSculptureApprove);

            if(CollectionUtils.isNotEmpty(userHeadSculptureApproves))
            {
                UserHeadSculptureApprove userHeadSculptureApprove =  userHeadSculptureApproves.get(0);
                userHeadSculptureApprove.setApproveStatus(3); //设置审核驳回
                userHeadSculptureApprove.setRejectText(userHeadSculptureApproveVO.getRejectText()); //驳回原因
                int ret = userHeadSculptureApproveService.update(userHeadSculptureApprove);
                if(ret>0) {

                    if (ret <= 0) {
                        logger.warn("头像审核失败 {} ", JSONObject.toJSONString(userHeadSculptureApproves));
                        return ResultObjectVO.fail(ResultVO.FAILD, "操作失败,请稍后重试");
                    }


                    //保存审核记录
                    UserHeadSculptureApproveRecord userHeadSculptureApproveRecord = new UserHeadSculptureApproveRecord();
                    userHeadSculptureApproveRecord.setId(idGenerator.id());
                    userHeadSculptureApproveRecord.setApproveId(userHeadSculptureApprove.getId()); //审核主表ID
                    userHeadSculptureApproveRecord.setUserMainId(userHeadSculptureApprove.getUserMainId());
                    userHeadSculptureApproveRecord.setApproveStatus(2);
                    userHeadSculptureApproveRecord.setRejectText(userHeadSculptureApproveVO.getRejectText()); //驳回原因
                    userHeadSculptureApproveRecord.setCreateAdminId(userHeadSculptureApproveVO.getApproveAdminId()); //审核管理员
                    userHeadSculptureApproveRecord.setCreateDate(new Date());
                    userHeadSculptureApproveRecord.setDeleteStatus((short)0);
                    userHeadSculptureApproveRecordService.save(userHeadSculptureApproveRecord);


                    resultObjectVO.setData(userHeadSculptureApproves);
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
            List<UserHeadSculptureApprove> userHeadSculptureApproves = JSONObject.parseArray(requestVo.getEntityJson(),UserHeadSculptureApprove.class);
            if(CollectionUtils.isEmpty(userHeadSculptureApproves))
            {
                return ResultObjectVO.fail(ResultVO.FAILD, "没有找到ID");
            }
            List<ResultObjectVO> resultObjectVOList = new ArrayList<ResultObjectVO>();
            for(UserHeadSculptureApprove userHeadSculptureApprove:userHeadSculptureApproves) {
                if(userHeadSculptureApprove.getId()!=null) {
                    ResultObjectVO appResultObjectVO = new ResultObjectVO();
                    appResultObjectVO.setData(userHeadSculptureApprove);

                    int row = userHeadSculptureApproveService.deleteById(userHeadSculptureApprove.getId());
                    if (row < 1) {
                        logger.warn("删除失败，id:{}",userHeadSculptureApprove.getId());
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
