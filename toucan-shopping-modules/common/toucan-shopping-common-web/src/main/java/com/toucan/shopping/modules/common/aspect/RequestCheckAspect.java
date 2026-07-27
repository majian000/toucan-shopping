package com.toucan.shopping.modules.common.aspect;

import com.toucan.shopping.modules.common.annotation.RequestCheck;
import com.toucan.shopping.modules.common.exception.BusinessValidationException;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 自动校验 RequestJsonVO 框架参数的切面
 *
 * 拦截标注了 @RequestCheck 的方法，校验：
 * 1. RequestJsonVO 不为 null
 * 2. appCode 不为空（可选）
 * 3. entityJson 不为空（可选）
 *
 * 校验失败抛出 BusinessValidationException，由全局异常处理器转换为 ResultObjectVO
 */
@Aspect
@Component
@Order(1)
public class RequestCheckAspect {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Around("@annotation(requestCheck)")
    public Object validate(ProceedingJoinPoint pjp, RequestCheck requestCheck) throws Throwable {
        RequestJsonVO requestJsonVO = findRequestJsonVO(pjp.getArgs());

        if (requestJsonVO == null) {
            logger.info("请求参数为空，拦截返回");
            throw new BusinessValidationException(ResultVO.FAILD, "没有找到请求对象");
        }

        if (requestCheck.requireAppCode()
                && (requestJsonVO.getAppCode() == null || requestJsonVO.getAppCode().isEmpty())) {
            logger.info("没有找到应用编码");
            throw new BusinessValidationException(ResultVO.FAILD, "没有找到应用编码");
        }

        if (requestCheck.requireEntity()
                && (requestJsonVO.getEntityJson() == null || requestJsonVO.getEntityJson().isEmpty())) {
            logger.info("请求数据为空");
            throw new BusinessValidationException(ResultVO.FAILD, "请求数据不能为空");
        }

        return pjp.proceed();
    }

    private RequestJsonVO findRequestJsonVO(Object[] args) {
        for (Object arg : args) {
            if (arg instanceof RequestJsonVO) {
                return (RequestJsonVO) arg;
            }
        }
        return null;
    }
}
