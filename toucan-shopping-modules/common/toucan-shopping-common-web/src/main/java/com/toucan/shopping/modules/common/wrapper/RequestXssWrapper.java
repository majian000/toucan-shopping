package com.toucan.shopping.modules.common.wrapper;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.xss.XSSConvert;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 解决request读取body 只能读取一次问题
 */
public class RequestXssWrapper extends HttpServletRequestWrapper {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    //将HttpServletRequest里的body备份
    public byte[] body;

    private HttpServletRequest request;

    public RequestXssWrapper(HttpServletRequest request)  {
        super(request);

        String contentType=request.getHeader("Content-Type");

        //如果是json请求就替换掉这个json里的所有参数
        if(StringUtils.isNotEmpty(contentType)&&contentType.toLowerCase().indexOf("application/json")!=-1) {

            //备份request里的body
            StringBuilder jsonData = new StringBuilder();
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(request.getInputStream(), Charset.defaultCharset()));
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonData.append(line);
                }

                //替换这个字符串的所有XSS代码
                String jsonReplaceData = XSSConvert.replaceStringXSS(jsonData.toString());
                body = jsonReplaceData.getBytes(Charset.defaultCharset());

            } catch (Exception e) {
                logger.warn(e.getMessage(), e);
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        logger.warn(e.getMessage(), e);
                    }
                }
            }
        }
        this.request = request;
    }

    @Override
    public String getParameter(String name) {
        String value = this.request.getParameter(name);
        if(StringUtils.isNotEmpty(value))
        {
            try {
                return XSSConvert.replaceStringXSS(value);
            }catch(Exception e)
            {
                logger.warn(e.getMessage(),e);
            }
        }
        return value;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> paramMap = (Map<String, String[]>) super.getParameterMap();
        Iterator<String> keys = paramMap.keySet().iterator();
        Map<String,String[]> newParamMap = new HashMap<String,String[]>();
        //复制一个新的对象,因为旧的对象不允许修改
        while(keys.hasNext())
        {
            String key = keys.next();
            newParamMap.put(key,paramMap.get(key));
        }

        Iterator iterator = newParamMap.entrySet().iterator();
        while(iterator.hasNext())
        {
            Map.Entry<String, String[]> entryMap = (Map.Entry<String, String[]>)iterator.next();
            String []values = entryMap.getValue();
            for (int i = 0; i < values.length; i++) {
                if (values[i] instanceof String) {
                    try {
                        values[i] = XSSConvert.replaceStringXSS(values[i]);
                    } catch (Exception e) {
                        logger.warn(e.getMessage(), e);
                    }
                }
            }
            entryMap.setValue(values);
        }
        return newParamMap;
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = this.request.getParameterValues(name);
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            try {
                encodedValues[i] = XSSConvert.replaceStringXSS(values[i]);
            }catch(Exception e)
            {
                logger.warn(e.getMessage(),e);
            }
        }
        return encodedValues;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        if(StringUtils.isNotEmpty(value))
        {
            try {
                return XSSConvert.replaceStringXSS(value);
            }catch(Exception e)
            {
                logger.warn(e.getMessage(),e);
            }
        }
        return value;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if(body==null){
            body=new byte[0];
        }
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
