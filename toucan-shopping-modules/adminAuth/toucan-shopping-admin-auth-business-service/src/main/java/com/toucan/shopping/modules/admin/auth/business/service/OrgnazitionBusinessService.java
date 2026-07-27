package com.toucan.shopping.modules.admin.auth.business.service;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.admin.auth.entity.*;
import com.toucan.shopping.modules.admin.auth.page.OrgnazitionTreeInfo;
import com.toucan.shopping.modules.admin.auth.service.*;
import com.toucan.shopping.modules.admin.auth.vo.AdminAppVO;
import com.toucan.shopping.modules.admin.auth.vo.OrgnazitionTreeVO;
import com.toucan.shopping.modules.admin.auth.vo.OrgnazitionVO;
import com.toucan.shopping.modules.common.annotation.RequestCheck;
import com.toucan.shopping.modules.common.exception.BusinessValidationException;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.util.Check;
import com.toucan.shopping.modules.common.util.CodeUtils;
import com.toucan.shopping.modules.common.util.GlobalUUID;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 组织机构管理
 */
@Service
public class OrgnazitionBusinessService {


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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO save(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            OrgnazitionVO entity = JSONObject.parseObject(requestVo.getEntityJson(),OrgnazitionVO.class);
            Check.notEmpty(entity.getName(), ResultVO.FAILD, "添加失败,请输入组织机构名称");

            if(entity.getPid()==null)
            {
                entity.setPid(-1L);
            }
            entity.setOrgnazitionId(GlobalUUID.uuid());
            entity.setCreateDate(new Date());
            entity.setDeleteStatus((short)0);
            Integer maxCodeVal = orgnazitionService.queryMaxCode();
            if(maxCodeVal==null){
                maxCodeVal=0;
            }
            entity.setCode("ORG"+CodeUtils.genMinCode(maxCodeVal+1,3));
            int row = orgnazitionService.save(entity);
            if (row < 1) {
                return ResultObjectVO.fail(ResultVO.FAILD, "添加失败,请重试!");
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

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO update(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            OrgnazitionVO entity = JSONObject.parseObject(requestVo.getEntityJson(),OrgnazitionVO.class);

            if(entity.getId().longValue()==entity.getPid().longValue())
            {
                logger.info("上级节点不能为自己 param:"+ JSONObject.toJSONString(entity));
                return ResultObjectVO.fail(ResultVO.FAILD, "上级节点不能为自己!");
            }
            Check.notEmpty(entity.getName(), ResultVO.FAILD, "请传入组织机构名称");
            Check.notNull(entity.getId(), ResultVO.FAILD, "请传入组织机构ID");


            Orgnazition query=new Orgnazition();
            query.setId(entity.getId());
            query.setDeleteStatus((short)0);
            List<OrgnazitionVO> orgnazitions = orgnazitionService.findListByEntity(query);
            if(CollectionUtils.isEmpty(orgnazitions))
            {
                return ResultObjectVO.fail(ResultVO.FAILD, "该组织机构不存在!");
            }

            entity.setUpdateDate(new Date());
            int row = orgnazitionService.update(entity);
            if (row < 1) {
                return ResultObjectVO.fail(ResultVO.FAILD, "请重试!");
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

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryAppOrgnazitionTreeTable(RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            OrgnazitionTreeInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), OrgnazitionTreeInfo.class);
            if(StringUtils.isEmpty(queryPageInfo.getAdminId()))
            {
                throw new IllegalArgumentException("adminId为空");
            }

            //查询所有结构树
            List<Orgnazition>  orgnazitions = orgnazitionService.findTreeTable(queryPageInfo);
            resultObjectVO.setData(orgnazitions);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO findById(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            Orgnazition entity = JSONObject.parseObject(requestVo.getEntityJson(),Orgnazition.class);
            Check.notNull(entity.getId(), ResultVO.FAILD, "没有找到组织机构ID");

            //查询是否存在该组织机构
            Orgnazition query=new Orgnazition();
            query.setId(entity.getId());
            List<OrgnazitionVO> orgnazitionVOS = orgnazitionService.findListByEntity(query);
            if(CollectionUtils.isEmpty(orgnazitionVOS))
            {
                return ResultObjectVO.fail(ResultVO.FAILD, "组织机构不存在!");
            }
            for(OrgnazitionVO orgnazitionVO:orgnazitionVOS) {
                OrgnazitionApp queryOrgnazitionApp = new OrgnazitionApp();
                queryOrgnazitionApp.setOrgnazitionId(orgnazitionVO.getOrgnazitionId());
                List<OrgnazitionApp> orgnazitionApps = orgnazitionAppService.findListByEntity(queryOrgnazitionApp);
                orgnazitionVO.setOrgnazitionApps(orgnazitionApps);
            }
            resultObjectVO.setData(orgnazitionVOS);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryAdminOrgnazitionTree(RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            AdminAppVO query = JSONObject.parseObject(requestJsonVO.getEntityJson(), AdminAppVO.class);
            String[] appCodes=new String[1];
            appCodes[0] = query.getAppCode();
            resultObjectVO.setData(orgnazitionService.queryTreeByAppCodeArray(appCodes));

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }





    /**
     * 删除指定组织机构(仅限中台使用)
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO deleteById(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            Orgnazition entity = JSONObject.parseObject(requestVo.getEntityJson(),Orgnazition.class);
            Check.notNull(entity.getId(), ResultVO.FAILD, "没有找到组织机构ID");

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

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 批量删除组织机构(仅限中台使用)
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO deleteByIds(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            List<Orgnazition> OrgnazitionList = JSONObject.parseArray(requestVo.getEntityJson(),Orgnazition.class);
            if(CollectionUtils.isEmpty(OrgnazitionList))
            {
                return ResultObjectVO.fail(ResultVO.FAILD, "没有找到组织机构ID");
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

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryOrgnazationTree(RequestJsonVO requestJsonVO)
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

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }





}
