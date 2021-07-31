package com.toucan.shopping.cloud.apps.web.controller.generate;

import com.toucan.shopping.cloud.apps.web.service.IndexService;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
@RestController("htmlIndexController")
@RequestMapping("/api/html/index")
public class IndexController {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private Toucan toucan;


    @Autowired
    private IndexService indexService;


    /**
     * 生成html
     * @return
     */
    @RequestMapping("/generate")
    @ResponseBody
    public ResultObjectVO generate(HttpServletRequest httpServletRequest)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try{
            if(toucan.getShoppingPC()!=null&&toucan.getShoppingPC().getFreemarker()!=null) {

                URL url = this.getClass().getClassLoader().getResource(toucan.getShoppingPC().getFreemarker().getFtlLocation());
                Configuration configuration = new Configuration(Configuration.VERSION_2_3_30);
                configuration.setDirectoryForTemplateLoading(new File(url.getPath()));
                //拿到首页模板
                String templateAndStatisFileName = "index.html";
                Template template = configuration.getTemplate(templateAndStatisFileName);

                String outDirePath = toucan.getShoppingPC().getFreemarker().getStaticLocation()+"/";
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

                    //查询轮播图
                    params.put("banners",indexService.queryBanners());

                    //查询类别列表
                    params.put("categorys", indexService.queryCategorys());

                    //设置basepath
                    params.put("basePath",httpServletRequest.getScheme() + "://" + httpServletRequest.getServerName() + ":" + httpServletRequest.getServerPort() + httpServletRequest.getContextPath());


                    template.process(params, fileWriterWithEncoding);
                }catch(Exception e)
                {
                    logger.warn(e.getMessage(),e);
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("生成失败,请稍后重试");
                }finally{
                    if(fileWriterWithEncoding!=null)
                    {
                        fileWriterWithEncoding.flush();
                        fileWriterWithEncoding.close();
                    }
                }
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
