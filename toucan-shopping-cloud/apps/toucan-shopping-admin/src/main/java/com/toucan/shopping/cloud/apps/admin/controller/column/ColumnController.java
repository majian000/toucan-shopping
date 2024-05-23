package com.toucan.shopping.cloud.apps.admin.controller.column;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignAdminService;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignAppService;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignDictService;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignFunctionService;
import com.toucan.shopping.cloud.apps.admin.auth.web.controller.base.UIController;
import com.toucan.shopping.cloud.common.data.api.feign.service.FeignAreaService;
import com.toucan.shopping.cloud.content.api.feign.service.FeignColumnAreaService;
import com.toucan.shopping.cloud.content.api.feign.service.FeignColumnService;
import com.toucan.shopping.cloud.content.api.feign.service.FeignColumnTypeService;
import com.toucan.shopping.cloud.content.api.feign.service.FeignIndexRecommendColumnService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignShopProductService;
import com.toucan.shopping.modules.admin.auth.entity.App;
import com.toucan.shopping.modules.admin.auth.vo.AdminVO;
import com.toucan.shopping.modules.admin.auth.vo.DictVO;
import com.toucan.shopping.modules.area.vo.AreaTreeVO;
import com.toucan.shopping.modules.area.vo.AreaVO;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.column.constant.ColumnDictConstant;
import com.toucan.shopping.modules.column.constant.PcIndexColumnConstant;
import com.toucan.shopping.modules.column.entity.Column;
import com.toucan.shopping.modules.column.entity.ColumnArea;
import com.toucan.shopping.modules.column.page.ColumnPageInfo;
import com.toucan.shopping.modules.column.vo.*;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.AuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultTypeObjectVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.layui.vo.TableVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 首页推荐栏目
 */
@Controller
@RequestMapping("/column")
public class ColumnController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignFunctionService feignFunctionService;

    @Autowired
    private FeignColumnService feignColumnService;

    @Autowired
    private FeignAdminService feignAdminService;

    @Autowired
    private FeignColumnTypeService feignColumnTypeService;


    @Autowired
    private FeignShopProductService feignShopProductService;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private FeignDictService feignDictService;

    @Autowired
    private FeignAppService feignAppService;


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/listPage",method = RequestMethod.GET)
    public String page(HttpServletRequest request) throws NoSuchAlgorithmException {
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/column/listPage",feignFunctionService);
        this.setColumnDictList(request);
        return "pages/column/column/list.html";
    }


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/addPage",method = RequestMethod.GET)
    public String addPage(HttpServletRequest request,@RequestParam String columnTypeCode) throws NoSuchAlgorithmException {
        this.setColumnDictList(request);
        request.setAttribute("columnTypeCode",columnTypeCode);
        request.setAttribute("defaultPosition","1");
        ColumnTypeVO queryColumnTypeVO= new ColumnTypeVO();
        queryColumnTypeVO.setCode(columnTypeCode);
        ResultTypeObjectVO<ColumnTypeVO> resultTypeObjectVO = feignColumnTypeService.findOneByCode(RequestJsonVOGenerator.generator(toucan.getAppCode(),queryColumnTypeVO));
        if(resultTypeObjectVO.isSuccess()){
            if(resultTypeObjectVO.getData()!=null){
                request.setAttribute("columnTypeName",resultTypeObjectVO.getData().getName());
            }
        }
        return "pages/column/column/add.html";
    }

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/query/type/list",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryCategoryTreeByParentId(@RequestParam Long id)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(id==null){
                id=-1L;
            }
            ColumnTypeVO query = new ColumnTypeVO();
            query.setAppCode(toucan.getShoppingPC().getAppCode());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode,query);
            resultObjectVO = feignColumnTypeService.queryList(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                if(resultObjectVO.getData()!=null) {
                    List<ColumnTypeTreeVO> columnTypeTreeVOS = resultObjectVO.formatDataList(ColumnTypeTreeVO.class);
                    for(ColumnTypeTreeVO columnTypeTreeVO:columnTypeTreeVOS)
                    {
                        columnTypeTreeVO.setOpen(false);
                        columnTypeTreeVO.setIcon(null);
                    }
                    resultObjectVO.setData(columnTypeTreeVOS);
                }
            }
            return resultObjectVO;
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }




    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType =AdminAuth.RESPONSE_FORM )
    @RequestMapping(value = "/query/column/tree")
    @ResponseBody
    public ResultObjectVO queryAppFunctionTree(HttpServletRequest request,ColumnTreeVO queryColumnTreeVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            //默认查询根节点
            if(queryColumnTreeVO.getId()==null)
            {
                ColumnTreeVO columnTreeVO = new ColumnTreeVO();
                columnTreeVO.setId(-1L);
                columnTreeVO.setPid(-2L);
                columnTreeVO.setParentId(-2L);
                columnTreeVO.setAppCode(toucan.getShoppingPC().getAppCode());
                columnTreeVO.setTitle("根节点");
                columnTreeVO.setName("根节点");
                columnTreeVO.setColumnTypeCode(queryColumnTreeVO.getColumnTypeCode());
                columnTreeVO.setIsParent(true);
                List<ColumnTreeVO> columnTrees = new LinkedList<>();
                columnTrees.add(columnTreeVO);
                resultObjectVO.setData(columnTrees);
            }else{
                queryColumnTreeVO.setParentId(queryColumnTreeVO.getId());
                queryColumnTreeVO.setAppCode(toucan.getShoppingPC().getAppCode());
                RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode,queryColumnTreeVO);
                return feignColumnService.queryColumnTreeByPid(requestJsonVO);
            }

        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }

    /**
     * 查询列表
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/tree/table/by/pid",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryTreeTableByPid(HttpServletRequest request, ColumnPageInfo pageInfo)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            RequestJsonVO requestJsonVO = null;
            if(StringUtils.isEmpty(pageInfo.getColumnTypeCode())){
                resultObjectVO.setMsg("栏目类型编码不能为空");
                resultObjectVO.setCode(TableVO.FAILD);
                return resultObjectVO;
            }

            requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),pageInfo);
            resultObjectVO = feignColumnService.queryTreeTableByPid(requestJsonVO);

            if(resultObjectVO.isSuccess()) {
                if (resultObjectVO.getData() != null) {

                    Set<String> adminIdList = new HashSet<String>();
                    Set<String> appCodes = new HashSet<>();
                    List<ColumnTreeVO> columnTreeVOS = resultObjectVO.formatDataList(ColumnTreeVO.class);
                    if(!CollectionUtils.isEmpty(columnTreeVOS)) {
                        for (ColumnTreeVO dictTreeVO : columnTreeVOS) {
                            if (dictTreeVO.getCreateAdminId() != null) {
                                adminIdList.add(dictTreeVO.getCreateAdminId());
                            }
                            if (dictTreeVO.getUpdateAdminId() != null) {
                                adminIdList.add(dictTreeVO.getUpdateAdminId());
                            }
                            appCodes.add(dictTreeVO.getAppCode());
                        }
                        this.setAdminNames(adminIdList, columnTreeVOS);
                        resultObjectVO.setData(columnTreeVOS);
                    }
                }
            }
            return resultObjectVO;
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



    /**
     * 保存
     * @param columnVO
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO save(HttpServletRequest request,@RequestBody ColumnVO columnVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(StringUtils.isEmpty(columnVO.getColumnTypeCode())){
                resultObjectVO.setMsg("栏目类型不能为空");
                resultObjectVO.setCode(TableVO.FAILD);
                return resultObjectVO;
            }
            columnVO.setAppCode(toucan.getShoppingPC().getAppCode());
            columnVO.setCreateAdminId(AuthHeaderUtil.getAdminId(toucan.getAppCode(),request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, columnVO);
            resultObjectVO = feignColumnService.save(requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请稍后重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



    /**
     * 设置管理员名称
     * @param adminIdList
     * @throws Exception
     */
    private void setAdminNames(Set<String> adminIdList, List<ColumnTreeVO> list) throws Exception{

        //查询创建人和修改人
        String[] createOrUpdateAdminIds = new String[adminIdList.size()];
        adminIdList.toArray(createOrUpdateAdminIds);
        AdminVO queryAdminVO = new AdminVO();
        queryAdminVO.setAdminIds(createOrUpdateAdminIds);
        RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryAdminVO);
        ResultObjectVO resultObjectVO = feignAdminService.queryListByEntity(requestJsonVO.sign(),requestJsonVO);
        if(resultObjectVO.isSuccess())
        {
            List<AdminVO> adminVOS = (List<AdminVO>)resultObjectVO.formatDataList(AdminVO.class);
            if(org.apache.commons.collections.CollectionUtils.isNotEmpty(adminVOS))
            {
                for(ColumnVO dictVO:list)
                {
                    for(AdminVO adminVO:adminVOS)
                    {
                        if(dictVO.getCreateAdminId()!=null&&dictVO.getCreateAdminId().equals(adminVO.getAdminId()))
                        {
                            dictVO.setCreateAdminName(adminVO.getUsername());
                        }
                        if(dictVO.getUpdateAdminId()!=null&&dictVO.getUpdateAdminId().equals(adminVO.getAdminId()))
                        {
                            dictVO.setUpdateAdminName(adminVO.getUsername());
                        }
                    }
                }
            }
        }
    }


    /**
     * 删除
     * @param request
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/delete/{id}",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteById(HttpServletRequest request,  @PathVariable String id)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(StringUtils.isEmpty(id))
            {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            ColumnVO entity =new ColumnVO();
            entity.setId(Long.parseLong(id));
            entity.setUpdateAdminId(AuthHeaderUtil.getAdminId(toucan.getAppCode(),request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));


            RequestJsonVO requestVo = RequestJsonVOGenerator.generator(appCode,entity);
            resultObjectVO = feignColumnService.deleteById(requestVo);
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
     * @param request
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/delete/ids",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteByIds(HttpServletRequest request, @RequestBody List<Column> columns)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(CollectionUtils.isEmpty(columns))
            {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }

            RequestJsonVO requestVo = RequestJsonVOGenerator.generator(appCode,columns);
            resultObjectVO = feignColumnService.deleteByIds(requestVo);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



    private void setColumnDictList(HttpServletRequest request) throws NoSuchAlgorithmException {
        //栏目字典
        DictVO queryDict=new DictVO();
        queryDict.setCategoryCode(ColumnDictConstant.COLUMN_DICT_CATEGORY_CODE);
        queryDict.setCodes(new LinkedList<>());
        queryDict.getCodes().add(ColumnDictConstant.COLUMN_DICT_TYPE_CODE);
        queryDict.getCodes().add(ColumnDictConstant.COLUMN_DICT_POSITION_CODE);
        queryDict.setAppCode(toucan.getAppCode());
        RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryDict);
        ResultTypeObjectVO<List<DictVO>> resultObjectVO = feignDictService.queryDictByCodesAndCategoryCode(requestJsonVO);
        if(resultObjectVO.isSuccess()) {
            if(!CollectionUtils.isEmpty(resultObjectVO.getData())){
                for(DictVO dictVO:resultObjectVO.getData()){
                    switch (dictVO.getCode()){
                        case ColumnDictConstant.COLUMN_DICT_TYPE_CODE:
                            request.setAttribute("columnTypeList",dictVO.getChildren());
                            break;
                        case ColumnDictConstant.COLUMN_DICT_POSITION_CODE:
                            request.setAttribute("columnPositionList",dictVO.getChildren());
                            break;
                    }
                }
            }
        }

    }

}

