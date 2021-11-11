package com.toucan.shopping.modules.exception.user;


/**
 * 没有找到用户ID
 */
public class NotFountUserMainIdException extends RuntimeException {

    public NotFountUserMainIdException(String message) {
        super(message);
    }
}
