package com.toucan.shopping.modules.content.mapper;

import com.toucan.shopping.modules.content.entity.ArticleContent;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface ArticleContentMapper {

    int insert(ArticleContent entity);

}
