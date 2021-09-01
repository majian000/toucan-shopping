package com.toucan.shopping.modules.common.util;


import java.util.regex.Pattern;

/**
 * 跨站脚本工具类
 */
public class XSSUtil {

    private static Pattern[] patterns = new Pattern[]{
            // Script fragments
            Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE),
            // src='...'
            Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            // lonely script tags
            Pattern.compile("</script>", Pattern.CASE_INSENSITIVE),
            Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            // eval(...)
            Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            // expression(...)
            Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            // javascript:...
            Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
            // vbscript:...
            Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
            // onload(...)=...
            Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            //现场安全测试增加校验
            Pattern.compile("alert(.*?)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("<", Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile(">", Pattern.MULTILINE | Pattern.DOTALL)
    };

    //过滤替换xss脚本为空
    public static String replaceXss(String value){
        if (value != null) {
            value = value.replaceAll("\0", "");

            for (Pattern scriptPattern : patterns){
                value = scriptPattern.matcher(value).replaceAll("");
            }
        }
        return value;
    }

    //含xss脚本则返回false
    public static  boolean  isXssPass(String valueString){
        if(valueString!=null){
            for (Pattern scriptPattern : patterns){
                if(scriptPattern.matcher(valueString).find()==true){
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println(XSSUtil.replaceXss("<IMG SRC=http://3w.org/XSS/xss.js/>"));
        System.out.println(XSSUtil.replaceXss("<a href=http://3w.org/XSS/xss.js/>"));
        System.out.println(XSSUtil.replaceXss("<scriptTT>alert(1);1111</script>"));
        System.out.println(XSSUtil.replaceXss("测试介绍<script>,/script> "));
        System.out.println(XSSUtil.isXssPass("<img src=0 onerror=alert(1)>"));
    }

}
