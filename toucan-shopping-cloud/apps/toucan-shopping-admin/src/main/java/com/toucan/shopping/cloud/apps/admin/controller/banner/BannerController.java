package com.toucan.shopping.cloud.apps.admin.controller.banner;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.*;
import com.toucan.shopping.cloud.apps.admin.auth.web.controller.base.UIController;
import com.toucan.shopping.cloud.area.api.feign.service.FeignAreaService;
import com.toucan.shopping.cloud.area.api.feign.service.FeignBannerAreaService;
import com.toucan.shopping.cloud.area.api.feign.service.FeignBannerService;
import com.toucan.shopping.modules.admin.auth.entity.Admin;
import com.toucan.shopping.modules.admin.auth.entity.AdminApp;
import com.toucan.shopping.modules.admin.auth.page.AdminPageInfo;
import com.toucan.shopping.modules.admin.auth.vo.*;
import com.toucan.shopping.modules.area.entity.Area;
import com.toucan.shopping.modules.area.entity.Banner;
import com.toucan.shopping.modules.area.entity.BannerArea;
import com.toucan.shopping.modules.area.page.BannerPageInfo;
import com.toucan.shopping.modules.area.vo.AreaTreeVO;
import com.toucan.shopping.modules.area.vo.AreaVO;
import com.toucan.shopping.modules.area.vo.BannerAreaVO;
import com.toucan.shopping.modules.area.vo.BannerVO;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.AuthHeaderUtil;
import com.toucan.shopping.modules.common.util.DateUtils;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 轮播图管理
 */
@Controller
@RequestMapping("/banner")
public class BannerController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignFunctionService feignFunctionService;

    @Autowired
    private FeignBannerAreaService feignBannerAreaService;

    @Autowired
    private FeignBannerService feignBannerService;

    @Autowired
    private FeignAreaService feignAreaService;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private FeignAdminService feignAdminService;

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/listPage",method = RequestMethod.GET)
    public String listPage(HttpServletRequest request)
    {
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/banner/listPage",feignFunctionService);
        return "pages/banner/list.html";
    }



    /**
     * 查询列表
     * @param pageInfo
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ResponseBody
    public TableVO list(HttpServletRequest request, BannerPageInfo pageInfo)
    {
        TableVO tableVO = new TableVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),pageInfo);
            ResultObjectVO resultObjectVO = feignBannerService.queryListPage(SignUtil.sign(requestJsonVO),requestJsonVO);
            if(resultObjectVO.getCode() == ResultObjectVO.SUCCESS)
            {
                if(resultObjectVO.getData()!=null)
                {
                    Map<String,Object> resultObjectDataMap = (Map<String,Object>)resultObjectVO.getData();
                    tableVO.setCount(Long.parseLong(String.valueOf(resultObjectDataMap.get("total")!=null?resultObjectDataMap.get("total"):"0")));
                    List<BannerVO> list = JSONArray.parseArray(JSONObject.toJSONString(resultObjectDataMap.get("list")),BannerVO.class);


                    //查询创建人和修改人
                    List<String> adminIdList = new ArrayList<String>();
                    for(int i=0;i<list.size();i++)
                    {
                        BannerVO bannerVO = list.get(i);
                        if(bannerVO.getCreateAdminId()!=null) {
                            adminIdList.add(bannerVO.getCreateAdminId());
                        }
                        if(bannerVO.getUpdateAdminId()!=null)
                        {
                            adminIdList.add(bannerVO.getUpdateAdminId());
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
                        List<AdminVO> adminVOS = (List<AdminVO>)resultObjectVO.formatDataArray(AdminVO.class);
                        if(!CollectionUtils.isEmpty(adminVOS))
                        {
                            for(BannerVO bannerVO:list)
                            {
                                for(AdminVO adminVO:adminVOS)
                                {
                                    if(bannerVO.getCreateAdminId()!=null&&bannerVO.getCreateAdminId().equals(adminVO.getAdminId()))
                                    {
                                        bannerVO.setCreateAdminName(adminVO.getUsername());
                                    }
                                    if(bannerVO.getUpdateAdminId()!=null&&bannerVO.getUpdateAdminId().equals(adminVO.getAdminId()))
                                    {
                                        bannerVO.setUpdateAdminName(adminVO.getUsername());
                                    }
                                }
                            }
                        }
                    }
                    for(BannerVO bannerVO:list)
                    {
                        if(bannerVO.getImgPath()!=null) {
                            bannerVO.setHttpImgPath(imageUploadService.getImageHttpPrefix() + bannerVO.getImgPath());
                        }
                    }
                    if(tableVO.getCount()>0) {
                        tableVO.setData((List)list);
                    }
                }
            }
        }catch(Exception e)
        {
            tableVO.setMsg("请求失败,请重试");
            tableVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return tableVO;
    }



    /**
     * 删除
     * @param request
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/delete/{id}",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteById(HttpServletRequest request,  @PathVariable String id)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(StringUtils.isEmpty(id))
            {
                resultObjectVO.setMsg("请求失败,请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            Banner banner =new Banner();
            banner.setId(Long.parseLong(id));
            banner.setUpdateAdminId(AuthHeaderUtil.getAdminId(request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));

            String entityJson = JSONObject.toJSONString(banner);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(appCode);
            requestVo.setEntityJson(entityJson);
            //先查询出实体对象,后面删除文件服务器的资源
            resultObjectVO = feignBannerService.findById(requestVo.sign(), requestVo);
            List<BannerVO> bannerVOS = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()),BannerVO.class);
            if(resultObjectVO.isSuccess()) {
                resultObjectVO = feignBannerService.deleteById(SignUtil.sign(requestVo),requestVo);
                if(!CollectionUtils.isEmpty(bannerVOS))
                {
                    banner = bannerVOS.get(0);
                    int ret = imageUploadService.deleteFile(banner.getImgPath());
                    if(ret!=0)
                    {
                        logger.warn("删除服务器中关联图片失败 {} ",banner.getImgPath());
                        resultObjectVO.setMsg("删除关联图片资源失败");
                        resultObjectVO.setCode(TableVO.FAILD);
                    }
                }
            }
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败,请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



    /**
     * 删除
     * @param request
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/delete/ids",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteByIds(HttpServletRequest request, @RequestBody List<BannerVO> bannerVOS)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(CollectionUtils.isEmpty(bannerVOS))
            {
                resultObjectVO.setMsg("请求失败,请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            String entityJson = JSONObject.toJSONString(bannerVOS);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(appCode);
            requestVo.setEntityJson(entityJson);
            resultObjectVO = feignBannerService.deleteByIds(SignUtil.sign(requestVo), requestVo);
            if(resultObjectVO.isSuccess()) {
                if(!CollectionUtils.isEmpty(bannerVOS))
                {
                    for(BannerVO bannerVO:bannerVOS) {
                        int ret = imageUploadService.deleteFile(bannerVO.getImgPath());
                        if (ret != 0) {
                            logger.warn("删除服务器中关联图片失败 {} ", bannerVO.getImgPath());
                            resultObjectVO.setMsg("删除关联图片资源失败");
                            resultObjectVO.setCode(ResultObjectVO.FAILD);
                        }
                    }
                }
            }
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败,请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    /**
     * 刷新redis缓存
     * @param request
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/flush/index/cache",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO flushIndexCache(HttpServletRequest request, @RequestBody List<BannerVO> bannerVOS)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(CollectionUtils.isEmpty(bannerVOS))
            {
                resultObjectVO.setMsg("请求失败,请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            String entityJson = JSONObject.toJSONString(bannerVOS);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(appCode);
            requestVo.setEntityJson(entityJson);
            resultObjectVO = feignBannerService.flushWebIndexCache(SignUtil.sign(requestVo), requestVo);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败,请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }

    /**
     * 清空PC首页缓存
     * @param request
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/clear/index/cache",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO clearIndexCache(HttpServletRequest request)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            BannerVO bannerVO = new BannerVO();
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, bannerVO);
            resultObjectVO = feignBannerService.clearWebIndexCache(requestJsonVO.sign(),requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败,请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping("/upload/img")
    @ResponseBody
    public ResultObjectVO  uploadHeadSculpture(@RequestParam("file") MultipartFile file)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        resultObjectVO.setCode(0);
        try{
            String fileName = file.getOriginalFilename();
            String fileExt = "jpg";
            if(StringUtils.isNotEmpty(fileName)&&fileName.indexOf(".")!=-1)
            {
                fileExt = fileName.substring(fileName.lastIndexOf(".")+1);

            }
            String groupPath = imageUploadService.uploadFile(file.getBytes(),fileExt);

            if(StringUtils.isEmpty(groupPath))
            {
                throw new RuntimeException("上传失败");
            }
            BannerVO bannerVO = new BannerVO();
            bannerVO.setImgPath(groupPath);
            bannerVO.setHttpImgPath(imageUploadService.getImageHttpPrefix()+groupPath);
            resultObjectVO.setData(bannerVO);
        }catch (Exception e)
        {
            resultObjectVO.setCode(1);
            resultObjectVO.setMsg("上传失败");
            logger.warn(e.getMessage(),e);
        }

        return resultObjectVO;
    }




    /**
     * 保存
     * @param entity
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO save(HttpServletRequest request, @RequestBody BannerVO entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            entity.setCreateAdminId(AuthHeaderUtil.getAdminId(request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = feignBannerService.save(SignUtil.sign(requestJsonVO),requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败,请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



    public void setTreeNodeSelect(AtomicLong id,AreaTreeVO parentTreeVO,List<AreaTreeVO> areaTreeVOList,List<BannerArea> bannerAreas)
    {
        for(AreaTreeVO areaTreeVO:areaTreeVOList)
        {
            areaTreeVO.setId(id.incrementAndGet());
            areaTreeVO.setNodeId(areaTreeVO.getId());
            areaTreeVO.setPid(parentTreeVO.getId());
            areaTreeVO.setParentId(areaTreeVO.getPid());
            for(BannerArea bannerArea:bannerAreas) {
                if(areaTreeVO.getCode().equals(bannerArea.getAreaCode())) {
                    //设置节点被选中
                    areaTreeVO.getState().setChecked(true);
                    break;
                }
            }
            if(!CollectionUtils.isEmpty(areaTreeVO.getChildren()))
            {
                setTreeNodeSelect(id,areaTreeVO,(List)areaTreeVO.getChildren(),bannerAreas);
            }
        }
    }


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/editPage/{id}",method = RequestMethod.GET)
    public String editPage(HttpServletRequest request,@PathVariable Long id)
    {
        try {
            Banner banner = new Banner();
            banner.setId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, banner);
            ResultObjectVO resultObjectVO = feignBannerService.findById(SignUtil.sign(requestJsonVO),requestJsonVO);
            if(resultObjectVO.getCode().intValue()==ResultObjectVO.SUCCESS.intValue())
            {
                if(resultObjectVO.getData()!=null) {
                    List<BannerVO> bannerVOS = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()),BannerVO.class);
                    if(!CollectionUtils.isEmpty(bannerVOS))
                    {
                        banner = bannerVOS.get(0);
                        banner.setHttpImgPath(imageUploadService.getImageHttpPrefix() + banner.getImgPath());
                        if(banner.getStartShowDate()!=null) {
                            banner.setStartShowDateString(DateUtils.format(banner.getStartShowDate(), DateUtils.FORMATTER_SS));
                        }

                        if(banner.getEndShowDate()!=null) {
                            banner.setEndShowDateString(DateUtils.format(banner.getEndShowDate(), DateUtils.FORMATTER_SS));
                        }

                        if(!CollectionUtils.isEmpty(banner.getAreas()))
                        {
                            StringBuilder areaNames = new StringBuilder();
                            StringBuilder areaCodes = new StringBuilder();
                            for(int i=0;i<banner.getAreas().size();i++)
                            {
                                Area area = banner.getAreas().get(i);
                                if(area!=null) {
                                    areaNames.append(area.getCity());
                                    areaCodes.append(area.getCode());
                                }
                                if(i+1<banner.getAreas().size())
                                {
                                    areaNames.append(",");
                                    areaCodes.append(",");
                                }
                            }
                            banner.setAreaNames(areaNames.toString());
                            banner.setAreaCodes(areaCodes.toString());
                        }

                        request.setAttribute("model",banner);
                    }
                }

            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return "pages/banner/edit.html";
    }


    /**
     * 修改
     * @param entity
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO update(HttpServletRequest request,@RequestBody BannerVO entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            entity.setUpdateAdminId(AuthHeaderUtil.getAdminId(request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));
            entity.setUpdateDate(new Date());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = feignBannerService.update(SignUtil.sign(requestJsonVO),requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败,请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }




    /**
     * 查询地区树
     * @param request
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/query/area/tree",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryAreaTree(HttpServletRequest request,String bannerId)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            //查询地区树
            AreaVO query = new AreaVO();
            query.setAppCode("10001001");
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),query);

            resultObjectVO = feignAreaService.queryTree(requestJsonVO.sign(),requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                List<AreaTreeVO> areaTreeVOList = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()), AreaTreeVO.class);

                //重新设置ID,由于这个树是多个表合并而成,可能会存在ID重复
                AtomicLong id = new AtomicLong();
                BannerAreaVO queryBannerAreaVo = new BannerAreaVO();
                if(StringUtils.isNotEmpty(bannerId)) {
                    queryBannerAreaVo.setBannerId(Long.parseLong(bannerId));
                }
                requestJsonVO = RequestJsonVOGenerator.generator(appCode,queryBannerAreaVo);

                resultObjectVO = feignBannerAreaService.queryBannerAreaList(requestJsonVO.sign(),requestJsonVO);
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
                    List<BannerArea> bannerAreas = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()), BannerArea.class);
                    if(!CollectionUtils.isEmpty(bannerAreas)) {
                        for(AreaTreeVO areaTreeVO:releaseAreaTreeVOList) {
                            areaTreeVO.setId(id.incrementAndGet());
                            areaTreeVO.setNodeId(areaTreeVO.getId());
                            areaTreeVO.setText(areaTreeVO.getTitle());
                            for(BannerArea bannerArea:bannerAreas) {
                                if(areaTreeVO.getCode().equals(bannerArea.getAreaCode())) {
                                    //设置节点被选中
                                    areaTreeVO.getState().setChecked(true);
                                }
                            }
                            setTreeNodeSelect(id,areaTreeVO,(List)areaTreeVO.getChildren(), bannerAreas);
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


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/addPage",method = RequestMethod.GET)
    public String addPage(HttpServletRequest request)
    {

        return "pages/banner/add.html";
    }



}

