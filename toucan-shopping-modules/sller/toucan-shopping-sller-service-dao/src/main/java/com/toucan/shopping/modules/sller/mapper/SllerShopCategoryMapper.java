package com.toucan.shopping.modules.sller.mapper;

import com.toucan.shopping.modules.sller.entity.SllerShopCategory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface SllerShopCategoryMapper {

    int insert(SllerShopCategory entity);


    /**
     * 根据ID删除
     * @param id
     * @return
     */
    int deleteById(Long id);

    int update(SllerShopCategory entity);

    List<SllerShopCategory> findListByEntity(SllerShopCategory query);

}
