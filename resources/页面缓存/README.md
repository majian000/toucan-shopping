#### 使用方式
    toucan_shopping_htmls目录下的是首页以及各个组件的页面缓存文件，可以登录商城管理端(http://8.140.187.184:8088/)
    1.商城C端
    首页管理》静态化管理》 生成对应的缓存文件
    2.卖家中心
    卖家管理》首页管理》免费开店静态生成 生成对应的缓存文件
    
    也可以直接使用该目录的文件(注意:不是最新版本)
    
    1.商城C端修改方式
    修改toucan-shopping-web-dev.yml与toucan-shopping-web-prod.yml文件下的
    toucan:
      shopping-pc: #PC端配置
        freemarker:
          release-mapping-url: /htmls/release/**
          preview-mapping-url: /htmls/preview/**
          release-location: C:/toucan_shopping_htmls/web/htmls/release/ 改成你的目录
          preview-location: C:/toucan_shopping_htmls/web/htmls/preview/ 改成你的目录
          
    2.卖家中心修改方式
    修改toucan-shopping-seller-web-dev.yml与toucan-shopping-seller-web-prod.yml文件下的
    toucan:
      shopping-seller-web-pc: #卖家PC端配置
        freemarker:
          release-mapping-url: /htmls/release/**
          preview-mapping-url: /htmls/preview/**
          release-location: C:/toucan_shopping_htmls/seller_web/htmls/release/  改成你的目录
          preview-location: C:/toucan_shopping_htmls/seller_web/htmls/preview/  改成你的目录
          ftl-location: templates/