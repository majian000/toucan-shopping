package com.toucan.shopping.modules.user.service;

import com.toucan.shopping.modules.user.entity.User;

import java.util.List;

public interface UserRedisService {

    public List<User> findListByMobileOrEmail(String username);
}
