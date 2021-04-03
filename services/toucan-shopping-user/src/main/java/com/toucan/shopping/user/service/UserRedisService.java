package com.toucan.shopping.user.service;

import com.toucan.shopping.user.entity.User;

import java.util.List;

public interface UserRedisService {

    public List<User> findListByMobileOrEmail(String username);
}
