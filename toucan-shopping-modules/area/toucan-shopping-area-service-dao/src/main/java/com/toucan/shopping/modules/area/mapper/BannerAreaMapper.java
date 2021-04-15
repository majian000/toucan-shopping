package com.toucan.shopping.modules.area.mapper;

import com.toucan.shopping.modules.area.entity.Banner;
import com.toucan.shopping.modules.area.entity.BannerArea;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface BannerAreaMapper {

    List<BannerArea> queryList(BannerArea entity);


    int insert(BannerArea entity);

    int deleteByBannerId(Long bannerId);




}
