package com.toucan.shopping.modules.admin.auth.mapper;

import com.toucan.shopping.modules.admin.auth.entity.AdminLoginHistory;
import com.toucan.shopping.modules.admin.auth.page.AdminLoginHistoryPageInfo;
import com.toucan.shopping.modules.admin.auth.vo.AdminLoginHistoryVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface AdminLoginHistoryMapper {


    /**
     * 保存
     * @param adminLoginHistory
     * @return
     */
    int insert(AdminLoginHistory adminLoginHistory);

    /**
     * 查询列表分页
     * @param pageInfo
     * @return
     */
    List<AdminLoginHistoryVO> queryListPage(AdminLoginHistoryPageInfo pageInfo);

    /**
     * 查询列表分页数量
     * @param pageInfo
     * @return
     */
    Long queryListPageCount(AdminLoginHistoryPageInfo pageInfo);

    /**
     * 根据实体查询列表
     * @param entity
     * @return
     */
    List<AdminLoginHistory> findListByEntity(AdminLoginHistory entity);

}
