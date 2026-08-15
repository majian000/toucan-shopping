package com.toucan.shopping.modules.order.enums;

/**
 * 支付状态
 *
 * @author majian
 */
public enum PayStatusEnum {

    ALL(-1, "全部"),
    NON_PAYMENT(0, "未支付"),
    PAID(1, "已支付"),
    CANCEL_PAYMENT(4, "取消支付"),
    REFUNDED(5, "已退款")
    ;

    private int code;
    private String name;

    PayStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static PayStatusEnum getByCode(int code) {
        for (PayStatusEnum payStatusEnum : PayStatusEnum.values()) {
            if (payStatusEnum.getCode() == code) {
                return payStatusEnum;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
