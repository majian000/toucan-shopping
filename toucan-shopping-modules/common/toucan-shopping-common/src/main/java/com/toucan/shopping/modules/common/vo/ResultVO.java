package com.toucan.shopping.modules.common.vo;

/**
 * 通用响应状态码
 */
public class ResultVO {

    /** 业务处理成功 */
    public static Integer SUCCESS = 1;

    /** 业务处理失败 */
    public static Integer FAILD = 0;

    /** 重定向 */
    public static Integer HTTPCODE_302 = 302;

    /** 未登录或登录超时 */
    public static Integer HTTPCODE_401 = 401;

    /** 无权限访问 */
    public static Integer HTTPCODE_403 = 403;

    /** 资源不存在 */
    public static Integer HTTPCODE_404 = 404;
}
