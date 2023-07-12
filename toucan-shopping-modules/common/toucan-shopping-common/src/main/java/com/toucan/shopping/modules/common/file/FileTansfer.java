package com.toucan.shopping.modules.common.file;

import java.io.IOException;
import java.io.OutputStream;

public interface FileTansfer {
    String MIME_TYPE_DEFAULT = "application/octet-stream";

    int READ_BUFFER_SIZE = 4096;

    boolean isValid();

    String getFileName();

    String getMimeType();

    long getFileLength();

    void write(OutputStream var1) throws IOException;
}
