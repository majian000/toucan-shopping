package com.toucan.shopping.modules.common.vo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.List;

@Data
public class ResultObjectVO extends ResultVO {
    private Integer code;
    private String msg;
    private Object data;

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

    public <T> T formatData(Class<T> clazz)
    {
        if(data!=null) {
            return JSONObject.parseObject(JSONObject.toJSONString(data),clazz);
        }
        return null;
    }


    public <T> List<T> formatDataArray(Class<T> clazz)
    {
        if(data!=null) {
            return JSONArray.parseArray(JSONObject.toJSONString(data),clazz);
        }
        return null;
    }
}
