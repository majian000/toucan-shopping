package com.toucan.shopping.modules.content.vo;

import com.toucan.shopping.modules.area.vo.AreaVO;
import com.toucan.shopping.modules.content.entity.Banner;
import com.toucan.shopping.modules.content.entity.BannerArea;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 轮播图
 *
 * @author majian
 */
@Data
public class BannerVO extends Banner {

    private Long[] idArray; //主键列表

    private String[] areaCodeArray; //地区编码列表


    private List<BannerArea> bannerAreas; //轮播图地区关联
    /**
     * 关联所有市名称
     */
    private String areaNames;

    /**
     * 关联所有地区编码
     */
    private String areaCodes;


    private String createAdminName; //创建人姓名
    private String updateAdminName; //修改人姓名

}
