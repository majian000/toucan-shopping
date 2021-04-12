package com.toucan.shopping.modules.common.wrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.Charset;

/**
 * 解决request读取body 只能读取一次问题
 */
public class RequestWrapper extends HttpServletRequestWrapper {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    //将HttpServletRequest里的body备份
    public final byte[] body;

    public RequestWrapper(HttpServletRequest request)  {
        super(request);

        //备份request里的body
        StringBuilder jsonData = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(request.getInputStream(), Charset.defaultCharset()));
            String line;
            while ((line = reader.readLine()) != null) {
                jsonData.append(line);
            }

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }finally{
            if(reader!=null)
            {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.warn(e.getMessage(),e);
                }
            }
        }

        body = jsonData.toString().getBytes(Charset.defaultCharset());

    }


    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {

        final ByteArrayInputStream inputStream = new ByteArrayInputStream(body);

        return new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return inputStream.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }
        };
    }
}
