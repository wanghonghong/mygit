<style>
    .shop-manage .shop-panel-group .shop-item .self-support-shop .self-support-shop-ul .self-support-shop-img img{
        border-bottom: 1px solid #e0dfdf;
    }
    .enter-btn a{
        color:#FFFFFF;
    }
</style>
<!DOCTYPE html>
<html lang="en" class="fsvs demo">
<head>
    <meta charset="UTF-8">
    <title></title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <#--<meta http-equiv="Content-Security-Policy" content="upgrade-insecure-requests" />-->
    <meta name="renderer" content="webkit">
    <link rel="icon" href="${THIRD_URL}/css/pc/img/jmico.png" type="image/x-icon" />
    <#include "/pc/common/common_css.ftl" />
    <#include "/pc/common/common_js.ftl" />

</head>
<body class="fsvs">
<!-- 头部模块 -->
<header class="g-hd s-white">
    <header>
        <div class="g-mn ">
            <label>欢迎来到聚米为谷！</label>
            <div class="g-memu" >
                <img src="${THIRD_URL}/css/pc/img/logo1.png" />
                <div>
                    商家平台
                    <i class="iconfont icon-belowtriangle"></i>
                </div><br>
                <ul>
                    <a href="${basePath!''}/shop">
                        <li>店铺管理</li>
                    </a>
                    <a href="${basePath!''}/shop#3" target="_blank">
                        <li>个人资料</li>
                    </a>
                    <a href="http://jumiweigu.com/" target="_blank">
                        <li>官网</li>
                    </a>
                    <a href="${basePath!''}/loginout">
                        <li>退出</li>
                    </a>
                </ul>
            </div>
        </div>
    </header>
	<#include "/pc/shop/shop_index_headmenu.ftl" />
</header>