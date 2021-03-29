package com.toucan.shopping.area.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.area.entity.Area;
import com.toucan.shopping.area.entity.Banner;
import com.toucan.shopping.area.entity.BannerArea;
import com.toucan.shopping.area.service.AreaService;
import com.toucan.shopping.area.service.BannerAreaService;
import com.toucan.shopping.area.service.BannerService;
import com.toucan.shopping.area.vo.BannerAreaVO;
import com.toucan.shopping.area.vo.BannerVO;
import com.toucan.shopping.common.generator.IdGenerator;
import com.toucan.shopping.common.vo.RequestJsonVO;
import com.toucan.shopping.common.vo.ResultObjectVO;
import com.toucan.shopping.common.vo.ResultVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
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
public class AdminBannerController {

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






}
