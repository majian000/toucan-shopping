package com.toucan.shopping.cloud.apps.admin.helper;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.layui.vo.TableVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分页数据解析工具
 * cloud 模式: data 是 Map (Feign HTTP JSON 反序列化)
 * single 模式: data 是 PageInfo (直接 Java 对象)
 */
public class PageHelper {

    @SuppressWarnings("unchecked")
    public static Map<String, Object> extractPageData(Object data) {
        Map<String, Object> resultMap = new HashMap<>();
        if (data instanceof Map) {
            return (Map<String, Object>) data;
        } else if (data instanceof PageInfo) {
            PageInfo<?> pageInfo = (PageInfo<?>) data;
            if (pageInfo.getTotal() != null) {
                resultMap.put("total", pageInfo.getTotal());
            } else {
                resultMap.put("total", 0L);
            }
            if (pageInfo.getList() != null) {
                resultMap.put("list", pageInfo.getList());
            } else {
                resultMap.put("list", List.of());
            }
            return resultMap;
        }
        resultMap.put("total", 0L);
        resultMap.put("list", List.of());
        return resultMap;
    }

    public static void fillTableVOPageData(TableVO tableVO, Object data) {
        if (data instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) data;
            Object totalObj = map.get("total");
            tableVO.setCount(totalObj != null ? Long.parseLong(String.valueOf(totalObj)) : 0L);
            if (tableVO.getCount() > 0) {
                tableVO.setData((List<Object>) map.get("list"));
            }
        } else if (data instanceof PageInfo) {
            PageInfo<?> pageInfo = (PageInfo<?>) data;
            tableVO.setCount(pageInfo.getTotal() != null ? pageInfo.getTotal() : 0L);
            if (tableVO.getCount() > 0 && pageInfo.getList() != null) {
                tableVO.setData((List<Object>) (List<?>) pageInfo.getList());
            }
        }
    }
}
