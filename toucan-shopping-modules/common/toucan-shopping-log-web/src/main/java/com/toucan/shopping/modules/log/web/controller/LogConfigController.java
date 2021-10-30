package com.toucan.shopping.modules.log.web.controller;

import com.toucan.shopping.modules.common.properties.modules.log.Email;
import com.toucan.shopping.modules.common.properties.modules.log.Log;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.log.appender.LogEmailAppender;
import com.toucan.shopping.modules.log.validate.LogConfigValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/toucan/log/config")
public class LogConfigController {

    private final Logger logger = LoggerFactory.getLogger(getClass());



    @RequestMapping(value="/email/flush",produces = "application/json;charset=UTF-8")
    public ResultObjectVO findByMobilePhone(@RequestBody Log log){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try{
            LogEmailAppender.flushConfig(log);
        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg(e.getMessage());
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }

}
