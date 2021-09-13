package com.toucan.shopping.modules.common.util;

import java.lang.reflect.Field;

/**
 * 空格字段过滤
 */
public class BlankSpaceUtils {


    /**
     * 遍历所有字符串类型的字段,过滤空格
     * @param entity
     * @return
     */
    public static void replaceBlankSpace(Object entity) throws Exception
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
                    String fieldValue = String.valueOf(fieldObject);
                    field.set(entity,fieldValue.replace(" ",""));
                }
            }
        }
    }

    static class BlankSpaceEntity {
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
        BlankSpaceEntity xssEntity1=  new BlankSpaceEntity();
        xssEntity1.setContent("<script>     <111111111    /scriptaaa   aaaaaaa><a></a>《");
        xssEntity1.setId(1);
        try {
            BlankSpaceUtils.replaceBlankSpace(xssEntity1);

            System.out.println(xssEntity1.getId()+"   "+xssEntity1.getContent());
        }catch(Exception e)
        {
            e.printStackTrace();
        }

    }

}
