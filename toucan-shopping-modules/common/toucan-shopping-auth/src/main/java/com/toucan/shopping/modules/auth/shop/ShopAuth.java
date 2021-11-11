package com.toucan.shopping.modules.auth.shop;


import java.lang.annotation.*;


/**
 * 店铺权限
 * 1.用户是否实名
 * 2.用户是否有店铺
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Documented
@Inherited
public @interface ShopAuth {

    /**
     * 请求类型 0 json 1 form
     */
    public static int REQUEST_JSON =0;
    public static int REQUEST_FORM=1;

    /**
     * 请求类型 0 json 1 form
     * @return
     */
    int requestType() default REQUEST_FORM;

    /**
     * 用户是否实名
     */
    boolean userRealName() default true;

    /**
     * 是否存在店铺
     */
    boolean existsShop() default true;


}
