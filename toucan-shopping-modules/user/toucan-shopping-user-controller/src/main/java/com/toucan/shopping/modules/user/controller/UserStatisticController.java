package com.toucan.shopping.modules.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.redis.service.ToucanStringRedisService;
import com.toucan.shopping.modules.user.constant.UserStatisticConstant;
import com.toucan.shopping.modules.user.redis.UserStatisticKey;
import com.toucan.shopping.modules.user.service.*;
import com.toucan.shopping.modules.user.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
            Object userStatisticObj = toucanStringRedisService.get(UserStatisticKey.getUserTotalKey());
            String userStatisticInfoStr= userStatisticObj!=null?String.valueOf(userStatisticObj):"";
            if(StringUtils.isNotEmpty(userStatisticInfoStr)){
                userStatisticVO = JSONObject.parseObject(userStatisticInfoStr,UserStatisticVO.class);
            }else{
                userStatisticVO.setTotal(userStatisticService.queryTotal());
                userStatisticVO.setTodayCount(userStatisticService.queryTodayTotal()); //今日新增
                userStatisticVO.setCurMonthCount(userStatisticService.queryCurMonthTotal()); //本月新增
                userStatisticVO.setCurYearCount(userStatisticService.queryCurYearTotal()); //本年新增
                toucanStringRedisService.set(UserStatisticKey.getUserTotalKey(),
                        JSONObject.toJSONString(userStatisticVO), UserStatisticConstant.MAX_CACHE_USER_TOTAL_AGE, TimeUnit.SECONDS);
            }
            resultObjectVO.setData(userStatisticVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    /**
     * 刷新用户总数
     * @param requestVo
     * @return
     */
    @RequestMapping(value = "/refershTotal",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO refershTotal(RequestJsonVO requestVo)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            UserStatisticVO userStatisticVO = new UserStatisticVO();
            userStatisticVO.setTotal(userStatisticService.queryTotal());
            userStatisticVO.setTodayCount(userStatisticService.queryTodayTotal()); //今日新增
            userStatisticVO.setCurMonthCount(userStatisticService.queryCurMonthTotal()); //本月新增
            userStatisticVO.setCurYearCount(userStatisticService.queryCurYearTotal()); //本年新增
            toucanStringRedisService.set(UserStatisticKey.getUserTotalKey(),
                    JSONObject.toJSONString(userStatisticVO), UserStatisticConstant.MAX_CACHE_USER_TOTAL_AGE, TimeUnit.SECONDS);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }
}
