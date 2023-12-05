package com.toucan.shopping.cloud.apps.seller.web.controller.designer.image;

import com.toucan.shopping.cloud.apps.seller.web.controller.BaseController;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignSellerShopService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignSellerDesignerImageService;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.ImageUtils;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.seller.entity.SellerShop;
import com.toucan.shopping.modules.seller.page.SellerDesignerImagePageInfo;
import com.toucan.shopping.modules.seller.vo.SellerShopVO;
import com.toucan.shopping.modules.seller.vo.SellerDesignerImageVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 店铺装修图片
 * @author majian
 */
@Controller("sellerDesignerImageApiController")
@RequestMapping("/api/designer/image")
public class SellerDesignerImageApiController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    @Autowired
    private SkylarkLock skylarkLock;

    @Autowired
    private FeignSellerDesignerImageService feignSellerDesignerImageService;

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
    public ResultObjectVO list(HttpServletRequest httpServletRequest,@RequestBody SellerDesignerImagePageInfo pageInfo)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try{
            if(pageInfo==null)
            {
                pageInfo = new SellerDesignerImagePageInfo();
            }
            String userMainId = UserAuthHeaderUtil.getUserMainId(httpServletRequest.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            if(StringUtils.isEmpty(userMainId))
            {
                logger.warn("查询店铺图片 没有找到用户ID {} ",userMainId);
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("查询失败,请稍后重试");
                return resultObjectVO;
            }

            SellerShop querySellerShop = new SellerShop();
            querySellerShop.setUserMainId(Long.parseLong(userMainId));
            querySellerShop.setEnableStatus((short)1);
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
                    resultObjectVO = feignSellerDesignerImageService.queryListPage(requestJsonVO);
                    if (resultObjectVO.isSuccess()&&resultObjectVO.getData() != null) {
                        SellerDesignerImagePageInfo sellerDesignerImagePageInfo = resultObjectVO.formatData(SellerDesignerImagePageInfo.class);
                        if(sellerDesignerImagePageInfo !=null&& sellerDesignerImagePageInfo.getList()!=null)
                        {
                            for(SellerDesignerImageVO shopImageVO: sellerDesignerImagePageInfo.getList())
                            {
                                if(shopImageVO.getImgPath()!=null) {
                                    shopImageVO.setHttpImgPath(imageUploadService.getImageHttpPrefix()+shopImageVO.getImgPath());
                                }
                            }
                        }
                        resultObjectVO.setData(sellerDesignerImagePageInfo);
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
     * 添加图片
     * @return
     */
    @UserAuth
    @RequestMapping(value="/save")
    @ResponseBody
    public ResultObjectVO save(HttpServletRequest request, @RequestParam(value="imageImgFile",required=false) MultipartFile imageImgFile, SellerDesignerImageVO shopImageVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        String userMainId="-1";
        try {

            if(imageImgFile==null)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请上传图片");
                return resultObjectVO;
            }

            if(imageImgFile!=null) {
                if (!ImageUtils.isImage(imageImgFile.getOriginalFilename())) {
                    resultObjectVO.setCode(ResultObjectVO.FAILD - 4);
                    resultObjectVO.setMsg("请上传图片格式(.jpg|.jpeg|.png|.gif|bmp)");
                    return resultObjectVO;
                }
                if(imageImgFile.getOriginalFilename()!=null&&imageImgFile.getOriginalFilename().length()>50)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD - 4);
                    resultObjectVO.setMsg("文件名最大长度不能超过50");
                    return resultObjectVO;
                }
            }

            if(StringUtils.isEmpty(shopImageVO.getTitle()))
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("标题不能为空");
                return resultObjectVO;
            }


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

                shopImageVO.setShopId(sellerShopVORet.getId());
                shopImageVO.setCreaterId(userMainId);

                //LOGO上传
                String logoImgExt = ImageUtils.getImageExt(imageImgFile.getOriginalFilename());
                if (logoImgExt.indexOf(".") != -1) {
                    logoImgExt = logoImgExt.substring(logoImgExt.indexOf(".") + 1, logoImgExt.length());
                }
                String logoImgFilePath = imageUploadService.uploadFile(imageImgFile.getBytes(), logoImgExt);
                shopImageVO.setImgPath(logoImgFilePath);
                shopImageVO.setFileName(imageImgFile.getOriginalFilename());
                shopImageVO.setFileSize(imageImgFile.getSize());
                resultObjectVO = feignSellerDesignerImageService.save(RequestJsonVOGenerator.generator(toucan.getAppCode(),shopImageVO));
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
        String userMainId="-1";
        try {
            userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));

            SellerShopVO sellerShopVO = queryByShop(userMainId);
            if(sellerShopVO==null)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("没有查询到店铺");
                return resultObjectVO;
            }
            SellerDesignerImageVO shopImageVO = new SellerDesignerImageVO();
            shopImageVO.setId(id);
            shopImageVO.setShopId(sellerShopVO.getId());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),shopImageVO);
            resultObjectVO = feignSellerDesignerImageService.deleteById(requestJsonVO);

        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }





    /**
     * 修改图片
     * @return
     */
    @UserAuth
    @RequestMapping(value="/update")
    @ResponseBody
    public ResultObjectVO update(HttpServletRequest request, @RequestParam(value="imageImgFile",required=false) MultipartFile imageImgFile, SellerDesignerImageVO shopImageVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        String userMainId="-1";
        try {

            if(imageImgFile!=null) {
                if (!ImageUtils.isImage(imageImgFile.getOriginalFilename())) {
                    resultObjectVO.setCode(ResultObjectVO.FAILD - 4);
                    resultObjectVO.setMsg("请上传图片格式(.jpg|.jpeg|.png|.gif|bmp)");
                    return resultObjectVO;
                }
                if(imageImgFile.getOriginalFilename()!=null&&imageImgFile.getOriginalFilename().length()>50)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD - 4);
                    resultObjectVO.setMsg("文件名最大长度不能超过50");
                    return resultObjectVO;
                }
            }

            if(StringUtils.isEmpty(shopImageVO.getTitle()))
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("标题不能为空");
                return resultObjectVO;
            }


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

                shopImageVO.setShopId(sellerShopVORet.getId());
                shopImageVO.setCreaterId(userMainId);

                //图片上传
                if(imageImgFile!=null) {
                    String logoImgExt = ImageUtils.getImageExt(imageImgFile.getOriginalFilename());
                    if (logoImgExt.indexOf(".") != -1) {
                        logoImgExt = logoImgExt.substring(logoImgExt.indexOf(".") + 1, logoImgExt.length());
                    }
                    String logoImgFilePath = imageUploadService.uploadFile(imageImgFile.getBytes(), logoImgExt);
                    shopImageVO.setImgPath(logoImgFilePath);
                    shopImageVO.setFileName(imageImgFile.getOriginalFilename());
                    shopImageVO.setFileSize(imageImgFile.getSize());
                }

                SellerDesignerImageVO queryShopImageVO = new SellerDesignerImageVO();
                queryShopImageVO.setId(shopImageVO.getId());
                resultObjectVO = feignSellerDesignerImageService.findById(RequestJsonVOGenerator.generator(toucan.getAppCode(),shopImageVO));
                if(resultObjectVO.isSuccess()) {
                    SellerDesignerImageVO resultShopImageVO = resultObjectVO.formatData(SellerDesignerImageVO.class);
                    if (resultShopImageVO != null && resultShopImageVO.getShopId().equals(sellerShopVORet.getId())) {
                        if(StringUtils.isEmpty(shopImageVO.getImgPath()))
                        {
                            shopImageVO.setImgPath(resultShopImageVO.getImgPath());
                            shopImageVO.setFileName(resultShopImageVO.getFileName());
                            shopImageVO.setFileSize(resultShopImageVO.getFileSize());
                            shopImageVO.setUpdaterId(userMainId);
                            shopImageVO.setUpdateDate(new Date());
                        }else{
                            imageUploadService.deleteFile(resultShopImageVO.getImgPath());
                        }
                        resultObjectVO = feignSellerDesignerImageService.update(RequestJsonVOGenerator.generator(toucan.getAppCode(), shopImageVO));
                        if (!resultObjectVO.isSuccess()) {
                            resultObjectVO.setCode(ResultObjectVO.FAILD);
                            resultObjectVO.setMsg("修改失败,请稍后重试");
                        }
                    }else{
                        resultObjectVO.setCode(ResultObjectVO.FAILD);
                        resultObjectVO.setMsg("修改失败,请稍后重试");
                    }
                }else{
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("修改失败,请稍后重试");
                }

            }

        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("修改失败,请稍后重试");
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }

}
