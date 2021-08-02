package com.toucan.shopping.cloud.apps.web.controller.index;

import com.toucan.shopping.cloud.apps.web.vo.index.HotProductVo;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页控制器
 */
@RestController("apiIndexController")
@RequestMapping("/api/index")
public class IndexApiController {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private Toucan toucan;


    @RequestMapping("/hot_product")
    @ResponseBody
    public ResultObjectVO sendRegistVerifyCode(String mobilePhone) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try{
            List<HotProductVo> hotProductVoList = new ArrayList<HotProductVo>();
            resultObjectVO.setData(hotProductVoList);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请求失败");
        }
        return resultObjectVO;
    }



}
