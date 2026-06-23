package com.toucan.shopping.cloud.apps.admin.controller.column;


import com.toucan.shopping.cloud.admin.auth.api.AdminServiceAPI;
import com.toucan.shopping.cloud.admin.auth.api.AppServiceAPI;
import com.toucan.shopping.cloud.admin.auth.api.DictServiceAPI;
import com.toucan.shopping.cloud.admin.auth.api.FunctionServiceAPI;
import com.toucan.shopping.cloud.apps.admin.auth.web.controller.base.UIController;
import com.toucan.shopping.cloud.content.api.feign.service.FeignColumnService;
import com.toucan.shopping.cloud.content.api.feign.service.FeignColumnTypeService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignShopProductService;
import com.toucan.shopping.modules.admin.auth.vo.AdminVO;
import com.toucan.shopping.modules.admin.auth.vo.DictVO;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.column.constant.ColumnDictConstant;
import com.toucan.shopping.modules.column.entity.Column;
import com.toucan.shopping.modules.column.page.ColumnPageInfo;
import com.toucan.shopping.modules.column.vo.*;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.AlphabetNumberUtils;
import com.toucan.shopping.modules.common.util.AuthHeaderUtil;
import com.toucan.shopping.modules.common.util.DateUtils;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultTypeObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
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

import jakarta.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

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
    private FunctionServiceAPI functionServiceAPI;

    @Autowired
    private FeignColumnService feignColumnService;

    @Autowired
    private AdminServiceAPI adminServiceAPI;

    @Autowired
    private FeignColumnTypeService feignColumnTypeService;


    @Autowired
    private FeignShopProductService feignShopProductService;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private DictServiceAPI dictServiceAPI;

    @Autowired
    private AppServiceAPI appServiceAPI;


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/listPage",method = RequestMethod.GET)
    public String listPage(HttpServletRequest request) throws NoSuchAlgorithmException {
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/column/listPage", functionServiceAPI);
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


    /**
     * 修改
     * @param entity
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO update(HttpServletRequest request, @RequestBody ColumnVO entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {

            if(StringUtils.isEmpty(entity.getCode()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("修改失败,请输入编码");
                return resultObjectVO;
            }
            if(!AlphabetNumberUtils.isAlphabetNumber(entity.getCode(),1,100))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("修改失败,编码只允许字母、数字、下划线组成,长度1-100位");
                return resultObjectVO;
            }

            entity.setAppCode(toucan.getShoppingPC().getAppCode());
            entity.setUpdateAdminId(AuthHeaderUtil.getAdminId(toucan.getAppCode(),request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));
            entity.setUpdateDate(new Date());

            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = feignColumnService.update(requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/editPage/{id}",method = RequestMethod.GET)
    public String editPage(HttpServletRequest request,@PathVariable Long id)
    {
        try {
            this.setColumnDictList(request);
            ColumnVO queryEntity = new ColumnVO();
            queryEntity.setId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, queryEntity);
            ResultTypeObjectVO<ColumnVO> resultObjectVO = feignColumnService.findById(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                ColumnVO columnVO = resultObjectVO.getData();

                if(columnVO!=null){
                    request.setAttribute("defaultPosition","1");
                    ColumnTypeVO queryColumnTypeVO= new ColumnTypeVO();
                    queryColumnTypeVO.setCode(columnVO.getColumnTypeCode());
                    ResultTypeObjectVO<ColumnTypeVO> resultTypeObjectVO = feignColumnTypeService.findOneByCode(RequestJsonVOGenerator.generator(toucan.getAppCode(),queryColumnTypeVO));
                    if(resultTypeObjectVO.isSuccess()){
                        if(resultTypeObjectVO.getData()!=null){
                            request.setAttribute("columnTypeName",resultTypeObjectVO.getData().getName());
                        }
                    }
                    if(columnVO.getStartShowDate()!=null) {
                        columnVO.setStartShowDateString(DateUtils.FORMATTER_SS.get().format(columnVO.getStartShowDate()));
                    }
                    if(columnVO.getEndShowDate()!=null) {
                        columnVO.setEndShowDateString(DateUtils.FORMATTER_SS.get().format(columnVO.getEndShowDate()));
                    }
                    List<String> selectTypes = new LinkedList<>();
                    if(StringUtils.isNotEmpty(columnVO.getType())){
                        selectTypes.addAll(Arrays.asList(columnVO.getType().split(",")));
                    }
                    request.setAttribute("selectTypes",selectTypes);
                }
                request.setAttribute("model",columnVO);
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return "pages/column/column/edit.html";
    }

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/query/type/list",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryCategoryTreeByParentId(@RequestParam(defaultValue = "-1") Long id)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
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
    public ResultObjectVO queryColumnTree(HttpServletRequest request,ColumnTreeVO queryColumnTreeVO)
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
                resultObjectVO = feignColumnService.queryColumnTreeByPid(requestJsonVO);
                return resultObjectVO;
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
                        this.setColumnDictList(request);
                        List<DictVO> columnTypeList = request.getAttribute("columnTypeList")!=null
                                ?(List<DictVO>)request.getAttribute("columnTypeList"):null;
                        List<DictVO> columnPositionList = request.getAttribute("columnPositionList")!=null
                                ?(List<DictVO>)request.getAttribute("columnPositionList"):null;
                        Map<String, DictVO> columnTypeMap = null;
                        if(columnTypeList!=null) {
                            columnTypeMap = columnTypeList.stream()
                                    .collect(Collectors.toMap(DictVO::getCode, dict -> dict));
                        }

                        Map<String, DictVO> columnPositionMap = null;
                        if(columnPositionList!=null) {
                            columnPositionMap = columnPositionList.stream()
                                    .collect(Collectors.toMap(DictVO::getCode, dict -> dict));
                        }
                        for (ColumnTreeVO columnTreeVO : columnTreeVOS) {
                            if (columnTreeVO.getCreateAdminId() != null) {
                                adminIdList.add(columnTreeVO.getCreateAdminId());
                            }
                            if (columnTreeVO.getUpdateAdminId() != null) {
                                adminIdList.add(columnTreeVO.getUpdateAdminId());
                            }
                            //设置栏目类型名称
                            if(StringUtils.isNotEmpty(columnTreeVO.getType())){
                                if(columnTypeMap!=null){
                                    String[] types = columnTreeVO.getType().split(",");
                                    String typeNames = "";
                                    for(int i=0;i<types.length;i++){
                                        String type = types[i];
                                        typeNames+=columnTypeMap.get(type).getName();
                                        if((i+1)<types.length){
                                            typeNames+=",";
                                        }
                                    }
                                    columnTreeVO.setTypeNames(typeNames);
                                }
                            }
                            //设置栏目位置
                            if(StringUtils.isNotEmpty(columnTreeVO.getPosition())) {
                                if (columnPositionMap != null) {
                                    String[] positions = columnTreeVO.getPosition().split(",");
                                    String positionNames = "";
                                    for (int i = 0; i < positions.length; i++) {
                                        String position = positions[i];
                                        positionNames += columnPositionMap.get(position).getName();
                                        if ((i+1) < positions.length) {
                                            positionNames += ",";
                                        }
                                    }
                                    columnTreeVO.setPositionNames(positionNames);
                                }
                            }
                            appCodes.add(columnTreeVO.getAppCode());
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

            if(!AlphabetNumberUtils.isAlphabetNumber(columnVO.getCode(),1,100))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("修改失败,编码只允许字母、数字、下划线组成,长度1-100位");
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
        ResultObjectVO resultObjectVO = adminServiceAPI.queryListByEntity(requestJsonVO.sign(),requestJsonVO);
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
        ResultTypeObjectVO<List<DictVO>> resultObjectVO = dictServiceAPI.queryDictByCodesAndCategoryCode(requestJsonVO);
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

