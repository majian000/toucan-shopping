package com.toucan.shopping.modules.common.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 文件传输对象
 * 可以直接通过byte数组构造出一个文件传输对象,使用httpClient可以直接发送这个对象(无需File以及MultipartFile)
 * @author majian
 * @date 2023-7-12 11:53:57
 */
public class FileTansferImpl {

    private String fileName;
    private InputStream stream;
    private String mimeType;
    public static final String DEFAULT_FILE_NAME = "streamFile";

    public FileTansferImpl(String fileName, InputStream stream, String mimeType) {
        this.fileName = fileName;
        this.stream = stream;
        this.mimeType = mimeType;
    }

    public FileTansferImpl(String fileName, InputStream stream) {
        this.fileName = fileName;
        this.stream = stream;
        this.mimeType = "application/octet-stream";
    }

    public FileTansferImpl(InputStream stream) {
        this.fileName = "streamFile";
        this.stream = stream;
        this.mimeType = "application/octet-stream";
    }

    public boolean isValid() {
        return this.stream != null && this.fileName != null;
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getMimeType() {
        return this.mimeType == null ? "application/octet-stream" : this.mimeType;
    }

    public long getFileLength() {
        return 0L;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public InputStream getStream() {
        return stream;
    }

    public void setStream(InputStream stream) {
        this.stream = stream;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public void write(OutputStream output) throws IOException {
        try {
            byte[] buffer = new byte[4096];
            boolean var3 = false;

            int n;
            while(-1 != (n = this.stream.read(buffer))) {
                output.write(buffer, 0, n);
            }
        } finally {
            if (this.stream != null) {
                this.stream.close();
            }

        }

    }
}
