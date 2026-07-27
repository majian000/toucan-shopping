package com.toucan.shopping.modules.category.business.service;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.category.page.CategoryHotTreeInfo;
import com.toucan.shopping.modules.category.service.CategoryHotService;
import com.toucan.shopping.modules.category.vo.CategoryHotTreeVO;
import com.toucan.shopping.modules.category.vo.CategoryHotVO;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 热门类别业务服务
 */
@Service
public class CategoryHotBusinessService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CategoryHotService categoryHotService;

    @Autowired
    private IdGenerator idGenerator;


    /**
     * 保存热门类别
     * @param requestJsonVO
     * @return
     */
    public ResultObjectVO save(RequestJsonVO requestJsonVO) {
        if (requestJsonVO == null) {
            logger.info("请求参数为空");
            return failResult("请重试!");
        }

        try {
            CategoryHotVO categoryHot = JSONObject.parseObject(requestJsonVO.getEntityJson(), CategoryHotVO.class);

            if (categoryHot.getCategoryId() == null) {
                logger.info("类别ID为空 param:" + JSONObject.toJSONString(categoryHot));
                return failResult("类别ID不能为空!");
            }

            CategoryHotVO queryCategory = new CategoryHotVO();
            queryCategory.setName(categoryHot.getName());
            queryCategory.setDeleteStatus((short) 0);

            if (!CollectionUtils.isEmpty(categoryHotService.queryList(queryCategory))) {
                return failResult("已存在该类别!");
            }

            categoryHot.setId(idGenerator.id());
            categoryHot.setCreateDate(new Date());
            int row = categoryHotService.save(categoryHot);
            if (row != 1) {
                return failResult("请重试!");
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return failResult("请重试!");
        }
        return new ResultObjectVO();
    }


    /**
     * 查询树表格
     * @param requestJsonVO
     * @return
     */
    public ResultObjectVO queryTreeTableByPid(RequestJsonVO requestJsonVO) {
        if (requestJsonVO == null || requestJsonVO.getEntityJson() == null) {
            return failResult("没有找到实体对象");
        }

        try {
            CategoryHotTreeInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), CategoryHotTreeInfo.class);

            List<CategoryHotTreeVO> categoryTreeVOS;
            if (StringUtils.isNotEmpty(queryPageInfo.getName())) {
                categoryTreeVOS = queryByName(queryPageInfo.getName());
            } else {
                categoryTreeVOS = queryByParentId(queryPageInfo.getParentId());
            }

            // 将按名称查询的结果设置为顶级节点
            if (StringUtils.isNotEmpty(queryPageInfo.getName())) {
                setAsTopLevel(categoryTreeVOS);
            }

            ResultObjectVO resultObjectVO = new ResultObjectVO();
            resultObjectVO.setData(categoryTreeVOS);
            return resultObjectVO;

        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return failResult("请稍后重试");
        }
    }


    // ==================== 私有辅助方法 ====================

    /**
     * 构建失败结果
     */
    private ResultObjectVO failResult(String msg) {
        ResultObjectVO result = new ResultObjectVO();
        result.setCode(ResultVO.FAILD);
        result.setMsg(msg);
        return result;
    }

    /**
     * 按名称模糊查询
     */
    private List<CategoryHotTreeVO> queryByName(String name) {
        List<CategoryHotTreeVO> result = new ArrayList<>();
        CategoryHotVO queryCategoryHot = new CategoryHotVO();
        queryCategoryHot.setName(name);
        List<CategoryHotVO> categories = categoryHotService.queryList(queryCategoryHot);
        for (CategoryHotVO category : categories) {
            result.add(buildTreeVO(category));
        }
        return result;
    }

    /**
     * 按父节点ID查询子节点
     */
    private List<CategoryHotTreeVO> queryByParentId(Long parentId) {
        List<CategoryHotTreeVO> result = new ArrayList<>();
        CategoryHotVO queryCategoryHot = new CategoryHotVO();
        queryCategoryHot.setParentId(parentId != null ? parentId : -1L);
        List<CategoryHotVO> categories = categoryHotService.queryList(queryCategoryHot);
        for (CategoryHotVO category : categories) {
            CategoryHotTreeVO treeVO = buildTreeVO(category);
            // 检查是否有子节点
            CategoryHotVO childQuery = new CategoryHotVO();
            childQuery.setParentId(category.getId());
            if (categoryHotService.queryCount(childQuery) > 0) {
                treeVO.setHaveChild(true);
            }
            result.add(treeVO);
        }
        return result;
    }

    /**
     * 将CategoryHotVO属性复制到CategoryHotTreeVO
     */
    private CategoryHotTreeVO buildTreeVO(CategoryHotVO source) {
        CategoryHotTreeVO treeVO = new CategoryHotTreeVO();
        try {
            BeanUtils.copyProperties(treeVO, source);
        } catch (Exception e) {
            logger.warn("复制CategoryHot属性失败", e);
        }
        return treeVO;
    }

    /**
     * 将所有节点设置为顶级节点（parentId=-1）
     */
    private void setAsTopLevel(List<CategoryHotTreeVO> categoryTreeVOS) {
        if (!CollectionUtils.isEmpty(categoryTreeVOS)) {
            for (CategoryHotTreeVO vo : categoryTreeVOS) {
                vo.setParentId(-1L);
            }
        }
    }

}
