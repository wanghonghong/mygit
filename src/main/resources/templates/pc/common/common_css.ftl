<#if COMPRESS=='1'>

    <#if PLAT_FORM ?? && PLAT_FORM=='0'>
        <link rel="stylesheet" type="text/css" href="${THIRD_URL}/css/pc/common.min.css">
    <#else>
        <link rel="stylesheet" type="text/css" href="${THIRD_URL}/css/zb/common.min.css">
    </#if>


<#else>
    <!-- 聚米组件样式 -->
    <link rel="stylesheet" type="text/css" href="${THIRD_URL}/css/pc/jm-module.css">
    <!-- 框架样式 -->
    <link rel="stylesheet" type="text/css" href="${THIRD_URL}/css/pc/frame.css"/>
    <!-- 主模块内容样式 -->
    <link rel="stylesheet" type="text/css" href="${THIRD_URL}/css/pc/mian-model.css"/>
    <!-- 手机壳样式 -->
    <link rel="stylesheet" type="text/css" href="${THIRD_URL}/css/pc/phone-shell.css"/>
    <!-- 商品发布详情 和 商城首页搭建所要用到 wap版样式 -->
    <link rel="stylesheet" type="text/css" href="${THIRD_URL}/css/pc/wapcss/index.css"/>

    <!-- 弹窗的内容样式 -->
    <link rel="stylesheet" type="text/css" href="${THIRD_URL}/css/pc/dialog-box.css"/>
    <link href="${THIRD_URL}/third/jquery/pagination/pagination.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" type="text/css" href="${THIRD_URL}/third/util/select2/css/select2.css"/>
    <link rel="stylesheet" type="text/css" href="${THIRD_URL}/third/util/select2/css/select2.jumi.css"/>
    <link href="${THIRD_URL}/third/jquery/datetimeaddon/css/jquery-ui-1.8.16.custom.css" rel="stylesheet" type="text/css"/>
    <link href="${THIRD_URL}/third/jquery/ztree/v3.3/css/ztreestyle/ztreestyle.css" rel="stylesheet" type="text/css"/>
    <link href="${THIRD_URL}/third/util/artDialog/css/ui-dialog.css" rel="stylesheet" type="text/css"/>
    <link href="${THIRD_URL}/third/util/lightbox/css/lightbox.css" rel="stylesheet" type="text/css"/>

    <#if PLAT_FORM ?? && PLAT_FORM=='0'>

        <link rel="stylesheet" type="text/css" href="${THIRD_URL}/css/pc/login.css"/>
        <!-- 店铺管理样式 -->
        <link rel="stylesheet" type="text/css" href="${THIRD_URL}/css/pc/shop-manage.css"/>

        <link rel="stylesheet" type="text/css" href="${THIRD_URL}/css/pc/frame-person.css"/>
        <link rel="stylesheet" type="text/css" href="${THIRD_URL}/css/pc/mian-person.css"/>
        <link rel="stylesheet" type="text/css" href="${THIRD_URL}/css/pc/dialog-person.css"/>
        <!-- 聚米新版样式 -->
        <link rel="stylesheet" type="text/css" href="${THIRD_URL}/css/pc/index.css">
    <#else>

        <link rel="stylesheet" href="${THIRD_URL}/css/zb/red-login.css" />

        <!-- 聚米新版样式 -->
        <link rel="stylesheet" href="${THIRD_URL}/css/zb/index.css" >
        <link rel="stylesheet" href="${THIRD_URL}/css/zb/white-login.css" />
        <link rel="stylesheet" href="${THIRD_URL}/css/zb/iconfont.css">
    </#if>

</#if>

