package com.toucan.shopping.modules.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.*;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.redis.service.ToucanStringRedisService;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import com.toucan.shopping.modules.user.constant.UserBindEmailConstant;
import com.toucan.shopping.modules.user.constant.UserLoginConstant;
import com.toucan.shopping.modules.user.constant.UserRegistConstant;
import com.toucan.shopping.modules.user.constant.UserStatisticConstant;
import com.toucan.shopping.modules.user.entity.*;
import com.toucan.shopping.modules.user.kafka.message.UserCreateMessage;
import com.toucan.shopping.modules.user.login.cache.service.UserLoginCacheService;
import com.toucan.shopping.modules.user.page.UserPageInfo;
import com.toucan.shopping.modules.user.queue.NewUserMessageQueue;
import com.toucan.shopping.modules.user.queue.UserLoginHistoryQueue;
import com.toucan.shopping.modules.user.redis.UserCenterLoginRedisKey;
import com.toucan.shopping.modules.user.redis.UserCenterRedisKey;
import com.toucan.shopping.modules.user.redis.UserCenterRegistRedisKey;
import com.toucan.shopping.modules.user.redis.UserStatisticKey;
import com.toucan.shopping.modules.user.service.*;
import com.toucan.shopping.modules.user.util.LoginTokenUtil;
import com.toucan.shopping.modules.user.vo.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 用户统计
 */
@RestController
@RequestMapping("/userStatistic")
public class UserStatisticController {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private Toucan toucan;

    @Autowired
    private UserStatisticService userStatisticService;

    @Autowired
    private ToucanStringRedisService toucanStringRedisService;


    @RequestMapping(value = "/queryTotalAndTodayAndCurrentMonthAndCurrentYear",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryTotalAndTodayAndCurrentMonthAndCurrentYear(RequestJsonVO requestVo)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            UserStatisticVO userStatisticVO = new UserStatisticVO();
            Object userTotalObj = toucanStringRedisService.get(UserStatisticKey.getUserTotalKey());
            if(userTotalObj!=null&&!"0".equals(String.valueOf(userTotalObj)))
            {
                userStatisticVO.setTotal(Long.parseLong(String.valueOf(userTotalObj))); //总数
            }else{
                userStatisticVO.setTotal(userStatisticService.queryTodayTotal());
                toucanStringRedisService.set(UserStatisticKey.getUserTotalKey(),String.valueOf(userStatisticVO.getTotal()), UserStatisticConstant.MAX_CACHE_USER_TOTAL_AGE, TimeUnit.SECONDS);
            }
            userStatisticVO.setTodayCount(userStatisticService.queryTodayTotal()); //今日新增
            userStatisticVO.setCurMonthCount(userStatisticService.queryCurMonthTotal()); //本月新增
            userStatisticVO.setCurYearCount(userStatisticService.queryCurYearTotal()); //本年新增
            resultObjectVO.setData(userStatisticVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


}
