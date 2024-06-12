package com.toucan.shopping.modules.jsoup.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedList;
import java.util.List;

public class JsoupUtil {


    /**
     * 返回指定标签的指定属性
     * @param html
     * @param label
     * @param attrName
     * @return
     */
    public static List<String> queryAttributeValueList(String html,String label,String attrName){
        List<String> attributeValueList = new LinkedList<>();
        Elements elements = Jsoup.parse(html).select(label);
        if(elements!=null&&elements.size()>0){
            for(int i=0;i<elements.size();i++){
                attributeValueList.add(elements.get(i).attr(attrName));
            }
        }
        return attributeValueList;
    }



}
