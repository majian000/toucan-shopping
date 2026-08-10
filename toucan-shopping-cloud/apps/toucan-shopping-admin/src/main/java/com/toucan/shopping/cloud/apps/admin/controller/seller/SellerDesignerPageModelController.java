package com.toucan.shopping.cloud.apps.admin.controller.seller;


import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.seller.api.SellerDesignerPageModelServiceAPI;
import com.toucan.shopping.cloud.seller.api.SellerShopServiceAPI;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.layui.vo.TableVO;
import com.toucan.shopping.modules.seller.page.SellerDesignerPageModelPageInfo;
import com.toucan.shopping.modules.seller.util.ShopUtils;
import com.toucan.shopping.modules.seller.vo.SellerDesignerPageModelVO;
import com.toucan.shopping.modules.seller.vo.SellerShopVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *  卖家设计器页面模型管理
 */
@RestController
@RequestMapping("/seller/designer/page/model")
public class SellerDesignerPageModelController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private SellerDesignerPageModelServiceAPI sellerDesignerPageModelService;

    @Autowired
    private SellerShopServiceAPI sellerShopService;



    /**
     * 查询列表
     * @param pageInfo
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:seller:pageModel:list"})
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public TableVO list(SellerDesignerPageModelPageInfo pageInfo)
    {
        TableVO tableVO = new TableVO();
        try {
            //排除预览页
            pageInfo.setExTypes(new LinkedList<>());
            pageInfo.getExTypes().add(1);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),pageInfo);
            ResultObjectVO resultObjectVO = sellerDesignerPageModelService.queryListPage(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                if(resultObjectVO.getData()!=null)
                {
                    SellerDesignerPageModelPageInfo retPageInfo = resultObjectVO.formatData(SellerDesignerPageModelPageInfo.class);
                    tableVO.setCount(retPageInfo.getTotal());
                    if(tableVO.getCount()>0) {
                        List<SellerDesignerPageModelVO> sellerDesignerPageModels = retPageInfo.getList();
                        if(CollectionUtils.isNotEmpty(sellerDesignerPageModels))
                        {
                            List<Long> shopIdList = sellerDesignerPageModels.stream().map(SellerDesignerPageModelVO::getShopId).collect(Collectors.toList());
                            if(CollectionUtils.isNotEmpty(shopIdList)) {
                                SellerShopVO queryShopVO = new SellerShopVO();
                                queryShopVO.setIdList(shopIdList);
                                requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryShopVO);
                                resultObjectVO = sellerShopService.findByIdList(requestJsonVO);
                                if(resultObjectVO.isSuccess())
                                {
                                    List<SellerShopVO> sellerShopVOS = resultObjectVO.formatDataList(SellerShopVO.class);
                                    if(CollectionUtils.isNotEmpty(sellerShopVOS))
                                    {
                                        for(SellerDesignerPageModelVO sellerDesignerPageModelVO:sellerDesignerPageModels)
                                        {
                                            for(SellerShopVO sellerShopVO:sellerShopVOS)
                                            {
                                                if(sellerDesignerPageModelVO.getShopId().longValue()==sellerShopVO.getId().longValue())
                                                {
                                                    sellerDesignerPageModelVO.setShopName(sellerShopVO.getName());
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            for(SellerDesignerPageModelVO sellerDesignerPageModelVO:sellerDesignerPageModels)
                            {
                                //设置PC首页预览页
                                sellerDesignerPageModelVO.setPcIndexPage(toucan.getShoppingPC().getBasePath()+toucan.getShoppingPC().getShopPcIndexReleasePage()+"/"+ ShopUtils.encShopId(String.valueOf(sellerDesignerPageModelVO.getShopId()))+"/"+String.valueOf(sellerDesignerPageModelVO.getShopId()));
                            }
                        }
                        tableVO.setData(sellerDesignerPageModels);
                    }
                }
            }
        }catch(Exception e)
        {
            tableVO.setMsg("请重试");
            tableVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return tableVO;
    }


    /**
     * 删除
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:seller:pageModel:delete"})
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public ResultObjectVO deleteById(@RequestBody SellerDesignerPageModelVO entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(entity.getId() == null)
            {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }

            String entityJson = JSONObject.toJSONString(entity);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(toucan.getAppCode());
            requestVo.setEntityJson(entityJson);
            resultObjectVO = sellerDesignerPageModelService.deleteByIdForAdmin(requestVo);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



}
