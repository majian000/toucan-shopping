package com.toucan.shopping.common.util;

import com.toucan.shopping.common.vo.RequestJsonVO;
import org.apache.commons.lang3.StringUtils;

import java.security.NoSuchAlgorithmException;

public class SignUtil {

    public static String salt="0.4573";


    public static String sign(String appCode,String entityJson) throws NoSuchAlgorithmException {
        return MD5Util.md5(appCode+salt+entityJson);
    }


    public static String sign(RequestJsonVO requestJsonVO) throws NoSuchAlgorithmException {
        if(StringUtils.isEmpty(requestJsonVO.getAppCode()))
        {
            throw new IllegalArgumentException("appCode不能为空");
        }
        if(StringUtils.isEmpty(requestJsonVO.getEntityJson())){
            throw new IllegalArgumentException("entityJson不能为空");
        }
        return MD5Util.md5(requestJsonVO.getAppCode()+salt+requestJsonVO.getEntityJson());
    }

}
