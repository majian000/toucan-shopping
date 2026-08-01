package com.toucan.shopping.modules.common.page;


import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
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
     * 最大数量(ES中的maxResultWindow)
     */
    private Long maxTotal = 0L;

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


    /**
     * 设置每页显示数量时同步更新 limit，兼容前端传 size 后端用 limit 的问题
     */
    public void setSize(int size) {
        this.size = size;
        this.limit = size;
    }


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
