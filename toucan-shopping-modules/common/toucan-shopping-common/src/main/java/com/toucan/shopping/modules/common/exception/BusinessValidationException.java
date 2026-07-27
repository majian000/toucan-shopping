package com.toucan.shopping.modules.common.exception;

import lombok.Getter;

/**
 * 业务校验异常，由全局异常处理器转换为 ResultObjectVO 响应
 */
@Getter
public class BusinessValidationException extends RuntimeException {

    private final Integer code;

    public BusinessValidationException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
