package com.toucan.shopping.modules.common.properties.adminAuthScheduler;

import lombok.Data;

/**
 * 权限中台调度
 */
@Data
public class AdminAuthScheduler {

    private boolean loopEsCache = false;

    private boolean loopLoginCache = false;


}
