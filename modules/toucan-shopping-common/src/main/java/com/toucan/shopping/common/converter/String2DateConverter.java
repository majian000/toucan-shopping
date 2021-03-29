package com.toucan.shopping.common.converter;

import com.toucan.shopping.common.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

import java.util.Date;

public class String2DateConverter implements Converter<String,Date> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Date convert(String date) {
        try {
            return DateUtils.parse(date, DateUtils.FORMATTER_SS);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return null;
    }
}
