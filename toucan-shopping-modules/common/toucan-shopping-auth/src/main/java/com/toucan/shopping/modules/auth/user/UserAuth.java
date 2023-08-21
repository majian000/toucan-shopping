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
	 int VERIFYMETHOD_LOCAL =0;
	 int VERIFYMETHOD_USER_AUTH =1;

	/**
	 * 请求类型 0 json 1 form
	 */
	 int REQUEST_JSON =0;
	 int REQUEST_FORM=1;
	 int REQUEST_AJAX=2;


	/**
	 * 用户类型
	 */
	 int USERTYPE_USER =1;


	/**
	 * 校验方式
	 * @return
	 */
	int verifyMethod() default VERIFYMETHOD_USER_AUTH;



	/**
	 * 登录校验
	 * @return
	 */
	boolean login() default true;


	/**
	 * 请求类型 0 json 1 form
	 * @return
	 */
	int requestType() default REQUEST_JSON;





}
