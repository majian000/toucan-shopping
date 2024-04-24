package com.toucan.shopping.modules.common.vo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.page.PageInfo;
import lombok.Data;

import java.util.List;

@Data
public class ResultPageInfoVO<T> extends ResultVO {
    private Integer code;
    private String msg;
    private PageInfo<T> data;

    public ResultPageInfoVO()
    {
        this(SUCCESS,"操作成功");
    }

    public ResultPageInfoVO(Integer code, String msg) {
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


}
