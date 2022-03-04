package com.toucan.shopping.modules.admin.auth.service.impl;

import com.toucan.shopping.modules.admin.auth.entity.Function;
import com.toucan.shopping.modules.admin.auth.entity.RoleFunction;
import com.toucan.shopping.modules.admin.auth.mapper.FunctionMapper;
import com.toucan.shopping.modules.admin.auth.mapper.RoleFunctionMapper;
import com.toucan.shopping.modules.admin.auth.mapper.RoleFunctionMapper;
import com.toucan.shopping.modules.admin.auth.page.RoleFunctionPageInfo;
import com.toucan.shopping.modules.admin.auth.service.FunctionService;
import com.toucan.shopping.modules.admin.auth.service.RoleFunctionService;
import com.toucan.shopping.modules.admin.auth.service.RoleFunctionService;
import com.toucan.shopping.modules.admin.auth.vo.FunctionTreeVO;
import com.toucan.shopping.modules.admin.auth.vo.FunctionVO;
import com.toucan.shopping.modules.admin.auth.vo.RoleFunctionVO;
import com.toucan.shopping.modules.common.page.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class RoleFunctionServiceImpl implements RoleFunctionService {

    @Autowired
    private RoleFunctionMapper roleFunctionMapper;

    @Autowired
    private FunctionService functionService;


    @Override
    public List<RoleFunction> findListByEntity(RoleFunction entity) {
        return roleFunctionMapper.findListByEntity(entity);
    }

    @Override
    public void setHalfCheckAndIsParent(FunctionTreeVO functionTreeVO, List<RoleFunction> roleFunctions) {
        List<FunctionVO> functionTreeVOS = new LinkedList<>();
        //查询所有子节点
        functionService.queryChildren(functionTreeVOS,functionTreeVO);

        //设置父节点状态
        if(!CollectionUtils.isEmpty(functionTreeVOS))
        {
            functionTreeVO.setIsParent(true);
        }else{
            functionTreeVO.setIsParent(false);
        }

        //设置选中状态
        for(RoleFunction roleFunction:roleFunctions)
        {
            if(roleFunction.getFunctionId().equals(functionTreeVO.getFunctionId()))
            {
                functionTreeVO.setChecked(true);
                break;
            }
        }


        boolean isAllMatch = true; //是否全部关联上了
        boolean isChildFind; //这个子节点是否找到
        for(FunctionVO functionTreeChild:functionTreeVOS)
        {
            isChildFind = false;
            for(RoleFunction roleFunction:roleFunctions)
            {
                if(functionTreeChild.getFunctionId().equals(roleFunction.getFunctionId()))
                {
                    isChildFind = true;
                    break;
                }
            }
            //如果这个子节点没有找到
            if(!isChildFind)
            {
                isAllMatch = false;
                break;
            }
        }

        if(isAllMatch)
        {
            //全选
            functionTreeVO.setHalfCheck(false);
        }else{
            //半选
            functionTreeVO.setHalfCheck(true);
        }
    }

    @Override
    public int save(RoleFunction entity) {
        return roleFunctionMapper.insert(entity);
    }


    @Override
    public int saves(RoleFunction[] entitys) {
        return roleFunctionMapper.inserts(entitys);
    }

    @Override
    public int deleteByRoleId(String roleId) {
        return roleFunctionMapper.deleteByRoleId(roleId);
    }

    @Override
    public int deleteByFunctionId(String functionId) {
        return roleFunctionMapper.deleteByFunctionId(functionId);
    }

    @Override
    public int deleteByFunctionIdArray(String[] functionIdArray) {
        return roleFunctionMapper.deleteByFunctionIdArray(functionIdArray);
    }

    @Override
    public List<RoleFunction> findListByAdminIdAndFunctionUrlAndAppCodeAndRoleIds(String url, String appCode, String[] roleIdArray) {
        return roleFunctionMapper.findListByAdminIdAndFunctionUrlAndAppCodeAndRoleIds(url,appCode,roleIdArray);
    }

    @Override
    public Long findCountByAdminIdAndFunctionUrlAndAppCodeAndRoleIds(String url, String appCode, String[] roleIdArray) {
        return roleFunctionMapper.findCountByAdminIdAndFunctionUrlAndAppCodeAndRoleIds(url,appCode,roleIdArray);
    }

    @Override
    public PageInfo<RoleFunction> queryListPage(RoleFunctionPageInfo queryPageInfo) {
        queryPageInfo.setStart(queryPageInfo.getPage()*queryPageInfo.getLimit()-queryPageInfo.getLimit());
        PageInfo<RoleFunction> pageInfo = new PageInfo();
        pageInfo.setList(roleFunctionMapper.queryListPage(queryPageInfo));
        pageInfo.setTotal(roleFunctionMapper.queryListPageCount(queryPageInfo));
        return pageInfo;
    }

    @Override
    public void queryReleaseFunctionList(RoleFunctionVO roleFunctionVO, List<FunctionTreeVO> functionTreeVOS) {
        List<FunctionTreeVO> selectFunctions = roleFunctionVO.getFunctions();
        if(CollectionUtils.isNotEmpty(selectFunctions))
        {
            //先判断根节点是否被全选
            for(FunctionTreeVO selectFunction:selectFunctions)
            {
                if(selectFunction.getPid()!=null&&selectFunction.getPid().longValue()==-1L)
                {
                    //如果这个根节点被全选,直接查询所有子节点
                    if(selectFunction.getHalfCheck())
                    {
                        functionService.queryChildren(functionTreeVOS,selectFunction);
                        //将这个全选节点增加节点集合中
                        functionTreeVOS.add(selectFunction);
                    }
                }
            }

            //找出没有被全选的节点(半选)
            for(FunctionTreeVO selectFunction:selectFunctions)
            {
                //如果是半选
                if(!selectFunction.getHalfCheck())
                {
                    //将这个半选节点加到节点集合中
                    functionTreeVOS.add(selectFunction);

                    //先查询出这次选择的所有子节点
                    for(FunctionTreeVO selectFunctionChild:selectFunctions)
                    {
                        if(selectFunctionChild.getPid().longValue() == selectFunction.getId().longValue())
                        {
                            //如果这个子节点是全选
                            if(selectFunctionChild.getHalfCheck()) {
                                //将这个子节点的所有子节点放到集合中
                                functionService.queryChildren(functionTreeVOS, selectFunctionChild);
                            }
                        }
                    }

                }
            }
        }
    }


}
