package com.toucan.shopping.modules.poi.util;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 操作poi的word对象
 */
public class POIDocHelper {

    /**
     * 直接返回poi word对象的字节数组
     * @param document
     * @return
     * @throws IOException
     */
    public static byte[] getByteArray(XWPFDocument document) throws IOException  {
        byte[] documentByteArray = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            document.write(byteArrayOutputStream);
            documentByteArray = byteArrayOutputStream.toByteArray();
        }catch(Exception e)
        {
            throw e;
        }finally{
            byteArrayOutputStream.reset();
        }
        return documentByteArray;
    }

}
