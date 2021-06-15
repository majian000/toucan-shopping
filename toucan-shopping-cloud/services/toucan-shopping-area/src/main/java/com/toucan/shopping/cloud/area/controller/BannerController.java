package com.toucan.shopping.cloud.area.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.area.cache.service.BannerRedisService;
import com.toucan.shopping.modules.area.entity.Area;
import com.toucan.shopping.modules.area.entity.Banner;
import com.toucan.shopping.modules.area.entity.BannerArea;
import com.toucan.shopping.modules.area.page.BannerPageInfo;
import com.toucan.shopping.modules.area.service.AreaService;
import com.toucan.shopping.modules.area.service.BannerAreaService;
import com.toucan.shopping.modules.area.service.BannerService;
import com.toucan.shopping.modules.area.vo.BannerAreaVO;
import com.toucan.shopping.modules.area.vo.BannerVO;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 轮播图操作
 */
@RestController
@RequestMapping("/banner")
public class BannerController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private BannerService bannerService;

    @Autowired
    private BannerAreaService bannerAreaService;


    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private AreaService areaService;

    @Autowired
    private BannerRedisService bannerRedisService;

    @Value("${fastdfs.http.url}")
    private String fastDfsHttpUrl;

    /**
     * 保存轮播图
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/save",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO save(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            logger.warn("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }
        if(requestJsonVO.getAppCode()==null)
        {
            logger.warn("没有找到对象编码: param:"+ JSONObject.toJSONString(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到对象编码!");
            return resultObjectVO;
        }

        Long bannerId = -1L;
        try {
            bannerId = idGenerator.id();
            BannerVO bannerVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), BannerVO.class);

            if(bannerVO.getAreaCodeArray()==null||bannerVO.getAreaCodeArray().length<=0)
            {
                logger.warn("关联地区编码为空 param:"+ requestJsonVO.getEntityJson());
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("关联地区编码为空!");
                return resultObjectVO;
            }

            Banner banner = new Banner();
            BeanUtils.copyProperties(banner,bannerVO);
            banner.setId(bannerId);
            banner.setCreateDate(new Date());
            int row = bannerService.save(banner);
            if (row <= 0) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("保存失败,请重试!");
                return resultObjectVO;
            }

            BannerArea[] bannerAreas=new BannerArea[bannerVO.getAreaCodeArray().length];
            for(int i=0;i<bannerVO.getAreaCodeArray().length;i++)
            {
                String areaCode = bannerVO.getAreaCodeArray()[i];
                BannerArea bannerArea = new BannerArea();
                bannerArea.setId(idGenerator.id());
                bannerArea.setAreaCode(areaCode);
                bannerArea.setBannerId(banner.getId());
                bannerArea.setCreateDate(new Date());
                bannerAreas[i]=bannerArea;
            }
            //保存轮播图与地区关联
            row = bannerAreaService.saves(bannerAreas);
            if(row<=0)
            {
                throw new IllegalArgumentException("保存地区轮播图关联失败");
            }
        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("保存失败,请重试!");
            logger.warn(e.getMessage(),e);

            //地区与轮播图有一条关联保存失败,回滚数据
            bannerService.deleteById(bannerId);
            bannerAreaService.deleteByBannerId(bannerId);
        }
        return resultObjectVO;
    }




    /**
     * 批量刷新缓存
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/flush/index/cache",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO flushWebIndexCache(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,没有找到实体对象");
            return resultObjectVO;
        }

        try {
            List<BannerVO> banners = JSONObject.parseArray(requestVo.getEntityJson(),BannerVO.class);
            if(CollectionUtils.isEmpty(banners))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,没有找到ID");
                return resultObjectVO;
            }
            List<ResultObjectVO> resultObjectVOList = new ArrayList<ResultObjectVO>();
            for(BannerVO banner:banners) {
                if(banner.getId()!=null) {
                    ResultObjectVO appResultObjectVO = new ResultObjectVO();
                    appResultObjectVO.setData(banner);
                    try{
                        BannerVO query = new BannerVO();
                        query.setId(banner.getId());
                        List<BannerVO> bannerVOS = bannerService.queryList(query);
                        if(CollectionUtils.isNotEmpty(bannerVOS))
                        {
                            BannerVO bannerVO = bannerVOS.get(0);
                            //查询出所有轮播图地区关联
                            BannerArea queryBannerArea = new BannerArea();
                            queryBannerArea.setBannerId(bannerVO.getId());
                            List<BannerArea> bannerAreas = bannerAreaService.queryList(queryBannerArea);
                            String[] areaCodeArray = new String[bannerAreas.size()];
                            for(int i=0;i<bannerAreas.size();i++)
                            {
                                areaCodeArray[i]= bannerAreas.get(i).getAreaCode();
                            }
                            //设置关联地区编码
                            bannerVO.setAreaCodeArray(areaCodeArray);
                            if(bannerVO.getImgPath()!=null) {
                                bannerVO.setHttpImgPath(fastDfsHttpUrl +bannerVO.getImgPath());
                            }
                            //刷新缓存
                            bannerRedisService.flushWebIndexCache(bannerVO);
                        }
                    }catch(Exception e)
                    {
                        logger.warn("刷新轮播图缓存失败，id:{}",banner.getId());
                        logger.warn(e.getMessage(),e);
                        resultObjectVO.setCode(ResultVO.FAILD);
                        resultObjectVO.setMsg("请求失败,请重试!");
                        continue;
                    }
                }
            }
            resultObjectVO.setData(resultObjectVOList);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }



    /**
     * 清空首页缓存
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/clear/index/cache",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO clearWebIndexCache(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            resultObjectVO.setData(bannerRedisService.clearWebIndexBanner());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }

    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/id",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO findById(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,没有找到实体对象");
            return resultObjectVO;
        }

        try {
            BannerVO bannerVO = JSONObject.parseObject(requestVo.getEntityJson(),BannerVO.class);
            if(bannerVO.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,没有找到ID");
                return resultObjectVO;
            }

            //查询是否存在该对象
            BannerVO query=new BannerVO();
            query.setId(bannerVO.getId());
            List<BannerVO> banners = bannerService.queryList(query);
            if(CollectionUtils.isEmpty(banners))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,不存在!");
                return resultObjectVO;
            }
            //查询出所有轮播图地区关联
            BannerArea queryBannerArea = new BannerArea();
            queryBannerArea.setBannerId(bannerVO.getId());
            List<BannerArea> bannerAreas = bannerAreaService.queryList(queryBannerArea);
            if(CollectionUtils.isNotEmpty(bannerAreas)) {
                String[] areaCodeArray = new String[bannerAreas.size()];
                for(int i=0;i<bannerAreas.size();i++)
                {
                    areaCodeArray[i]= bannerAreas.get(i).getAreaCode();
                }
                Area queryArea = new Area();
                queryArea.setCodeArray(areaCodeArray);

                //设置这个轮播图下关联的所有地区
                banners.get(0).setAreas(areaService.queryList(queryArea));
            }

            resultObjectVO.setData(banners);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }



    /**
     * 編輯
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/update",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO update(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,没有找到实体对象");
            return resultObjectVO;
        }

        try {
            BannerVO entity = JSONObject.parseObject(requestVo.getEntityJson(),BannerVO.class);

            if(StringUtils.isEmpty(entity.getTitle()))
            {
                logger.info("标题为空 param:"+ JSONObject.toJSONString(entity));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("标题不能为空!");
                return resultObjectVO;
            }
            if(StringUtils.isEmpty(entity.getClickPath()))
            {
                logger.info("点击连接为空 param:"+ JSONObject.toJSONString(entity));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("点击连接不能为空!");
                return resultObjectVO;
            }

            if(entity.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,请传入ID");
                return resultObjectVO;
            }

            entity.setUpdateDate(new Date());
            int row = bannerService.update(entity);
            if (row < 1) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,请重试!");
                return resultObjectVO;
            }

            //删除轮播图地区关联
            bannerAreaService.deleteByBannerId(entity.getId());

            BannerArea[] bannerAreas=new BannerArea[entity.getAreaCodeArray().length];
            for(int i=0;i<entity.getAreaCodeArray().length;i++)
            {
                String areaCode = entity.getAreaCodeArray()[i];
                BannerArea bannerArea = new BannerArea();
                bannerArea.setId(idGenerator.id());
                bannerArea.setAreaCode(areaCode);
                bannerArea.setBannerId(entity.getId());
                bannerArea.setCreateDate(new Date());
                bannerAreas[i]=bannerArea;
            }
            //保存轮播图与地区关联
            row = bannerAreaService.saves(bannerAreas);
            if(row<=0)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,保存地区轮播图关联失败");
            }

            resultObjectVO.setData(entity);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }



    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/list/page",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            logger.info("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }
        if(requestJsonVO.getAppCode()==null)
        {
            logger.info("没有找到对象: param:"+ JSONObject.toJSONString(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到对象!");
            return resultObjectVO;
        }
        try {
            BannerPageInfo adminPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), BannerPageInfo.class);
            PageInfo<BannerVO> pageInfo =  bannerService.queryListPage(adminPageInfo);
            resultObjectVO.setData(pageInfo);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }




    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/list",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryList(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            logger.info("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }
        if(requestJsonVO.getAppCode()==null)
        {
            logger.info("没有找到对象: param:"+ JSONObject.toJSONString(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到对象!");
            return resultObjectVO;
        }
        try {
            BannerVO bannerVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), BannerVO.class);
            List<BannerVO> bannerVOS = bannerService.queryList(bannerVO);
            if(CollectionUtils.isNotEmpty(bannerVOS))
            {
                for(BannerVO bv:bannerVOS)
                {
                    //查询出所有轮播图地区关联
                    BannerArea queryBannerArea = new BannerArea();
                    queryBannerArea.setBannerId(bannerVO.getId());
                    List<BannerArea> bannerAreas = bannerAreaService.queryList(queryBannerArea);
                    if(CollectionUtils.isNotEmpty(bannerAreas)) {
                        String[] areaCodeArray = new String[bannerAreas.size()];
                        for(int i=0;i<bannerAreas.size();i++)
                        {
                            areaCodeArray[i]= bannerAreas.get(i).getAreaCode();
                        }
                        bv.setAreaCodeArray(areaCodeArray);;
                    }
                }
            }
            resultObjectVO.setData(bannerVOS);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }


    /**
     * 查询PD端首页列表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/index/list",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryIndexList(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            logger.info("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }
        if(requestJsonVO.getAppCode()==null)
        {
            logger.info("没有找到对象: param:"+ JSONObject.toJSONString(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到对象!");
            return resultObjectVO;
        }
        try {
            BannerVO bannerVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), BannerVO.class);
            //查询首页轮播图
            List<BannerVO> bannerVOS = bannerService.queryIndexList(bannerVO);
            if(CollectionUtils.isNotEmpty(bannerVOS))
            {
                for(BannerVO bv:bannerVOS)
                {
                    //查询出所有轮播图地区关联
                    BannerArea queryBannerArea = new BannerArea();
                    queryBannerArea.setBannerId(bannerVO.getId());
                    List<BannerArea> bannerAreas = bannerAreaService.queryList(queryBannerArea);
                    if(CollectionUtils.isNotEmpty(bannerAreas)) {
                        String[] areaCodeArray = new String[bannerAreas.size()];
                        for(int i=0;i<bannerAreas.size();i++)
                        {
                            areaCodeArray[i]= bannerAreas.get(i).getAreaCode();
                        }
                        bv.setAreaCodeArray(areaCodeArray);;
                    }
                    bv.setHttpImgPath(fastDfsHttpUrl+bv.getImgPath());
                }
            }
            resultObjectVO.setData(bannerVOS);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }

    /**
     * 删除指定
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/id",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteById(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,没有找到实体对象");
            return resultObjectVO;
        }

        try {
            Banner entity = JSONObject.parseObject(requestVo.getEntityJson(),Banner.class);
            if(entity.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,没有找到ID");
                return resultObjectVO;
            }

            //查询是否存在该数据
            BannerVO query=new BannerVO();
            query.setId(entity.getId());
            List<BannerVO> adminList = bannerService.queryList(query);
            if(CollectionUtils.isEmpty(adminList))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,轮播图不存在!");
                return resultObjectVO;
            }

            int row = bannerService.deleteById(entity.getId());
            if (row < 1) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,请重试!");
                return resultObjectVO;
            }


            //删除与地区关联
            bannerAreaService.deleteByBannerId(entity.getId());


            resultObjectVO.setData(entity);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 批量删除
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/ids",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteByIds(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,没有找到实体对象");
            return resultObjectVO;
        }

        try {
            List<Banner> banners = JSONObject.parseArray(requestVo.getEntityJson(),Banner.class);
            if(CollectionUtils.isEmpty(banners))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,没有找到ID");
                return resultObjectVO;
            }
            List<ResultObjectVO> resultObjectVOList = new ArrayList<ResultObjectVO>();
            for(Banner banner:banners) {
                if(banner.getId()!=null) {
                    ResultObjectVO appResultObjectVO = new ResultObjectVO();
                    appResultObjectVO.setData(banner);

                    int row = bannerService.deleteById(banner.getId());
                    if (row < 1) {
                        logger.warn("删除轮播图失败，id:{}",banner.getId());
                        resultObjectVO.setCode(ResultVO.FAILD);
                        resultObjectVO.setMsg("请求失败,请重试!");
                        continue;
                    }
                    //删除与地区关联
                    bannerAreaService.deleteByBannerId(banner.getId());

                }
            }
            resultObjectVO.setData(resultObjectVOList);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }



}
