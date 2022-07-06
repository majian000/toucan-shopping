package com.toucan.shopping.modules.admin.auth.mapper;

import com.toucan.shopping.modules.admin.auth.entity.AdminApp;
import com.toucan.shopping.modules.admin.auth.page.AdminAppPageInfo;
import com.toucan.shopping.modules.admin.auth.page.AdminPageInfo;
import com.toucan.shopping.modules.admin.auth.vo.AdminAppVO;
import com.toucan.shopping.modules.admin.auth.vo.AppLoginUserVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;


@Mapper
public interface AdminAppMapper {


    /**
     * 保存关联
     * @param adminApp
     * @return
     */
    int insert(AdminApp adminApp);

    /**
     * 根据实体查询列表
     * @param adminApp
     * @return
     */
    List<AdminApp> findListByEntity(AdminApp adminApp);

    /**
     * 删除指定应用编码下所有关联
     * @param appCode
     * @return
     */
    int deleteByAppCode(String appCode);


    /**
     * 查询账号应用关联列表
     * @param query
     * @return
     */
    List<AdminAppVO> findAppListByAdminAppEntity(AdminApp query);



    /**
     * 查询列表页
     * @param adminPageInfo
     * @return
     */
    List<AdminAppVO> queryListPage(AdminAppPageInfo adminPageInfo);

    /**
     * 返回列表页数量
     * @param adminPageInfo
     * @return
     */
    Long queryListPageCount(AdminAppPageInfo adminPageInfo);


    /**
     * 查询列表页
     * @param adminPageInfo
     * @return
     */
    List<AdminAppVO> queryOnlineListPage(AdminAppPageInfo adminPageInfo);

    /**
     * 返回列表页数量
     * @param adminPageInfo
     * @return
     */
    Long queryOnlineListPageCount(AdminAppPageInfo adminPageInfo);

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    AdminAppVO findById(Long id);

    /**
     * 查询列表页
     * @param adminPageInfo
     * @return
     */
    List<AdminAppVO> queryLoginListPage(AdminAppPageInfo adminPageInfo);

    /**
     * 返回列表页数量
     * @param adminPageInfo
     * @return
     */
    Long queryLoginListPageCount(AdminAppPageInfo adminPageInfo);


    /**
     * 删除指定账号下所有关联
     * @param adminId
     * @return
     */
    int deleteByAdminId(String adminId);


    /**
     * 删除指定账号下指定应用的关联
     * @param adminId
     * @return
     */
    int deleteByAdminIdAndAppCode(String adminId, String appCode);

    /**
     * 更新登录状态
     * @param adminId
     * @param appCode
     * @param loginStatus
     * @return
     */
    int updateLoginStatus(String adminId,String appCode,Short loginStatus);



    /**
     * 查询应用登录用户数
     * @param appLoginUserVO
     * @return
     */
    List<AppLoginUserVO> queryAppLoginUserCountList(AppLoginUserVO appLoginUserVO);
}
