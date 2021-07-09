package com.toucan.shopping.modules.common.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class ImageUtils {

    public static boolean isImage(String fileName){
        if(StringUtils.isEmpty(fileName))
        {
            return false;
        }
        String fileUpperName = fileName.toUpperCase();
        if(fileUpperName.indexOf(".")!=-1)
        {
            String fileExt = fileUpperName.substring(fileUpperName.lastIndexOf("."),fileUpperName.length());
            if(".JPG".equals(fileExt))
            {
                return true;
            }
            if(".JPEG".equals(fileExt))
            {
                return true;
            }
            if(".PNG".equals(fileExt))
            {
                return true;
            }
            if(".GIF".equals(fileExt))
            {
                return true;
            }
            if(".BMP".equals(fileExt))
            {
                return true;
            }
        }

        return false;
    }

    public static String getImageExt(String fileName)
    {
        if(StringUtils.isEmpty(fileName))
        {
            return "";
        }
        String fileUpperName = fileName.toUpperCase();
        if(fileUpperName.indexOf(".")!=-1) {
            return fileUpperName.substring(fileUpperName.lastIndexOf("."), fileUpperName.length());
        }
        return "";
    }

}
