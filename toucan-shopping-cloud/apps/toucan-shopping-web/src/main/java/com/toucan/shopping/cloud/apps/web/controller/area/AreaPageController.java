package com.toucan.shopping.cloud.apps.web.controller.area;

import com.toucan.shopping.cloud.apps.web.service.IndexService;
import com.toucan.shopping.cloud.apps.web.service.ProvinceCityAreaGeneratorService;
import com.toucan.shopping.modules.common.properties.Toucan;
import freemarker.cache.ClassTemplateLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.File;


/**
 * 省市区控制器
 */
@Controller("pageAreaController")
@RequestMapping("/htmls")
public class AreaPageController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${spring.profiles.active}")
    private String profile;

    @Autowired
    private Toucan toucan;

    @Autowired
    private ProvinceCityAreaGeneratorService provinceCityAreaGeneratorService;


    @RequestMapping("/province/city/area")
    public String provinceCityAreaPage(HttpServletRequest request)
    {

        try {
            File dirFile = new File(toucan.getShoppingPC().getFreemarker().getReleaseLocation()+"/area");
            if(!dirFile.exists())
            {
                dirFile.mkdirs();
            }
            File file = new File(toucan.getShoppingPC().getFreemarker().getReleaseLocation()+"/area/province_city_area.html");
            if(file==null||!file.exists()) {
                if (toucan.getShoppingPC() != null && toucan.getShoppingPC().getFreemarker() != null) {
                    //生成文件
                    provinceCityAreaGeneratorService.generateFile(request, toucan.getShoppingPC().getFreemarker().getReleaseLocation());
                }
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return "/htmls/release/area/province_city_area";
    }


}
