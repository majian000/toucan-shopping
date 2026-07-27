package com.toucan.shopping.modules.product.controller;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.product.business.service.BrandBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 品牌管理
 * @author majian
 *
 */
@RestController
@RequestMapping("/brand")
public class BrandController {

    @Autowired
    private BrandBusinessService brandBusinessService;


    /**
     * 保存
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/save", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO save(@RequestBody RequestJsonVO requestJsonVO)
    {
        return brandBusinessService.save(requestJsonVO);
    }


    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/list/by/categoryId", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO queryListByCategoryId(@RequestBody RequestJsonVO requestJsonVO)
    {
        return brandBusinessService.queryListByCategoryId(requestJsonVO);
    }


    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/list/page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestJsonVO)
    {
        return brandBusinessService.queryListPage(requestJsonVO);
    }


    /**
     * 更新
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO update(@RequestBody RequestJsonVO requestJsonVO)
    {
        return brandBusinessService.update(requestJsonVO);
    }


    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/id", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public ResultObjectVO findById(@RequestBody RequestJsonVO requestVo){
        return brandBusinessService.findById(requestVo);
    }


    /**
     * 根据ID集合查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/idList", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public ResultObjectVO findByIdList(@RequestBody RequestJsonVO requestVo){
        return brandBusinessService.findByIdList(requestVo);
    }


    /**
     * 根据名称以及分类ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/name/categoryId/enabled", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public ResultObjectVO findListByNameAndCategoryIdAndEnabled(@RequestBody RequestJsonVO requestVo){
        return brandBusinessService.findListByNameAndCategoryIdAndEnabled(requestVo);
    }


    /**
     * 根据ID删除
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/delete/id", produces = "application/json;charset=UTF-8", method = RequestMethod.DELETE)
    public ResultObjectVO deleteById(@RequestBody RequestJsonVO requestJsonVO)
    {
        return brandBusinessService.deleteById(requestJsonVO);
    }


    /**
     * 批量删除
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/ids", produces = "application/json;charset=UTF-8", method = RequestMethod.DELETE)
    public ResultObjectVO deleteByIds(@RequestBody RequestJsonVO requestVo){
        return brandBusinessService.deleteByIds(requestVo);
    }


    /**
     * 保存类别
     * @return
     */
    @RequestMapping(value="/saveByDisk", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO saveByDisk()
    {
        return brandBusinessService.saveByDisk();
    }


}
