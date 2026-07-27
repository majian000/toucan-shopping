package com.toucan.shopping.modules.common.vo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.List;

@Data
public class ResultTypeObjectVO<T> extends ResultVO {
    private Integer code;
    private String msg;
    private T data;

    public ResultTypeObjectVO()
    {
        this(SUCCESS,"操作成功");
    }

    public ResultTypeObjectVO(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    public boolean isSuccess()
    {
        if(this.code.intValue()== ResultVO.SUCCESS.intValue())
        {
            return true;
        }
        return false;
    }

    /** 快速构造失败响应 */
    public static <T> ResultTypeObjectVO<T> fail(Integer code, String msg) {
        return new ResultTypeObjectVO<>(code, msg);
    }

    public <T> T formatData(Class<T> clazz)
    {
        if(data!=null) {
            return JSONObject.parseObject(JSONObject.toJSONString(data),clazz);
        }
        return null;
    }


    public <T> List<T> formatDataList(Class<T> clazz)
    {
        if(data!=null) {
            return JSONArray.parseArray(JSONObject.toJSONString(data),clazz);
        }
        return null;
    }
}
