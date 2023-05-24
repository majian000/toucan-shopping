package com.toucan.shopping.cloud.apps.web.controller.generate;

import com.alibaba.fastjson.JSONArray;
import com.toucan.shopping.cloud.apps.web.service.ProvinceCityAreaGeneratorService;
import com.toucan.shopping.cloud.common.data.api.feign.service.FeignAreaService;
import com.toucan.shopping.modules.area.vo.AreaVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.MD5Util;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@RestController("htmlProvinceCityAreaController")
@RequestMapping("/api/html/province/city/area")
public class ProvinceCityAreaGeneratorController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignAreaService feignAreaService;


    @Value("${spring.profiles.active}")
    private String profile;


    @Autowired
    private ProvinceCityAreaGeneratorService provinceCityAreaGeneratorService;




    /**
     * 生成最终文件
     * @return
     */
    @RequestMapping("/generate/release")
    @ResponseBody
    public ResultObjectVO generateRelease(HttpServletRequest httpServletRequest)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try{
            String token = httpServletRequest.getHeader("ts_web_generator_token");
            if(StringUtils.isEmpty(token))
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("生成失败,签名为空");
                return resultObjectVO;
            }
            String genToken = MD5Util.md5("toucan_shopping_generator");
            if(!genToken.equals(token))
            {
                logger.warn("签名验证失败,正确签名为 {}",genToken);
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("生成失败,签名错误");
                return resultObjectVO;
            }
            if(toucan.getShoppingPC()!=null&&toucan.getShoppingPC().getFreemarker()!=null) {
                //生成文件
                provinceCityAreaGeneratorService.generateFile(httpServletRequest,toucan.getShoppingPC().getFreemarker().getReleaseLocation());
            }

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("生成失败,请稍后重试");
        }

        return resultObjectVO;
    }





}
