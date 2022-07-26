package com.toucan.shopping.modules.user.mapper;

import com.toucan.shopping.modules.user.entity.UserDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface UserDetailMapper {

    int insert(UserDetail entity);

    List<UserDetail> findListByEntity(UserDetail entity);

    int deleteById(Long id);

    List<UserDetail> findById(Long id);

    List<UserDetail> queryListByUserId(Long[] userIdArray);

    int update(UserDetail entity);

    int updateInfo(UserDetail entity);

    int updateIsShop(UserDetail entity);

    List<UserDetail> findByUserMainId(Long id);

    int updateHeadSculpture(UserDetail entity);

}
