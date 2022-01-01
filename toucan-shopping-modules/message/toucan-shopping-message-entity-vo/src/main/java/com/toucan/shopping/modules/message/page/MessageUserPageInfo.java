package com.toucan.shopping.modules.message.page;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.message.vo.MessageUserVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 列表查询页对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class MessageUserPageInfo extends PageInfo<MessageUserVO> {


    // ===============查询条件===================

    private Integer id;


    /**
     * 接收用户ID
     */
    private Long userMainId;

    /**
     * 是否读取 0:未读 1:已读
     */
    private Integer status;

    /**
     * 消息类型编码(用于消息历史)
     */
    private String messageTypeCode;


    private Long[] idArray; //ID数组

    //==============================================

}
