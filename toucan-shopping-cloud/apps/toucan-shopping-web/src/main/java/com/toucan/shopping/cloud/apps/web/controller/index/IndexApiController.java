package com.toucan.shopping.cloud.apps.web.controller.index;

import com.toucan.shopping.cloud.apps.web.vo.index.HotProductVo;
import com.toucan.shopping.cloud.apps.web.vo.index.LikeProductVo;
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
    public ResultObjectVO queryHotProduct(String mobilePhone) {
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





    @RequestMapping("/like_product")
    @ResponseBody
    public ResultObjectVO queryLikeProduct(String mobilePhone) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try{
            List<LikeProductVo> likeProductVos = new ArrayList<LikeProductVo>();

            LikeProductVo likeProductVo1= new LikeProductVo();
            likeProductVo1.setPrice(99.5D);
            likeProductVo1.setProductName("德亚全脂纯牛奶");
            likeProductVo1.setDesc("200ml*48盒");
            likeProductVo1.setHttpPreviewImg("http://127.0.0.1:8083/static/images/hot4.jpg");
            likeProductVos.add(likeProductVo1);


            LikeProductVo likeProductVo2= new LikeProductVo();
            likeProductVo2.setPrice(5288D);
            likeProductVo2.setProductName("iphone 6S");
            likeProductVo1.setDesc("Apple/苹果 iPhone 6s Plus公开版");
            likeProductVo2.setHttpPreviewImg("http://127.0.0.1:8083/static/images/hot4.jpg");
            likeProductVos.add(likeProductVo2);

            resultObjectVO.setData(likeProductVos);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请求失败");
        }
        return resultObjectVO;
    }


}
