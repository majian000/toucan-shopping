package com.toucan.shopping.user.mapper;

import com.toucan.shopping.user.entity.User;
import com.toucan.shopping.user.page.UserPageInfo;
import com.toucan.shopping.user.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface UserMapper {

    int insert(User user);

    List<User> findListByEntity(User user);

    int deleteById(Long id);

    List<User> findById(Long id);


    List<UserVO> queryListPage(UserPageInfo appPageInfo);

    Long queryListPageCount(UserPageInfo appPageInfo);

}
