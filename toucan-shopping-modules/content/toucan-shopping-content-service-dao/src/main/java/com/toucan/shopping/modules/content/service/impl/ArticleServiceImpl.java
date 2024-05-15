package com.toucan.shopping.modules.content.service.impl;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.content.entity.Banner;
import com.toucan.shopping.modules.content.mapper.ArticleMapper;
import com.toucan.shopping.modules.content.mapper.BannerMapper;
import com.toucan.shopping.modules.content.page.BannerPageInfo;
import com.toucan.shopping.modules.content.service.ArticleService;
import com.toucan.shopping.modules.content.service.BannerService;
import com.toucan.shopping.modules.content.vo.BannerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;



}
