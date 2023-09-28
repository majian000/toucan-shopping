package com.toucan.shopping.cloud.apps.seller.web.controller.shop.banner;

import com.toucan.shopping.cloud.apps.seller.web.controller.BaseController;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignSellerShopService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignShopBannerService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignShopCategoryService;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.DateUtils;
import com.toucan.shopping.modules.common.util.ImageUtils;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.seller.entity.SellerShop;
import com.toucan.shopping.modules.seller.page.ShopBannerPageInfo;
import com.toucan.shopping.modules.seller.vo.SellerShopVO;
import com.toucan.shopping.modules.seller.vo.ShopBannerVO;
import com.toucan.shopping.modules.seller.vo.ShopCategoryVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * 店铺轮播图
 * @author majian
 */
@Controller("shopBannerApiController")
@RequestMapping("/api/shop/banner")
public class ShopBannerApiController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    @Autowired
    private SkylarkLock skylarkLock;

    @Autowired
    private FeignShopBannerService feignShopBannerService;

    @Autowired
    private FeignSellerShopService feignSellerShopService;

    @Autowired
    private ImageUploadService imageUploadService;


    /**
     * 查询列表
     * @param pageInfo
     * @return
     */
    @UserAuth
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO list(HttpServletRequest httpServletRequest,@RequestBody ShopBannerPageInfo pageInfo)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try{
            if(pageInfo==null)
            {
                pageInfo = new ShopBannerPageInfo();
            }
            String userMainId = UserAuthHeaderUtil.getUserMainId(httpServletRequest.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            if(StringUtils.isEmpty(userMainId))
            {
                logger.warn("查询店铺轮播图 没有找到用户ID {} ",userMainId);
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
                    resultObjectVO.setData(null);
                    pageInfo.setShopId(sellerShopVO.getId());
                    pageInfo.setOrderColumn("update_date");
                    pageInfo.setOrderSort("desc");
                    requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), pageInfo);
                    resultObjectVO = feignShopBannerService.queryListPage(requestJsonVO);
                    if (resultObjectVO.isSuccess()&&resultObjectVO.getData() != null) {

                    }
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
     * 个人店铺编辑
     * @return
     */
    @UserAuth(requestType = UserAuth.REQUEST_AJAX)
    @RequestMapping(value="/edit")
    @ResponseBody
    public ResultObjectVO edit(HttpServletRequest request, @RequestParam(value="bannerImgFile",required=false) MultipartFile bannerImgFile, ShopBannerVO shopBannerVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        String userMainId="-1";
        try {

            if(bannerImgFile==null)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请上传图片");
                return resultObjectVO;
            }

            if(bannerImgFile!=null) {
                if (!ImageUtils.isImage(bannerImgFile.getOriginalFilename())) {
                    resultObjectVO.setCode(ResultObjectVO.FAILD - 4);
                    resultObjectVO.setMsg("请上传图片格式(.jpg|.jpeg|.png|.gif|bmp)");
                    return resultObjectVO;
                }
            }

            if(StringUtils.isEmpty(shopBannerVO.getTitle()))
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("标题不能为空");
                return resultObjectVO;
            }

            shopBannerVO.setStartShowDate(DateUtils.FORMATTER_SS.get().parse(shopBannerVO.getStartShowDateString()));
            shopBannerVO.setEndShowDate(DateUtils.FORMATTER_SS.get().parse(shopBannerVO.getEndShowDateString()));

            userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));

            SellerShop querySellerShop = new SellerShop();
            querySellerShop.setUserMainId(Long.parseLong(userMainId));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), querySellerShop);
            //判断是个人店铺还是企业店铺
            resultObjectVO = feignSellerShopService.findByUser(requestJsonVO.sign(),requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                SellerShopVO sellerShopVORet = resultObjectVO.formatData(SellerShopVO.class);
                if(sellerShopVORet!=null&&sellerShopVORet.getEnableStatus().intValue()!=1)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("店铺已被禁用");
                    return resultObjectVO;
                }

                shopBannerVO.setShopId(sellerShopVORet.getId());
                shopBannerVO.setCreaterId(userMainId);

                //LOGO上传
                String logoImgExt = ImageUtils.getImageExt(bannerImgFile.getOriginalFilename());
                if (logoImgExt.indexOf(".") != -1) {
                    logoImgExt = logoImgExt.substring(logoImgExt.indexOf(".") + 1, logoImgExt.length());
                }
                String logoImgFilePath = imageUploadService.uploadFile(bannerImgFile.getBytes(), logoImgExt);
                shopBannerVO.setImgPath(logoImgFilePath);


                resultObjectVO = feignShopBannerService.save(RequestJsonVOGenerator.generator(toucan.getAppCode(),shopBannerVO));
                if(!resultObjectVO.isSuccess())
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("添加失败,请稍后重试");
                }

            }

        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("添加失败,请稍后重试");
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



}