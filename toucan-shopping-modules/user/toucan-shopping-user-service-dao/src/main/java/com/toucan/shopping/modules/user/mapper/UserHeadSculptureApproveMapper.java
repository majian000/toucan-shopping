package com.toucan.shopping.modules.user.mapper;

import com.toucan.shopping.modules.user.entity.UserHeadSculptureApprove;
import com.toucan.shopping.modules.user.page.UserHeadSculptureApprovePageInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * 用户实名制审核
 */
@Mapper
public interface UserHeadSculptureApproveMapper {

    int insert(UserHeadSculptureApprove entity);

    int update(UserHeadSculptureApprove entity);

    List<UserHeadSculptureApprove> findListByEntity(UserHeadSculptureApprove entity);

    List<UserHeadSculptureApprove> findListByEntityOrderByUpdateDateDesc(UserHeadSculptureApprove entity);


    List<UserHeadSculptureApprove> queryListPage(UserHeadSculptureApprovePageInfo queryPageInfo);

    Long queryListPageCount(UserHeadSculptureApprovePageInfo queryPageInfo);

    int deleteById(Long id);

}
