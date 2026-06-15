package com.toucan.shopping.modules.common.util;

import com.toucan.shopping.modules.common.multipart.ToucanMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

public class MultipartFileUtil {

    public static MultipartFile base64ConvertMutipartFile(String base64) throws Exception {

        String[] baseStrs = base64.split(",");

        byte[] b = Base64.getMimeDecoder().decode(baseStrs[1]);
        return new ToucanMultipartFile(b, baseStrs[0]);
    }

}
