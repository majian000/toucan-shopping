package com.toucan.shopping.modules.product.service.impl;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.product.entity.AttributeKey;
import com.toucan.shopping.modules.product.mapper.AttributeKeyMapper;
import com.toucan.shopping.modules.product.mapper.AttributeValueMapper;
import com.toucan.shopping.modules.product.page.AttributeKeyPageInfo;
import com.toucan.shopping.modules.product.service.AttributeKeyService;
import com.toucan.shopping.modules.product.vo.AttributeKeyVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AttributeKeyServiceImpl implements AttributeKeyService {


    @Autowired
    private AttributeKeyMapper attributeKeyMapper;

    @Autowired
    private AttributeValueMapper attributeValueMapper;


    @Override
    public PageInfo<AttributeKeyVO> queryListPage(AttributeKeyPageInfo queryPageInfo) {
        queryPageInfo.setStart(queryPageInfo.getPage()*queryPageInfo.getLimit()-queryPageInfo.getLimit());
        PageInfo<AttributeKeyVO> pageInfo = new PageInfo();
        pageInfo.setList(attributeKeyMapper.queryListPage(queryPageInfo));
        pageInfo.setTotal(attributeKeyMapper.queryListPageCount(queryPageInfo));
        return pageInfo;
    }


    @Override
    public PageInfo<AttributeKeyVO> queryListPageBySortDesc(AttributeKeyPageInfo queryPageInfo) {
        queryPageInfo.setStart(queryPageInfo.getPage()*queryPageInfo.getLimit()-queryPageInfo.getLimit());
        PageInfo<AttributeKeyVO> pageInfo = new PageInfo();
        pageInfo.setList(attributeKeyMapper.queryListPageBySortDesc(queryPageInfo));
        pageInfo.setTotal(attributeKeyMapper.queryListPageBySortDescCount(queryPageInfo));
        return pageInfo;
    }

    @Override
    public Long exists(AttributeKeyVO entity) {
        return attributeKeyMapper.exists(entity);
    }


    @Override
    public int save(AttributeKey attributeKey) {
        return attributeKeyMapper.insert(attributeKey);
    }

    @Override
    public int update(AttributeKey attributeKey) {
        return attributeKeyMapper.update(attributeKey);
    }

    @Override
    public int deleteById(Long id) {
        return attributeKeyMapper.deleteById(id);
    }

    @Override
    public List<AttributeKeyVO> queryList(AttributeKeyVO query) {
        return attributeKeyMapper.queryList(query);
    }

    @Override
    public void queryChildList(List<AttributeKeyVO> allChildList,Long parentId) {
        List<AttributeKeyVO> childList = attributeKeyMapper.queryListByParentId(parentId);
        if(CollectionUtils.isNotEmpty(childList))
        {
            allChildList.addAll(childList);
            for(AttributeKeyVO attributeKeyVO:childList) {
                this.queryChildList(allChildList,attributeKeyVO.getId());
            }
        }
    }

    @Override
    public AttributeKeyVO queryById(Long id) {
        return attributeKeyMapper.queryById(id);
    }

    @Override
    public List<AttributeKeyVO> queryListBySortDesc(AttributeKeyVO query) {
        return attributeKeyMapper.queryListBySortDesc(query);
    }

    @Override
    public Long queryCount(AttributeKeyVO attributeKeyVO) {
        return attributeKeyMapper.queryCount(attributeKeyVO);
    }



    public void setChildren(List<AttributeKeyVO> attributeKeyVOS, AttributeKeyVO currentNode) throws InvocationTargetException, IllegalAccessException {
        for (AttributeKeyVO attributeKeyVO : attributeKeyVOS) {
            //为当前参数的子节点
            if(attributeKeyVO.getParentId().longValue()==currentNode.getId().longValue())
            {
                AttributeKeyVO treeVO = new AttributeKeyVO();
                BeanUtils.copyProperties(treeVO, attributeKeyVO);
                treeVO.setTitle(attributeKeyVO.getAttributeName());
                treeVO.setText(attributeKeyVO.getAttributeName());
                treeVO.setPid(attributeKeyVO.getParentId());
                treeVO.setChildren(new ArrayList<AttributeKeyVO>());

                currentNode.getChildren().add(treeVO);

                //查找当前节点的子节点
                setChildren(attributeKeyVOS,treeVO);
            }
        }
    }

    @Override
    public void setChildNodeAndChildNodeValue(AttributeKeyVO currentNode) {
        List<AttributeKeyVO> children = attributeKeyMapper.queryListByParentId(currentNode.getId());
        if(CollectionUtils.isNotEmpty(children))
        {
            currentNode.setChildren(children);
            for(AttributeKeyVO child:children)
            {
                child.setValues(attributeValueMapper.queryListByAttributeKeyIdSortDesc(child.getId()));

                setChildNodeAndChildNodeValue(child);
            }
        }
    }

    @Override
    public void setPath(AttributeKeyVO attributeKeyVO) {
        if(attributeKeyVO.getParentId()!=null&&attributeKeyVO.getParentId().longValue()!=-1)
        {
            AttributeKey parentEntity = this.queryById(attributeKeyVO.getParentId());
            if(parentEntity!=null) {
                attributeKeyVO.setPath(parentEntity.getAttributeName()+"》"+attributeKeyVO.getAttributeName());
                attributeKeyVO.setParentId(parentEntity.getParentId());
                this.setPath(attributeKeyVO);
            }
        }
    }


}
