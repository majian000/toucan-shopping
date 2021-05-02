package com.toucan.shopping.cloud.area.controller;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.area.entity.Banner;
import com.toucan.shopping.modules.area.entity.BannerArea;
import com.toucan.shopping.modules.area.service.BannerAreaService;
import com.toucan.shopping.modules.area.service.BannerService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 用户端端轮播图操作
 */
@RestController
@RequestMapping("/banner/user")
public class UserBannerController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private BannerService bannerService;

    @Autowired
    private BannerAreaService bannerAreaService;


    @Autowired
    private IdGenerator idGenerator;




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
