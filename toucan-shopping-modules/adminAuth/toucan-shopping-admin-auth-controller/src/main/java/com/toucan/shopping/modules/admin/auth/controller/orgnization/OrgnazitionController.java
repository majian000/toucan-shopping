package com.toucan.shopping.modules.admin.auth.controller.orgnization;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.admin.auth.entity.*;
import com.toucan.shopping.modules.admin.auth.page.OrgnazitionTreeInfo;
import com.toucan.shopping.modules.admin.auth.service.*;
import com.toucan.shopping.modules.admin.auth.vo.AdminAppVO;
import com.toucan.shopping.modules.admin.auth.vo.OrgnazitionTreeVO;
import com.toucan.shopping.modules.admin.auth.vo.OrgnazitionVO;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.util.GlobalUUID;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 组织机构管理
 */
@RestController
@RequestMapping("/orgnazition")
public class OrgnazitionController {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private OrgnazitionService orgnazitionService;

    @Autowired
    private AppService appService;


    @Autowired
    private AdminOrgnazitionService adminOrgnazitionService;

    @Autowired
    private OrgnazitionAppService orgnazitionAppService;

    @Autowired
    private AdminAppService adminAppService;


    /**
     * 添加组织机构
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/save",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO save(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("添加失败,没有找到实体对象");
            return resultObjectVO;
        }

        try {
            OrgnazitionVO entity = JSONObject.parseObject(requestVo.getEntityJson(),OrgnazitionVO.class);
            if(StringUtils.isEmpty(entity.getName()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("添加失败,请输入组织机构名称");
                return resultObjectVO;
            }

            if(entity.getPid()==null)
            {
                entity.setPid(-1L);
            }
            entity.setOrgnazitionId(GlobalUUID.uuid());
            entity.setCreateDate(new Date());
            entity.setDeleteStatus((short)0);
            int row = orgnazitionService.save(entity);
            if (row < 1) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("添加失败,请重试!");
                return resultObjectVO;
            }

            if(!CollectionUtils.isEmpty(entity.getAppCodes()))
            {
                for(String appCode:entity.getAppCodes())
                {
                    OrgnazitionApp orgnazitionApp = new OrgnazitionApp();
                    orgnazitionApp.setOrgnazitionId(entity.getOrgnazitionId());
                    orgnazitionApp.setAppCode(appCode);
                    orgnazitionApp.setCreateDate(new Date());
                    orgnazitionApp.setDeleteStatus((short)0);
                    orgnazitionApp.setCreateAdminId(entity.getCreateAdminId());

                    orgnazitionAppService.save(orgnazitionApp);
                }
            }

            resultObjectVO.setData(entity);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("添加失败,请稍后重试");
        }
        return resultObjectVO;
    }

    


    /**
     * 編輯组织机构
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/update",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO update(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            OrgnazitionVO entity = JSONObject.parseObject(requestVo.getEntityJson(),OrgnazitionVO.class);

            if(entity.getId().longValue()==entity.getPid().longValue())
            {
                logger.info("上级节点不能为自己 param:"+ JSONObject.toJSONString(entity));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("上级节点不能为自己!");
                return resultObjectVO;
            }
            if(StringUtils.isEmpty(entity.getName()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请传入组织机构名称");
                return resultObjectVO;
            }
            if(entity.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请传入组织机构ID");
                return resultObjectVO;
            }


            Orgnazition query=new Orgnazition();
            query.setId(entity.getId());
            query.setDeleteStatus((short)0);
            List<OrgnazitionVO> orgnazitions = orgnazitionService.findListByEntity(query);
            if(CollectionUtils.isEmpty(orgnazitions))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("该组织机构不存在!");
                return resultObjectVO;
            }

            entity.setUpdateDate(new Date());
            int row = orgnazitionService.update(entity);
            if (row < 1) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请重试!");
                return resultObjectVO;
            }

            //删除机构应用关联
            orgnazitionAppService.deleteByOrgnazitionId(orgnazitions.get(0).getOrgnazitionId());


            //重新生成关联
            if(!CollectionUtils.isEmpty(entity.getAppCodes()))
            {
                for(String appCode:entity.getAppCodes())
                {
                    OrgnazitionApp orgnazitionApp = new OrgnazitionApp();
                    orgnazitionApp.setOrgnazitionId(orgnazitions.get(0).getOrgnazitionId());
                    orgnazitionApp.setAppCode(appCode);
                    orgnazitionApp.setCreateDate(new Date());
                    orgnazitionApp.setDeleteStatus((short)0);
                    orgnazitionApp.setCreateAdminId(entity.getUpdateAdminId());

                    orgnazitionAppService.save(orgnazitionApp);
                }
            }


            resultObjectVO.setData(entity);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }



    /**
     * 查询树表格
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/tree/table",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryAppOrgnazitionTreeTable(@RequestBody RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null||requestJsonVO.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            OrgnazitionTreeInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), OrgnazitionTreeInfo.class);
            if(StringUtils.isEmpty(queryPageInfo.getAdminId()))
            {
                throw new IllegalArgumentException("adminId为空");
            }

            //查询所有结构树
            List<Orgnazition>  orgnazitions = orgnazitionService.findTreeTable(queryPageInfo);
            resultObjectVO.setData(orgnazitions);

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
            Orgnazition entity = JSONObject.parseObject(requestVo.getEntityJson(),Orgnazition.class);
            if(entity.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到组织机构ID");
                return resultObjectVO;
            }

            //查询是否存在该组织机构
            Orgnazition query=new Orgnazition();
            query.setId(entity.getId());
            List<OrgnazitionVO> orgnazitionVOS = orgnazitionService.findListByEntity(query);
            if(CollectionUtils.isEmpty(orgnazitionVOS))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("组织机构不存在!");
                return resultObjectVO;
            }
            for(OrgnazitionVO orgnazitionVO:orgnazitionVOS) {
                OrgnazitionApp queryOrgnazitionApp = new OrgnazitionApp();
                queryOrgnazitionApp.setOrgnazitionId(orgnazitionVO.getOrgnazitionId());
                List<OrgnazitionApp> orgnazitionApps = orgnazitionAppService.findListByEntity(queryOrgnazitionApp);
                orgnazitionVO.setOrgnazitionApps(orgnazitionApps);
            }
            resultObjectVO.setData(orgnazitionVOS);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }







    /**
     * 查询当前账号下组织机构树
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/query/admin/orgnazition/tree",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryAdminOrgnazitionTree(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            AdminAppVO query = JSONObject.parseObject(requestJsonVO.getEntityJson(), AdminAppVO.class);
            String[] appCodes=new String[1];
            appCodes[0] = query.getAppCode();
            resultObjectVO.setData(orgnazitionService.queryTreeByAppCodeArray(appCodes));

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }





    /**
     * 删除指定组织机构
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/id",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteById(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            Orgnazition entity = JSONObject.parseObject(requestVo.getEntityJson(),Orgnazition.class);
            if(entity.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到组织机构ID");
                return resultObjectVO;
            }

            List<Orgnazition> chidlren = new ArrayList<Orgnazition>();
            orgnazitionService.queryChildren(chidlren,entity);

            //把当前组织机构添加进去,循环这个集合
            chidlren.add(entity);

            for(Orgnazition f:chidlren) {
                //删除当前组织机构
                int row = orgnazitionService.deleteById(f.getId());
                if (row < 1) {
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg("请重试!");
                    continue;
                }

                //删除组织机构下所有关联
                AdminOrgnazition queryAdminOrgnazition = new AdminOrgnazition();
                queryAdminOrgnazition.setOrgnazitionId(f.getOrgnazitionId());

                List<AdminOrgnazition> adminOrgnazitions = adminOrgnazitionService.findListByEntity(queryAdminOrgnazition);
                if (!CollectionUtils.isEmpty(adminOrgnazitions)) {
                    row = adminOrgnazitionService.deleteByOrgnazitionId(f.getOrgnazitionId());

                }

            }

            resultObjectVO.setData(entity);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 批量删除组织机构
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/ids",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteByIds(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            List<Orgnazition> OrgnazitionList = JSONObject.parseArray(requestVo.getEntityJson(),Orgnazition.class);
            if(CollectionUtils.isEmpty(OrgnazitionList))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到组织机构ID");
                return resultObjectVO;
            }
            List<ResultObjectVO> resultObjectVOList = new ArrayList<ResultObjectVO>();
            for(Orgnazition Orgnazition:OrgnazitionList) {
                if(Orgnazition.getId()!=null) {
                    ResultObjectVO appResultObjectVO = new ResultObjectVO();
                    appResultObjectVO.setData(Orgnazition);


                    List<Orgnazition> chidlren = new ArrayList<Orgnazition>();
                    orgnazitionService.queryChildren(chidlren,Orgnazition);

                    //把当前组织机构添加进去,循环这个集合
                    chidlren.add(Orgnazition);

                    for(Orgnazition f:chidlren) {
                        //删除当前组织机构
                        int row = orgnazitionService.deleteById(f.getId());
                        if (row < 1) {
                            resultObjectVO.setCode(ResultVO.FAILD);
                            resultObjectVO.setMsg("请重试!");
                            continue;
                        }

                        //删除组织机构下所有关联
                        AdminOrgnazition queryAdminOrgnazition = new AdminOrgnazition();
                        queryAdminOrgnazition.setOrgnazitionId(f.getOrgnazitionId());

                        List<AdminOrgnazition> adminOrgnazitions = adminOrgnazitionService.findListByEntity(queryAdminOrgnazition);
                        if (!CollectionUtils.isEmpty(adminOrgnazitions)) {
                            adminOrgnazitionService.deleteByOrgnazitionId(f.getOrgnazitionId());
                        }

                    }

                }
            }
            resultObjectVO.setData(resultObjectVOList);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 查询组织机构树
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/query/orgnazation/tree",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryOrgnazationTree(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            App queryApp = JSONObject.parseObject(requestJsonVO.getEntityJson(),App.class);
            if(queryApp==null||StringUtils.isEmpty(queryApp.getCode()))
            {
                resultObjectVO.setData(orgnazitionService.queryTree());
            }else {
                resultObjectVO.setData(orgnazitionService.queryTree(queryApp.getCode()));
            }

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }





}
