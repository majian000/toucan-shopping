package com.toucan.shopping.modules.admin.auth.service.impl;

import com.toucan.shopping.modules.admin.auth.entity.Orgnazition;
import com.toucan.shopping.modules.admin.auth.mapper.OrgnazitionMapper;
import com.toucan.shopping.modules.admin.auth.page.OrgnazitionTreeInfo;
import com.toucan.shopping.modules.admin.auth.service.OrgnazitionService;
import com.toucan.shopping.modules.admin.auth.vo.OrgnazitionTreeVO;
import com.toucan.shopping.modules.admin.auth.vo.OrgnazitionVO;
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
public class OrgnazitionServiceImpl implements OrgnazitionService {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private OrgnazitionMapper orgnazitionMapper;

    @Override
    public List<Orgnazition> findListByEntity(Orgnazition entity) {
        return orgnazitionMapper.findListByEntity(entity);
    }


    public void setChildren(List<OrgnazitionVO> OrgnazitionVOS,OrgnazitionTreeVO currentNode) throws InvocationTargetException, IllegalAccessException {
        for (OrgnazitionVO OrgnazitionVO : OrgnazitionVOS) {
            //为当前参数的子节点
            if(OrgnazitionVO.getPid().longValue()==currentNode.getId().longValue())
            {
                OrgnazitionTreeVO OrgnazitionTreeVO = new OrgnazitionTreeVO();
                OrgnazitionTreeVO.setTitle(OrgnazitionVO.getName());
                OrgnazitionTreeVO.setText(OrgnazitionVO.getName());
                BeanUtils.copyProperties(OrgnazitionTreeVO, OrgnazitionVO);
                OrgnazitionTreeVO.setChildren(new ArrayList<OrgnazitionTreeVO>());

                currentNode.getChildren().add(OrgnazitionTreeVO);

                //查找当前节点的子节点
                setChildren(OrgnazitionVOS,OrgnazitionTreeVO);
            }
        }
    }


    @Override
    public List<OrgnazitionTreeVO> queryTreeByAppCode(String appCode) {
        List<OrgnazitionTreeVO> OrgnazitionTreeVOS = new ArrayList<OrgnazitionTreeVO>();
        try {
            List<OrgnazitionVO> OrgnazitionVOS = orgnazitionMapper.queryListByAppCode(appCode);
            for (OrgnazitionVO OrgnazitionVO : OrgnazitionVOS) {
                if (OrgnazitionVO.getPid().longValue() == -1L) {
                    OrgnazitionTreeVO OrgnazitionTreeVO = new OrgnazitionTreeVO();
                    OrgnazitionTreeVO.setTitle(OrgnazitionVO.getName());
                    OrgnazitionTreeVO.setText(OrgnazitionVO.getName());
                    BeanUtils.copyProperties(OrgnazitionTreeVO, OrgnazitionVO);
                    OrgnazitionTreeVO.setChildren(new ArrayList<OrgnazitionTreeVO>());
                    OrgnazitionTreeVOS.add(OrgnazitionTreeVO);

                    //递归查找子节点
                    setChildren(OrgnazitionVOS,OrgnazitionTreeVO);
                }
            }
        }catch (Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return OrgnazitionTreeVOS;
    }

    @Override
    public int save(Orgnazition entity) {
        return orgnazitionMapper.insert(entity);
    }

    @Transactional
    @Override
    public int update(Orgnazition entity) {
        return orgnazitionMapper.update(entity);
    }

    @Override
    public void updateChildAppCode(Orgnazition parentEntity) {
        List<OrgnazitionVO> children = orgnazitionMapper.findListByPid(parentEntity.getId());
        if(!CollectionUtils.isEmpty(children)) {
            for (OrgnazitionVO child : children) {
                //child.setAppCode(parentEntity.getAppCode());
                this.update(child);
                updateChildAppCode(child);
            }
        }
    }

    @Override
    public boolean exists(String name) {
        Orgnazition entity = new Orgnazition();
        entity.setName(name);
        entity.setDeleteStatus((short)0);
        List<Orgnazition> users = orgnazitionMapper.findListByEntity(entity);
        if(!CollectionUtils.isEmpty(users))
        {
            return true;
        }
        return false;
    }

    @Override
    public PageInfo<Orgnazition> queryListPage(OrgnazitionTreeInfo queryPageInfo) {
        queryPageInfo.setStart(queryPageInfo.getPage()*queryPageInfo.getLimit()-queryPageInfo.getLimit());
        OrgnazitionTreeInfo pageInfo = new OrgnazitionTreeInfo();
        pageInfo.setList(orgnazitionMapper.queryListPage(queryPageInfo));
        pageInfo.setTotal(orgnazitionMapper.queryListPageCount(queryPageInfo));
        return pageInfo;
    }

    @Override
    public int deleteById(Long id) {
        return orgnazitionMapper.deleteById(id);
    }

    @Override
    public List<OrgnazitionVO> queryListByAppCode(String appCode) {
        return orgnazitionMapper.queryListByAppCode(appCode);
    }

    @Override
    public void queryChildren(List<Orgnazition> children, Orgnazition query) {
        List<OrgnazitionVO> Orgnazitions = orgnazitionMapper.findListByPid(query.getId());
        children.addAll(Orgnazitions);
        for(Orgnazition Orgnazition:Orgnazitions)
        {
            queryChildren(children,Orgnazition);
        }
    }

    /**
     * 查询上级节点
     * @param retNodes 返回的所有节点
     * @param child
     */
    public void queryParentNode(List<Orgnazition> retNodes,Orgnazition child)
    {
        List<OrgnazitionVO> parentNode = orgnazitionMapper.findById(child.getPid());
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

    public boolean existsParent(List<Orgnazition> nodes,Orgnazition node)
    {
        if(!CollectionUtils.isEmpty(nodes))
        {
            for(Orgnazition n:nodes)
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
    public List<Orgnazition> findTreeTable(OrgnazitionTreeInfo OrgnazitionTreeInfo){
        List<Orgnazition> retNodes = new ArrayList<Orgnazition>();
        List<Orgnazition> nodes = orgnazitionMapper.findTreeTableByPageInfo(OrgnazitionTreeInfo);
        if(!CollectionUtils.isEmpty(nodes))
        {
            retNodes.addAll(nodes);
            for(Orgnazition node:nodes)
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



}
