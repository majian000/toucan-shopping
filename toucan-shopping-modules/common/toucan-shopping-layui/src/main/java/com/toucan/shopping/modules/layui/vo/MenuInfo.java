package com.toucan.shopping.modules.layui.vo;


import lombok.Data;

import java.util.List;

@Data
public class MenuInfo {

    private Long id;
    private String title;
    private String icon;
    private String href;
    private String target;
    private List<MenuInfo> child;

}
