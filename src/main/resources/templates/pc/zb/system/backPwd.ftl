<!DOCTYPE html>
<html>

<head>
    <title>找回密码</title>
    <meta charset="utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta name="renderer" content="webkit">
</head>
<style>
    .error {
        font-size: 12px;
        color:orangered;
    }
    .g-login-box-xyh .g-logininput-box-xyh span.yzm-img {
        display: inline-block;
        padding: 0 5px;
        border-radius: 2px;
        background-color: #bcc5cc;
        color: #fff;
        height: 32px;
        line-height: 32px;
        top: 5px;
        right: 5px;
        position: absolute;
        cursor: pointer;
    }
</style>
<body>
<#include "/pc/common/common_css.ftl" />
<#include "/pc/common/common_js.ftl" />
<div class="g-login-box-xyh">
    <div class="g-nav-box-xyh">
        <div class="g-logo-box-xyh col-xs-2">
            <img src="${THIRD_URL}/css/zb/img/logo1.png" />
        </div>
        <span class="nav-title"><i class="icon iconfont">&#xe62f;</i>找回密码</span>
        <span class="go-login">想起密码了，<a href="${basePath}/login">去登录</a></span>
    </div>
    <div class="g-logininput-box-xyh">
        <form id="form3">
        <div class="g-input-box-xyh">
            <ul class="list-group">
                <li>
                    <input type="text" class="form-control" id="phoneNumber" name="phoneNumber" placeholder="手机号码">
                    <span class="yzm-img" id="codeFor">发送验证码</span>
                    <div id="phone-error1"></div>
                </li>
                <li>
                    <input type="text" class="form-control" id="code" name="code" placeholder="验证码">
                    <div id="code-error1"></div>
                </li>
                <li>
                    <input type="password" class="form-control" id="password" name="password" placeholder="输入密码">
                </li>
                <li>
                    <input type="password" class="form-control" id="password2" name="password2"  placeholder="再次输入密码">
                    <div id="password2-error1" class="error"  style="display: none">两次输入的密码不相同！</div>
                </li>
            </ul>
        </div>
        </form>
        <div class="g-btn-box-xyh">
            <button id="save-btn">提交</button>
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