package com.toucan.shopping.cloud.apps.admin.controller.seller;


import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.seller.api.ShopCategoryServiceAPI;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.layui.vo.TableVO;
import com.toucan.shopping.modules.seller.page.ShopCategoryTreeInfo;
import com.toucan.shopping.modules.seller.vo.ShopCategoryVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 *店铺分类控制器
 */
@RestController
@RequestMapping("/seller/shop/category")
public class ShopCategoryController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private ShopCategoryServiceAPI shopCategoryService;




    /**
     * 保存
     * @param entity
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"shopping:seller:shop:category:save:api"})
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public ResultObjectVO save(HttpServletRequest request, @RequestBody ShopCategoryVO entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = shopCategoryService.saveForAdmin(requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



    /**
     * 向上
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"shopping:seller:shop:category:move:up:api"})
    @RequestMapping(value="/move/up",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    public ResultObjectVO moveUp(HttpServletRequest request, @RequestBody ShopCategoryVO shopCategoryVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),shopCategoryVO);
            resultObjectVO = shopCategoryService.moveUpForAdmin(requestJsonVO);

        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("移动失败,请稍后重试");
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    /**
     * 向下
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"shopping:seller:shop:category:move:down:api"})
    @RequestMapping(value="/move/down",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    public ResultObjectVO moveDown(HttpServletRequest request, @RequestBody ShopCategoryVO shopCategoryVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),shopCategoryVO);
            resultObjectVO = shopCategoryService.moveDownForAdmin(requestJsonVO);

        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("移动失败,请稍后重试");
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    /**
     * 置底
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"shopping:seller:shop:category:move:bottom:api"})
    @RequestMapping(value="/move/bottom",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    public ResultObjectVO moveBottom(HttpServletRequest request, @RequestBody ShopCategoryVO shopCategoryVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),shopCategoryVO);
            resultObjectVO = shopCategoryService.moveBottomForAdmin(requestJsonVO);

        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("移动失败,请稍后重试");
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }

    /**
     * 置顶
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"shopping:seller:shop:category:move:top:api"})
    @RequestMapping(value="/move/top",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    public ResultObjectVO moveTop(HttpServletRequest request, @RequestBody ShopCategoryVO shopCategoryVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),shopCategoryVO);
            resultObjectVO = shopCategoryService.moveTopForAdmin(requestJsonVO);

        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("移动失败,请稍后重试");
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



    /**
     * 修改
     * @param entity
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"shopping:seller:shop:category:update:api"})
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public ResultObjectVO update(HttpServletRequest request, @RequestBody ShopCategoryVO entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            entity.setUpdateDate(new Date());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = shopCategoryService.updateForAdmin(requestJsonVO);
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
     * @param queryPageInfo
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"shopping:seller:shop:category:treee:table:api"})
    @RequestMapping(value = "/tree/table",method = RequestMethod.POST)
    public ResultObjectVO treeTable(HttpServletRequest request, @RequestBody ShopCategoryTreeInfo queryPageInfo)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryPageInfo);
            resultObjectVO = shopCategoryService.queryTreeTable(requestJsonVO);
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
     * 查询列表
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"shopping:seller:shop:category:tree:table:by:pid:api"})
    @RequestMapping(value = "/tree/table/by/pid",method = RequestMethod.POST)
    public ResultObjectVO queryListByPid(HttpServletRequest request, @RequestBody ShopCategoryTreeInfo ShopCategoryTreeInfo)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),ShopCategoryTreeInfo);
            resultObjectVO = shopCategoryService.queryTreeTableByPid(requestJsonVO);
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
     * 删除
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"shopping:seller:shop:category:delete:api"})
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public ResultObjectVO deleteById(@RequestBody ShopCategoryVO entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(entity.getId() == null)
            {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }

            String entityJson = JSONObject.toJSONString(entity);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(toucan.getAppCode());
            requestVo.setEntityJson(entityJson);
            resultObjectVO = shopCategoryService.deleteByIdForAdmin(requestVo);
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
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"shopping:seller:shop:category:deletes:api"})
    @RequestMapping(value = "/delete/ids",method = RequestMethod.POST)
    public ResultObjectVO deleteByIds(@RequestBody List<CategoryVO> categoryVOS)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(CollectionUtils.isEmpty(categoryVOS))
            {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            String entityJson = JSONObject.toJSONString(categoryVOS);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(toucan.getAppCode());
            requestVo.setEntityJson(entityJson);
            resultObjectVO = shopCategoryService.deleteByIds(requestVo);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



}
