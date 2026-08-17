package com.toucan.shopping.cloud.apps.admin.controller.user;


import com.toucan.shopping.cloud.apps.admin.helper.PageHelper;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.*;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.cloud.user.api.UserServiceAPI;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.layui.vo.TableVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import com.toucan.shopping.modules.user.constant.UserRegistConstant;
import com.toucan.shopping.modules.user.entity.User;
import com.toucan.shopping.modules.user.page.UserPageInfo;
import com.toucan.shopping.modules.user.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private Toucan toucan;

    @Autowired
    private UserServiceAPI userService;

    @Autowired
    private SkylarkLock skylarkLock;


    /**
     * 查询列表
     * @param userPageInfo
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:user:list"})
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public TableVO list(@RequestBody UserPageInfo userPageInfo)
    {
        TableVO tableVO = new TableVO();
        try {
            RequestJsonVO requestVo = RequestJsonVOGenerator.generator(toucan.getAppCode(),userPageInfo);
            ResultObjectVO resultObjectVO = userService.list(requestVo);
            if(resultObjectVO.getCode() == ResultObjectVO.SUCCESS)
            {
                if(resultObjectVO.getData()!=null)
                {
                    Map<String,Object> resultObjectDataMap = PageHelper.extractPageData(resultObjectVO.getData());
                    tableVO.setCount(Long.parseLong(String.valueOf(resultObjectDataMap.get("total"))));
                    List<UserVO> list = JSONArray.parseArray(JSONObject.toJSONString(resultObjectDataMap.get("list")),UserVO.class);
                    if(list!=null) {
                        for (UserVO userVO : list) {
                            if (userVO.getHeadSculpture() != null) {
                                userVO.setHttpHeadSculpture(imageUploadService.getImageHttpPrefix() + userVO.getHeadSculpture());
                            }
                            if (userVO.getIdcardImg1() != null) {
                                userVO.setHttpIdcardImg1(imageUploadService.getImageHttpPrefix() + userVO.getIdcardImg1());
                            }
                            if (userVO.getIdcardImg2() != null) {
                                userVO.setHttpIdcardImg2(imageUploadService.getImageHttpPrefix() + userVO.getIdcardImg2());
                            }
                        }
                        if (tableVO.getCount() > 0) {
                            tableVO.setData((List) list);
                        }
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
     * 查看用户详情(含http地址与base64头像/证件照,用于前端查看与编辑回显)
     * @param userVO
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:user:list"})
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public ResultObjectVO detail(@RequestBody UserVO userVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(userVO == null || userVO.getUserMainId() == null)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("没有找到用户ID");
                return resultObjectVO;
            }
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getShoppingPC().getAppCode(), userVO);
            ResultObjectVO detailResult = userService.findByUserMainId(requestJsonVO);
            if(detailResult.isSuccess())
            {
                UserVO vo = detailResult.formatData(UserVO.class);
                if(vo == null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("用户不存在");
                    return resultObjectVO;
                }
                fillUserImage(vo);
                resultObjectVO.setData(vo);
            }else
            {
                resultObjectVO.setMsg(detailResult.getMsg());
                resultObjectVO.setCode(ResultObjectVO.FAILD);
            }
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    /**
     * 填充用户图片的http地址与base64数据
     * @param vo
     */
    private void fillUserImage(UserVO vo)
    {
        if(StringUtils.isNotEmpty(vo.getHeadSculpture()))
        {
            vo.setHttpHeadSculpture(imageUploadService.getImageHttpPrefix() + vo.getHeadSculpture());
            vo.setHeadSculptureBase64(toBase64(vo.getHeadSculpture()));
        }
        if(StringUtils.isNotEmpty(vo.getIdcardImg1()))
        {
            vo.setHttpIdcardImg1(imageUploadService.getImageHttpPrefix() + vo.getIdcardImg1());
            vo.setIdcardImg1Base64(toBase64(vo.getIdcardImg1()));
        }
        if(StringUtils.isNotEmpty(vo.getIdcardImg2()))
        {
            vo.setHttpIdcardImg2(imageUploadService.getImageHttpPrefix() + vo.getIdcardImg2());
            vo.setIdcardImg2Base64(toBase64(vo.getIdcardImg2()));
        }
    }


    /**
     * 读取图片文件并转成base64 data url
     * @param storagePath
     * @return
     */
    private String toBase64(String storagePath)
    {
        try {
            byte[] fileBytes = imageUploadService.downloadFile(storagePath);
            if(fileBytes != null && fileBytes.length > 0)
            {
                String ext = "jpg";
                if(storagePath.contains("."))
                {
                    ext = storagePath.substring(storagePath.lastIndexOf(".") + 1).toLowerCase();
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
                return "data:" + mime + ";base64," + Base64.getEncoder().encodeToString(fileBytes);
            }
        }catch(Exception e)
        {
            logger.warn("读取图片失败 {} {}", storagePath, e.getMessage());
        }
        return null;
    }


    /**
     * 手机号列表
     * @param userPageInfo
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:user:mobile:phone:list"})
    @RequestMapping(value = "/mobile/phone/list",method = RequestMethod.POST)
    public TableVO mobilePhoneList(@RequestBody UserPageInfo userPageInfo)
    {
        TableVO tableVO = new TableVO();
        try {
            RequestJsonVO requestVo = RequestJsonVOGenerator.generator(toucan.getAppCode(),userPageInfo);
            ResultObjectVO resultObjectVO = userService.mobilePhoneList(requestVo);
            if(resultObjectVO.getCode() == ResultObjectVO.SUCCESS)
            {
                if(resultObjectVO.getData()!=null)
                {
                    Map<String,Object> resultObjectDataMap = PageHelper.extractPageData(resultObjectVO.getData());
                    tableVO.setCount(Long.parseLong(String.valueOf(resultObjectDataMap.get("total"))));
                    if(tableVO.getCount()>0) {
                        tableVO.setData((List<Object>) resultObjectDataMap.get("list"));
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
     * 邮箱列表
     * @param userPageInfo
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:user:email:list"})
    @RequestMapping(value = "/email/list",method = RequestMethod.POST)
    public TableVO emailList(@RequestBody UserPageInfo userPageInfo)
    {
        TableVO tableVO = new TableVO();
        try {
            RequestJsonVO requestVo = RequestJsonVOGenerator.generator(toucan.getAppCode(),userPageInfo);
            ResultObjectVO resultObjectVO = userService.emailList(requestVo);
            if(resultObjectVO.getCode() == ResultObjectVO.SUCCESS)
            {
                if(resultObjectVO.getData()!=null)
                {
                    Map<String,Object> resultObjectDataMap = PageHelper.extractPageData(resultObjectVO.getData());
                    tableVO.setCount(Long.parseLong(String.valueOf(resultObjectDataMap.get("total"))));
                    if(tableVO.getCount()>0) {
                        tableVO.setData((List<Object>) resultObjectDataMap.get("list"));
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
     * 用户名列表
     * @param userPageInfo
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:user:username:list"})
    @RequestMapping(value = "/username/list",method = RequestMethod.POST)
    public TableVO usernameList(@RequestBody UserPageInfo userPageInfo)
    {
        TableVO tableVO = new TableVO();
        try {
            RequestJsonVO requestVo = RequestJsonVOGenerator.generator(toucan.getAppCode(),userPageInfo);
            ResultObjectVO resultObjectVO = userService.usernameList(requestVo);
            if(resultObjectVO.getCode() == ResultObjectVO.SUCCESS)
            {
                if(resultObjectVO.getData()!=null)
                {
                    Map<String,Object> resultObjectDataMap = PageHelper.extractPageData(resultObjectVO.getData());
                    tableVO.setCount(Long.parseLong(String.valueOf(resultObjectDataMap.get("total"))));
                    if(tableVO.getCount()>0) {
                        tableVO.setData((List<Object>) resultObjectDataMap.get("list"));
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
     * 添加用户
     * @param user
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:user:regist"})
    @RequestMapping(value="/regist", method = RequestMethod.POST)
    public ResultObjectVO regist(@RequestBody UserRegistVO user){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(user==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("没有找到要注册的用户");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(user.getMobilePhone()))
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_MOBILE);
            resultObjectVO.setMsg("请输入注册手机号");
            return resultObjectVO;
        }

        if(!PhoneUtils.isChinaPhoneLegal(user.getMobilePhone()))
        {
            resultObjectVO.setCode(UserRegistConstant.MOBILE_ERROR);
            resultObjectVO.setMsg("手机号错误");
            return resultObjectVO;
        }

        if(StringUtils.isEmpty(user.getPassword()))
        {
            resultObjectVO.setCode(UserRegistConstant.PASSWORD_NOT_FOUND);
            resultObjectVO.setMsg("请输入密码");
            return resultObjectVO;
        }
        if(!StringUtils.equals(user.getPassword(),user.getConfirmPassword()))
        {
            resultObjectVO.setCode(UserRegistConstant.PASSWORD_NOT_FOUND);
            resultObjectVO.setMsg("密码与确认密码不一致");
            return resultObjectVO;
        }

        if(!UserRegistUtil.checkPwd(user.getPassword()))
        {
            resultObjectVO.setCode(UserRegistConstant.PASSWORD_ERROR);
            resultObjectVO.setMsg(UserRegistUtil.checkPwdFailText());
            return resultObjectVO;
        }


        //商城应用编码
        String shoppingAppCode = toucan.getShoppingPC().getAppCode();
        String mobilePhone = user.getMobilePhone();
        String lockKey = toucan.getAppCode()+"_user_regist_mobile_"+mobilePhone;
        try {

            boolean lockStatus = skylarkLock.lock(lockKey, user.getMobilePhone());
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求超时,请稍后重试");
                return resultObjectVO;
            }


            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(shoppingAppCode,user);
            //查询是否已注册用户名
            if(StringUtils.isNotEmpty(user.getUsername()))
            {
                resultObjectVO = userService.findUsernameListByUsername(requestJsonVO);
                if(!resultObjectVO.isSuccess())
                {
                    return resultObjectVO;
                }
                List userNames = resultObjectVO.formatData(ArrayList.class);
                if(!CollectionUtils.isEmpty(userNames))
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("用户名已注册");
                    return resultObjectVO;
                }
            }

            //查询是否已注册邮箱
            if(StringUtils.isNotEmpty(user.getEmail()))
            {
                resultObjectVO = userService.findEmailListByEmail(requestJsonVO);
                if(!resultObjectVO.isSuccess())
                {
                    return resultObjectVO;
                }
                List emails = (List)resultObjectVO.formatData(ArrayList.class);
                if(!CollectionUtils.isEmpty(emails))
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("邮箱已注册");
                    return resultObjectVO;
                }
            }


            logger.info(" 用户注册 {} ", user.getMobilePhone());

            resultObjectVO = userService.registByMobilePhone(requestJsonVO);
            if(resultObjectVO.isSuccess()) {
                //拿到用户主ID
                UserRegistVO userRegistResult = (UserRegistVO) resultObjectVO.formatData(UserRegistVO.class);
                user.setUserMainId(userRegistResult.getUserMainId());

                //如果输入了用户名,进行用户名的关联
                if(StringUtils.isNotEmpty(user.getUsername())) {
                    requestJsonVO = RequestJsonVOGenerator.generator(shoppingAppCode, user);
                    resultObjectVO = userService.connectUsername( requestJsonVO);
                }

                //如果输入了邮箱,进行邮箱关联
                if (StringUtils.isNotEmpty(user.getEmail())) {
                    requestJsonVO = RequestJsonVOGenerator.generator(shoppingAppCode, user);
                    resultObjectVO = userService.connectEmail( requestJsonVO);
                }

                //修改用户详情
                requestJsonVO = RequestJsonVOGenerator.generator(shoppingAppCode, user);
                resultObjectVO = userService.updateDetail( requestJsonVO);
            }

            resultObjectVO.setData(null);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }finally{
            skylarkLock.unLock(lockKey, mobilePhone);
        }
        return resultObjectVO;
    }





    /**
     * 修改详情
     * @param user
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:user:update:detail"})
    @RequestMapping(value="/update/detail", method = RequestMethod.POST)
    public ResultObjectVO updateDetail(@RequestBody UserRegistVO user){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(user==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("没有找到要修改的用户");
            return resultObjectVO;
        }

        if(user.getUserMainId()==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("没有找到要用户ID");
            return resultObjectVO;
        }

        String userMainId = String.valueOf(user.getUserMainId());
        String lockKey = toucan.getAppCode()+"_user_update_detail_"+userMainId;
        try {

            boolean lockStatus = skylarkLock.lock(lockKey, userMainId);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求超时,请稍后重试");
                return resultObjectVO;
            }

            //商城应用编码
            user.setAppCode(toucan.getShoppingPC().getAppCode());

            //如果有base64图片，上传到文件服务
            if (StringUtils.isNotEmpty(user.getHeadSculptureBase64())) {
                user.setHeadSculpture(imageUploadService.uploadBase64(user.getHeadSculptureBase64()));
            }
            if (StringUtils.isNotEmpty(user.getIdcardImg1Base64())) {
                user.setIdcardImg1(imageUploadService.uploadBase64(user.getIdcardImg1Base64()));
            }
            if (StringUtils.isNotEmpty(user.getIdcardImg2Base64())) {
                user.setIdcardImg2(imageUploadService.uploadBase64(user.getIdcardImg2Base64()));
            }

            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),user);
            logger.info(" 修改详情 {} ", user.getUserMainId());
            UserVO userVO=null;
            //保存旧的详情数据,用于删除旧的图片资源
            resultObjectVO = userService.findByUserMainId(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                userVO = resultObjectVO.formatData(UserVO.class);
            }
            //修改详情
            resultObjectVO = userService.updateDetail(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                if(userVO!=null)
                {
                    //删除头像
                    if(StringUtils.isNotEmpty(userVO.getHeadSculpture()))
                    {
                        //如果不是默认头像,就删除旧的头像
                        if(!toucan.getUser().getDefaultHeadSculpture().equals(userVO.getHeadSculpture())) {
                            //本次修改了头像,删除了旧头像
                            if(!userVO.getHeadSculpture().equals(user.getHeadSculpture())) {
                                int ret = imageUploadService.deleteFile(userVO.getHeadSculpture());
                                if (ret != 0) {
                                    logger.warn("删除旧头像失败 {} userVO {} ", userVO.getHeadSculpture(), JSONObject.toJSONString(userVO));
                                }
                            }
                        }
                    }
                    //删除证件照片
                    if(StringUtils.isNotEmpty(userVO.getIdcardImg1()))
                    {
                        if(!user.getIdcardImg1().equals(userVO.getIdcardImg1())) {
                            int ret = imageUploadService.deleteFile(userVO.getIdcardImg1());
                            if (ret != 0) {
                                logger.warn("删除证件照片失败 {} userVO {} ", userVO.getIdcardImg1(), JSONObject.toJSONString(userVO));
                            }
                        }
                    }
                    if(StringUtils.isNotEmpty(userVO.getIdcardImg2()))
                    {
                        if(!user.getIdcardImg2().equals(userVO.getIdcardImg2())) {
                            int ret = imageUploadService.deleteFile(userVO.getIdcardImg2());
                            if (ret != 0) {
                                logger.warn("删除证件照片失败 {} userVO {} ", userVO.getIdcardImg2(), JSONObject.toJSONString(userVO));
                            }
                        }
                    }
                }
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }finally{
            skylarkLock.unLock(lockKey, userMainId);
        }
        return resultObjectVO;
    }




    /**
     * 刷新缓存
     * @param userMainId
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:user:flushCache"})
    @RequestMapping(value="/flush/cache/{userMainId}",method = RequestMethod.POST)
    public ResultObjectVO flushCache(@PathVariable String userMainId){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(StringUtils.isEmpty(userMainId))
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_MOBILE);
            resultObjectVO.setMsg("刷新失败,没有找到用户ID");
            return resultObjectVO;
        }


        //商城应用编码
        String shoppingAppCode = toucan.getShoppingPC().getAppCode();
        try {
            UserRegistVO user = new UserRegistVO();
            user.setUserMainId(Long.parseLong(userMainId));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(shoppingAppCode,user);
            resultObjectVO = userService.flushCache(requestJsonVO);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("刷新失败,请稍后重试");
        }
        return resultObjectVO;
    }




    /**
     * 修改密码
     * @param user
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:user:reset:password"})
    @RequestMapping(value="/reset/password", method = RequestMethod.POST)
    public ResultObjectVO resetPassword(@RequestBody UserRegistVO user){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(user==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("重置失败,没有找到要注册的用户");
            return resultObjectVO;
        }

        if(user.getUserMainId()==null)
        {
            resultObjectVO.setCode(UserRegistConstant.PASSWORD_NOT_FOUND);
            resultObjectVO.setMsg("重置失败,用户ID为空");
            return resultObjectVO;
        }

        if(StringUtils.isEmpty(user.getPassword()))
        {
            resultObjectVO.setCode(UserRegistConstant.PASSWORD_NOT_FOUND);
            resultObjectVO.setMsg("重置失败,请输入密码");
            return resultObjectVO;
        }
        if(!StringUtils.equals(user.getPassword(),user.getConfirmPassword()))
        {
            resultObjectVO.setCode(UserRegistConstant.PASSWORD_NOT_FOUND);
            resultObjectVO.setMsg("重置失败,密码与确认密码不一致");
            return resultObjectVO;
        }

        if(!UserRegistUtil.checkPwd(user.getPassword()))
        {
            resultObjectVO.setCode(UserRegistConstant.PASSWORD_ERROR);
            resultObjectVO.setMsg("重置失败,"+UserRegistUtil.checkPwdFailText());
            return resultObjectVO;
        }


        //商城应用编码
        String shoppingAppCode = toucan.getShoppingPC().getAppCode();
        String userMainId = String.valueOf(user.getUserMainId());
        String lockKey = toucan.getAppCode()+"_user_reset_password_"+user.getUserMainId();
        try {

            boolean lockStatus = skylarkLock.lock(lockKey, userMainId);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求超时,请稍后重试");
                return resultObjectVO;
            }


            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(shoppingAppCode,user);

            logger.info(" 重置密码 {} ", user.getUserMainId());

            resultObjectVO = userService.resetPassword(requestJsonVO);
            resultObjectVO.setData(null);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }finally{
            skylarkLock.unLock(lockKey, userMainId);
        }
        return resultObjectVO;
    }




    /**
     * 关联手机号
     * @param user
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:user:mobile:phone:connectMobilePhone"})
    @RequestMapping(value="/connect/mobile/phone", method = RequestMethod.POST)
    public ResultObjectVO connectMobilePhone(@RequestBody UserRegistVO user){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(user==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("没有找到要注册的用户");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(user.getMobilePhone()))
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_MOBILE);
            resultObjectVO.setMsg("请输入关联手机号");
            return resultObjectVO;
        }

        if(!PhoneUtils.isChinaPhoneLegal(user.getMobilePhone()))
        {
            resultObjectVO.setCode(UserRegistConstant.MOBILE_ERROR);
            resultObjectVO.setMsg("手机号错误");
            return resultObjectVO;
        }


        //商城应用编码
        String shoppingAppCode = toucan.getShoppingPC().getAppCode();
        String mobilePhone = user.getMobilePhone();
        String lockKey = toucan.getAppCode()+"_user_regist_mobile_"+mobilePhone;
        try {

            boolean lockStatus = skylarkLock.lock(lockKey, user.getMobilePhone());
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求超时,请稍后重试");
                return resultObjectVO;
            }

            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(shoppingAppCode,user);
            logger.info(" 关联手机号 {} ", user.getMobilePhone());

            resultObjectVO = userService.connectMobilePhone(requestJsonVO);

            resultObjectVO.setData(null);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }finally{
            skylarkLock.unLock(lockKey, mobilePhone);
        }
        return resultObjectVO;
    }


    /**
     * 关联邮箱
     * @param user
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:user:email:email"})
    @RequestMapping(value="/connect/email", method = RequestMethod.POST)
    public ResultObjectVO connectEmail(@RequestBody UserRegistVO user){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(user==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("关联失败,没有找到要注册的用户");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(user.getEmail()))
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_MOBILE);
            resultObjectVO.setMsg("关联失败,请输入关联邮箱");
            return resultObjectVO;
        }

        if(!EmailUtils.isEmail(user.getEmail()))
        {
            resultObjectVO.setCode(UserRegistConstant.MOBILE_ERROR);
            resultObjectVO.setMsg("关联失败,邮箱错误");
            return resultObjectVO;
        }


        //商城应用编码
        String shoppingAppCode = toucan.getShoppingPC().getAppCode();
        String email = user.getEmail();
        String lockKey = toucan.getAppCode()+"_user_regist_email_"+email;
        try {

            boolean lockStatus = skylarkLock.lock(lockKey, user.getEmail());
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求超时,请稍后重试");
                return resultObjectVO;
            }

            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(shoppingAppCode,user);
            logger.info(" 关联邮箱 {} ", user.getEmail());

            resultObjectVO = userService.connectEmail(requestJsonVO);

            resultObjectVO.setData(null);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }finally{
            skylarkLock.unLock(lockKey, email);
        }
        return resultObjectVO;
    }


    /**
     * 关联用户名
     * @param user
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:user:username:username"})
    @RequestMapping(value="/connect/username", method = RequestMethod.POST)
    public ResultObjectVO connectUsername(@RequestBody UserRegistVO user){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(user==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("关联失败,没有找到要注册的用户");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(user.getUsername()))
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_MOBILE);
            resultObjectVO.setMsg("关联失败,请输入关联用户名");
            return resultObjectVO;
        }

        if(!UsernameUtils.isUsername(user.getUsername()))
        {
            resultObjectVO.setCode(UserRegistConstant.MOBILE_ERROR);
            resultObjectVO.setMsg("关联失败,用户名错误");
            return resultObjectVO;
        }


        //商城应用编码
        String shoppingAppCode = toucan.getShoppingPC().getAppCode();
        String username = user.getUsername();
        String lockKey = toucan.getAppCode()+"_user_regist_username_"+username;
        try {

            boolean lockStatus = skylarkLock.lock(lockKey, user.getUsername());
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求超时,请稍后重试");
                return resultObjectVO;
            }

            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(shoppingAppCode,user);
            logger.info(" 关联用户名 {} ", user.getUsername());

            resultObjectVO = userService.connectUsername(requestJsonVO);

            resultObjectVO.setData(null);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }finally{
            skylarkLock.unLock(lockKey, username);
        }
        return resultObjectVO;
    }




    /**
     * 禁用/启用
     * @param request
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:user:disabled:enabled"})
    @RequestMapping(value = "/disabled/enabled/{id}",method = RequestMethod.DELETE)
    public ResultObjectVO disabledById(@PathVariable String id)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(StringUtils.isEmpty(id))
            {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            User user =new User();
            user.setUserMainId(Long.parseLong(id));

            String shoppingAppCode = toucan.getShoppingPC().getAppCode();
            String entityJson = JSONObject.toJSONString(user);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(shoppingAppCode);
            requestVo.setEntityJson(entityJson);
            resultObjectVO = userService.disabledEnabledById(requestVo);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }

    /**
     * 手机号 禁用/启用
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:user:mobile:phone:list:disabled:enabled"})
    @RequestMapping(value = "/mobile/phone/disabled/enabled",method = RequestMethod.POST)
    public ResultObjectVO disabledEnabledMobilePhone(@RequestBody UserMobilePhoneVO userMobilePhoneVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(userMobilePhoneVO.getId()==null)
            {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            userMobilePhoneVO.setAppCode(toucan.getShoppingPC().getAppCode());

            RequestJsonVO requestVo = RequestJsonVOGenerator.generator(toucan.getAppCode(),userMobilePhoneVO);
            resultObjectVO = userService.disabledEnabledMobilePhone(requestVo);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



    /**
     * 邮箱 禁用/启用
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:user:email:list:disabled:enabled"})
    @RequestMapping(value = "/email/disabled/enabled",method = RequestMethod.POST)
    public ResultObjectVO disabledEnabledEmail(@RequestBody UserEmailVO userEmailVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(userEmailVO.getId()==null)
            {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            userEmailVO.setAppCode(toucan.getShoppingPC().getAppCode());
            RequestJsonVO requestVo = RequestJsonVOGenerator.generator(toucan.getAppCode(),userEmailVO);
            resultObjectVO = userService.disabledEnabledEmail(requestVo);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }




    /**
     * 用户名 禁用/启用
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:user:username:list:disabled:enabled"})
    @RequestMapping(value = "/username/disabled/enabled",method = RequestMethod.POST)
    public ResultObjectVO disabledEnabledUsernameByUserMainIdAndUsername(@RequestBody UserUserNameVO userUserNameVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(userUserNameVO.getId()==null)
            {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            userUserNameVO.setAppCode(toucan.getShoppingPC().getAppCode());

            RequestJsonVO requestVo = RequestJsonVOGenerator.generator(toucan.getAppCode(),userUserNameVO);
            resultObjectVO = userService.disabledEnabledUsernameByUserMainIdAndUsername(requestVo);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }

    /**
     * 批量禁用
     * @param request
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:user:disabled:ids"})
    @RequestMapping(value = "/disabled/ids",method = RequestMethod.DELETE)
    public ResultObjectVO disabledByIds(@RequestBody List<UserVO> userVOS)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(CollectionUtils.isEmpty(userVOS))
            {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            if(!CollectionUtils.isEmpty(userVOS))
            {
                for(UserVO userVO:userVOS)
                {
                    userVO.setAppCode(toucan.getShoppingPC().getAppCode());
                }
            }
            String entityJson = JSONObject.toJSONString(userVOS);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(toucan.getAppCode());
            requestVo.setEntityJson(entityJson);
            resultObjectVO = userService.disabledByIds( requestVo);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:user:list:upload:head:sculpture"})
    @RequestMapping(value = "/upload/head/sculpture", method = RequestMethod.POST)
    public ResultObjectVO  uploadHeadSculpture(@RequestParam("file") MultipartFile file, @RequestParam("userMainId")Long userMainId)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        resultObjectVO.setCode(0);
        try{
            String fileName = file.getOriginalFilename();
            if(!ImageUtils.isStaticImage(fileName))
            {
                throw new RuntimeException("请上传图片格式(.jpg|.jpeg|.png)");
            }
            String fileExt = ".jpg";
            if(StringUtils.isNotEmpty(fileName)&&fileName.indexOf(".")!=-1)
            {
                fileExt = fileName.substring(fileName.lastIndexOf(".")+1);

            }
            String groupPath = imageUploadService.uploadFile(file.getBytes(),fileExt);

            if(StringUtils.isEmpty(groupPath))
            {
                throw new RuntimeException("头像上传失败");
            }
            UserVO userVO = new UserVO();
            if(userMainId!=null&&userMainId.longValue()!=-1) { //给修改功能和注册功能使用,注册功能没有用户ID
                userVO.setUserMainId(userMainId);
                RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), userVO);
                ResultObjectVO userResultObjectVO = userService.findByUserMainId( requestJsonVO);
                if (userResultObjectVO.isSuccess()) {
                    userVO = userResultObjectVO.formatData(UserVO.class);
                    userVO.setHeadSculpture(groupPath);

                    //设置预览头像
                    if (userVO.getHeadSculpture() != null) {
                        userVO.setHttpHeadSculpture(imageUploadService.getImageHttpPrefix() + userVO.getHeadSculpture());
                    }
                    resultObjectVO.setData(userVO);
                }
            }else{
                userVO.setHeadSculpture(groupPath);

                //设置预览头像
                if (userVO.getHeadSculpture() != null) {
                    userVO.setHttpHeadSculpture(imageUploadService.getImageHttpPrefix() + userVO.getHeadSculpture());
                }
                resultObjectVO.setData(userVO);
            }
        }catch (Exception e)
        {
            resultObjectVO.setCode(1);
            resultObjectVO.setMsg("头像上传失败");
            logger.warn(e.getMessage(),e);
        }

        return resultObjectVO;
    }



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:user:list:upload:idcardimg1"})
    @RequestMapping(value = "/upload/idcardImg1", method = RequestMethod.POST)
    public ResultObjectVO  uploadIdcardImg1(@RequestParam("file") MultipartFile file, @RequestParam("userMainId")Long userMainId)
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
                throw new RuntimeException("证件照正面上传失败");
            }
            UserVO userVO = new UserVO();
            if(userMainId!=null&&userMainId.longValue()!=-1) {
                userVO.setUserMainId(userMainId);
                RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), userVO);
                ResultObjectVO userResultObjectVO = userService.findByUserMainId( requestJsonVO);
                if (userResultObjectVO.isSuccess()) {
                    userVO = userResultObjectVO.formatData(UserVO.class);
                    userVO.setIdcardImg1(groupPath);

                    //设置预览头像
                    if (userVO.getIdcardImg1() != null) {
                        userVO.setHttpIdcardImg1(imageUploadService.getImageHttpPrefix() + userVO.getIdcardImg1());
                    }
                    resultObjectVO.setData(userVO);
                }
            }else{
                userVO.setIdcardImg1(groupPath);

                //设置预览头像
                if (userVO.getIdcardImg1() != null) {
                    userVO.setHttpIdcardImg1(imageUploadService.getImageHttpPrefix() + userVO.getIdcardImg1());
                }
                resultObjectVO.setData(userVO);
            }
        }catch (Exception e)
        {
            resultObjectVO.setCode(1);
            resultObjectVO.setMsg("证件照正面上传失败");
            logger.warn(e.getMessage(),e);
        }

        return resultObjectVO;
    }




    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:user:list:upload:idcardimg2"})
    @RequestMapping(value = "/upload/idcardImg2", method = RequestMethod.POST)
    public ResultObjectVO  uploadIdcardImg2(@RequestParam("file") MultipartFile file, @RequestParam("userMainId")Long userMainId)
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
                throw new RuntimeException("证件照背面上传失败");
            }
            UserVO userVO = new UserVO();
            if(userMainId!=null&&userMainId.longValue()!=-1) {
                userVO.setUserMainId(userMainId);
                RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), userVO);
                ResultObjectVO userResultObjectVO = userService.findByUserMainId( requestJsonVO);
                if (userResultObjectVO.isSuccess()) {
                    userVO = userResultObjectVO.formatData(UserVO.class);
                    userVO.setIdcardImg2(groupPath);

                    //设置预览头像
                    if (userVO.getIdcardImg2() != null) {
                        userVO.setHttpIdcardImg2(imageUploadService.getImageHttpPrefix() + userVO.getIdcardImg2());
                    }
                    resultObjectVO.setData(userVO);
                }
            }else{

                userVO.setIdcardImg2(groupPath);

                //设置预览头像
                if (userVO.getIdcardImg2() != null) {
                    userVO.setHttpIdcardImg2(imageUploadService.getImageHttpPrefix() + userVO.getIdcardImg2());
                }
                resultObjectVO.setData(userVO);
            }
        }catch (Exception e)
        {
            resultObjectVO.setCode(1);
            resultObjectVO.setMsg("证件照背面上传失败");
            logger.warn(e.getMessage(),e);
        }

        return resultObjectVO;
    }

}

