package com.toucan.shopping.center.user.controller;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.center.user.constant.UserLoginConstant;
import com.toucan.shopping.center.user.constant.UserRegistConstant;
import com.toucan.shopping.center.user.entity.User;
import com.toucan.shopping.center.user.entity.UserDetail;
import com.toucan.shopping.center.user.entity.UserMobilePhone;
import com.toucan.shopping.center.user.page.UserPageInfo;
import com.toucan.shopping.center.user.redis.UserCenterLoginRedisKey;
import com.toucan.shopping.center.user.redis.UserCenterRegistRedisKey;
import com.toucan.shopping.center.user.service.*;
import com.toucan.shopping.center.user.vo.UserLoginVO;
import com.toucan.shopping.center.user.vo.UserRegistVO;
import com.toucan.shopping.center.user.vo.UserResultVO;
import com.toucan.shopping.common.generator.IdGenerator;
import com.toucan.shopping.common.util.EmailUtils;
import com.toucan.shopping.common.util.MD5Util;
import com.toucan.shopping.common.util.PhoneUtils;
import com.toucan.shopping.common.util.UserRegistUtil;
import com.toucan.shopping.common.vo.RequestJsonVO;
import com.toucan.shopping.common.vo.ResultObjectVO;
import com.toucan.shopping.common.vo.ResultVO;
import com.toucan.shopping.lock.redis.RedisLock;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 对es中用户索引的处理
 */
@RestController
@RequestMapping("/user/elasticsearch")
public class UserElasticSearchController {


    private final Logger logger = LoggerFactory.getLogger(getClass());



    /**
     * 查询列表分页
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/list",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO list(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,没有找到实体对象");
            return resultObjectVO;
        }

        try {
            UserPageInfo userPageInfo = JSONObject.parseObject(requestVo.getEntityJson(), UserPageInfo.class);

            if(StringUtils.isEmpty(requestVo.getAppCode()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到应用编码");
                return resultObjectVO;
            }


//            resultObjectVO.setData(userService.queryListPage(userPageInfo));

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }



}
