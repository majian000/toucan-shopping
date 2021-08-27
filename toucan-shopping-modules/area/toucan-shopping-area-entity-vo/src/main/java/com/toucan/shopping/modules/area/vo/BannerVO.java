package com.toucan.shopping.modules.area.vo;

import com.toucan.shopping.modules.area.entity.Banner;
import lombok.Data;

import java.util.Date;

/**
 * 轮播图
 *
 * @author majian
 */
@Data
public class BannerVO extends Banner {

    private Long[] idArray; //主键列表

    private String[] areaCodeArray; //地区编码列表



    private String createAdminName; //创建人姓名
    private String updateAdminName; //修改人姓名

}
