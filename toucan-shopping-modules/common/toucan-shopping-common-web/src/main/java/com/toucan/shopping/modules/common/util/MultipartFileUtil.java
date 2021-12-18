package com.toucan.shopping.modules.common.util;

import com.toucan.shopping.modules.common.multipart.ToucanMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

public class MultipartFileUtil {

    public static MultipartFile base64ConvertMutipartFile(String base64) throws Exception {

        String[] baseStrs = base64.split(",");

        BASE64Decoder decoder = new BASE64Decoder();
        byte[] b = new byte[0];
        b = decoder.decodeBuffer(baseStrs[1]);
        for (int i = 0; i < b.length; ++i) {
            if (b[i] < 0) {
                b[i] += 256;
            }
        }
        return new ToucanMultipartFile(b, baseStrs[0]);
    }

}
