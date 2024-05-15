package com.toucan.shopping.modules.content.vo;

import com.toucan.shopping.modules.content.entity.Article;
import com.toucan.shopping.modules.content.entity.Banner;
import com.toucan.shopping.modules.content.entity.BannerArea;
import lombok.Data;

import java.util.List;

/**
 * 文章
 *
 * @author majian
 */
@Data
public class ArticleVO extends Article {



    private String createAdminName; //创建人姓名
    private String updateAdminName; //修改人姓名

}
