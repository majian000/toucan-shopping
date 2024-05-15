package com.toucan.shopping.modules.content.mapper;

import com.toucan.shopping.modules.content.entity.Banner;
import com.toucan.shopping.modules.content.page.ArticlePageInfo;
import com.toucan.shopping.modules.content.page.BannerPageInfo;
import com.toucan.shopping.modules.content.vo.ArticleVO;
import com.toucan.shopping.modules.content.vo.BannerVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface ArticleMapper {


    /**
     * 查询列表页
     * @param pageInfo
     * @return
     */
    List<ArticleVO> queryListPage(ArticlePageInfo pageInfo);

    /**
     * 返回列表页数量
     * @param pageInfo
     * @return
     */
    Long queryListPageCount(ArticlePageInfo pageInfo);


}
