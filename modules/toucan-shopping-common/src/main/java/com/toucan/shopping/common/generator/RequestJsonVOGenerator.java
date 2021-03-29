package com.toucan.shopping.common.generator;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.common.util.SignUtil;
import com.toucan.shopping.common.vo.RequestJsonVO;

import java.security.NoSuchAlgorithmException;

public class RequestJsonVOGenerator {


    public static RequestJsonVO generator(String appCode,String userId,String adminId,Object object) throws NoSuchAlgorithmException {
        RequestJsonVO requestJsonVO = new RequestJsonVO();
        requestJsonVO.setAppCode(appCode);
        requestJsonVO.setUserId(userId);
        requestJsonVO.setAdminId(adminId);
        requestJsonVO.setEntityJson(JSONObject.toJSONString(object));
        return requestJsonVO;
    }


    public static RequestJsonVO generatorByUser(String appCode,String userId,Object object) throws NoSuchAlgorithmException {
        RequestJsonVO requestJsonVO = new RequestJsonVO();
        requestJsonVO.setAppCode(appCode);
        requestJsonVO.setUserId(userId);
        requestJsonVO.setEntityJson(JSONObject.toJSONString(object));
        return requestJsonVO;
    }

    public static RequestJsonVO generator(String appCode,Object object) throws NoSuchAlgorithmException {
        RequestJsonVO requestJsonVO = new RequestJsonVO();
        requestJsonVO.setAppCode(appCode);
        requestJsonVO.setEntityJson(JSONObject.toJSONString(object));
        return requestJsonVO;
    }

    public static RequestJsonVO generatorByAdmin(String appCode,String adminId,Object object) throws NoSuchAlgorithmException {
        RequestJsonVO requestJsonVO = new RequestJsonVO();
        requestJsonVO.setAppCode(appCode);
        requestJsonVO.setAdminId(adminId);
        requestJsonVO.setEntityJson(JSONObject.toJSONString(object));
        return requestJsonVO;
    }
}
