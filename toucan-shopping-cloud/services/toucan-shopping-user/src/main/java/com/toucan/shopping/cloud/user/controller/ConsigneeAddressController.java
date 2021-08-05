package com.toucan.shopping.cloud.user.controller;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.util.GlobalUUID;
import com.toucan.shopping.modules.common.util.MD5Util;
import com.toucan.shopping.modules.common.util.UserRegistUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.redis.service.ToucanStringRedisService;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import com.toucan.shopping.modules.user.entity.ConsigneeAddress;
import com.toucan.shopping.modules.user.redis.UserCenterConsigneeAddressKey;
import com.toucan.shopping.modules.user.service.ConsigneeAddressService;
import com.toucan.shopping.modules.user.vo.ConsigneeAddressVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.xml.transform.Result;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 收货地址 增删改查
 */
@RestController
@RequestMapping("/consigneeAddress")
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
            resultObjectVO.setMsg("请求失败,没有找到请求对象");
            return resultObjectVO;
        }
        if (StringUtils.isEmpty(requestJsonVO.getAppCode())) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请求失败,没有找到应用编码");
            return resultObjectVO;
        }
        ConsigneeAddressVO consigneeAddressVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), ConsigneeAddressVO.class);
        if(StringUtils.isEmpty(consigneeAddressVO.getName()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请求失败,收货人不能为空");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(consigneeAddressVO.getAddress()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请求失败,收货地址不能为空");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(consigneeAddressVO.getPhone()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请求失败,联系电话不能为空");
            return resultObjectVO;
        }
        String userMainId = String.valueOf(consigneeAddressVO.getUserMainId());
        try {
            boolean lockStatus = skylarkLock.lock(UserCenterConsigneeAddressKey.getSaveLockKey(userMainId), userMainId);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求失败,请稍候重试");
                return resultObjectVO;
            }
            //查询收货人数量,最多20个
            ConsigneeAddress queryConsigneeAddress = new ConsigneeAddress();
            queryConsigneeAddress.setUserMainId(consigneeAddressVO.getUserMainId());
            List<ConsigneeAddress> consigneeAddresses = consigneeAddressService.findListByEntity(queryConsigneeAddress);
            if(!CollectionUtils.isEmpty(consigneeAddresses)&&consigneeAddresses.size()>=20)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("保存失败,收货信息数量达到上限");
                return resultObjectVO;
            }

            consigneeAddressVO.setId(idGenerator.id());
            consigneeAddressVO.setDeleteStatus((short)0);
            int ret = consigneeAddressService.save(consigneeAddressVO);
            if(ret<=0)
            {
                logger.warn("保存收货信息失败 requestJson{} id{}",requestJsonVO.getEntityJson(),consigneeAddressVO.getId());
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,请稍后重试");
            }
            resultObjectVO.setData(consigneeAddressVO);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }finally{
            skylarkLock.unLock(UserCenterConsigneeAddressKey.getSaveLockKey(userMainId), userMainId);
        }
        return resultObjectVO;
    }
}
