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
    public List<OrgnazitionVO> findListByEntity(Orgnazition entity) {
        return orgnazitionMapper.findListByEntity(entity);
    }


    public void setChildren(List<OrgnazitionVO> orgnazitionVOS,OrgnazitionTreeVO currentNode) throws InvocationTargetException, IllegalAccessException {
        for (OrgnazitionVO OrgnazitionVO : orgnazitionVOS) {
            //为当前参数的子节点
            if(OrgnazitionVO.getPid().longValue()==currentNode.getId().longValue())
            {
                OrgnazitionTreeVO orgnazitionTreeVO = new OrgnazitionTreeVO();
                orgnazitionTreeVO.setTitle(OrgnazitionVO.getName());
                orgnazitionTreeVO.setText(OrgnazitionVO.getName());
                BeanUtils.copyProperties(orgnazitionTreeVO, OrgnazitionVO);
                orgnazitionTreeVO.setChildren(new ArrayList<OrgnazitionTreeVO>());

                currentNode.getChildren().add(orgnazitionTreeVO);

                //查找当前节点的子节点
                setChildren(orgnazitionVOS,orgnazitionTreeVO);
            }
        }
    }


    @Override
    public List<OrgnazitionTreeVO> queryTreeByAppCode(String appCode) {
        List<OrgnazitionTreeVO> orgnazitionTreeVOS = new ArrayList<OrgnazitionTreeVO>();
        try {
            List<OrgnazitionVO> orgnazitionVOS = orgnazitionMapper.queryListByAppCode(appCode);
            for (OrgnazitionVO orgnazitionVO : orgnazitionVOS) {
                if (orgnazitionVO.getPid().longValue() == -1L) {
                    OrgnazitionTreeVO orgnazitionTreeVO = new OrgnazitionTreeVO();
                    orgnazitionTreeVO.setTitle(orgnazitionVO.getName());
                    orgnazitionTreeVO.setText(orgnazitionVO.getName());
                    BeanUtils.copyProperties(orgnazitionTreeVO, orgnazitionVO);
                    orgnazitionTreeVO.setChildren(new ArrayList<OrgnazitionTreeVO>());
                    orgnazitionTreeVOS.add(orgnazitionTreeVO);

                    //递归查找子节点
                    setChildren(orgnazitionVOS,orgnazitionTreeVO);
                }
            }
        }catch (Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return orgnazitionTreeVOS;
    }

    @Override
    public Integer queryMaxCode() {
        return orgnazitionMapper.queryMaxCode();
    }

    @Override
    public List<OrgnazitionTreeVO> queryTree() {
        List<OrgnazitionTreeVO> orgnazitionTreeVOS = new ArrayList<OrgnazitionTreeVO>();
        try {
            List<OrgnazitionVO> orgnazitionVOS = orgnazitionMapper.queryAll();
            for (OrgnazitionVO orgnazitionVO : orgnazitionVOS) {
                if (orgnazitionVO.getPid().longValue() == -1L) {
                    OrgnazitionTreeVO orgnazitionTreeVO = new OrgnazitionTreeVO();
                    orgnazitionTreeVO.setTitle(orgnazitionVO.getName());
                    orgnazitionTreeVO.setText(orgnazitionVO.getName());
                    BeanUtils.copyProperties(orgnazitionTreeVO, orgnazitionVO);
                    orgnazitionTreeVO.setChildren(new ArrayList<OrgnazitionTreeVO>());
                    orgnazitionTreeVOS.add(orgnazitionTreeVO);

                    //递归查找子节点
                    setChildren(orgnazitionVOS,orgnazitionTreeVO);
                }
            }
        }catch (Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return orgnazitionTreeVOS;
    }

    @Override
    public List<OrgnazitionTreeVO> queryTree(String appCode) {
        List<OrgnazitionTreeVO> orgnazitionTreeVOS = new ArrayList<OrgnazitionTreeVO>();
        try {
            List<OrgnazitionVO> orgnazitionVOS = orgnazitionMapper.queryListByAppCode(appCode);
            for (OrgnazitionVO orgnazitionVO : orgnazitionVOS) {
                if (orgnazitionVO.getPid().longValue() == -1L) {
                    OrgnazitionTreeVO orgnazitionTreeVO = new OrgnazitionTreeVO();
                    orgnazitionTreeVO.setTitle(orgnazitionVO.getName());
                    orgnazitionTreeVO.setText(orgnazitionVO.getName());
                    BeanUtils.copyProperties(orgnazitionTreeVO, orgnazitionVO);
                    orgnazitionTreeVO.setChildren(new ArrayList<OrgnazitionTreeVO>());
                    orgnazitionTreeVOS.add(orgnazitionTreeVO);

                    //递归查找子节点
                    setChildren(orgnazitionVOS,orgnazitionTreeVO);
                }else if(!existsParentForOrgnazitionVOList(orgnazitionVOS,orgnazitionVO)) //如果没有上级节点,那么该节点默认为根级节点
                {
                    OrgnazitionTreeVO orgnazitionTreeVO = new OrgnazitionTreeVO();
                    orgnazitionTreeVO.setTitle(orgnazitionVO.getName());
                    orgnazitionTreeVO.setText(orgnazitionVO.getName());
                    BeanUtils.copyProperties(orgnazitionTreeVO, orgnazitionVO);
                    orgnazitionTreeVO.setChildren(new ArrayList<OrgnazitionTreeVO>());
                    orgnazitionTreeVOS.add(orgnazitionTreeVO);
                }
            }
        }catch (Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return orgnazitionTreeVOS;
    }

    /**
     * 设置顶级节点ID
     * @param orgnazitionVOS
     */
    public void setTopParentId(List<OrgnazitionVO> orgnazitionVOS)
    {
        for(OrgnazitionVO orgnazitionVO:orgnazitionVOS)
        {
            boolean find=false;
            for(OrgnazitionVO parentOrgnazitionVO:orgnazitionVOS)
            {
                if(orgnazitionVO.getPid().longValue()==parentOrgnazitionVO.getId())
                {
                    find=true;
                }
            }
            if(!find)
            {
                orgnazitionVO.setPid(-1L);
            }
        }
    }


    @Override
    public List<OrgnazitionTreeVO> queryTreeByAppCodeArray(String[] appCodeArray) {
        List<OrgnazitionTreeVO> orgnazitionTreeVOS = new ArrayList<OrgnazitionTreeVO>();
        try {
            List<OrgnazitionVO> orgnazitionVOS = orgnazitionMapper.queryListByAppCodeArray(appCodeArray);
            if(!CollectionUtils.isEmpty(orgnazitionVOS)) {
                //如果当前集合里没有它的父节点,那么它设置为顶级节点,因为传入应用编码,可能会出现父节点与子节点应用编码不一致情况
                setTopParentId(orgnazitionVOS);
                for (OrgnazitionVO orgnazitionVO : orgnazitionVOS) {
                    if (orgnazitionVO.getPid().longValue() == -1L) {
                        OrgnazitionTreeVO orgnazitionTreeVO = new OrgnazitionTreeVO();
                        orgnazitionTreeVO.setTitle(orgnazitionVO.getName());
                        orgnazitionTreeVO.setText(orgnazitionVO.getName());
                        BeanUtils.copyProperties(orgnazitionTreeVO, orgnazitionVO);
                        orgnazitionTreeVO.setChildren(new ArrayList<OrgnazitionTreeVO>());
                        orgnazitionTreeVOS.add(orgnazitionTreeVO);

                        //递归查找子节点
                        setChildren(orgnazitionVOS, orgnazitionTreeVO);
                    }
                }
            }
        }catch (Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return orgnazitionTreeVOS;
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
    public boolean exists(String name) {
        Orgnazition entity = new Orgnazition();
        entity.setName(name);
        entity.setDeleteStatus((short)0);
        List<OrgnazitionVO> list = orgnazitionMapper.findListByEntity(entity);
        if(!CollectionUtils.isEmpty(list))
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
        pageInfo.setLimit(queryPageInfo.getLimit());
        pageInfo.setPage(queryPageInfo.getPage());
        pageInfo.setSize(queryPageInfo.getSize());
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
    public void queryParentNode(List<Orgnazition> retNodes,Orgnazition child,String appCode)
    {
        List<OrgnazitionVO> parentNode = orgnazitionMapper.findByIdAndAppCode(child.getPid(),appCode);
        retNodes.addAll(parentNode);
        if(!CollectionUtils.isEmpty(parentNode))
        {
            //当前节点不是顶级节点并且这个集合里没有它的父节点,那么就去数据库查询出它的父节点
            if(parentNode.get(0).getPid().longValue()!=-1&&!existsParent(retNodes,parentNode.get(0)))
            {
                queryParentNode(retNodes,parentNode.get(0),appCode);
            }
        }else{ //如果父节点和当前节点不属于同一个应用,当前节点设置为顶级节点
            child.setPid(-1L);
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


    public boolean existsParentForOrgnazitionVOList(List<OrgnazitionVO> nodes,Orgnazition node)
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
    public List<Orgnazition> findTreeTable(OrgnazitionTreeInfo orgnazitionTreeInfo){
        List<Orgnazition> retNodes = new ArrayList<Orgnazition>();
        List<Orgnazition> nodes = orgnazitionMapper.findTreeTableByPageInfo(orgnazitionTreeInfo);
        if(!CollectionUtils.isEmpty(nodes))
        {
            retNodes.addAll(nodes);
            for(Orgnazition node:nodes)
            {
                //当前节点不是顶级节点并且这个集合里没有它的父节点,那么就去数据库查询出它的父节点
                if(node.getPid().longValue()!=-1&&!existsParent(retNodes,node))
                {
                    queryParentNode(retNodes,node,orgnazitionTreeInfo.getAppCode());
                }
            }
        }
        return retNodes;
    }



}
