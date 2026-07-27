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

    // ColumnBanner position constants
    private static final int BANNER_POS_TOP = 1;
    private static final int BANNER_POS_LEFT_CAROUSEL = 2;
    private static final int BANNER_POS_RIGHT_TOP = 3;
    private static final int BANNER_POS_RIGHT_BOTTOM = 4;
    private static final int BANNER_POS_BOTTOM = 5;

    // ColumnRecommendLabel position constants
    private static final short LABEL_POS_TOP = 1;
    private static final short LABEL_POS_LEFT = 2;

    // ColumnBanner/ColumnRecommendLabel default sort
    private static final int DEFAULT_BANNER_SORT = 0;
    private static final long DEFAULT_LABEL_SORT = 0L;
    private static final long DEFAULT_PRODUCT_SORT = 0L;

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

    // ==================== Public API ====================

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

            saveChildEntities(indexRecommendColumnVO);

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

            replaceChildEntities(indexRecommendColumnVO);

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

            deleteChildEntitiesByColumnId(indexRecommendColumnVO.getId());

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
        if (requestVo.getAppCode() == null) {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到应用编码");
            return resultObjectVO;
        }
        try {
            ColumnVO columnVO = requestVo.formatEntity(ColumnVO.class);
            List<PcIndexColumnVO> indexRecommendColumnVOS = columnService.queryPcIndexColumns(columnVO);
            List<Long> columnIds = new LinkedList<>();
            if (!CollectionUtils.isEmpty(indexRecommendColumnVOS)) {
                for (PcIndexColumnVO vo : indexRecommendColumnVOS) {
                    columnIds.add(vo.getId());
                    initEmptyChildLists(vo);
                }
            }
            if (!CollectionUtils.isEmpty(columnIds)) {
                populateColumnsBanners(indexRecommendColumnVOS, columnIds);
                populateColumnsLabels(indexRecommendColumnVOS, columnIds);
                populateColumnsProducts(indexRecommendColumnVOS, columnIds);
                populateColumnsAreas(indexRecommendColumnVOS, columnIds);
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

            columnVO = columnService.findById(columnVO.getId());
            if (columnVO == null) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("不存在!");
                return resultObjectVO;
            }

            PcIndexColumnVO indexRecommendColumnVO = new PcIndexColumnVO();
            BeanUtils.copyProperties(indexRecommendColumnVO, columnVO);

            populateSingleColumnBanners(indexRecommendColumnVO);
            populateSingleColumnLabels(indexRecommendColumnVO);
            populateSingleColumnProducts(indexRecommendColumnVO);
            populateSingleColumnAreas(indexRecommendColumnVO);

            resultObjectVO.setData(indexRecommendColumnVO);

        } catch (Exception e) {
            logger.warn(e.getMessage(), e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    // ==================== Child entity persistence ====================

    /**
     * Save all child entities for a newly created column.
     */
    private void saveChildEntities(PcIndexColumnVO vo) {
        columnBannerService.saves(buildAllBanners(vo));

        List<ColumnRecommendLabel> labels = buildAllLabels(vo);
        if (!CollectionUtils.isEmpty(labels)) {
            columnRecommendLabelService.saves(labels);
        }

        columnAreaService.saves(buildAreas(vo));
        columnRecommendProductService.saves(buildProducts(vo));
    }

    /**
     * Delete existing child entities then save new ones. Used in update path.
     */
    private void replaceChildEntities(PcIndexColumnVO vo) {
        deleteChildEntitiesByColumnId(vo.getId());
        saveChildEntities(vo);
    }

    /**
     * Delete all child entities associated with a column.
     */
    private void deleteChildEntitiesByColumnId(Long columnId) {
        columnBannerService.deleteByColumnId(columnId);
        columnAreaService.deleteByColumnId(columnId);
        columnRecommendLabelService.deleteByColumnId(columnId);
        columnRecommendProductService.deleteByColumnId(columnId);
    }

    // ==================== Child entity builders ====================

    /**
     * Build all ColumnBanner entities for a column VO.
     */
    private List<ColumnBanner> buildAllBanners(PcIndexColumnVO vo) {
        List<ColumnBanner> banners = new LinkedList<>();

        // top banner
        banners.add(buildBanner(vo, vo.getTopBanner(), BANNER_POS_TOP));

        // left carousel banners
        if (!CollectionUtils.isEmpty(vo.getColumnLeftBannerVOS())) {
            for (ColumnBannerVO bannerVO : vo.getColumnLeftBannerVOS()) {
                banners.add(buildBanner(vo, bannerVO, BANNER_POS_LEFT_CAROUSEL));
            }
        }

        // right-top banner
        banners.add(buildBanner(vo, vo.getRightTopBanner(), BANNER_POS_RIGHT_TOP));

        // right-bottom banner
        banners.add(buildBanner(vo, vo.getRightBottomBanner(), BANNER_POS_RIGHT_BOTTOM));

        // bottom banner
        banners.add(buildBanner(vo, vo.getBottomBanner(), BANNER_POS_BOTTOM));

        return banners;
    }

    /**
     * Build a single ColumnBanner from the source VO with common fields populated.
     */
    private ColumnBanner buildBanner(PcIndexColumnVO vo, ColumnBannerVO source, int position) {
        ColumnBanner banner = new ColumnBanner();
        try { BeanUtils.copyProperties(banner, source); } catch (Exception e) { throw new RuntimeException(e); }
        banner.setId(idGenerator.id());
        banner.setColumnId(vo.getId());
        banner.setPosition(position);
        banner.setCreateDate(new Date());
        banner.setCreateAdminId(vo.getCreateAdminId());
        banner.setAppCode(vo.getAppCode());
        banner.setBannerSort(DEFAULT_BANNER_SORT);
        return banner;
    }

    /**
     * Build all ColumnRecommendLabel entities for a column VO.
     */
    private List<ColumnRecommendLabel> buildAllLabels(PcIndexColumnVO vo) {
        List<ColumnRecommendLabel> labels = new LinkedList<>();

        // top labels
        if (!CollectionUtils.isEmpty(vo.getTopLabels())) {
            for (ColumnRecommendLabelVO labelVO : vo.getTopLabels()) {
                labels.add(buildLabel(vo, labelVO, LABEL_POS_TOP));
            }
        }

        // left labels
        if (!CollectionUtils.isEmpty(vo.getLeftLabels())) {
            for (ColumnRecommendLabelVO labelVO : vo.getLeftLabels()) {
                labels.add(buildLabel(vo, labelVO, LABEL_POS_LEFT));
            }
        }

        return labels;
    }

    /**
     * Build a single ColumnRecommendLabel from the source VO with common fields populated.
     */
    private ColumnRecommendLabel buildLabel(PcIndexColumnVO vo, ColumnRecommendLabelVO source, short position) {
        ColumnRecommendLabel label = new ColumnRecommendLabel();
        try { BeanUtils.copyProperties(label, source); } catch (Exception e) { throw new RuntimeException(e); }
        label.setId(idGenerator.id());
        label.setColumnId(vo.getId());
        label.setCreateAdminId(vo.getCreateAdminId());
        label.setCreateDate(new Date());
        label.setAppCode(vo.getAppCode());
        label.setPosition(position);
        label.setLabelSort(DEFAULT_LABEL_SORT);
        return label;
    }

    /**
     * Build all ColumnArea entities for a column VO.
     */
    private List<ColumnArea> buildAreas(PcIndexColumnVO vo) {
        List<ColumnArea> areas = new LinkedList<>();
        if (!CollectionUtils.isEmpty(vo.getAreaCodeList())) {
            for (int i = 0; i < vo.getAreaCodeList().size(); i++) {
                ColumnArea area = new ColumnArea();
                area.setId(idGenerator.id());
                area.setColumnId(vo.getId());
                area.setCreateAdminId(vo.getCreateAdminId());
                area.setCreateDate(new Date());
                area.setAreaCode(vo.getAreaCodeList().get(i));
                area.setAreaName(vo.getAreaNameList().get(i));
                area.setAppCode(vo.getAppCode());
                areas.add(area);
            }
        }
        return areas;
    }

    /**
     * Build all ColumnRecommendProduct entities for a column VO.
     */
    private List<ColumnRecommendProduct> buildProducts(PcIndexColumnVO vo) {
        List<ColumnRecommendProduct> products = new LinkedList<>();
        if (!CollectionUtils.isEmpty(vo.getColumnRecommendProducts())) {
            for (ColumnRecommendProductVO productVO : vo.getColumnRecommendProducts()) {
                ColumnRecommendProduct product = new ColumnRecommendProduct();
                try { BeanUtils.copyProperties(product, productVO); } catch (Exception e) { throw new RuntimeException(e); }
                product.setId(idGenerator.id());
                product.setColumnId(vo.getId());
                product.setCreateAdminId(vo.getCreateAdminId());
                product.setCreateDate(new Date());
                product.setProductSort(DEFAULT_PRODUCT_SORT);
                product.setAppCode(vo.getAppCode());
                products.add(product);
            }
        }
        return products;
    }

    // ==================== Query population for queryPcIndexColumns ====================

    /**
     * Initialize empty child lists on a PcIndexColumnVO so callers never get null lists.
     */
    private void initEmptyChildLists(PcIndexColumnVO vo) {
        vo.setColumnLeftBannerVOS(new LinkedList<>());
        vo.setTopLabels(new LinkedList<>());
        vo.setLeftLabels(new LinkedList<>());
        vo.setColumnRecommendProducts(new LinkedList<>());
        vo.setColumnAreas(new LinkedList<>());
    }

    private void populateColumnsBanners(List<PcIndexColumnVO> columns, List<Long> columnIds) {
        List<ColumnBannerVO> banners = columnBannerService.queryListByColumnIds(columnIds);
        if (CollectionUtils.isEmpty(banners)) {
            return;
        }
        for (PcIndexColumnVO column : columns) {
            for (ColumnBannerVO banner : banners) {
                if (column.getId().longValue() == banner.getColumnId().longValue()) {
                    assignBannerToColumn(column, banner);
                }
            }
        }
    }

    private void populateColumnsLabels(List<PcIndexColumnVO> columns, List<Long> columnIds) {
        List<ColumnRecommendLabelVO> labels = columnRecommendLabelService.queryListByColumnIds(columnIds);
        if (CollectionUtils.isEmpty(labels)) {
            return;
        }
        for (PcIndexColumnVO column : columns) {
            for (ColumnRecommendLabelVO label : labels) {
                if (column.getId().longValue() == label.getColumnId().longValue()) {
                    assignLabelToColumn(column, label);
                }
            }
        }
    }

    private void populateColumnsProducts(List<PcIndexColumnVO> columns, List<Long> columnIds) {
        List<ColumnRecommendProductVO> products = columnRecommendProductService.queryListCreateDateAscByColumnIds(columnIds);
        if (CollectionUtils.isEmpty(products)) {
            return;
        }
        for (PcIndexColumnVO column : columns) {
            for (ColumnRecommendProductVO product : products) {
                if (column.getId().longValue() == product.getColumnId().longValue()) {
                    column.getColumnRecommendProducts().add(product);
                }
            }
        }
    }

    private void populateColumnsAreas(List<PcIndexColumnVO> columns, List<Long> columnIds) {
        List<ColumnAreaVO> areas = columnAreaService.queryListByColumnIds(columnIds);
        if (CollectionUtils.isEmpty(areas)) {
            return;
        }
        for (PcIndexColumnVO column : columns) {
            for (ColumnAreaVO area : areas) {
                if (column.getId().longValue() == area.getColumnId().longValue()) {
                    column.getColumnAreas().add(area);
                }
            }
        }
    }

    // ==================== Query population for findById ====================

    private void populateSingleColumnBanners(PcIndexColumnVO vo) {
        List<ColumnBannerVO> banners = columnBannerService.queryListByColumnId(vo.getId());
        if (!CollectionUtils.isEmpty(banners)) {
            vo.setColumnLeftBannerVOS(new LinkedList<>());
            for (ColumnBannerVO banner : banners) {
                assignBannerToColumn(vo, banner);
            }
        }
    }

    private void populateSingleColumnLabels(PcIndexColumnVO vo) {
        List<ColumnRecommendLabelVO> labels = columnRecommendLabelService.queryListByColumnId(vo.getId());
        if (!CollectionUtils.isEmpty(labels)) {
            vo.setTopLabels(new LinkedList<>());
            vo.setLeftLabels(new LinkedList<>());
            for (ColumnRecommendLabelVO label : labels) {
                assignLabelToColumn(vo, label);
            }
        }
    }

    private void populateSingleColumnProducts(PcIndexColumnVO vo) {
        List<ColumnRecommendProductVO> products = columnRecommendProductService.queryListCreateDateAscByColumnId(vo.getId());
        vo.setColumnRecommendProducts(products);
    }

    private void populateSingleColumnAreas(PcIndexColumnVO vo) {
        List<ColumnAreaVO> areas = columnAreaService.queryListByColumnId(vo.getId());
        vo.setColumnAreas(areas);
    }

    // ==================== Position assignment helpers ====================

    /**
     * Assign a banner to the correct field on the column VO based on its position.
     */
    private void assignBannerToColumn(PcIndexColumnVO column, ColumnBannerVO banner) {
        int pos = banner.getPosition().intValue();
        if (pos == BANNER_POS_TOP) {
            column.setTopBanner(banner);
        } else if (pos == BANNER_POS_LEFT_CAROUSEL) {
            column.getColumnLeftBannerVOS().add(banner);
        } else if (pos == BANNER_POS_RIGHT_TOP) {
            column.setRightTopBanner(banner);
        } else if (pos == BANNER_POS_RIGHT_BOTTOM) {
            column.setRightBottomBanner(banner);
        } else if (pos == BANNER_POS_BOTTOM) {
            column.setBottomBanner(banner);
        }
    }

    /**
     * Assign a label to the correct field on the column VO based on its position.
     */
    private void assignLabelToColumn(PcIndexColumnVO column, ColumnRecommendLabelVO label) {
        if (label.getPosition().intValue() == LABEL_POS_TOP) {
            column.getTopLabels().add(label);
        } else if (label.getPosition().intValue() == LABEL_POS_LEFT) {
            column.getLeftLabels().add(label);
        }
    }

}
