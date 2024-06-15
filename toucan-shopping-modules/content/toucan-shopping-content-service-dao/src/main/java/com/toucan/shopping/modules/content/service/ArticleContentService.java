package com.toucan.shopping.modules.content.service;


import com.toucan.shopping.modules.content.entity.Article;
import com.toucan.shopping.modules.content.entity.ArticleContent;
import com.toucan.shopping.modules.content.vo.ArticleContentVO;

public interface ArticleContentService {


    int save(ArticleContent entity);

    int deleteByArticleId(Long articleId);

    ArticleContentVO findByArticleId(Long articleId);

}
