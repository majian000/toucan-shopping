package com.toucan.shopping.modules.common.exception;

import com.toucan.shopping.modules.common.context.ToucanApplicationContext;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalControllerException {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @ExceptionHandler(BusinessValidationException.class)
    @ResponseBody
    public ResultObjectVO handleBusinessValidationException(BusinessValidationException ex) {
        logger.warn("业务校验失败: code={}, msg={}", ex.getCode(), ex.getMessage());
        return new ResultObjectVO(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultObjectVO handleException(Exception ex, HttpServletResponse response) throws Exception {
        // 404 异常交给 Spring 默认错误处理，不在此拦截
        if (ex instanceof NoResourceFoundException || ex instanceof NoHandlerFoundException) {
            throw ex;
        }
        logger.warn(ex.getMessage(), ex);
        if (ex instanceof MaxUploadSizeExceededException) {
            if (StringUtils.isNotEmpty(ToucanApplicationContext.getMaxFileSize())) {
                return new ResultObjectVO(ResultVO.FAILD, "文件大小超过限制,最大" + ToucanApplicationContext.getMaxFileSize());
            }
            return new ResultObjectVO(ResultVO.FAILD, "文件大小超过限制");
        }
        return new ResultObjectVO(ResultVO.FAILD, "操作异常");
    }
}
