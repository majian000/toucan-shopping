package com.toucan.shopping.cloud.apps.web.controller.index;

import com.toucan.shopping.cloud.apps.web.service.IndexService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 首页控制器
 */
@Controller("pageIndexController")
public class IndexPageController {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private IndexService indexService;



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




    @RequestMapping("/index")
    public String index(HttpServletRequest request)
    {
        return "/htmls/release/index";
    }


    @RequestMapping("/preview/index")
    public String previewIndex(HttpServletRequest request)
    {
        return "/htmls/preview/index";
    }


    @RequestMapping("/dynamic/index")
    public String dynamicIndex(HttpServletRequest request)
    {
        //查询地区树
//        queryAreaTree(request);

        //查询轮播图
//        request.setAttribute("banners",indexService.queryBanners());

        //查询类别列表
        request.setAttribute("categorys", indexService.queryCategorys());

        //查询顶部栏目

        return "/index";
    }


    @RequestMapping("/")
    public String defaultIndex(HttpServletRequest request)
    {
        return index(request);
    }

}
