package com.toucan.shopping.modules.order.serializer;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.writer.ObjectWriter;

import java.lang.reflect.Type;
import java.math.BigDecimal;

/**
 * 金额序列化 去掉多余0
 */
public class AmountSerializer implements ObjectWriter<Object> {
    public static final AmountSerializer instance = new AmountSerializer();

    public AmountSerializer() {
    }

    @Override
    public void write(JSONWriter jsonWriter, Object object, Object fieldName, Type fieldType, long features) {
        if (object == null) {
            jsonWriter.writeNull();
        } else {
            if (object instanceof BigDecimal) {
                String strVal = ((BigDecimal) object).toPlainString();
                jsonWriter.writeString(strVal);
            } else {
                String strVal = object.toString();
                jsonWriter.writeString(strVal);
            }
        }
    }
}
