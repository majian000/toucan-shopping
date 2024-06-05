package com.toucan.shopping.modules.content.service;


import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.content.entity.Article;
import com.toucan.shopping.modules.content.entity.Banner;
import com.toucan.shopping.modules.content.page.ArticlePageInfo;
import com.toucan.shopping.modules.content.page.BannerPageInfo;
import com.toucan.shopping.modules.content.vo.ArticleVO;
import com.toucan.shopping.modules.content.vo.BannerVO;

import java.util.List;

public interface ArticleService {




    /**
     * 查询列表页
     * @param pageInfo
     * @return
     */
    PageInfo<ArticleVO> queryListPage(ArticlePageInfo pageInfo);



    List<ArticleVO> queryList(ArticleVO query);


    int save(Article entity);


}
