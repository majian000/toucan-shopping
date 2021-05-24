package com.toucan.shopping.modules.area.vo;

import com.toucan.shopping.modules.area.entity.Area;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 地区编码
 *
 * @author majian
 */
@Data
public class AreaVO extends Area {

    private List<AreaVO> children;


    /**
     * 创建人
     */
    private String createAdminUsername;


    /**
     * 修改人
     */
    private String updateAdminUsername;

}
