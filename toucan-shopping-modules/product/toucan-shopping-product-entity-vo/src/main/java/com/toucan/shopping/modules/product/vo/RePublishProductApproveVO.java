package com.toucan.shopping.modules.product.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 发布商品审核VO
 */
@Data
public class RePublishProductApproveVO extends PublishProductApproveVO {


    /**
     * 已删除的预览图索引
     */
    private String previewPhotoDelPosArray;






}
