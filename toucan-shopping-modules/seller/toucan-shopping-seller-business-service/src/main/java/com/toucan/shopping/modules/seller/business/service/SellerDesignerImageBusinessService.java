package com.toucan.shopping.modules.seller.business.service;

import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.modules.common.annotation.RequestCheck;
import com.toucan.shopping.modules.common.exception.BusinessValidationException;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.util.Check;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.seller.entity.SellerDesignerImage;
import com.toucan.shopping.modules.seller.page.SellerDesignerImagePageInfo;
import com.toucan.shopping.modules.seller.redis.SellerDesignerImageKey;
import com.toucan.shopping.modules.seller.service.SellerDesignerImageService;
import com.toucan.shopping.modules.seller.vo.SellerDesignerImageVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SellerDesignerImageBusinessService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SellerDesignerImageService sellerDesignerImageService;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private SkylarkLock skylarkLock;

    @RequestCheck(requireEntity = true)
    public ResultObjectVO findById(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            SellerDesignerImageVO entity = JSONObject.parseObject(requestVo.getEntityJson(), SellerDesignerImageVO.class);
            Check.notNull(entity.getId(), ResultVO.FAILD, "没有找到ID");

            resultObjectVO.setData(sellerDesignerImageService.findById(entity.getId()));

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryListPage(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            SellerDesignerImagePageInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), SellerDesignerImagePageInfo.class);
            PageInfo<SellerDesignerImageVO> pageInfo = sellerDesignerImageService.queryListPage(queryPageInfo);
            resultObjectVO.setData(pageInfo);
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }

    @RequestCheck(requireEntity = true)
    public ResultObjectVO save(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        Long entityId = -1L;
        try {
            entityId = idGenerator.id();

            SellerDesignerImageVO sellerDesignerImageVO = requestJsonVO.formatEntity(SellerDesignerImageVO.class);
            SellerDesignerImage sellerDesignerImage = new SellerDesignerImage();
            BeanUtils.copyProperties(sellerDesignerImage, sellerDesignerImageVO);
            sellerDesignerImage.setId(entityId);
            sellerDesignerImage.setCreateDate(new Date());
            sellerDesignerImage.setDeleteStatus((short) 0);
            int row = sellerDesignerImageService.save(sellerDesignerImage);
            if (row <= 0) {
                return ResultObjectVO.fail(ResultVO.FAILD, "保存失败,请重试!");
            }

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("保存失败,请重试!");
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }

    @RequestCheck(requireEntity = true)
    public ResultObjectVO deleteById(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        SellerDesignerImage sellerDesignerImage = JSONObject.parseObject(requestJsonVO.getEntityJson(), SellerDesignerImage.class);

        Check.notNull(sellerDesignerImage.getId(), ResultVO.FAILD, "ID不能为空!");

        String shopId = String.valueOf(sellerDesignerImage.getShopId());
        try {

            boolean lockStatus = skylarkLock.lock(SellerDesignerImageKey.getDeleteLockKey(shopId), shopId);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
                return resultObjectVO;
            }

            if (sellerDesignerImage.getShopId() == null) {
                //释放锁
                skylarkLock.unLock(SellerDesignerImageKey.getDeleteLockKey(shopId), shopId);

                logger.warn("店铺ID为空 param:" + JSONObject.toJSONString(sellerDesignerImage));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有查询到关联店铺!");
                return resultObjectVO;
            }

            int row = sellerDesignerImageService.deleteByIdAndShopId(sellerDesignerImage.getId(), sellerDesignerImage.getShopId());
            if (row <= 0) {
                //释放锁
                skylarkLock.unLock(SellerDesignerImageKey.getDeleteLockKey(shopId), shopId);

                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请重试!");
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
            skylarkLock.unLock(SellerDesignerImageKey.getDeleteLockKey(shopId), shopId);
        }
        return resultObjectVO;
    }

    @RequestCheck(requireEntity = true)
    public ResultObjectVO update(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            SellerDesignerImageVO bannerVO = requestJsonVO.formatEntity(SellerDesignerImageVO.class);
            Check.notNull(bannerVO.getId(), ResultVO.FAILD, "ID不能为空");
            SellerDesignerImage sellerDesignerImage = new SellerDesignerImage();
            BeanUtils.copyProperties(sellerDesignerImage, bannerVO);
            int row = sellerDesignerImageService.update(sellerDesignerImage);
            if (row <= 0) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("修改失败,请稍后请重试!");
                return resultObjectVO;
            }

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("修改失败,请稍后请重试!");
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }

    @RequestCheck(requireEntity = true)
    public ResultObjectVO deleteByIdForAdmin(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        SellerDesignerImageVO sellerDesignerImageVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), SellerDesignerImageVO.class);

        Check.notNull(sellerDesignerImageVO.getId(), ResultVO.FAILD, "ID不能为空!");

        String shopId = String.valueOf(sellerDesignerImageVO.getShopId());
        try {

            boolean lockStatus = skylarkLock.lock(SellerDesignerImageKey.getDeleteLockKey(shopId), shopId);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
                return resultObjectVO;
            }

            if (sellerDesignerImageVO.getShopId() == null) {
                //释放锁
                skylarkLock.unLock(SellerDesignerImageKey.getDeleteLockKey(shopId), shopId);

                logger.warn("店铺ID为空 param:" + JSONObject.toJSONString(sellerDesignerImageVO));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有查询到关联店铺!");
                return resultObjectVO;
            }

            int row = sellerDesignerImageService.deleteById(sellerDesignerImageVO.getId());
            if (row <= 0) {
                //释放锁
                skylarkLock.unLock(SellerDesignerImageKey.getDeleteLockKey(shopId), shopId);

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
            skylarkLock.unLock(SellerDesignerImageKey.getDeleteLockKey(shopId), shopId);
        }
        return resultObjectVO;
    }

}
