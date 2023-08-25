package com.toucan.shopping.cloud.apps.web.controller.user.userCollectProduct;


import com.toucan.shopping.cloud.apps.web.controller.BaseController;
import com.toucan.shopping.cloud.apps.web.service.VerifyCodeService;
import com.toucan.shopping.cloud.apps.web.util.VCodeUtil;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserCollectProductService;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.redis.service.ToucanStringRedisService;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import com.toucan.shopping.modules.user.page.UserCollectProductPageInfo;
import com.toucan.shopping.modules.user.vo.UserCollectProductVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 * 用户收藏商品
 */
@RestController("userCollectProductApiController")
@RequestMapping("/api/user/collect/product")
public class UserCollectProductApiController extends BaseController {


    private final Logger logger = LoggerFactory.getLogger(getClass());



    @Autowired
    private FeignUserCollectProductService feignUserCollectProductService;


    @Autowired
    private Toucan toucan;


    @UserAuth(requestType = UserAuth.REQUEST_AJAX)
    @RequestMapping(value="/collect")
    @ResponseBody
    public ResultObjectVO collect(HttpServletRequest request, @RequestBody UserCollectProductVO userCollectProductVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(userCollectProductVO==null)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("提交失败,没有找到收藏商品");
            return resultObjectVO;
        }

        String userMainId="-1";
        try {
            //从请求头中拿到uid
            userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            userCollectProductVO.setUserMainId(Long.parseLong(userMainId));
            userCollectProductVO.setAppCode(toucan.getAppCode());

            if(userCollectProductVO.getUserMainId()==null)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("用户ID不能为空");
                return resultObjectVO;
            }
            if(userCollectProductVO.getProductSkuId()==null)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("没有找到收藏商品");
                return resultObjectVO;
            }

            userCollectProductVO.setAppCode(toucan.getAppCode());
            if(userCollectProductVO.getType()==1) {
                resultObjectVO = feignUserCollectProductService.save(RequestJsonVOGenerator.generator(toucan.getAppCode(), userCollectProductVO));
            }else{
                resultObjectVO = feignUserCollectProductService.deleteBySkuIdAndUserMainIdAndAppCode(RequestJsonVOGenerator.generator(toucan.getAppCode(), userCollectProductVO));
            }
            resultObjectVO.setData(null);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }






    @UserAuth
    @RequestMapping(value="/isCollect")
    @ResponseBody
    public ResultObjectVO isCollect(HttpServletRequest request, @RequestBody UserCollectProductVO consigneeAddressVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if (consigneeAddressVO == null) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("查询失败,没有找到收藏商品");
            return resultObjectVO;
        }
        if (consigneeAddressVO.getProductSkuIds()==null) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("查询失败,ID不能为空");
            return resultObjectVO;
        }

        try {
            //从请求头中拿到uid
            consigneeAddressVO.setUserMainId(Long.parseLong(UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()))));
            consigneeAddressVO.setAppCode(toucan.getAppCode());

            if(consigneeAddressVO.getUserMainId()==null)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("查询失败,用户ID不能为空");
                return resultObjectVO;
            }
            resultObjectVO = feignUserCollectProductService.queryCollectProducts(RequestJsonVOGenerator.generator(toucan.getAppCode(),consigneeAddressVO));

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



    /**
     * 收藏列表
     * @param request
     * @return
     */
    @UserAuth(requestType = UserAuth.REQUEST_AJAX)
    @RequestMapping(value="/list",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryOrderList(HttpServletRequest request,@RequestBody UserCollectProductPageInfo pageInfo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try{
            pageInfo.setUserMainId(Long.parseLong(UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()))));
            pageInfo.setAppCode(toucan.getAppCode());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),pageInfo);
            resultObjectVO = feignUserCollectProductService.queryListPage(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                pageInfo = resultObjectVO.formatData(UserCollectProductPageInfo.class);
                resultObjectVO.setData(pageInfo);
            }
        }catch (Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

}
