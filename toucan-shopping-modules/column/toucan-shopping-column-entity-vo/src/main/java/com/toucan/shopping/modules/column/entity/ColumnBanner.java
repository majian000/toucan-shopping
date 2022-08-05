package com.toucan.shopping.modules.column.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 栏目轮播图关联
 *
 * @author majian
 */
@Data
public class ColumnBanner {

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id; //主键
    private Long columnId; //栏目主键
    private Date createDate; //创建时间
    private Date updateDate; //创建时间
    private String createAdminId; //创建人ID
    private String updateAdminId; //修改人ID
    private String remark; //备注
    private Integer position; //位置 1顶部 2左侧 3右侧顶部 4右侧底部
    private String appCode; //所属应用

    private String title; //标题
    private String imgPath; //图片路径
    private String clickPath; //点击路径
    private Integer bannerSort; //排序 从大到小
    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;
}
