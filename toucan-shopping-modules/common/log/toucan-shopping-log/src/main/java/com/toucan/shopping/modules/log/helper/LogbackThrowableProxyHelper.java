package com.toucan.shopping.modules.log.helper;

import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;

/**
 * 将Logback的异常栈转换成字符串
 */
public class LogbackThrowableProxyHelper {



    public static String convertExceptionStack2StringByThrowable(IThrowableProxy throwableProxy){
        StringBuilder builder = new StringBuilder();
        StackTraceElementProxy[] stackTraceElements = throwableProxy.getStackTraceElementProxyArray();
        builder.append(throwableProxy.getClassName());
        builder.append(" ");
        builder.append(throwableProxy.getMessage());
        builder.append("<br>");
        if(stackTraceElements!=null&&stackTraceElements.length>0)
        {
            for(int i=0;i<stackTraceElements.length;i++)
            {
                StackTraceElementProxy stackTraceElement = stackTraceElements[i];
                builder.append(stackTraceElement.getSTEAsString());
                builder.append("<br>");
            }
        }

        return builder.toString();
    }


}
