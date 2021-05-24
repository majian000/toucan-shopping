package com.toucan.shopping.cloud.area.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.area.entity.Area;
import com.toucan.shopping.modules.area.entity.Banner;
import com.toucan.shopping.modules.area.entity.BannerArea;
import com.toucan.shopping.modules.area.service.AreaService;
import com.toucan.shopping.modules.area.service.BannerAreaService;
import com.toucan.shopping.modules.area.service.BannerService;
import com.toucan.shopping.modules.area.vo.BannerAreaVO;
import com.toucan.shopping.modules.area.vo.BannerVO;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 管理端轮播图操作
 */
@RestController
@RequestMapping("/banner/admin")
public class BannerController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private BannerService bannerService;

    @Autowired
    private BannerAreaService bannerAreaService;


    @Autowired
    private IdGenerator idGenerator;



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
            logger.warn("没有找到应用编码: param:"+ JSONObject.toJSON(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到应用编码!");
            return resultObjectVO;
        }

        Long bannerId = -1L;
        try {
            bannerId = idGenerator.id();
            BannerVO bannerVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), BannerVO.class);

            if(StringUtils.isEmpty(bannerVO.getAppCode()))
            {
                logger.warn("编码为空 param:"+ requestJsonVO.getEntityJson());
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("编码不能为空!");
                return resultObjectVO;
            }
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

            //保存轮播图与地区关联
            for(int i=0;i<bannerVO.getAreaCodeArray().length;i++)
            {
                String areaCode = bannerVO.getAreaCodeArray()[i];
                BannerArea bannerArea = new BannerArea();
                bannerArea.setId(idGenerator.id());
                bannerArea.setAreaCode(areaCode);
                bannerArea.setBannerId(banner.getId());
                bannerArea.setAppCode(requestJsonVO.getAppCode());
                bannerArea.setCreateDate(new Date());
                row = bannerAreaService.save(bannerArea);
                if(row<=0)
                {
                    throw new IllegalArgumentException("保存地区轮播图关联失败");
                }
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
            logger.info("没有找到应用: param:"+ JSONObject.toJSONString(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到应用!");
            return resultObjectVO;
        }
        try {
            BannerVO bannerVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), BannerVO.class);
            if(bannerVO.getAreaCodeArray()!=null&&bannerVO.getAreaCodeArray().length>0)
            {
                List<Long> bannerIdList = new ArrayList<Long>();
                for(int i=0;i<bannerVO.getAreaCodeArray().length;i++)
                {
                    BannerArea bannerArea = new BannerArea();
                    bannerArea.setAppCode(requestJsonVO.getAppCode());
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
            resultObjectVO.setData(bannerService.queryList(bannerVO));
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }




}
