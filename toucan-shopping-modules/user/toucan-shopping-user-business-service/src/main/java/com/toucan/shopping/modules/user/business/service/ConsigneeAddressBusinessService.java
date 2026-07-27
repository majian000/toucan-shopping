package com.toucan.shopping.modules.user.business.service;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.annotation.RequestCheck;
import com.toucan.shopping.modules.common.exception.BusinessValidationException;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.util.Check;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import com.toucan.shopping.modules.user.constant.ConsigneeAddressConstant;
import com.toucan.shopping.modules.user.entity.ConsigneeAddress;
import com.toucan.shopping.modules.user.page.ConsigneeAddressPageInfo;
import com.toucan.shopping.modules.user.redis.UserCenterConsigneeAddressKey;
import com.toucan.shopping.modules.user.service.ConsigneeAddressService;
import com.toucan.shopping.modules.user.vo.ConsigneeAddressVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Service
public class ConsigneeAddressBusinessService {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private SkylarkLock skylarkLock;

    @Autowired
    private ConsigneeAddressService consigneeAddressService;


    @RequestCheck(requireEntity = true)
    public ResultObjectVO save(RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        ConsigneeAddressVO consigneeAddressVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), ConsigneeAddressVO.class);
        Check.notEmpty(consigneeAddressVO.getAppCode(), ResultObjectVO.FAILD, "应用编码不能为空");
        Check.notEmpty(consigneeAddressVO.getName(), ResultObjectVO.FAILD, "收货人不能为空");
        Check.notEmpty(consigneeAddressVO.getAddress(), ResultObjectVO.FAILD, "收货地址不能为空");
        Check.notEmpty(consigneeAddressVO.getPhone(), ResultObjectVO.FAILD, "联系电话不能为空");
        String userMainId = String.valueOf(consigneeAddressVO.getUserMainId());
        try {
            boolean lockStatus = skylarkLock.lock(UserCenterConsigneeAddressKey.getSaveLockKey(userMainId), userMainId);
            if (!lockStatus) {
                return ResultObjectVO.fail(ResultObjectVO.FAILD, "请稍后重试");
            }
            //查询收货人数量
            ConsigneeAddressVO queryConsigneeAddress = new ConsigneeAddressVO();
            queryConsigneeAddress.setUserMainId(consigneeAddressVO.getUserMainId());
            queryConsigneeAddress.setAppCode(consigneeAddressVO.getAppCode());
            List<ConsigneeAddress> consigneeAddresses = consigneeAddressService.findListByEntity(queryConsigneeAddress);
            if(!CollectionUtils.isEmpty(consigneeAddresses)&&consigneeAddresses.size()>= ConsigneeAddressConstant.MAX_COUNT)
            {
                return ResultObjectVO.fail(ResultObjectVO.FAILD, "收货信息数量达到"+ ConsigneeAddressConstant.MAX_COUNT+"个上限");
            }


            consigneeAddressVO.setId(idGenerator.id());
            consigneeAddressVO.setDeleteStatus((short)0);
            consigneeAddressVO.setCreateDate(new Date());
            if(CollectionUtils.isEmpty(consigneeAddresses))
            {
                consigneeAddressVO.setDefaultStatus((short)1);
            }else{
                consigneeAddressVO.setDefaultStatus((short)0);
            }
            int ret = consigneeAddressService.save(consigneeAddressVO);
            if(ret<=0)
            {
                logger.warn("保存收货信息失败 requestJson{} id{}",requestJsonVO.getEntityJson(),consigneeAddressVO.getId());
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
            }
            resultObjectVO.setData(consigneeAddressVO);
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            return ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO deleteByIdAndUserMainIdAndAppCode(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            ConsigneeAddress entity = JSONObject.parseObject(requestVo.getEntityJson(),ConsigneeAddress.class);
            Check.notNull(entity.getId(), ResultVO.FAILD, "ID不能为空");
            Check.notNull(entity.getUserMainId(), ResultVO.FAILD, "用户ID不能为空");
            Check.notEmpty(entity.getAppCode(), ResultVO.FAILD, "应用编码不能为空");
            int row = consigneeAddressService.deleteByIdAndUserMainIdAndAppCode(entity.getId(),entity.getUserMainId(),entity.getAppCode());
            if (row < 1) {
                return ResultObjectVO.fail(ResultVO.FAILD, "请重试!");
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


    /**
     * 根据用户ID查询
     * @param requestJsonVO
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO listByUserMainId(RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        ConsigneeAddressVO consigneeAddressVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), ConsigneeAddressVO.class);
        Check.notEmpty(consigneeAddressVO.getName(), ResultObjectVO.FAILD, "收货人不能为空");
        Check.notEmpty(consigneeAddressVO.getAddress(), ResultObjectVO.FAILD, "收货地址不能为空");
        Check.notEmpty(consigneeAddressVO.getPhone(), ResultObjectVO.FAILD, "联系电话不能为空");
        Check.notNull(consigneeAddressVO.getUserMainId(), ResultObjectVO.FAILD, "用户ID不能为空");
        try {
            //查询收货信息列表
            ConsigneeAddressVO queryConsigneeAddress = new ConsigneeAddressVO();
            queryConsigneeAddress.setUserMainId(consigneeAddressVO.getUserMainId());
            resultObjectVO.setData(consigneeAddressService.findListByEntity(queryConsigneeAddress));
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
            ConsigneeAddressPageInfo queryPageInfo = JSONObject.parseObject(requestVo.getEntityJson(), ConsigneeAddressPageInfo.class);

            //查询列表页
            resultObjectVO.setData(consigneeAddressService.queryListPage(queryPageInfo));

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
     * 设置默认
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO setDefaultByIdAndUserMainId(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            ConsigneeAddress entity = JSONObject.parseObject(requestVo.getEntityJson(),ConsigneeAddress.class);
            Check.notNull(entity.getId(), ResultVO.FAILD, "ID不能为空");
            Check.notNull(entity.getUserMainId(), ResultVO.FAILD, "用户ID不能为空");
            Check.notEmpty(entity.getAppCode(), ResultVO.FAILD, "应用编码不能为空");

            consigneeAddressService.setCancelDefaultByUserMainIdAndAppCode(entity.getUserMainId(),entity.getAppCode());

            int row = consigneeAddressService.setDefaultByIdAndUserMainIdAndAppCode(entity.getId(),entity.getUserMainId(),entity.getAppCode());
            if (row < 1) {
                return ResultObjectVO.fail(ResultVO.FAILD, "请重试!");
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




    /**
     * 根据ID、用户ID、应用编码 查询单条数据
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO findByIdAndUserMainIdAndAppcode(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            ConsigneeAddress entity = JSONObject.parseObject(requestVo.getEntityJson(),ConsigneeAddress.class);
            Check.notNull(entity.getId(), ResultVO.FAILD, "ID不能为空");
            Check.notNull(entity.getUserMainId(), ResultVO.FAILD, "用户ID不能为空");
            Check.notEmpty(entity.getAppCode(), ResultVO.FAILD, "应用编码不能为空");

            resultObjectVO.setData(consigneeAddressService.findByIdAndUserMainIdAndAppCode(entity.getId(),entity.getUserMainId(),entity.getAppCode()));

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            return ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        }
        return resultObjectVO;
    }





    @RequestCheck(requireEntity = true)
    public ResultObjectVO update(RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        ConsigneeAddressVO consigneeAddressVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), ConsigneeAddressVO.class);
        Check.notEmpty(consigneeAddressVO.getAppCode(), ResultObjectVO.FAILD, "应用编码不能为空");
        Check.notEmpty(consigneeAddressVO.getName(), ResultObjectVO.FAILD, "收货人不能为空");
        Check.notEmpty(consigneeAddressVO.getAddress(), ResultObjectVO.FAILD, "收货地址不能为空");
        Check.notEmpty(consigneeAddressVO.getPhone(), ResultObjectVO.FAILD, "联系电话不能为空");
        Check.notNull(consigneeAddressVO.getId(), ResultObjectVO.FAILD, "ID不能为空");
        String userMainId = String.valueOf(consigneeAddressVO.getUserMainId());
        try {
            boolean lockStatus = skylarkLock.lock(UserCenterConsigneeAddressKey.getUpdateLockKey(userMainId), userMainId);
            if (!lockStatus) {
                return ResultObjectVO.fail(ResultObjectVO.FAILD, "请稍后重试");
            }

            consigneeAddressVO.setUpdateDate(new Date());

            int ret = consigneeAddressService.update(consigneeAddressVO);
            if(ret<=0)
            {
                logger.warn("修改收货信息失败 requestJson{} id{}",requestJsonVO.getEntityJson(),consigneeAddressVO.getId());
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
            }
            resultObjectVO.setData(consigneeAddressVO);
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            return ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        }finally{
            skylarkLock.unLock(UserCenterConsigneeAddressKey.getUpdateLockKey(userMainId), userMainId);
        }
        return resultObjectVO;
    }




    /**
     * 查询设置为默认的收货信息,如果没有默认就查询最新一条
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO findDefaultByUserMainIdAndAppcode(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            ConsigneeAddress entity = JSONObject.parseObject(requestVo.getEntityJson(),ConsigneeAddress.class);

            Check.notNull(entity.getUserMainId(), ResultVO.FAILD, "用户ID不能为空");
            Check.notEmpty(entity.getAppCode(), ResultVO.FAILD, "应用编码不能为空");

            ConsigneeAddressVO consigneeAddressVO = consigneeAddressService.findDefaultByUserMainIdAndAppCode(entity.getUserMainId(),entity.getAppCode());
            //如果没有默认 就查询最新一条
            if(consigneeAddressVO==null)
            {
                consigneeAddressVO = consigneeAddressService.findNewestOneByUserMainIdAndAppCode(entity.getUserMainId(),entity.getAppCode());
            }
            resultObjectVO.setData(consigneeAddressVO);

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
