package com.toucan.shopping.modules.content.service.impl;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.content.entity.Article;
import com.toucan.shopping.modules.content.entity.Banner;
import com.toucan.shopping.modules.content.mapper.ArticleMapper;
import com.toucan.shopping.modules.content.mapper.BannerMapper;
import com.toucan.shopping.modules.content.page.ArticlePageInfo;
import com.toucan.shopping.modules.content.page.BannerPageInfo;
import com.toucan.shopping.modules.content.service.ArticleService;
import com.toucan.shopping.modules.content.service.BannerService;
import com.toucan.shopping.modules.content.vo.ArticleVO;
import com.toucan.shopping.modules.content.vo.BannerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;


    @Override
    public PageInfo<ArticleVO> queryListPage(ArticlePageInfo queryPageInfo) {
        queryPageInfo.setStart(queryPageInfo.getPage()*queryPageInfo.getLimit()-queryPageInfo.getLimit());
        PageInfo<ArticleVO> pageInfo = new PageInfo();
        pageInfo.setList(articleMapper.queryListPage(queryPageInfo));
        pageInfo.setTotal(articleMapper.queryListPageCount(queryPageInfo));
        return pageInfo;
    }

    @Override
    public List<ArticleVO> queryList(ArticleVO query) {
        return articleMapper.queryList(query);
    }

    @Override
    public int save(Article entity) {
        return articleMapper.insert(entity);
    }

    @Override
    public int update(ArticleVO articleVO) {
        return articleMapper.update(articleVO);
    }

    @Override
    public Long queryMaxSort(Long columnId) {
        return articleMapper.queryMaxSort(columnId);
    }

    @Override
    public ArticleVO findById(Long id) {
        return articleMapper.findById(id);
    }


}
