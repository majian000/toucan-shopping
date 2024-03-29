package com.toucan.shopping.modules.user.mapper;

import com.toucan.shopping.modules.user.entity.UserEmail;
import com.toucan.shopping.modules.user.page.UserPageInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface UserEmailMapper {

    int insert(UserEmail userEmail);

    List<UserEmail> findListByEntity(UserEmail userEmail);

    List<UserEmail> queryListByUserId(Long[] userIdArray);

    List<UserEmail> findListByEmail(String email);

    List<UserEmail> findListByEmailAndUserMianIdList(String email,List<Long> userMianIdList);

    List<UserEmail> findListByUserMainId(Long userMainId);


    List<UserEmail> queryListPageNothingDeleteStatus(UserPageInfo appPageInfo);

    Long queryListPageNothingDeleteStatusCount(UserPageInfo appPageInfo);


    List<UserEmail> findListByEntityNothingDeleteStatus(UserEmail entity);

    int updateDeleteStatus(Short deleteStatus, Long userMainId, String email);


    int updateDeleteStatusById(Short deleteStatus, Long userMainId, String email,Long id);

    int deleteByUserMainId(Long userMainId);

    UserEmail findByUserMainId(Long userMainId);
}
