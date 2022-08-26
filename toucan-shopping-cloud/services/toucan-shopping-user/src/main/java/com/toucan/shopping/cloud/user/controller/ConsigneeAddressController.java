package com.toucan.shopping.cloud.user.controller;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.user.constant.ConsigneeAddressConstant;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import com.toucan.shopping.modules.user.entity.ConsigneeAddress;
import com.toucan.shopping.modules.user.redis.UserCenterConsigneeAddressKey;
import com.toucan.shopping.modules.user.service.ConsigneeAddressService;
import com.toucan.shopping.modules.user.vo.ConsigneeAddressVO;
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
@RequestMapping("/consignee/address")
public class ConsigneeAddressController {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private SkylarkLock skylarkLock;

    @Autowired
    private ConsigneeAddressService consigneeAddressService;


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
        ConsigneeAddressVO consigneeAddressVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), ConsigneeAddressVO.class);
        if(StringUtils.isEmpty(consigneeAddressVO.getName()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("收货人不能为空");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(consigneeAddressVO.getAddress()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("收货地址不能为空");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(consigneeAddressVO.getPhone()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("联系电话不能为空");
            return resultObjectVO;
        }
        String userMainId = String.valueOf(consigneeAddressVO.getUserMainId());
        try {
            boolean lockStatus = skylarkLock.lock(UserCenterConsigneeAddressKey.getSaveLockKey(userMainId), userMainId);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
                return resultObjectVO;
            }
            //查询收货人数量,最多20个
            ConsigneeAddressVO queryConsigneeAddress = new ConsigneeAddressVO();
            queryConsigneeAddress.setUserMainId(consigneeAddressVO.getUserMainId());
            List<ConsigneeAddress> consigneeAddresses = consigneeAddressService.findListByEntity(queryConsigneeAddress);
            if(!CollectionUtils.isEmpty(consigneeAddresses)&&consigneeAddresses.size()>= ConsigneeAddressConstant.MAX_COUNT)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("收货信息数量达到"+ConsigneeAddressConstant.MAX_COUNT+"个上限");
                return resultObjectVO;
            }

            consigneeAddressVO.setId(idGenerator.id());
            consigneeAddressVO.setDeleteStatus((short)0);
            consigneeAddressVO.setCreateDate(new Date());
            int ret = consigneeAddressService.save(consigneeAddressVO);
            if(ret<=0)
            {
                logger.warn("保存收货信息失败 requestJson{} id{}",requestJsonVO.getEntityJson(),consigneeAddressVO.getId());
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
            }
            resultObjectVO.setData(consigneeAddressVO);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }finally{
            skylarkLock.unLock(UserCenterConsigneeAddressKey.getSaveLockKey(userMainId), userMainId);
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
            ConsigneeAddress entity = JSONObject.parseObject(requestVo.getEntityJson(),ConsigneeAddress.class);
            if(entity.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到ID");
                return resultObjectVO;
            }

            //查询是否存在
            ConsigneeAddressVO query=new ConsigneeAddressVO();
            query.setId(entity.getId());
            List<ConsigneeAddress> adminList = consigneeAddressService.findListByEntity(query);
            if(CollectionUtils.isEmpty(adminList))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("收货信息不存在!");
                return resultObjectVO;
            }


            int row = consigneeAddressService.deleteById(entity.getId());
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
        ConsigneeAddressVO consigneeAddressVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), ConsigneeAddressVO.class);
        if(StringUtils.isEmpty(consigneeAddressVO.getName()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("收货人不能为空");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(consigneeAddressVO.getAddress()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("收货地址不能为空");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(consigneeAddressVO.getPhone()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("联系电话不能为空");
            return resultObjectVO;
        }
        if(consigneeAddressVO.getUserMainId()==null)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("用户ID不能为空");
            return resultObjectVO;
        }
        try {
            //查询收货信息列表
            ConsigneeAddressVO queryConsigneeAddress = new ConsigneeAddressVO();
            queryConsigneeAddress.setUserMainId(consigneeAddressVO.getUserMainId());
            resultObjectVO.setData(consigneeAddressService.findListByEntity(queryConsigneeAddress));
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }




}
