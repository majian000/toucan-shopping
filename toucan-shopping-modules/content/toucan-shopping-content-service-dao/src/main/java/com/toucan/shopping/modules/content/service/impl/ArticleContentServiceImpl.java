package com.toucan.shopping.modules.content.service.impl;

import com.toucan.shopping.modules.content.entity.ArticleContent;
import com.toucan.shopping.modules.content.mapper.ArticleContentMapper;
import com.toucan.shopping.modules.content.mapper.ArticleMapper;
import com.toucan.shopping.modules.content.service.ArticleContentService;
import com.toucan.shopping.modules.content.service.ArticleService;
import com.toucan.shopping.modules.content.vo.ArticleContentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleContentServiceImpl implements ArticleContentService {

    @Autowired
    private ArticleContentMapper articleContentMapper;


    @Override
    public int save(ArticleContent entity) {
        return articleContentMapper.insert(entity);
    }

    @Override
    public int deleteByArticleId(Long articleId) {
        return articleContentMapper.deleteByArticleId(articleId);
    }

    @Override
    public ArticleContentVO findByArticleId(Long articleId) {
        return articleContentMapper.findByArticleId(articleId);
    }

    @Override
    public int update(ArticleContent entity) {
        return articleContentMapper.update(entity);
    }
}
