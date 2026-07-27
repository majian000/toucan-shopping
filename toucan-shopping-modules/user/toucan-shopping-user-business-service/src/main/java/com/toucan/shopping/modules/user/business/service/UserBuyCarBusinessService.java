package com.toucan.shopping.modules.user.business.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.annotation.RequestCheck;
import com.toucan.shopping.modules.common.exception.BusinessValidationException;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.util.Check;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import com.toucan.shopping.modules.user.constant.UserBuyCarConstant;
import com.toucan.shopping.modules.user.entity.UserBuyCarItem;
import com.toucan.shopping.modules.user.redis.UserBuyCarKey;
import com.toucan.shopping.modules.user.service.UserBuyCarItemService;
import com.toucan.shopping.modules.user.vo.UserBuyCarItemVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Service
public class UserBuyCarBusinessService {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private SkylarkLock skylarkLock;

    @Autowired
    private UserBuyCarItemService userBuyCarItemService;


    @RequestCheck(requireEntity = true)
    public ResultObjectVO save(RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        UserBuyCarItemVO userBuyCarVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), UserBuyCarItemVO.class);
        Check.notNull(userBuyCarVO.getShopProductSkuId(), ResultObjectVO.FAILD, "SKUID不能为空");
        Check.notNull(userBuyCarVO.getUserMainId(), ResultObjectVO.FAILD, "用户ID不能为空");
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
            Check.isTrue(CollectionUtils.isEmpty(userBuyCarItems) || userBuyCarItems.size() < UserBuyCarConstant.MAX_BUY_ITEM_COUNT, ResultObjectVO.FAILD, "购物车中商品数量达到上限");

            queryUserBuyCar.setShopProductSkuId(userBuyCarVO.getShopProductSkuId());
            userBuyCarItems = userBuyCarItemService.findListByEntity(queryUserBuyCar);
            if(!CollectionUtils.isEmpty(userBuyCarItems)){
                UserBuyCarItem userBuyCarItem = userBuyCarItems.get(0);
                if(userBuyCarItem.getBuyCount()!=null)
                {
                    userBuyCarItem.setBuyCount(userBuyCarItem.getBuyCount().intValue()+userBuyCarVO.getBuyCount().intValue());
                }else {
                    userBuyCarItem.setBuyCount(userBuyCarVO.getBuyCount());
                }
                userBuyCarItem.setUpdateDate(new Date());
                userBuyCarItemService.update(userBuyCarItem);

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
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO removeBuyCar(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            UserBuyCarItem entity = JSONObject.parseObject(requestVo.getEntityJson(), UserBuyCarItem.class);
            Check.notNull(entity.getId(), ResultObjectVO.FAILD, "没有找到ID");


            int row = userBuyCarItemService.deleteByIdAndUserMainId(entity.getId(),entity.getUserMainId());
            resultObjectVO.setData(entity);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO clearByUserMainId(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            UserBuyCarItem entity = JSONObject.parseObject(requestVo.getEntityJson(), UserBuyCarItem.class);
            Check.notNull(entity.getUserMainId(), ResultObjectVO.FAILD, "没有找到用户ID");


            int row = userBuyCarItemService.deleteByUserMainId(entity.getUserMainId());
            resultObjectVO.setData(entity);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO listByUserMainId(RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        UserBuyCarItemVO userBuyCarVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), UserBuyCarItemVO.class);
        Check.notNull(userBuyCarVO.getUserMainId(), ResultObjectVO.FAILD, "用户ID不能为空");
        try {
            //查询购物车信息列表
            resultObjectVO.setData(userBuyCarItemService.findListByUserMainId(userBuyCarVO.getUserMainId()));
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    @RequestCheck(requireEntity = true)
    public ResultObjectVO updates(RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        List<UserBuyCarItemVO> userBuyCarVos = JSONArray.parseArray(requestJsonVO.getEntityJson(), UserBuyCarItemVO.class);

        if(!CollectionUtils.isEmpty(userBuyCarVos)) {
            String userMainId = "-1";
            for(UserBuyCarItemVO userBuyCarVO:userBuyCarVos)
            {
                Check.notNull(userBuyCarVO.getId(), ResultObjectVO.FAILD, "ID不能为空");
                Check.notNull(userBuyCarVO.getUserMainId(), ResultObjectVO.FAILD, "用户ID不能为空");
                Check.notNull(userBuyCarVO.getBuyCount(), ResultObjectVO.FAILD, "数量不能为空");
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


            } catch (BusinessValidationException e) {
                return ResultObjectVO.fail(e.getCode(), e.getMessage());
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


    @RequestCheck(requireEntity = true)
    public ResultObjectVO update(RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        UserBuyCarItemVO userBuyCarItemVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), UserBuyCarItemVO.class);
        String userMainId = "-1";
        Check.notNull(userBuyCarItemVO.getId(), ResultObjectVO.FAILD, "ID不能为空");
        Check.notNull(userBuyCarItemVO.getUserMainId(), ResultObjectVO.FAILD, "用户ID不能为空");
        Check.notNull(userBuyCarItemVO.getBuyCount(), ResultObjectVO.FAILD, "数量不能为空");
        try {
            userMainId = String.valueOf(userBuyCarItemVO.getUserMainId());
            boolean lockStatus = skylarkLock.lock(UserBuyCarKey.getUpdateLockKey(userMainId), userMainId);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
                return resultObjectVO;
            }
            //查询出库里的所有购物车项
            UserBuyCarItem userBuyCarItem = userBuyCarItemService.findById(userBuyCarItemVO.getId());
            if(userBuyCarItemVO.getId().longValue()==userBuyCarItem.getId().longValue()
                &&userBuyCarItemVO.getUserMainId().longValue()==userBuyCarItem.getUserMainId().longValue())
            {
                //本次修改了数量,更新库中数据
                if(userBuyCarItemVO.getBuyCount().intValue()!=userBuyCarItem.getBuyCount().intValue())
                {
                    userBuyCarItem.setBuyCount(userBuyCarItemVO.getBuyCount());
                    userBuyCarItem.setUpdateDate(new Date());
                    userBuyCarItemService.update(userBuyCarItem);
                }
            }



        } catch (BusinessValidationException e) {
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        } finally {
            skylarkLock.unLock(UserBuyCarKey.getUpdateLockKey(userMainId), userMainId);
        }
        return resultObjectVO;
    }

}
