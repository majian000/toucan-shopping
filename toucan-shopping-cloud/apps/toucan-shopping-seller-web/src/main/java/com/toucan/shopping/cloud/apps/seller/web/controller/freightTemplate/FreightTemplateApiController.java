package com.toucan.shopping.cloud.apps.seller.web.controller.freightTemplate;

import com.toucan.shopping.cloud.apps.seller.web.controller.BaseController;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignFreightTemplateService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignSellerShopService;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.seller.entity.SellerShop;
import com.toucan.shopping.modules.seller.page.FreightTemplatePageInfo;
import com.toucan.shopping.modules.seller.vo.SellerShopVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;


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



}
