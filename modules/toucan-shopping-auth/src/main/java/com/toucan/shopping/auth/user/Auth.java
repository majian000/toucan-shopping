package com.toucan.shopping.auth.user;

import java.lang.annotation.*;

/**
 * 应用的权限注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Documented
@Inherited
public @interface Auth {


	/**
	 * 校验方式 0本地验证 1用户中心验证
	 */
	public static int VERIFYMETHOD_LOCAL =0;
	public static int VERIFYMETHOD_USER_CENTER =1;


	/**
	 * 请求类型 0 json 1 form
	 */
	public static int REQUEST_JSON =0;
	public static int REQUEST_FORM=1;


	/**
	 * 校验方式
	 * @return
	 */
	public int verifyMethod() default VERIFYMETHOD_USER_CENTER;

	/**
	 * 登录校验
	 * @return
	 */
	public boolean login() default true;

	/**
	 * 请求类型 0 json 1 form
	 * @return
	 */
	public int requestType() default REQUEST_JSON;



}
