package com.toucan.shopping.modules.category.mapper;

import com.toucan.shopping.modules.category.entity.Category;
import com.toucan.shopping.modules.category.page.CategoryTreeInfo;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


@Mapper
public interface CategoryMapper {

    List<Category> queryList(CategoryVO category);

    List<Category> queryPcIndexList(CategoryVO category);

    int insert(Category category);

    Long queryOneChildCountByPid(Long pid);

    int inserts(Category[] entitys);

    Category queryById(Long id);

    List<Category> queryByParentId(Long parentId);

    Long findCountByParentId(Long parentId);

    /**
     * 批量统计每个父节点下的子节点数量
     * @param parentIds 父节点ID列表
     * @return [{"parentId":xx,"childCount":yy}, ...]
     */
    List<Map<String, Object>> queryChildCountByParentIds(@Param("parentIds") List<Long> parentIds);

    List<Category> queryListByIdList(List<Long> ids);

    int deleteById(Long id);

    int update(Category category);

    Long queryCount(Category category);

    /**
     * 查询表格树
     * @param queryTreeInfo
     * @return
     */
    List<CategoryVO> findTreeTableByPageInfo(CategoryTreeInfo queryTreeInfo);


}
