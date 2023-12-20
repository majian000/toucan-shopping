var g_componentConfig=new Map(); //组件配置
var g_components=new Array(); //组件实例

/**
 * 初始化组件配置
 */
function initComponentConfig(){
    initShopBannerConfig();
    initImageConfig();
}


/**
 * 初始化轮播图
 */
function initShopBannerConfig(){
    var componentConfig={
        type:"shopBanner",
        name:"店铺轮播图",
        propertyPanel:"mtc2",
        instanceType:"single"
    };
    g_componentConfig.set(componentConfig.type,componentConfig);
}

/**
 * 初始化图片
 */
function initImageConfig(){

    var componentConfig={
        type:"image",
        name:"图片",
        propertyPanel:"mtc2",
        instanceType:"multi"
    };
    g_componentConfig.set(componentConfig.type,componentConfig);
}

/**
 * 填充全局组件集合
 */
function loadGlobalComponents(components){
    if(components!=null&&components.length>0)
    {
        for(var i=0;i<components.length;i++)
        {
            var component = components[i];
            var newComponent = createComponentInstance(component.type);
            objectCopy(newComponent,component);
            g_components.push(newComponent);
        }
    }
}

/**
 * 创建组件实例
 * @param type
 * @returns {*}
 */
function createComponentInstance(type){
    var componentInstance= null;
    if(type=="image")
    {
        componentInstance = createImageInstance();
    }if(type=="shopBanner"){
        componentInstance = createShopBannerInstance();
    }else{

    }
    componentInstance.instanceId = toucan_uuid();
    g_components.push(componentInstance);
    return componentInstance;
}

/**
 * 创建图片实例
 */
function createImageInstance(){
    var componentInstance={
        type:"image",
        propertys:new Array(),
        setComponentPorperty:function(propertyName,propertyValue){
            setComponentPorperty(this,propertyName,propertyValue)
        }
    };
    return componentInstance;
}

/**
 * 创建轮播图实例
 */
function createShopBannerInstance(){
    var componentInstance={
        type:"shopBanner",
        propertys:new Array(),
        setComponentPorperty:function(propertyName,propertyValue){
            setComponentPorperty(this,propertyName,propertyValue)
        }
    };
    return componentInstance;
}


/**
 * 设置组件属性
 * @param componentInstance
 * @param propertyName
 * @param propertyValue
 */
function setComponentPorperty(componentInstance,propertyName,propertyValue){
    if(componentInstance!=null) {
        var existsProperty = false;
        if (componentInstance.propertys != null && componentInstance.propertys.length > 0) {
            for (var i = 0; i < componentInstance.propertys.length; i++) {
                if (componentInstance.propertys[i].name == propertyName) {
                    componentInstance.propertys[i].value = propertyValue;
                    existsProperty = true;
                }
            }
        }
        if (!existsProperty) {
            componentInstance.propertys.push({"name": propertyName, "value": propertyValue});
        }
    }
}

/**
 * 删除组件实例
 * @param instanceId
 */
function removeComponentInstanceByInstanceId(instanceId){
    if(g_components!=null&&g_components.length>0)
    {
        for(var i=0;i<g_components.length;i++)
        {
            if(g_components[i].instanceId==instanceId){
                g_components.splice(i, 1);
            }
        }
    }
}

/**
 * 根据实例ID查询
 * @param instanceId
 * @returns {*}
 */
function getComponentInstanceByInstanceId(instanceId){
    if(g_components!=null&&g_components.length>0)
    {
        for(var i=0;i<g_components.length;i++)
        {
            if(g_components[i].instanceId==instanceId){
                return g_components[i];
            }
        }
    }
    return null;
}

initComponentConfig();