package com.toucan.shopping.cloud.apps.seller.web.controller.shop;

import com.toucan.shopping.cloud.apps.seller.web.controller.BaseController;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignSellerShopService;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserService;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.seller.entity.SellerShop;
import com.toucan.shopping.modules.seller.vo.SellerShopVO;
import com.toucan.shopping.modules.user.vo.UserVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 店铺信息
 */
@Controller("shopApiController")
@RequestMapping("/page/api")
public class ShopApiController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private FeignUserService feignUserService;

    @Autowired
    private FeignSellerShopService feignSellerShopService;

    @Autowired
    private Toucan toucan;

    @Autowired
    private ImageUploadService imageUploadService;



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping("/upload/logo")
    @ResponseBody
    public ResultObjectVO  uploadLogo(@RequestParam("file") MultipartFile file,HttpServletRequest request)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        resultObjectVO.setCode(0);
        try{
            String fileName = file.getOriginalFilename();
            String fileExt = ".jpg";
            if(StringUtils.isNotEmpty(fileName)&&fileName.indexOf(".")!=-1)
            {
                fileExt = fileName.substring(fileName.lastIndexOf(".")+1);
            }
            String fileExtUpper=  fileExt.toUpperCase();
            if(!"JPG".equals(fileExtUpper)&&!"JPEG".equals(fileExtUpper)
                    &&!"PNG".equals(fileExtUpper)&&!"BMP".equals(fileExtUpper))
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("图标只能上传JPG、JPEG、PNG、BMP格式!");
                return resultObjectVO;
            }
            String userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            String oldLogo = null;
            try{
                //查询该用户的店铺
                SellerShop sellerShop = new SellerShop();
                sellerShop.setUserMainId(Long.parseLong(userMainId));

                RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), sellerShop);
                resultObjectVO = feignSellerShopService.findByUser(requestJsonVO.sign(),requestJsonVO);
                if(resultObjectVO.isSuccess())
                {
                    SellerShopVO sellerShopVO = resultObjectVO.formatData(SellerShopVO.class);
                    oldLogo = sellerShopVO.getLogo();
                    if(sellerShopVO.getEnableStatus().intValue()==0)
                    {
                        resultObjectVO.setCode(ResultObjectVO.FAILD);
                        resultObjectVO.setMsg("店铺已被禁用!");
                        return resultObjectVO;
                    }
                    if(sellerShopVO.getApproveStatus().intValue()!=2)
                    {
                        resultObjectVO.setCode(ResultObjectVO.FAILD);
                        if(sellerShopVO.getApproveStatus().intValue()==1) {
                            resultObjectVO.setMsg("店铺正在审核中!");
                        }else{
                            resultObjectVO.setMsg("店铺申请已被驳回!");
                        }
                        return resultObjectVO;
                    }
                }
            }catch(Exception e)
            {
                logger.warn("查询商铺失败 用户ID{}",userMainId);
                logger.warn(e.getMessage(),e);
            }

            String groupPath = imageUploadService.uploadFile(file.getBytes(),fileExt);

            if(StringUtils.isEmpty(groupPath))
            {
                throw new RuntimeException("店铺图标上传失败");
            }

            //如果不是默认图标或者不为空的话,就去文件服务器删除这个文件
            if(StringUtils.isNotEmpty(oldLogo)&&!toucan.getSeller().getDefaultShopLogo().equals(oldLogo))
            {
                try{
                    imageUploadService.deleteFile(oldLogo);
                }catch(Exception e)
                {
                    logger.warn("删除旧店铺图标失败, 用户ID {} 店铺图标 {}",userMainId,oldLogo);
                    logger.warn(e.getMessage(),e);
                }
            }
            SellerShopVO sellerShopVO = new SellerShopVO();
            sellerShopVO.setLogo(groupPath);
            sellerShopVO.setUserMainId(Long.parseLong(userMainId));

            //修改店铺图标
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), sellerShopVO);
            resultObjectVO = feignSellerShopService.updateLogo(requestJsonVO.sign(),requestJsonVO);
            if(resultObjectVO.isSuccess()) {

                //设置预览
                if (sellerShopVO.getLogo() != null) {
                    sellerShopVO.setHttpLogo(imageUploadService.getImageHttpPrefix() + sellerShopVO.getLogo());
                }
                resultObjectVO.setData(sellerShopVO);
            }
        }catch (Exception e)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("店铺图标上传失败");
            logger.warn(e.getMessage(),e);
        }

        return resultObjectVO;
    }



}
