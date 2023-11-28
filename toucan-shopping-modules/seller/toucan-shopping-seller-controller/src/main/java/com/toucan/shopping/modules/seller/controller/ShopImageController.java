package com.toucan.shopping.modules.seller.controller;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.seller.entity.ShopBanner;
import com.toucan.shopping.modules.seller.page.ShopBannerPageInfo;
import com.toucan.shopping.modules.seller.redis.ShopBannerKey;
import com.toucan.shopping.modules.seller.service.ShopBannerService;
import com.toucan.shopping.modules.seller.service.ShopImageService;
import com.toucan.shopping.modules.seller.vo.ShopBannerVO;
import com.toucan.shopping.modules.seller.vo.ShopImageVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


/**
 * 店铺图片
 */
@RestController
@RequestMapping("/shop/image")
public class ShopImageController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ShopImageService shopImageService;


    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private SkylarkLock skylarkLock;



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
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            ShopImageVO entity = JSONObject.parseObject(requestVo.getEntityJson(),ShopImageVO.class);
            if(entity.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到ID");
                return resultObjectVO;
            }

            resultObjectVO.setData(shopImageService.findById(entity.getId()));

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }




}
