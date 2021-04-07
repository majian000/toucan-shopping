package com.toucan.shopping.admin.auth.mapper;

import com.toucan.shopping.admin.auth.entity.Admin;
import com.toucan.shopping.admin.auth.page.AdminPageInfo;
import com.toucan.shopping.admin.auth.vo.AdminVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface AdminMapper {

    public int insert(Admin admin);

    public List<Admin> findListByEntity(Admin admin);

    List<AdminVO> queryListPage(AdminPageInfo adminPageInfo);

    Long queryListPageCount(AdminPageInfo adminPageInfo);
}
