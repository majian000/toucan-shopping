package com.toucan.shopping.cloud.apps.seller.web.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.toucan.shopping.cloud.apps.seller.web.service.ProvinceCityAreaGeneratorService;
import com.toucan.shopping.cloud.common.data.api.feign.service.FeignAreaService;
import com.toucan.shopping.modules.area.vo.AreaVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service
public class ProvinceCityAreaGeneratorServiceImpl implements ProvinceCityAreaGeneratorService {


    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignAreaService feignAreaService;


    @Value("${spring.profiles.active}")
    private String profile;


    public void generateFile(HttpServletRequest httpServletRequest, String filePath) throws Exception
    {
        try {
            Configuration configuration = new Configuration(Configuration.VERSION_2_3_30);
            if("dev".equals(profile)) {
                URL url = this.getClass().getClassLoader().getResource(toucan.getShoppingSellerWebPC().getFreemarker().getFtlLocation());
                configuration.setDirectoryForTemplateLoading(new File(url.getPath()));
            }else if("prod".equals(profile))
            {
                configuration.setClassForTemplateLoading(this.getClass(),"/"+toucan.getShoppingSellerWebPC().getFreemarker().getFtlLocation());
                configuration.setTemplateLoader(new ClassTemplateLoader(this.getClass(),"/"+toucan.getShoppingSellerWebPC().getFreemarker().getFtlLocation()));
            }
            //拿到首页模板
            String templateAndStatisFileName = "area/province_city_area.html";
            Template template = configuration.getTemplate(templateAndStatisFileName);

            String outDirePath = filePath+"/";
            File direFile = new File(outDirePath);
            if(!direFile.exists())
            {
                direFile.mkdirs();
            }
            String outFilePath = outDirePath+templateAndStatisFileName;
            FileWriterWithEncoding fileWriterWithEncoding = null;
            try {
                fileWriterWithEncoding = new FileWriterWithEncoding(outFilePath, "UTF-8");
                Map<String,Object> params= new HashMap<String,Object>();


                //查询省市区大对象
                RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), new AreaVO());
                ResultObjectVO resultObjectVO = feignAreaService.queryFullCache(requestJsonVO.sign(), requestJsonVO);
                if(resultObjectVO.isSuccess())
                {
                    params.put("areaList", JSONArray.toJSONString(resultObjectVO.getData()));
                }else{
                    params.put("areaList","[]");
                }

                //设置basepath
                params.put("basePath","");

                template.process(params, fileWriterWithEncoding);
            }catch(Exception e)
            {
                throw e;
            }finally{
                if(fileWriterWithEncoding!=null)
                {
                    fileWriterWithEncoding.flush();
                    fileWriterWithEncoding.close();
                }
            }
        }catch(Exception e)
        {
            throw e;
        }
    }
}
