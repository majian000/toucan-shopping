package com.toucan.shopping.cloud.apps.seller.web.controller.freightTemplate;

import com.toucan.shopping.cloud.apps.seller.web.controller.BaseController;
import com.toucan.shopping.cloud.common.data.api.feign.service.FeignAreaService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignShopProductApproveService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignShopProductService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignFreightTemplateService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignSellerShopService;
import com.toucan.shopping.modules.area.vo.AreaVO;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.product.vo.ShopProductApproveVO;
import com.toucan.shopping.modules.product.vo.ShopProductVO;
import com.toucan.shopping.modules.seller.constant.FreightTemplateConstant;
import com.toucan.shopping.modules.seller.entity.FreightTemplateAreaRule;
import com.toucan.shopping.modules.seller.entity.SellerShop;
import com.toucan.shopping.modules.seller.page.FreightTemplatePageInfo;
import com.toucan.shopping.modules.seller.util.FreightTemplateUtils;
import com.toucan.shopping.modules.seller.vo.FreightTemplateAreaRuleVO;
import com.toucan.shopping.modules.seller.vo.FreightTemplateVO;
import com.toucan.shopping.modules.seller.vo.SellerShopVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 运费模板
 * @author majian
 * @date 2022-9-21 14:15:24
 */
@Controller("freightTemplateApiController")
@RequestMapping("/api/freightTemplate")
public class FreightTemplateApiController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignFreightTemplateService feignFreightTemplateService;

    @Autowired
    private FeignSellerShopService feignSellerShopService;

    @Autowired
    private FeignShopProductApproveService feignShopProductApproveService;

    @Autowired
    private FeignShopProductService feignShopProductService;

    @Autowired
    private FeignAreaService feignAreaService;


    /**
     * 查询列表
     * @param pageInfo
     * @return
     */
    @UserAuth
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO list(HttpServletRequest httpServletRequest,@RequestBody FreightTemplatePageInfo pageInfo)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try{
            if(pageInfo==null)
            {
                pageInfo = new FreightTemplatePageInfo();
            }
            String userMainId = UserAuthHeaderUtil.getUserMainId(httpServletRequest.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            if(StringUtils.isEmpty(userMainId))
            {
                logger.warn("查询商品审核 没有找到用户ID {} ",userMainId);
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("查询失败,用户ID不能为空");
                return resultObjectVO;
            }
            pageInfo.setUserMainId(Long.parseLong(userMainId));
            SellerShop querySellerShop = new SellerShop();
            querySellerShop.setUserMainId(Long.parseLong(userMainId));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), querySellerShop);
            resultObjectVO = feignSellerShopService.findByUser(requestJsonVO.sign(),requestJsonVO);
            if(resultObjectVO.isSuccess()&&resultObjectVO.getData()!=null) {
                SellerShopVO sellerShopVO = resultObjectVO.formatData(SellerShopVO.class);
                if(sellerShopVO!=null) {
                    pageInfo.setShopId(sellerShopVO.getId());
                }
            }
            if(pageInfo.getShopId()==null)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("查询失败,没有找到店铺");
                return resultObjectVO;
            }
            requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), pageInfo);
            resultObjectVO = feignFreightTemplateService.queryListPage(requestJsonVO);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("查询失败,请稍后重试");
        }

        return resultObjectVO;
    }

    /**
     * 保存运费模板
     * @return
     */
    @UserAuth
    @RequestMapping(value="/save",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO save(HttpServletRequest request, @RequestBody FreightTemplateVO freightTemplateVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            String userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            if(StringUtils.isEmpty(userMainId))
            {
                logger.warn("保存运费模板失败 没有找到用户ID {} ",userMainId);
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("保存失败,请稍后重试");
                return resultObjectVO;
            }
            freightTemplateVO.setUserMainId(Long.parseLong(userMainId));
            SellerShop querySellerShop = new SellerShop();
            querySellerShop.setUserMainId(Long.parseLong(userMainId));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), querySellerShop);
            resultObjectVO = feignSellerShopService.findByUser(requestJsonVO.sign(),requestJsonVO);
            if(resultObjectVO.isSuccess()&&resultObjectVO.getData()!=null) {
                SellerShopVO sellerShopVO = resultObjectVO.formatData(SellerShopVO.class);
                if(sellerShopVO!=null) {
                    freightTemplateVO.setShopId(sellerShopVO.getId());
                }
            }
            freightTemplateVO.setCreateDate(new Date());

            List<String> transportModels= new LinkedList<>();
            if(StringUtils.isNotEmpty(freightTemplateVO.getTransportModelExpress()))
            {
                transportModels.add(freightTemplateVO.getTransportModelExpress());
            }
            if(StringUtils.isNotEmpty(freightTemplateVO.getTransportModelEms()))
            {
                transportModels.add(freightTemplateVO.getTransportModelEms());
            }
            if(StringUtils.isNotEmpty(freightTemplateVO.getTransportModelOrdinaryMail()))
            {
                transportModels.add(freightTemplateVO.getTransportModelOrdinaryMail());
            }
            if(CollectionUtils.isNotEmpty(transportModels))
            {
                freightTemplateVO.setTransportModel(transportModels.stream().map(String::valueOf).collect(Collectors.joining(",")));
            }

            if(freightTemplateVO.getFreightStatus().shortValue()==1) {
                List<String> cityNameList = new LinkedList<>();
                //查询出快递的所选城市
                if (CollectionUtils.isNotEmpty(freightTemplateVO.getExpressAreaRules())) {
                    for (FreightTemplateAreaRuleVO freightTemplateAreaRuleVO : freightTemplateVO.getExpressAreaRules()) {
                        String selectAreas = freightTemplateAreaRuleVO.getSelectAreas();
                        if (StringUtils.isNotEmpty(selectAreas)) {
                            cityNameList.addAll(Arrays.asList(selectAreas.split(FreightTemplateConstant.VIEW_SELECT_AREA_SPLIT)));
                        }
                    }
                }
                //查询出EMS的所选城市
                if (CollectionUtils.isNotEmpty(freightTemplateVO.getEmsAreaRules())) {
                    for (FreightTemplateAreaRuleVO freightTemplateAreaRuleVO : freightTemplateVO.getEmsAreaRules()) {
                        String selectAreas = freightTemplateAreaRuleVO.getSelectAreas();
                        if (StringUtils.isNotEmpty(selectAreas)) {
                            cityNameList.addAll(Arrays.asList(selectAreas.split(FreightTemplateConstant.VIEW_SELECT_AREA_SPLIT)));
                        }
                    }
                }
                //查询出平邮的所选城市
                if (CollectionUtils.isNotEmpty(freightTemplateVO.getOrdinaryMailAreaRules())) {
                    for (FreightTemplateAreaRuleVO freightTemplateAreaRuleVO : freightTemplateVO.getOrdinaryMailAreaRules()) {
                        String selectAreas = freightTemplateAreaRuleVO.getSelectAreas();
                        if (StringUtils.isNotEmpty(selectAreas)) {
                            cityNameList.addAll(Arrays.asList(selectAreas.split(FreightTemplateConstant.VIEW_SELECT_AREA_SPLIT)));
                        }
                    }
                }
                if (CollectionUtils.isNotEmpty(cityNameList)) {
                    AreaVO queryAreaVO = new AreaVO();
                    queryAreaVO.setCityNameList(cityNameList);
                    requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryAreaVO);
                    resultObjectVO = feignAreaService.queryCityListByNames(requestJsonVO);
                    if (resultObjectVO.isSuccess()) {

                        List<AreaVO> areaVOS = resultObjectVO.formatDataList(AreaVO.class);
                        if (CollectionUtils.isNotEmpty(areaVOS)) {
                            Map<String, String> cityCodeToProvinceCode = new HashMap<>(); //市级编码对应的省级编码
                            Map<String, String> cityNameToCityCode = new HashMap<>(); //市级名称对应市级编码
                            Map<String, String> cityNameToProvinceName = new HashMap<>(); //市级名称对应省级名称
                            for (AreaVO areaVO : areaVOS) {
                                cityNameToCityCode.put(areaVO.getCity(), areaVO.getCode());
                                cityCodeToProvinceCode.put(areaVO.getCode(), areaVO.getParentCode());
                                cityNameToProvinceName.put(areaVO.getCity(),areaVO.getProvince());
                            }

                            freightTemplateVO.setCityNameToCityCode(cityNameToCityCode);
                            freightTemplateVO.setCityCodeToProvinceCode(cityCodeToProvinceCode);
                            freightTemplateVO.setCityNameToProvinceName(cityNameToProvinceName);
                        }
                    }
                }
            }
            requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), freightTemplateVO);
            resultObjectVO = feignFreightTemplateService.save(requestJsonVO);

        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }




    /**
     * 根据ID查询
     * @return
     */
    @UserAuth
    @RequestMapping(value="/findById",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO findById(HttpServletRequest request,@RequestBody FreightTemplateVO freightTemplateVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        String userMainId="-1";
        try {
            if(freightTemplateVO.getId()==null)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("ID不能为空");
                return resultObjectVO;
            }
            userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));

            FreightTemplateVO queryVO = new FreightTemplateVO();
            queryVO.setId(freightTemplateVO.getId());
            queryVO.setUserMainId(Long.parseLong(userMainId));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryVO);
            resultObjectVO = feignFreightTemplateService.findByIdAndUserMainId(requestJsonVO);

        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



    /**
     * 修改运费模板
     * @return
     */
    @UserAuth
    @RequestMapping(value="/update",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO update(HttpServletRequest request, @RequestBody FreightTemplateVO freightTemplateVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(freightTemplateVO.getId()==null)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("保存失败,没有找到模板ID");
                return resultObjectVO;
            }
            String userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            if(StringUtils.isEmpty(userMainId))
            {
                logger.warn("保存运费模板失败 没有找到用户ID {} ",userMainId);
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("保存失败,请稍后重试");
                return resultObjectVO;
            }
            freightTemplateVO.setUserMainId(Long.parseLong(userMainId));
            SellerShop querySellerShop = new SellerShop();
            querySellerShop.setUserMainId(Long.parseLong(userMainId));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), querySellerShop);
            resultObjectVO = feignSellerShopService.findByUser(requestJsonVO.sign(),requestJsonVO);
            if(resultObjectVO.isSuccess()&&resultObjectVO.getData()!=null) {
                SellerShopVO sellerShopVO = resultObjectVO.formatData(SellerShopVO.class);
                if(sellerShopVO!=null) {
                    freightTemplateVO.setShopId(sellerShopVO.getId());
                }
            }
            freightTemplateVO.setCreateDate(new Date());

            List<String> transportModels= new LinkedList<>();
            if(StringUtils.isNotEmpty(freightTemplateVO.getTransportModelExpress()))
            {
                transportModels.add(freightTemplateVO.getTransportModelExpress());
            }
            if(StringUtils.isNotEmpty(freightTemplateVO.getTransportModelEms()))
            {
                transportModels.add(freightTemplateVO.getTransportModelEms());
            }
            if(StringUtils.isNotEmpty(freightTemplateVO.getTransportModelOrdinaryMail()))
            {
                transportModels.add(freightTemplateVO.getTransportModelOrdinaryMail());
            }
            if(CollectionUtils.isNotEmpty(transportModels))
            {
                freightTemplateVO.setTransportModel(transportModels.stream().map(String::valueOf).collect(Collectors.joining(",")));
            }

            if(freightTemplateVO.getFreightStatus().shortValue()==1) {
                List<String> cityNameList = new LinkedList<>();
                //查询出快递的所选城市
                if (CollectionUtils.isNotEmpty(freightTemplateVO.getExpressAreaRules())) {
                    for (FreightTemplateAreaRuleVO freightTemplateAreaRuleVO : freightTemplateVO.getExpressAreaRules()) {
                        String selectAreas = freightTemplateAreaRuleVO.getSelectAreas();
                        if (StringUtils.isNotEmpty(selectAreas)) {
                            cityNameList.addAll(Arrays.asList(selectAreas.split(FreightTemplateConstant.VIEW_SELECT_AREA_SPLIT)));
                        }
                    }
                }
                //查询出EMS的所选城市
                if (CollectionUtils.isNotEmpty(freightTemplateVO.getEmsAreaRules())) {
                    for (FreightTemplateAreaRuleVO freightTemplateAreaRuleVO : freightTemplateVO.getEmsAreaRules()) {
                        String selectAreas = freightTemplateAreaRuleVO.getSelectAreas();
                        if (StringUtils.isNotEmpty(selectAreas)) {
                            cityNameList.addAll(Arrays.asList(selectAreas.split(FreightTemplateConstant.VIEW_SELECT_AREA_SPLIT)));
                        }
                    }
                }
                //查询出平邮的所选城市
                if (CollectionUtils.isNotEmpty(freightTemplateVO.getOrdinaryMailAreaRules())) {
                    for (FreightTemplateAreaRuleVO freightTemplateAreaRuleVO : freightTemplateVO.getOrdinaryMailAreaRules()) {
                        String selectAreas = freightTemplateAreaRuleVO.getSelectAreas();
                        if (StringUtils.isNotEmpty(selectAreas)) {
                            cityNameList.addAll(Arrays.asList(selectAreas.split(FreightTemplateConstant.VIEW_SELECT_AREA_SPLIT)));
                        }
                    }
                }
                if (CollectionUtils.isNotEmpty(cityNameList)) {
                    AreaVO queryAreaVO = new AreaVO();
                    queryAreaVO.setCityNameList(cityNameList);
                    requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryAreaVO);
                    resultObjectVO = feignAreaService.queryCityListByNames(requestJsonVO);
                    if (resultObjectVO.isSuccess()) {

                        List<AreaVO> areaVOS = resultObjectVO.formatDataList(AreaVO.class);
                        if (CollectionUtils.isNotEmpty(areaVOS)) {
                            Map<String, String> cityCodeToProvinceCode = new HashMap<>(); //市级编码对应的省级编码
                            Map<String, String> cityNameToCityCode = new HashMap<>(); //市级名称对应市级编码
                            Map<String, String> cityNameToProvinceName = new HashMap<>(); //市级名称对应省级名称
                            for (AreaVO areaVO : areaVOS) {
                                cityNameToCityCode.put(areaVO.getCity(), areaVO.getCode());
                                cityCodeToProvinceCode.put(areaVO.getCode(), areaVO.getParentCode());
                                cityNameToProvinceName.put(areaVO.getCity(),areaVO.getProvince());
                            }

                            freightTemplateVO.setCityNameToCityCode(cityNameToCityCode);
                            freightTemplateVO.setCityCodeToProvinceCode(cityCodeToProvinceCode);
                            freightTemplateVO.setCityNameToProvinceName(cityNameToProvinceName);
                        }
                    }
                }
            }
            requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), freightTemplateVO);
            resultObjectVO = feignFreightTemplateService.update(requestJsonVO);

        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    private SellerShopVO queryByShop(String userMainId) throws Exception
    {
        SellerShop querySellerShop = new SellerShop();
        querySellerShop.setUserMainId(Long.parseLong(userMainId));
        RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), querySellerShop);
        ResultObjectVO resultObjectVO = feignSellerShopService.findByUser(requestJsonVO.sign(),requestJsonVO);
        if(resultObjectVO.isSuccess()&&resultObjectVO.getData()!=null) {
            SellerShopVO sellerShopVO = resultObjectVO.formatData(SellerShopVO.class);
            return sellerShopVO;
        }
        return null;
    }

    /**
     * 根据ID删除
     * @return
     */
    @UserAuth
    @RequestMapping(value="/delete/{id}",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO deleteById(HttpServletRequest request,@PathVariable Long id)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(id==null)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("ID不能为空");
            return resultObjectVO;
        }
        String userMainId="-1";
        try {
            userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));

            SellerShopVO sellerShopVO = queryByShop(userMainId);
            ShopProductApproveVO shopProductApproveVO = new ShopProductApproveVO();
            shopProductApproveVO.setFreightTemplateId(id);
            shopProductApproveVO.setShopId(sellerShopVO.getId());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),shopProductApproveVO);
            resultObjectVO = feignShopProductApproveService.findOneUnderReviewByFreightTemplateId(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                if(resultObjectVO.getData()!=null) {
                    shopProductApproveVO = resultObjectVO.formatData(ShopProductApproveVO.class);
                    if (shopProductApproveVO != null) {

                        resultObjectVO.setCode(ResultObjectVO.FAILD);
                        resultObjectVO.setMsg("无法删除该模板,因为\""+shopProductApproveVO.getName()+"\"正在发布中,您可以删除该商品后重试");
                        return resultObjectVO;
                    }
                }
            }

            ShopProductVO shopProductVO = new ShopProductVO();
            shopProductVO.setFreightTemplateId(id);
            shopProductVO.setShopId(sellerShopVO.getId());
            requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),shopProductVO);
            resultObjectVO = feignShopProductService.queryOneSaleByFreightTemplateId(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                if(resultObjectVO.getData()!=null) {
                    shopProductVO = resultObjectVO.formatData(ShopProductVO.class);
                    if (shopProductApproveVO != null) {

                        resultObjectVO.setCode(ResultObjectVO.FAILD);
                        resultObjectVO.setMsg("无法删除该模板,因为\""+shopProductVO.getName()+"\"已上架,您可以删除该商品后重试");
                        return resultObjectVO;
                    }
                }
            }

            FreightTemplateVO freightTemplateVO = new FreightTemplateVO();
            freightTemplateVO.setId(id);
            freightTemplateVO.setUserMainId(Long.parseLong(userMainId));
            requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),freightTemplateVO);
            resultObjectVO = feignFreightTemplateService.deleteById(FreightTemplateUtils.getDeleteSignHeader(userMainId),requestJsonVO);


        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }

}
