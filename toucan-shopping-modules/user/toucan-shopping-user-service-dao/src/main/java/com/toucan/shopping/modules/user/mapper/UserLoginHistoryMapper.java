package com.toucan.shopping.modules.user.mapper;

import com.toucan.shopping.modules.user.entity.UserLoginHistory;
import com.toucan.shopping.modules.user.page.UserLoginHistoryPageInfo;
import com.toucan.shopping.modules.user.vo.UserLoginHistoryVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface UserLoginHistoryMapper {

    int insert(UserLoginHistory entity);

    List<UserLoginHistory> queryListPage(UserLoginHistoryPageInfo queryPageInfo);

    Long queryListPageCount(UserLoginHistoryPageInfo queryPageInfo);

    List<UserLoginHistory> queryListByCreateDateDesc(UserLoginHistoryVO query);

}
