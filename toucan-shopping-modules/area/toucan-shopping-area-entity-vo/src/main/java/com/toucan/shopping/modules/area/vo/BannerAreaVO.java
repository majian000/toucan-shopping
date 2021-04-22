package com.toucan.shopping.modules.area.vo;

import com.toucan.shopping.modules.area.entity.BannerArea;
import lombok.Data;

import java.util.Date;

/**
 * 轮播图地区关联
 *
 * @author majian
 */
@Data
public class BannerAreaVO extends BannerArea {

    private String[] areaCodeArray; //地区编码列表

}
