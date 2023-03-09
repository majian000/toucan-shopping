package com.toucan.shopping.modules.user.mapper;

import com.toucan.shopping.modules.user.entity.UserMobilePhone;
import com.toucan.shopping.modules.user.page.UserPageInfo;
import com.toucan.shopping.modules.user.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface UserMobilePhoneMapper {

    int insert(UserMobilePhone userMobilePhone);

    List<UserMobilePhone> findListByEntity(UserMobilePhone userMobilePhone);

    List<UserMobilePhone> findListByEntityNothingDeleteStatus(UserMobilePhone userMobilePhone);

    List<UserMobilePhone> findListByMobilePhone(String mobilePhone);

    List<UserMobilePhone> findListByMobilePhoneAndUserMainIdList(String mobilePhone,List<Long> userMianIdList);

    List<UserMobilePhone> findListByMobilePhoneLike(String mobilePhone);

    List<UserMobilePhone> queryListByUserMainId(Long[] userMainIdArray);

    List<UserMobilePhone> queryListByUserMainIdNothingDeleteStatus(Long[] userMainIdArray);


    List<UserMobilePhone> queryListPageNothingDeleteStatus(UserPageInfo appPageInfo);

    Long queryListPageNothingDeleteStatusCount(UserPageInfo appPageInfo);

    int updateDeleteStatus(Short deleteStatus, Long userMainId, String mobilePhone);

    int deleteByUserMainId(Long userMainId);

    UserMobilePhone findByUserMainId(Long userMainId);
}
