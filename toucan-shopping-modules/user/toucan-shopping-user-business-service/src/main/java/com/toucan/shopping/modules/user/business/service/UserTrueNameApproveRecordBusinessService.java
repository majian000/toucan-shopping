package com.toucan.shopping.modules.user.business.service;


import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.modules.common.annotation.RequestCheck;
import com.toucan.shopping.modules.common.exception.BusinessValidationException;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.Check;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.user.constant.UserRegistConstant;
import com.toucan.shopping.modules.user.entity.UserTrueNameApprove;
import com.toucan.shopping.modules.user.entity.UserTrueNameApproveRecord;
import com.toucan.shopping.modules.user.service.UserTrueNameApproveRecordService;
import com.toucan.shopping.modules.user.service.UserTrueNameApproveService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserTrueNameApproveRecordBusinessService {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private UserTrueNameApproveRecordService userTrueNameApproveRecordService;

    @Autowired
    private Toucan toucan;


    @RequestCheck(requireEntity = true)
    public ResultObjectVO save(RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        UserTrueNameApproveRecord userTrueNameApproveRecord = JSONObject.parseObject(requestJsonVO.getEntityJson(),UserTrueNameApproveRecord.class);
        try {
            Check.notNull(userTrueNameApproveRecord.getApproveStatus(), ResultObjectVO.FAILD, "审核状态不能为空");


            userTrueNameApproveRecord.setId(idGenerator.id());
            int ret = userTrueNameApproveRecordService.save(userTrueNameApproveRecord);
            if(ret<=0)
            {
                logger.warn("保存用户实名审核记录失败 requestJson{} id{}",requestJsonVO.getEntityJson(),userTrueNameApproveRecord.getId());
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
            }
            resultObjectVO.setData(userTrueNameApproveRecord);
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
