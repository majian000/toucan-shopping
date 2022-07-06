package com.toucan.shopping.modules.admin.auth.mapper;

import com.toucan.shopping.modules.admin.auth.entity.AdminApp;
import com.toucan.shopping.modules.admin.auth.entity.AdminLoginHistory;
import com.toucan.shopping.modules.admin.auth.page.AdminAppPageInfo;
import com.toucan.shopping.modules.admin.auth.vo.AdminAppVO;
import com.toucan.shopping.modules.admin.auth.vo.AppLoginUserVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;


@Mapper
public interface AdminLoginHistoryMapper {


    /**
     * 保存关联
     * @param adminLoginHistory
     * @return
     */
    int insert(AdminLoginHistory adminLoginHistory);

}
