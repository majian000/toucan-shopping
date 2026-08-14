package com.toucan.shopping.modules.content.business.service;

import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.modules.common.annotation.RequestCheck;
import com.toucan.shopping.modules.common.exception.BusinessValidationException;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.util.Check;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.content.entity.ArticleImage;
import com.toucan.shopping.modules.content.page.ArticleImagePageInfo;
import com.toucan.shopping.modules.content.page.DeleteArticleImagePageInfo;
import com.toucan.shopping.modules.content.service.ArticleImageService;
import com.toucan.shopping.modules.content.vo.ArticleImageVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
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
public class ArticleImageBusinessService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private ArticleImageService articleImageService;

    @Autowired
    private SkylarkLock skylarkLock;

    @RequestCheck(requireEntity = true)
    public ResultObjectVO save(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        ArticleImageVO articleVO = requestJsonVO.formatEntity(ArticleImageVO.class);
        Check.notEmpty(articleVO.getAppCode(), ResultObjectVO.FAILD, "所属应用不能为空");
        try {
            articleVO.setId(idGenerator.id());
            articleVO.setDeleteStatus((short) 0);
            articleVO.setCreateDate(new Date());
            articleVO.setFileDeleteStatus((short) 0);
            int ret = articleImageService.save(articleVO);
            if (ret <= 0) {
                logger.warn("保存文章图片失败 requestJson{} id{}", requestJsonVO.getEntityJson(), articleVO.getId());
                return ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
            }
            resultObjectVO.setData(articleVO);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    @RequestCheck(requireEntity = true)
    public ResultObjectVO deleteInvalidData(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        DeleteArticleImagePageInfo deleteArticleImagePageInfo = requestJsonVO.formatEntity(DeleteArticleImagePageInfo.class);
        Check.notNull(deleteArticleImagePageInfo.getEndDate(), ResultObjectVO.FAILD, "截止时间不能为空");
        try {
            PageInfo pageInfo = null;
            int page = 1;
            int limit = 100;
            DeleteArticleImagePageInfo query = new DeleteArticleImagePageInfo();
            query.setEndDate(deleteArticleImagePageInfo.getEndDate());
            query.setLimit(limit);
            do {
                logger.info(" 查询无效文章图片列表 页码:{} 每页显示 {} ", page, limit);
                query.setPage(page);
                pageInfo = articleImageService.queryInvalidListPage(query);
                page++;
                if (!CollectionUtils.isEmpty(pageInfo.getList())) {
                    List<ArticleImage> articleImageList = pageInfo.getList();
                    List<Long> successArticleImageIdList = new LinkedList<>();
                    List<Long> faildArticleImageIdList = new LinkedList<>();
                    for (ArticleImage articleImage : articleImageList) {
                        if (StringUtils.isNotEmpty(articleImage.getImgPath())) {
                            logger.info("删除图片 {} ", articleImage.getImgPath());
                            int ret = imageUploadService.deleteFile(articleImage.getImgPath());
                            if (ret == 0) {
                                successArticleImageIdList.add(articleImage.getId());
                            } else {
                                faildArticleImageIdList.add(articleImage.getId());
                            }
                        } else {
                            faildArticleImageIdList.add(articleImage.getId());
                        }
                    }
                    if (CollectionUtils.isNotEmpty(successArticleImageIdList)) {
                        articleImageService.updateFileDeleteStatusByIdList(successArticleImageIdList, 1, "-1");
                        articleImageService.deleteByIdList(successArticleImageIdList, "定时任务-删除成功");
                    }

                    if (CollectionUtils.isNotEmpty(faildArticleImageIdList)) {
                        articleImageService.updateFileDeleteStatusByIdList(successArticleImageIdList, 0, "-1");
                        articleImageService.deleteByIdList(faildArticleImageIdList, "定时任务-删除失败");
                    }
                }
            } while (pageInfo != null && !CollectionUtils.isEmpty(pageInfo.getList()));

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }

        return resultObjectVO;
    }

    @RequestCheck(requireEntity = true)
    public ResultObjectVO deleteById(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            ArticleImage entity = JSONObject.parseObject(requestVo.getEntityJson(), ArticleImage.class);
            Check.notNull(entity.getId(), ResultVO.FAILD, "没有找到ID");

            //查询是否存在该数据
            ArticleImage articleImage = articleImageService.findById(entity.getId());
            if (articleImage == null) {
                return ResultObjectVO.fail(ResultVO.FAILD, "图片不存在!");
            }

            int ret = imageUploadService.deleteFile(articleImage.getImgPath());
            if (ret != 0) {
                articleImageService.updateFileDeleteStatusById(articleImage.getId(), 0, articleImage.getUpdateAdminId());
                logger.info("文件中心图片删除失败 {} ", articleImage.getImgPath());
                return ResultObjectVO.fail(ResultVO.FAILD, "图片删除失败!");
            }

            int row = articleImageService.deleteById(articleImage.getId(), articleImage.getUpdateAdminId());
            if (row != 1) {
                return ResultObjectVO.fail(ResultVO.FAILD, "删除失败!");
            } else {
                articleImageService.updateFileDeleteStatusById(articleImage.getId(), 1, articleImage.getUpdateAdminId());
            }

            resultObjectVO.setData(entity);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    @RequestCheck(requireEntity = true)
    public ResultObjectVO deleteByIds(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            List<ArticleImage> articleImages = requestVo.formatEntityList(ArticleImage.class);
            if (CollectionUtils.isEmpty(articleImages)) {
                return ResultObjectVO.fail(ResultVO.FAILD, "没有找到ID");
            }
            List<ResultObjectVO> resultObjectVOList = new ArrayList<ResultObjectVO>();
            for (ArticleImage articleImage : articleImages) {
                if (articleImage.getId() != null) {
                    ResultObjectVO appResultObjectVO = new ResultObjectVO();
                    appResultObjectVO.setData(articleImage);
                    //查询是否存在该数据
                    ArticleImage articleImageEntity = articleImageService.findById(articleImage.getId());
                    if (articleImageEntity == null) {
                        appResultObjectVO.setCode(ResultVO.FAILD);
                        appResultObjectVO.setMsg("图片不存在!");
                    }
                    int ret = imageUploadService.deleteFile(articleImageEntity.getImgPath());
                    if (ret != 0) {
                        articleImageService.updateFileDeleteStatusById(articleImage.getId(), 0, articleImage.getUpdateAdminId());
                        articleImageService.deleteById(articleImageEntity.getId(), articleImageEntity.getUpdateAdminId());
                        logger.info("文件中心图片删除失败 {} ", articleImageEntity.getImgPath());
                        return resultObjectVO;
                    }

                    int row = articleImageService.deleteById(articleImageEntity.getId(), articleImageEntity.getUpdateAdminId());
                    if (row != 1) {
                        resultObjectVO.setCode(ResultVO.FAILD);
                        resultObjectVO.setMsg("删除失败!");
                    } else {
                        articleImageService.updateFileDeleteStatusById(articleImage.getId(), 1, articleImage.getUpdateAdminId());
                    }
                    resultObjectVOList.add(appResultObjectVO);
                }
            }
            resultObjectVO.setData(resultObjectVOList);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryListPage(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            ArticleImagePageInfo queryPageInfo = requestJsonVO.formatEntity(ArticleImagePageInfo.class);
            PageInfo<ArticleImageVO> pageInfo = articleImageService.queryListPage(queryPageInfo);
            resultObjectVO.setData(pageInfo);
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }

}
