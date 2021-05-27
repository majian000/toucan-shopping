package com.toucan.shopping.cloud.apps.web.controller.page.index;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.apps.web.redis.BannerRedisKey;
import com.toucan.shopping.cloud.area.api.feign.service.FeignAreaService;
import com.toucan.shopping.cloud.area.api.feign.service.FeignBannerService;
import com.toucan.shopping.cloud.category.api.feign.service.FeignCategoryService;
import com.toucan.shopping.modules.area.entity.Banner;
import com.toucan.shopping.modules.area.vo.BannerVO;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * 首页控制器
 */
@Controller("pageIndexController")
public class IndexPageController {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private FeignAreaService feignAreaService;

    @Autowired
    private Toucan toucan;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private FeignBannerService feignBannerService;

    @Autowired
    private FeignCategoryService feignCategoryService;



    /**
     * 类别缓存有效期1小时
     */
    private final long CATEGORY_KEY_MILLISECOND=1000*60*60*8;


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
//                    areaJson = JSONObject.toJSONString(resultObjectVO.getData());
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
    public void queryBanners(HttpServletRequest request)
    {
        List<Banner> banners = null;
        try {
            Object bannersRedisObject = stringRedisTemplate.opsForValue().get(BannerRedisKey.getIndexBanner("110000"));
            if(bannersRedisObject==null) {
                BannerVO bannerVO = new BannerVO();
                bannerVO.setAreaCodeArray(new String[]{"110000"});
                RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), bannerVO);
                ResultObjectVO resultObjectVO = feignBannerService.queryList(SignUtil.sign(requestJsonVO), requestJsonVO);
                if (resultObjectVO.getCode().intValue() == ResultObjectVO.SUCCESS.intValue()) {
                    banners = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()), Banner.class);
                    //保存轮播图到redis
                    stringRedisTemplate.opsForValue().set(BannerRedisKey.getIndexBanner("110000"),JSONObject.toJSONString(resultObjectVO.getData()));
                }else{
                    banners = new ArrayList<Banner>();
                }
            }else{
                banners = JSONArray.parseArray(String.valueOf(bannersRedisObject), Banner.class);
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(),e);
            banners = new ArrayList<Banner>();
        }

        request.setAttribute("banners",banners);
    }


    /**
     * 查询类别列表
     * @param request
     */
    public void queryCategorys(HttpServletRequest request)
    {
        try {
            ResultObjectVO resultObjectVO = new ResultObjectVO();
            String categoryKey = toucan.getAppCode()+"_INDEX_CATEGORYS";
            Object CategoryTreeObject = stringRedisTemplate.opsForValue().get(categoryKey);
            if(CategoryTreeObject!=null)
            {
                request.setAttribute("categorys",JSONArray.parseArray(String.valueOf(CategoryTreeObject), CategoryVO.class));
            }else {
                CategoryVO categoryVO = new CategoryVO();
                categoryVO.setAreaCode("110000");
                RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), categoryVO);
                resultObjectVO = feignCategoryService.queryCategoryTreeByAreaCode(SignUtil.sign(requestJsonVO.getAppCode(), requestJsonVO.getEntityJson()), requestJsonVO);
                if (resultObjectVO.getCode().intValue() == ResultVO.SUCCESS.intValue()) {
                    stringRedisTemplate.opsForValue().set(categoryKey, JSONObject.toJSONString(resultObjectVO.getData()));
                    stringRedisTemplate.expire(categoryKey, CATEGORY_KEY_MILLISECOND, TimeUnit.SECONDS);

                    request.setAttribute("categorys", resultObjectVO.getData());
                }else{
                    request.setAttribute("categorys", new ArrayList<CategoryVO>());
                }
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(),e);

            request.setAttribute("categorys", new ArrayList<CategoryVO>());
        }

    }

    @RequestMapping("/index")
    public String index(HttpServletRequest request)
    {
        //查询地区树
//        queryAreaTree(request);

        //查询轮播图
        queryBanners(request);

        //查询类别列表
        queryCategorys(request);

        //查询顶部栏目

        return "index";
    }


    @RequestMapping("/")
    public String defaultIndex(HttpServletRequest request)
    {
        return index(request);
    }

}
