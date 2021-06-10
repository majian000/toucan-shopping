package com.toucan.shopping.modules.area.mapper;

import com.toucan.shopping.modules.area.entity.Area;
import com.toucan.shopping.modules.area.entity.Banner;
import com.toucan.shopping.modules.area.entity.BannerArea;
import com.toucan.shopping.modules.area.page.BannerPageInfo;
import com.toucan.shopping.modules.area.vo.BannerVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface BannerMapper {

    List<Banner> queryList(BannerVO entity);

    int insert(Banner banner);

    int deleteById(Long id);

    int update(Banner banner);



    /**
     * 查询列表页
     * @param pageInfo
     * @return
     */
    List<BannerVO> queryListPage(BannerPageInfo pageInfo);

    /**
     * 返回列表页数量
     * @param pageInfo
     * @return
     */
    Long queryListPageCount(BannerPageInfo pageInfo);

}
