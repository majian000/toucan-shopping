package com.toucan.shopping.cloud.seller.controller;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.redis.service.ToucanStringRedisService;
import com.toucan.shopping.modules.seller.constant.FreightTemplateConstant;
import com.toucan.shopping.modules.seller.constant.ShopConstant;
import com.toucan.shopping.modules.seller.entity.FreightTemplate;
import com.toucan.shopping.modules.seller.entity.FreightTemplateAreaRule;
import com.toucan.shopping.modules.seller.entity.FreightTemplateDefaultRule;
import com.toucan.shopping.modules.seller.entity.SellerShop;
import com.toucan.shopping.modules.seller.page.FreightTemplatePageInfo;
import com.toucan.shopping.modules.seller.page.SellerShopPageInfo;
import com.toucan.shopping.modules.seller.redis.FreightTemplateKey;
import com.toucan.shopping.modules.seller.redis.SellerShopKey;
import com.toucan.shopping.modules.seller.service.*;
import com.toucan.shopping.modules.seller.util.FreightTemplateUtils;
import com.toucan.shopping.modules.seller.vo.FreightTemplateAreaRuleVO;
import com.toucan.shopping.modules.seller.vo.FreightTemplateDefaultRuleVO;
import com.toucan.shopping.modules.seller.vo.FreightTemplateVO;
import com.toucan.shopping.modules.seller.vo.SellerShopVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 运费模板
 * @author majian
 * @date 2022-9-21 14:13:19
 */
@RestController
@RequestMapping("/freightTemplate")
public class FreightTemplateController {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private SkylarkLock skylarkLock;

    @Autowired
    private FreightTemplateService freightTemplateService;

    @Autowired
    private FreightTemplateDefaultRuleService freightTemplateDefaultRuleService;

    @Autowired
    private FreightTemplateAreaRuleService freightTemplateAreaRuleService;

    @Autowired
    private Toucan toucan;

    @Autowired
    private SellerShopService sellerShopService;





    /**
     * 查询列表页
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/list/page",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            FreightTemplatePageInfo queryPageInfo = JSONObject.parseObject(requestVo.getEntityJson(), FreightTemplatePageInfo.class);

            if(StringUtils.isEmpty(requestVo.getAppCode()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到应用编码");
                return resultObjectVO;
            }

            //查询列表页
            resultObjectVO.setData(freightTemplateService.queryListPage(queryPageInfo));

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }



    /**
     * 保存
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/save",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO save(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            logger.info("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }

        FreightTemplateVO freightTemplateVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), FreightTemplateVO.class);
        String userMainId = String.valueOf(freightTemplateVO.getUserMainId());
        try {

            boolean lockStatus = skylarkLock.lock(FreightTemplateKey.getSaveLockKey(userMainId), userMainId);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
                return resultObjectVO;
            }


            if(StringUtils.isEmpty(freightTemplateVO.getName()))
            {
                //释放锁
                skylarkLock.unLock(FreightTemplateKey.getSaveLockKey(userMainId), userMainId);

                logger.warn("名称为空 param:"+ JSONObject.toJSONString(freightTemplateVO));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("名称不能为空!");


                return resultObjectVO;
            }
            if(freightTemplateVO.getUserMainId()==null)
            {
                //释放锁
                skylarkLock.unLock(FreightTemplateKey.getSaveLockKey(userMainId), userMainId);

                logger.warn("用户ID为空 param:"+ JSONObject.toJSONString(freightTemplateVO));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("用户ID不能为空!");
                return resultObjectVO;
            }

            if(freightTemplateVO.getShopId()==null)
            {
                //释放锁
                skylarkLock.unLock(FreightTemplateKey.getSaveLockKey(userMainId), userMainId);

                logger.warn("店铺ID为空 param:"+ JSONObject.toJSONString(freightTemplateVO));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("店铺ID不能为空!");
                return resultObjectVO;
            }


            FreightTemplateVO queryCountVO = new FreightTemplateVO();
            queryCountVO.setUserMainId(freightTemplateVO.getUserMainId());
            queryCountVO.setShopId(freightTemplateVO.getShopId());
            Long count = freightTemplateService.queryCount(queryCountVO);
            if(count+1>toucan.getSeller().getFreightTemplateMaxCount())
            {
                //释放锁
                skylarkLock.unLock(FreightTemplateKey.getSaveLockKey(userMainId), userMainId);

                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("数量已达到上限");
                return resultObjectVO;
            }

            queryCountVO.setName(freightTemplateVO.getName());
            count = freightTemplateService.queryCount(queryCountVO);
            if(count>0)
            {
                //释放锁
                skylarkLock.unLock(FreightTemplateKey.getSaveLockKey(userMainId), userMainId);

                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("模板已存在,请检查名称是否重复");
                return resultObjectVO;
            }

            Long templateId = idGenerator.id();
            freightTemplateVO.setId(templateId);
            freightTemplateVO.setCreateDate(new Date());
            freightTemplateVO.setDeleteStatus((short)0);

            int row = freightTemplateService.save(freightTemplateVO);
            if (row < 1) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请重试!");
                return resultObjectVO;
            }

            try {
                //快递默认运费规则
                if (freightTemplateVO.getExpressDefaultRule() != null
                        && freightTemplateVO.getExpressDefaultRule().getDefaultWeight() != null) {

                    FreightTemplateDefaultRuleVO expressDefaultRule = freightTemplateVO.getExpressDefaultRule();
                    expressDefaultRule.setId(idGenerator.id());
                    expressDefaultRule.setUserMainId(freightTemplateVO.getUserMainId());
                    expressDefaultRule.setShopId(freightTemplateVO.getShopId());
                    expressDefaultRule.setCreateDate(new Date());
                    expressDefaultRule.setDeleteStatus((short) 0);
                    expressDefaultRule.setTemplateId(freightTemplateVO.getId());
                    expressDefaultRule.setTransportModel("1");
                    row = freightTemplateDefaultRuleService.save(expressDefaultRule);
                    if(row<=0)
                    {
                        throw new IllegalArgumentException("保存失败");
                    }

                    //保存快递的所有选择项
                    if (!CollectionUtils.isEmpty(freightTemplateVO.getExpressAreaRules())) {
                        List<FreightTemplateAreaRule> freightTemplateAreaRules = new LinkedList<>();
                        for (FreightTemplateAreaRuleVO freightTemplateAreaRuleVO : freightTemplateVO.getExpressAreaRules()) {
                            if (StringUtils.isNotEmpty(freightTemplateAreaRuleVO.getSelectAreas())) {
                                String[] selectAreaArray = freightTemplateAreaRuleVO.getSelectAreas().split(FreightTemplateConstant.VIEW_SELECT_AREA_SPLIT);
                                Long groupId = idGenerator.id();
                                if (selectAreaArray != null && selectAreaArray.length > 0) {
                                    //查询出这行选的所有地市
                                    for (String selectArea : selectAreaArray) {
                                        FreightTemplateAreaRule freightTemplateAreaRule = new FreightTemplateAreaRule();
                                        BeanUtils.copyProperties(freightTemplateAreaRule, freightTemplateAreaRuleVO);
                                        freightTemplateAreaRule.setId(idGenerator.id());
                                        freightTemplateAreaRule.setTransportModel("1");
                                        freightTemplateAreaRule.setCityName(selectArea);
                                        freightTemplateAreaRule.setGroupId(groupId);
                                        freightTemplateAreaRule.setCityCode(freightTemplateVO.getCityNameToCityCode().get(selectArea));
                                        freightTemplateAreaRule.setProvinceCode(freightTemplateVO.getCityCodeToProvinceCode().get(freightTemplateAreaRule.getCityCode()));
                                        freightTemplateAreaRule.setProvinceName(freightTemplateVO.getCityNameToProvinceName().get(selectArea));
                                        freightTemplateAreaRule.setAreaCode("");
                                        freightTemplateAreaRule.setAreaName("");
                                        freightTemplateAreaRule.setTemplateId(freightTemplateVO.getId());
                                        freightTemplateAreaRule.setDefaultRuleId(expressDefaultRule.getId());
                                        freightTemplateAreaRule.setUserMainId(freightTemplateVO.getUserMainId());
                                        freightTemplateAreaRule.setShopId(freightTemplateVO.getShopId());
                                        freightTemplateAreaRule.setCreateDate(new Date());
                                        freightTemplateAreaRule.setDeleteStatus((short) 0);
                                        freightTemplateAreaRules.add(freightTemplateAreaRule);
                                    }
                                }
                            }
                        }

                        //防止insert超过1000行 所以在这里先进行保存
                        if (!CollectionUtils.isEmpty(freightTemplateAreaRules)) {
                            row = freightTemplateAreaRuleService.saves(freightTemplateAreaRules);
                            if(row<=0)
                            {
                                throw new IllegalArgumentException("保存失败");
                            }
                        }
                    }

                }

                //EMS默认运费规则
                if (freightTemplateVO.getEmsDefaultRule() != null
                        && freightTemplateVO.getEmsDefaultRule().getDefaultWeight() != null) {
                    FreightTemplateDefaultRuleVO emsDefaultRule = freightTemplateVO.getEmsDefaultRule();
                    emsDefaultRule.setId(idGenerator.id());
                    emsDefaultRule.setUserMainId(freightTemplateVO.getUserMainId());
                    emsDefaultRule.setShopId(freightTemplateVO.getShopId());
                    emsDefaultRule.setCreateDate(new Date());
                    emsDefaultRule.setDeleteStatus((short) 0);
                    emsDefaultRule.setTemplateId(freightTemplateVO.getId());
                    emsDefaultRule.setTransportModel("2");
                    row = freightTemplateDefaultRuleService.save(emsDefaultRule);
                    if(row<=0)
                    {
                        throw new IllegalArgumentException("保存失败");
                    }


                    //保存EMS的所有选择项
                    if (!CollectionUtils.isEmpty(freightTemplateVO.getEmsAreaRules())) {
                        List<FreightTemplateAreaRule> freightTemplateAreaRules = new LinkedList<>();
                        for (FreightTemplateAreaRuleVO freightTemplateAreaRuleVO : freightTemplateVO.getEmsAreaRules()) {
                            if (StringUtils.isNotEmpty(freightTemplateAreaRuleVO.getSelectAreas())) {
                                String[] selectAreaArray = freightTemplateAreaRuleVO.getSelectAreas().split(FreightTemplateConstant.VIEW_SELECT_AREA_SPLIT);
                                Long groupId = idGenerator.id();
                                if (selectAreaArray != null && selectAreaArray.length > 0) {
                                    //查询出这行选的所有地市
                                    for (String selectArea : selectAreaArray) {
                                        FreightTemplateAreaRule freightTemplateAreaRule = new FreightTemplateAreaRule();
                                        BeanUtils.copyProperties(freightTemplateAreaRule, freightTemplateAreaRuleVO);
                                        freightTemplateAreaRule.setId(idGenerator.id());
                                        freightTemplateAreaRule.setTransportModel("2");
                                        freightTemplateAreaRule.setCityName(selectArea);
                                        freightTemplateAreaRule.setGroupId(groupId);
                                        freightTemplateAreaRule.setCityCode(freightTemplateVO.getCityNameToCityCode().get(selectArea));
                                        freightTemplateAreaRule.setProvinceCode(freightTemplateVO.getCityCodeToProvinceCode().get(freightTemplateAreaRule.getCityCode()));
                                        freightTemplateAreaRule.setProvinceName(freightTemplateVO.getCityNameToProvinceName().get(selectArea));
                                        freightTemplateAreaRule.setAreaCode("");
                                        freightTemplateAreaRule.setAreaName("");
                                        freightTemplateAreaRule.setTemplateId(freightTemplateVO.getId());
                                        freightTemplateAreaRule.setDefaultRuleId(emsDefaultRule.getId());
                                        freightTemplateAreaRule.setUserMainId(freightTemplateVO.getUserMainId());
                                        freightTemplateAreaRule.setShopId(freightTemplateVO.getShopId());
                                        freightTemplateAreaRule.setCreateDate(new Date());
                                        freightTemplateAreaRule.setDeleteStatus((short) 0);
                                        freightTemplateAreaRules.add(freightTemplateAreaRule);
                                    }
                                }
                            }
                        }

                        //防止insert超过1000行 所以在这里先进行保存
                        if (!CollectionUtils.isEmpty(freightTemplateAreaRules)) {
                            row = freightTemplateAreaRuleService.saves(freightTemplateAreaRules);
                            if(row<=0)
                            {
                                throw new IllegalArgumentException("保存失败");
                            }
                        }
                    }
                }


                //平邮默认运费规则
                if (freightTemplateVO.getOrdinaryMailDefaultRule() != null
                        && freightTemplateVO.getOrdinaryMailDefaultRule().getDefaultWeight() != null) {
                    FreightTemplateDefaultRuleVO ordinaryMailDefaultRule = freightTemplateVO.getOrdinaryMailDefaultRule();
                    ordinaryMailDefaultRule.setId(idGenerator.id());
                    ordinaryMailDefaultRule.setUserMainId(freightTemplateVO.getUserMainId());
                    ordinaryMailDefaultRule.setShopId(freightTemplateVO.getShopId());
                    ordinaryMailDefaultRule.setCreateDate(new Date());
                    ordinaryMailDefaultRule.setDeleteStatus((short) 0);
                    ordinaryMailDefaultRule.setTemplateId(freightTemplateVO.getId());
                    ordinaryMailDefaultRule.setTransportModel("3");
                    row = freightTemplateDefaultRuleService.save(ordinaryMailDefaultRule);
                    if(row<=0)
                    {
                        throw new IllegalArgumentException("保存失败");
                    }

                    //保存平邮的所有选择项
                    if (!CollectionUtils.isEmpty(freightTemplateVO.getOrdinaryMailAreaRules())) {
                        List<FreightTemplateAreaRule> freightTemplateAreaRules = new LinkedList<>();
                        for (FreightTemplateAreaRuleVO freightTemplateAreaRuleVO : freightTemplateVO.getOrdinaryMailAreaRules()) {
                            if (StringUtils.isNotEmpty(freightTemplateAreaRuleVO.getSelectAreas())) {
                                String[] selectAreaArray = freightTemplateAreaRuleVO.getSelectAreas().split(FreightTemplateConstant.VIEW_SELECT_AREA_SPLIT);
                                Long groupId = idGenerator.id();
                                if (selectAreaArray != null && selectAreaArray.length > 0) {
                                    //查询出这行选的所有地市
                                    for (String selectArea : selectAreaArray) {
                                        FreightTemplateAreaRule freightTemplateAreaRule = new FreightTemplateAreaRule();
                                        BeanUtils.copyProperties(freightTemplateAreaRule, freightTemplateAreaRuleVO);
                                        freightTemplateAreaRule.setId(idGenerator.id());
                                        freightTemplateAreaRule.setTransportModel("3");
                                        freightTemplateAreaRule.setCityName(selectArea);
                                        freightTemplateAreaRule.setGroupId(groupId);
                                        freightTemplateAreaRule.setCityCode(freightTemplateVO.getCityNameToCityCode().get(selectArea));
                                        freightTemplateAreaRule.setProvinceCode(freightTemplateVO.getCityCodeToProvinceCode().get(freightTemplateAreaRule.getCityCode()));
                                        freightTemplateAreaRule.setProvinceName(freightTemplateVO.getCityNameToProvinceName().get(selectArea));
                                        freightTemplateAreaRule.setAreaCode("");
                                        freightTemplateAreaRule.setAreaName("");
                                        freightTemplateAreaRule.setTemplateId(freightTemplateVO.getId());
                                        freightTemplateAreaRule.setDefaultRuleId(ordinaryMailDefaultRule.getId());
                                        freightTemplateAreaRule.setUserMainId(freightTemplateVO.getUserMainId());
                                        freightTemplateAreaRule.setShopId(freightTemplateVO.getShopId());
                                        freightTemplateAreaRule.setCreateDate(new Date());
                                        freightTemplateAreaRule.setDeleteStatus((short) 0);
                                        freightTemplateAreaRules.add(freightTemplateAreaRule);
                                    }
                                }
                            }
                        }

                        //防止insert超过1000行 所以在这里先进行保存
                        if (!CollectionUtils.isEmpty(freightTemplateAreaRules)) {
                            row = freightTemplateAreaRuleService.saves(freightTemplateAreaRules);
                            if(row<=0)
                            {
                                throw new IllegalArgumentException("保存失败");
                            }
                        }
                    }
                }
            }catch(Exception e)
            {
                logger.warn("保存失败 开始回滚数据 templateId {} ",templateId);
                freightTemplateService.deleteById(templateId);
                freightTemplateAreaRuleService.deleteByTemplateId(templateId);
                freightTemplateDefaultRuleService.deleteByTemplateId(templateId);
                throw e;
            }

        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试!");
            logger.warn(e.getMessage(),e);
        }finally{
            skylarkLock.unLock(FreightTemplateKey.getSaveLockKey(userMainId), userMainId);
        }
        return resultObjectVO;
    }





    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/id/userMainId",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO findByIdAndUserMainId(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            FreightTemplateVO query = JSONObject.parseObject(requestVo.getEntityJson(),FreightTemplateVO.class);
            if(query.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到ID");
                return resultObjectVO;
            }
            if(query.getUserMainId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到用户ID");
                return resultObjectVO;
            }

            SellerShop sellerShop = sellerShopService.findByUserMainId(query.getUserMainId());

            if(sellerShop==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到店铺");
                return resultObjectVO;
            }
            if(sellerShop.getEnableStatus().shortValue()==0)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("店铺已被禁用");
                return resultObjectVO;
            }

            //查询运费模板
            List<FreightTemplate> freightTemplates = freightTemplateService.queryListByVO(query);
            if(CollectionUtils.isEmpty(freightTemplates))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("对象不存在!");
                return resultObjectVO;
            }

            FreightTemplateVO freightTemplateVO = new FreightTemplateVO();
            BeanUtils.copyProperties(freightTemplateVO,freightTemplates.get(0));

            freightTemplateVO.setExpressAreaRules(new LinkedList<>());
            freightTemplateVO.setEmsAreaRules(new LinkedList<>());
            freightTemplateVO.setOrdinaryMailAreaRules(new LinkedList<>());

            //查询默认运费规则
            FreightTemplateDefaultRuleVO queryFreightTemplateDefaultRuleVO= new FreightTemplateDefaultRuleVO();
            queryFreightTemplateDefaultRuleVO.setTemplateId(freightTemplateVO.getId());
            List<FreightTemplateDefaultRule> freightTemplateDefaultRules = freightTemplateDefaultRuleService.queryListByVO(queryFreightTemplateDefaultRuleVO);
            if(!CollectionUtils.isEmpty(freightTemplateDefaultRules))
            {
                for(FreightTemplateDefaultRule freightTemplateDefaultRule:freightTemplateDefaultRules)
                {
                    FreightTemplateDefaultRuleVO freightTemplateDefaultRuleVO = new FreightTemplateDefaultRuleVO();
                    BeanUtils.copyProperties(freightTemplateDefaultRuleVO,freightTemplateDefaultRule);
                    if("1".equals(freightTemplateDefaultRuleVO.getTransportModel()))
                    {
                        freightTemplateVO.setExpressDefaultRule(freightTemplateDefaultRuleVO);
                    }else if("2".equals(freightTemplateDefaultRuleVO.getTransportModel()))
                    {
                        freightTemplateVO.setEmsDefaultRule(freightTemplateDefaultRuleVO);
                    }else if("3".equals(freightTemplateDefaultRuleVO.getTransportModel()))
                    {
                        freightTemplateVO.setOrdinaryMailDefaultRule(freightTemplateDefaultRuleVO);
                    }
                }
            }

            //查询快递地区规则
            FreightTemplateAreaRuleVO queryFreightTemplateAreaRuleVO = new FreightTemplateAreaRuleVO();
            queryFreightTemplateAreaRuleVO.setTemplateId(freightTemplateVO.getId());
            queryFreightTemplateAreaRuleVO.setTransportModel("1");
            List<FreightTemplateAreaRule> expressRules =  freightTemplateAreaRuleService.queryListByVO(queryFreightTemplateAreaRuleVO);
            if(!CollectionUtils.isEmpty(expressRules))
            {
                Map<Long, List<FreightTemplateAreaRule>> expressRuleMap = expressRules.stream().collect(Collectors.groupingBy(FreightTemplateAreaRule::getGroupId));
                Set<Long> expressRuleKeys = expressRuleMap.keySet();
                for(Long expressKey:expressRuleKeys)
                {
                    FreightTemplateAreaRuleVO freightTemplateAreaRuleVO = new FreightTemplateAreaRuleVO();
                    List<FreightTemplateAreaRule> rows = expressRuleMap.get(expressKey);
                    BeanUtils.copyProperties(freightTemplateAreaRuleVO,rows.get(0));
                    String selectAreas = rows.stream().map(FreightTemplateAreaRule::getCityName).collect(Collectors.joining(FreightTemplateConstant.VIEW_SELECT_AREA_SPLIT));
                    freightTemplateAreaRuleVO.setSelectAreas(selectAreas);
                    freightTemplateAreaRuleVO.setSelectItems(rows);
                    freightTemplateVO.getExpressAreaRules().add(freightTemplateAreaRuleVO);
                }
            }


            //查询EMS地区规则
            queryFreightTemplateAreaRuleVO.setTransportModel("2");
            List<FreightTemplateAreaRule> emsRules =  freightTemplateAreaRuleService.queryListByVO(queryFreightTemplateAreaRuleVO);
            if(!CollectionUtils.isEmpty(emsRules))
            {
                Map<Long, List<FreightTemplateAreaRule>> emsRuleMap = emsRules.stream().collect(Collectors.groupingBy(FreightTemplateAreaRule::getGroupId));
                Set<Long> emsRuleKeys = emsRuleMap.keySet();
                for(Long expressKey:emsRuleKeys)
                {
                    FreightTemplateAreaRuleVO freightTemplateAreaRuleVO = new FreightTemplateAreaRuleVO();
                    List<FreightTemplateAreaRule> rows = emsRuleMap.get(expressKey);
                    BeanUtils.copyProperties(freightTemplateAreaRuleVO,rows.get(0));
                    String selectAreas = rows.stream().map(FreightTemplateAreaRule::getCityName).collect(Collectors.joining(FreightTemplateConstant.VIEW_SELECT_AREA_SPLIT));
                    freightTemplateAreaRuleVO.setSelectAreas(selectAreas);
                    freightTemplateAreaRuleVO.setSelectItems(rows);
                    freightTemplateVO.getEmsAreaRules().add(freightTemplateAreaRuleVO);
                }
            }



            //查询平邮地区规则
            queryFreightTemplateAreaRuleVO.setTransportModel("3");
            List<FreightTemplateAreaRule> ordinaryMailRules =  freightTemplateAreaRuleService.queryListByVO(queryFreightTemplateAreaRuleVO);
            if(!CollectionUtils.isEmpty(ordinaryMailRules))
            {
                Map<Long, List<FreightTemplateAreaRule>> emsRuleMap = ordinaryMailRules.stream().collect(Collectors.groupingBy(FreightTemplateAreaRule::getGroupId));
                Set<Long> emsKeys = emsRuleMap.keySet();
                for(Long expressKey:emsKeys)
                {
                    FreightTemplateAreaRuleVO freightTemplateAreaRuleVO = new FreightTemplateAreaRuleVO();
                    List<FreightTemplateAreaRule> rows = emsRuleMap.get(expressKey);
                    BeanUtils.copyProperties(freightTemplateAreaRuleVO,rows.get(0));
                    String selectAreas = rows.stream().map(FreightTemplateAreaRule::getCityName).collect(Collectors.joining(FreightTemplateConstant.VIEW_SELECT_AREA_SPLIT));
                    freightTemplateAreaRuleVO.setSelectAreas(selectAreas);
                    freightTemplateAreaRuleVO.setSelectItems(rows);
                    freightTemplateVO.getOrdinaryMailAreaRules().add(freightTemplateAreaRuleVO);
                }
            }

            resultObjectVO.setData(freightTemplateVO);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/id",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO findById(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            FreightTemplateVO query = JSONObject.parseObject(requestVo.getEntityJson(),FreightTemplateVO.class);
            if(query.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到ID");
                return resultObjectVO;
            }

            //查询运费模板
            List<FreightTemplate> freightTemplates = freightTemplateService.queryListByVO(query);
            if(CollectionUtils.isEmpty(freightTemplates))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("对象不存在!");
                return resultObjectVO;
            }

            FreightTemplateVO freightTemplateVO = new FreightTemplateVO();
            BeanUtils.copyProperties(freightTemplateVO,freightTemplates.get(0));

            freightTemplateVO.setExpressAreaRules(new LinkedList<>());
            freightTemplateVO.setEmsAreaRules(new LinkedList<>());
            freightTemplateVO.setOrdinaryMailAreaRules(new LinkedList<>());

            //查询默认运费规则
            FreightTemplateDefaultRuleVO queryFreightTemplateDefaultRuleVO= new FreightTemplateDefaultRuleVO();
            queryFreightTemplateDefaultRuleVO.setTemplateId(freightTemplateVO.getId());
            List<FreightTemplateDefaultRule> freightTemplateDefaultRules = freightTemplateDefaultRuleService.queryListByVO(queryFreightTemplateDefaultRuleVO);
            if(!CollectionUtils.isEmpty(freightTemplateDefaultRules))
            {
                for(FreightTemplateDefaultRule freightTemplateDefaultRule:freightTemplateDefaultRules)
                {
                    FreightTemplateDefaultRuleVO freightTemplateDefaultRuleVO = new FreightTemplateDefaultRuleVO();
                    BeanUtils.copyProperties(freightTemplateDefaultRuleVO,freightTemplateDefaultRule);
                    if("1".equals(freightTemplateDefaultRuleVO.getTransportModel()))
                    {
                        freightTemplateVO.setExpressDefaultRule(freightTemplateDefaultRuleVO);
                    }else if("2".equals(freightTemplateDefaultRuleVO.getTransportModel()))
                    {
                        freightTemplateVO.setEmsDefaultRule(freightTemplateDefaultRuleVO);
                    }else if("3".equals(freightTemplateDefaultRuleVO.getTransportModel()))
                    {
                        freightTemplateVO.setOrdinaryMailDefaultRule(freightTemplateDefaultRuleVO);
                    }
                }
            }

            //查询快递地区规则
            FreightTemplateAreaRuleVO queryFreightTemplateAreaRuleVO = new FreightTemplateAreaRuleVO();
            queryFreightTemplateAreaRuleVO.setTemplateId(freightTemplateVO.getId());
            queryFreightTemplateAreaRuleVO.setTransportModel("1");
            List<FreightTemplateAreaRule> expressRules =  freightTemplateAreaRuleService.queryListByVO(queryFreightTemplateAreaRuleVO);
            if(!CollectionUtils.isEmpty(expressRules))
            {
                Map<Long, List<FreightTemplateAreaRule>> expressRuleMap = expressRules.stream().collect(Collectors.groupingBy(FreightTemplateAreaRule::getGroupId));
                Set<Long> expressRuleKeys = expressRuleMap.keySet();
                for(Long expressKey:expressRuleKeys)
                {
                    FreightTemplateAreaRuleVO freightTemplateAreaRuleVO = new FreightTemplateAreaRuleVO();
                    List<FreightTemplateAreaRule> rows = expressRuleMap.get(expressKey);
                    BeanUtils.copyProperties(freightTemplateAreaRuleVO,rows.get(0));
                    String selectAreas = rows.stream().map(FreightTemplateAreaRule::getCityName).collect(Collectors.joining(FreightTemplateConstant.VIEW_SELECT_AREA_SPLIT));
                    freightTemplateAreaRuleVO.setSelectAreas(selectAreas);
                    freightTemplateAreaRuleVO.setSelectItems(rows);
                    freightTemplateVO.getExpressAreaRules().add(freightTemplateAreaRuleVO);
                }
            }


            //查询EMS地区规则
            queryFreightTemplateAreaRuleVO.setTransportModel("2");
            List<FreightTemplateAreaRule> emsRules =  freightTemplateAreaRuleService.queryListByVO(queryFreightTemplateAreaRuleVO);
            if(!CollectionUtils.isEmpty(emsRules))
            {
                Map<Long, List<FreightTemplateAreaRule>> emsRuleMap = emsRules.stream().collect(Collectors.groupingBy(FreightTemplateAreaRule::getGroupId));
                Set<Long> emsRuleKeys = emsRuleMap.keySet();
                for(Long expressKey:emsRuleKeys)
                {
                    FreightTemplateAreaRuleVO freightTemplateAreaRuleVO = new FreightTemplateAreaRuleVO();
                    List<FreightTemplateAreaRule> rows = emsRuleMap.get(expressKey);
                    BeanUtils.copyProperties(freightTemplateAreaRuleVO,rows.get(0));
                    String selectAreas = rows.stream().map(FreightTemplateAreaRule::getCityName).collect(Collectors.joining(FreightTemplateConstant.VIEW_SELECT_AREA_SPLIT));
                    freightTemplateAreaRuleVO.setSelectAreas(selectAreas);
                    freightTemplateAreaRuleVO.setSelectItems(rows);
                    freightTemplateVO.getEmsAreaRules().add(freightTemplateAreaRuleVO);
                }
            }



            //查询平邮地区规则
            queryFreightTemplateAreaRuleVO.setTransportModel("3");
            List<FreightTemplateAreaRule> ordinaryMailRules =  freightTemplateAreaRuleService.queryListByVO(queryFreightTemplateAreaRuleVO);
            if(!CollectionUtils.isEmpty(ordinaryMailRules))
            {
                Map<Long, List<FreightTemplateAreaRule>> emsRuleMap = ordinaryMailRules.stream().collect(Collectors.groupingBy(FreightTemplateAreaRule::getGroupId));
                Set<Long> emsKeys = emsRuleMap.keySet();
                for(Long expressKey:emsKeys)
                {
                    FreightTemplateAreaRuleVO freightTemplateAreaRuleVO = new FreightTemplateAreaRuleVO();
                    List<FreightTemplateAreaRule> rows = emsRuleMap.get(expressKey);
                    BeanUtils.copyProperties(freightTemplateAreaRuleVO,rows.get(0));
                    String selectAreas = rows.stream().map(FreightTemplateAreaRule::getCityName).collect(Collectors.joining(FreightTemplateConstant.VIEW_SELECT_AREA_SPLIT));
                    freightTemplateAreaRuleVO.setSelectAreas(selectAreas);
                    freightTemplateAreaRuleVO.setSelectItems(rows);
                    freightTemplateVO.getOrdinaryMailAreaRules().add(freightTemplateAreaRuleVO);
                }
            }

            resultObjectVO.setData(freightTemplateVO);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }





    /**
     * 修改
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/update",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO update(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            logger.info("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }

        FreightTemplateVO freightTemplateVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), FreightTemplateVO.class);
        if(freightTemplateVO.getId()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("ID不能为空!");
            return resultObjectVO;
        }

        String userMainId = String.valueOf(freightTemplateVO.getUserMainId());
        try {

            boolean lockStatus = skylarkLock.lock(FreightTemplateKey.getUpdateLockKey(userMainId), userMainId);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
                return resultObjectVO;
            }


            if(StringUtils.isEmpty(freightTemplateVO.getName()))
            {
                //释放锁
                skylarkLock.unLock(FreightTemplateKey.getUpdateLockKey(userMainId), userMainId);

                logger.warn("名称为空 param:"+ JSONObject.toJSONString(freightTemplateVO));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("名称不能为空!");


                return resultObjectVO;
            }
            if(freightTemplateVO.getUserMainId()==null)
            {
                //释放锁
                skylarkLock.unLock(FreightTemplateKey.getUpdateLockKey(userMainId), userMainId);

                logger.warn("用户ID为空 param:"+ JSONObject.toJSONString(freightTemplateVO));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("用户ID不能为空!");
                return resultObjectVO;
            }

            if(freightTemplateVO.getShopId()==null)
            {
                //释放锁
                skylarkLock.unLock(FreightTemplateKey.getUpdateLockKey(userMainId), userMainId);

                logger.warn("店铺ID为空 param:"+ JSONObject.toJSONString(freightTemplateVO));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("店铺ID不能为空!");
                return resultObjectVO;
            }


            FreightTemplateVO queryCountVO = new FreightTemplateVO();
            queryCountVO.setUserMainId(freightTemplateVO.getUserMainId());
            queryCountVO.setShopId(freightTemplateVO.getShopId());
            Long count = freightTemplateService.queryCount(queryCountVO);
            if(count+1>toucan.getSeller().getFreightTemplateMaxCount())
            {
                //释放锁
                skylarkLock.unLock(FreightTemplateKey.getUpdateLockKey(userMainId), userMainId);

                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("数量已达到上限");
                return resultObjectVO;
            }

            queryCountVO.setName(freightTemplateVO.getName());
            List<FreightTemplate> freightTemplates = freightTemplateService.queryListByVO(queryCountVO);
            if(!CollectionUtils.isEmpty(freightTemplates))
            {
                for(FreightTemplate freightTemplate:freightTemplates) {
                    if(freightTemplate.getId().longValue()!=freightTemplateVO.getId().longValue()) {
                        //释放锁
                        skylarkLock.unLock(FreightTemplateKey.getUpdateLockKey(userMainId), userMainId);

                        resultObjectVO.setCode(ResultVO.FAILD);
                        resultObjectVO.setMsg("模板已存在,请检查名称是否重复");
                        return resultObjectVO;
                    }
                }
            }

            //如果修改失败用于回滚数据
            FreightTemplate persistentFreightTemplate = freightTemplateService.findByIdAndUserMainId(freightTemplateVO.getId(),freightTemplateVO.getUserMainId());
            if(persistentFreightTemplate==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到该模板,请检查是否已被删除");
                return resultObjectVO;
            }
            //查询模板默认规则
            FreightTemplateDefaultRuleVO queryPerFreightTemplateDefaultRuleVO = new FreightTemplateDefaultRuleVO();
            queryPerFreightTemplateDefaultRuleVO.setTemplateId(freightTemplateVO.getId());
            queryPerFreightTemplateDefaultRuleVO.setUserMainId(freightTemplateVO.getUserMainId());
            List<FreightTemplateDefaultRule> persistentFreightTemplateDefaultRules = freightTemplateDefaultRuleService.queryListByVO(queryPerFreightTemplateDefaultRuleVO);
            //查询模板地区规则
            FreightTemplateAreaRuleVO queryPerFreightTemplateAreaRuleVO = new FreightTemplateAreaRuleVO();
            queryPerFreightTemplateAreaRuleVO.setTemplateId(freightTemplateVO.getId());
            queryPerFreightTemplateAreaRuleVO.setUserMainId(freightTemplateVO.getUserMainId());
            List<FreightTemplateAreaRule> persistentFreightTemplateAreaRules = freightTemplateAreaRuleService.queryListByVO(queryPerFreightTemplateAreaRuleVO);


            freightTemplateVO.setUpdateDate(new Date());
            int row = freightTemplateService.update(freightTemplateVO);
            if (row < 1) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("修改失败,请稍后重试!");
                return resultObjectVO;
            }

            try {

                //删除库中的默认规则
                freightTemplateAreaRuleService.deleteByTemplateId(persistentFreightTemplate.getId());
                //删除库中的地区规则
                freightTemplateDefaultRuleService.deleteByTemplateId(persistentFreightTemplate.getId());


                //快递默认运费规则
                if (freightTemplateVO.getExpressDefaultRule() != null
                        && freightTemplateVO.getExpressDefaultRule().getDefaultWeight() != null) {

                    FreightTemplateDefaultRuleVO expressDefaultRule = freightTemplateVO.getExpressDefaultRule();
                    expressDefaultRule.setId(idGenerator.id());
                    expressDefaultRule.setUserMainId(freightTemplateVO.getUserMainId());
                    expressDefaultRule.setShopId(freightTemplateVO.getShopId());
                    expressDefaultRule.setCreateDate(new Date());
                    expressDefaultRule.setDeleteStatus((short) 0);
                    expressDefaultRule.setTemplateId(freightTemplateVO.getId());
                    expressDefaultRule.setTransportModel("1");
                    row = freightTemplateDefaultRuleService.save(expressDefaultRule);
                    if(row<=0)
                    {
                        throw new IllegalArgumentException("保存失败");
                    }

                    //保存快递的所有选择项
                    if (!CollectionUtils.isEmpty(freightTemplateVO.getExpressAreaRules())) {
                        List<FreightTemplateAreaRule> freightTemplateAreaRules = new LinkedList<>();
                        for (FreightTemplateAreaRuleVO freightTemplateAreaRuleVO : freightTemplateVO.getExpressAreaRules()) {
                            if (StringUtils.isNotEmpty(freightTemplateAreaRuleVO.getSelectAreas())) {
                                String[] selectAreaArray = freightTemplateAreaRuleVO.getSelectAreas().split(FreightTemplateConstant.VIEW_SELECT_AREA_SPLIT);
                                Long groupId = idGenerator.id();
                                if (selectAreaArray != null && selectAreaArray.length > 0) {
                                    //查询出这行选的所有地市
                                    for (String selectArea : selectAreaArray) {
                                        FreightTemplateAreaRule freightTemplateAreaRule = new FreightTemplateAreaRule();
                                        BeanUtils.copyProperties(freightTemplateAreaRule, freightTemplateAreaRuleVO);
                                        freightTemplateAreaRule.setId(idGenerator.id());
                                        freightTemplateAreaRule.setTransportModel("1");
                                        freightTemplateAreaRule.setCityName(selectArea);
                                        freightTemplateAreaRule.setGroupId(groupId);
                                        freightTemplateAreaRule.setCityCode(freightTemplateVO.getCityNameToCityCode().get(selectArea));
                                        freightTemplateAreaRule.setProvinceCode(freightTemplateVO.getCityCodeToProvinceCode().get(freightTemplateAreaRule.getCityCode()));
                                        freightTemplateAreaRule.setProvinceName(freightTemplateVO.getCityNameToProvinceName().get(selectArea));
                                        freightTemplateAreaRule.setAreaCode("");
                                        freightTemplateAreaRule.setAreaName("");
                                        freightTemplateAreaRule.setTemplateId(freightTemplateVO.getId());
                                        freightTemplateAreaRule.setDefaultRuleId(expressDefaultRule.getId());
                                        freightTemplateAreaRule.setUserMainId(freightTemplateVO.getUserMainId());
                                        freightTemplateAreaRule.setShopId(freightTemplateVO.getShopId());
                                        freightTemplateAreaRule.setCreateDate(new Date());
                                        freightTemplateAreaRule.setDeleteStatus((short) 0);
                                        freightTemplateAreaRules.add(freightTemplateAreaRule);
                                    }
                                }
                            }
                        }

                        //防止insert超过1000行 所以在这里先进行保存
                        if (!CollectionUtils.isEmpty(freightTemplateAreaRules)) {
                            row = freightTemplateAreaRuleService.saves(freightTemplateAreaRules);
                            if(row<=0)
                            {
                                throw new IllegalArgumentException("保存失败");
                            }
                        }
                    }

                }

                //EMS默认运费规则
                if (freightTemplateVO.getEmsDefaultRule() != null
                        && freightTemplateVO.getEmsDefaultRule().getDefaultWeight() != null) {
                    FreightTemplateDefaultRuleVO emsDefaultRule = freightTemplateVO.getEmsDefaultRule();
                    emsDefaultRule.setId(idGenerator.id());
                    emsDefaultRule.setUserMainId(freightTemplateVO.getUserMainId());
                    emsDefaultRule.setShopId(freightTemplateVO.getShopId());
                    emsDefaultRule.setCreateDate(new Date());
                    emsDefaultRule.setDeleteStatus((short) 0);
                    emsDefaultRule.setTemplateId(freightTemplateVO.getId());
                    emsDefaultRule.setTransportModel("2");
                    row = freightTemplateDefaultRuleService.save(emsDefaultRule);
                    if(row<=0)
                    {
                        throw new IllegalArgumentException("保存失败");
                    }


                    //保存EMS的所有选择项
                    if (!CollectionUtils.isEmpty(freightTemplateVO.getEmsAreaRules())) {
                        List<FreightTemplateAreaRule> freightTemplateAreaRules = new LinkedList<>();
                        for (FreightTemplateAreaRuleVO freightTemplateAreaRuleVO : freightTemplateVO.getEmsAreaRules()) {
                            if (StringUtils.isNotEmpty(freightTemplateAreaRuleVO.getSelectAreas())) {
                                String[] selectAreaArray = freightTemplateAreaRuleVO.getSelectAreas().split(FreightTemplateConstant.VIEW_SELECT_AREA_SPLIT);
                                Long groupId = idGenerator.id();
                                if (selectAreaArray != null && selectAreaArray.length > 0) {
                                    //查询出这行选的所有地市
                                    for (String selectArea : selectAreaArray) {
                                        FreightTemplateAreaRule freightTemplateAreaRule = new FreightTemplateAreaRule();
                                        BeanUtils.copyProperties(freightTemplateAreaRule, freightTemplateAreaRuleVO);
                                        freightTemplateAreaRule.setId(idGenerator.id());
                                        freightTemplateAreaRule.setTransportModel("2");
                                        freightTemplateAreaRule.setCityName(selectArea);
                                        freightTemplateAreaRule.setGroupId(groupId);
                                        freightTemplateAreaRule.setCityCode(freightTemplateVO.getCityNameToCityCode().get(selectArea));
                                        freightTemplateAreaRule.setProvinceCode(freightTemplateVO.getCityCodeToProvinceCode().get(freightTemplateAreaRule.getCityCode()));
                                        freightTemplateAreaRule.setProvinceName(freightTemplateVO.getCityNameToProvinceName().get(selectArea));
                                        freightTemplateAreaRule.setAreaCode("");
                                        freightTemplateAreaRule.setAreaName("");
                                        freightTemplateAreaRule.setTemplateId(freightTemplateVO.getId());
                                        freightTemplateAreaRule.setDefaultRuleId(emsDefaultRule.getId());
                                        freightTemplateAreaRule.setUserMainId(freightTemplateVO.getUserMainId());
                                        freightTemplateAreaRule.setShopId(freightTemplateVO.getShopId());
                                        freightTemplateAreaRule.setCreateDate(new Date());
                                        freightTemplateAreaRule.setDeleteStatus((short) 0);
                                        freightTemplateAreaRules.add(freightTemplateAreaRule);
                                    }
                                }
                            }
                        }

                        //防止insert超过1000行 所以在这里先进行保存
                        if (!CollectionUtils.isEmpty(freightTemplateAreaRules)) {
                            row = freightTemplateAreaRuleService.saves(freightTemplateAreaRules);
                            if(row<=0)
                            {
                                throw new IllegalArgumentException("保存失败");
                            }
                        }
                    }
                }


                //平邮默认运费规则
                if (freightTemplateVO.getOrdinaryMailDefaultRule() != null
                        && freightTemplateVO.getOrdinaryMailDefaultRule().getDefaultWeight() != null) {
                    FreightTemplateDefaultRuleVO ordinaryMailDefaultRule = freightTemplateVO.getOrdinaryMailDefaultRule();
                    ordinaryMailDefaultRule.setId(idGenerator.id());
                    ordinaryMailDefaultRule.setUserMainId(freightTemplateVO.getUserMainId());
                    ordinaryMailDefaultRule.setShopId(freightTemplateVO.getShopId());
                    ordinaryMailDefaultRule.setCreateDate(new Date());
                    ordinaryMailDefaultRule.setDeleteStatus((short) 0);
                    ordinaryMailDefaultRule.setTemplateId(freightTemplateVO.getId());
                    ordinaryMailDefaultRule.setTransportModel("3");
                    row = freightTemplateDefaultRuleService.save(ordinaryMailDefaultRule);
                    if(row<=0)
                    {
                        throw new IllegalArgumentException("保存失败");
                    }

                    //保存平邮的所有选择项
                    if (!CollectionUtils.isEmpty(freightTemplateVO.getOrdinaryMailAreaRules())) {
                        List<FreightTemplateAreaRule> freightTemplateAreaRules = new LinkedList<>();
                        for (FreightTemplateAreaRuleVO freightTemplateAreaRuleVO : freightTemplateVO.getOrdinaryMailAreaRules()) {
                            if (StringUtils.isNotEmpty(freightTemplateAreaRuleVO.getSelectAreas())) {
                                String[] selectAreaArray = freightTemplateAreaRuleVO.getSelectAreas().split(FreightTemplateConstant.VIEW_SELECT_AREA_SPLIT);
                                Long groupId = idGenerator.id();
                                if (selectAreaArray != null && selectAreaArray.length > 0) {
                                    //查询出这行选的所有地市
                                    for (String selectArea : selectAreaArray) {
                                        FreightTemplateAreaRule freightTemplateAreaRule = new FreightTemplateAreaRule();
                                        BeanUtils.copyProperties(freightTemplateAreaRule, freightTemplateAreaRuleVO);
                                        freightTemplateAreaRule.setId(idGenerator.id());
                                        freightTemplateAreaRule.setTransportModel("3");
                                        freightTemplateAreaRule.setCityName(selectArea);
                                        freightTemplateAreaRule.setGroupId(groupId);
                                        freightTemplateAreaRule.setCityCode(freightTemplateVO.getCityNameToCityCode().get(selectArea));
                                        freightTemplateAreaRule.setProvinceCode(freightTemplateVO.getCityCodeToProvinceCode().get(freightTemplateAreaRule.getCityCode()));
                                        freightTemplateAreaRule.setProvinceName(freightTemplateVO.getCityNameToProvinceName().get(selectArea));
                                        freightTemplateAreaRule.setAreaCode("");
                                        freightTemplateAreaRule.setAreaName("");
                                        freightTemplateAreaRule.setTemplateId(freightTemplateVO.getId());
                                        freightTemplateAreaRule.setDefaultRuleId(ordinaryMailDefaultRule.getId());
                                        freightTemplateAreaRule.setUserMainId(freightTemplateVO.getUserMainId());
                                        freightTemplateAreaRule.setShopId(freightTemplateVO.getShopId());
                                        freightTemplateAreaRule.setCreateDate(new Date());
                                        freightTemplateAreaRule.setDeleteStatus((short) 0);
                                        freightTemplateAreaRules.add(freightTemplateAreaRule);
                                    }
                                }
                            }
                        }

                        //防止insert超过1000行 所以在这里先进行保存
                        if (!CollectionUtils.isEmpty(freightTemplateAreaRules)) {
                            row = freightTemplateAreaRuleService.saves(freightTemplateAreaRules);
                            if(row<=0)
                            {
                                throw new IllegalArgumentException("保存失败");
                            }
                        }

                    }
                }
//                throw new IllegalArgumentException("测试回滚数据");
            }catch(Exception e)
            {
                logger.warn("保存失败 开始回滚数据 修改前对象 {} ",JSONObject.toJSONString(persistentFreightTemplate));
                logger.warn("保存失败 开始回滚数据 修改后对象 {} ",JSONObject.toJSONString(freightTemplateVO));
                freightTemplateService.update(persistentFreightTemplate);

                freightTemplateAreaRuleService.deleteByTemplateId(persistentFreightTemplate.getId());
                freightTemplateDefaultRuleService.deleteByTemplateId(persistentFreightTemplate.getId());

                //恢复库中的默认规则对象
                if(!CollectionUtils.isEmpty(persistentFreightTemplateDefaultRules)) {

                    logger.warn("保存失败 开始回滚数据 默认规则对象 修改前对象 {} ",JSONObject.toJSONString(persistentFreightTemplateDefaultRules));
                    List<Long> idList = persistentFreightTemplateDefaultRules.stream().map(FreightTemplateDefaultRule::getId).collect(Collectors.toList());
                    freightTemplateDefaultRuleService.updateResumeByIdList(idList);
                }

                //恢复库中的地区规则对象
                if(!CollectionUtils.isEmpty(persistentFreightTemplateAreaRules)) {
                    logger.warn("保存失败 开始回滚数据 地区规则对象 修改前对象 {} ",JSONObject.toJSONString(persistentFreightTemplateAreaRules));
                    if(persistentFreightTemplateAreaRules.size()<100){
                        List<Long> idList = persistentFreightTemplateAreaRules.stream().map(FreightTemplateAreaRule::getId).collect(Collectors.toList());
                        freightTemplateAreaRuleService.updateResumeByIdList(idList);
                    }else {
                        List<Long> idList = new LinkedList<>();
                        for (int i = 0; i < persistentFreightTemplateAreaRules.size(); i++) {
                            idList.add(persistentFreightTemplateAreaRules.get(i).getId());
                            //到了数据边界 开始恢复
                            if((i+1)%100==0)
                            {
                                freightTemplateAreaRuleService.updateResumeByIdList(idList);
                                idList.clear();
                            }
                        }
                        //如果不整除的话 恢复剩余的
                        if(!CollectionUtils.isEmpty(idList))
                        {
                            freightTemplateAreaRuleService.updateResumeByIdList(idList);
                        }
                    }
                }
                throw e;
            }

        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试!");
            logger.warn(e.getMessage(),e);
        }finally{
            skylarkLock.unLock(FreightTemplateKey.getUpdateLockKey(userMainId), userMainId);
        }
        return resultObjectVO;
    }



    /**
     * 根据ID删除
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/delete/id",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteById(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            logger.info("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }
        if(requestJsonVO.getAppCode()==null)
        {
            logger.info("没有找到应用编码: param:"+ JSONObject.toJSONString(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到应用编码!");
            return resultObjectVO;
        }

        FreightTemplateVO freightTemplate = JSONObject.parseObject(requestJsonVO.getEntityJson(), FreightTemplateVO.class);

        if(freightTemplate.getId()==null)
        {
            logger.warn("ID为空 param:"+ requestJsonVO.getEntityJson());
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("ID不能为空!");
            return resultObjectVO;
        }


        if(freightTemplate.getUserMainId()==null)
        {
            logger.warn("用户ID为空 param:"+ requestJsonVO.getEntityJson());
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("用户ID不能为空!");
            return resultObjectVO;
        }


        String userMainId = String.valueOf(freightTemplate.getUserMainId());
        try {

            String newSign = FreightTemplateUtils.getDeleteSignHeader(userMainId);
            if(!signHeader.equals(newSign))
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("签名校验失败,请稍后重试");
                return resultObjectVO;
            }

            boolean lockStatus = skylarkLock.lock(FreightTemplateKey.getDeleteLockKey(userMainId), userMainId);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
                return resultObjectVO;
            }


            SellerShop sellerShopEntity = sellerShopService.findByUserMainId(freightTemplate.getUserMainId());
            if(sellerShopEntity!=null)
            {
                freightTemplate.setShopId(sellerShopEntity.getId());
            }

            if(freightTemplate.getShopId()==null)
            {
                //释放锁
                skylarkLock.unLock(FreightTemplateKey.getDeleteLockKey(userMainId), userMainId);

                logger.warn("店铺ID为空 param:"+ JSONObject.toJSONString(freightTemplate));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有查询到关联店铺!");
                return resultObjectVO;
            }

            int row = freightTemplateService.deleteByIdAndUserMainId(freightTemplate.getId(),freightTemplate.getUserMainId());
            if (row <=0) {
                //释放锁
                skylarkLock.unLock(FreightTemplateKey.getDeleteLockKey(userMainId), userMainId);

                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请重试!");
                return resultObjectVO;
            }

        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            logger.warn(e.getMessage(),e);
        }finally{
            //释放锁
            skylarkLock.unLock(FreightTemplateKey.getDeleteLockKey(userMainId), userMainId);
        }
        return resultObjectVO;
    }



}
