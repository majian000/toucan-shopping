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

    /** 快速构造失败响应 */
    public static ResultObjectVO fail(Integer code, String msg) {
        return new ResultObjectVO(code, msg);
    }

    /** 快速构造成功响应 */
    public static ResultObjectVO ok() {
        return new ResultObjectVO();
    }

    /** 快速构造成功响应（带数据） */
    public static ResultObjectVO ok(Object data) {
        ResultObjectVO vo = new ResultObjectVO();
        vo.setData(data);
        return vo;
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
