package com.toucan.shopping.modules.seller.business.service;

import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.modules.common.annotation.RequestCheck;
import com.toucan.shopping.modules.common.exception.BusinessValidationException;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.util.Check;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.seller.entity.SellerDesignerPageModel;
import com.toucan.shopping.modules.seller.page.SellerDesignerPageModelPageInfo;
import com.toucan.shopping.modules.seller.redis.SellerDesignerPageKey;
import com.toucan.shopping.modules.seller.service.SellerDesignerPageModelService;
import com.toucan.shopping.modules.seller.vo.SellerDesignerPageModelVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Service
public class SellerDesignerPageModelBusinessService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SellerDesignerPageModelService sellerDesignerPageModelService;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private SkylarkLock skylarkLock;

    /**
     * 只保存1个
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO onlySaveOne(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        Long designerPageId = -1L;
        String userMainId = "-1";
        try {

            SellerDesignerPageModelVO sellerDesignerPageVO = requestJsonVO.formatEntity(SellerDesignerPageModelVO.class);
            userMainId = String.valueOf(sellerDesignerPageVO.getUserMainId());
            boolean lockStatus = skylarkLock.lock(SellerDesignerPageKey.getSaveUpdateLockKey(userMainId), userMainId);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("保存失败,请稍后重试");
                return resultObjectVO;
            }
            SellerDesignerPageModel query = new SellerDesignerPageModel();
            query.setShopId(sellerDesignerPageVO.getShopId());
            query.setType(sellerDesignerPageVO.getType());
            query.setPosition(sellerDesignerPageVO.getPosition());
            query.setEnableStatus(0);
            List<SellerDesignerPageModel> sellerDesignerPageModels = sellerDesignerPageModelService.queryList(query);
            //如果没有被禁用的话,就刷新数据
            if (CollectionUtils.isEmpty(sellerDesignerPageModels)) {
                query.setEnableStatus(1);
                SellerDesignerPageModel sellerDesignerPageModelRet = sellerDesignerPageModelService.queryLastOne(query);
                if (sellerDesignerPageModelRet == null) {
                    designerPageId = idGenerator.id();

                    SellerDesignerPageModel sellerDesignerPageModel = new SellerDesignerPageModel();
                    BeanUtils.copyProperties(sellerDesignerPageModel, sellerDesignerPageVO);
                    sellerDesignerPageModel.setId(designerPageId);
                    sellerDesignerPageModel.setAppCode(requestJsonVO.getAppCode());
                    sellerDesignerPageModel.setCreateDate(new Date());
                    sellerDesignerPageModel.setCreaterId(String.valueOf(sellerDesignerPageVO.getUserMainId()));
                    sellerDesignerPageModel.setDeleteStatus((short) 0);
                    sellerDesignerPageModel.setEnableStatus(1);
                    int row = sellerDesignerPageModelService.save(sellerDesignerPageModel);
                    if (row <= 0) {
                        resultObjectVO.setCode(ResultVO.FAILD);
                        resultObjectVO.setMsg("保存失败,请稍后重试!");
                        return resultObjectVO;
                    }
                } else { //覆盖该最新对象
                    sellerDesignerPageModelRet.setUpdaterId(String.valueOf(sellerDesignerPageVO.getUserMainId()));
                    sellerDesignerPageModelRet.setUpdateDate(new Date());
                    sellerDesignerPageModelRet.setPageJson(sellerDesignerPageVO.getPageJson());
                    sellerDesignerPageModelRet.setDesignerVersion(sellerDesignerPageVO.getDesignerVersion());
                    int row = sellerDesignerPageModelService.update(sellerDesignerPageModelRet);
                    if (row <= 0) {
                        resultObjectVO.setCode(ResultVO.FAILD);
                        resultObjectVO.setMsg("保存失败,请稍后重试!");
                        return resultObjectVO;
                    }

                }
            } else {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("已被禁用");
                return resultObjectVO;
            }

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("保存失败,请稍后重试!");
            logger.warn(e.getMessage(), e);
        } finally {
            skylarkLock.unLock(SellerDesignerPageKey.getSaveUpdateLockKey(userMainId), userMainId);
        }
        return resultObjectVO;
    }

    /**
     * 查询列表页
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryListPage(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            SellerDesignerPageModelPageInfo queryPageInfo = JSONObject.parseObject(requestVo.getEntityJson(), SellerDesignerPageModelPageInfo.class);

            //查询列表页
            resultObjectVO.setData(sellerDesignerPageModelService.queryListPage(queryPageInfo));

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    /**
     * 查询最后一个模型
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryLastOne(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        String userMainId = "-1";
        try {
            SellerDesignerPageModelVO sellerDesignerPageVO = requestJsonVO.formatEntity(SellerDesignerPageModelVO.class);
            Check.notNull(sellerDesignerPageVO.getShopId(), ResultVO.FAILD, "店铺ID不能为空!");
            SellerDesignerPageModel query = new SellerDesignerPageModel();
            BeanUtils.copyProperties(query, sellerDesignerPageVO);
            query.setEnableStatus(1);
            resultObjectVO.setData(sellerDesignerPageModelService.queryLastOne(query));
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败,请稍后重试!");
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }

    /**
     * 根据ID删除
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO deleteByIdForAdmin(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        SellerDesignerPageModel sellerDesignerPageModel = JSONObject.parseObject(requestJsonVO.getEntityJson(), SellerDesignerPageModel.class);

        Check.notNull(sellerDesignerPageModel.getId(), ResultVO.FAILD, "ID不能为空!");

        String id = String.valueOf(sellerDesignerPageModel.getId());
        try {

            boolean lockStatus = skylarkLock.lock(SellerDesignerPageKey.getDeleteLockKey(id), id);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
                return resultObjectVO;
            }

            int row = sellerDesignerPageModelService.deleteById(sellerDesignerPageModel.getId());
            if (row <= 0) {
                //释放锁
                skylarkLock.unLock(SellerDesignerPageKey.getDeleteLockKey(id), id);

                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("删除失败，请重试!");
                return resultObjectVO;
            }

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            logger.warn(e.getMessage(), e);
        } finally {
            //释放锁
            skylarkLock.unLock(SellerDesignerPageKey.getDeleteLockKey(id), id);
        }
        return resultObjectVO;
    }

}
