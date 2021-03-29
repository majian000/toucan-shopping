package com.toucan.shopping.center.admin.auth.mapper;

import com.toucan.shopping.center.admin.auth.entity.AdminApp;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface AdminAppMapper {

    public int insert(AdminApp AdminApp);

    public List<AdminApp> findListByEntity(AdminApp AdminApp);

    public int deleteByAppCode(String appCode);


}
