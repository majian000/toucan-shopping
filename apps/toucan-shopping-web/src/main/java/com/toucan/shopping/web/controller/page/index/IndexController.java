package com.toucan.shopping.web.controller.page.index;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.area.api.feign.service.FeignAdminAreaService;
import com.toucan.shopping.area.api.feign.service.FeignUserAreaService;
import com.toucan.shopping.area.api.feign.service.FeignUserBannerService;
import com.toucan.shopping.area.export.entity.Banner;
import com.toucan.shopping.area.export.vo.BannerVO;
import com.toucan.shopping.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.common.properties.Toucan;
import com.toucan.shopping.common.util.SignUtil;
import com.toucan.shopping.common.vo.RequestJsonVO;
import com.toucan.shopping.common.vo.ResultObjectVO;
import com.toucan.shopping.web.redis.BannerRedisKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


/**
 * 首页控制器
 */
@Controller("pageIndexController")
public class IndexController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private FeignUserAreaService feignUserAreaService;

    @Autowired
    private FeignAdminAreaService feignAdminAreaService;

    @Autowired
    private Toucan toucan;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private FeignUserBannerService feignUserBannerService;


//    /**
//     * 查询地区树
//     * @param request
//     */
//    public void queryAreaTree(HttpServletRequest request)
//    {
//        try {
//            Object areaObject = stringRedisTemplate.opsForValue().get(AreaRedisKey.getAreaKey());
//            String areaJson = "[]";
//            //如果缓存有 直接拿缓存
//            if(areaObject!=null) {
//                areaJson = String.valueOf(areaObject);
//            }else{ //调用地区服务查询地区树 放到缓存中
//                RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(blackBird.getAppCode(), "");
//                ResultObjectVO resultObjectVO = feignUserAreaService.queryAll(SignUtil.sign(requestJsonVO), requestJsonVO);
//                if (resultObjectVO.getCode().intValue() == ResultObjectVO.SUCCESS.intValue()) {
//                    areaJson = JSONObject.toJSONString(resultObjectVO.getCode());
//                    //保存地区树到redis
//                    stringRedisTemplate.opsForValue().set(AreaRedisKey.getAreaKey(),areaJson);
//                }
//            }
//            List<AreaVO> areaVOS = JSONArray.parseArray(areaJson, AreaVO.class);
//            if (CollectionUtils.isNotEmpty(areaVOS)) {
//                request.setAttribute("areaVOS", areaVOS);
//            }
//        } catch (NoSuchAlgorithmException e) {
//            logger.warn(e.getMessage(),e);
//        }
//    }


    /**
     * 查询轮播图
     * @param request
     */
    public void queryBanner(HttpServletRequest request)
    {
        List<Banner> banners = null;
        try {
            Object bannersRedisObject = stringRedisTemplate.opsForValue().get(BannerRedisKey.getIndexBanner("110000"));
            if(bannersRedisObject==null) {
                BannerVO bannerVO = new BannerVO();
                bannerVO.setAreaCodeArray(new String[]{"110000"});
                RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), bannerVO);
                ResultObjectVO resultObjectVO = feignUserBannerService.queryList(SignUtil.sign(requestJsonVO), requestJsonVO);
                if (resultObjectVO.getCode().intValue() == ResultObjectVO.SUCCESS.intValue()) {
                    banners = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()), Banner.class);
                    //保存轮播图到redis
                    stringRedisTemplate.opsForValue().set(BannerRedisKey.getIndexBanner("110000"),JSONObject.toJSONString(resultObjectVO.getData()));
                }
            }else{
                banners = JSONArray.parseArray(JSONObject.toJSONString(String.valueOf(bannersRedisObject)), Banner.class);
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(),e);
        }

        if(banners==null)
        {
            banners = new ArrayList<Banner>();
        }
        request.setAttribute("banners",banners);
    }

    @RequestMapping("/index")
    public String index(HttpServletRequest request)
    {
        //查询地区树
//        queryAreaTree(request);

        queryBanner(request);
        return "index";
    }


    @RequestMapping("/")
    public String defaultIndex(HttpServletRequest request)
    {
        return index(request);
    }

}
