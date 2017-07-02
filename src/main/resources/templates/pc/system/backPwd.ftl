<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>找回密码</title>
		<link rel="stylesheet" type="text/css" href="${THIRD_URL}/css/pc/login.css"/>
		<link rel="stylesheet" type="text/css" href="${THIRD_URL}/css/pc/iconfont.css"/>
        <!-- 弹窗的内容样式 -->
        <link rel="stylesheet" type="text/css" href="${THIRD_URL}/css/pc/dialog-box.css"/>
        <link href="${THIRD_URL}/third/util/artDialog/css/ui-dialog.css" rel="stylesheet" type="text/css" />
        <script type="text/javascript" charset="utf-8">
            var CONTEXT_PATH = '${basePath}';
            var STATIC_URL = '${STATIC_URL}';
            var TPL_CACHE = '${TPL_CACHE}';
            var DOMAIN = '${DOMAIN}';
            var THIRD_URL = '${THIRD_URL}';
            var COMPRESS = '${COMPRESS}';
        </script>

	</head>
	<body class="bg-color">
		<div class="container-pc">
			<div class="login-div">
				<div class="logo">
					<img src="${THIRD_URL}/img/pc/logo1.png" />
				</div>
				
				<div class="pageall">													
				<div class="pagegroup" id="pagegroup">

					<div class="page">
					<form class="login-form" action="" method="post" id="register-form">
					<h1>找回密码</h1>
					<div class="form-group">
						<input type="text" class="form-control" value="" placeholder="请输入手机号" id="phoneNumber" name="phoneNumber" />
						<label class="iconfont icon-phone" style="top: -1px;"></label>
					</div>
					<div  class="form-group verify-code-group verify-yzm">
						<input type="text" class="form-control" value="" placeholder="验证码" id="code" name="code" />
						<label class="iconfont icon-verification "></label>
						  <input type="button" value="获取手机验证码" style="background:#f3ba58" class="btn  gray-btn " id="backcodebtn" />
					</div>
					<div class="form-group  ">
						<input type="password" class="form-control" value="" placeholder="请输入密码" id="password" name="password" />
						<label class="iconfont icon-password  " style="top: 2px;"></label>
					</div>
					<div class="form-group  ">
						<input type="password" class="form-control" value="" placeholder="确认密码" id="password1" name="password1" />
						<label class="iconfont icon-password  " style="top: 2px;"></label>
					</div>
					<div style="height:2px;"></div>
					 <div  class="login-btn-group">
					    <input type="button" value="保存" class="btn orange-btn col-xs-12  " id="subBtn" />
					</div>
                        <div class="jump-link" id="login-jump-link">
                            已有账号？<a href="${basePath}/login" id="toRegisterBtn">立即登录</a>
                        </div>
				    </form>
		
						
					</div>
				</div>
				</div>
			    
		
			</div>
		</div>
        <script src="${THIRD_URL}/third/jquery/jquery-1.11.0.js" type="text/javascript" charset="utf-8"></script>
        <script src="${THIRD_URL}/third/jquery/jquery.md5.js" type="text/javascript" charset="utf-8"></script>
        <script src="${STATIC_URL}/js/our-js/jumi.js" type="text/javascript"></script>
        <script src="${STATIC_URL}/js/our-js/jmClient.js" type="text/javascript" charset="utf-8"></script>
        <script src="${THIRD_URL}/third/util/artDialog/js/dialog.js" type="text/javascript" charset="utf-8"></script>
        <script src="${THIRD_URL}/third/util/artDialog/js/dialog-plus.js" type="text/javascript" charset="utf-8"></script>
        <script src="${STATIC_URL}/js/our-js/dialog_message.js" type="text/javascript" charset="utf-8"></script>
        <script>
            /*错误提示*/
             function errMsg(msg){
                var dm = new dialogMessage({
                    type:2,
                    title:'操作提醒',
                    fixed:true,
                    msg:msg,
                    isAutoDisplay:false

                });
                dm.render();
            };



		$(function(){
				//获取验证码单击事件
				$('#backcodebtn').click(function () {
		        	var phoneNumber = $("#phoneNumber").val();
		        	if(checkSubmitMobil("phoneNumber")){
		        	if(phoneNumber==""){
                        errMsg("请填写手机号码！");
		        		return;
		        	}
		        	
		        	var mydata = '{"phoneNumber":"' + phoneNumber + '"}'; 
		        	//调后台
		        	$.ajax({  
				        type : 'POST',  
				        contentType : 'application/json',  
				        url:'${basePath}/sendmsg/1',
				        processData : false,  
				        dataType : 'json',  
				        data : mydata,  
				        success : function(data) {  
				        	if(data.code==0){
				        		//点击获取验证码后改变验证码的样式
				            	document.getElementById('backcodebtn').style.cssText="background-color:#c2c1c1;";
				        		var count = 60;
				                var countdown = setInterval(CountDown, 1000);
				                function CountDown() {
				                    $("#backcodebtn").attr("disabled", true);
				                    $("#backcodebtn").val(count + " 秒后重新获取");
				                    if (count == 0) {
				                        $("#backcodebtn").val("获取手机验证码").removeAttr("disabled");
				                        document.getElementById('backcodebtn').style.cssText="background-color:#f3ba58;";
				                        clearInterval(countdown);
				                    }
				                    count--;
				                }
				        	}else{
                                errMsg(data.cause);
				        	}
				            
				        },  
				        error : function() {
                            errMsg('Err...');
				        }  
				    }); 
		        	}
		            
		        });
				
				
				//单击保存按钮
				$("#subBtn").click(function() {
					
					if(checkSubmitMobil("phoneNumber")){
					var phoneNumber = $("#phoneNumber").val();
					var password = $("#password").val();
					var password1 = $("#password1").val();
					var code = $("#code").val();
					var md5pwd=$.md5(password);
					var mydata = '{"phoneNumber":"' + phoneNumber + '","password":"' + md5pwd + '","code":"' + code + '"}';  
					//验证码
					if(password==""){
                        errMsg("请输入密码！");
						return;
					}
					
					if(password!=password1){
                        errMsg("两次输入的密码不相同！");
						return;
					}
					
						//跳转地址
						var url='${basePath}/shop';
						//发送post请求
						ajaxPost('${basePath}/system/backPwd', mydata,url);
					}
					
					
				});
		});
		
		 function ajaxPost(url,data,url2){

             $.ajaxJson(url,data,{
                 done:function(respone) {
                     if(respone.data.code==0){
                         window.location.href=url2;
                     }else{
                         errMsg(respone.data.cause);
                     }
                 }
             });

		 }
		 
		//jquery验证手机号码
		function checkSubmitMobil(phoneId) {
			if ($("#"+phoneId).val() == "") {
                errMsg("手机号码不能为空！");
				$("#"+phoneId).focus();
				return false;
			}

			if (!$("#"+phoneId).val().match(/^[1][34578][0-9]{9}$/)) {
                errMsg("手机号码格式不正确！");
				$("#"+phoneId).focus();
				return false;
			}
			return true;
		} 
		</script>
	</body>
</html>

