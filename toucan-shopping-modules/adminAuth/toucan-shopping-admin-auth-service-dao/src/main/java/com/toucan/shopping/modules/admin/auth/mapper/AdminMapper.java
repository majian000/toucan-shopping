package com.toucan.shopping.modules.admin.auth.mapper;

import com.toucan.shopping.modules.admin.auth.entity.Admin;
import com.toucan.shopping.modules.admin.auth.page.AdminPageInfo;
import com.toucan.shopping.modules.admin.auth.vo.AdminVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface AdminMapper {

    /**
     * 保存账号
     * @param admin
     * @return
     */
    int insert(Admin admin);

    /**
     * 根据实体查询列表
     * @param admin
     * @return
     */
    List<Admin> findListByEntity(Admin admin);

    /**
     * 查询列表页
     * @param adminPageInfo
     * @return
     */
    List<AdminVO> queryListPage(AdminPageInfo adminPageInfo);

    /**
     * 返回列表页数量
     * @param adminPageInfo
     * @return
     */
    Long queryListPageCount(AdminPageInfo adminPageInfo);

    /**
     * 修改
     * @param admin
     * @return
     */
    int update(Admin admin);

    /**
     * 修改密码
     * @param adminVO
     * @return
     */
    int updatePassword(AdminVO adminVO);


    /**
     * 根据ID删除
     * @param id
     * @return
     */
    int deleteById(Long id);
}
