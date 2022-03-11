package com.toucan.shopping.modules.product.mapper;

import com.toucan.shopping.modules.product.entity.AttributeValue;
import com.toucan.shopping.modules.product.page.AttributeValuePageInfo;
import com.toucan.shopping.modules.product.vo.AttributeValueVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface AttributeValueMapper {

    List<AttributeValueVO> queryList(AttributeValueVO entity);

    List<AttributeValueVO> queryListBySortDesc(AttributeValueVO entity);

    int insert(AttributeValue attributeValue);

    int inserts(List<AttributeValue> entitys);

    /**
     * 查询列表页
     * @param pageInfo
     * @return
     */
    List<AttributeValueVO> queryListPage(AttributeValuePageInfo pageInfo);

    /**
     * 返回列表页数量
     * @param pageInfo
     * @return
     */
    Long queryListPageCount(AttributeValuePageInfo pageInfo);


    /**
     * 修改
     * @param attributeValue
     * @return
     */
    int update(AttributeValue attributeValue);

    int deleteById(Long id);

    int deleteByAttributeKeyId(Long attributeKeyId);

    List<AttributeValueVO> queryListByAttributeKeyIdSortDesc(Long attributeKeyId);
}
