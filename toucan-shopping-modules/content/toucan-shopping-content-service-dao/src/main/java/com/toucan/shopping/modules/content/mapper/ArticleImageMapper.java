package com.toucan.shopping.modules.content.mapper;

import com.toucan.shopping.modules.content.entity.ArticleImage;
import com.toucan.shopping.modules.content.page.ArticleImagePageInfo;
import com.toucan.shopping.modules.content.page.DeleteArticleImagePageInfo;
import com.toucan.shopping.modules.content.vo.ArticleImageVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * 店铺图片
 * @author majian
 */
@Mapper
public interface ArticleImageMapper {

    ArticleImage findById(Long id);


    int insert(ArticleImage entity);


    /**
     * 查询列表页
     * @param pageInfo
     * @return
     */
    List<ArticleImageVO> queryListPage(ArticleImagePageInfo pageInfo);




    /**
     * 查询列表页
     * @param pageInfo
     * @return
     */
    List<ArticleImageVO> queryInvalidListPage(DeleteArticleImagePageInfo pageInfo);

    /**
     * 根据图片路径列表查询
     * @param imgPathList
     * @return
     */
    List<ArticleImage> queryListByImgPathList(List imgPathList);

    /**
     * 返回列表页数量
     * @param pageInfo
     * @return
     */
    Long queryListPageCount(ArticleImagePageInfo pageInfo);

    /**
     * 根据文章ID查询图片关联
     * @param articleId
     * @return
     */
    List<ArticleImage> queryListByArticleId(Long articleId);

    /**
     * 返回列表页数量
     * @param pageInfo
     * @return
     */
    Long queryInvalidListPageCount(DeleteArticleImagePageInfo pageInfo);

    int deleteById(Long id);

    int deleteByIdAndArticleId(Long id, Long shopId);

    int updateArticleId(Long id,Long articleId);

    int deleteByIdList(List idList, String remark);

    int deleteByIdAndSaveOperateUserId(Long id, String adminId);

    int updateFileDeleteStatusById(Long id,Integer fileDeleteStatus, String adminId);

    int updateFileDeleteStatusAndDeleteStatusById(Long id,Integer fileDeleteStatus,Integer deleteStatus, String adminId);

    int updateFileDeleteStatusByIdList(List idList,Integer fileDeleteStatus, String adminId);

}
