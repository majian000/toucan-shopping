package com.toucan.shopping.modules.auth.admin;

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
public @interface AdminAuth {

	/**
	 * 校验方式 0本地验证 1权限中台验证
	 */
	 int VERIFYMETHOD_LOCAL =0;
	 int VERIFYMETHOD_ADMIN_AUTH =1;

	/**
	 * 请求类型 0 json 1 form
	 */
	 int REQUEST_JSON =0;
	 int REQUEST_FORM=1;

	/**
	 * 响应类型 0 json 1 form
	 */
	 int RESPONSE_JSON =0;
	 int RESPONSE_FORM=1;


	/**
	 * 校验类型
	 * 1:URL或权限标识任意通过(OR关系)
	 * 2:仅URL校验(默认)
	 * 3:仅权限标识校验
	 */
	int VERIFY_TYPE_ANY=1;
	int VERIFY_TYPE_URL=2;
	int VERIFY_TYPE_PERMISSION=3;

	/**
	 * 用户类型
	 */
	 static int USERTYPE_ADMIN =0;


	/**
	 * 校验方式
	 * @return
	 */
	 int verifyMethod() default VERIFYMETHOD_LOCAL;

	/**
	 * 权限标识
	 * @return
	 */
	String[] permissions() default {};

	/**
	 * 校验类型
	 * @return
	 */
	int verifyType() default VERIFY_TYPE_URL;

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

	/**
	 * 响应类型 0 json 1 form
	 * @return
	 */
	 int responseType() default RESPONSE_JSON;

	/**
	 * 用户类型 0管理员账户 1普通账户
	 * @return
	 */
	 int userType() default USERTYPE_ADMIN;

}
