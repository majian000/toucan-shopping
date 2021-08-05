package com.toucan.shopping.modules.sller.mapper;

import com.toucan.shopping.modules.sller.entity.SllerShop;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface SllerShopMapper {


    int insert(SllerShop entity);


    /**
     * 根据ID删除
     * @param id
     * @return
     */
    int deleteById(Long id);

    int update(SllerShop entity);

    List<SllerShop> findListByEntity(SllerShop query);
}
