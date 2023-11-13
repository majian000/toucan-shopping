package com.toucan.shopping.cloud.apps.web.controller.user.consigneeAddress;


import com.toucan.shopping.cloud.apps.web.controller.BaseController;
import com.toucan.shopping.cloud.apps.web.redis.VerifyCodeRedisKey;
import com.toucan.shopping.cloud.apps.web.service.VerifyCodeService;
import com.toucan.shopping.cloud.apps.web.util.VCodeUtil;
import com.toucan.shopping.cloud.user.api.feign.service.FeignConsigneeAddressService;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserHeadSculptureApproveService;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.ImageUtils;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.redis.service.ToucanStringRedisService;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import com.toucan.shopping.modules.user.page.ConsigneeAddressPageInfo;
import com.toucan.shopping.modules.user.redis.UserCenterConsigneeAddressKey;
import com.toucan.shopping.modules.user.redis.UserCenterHeadSculptureApproveKey;
import com.toucan.shopping.modules.user.vo.ConsigneeAddressVO;
import com.toucan.shopping.modules.user.vo.UserHeadSculptureApproveVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;


/**
 * 收货地址
 */
@RestController("consigneeAddressApiController")
@RequestMapping("/api/user/consigneeAddress")
public class ConsigneeAddressApiController extends BaseController {


    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private SkylarkLock skylarkLock;

    @Autowired
    private ToucanStringRedisService toucanStringRedisService;

    @Autowired
    private VerifyCodeService verifyCodeService;

    @Autowired
    private FeignConsigneeAddressService feignConsigneeAddressService;


    @Autowired
    private Toucan toucan;


    @UserAuth
    @RequestMapping(value="/save")
    @ResponseBody
    public ResultObjectVO save(HttpServletRequest request, @RequestBody ConsigneeAddressVO consigneeAddressVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(consigneeAddressVO==null)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("提交失败,没有找到收货信息");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(consigneeAddressVO.getName()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("提交失败,收货人不能为空");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(consigneeAddressVO.getAddress())||StringUtils.isEmpty(consigneeAddressVO.getAddress().replace(" ","")))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("提交失败,详细地址不能为空");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(consigneeAddressVO.getProvinceCode())||StringUtils.isEmpty(consigneeAddressVO.getProvinceName()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("提交失败,省/直辖市不能为空");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(consigneeAddressVO.getAreaCode())||StringUtils.isEmpty(consigneeAddressVO.getAreaName()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("提交失败,市区不能为空");
            return resultObjectVO;
        }


        String userMainId="-1";
        try {
            //从请求头中拿到uid
            userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            consigneeAddressVO.setUserMainId(Long.parseLong(userMainId));
            consigneeAddressVO.setAppCode(toucan.getAppCode());

            if(consigneeAddressVO.getUserMainId()==null)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("提交失败,用户ID不能为空");
                return resultObjectVO;
            }


            resultObjectVO = verifyCodeService.verifyCode(request,consigneeAddressVO.getVcode(),toucan.getAppCode());
            if(resultObjectVO.isSuccess()) {
                String vcodeRedisKey = VCodeUtil.getVerifyCodeKey(toucan.getAppCode(), request);
                //删除缓存中验证码
                toucanStringRedisService.delete(vcodeRedisKey);

                consigneeAddressVO.setAppCode(toucan.getAppCode());
                resultObjectVO = feignConsigneeAddressService.save(RequestJsonVOGenerator.generator(toucan.getAppCode(),consigneeAddressVO));
            }

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }




    @UserAuth
    @RequestMapping("/list")
    @ResponseBody
    public ResultObjectVO queryMyMessageList(HttpServletRequest request,@RequestBody ConsigneeAddressPageInfo queryPageInfo)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        String userMainId = "-1";
        try {
            if(queryPageInfo==null) {
                queryPageInfo = new ConsigneeAddressPageInfo();
            }
            userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            queryPageInfo.setUserMainId(Long.parseLong(userMainId));
            queryPageInfo.setAppCode(toucan.getAppCode());

            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryPageInfo);
            resultObjectVO = feignConsigneeAddressService.queryListPage(requestJsonVO);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }






    @UserAuth
    @RequestMapping(value="/delete")
    @ResponseBody
    public ResultObjectVO delete(HttpServletRequest request, @RequestBody ConsigneeAddressVO consigneeAddressVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if (consigneeAddressVO == null) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("删除失败,没有找到收货信息");
            return resultObjectVO;
        }
        if (consigneeAddressVO.getId()==null) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("删除失败,ID不能为空");
            return resultObjectVO;
        }

        String userMainId="-1";
        try {
            //从请求头中拿到uid
            userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            consigneeAddressVO.setUserMainId(Long.parseLong(userMainId));
            consigneeAddressVO.setAppCode(toucan.getAppCode());

            if(consigneeAddressVO.getUserMainId()==null)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("删除失败,用户ID不能为空");
                return resultObjectVO;
            }
            resultObjectVO = feignConsigneeAddressService.deleteByIdAndUserMainIdAndAppCode(RequestJsonVOGenerator.generator(toucan.getAppCode(),consigneeAddressVO));

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }





    @UserAuth
    @RequestMapping(value="/set/default")
    @ResponseBody
    public ResultObjectVO setDefault(HttpServletRequest request, @RequestBody ConsigneeAddressVO consigneeAddressVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if (consigneeAddressVO == null) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("设置失败,没有找到收货信息");
            return resultObjectVO;
        }
        if (consigneeAddressVO.getId()==null) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("设置失败,ID不能为空");
            return resultObjectVO;
        }

        String userMainId="-1";
        try {
            //从请求头中拿到uid
            userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            consigneeAddressVO.setUserMainId(Long.parseLong(userMainId));
            consigneeAddressVO.setAppCode(toucan.getAppCode());

            if(consigneeAddressVO.getUserMainId()==null)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("设置失败,用户ID不能为空");
                return resultObjectVO;
            }
            resultObjectVO = feignConsigneeAddressService.setDefaultByIdAndUserMainId(RequestJsonVOGenerator.generator(toucan.getAppCode(),consigneeAddressVO));

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }




    @UserAuth
    @RequestMapping(value="/load")
    @ResponseBody
    public ResultObjectVO load(HttpServletRequest request, @RequestBody ConsigneeAddressVO consigneeAddressVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if (consigneeAddressVO == null) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("设置失败,没有找到收货信息");
            return resultObjectVO;
        }
        if (consigneeAddressVO.getId()==null) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("设置失败,ID不能为空");
            return resultObjectVO;
        }

        String userMainId="-1";
        try {
            //从请求头中拿到uid
            userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            consigneeAddressVO.setUserMainId(Long.parseLong(userMainId));
            consigneeAddressVO.setAppCode(toucan.getAppCode());

            if(consigneeAddressVO.getUserMainId()==null)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("设置失败,用户ID不能为空");
                return resultObjectVO;
            }
            resultObjectVO = feignConsigneeAddressService.findByIdAndUserMainIdAndAppcode(RequestJsonVOGenerator.generator(toucan.getAppCode(),consigneeAddressVO));

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    @UserAuth
    @RequestMapping(value="/update")
    @ResponseBody
    public ResultObjectVO update(HttpServletRequest request, @RequestBody ConsigneeAddressVO consigneeAddressVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if (consigneeAddressVO == null) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("设置失败,没有找到收货信息");
            return resultObjectVO;
        }
        if (consigneeAddressVO.getId()==null) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("设置失败,ID不能为空");
            return resultObjectVO;
        }

        if(StringUtils.isEmpty(consigneeAddressVO.getName()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("提交失败,收货人不能为空");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(consigneeAddressVO.getAddress())||StringUtils.isEmpty(consigneeAddressVO.getAddress().replace(" ","")))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("提交失败,详细地址不能为空");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(consigneeAddressVO.getProvinceCode())||StringUtils.isEmpty(consigneeAddressVO.getProvinceName()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("提交失败,省/直辖市不能为空");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(consigneeAddressVO.getAreaCode())||StringUtils.isEmpty(consigneeAddressVO.getAreaName()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("提交失败,市区不能为空");
            return resultObjectVO;
        }

        String userMainId="-1";
        try {
            //从请求头中拿到uid
            userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            consigneeAddressVO.setUserMainId(Long.parseLong(userMainId));
            consigneeAddressVO.setAppCode(toucan.getAppCode());

            if(consigneeAddressVO.getUserMainId()==null)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("设置失败,用户ID不能为空");
                return resultObjectVO;
            }
            resultObjectVO = feignConsigneeAddressService.update(RequestJsonVOGenerator.generator(toucan.getAppCode(),consigneeAddressVO));

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }




    @UserAuth
    @RequestMapping(value="/update/save")
    @ResponseBody
    public ResultObjectVO updateOrSave(HttpServletRequest request, @RequestBody ConsigneeAddressVO consigneeAddressVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if (consigneeAddressVO == null) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("设置失败,没有找到收货信息");
            return resultObjectVO;
        }

        if(StringUtils.isEmpty(consigneeAddressVO.getName()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("提交失败,收货人不能为空");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(consigneeAddressVO.getAddress())||StringUtils.isEmpty(consigneeAddressVO.getAddress().replace(" ","")))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("提交失败,详细地址不能为空");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(consigneeAddressVO.getProvinceCode())||StringUtils.isEmpty(consigneeAddressVO.getProvinceName()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("提交失败,省/直辖市不能为空");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(consigneeAddressVO.getAreaCode())||StringUtils.isEmpty(consigneeAddressVO.getAreaName()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("提交失败,市区不能为空");
            return resultObjectVO;
        }

        String userMainId="-1";
        try {
            //从请求头中拿到uid
            userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            consigneeAddressVO.setUserMainId(Long.parseLong(userMainId));
            consigneeAddressVO.setAppCode(toucan.getAppCode());

            if(consigneeAddressVO.getUserMainId()==null)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("设置失败,用户ID不能为空");
                return resultObjectVO;
            }

            if(consigneeAddressVO.getId()!=null) {
                resultObjectVO = feignConsigneeAddressService.update(RequestJsonVOGenerator.generator(toucan.getAppCode(), consigneeAddressVO));
            }else{
                consigneeAddressVO.setDeleteStatus((short)0);
                resultObjectVO = feignConsigneeAddressService.save(RequestJsonVOGenerator.generator(toucan.getAppCode(), consigneeAddressVO));
            }

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



    /**
     * 查询设置为默认的收货信息,如果没有默认就查询最新一条
     * @return
     */
    @RequestMapping(value="/find/default",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO findDefault(HttpServletRequest request)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        String userMainId="-1";
        try {
            //从请求头中拿到uid
            ConsigneeAddressVO consigneeAddressVO = new ConsigneeAddressVO();
            userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            consigneeAddressVO.setUserMainId(Long.parseLong(userMainId));
            consigneeAddressVO.setAppCode(toucan.getAppCode());

            if(consigneeAddressVO.getUserMainId()==null)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("设置失败,用户ID不能为空");
                return resultObjectVO;
            }
            resultObjectVO = feignConsigneeAddressService.findDefaultByUserMainIdAndAppcode(RequestJsonVOGenerator.generator(toucan.getAppCode(),consigneeAddressVO));

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }

}
