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

    List<UserMobilePhone> findListByMobilePhone(String mobilePhone);

    List<UserMobilePhone> findListByMobilePhoneLike(String mobilePhone);

    List<UserMobilePhone> queryListByUserMainId(Long[] userMainIdArray);

    List<UserMobilePhone> queryListByUserMainIdNothingDeleteStatus(Long[] userMainIdArray);


    List<UserMobilePhone> queryListPageNothingDeleteStatus(UserPageInfo appPageInfo);

    Long queryListPageNothingDeleteStatusCount(UserPageInfo appPageInfo);

}
