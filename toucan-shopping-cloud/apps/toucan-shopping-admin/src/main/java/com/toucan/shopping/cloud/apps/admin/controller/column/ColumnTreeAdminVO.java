package com.toucan.shopping.cloud.apps.admin.controller.column;

import com.toucan.shopping.modules.admin.auth.vo.DictVO;
import com.toucan.shopping.modules.column.vo.ColumnTreeVO;
import lombok.Data;

import java.util.List;

/**
 * 栏目树VO（admin扩展，含匹配到的字典集合）
 *
 * @author majian
 */
@Data
public class ColumnTreeAdminVO extends ColumnTreeVO {

    /**
     * 匹配到的栏目类型字典集合
     */
    private List<DictVO> typeDictVos;

    /**
     * 匹配到的栏目位置字典集合
     */
    private List<DictVO> positionDictVos;

}
