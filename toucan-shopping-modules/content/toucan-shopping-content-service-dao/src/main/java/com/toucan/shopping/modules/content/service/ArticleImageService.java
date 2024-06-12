package com.toucan.shopping.modules.content.service;


import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.content.entity.ArticleImage;
import com.toucan.shopping.modules.content.page.ArticleImagePageInfo;
import com.toucan.shopping.modules.content.page.DeleteArticleImagePageInfo;
import com.toucan.shopping.modules.content.vo.ArticleImageVO;

import java.util.List;

public interface ArticleImageService {


    ArticleImage findById(Long id);

    /**
     * 查询列表页
     * @param pageInfo
     * @return
     */
    PageInfo<ArticleImageVO> queryListPage(ArticleImagePageInfo pageInfo);


    /**
     * 保存实体
     * @param entity
     * @return
     */
    int save(ArticleImage entity);

    List<ArticleImage> queryListByImgPathList(List imgPathList);

    int deleteById(Long id);

    int deleteByIdAndArticleId(Long id, Long articleId);

    int updateArticleId(Long id,Long articleId);


    /**
     * 查询无效数据列表页
     * @param queryPageInfo
     * @return
     */
    PageInfo<ArticleImageVO> queryInvalidListPage(DeleteArticleImagePageInfo queryPageInfo);



    int deleteByIdList(List idList,String remark);

}
