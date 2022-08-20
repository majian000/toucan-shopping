package com.toucan.shopping.cloud.user.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.user.constant.UserBuyCarConstant;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import com.toucan.shopping.modules.user.entity.UserBuyCarItem;
import com.toucan.shopping.modules.user.redis.UserBuyCarKey;
import com.toucan.shopping.modules.user.service.UserBuyCarItemService;
import com.toucan.shopping.modules.user.vo.UserBuyCarItemVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 收货地址 增删改查
 */
@RestController
@RequestMapping("/userBuyCar")
public class UserBuyCarController {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private SkylarkLock skylarkLock;

    @Autowired
    private UserBuyCarItemService userBuyCarItemService;


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
        UserBuyCarItemVO userBuyCarVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), UserBuyCarItemVO.class);
        if(userBuyCarVO.getShopProductSkuId()==null)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("SKUID不能为空");
            return resultObjectVO;
        }
        if(userBuyCarVO.getUserMainId()==null)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("用户ID不能为空");
            return resultObjectVO;
        }
        String userMainId = String.valueOf(userBuyCarVO.getUserMainId());
        try {
            boolean lockStatus = skylarkLock.lock(UserBuyCarKey.getSaveLockKey(userMainId), userMainId);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
                return resultObjectVO;
            }
            UserBuyCarItemVO queryUserBuyCar = new UserBuyCarItemVO();
            queryUserBuyCar.setUserMainId(userBuyCarVO.getUserMainId());
            List<UserBuyCarItem> userBuyCarItems = userBuyCarItemService.findListByEntity(queryUserBuyCar);
            if(!CollectionUtils.isEmpty(userBuyCarItems)&& userBuyCarItems.size()>=UserBuyCarConstant.MAX_BUY_ITEM_COUNT)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("购物车中商品数量达到上限");
                return resultObjectVO;
            }

            queryUserBuyCar.setShopProductSkuId(userBuyCarVO.getShopProductSkuId());
            userBuyCarItems = userBuyCarItemService.findListByEntity(queryUserBuyCar);
            if(!CollectionUtils.isEmpty(userBuyCarItems)){
                UserBuyCarItem userBuyCarItem = userBuyCarItems.get(0);
                if(userBuyCarItem.getBuyCount().intValue()!=userBuyCarVO.getBuyCount().intValue()) {
                    userBuyCarItem.setBuyCount(userBuyCarVO.getBuyCount());
                    userBuyCarItem.setUpdateDate(new Date());
                    userBuyCarItemService.update(userBuyCarItem);
                }

                resultObjectVO.setData(userBuyCarItemService.findListByUserMainId(userBuyCarVO.getUserMainId()));
                resultObjectVO.setCode(201);
                resultObjectVO.setMsg("购物车中已存在该商品");
                return resultObjectVO;
            }
            userBuyCarVO.setId(idGenerator.id());
            userBuyCarVO.setDeleteStatus((short)0);
            userBuyCarVO.setCreateDate(new Date());
            int ret = userBuyCarItemService.save(userBuyCarVO);
            if(ret<=0)
            {
                logger.warn("保存商品到购物车失败 requestJson{} id{}",requestJsonVO.getEntityJson(),userBuyCarVO.getId());
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
            }

            resultObjectVO.setData(userBuyCarItemService.findListByUserMainId(userBuyCarVO.getUserMainId()));
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }finally{
            skylarkLock.unLock(UserBuyCarKey.getSaveLockKey(userMainId), userMainId);
        }
        return resultObjectVO;
    }






    /**
     * 删除指定
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/remove/buy/car",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO removeBuyCar(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            UserBuyCarItem entity = JSONObject.parseObject(requestVo.getEntityJson(), UserBuyCarItem.class);
            if(entity.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到ID");
                return resultObjectVO;
            }


            int row = userBuyCarItemService.deleteByIdAndUserMainId(entity.getId(),entity.getUserMainId());
            resultObjectVO.setData(entity);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 清空指定用户的购物车
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/clear/buy/car/userMainId",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO clearByUserMainId(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            UserBuyCarItem entity = JSONObject.parseObject(requestVo.getEntityJson(), UserBuyCarItem.class);
            if(entity.getUserMainId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到用户ID");
                return resultObjectVO;
            }


            int row = userBuyCarItemService.deleteByUserMainId(entity.getUserMainId());
            resultObjectVO.setData(entity);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    /**
     * 根据用户ID查询
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/list/userMainId",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO listByUserMainId(@RequestBody RequestJsonVO requestJsonVO){
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
        UserBuyCarItemVO userBuyCarVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), UserBuyCarItemVO.class);
        if(userBuyCarVO.getUserMainId()==null)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("用户ID不能为空");
            return resultObjectVO;
        }
        try {
            //查询购物车信息列表
            resultObjectVO.setData(userBuyCarItemService.findListByUserMainId(userBuyCarVO.getUserMainId()));
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }




    @RequestMapping(value="/updates",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO updates(@RequestBody RequestJsonVO requestJsonVO){
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
        List<UserBuyCarItemVO> userBuyCarVos = JSONArray.parseArray(requestJsonVO.getEntityJson(), UserBuyCarItemVO.class);

        if(!CollectionUtils.isEmpty(userBuyCarVos)) {
            String userMainId = "-1";
            for(UserBuyCarItemVO userBuyCarVO:userBuyCarVos)
            {
                if(userBuyCarVO.getId()==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("ID不能为空");
                    return resultObjectVO;
                }
                if(userBuyCarVO.getUserMainId()==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("用户ID不能为空");
                    return resultObjectVO;
                }
                if(userBuyCarVO.getBuyCount()==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("数量不能为空");
                    return resultObjectVO;
                }
            }
            try {
                userMainId = String.valueOf(userBuyCarVos.get(0).getUserMainId());
                boolean lockStatus = skylarkLock.lock(UserBuyCarKey.getUpdateLockKey(userMainId), userMainId);
                if (!lockStatus) {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请稍后重试");
                    return resultObjectVO;
                }
                //查询出库里的所有购物车项
                List<UserBuyCarItem> userBuyCarItemList = userBuyCarItemService.findListByUserMainId(userBuyCarVos.get(0).getUserMainId());
                for (UserBuyCarItemVO userBuyCarVO : userBuyCarVos){
                    for (UserBuyCarItem userBuyCarItem : userBuyCarItemList) {
                        if(userBuyCarVO.getId().longValue()==userBuyCarItem.getId().longValue())
                        {
                            //本次修改了数量,更新库中数据
                            if(userBuyCarVO.getBuyCount().intValue()!=userBuyCarItem.getBuyCount().intValue())
                            {
                                userBuyCarItem.setBuyCount(userBuyCarVO.getBuyCount());
                                userBuyCarItem.setUpdateDate(new Date());
                                userBuyCarItemService.update(userBuyCarItem);
                            }
                            continue;
                        }

                    }
                }


            } catch (Exception e) {
                logger.warn(e.getMessage(), e);
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
            } finally {
                skylarkLock.unLock(UserBuyCarKey.getUpdateLockKey(userMainId), userMainId);
            }
        }
        return resultObjectVO;
    }


}
