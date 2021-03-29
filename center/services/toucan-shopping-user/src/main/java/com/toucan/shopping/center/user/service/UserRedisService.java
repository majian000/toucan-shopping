package com.toucan.shopping.center.user.service;

import com.toucan.shopping.center.user.entity.User;

import java.util.List;

public interface UserRedisService {

    public List<User> findListByMobileOrEmail(String username);
}
