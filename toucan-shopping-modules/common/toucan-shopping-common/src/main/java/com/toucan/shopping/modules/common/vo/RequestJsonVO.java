package com.toucan.shopping.modules.common.vo;

import com.toucan.shopping.modules.common.util.SignUtil;
import lombok.Data;

import java.security.NoSuchAlgorithmException;

@Data
public class RequestJsonVO extends RequestVO{


    /**
     * 请求数据对象
     */
    private String entityJson;

    public String sign() throws NoSuchAlgorithmException {
        //return SignUtil.sign(this);
        //TODO:暂时关闭参数签名
        return "";
    }

}
