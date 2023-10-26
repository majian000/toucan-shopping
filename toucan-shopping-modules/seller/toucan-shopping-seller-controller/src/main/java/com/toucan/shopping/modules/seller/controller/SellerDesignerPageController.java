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
import com.toucan.shopping.modules.seller.redis.SellerDesignerPageKey;
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
     * 只保存1个
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/onlySaveOne",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO onlySaveOne(@RequestBody RequestJsonVO requestJsonVO)
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
        String userMainId = "-1";
        try {

            SellerDesignerPageVO sellerDesignerPageVO = requestJsonVO.formatEntity(SellerDesignerPageVO.class);
            userMainId = String.valueOf(sellerDesignerPageVO.getUserMainId());
            boolean lockStatus = skylarkLock.lock(SellerDesignerPageKey.getSaveUpdateLockKey(userMainId), userMainId);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("保存失败,请稍后重试");
                return resultObjectVO;
            }
            SellerDesignerPage query = new SellerDesignerPage();
            query.setShopId(sellerDesignerPageVO.getShopId());
            query.setType(sellerDesignerPageVO.getType());
            query.setPosition(sellerDesignerPageVO.getPosition());
            SellerDesignerPage sellerDesignerPageRet = sellerDesignerPageService.queryLastOne(query);
            if(sellerDesignerPageRet==null) {
                designerPageId = idGenerator.id();

                SellerDesignerPage sellerDesignerPage = new SellerDesignerPage();
                BeanUtils.copyProperties(sellerDesignerPage, sellerDesignerPageVO);
                sellerDesignerPage.setId(designerPageId);
                sellerDesignerPage.setAppCode(requestJsonVO.getAppCode());
                sellerDesignerPage.setCreateDate(new Date());
                sellerDesignerPage.setCreaterId(String.valueOf(sellerDesignerPageVO.getUserMainId()));
                sellerDesignerPage.setDeleteStatus((short) 0);
                int row = sellerDesignerPageService.save(sellerDesignerPage);
                if (row <= 0) {
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg("保存失败,请稍后重试!");
                    return resultObjectVO;
                }
            }else{ //覆盖该最新对象
                sellerDesignerPageRet.setUpdaterId(String.valueOf(sellerDesignerPageVO.getUserMainId()));
                sellerDesignerPageRet.setUpdateDate(new Date());
                sellerDesignerPageRet.setPageJson(sellerDesignerPageVO.getPageJson());
                sellerDesignerPageRet.setDesignerVersion(sellerDesignerPageVO.getDesignerVersion());
                int row = sellerDesignerPageService.update(sellerDesignerPageRet);
                if (row <= 0) {
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg("保存失败,请稍后重试!");
                    return resultObjectVO;
                }

            }

        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("保存失败,请稍后重试!");
            logger.warn(e.getMessage(),e);
        }finally{
            skylarkLock.unLock(SellerDesignerPageKey.getSaveUpdateLockKey(userMainId), userMainId);
        }
        return resultObjectVO;
    }






}
