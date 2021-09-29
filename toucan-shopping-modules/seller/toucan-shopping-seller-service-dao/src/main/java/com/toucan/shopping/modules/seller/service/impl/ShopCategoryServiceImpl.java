package com.toucan.shopping.modules.seller.service.impl;

import com.toucan.shopping.modules.seller.entity.ShopCategory;
import com.toucan.shopping.modules.seller.mapper.ShopCategoryMapper;
import com.toucan.shopping.modules.seller.page.ShopCategoryTreeInfo;
import com.toucan.shopping.modules.seller.service.ShopCategoryService;
import com.toucan.shopping.modules.seller.vo.ShopCategoryTreeVO;
import com.toucan.shopping.modules.seller.vo.ShopCategoryVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShopCategoryServiceImpl implements ShopCategoryService {

    @Autowired
    private ShopCategoryMapper shopCategoryMapper;


    @Override
    public List<ShopCategory> queryList(ShopCategoryVO ShopCategory) {
        return shopCategoryMapper.queryList(ShopCategory);
    }

    @Override
    public List<ShopCategory> queryPcIndexList(ShopCategoryVO ShopCategory) {
        return shopCategoryMapper.queryPcIndexList(ShopCategory);
    }


    @Override
    public int save(ShopCategory ShopCategory) {
        return shopCategoryMapper.insert(ShopCategory);
    }


    @Override
    public int saves(ShopCategory[] ShopCategorys) {
        return shopCategoryMapper.inserts(ShopCategorys);
    }

    @Override
    public int update(ShopCategory ShopCategory) {
        return shopCategoryMapper.update(ShopCategory);
    }

    @Override
    public int updateName(ShopCategory ShopCategory) {
        return shopCategoryMapper.updateName(ShopCategory);
    }

    @Override
    public ShopCategory queryById(Long id) {
        return shopCategoryMapper.queryById(id);
    }

    @Transactional
    @Override
    public int deleteById(Long id) {
        return shopCategoryMapper.deleteById(id);
    }

    @Override
    public List<ShopCategory> findByParentId(Long parentId) {
        return shopCategoryMapper.queryByParentId(parentId);
    }

    @Override
    public Long queryCount(ShopCategory ShopCategory) {
        return shopCategoryMapper.queryCount(ShopCategory);
    }

    @Override
    public Long queryMaxSort(Long userMainId,Long shopId) {
        return shopCategoryMapper.queryMaxSort(userMainId,shopId);
    }

    public void setChildrenByParentId(ShopCategoryVO ShopCategoryVO,List<ShopCategory> ShopCategoryList) throws InvocationTargetException, IllegalAccessException {
        if(CollectionUtils.isNotEmpty(ShopCategoryList))
        {
            List<ShopCategoryVO> childrenShopCategoryVoList = new ArrayList<ShopCategoryVO>();
            for(ShopCategory ShopCategory:ShopCategoryList)
            {
                ShopCategoryVO childShopCategoryVo = new ShopCategoryVO();
                BeanUtils.copyProperties(childShopCategoryVo,ShopCategory);
                if(ShopCategory!=null&&ShopCategoryVO.getId().longValue() == ShopCategory.getParentId().longValue()) {
                    childrenShopCategoryVoList.add(childShopCategoryVo);
                    setChildrenByParentId(childShopCategoryVo,ShopCategoryList);
                }
            }
            ShopCategoryVO.setChildren(childrenShopCategoryVoList);
        }
    }

    @Override
    public void deleteChildrenByParentId(Long id) {
        List<ShopCategory> ShopCategoryList = this.findByParentId(id);
        if(CollectionUtils.isNotEmpty(ShopCategoryList))
        {
            for(ShopCategory shopCategory:ShopCategoryList)
            {
                if(shopCategory!=null&&shopCategory.getId()!=null) {
                    deleteChildrenByParentId(shopCategory.getId());
                    this.deleteById(shopCategory.getId());
                }
            }
        }
    }





    public void setChildren(List<ShopCategory> ShopCategorys, ShopCategoryTreeVO currentNode) throws InvocationTargetException, IllegalAccessException {
        for (ShopCategory ShopCategory : ShopCategorys) {
            //为当前参数的子节点
            if(ShopCategory.getParentId().longValue()==currentNode.getId().longValue())
            {
                ShopCategoryTreeVO ShopCategoryTreeVO = new ShopCategoryTreeVO();
                BeanUtils.copyProperties(ShopCategoryTreeVO, ShopCategory);
                ShopCategoryTreeVO.setTitle(ShopCategory.getName());
                ShopCategoryTreeVO.setText(ShopCategory.getName());
                ShopCategoryTreeVO.setChildren(new ArrayList<ShopCategoryVO>());

                currentNode.getChildren().add(ShopCategoryTreeVO);

                //查找当前节点的子节点
                setChildren(ShopCategorys,ShopCategoryTreeVO);
            }
        }
    }




    @Override
    public List<ShopCategoryVO> findTreeTable(ShopCategoryTreeInfo queryTreeInfo) {
        return shopCategoryMapper.findTreeTableByPageInfo(queryTreeInfo);
    }


    @Override
    public void queryChildren(List<ShopCategory> children, ShopCategory query) {
        List<ShopCategory> ShopCategoryList = shopCategoryMapper.queryByParentId(query.getId());
        children.addAll(ShopCategoryList);
        for(ShopCategory ShopCategory:ShopCategoryList)
        {
            queryChildren(children,ShopCategory);
        }
    }


}
