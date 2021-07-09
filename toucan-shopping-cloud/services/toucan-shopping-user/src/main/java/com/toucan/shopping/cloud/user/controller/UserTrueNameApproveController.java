package com.toucan.shopping.cloud.user.controller;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.user.queue.NewUserMessageQueue;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.lock.redis.RedisLock;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.*;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.user.constant.UserLoginConstant;
import com.toucan.shopping.modules.user.constant.UserRegistConstant;
import com.toucan.shopping.modules.user.entity.*;
import com.toucan.shopping.modules.user.es.service.UserElasticSearchService;
import com.toucan.shopping.modules.user.kafka.message.UserCreateMessage;
import com.toucan.shopping.modules.user.page.UserPageInfo;
import com.toucan.shopping.modules.user.redis.UserCenterLoginRedisKey;
import com.toucan.shopping.modules.user.redis.UserCenterRegistRedisKey;
import com.toucan.shopping.modules.user.redis.UserCenterTrueNameApproveKey;
import com.toucan.shopping.modules.user.service.*;
import com.toucan.shopping.modules.user.vo.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

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
    private UserTrueNameApproveService userTrueNameApproveService;

    @Autowired
    private Toucan toucan;

    @Autowired
    private RedisLock redisLock;

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
            boolean lockStatus = redisLock.lock(UserCenterTrueNameApproveKey.getSaveApproveLockKey(userMainId), userMainId);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("超时重试");
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
                resultObjectVO.setMsg("保存失败,实名验证正在审核中");
                return resultObjectVO;
            }

            userTrueNameApprove.setId(idGenerator.id());
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
            redisLock.unLock(UserCenterTrueNameApproveKey.getSaveApproveLockKey(userMainId), userMainId);
        }
        return resultObjectVO;
    }




}
