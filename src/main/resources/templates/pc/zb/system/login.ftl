
<!DOCTYPE html>
<html>
	<head>
		<title>聚米为谷系统登录</title>
		<meta charset="utf-8" />
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
        <meta name="renderer" content="webkit">
		<style>
            .yzm {
                display: inline-block;
                width: 80px;
                height: 40px;
                position: absolute;
                right: 0;
                top: 0;
				color: green;
				line-height: 40px;
				text-align: center;
				cursor: pointer;
                font-weight:bold;
            }
             .error {
                 font-size: 12px;
                 color:orangered;
             }
			.list-group .form-control{
                float: left;
			}
			.list-group .iconfont{
				position: absolute;
                top:7px;
				left: 10px;
				color: #BFBFBF;
			}
            .list-group .form-control{
				padding-left: 35px;
			}
		</style>
	</head>

	<body>

	<#include "/pc/common/common_css.ftl" />
    <link rel="stylesheet" href="${THIRD_URL}/css/zb/bootstrap.min.css">
	<#include "/pc/common/common_js.ftl" />

		<div class="g-login-box-xyh">
			<div class="g-login-logo-box-xyh">
				<img src="${THIRD_URL}/css/zb/img/login-logo.png" />
			</div>

			<div class="g-logininput-box-xyh">
			<form id="form1">
			<div class="g-input-box-xyh">
				<ul class="list-group">
					<li>
						<input type="text" class="form-control" id="phoneNumber" name="phoneNumber" placeholder="请输入手机号"  >
                        <label class="iconfont icon-person  "></label>
					</li>
					<li>
						<input type="password" class="form-control" id="password" name="password" placeholder="请输入密码"   >
                        <label class="iconfont icon-password  " style="top:7px;"></label>
					</li>
					<li style="position: relative;">
						<input type="text" class="form-control" id="code" name="code" placeholder="验证码" >
						<div id="code-error1"></div>
                        <label class="iconfont icon-verification "></label>
						<div  align="absmiddle" class="yzm"  id="checkCode"></div>
					</li>
					<li class="g-checkbox-xyh">
						<label>
						<div class="checkbox-box">
							<input type="checkbox" value="1" id="remember-num" name="" />
							<label for="remember-num"></label>
						</div>
						下次自动登录
						</label>
						<a href="${basePath}/zb/backPwd">忘记密码？</a>
					</li>
				</ul>
			</div>
			</form>
			<div class="g-btn-box-xyh">
				<button  id="login">登录</button>
				<a href="${basePath}/zb/register" class="register-link">还没有账号，去注册</a>
			</div>
		</div>
		</div>
	</body>

    <script>
        $(function() {
            zb.login.init();
        });
    </script>
</html>