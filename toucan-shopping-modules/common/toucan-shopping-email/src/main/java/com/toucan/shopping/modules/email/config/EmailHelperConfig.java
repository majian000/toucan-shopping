package com.toucan.shopping.modules.email.config;


import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.email.EmailConfig;
import com.toucan.shopping.modules.email.helper.EmailConfigHelper;
import com.toucan.shopping.modules.email.queue.EmailQueue;
import com.toucan.shopping.modules.email.validate.EmailConfigValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class EmailHelperConfig {


    @Autowired
    private Toucan toucan;

    @Autowired
    private EmailQueue emailQueue;


    @PostConstruct
    public void initConfig()
    {
        EmailConfigValidator.validate(toucan.getModules().getEmail());
        EmailConfigHelper.toucan=toucan;
    }

}
