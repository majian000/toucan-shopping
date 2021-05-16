package com.toucan.shopping.standard.apps.web.controller.page.index;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.area.entity.Banner;
import com.toucan.shopping.modules.area.entity.BannerArea;
import com.toucan.shopping.modules.area.service.AreaService;
import com.toucan.shopping.modules.area.service.BannerAreaService;
import com.toucan.shopping.modules.area.service.BannerService;
import com.toucan.shopping.modules.area.vo.AreaVO;
import com.toucan.shopping.modules.area.vo.BannerVO;
import com.toucan.shopping.modules.category.service.CategoryService;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.standard.apps.web.redis.AreaRedisKey;
import com.toucan.shopping.standard.apps.web.redis.BannerRedisKey;
import org.apache.commons.collections4.CollectionUtils;
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
    private AreaService areaService;


    @Autowired
    private Toucan toucan;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private BannerService bannerService;

    @Autowired
    private BannerAreaService bannerAreaService;


    @Autowired
    private CategoryService categoryService;



    /**
     * 类别缓存有效期1小时
     */
    private final long CATEGORY_KEY_MILLISECOND=1000*60*60*8;


    /**
     * 查询地区树
     * @param request
     */
    public void queryAreaTree(HttpServletRequest request)
    {
        try {
            Object areaObject = stringRedisTemplate.opsForValue().get(AreaRedisKey.getAreaKey());
            String areaJson = "[]";
            //如果缓存有 直接拿缓存
            if(areaObject!=null) {
                areaJson = String.valueOf(areaObject);
            }else{ //调用地区服务查询地区树 放到缓存中
                List<AreaVO> areaVOS = areaService.queryTree(toucan.getAppCode());
                if (CollectionUtils.isNotEmpty(areaVOS)) {
                    areaJson = JSONObject.toJSONString(areaVOS);
                    //保存地区树到redis
                    stringRedisTemplate.opsForValue().set(AreaRedisKey.getAreaKey(),areaJson);
                }
            }
            List<AreaVO> areaVOS = JSONArray.parseArray(areaJson, AreaVO.class);
            if (CollectionUtils.isNotEmpty(areaVOS)) {
                request.setAttribute("areaVOS", areaVOS);
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(),e);
        }
    }


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
                if(bannerVO.getAreaCodeArray()!=null&&bannerVO.getAreaCodeArray().length>0)
                {
                    List<Long> bannerIdList = new ArrayList<Long>();
                    for(int i=0;i<bannerVO.getAreaCodeArray().length;i++)
                    {
                        BannerArea bannerArea = new BannerArea();
                        bannerArea.setAppCode(toucan.getAppCode());
                        bannerArea.setAreaCode(bannerVO.getAreaCodeArray()[i]);
                        List<BannerArea> bannerAreaList = bannerAreaService.queryList(bannerArea);
                        if(CollectionUtils.isNotEmpty(bannerAreaList))
                        {
                            for(BannerArea ba:bannerAreaList) {
                                if(ba!=null) {
                                    bannerIdList.add(ba.getBannerId());
                                }
                            }
                        }
                    }
                    if(CollectionUtils.isNotEmpty(bannerIdList))
                    {
                        Long[] bannerIdArray = new Long[bannerIdList.size()];
                        for(int i=0;i<bannerIdList.size();i++)
                        {
                            bannerIdArray[i]=bannerIdList.get(i);
                        }
                        bannerVO.setIdArray(bannerIdArray);
                    }

                }

                List<Banner> bannerList = bannerService.queryList(bannerVO);
                if (CollectionUtils.isNotEmpty(bannerList)) {
                    banners = JSONArray.parseArray(JSONObject.toJSONString(bannerList), Banner.class);
                    //保存轮播图到redis
                    stringRedisTemplate.opsForValue().set(BannerRedisKey.getIndexBanner("110000"),JSONObject.toJSONString(banners));
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
            String categoryKey = toucan.getAppCode()+"_INDEX_CATEGORYS";
            Object CategoryTreeObject = stringRedisTemplate.opsForValue().get(categoryKey);
            if(CategoryTreeObject!=null)
            {
                request.setAttribute("categorys",JSONArray.parseArray(String.valueOf(CategoryTreeObject), CategoryVO.class));
            }else {
                CategoryVO categoryVO = new CategoryVO();
                categoryVO.setAreaCode("110000");
                RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), categoryVO);
                List<CategoryVO> categoryVOS = categoryService.queryTree(categoryVO.getAreaCode());
                if (CollectionUtils.isNotEmpty(categoryVOS)) {
                    stringRedisTemplate.opsForValue().set(categoryKey, JSONObject.toJSONString(categoryVOS));
                    stringRedisTemplate.expire(categoryKey, CATEGORY_KEY_MILLISECOND, TimeUnit.SECONDS);

                    request.setAttribute("categorys", categoryVOS);
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
        return "index";
    }


    @RequestMapping("/")
    public String defaultIndex(HttpServletRequest request)
    {
        return index(request);
    }

}
