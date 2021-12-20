package com.toucan.shopping.cloud.user.controller;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.user.constant.AppCodeEnum;
import com.toucan.shopping.cloud.user.service.UserRedisService;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
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
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户实名制审核
 */
@RestController
@RequestMapping("/user/head/sculpture/approve")
public class UserHeadSculptureApproveController {


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
        UserHeadSculptureApprove userHeadSculptureApprove = JSONObject.parseObject(requestJsonVO.getEntityJson(),UserHeadSculptureApprove.class);
        if(StringUtils.isEmpty(userHeadSculptureApprove.getHeadSculpture()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请求失败,真实姓名不能为空");
            return resultObjectVO;
        }
        if(userHeadSculptureApprove.getUserMainId()==null)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请求失败,用户ID不能为空");
            return resultObjectVO;
        }
        String userMainId = String.valueOf(userHeadSculptureApprove.getUserMainId());
        try {
            boolean lockStatus = skylarkLock.lock(UserCenterHeadSculptureApproveKey.getSaveApproveLockKeyForService(userMainId), userMainId);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求失败,请稍后重试");
                return resultObjectVO;
            }
            //查询是否存在审核中
            UserHeadSculptureApprove queryUserHeadSculptureApprove = new UserHeadSculptureApprove();
            queryUserHeadSculptureApprove.setUserMainId(userHeadSculptureApprove.getUserMainId());
            queryUserHeadSculptureApprove.setApproveStatus(userHeadSculptureApprove.getApproveStatus());
            List<UserHeadSculptureApprove> userHeadSculptureApproves = userHeadSculptureApproveService.findListByEntity(queryUserHeadSculptureApprove);
            if(CollectionUtils.isNotEmpty(userHeadSculptureApproves))
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("保存失败,实名认证正在审核中");
                return resultObjectVO;
            }

            userHeadSculptureApprove.setId(idGenerator.id());
            userHeadSculptureApprove.setDeleteStatus((short)0);
            int ret = userHeadSculptureApproveService.save(userHeadSculptureApprove);
            if(ret<=0)
            {
                logger.warn("保存用户头像审核记录失败 requestJson{} id{}",requestJsonVO.getEntityJson(),userHeadSculptureApprove.getId());
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,请稍后重试");
            }
            resultObjectVO.setData(userHeadSculptureApprove);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }finally{
            skylarkLock.unLock(UserCenterHeadSculptureApproveKey.getSaveApproveLockKeyForService(userMainId), userMainId);
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
        UserHeadSculptureApprove userHeadSculptureApprove = JSONObject.parseObject(requestJsonVO.getEntityJson(),UserHeadSculptureApprove.class);
        if(userHeadSculptureApprove.getId()==null)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请求失败,ID为空");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(userHeadSculptureApprove.getHeadSculpture()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请求失败,真实姓名不能为空");
            return resultObjectVO;
        }
        if(userHeadSculptureApprove.getUserMainId()==null)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请求失败,用户ID不能为空");
            return resultObjectVO;
        }

        String userMainId = String.valueOf(userHeadSculptureApprove.getUserMainId());
        try {
            boolean lockStatus = skylarkLock.lock(UserCenterHeadSculptureApproveKey.getUpdateApproveLockKeyForService(userMainId), userMainId);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求失败,请稍后重试");
                return resultObjectVO;
            }

            userHeadSculptureApprove.setDeleteStatus((short)0);
            int ret = userHeadSculptureApproveService.update(userHeadSculptureApprove);
            if(ret<=0)
            {
                logger.warn("保存用户头像审核记录失败 requestJson{} id{}",requestJsonVO.getEntityJson(),userHeadSculptureApprove.getId());
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,请稍后重试");
            }
            resultObjectVO.setData(userHeadSculptureApprove);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
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
            UserHeadSculptureApprovePageInfo queryPageInfo = JSONObject.parseObject(requestVo.getEntityJson(), UserHeadSculptureApprovePageInfo.class);

            if(StringUtils.isEmpty(requestVo.getAppCode()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到应用编码");
                return resultObjectVO;
            }

            //查询列表页
            resultObjectVO.setData(userHeadSculptureApproveService.queryListPage(queryPageInfo));

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
        UserHeadSculptureApprove userHeadSculptureApprove = JSONObject.parseObject(requestJsonVO.getEntityJson(),UserHeadSculptureApprove.class);
        if(userHeadSculptureApprove.getUserMainId()==null)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请求失败,用户ID不能为空");
            return resultObjectVO;
        }
        try {
            UserHeadSculptureApprove queryUserHeadSculptureApprove = new UserHeadSculptureApprove();
            queryUserHeadSculptureApprove.setUserMainId(userHeadSculptureApprove.getUserMainId());
            if(userHeadSculptureApprove.getApproveStatus()!=null) {
                queryUserHeadSculptureApprove.setApproveStatus(userHeadSculptureApprove.getApproveStatus());
            }
            List<UserHeadSculptureApprove> userHeadSculptureApproves = userHeadSculptureApproveService.findListByEntity(queryUserHeadSculptureApprove);
            resultObjectVO.setData(userHeadSculptureApproves);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }


    @RequestMapping(value="/queryListByUserMainIdAndOrderByUpdateDateDesc",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryListByUserMainIdAndOrderByUpdateDateDesc(@RequestBody RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("请求失败,没有找到请求对象");
            return resultObjectVO;
        }
        UserHeadSculptureApprove userHeadSculptureApprove = JSONObject.parseObject(requestJsonVO.getEntityJson(),UserHeadSculptureApprove.class);
        if(userHeadSculptureApprove.getUserMainId()==null)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请求失败,用户ID不能为空");
            return resultObjectVO;
        }
        try {
            UserHeadSculptureApprove queryUserHeadSculptureApprove = new UserHeadSculptureApprove();
            queryUserHeadSculptureApprove.setUserMainId(userHeadSculptureApprove.getUserMainId());
            if(userHeadSculptureApprove.getApproveStatus()!=null) {
                queryUserHeadSculptureApprove.setApproveStatus(userHeadSculptureApprove.getApproveStatus());
            }
            List<UserHeadSculptureApprove> userHeadSculptureApproves = userHeadSculptureApproveService.findListByEntityOrderByUpdateDateDesc(queryUserHeadSculptureApprove);
            resultObjectVO.setData(userHeadSculptureApproves);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }



    @RequestMapping(value="/queryById",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryById(@RequestBody RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("查询失败,没有找到请求对象");
            return resultObjectVO;
        }
        UserHeadSculptureApprove userHeadSculptureApprove = JSONObject.parseObject(requestJsonVO.getEntityJson(),UserHeadSculptureApprove.class);
        if(userHeadSculptureApprove.getId()==null)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("查询失败,ID不能为空");
            return resultObjectVO;
        }
        try {
            UserHeadSculptureApprove queryUserHeadSculptureApprove = new UserHeadSculptureApprove();
            queryUserHeadSculptureApprove.setId(userHeadSculptureApprove.getId());
            List<UserHeadSculptureApprove> userHeadSculptureApproves = userHeadSculptureApproveService.findListByEntity(queryUserHeadSculptureApprove);
            resultObjectVO.setData(userHeadSculptureApproves);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败,请稍后重试");
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
            resultObjectVO.setMsg("操作失败,没有找到实体对象");
            return resultObjectVO;
        }

        try {
            UserHeadSculptureApproveVO userHeadSculptureApproveVO = JSONObject.parseObject(requestVo.getEntityJson(),UserHeadSculptureApproveVO.class);
            if(userHeadSculptureApproveVO.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("操作失败,没有找到ID");
                return resultObjectVO;
            }

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
                        resultObjectVO.setCode(ResultVO.FAILD);
                        resultObjectVO.setMsg("操作失败,请稍后重试");
                        return resultObjectVO;
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

                    //设置用户实名
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

                            try {
                                //更新商城用户缓存
                                userRedisService.flushLoginCache(String.valueOf(userHeadSculptureApprove.getUserMainId()), AppCodeEnum.SHOPPING_WEB.value());
                            }catch(Exception e)
                            {
                                logger.warn(e.getMessage(),e);
                            }

                            return resultObjectVO;
                        }


                    }

                    resultObjectVO.setData(userHeadSculptureApproves);
                    return resultObjectVO;
                }
            }

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("操作失败,请稍后重试");
            return resultObjectVO;
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("操作失败,请稍后重试");
        }
        return resultObjectVO;
    }





    /**
     * 驳回指定
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/reject/id",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO rejectById(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("操作失败,没有找到实体对象");
            return resultObjectVO;
        }

        try {
            UserHeadSculptureApproveVO userHeadSculptureApproveVO = JSONObject.parseObject(requestVo.getEntityJson(),UserHeadSculptureApproveVO.class);
            if(userHeadSculptureApproveVO.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("操作失败,没有找到ID");
                return resultObjectVO;
            }

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
                        resultObjectVO.setCode(ResultVO.FAILD);
                        resultObjectVO.setMsg("操作失败,请稍后重试");
                        return resultObjectVO;
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

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("操作失败,请稍后重试");
            return resultObjectVO;
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("操作失败,请稍后重试");
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
            resultObjectVO.setMsg("删除失败,没有找到实体对象");
            return resultObjectVO;
        }

        try {
            List<UserHeadSculptureApprove> userHeadSculptureApproves = JSONObject.parseArray(requestVo.getEntityJson(),UserHeadSculptureApprove.class);
            if(CollectionUtils.isEmpty(userHeadSculptureApproves))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("删除失败,没有找到ID");
                return resultObjectVO;
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
                        resultObjectVO.setMsg("删除失败,请重试!");
                        continue;
                    }

                }
            }
            resultObjectVO.setData(resultObjectVOList);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("删除失败,请稍后重试");
        }
        return resultObjectVO;
    }



}
