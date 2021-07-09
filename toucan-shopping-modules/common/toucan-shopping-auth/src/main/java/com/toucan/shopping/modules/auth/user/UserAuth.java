package com.toucan.shopping.modules.auth.user;

import java.lang.annotation.*;

/**
 * 应用的权限注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Documented
@Inherited
public @interface UserAuth {

	/**
	 * 校验方式 0本地验证 1用户中心验证
	 */
	public static int VERIFYMETHOD_LOCAL =0;
	public static int VERIFYMETHOD_USER_AUTH =1;

	/**
	 * 请求类型 0 json 1 form
	 */
	public static int REQUEST_JSON =0;
	public static int REQUEST_FORM=1;


	/**
	 * 用户类型
	 */
	public static int USERTYPE_USER =1;


	/**
	 * 校验方式
	 * @return
	 */
	public int verifyMethod() default VERIFYMETHOD_LOCAL;



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


	/**
	 * 用户类型 1普通账户
	 * @return
	 */
	public int userType() default USERTYPE_USER;



}
