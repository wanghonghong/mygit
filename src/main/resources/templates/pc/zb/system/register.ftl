<!DOCTYPE html>
<html>

<head>
    <title>注册</title>
    <meta charset="utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta name="renderer" content="webkit">
</head>
<style>
    .error {
        font-size: 12px;
        color:orangered;
    }
</style>

<body>
<#include "/pc/common/common_css.ftl" />
<link rel="stylesheet" href="${THIRD_URL}/css/zb/bootstrap.min.css">
<#include "/pc/common/common_js.ftl" />
<div class="g-login-box-xyh" style="background: #fff;">
    <div class="g-nav-box-xyh">
        <div class="g-logo-box-xyh col-xs-2">
            <img src="${THIRD_URL}/css/zb/img/logo1.png" />
        </div>
        <span class="nav-title"><i class="icon iconfont">&#xe628;</i>注册</span>
        <span class="go-login">已有账号了，<a href="${basePath}/login">去登录</a></span>
    </div>

    <div class="g-logininput-box-xyh">
        <form id="form2">
        <div class="g-input-box-xyh">
            <ul class="list-group">
                <li class="progress-box">
                    <div class="bar">
                        <div class="active">
                            <span>1</span>
                            <p>基本资料</p>
                        </div>
                        <div>
                            <span>2</span>
                            <p>关注扫码</p>
                        </div>
                        <div>
                            <span>3</span>
                            <p>完成注册</p>
                        </div>
                    </div>
                    <p class=""></p>
                </li>
                <li>
                    <input type="text" class="form-control" id="phoneNumber" placeholder="手机号码" name="phoneNumber">
                    <button type="button" class="yzm-img" id="codeForRegister">发送验证码</button>
                    <div id="phone-error1"></div>
                </li>
                <li>
                    <input type="text" class="form-control" id="code" placeholder="验证码" name="code">
                    <div id="code-error1"></div>
                </li>
                <li>
                    <input type="password" class="form-control" id="password" placeholder="输入密码" name="password">
                </li>
                <li>
                    <input type="password" class="form-control"id="password2" placeholder="再次输入密码" name="password2">
                    <div id="password2-error1" class="error"  style="display: none">两次输入的密码不相同！</div>
                </li>
            </ul>
        </div>
    </form>
        <div class="g-btn-box-xyh">
            <button id="register_btn">注册</button>
        </div>
    </div>

</div>
</body>
<script>
    $(function() {
        zb.login.init();
    })
</script>

</html>