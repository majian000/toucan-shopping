package com.toucan.shopping.modules.user.page;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.user.vo.UserCollectProductVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 列表查询页
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class UserCollectProductPageInfo extends PageInfo<UserCollectProductVO> {


    // ===============查询条件===================


    /**
     * 主键 雪花算法生成
     */
    private Long id;

    private String appCode; //所属应用
    private String userMainId; //所属用户
    private String orderNo; //订单编号


    //==============================================





}
