package com.toucan.shopping.modules.freemarker.exception;

import freemarker.core.Environment;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.Writer;

@Slf4j
public class PageErrorException implements TemplateExceptionHandler {
    public void handleTemplateException(TemplateException te, Environment env, Writer out)
            throws TemplateException {
        try {
            log.warn(te.getMessage(),te);
            out.write("<script>window.location.href='/index';</script>");
        } catch (IOException e) {
            throw new TemplateException("Failed to print error message. Cause: " + e, env);
        }
    }
}
