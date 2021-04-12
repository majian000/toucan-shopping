package com.toucan.shopping.modules.common.util;

import java.util.UUID;

public class GlobalUUID {

    public static String uuid()
    {
        return UUID.randomUUID().toString().replace("-","");
    }
}
