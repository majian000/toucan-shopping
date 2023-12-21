
var g_pageContainer = newPageContainer(); //页面容器

/**
 * 容器组件
 * @returns {{minHeight: null, x: null, width: null, name: null, y: null, minWidth: null, type: null, height: null}}
 */
function newContainerComponent(){
    var containerComponent=newComponent();
    containerComponent.mapComponents=new Array();
    containerComponent.components=new Array();
    return containerComponent;
}

/**
 * 页面容器
 * @returns {{minHeight: null, x: null, width: null, name: null, y: null, minWidth: null, type: null, height: null}}
 */
function newPageContainer(){
    var container=newContainerComponent();
    container.instanceId=toucan_uuid();
    return container;
}

/**
 * 构造抽象组件
 * @returns {{minHeight: null, x: null, width: null, name: null, y: null, minWidth: null, type: null, height: null}}
 */
function newComponent(){
    var abstractComponent={
        x:null, //坐标
        y:null,
        width:null, //宽高
        height:null,
        minHeight:null,
        minWidth:null,
        type:null, //组件类型
        instanceId:null, //实例ID
        name:null //组件名称
    };
    return abstractComponent;
}

/**
 * 基础组件
 * @returns {{minHeight: null, x: null, width: null, name: null, y: null, minWidth: null, type: null, height: null}}
 */
function newBaseComponent(){
    var baseComponent = newComponent();
    return baseComponent;
}

/**
 * 店铺组件
 * @returns {{minHeight: null, x: null, width: null, name: null, y: null, minWidth: null, type: null, height: null}}
 */
function newShopBannerComponent(){
    var shopBannerComponent=newBaseComponent();

    return shopBannerComponent;
}

/**
 * 图片组件
 * @returns {{minHeight: null, x: null, width: null, name: null, y: null, minWidth: null, type: null, height: null}}
 */
function newImageComponent(){
    var imageComponent=newBaseComponent();

    return imageComponent;
}