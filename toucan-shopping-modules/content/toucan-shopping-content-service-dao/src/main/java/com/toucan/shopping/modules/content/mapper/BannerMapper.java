package com.toucan.shopping.modules.content.mapper;

import com.toucan.shopping.modules.content.entity.Banner;
import com.toucan.shopping.modules.content.entity.BannerArea;
import com.toucan.shopping.modules.content.page.BannerPageInfo;
import com.toucan.shopping.modules.content.vo.BannerVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface BannerMapper {

    List<BannerVO> queryList(BannerVO entity);

    List<BannerVO> queryIndexList(BannerVO entity);

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
