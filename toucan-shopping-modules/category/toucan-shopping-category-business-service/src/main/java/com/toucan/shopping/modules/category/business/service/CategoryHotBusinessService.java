package com.toucan.shopping.modules.category.business.service;

import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.modules.category.page.CategoryHotTreeInfo;
import com.toucan.shopping.modules.category.service.CategoryHotService;
import com.toucan.shopping.modules.category.vo.CategoryHotTreeVO;
import com.toucan.shopping.modules.category.vo.CategoryHotVO;
import com.toucan.shopping.modules.common.annotation.RequestCheck;
import com.toucan.shopping.modules.common.exception.BusinessValidationException;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.util.Check;
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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO save(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            CategoryHotVO categoryHot = JSONObject.parseObject(requestJsonVO.getEntityJson(), CategoryHotVO.class);

            Check.notNull(categoryHot.getCategoryId(), ResultVO.FAILD, "类别ID不能为空!");

            CategoryHotVO queryCategory = new CategoryHotVO();
            queryCategory.setName(categoryHot.getName());
            queryCategory.setDeleteStatus((short) 0);

            Check.isTrue(CollectionUtils.isEmpty(categoryHotService.queryList(queryCategory)), ResultVO.FAILD, "已存在该类别!");

            categoryHot.setId(idGenerator.id());
            categoryHot.setCreateDate(new Date());
            int row = categoryHotService.save(categoryHot);
            Check.isTrue(row == 1, ResultVO.FAILD, "请重试!");

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultVO.FAILD, "请重试!");
        }
        return resultObjectVO;
    }


    /**
     * 查询树表格
     * @param requestJsonVO
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryTreeTableByPid(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

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

            resultObjectVO.setData(categoryTreeVOS);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        }
        return resultObjectVO;
    }


    // ==================== 私有辅助方法 ====================

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
