package com.toucan.shopping.modules.column.page;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.toucan.shopping.modules.column.vo.ColumnTypeVO;
import com.toucan.shopping.modules.column.vo.ColumnVO;
import com.toucan.shopping.modules.common.page.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 列表查询页对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class ColumnPageInfo extends PageInfo<ColumnVO> {


    // ===============查询条件===================

    private Integer id;

    private String columnTypeCode;

    private String position; //栏目位置 1 PC门户首页

    private String title; //标题

    private Short showStatus;  //显示状态 0隐藏 1显示

    private Long pid; //上级节点

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date startShowDate; //开始展示时间

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date endShowDate; //结束展示时间

    private String code; //栏目编码

    private String type; //类型 10:pc端 11:移动端

    /**
     * 应用编码
     */
    private String appCode;

    private Long[] idArray; //ID数组

    //==============================================

}
