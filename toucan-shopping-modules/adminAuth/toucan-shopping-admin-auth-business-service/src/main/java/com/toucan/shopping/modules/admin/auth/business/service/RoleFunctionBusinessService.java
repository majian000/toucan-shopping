package com.toucan.shopping.modules.admin.auth.business.service;


import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.modules.admin.auth.cache.service.RoleFunctionCacheService;
import com.toucan.shopping.modules.admin.auth.entity.AdminRole;
import com.toucan.shopping.modules.admin.auth.entity.Function;
import com.toucan.shopping.modules.admin.auth.entity.Role;
import com.toucan.shopping.modules.admin.auth.entity.RoleFunction;
import com.toucan.shopping.modules.admin.auth.helper.AdminAuthCacheHelper;
import com.toucan.shopping.modules.admin.auth.page.RoleFunctionPageInfo;
import com.toucan.shopping.modules.admin.auth.page.RolePageInfo;
import com.toucan.shopping.modules.admin.auth.service.AdminRoleService;
import com.toucan.shopping.modules.admin.auth.service.FunctionService;
import com.toucan.shopping.modules.admin.auth.service.RoleFunctionService;
import com.toucan.shopping.modules.admin.auth.service.RoleService;
import com.toucan.shopping.modules.admin.auth.vo.*;
import com.toucan.shopping.modules.common.annotation.RequestCheck;
import com.toucan.shopping.modules.common.exception.BusinessValidationException;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.util.Check;
import com.toucan.shopping.modules.common.util.GlobalUUID;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 角色功能项管理
 */
@Service
public class RoleFunctionBusinessService {


    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private RoleService roleService;

    @Autowired
    private AdminRoleService adminRoleService;

    @Autowired
    private RoleFunctionService roleFunctionService;

    @Autowired
    private FunctionService functionService;


    /**
     * 查询指定角色的所有功能项
     *
     * @param requestJsonVO
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryRoleFunctionList(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            RoleFunction query = JSONObject.parseObject(requestJsonVO.getEntityJson(), RoleFunction.class);

            Check.notEmpty(query.getRoleId(), ResultVO.FAILD, "roleId为空");

            List<RoleFunction> roleFunctions = roleFunctionService.findListByEntity(query);
            if (!CollectionUtils.isEmpty(roleFunctions)) {
                resultObjectVO.setData(roleFunctions);
            }

        } catch (BusinessValidationException e) {
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 保存角色功能项
     *
     * @param requestJsonVO
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO saveFunctions(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            RoleFunctionVO entity = JSONObject.parseObject(requestJsonVO.getEntityJson(), RoleFunctionVO.class);
            Check.notEmpty(entity.getRoleId(), ResultVO.FAILD, "roleId为空");
            // 前端传什么就存什么，级联由前端全量树处理
            List<FunctionTreeVO> functionTreeVOS = entity.getFunctions();
            if (functionTreeVOS == null) {
                functionTreeVOS = new LinkedList<>();
            }
            // 先清空旧关联，再保存新提交的
            roleFunctionService.deleteByRoleId(entity.getRoleId());
            if (!CollectionUtils.isEmpty(functionTreeVOS)) {

                RoleFunction[] roleFunctions = new RoleFunction[functionTreeVOS.size()];
                int pos = 0;
                for (Function function : functionTreeVOS) {
                    RoleFunction roleFunction = new RoleFunction();
                    roleFunction.setRoleId(entity.getRoleId());
                    roleFunction.setFunctionId(function.getFunctionId());
                    roleFunction.setAppCode(entity.getAppCode());
                    roleFunction.setCreateAdminId(entity.getCreateAdminId());
                    roleFunction.setCreateDate(new Date());
                    roleFunction.setDeleteStatus((short) 0);

                    roleFunctions[pos] = roleFunction;
                    pos++;
                }
                roleFunctionService.saves(roleFunctions);

                try {
                    RoleFunctionCacheService roleFunctionCacheService = AdminAuthCacheHelper.getRoleFunctionCacheService();
                    if (roleFunctionCacheService != null) {
                        //先清空所有缓存,让权限校验的时候第一次从缓存中没有找到之后初始化,在某种意义上降低数据不一致性的风险
                        roleFunctionCacheService.deleteIndex();
                        //刷新到es缓存
                        if (roleFunctions != null && roleFunctions.length > 0) {
                            RoleFunctionCacheVO[] roleFunctionCacheVOS = new RoleFunctionCacheVO[roleFunctions.length];
                            for (int i = 0; i < roleFunctions.length; i++) {
                                RoleFunction roleFunction = roleFunctions[i];
                                RoleFunctionCacheVO roleFunctionCacheVO = new RoleFunctionCacheVO();
                                if (roleFunction != null) {
                                    BeanUtils.copyProperties(roleFunctionCacheVO, roleFunction);
                                }
                                roleFunctionCacheVOS[i] = roleFunctionCacheVO;
                            }
                            roleFunctionCacheService.saves(roleFunctionCacheVOS);
                        }
                    }

                } catch (Exception e) {
                    resultObjectVO.setCode(ResultVO.SUCCESS);
                    resultObjectVO.setMsg("更新缓存出现异常");
                    logger.warn(e.getMessage(), e);
                }
            }

        } catch (BusinessValidationException e) {
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 刷新缓存
     *
     * @param requestJsonVO
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO refreshCache(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            RoleFunctionVO entity = JSONObject.parseObject(requestJsonVO.getEntityJson(), RoleFunctionVO.class);
            Check.notEmpty(entity.getRoleId(), ResultVO.FAILD, "roleId为空");
            List<RoleFunction> roleFunctions = roleFunctionService.queryListByRoleId(entity.getRoleId());

            RoleFunctionCacheService roleFunctionCacheService = AdminAuthCacheHelper.getRoleFunctionCacheService();
            if (roleFunctionCacheService != null) {
                roleFunctionCacheService.deleteIndex();
                //刷新到缓存
                if (roleFunctions != null && roleFunctions.size() > 0) {
                    RoleFunctionCacheVO[] roleFunctionCacheVOS = new RoleFunctionCacheVO[roleFunctions.size()];
                    for (int i = 0; i < roleFunctions.size(); i++) {
                        RoleFunction roleFunction = roleFunctions.get(i);
                        RoleFunctionCacheVO roleFunctionCacheVO = new RoleFunctionCacheVO();
                        if (roleFunction != null) {
                            BeanUtils.copyProperties(roleFunctionCacheVO, roleFunction);
                        }
                        roleFunctionCacheVOS[i] = roleFunctionCacheVO;
                    }
                    roleFunctionCacheService.saves(roleFunctionCacheVOS);
                }
            }


        } catch (BusinessValidationException e) {
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            resultObjectVO.setCode(ResultVO.SUCCESS);
            resultObjectVO.setMsg("更新缓存出现异常");
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    /**
     * 查询列表分页
     *
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO list(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            RoleFunctionPageInfo queryPageInfo = JSONObject.parseObject(requestVo.getEntityJson(), RoleFunctionPageInfo.class);


            //查询角色 功能项关联
            PageInfo<RoleFunction> pageInfo = roleFunctionService.queryListPage(queryPageInfo);
            resultObjectVO.setData(pageInfo);

        } catch (BusinessValidationException e) {
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryFunctionTreeByRoleIdAndParentId(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            RoleFunctionVO query = requestVo.formatEntity(RoleFunctionVO.class);

            Check.notEmpty(query.getRoleId(), ResultVO.FAILD, "roleId为空");
            Check.notNull(query.getPid(), ResultVO.FAILD, "pid为空");

            //当前角色的所有关联项
            List<RoleFunction> roleFunctions = roleFunctionService.findListByEntity(query);
            //当前节点的子节点
            List<FunctionVO> functionVOS = functionService.queryOneLevelChildrenByIdAndAppCode(query.getPid(), query.getAppCode());

            List<FunctionTreeVO> functionTreeVOS = new LinkedList<>();
            for (FunctionVO functionVO : functionVOS) {
                FunctionTreeVO functionTreeVO = new FunctionTreeVO();
                BeanUtils.copyProperties(functionTreeVO, functionVO);
                functionTreeVOS.add(functionTreeVO);
            }

            // 一次性加载该应用所有功能项，构建 pid→子节点 内存索引
            Set<String> roleFunctionIdSet = new HashSet<>();
            for (RoleFunction rf : roleFunctions) {
                roleFunctionIdSet.add(rf.getFunctionId());
            }
            List<FunctionVO> allFunctions = functionService.queryListByAppCode(query.getAppCode());
            Map<Long, List<FunctionVO>> childrenMap = new HashMap<>();
            for (FunctionVO f : allFunctions) {
                Long pid = f.getPid() != null ? f.getPid() : -1L;
                childrenMap.computeIfAbsent(pid, k -> new ArrayList<>()).add(f);
            }
            // 内存设置 isParent/checked/halfCheck + 统计子孙（不做DB查询）
            for (FunctionTreeVO functionTreeVO : functionTreeVOS) {
                // 设置选中状态
                functionTreeVO.setChecked(roleFunctionIdSet.contains(functionTreeVO.getFunctionId()));
                // 设置父节点状态
                functionTreeVO.setIsParent(childrenMap.containsKey(functionTreeVO.getId()));
                if (functionTreeVO.getIsParent()) {
                    // halfCheck: 是否所有直接子节点都在 roleFunctions 中
                    List<FunctionVO> directChildren = childrenMap.get(functionTreeVO.getId());
                    boolean allChildrenChecked = true;
                    for (FunctionVO child : directChildren) {
                        if (!roleFunctionIdSet.contains(child.getFunctionId())) {
                            allChildrenChecked = false;
                            break;
                        }
                    }
                    functionTreeVO.setHalfCheck(!allChildrenChecked);
                    // 统计子孙
                    int[] counts = countDescendants(functionTreeVO.getId(), childrenMap, roleFunctionIdSet);
                    functionTreeVO.setDescendantCount(counts[0] + 1);
                    functionTreeVO.setCheckedDescendantCount(counts[1] + (roleFunctionIdSet.contains(functionTreeVO.getFunctionId()) ? 1 : 0));
                    functionTreeVO.setCascaded(functionTreeVO.getCheckedDescendantCount().intValue() == functionTreeVO.getDescendantCount().intValue());
                }
            }

            resultObjectVO.setData(functionTreeVOS);
        } catch (BusinessValidationException e) {
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    private int[] countDescendants(Long nodeId, Map<Long, List<FunctionVO>> childrenMap, Set<String> checkedSet) {
        int[] result = new int[]{0, 0};
        List<FunctionVO> children = childrenMap.get(nodeId);
        if (children == null) return result;
        for (FunctionVO child : children) {
            result[0]++;
            if (checkedSet.contains(child.getFunctionId())) result[1]++;
            int[] sub = countDescendants(child.getId(), childrenMap, checkedSet);
            result[0] += sub[0];
            result[1] += sub[1];
        }
        return result;
    }


    /**
     * 查询角色完整功能树（含所有层级的 children 嵌套）
     */
    public ResultObjectVO queryRoleFunctionFullTree(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            RoleFunctionVO query = requestVo.formatEntity(RoleFunctionVO.class);
            Check.notEmpty(query.getRoleId(), ResultVO.FAILD, "roleId为空");
            Check.notEmpty(query.getAppCode(), ResultVO.FAILD, "appCode为空");

            Set<String> roleFunctionIdSet = new HashSet<>();
            List<RoleFunction> roleFunctions = roleFunctionService.findListByEntity(query);
            for (RoleFunction rf : roleFunctions) {
                roleFunctionIdSet.add(rf.getFunctionId());
            }

            List<FunctionVO> allFunctions = functionService.queryListByAppCode(query.getAppCode());
            Map<Long, List<FunctionVO>> childrenMap = new HashMap<>();
            for (FunctionVO f : allFunctions) {
                Long pid = f.getPid() != null ? f.getPid() : -1L;
                childrenMap.computeIfAbsent(pid, k -> new ArrayList<>()).add(f);
            }

            // 递归构建精简树，只返回前端需要的字段
            List<RoleFunctionTreeVO> result = buildFullTree(-1L, childrenMap, roleFunctionIdSet);
            resultObjectVO.setData(result);
        } catch (BusinessValidationException e) {
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    private List<RoleFunctionTreeVO> buildFullTree(Long pid, Map<Long, List<FunctionVO>> childrenMap, Set<String> checkedSet) {
        List<RoleFunctionTreeVO> result = new ArrayList<>();
        List<FunctionVO> children = childrenMap.get(pid);
        if (children == null) return result;
        for (FunctionVO vo : children) {
            RoleFunctionTreeVO node = new RoleFunctionTreeVO();
            node.setId(vo.getId());
            node.setFunctionId(vo.getFunctionId());
            node.setPid(vo.getPid());
            node.setName(vo.getName());
            node.setType(vo.getType() != null ? vo.getType().intValue() : null);
            boolean isParent = childrenMap.containsKey(vo.getId());
            node.setIsParent(isParent);
            node.setChecked(checkedSet.contains(vo.getFunctionId()));
            if (isParent) {
                node.setChildren(buildFullTree(vo.getId(), childrenMap, checkedSet));
                int[] counts = countDescendants(vo.getId(), childrenMap, checkedSet);
                node.setDescendantCount(counts[0] + 1);
                node.setCheckedDescendantCount(counts[1] + (checkedSet.contains(vo.getFunctionId()) ? 1 : 0));
                node.setCascaded(node.getCheckedDescendantCount().intValue() == node.getDescendantCount().intValue());
            }
            result.add(node);
        }
        return result;
    }

}
