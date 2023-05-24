package com.toucan.shopping.modules.user.queue;

import com.toucan.shopping.modules.user.vo.UserLoginHistoryVO;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 登录历史队列
 */
@Component
public class UserLoginHistoryQueue {

    private ConcurrentLinkedQueue<UserLoginHistoryVO> UserLoginHistoryVOs = new ConcurrentLinkedQueue<UserLoginHistoryVO>();

    public void push(UserLoginHistoryVO UserLoginHistoryVO)
    {
        UserLoginHistoryVOs.add(UserLoginHistoryVO);
    }

    public UserLoginHistoryVO pop()
    {
        return UserLoginHistoryVOs.poll();
    }
}
