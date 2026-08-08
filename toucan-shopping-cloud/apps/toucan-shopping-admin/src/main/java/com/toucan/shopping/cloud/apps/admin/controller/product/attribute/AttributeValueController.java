package com.toucan.shopping.cloud.apps.admin.controller.product.attribute;


import com.toucan.shopping.cloud.apps.admin.helper.PageHelper;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.AdminServiceAPI;
import com.toucan.shopping.cloud.product.api.AttributeValueServiceAPI;
import com.toucan.shopping.modules.admin.auth.holder.AdminLoginHolder;
import com.toucan.shopping.modules.admin.auth.vo.AdminVO;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.layui.vo.TableVO;
import com.toucan.shopping.modules.product.entity.AttributeValue;
import com.toucan.shopping.modules.product.page.AttributeValuePageInfo;
import com.toucan.shopping.modules.product.vo.AttributeValueVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 商品属性管理
 */
@RestController
@RequestMapping("/product/attribute/attributeValue")
public class AttributeValueController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private AttributeValueServiceAPI attributeValueService;

    @Autowired
    private AdminServiceAPI adminServiceAPI;



    /**
     * 查询列表
     * @param pageInfo
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"shopping:attributeKey:attributeValue:list:api"})
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public TableVO list(AttributeValuePageInfo pageInfo)
    {
        TableVO tableVO = new TableVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),pageInfo);
            ResultObjectVO resultObjectVO = attributeValueService.queryListPage(requestJsonVO);
            if(resultObjectVO.getCode() == ResultObjectVO.SUCCESS)
            {
                if(resultObjectVO.getData()!=null)
                {
                    Map<String,Object> resultObjectDataMap = PageHelper.extractPageData(resultObjectVO.getData());
                    tableVO.setCount(Long.parseLong(String.valueOf(resultObjectDataMap.get("total")!=null?resultObjectDataMap.get("total"):"0")));
                    List<AttributeValueVO> list = JSONArray.parseArray(JSONObject.toJSONString(resultObjectDataMap.get("list")),AttributeValueVO.class);
                    if(CollectionUtils.isNotEmpty(list))
                    {
                        List<String> adminIdList = new ArrayList<String>();
                        for(int i=0;i<list.size();i++)
                        {
                            AttributeValueVO attributeValueVO = list.get(i);
                            if(attributeValueVO.getCreateAdminId()!=null) {
                                adminIdList.add(attributeValueVO.getCreateAdminId());
                            }
                            if(attributeValueVO.getUpdateAdminId()!=null)
                            {
                                adminIdList.add(attributeValueVO.getUpdateAdminId());
                            }
                        }

                        //查询创建人和修改人
                        String[] createOrUpdateAdminIds = new String[adminIdList.size()];
                        adminIdList.toArray(createOrUpdateAdminIds);
                        AdminVO queryAdminVO = new AdminVO();
                        queryAdminVO.setAdminIds(createOrUpdateAdminIds);
                        requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryAdminVO);
                        resultObjectVO = adminServiceAPI.queryListByEntity(requestJsonVO);
                        if(resultObjectVO.isSuccess())
                        {
                            List<AdminVO> adminVOS = (List<AdminVO>)resultObjectVO.formatDataList(AdminVO.class);
                            if(CollectionUtils.isNotEmpty(adminVOS))
                            {
                                for(AttributeValueVO attributeValueVO:list)
                                {
                                    for(AdminVO adminVO:adminVOS)
                                    {
                                        if(attributeValueVO.getCreateAdminId()!=null&&attributeValueVO.getCreateAdminId().equals(adminVO.getAdminId()))
                                        {
                                            attributeValueVO.setCreateAdminName(adminVO.getUsername());
                                        }
                                        if(attributeValueVO.getUpdateAdminId()!=null&&attributeValueVO.getUpdateAdminId().equals(adminVO.getAdminId()))
                                        {
                                            attributeValueVO.setUpdateAdminName(adminVO.getUsername());
                                        }
                                    }
                                }
                            }
                        }

                    }
                    if(tableVO.getCount()>0) {
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
     * 保存
     * @param entity
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"shopping:product:attribute:value:save:api"})
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public ResultObjectVO save(@RequestBody AttributeValueVO entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            entity.setCreateAdminId(AdminLoginHolder.getCurrentAdminId());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = attributeValueService.save(requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }





    /**
     * 修改
     * @param entity
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"shopping:attributeValue:update:api"})
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public ResultObjectVO update(@RequestBody AttributeValueVO entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            entity.setUpdateAdminId(AdminLoginHolder.getCurrentAdminId());
            entity.setUpdateDate(new Date());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = attributeValueService.update(requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }




    /**
     * 删除
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"shopping:attributeValue:delete:api"})
    @RequestMapping(value = "/delete/{id}",method = RequestMethod.DELETE)
    public ResultObjectVO deleteById(@PathVariable String id)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(StringUtils.isEmpty(id))
            {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            AttributeValue attributeValue =new AttributeValue();
            attributeValue.setId(Long.parseLong(id));
            attributeValue.setUpdateAdminId(AdminLoginHolder.getCurrentAdminId());

            String entityJson = JSONObject.toJSONString(attributeValue);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(appCode);
            requestVo.setEntityJson(entityJson);
            resultObjectVO = attributeValueService.deleteById( requestVo);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



    /**
     * 删除
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"shopping:attributeValue:deletes:api"})
    @RequestMapping(value = "/delete/ids",method = RequestMethod.DELETE)
    public ResultObjectVO deleteByIds( @RequestBody List<AttributeValueVO> attributeValueVOS)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(org.springframework.util.CollectionUtils.isEmpty(attributeValueVOS))
            {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            String entityJson = JSONObject.toJSONString(attributeValueVOS);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(appCode);
            requestVo.setEntityJson(entityJson);
            resultObjectVO = attributeValueService.deleteByIds( requestVo);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }








}
