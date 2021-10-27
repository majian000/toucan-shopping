package com.toucan.shopping.modules.common.util;


/**
 * 将异常对象的异常栈转换成字符串
 */
public class ExceptionHelper {

    /**
     * 换行符
     */
    private static String lineSeparator;

    static{
        lineSeparator = System.getProperty("line.separator");
    }

    /**
     * 将异常栈转换成字符串
     * @param exception
     * @return
     */
    public static String convertExceptionStack2String(Exception exception)
    {
        StringBuilder builder = new StringBuilder();
        StackTraceElement[] stackTraceElements = exception.getStackTrace();
        builder.append(exception.getClass().getName());
        builder.append(" ");
        builder.append(exception.getLocalizedMessage());
        builder.append("<br>");
        if(stackTraceElements!=null&&stackTraceElements.length>0)
        {
            for(int i=0;i<stackTraceElements.length;i++)
            {
                StackTraceElement stackTraceElement = stackTraceElements[i];
                builder.append("at ");
                builder.append(stackTraceElement.getClassName());
                builder.append(".");
                builder.append(stackTraceElement.getMethodName());
                builder.append("(");
                builder.append(stackTraceElement.getFileName());
                builder.append(":");
                builder.append(stackTraceElement.getLineNumber());
                builder.append(")");
                builder.append("<br>");
            }
        }

        return builder.toString();
    }


}
