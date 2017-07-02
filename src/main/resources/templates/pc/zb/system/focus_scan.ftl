<!DOCTYPE html>
<html>

<head>
    <title>注册</title>
    <meta charset="utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
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
        <div class="g-input-box-xyh">
            <ul class="list-group">
                <li class="progress-box">
                    <div class="bar">
                        <div class="active">
                            <span>1</span>
                            <p>基本资料</p>
                        </div>
                        <div class="active">
                            <span>2</span>
                            <p>关注扫码</p>
                        </div>
                        <div>
                            <span>3</span>
                            <p>完成注册</p>
                        </div>
                    </div>
                    <p class="half"></p>
                </li>
            </ul>
            <div class="img-box">
                <a href="${basePath}/zb/register/finish_register"><img src="${THIRD_URL}/css/zb/img/qrcode_for_gh.jpg" class="ewm" /></a>
                <p class="ewm-text">扫一扫关注聚米为谷公众号</p>
            </div>
        </div>
    </div>
</div>
</body>
<script>
    zb.login.init();
</script>

</html>