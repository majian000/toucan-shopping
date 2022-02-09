package com.toucan.shopping.modules.admin.auth.cache.memory.context;

import com.toucan.shopping.modules.admin.auth.vo.AdminRoleCacheVO;
import com.toucan.shopping.modules.admin.auth.vo.FunctionCacheVO;
import com.toucan.shopping.modules.admin.auth.vo.RoleFunctionCacheVO;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存上下文
 *
 */
public class AdminAuthMemoryContext {

    private static AdminAuthMemoryContext adminAuthMemoryContext = null;

    /**
     * 管理员角色
     */
    private ConcurrentHashMap<Long, AdminRoleCacheVO> adminRoleHashMap = new ConcurrentHashMap<>();

    /**
     * 功能项
     */
    private ConcurrentHashMap<Long, FunctionCacheVO> functionHashMap = new ConcurrentHashMap<>();

    /**
     * 角色功能项
     */
    private ConcurrentHashMap<Long, RoleFunctionCacheVO> roleFunctionHashMap = new ConcurrentHashMap<>();

    public ConcurrentHashMap<Long, RoleFunctionCacheVO> getRoleFunctionHashMap() {
        return roleFunctionHashMap;
    }

    public void setRoleFunctionHashMap(ConcurrentHashMap<Long, RoleFunctionCacheVO> roleFunctionHashMap) {
        this.roleFunctionHashMap = roleFunctionHashMap;
    }

    public ConcurrentHashMap<Long, FunctionCacheVO> getFunctionHashMap() {
        return functionHashMap;
    }

    public void setFunctionHashMap(ConcurrentHashMap<Long, FunctionCacheVO> functionHashMap) {
        this.functionHashMap = functionHashMap;
    }

    public ConcurrentHashMap<Long, AdminRoleCacheVO> getAdminRoleHashMap() {
        return adminRoleHashMap;
    }

    public void setAdminRoleHashMap(ConcurrentHashMap<Long, AdminRoleCacheVO> adminRoleHashMap) {
        this.adminRoleHashMap = adminRoleHashMap;
    }

    static{
        adminAuthMemoryContext = new AdminAuthMemoryContext();
    }

    public static AdminAuthMemoryContext instance()
    {
        return adminAuthMemoryContext;
    }

    private AdminAuthMemoryContext()
    {

    }

}
