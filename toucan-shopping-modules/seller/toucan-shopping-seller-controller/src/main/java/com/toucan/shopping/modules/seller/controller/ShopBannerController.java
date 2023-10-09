package com.toucan.shopping.modules.seller.controller;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.seller.entity.ShopBanner;
import com.toucan.shopping.modules.seller.page.ShopBannerPageInfo;
import com.toucan.shopping.modules.seller.redis.ShopBannerKey;
import com.toucan.shopping.modules.seller.service.ShopBannerService;
import com.toucan.shopping.modules.seller.vo.ShopBannerVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


/**
 * 轮播图操作
 */
@RestController
@RequestMapping("/shop/banner")
public class ShopBannerController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ShopBannerService shopBannerService;


    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private SkylarkLock skylarkLock;

    /**
     * 保存轮播图
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/save",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO save(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            logger.warn("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }
        if(requestJsonVO.getAppCode()==null)
        {
            logger.warn("没有找到对象编码: param:"+ JSONObject.toJSONString(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到对象编码!");
            return resultObjectVO;
        }

        Long bannerId = -1L;
        try {
            bannerId = idGenerator.id();

            ShopBannerVO bannerVO = requestJsonVO.formatEntity(ShopBannerVO.class);
            ShopBanner shopBanner = new ShopBanner();
            BeanUtils.copyProperties(shopBanner,bannerVO);
            shopBanner.setId(bannerId);
            shopBanner.setCreateDate(new Date());
            shopBanner.setDeleteStatus((short)0);
            int row = shopBannerService.save(shopBanner);
            if (row <= 0) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请重试!");
                return resultObjectVO;
            }

        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }




    /**
     * 根据ID删除
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/delete/id",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteById(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            logger.info("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }
        if(requestJsonVO.getAppCode()==null)
        {
            logger.info("没有找到应用编码: param:"+ JSONObject.toJSONString(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到应用编码!");
            return resultObjectVO;
        }

        ShopBanner shopBanner = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopBanner.class);

        if(shopBanner.getId()==null)
        {
            logger.warn("ID为空 param:"+ requestJsonVO.getEntityJson());
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("ID不能为空!");
            return resultObjectVO;
        }

        String shopId = String.valueOf(shopBanner.getShopId());
        try {

            boolean lockStatus = skylarkLock.lock(ShopBannerKey.getDeleteLockKey(shopId), shopId);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
                return resultObjectVO;
            }



            if(shopBanner.getShopId()==null)
            {
                //释放锁
                skylarkLock.unLock(ShopBannerKey.getDeleteLockKey(shopId), shopId);

                logger.warn("店铺ID为空 param:"+ JSONObject.toJSONString(shopBanner));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有查询到关联店铺!");
                return resultObjectVO;
            }



            int row = shopBannerService.deleteById(shopBanner.getId());
            if (row <=0) {
                //释放锁
                skylarkLock.unLock(ShopBannerKey.getDeleteLockKey(shopId), shopId);

                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请重试!");
                return resultObjectVO;
            }

        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            logger.warn(e.getMessage(),e);
        }finally{
            //释放锁
            skylarkLock.unLock(ShopBannerKey.getDeleteLockKey(shopId), shopId);
        }
        return resultObjectVO;
    }



    /**
     * 根据ID删除
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/admin/delete/id",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteByIdForAdmin(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            logger.info("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }
        if(requestJsonVO.getAppCode()==null)
        {
            logger.info("没有找到应用编码: param:"+ JSONObject.toJSONString(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到应用编码!");
            return resultObjectVO;
        }

        ShopBanner shopBanner = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopBanner.class);

        if(shopBanner.getId()==null)
        {
            logger.warn("ID为空 param:"+ requestJsonVO.getEntityJson());
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("ID不能为空!");
            return resultObjectVO;
        }

        String shopId = String.valueOf(shopBanner.getShopId());
        try {

            boolean lockStatus = skylarkLock.lock(ShopBannerKey.getDeleteLockKey(shopId), shopId);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
                return resultObjectVO;
            }



            if(shopBanner.getShopId()==null)
            {
                //释放锁
                skylarkLock.unLock(ShopBannerKey.getDeleteLockKey(shopId), shopId);

                logger.warn("店铺ID为空 param:"+ JSONObject.toJSONString(shopBanner));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有查询到关联店铺!");
                return resultObjectVO;
            }



            int row = shopBannerService.deleteById(shopBanner.getId());
            if (row <=0) {
                //释放锁
                skylarkLock.unLock(ShopBannerKey.getDeleteLockKey(shopId), shopId);

                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("删除失败，请重试!");
                return resultObjectVO;
            }

        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            logger.warn(e.getMessage(),e);
        }finally{
            //释放锁
            skylarkLock.unLock(ShopBannerKey.getDeleteLockKey(shopId), shopId);
        }
        return resultObjectVO;
    }


    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/list/page",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            logger.warn("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }
        if(requestJsonVO.getAppCode()==null)
        {
            logger.warn("没有找到应用编码: param:"+ JSONObject.toJSONString(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到应用编码!");
            return resultObjectVO;
        }
        try {
            ShopBannerPageInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopBannerPageInfo.class);
            PageInfo<ShopBannerVO> pageInfo =  shopBannerService.queryListPage(queryPageInfo);
            resultObjectVO.setData(pageInfo);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }




    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/id",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO findById(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            ShopBannerVO entity = JSONObject.parseObject(requestVo.getEntityJson(),ShopBannerVO.class);
            if(entity.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到ID");
                return resultObjectVO;
            }

            resultObjectVO.setData(shopBannerService.findById(entity.getId()));

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }



    /**
     * 修改轮播图
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/update",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO update(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            logger.warn("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }
        if(requestJsonVO.getAppCode()==null)
        {
            logger.warn("没有找到对象编码: param:"+ JSONObject.toJSONString(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到对象编码!");
            return resultObjectVO;
        }

        try {
            ShopBannerVO bannerVO = requestJsonVO.formatEntity(ShopBannerVO.class);
            if(bannerVO.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("ID不能为空");
                return resultObjectVO;
            }
            ShopBanner shopBanner = new ShopBanner();
            BeanUtils.copyProperties(shopBanner,bannerVO);
            int row = shopBannerService.update(shopBanner);
            if (row <= 0) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("修改失败,请稍后请重试!");
                return resultObjectVO;
            }

        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("修改失败,请稍后请重试!");
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


}
