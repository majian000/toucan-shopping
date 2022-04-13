package com.toucan.shopping.modules.product.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 重新发布商品VO
 */
@Data
public class RePublishProductVO extends PublishProductVO {


    /**
     * 已删除的预览图索引
     */
    private String previewPhotoDelPosArray;




}
