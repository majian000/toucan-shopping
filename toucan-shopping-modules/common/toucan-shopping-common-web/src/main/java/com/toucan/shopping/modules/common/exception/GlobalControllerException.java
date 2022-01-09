package com.toucan.shopping.modules.common.exception;

import com.toucan.shopping.modules.common.context.ToucanApplicationContext;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class GlobalControllerException {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultObjectVO handleException(Exception ex)
    {
        logger.warn(ex.getMessage(),ex);
        if(ex instanceof MaxUploadSizeExceededException)
        {
            if(StringUtils.isNotEmpty(ToucanApplicationContext.getMaxFileSize())) {
                return new ResultObjectVO(ResultVO.FAILD, "文件大小超过限制,最大" + ToucanApplicationContext.getMaxFileSize());
            }
            return new ResultObjectVO(ResultVO.FAILD, "文件大小超过限制");
        }
        return new ResultObjectVO(ResultVO.FAILD,"操作异常");
    }
}
