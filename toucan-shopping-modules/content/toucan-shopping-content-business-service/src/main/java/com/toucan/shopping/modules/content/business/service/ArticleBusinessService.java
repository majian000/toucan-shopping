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

    public ResultObjectVO queryListPage(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if (requestJsonVO == null) {
            logger.info("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }
        if (requestJsonVO.getAppCode() == null) {
            logger.info("没有找到对象: param:" + JSONObject.toJSONString(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到对象!");
            return resultObjectVO;
        }
        try {
            ArticlePageInfo queryPageInfo = requestJsonVO.formatEntity(ArticlePageInfo.class);
            PageInfo<ArticleVO> pageInfo = articleService.queryListPage(queryPageInfo);
            resultObjectVO.setData(pageInfo);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }

    public ResultObjectVO save(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if (requestJsonVO == null) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("没有找到请求对象");
            return resultObjectVO;
        }
        if (StringUtils.isEmpty(requestJsonVO.getAppCode())) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("没有找到应用编码");
            return resultObjectVO;
        }
        ArticleVO articleVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), ArticleVO.class);
        if (StringUtils.isEmpty(articleVO.getTitle())) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("标题不能为空");
            return resultObjectVO;
        }
        if (StringUtils.isEmpty(articleVO.getContent())) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("内容不能为空");
            return resultObjectVO;
        }
        if (StringUtils.isEmpty(articleVO.getAppCode())) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("所属应用不能为空");
            return resultObjectVO;
        }
        String lockKey = articleVO.getAppCode() + "_" + articleVO.getCreateAdminId();
        try {
            boolean lockStatus = skylarkLock.lock(ArticleLockKey.getSaveLockKey(lockKey), lockKey);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
                return resultObjectVO;
            }

            ArticleVO query = new ArticleVO();
            query.setTitle(articleVO.getTitle());
            query.setColumnId(articleVO.getColumnId());
            List<ArticleVO> articles = articleService.queryList(query);
            if (!CollectionUtils.isEmpty(articles)) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("\"" + articleVO.getTitle() + "\"文章已存在");
                return resultObjectVO;
            }

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
            if (ret <= 0) {
                logger.warn("保存文章内容失败 requestJson{} id{}", requestJsonVO.getEntityJson(), articleVO.getId());
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
                return resultObjectVO;
            }
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
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
                return resultObjectVO;
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

        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        } finally {
            skylarkLock.unLock(ArticleLockKey.getSaveLockKey(lockKey), lockKey);
        }
        return resultObjectVO;
    }

    public ResultTypeObjectVO<ArticleVO> findById(RequestJsonVO requestJsonVO) {
        ResultTypeObjectVO resultObjectVO = new ResultTypeObjectVO();
        if (requestJsonVO == null) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("没有找到请求对象");
            return resultObjectVO;
        }
        if (StringUtils.isEmpty(requestJsonVO.getAppCode())) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("没有找到应用编码");
            return resultObjectVO;
        }

        ArticleVO queryArticleVO = requestJsonVO.formatEntity(ArticleVO.class);
        if (queryArticleVO.getId() == null) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("ID不能为空");
            return resultObjectVO;
        }
        try {
            ArticleVO articleVO = articleService.findById(queryArticleVO.getId());
            if (articleVO != null) {
                ArticleContentVO articleContentVO = articleContentService.findByArticleId(articleVO.getId());
                if (articleContentVO != null) {
                    articleVO.setContent(articleContentVO.getContent());
                }
            }
            resultObjectVO.setData(articleVO);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    public ResultTypeObjectVO<Long> queryMaxSort(RequestJsonVO requestJsonVO) {
        ResultTypeObjectVO resultObjectVO = new ResultTypeObjectVO();
        try {
            Long maxSort = articleService.queryMaxSort(requestJsonVO.formatEntity(Long.class));
            if (maxSort == null) {
                maxSort = 0L;
            }
            resultObjectVO.setData(maxSort);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    public ResultObjectVO update(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if (requestJsonVO == null) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("没有找到请求对象");
            return resultObjectVO;
        }
        if (StringUtils.isEmpty(requestJsonVO.getAppCode())) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("没有找到应用编码");
            return resultObjectVO;
        }

        ArticleVO articleVO = requestJsonVO.formatEntity(ArticleVO.class);
        if (articleVO.getId() == null) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("文章ID不能为空");
            return resultObjectVO;
        }
        if (StringUtils.isEmpty(articleVO.getTitle())) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("栏目标题不能为空");
            return resultObjectVO;
        }
        if (StringUtils.isEmpty(articleVO.getAppCode())) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("所属应用不能为空");
            return resultObjectVO;
        }
        String lockKey = articleVO.getAppCode() + "_" + articleVO.getId();
        try {
            boolean lockStatus = skylarkLock.lock(ArticleLockKey.getUpdateLockKey(lockKey), lockKey);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
                return resultObjectVO;
            }

            int ret = articleService.update(articleVO);
            if (ret <= 0) {
                logger.warn("修改文章失败 requestJson{} id{}", requestJsonVO.getEntityJson(), articleVO.getId());
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
            }

            ArticleContent articleContent = articleContentService.findByArticleId(articleVO.getId());
            articleContent.setContent(articleVO.getContent());

            ret = articleContentService.update(articleContent);
            if (ret <= 0) {
                logger.warn("修改文章内容失败 requestJson{} id{}", requestJsonVO.getEntityJson(), articleVO.getId());
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
            }

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

        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        } finally {
            skylarkLock.unLock(ArticleLockKey.getUpdateLockKey(lockKey), lockKey);
        }
        return resultObjectVO;
    }

    public ResultObjectVO deleteById(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if (requestJsonVO == null) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("没有找到请求对象");
            return resultObjectVO;
        }
        if (StringUtils.isEmpty(requestJsonVO.getAppCode())) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("没有找到应用编码");
            return resultObjectVO;
        }

        ArticleVO articleVO = requestJsonVO.formatEntity(ArticleVO.class);
        if (articleVO.getId() == null) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("文章ID不能为空");
            return resultObjectVO;
        }
        if (StringUtils.isEmpty(articleVO.getAppCode())) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("所属应用不能为空");
            return resultObjectVO;
        }
        try {

            int ret = articleService.deleteById(articleVO.getId());
            if (ret <= 0) {
                logger.warn("删除文章失败 requestJson{} id{}", requestJsonVO.getEntityJson(), articleVO.getId());
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
                return resultObjectVO;
            }

            ret = articleContentService.deleteByArticleId(articleVO.getId());
            if (ret <= 0) {
                logger.warn("删除文章内容失败 requestJson{} id{}", requestJsonVO.getEntityJson(), articleVO.getId());
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
                return resultObjectVO;
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
            resultObjectVO.setData(articleVO);

        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    public ResultObjectVO deleteByIds(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if (requestJsonVO == null) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("没有找到请求对象");
            return resultObjectVO;
        }
        if (StringUtils.isEmpty(requestJsonVO.getAppCode())) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("没有找到应用编码");
            return resultObjectVO;
        }

        try {
            List<ArticleVO> articleVOS = requestJsonVO.formatEntityList(ArticleVO.class);
            if (CollectionUtils.isEmpty(articleVOS)) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到ID");
                return resultObjectVO;
            }

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

        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

}
