package com.toucan.shopping.cloud.apps.admin.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;


@MapperScan({"com.toucan.shopping.modules.common.persistence.mapper",
        "com.toucan.shopping.modules.common.persistence.event.mapper",
        "com.toucan.shopping.modules.category.mapper",
        "com.toucan.shopping.modules.order.mapper",
        "com.toucan.shopping.modules.product.mapper",
        "com.toucan.shopping.modules.user.mapper",
        "com.toucan.shopping.modules.admin.auth.mapper",
        "com.toucan.shopping.modules.stock.mapper",
        "com.toucan.shopping.modules.seller.mapper",
        "com.toucan.shopping.modules.area.mapper",
        "com.toucan.shopping.modules.content.mapper",
        "com.toucan.shopping.modules.column.mapper",
        "com.toucan.shopping.modules.color.table.mapper",
        "com.toucan.shopping.modules.message.mapper"})
@Configuration
public class MybatisConfig {

}
