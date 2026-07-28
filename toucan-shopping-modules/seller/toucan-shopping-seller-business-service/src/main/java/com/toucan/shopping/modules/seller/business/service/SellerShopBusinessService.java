package com.toucan.shopping.modules.seller.business.service;


import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.toucan.shopping.modules.common.annotation.RequestCheck;
import com.toucan.shopping.modules.common.exception.BusinessValidationException;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.Check;
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
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 店铺管理 增删改查 业务服务
 */
@Service
public class SellerShopBusinessService {


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


    @RequestCheck(requireEntity = true)
    public ResultObjectVO save(RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        SellerShopVO sellerShopVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), SellerShopVO.class);
        Check.notEmpty(sellerShopVO.getName(), ResultObjectVO.FAILD, "店铺名称不能为空");
        //去空格
        sellerShopVO.setName(sellerShopVO.getName().replace(" ",""));
        Check.notNull(sellerShopVO.getType(), ResultObjectVO.FAILD, "类型不能为空");

        String userMainId = String.valueOf(sellerShopVO.getUserMainId());
        try {

            boolean lockStatus = skylarkLock.lock(SellerShopKey.getSaveLockKey(userMainId), userMainId);
            if (!lockStatus) {
                return ResultObjectVO.fail(ResultObjectVO.FAILD, "请稍后重试");
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
                    return ResultObjectVO.fail(ResultVO.FAILD, "该用户已有店铺");
                }

                //查询该店铺是否已被注册
                querySellerShop = new SellerShop();
                querySellerShop.setName(sellerShopVO.getName());
                querySellerShop.setDeleteStatus((short)0);
                sellerShops = sellerShopService.findListByEntity(querySellerShop);
                if(!CollectionUtils.isEmpty(sellerShops))
                {
                    return ResultObjectVO.fail(ResultVO.FAILD, "该店铺已注册");
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
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            return ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO findByUser(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            SellerShop querySellerShop = JSONObject.parseObject(requestVo.getEntityJson(), SellerShop.class);

            Check.notNull(querySellerShop.getUserMainId(), ResultVO.FAILD, "没有找到用户ID");


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
            List<SellerShop> sellerShops = sellerShopService.findListByEntity(querySellerShop);
            if(!CollectionUtils.isEmpty(sellerShops)) {
                SellerShop sellerShop = sellerShops.get(0);
                SellerShopVO sellerShopVO = new SellerShopVO();
                BeanUtils.copyProperties(sellerShopVO,sellerShop);

                refershRedisCache(sellerShopVO.getId());

                resultObjectVO.setData(sellerShopVO);
            }


        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            return ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        }
        return resultObjectVO;
    }





    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO findById(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            SellerShopVO entity = JSONObject.parseObject(requestVo.getEntityJson(),SellerShopVO.class);
            Check.notNull(entity.getId(), ResultVO.FAILD, "没有找到ID");

            //查询是否存在
            SellerShop query=new SellerShop();
            query.setId(entity.getId());
            List<SellerShop> sellerShops = sellerShopService.findListByEntity(query);
            if(CollectionUtils.isEmpty(sellerShops))
            {
                return ResultObjectVO.fail(ResultVO.FAILD, "对象不存在!");
            }
            resultObjectVO.setData(sellerShops);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            return ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        }
        return resultObjectVO;
    }



    /**
     * 根据ID集合查询
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO findByIdList(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            SellerShopVO query = JSONObject.parseObject(requestVo.getEntityJson(),SellerShopVO.class);
            if(query.getIdList()==null||query.getIdList().size()<=0)
            {
                return ResultObjectVO.fail(ResultVO.FAILD, "没有找到ID集合");
            }

            List<SellerShop> entitys = sellerShopService.queryList(query);
            if(CollectionUtils.isEmpty(entitys))
            {
                return ResultObjectVO.fail(ResultVO.FAILD, "店铺列表为空");
            }

            resultObjectVO.setData(entitys);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            return ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 刷新到缓存
     * @param id
     */
    private void refershRedisCache(Long id)
    {
        try{
            SellerShop sellerShop = sellerShopService.findById(id);
            if(sellerShop!=null) {
                SellerShopVO sellerShopVO = new SellerShopVO();
                BeanUtils.copyProperties(sellerShopVO, sellerShop);
                toucanStringRedisService.set(SellerShopKey.getShopCacheKey(String.valueOf(sellerShopVO.getUserMainId())), JSONObject.toJSONString(sellerShopVO));
                toucanStringRedisService.expire(SellerShopKey.getShopCacheKey(String.valueOf(sellerShopVO.getUserMainId())), ShopConstant.CACHE_TIMEOUT_SECOND, TimeUnit.SECONDS);
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
    }

    /**
     *  删除缓存
     * @param userMainId
     */
    private void removeRedisCache(Long userMainId)
    {

        try{
            toucanStringRedisService.delete(SellerShopKey.getShopCacheKey(String.valueOf(userMainId)));
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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO flushCache(RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try{
            SellerShop querySellerShop = JSONObject.parseObject(requestJsonVO.getEntityJson(), SellerShop.class);

            Check.notNull(querySellerShop.getUserMainId(), ResultVO.FAILD, "没有找到用户ID");

            List<SellerShop> sellerShops = sellerShopService.findListByEntity(querySellerShop);
            if(!CollectionUtils.isEmpty(sellerShops)) {
                SellerShop sellerShop = sellerShops.get(0);
                SellerShopVO sellerShopVO = new SellerShopVO();
                BeanUtils.copyProperties(sellerShopVO,sellerShop);
                resultObjectVO.setData(sellerShopVO);

                refershRedisCache(sellerShopVO.getId());
            }
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO update(RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            SellerShop sellerShop = JSONObject.parseObject(requestJsonVO.getEntityJson(), SellerShop.class);


            Check.notEmpty(sellerShop.getName(), ResultVO.FAILD, "名称不能为空!");
            Check.notNull(sellerShop.getId(), ResultVO.FAILD, "ID不能为空!");

            SellerShop querySellerShop = new SellerShop();
            querySellerShop.setName(sellerShop.getName());
            querySellerShop.setDeleteStatus((short)0);

            List<SellerShop> sellerShops = sellerShopService.findListByEntity(querySellerShop);
            if(!CollectionUtils.isEmpty(sellerShops))
            {
                if(sellerShop.getId().longValue() != sellerShops.get(0).getId().longValue())
                {
                    return ResultObjectVO.fail(ResultVO.FAILD, "该店铺名称已被注册!");
                }
            }

            sellerShop.setUpdateDate(new Date());


            int row = sellerShopService.update(sellerShop);
            if (row != 1) {
                return ResultObjectVO.fail(ResultVO.FAILD, "请重试!");
            }

            refershRedisCache(sellerShop.getId());

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO updateLogo(RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            SellerShop sellerShop = JSONObject.parseObject(requestJsonVO.getEntityJson(), SellerShop.class);


            Check.notEmpty(sellerShop.getLogo(), ResultVO.FAILD, "图标不能为空!");
            Check.notNull(sellerShop.getUserMainId(), ResultVO.FAILD, "用户ID不能为空!");

            SellerShop updateSellerShop = new SellerShop();
            updateSellerShop.setLogo(sellerShop.getLogo());
            updateSellerShop.setUserMainId(sellerShop.getUserMainId());
            SellerShop sellerShopEntity = sellerShopService.findOneEnabledByUserMainId(sellerShop.getUserMainId());
            if(sellerShopEntity!=null) {
                updateSellerShop.setId(sellerShopEntity.getId());

                int row = sellerShopService.updateLogo(updateSellerShop);
                if (row <= 0) {
                    return ResultObjectVO.fail(ResultVO.FAILD, "请重试!");
                }

                refershRedisCache(sellerShopEntity.getId());
            }
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO updateInfo(RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            SellerShop sellerShop = JSONObject.parseObject(requestJsonVO.getEntityJson(), SellerShop.class);



            Check.notEmpty(sellerShop.getName(), ResultVO.FAILD, "店铺名称不能为空!");
            Check.notNull(sellerShop.getUserMainId(), ResultVO.FAILD, "用户ID不能为空!");

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
                        return ResultObjectVO.fail(ResultVO.FAILD, "该店铺名称已被注册!");
                    }
                }
            }
            sellerShops = sellerShopService.findEnabledByUserMainId(sellerShop.getUserMainId());
            SellerShop updateSellerShop = new SellerShop();

            if(CollectionUtils.isEmpty(sellerShops))
            {
                return ResultObjectVO.fail(ResultVO.FAILD, "店铺不存在或已被禁用!");
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
                updateSellerShop.setId(sellerShopRet.getId());
            }


            if(updateSellerShop.getChangeNameCount()> ShopConstant.CHANGE_NAME_COUNT) {
                return ResultObjectVO.fail(ResultVO.FAILD, "修改名称次数已达到限制!");
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
                return ResultObjectVO.fail(ResultVO.FAILD, "修改失败,请稍后重试!");
            }
            refershRedisCache(updateSellerShop.getId());
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("修改失败,请稍后重试!");
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }

    /**
     * 根据ID删除
     * @param requestJsonVO
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO deleteById(RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            SellerShop sellerShop = JSONObject.parseObject(requestJsonVO.getEntityJson(), SellerShop.class);



            Check.notNull(sellerShop.getId(), ResultVO.FAILD, "ID不能为空!");


            SellerShop query = new SellerShop();
            query.setId(sellerShop.getId());

            List<SellerShop> sellerShops = sellerShopService.findListByEntity(query);
            if(CollectionUtils.isEmpty(sellerShops))
            {
                return ResultObjectVO.fail(ResultVO.FAILD, "不存在该店铺!");
            }

            sellerShop = sellerShops.get(0);
            int row = sellerShopService.deleteById(sellerShop.getId());
            if (row <=0) {
                return ResultObjectVO.fail(ResultVO.FAILD, "请重试!");
            }

            removeRedisCache(sellerShop.getUserMainId());

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO deleteByIds(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            List<SellerShop> sellerShops = JSON.parseArray(requestVo.getEntityJson(),SellerShop.class);
            if(CollectionUtils.isEmpty(sellerShops))
            {
                return ResultObjectVO.fail(ResultVO.FAILD, "没有找ID");
            }
            List<ResultObjectVO> resultObjectVOList = new ArrayList<ResultObjectVO>();
            for(SellerShop sellerShop:sellerShops) {
                if(sellerShop.getId()!=null) {
                    //删除当前
                    SellerShop sellerShopEntity = sellerShopService.findById(sellerShop.getId());
                    int row = sellerShopService.deleteById(sellerShop.getId());
                    if (row < 1) {
                        logger.warn("删除店铺失败 {} ",JSONObject.toJSONString(sellerShop));
                        resultObjectVO.setCode(ResultVO.FAILD);
                        resultObjectVO.setMsg("请重试!");
                        continue;
                    }
                    if(sellerShopEntity!=null) {
                        this.removeRedisCache(sellerShopEntity.getUserMainId());
                    }
                }
            }
            resultObjectVO.setData(resultObjectVOList);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            return ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        }
        return resultObjectVO;
    }



    /**
     * 查询列表页
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryListPage(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            SellerShopPageInfo queryPageInfo = JSONObject.parseObject(requestVo.getEntityJson(), SellerShopPageInfo.class);

            //查询列表页
            resultObjectVO.setData(sellerShopService.queryListPage(queryPageInfo));

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            return ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        }
        return resultObjectVO;
    }





    /**
     * 禁用启用店铺
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO disabledEnabled(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            SellerShopVO entity = JSONObject.parseObject(requestVo.getEntityJson(),SellerShopVO.class);
            Check.notNull(entity.getPublicShopId(), ResultVO.FAILD, "没有找到ID");

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
                        return ResultObjectVO.fail(ResultVO.FAILD, "启用失败,该用户下已经存在其他店铺");
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


                refershRedisCache(sellerShop.getId());
            }

            resultObjectVO.setData(entity);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            return ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        }
        return resultObjectVO;
    }


}
