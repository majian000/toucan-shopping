package com.toucan.shopping.modules.common.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.List;

/**
 * HTTP请求封装
 * @author majian
 * @date 2021-8-3 16:59:28
 */
public class HttpUtils {

    public static String get(String url, List<Header> headerList) throws Exception
    {
        StringBuilder builder = new StringBuilder();
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try{
            httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            if(CollectionUtils.isNotEmpty(headerList))
            {
                for(Header header:headerList)
                {
                    httpGet.addHeader(header);
                }
            }
            response = httpClient.execute(httpGet);
            //解析响应
            if (response.getStatusLine().getStatusCode() == 200) {
                builder.append(EntityUtils.toString(response.getEntity(), "UTF8"));
            }
        }catch(Exception e)
        {
            throw e;
        }finally{
            if(response!=null)
            {
                response.close();
            }
            if(httpClient!=null)
            {
                httpClient.close();
            }
        }
        return builder.toString();
    }
}
