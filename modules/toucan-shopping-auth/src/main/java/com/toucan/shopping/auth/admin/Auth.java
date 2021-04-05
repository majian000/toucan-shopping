package com.toucan.shopping.auth.admin;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 应用的权限注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Documented
@Inherited
public @interface Auth {

	/**
	 * 校验方式 0本地验证 1权限中心验证
	 */
	public static int VERIFYMETHOD_LOCAL =0;
	public static int VERIFYMETHOD_ADMIN_AUTH =1;

	/**
	 * 请求类型 0 json 1 form
	 */
	public static int REQUEST_JSON =0;
	public static int REQUEST_FORM=1;


	/**
	 * 用户类型
	 */
	public static int USERTYPE_ADMIN =0;
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
	 * 用户类型 0管理员账户 1普通账户
	 * @return
	 */
	public int userType() default USERTYPE_ADMIN;

}
