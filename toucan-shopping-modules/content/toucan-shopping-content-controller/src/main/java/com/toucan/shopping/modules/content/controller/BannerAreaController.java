package com.toucan.shopping.modules.content.controller;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.content.business.service.BannerAreaBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 轮播图地区操作
 */
@RestController
@RequestMapping("/bannerArea")
public class BannerAreaController {

    @Autowired
    private BannerAreaBusinessService bannerAreaBusinessService;

    /**
     * 查询指定轮播图下所有地区关联
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/query/list",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryBannerAreaList(@RequestBody RequestJsonVO requestJsonVO)
    {
        return bannerAreaBusinessService.queryBannerAreaList(requestJsonVO);
    }

}
