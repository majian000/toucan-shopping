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
    public List<ShopCategory> queryList(ShopCategoryVO shopCategory) {
        return shopCategoryMapper.queryList(shopCategory);
    }


    @Override
    public List<ShopCategory> queryListOrderByCategorySortAsc(ShopCategoryVO shopCategory) {
        return shopCategoryMapper.queryListOrderByCategorySortAsc(shopCategory);
    }

    @Override
    public List<ShopCategory> queryTop1(ShopCategoryVO shopCategory) {
        return shopCategoryMapper.queryTop1(shopCategory);
    }

    @Override
    public List<ShopCategory> queryBottom1(ShopCategoryVO shopCategory) {
        return shopCategoryMapper.queryBottom1(shopCategory);
    }
    @Override
    public List<ShopCategory> queryPcIndexList(ShopCategoryVO shopCategory) {
        return shopCategoryMapper.queryPcIndexList(shopCategory);
    }


    @Override
    public int save(ShopCategory shopCategory) {
        return shopCategoryMapper.insert(shopCategory);
    }


    @Override
    public int saves(ShopCategory[] shopCategorys) {
        return shopCategoryMapper.inserts(shopCategorys);
    }

    @Override
    public int update(ShopCategory shopCategory) {
        return shopCategoryMapper.update(shopCategory);
    }

    @Override
    public int updateCategorySort(ShopCategory shopCategory) {
        return shopCategoryMapper.updateCategorySort(shopCategory);
    }
    @Override
    public int updateName(ShopCategory shopCategory) {
        return shopCategoryMapper.updateName(shopCategory);
    }

    @Override
    public ShopCategory queryById(Long id) {
        return shopCategoryMapper.queryById(id);
    }

    @Override
    public ShopCategory queryByIdAndUserMainIdAndShopId(Long id, Long userMainId, Long shopId) {
        return shopCategoryMapper.queryByIdAndUserMainIdAndShopId(id,userMainId,shopId);
    }

    @Override
    public ShopCategory queryByIdAndShopId(Long id, Long shopId) {
        return shopCategoryMapper.queryByIdAndShopId(id,shopId);
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
    public Long queryCount(ShopCategory shopCategory) {
        return shopCategoryMapper.queryCount(shopCategory);
    }

    @Override
    public Long queryMaxSort(Long userMainId,Long shopId) {
        return shopCategoryMapper.queryMaxSort(userMainId,shopId);
    }

    public void setChildrenByParentId(ShopCategoryVO shopCategoryVO,List<ShopCategory> shopCategoryList) throws InvocationTargetException, IllegalAccessException {
        if(CollectionUtils.isNotEmpty(shopCategoryList))
        {
            List<ShopCategoryVO> childrenShopCategoryVoList = new ArrayList<ShopCategoryVO>();
            for(ShopCategory ShopCategory:shopCategoryList)
            {
                ShopCategoryVO childShopCategoryVo = new ShopCategoryVO();
                BeanUtils.copyProperties(childShopCategoryVo,ShopCategory);
                if(ShopCategory!=null&&shopCategoryVO.getId().longValue() == ShopCategory.getParentId().longValue()) {
                    childrenShopCategoryVoList.add(childShopCategoryVo);
                    setChildrenByParentId(childShopCategoryVo,shopCategoryList);
                }
            }
            shopCategoryVO.setChildren(childrenShopCategoryVoList);
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





    public void setChildren(List<ShopCategory> shopCategorys, ShopCategoryTreeVO currentNode) throws InvocationTargetException, IllegalAccessException {
        for (ShopCategory ShopCategory : shopCategorys) {
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
                setChildren(shopCategorys,ShopCategoryTreeVO);
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
