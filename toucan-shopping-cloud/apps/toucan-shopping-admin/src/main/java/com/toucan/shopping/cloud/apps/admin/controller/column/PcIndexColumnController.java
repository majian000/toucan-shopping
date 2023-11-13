package com.toucan.shopping.cloud.apps.admin.controller.column;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignAdminService;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignFunctionService;
import com.toucan.shopping.cloud.apps.admin.auth.web.controller.base.UIController;
import com.toucan.shopping.cloud.common.data.api.feign.service.FeignAreaService;
import com.toucan.shopping.cloud.content.api.feign.service.FeignColumnAreaService;
import com.toucan.shopping.cloud.content.api.feign.service.FeignColumnService;
import com.toucan.shopping.cloud.content.api.feign.service.FeignColumnTypeService;
import com.toucan.shopping.cloud.content.api.feign.service.FeignPcIndexColumnService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignShopProductService;
import com.toucan.shopping.modules.admin.auth.vo.AdminVO;
import com.toucan.shopping.modules.area.vo.AreaTreeVO;
import com.toucan.shopping.modules.area.vo.AreaVO;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.column.constant.PcIndexColumnConstant;
import com.toucan.shopping.modules.column.entity.ColumnArea;
import com.toucan.shopping.modules.column.page.ColumnPageInfo;
import com.toucan.shopping.modules.column.page.ColumnTypePageInfo;
import com.toucan.shopping.modules.column.vo.*;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.AuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.content.entity.BannerArea;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.layui.vo.TableVO;
import com.toucan.shopping.modules.product.vo.ShopProductVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 首页栏目管理
 */
@Controller
@RequestMapping("/column/pcIndexColumn")
public class PcIndexColumnController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignFunctionService feignFunctionService;

    @Autowired
    private FeignPcIndexColumnService feignPcIndexColumnService;

    @Autowired
    private FeignAdminService feignAdminService;

    @Autowired
    private FeignColumnTypeService feignColumnTypeService;

    @Autowired
    private FeignAreaService feignAreaService;

    @Autowired
    private FeignColumnAreaService feignColumnAreaService;

    @Autowired
    private FeignShopProductService feignShopProductService;

    @Autowired
    private ImageUploadService imageUploadService;


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/listPage",method = RequestMethod.GET)
    public String listPage(HttpServletRequest request)
    {
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/column/pcIndexColumn/listPage",feignFunctionService);

        initColumnTypeCode(request);

        return "pages/column/pcIndexColumn/list.html";
    }

    private void initColumnTypeCode(HttpServletRequest request)
    {
        try {
            request.setAttribute("columnTypeCode",toucan.getShoppingPC().getPcIndexColumnTypeCode());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            request.setAttribute("columnTypeCode","");
        }
    }



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/addPage",method = RequestMethod.GET)
    public String addPage(HttpServletRequest request)
    {
        initColumnTypeCode(request);
        return "pages/column/pcIndexColumn/add.html";
    }

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/editPage/{id}",method = RequestMethod.GET)
    public String editPage(HttpServletRequest request,@PathVariable Long id)
    {
        request.setAttribute("id",id);
        return "pages/column/pcIndexColumn/edit.html";
    }


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/showPage/{id}",method = RequestMethod.GET)
    public String showPage(HttpServletRequest request,@PathVariable Long id)
    {
        request.setAttribute("id",id);
        return "pages/column/pcIndexColumn/show.html";
    }


    /**
     * 保存
     * @param pcIndexColumnVO
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO save(HttpServletRequest request,@RequestBody PcIndexColumnVO pcIndexColumnVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            pcIndexColumnVO.setAppCode(toucan.getShoppingPC().getAppCode());
            pcIndexColumnVO.setCreateAdminId(AuthHeaderUtil.getAdminId(toucan.getAppCode(),request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));
            pcIndexColumnVO.setPosition(1);
            pcIndexColumnVO.setColumnTypeCode(PcIndexColumnConstant.PC_INDEX_PRODUCT_RECOMMENT_COLUMN_TYPE_CODE);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, pcIndexColumnVO);
            resultObjectVO = feignPcIndexColumnService.save(requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请稍后重试");
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
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO update(HttpServletRequest request, @RequestBody PcIndexColumnVO entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            entity.setAppCode(toucan.getShoppingPC().getAppCode());
            entity.setColumnTypeCode(PcIndexColumnConstant.PC_INDEX_PRODUCT_RECOMMENT_COLUMN_TYPE_CODE);
            entity.setPosition(1);
            entity.setUpdateAdminId(AuthHeaderUtil.getAdminId(toucan.getAppCode(),request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = feignPcIndexColumnService.update(requestJsonVO);
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
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/findById",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO findById(HttpServletRequest request, @RequestBody PcIndexColumnVO entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            entity.setAppCode(toucan.getShoppingPC().getAppCode());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = feignPcIndexColumnService.findById(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                PcIndexColumnVO pcIndexColumnVO = resultObjectVO.formatData(PcIndexColumnVO.class);

                //右侧顶部预览图
                pcIndexColumnVO.getRightTopBanner().setHttpImgPath(imageUploadService.getImageHttpPrefix()+pcIndexColumnVO.getRightTopBanner().getImgPath());
                //右侧底部预览图
                pcIndexColumnVO.getRightBottomBanner().setHttpImgPath(imageUploadService.getImageHttpPrefix()+pcIndexColumnVO.getRightBottomBanner().getImgPath());
                //左侧轮播图
                for(ColumnBannerVO columnBannerVO:pcIndexColumnVO.getColumnLeftBannerVOS())
                {
                    columnBannerVO.setHttpImgPath(imageUploadService.getImageHttpPrefix()+columnBannerVO.getImgPath());
                }

                //顶部预览图
                if(pcIndexColumnVO.getTopBanner()!=null&&pcIndexColumnVO.getTopBanner().getImgPath()!=null)
                {
                    pcIndexColumnVO.getTopBanner().setHttpImgPath(imageUploadService.getImageHttpPrefix()+pcIndexColumnVO.getTopBanner().getImgPath());
                }

                //底部预览图
                if(pcIndexColumnVO.getBottomBanner()!=null&&pcIndexColumnVO.getBottomBanner().getImgPath()!=null)
                {
                    pcIndexColumnVO.getBottomBanner().setHttpImgPath(imageUploadService.getImageHttpPrefix()+pcIndexColumnVO.getBottomBanner().getImgPath());
                }

                //商品推荐图
                if(!CollectionUtils.isEmpty(pcIndexColumnVO.getColumnRecommendProducts()))
                {
                    for(ColumnRecommendProductVO columnRecommendProductVO:pcIndexColumnVO.getColumnRecommendProducts())
                    {
                        columnRecommendProductVO.setHttpImgPath(imageUploadService.getImageHttpPrefix()+columnRecommendProductVO.getImgPath());
                    }
                }


                resultObjectVO.setData(pcIndexColumnVO);
            }
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    /**
     * 查询列表
     * @param pageInfo
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ResponseBody
    public TableVO list(HttpServletRequest request, ColumnPageInfo pageInfo)
    {
        TableVO tableVO = new TableVO();
        try {
            pageInfo.setAppCode(toucan.getShoppingPC().getAppCode());
            pageInfo.setColumnTypeCode(PcIndexColumnConstant.PC_INDEX_PRODUCT_RECOMMENT_COLUMN_TYPE_CODE);
            pageInfo.setPosition(1);

            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),pageInfo);
            ResultObjectVO resultObjectVO = feignPcIndexColumnService.queryListPage(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                if(resultObjectVO.getData()!=null)
                {
                    Map<String,Object> resultObjectDataMap = (Map<String,Object>)resultObjectVO.getData();
                    tableVO.setCount(Long.parseLong(String.valueOf(resultObjectDataMap.get("total")!=null?resultObjectDataMap.get("total"):"0")));
                    List<ColumnVO> list = JSONArray.parseArray(JSONObject.toJSONString(resultObjectDataMap.get("list")),ColumnVO.class);

                    //查询创建人和修改人
                    List<String> adminIdList = new ArrayList<String>();
                    for(int i=0;i<list.size();i++)
                    {
                        ColumnVO columnVO = list.get(i);
                        if(columnVO.getCreateAdminId()!=null) {
                            adminIdList.add(columnVO.getCreateAdminId());
                        }
                        if(columnVO.getUpdateAdminId()!=null)
                        {
                            adminIdList.add(columnVO.getUpdateAdminId());
                        }
                    }
                    String[] createOrUpdateAdminIds = new String[adminIdList.size()];
                    adminIdList.toArray(createOrUpdateAdminIds);
                    AdminVO queryAdminVO = new AdminVO();
                    queryAdminVO.setAdminIds(createOrUpdateAdminIds);
                    requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryAdminVO);
                    resultObjectVO = feignAdminService.queryListByEntity(requestJsonVO.sign(),requestJsonVO);
                    if(resultObjectVO.isSuccess())
                    {
                        List<AdminVO> adminVOS = (List<AdminVO>)resultObjectVO.formatDataList(AdminVO.class);
                        if(!CollectionUtils.isEmpty(adminVOS))
                        {
                            for(ColumnVO columnVO:list)
                            {
                                for(AdminVO adminVO:adminVOS)
                                {
                                    if(columnVO.getCreateAdminId()!=null&&columnVO.getCreateAdminId().equals(adminVO.getAdminId()))
                                    {
                                        columnVO.setCreateAdminName(adminVO.getUsername());
                                    }
                                    if(columnVO.getUpdateAdminId()!=null&&columnVO.getUpdateAdminId().equals(adminVO.getAdminId()))
                                    {
                                        columnVO.setUpdateAdminName(adminVO.getUsername());
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




    public void setTreeNodeSelect(AtomicLong id,AreaTreeVO parentTreeVO,List<AreaTreeVO> areaTreeVOList,List<ColumnArea> columnAreas)
    {
        for(AreaTreeVO areaTreeVO:areaTreeVOList)
        {
            areaTreeVO.setId(id.incrementAndGet());
            areaTreeVO.setNodeId(areaTreeVO.getId());
            areaTreeVO.setPid(parentTreeVO.getId());
            areaTreeVO.setParentId(areaTreeVO.getPid());
            for(ColumnArea columnArea:columnAreas) {
                if(areaTreeVO.getCode().equals(columnArea.getAreaCode())) {
                    //设置节点被选中
                    areaTreeVO.getState().setChecked(true);
                    break;
                }
            }
            if(!CollectionUtils.isEmpty(areaTreeVO.getChildren()))
            {
                setTreeNodeSelect(id,areaTreeVO,(List)areaTreeVO.getChildren(),columnAreas);
            }
        }
    }


    /**
     * 查询地区树
     * @param request
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/query/area/tree",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryAreaTree(HttpServletRequest request,String columnId)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            //查询地区树
            AreaVO query = new AreaVO();
            query.setAppCode(toucan.getShoppingPC().getAppCode());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),query);

            resultObjectVO = feignAreaService.queryTree(requestJsonVO.sign(),requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                List<AreaTreeVO> areaTreeVOList = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()), AreaTreeVO.class);

                //重新设置ID,由于这个树是多个表合并而成,可能会存在ID重复
                AtomicLong id = new AtomicLong();
                ColumnAreaVO queryBannerAreaVo = new ColumnAreaVO();
                if(StringUtils.isNotEmpty(columnId)) {
                    queryBannerAreaVo.setColumnId(Long.parseLong(columnId));
                }
                requestJsonVO = RequestJsonVOGenerator.generator(appCode,queryBannerAreaVo);

                resultObjectVO = feignColumnAreaService.queryColumnAreaList(requestJsonVO.sign(),requestJsonVO);
                List<AreaTreeVO> releaseAreaTreeVOList = new ArrayList<AreaTreeVO>();
                if(resultObjectVO.isSuccess())
                {
                    //只保留省市节点
                    AreaTreeVO rootTree = areaTreeVOList.get(0);
                    if(!CollectionUtils.isEmpty(rootTree.getChildren())) {
                        List<AreaTreeVO> rootChilren = JSONArray.parseArray(JSONObject.toJSONString(rootTree.getChildren()), AreaTreeVO.class);
                        for (AreaTreeVO areaTreeVO : rootChilren) {
                            //直辖市
                            if (areaTreeVO.getIsMunicipality().shortValue() == 1) {
                                areaTreeVO.setChildren(null);
                            } else { //省
                                //遍历所有市节点,删除区县节点
                                if (!CollectionUtils.isEmpty(areaTreeVO.getChildren())) {
                                    List<AreaTreeVO> chilren = JSONArray.parseArray(JSONObject.toJSONString(areaTreeVO.getChildren()), AreaTreeVO.class);
                                    for (AreaVO cityTreeVO : chilren) {
                                        //删除区县节点
                                        cityTreeVO.setChildren(null);
                                    }
                                    areaTreeVO.setChildren(chilren);
                                }
                            }
                        }
                        rootTree.setChildren(rootChilren);
                    }
                    releaseAreaTreeVOList.add(rootTree);
                    List<ColumnArea> columnAreas = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()), ColumnArea.class);
                    if(!CollectionUtils.isEmpty(columnAreas)) {
                        for(AreaTreeVO areaTreeVO:releaseAreaTreeVOList) {
                            areaTreeVO.setId(id.incrementAndGet());
                            areaTreeVO.setNodeId(areaTreeVO.getId());
                            areaTreeVO.setText(areaTreeVO.getTitle());
                            for(ColumnArea columnArea:columnAreas) {
                                if(areaTreeVO.getCode().equals(columnArea.getAreaCode())) {
                                    //设置节点被选中
                                    areaTreeVO.getState().setChecked(true);
                                }
                            }
                            setTreeNodeSelect(id,areaTreeVO,(List)areaTreeVO.getChildren(), columnAreas);
                        }
                    }
                }
                resultObjectVO.setData(releaseAreaTreeVOList);
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


    /**
     * 删除
     * @param request
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
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
            ColumnVO columnVO =new ColumnVO();
            columnVO.setId(Long.parseLong(id));

            String entityJson = JSONObject.toJSONString(columnVO);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(appCode);
            requestVo.setEntityJson(entityJson);

            resultObjectVO = feignPcIndexColumnService.deleteById(requestVo);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



}

