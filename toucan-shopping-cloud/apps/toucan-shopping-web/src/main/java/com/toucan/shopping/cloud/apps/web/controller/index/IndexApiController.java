package com.toucan.shopping.cloud.apps.web.controller.index;

import com.toucan.shopping.cloud.apps.web.service.IndexService;
import com.toucan.shopping.cloud.apps.web.vo.index.LikeProductVo;
import com.toucan.shopping.cloud.content.api.feign.service.FeignPcIndexColumnService;
import com.toucan.shopping.modules.column.vo.HotProductVO;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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
    private IndexService indexService;


    @Autowired
    private Toucan toucan;


    @RequestMapping("/hot_product")
    @ResponseBody
    public ResultObjectVO queryHotProduct() {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try{
            resultObjectVO.setData(indexService.queryHotProduces());
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
    public ResultObjectVO queryLikeProduct() {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try{
            List<LikeProductVo> likeProductVos = new ArrayList<LikeProductVo>();

            LikeProductVo likeProductVo1= new LikeProductVo();
            likeProductVo1.setPrice(99.5D);
            likeProductVo1.setName("德亚全脂纯牛奶");
            likeProductVo1.setDesc("200ml*48盒");
            likeProductVo1.setHttpMainPhoto("http://8.140.187.184:8083/static/images/hot1.jpg");
            likeProductVos.add(likeProductVo1);


            LikeProductVo likeProductVo2= new LikeProductVo();
            likeProductVo2.setPrice(5288D);
            likeProductVo2.setName("iphone 6S");
            likeProductVo2.setDesc("Apple/苹果 iPhone 6s Plus公开版");
            likeProductVo2.setHttpMainPhoto("http://8.140.187.184:8083/static/images/hot2.jpg");
            likeProductVos.add(likeProductVo2);

            LikeProductVo likeProductVo3= new LikeProductVo();
            likeProductVo3.setPrice(368D);
            likeProductVo3.setName("倩碧特惠组合套装");
            likeProductVo3.setDesc("倩碧补水组合套装8折促销");
            likeProductVo3.setHttpMainPhoto("http://8.140.187.184:8083/static/images/hot3.jpg");
            likeProductVos.add(likeProductVo3);

            LikeProductVo likeProductVo4= new LikeProductVo();
            likeProductVo4.setPrice(280D);
            likeProductVo4.setName("品利特级橄榄油");
            likeProductVo4.setDesc("750ml*4瓶装组合 西班牙原装进口");
            likeProductVo4.setHttpMainPhoto("http://8.140.187.184:8083/static/images/hot4.jpg");
            likeProductVos.add(likeProductVo4);

            LikeProductVo likeProductVo5= new LikeProductVo();
            likeProductVo5.setPrice(280D);
            likeProductVo5.setName("品利特级橄榄油");
            likeProductVo5.setDesc("750ml*4瓶装组合 西班牙原装进口");
            likeProductVo5.setHttpMainPhoto("http://8.140.187.184:8083/static/images/hot4.jpg");
            likeProductVos.add(likeProductVo5);


            resultObjectVO.setData(likeProductVos);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请求失败");
        }
        return resultObjectVO;
    }



    @RequestMapping("/banners")
    @ResponseBody
    public ResultObjectVO banners(HttpServletRequest request) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        resultObjectVO.setData(indexService.queryBanners());
        return resultObjectVO;
    }


    @RequestMapping("/columns")
    @ResponseBody
    public ResultObjectVO columns(HttpServletRequest request) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        resultObjectVO.setData(indexService.queryColumns());
        return resultObjectVO;
    }
}
