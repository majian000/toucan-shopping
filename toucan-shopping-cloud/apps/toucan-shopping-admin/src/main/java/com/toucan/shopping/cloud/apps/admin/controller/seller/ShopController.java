package com.toucan.shopping.cloud.apps.admin.controller.seller;


import com.toucan.shopping.cloud.apps.admin.helper.PageHelper;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.AdminServiceAPI;
import com.toucan.shopping.cloud.seller.api.SellerShopServiceAPI;
import com.toucan.shopping.modules.admin.auth.holder.AdminLoginHolder;
import com.toucan.shopping.modules.admin.auth.vo.AdminVO;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.layui.vo.TableVO;
import com.toucan.shopping.modules.seller.page.SellerShopPageInfo;
import com.toucan.shopping.modules.seller.vo.SellerShopVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *  卖家店铺管理
 */
@RestController
@RequestMapping("/seller/shop")
public class ShopController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private SellerShopServiceAPI sellerShopService;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private AdminServiceAPI adminServiceAPI;



    /**
     * 查询列表
     * @param pageInfo
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"shopping:seller:shop:list:api"})
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public TableVO list(HttpServletRequest request, @RequestBody SellerShopPageInfo pageInfo)
    {
        TableVO tableVO = new TableVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),pageInfo);
            ResultObjectVO resultObjectVO = sellerShopService.queryListPage(requestJsonVO);
            if(resultObjectVO.getCode() == ResultObjectVO.SUCCESS)
            {
                if(resultObjectVO.getData()!=null)
                {
                    Map<String,Object> resultObjectDataMap = PageHelper.extractPageData(resultObjectVO.getData());
                    tableVO.setCount(Long.parseLong(String.valueOf(resultObjectDataMap.get("total")!=null?resultObjectDataMap.get("total"):"0")));
                    List<SellerShopVO> list = JSONArray.parseArray(JSONObject.toJSONString(resultObjectDataMap.get("list")),SellerShopVO.class);
                    if(tableVO.getCount()>0) {
                        //查询创建人和修改人
                        List<String> adminIdList = new ArrayList<String>();
                        for(SellerShopVO sellerShopVO:list)
                        {
                            if(sellerShopVO.getLogo()!=null) {
                                sellerShopVO.setHttpLogo(imageUploadService.getImageHttpPrefix() + "/" + sellerShopVO.getLogo());
                            }

                            if(sellerShopVO.getCreateAdminId()!=null) {
                                adminIdList.add(sellerShopVO.getCreateAdminId());
                            }
                            if(sellerShopVO.getUpdateAdminId()!=null)
                            {
                                adminIdList.add(sellerShopVO.getUpdateAdminId());
                            }
                        }
                        if(CollectionUtils.isNotEmpty(adminIdList)) {
                            String[] createOrUpdateAdminIds = new String[adminIdList.size()];
                            adminIdList.toArray(createOrUpdateAdminIds);
                            AdminVO queryAdminVO = new AdminVO();
                            queryAdminVO.setAdminIds(createOrUpdateAdminIds);
                            RequestJsonVO adminRequestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryAdminVO);
                            ResultObjectVO adminResultVO = adminServiceAPI.queryListByEntity(adminRequestJsonVO);
                            if (adminResultVO.isSuccess()) {
                                List<AdminVO> adminVOS = adminResultVO.formatDataList(AdminVO.class);
                                if (CollectionUtils.isNotEmpty(adminVOS)) {
                                    for (SellerShopVO sellerShopVO : list) {
                                        for (AdminVO adminVO : adminVOS) {
                                            if (sellerShopVO.getCreateAdminId() != null && sellerShopVO.getCreateAdminId().equals(adminVO.getAdminId())) {
                                                sellerShopVO.setCreateAdminName(adminVO.getUsername());
                                            }
                                            if (sellerShopVO.getUpdateAdminId() != null && sellerShopVO.getUpdateAdminId().equals(adminVO.getAdminId())) {
                                                sellerShopVO.setUpdateAdminName(adminVO.getUsername());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        tableVO.setData((List)list);
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
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"shopping:seller:shop:delete:api"})
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public ResultObjectVO deleteById(@RequestBody SellerShopVO entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(entity.getId() == null)
            {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            entity.setUpdateAdminId(AdminLoginHolder.getCurrentAdminId());

            String entityJson = JSONObject.toJSONString(entity);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(toucan.getAppCode());
            requestVo.setEntityJson(entityJson);
            resultObjectVO = sellerShopService.deleteById(requestVo);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }





    /**
     * 批量删除
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"shopping:seller:shop:deletes:api"})
    @RequestMapping(value = "/delete/ids",method = RequestMethod.POST)
    public ResultObjectVO deleteByIds(@RequestBody List<SellerShopVO> sellerShopVOS)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(CollectionUtils.isEmpty(sellerShopVOS))
            {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            String entityJson = JSONObject.toJSONString(sellerShopVOS);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(toucan.getAppCode());
            requestVo.setEntityJson(entityJson);
            resultObjectVO = sellerShopService.deleteByIds( requestVo);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    /**
     * 商铺 启用/禁用
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"shopping:seller:shop:api:disabledEnabled"})
    @RequestMapping(value = "/disabled/enabled",method = RequestMethod.POST)
    public ResultObjectVO disabledEnabledByPublicShopId(@RequestBody SellerShopVO sellerShopVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(StringUtils.isEmpty(sellerShopVO.getPublicShopId()))
            {
                resultObjectVO.setMsg("请传入公开店铺ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }

            RequestJsonVO requestVo = RequestJsonVOGenerator.generator(appCode,sellerShopVO);
            resultObjectVO = sellerShopService.disabledEnabled(requestVo);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }




    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, requestType = AdminAuth.REQUEST_FORM, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"shopping:seller:shop:uploadLog:api"})
    @RequestMapping("/upload/logo")
    public ResultObjectVO  uploadLogo(@RequestParam("file") MultipartFile file, @RequestParam("publicShopId")String publicShopId)
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
            String groupPath = imageUploadService.uploadFile(file.getBytes(),fileExt);

            if(StringUtils.isEmpty(groupPath))
            {
                throw new RuntimeException("店铺图标上传失败");
            }
            SellerShopVO sellerShopVO = new SellerShopVO();
            sellerShopVO.setLogo(groupPath);

            //设置预览
            if (sellerShopVO.getLogo() != null) {
                sellerShopVO.setHttpLogo(imageUploadService.getImageHttpPrefix() + sellerShopVO.getLogo());
            }
            resultObjectVO.setData(sellerShopVO);
        }catch (Exception e)
        {
            resultObjectVO.setCode(1);
            resultObjectVO.setMsg("店铺图标上传失败");
            logger.warn(e.getMessage(),e);
        }

        return resultObjectVO;
    }






    /**
     * 修改
     * @param entity
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"shopping:seller:shop:update:api"})
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public ResultObjectVO update(HttpServletRequest request, @RequestBody SellerShopVO entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            entity.setUpdateAdminId(AdminLoginHolder.getCurrentAdminId());
            entity.setUpdateDate(new Date());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = sellerShopService.update(requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



}
