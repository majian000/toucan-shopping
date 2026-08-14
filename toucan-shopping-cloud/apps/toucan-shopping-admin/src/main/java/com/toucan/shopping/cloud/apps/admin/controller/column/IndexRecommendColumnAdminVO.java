package com.toucan.shopping.cloud.apps.admin.controller.column;

import com.toucan.shopping.modules.admin.auth.vo.DictVO;
import com.toucan.shopping.modules.column.vo.ColumnVO;
import lombok.Data;

import java.util.List;

/**
 * 首页推荐栏目列表VO（admin扩展，含匹配到的字典集合）
 *
 * @author majian
 */
@Data
public class IndexRecommendColumnAdminVO extends ColumnVO {

    /**
     * 匹配到的栏目类型字典集合
     */
    private List<DictVO> typeDictVos;

}
