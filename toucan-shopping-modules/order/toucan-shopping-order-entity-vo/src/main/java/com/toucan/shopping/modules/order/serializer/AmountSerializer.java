package com.toucan.shopping.modules.order.serializer;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;

/**
 * 金额反序列化 去掉多余0
 */
public class AmountSerializer implements ObjectSerializer {
    public static final AmountSerializer instance = new AmountSerializer();

    public AmountSerializer() {
    }

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;
        if (object == null) {
            out.writeNull();
        } else {
            if(object instanceof BigDecimal) {
                String strVal = ((BigDecimal) object).toPlainString();
                out.writeString(strVal);
            }else{
                String strVal = object.toString();
                out.writeString(strVal);
            }
        }
    }
}
