package com.toucan.shopping.modules.admin.auth.business.service;

import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.modules.admin.auth.entity.AdminInfo;
import com.toucan.shopping.modules.admin.auth.service.AdminInfoService;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import com.toucan.shopping.modules.common.annotation.RequestCheck;
import com.toucan.shopping.modules.common.exception.BusinessValidationException;
import com.toucan.shopping.modules.common.util.Check;

/**
 * 管理员信息管理
 */
@Service
public class AdminInfoBusinessService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AdminInfoService adminInfoService;

    @Autowired
    private IdGenerator idGenerator;

    /**
     * 保存/更新管理员信息
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO saveOrUpdate(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            AdminInfo adminInfo = JSONObject.parseObject(requestVo.getEntityJson(), AdminInfo.class);
            Check.notEmpty(adminInfo.getAdminId(), ResultVO.FAILD, "adminId不能为空");

            Check.notEmpty(adminInfo.getRealName(), ResultVO.FAILD, "请输入真实姓名");


            AdminInfo existInfo = adminInfoService.findByAdminId(adminInfo.getAdminId());
            if (existInfo != null) {
                // 更新
                BeanUtils.copyProperties(existInfo,adminInfo);
                existInfo.setUpdateAdminId(adminInfo.getUpdateAdminId());
                existInfo.setUpdateDate(new Date());
                adminInfoService.update(existInfo);
            } else {
                // 新增
                adminInfo.setId(idGenerator.id());
                adminInfo.setDeleteStatus((short) 0);
                adminInfo.setCreateDate(new Date());
                adminInfoService.save(adminInfo);
            }

            resultObjectVO.setData(adminInfo);
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        }
        return resultObjectVO;
    }

    /**
     * 根据adminId查询
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO findByAdminId(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            AdminInfo query = JSONObject.parseObject(requestVo.getEntityJson(), AdminInfo.class);
            Check.notEmpty(query.getAdminId(), ResultVO.FAILD, "adminId不能为空");


            AdminInfo adminInfo = adminInfoService.findByAdminId(query.getAdminId());
            resultObjectVO.setData(adminInfo);
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        }
        return resultObjectVO;
    }
}
