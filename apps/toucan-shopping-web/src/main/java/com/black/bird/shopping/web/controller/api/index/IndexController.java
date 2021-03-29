package com.toucan.shopping.web.controller.api.index;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.category.api.feign.service.FeignCategoryService;
import com.toucan.shopping.category.export.vo.CategoryVO;
import com.toucan.shopping.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.common.properties.BlackBird;
import com.toucan.shopping.common.util.SignUtil;
import com.toucan.shopping.common.vo.RequestJsonVO;
import com.toucan.shopping.common.vo.ResultObjectVO;
import com.toucan.shopping.common.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 首页控制器
 */
@RestController("apiIndexController")
@RequestMapping("/api/index")
public class IndexController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 类别缓存有效期1小时
     */
    private final long CATEGORY_KEY_MILLISECOND=1000*60*60*8;

    @Autowired
    private BlackBird blackBird;

    @Autowired
    private FeignCategoryService feignCategoryService;


    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 首页查询类别树
     * @return
     */
    @RequestMapping(value="/category/list",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryCategoryList(){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        String categoryKey = blackBird.getAppCode()+"_index_categorys";
        try {
            Object CategoryTreeObject = stringRedisTemplate.opsForValue().get(categoryKey);
            if(CategoryTreeObject!=null)
            {
                resultObjectVO.setData(JSONArray.parseArray(String.valueOf(CategoryTreeObject), CategoryVO.class));
                return resultObjectVO;
            }
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(blackBird.getAppCode(),"","","");
            resultObjectVO=feignCategoryService.queryCategoryTree(SignUtil.sign(requestJsonVO.getAppCode(),requestJsonVO.getEntityJson()),requestJsonVO);
            if(resultObjectVO.getCode().intValue()==ResultVO.SUCCESS.intValue())
            {
                stringRedisTemplate.opsForValue().set(categoryKey,JSONObject.toJSONString(resultObjectVO.getData()));
                stringRedisTemplate.expire(categoryKey,CATEGORY_KEY_MILLISECOND, TimeUnit.SECONDS);
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败,请重试!");
        }

        return resultObjectVO;
    }



}
