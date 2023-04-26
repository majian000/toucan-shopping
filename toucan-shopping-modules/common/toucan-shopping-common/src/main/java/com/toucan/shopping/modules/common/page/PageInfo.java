package com.toucan.shopping.modules.common.page;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageInfo<E> {

    /**
     * 数据总数
     */
    private Long total = 0L;

    /**
     * 页总数
     */
    private Long pageTotal = 0L;

    /**
     * 页码
     */
    private int page = 1;


    /**
     * 每页显示数量
     */
    private int size = 10;


    /**
     * 起始位置
     */
    private int start;


    /**
     * 前台传入
     */


    /**
     * 每页显示数量
     */
    private int limit = 10;




    /**
     * 数据列表
     */
    private List<E> list;



    public <T> T formatData(Class<T> clazz)
    {
        if(list!=null) {
            return JSONObject.parseObject(JSONObject.toJSONString(list),clazz);
        }
        return null;
    }


    public <T> List<T> formatDataList(Class<T> clazz)
    {
        if(list!=null) {
            return JSONArray.parseArray(JSONObject.toJSONString(list),clazz);
        }
        return null;
    }

}
