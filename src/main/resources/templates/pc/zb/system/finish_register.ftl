<!DOCTYPE html>
<html>

<head>
    <title>注册</title>
    <meta charset="utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta name="renderer" content="webkit">
</head>

<body>
<#include "/pc/common/common_css.ftl" />
<#include "/pc/common/common_js.ftl" />
<div class="g-login-box-xyh">
    <div class="g-nav-box-xyh">
        <div class="g-logo-box-xyh col-xs-2">
            <img src="${THIRD_URL}/css/zb/img/logo1.png" />
        </div>
        <span class="nav-title"><i class="icon iconfont">&#xe628;</i>注册</span>
        <span class="go-login">已有账号了，<a href="${basePath}/login">去登录</a></span>
    </div>
    <div class="g-logininput-box-xyh">
        <div class="img-box">
            <img src="${THIRD_URL}/css/zb/img/suc.jpg" class="suc-img" />
            <div class="suc-text-box">
                <p class="suc-title">注册成功，热烈欢迎您成为聚米一员！</p>
                <p class="suc-text"><span id="count">3</span>秒后跳转至系统···</p>
            </div>

        </div>
    </div>
</div>
</body>
<script>
    $(function() {
        zb.login.countTime();
    });
</script>

</html>