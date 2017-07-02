<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>登录</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta name="renderer" content="webkit">
    <link rel="icon" href="${THIRD_URL}/css/pc/img/jmico.png" type="image/x-icon" />
    <#include "/pc/common/common_css.ftl" />
    <#include "/pc/common/common_js.ftl" />

    <style type="text/css">
        .code
        {
            font-family:Arial;
            font-style:italic;
            color:green;
            border:1;
            top:0;
            padding:5px 3px;
            letter-spacing:3px;
            font-weight:bolder;
            right: 10px;
            cursor: pointer;
            position: absolute;
        }
        .unchanged
        {
            border:0;
        }
        .login-div .login-form .form-group .iconfont{
            top: 4px;
        }
        .form-group{
            padding: 5px 0;
        }
    </style>

</head>
<body >
<div class="container-pc">
    <div class="login-div">
        <div class="logo">
            <img src="${THIRD_URL}/img/pc/logo1.png" />
        </div>
        <div class="pageall pageall1">
            <div class="pagegroup" id="pagegroup">
                <div class="page">
                    <form class="login-form" action="" method="post" id="login-form">
                        <div class="form-group">
                            <input type="text" class="form-control" value="${phoneNumber!''}" id="phoneNumber" name="phoneNumber" placeholder="请输入手机号" />
                            <label class="iconfont icon-person  "></label>
                        </div>

                        <div class="form-group  ">
                            <input type="password" class="form-control" value="" id="password" name="password" placeholder="请输入密码" />
                            <label class="iconfont icon-password  " style="top:7px;"></label>
                        </div>

                        <div  class="form-group verify-code-group">
                            <input type="text"   id="codeN" class="form-control"  value="" placeholder="验证码" />
                            <label class="iconfont icon-verification "></label>
                            <div  align="absmiddle" class="code"  >
                                <img src="${basePath}/captcha-image" id="codeImage" style="height: 45px;margin-top: 3px;" onclick="system.login.chageCode();" >
                            </div>
                        </div>

                        <div  class="remember-pwd-group">
                            <a href="${basePath}/system/backPwd">忘记密码?</a>
                        </div>
                        <div  class="login-btn-group clearfix">
                            <input type="button" value="登录" class="btn orange-btn col-xs-12" onclick="system.login.toLogin()" />
                        </div>
                        <div class="jump-link" id="login-jump-link">
                            还没有账号？<a href="javascript:void(0)" onclick="system.login.toRegister()" >立即注册</a>
                        </div>
                    </form>
                </div>
                <div class="page">
                    <form class="login-form scale" action="" method="post" id="register-form">
                        <div class="form-group">
                            <input type="text" class="form-control" value="" placeholder="请输入手机号" id="regPhoneNumber" name="regPhoneNumber" />
                            <label class="iconfont icon-person  "></label>
                        </div>


                        <div  class="form-group verify-code-group verify-yzm">
                            <input type="text" class="form-control" value="" placeholder="验证码" id="regcode" name="regcode" />
                            <label class="iconfont icon-verification "></label>
                            <input type="button" value="获取手机验证码" class="btn  gray-btn" style="background:#f3ba58;margin-right: 10px" id="regcodebtn" onclick="system.login.sendCode();" />
                        </div>
                        <div class="form-group  ">
                            <input type="password" class="form-control" value="" placeholder="请输入密码" id="regPassword1" name="regPassword1" />
                            <label class="iconfont icon-password  "style="top: 6px;"></label>
                        </div>
                        <div class="form-group  ">
                            <input type="password" class="form-control" value="" placeholder="确认密码" id="regPassword2" name="regPassword2" />
                            <label class="iconfont icon-password  "style="top:6px;"></label>
                        </div>
                        <div  class="agreement-group">
                            <i class="iconfont icon-checkboxactive"></i>
                            <span>我已经认真阅读并同意聚米为谷的</span>《<a href="#">用户协议</a>》
                        </div>
                        <div  class="login-btn-group clearfix">
                            <input type="button" value="注册"  class="btn orange-btn col-xs-12" onclick="system.login.register()" />
                        </div>
                        <div class="jump-link" id="register-jump-link" style="display: none;">
                            已经有聚米账号？<a href="javascript:void(0)" onclick="system.login.toLoginDiv()">立即登录</a>
                        </div>
                    </form>

                </div>
            </div>
        </div>


    </div>
</div>

<div id="dialoginfo" class="dialoginfo dhk">
    <div class="select dialog-js login-tc">
        <div class="dialog-jsimg"></div>
        <div class="dialog-jstxt1 text-center"><span class="login-tis">恭喜你，注册成功！</span></div>
        <div class="dialog-jstxt1 text-center" ><span><span id="mes">5</span>秒后跳转商家登录页面</span></div>
    </div>
</div>
<script>
    $(function(){
        //注册回车事件
        $('#regPassword2').keydown(function(e){
            if(e.keyCode==13){
                system.login.register()
            }
        });
        //登录回车事件
        $("#codeN").keydown(function(e){
            if(e.keyCode==13){
                system.login.toLogin()
            }
        });
    });
</script>
</body>
</html>