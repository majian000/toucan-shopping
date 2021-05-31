package com.toucan.shopping.modules.common.stream;

import org.springframework.core.io.InputStreamResource;

import java.io.InputStream;

/**
 * 重写输入流
 */
public class CommonInputStreamResource extends InputStreamResource {
    private int length;
    private String filename = "temp";

    public CommonInputStreamResource(InputStream inputStream) {
        super(inputStream);
    }

    public CommonInputStreamResource(InputStream inputStream, int length) {
        super(inputStream);
        this.length = length;
    }

    public CommonInputStreamResource(InputStream inputStream, int length, String filename) {
        super(inputStream);
        this.length = length;
        this.filename = filename;
    }

    public CommonInputStreamResource(InputStream inputStream, String description, int length, String filename) {
        super(inputStream, description);
        this.length = length;
        this.filename = filename;
    }

    @Override
    public String getFilename() {
        return filename;
    }

    @Override
    public long contentLength() {
        int estimate = length;
        return estimate == 0 ? 1 : estimate;
    }
}
