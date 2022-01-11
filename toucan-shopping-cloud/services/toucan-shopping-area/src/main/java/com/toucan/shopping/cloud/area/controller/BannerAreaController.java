package com.toucan.shopping.cloud.area.controller;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.area.entity.Banner;
import com.toucan.shopping.modules.area.entity.BannerArea;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 轮播图地区操作
 */
@RestController
@RequestMapping("/bannerArea")
public class BannerAreaController {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private BannerAreaService bannerAreaService;

    /**
     * 查询指定轮播图下所有地区关联
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/query/list",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryBannerAreaList(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            BannerAreaVO query = JSONObject.parseObject(requestJsonVO.getEntityJson(), BannerAreaVO.class);

            if(query.getBannerId()==null)
            {
                query.setBannerId(-1L);
            }

            List<BannerArea> bannerAreas = bannerAreaService.queryList(query);
            if(!CollectionUtils.isEmpty(bannerAreas))
            {
                resultObjectVO.setData(bannerAreas);
            }

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }



}
