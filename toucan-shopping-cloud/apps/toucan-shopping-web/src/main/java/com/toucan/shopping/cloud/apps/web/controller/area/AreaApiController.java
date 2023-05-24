package com.toucan.shopping.cloud.apps.web.controller.area;


import com.toucan.shopping.cloud.apps.web.controller.BaseController;
import com.toucan.shopping.cloud.apps.web.service.IndexService;
import com.toucan.shopping.cloud.common.data.api.feign.service.FeignAreaService;
import com.toucan.shopping.modules.area.entity.Area;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 地区控制器
 */
@RestController("areaApiController")
@RequestMapping("/api/area")
public class AreaApiController extends BaseController {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private FeignAreaService feignAreaService;


    /**
     * 省市区级联
     * @return
     */
    @UserAuth
    @RequestMapping("/query/pid")
    public ResultObjectVO queryListByPid(@RequestBody Area area)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try{
            if(area.getPid()==null)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("父节点ID不能为空");
                return resultObjectVO;
            }
            Area query = new Area();
            query.setPid(area.getPid());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(),query);
            resultObjectVO = feignAreaService.queryListByPid(requestJsonVO.sign(),requestJsonVO);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }

}
