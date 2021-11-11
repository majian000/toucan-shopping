package com.toucan.shopping.modules.exception.user;

/**
 * 用户请求头异常
 */
public class UserHeaderException extends RuntimeException {

    public UserHeaderException(String message) {
        super(message);
    }
}
