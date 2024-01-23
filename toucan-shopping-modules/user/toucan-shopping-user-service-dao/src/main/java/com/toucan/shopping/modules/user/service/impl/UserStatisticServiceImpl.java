package com.toucan.shopping.modules.user.service.impl;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.user.entity.User;
import com.toucan.shopping.modules.user.mapper.UserMapper;
import com.toucan.shopping.modules.user.mapper.UserStatisticMapper;
import com.toucan.shopping.modules.user.page.UserPageInfo;
import com.toucan.shopping.modules.user.service.UserService;
import com.toucan.shopping.modules.user.service.UserStatisticService;
import com.toucan.shopping.modules.user.vo.UserStatisticVO;
import com.toucan.shopping.modules.user.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserStatisticServiceImpl implements UserStatisticService {

    @Autowired
    private UserStatisticMapper userStatisticMapper;


    @Override
    public UserStatisticVO queryTotalAndTodayAndCurrentMonthAndCurrentYear() {
        UserStatisticVO userStatisticVO = new UserStatisticVO();
        userStatisticVO.setTotal(userStatisticMapper.queryTotal()); //总数
        userStatisticVO.setTodayCount(userStatisticMapper.queryTodayTotal()); //今日新增
        userStatisticVO.setCurMonthCount(userStatisticMapper.queryCurMonthTotal()); //本月新增
        userStatisticVO.setCurYearCount(userStatisticMapper.queryCurYearTotal()); //本年新增
        return userStatisticVO;
    }
}
