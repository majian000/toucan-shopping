package com.toucan.shopping.modules.layui.vo;

import lombok.Data;

import java.util.List;

@Data
public class IndexInfo {

    private HomeInfo homeInfo;

    private LogoInfo logoInfo;

    private List<MenuInfo> menuInfo;
}
