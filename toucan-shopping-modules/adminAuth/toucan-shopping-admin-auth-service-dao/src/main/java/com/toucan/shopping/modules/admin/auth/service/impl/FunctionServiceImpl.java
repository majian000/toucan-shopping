package com.toucan.shopping.modules.admin.auth.service.impl;

import com.toucan.shopping.modules.admin.auth.entity.Function;
import com.toucan.shopping.modules.admin.auth.mapper.FunctionMapper;
import com.toucan.shopping.modules.admin.auth.page.FunctionTreeInfo;
import com.toucan.shopping.modules.admin.auth.service.FunctionService;
import com.toucan.shopping.modules.admin.auth.vo.FunctionTreeVO;
import com.toucan.shopping.modules.admin.auth.vo.FunctionVO;
import com.toucan.shopping.modules.common.page.PageInfo;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FunctionServiceImpl implements FunctionService {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private FunctionMapper functionMapper;

    @Override
    public List<Function> findListByEntity(Function entity) {
        return functionMapper.findListByEntity(entity);
    }

    @Override
    public List<Function> findListByEntityFieldLike(Function entity) {
        return functionMapper.findListByEntityFieldLike(entity);
    }

    public void setChildren(List<FunctionVO> functionVOS,FunctionTreeVO currentNode) throws InvocationTargetException, IllegalAccessException {
        for (FunctionVO functionVO : functionVOS) {
            //为当前参数的子节点
            if(functionVO.getPid().longValue()==currentNode.getId().longValue())
            {
                FunctionTreeVO functionTreeVO = new FunctionTreeVO();
                functionTreeVO.setTitle(functionVO.getName());
                functionTreeVO.setText(functionVO.getName());
                BeanUtils.copyProperties(functionTreeVO, functionVO);
                functionTreeVO.setChildren(new ArrayList<FunctionTreeVO>());

                currentNode.getChildren().add(functionTreeVO);

                //查找当前节点的子节点
                setChildren(functionVOS,functionTreeVO);
            }
        }
    }


    @Override
    public List<FunctionTreeVO> queryTreeByAppCode(String appCode) {
        List<FunctionTreeVO> functionTreeVOS = new ArrayList<FunctionTreeVO>();
        try {
            List<FunctionVO> functionVOS = functionMapper.queryListByAppCode(appCode);
            for (FunctionVO functionVO : functionVOS) {
                if (functionVO.getPid().longValue() == -1L) {
                    FunctionTreeVO functionTreeVO = new FunctionTreeVO();
                    functionTreeVO.setTitle(functionVO.getName());
                    functionTreeVO.setText(functionVO.getName());
                    BeanUtils.copyProperties(functionTreeVO, functionVO);
                    functionTreeVO.setChildren(new ArrayList<FunctionTreeVO>());
                    functionTreeVOS.add(functionTreeVO);

                    //递归查找子节点
                    setChildren(functionVOS,functionTreeVO);
                }
            }
        }catch (Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return functionTreeVOS;
    }

    @Override
    public int save(Function entity) {
        return functionMapper.insert(entity);
    }

    @Transactional
    @Override
    public int update(Function entity) {
        return functionMapper.update(entity);
    }

    @Override
    public void updateChildAppCode(Function parentEntity) {
        List<FunctionVO> children = functionMapper.findListByPid(parentEntity.getId());
        if(!CollectionUtils.isEmpty(children)) {
            for (FunctionVO child : children) {
                child.setAppCode(parentEntity.getAppCode());
                this.update(child);
                updateChildAppCode(child);
            }
        }
    }

    @Override
    public boolean exists(String name) {
        Function entity = new Function();
        entity.setName(name);
        entity.setDeleteStatus((short)0);
        List<Function> users = functionMapper.findListByEntity(entity);
        if(!CollectionUtils.isEmpty(users))
        {
            return true;
        }
        return false;
    }

    @Override
    public PageInfo<Function> queryListPage(FunctionTreeInfo queryPageInfo) {
        queryPageInfo.setStart(queryPageInfo.getPage()*queryPageInfo.getLimit()-queryPageInfo.getLimit());
        FunctionTreeInfo pageInfo = new FunctionTreeInfo();
        pageInfo.setList(functionMapper.queryListPage(queryPageInfo));
        pageInfo.setTotal(functionMapper.queryListPageCount(queryPageInfo));
        return pageInfo;
    }

    @Override
    public int deleteById(Long id) {
        return functionMapper.deleteById(id);
    }

    @Override
    public List<FunctionVO> queryListByAppCode(String appCode) {
        return functionMapper.queryListByAppCode(appCode);
    }

    @Override
    public void queryChildren(List children, Function query) {
        List<FunctionVO> functions = functionMapper.findListByPid(query.getId());
        children.addAll(functions);
        for(Function function:functions)
        {
            queryChildren(children,function);
        }
    }

    @Override
    public List queryOneLevelChildren(Function query) {
        return functionMapper.findListByPid(query.getId());
    }
    /**
     * 查询上级节点
     * @param retNodes 返回的所有节点
     * @param child
     */
    public void queryParentNode(List<Function> retNodes,Function child)
    {
        List<FunctionVO> parentNode = functionMapper.findById(child.getPid());
        retNodes.addAll(parentNode);
        if(!CollectionUtils.isEmpty(parentNode))
        {
            //当前节点不是顶级节点并且这个集合里没有它的父节点,那么就去数据库查询出它的父节点
            if(parentNode.get(0).getPid().longValue()!=-1&&!existsParent(retNodes,parentNode.get(0)))
            {
                queryParentNode(retNodes,parentNode.get(0));
            }
        }
    }

    public boolean existsParent(List<Function> nodes,Function node)
    {
        if(!CollectionUtils.isEmpty(nodes))
        {
            for(Function n:nodes)
            {
                if(node.getPid().longValue()==n.getId().longValue())
                {
                    return true;
                }
            }

        }
        return false;
    }

    @Override
    public List<Function> findTreeTable(FunctionTreeInfo functionTreeInfo){
        List<Function> retNodes = new ArrayList<Function>();
        List<Function> nodes = functionMapper.findTreeTableByPageInfo(functionTreeInfo);
        if(!CollectionUtils.isEmpty(nodes))
        {
            retNodes.addAll(nodes);
            for(Function node:nodes)
            {
                //当前节点不是顶级节点并且这个集合里没有它的父节点,那么就去数据库查询出它的父节点
                if(node.getPid().longValue()!=-1&&!existsParent(retNodes,node))
                {
                    queryParentNode(retNodes,node);
                }
            }

        }
        return retNodes;
    }

    @Override
    public List<FunctionVO> queryListByRoleIdArray(String[] roleIds) {
        return functionMapper.queryListByRoleIdArray(roleIds);
    }

    @Override
    public List<FunctionVO> queryListByRoleIdArrayAndParentId(String[] roleIds,String parentId) {
        return functionMapper.queryListByRoleIdArrayAndParentId(roleIds,parentId);
    }

}
