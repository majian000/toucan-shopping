package com.toucan.shopping.modules.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {


    private static final Logger logger = LoggerFactory.getLogger(MD5Util.class);

    private static MessageDigest md5;
    static{
        try {
            md5 =  MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            logger.warn(e.getMessage(),e);
        }
    }

    /**
     * MD5 32位加密
     * @param text
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String md5(String text) throws NoSuchAlgorithmException {

        md5.update(text.getBytes());
        byte bytes[] = md5.digest();

        int i;

        StringBuilder builder = new StringBuilder("");
        for (int offset = 0; offset < bytes.length; offset++) {
            i = bytes[offset];
            if (i < 0)
                i += 256;
            if (i < 16)
                builder.append("0");
            builder.append(Integer.toHexString(i));
        }

        return builder.toString();
    }

    public static void main(String[] args) throws Exception
    {
        System.out.println(MD5Util.md5("测试商品11111"));
        System.out.println(MD5Util.md5("测试商品11111"));
        System.out.println(MD5Util.md5("测试商品2222"));
    }
}
