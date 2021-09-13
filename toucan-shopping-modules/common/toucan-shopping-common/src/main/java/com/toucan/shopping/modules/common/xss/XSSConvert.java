package com.toucan.shopping.modules.common.xss;

import com.toucan.shopping.modules.common.util.XSSUtil;

import java.lang.reflect.Field;

/**
 * xss实体字段过滤
 */
public class XSSConvert {


    /**
     * 遍历所有字符串类型的字段,过滤xss代码
     * @param entity
     * @return
     */
    public static void replaceXSS(Object entity) throws Exception
    {
        Class clazz = entity.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for(int i=0; i<fields.length; i++){
            Field field = fields[i];
            field.setAccessible(true); //设置访问状态
            //如果是字符串类型的字段 才进行xss过滤
            if(field.getType().equals(String.class))
            {
                Object fieldObject = field.get(entity);
                if(fieldObject!=null)
                {
                    field.set(entity,XSSUtil.replaceXss(String.valueOf(fieldObject)));
                }
            }
        }
    }


    /**
     * 替换该字符串的XSS代码
     * @param string
     * @return
     */
    public static String replaceStringXSS(String string) throws Exception
    {
        return XSSUtil.replaceXss(string);
    }

    static class XSSEntity{
        private String content;
        private Integer id;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }
    }

    public static void main(String[] args)
    {
        XSSEntity xssEntity1=  new XSSConvert.XSSEntity();
        xssEntity1.setContent("<script><111111111/scriptaaaaaaaaaa><a></a>《");
        xssEntity1.setId(1);
        try {
            XSSConvert.replaceXSS(xssEntity1);

            System.out.println(xssEntity1.getId()+"   "+xssEntity1.getContent());
        }catch(Exception e)
        {
            e.printStackTrace();
        }

    }

}
