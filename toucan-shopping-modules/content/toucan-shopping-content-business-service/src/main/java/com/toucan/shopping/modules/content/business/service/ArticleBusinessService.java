package com.toucan.shopping.modules.content.business.service;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultTypeObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.content.entity.*;
import com.toucan.shopping.modules.content.page.ArticlePageInfo;
import com.toucan.shopping.modules.content.redis.ArticleLockKey;
import com.toucan.shopping.modules.content.service.*;
import com.toucan.shopping.modules.content.vo.ArticleContentVO;
import com.toucan.shopping.modules.content.vo.ArticleVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.jsoup.utils.JsoupUtil;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.toucan.shopping.modules.common.annotation.RequestCheck;
import com.toucan.shopping.modules.common.exception.BusinessValidationException;
import com.toucan.shopping.modules.common.util.Check;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class ArticleBusinessService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleContentService articleContentService;

    @Autowired
    private ArticleImageService articleImageService;

    @Autowired
    private SkylarkLock skylarkLock;

    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryListPage(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            ArticlePageInfo queryPageInfo = requestJsonVO.formatEntity(ArticlePageInfo.class);
            PageInfo<ArticleVO> pageInfo = articleService.queryListPage(queryPageInfo);
            resultObjectVO.setData(pageInfo);
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultVO.FAILD, "查询失败!");
        }

        return resultObjectVO;
    }

    @RequestCheck(requireEntity = true)
    public ResultObjectVO save(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        ArticleVO articleVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), ArticleVO.class);
        Check.notEmpty(articleVO.getTitle(), ResultObjectVO.FAILD, "标题不能为空");
        Check.notEmpty(articleVO.getContent(), ResultObjectVO.FAILD, "内容不能为空");
        Check.notEmpty(articleVO.getAppCode(), ResultObjectVO.FAILD, "所属应用不能为空");
        String lockKey = articleVO.getAppCode() + "_" + articleVO.getCreateAdminId();
        try {
            boolean lockStatus = skylarkLock.lock(ArticleLockKey.getSaveLockKey(lockKey), lockKey);
            Check.isTrue(lockStatus, ResultObjectVO.FAILD, "请稍后重试");

            ArticleVO query = new ArticleVO();
            query.setTitle(articleVO.getTitle());
            query.setColumnId(articleVO.getColumnId());
            List<ArticleVO> articles = articleService.queryList(query);
            Check.isTrue(CollectionUtils.isEmpty(articles), ResultObjectVO.FAILD, "\"" + articleVO.getTitle() + "\"文章已存在");

            Long articleId = idGenerator.id();
            Long articleContentId = idGenerator.id();

            ArticleContent articleContent = new ArticleContent();
            articleContent.setId(articleContentId);
            articleContent.setArticleId(articleId);
            articleContent.setContent(articleVO.getContent());
            articleContent.setCreateDate(new Date());
            articleContent.setCreateAdminId(articleVO.getCreateAdminId());
            articleContent.setAppCode(articleVO.getAppCode());
            articleContent.setDeleteStatus((short) 0);
            int ret = articleContentService.save(articleContent);
            Check.isTrue(ret > 0, ResultVO.FAILD, "请稍后重试");
            if (articleVO.getArticleSort() == null) {
                Long maxArticleSort = articleService.queryMaxSort(articleVO.getColumnId());
                articleVO.setArticleSort(maxArticleSort + 1);
            }
            articleVO.setId(articleId);
            articleVO.setContentId(articleContentId);
            articleVO.setDeleteStatus((short) 0);
            articleVO.setCreateDate(new Date());
            if (articleVO.getPerpetualStatus() == null) {
                articleVO.setPerpetualStatus((short) 0);
            }
            ret = articleService.save(articleVO);
            if (ret <= 0) {
                articleContentService.deleteByArticleId(articleId);
                logger.warn("保存文章失败 requestJson{} id{}", requestJsonVO.getEntityJson(), articleVO.getId());
                return ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
            }

            //更新图片和文章关联
            List<String> attributeValueList = JsoupUtil.queryAttributeValueList(articleVO.getContent(), "img", "src");
            if (CollectionUtils.isNotEmpty(attributeValueList)) {
                for (int i = 0; i < attributeValueList.size(); i++) {
                    attributeValueList.set(i, attributeValueList.get(i).replaceAll(imageUploadService.getImageHttpPrefix(), ""));
                }
                List<ArticleImage> articleImages = articleImageService.queryListByImgPathList(attributeValueList);
                if (CollectionUtils.isNotEmpty(articleImages)) {
                    for (ArticleImage articleImage : articleImages) {
                        articleImageService.updateArticleId(articleImage.getId(), articleId);
                    }
                }
            }

            resultObjectVO.setData(articleVO);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        } finally {
            skylarkLock.unLock(ArticleLockKey.getSaveLockKey(lockKey), lockKey);
        }
        return resultObjectVO;
    }

    @RequestCheck(requireEntity = true)
    public ResultTypeObjectVO<ArticleVO> findById(RequestJsonVO requestJsonVO) {
        ResultTypeObjectVO resultObjectVO = new ResultTypeObjectVO();

        ArticleVO queryArticleVO = requestJsonVO.formatEntity(ArticleVO.class);
        Check.notNull(queryArticleVO.getId(), ResultObjectVO.FAILD, "ID不能为空");
        try {
            ArticleVO articleVO = articleService.findById(queryArticleVO.getId());
            if (articleVO != null) {
                ArticleContentVO articleContentVO = articleContentService.findByArticleId(articleVO.getId());
                if (articleContentVO != null) {
                    articleVO.setContent(articleContentVO.getContent());
                }
            }
            resultObjectVO.setData(articleVO);
        }catch(BusinessValidationException e){
            return new ResultTypeObjectVO<>(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return new ResultTypeObjectVO<>(ResultVO.FAILD, "请稍后重试");
        }
        return resultObjectVO;
    }

    @RequestCheck(requireEntity = true)
    public ResultTypeObjectVO<Long> queryMaxSort(RequestJsonVO requestJsonVO) {
        ResultTypeObjectVO resultObjectVO = new ResultTypeObjectVO();
        try {
            Long maxSort = articleService.queryMaxSort(requestJsonVO.formatEntity(Long.class));
            if (maxSort == null) {
                maxSort = 0L;
            }
            resultObjectVO.setData(maxSort);
        }catch(BusinessValidationException e){
            return new ResultTypeObjectVO<>(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return new ResultTypeObjectVO<>(ResultVO.FAILD, "请稍后重试");
        }
        return resultObjectVO;
    }

    @RequestCheck(requireEntity = true)
    public ResultObjectVO update(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        ArticleVO articleVO = requestJsonVO.formatEntity(ArticleVO.class);
        Check.notNull(articleVO.getId(), ResultObjectVO.FAILD, "文章ID不能为空");
        Check.notEmpty(articleVO.getTitle(), ResultObjectVO.FAILD, "栏目标题不能为空");
        Check.notEmpty(articleVO.getAppCode(), ResultObjectVO.FAILD, "所属应用不能为空");
        String lockKey = articleVO.getAppCode() + "_" + articleVO.getId();
        try {
            boolean lockStatus = skylarkLock.lock(ArticleLockKey.getUpdateLockKey(lockKey), lockKey);
            Check.isTrue(lockStatus, ResultObjectVO.FAILD, "请稍后重试");

            int ret = articleService.update(articleVO);
            Check.isTrue(ret > 0, ResultVO.FAILD, "请稍后重试");

            ArticleContent articleContent = articleContentService.findByArticleId(articleVO.getId());
            articleContent.setContent(articleVO.getContent());

            ret = articleContentService.update(articleContent);
            Check.isTrue(ret > 0, ResultVO.FAILD, "请稍后重试");

            //更新图片和文章关联
            List<ArticleImage> oldArticleImages = articleImageService.queryListByArticleId(articleVO.getId());
            List<String> attributeValueList = JsoupUtil.queryAttributeValueList(articleVO.getContent(), "img", "src");
            if (CollectionUtils.isNotEmpty(attributeValueList)) {
                for (int i = 0; i < attributeValueList.size(); i++) {
                    attributeValueList.set(i, attributeValueList.get(i).replaceAll(imageUploadService.getImageHttpPrefix(), ""));
                }
                List<ArticleImage> currentNotIncludeList = new LinkedList<>(); //本次提交未包含这张图片
                boolean isInclude = false;
                //判断当前提交是否进行了图片删除,如果删除了图片,把那些删除的图片进行删除
                for (ArticleImage articleImage : oldArticleImages) {
                    isInclude = false;
                    for (String currentSubmitImage : attributeValueList) {
                        if (articleImage.getImgPath().equals(currentSubmitImage)) {
                            isInclude = true;
                            break;
                        }
                    }
                    if (!isInclude) {
                        currentNotIncludeList.add(articleImage);
                    }
                }
                if (CollectionUtils.isNotEmpty(currentNotIncludeList)) {
                    for (ArticleImage deleteArticleImage : currentNotIncludeList) {
                        int articleImageRet = imageUploadService.deleteFile(deleteArticleImage.getImgPath());
                        if (articleImageRet != 0) {
                            articleImageService.updateFileDeleteStatusAndDeleteStatusById(deleteArticleImage.getId(), 0, 1, articleVO.getUpdateAdminId());
                        } else {
                            articleImageService.updateFileDeleteStatusAndDeleteStatusById(deleteArticleImage.getId(), 1, 1, articleVO.getUpdateAdminId());
                            articleImageService.deleteById(deleteArticleImage.getId(), articleVO.getUpdateAdminId());
                        }
                    }
                }
            } else { //本次提交没有图片
                if (CollectionUtils.isNotEmpty(oldArticleImages)) {
                    for (ArticleImage articleImage : oldArticleImages) {
                        int articleImageRet = imageUploadService.deleteFile(articleImage.getImgPath());
                        if (articleImageRet != 0) {
                            articleImageService.updateFileDeleteStatusAndDeleteStatusById(articleImage.getId(), 0, 1, articleVO.getUpdateAdminId());
                        } else {
                            articleImageService.updateFileDeleteStatusAndDeleteStatusById(articleImage.getId(), 1, 1, articleVO.getUpdateAdminId());
                            articleImageService.deleteById(articleImage.getId(), articleVO.getUpdateAdminId());
                        }
                    }
                }
            }

            resultObjectVO.setData(articleVO);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        } finally {
            skylarkLock.unLock(ArticleLockKey.getUpdateLockKey(lockKey), lockKey);
        }
        return resultObjectVO;
    }

    @RequestCheck(requireEntity = true)
    public ResultObjectVO deleteById(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        ArticleVO articleVO = requestJsonVO.formatEntity(ArticleVO.class);
        Check.notNull(articleVO.getId(), ResultObjectVO.FAILD, "文章ID不能为空");
        Check.notEmpty(articleVO.getAppCode(), ResultObjectVO.FAILD, "所属应用不能为空");
        try {

            int ret = articleService.deleteById(articleVO.getId());
            Check.isTrue(ret > 0, ResultVO.FAILD, "请稍后重试");

            ret = articleContentService.deleteByArticleId(articleVO.getId());
            Check.isTrue(ret > 0, ResultVO.FAILD, "请稍后重试");
            List<ArticleImage> articleImages = articleImageService.queryListByArticleId(articleVO.getId());
            for (ArticleImage articleImage : articleImages) {
                int articleImageRet = imageUploadService.deleteFile(articleImage.getImgPath());
                if (articleImageRet != 0) {
                    articleImageService.updateFileDeleteStatusAndDeleteStatusById(articleImage.getId(), 0, 1, articleVO.getUpdateAdminId());
                } else {
                    articleImageService.updateFileDeleteStatusAndDeleteStatusById(articleImage.getId(), 1, 1, articleVO.getUpdateAdminId());
                    articleImageService.deleteById(articleImage.getId(), articleVO.getUpdateAdminId());
                }
            }
            resultObjectVO.setData(articleVO);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        }
        return resultObjectVO;
    }

    @RequestCheck(requireEntity = true)
    public ResultObjectVO deleteByIds(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            List<ArticleVO> articleVOS = requestJsonVO.formatEntityList(ArticleVO.class);
            Check.notEmpty(articleVOS, ResultVO.FAILD, "没有找到ID");

            List<ResultObjectVO> resultObjectVOList = new ArrayList<ResultObjectVO>();
            for (ArticleVO articleVO : articleVOS) {
                ResultObjectVO articleResultObjectVO = new ResultObjectVO();
                articleResultObjectVO.setData(articleVO);
                if (articleVO.getId() == null) {
                    articleResultObjectVO.setCode(ResultVO.FAILD);
                    articleResultObjectVO.setMsg("ID不存在!");
                    resultObjectVOList.add(articleResultObjectVO);
                    continue;
                }
                int ret = articleService.deleteById(articleVO.getId());
                if (ret <= 0) {
                    logger.warn("删除文章失败 requestJson{} id{}", requestJsonVO.getEntityJson(), articleVO.getId());
                    articleResultObjectVO.setCode(ResultVO.FAILD);
                    articleResultObjectVO.setMsg("请稍后重试");
                    resultObjectVOList.add(articleResultObjectVO);
                    continue;
                }

                ret = articleContentService.deleteByArticleId(articleVO.getId());
                if (ret <= 0) {
                    logger.warn("删除文章内容失败 requestJson{} id{}", requestJsonVO.getEntityJson(), articleVO.getId());
                    articleResultObjectVO.setCode(ResultVO.FAILD);
                    articleResultObjectVO.setMsg("请稍后重试");
                    resultObjectVOList.add(articleResultObjectVO);
                    continue;
                }
                List<ArticleImage> articleImages = articleImageService.queryListByArticleId(articleVO.getId());
                for (ArticleImage articleImage : articleImages) {
                    int articleImageRet = imageUploadService.deleteFile(articleImage.getImgPath());
                    if (articleImageRet != 0) {
                        articleImageService.updateFileDeleteStatusAndDeleteStatusById(articleImage.getId(), 0, 1, articleVO.getUpdateAdminId());
                    } else {
                        articleImageService.updateFileDeleteStatusAndDeleteStatusById(articleImage.getId(), 1, 1, articleVO.getUpdateAdminId());
                        articleImageService.deleteById(articleImage.getId(), articleVO.getUpdateAdminId());
                    }
                }
                resultObjectVOList.add(articleResultObjectVO);
            }
            resultObjectVO.setData(resultObjectVOList);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        }
        return resultObjectVO;
    }

}
