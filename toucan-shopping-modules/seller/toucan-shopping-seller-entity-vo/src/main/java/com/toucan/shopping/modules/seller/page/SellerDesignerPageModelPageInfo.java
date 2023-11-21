package com.toucan.shopping.modules.seller.page;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.seller.entity.SellerDesignerPageModel;
import com.toucan.shopping.modules.seller.entity.SellerLoginHistory;
import com.toucan.shopping.modules.seller.vo.SellerDesignerPageModelVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * 列表查询页
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class SellerDesignerPageModelPageInfo extends PageInfo<SellerDesignerPageModelVO> {


    // ===============查询条件===================

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long userMainId;


    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long shopId;

    private Integer type; //类型 1:预览页 2:正式页

    private Integer position; //位置 1:PC首页

    private List<Integer> exTypes; //排除类型

    //==============================================





}
