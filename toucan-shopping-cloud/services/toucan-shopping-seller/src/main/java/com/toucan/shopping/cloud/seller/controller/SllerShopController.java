package com.toucan.shopping.cloud.seller.controller;


import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import com.toucan.shopping.modules.sller.service.SllerShopService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 收货地址 增删改查
 */
@RestController
@RequestMapping("/consigneeAddress")
public class SllerShopController {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private SkylarkLock skylarkLock;

    @Autowired
    private SllerShopService sllerShopService;





}
