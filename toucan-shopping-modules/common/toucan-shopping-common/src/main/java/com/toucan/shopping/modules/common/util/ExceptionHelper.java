package com.toucan.shopping.modules.common.util;


/**
 * 将异常对象的异常栈转换成字符串
 */
public class ExceptionHelper {


    /**
     * 将异常栈转换成字符串
     * @param exception
     * @return
     */
    public static String convertExceptionStack2String(Exception exception)
    {
        StringBuilder builder = new StringBuilder();
        StackTraceElement[] stackTraceElements = exception.getStackTrace();
        if(stackTraceElements!=null&&stackTraceElements.length>0)
        {
            builder.append(stackTraceElements[0].getClassName());
            builder.append(" ");
            builder.append(exception.getLocalizedMessage());
            builder.append("\r\n");
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
                builder.append("\r\n");
            }
        }

        return builder.toString();
    }


}
