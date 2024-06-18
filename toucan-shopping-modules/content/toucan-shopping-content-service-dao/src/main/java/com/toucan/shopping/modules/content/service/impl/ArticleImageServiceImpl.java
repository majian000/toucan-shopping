package com.toucan.shopping.modules.content.service.impl;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.content.entity.ArticleImage;
import com.toucan.shopping.modules.content.mapper.ArticleImageMapper;
import com.toucan.shopping.modules.content.page.ArticleImagePageInfo;
import com.toucan.shopping.modules.content.page.DeleteArticleImagePageInfo;
import com.toucan.shopping.modules.content.service.ArticleImageService;
import com.toucan.shopping.modules.content.vo.ArticleImageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleImageServiceImpl implements ArticleImageService {

    @Autowired
    private ArticleImageMapper articleImageMapper;


    @Override
    public ArticleImage findById(Long id) {
        return articleImageMapper.findById(id);
    }

    @Override
    public PageInfo<ArticleImageVO> queryListPage(ArticleImagePageInfo queryPageInfo) {
        queryPageInfo.setStart(queryPageInfo.getPage()*queryPageInfo.getLimit()-queryPageInfo.getLimit());
        PageInfo<ArticleImageVO> pageInfo = new PageInfo();
        pageInfo.setList(articleImageMapper.queryListPage(queryPageInfo));
        pageInfo.setTotal(articleImageMapper.queryListPageCount(queryPageInfo));
        return pageInfo;
    }

    @Override
    public int save(ArticleImage entity) {
        return articleImageMapper.insert(entity);
    }

    @Override
    public List<ArticleImage> queryListByImgPathList(List imgPathList) {
        return articleImageMapper.queryListByImgPathList(imgPathList);
    }

    @Override
    public List<ArticleImage> queryListByArticleId(Long articleId) {
        return articleImageMapper.queryListByArticleId(articleId);
    }

    @Override
    public int deleteById(Long id) {
        return articleImageMapper.deleteById(id);
    }

    @Override
    public int deleteById(Long id, String adminId) {
        return articleImageMapper.deleteByIdAndSaveOperateUserId(id,adminId);
    }

    @Override
    public int updateFileDeleteStatusById(Long id,Integer fileDeleteStatus, String adminId) {
        return articleImageMapper.updateFileDeleteStatusById(id,fileDeleteStatus, adminId);
    }
    @Override
    public int deleteByIdAndArticleId(Long id,Long articleId) {
        return articleImageMapper.deleteByIdAndArticleId(id,articleId);
    }

    @Override
    public int updateArticleId(Long id,Long articleId){
        return articleImageMapper.updateArticleId(id,articleId);
    }


    @Override
    public PageInfo<ArticleImageVO> queryInvalidListPage(DeleteArticleImagePageInfo queryPageInfo) {
        queryPageInfo.setStart(queryPageInfo.getPage()*queryPageInfo.getLimit()-queryPageInfo.getLimit());
        PageInfo<ArticleImageVO> pageInfo = new PageInfo();
        pageInfo.setList(articleImageMapper.queryInvalidListPage(queryPageInfo));
        pageInfo.setTotal(articleImageMapper.queryInvalidListPageCount(queryPageInfo));
        return pageInfo;
    }

    @Override
    public int deleteByIdList(List idList, String remark) {
        return articleImageMapper.deleteByIdList(idList,remark);
    }

    @Override
    public int updateFileDeleteStatusByIdList(List idList, Integer fileDeleteStatus, String adminId) {
        return articleImageMapper.updateFileDeleteStatusByIdList(idList,fileDeleteStatus,adminId);
    }

}
