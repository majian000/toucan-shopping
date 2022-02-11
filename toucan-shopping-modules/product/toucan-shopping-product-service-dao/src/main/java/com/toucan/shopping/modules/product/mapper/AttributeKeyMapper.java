package com.toucan.shopping.modules.product.mapper;

import com.toucan.shopping.modules.product.entity.AttributeKey;
import com.toucan.shopping.modules.product.page.AttributeKeyPageInfo;
import com.toucan.shopping.modules.product.vo.AttributeKeyVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface AttributeKeyMapper {

    List<AttributeKeyVO> queryList(AttributeKeyVO entity);

    List<AttributeKeyVO> queryListBySortDesc(AttributeKeyVO entity);

    int insert(AttributeKey attributeKey);

    /**
     * 查询列表页
     * @param pageInfo
     * @return
     */
    List<AttributeKeyVO> queryListPage(AttributeKeyPageInfo pageInfo);

    /**
     * 返回列表页数量
     * @param pageInfo
     * @return
     */
    Long queryListPageCount(AttributeKeyPageInfo pageInfo);


    /**
     * 修改
     * @param attributeKey
     * @return
     */
    int update(AttributeKey attributeKey);

    int deleteById(Long id);

}
