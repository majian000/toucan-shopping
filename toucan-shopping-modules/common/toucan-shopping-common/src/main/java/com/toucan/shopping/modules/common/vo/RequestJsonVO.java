package com.toucan.shopping.modules.common.vo;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.util.SignUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

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


    public <T> T formatEntity(Class<T> clazz)
    {
        if(StringUtils.isNotEmpty(entityJson)) {
            return JSONObject.parseObject(entityJson,clazz);
        }
        return null;
    }


}
