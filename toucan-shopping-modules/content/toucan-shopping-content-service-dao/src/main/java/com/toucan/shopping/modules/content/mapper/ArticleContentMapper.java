package com.toucan.shopping.modules.content.mapper;

import com.toucan.shopping.modules.content.entity.ArticleContent;
import com.toucan.shopping.modules.content.vo.ArticleContentVO;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface ArticleContentMapper {

    int insert(ArticleContent entity);

    int deleteByArticleId(Long articleId);

    ArticleContentVO findByArticleId(Long articleId);

    int update(ArticleContent entity);
}
