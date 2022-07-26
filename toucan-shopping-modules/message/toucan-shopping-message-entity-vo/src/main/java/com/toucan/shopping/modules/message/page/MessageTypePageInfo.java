package com.toucan.shopping.modules.message.page;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.message.vo.MessageTypeVO;
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
public class MessageTypePageInfo extends PageInfo<MessageTypeVO> {


    // ===============查询条件===================

    private Integer id;


    /**
     * 名称
     */
    private String name;


    /**
     * 名称
     */
    private String code;

    /**
     * 应用编码
     */
    private String appCode;

    private Long[] idArray; //ID数组

    //==============================================

}
