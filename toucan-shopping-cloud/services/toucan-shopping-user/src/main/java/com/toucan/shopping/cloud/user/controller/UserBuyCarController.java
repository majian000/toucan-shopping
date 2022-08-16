package com.toucan.shopping.cloud.user.controller;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import com.toucan.shopping.modules.user.entity.UserBuyCar;
import com.toucan.shopping.modules.user.redis.UserBuyCarKey;
import com.toucan.shopping.modules.user.service.UserBuyCarService;
import com.toucan.shopping.modules.user.vo.UserBuyCarVO;
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
    private UserBuyCarService userBuyCarService;


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
        UserBuyCarVO userBuyCarVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), UserBuyCarVO.class);
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
            //查询收货人数量,最多20个
            UserBuyCarVO queryUserBuyCar = new UserBuyCarVO();
            queryUserBuyCar.setUserMainId(userBuyCarVO.getUserMainId());
            List<UserBuyCar> userBuyCars = userBuyCarService.findListByEntity(queryUserBuyCar);
            if(!CollectionUtils.isEmpty(userBuyCars)&&userBuyCars.size()>=100)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("购物车中商品数量达到上限");
                return resultObjectVO;
            }

            queryUserBuyCar.setShopProductSkuId(userBuyCarVO.getShopProductSkuId());
            userBuyCars = userBuyCarService.findListByEntity(queryUserBuyCar);
            if(!CollectionUtils.isEmpty(userBuyCars)){
                UserBuyCar userBuyCar = userBuyCars.get(0);
                if(userBuyCar.getBuyCount().intValue()!=userBuyCarVO.getBuyCount().intValue()) {
                    userBuyCar.setBuyCount(userBuyCarVO.getBuyCount());
                    userBuyCar.setUpdateDate(new Date());
                    userBuyCarService.update(userBuyCar);
                }

                resultObjectVO.setData(userBuyCarService.findListByUserMainId(userBuyCarVO.getUserMainId()));
                resultObjectVO.setCode(201);
                resultObjectVO.setMsg("购物车中已存在该商品");
                return resultObjectVO;
            }
            userBuyCarVO.setId(idGenerator.id());
            userBuyCarVO.setDeleteStatus((short)0);
            userBuyCarVO.setCreateDate(new Date());
            int ret = userBuyCarService.save(userBuyCarVO);
            if(ret<=0)
            {
                logger.warn("保存商品到购物车失败 requestJson{} id{}",requestJsonVO.getEntityJson(),userBuyCarVO.getId());
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
            }

            resultObjectVO.setData(userBuyCarService.findListByUserMainId(userBuyCarVO.getUserMainId()));
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
    @RequestMapping(value="/delete/id",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteById(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            UserBuyCar entity = JSONObject.parseObject(requestVo.getEntityJson(),UserBuyCar.class);
            if(entity.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到ID");
                return resultObjectVO;
            }

            //查询是否存在该角色
            UserBuyCarVO query=new UserBuyCarVO();
            query.setId(entity.getId());
            List<UserBuyCar> adminList = userBuyCarService.findListByEntity(query);
            if(CollectionUtils.isEmpty(adminList))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("该商品不存在!");
                return resultObjectVO;
            }


            int row = userBuyCarService.deleteById(entity.getId());
            if (row < 1) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请重试!");
                return resultObjectVO;
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
        UserBuyCarVO userBuyCarVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), UserBuyCarVO.class);
        if(userBuyCarVO.getUserMainId()==null)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("用户ID不能为空");
            return resultObjectVO;
        }
        try {
            //查询购物车信息列表
            resultObjectVO.setData(userBuyCarService.findListByUserMainId(userBuyCarVO.getUserMainId()));
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }




}
