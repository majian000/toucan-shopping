package com.toucan.shopping.modules.product.service.impl;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.product.entity.AttributeValue;
import com.toucan.shopping.modules.product.entity.ProductSpuAttributeValue;
import com.toucan.shopping.modules.product.mapper.AttributeValueMapper;
import com.toucan.shopping.modules.product.mapper.ProductSpuAttributeValueMapper;
import com.toucan.shopping.modules.product.page.AttributeValuePageInfo;
import com.toucan.shopping.modules.product.service.AttributeValueService;
import com.toucan.shopping.modules.product.service.ProductSpuAttributeValueService;
import com.toucan.shopping.modules.product.vo.AttributeValueVO;
import com.toucan.shopping.modules.product.vo.ProductSpuAttributeKeyValueTreeVO;
import com.toucan.shopping.modules.product.vo.ProductSpuAttributeKeyValueVO;
import com.toucan.shopping.modules.product.vo.ProductSpuAttributeValueVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class ProductSpuAttributeValueServiceImpl implements ProductSpuAttributeValueService {


    @Autowired
    private ProductSpuAttributeValueMapper attributeValueMapper;


    @Override
    public int save(ProductSpuAttributeValue entity) {
        return attributeValueMapper.insert(entity);
    }

    @Override
    public int saves(List<ProductSpuAttributeValue> entitys) {
        return attributeValueMapper.inserts(entitys);
    }

    @Override
    public int deleteByProductSpuId(Long productSpuId) {
        return attributeValueMapper.deleteByProductSpuId(productSpuId);
    }

    @Override
    public List<ProductSpuAttributeValueVO> queryListBySortDesc(ProductSpuAttributeValueVO query) {
        return attributeValueMapper.queryListBySortDesc(query);
    }

    @Override
    public void queryAttributeTree(List<ProductSpuAttributeKeyValueVO> allList, ProductSpuAttributeKeyValueTreeVO parentAttributeTree) {
        for(int i=0;i<allList.size();i++)
        {
            ProductSpuAttributeKeyValueVO attributeKeyVO = allList.get(i);
            if(attributeKeyVO.getType().intValue()==1
                    &&attributeKeyVO.getParentAttributeKeyId()!=null&&parentAttributeTree.getAttributeKeyId()!=null
                    &&attributeKeyVO.getParentAttributeKeyId().longValue()==parentAttributeTree.getAttributeKeyId().longValue())
            {
                ProductSpuAttributeKeyValueTreeVO productSpuAttributeKeyChildVO = new ProductSpuAttributeKeyValueTreeVO();
                productSpuAttributeKeyChildVO.setAttributeKeyId(attributeKeyVO.getAttributeKeyId());
                productSpuAttributeKeyChildVO.setName(attributeKeyVO.getAttributeName());
                productSpuAttributeKeyChildVO.setValues(new LinkedList<>());
                productSpuAttributeKeyChildVO.setChildren(new LinkedList<>());
                parentAttributeTree.getChildren().add(productSpuAttributeKeyChildVO);

                for (int j = 0; j < allList.size(); j++) {
                    ProductSpuAttributeKeyValueVO attributeValueVO = allList.get(j);
                    if(attributeValueVO.getType().intValue()==2
                            &&attributeValueVO.getAttributeKeyId().longValue()==productSpuAttributeKeyChildVO.getAttributeKeyId().longValue())
                    {
                        productSpuAttributeKeyChildVO.getValues().add(attributeValueVO.getAttributeValue());
                    }
                }
                this.queryAttributeTree(allList,productSpuAttributeKeyChildVO);
            }
        }
    }

    @Override
    public int updateShowStatusAndSearchStatus(Long attributeValueId, Short showStatus, Short queryStatus) {
        return attributeValueMapper.updateShowStatusAndSearchStatus(attributeValueId,showStatus,queryStatus);
    }


}
