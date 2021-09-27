package com.toucan.shopping.modules.user.mapper;

import com.toucan.shopping.modules.user.entity.UserUserName;
import com.toucan.shopping.modules.user.page.UserPageInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface UserUserNameMapper {

    int insert(UserUserName userUserName);

    List<UserUserName> findListByEntity(UserUserName userUserName);

    List<UserUserName> queryListByUserId(Long[] userIdArray);

    List<UserUserName> findListByUserName(String username);

    List<UserUserName> findListByUserNameAndUserMainIdList(String username,List<Long> userMianIdList);

    List<UserUserName> findListByUserMainId(Long userMainId);


    List<UserUserName> findListByEntityNothingDeleteStatus(UserUserName entity);

    List<UserUserName> queryListPageNothingDeleteStatus(UserPageInfo appPageInfo);

    Long queryListPageNothingDeleteStatusCount(UserPageInfo appPageInfo);

    int updateDeleteStatus(Short deleteStatus, Long userMainId, String username);

    int deleteByUserMainId(Long userMainId);

}
