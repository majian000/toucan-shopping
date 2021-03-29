package com.toucan.shopping.area.mapper;

import com.toucan.shopping.area.entity.Area;
import com.toucan.shopping.area.entity.Banner;
import com.toucan.shopping.area.entity.BannerArea;
import com.toucan.shopping.area.vo.BannerVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface BannerMapper {

    List<Banner> queryList(BannerVO entity);

    int insert(Banner banner);


    int deleteById(Long id);



}
