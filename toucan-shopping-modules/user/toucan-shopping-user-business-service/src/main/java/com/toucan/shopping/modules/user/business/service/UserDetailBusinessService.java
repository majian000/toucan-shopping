package com.toucan.shopping.modules.user.business.service;


import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.modules.common.annotation.RequestCheck;
import com.toucan.shopping.modules.common.exception.BusinessValidationException;
import com.toucan.shopping.modules.user.entity.UserDetail;
import com.toucan.shopping.modules.user.kafka.constant.UserMessageTopicConstant;
import com.toucan.shopping.modules.user.kafka.message.UserDetailModifyMessage;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.util.Check;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.user.service.UserDetailService;
import com.toucan.shopping.modules.user.vo.UserDetailVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserDetailBusinessService {


    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private IdGenerator idGenerator;


    @Autowired
    private UserDetailService userDetailService;


    @Autowired
    private KafkaTemplate kafkaTemplate;




    @RequestCheck(requireEntity = true)
    public ResultObjectVO updateDetail(RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            UserDetailVO userDetailVO = JSONObject.parseObject(requestJsonVO.getEntityJson(),UserDetailVO.class);

            Check.notNull(userDetailVO.getUserMainId(), ResultObjectVO.FAILD, "没有找到用户ID");
            Check.notEmpty(userDetailVO.getNickName(), ResultObjectVO.FAILD, "请输入昵称");

            if(userDetailVO.getNickName().length()>15)
            {
                return ResultObjectVO.fail(ResultObjectVO.FAILD, "昵称过长,请重新输入");
            }
            UserDetail userDetail = new UserDetail();
            BeanUtils.copyProperties(userDetail,userDetailVO);

            List<UserDetail> userDetails = userDetailService.queryListByUserId(new Long[]{userDetailVO.getUserMainId()});
            int row =0;
            if(CollectionUtils.isNotEmpty(userDetails))
            {
                row = userDetailService.update(userDetail);

            }else{
                userDetail.setId(idGenerator.id());
                userDetail.setCreateDate(new Date());
                userDetail.setDeleteStatus((short)0);
                row = userDetailService.save(userDetail);
            }

            //发送消息通知用户调度中心更新es缓存
            if(row>=1)
            {
                UserDetailModifyMessage userDetailModifyMessage = new UserDetailModifyMessage();
                BeanUtils.copyProperties(userDetailModifyMessage,userDetail);

                kafkaTemplate.send(UserMessageTopicConstant.user_detail_modify.name(),JSONObject.toJSONString(userDetailModifyMessage));
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




}
