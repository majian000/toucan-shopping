package com.toucan.shopping.modules.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记方法自动校验 RequestJsonVO 的完整性
 *
 * 校验逻辑：requestJsonVO != null、appCode 不为空、entityJson 不为空（可选）
 * 校验失败抛出 BusinessValidationException，由全局异常处理器转换为统一响应格式
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestCheck {

    /** 是否需要校验 appCode 不为空，默认需要 */
    boolean requireAppCode() default true;

    /** 是否需要校验 entityJson 不为空，默认不需要（查询类接口可以不传 entityJson） */
    boolean requireEntity() default false;
}
