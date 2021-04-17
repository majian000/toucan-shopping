package com.toucan.shopping.modules.admin.auth.mapper;

import com.toucan.shopping.modules.admin.auth.entity.AdminApp;
import com.toucan.shopping.modules.admin.auth.vo.AdminAppVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface AdminAppMapper {

    int insert(AdminApp AdminApp);

    List<AdminApp> findListByEntity(AdminApp AdminApp);

    int deleteByAppCode(String appCode);


    List<AdminAppVO> findAppListByAdminId(AdminApp query);



}
