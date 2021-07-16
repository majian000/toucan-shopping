package com.toucan.shopping.modules.request.verify.code;

import java.lang.annotation.*;

/**
 * 请求验证码注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Documented
@Inherited
public @interface RequestVerifyCode {

	/**
	 * 请求类型 0 json 1 form
	 */
	public static int REQUEST_JSON =0;
	public static int REQUEST_FORM=1;
	public static int REQUEST_AJAX=2;

	/**
	 * 默认60秒
	 */
	public static int TIME_SECOND = 60;

	/**
	 * 请求次数
	 */
	public static int REQUEST_COUNT = 3;

	/**
	 * 请求类型 0 json 1 form
	 * @return
	 */
	public int requestType() default REQUEST_JSON;


	/**
	 * 时间范围
	 * @return
	 */
	public int timeScope() default TIME_SECOND;

	/**
	 * 请求次数
	 * @return
	 */
	public int requestCount() default REQUEST_COUNT;



}
