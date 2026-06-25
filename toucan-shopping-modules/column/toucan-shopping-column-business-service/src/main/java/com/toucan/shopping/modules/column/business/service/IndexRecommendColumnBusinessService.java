package com.toucan.shopping.modules.column.business.service;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.column.entity.ColumnArea;
import com.toucan.shopping.modules.column.entity.ColumnBanner;
import com.toucan.shopping.modules.column.entity.ColumnRecommendLabel;
import com.toucan.shopping.modules.column.entity.ColumnRecommendProduct;
import com.toucan.shopping.modules.column.page.ColumnPageInfo;
import com.toucan.shopping.modules.column.redis.ColumnLockKey;
import com.toucan.shopping.modules.column.service.*;
import com.toucan.shopping.modules.column.vo.*;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class IndexRecommendColumnBusinessService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ColumnService columnService;

    @Autowired
    private ColumnBannerService columnBannerService;

    @Autowired
    private ColumnRecommendLabelService columnRecommendLabelService;

    @Autowired
    private ColumnRecommendProductService columnRecommendProductService;

    @Autowired
    private ColumnAreaService columnAreaService;

    @Autowired
    private SkylarkLock skylarkLock;

    @Autowired
    private IdGenerator idGenerator;

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
            ColumnPageInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), ColumnPageInfo.class);
            PageInfo<ColumnVO> pageInfo = columnService.queryListPage(queryPageInfo);
            resultObjectVO.setData(pageInfo);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }

    @Transactional
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
        PcIndexColumnVO indexRecommendColumnVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), PcIndexColumnVO.class);
        if (StringUtils.isEmpty(indexRecommendColumnVO.getTitle())) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("栏目标题不能为空");
            return resultObjectVO;
        }
        if (StringUtils.isEmpty(indexRecommendColumnVO.getColumnTypeCode())) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("栏目类型编码不能为空");
            return resultObjectVO;
        }
        if (StringUtils.isEmpty(indexRecommendColumnVO.getAppCode())) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("所属应用不能为空");
            return resultObjectVO;
        }
        String lockKey = indexRecommendColumnVO.getAppCode() + "_" + indexRecommendColumnVO.getColumnTypeCode();
        try {
            boolean lockStatus = skylarkLock.lock(ColumnLockKey.getSaveLockKey(lockKey), lockKey);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
                return resultObjectVO;
            }

            ColumnVO query = new ColumnVO();
            query.setTitle(indexRecommendColumnVO.getTitle());
            query.setColumnTypeCode(indexRecommendColumnVO.getColumnTypeCode());
            query.setAppCode(indexRecommendColumnVO.getAppCode());
            List<ColumnVO> columnVOS = columnService.queryList(query);
            if (!CollectionUtils.isEmpty(columnVOS)) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("该栏目已存在");
                return resultObjectVO;
            }

            indexRecommendColumnVO.setId(idGenerator.id());
            indexRecommendColumnVO.setDeleteStatus((short) 0);
            indexRecommendColumnVO.setCreateDate(new Date());
            int ret = columnService.save(indexRecommendColumnVO);
            if (ret <= 0) {
                logger.warn("保存栏目失败 requestJson{} id{}", requestJsonVO.getEntityJson(), indexRecommendColumnVO.getId());
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
            }


            List<ColumnBanner> columnBanners = new LinkedList<>();

            //顶部图片预览
            ColumnBanner topBanner = new ColumnBanner();
            BeanUtils.copyProperties(topBanner, indexRecommendColumnVO.getTopBanner());
            topBanner.setId(idGenerator.id());
            topBanner.setColumnId(indexRecommendColumnVO.getId());
            topBanner.setPosition(1);
            topBanner.setCreateDate(new Date());
            topBanner.setCreateAdminId(indexRecommendColumnVO.getCreateAdminId());
            topBanner.setAppCode(indexRecommendColumnVO.getAppCode());
            topBanner.setBannerSort(0);
            columnBanners.add(topBanner);

            List<ColumnBannerVO> columnLeftBannerVOS = indexRecommendColumnVO.getColumnLeftBannerVOS();


            //左侧顶部轮播图
            if (!CollectionUtils.isEmpty(columnLeftBannerVOS)) {
                for (ColumnBannerVO columnBannerVO : columnLeftBannerVOS) {
                    ColumnBanner columnBanner = new ColumnBanner();
                    BeanUtils.copyProperties(columnBanner, columnBannerVO);
                    columnBanner.setId(idGenerator.id());
                    columnBanner.setColumnId(indexRecommendColumnVO.getId());
                    columnBanner.setPosition(2);
                    columnBanner.setCreateDate(new Date());
                    columnBanner.setCreateAdminId(indexRecommendColumnVO.getCreateAdminId());
                    columnBanner.setAppCode(indexRecommendColumnVO.getAppCode());
                    columnBanner.setBannerSort(0);
                    columnBanners.add(columnBanner);
                }
            }

            //右侧顶部图片预览
            ColumnBanner rightTopBanner = new ColumnBanner();
            BeanUtils.copyProperties(rightTopBanner, indexRecommendColumnVO.getRightTopBanner());
            rightTopBanner.setId(idGenerator.id());
            rightTopBanner.setColumnId(indexRecommendColumnVO.getId());
            rightTopBanner.setPosition(3);
            rightTopBanner.setCreateDate(new Date());
            rightTopBanner.setCreateAdminId(indexRecommendColumnVO.getCreateAdminId());
            rightTopBanner.setAppCode(indexRecommendColumnVO.getAppCode());
            rightTopBanner.setBannerSort(0);
            columnBanners.add(rightTopBanner);


            //右侧底部图片预览
            ColumnBanner rightBottomBanner = new ColumnBanner();
            BeanUtils.copyProperties(rightBottomBanner, indexRecommendColumnVO.getRightBottomBanner());
            rightBottomBanner.setId(idGenerator.id());
            rightBottomBanner.setColumnId(indexRecommendColumnVO.getId());
            rightBottomBanner.setPosition(4);
            rightBottomBanner.setCreateDate(new Date());
            rightBottomBanner.setCreateAdminId(indexRecommendColumnVO.getCreateAdminId());
            rightBottomBanner.setAppCode(indexRecommendColumnVO.getAppCode());
            rightBottomBanner.setBannerSort(0);
            columnBanners.add(rightBottomBanner);


            //底部图片预览
            ColumnBanner bottomBanner = new ColumnBanner();
            BeanUtils.copyProperties(bottomBanner, indexRecommendColumnVO.getBottomBanner());
            bottomBanner.setId(idGenerator.id());
            bottomBanner.setColumnId(indexRecommendColumnVO.getId());
            bottomBanner.setPosition(5);
            bottomBanner.setCreateDate(new Date());
            bottomBanner.setCreateAdminId(indexRecommendColumnVO.getCreateAdminId());
            bottomBanner.setAppCode(indexRecommendColumnVO.getAppCode());
            bottomBanner.setBannerSort(0);
            columnBanners.add(bottomBanner);


            //保存栏目轮播图
            columnBannerService.saves(columnBanners);


            List<ColumnRecommendLabel> columnRecommendLabels = new LinkedList<>();


            //栏目顶部标签
            if (!CollectionUtils.isEmpty(indexRecommendColumnVO.getTopLabels())) {
                for (ColumnRecommendLabelVO columnRecommendLabelVO : indexRecommendColumnVO.getTopLabels()) {
                    ColumnRecommendLabel columnRecommendLabel = new ColumnRecommendLabel();
                    BeanUtils.copyProperties(columnRecommendLabel, columnRecommendLabelVO);
                    columnRecommendLabel.setId(idGenerator.id());
                    columnRecommendLabel.setColumnId(indexRecommendColumnVO.getId());
                    columnRecommendLabel.setCreateAdminId(indexRecommendColumnVO.getCreateAdminId());
                    columnRecommendLabel.setCreateDate(new Date());
                    columnRecommendLabel.setAppCode(indexRecommendColumnVO.getAppCode());
                    columnRecommendLabel.setPosition((short) 1);
                    columnRecommendLabel.setLabelSort(0L);

                    columnRecommendLabels.add(columnRecommendLabel);

                }
            }

            //栏目左侧标签
            if (!CollectionUtils.isEmpty(indexRecommendColumnVO.getLeftLabels())) {
                for (ColumnRecommendLabelVO columnRecommendLabelVO : indexRecommendColumnVO.getLeftLabels()) {
                    ColumnRecommendLabel columnRecommendLabel = new ColumnRecommendLabel();
                    BeanUtils.copyProperties(columnRecommendLabel, columnRecommendLabelVO);
                    columnRecommendLabel.setId(idGenerator.id());
                    columnRecommendLabel.setColumnId(indexRecommendColumnVO.getId());
                    columnRecommendLabel.setCreateAdminId(indexRecommendColumnVO.getCreateAdminId());
                    columnRecommendLabel.setCreateDate(new Date());
                    columnRecommendLabel.setAppCode(indexRecommendColumnVO.getAppCode());
                    columnRecommendLabel.setPosition((short) 2);
                    columnRecommendLabel.setLabelSort(0L);

                    columnRecommendLabels.add(columnRecommendLabel);

                }
            }

            //保存栏目推荐标签
            if (!CollectionUtils.isEmpty(columnRecommendLabels)) {
                columnRecommendLabelService.saves(columnRecommendLabels);
            }


            List<ColumnArea> columnAreas = new LinkedList<>();
            //保存栏目地区
            if (!CollectionUtils.isEmpty(indexRecommendColumnVO.getAreaCodeList())) {
                for (int i = 0; i < indexRecommendColumnVO.getAreaCodeList().size(); i++) {
                    String areaCode = indexRecommendColumnVO.getAreaCodeList().get(i);
                    String areaName = indexRecommendColumnVO.getAreaNameList().get(i);
                    ColumnArea columnArea = new ColumnArea();
                    columnArea.setId(idGenerator.id());
                    columnArea.setColumnId(indexRecommendColumnVO.getId());
                    columnArea.setCreateAdminId(indexRecommendColumnVO.getCreateAdminId());
                    columnArea.setCreateDate(new Date());
                    columnArea.setAreaCode(areaCode);
                    columnArea.setAreaName(areaName);
                    columnArea.setAppCode(indexRecommendColumnVO.getAppCode());
                    columnAreas.add(columnArea);
                }
            }

            //保存栏目地区关联
            columnAreaService.saves(columnAreas);

            //保存商品推荐
            List<ColumnRecommendProduct> columnRecommendProducts = new LinkedList<>();
            if (!CollectionUtils.isEmpty(indexRecommendColumnVO.getColumnRecommendProducts())) {
                for (ColumnRecommendProductVO columnRecommendProductVO : indexRecommendColumnVO.getColumnRecommendProducts()) {
                    ColumnRecommendProduct columnRecommendProduct = new ColumnRecommendProduct();
                    BeanUtils.copyProperties(columnRecommendProduct, columnRecommendProductVO);
                    columnRecommendProduct.setId(idGenerator.id());
                    columnRecommendProduct.setColumnId(indexRecommendColumnVO.getId());
                    columnRecommendProduct.setCreateAdminId(indexRecommendColumnVO.getCreateAdminId());
                    columnRecommendProduct.setCreateDate(new Date());
                    columnRecommendProduct.setProductSort(0L);
                    columnRecommendProduct.setAppCode(indexRecommendColumnVO.getAppCode());
                    columnRecommendProducts.add(columnRecommendProduct);
                }
            }
            columnRecommendProductService.saves(columnRecommendProducts);


            resultObjectVO.setData(indexRecommendColumnVO);

        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        } finally {
            skylarkLock.unLock(ColumnLockKey.getSaveLockKey(lockKey), lockKey);
        }
        return resultObjectVO;
    }

    @Transactional
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
        PcIndexColumnVO indexRecommendColumnVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), PcIndexColumnVO.class);
        if (StringUtils.isEmpty(indexRecommendColumnVO.getTitle())) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("栏目标题不能为空");
            return resultObjectVO;
        }
        if (indexRecommendColumnVO.getId() == null) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("栏目ID不能为空");
            return resultObjectVO;
        }
        if (StringUtils.isEmpty(indexRecommendColumnVO.getAppCode())) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("所属应用不能为空");
            return resultObjectVO;
        }
        String lockKey = indexRecommendColumnVO.getAppCode() + "_" + indexRecommendColumnVO.getColumnTypeCode();
        try {
            boolean lockStatus = skylarkLock.lock(ColumnLockKey.getUpdateLockKey(lockKey), lockKey);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
                return resultObjectVO;
            }

            ColumnVO query = new ColumnVO();
            query.setTitle(indexRecommendColumnVO.getTitle());
            query.setColumnTypeCode(indexRecommendColumnVO.getColumnTypeCode());
            query.setAppCode(indexRecommendColumnVO.getAppCode());
            List<ColumnVO> columnVOS = columnService.queryList(query);
            if (!CollectionUtils.isEmpty(columnVOS)) {
                if (columnVOS.get(0).getId().longValue() != indexRecommendColumnVO.getId().longValue()) {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("该栏目已存在");
                    return resultObjectVO;
                }
            }

            indexRecommendColumnVO.setUpdateDate(new Date());
            int ret = columnService.update(indexRecommendColumnVO);
            if (ret <= 0) {
                logger.warn("修改栏目失败 requestJson{} id{}", requestJsonVO.getEntityJson(), indexRecommendColumnVO.getId());
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
            }


            //删除栏目轮播图
            columnBannerService.deleteByColumnId(indexRecommendColumnVO.getId());

            List<ColumnBanner> columnBanners = new LinkedList<>();


            //顶部图片预览
            ColumnBanner topBanner = new ColumnBanner();
            BeanUtils.copyProperties(topBanner, indexRecommendColumnVO.getTopBanner());
            topBanner.setId(idGenerator.id());
            topBanner.setColumnId(indexRecommendColumnVO.getId());
            topBanner.setPosition(1);
            topBanner.setCreateDate(new Date());
            topBanner.setCreateAdminId(indexRecommendColumnVO.getCreateAdminId());
            topBanner.setAppCode(indexRecommendColumnVO.getAppCode());
            topBanner.setBannerSort(0);
            columnBanners.add(topBanner);

            List<ColumnBannerVO> columnLeftBannerVOS = indexRecommendColumnVO.getColumnLeftBannerVOS();
            //左侧顶部轮播图
            if (!CollectionUtils.isEmpty(columnLeftBannerVOS)) {
                for (ColumnBannerVO columnBannerVO : columnLeftBannerVOS) {
                    ColumnBanner columnBanner = new ColumnBanner();
                    BeanUtils.copyProperties(columnBanner, columnBannerVO);
                    columnBanner.setId(idGenerator.id());
                    columnBanner.setColumnId(indexRecommendColumnVO.getId());
                    columnBanner.setPosition(2);
                    columnBanner.setCreateDate(new Date());
                    columnBanner.setCreateAdminId(indexRecommendColumnVO.getCreateAdminId());
                    columnBanner.setAppCode(indexRecommendColumnVO.getAppCode());
                    columnBanner.setBannerSort(0);
                    columnBanners.add(columnBanner);
                }
            }

            //右侧顶部图片预览
            ColumnBanner rightTopBanner = new ColumnBanner();
            BeanUtils.copyProperties(rightTopBanner, indexRecommendColumnVO.getRightTopBanner());
            rightTopBanner.setId(idGenerator.id());
            rightTopBanner.setColumnId(indexRecommendColumnVO.getId());
            rightTopBanner.setPosition(3);
            rightTopBanner.setCreateDate(new Date());
            rightTopBanner.setCreateAdminId(indexRecommendColumnVO.getCreateAdminId());
            rightTopBanner.setAppCode(indexRecommendColumnVO.getAppCode());
            rightTopBanner.setBannerSort(0);
            columnBanners.add(rightTopBanner);


            //右侧底部图片预览
            ColumnBanner rightBottomBanner = new ColumnBanner();
            BeanUtils.copyProperties(rightBottomBanner, indexRecommendColumnVO.getRightBottomBanner());
            rightBottomBanner.setId(idGenerator.id());
            rightBottomBanner.setColumnId(indexRecommendColumnVO.getId());
            rightBottomBanner.setPosition(4);
            rightBottomBanner.setCreateDate(new Date());
            rightBottomBanner.setCreateAdminId(indexRecommendColumnVO.getCreateAdminId());
            rightBottomBanner.setAppCode(indexRecommendColumnVO.getAppCode());
            rightBottomBanner.setBannerSort(0);
            columnBanners.add(rightBottomBanner);

            //底部图片预览
            ColumnBanner bottomBanner = new ColumnBanner();
            BeanUtils.copyProperties(bottomBanner, indexRecommendColumnVO.getBottomBanner());
            bottomBanner.setId(idGenerator.id());
            bottomBanner.setColumnId(indexRecommendColumnVO.getId());
            bottomBanner.setPosition(5);
            bottomBanner.setCreateDate(new Date());
            bottomBanner.setCreateAdminId(indexRecommendColumnVO.getCreateAdminId());
            bottomBanner.setAppCode(indexRecommendColumnVO.getAppCode());
            bottomBanner.setBannerSort(0);
            columnBanners.add(bottomBanner);


            //保存栏目轮播图
            columnBannerService.saves(columnBanners);


            //删除栏目标签
            columnRecommendLabelService.deleteByColumnId(indexRecommendColumnVO.getId());

            List<ColumnRecommendLabel> columnRecommendLabels = new LinkedList<>();
            //栏目顶部标签
            if (!CollectionUtils.isEmpty(indexRecommendColumnVO.getTopLabels())) {
                for (ColumnRecommendLabelVO columnRecommendLabelVO : indexRecommendColumnVO.getTopLabels()) {
                    ColumnRecommendLabel columnRecommendLabel = new ColumnRecommendLabel();
                    BeanUtils.copyProperties(columnRecommendLabel, columnRecommendLabelVO);
                    columnRecommendLabel.setId(idGenerator.id());
                    columnRecommendLabel.setColumnId(indexRecommendColumnVO.getId());
                    columnRecommendLabel.setCreateAdminId(indexRecommendColumnVO.getCreateAdminId());
                    columnRecommendLabel.setCreateDate(new Date());
                    columnRecommendLabel.setAppCode(indexRecommendColumnVO.getAppCode());
                    columnRecommendLabel.setPosition((short) 1);
                    columnRecommendLabel.setLabelSort(0L);

                    columnRecommendLabels.add(columnRecommendLabel);

                }
            }

            //栏目左侧标签
            if (!CollectionUtils.isEmpty(indexRecommendColumnVO.getLeftLabels())) {
                for (ColumnRecommendLabelVO columnRecommendLabelVO : indexRecommendColumnVO.getLeftLabels()) {
                    ColumnRecommendLabel columnRecommendLabel = new ColumnRecommendLabel();
                    BeanUtils.copyProperties(columnRecommendLabel, columnRecommendLabelVO);
                    columnRecommendLabel.setId(idGenerator.id());
                    columnRecommendLabel.setColumnId(indexRecommendColumnVO.getId());
                    columnRecommendLabel.setCreateAdminId(indexRecommendColumnVO.getCreateAdminId());
                    columnRecommendLabel.setCreateDate(new Date());
                    columnRecommendLabel.setAppCode(indexRecommendColumnVO.getAppCode());
                    columnRecommendLabel.setPosition((short) 2);
                    columnRecommendLabel.setLabelSort(0L);

                    columnRecommendLabels.add(columnRecommendLabel);

                }
            }

            //保存栏目推荐标签
            if (!CollectionUtils.isEmpty(columnRecommendLabels)) {
                columnRecommendLabelService.saves(columnRecommendLabels);
            }

            //删除栏目地区
            columnAreaService.deleteByColumnId(indexRecommendColumnVO.getId());

            List<ColumnArea> columnAreas = new LinkedList<>();
            //保存栏目地区
            if (!CollectionUtils.isEmpty(indexRecommendColumnVO.getAreaCodeList())) {
                for (int i = 0; i < indexRecommendColumnVO.getAreaCodeList().size(); i++) {
                    String areaCode = indexRecommendColumnVO.getAreaCodeList().get(i);
                    String areaName = indexRecommendColumnVO.getAreaNameList().get(i);
                    ColumnArea columnArea = new ColumnArea();
                    columnArea.setId(idGenerator.id());
                    columnArea.setColumnId(indexRecommendColumnVO.getId());
                    columnArea.setCreateAdminId(indexRecommendColumnVO.getCreateAdminId());
                    columnArea.setCreateDate(new Date());
                    columnArea.setAreaCode(areaCode);
                    columnArea.setAreaName(areaName);
                    columnArea.setAppCode(indexRecommendColumnVO.getAppCode());
                    columnAreas.add(columnArea);
                }
            }

            //保存栏目地区关联
            columnAreaService.saves(columnAreas);


            //删除栏目推荐商品
            columnRecommendProductService.deleteByColumnId(indexRecommendColumnVO.getId());

            //保存商品推荐
            List<ColumnRecommendProduct> columnRecommendProducts = new LinkedList<>();
            if (!CollectionUtils.isEmpty(indexRecommendColumnVO.getColumnRecommendProducts())) {
                for (ColumnRecommendProductVO columnRecommendProductVO : indexRecommendColumnVO.getColumnRecommendProducts()) {
                    ColumnRecommendProduct columnRecommendProduct = new ColumnRecommendProduct();
                    BeanUtils.copyProperties(columnRecommendProduct, columnRecommendProductVO);
                    columnRecommendProduct.setId(idGenerator.id());
                    columnRecommendProduct.setColumnId(indexRecommendColumnVO.getId());
                    columnRecommendProduct.setCreateAdminId(indexRecommendColumnVO.getCreateAdminId());
                    columnRecommendProduct.setCreateDate(new Date());
                    columnRecommendProduct.setProductSort(0L);
                    columnRecommendProduct.setAppCode(indexRecommendColumnVO.getAppCode());
                    columnRecommendProducts.add(columnRecommendProduct);
                }
            }
            columnRecommendProductService.saves(columnRecommendProducts);


            resultObjectVO.setData(indexRecommendColumnVO);

        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        } finally {
            skylarkLock.unLock(ColumnLockKey.getUpdateLockKey(lockKey), lockKey);
        }
        return resultObjectVO;
    }

    public ResultObjectVO deleteById(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if (requestJsonVO == null) {
            logger.info("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }
        if (requestJsonVO.getAppCode() == null) {
            logger.info("没有找到应用编码: param:" + JSONObject.toJSONString(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到应用编码!");
            return resultObjectVO;
        }

        try {
            PcIndexColumnVO indexRecommendColumnVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), PcIndexColumnVO.class);

            if (indexRecommendColumnVO.getId() == null) {
                logger.info("ID为空 param:" + JSONObject.toJSONString(indexRecommendColumnVO));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("ID不能为空!");
                return resultObjectVO;
            }


            int ret = columnService.deleteById(indexRecommendColumnVO.getId());
            if (ret <= 0) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("不存在该栏目!");
                return resultObjectVO;
            }

            columnBannerService.deleteByColumnId(indexRecommendColumnVO.getId());
            columnAreaService.deleteByColumnId(indexRecommendColumnVO.getId());
            columnRecommendLabelService.deleteByColumnId(indexRecommendColumnVO.getId());
            columnRecommendProductService.deleteByColumnId(indexRecommendColumnVO.getId());


        } catch (Exception e) {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }

    public ResultObjectVO queryPcIndexColumns(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if (requestVo == null || requestVo.getEntityJson() == null) {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }
        if (requestVo == null || requestVo.getAppCode() == null) {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到应用编码");
            return resultObjectVO;
        }
        try {
            ColumnVO columnVO = requestVo.formatEntity(ColumnVO.class);
            List<PcIndexColumnVO> indexRecommendColumnVOS = columnService.queryPcIndexColumns(columnVO);
            List<Long> columnIds = new LinkedList<>();
            if (!CollectionUtils.isEmpty(indexRecommendColumnVOS)) {
                for (PcIndexColumnVO indexRecommendColumnVO : indexRecommendColumnVOS) {
                    columnIds.add(indexRecommendColumnVO.getId());

                    indexRecommendColumnVO.setColumnLeftBannerVOS(new LinkedList<>());
                    indexRecommendColumnVO.setTopLabels(new LinkedList<>());
                    indexRecommendColumnVO.setLeftLabels(new LinkedList<>());
                    indexRecommendColumnVO.setColumnRecommendProducts(new LinkedList<>());
                    indexRecommendColumnVO.setColumnAreas(new LinkedList<>());
                }
            }
            if (!CollectionUtils.isEmpty(columnIds)) {

                //查询轮播图以及预览图相关
                List<ColumnBannerVO> columnBannerVOS = columnBannerService.queryListByColumnIds(columnIds);
                if (!CollectionUtils.isEmpty(columnBannerVOS)) {
                    for (PcIndexColumnVO indexRecommendColumnVO : indexRecommendColumnVOS) {
                        for (ColumnBannerVO columnBannerVO : columnBannerVOS) {
                            if (indexRecommendColumnVO.getId().longValue() == columnBannerVO.getColumnId().longValue()) {
                                //左侧顶部
                                if (columnBannerVO.getPosition().intValue() == 1) {
                                    indexRecommendColumnVO.setTopBanner(columnBannerVO);
                                } else if (columnBannerVO.getPosition().intValue() == 2) {
                                    indexRecommendColumnVO.getColumnLeftBannerVOS().add(columnBannerVO);
                                } else if (columnBannerVO.getPosition().intValue() == 3)  //右侧顶部
                                {
                                    indexRecommendColumnVO.setRightTopBanner(columnBannerVO);
                                } else if (columnBannerVO.getPosition().intValue() == 4)  //右侧底部
                                {
                                    indexRecommendColumnVO.setRightBottomBanner(columnBannerVO);
                                } else if (columnBannerVO.getPosition().intValue() == 5)  //底部
                                {
                                    indexRecommendColumnVO.setBottomBanner(columnBannerVO);
                                }
                            }
                        }
                    }
                }


                //查询栏目标签
                List<ColumnRecommendLabelVO> columnRecommendLabelVOS = columnRecommendLabelService.queryListByColumnIds(columnIds);
                if (!CollectionUtils.isEmpty(columnRecommendLabelVOS)) {
                    for (PcIndexColumnVO indexRecommendColumnVO : indexRecommendColumnVOS) {
                        for (ColumnRecommendLabelVO columnRecommendLabelVO : columnRecommendLabelVOS) {
                            if (indexRecommendColumnVO.getId().longValue() == columnRecommendLabelVO.getColumnId().longValue()) {
                                //顶部标签
                                if (columnRecommendLabelVO.getPosition().intValue() == 1) {
                                    indexRecommendColumnVO.getTopLabels().add(columnRecommendLabelVO);
                                } else if (columnRecommendLabelVO.getPosition().intValue() == 2) //左侧
                                {
                                    indexRecommendColumnVO.getLeftLabels().add(columnRecommendLabelVO);
                                }
                            }
                        }
                    }
                }


                //查询推荐商品
                List<ColumnRecommendProductVO> columnRecommendProductVOS = columnRecommendProductService.queryListCreateDateAscByColumnIds(columnIds);
                if (!CollectionUtils.isEmpty(columnRecommendProductVOS)) {
                    for (PcIndexColumnVO indexRecommendColumnVO : indexRecommendColumnVOS) {
                        for (int i = 0; i < columnRecommendProductVOS.size(); i++) {
                            ColumnRecommendProductVO columnRecommendProductVO = columnRecommendProductVOS.get(i);
                            if (indexRecommendColumnVO.getId().longValue() == columnRecommendProductVO.getColumnId().longValue()) {
                                indexRecommendColumnVO.getColumnRecommendProducts().add(columnRecommendProductVO);
                            }
                        }
                    }
                }

                //查询栏目地区
                List<ColumnAreaVO> columnAreaVOS = columnAreaService.queryListByColumnIds(columnIds);
                if (!CollectionUtils.isEmpty(columnAreaVOS)) {
                    for (PcIndexColumnVO indexRecommendColumnVO : indexRecommendColumnVOS) {
                        for (ColumnAreaVO columnAreaVO : columnAreaVOS) {
                            if (indexRecommendColumnVO.getId().longValue() == columnAreaVO.getColumnId().longValue()) {
                                indexRecommendColumnVO.getColumnAreas().add(columnAreaVO);
                            }
                        }
                    }
                }

            }

            resultObjectVO.setData(indexRecommendColumnVOS);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    public ResultObjectVO findById(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if (requestVo == null || requestVo.getEntityJson() == null) {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            ColumnVO columnVO = JSONObject.parseObject(requestVo.getEntityJson(), ColumnVO.class);
            if (columnVO.getId() == null) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到ID");
                return resultObjectVO;
            }

            //查询是否存在该对象
            columnVO = columnService.findById(columnVO.getId());
            if (columnVO == null) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("不存在!");
                return resultObjectVO;
            }

            PcIndexColumnVO indexRecommendColumnVO = new PcIndexColumnVO();
            BeanUtils.copyProperties(indexRecommendColumnVO, columnVO);

            //查询轮播图以及预览图相关
            List<ColumnBannerVO> columnBannerVOS = columnBannerService.queryListByColumnId(indexRecommendColumnVO.getId());
            if (!CollectionUtils.isEmpty(columnBannerVOS)) {
                indexRecommendColumnVO.setColumnLeftBannerVOS(new LinkedList<>());
                for (ColumnBannerVO columnBannerVO : columnBannerVOS) {
                    //左侧顶部
                    if (columnBannerVO.getPosition().intValue() == 1) {
                        indexRecommendColumnVO.setTopBanner(columnBannerVO);
                    } else if (columnBannerVO.getPosition().intValue() == 2) {
                        indexRecommendColumnVO.getColumnLeftBannerVOS().add(columnBannerVO);
                    } else if (columnBannerVO.getPosition().intValue() == 3)  //右侧顶部
                    {
                        indexRecommendColumnVO.setRightTopBanner(columnBannerVO);
                    } else if (columnBannerVO.getPosition().intValue() == 4)  //右侧底部
                    {
                        indexRecommendColumnVO.setRightBottomBanner(columnBannerVO);
                    } else if (columnBannerVO.getPosition().intValue() == 5)  //底部
                    {
                        indexRecommendColumnVO.setBottomBanner(columnBannerVO);
                    }
                }
            }

            //查询栏目标签
            List<ColumnRecommendLabelVO> columnRecommendLabelVOS = columnRecommendLabelService.queryListByColumnId(indexRecommendColumnVO.getId());
            if (!CollectionUtils.isEmpty(columnRecommendLabelVOS)) {

                indexRecommendColumnVO.setTopLabels(new LinkedList<>());
                indexRecommendColumnVO.setLeftLabels(new LinkedList<>());
                for (ColumnRecommendLabelVO columnRecommendLabelVO : columnRecommendLabelVOS) {
                    //顶部标签
                    if (columnRecommendLabelVO.getPosition().intValue() == 1) {
                        indexRecommendColumnVO.getTopLabels().add(columnRecommendLabelVO);
                    } else if (columnRecommendLabelVO.getPosition().intValue() == 2) //左侧
                    {
                        indexRecommendColumnVO.getLeftLabels().add(columnRecommendLabelVO);
                    }
                }
            }

            //查询推荐商品
            List<ColumnRecommendProductVO> columnRecommendProductVOS = columnRecommendProductService.queryListCreateDateAscByColumnId(indexRecommendColumnVO.getId());
            indexRecommendColumnVO.setColumnRecommendProducts(columnRecommendProductVOS);

            //查询栏目地区
            List<ColumnAreaVO> columnAreaVOS = columnAreaService.queryListByColumnId(indexRecommendColumnVO.getId());
            indexRecommendColumnVO.setColumnAreas(columnAreaVOS);

            resultObjectVO.setData(indexRecommendColumnVO);

        } catch (Exception e) {
            logger.warn(e.getMessage(), e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

}
