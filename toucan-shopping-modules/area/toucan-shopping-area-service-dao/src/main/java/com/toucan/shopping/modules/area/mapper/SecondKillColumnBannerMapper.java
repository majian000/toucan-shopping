package com.toucan.shopping.modules.area.mapper;

import com.toucan.shopping.modules.area.entity.SecondKillColumnArea;
import com.toucan.shopping.modules.area.entity.SecondKillColumnBanner;
import com.toucan.shopping.modules.area.vo.SecondKillColumnAreaVO;
import com.toucan.shopping.modules.area.vo.SecondKillColumnBannerVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface SecondKillColumnBannerMapper {

    List<SecondKillColumnBanner> queryList(SecondKillColumnBannerVO entity);

    int insert(SecondKillColumnBanner entity);


    int deleteById(Long id);



}
