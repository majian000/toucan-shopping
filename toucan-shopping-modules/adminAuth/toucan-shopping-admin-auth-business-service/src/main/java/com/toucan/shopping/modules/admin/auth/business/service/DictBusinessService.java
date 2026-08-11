package com.toucan.shopping.modules.admin.auth.business.service;


import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.toucan.shopping.modules.admin.auth.entity.Admin;
import com.toucan.shopping.modules.admin.auth.entity.App;
import com.toucan.shopping.modules.admin.auth.entity.Dict;
import com.toucan.shopping.modules.admin.auth.page.DictPageInfo;
import com.toucan.shopping.modules.admin.auth.service.AdminAppService;
import com.toucan.shopping.modules.admin.auth.service.AdminService;
import com.toucan.shopping.modules.admin.auth.service.AppService;
import com.toucan.shopping.modules.admin.auth.service.DictService;
import com.toucan.shopping.modules.admin.auth.vo.AppVO;
import com.toucan.shopping.modules.admin.auth.vo.DictTreeVO;
import com.toucan.shopping.modules.admin.auth.vo.DictVO;
import com.toucan.shopping.modules.common.annotation.RequestCheck;
import com.toucan.shopping.modules.common.exception.BusinessValidationException;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.util.Check;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultTypeObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 字典
 */
@Service
public class DictBusinessService {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DictService dictService;

    @Autowired
    private AppService appService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private AdminAppService adminAppService;

    @Autowired
    private IdGenerator idGenerator;


    /**
     * 添加字典
     *
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO save(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            DictVO dictVO = JSONObject.parseObject(requestVo.getEntityJson(), DictVO.class);
            Check.notEmpty(dictVO.getName(), ResultVO.FAILD, "请输入字典名称");
            Check.notEmpty(dictVO.getCode(), ResultVO.FAILD, "请输入字典编码");
            Check.notNull(dictVO.getCategoryId(), ResultVO.FAILD, "请选择字典分类");
            dictVO.setAppCodes(new LinkedList<>());
            dictVO.getAppCodes().add(dictVO.getAppCode());
            List<DictVO> dicts = dictService.queryListByCodeAndAppCodes(dictVO.getCode(), dictVO.getAppCodes(), dictVO.getPid());
            if (!CollectionUtils.isEmpty(dicts)) {
                DictVO dcv = dicts.get(0);
                AppVO appVO = appService.findByCodeIngoreDelete(dcv.getAppCode());
                String nodeName = "根节点";
                ;
                if (dictVO.getPid().longValue() != -1) {
                    DictVO parentNode = dictService.findById(dictVO.getPid());
                    nodeName = parentNode.getName();
                }
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("在" + appVO.getName() + ":" + appVO.getCode() + "中的" + nodeName + "下该编码已存在");
                return resultObjectVO;
            }

            dictVO.setId(idGenerator.id());
            dictVO.setCreateDate(new Date());
            dictVO.setDeleteStatus((short) 0);
            dictVO.setDictSort(dictService.queryMaxSort() + 1);
            dictVO.setDictVersion(1);
            dictVO.setIsActive((short) 1);
            dictVO.setBatchId(UUID.randomUUID().toString().replaceAll("-", ""));
            int row = dictService.save(dictVO);
            if (row < 1) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请重试!");
                return resultObjectVO;
            }

            resultObjectVO.setData(dictVO);

        } catch (BusinessValidationException e) {
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 編輯字典
     *
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO update(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            DictVO entity = JSONObject.parseObject(requestVo.getEntityJson(), DictVO.class);

            Check.notEmpty(entity.getName(), ResultVO.FAILD, "请传入字典名称");
            Check.notNull(entity.getId(), ResultVO.FAILD, "请传入字典ID");

            //上级字典不能选择自己
            if (entity.getPid() != null && entity.getPid().equals(entity.getId())) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("上级字典不能选择自己");
                return resultObjectVO;
            }

            Dict query = new Dict();
            query.setDeleteStatus((short) 0);
            query.setPid(entity.getPid());
            query.setCode(entity.getCode());
            query.setAppCode(entity.getAppCode());
            List<DictVO> dictList = dictService.findListByEntity(query);
            if (!CollectionUtils.isEmpty(dictList)) {
                if (!dictList.get(0).getId().equals(entity.getId())) {
                    DictVO dcv = dictList.get(0);
                    AppVO appVO = appService.findByCodeIngoreDelete(dcv.getAppCode());
                    String nodeName = "根节点";
                    ;
                    if (entity.getPid().longValue() != -1) {
                        DictVO parentNode = dictService.findById(entity.getPid());
                        nodeName = parentNode.getName();
                    }
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg("在" + appVO.getName() + ":" + appVO.getCode() + "中的" + nodeName + "下该编码已存在");
                    return resultObjectVO;
                }
            }

            boolean isSnapshot = false; //是否快照字典
            DictVO dict = dictService.findById(entity.getId());
            //快照该字典
            if (entity.getIsSnapshot().intValue() == 1) {
                //下面的数据变更,就将字典进行快照
                if (!dict.getCode().equals(entity.getCode())
                        || !dict.getName().equals(entity.getName())
                        || !dict.getExtendProperty().equals(entity.getExtendProperty())
                        || !dict.getPid().equals(entity.getPid())) {

                    isSnapshot = true;
                }
            }

            if (isSnapshot) {
                //将所有这个批次的字典活动状态为非活动
                dictService.updateIsActiveByBatchId((short) 0, dict.getBatchId());
                //逻辑删除这个批次的字典,让上一条数据形成快照
                dictService.deleteByBatchId(dict.getBatchId());
                entity.setDictVersion(dictService.queryMaxVersion(dict.getBatchId()) + 1);
                entity.setId(idGenerator.id());
                entity.setIsActive((short) 1);
                entity.setBatchId(dict.getBatchId());
                entity.setCreateDate(new Date());
                entity.setCreateAdminId(entity.getUpdateAdminId());
                entity.setDeleteStatus((short) 0);
                dictService.save(entity);
                //更新子节点的父节点ID为新的ID
                dictService.updateParentId(dict.getId(), entity.getId());
                //如果修改了字典分类，级联更新所有子节点的分类
                if (entity.getCategoryId() != null && !entity.getCategoryId().equals(dict.getCategoryId())) {
                    cascadeUpdateCategoryId(entity.getId(), entity.getCategoryId());
                }

            } else {
                entity.setUpdateDate(new Date());
                int row = dictService.update(entity);
                if (row < 1) {
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg("请重试!");
                    return resultObjectVO;
                }
                //如果修改了字典分类，级联更新所有子节点的分类
                if (entity.getCategoryId() != null && !entity.getCategoryId().equals(dict.getCategoryId())) {
                    cascadeUpdateCategoryId(entity.getId(), entity.getCategoryId());
                }
            }


            resultObjectVO.setData(entity);

        } catch (BusinessValidationException e) {
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 查询列表分页
     *
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO listPage(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            DictPageInfo pageInfo = JSONObject.parseObject(requestVo.getEntityJson(), DictPageInfo.class);
            resultObjectVO.setData(dictService.queryListPage(pageInfo));

        } catch (BusinessValidationException e) {
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 根据ID查询
     *
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO findById(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            Dict entity = JSONObject.parseObject(requestVo.getEntityJson(), Dict.class);
            Check.notNull(entity.getId(), ResultVO.FAILD, "没有找到字典ID");

            //查询是否存在该字典
            Dict query = new Dict();
            query.setId(entity.getId());
            List<DictVO> list = dictService.findListByEntity(query);
            if (CollectionUtils.isEmpty(list)) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("字典不存在!");
                return resultObjectVO;
            }
            DictVO dictVO = list.get(0);
            DictVO parentDictVO = dictService.findById(dictVO.getPid());
            if (parentDictVO != null) {
                dictVO.setParentName(parentDictVO.getName());
            } else {
                dictVO.setParentName("根节点");
            }
            resultObjectVO.setData(list);

        } catch (BusinessValidationException e) {
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 删除指定字典(仅限中台使用)
     *
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO deleteById(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            DictVO dictVO = JSONObject.parseObject(requestVo.getEntityJson(), DictVO.class);
            Check.notNull(dictVO.getId(), ResultVO.FAILD, "没有找到字典ID");


            List<DictVO> chidlren = new ArrayList<DictVO>();
            dictService.queryChildren(chidlren, dictVO);
            //把当前的添加进去
            chidlren.add(dictVO);

            List<Long> dictIdList = chidlren.stream().map(DictVO::getId).collect(Collectors.toList());
            dictService.deleteByIdList(dictIdList);


            resultObjectVO.setData(dictVO);

        } catch (BusinessValidationException e) {
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 批量删除字典(仅限中台使用)
     *
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO deleteByIds(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            List<DictVO> dictVOS = JSON.parseArray(requestVo.getEntityJson(), DictVO.class);
            Check.notEmpty(dictVOS, ResultVO.FAILD, "没有找到字典ID");
            List<ResultObjectVO> resultObjectVOList = new ArrayList<ResultObjectVO>();
            for (DictVO dictVO : dictVOS) {
                if (dictVO.getId() != null) {
                    ResultObjectVO appResultObjectVO = new ResultObjectVO();
                    appResultObjectVO.setData(dictVO);


                    List<DictVO> chidlren = new ArrayList<DictVO>();
                    dictService.queryChildren(chidlren, dictVO);
                    //把当前的添加进去
                    chidlren.add(dictVO);

                    List<Long> dictIdList = chidlren.stream().map(DictVO::getId).collect(Collectors.toList());
                    dictService.deleteByIdList(dictIdList);
                }
            }
            resultObjectVO.setData(resultObjectVOList);

        } catch (BusinessValidationException e) {
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 查询树表格
     *
     * @param requestJsonVO
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryTreeTableByPid(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            DictPageInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), DictPageInfo.class);

            List<DictVO> dictVoList = new ArrayList<DictVO>();
            boolean queryCriteria = false;
            //按指定条件查询
            if (StringUtils.isNotEmpty(queryPageInfo.getName())
                    || StringUtils.isNotEmpty(queryPageInfo.getCode())
                    || (queryPageInfo.getEnableStatus() != null && queryPageInfo.getEnableStatus().intValue() != -1)
                    || StringUtils.isNotEmpty(queryPageInfo.getAppCode())) {
                queryCriteria = true;
                DictVO queryDictVO = new DictVO();
                BeanUtils.copyProperties(queryDictVO, queryPageInfo);
                queryDictVO.setPid(null);
                List<DictVO> dictVOS = dictService.queryList(queryDictVO);
                for (int i = 0; i < dictVOS.size(); i++) {
                    dictVoList.add(dictVOS.get(i));
                }
            } else {
                //查询当前节点下的所有子节点
                DictVO queryDict = new DictVO();
                if (queryPageInfo.getPid() != null) {
                    queryDict.setPid(queryPageInfo.getPid());
                } else {
                    queryDict.setPid(-1L);
                }
                List<Integer> categoryIdList = new LinkedList<>();
                if (!CollectionUtils.isEmpty(queryPageInfo.getCategoryIdList())) {
                    for (Integer categoryId : queryPageInfo.getCategoryIdList()) {
                        if (categoryId != null && categoryId.longValue() != -1) {
                            categoryIdList.add(categoryId);
                        }
                    }
                }
                if (queryPageInfo.getCategoryId() != null) {
                    categoryIdList.add(queryPageInfo.getCategoryId());
                }
                //设置分类
                queryDict.setName(queryPageInfo.getName());
                queryDict.setCode(queryPageInfo.getCode());
                queryDict.setEnableStatus(queryPageInfo.getEnableStatus());
                queryDict.setCategoryIdList(categoryIdList);
                List<DictVO> dictVOS = dictService.queryList(queryDict);
                for (int i = 0; i < dictVOS.size(); i++) {
                    DictVO dictVO = dictVOS.get(i);

                    queryDict = new DictVO();
                    queryDict.setPid(dictVO.getId());
                    Long childCount = dictService.queryListCount(queryDict);
                    if (childCount > 0) {
                        dictVO.setHaveChild(true);
                    }
                    dictVoList.add(dictVO);
                }
            }


            //先查询出属性路径相关
            if (!CollectionUtils.isEmpty(dictVoList)) {
                List<Long> parentIdList = new LinkedList<>();
                boolean parentIdExists = false;

                for (DictVO dictVO : dictVoList) {
                    //设置上级节点ID
                    parentIdExists = false;
                    for (Long parentId : parentIdList) {
                        if (dictVO.getPid() != null && parentId != null
                                && parentId.longValue() == dictVO.getPid().longValue()) {
                            parentIdExists = true;
                            break;
                        }
                    }
                    if (!parentIdExists && dictVO.getPid() != null && dictVO.getPid().longValue() != -1) {
                        parentIdList.add(dictVO.getPid());
                    }
                }
                for (DictVO dictVO : dictVoList) {
                    if (dictVO.getPid() != null && dictVO.getPid().longValue() == -1) {
                        dictVO.setParentName("根节点");
                    }
                }
                if (!CollectionUtils.isEmpty(parentIdList)) {
                    DictVO queryParentDictVO = new DictVO();
                    queryParentDictVO.setIdList(parentIdList);
                    List<DictVO> parentList = dictService.queryList(queryParentDictVO);
                    if (!CollectionUtils.isEmpty(parentList)) {
                        for (DictVO dictVO : dictVoList) {
                            if (dictVO.getPid() != null
                                    && dictVO.getPid().longValue() != -1) {
                                for (DictVO parent : parentList) {
                                    if (dictVO.getPid() != null
                                            && dictVO.getPid().longValue() == parent.getId().longValue()) {
                                        dictVO.setParentName(parent.getName());
                                        break;
                                    }
                                }
                            } else {
                                dictVO.setParentName("根节点");
                            }
                        }
                    }
                }
            }


            //如果做了条件查询 就将查询的这些节点设置为顶级节点
            if (queryCriteria) {
                if (!org.apache.commons.collections.CollectionUtils.isEmpty(dictVoList)) {
                    for (DictVO dictVO : dictVoList) {
                        dictVO.setPid(-1L);
                    }
                }
            }

            resultObjectVO.setData(dictVoList);

        } catch (BusinessValidationException e) {
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 查询指定节点下子节点
     *
     * @param requestJsonVO
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryTreeChildByPid(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            DictVO dict = requestJsonVO.formatEntity(DictVO.class);
            List<DictTreeVO> areaVOS = new ArrayList<DictTreeVO>();
            if (dict.getPid() == null) {
                DictTreeVO areaVO = new DictTreeVO();
                areaVO.setId(-1L);
                areaVO.setName("根节点");
                areaVO.setParentId(-1L);
                areaVO.setCategoryId(dict.getCategoryId());
                Long childCount = dictService.queryOneChildCountByPid(-1L, dict.getAppCode(), dict.getCategoryId());
                if (childCount > 0) {
                    areaVO.setIsParent(true);
                }
                areaVOS.add(areaVO);
            } else {
                List<DictVO> dictVOS = dictService.queryList(dict);
                for (int i = 0; i < dictVOS.size(); i++) {
                    DictVO dvo = dictVOS.get(i);
                    DictTreeVO dictTreeVO = new DictTreeVO();
                    BeanUtils.copyProperties(dictTreeVO, dvo);
                    Long childCount = 0L;
                    if (StringUtils.isNotEmpty(dict.getAppCode())) {
                        childCount = dictService.queryOneChildCountByPid(dictTreeVO.getId(), dictTreeVO.getAppCode(), dict.getCategoryId());
                    } else {
                        childCount = dictService.queryOneChildCountByPid(dictTreeVO.getId(), null, dict.getCategoryId());
                    }
                    if (childCount > 0) {
                        dictTreeVO.setIsParent(true);
                    } else {
                        dictTreeVO.setIsParent(false);
                    }
                    areaVOS.add(dictTreeVO);
                }
            }

            resultObjectVO.setData(areaVOS);

        } catch (BusinessValidationException e) {
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 查询全部字典树（非懒加载，返回嵌套树结构）
     *
     * @param requestJsonVO
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryTreeAll(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            DictPageInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), DictPageInfo.class);

            DictVO queryDictVO = new DictVO();
            if (StringUtils.isNotEmpty(queryPageInfo.getName())) {
                queryDictVO.setName(queryPageInfo.getName());
            }
            if (StringUtils.isNotEmpty(queryPageInfo.getCode())) {
                queryDictVO.setCode(queryPageInfo.getCode());
            }
            if (queryPageInfo.getEnableStatus() != null && queryPageInfo.getEnableStatus().intValue() != -1) {
                queryDictVO.setEnableStatus(queryPageInfo.getEnableStatus());
            }
            if (StringUtils.isNotEmpty(queryPageInfo.getAppCode())) {
                queryDictVO.setAppCode(queryPageInfo.getAppCode());
            }
            List<Integer> categoryIdList = new LinkedList<>();
            if (queryPageInfo.getCategoryId() != null) {
                categoryIdList.add(queryPageInfo.getCategoryId());
            }
            queryDictVO.setCategoryIdList(categoryIdList);
            // 不设置pid → 返回所有层级的字典
            List<DictVO> dictVOS = dictService.queryList(queryDictVO);

            if (!CollectionUtils.isEmpty(dictVOS)) {
                // 收集管理员ID和应用编码
                Set<String> adminIdSet = new HashSet<>();
                Set<String> appCodeSet = new HashSet<>();
                for (DictVO dictVO : dictVOS) {
                    if (StringUtils.isNotEmpty(dictVO.getCreateAdminId())) {
                        adminIdSet.add(dictVO.getCreateAdminId());
                    }
                    if (StringUtils.isNotEmpty(dictVO.getUpdateAdminId())) {
                        adminIdSet.add(dictVO.getUpdateAdminId());
                    }
                    if (StringUtils.isNotEmpty(dictVO.getAppCode())) {
                        appCodeSet.add(dictVO.getAppCode());
                    }
                }

                // 设置管理员名称
                if (!CollectionUtils.isEmpty(adminIdSet)) {
                    Admin adminQuery = new Admin();
                    adminQuery.setAdminIds(adminIdSet.toArray(new String[0]));
                    List<Admin> admins = adminService.findListByEntity(adminQuery);
                    if (!CollectionUtils.isEmpty(admins)) {
                        for (DictVO dictVO : dictVOS) {
                            for (Admin admin : admins) {
                                if (dictVO.getCreateAdminId() != null && dictVO.getCreateAdminId().equals(admin.getAdminId())) {
                                    dictVO.setCreateAdminName(admin.getUsername());
                                }
                                if (dictVO.getUpdateAdminId() != null && dictVO.getUpdateAdminId().equals(admin.getAdminId())) {
                                    dictVO.setUpdateAdminName(admin.getUsername());
                                }
                            }
                        }
                    }
                }

                // 设置应用名称
                if (!CollectionUtils.isEmpty(appCodeSet)) {
                    List<App> apps = appService.queryListByCodesIngoreDelete(new ArrayList<>(appCodeSet));
                    if (!CollectionUtils.isEmpty(apps)) {
                        for (DictVO dictVO : dictVOS) {
                            for (App app : apps) {
                                if (dictVO.getAppCode() != null && dictVO.getAppCode().equals(app.getCode())) {
                                    dictVO.setAppName(app.getName());
                                    break;
                                }
                            }
                        }
                    }
                }

                // 服务端拼接嵌套树结构
                List<DictVO> treeRoots = buildTreeOnServer(dictVOS);
                resultObjectVO.setData(treeRoots);
            } else {
                resultObjectVO.setData(dictVOS);
            }

        } catch (BusinessValidationException e) {
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 将扁平字典列表拼接为嵌套树结构
     */
    private List<DictVO> buildTreeOnServer(List<DictVO> flatList) {
        if (CollectionUtils.isEmpty(flatList)) {
            return flatList;
        }
        Map<Long, DictVO> map = new LinkedHashMap<>();
        for (DictVO node : flatList) {
            node.setChildren(new ArrayList<>());
            map.put(node.getId(), node);
        }
        List<DictVO> roots = new ArrayList<>();
        for (DictVO node : flatList) {
            Long pid = node.getPid();
            if (pid == null || pid.longValue() == -1 || !map.containsKey(pid)) {
                if (pid == null || pid.longValue() == -1) {
                    node.setParentName("根节点");
                }
                roots.add(node);
            } else {
                DictVO parent = map.get(pid);
                parent.getChildren().add(node);
                node.setParentName(parent.getName());
            }
        }
        sortTreeByDictSort(roots);
        cleanEmptyChildren(roots);
        return roots;
    }

    /**
     * 清理叶子节点的空children
     */
    private void cleanEmptyChildren(List<DictVO> list) {
        if (CollectionUtils.isEmpty(list)) return;
        for (DictVO node : list) {
            if (CollectionUtils.isEmpty(node.getChildren())) {
                node.setChildren(null);
            } else {
                cleanEmptyChildren(node.getChildren());
            }
        }
    }

    /**
     * 递归按字典排序号排序
     */
    private void sortTreeByDictSort(List<DictVO> list) {
        if (CollectionUtils.isEmpty(list)) return;
        list.sort((a, b) -> {
            int sortA = a.getDictSort() != null ? a.getDictSort() : 0;
            int sortB = b.getDictSort() != null ? b.getDictSort() : 0;
            return Integer.compare(sortA, sortB);
        });
        for (DictVO node : list) {
            if (!CollectionUtils.isEmpty(node.getChildren())) {
                sortTreeByDictSort(node.getChildren());
            }
        }
    }


    /**
     * 查询分类下的字典
     *
     * @param requestJsonVO
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryDictByCodeAndCategoryCode(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            DictVO query = requestJsonVO.formatEntity(DictVO.class);
            Check.notEmpty(query.getAppCode(), ResultVO.FAILD, "没有找到应用编码");
            DictVO dictVO = dictService.findByCodeAndCategoryCode(query.getCode(), query.getCategoryCode(), query.getAppCode());
            if (dictVO != null) {
                dictVO.setChildren(new LinkedList<>());
                dictService.setChildrenByVO(dictVO);
            }
            resultObjectVO.setData(dictVO);
        } catch (BusinessValidationException e) {
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 级联更新子节点的字典分类
     * @param parentId 父节点ID
     * @param categoryId 新的分类ID
     */
    private void cascadeUpdateCategoryId(Long parentId, Integer categoryId) {
        List<DictVO> children = new ArrayList<>();
        Dict childQuery = new Dict();
        childQuery.setId(parentId);
        dictService.queryChildren(children, childQuery);
        if (!CollectionUtils.isEmpty(children)) {
            List<Long> childIds = children.stream().map(DictVO::getId).collect(Collectors.toList());
            dictService.updateCategoryByIdList(childIds, categoryId);
        }
    }


    /**
     * 查询分类下的字典
     *
     * @param requestJsonVO
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultTypeObjectVO<List<DictVO>> queryDictByCodesAndCategoryCode(RequestJsonVO requestJsonVO) {
        ResultTypeObjectVO resultObjectVO = new ResultTypeObjectVO();

        try {
            DictVO query = requestJsonVO.formatEntity(DictVO.class);
            Check.notEmpty(query.getAppCode(), ResultVO.FAILD, "没有找到应用编码");
            List<DictVO> dictList = dictService.findByCodesAndCategoryCode(query.getCodes(), query.getCategoryCode(), query.getAppCode());
            if (!CollectionUtils.isEmpty(dictList)) {
                for (DictVO dictVO : dictList) {
                    dictVO.setChildren(new LinkedList<>());
                    dictService.setChildrenByVO(dictVO);
                }
            }
            resultObjectVO.setData(dictList);
        } catch (BusinessValidationException e) {
            return ResultTypeObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

}
