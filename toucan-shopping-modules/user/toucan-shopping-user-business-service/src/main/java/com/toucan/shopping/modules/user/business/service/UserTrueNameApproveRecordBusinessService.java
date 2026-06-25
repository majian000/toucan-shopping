package com.toucan.shopping.modules.user.business.service;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
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


    public ResultObjectVO save(RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("没有找到请求对象");
            return resultObjectVO;
        }
        if (StringUtils.isEmpty(requestJsonVO.getAppCode())) {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("没有找到应用编码");
            return resultObjectVO;
        }
        UserTrueNameApproveRecord userTrueNameApproveRecord = JSONObject.parseObject(requestJsonVO.getEntityJson(),UserTrueNameApproveRecord.class);
        try {
            if(userTrueNameApproveRecord.getApproveStatus()==null)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("审核状态不能为空");
                return resultObjectVO;
            }


            userTrueNameApproveRecord.setId(idGenerator.id());
            int ret = userTrueNameApproveRecordService.save(userTrueNameApproveRecord);
            if(ret<=0)
            {
                logger.warn("保存用户实名审核记录失败 requestJson{} id{}",requestJsonVO.getEntityJson(),userTrueNameApproveRecord.getId());
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
            }
            resultObjectVO.setData(userTrueNameApproveRecord);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }




}
