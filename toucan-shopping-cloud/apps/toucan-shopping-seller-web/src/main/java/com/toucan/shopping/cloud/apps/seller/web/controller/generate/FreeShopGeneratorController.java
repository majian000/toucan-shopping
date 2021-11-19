package com.toucan.shopping.cloud.apps.seller.web.controller.generate;

import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.MD5Util;
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
 * 首页生成为html
 */
@RestController("htmlFreeShopController")
@RequestMapping("/api/html/freeShop")
public class FreeShopGeneratorController {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private Toucan toucan;

    @Value("${spring.profiles.active}")
    private String profile;


    private void generateFile(HttpServletRequest httpServletRequest,String filePath) throws Exception
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
            String templateAndStatisFileName = "freeShop.html";
            Template template = configuration.getTemplate(templateAndStatisFileName);

            String outDirePath = filePath+"/";
            File direFile = new File(outDirePath);
            if(!direFile.exists())
            {
                direFile.mkdirs();
            }
            String outFilePath = outDirePath+templateAndStatisFileName;
            File outFile = new File(outFilePath);
            FileWriterWithEncoding fileWriterWithEncoding = null;
            try {
                fileWriterWithEncoding = new FileWriterWithEncoding(outFilePath, "UTF-8");
                Map<String,Object> params= new HashMap<String,Object>();


                //设置basepath
                params.put("basePath","");
                if(toucan.getShoppingPC()!=null&&toucan.getShoppingPC().getBasePath()!=null) {
                    params.put("shoppingPcPath", toucan.getShoppingPC());
                }else{
                    params.put("shoppingPcPath", "");
                }


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
            if(toucan.getShoppingSellerWebPC()!=null&&toucan.getShoppingSellerWebPC().getFreemarker()!=null) {
                //生成文件
                generateFile(httpServletRequest,toucan.getShoppingSellerWebPC().getFreemarker().getReleaseLocation());
            }

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("生成失败,请稍后重试");
        }

        return resultObjectVO;
    }


    /**
     * 生成预览文件
     * @return
     */
    @RequestMapping("/generate/preview")
    @ResponseBody
    public ResultObjectVO generatePreview(HttpServletRequest httpServletRequest)
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
            if(toucan.getShoppingSellerWebPC()!=null&&toucan.getShoppingSellerWebPC().getFreemarker()!=null) {
                //生成文件
                generateFile(httpServletRequest,toucan.getShoppingSellerWebPC().getFreemarker().getPreviewLocation());
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
