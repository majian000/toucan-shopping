package com.toucan.shopping.modules.seller.controller;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.seller.entity.SellerDesignerPage;
import com.toucan.shopping.modules.seller.entity.ShopBanner;
import com.toucan.shopping.modules.seller.page.ShopBannerPageInfo;
import com.toucan.shopping.modules.seller.redis.ShopBannerKey;
import com.toucan.shopping.modules.seller.service.SellerDesignerPageService;
import com.toucan.shopping.modules.seller.service.ShopBannerService;
import com.toucan.shopping.modules.seller.vo.SellerDesignerPageVO;
import com.toucan.shopping.modules.seller.vo.ShopBannerVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


/**
 * 设计器页面操作
 */
@RestController
@RequestMapping("/seller/designer/page")
public class SellerDesignerPageController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SellerDesignerPageService sellerDesignerPageService;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private SkylarkLock skylarkLock;

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

        Long designerPageId = -1L;
        try {
            designerPageId = idGenerator.id();

            SellerDesignerPageVO sellerDesignerPageVO = requestJsonVO.formatEntity(SellerDesignerPageVO.class);
            SellerDesignerPage sellerDesignerPage = new SellerDesignerPage();
            BeanUtils.copyProperties(sellerDesignerPage,sellerDesignerPageVO);
            sellerDesignerPage.setId(designerPageId);
            sellerDesignerPage.setCreateDate(new Date());
            sellerDesignerPage.setDeleteStatus((short)0);
            int row = sellerDesignerPageService.save(sellerDesignerPage);
            if (row <= 0) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("保存失败,请稍后重试!");
                return resultObjectVO;
            }

        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("保存失败,请稍后重试!");
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }






}
