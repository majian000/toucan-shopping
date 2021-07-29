package com.toucan.shopping.modules.auth;

import java.lang.annotation.*;

/**
 * 签名验证
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Documented
@Inherited
public @interface Sign {

	/*
	 * 请求类型 0 json 1 form
	 */
	static int REQUEST_JSON =0;

	/**
	 * 签名类型
	 */
	int requestType() default REQUEST_JSON;
	
}
