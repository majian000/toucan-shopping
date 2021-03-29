package com.toucan.shopping.auth;

import java.lang.annotation.*;

/**
 * 签名验证
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Documented
@Inherited
public @interface Sign {

	/**
	 * 校验方式 0 JSON签名
	 */

	public static int JSON=0;

	/**
	 * 是否支持
	 * @return
	 */
	public boolean sign() default true;

	/**
	 * 签名类型
	 */
	public int type() default JSON;
	
}
