package com.toucan.shopping.modules.column.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.toucan.shopping.modules.column.entity.ColumnArea;
import com.toucan.shopping.modules.column.entity.ColumnBanner;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 栏目轮播图VO
 *
 * @author majian
 */
@Data
public class ColumnBannerVO extends ColumnBanner {

    private Long[] idArray; //ID数组


    /**
     * 预览图
     */
    @JSONField(serialize = false)
    private MultipartFile img;

    private String httpImgPath; //外网访问地址

}
