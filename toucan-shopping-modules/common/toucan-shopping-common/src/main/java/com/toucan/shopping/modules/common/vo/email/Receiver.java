package com.toucan.shopping.modules.common.vo.email;

import lombok.Data;

/**
 * 收件人
 */
@Data
public class Receiver {

    /**
     * 邮件地址
     */
    private String email;

    /**
     * 收件人名称
     */
    private String name;

}
