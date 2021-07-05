package com.toucan.shopping.modules.user.mapper;

import com.toucan.shopping.modules.user.entity.User;
import com.toucan.shopping.modules.user.page.UserPageInfo;
import com.toucan.shopping.modules.user.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface UserMapper {

    int insert(User user);

    List<User> findListByEntity(User user);

    List<User> findListByEntityNothingDeleteStatus(User user);

    int deleteById(Long id);

    List<User> findById(Long id);


    List<UserVO> queryListPage(UserPageInfo appPageInfo);

    Long queryListPageCount(UserPageInfo appPageInfo);

    int updateEnableStatus(Short enableStatus,Long userMainId);

    List<User> findListByUserMainId(Long userMainId);

    int updatePasswordByUserMainId(Long userMainId, String password);

    List<User> findByUserMainId(Long id);
}
