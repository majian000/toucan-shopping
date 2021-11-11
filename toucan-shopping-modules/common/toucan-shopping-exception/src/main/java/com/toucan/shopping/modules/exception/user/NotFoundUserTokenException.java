package com.toucan.shopping.modules.exception.user;

/**
 * 找不到用户会话
 */
public class NotFoundUserTokenException extends RuntimeException {
    public NotFoundUserTokenException(String message) {
        super(message);
    }
}
