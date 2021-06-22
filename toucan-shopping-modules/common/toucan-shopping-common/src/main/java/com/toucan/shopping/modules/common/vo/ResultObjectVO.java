package com.toucan.shopping.modules.common.vo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class ResultObjectVO<T> extends ResultVO {
    private Integer code;
    private String msg;
    private T data;

    public ResultObjectVO()
    {
        this(SUCCESS,"操作成功");
    }

    public ResultObjectVO(Integer code, String msg) {
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

    public Object formatData(Class clazz)
    {
        if(data!=null) {
            return JSONObject.parseObject(JSONObject.toJSONString(data),clazz);
        }
        return null;
    }
    public Object formatDataArray(Class clazz)
    {
        if(data!=null) {
            return JSONArray.parseArray(JSONObject.toJSONString(data),clazz);
        }
        return null;
    }
}
