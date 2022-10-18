package com.toucan.shopping.cloud.apps.seller.web.controller.freightTemplate;

import com.toucan.shopping.cloud.apps.seller.web.controller.BaseController;
import com.toucan.shopping.cloud.common.data.api.feign.service.FeignAreaService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignFreightTemplateService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignSellerShopService;
import com.toucan.shopping.modules.area.vo.AreaVO;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.seller.entity.FreightTemplateAreaRule;
import com.toucan.shopping.modules.seller.entity.SellerShop;
import com.toucan.shopping.modules.seller.page.FreightTemplatePageInfo;
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
                resultObjectVO.setMsg("查询失败,请稍后重试");
                return resultObjectVO;
            }

            SellerShop querySellerShop = new SellerShop();
            querySellerShop.setUserMainId(Long.parseLong(userMainId));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), querySellerShop);
            resultObjectVO = feignSellerShopService.findByUser(requestJsonVO.sign(),requestJsonVO);
            if(resultObjectVO.isSuccess()&&resultObjectVO.getData()!=null) {
                SellerShopVO sellerShopVO = resultObjectVO.formatData(SellerShopVO.class);
                if(sellerShopVO!=null) {
                    pageInfo.setShopId(sellerShopVO.getId());
                    requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), pageInfo);
                    resultObjectVO = feignFreightTemplateService.queryListPage(requestJsonVO);
                }
            }
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
                            cityNameList.addAll(Arrays.asList(selectAreas.split("，")));
                        }
                    }
                }
                //查询出EMS的所选城市
                if (CollectionUtils.isNotEmpty(freightTemplateVO.getEmsAreaRules())) {
                    for (FreightTemplateAreaRuleVO freightTemplateAreaRuleVO : freightTemplateVO.getEmsAreaRules()) {
                        String selectAreas = freightTemplateAreaRuleVO.getSelectAreas();
                        if (StringUtils.isNotEmpty(selectAreas)) {
                            cityNameList.addAll(Arrays.asList(selectAreas.split("，")));
                        }
                    }
                }
                //查询出平邮的所选城市
                if (CollectionUtils.isNotEmpty(freightTemplateVO.getOrdinaryMailAreaRules())) {
                    for (FreightTemplateAreaRuleVO freightTemplateAreaRuleVO : freightTemplateVO.getOrdinaryMailAreaRules()) {
                        String selectAreas = freightTemplateAreaRuleVO.getSelectAreas();
                        if (StringUtils.isNotEmpty(selectAreas)) {
                            cityNameList.addAll(Arrays.asList(selectAreas.split("，")));
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
                            for (AreaVO areaVO : areaVOS) {
                                cityNameToCityCode.put(areaVO.getCity(), areaVO.getCode());
                                cityCodeToProvinceCode.put(areaVO.getCode(), areaVO.getParentCode());
                            }

                            freightTemplateVO.setCityNameToCityCode(cityNameToCityCode);
                            freightTemplateVO.setCityCodeToProvinceCode(cityCodeToProvinceCode);
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


}
