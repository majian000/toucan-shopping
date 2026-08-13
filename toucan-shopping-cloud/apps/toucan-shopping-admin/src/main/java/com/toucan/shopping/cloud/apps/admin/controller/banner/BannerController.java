package com.toucan.shopping.cloud.apps.admin.controller.banner;


import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.AdminServiceAPI;
import com.toucan.shopping.cloud.common.data.api.AreaServiceAPI;
import com.toucan.shopping.cloud.content.api.BannerAreaServiceAPI;
import com.toucan.shopping.cloud.content.api.BannerServiceAPI;
import com.toucan.shopping.modules.admin.auth.holder.AdminLoginHolder;
import com.toucan.shopping.modules.admin.auth.vo.AdminVO;
import com.toucan.shopping.modules.area.vo.AreaTreeVO;
import com.toucan.shopping.modules.area.vo.AreaVO;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.ImageUtils;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.content.entity.Banner;
import com.toucan.shopping.modules.content.entity.BannerArea;
import com.toucan.shopping.modules.content.page.BannerPageInfo;
import com.toucan.shopping.modules.content.vo.BannerAreaVO;
import com.toucan.shopping.modules.content.vo.BannerDetailVO;
import com.toucan.shopping.modules.content.vo.BannerVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.layui.vo.TableVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;

import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * 轮播图管理
 */
@RestController
@RequestMapping("/banner")
public class BannerController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private BannerAreaServiceAPI bannerAreaService;

    @Autowired
    private BannerServiceAPI bannerService;

    @Autowired
    private AreaServiceAPI areaService;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private AdminServiceAPI adminServiceAPI;


    /**
     * 查询列表
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:content:banner:list"})
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public TableVO list(HttpServletRequest request, @RequestBody BannerPageInfo pageInfo)
    {
        TableVO tableVO = new TableVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),pageInfo);
            ResultObjectVO resultObjectVO = bannerService.queryListPage(requestJsonVO);
            if(resultObjectVO.getCode() == ResultObjectVO.SUCCESS)
            {
                if(resultObjectVO.getData()!=null)
                {
                    Map<String,Object> resultObjectDataMap = (Map<String,Object>)resultObjectVO.getData();
                    tableVO.setCount(Long.parseLong(String.valueOf(resultObjectDataMap.get("total")!=null?resultObjectDataMap.get("total"):"0")));
                    List<BannerVO> list = JSONArray.parseArray(JSONObject.toJSONString(resultObjectDataMap.get("list")),BannerVO.class);

                    // 查询创建人和修改人
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
                    resultObjectVO = adminServiceAPI.queryListByEntity(requestJsonVO);
                    if(resultObjectVO.isSuccess())
                    {
                        List<AdminVO> adminVOS = (List<AdminVO>)resultObjectVO.formatDataList(AdminVO.class);
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
            tableVO.setMsg("请重试");
            tableVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return tableVO;
    }


    /**
     * 保存
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:content:banner:save"})
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResultObjectVO save(HttpServletRequest request, @RequestBody BannerVO entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            // 如果有imgBase64，解码后上传到文件服务
            if(StringUtils.isNotEmpty(entity.getImgBase64())) {
                entity.setImgPath(imageUploadService.uploadBase64(entity.getImgBase64()));
            }
            entity.setAppCode(toucan.getShoppingPC().getAppCode());
            entity.setCreateAdminId(AdminLoginHolder.getCurrentAdminId());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = bannerService.save(requestJsonVO);
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
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:content:banner:update"})
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResultObjectVO update(HttpServletRequest request, @RequestBody BannerVO entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(StringUtils.isNotEmpty(entity.getImgBase64())) {
                // 查询旧图片并删除
                BannerVO queryVO = new BannerVO();
                queryVO.setId(entity.getId());
                RequestJsonVO queryRequest = RequestJsonVOGenerator.generator(appCode, queryVO);
                ResultObjectVO oldResult = bannerService.findById(queryRequest);
                if(oldResult.isSuccess()) {
                    List<BannerVO> oldList = oldResult.formatDataList(BannerVO.class);
                    if(!CollectionUtils.isEmpty(oldList) && StringUtils.isNotEmpty(oldList.get(0).getImgPath())) {
                        imageUploadService.deleteFile(oldList.get(0).getImgPath());
                    }
                }
                // 上传新图片
                entity.setImgPath(imageUploadService.uploadBase64(entity.getImgBase64()));
            }
            entity.setUpdateAdminId(AdminLoginHolder.getCurrentAdminId());
            entity.setUpdateDate(new Date());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = bannerService.update(requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    /**
     * 根据ID查询（回显用，含base64图片数据）
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:content:banner:list"})
    @RequestMapping(value = "/queryById", method = RequestMethod.POST)
    public ResultObjectVO queryById(HttpServletRequest request, @RequestBody BannerVO bannerVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(bannerVO.getId() == null)
            {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, bannerVO);
            ResultObjectVO detailResult = bannerService.findById(requestJsonVO);
            if(detailResult.isSuccess())
            {
                List<BannerVO> list = detailResult.formatDataList(BannerVO.class);
                if(CollectionUtils.isEmpty(list))
                {
                    resultObjectVO.setMsg("轮播图不存在");
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    return resultObjectVO;
                }
                BannerVO vo = list.get(0);
                if(StringUtils.isNotEmpty(vo.getImgPath()))
                {
                    vo.setHttpImgPath(imageUploadService.getImageHttpPrefix() + vo.getImgPath());
                    // 下载文件并转base64
                    byte[] fileBytes = imageUploadService.downloadFile(vo.getImgPath());
                    if(fileBytes != null && fileBytes.length > 0)
                    {
                        String path = vo.getImgPath();
                        String ext = "jpg";
                        if(path.contains("."))
                        {
                            ext = path.substring(path.lastIndexOf(".") + 1).toLowerCase();
                        }
                        String mime;
                        switch (ext) {
                            case "png": mime = "image/png"; break;
                            case "gif": mime = "image/gif"; break;
                            case "bmp": mime = "image/bmp"; break;
                            case "jpeg": mime = "image/jpeg"; break;
                            case "jpg": mime = "image/jpeg"; break;
                            default: mime = "image/jpeg"; break;
                        }
                        vo.setImgBase64("data:" + mime + ";base64," + Base64.getEncoder().encodeToString(fileBytes));
                    }
                }
                resultObjectVO.setData(vo);
            }else
            {
                resultObjectVO.setMsg(detailResult.getMsg());
                resultObjectVO.setCode(ResultObjectVO.FAILD);
            }
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    /**
     * 查看详情
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:content:banner:detail"})
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public ResultObjectVO detail(HttpServletRequest request, @RequestBody BannerVO bannerVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(bannerVO.getId() == null)
            {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, bannerVO);
            ResultObjectVO detailResult = bannerService.findById(requestJsonVO);
            if(detailResult.isSuccess())
            {
                List<BannerVO> list = detailResult.formatDataList(BannerVO.class);
                if(CollectionUtils.isEmpty(list))
                {
                    resultObjectVO.setMsg("轮播图不存在");
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    return resultObjectVO;
                }
                BannerVO vo = list.get(0);
                if(StringUtils.isNotEmpty(vo.getImgPath()))
                {
                    vo.setHttpImgPath(imageUploadService.getImageHttpPrefix() + vo.getImgPath());
                }
                // 填充创建人/修改人姓名
                fillAdminName(vo);
                // 查询关联地区名称
                if(!CollectionUtils.isEmpty(vo.getBannerAreas()))
                {
                    String[] areaCodeArray = new String[vo.getBannerAreas().size()];
                    for (int i = 0; i < vo.getBannerAreas().size(); i++) {
                        areaCodeArray[i] = vo.getBannerAreas().get(i).getAreaCode();
                    }
                    AreaVO queryArea = new AreaVO();
                    queryArea.setCodeArray(areaCodeArray);
                    requestJsonVO = RequestJsonVOGenerator.generator(appCode, queryArea);
                    ResultObjectVO areaResult = areaService.findByCodes(requestJsonVO);
                    if(areaResult.isSuccess())
                    {
                        List<AreaVO> areaVOS = areaResult.formatDataList(AreaVO.class);
                        if(!CollectionUtils.isEmpty(areaVOS))
                        {
                            StringBuilder areaNamesBuilder = new StringBuilder();
                            StringBuilder areaCodesBuilder = new StringBuilder();
                            for (int i = 0; i < areaVOS.size(); i++) {
                                AreaVO areaVO = areaVOS.get(i);
                                areaNamesBuilder.append(areaVO.getName());
                                areaCodesBuilder.append(areaVO.getCode());
                                if (i + 1 < areaVOS.size()) {
                                    areaNamesBuilder.append(",");
                                    areaCodesBuilder.append(",");
                                }
                            }
                            vo.setAreaCodes(areaCodesBuilder.toString());
                            vo.setAreaNames(areaNamesBuilder.toString());
                        }
                    }
                }
                BannerDetailVO detailVO = new BannerDetailVO();
                detailVO.setBasicInfo(vo);
                resultObjectVO.setData(detailVO);
            }else
            {
                resultObjectVO.setMsg(detailResult.getMsg());
                resultObjectVO.setCode(ResultObjectVO.FAILD);
            }
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    /**
     * 删除
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:content:banner:delete"})
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResultObjectVO deleteById(HttpServletRequest request, @RequestBody Banner banner)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(banner.getId() == null)
            {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            banner.setUpdateAdminId(AdminLoginHolder.getCurrentAdminId());

            String entityJson = JSONObject.toJSONString(banner);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(appCode);
            requestVo.setEntityJson(entityJson);
            // 先查询出实体对象,后面删除文件服务器的资源
            resultObjectVO = bannerService.findById(requestVo);
            List<BannerVO> bannerVOS = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()),BannerVO.class);
            if(resultObjectVO.isSuccess()) {
                resultObjectVO = bannerService.deleteById(requestVo);
                if(!CollectionUtils.isEmpty(bannerVOS))
                {
                    Banner b = bannerVOS.get(0);
                    int ret = imageUploadService.deleteFile(b.getImgPath());
                    if(ret!=0)
                    {
                        logger.warn("删除服务器中关联图片失败 {} ",b.getImgPath());
                        resultObjectVO.setMsg("删除关联图片资源失败");
                        resultObjectVO.setCode(TableVO.FAILD);
                    }
                }
            }
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
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:content:banner:deletes"})
    @RequestMapping(value = "/delete/ids", method = RequestMethod.POST)
    public ResultObjectVO deleteByIds(HttpServletRequest request, @RequestBody List<BannerVO> bannerVOS)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(CollectionUtils.isEmpty(bannerVOS))
            {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            String entityJson = JSONObject.toJSONString(bannerVOS);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(appCode);
            requestVo.setEntityJson(entityJson);
            resultObjectVO = bannerService.deleteByIds(requestVo);
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
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    /**
     * 刷新PC首页缓存
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:content:banner:flushCache"})
    @RequestMapping(value = "/flush/index/cache", method = RequestMethod.POST)
    public ResultObjectVO flushIndexCache(HttpServletRequest request, @RequestBody List<BannerVO> bannerVOS)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(CollectionUtils.isEmpty(bannerVOS))
            {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            String entityJson = JSONObject.toJSONString(bannerVOS);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(appCode);
            requestVo.setEntityJson(entityJson);
            resultObjectVO = bannerService.flushWebIndexCache(requestVo);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    /**
     * 清空PC首页缓存
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:content:banner:clear:index:cache"})
    @RequestMapping(value = "/clear/index/cache", method = RequestMethod.POST)
    public ResultObjectVO clearIndexCache(HttpServletRequest request)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            BannerVO bannerVO = new BannerVO();
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, bannerVO);
            resultObjectVO = bannerService.clearWebIndexCache(requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    /**
     * 上传图片
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:content:banner:upload:img"})
    @RequestMapping("/upload/img")
    public ResultObjectVO uploadImg(@RequestParam("file") MultipartFile file)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        resultObjectVO.setCode(0);
        try{
            String fileName = file.getOriginalFilename();
            if(!ImageUtils.isImage(fileName)){
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("上传图片只支持("+ImageUtils.imageExtScope.stream().collect(Collectors.joining("、"))+")");
                return resultObjectVO;
            }
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
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("上传失败");
            logger.warn(e.getMessage(),e);
        }

        return resultObjectVO;
    }


    /**
     * 查询地区树
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:content:banner:area:tree"})
    @RequestMapping(value = "/query/area/tree", method = RequestMethod.POST)
    public ResultObjectVO queryAreaTree(HttpServletRequest request, String bannerId)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            AreaVO query = new AreaVO();
            query.setAppCode(toucan.getShoppingPC().getAppCode());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),query);

            resultObjectVO = areaService.queryTree(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                List<AreaTreeVO> areaTreeVOList = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()), AreaTreeVO.class);

                // 重新设置ID,由于这个树是多个表合并而成,可能会存在ID重复
                AtomicLong id = new AtomicLong();
                BannerAreaVO queryBannerAreaVo = new BannerAreaVO();
                if(StringUtils.isNotEmpty(bannerId)) {
                    queryBannerAreaVo.setBannerId(Long.parseLong(bannerId));
                }
                requestJsonVO = RequestJsonVOGenerator.generator(appCode,queryBannerAreaVo);

                resultObjectVO = bannerAreaService.queryBannerAreaList(requestJsonVO);
                List<AreaTreeVO> releaseAreaTreeVOList = new ArrayList<AreaTreeVO>();
                if(resultObjectVO.isSuccess())
                {
                    // 只保留省市节点
                    AreaTreeVO rootTree = areaTreeVOList.get(0);
                    if(!CollectionUtils.isEmpty(rootTree.getChildren())) {
                        List<AreaTreeVO> rootChilren = JSONArray.parseArray(JSONObject.toJSONString(rootTree.getChildren()), AreaTreeVO.class);
                        for (AreaTreeVO areaTreeVO : rootChilren) {
                            // 直辖市
                            if (areaTreeVO.getIsMunicipality().shortValue() == 1) {
                                areaTreeVO.setChildren(null);
                            } else { // 省
                                // 遍历所有市节点,删除区县节点
                                if (!CollectionUtils.isEmpty(areaTreeVO.getChildren())) {
                                    List<AreaTreeVO> chilren = JSONArray.parseArray(JSONObject.toJSONString(areaTreeVO.getChildren()), AreaTreeVO.class);
                                    for (AreaVO cityTreeVO : chilren) {
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


    /**
     * 填充创建人/修改人姓名
     */
    private void fillAdminName(BannerVO bannerVO) throws NoSuchAlgorithmException {
        List<String> adminIdList = new ArrayList<String>();
        if(bannerVO.getCreateAdminId() != null)
        {
            adminIdList.add(bannerVO.getCreateAdminId());
        }
        if(bannerVO.getUpdateAdminId() != null)
        {
            adminIdList.add(bannerVO.getUpdateAdminId());
        }
        if(adminIdList.isEmpty())
        {
            return;
        }
        String[] adminIds = adminIdList.toArray(new String[0]);
        AdminVO queryAdminVO = new AdminVO();
        queryAdminVO.setAdminIds(adminIds);
        RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryAdminVO);
        ResultObjectVO resultObjectVO = adminServiceAPI.queryListByEntity(requestJsonVO);
        if(resultObjectVO.isSuccess())
        {
            List<AdminVO> adminVOS = resultObjectVO.formatDataList(AdminVO.class);
            if(!CollectionUtils.isEmpty(adminVOS))
            {
                for(AdminVO adminVO : adminVOS)
                {
                    if(bannerVO.getCreateAdminId() != null && bannerVO.getCreateAdminId().equals(adminVO.getAdminId()))
                    {
                        bannerVO.setCreateAdminName(adminVO.getUsername());
                    }
                    if(bannerVO.getUpdateAdminId() != null && bannerVO.getUpdateAdminId().equals(adminVO.getAdminId()))
                    {
                        bannerVO.setUpdateAdminName(adminVO.getUsername());
                    }
                }
            }
        }
    }

    /**
     * 递归设置树节点选中状态
     */
    private void setTreeNodeSelect(AtomicLong id, AreaTreeVO parentTreeVO, List<AreaTreeVO> areaTreeVOList, List<BannerArea> bannerAreas)
    {
        for(AreaTreeVO areaTreeVO:areaTreeVOList)
        {
            areaTreeVO.setId(id.incrementAndGet());
            areaTreeVO.setNodeId(areaTreeVO.getId());
            areaTreeVO.setPid(parentTreeVO.getId());
            areaTreeVO.setParentId(areaTreeVO.getPid());
            for(BannerArea bannerArea:bannerAreas) {
                if(areaTreeVO.getCode().equals(bannerArea.getAreaCode())) {
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

}
