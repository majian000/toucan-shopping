package com.toucan.shopping.modules.admin.auth.holder;

import com.toucan.shopping.modules.admin.auth.vo.AdminLoginVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdminLoginHolder {

    private static final Logger log = LoggerFactory.getLogger(AdminLoginHolder.class);

     private static final ThreadLocal<AdminLoginVO> CONTEXT = new ThreadLocal<>();


    /**
     * 设置当前线程上下文（登录成功后调用）
     */
    public static void setAdminLoginContext(String adminId, String token) {
        if (adminId == null || token == null || token.isEmpty()) {
            log.warn("尝试设置空的AdminContext，忽略操作");
            return;
        }
        AdminLoginVO adminLoginVO = new AdminLoginVO();
        adminLoginVO.setAdminId(adminId);
        adminLoginVO.setLoginToken(token);
        CONTEXT.set(adminLoginVO);
    }

    /**
     * 直接设置已存在的上下文对象
     */
    public static void setAdminLoginContext(AdminLoginVO context) {
        if (context == null) {
            clear();
            return;
        }
        CONTEXT.set(context);
    }

    /**
     * 获取当前线程上下文
     */
    public static AdminLoginVO getAdminLogin() {
        return CONTEXT.get();
    }

    /**
     * 获取当前登录用户ID（便捷方法）
     */
    public static String getCurrentAdminId() {
        AdminLoginVO context = CONTEXT.get();
        return context != null ? context.getAdminId() : null;
    }

    /**
     * 获取当前登录Token（便捷方法）
     */
    public static String getCurrentToken() {
        AdminLoginVO context = CONTEXT.get();
        return context != null ? context.getLoginToken() : null;
    }

    /**
     * 清除上下文（请求结束后必须调用，防止内存泄漏）
     */
    public static void clear() {
        CONTEXT.remove();
    }
}
