package com.toucan.shopping.modules.user.mapper;

import com.toucan.shopping.modules.user.entity.UserTrueNameApprove;
import com.toucan.shopping.modules.user.page.UserTrueNameApprovePageInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * 用户实名制审核
 */
@Mapper
public interface UserTrueNameApproveMapper {

    int insert(UserTrueNameApprove entity);

    List<UserTrueNameApprove> findListByEntity(UserTrueNameApprove entity);

    List<UserTrueNameApprove> queryListPage(UserTrueNameApprovePageInfo queryPageInfo);

    Long queryListPageCount(UserTrueNameApprovePageInfo queryPageInfo);



}
