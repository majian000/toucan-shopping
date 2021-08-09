package com.toucan.shopping.modules.freemarker.exception;

import freemarker.core.Environment;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Writer;

@Slf4j
public class PageErrorException implements TemplateExceptionHandler {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    public void handleTemplateException(TemplateException te, Environment env, Writer out)
            throws TemplateException {
        try {
            logger.warn(te.getMessage(),te);
            out.write("<script>window.location.href='/index';</script>");
        } catch (IOException e) {
            throw new TemplateException("Failed to print error message. Cause: " + e, env);
        }
    }
}
