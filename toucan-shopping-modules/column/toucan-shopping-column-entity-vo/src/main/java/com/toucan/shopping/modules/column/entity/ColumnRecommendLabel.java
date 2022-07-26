package com.toucan.shopping.modules.column.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 栏目推荐标签
 *
 * @author majian
 */
@Data
public class ColumnRecommendLabel {

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id; //主键


    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long columnId; //栏目主键
    private String labelName; //标签名称
    private String clickPath; //点击路径
    private Short position; //位置 1顶部 2左侧

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate; //创建时间

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateDate; //创建时间
    private String appCode; //所属应用
    private String createAdminId; //创建人ID
    private String updateAdminId; //修改人ID

    private Long labelSort; //排序

    private String remark; //备注


    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;
}
