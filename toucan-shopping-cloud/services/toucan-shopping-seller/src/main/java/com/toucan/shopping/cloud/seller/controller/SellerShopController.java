package com.toucan.shopping.cloud.seller.controller;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.redis.service.ToucanStringRedisService;
import com.toucan.shopping.modules.seller.constant.ShopConstant;
import com.toucan.shopping.modules.seller.entity.SellerShop;
import com.toucan.shopping.modules.seller.page.SellerShopPageInfo;
import com.toucan.shopping.modules.seller.redis.SellerShopKey;
import com.toucan.shopping.modules.seller.service.SellerLoginHistoryService;
import com.toucan.shopping.modules.seller.service.SellerShopService;
import com.toucan.shopping.modules.seller.vo.SellerShopVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 店铺管理 增删改查
 */
@RestController
@RequestMapping("/sellerShop")
public class SellerShopController {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private SkylarkLock skylarkLock;

    @Autowired
    private SellerShopService sellerShopService;

    @Autowired
    private SellerLoginHistoryService sellerLoginHistoryService;

    @Autowired
    private Toucan toucan;

    @Autowired
    private ToucanStringRedisService toucanStringRedisService;



    @RequestMapping(value="/save",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO save(@RequestBody RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("没有找到请求对象");
            return resultObjectVO;
        }
        if (StringUtils.isEmpty(requestJsonVO.getAppCode())) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("没有找到应用编码");
            return resultObjectVO;
        }
        SellerShopVO sellerShopVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), SellerShopVO.class);
        if(StringUtils.isEmpty(sellerShopVO.getName()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("店铺名称不能为空");
            return resultObjectVO;
        }
        //去空格
        sellerShopVO.setName(sellerShopVO.getName().replace(" ",""));
        if(sellerShopVO.getType()==null)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("类型不能为空");
            return resultObjectVO;
        }

        String userMainId = String.valueOf(sellerShopVO.getUserMainId());
        try {

            boolean lockStatus = skylarkLock.lock(SellerShopKey.getSaveLockKey(userMainId), userMainId);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
                return resultObjectVO;
            }
            //查询关联店铺
            SellerShop querySellerShop = new SellerShop();
            querySellerShop.setUserMainId(sellerShopVO.getUserMainId());
            querySellerShop.setDeleteStatus((short)0);
            //个人申请
            if(sellerShopVO.getType().intValue()==1)
            {
                //查询该用户是否已存在店铺
                List<SellerShop> sellerShops = sellerShopService.findListByEntity(querySellerShop);
                if(!CollectionUtils.isEmpty(sellerShops))
                {
                    //释放锁
                    skylarkLock.unLock(SellerShopKey.getSaveLockKey(userMainId), userMainId);
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg("该用户已有店铺");
                    return resultObjectVO;
                }

                //查询该店铺是否已被注册
                querySellerShop = new SellerShop();
                querySellerShop.setName(sellerShopVO.getName());
                querySellerShop.setDeleteStatus((short)0);
                sellerShops = sellerShopService.findListByEntity(querySellerShop);
                if(!CollectionUtils.isEmpty(sellerShops))
                {
                    //释放锁
                    skylarkLock.unLock(SellerShopKey.getSaveLockKey(userMainId), userMainId);
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg("该店铺已注册");
                    return resultObjectVO;
                }

                sellerShopVO.setApproveStatus(2);  //个人店铺直接审核通过

            }else if(sellerShopVO.getType().intValue()==2) //企业申请
            {
                //查询企业相关信息,判断是否已注册

                sellerShopVO.setApproveStatus(1); //企业店铺需要审核资质等相关
            }

            sellerShopVO.setId(idGenerator.id());
            sellerShopVO.setPublicShopId(String.valueOf(idGenerator.id()));
            sellerShopVO.setLogo(toucan.getSeller().getDefaultShopLogo()); //默认店铺图标
            sellerShopVO.setChangeNameCount(0); //已改店名次数
            sellerShopVO.setCreateDate(new Date());
            sellerShopVO.setEnableStatus((short)1); //启用
            sellerShopVO.setDeleteStatus((short)0);
            sellerShopVO.setShopRank(0L); //排序
            sellerShopVO.setCategoryMaxCount(toucan.getSeller().getShopCategoryMaxCount());
            int ret = sellerShopService.save(sellerShopVO);
            if(ret<=0)
            {
                logger.warn("保存商户店铺失败 requestJson{} id{}",requestJsonVO.getEntityJson(),sellerShopVO.getId());
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
            }
            resultObjectVO.setData(sellerShopVO);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }finally{
            skylarkLock.unLock(SellerShopKey.getSaveLockKey(userMainId), userMainId);
        }
        return resultObjectVO;
    }


    /**
     * 查询指定用户的店铺
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/by/user",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO findByUser(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            SellerShop querySellerShop = JSONObject.parseObject(requestVo.getEntityJson(), SellerShop.class);

            if(StringUtils.isEmpty(requestVo.getAppCode()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到应用编码");
                return resultObjectVO;
            }
            if(querySellerShop.getUserMainId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到用户ID");
                return resultObjectVO;
            }


            Object shopJsonObject = toucanStringRedisService.get(SellerShopKey.getShopCacheKey(String.valueOf(querySellerShop.getUserMainId())));
            if(shopJsonObject!=null)
            {
                SellerShopVO sellerShopVO = JSONObject.parseObject(String.valueOf(shopJsonObject),SellerShopVO.class);
                if(sellerShopVO!=null&&sellerShopVO.getEnableStatus()!=null&&sellerShopVO.getEnableStatus().intValue()==1)
                {
                    resultObjectVO.setData(sellerShopVO);
                    return resultObjectVO;
                }

            }
            querySellerShop.setEnableStatus((short)1);
            List<SellerShop> sellerShops = sellerShopService.findListByEntity(querySellerShop);
            if(!CollectionUtils.isEmpty(sellerShops)) {
                SellerShop sellerShop = sellerShops.get(0);
                SellerShopVO sellerShopVO = new SellerShopVO();
                BeanUtils.copyProperties(sellerShopVO,sellerShop);

                flushRedisCache(sellerShopVO);

                resultObjectVO.setData(sellerShopVO);
            }


        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
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
            SellerShopVO entity = JSONObject.parseObject(requestVo.getEntityJson(),SellerShopVO.class);
            if(entity.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到ID");
                return resultObjectVO;
            }

            //查询是否存在
            SellerShop query=new SellerShop();
            query.setId(entity.getId());
            List<SellerShop> sellerShops = sellerShopService.findListByEntity(query);
            if(CollectionUtils.isEmpty(sellerShops))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("对象不存在!");
                return resultObjectVO;
            }
            resultObjectVO.setData(sellerShops);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }



    /**
     * 根据ID集合查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/idList",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO findByIdList(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            SellerShopVO query = JSONObject.parseObject(requestVo.getEntityJson(),SellerShopVO.class);
            if(query.getIdList()==null||query.getIdList().size()<=0)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到ID集合");
                return resultObjectVO;
            }

            List<SellerShop> entitys = sellerShopService.queryList(query);
            if(CollectionUtils.isEmpty(entitys))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("店铺列表为空");
                return resultObjectVO;
            }

            resultObjectVO.setData(entitys);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 刷新到缓存
     * @param sellerShop
     */
    private void flushRedisCache(SellerShop sellerShop)
    {
        try{
            if(toucanStringRedisService.get(SellerShopKey.getShopCacheKey(String.valueOf(sellerShop.getUserMainId())))!=null) {
                SellerShopVO sellerShopVO = new SellerShopVO();
                BeanUtils.copyProperties(sellerShopVO, sellerShop);
                toucanStringRedisService.set(SellerShopKey.getShopCacheKey(String.valueOf(sellerShopVO.getUserMainId())), JSONObject.toJSONString(sellerShopVO));
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
    }


    /**
     * 刷新缓存
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/flushCache",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO flushCache(@RequestBody RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try{
            SellerShop querySellerShop = JSONObject.parseObject(requestJsonVO.getEntityJson(), SellerShop.class);

            if(StringUtils.isEmpty(requestJsonVO.getAppCode()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到应用编码");
                return resultObjectVO;
            }
            if(querySellerShop.getUserMainId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到用户ID");
                return resultObjectVO;
            }

            List<SellerShop> sellerShops = sellerShopService.findListByEntity(querySellerShop);
            if(!CollectionUtils.isEmpty(sellerShops)) {
                SellerShop sellerShop = sellerShops.get(0);
                SellerShopVO sellerShopVO = new SellerShopVO();
                BeanUtils.copyProperties(sellerShopVO,sellerShop);
                resultObjectVO.setData(sellerShopVO);

                flushRedisCache(sellerShopVO);
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    /**
     * 更新
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
            logger.info("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }

        try {
            SellerShop sellerShop = JSONObject.parseObject(requestJsonVO.getEntityJson(), SellerShop.class);


            if(StringUtils.isEmpty(sellerShop.getName()))
            {
                logger.info("名称为空 param:"+ JSONObject.toJSONString(sellerShop));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("名称不能为空!");
                return resultObjectVO;
            }


            if(sellerShop.getId()==null)
            {
                logger.info("ID为空 param:"+ JSONObject.toJSONString(sellerShop));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("ID不能为空!");
                return resultObjectVO;
            }

            SellerShop querySellerShop = new SellerShop();
            querySellerShop.setName(sellerShop.getName());
            querySellerShop.setDeleteStatus((short)0);

            List<SellerShop> sellerShops = sellerShopService.findListByEntity(querySellerShop);
            if(!CollectionUtils.isEmpty(sellerShops))
            {
                if(sellerShop.getId().longValue() != sellerShops.get(0).getId().longValue())
                {
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg("该店铺名称已被注册!");
                    return resultObjectVO;
                }
            }

            sellerShop.setUpdateDate(new Date());


            int row = sellerShopService.update(sellerShop);
            if (row != 1) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请重试!");
                return resultObjectVO;
            }

            flushRedisCache(sellerShop);

        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }




    /**
     * 更新图标
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/update/logo",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO updateLogo(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            logger.info("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }

        try {
            SellerShop sellerShop = JSONObject.parseObject(requestJsonVO.getEntityJson(), SellerShop.class);


            if(StringUtils.isEmpty(sellerShop.getLogo()))
            {
                logger.info("图标为空 param:"+ JSONObject.toJSONString(sellerShop));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("图标不能为空!");
                return resultObjectVO;
            }


            if(sellerShop.getUserMainId()==null)
            {
                logger.info("用户ID为空 param:"+ JSONObject.toJSONString(sellerShop));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("用户ID不能为空!");
                return resultObjectVO;
            }

            SellerShop updateSellerShop = new SellerShop();
            updateSellerShop.setLogo(sellerShop.getLogo());
            updateSellerShop.setUserMainId(sellerShop.getUserMainId());

            int row = sellerShopService.updateLogo(updateSellerShop);
            if (row <=0) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请重试!");
                return resultObjectVO;
            }

            flushRedisCache(sellerShop);
        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }




    /**
     * 更新介绍
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/update/info",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO updateInfo(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            logger.info("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }

        try {
            SellerShop sellerShop = JSONObject.parseObject(requestJsonVO.getEntityJson(), SellerShop.class);


            if(StringUtils.isEmpty(sellerShop.getName()))
            {
                logger.info("店铺名称为空 param:"+ JSONObject.toJSONString(sellerShop));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("店铺名称不能为空!");
                return resultObjectVO;
            }


            if(sellerShop.getUserMainId()==null)
            {
                logger.info("用户ID为空 param:"+ JSONObject.toJSONString(sellerShop));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("用户ID不能为空!");
                return resultObjectVO;
            }

            SellerShop querySellerShop = new SellerShop();
            querySellerShop.setName(sellerShop.getName());
            querySellerShop.setEnableStatus((short)1);
            List<SellerShop> sellerShops = sellerShopService.findListByEntity(querySellerShop);
            if(!CollectionUtils.isEmpty(sellerShops))
            {
                for (SellerShop sellerShopEntity:sellerShops)
                {
                    if(sellerShopEntity.getUserMainId().longValue()!=sellerShop.getUserMainId().longValue())
                    {
                        resultObjectVO.setCode(ResultVO.FAILD);
                        resultObjectVO.setMsg("该店铺名称已被注册!");
                        return resultObjectVO;
                    }
                }
            }
            sellerShops = sellerShopService.findEnabledByUserMainId(sellerShop.getUserMainId());
            SellerShop updateSellerShop = new SellerShop();

            if(CollectionUtils.isEmpty(sellerShops))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("店铺不存在或已被禁用!");
                return resultObjectVO;
            }else{
                SellerShop sellerShopRet= sellerShops.get(0);
                if(!sellerShopRet.getName().equals(sellerShop.getName()))
                {
                    if(sellerShopRet.getChangeNameCount()!=null) {
                        updateSellerShop.setChangeNameCount(sellerShopRet.getChangeNameCount().intValue()+1);
                    }else{
                        updateSellerShop.setChangeNameCount(1);
                    }
                }else{
                    updateSellerShop.setChangeNameCount(sellerShopRet.getChangeNameCount());
                }
            }


            if(updateSellerShop.getChangeNameCount()> ShopConstant.CHANGE_NAME_COUNT) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("修改名称次数已达到限制!");
                return resultObjectVO;
            }

            updateSellerShop.setName(sellerShop.getName());
            updateSellerShop.setIntroduce(sellerShop.getIntroduce());
            updateSellerShop.setUserMainId(sellerShop.getUserMainId());
            updateSellerShop.setProvince(sellerShop.getProvince());
            updateSellerShop.setCity(sellerShop.getCity());
            updateSellerShop.setArea(sellerShop.getArea());
            updateSellerShop.setProvinceCode(sellerShop.getProvinceCode());
            updateSellerShop.setCityCode(sellerShop.getCityCode());
            updateSellerShop.setAreaCode(sellerShop.getAreaCode());
            updateSellerShop.setDetailAddress(sellerShop.getDetailAddress());
            updateSellerShop.setLogo(sellerShop.getLogo());


            int row = sellerShopService.updateInfo(updateSellerShop);
            if (row <=0) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请重试!");
                return resultObjectVO;
            }
            sellerShops = sellerShopService.findEnabledByUserMainId(sellerShop.getUserMainId());
            if(!CollectionUtils.isEmpty(sellerShops))
            {
                flushRedisCache(sellerShops.get(0));
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

        try {
            SellerShop sellerShop = JSONObject.parseObject(requestJsonVO.getEntityJson(), SellerShop.class);



            if(sellerShop.getId()==null)
            {
                logger.info("ID为空 param:"+ JSONObject.toJSONString(sellerShop));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("ID不能为空!");
                return resultObjectVO;
            }


            SellerShop query = new SellerShop();
            query.setId(sellerShop.getId());

            List<SellerShop> sellerShops = sellerShopService.findListByEntity(query);
            if(CollectionUtils.isEmpty(sellerShops))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("不存在该店铺!");
                return resultObjectVO;
            }

            sellerShop = sellerShops.get(0);
            int row = sellerShopService.deleteById(sellerShop.getId());
            if (row <=0) {
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
     * 批量删除功能项
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/ids",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteByIds(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            List<SellerShop> sellerShops = JSONObject.parseArray(requestVo.getEntityJson(),SellerShop.class);
            if(CollectionUtils.isEmpty(sellerShops))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找ID");
                return resultObjectVO;
            }
            List<ResultObjectVO> resultObjectVOList = new ArrayList<ResultObjectVO>();
            for(SellerShop sellerShop:sellerShops) {
                if(sellerShop.getId()!=null) {
                    //删除当前功能项
                    int row = sellerShopService.deleteById(sellerShop.getId());
                    if (row < 1) {
                        logger.warn("删除店铺失败 {} ",JSONObject.toJSONString(sellerShop));
                        resultObjectVO.setCode(ResultVO.FAILD);
                        resultObjectVO.setMsg("请重试!");
                        continue;
                    }
                }
            }
            resultObjectVO.setData(resultObjectVOList);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }



    /**
     * 查询列表页
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/list/page",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            SellerShopPageInfo queryPageInfo = JSONObject.parseObject(requestVo.getEntityJson(), SellerShopPageInfo.class);

            if(StringUtils.isEmpty(requestVo.getAppCode()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到应用编码");
                return resultObjectVO;
            }

            //查询列表页
            resultObjectVO.setData(sellerShopService.queryListPage(queryPageInfo));

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }




    /**
     * 禁用启用店铺
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/disabled/enabled",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO disabledEnabled(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            SellerShopVO entity = JSONObject.parseObject(requestVo.getEntityJson(),SellerShopVO.class);
            if(entity.getPublicShopId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到ID");
                return resultObjectVO;
            }

            SellerShopVO querySellerShopVO = new SellerShopVO();
            querySellerShopVO.setPublicShopId(entity.getPublicShopId());

            List<SellerShop> sellerShopList = sellerShopService.findListByEntity(querySellerShopVO);
            if(!CollectionUtils.isEmpty(sellerShopList)) {
                SellerShop sellerShop = sellerShopList.get(0);
                if(sellerShop.getEnableStatus().shortValue()==0)
                {

                    //查询该用户下所有店铺
                    SellerShopVO queryUserSellerShopVO = new SellerShopVO();
                    queryUserSellerShopVO.setUserMainId(sellerShop.getUserMainId());
                    queryUserSellerShopVO.setEnableStatus((short)1);
                    sellerShopList = sellerShopService.findListByEntity(queryUserSellerShopVO);
                    if(!CollectionUtils.isEmpty(sellerShopList)&&sellerShopList.size()>1)
                    {
                        resultObjectVO.setCode(ResultVO.FAILD);
                        resultObjectVO.setMsg("启用失败,该用户下已经存在其他店铺");
                        return resultObjectVO;
                    }


                    //启用
                    sellerShop.setEnableStatus((short)1);

                }else{
                    //禁用
                    sellerShop.setEnableStatus((short)0);
                }
                int ret = sellerShopService.update(sellerShop);
                if(ret<=0)
                {
                    logger.warn("启用/禁用店铺失败 requestJson{} sellerShop {} ",requestVo.getEntityJson(),JSONObject.toJSONString(sellerShop));
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg("请稍后重试");
                }

                SellerShopVO sellerShopVO = new SellerShopVO();
                BeanUtils.copyProperties(sellerShopVO,sellerShop);

                flushRedisCache(sellerShopVO);
            }

            resultObjectVO.setData(entity);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


}
