package com.toucan.shopping.cloud.product.controller;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.product.entity.ShopProductImg;
import com.toucan.shopping.modules.product.page.ShopProductPageInfo;
import com.toucan.shopping.modules.product.service.ProductSkuService;
import com.toucan.shopping.modules.product.service.ShopProductApproveRecordService;
import com.toucan.shopping.modules.product.service.ShopProductImgService;
import com.toucan.shopping.modules.product.service.ShopProductService;
import com.toucan.shopping.modules.product.vo.*;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

/**
 * 店铺商品
 * @auth majian
 */
@RestController
@RequestMapping("/shopProduct")
public class ShopProductController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private SkylarkLock skylarkLock;

    @Autowired
    private ProductSkuService productSkuService;

    @Autowired
    private ShopProductService shopProductService;

    @Autowired
    private ShopProductImgService shopProductImgService;

    @Autowired
    private ShopProductApproveRecordService shopProductApproveRecordService;






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
            logger.warn("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }
        if(requestJsonVO.getAppCode()==null)
        {
            logger.warn("没有找到应用编码: param:"+ JSONObject.toJSONString(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到应用编码!");
            return resultObjectVO;
        }
        try {
            ShopProductPageInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopProductPageInfo.class);
            PageInfo<ShopProductVO> pageInfo =  shopProductService.queryListPage(queryPageInfo);

            if(pageInfo.getTotal()!=null&&pageInfo.getTotal().longValue()>0)
            {
                List<Long> shopProductIdList = new LinkedList<>();
                for(ShopProductVO shopProductVO : pageInfo.getList())
                {
                    shopProductVO.setPreviewPhotoPaths(new LinkedList<>());

                    shopProductIdList.add(shopProductVO.getId());
                }
                ShopProductImgVO shopProductImgVO = new ShopProductImgVO();
                shopProductImgVO.setShopProductIdList(shopProductIdList);
                List<ShopProductImg> shopProductImgs = shopProductImgService.queryList(shopProductImgVO);
                if(CollectionUtils.isNotEmpty(shopProductImgs))
                {
                    for(ShopProductImg shopProductImg:shopProductImgs)
                    {
                        for(ShopProductVO shopProductVO:pageInfo.getList()) {
                            if (shopProductImg.getShopProductId() != null &&
                                    shopProductImg.getShopProductId().longValue()==shopProductVO.getId().longValue())
                            {
                                //如果是商品主图
                                if(shopProductImg.getImgType().intValue()==1)
                                {
                                    shopProductVO.setMainPhotoFilePath(shopProductImg.getFilePath());
                                }else if(shopProductImg.getImgType().intValue()==2){
                                    shopProductVO.getPreviewPhotoPaths().add(shopProductImg.getFilePath());
                                }
                                break;
                            }

                        }
                    }
                }
            }

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
     * 根据ID查询
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/id",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryByShopProductId(@RequestBody RequestJsonVO requestJsonVO)
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
            logger.warn("没有找到应用编码: param:"+ JSONObject.toJSONString(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到应用编码!");
            return resultObjectVO;
        }
        try {
            ShopProductVO shopProductVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopProductVO.class);
            if(shopProductVO==null||shopProductVO.getId()==null||shopProductVO.getId().longValue()==-1)
            {
                logger.warn("没有找到ID: param:"+ JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到ID!");
                return resultObjectVO;
            }
            ShopProductVO queryShopProductVO = new ShopProductVO();
            queryShopProductVO.setId(shopProductVO.getId());
            List<ShopProductVO> shopProductVOS = shopProductService.queryList(queryShopProductVO);

            if(CollectionUtils.isNotEmpty(shopProductVOS)) {

                shopProductVO = shopProductVOS.get(0);
                shopProductVO.setPreviewPhotoPaths(new LinkedList<>());

                ProductSkuVO queryProductSku = new ProductSkuVO();
                queryProductSku.setShopProductId(shopProductVO.getId());
                //查询SKU
                List<ProductSkuVO> productSkuVOS = productSkuService.queryList(queryProductSku);
                shopProductVO.setProductSkuVOList(productSkuVOS);

                //查询商品图片
                ShopProductImgVO shopProductImgVO = new ShopProductImgVO();
                shopProductImgVO.setShopProductId(shopProductVO.getId());
                List<ShopProductImg> shopProductImgs = shopProductImgService.queryList(shopProductImgVO);
                if (CollectionUtils.isNotEmpty(shopProductImgs)) {
                    for (ShopProductImg shopProductImg : shopProductImgs) {
                        //如果是商品主图
                        if (shopProductImg.getImgType().intValue() == 1) {
                            shopProductVO.setMainPhotoFilePath(shopProductImg.getFilePath());
                        } else  if (shopProductImg.getImgType().intValue() == 2) {
                            shopProductVO.getPreviewPhotoPaths().add(shopProductImg.getFilePath());
                        }
                    }
                }
            }
            resultObjectVO.setData(shopProductVOS);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }









}
