package com.toucan.shopping.common.converter;

import com.toucan.shopping.common.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

import java.util.Date;

public class Date2StringConverter implements Converter<Date,String> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public String convert(Date date) {
        try {
            return DateUtils.format(date, DateUtils.FORMATTER_SS);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return "";
    }
}
