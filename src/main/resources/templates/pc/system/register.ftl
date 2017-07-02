<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>注册</title>
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
					<img src="${THIRD_URL}/img/pc/logo.png" />
				</div>
				<div class="jump-link" >
					已经有聚米账号？<a href="${basePath}/login">立即登录</a>
				</div>
			    <form class="login-form" action="" method="post">
					<div class="form-group">
						<input type="text" class="form-control" value="" name="regPhoneNumber" id="regPhoneNumber" placeholder="请输入手机号" />
						<label class="iconfont icon-person  "></label>
					</div> 
				<div  class="verify-code-group">
						<input type="text" class="form-control" value="" placeholder="验证码" id="regcode" name="regcode" />
						  <input type="button" value="免费获取手机验证码" class="btn  gray-btn " id="regcodebtn" />
					</div>
					<div class="form-group  ">
						<input type="password" class="form-control" id="regPassword1" name="regPassword1" value="" placeholder="请输入密码" />
						<label class="iconfont icon-password  "></label>
					</div>
					<div class="form-group  ">
						<input type="password" class="form-control" id="regPassword2" name="regPassword2" value="" placeholder="确认密码" />
						<label class="iconfont icon-password  "></label>
					</div>
					 <div  class="agreement-group">
					 	 <i  class="iconfont icon-checkboxactive"></i>
							我已经认真阅读并同意聚米为谷的《<a href="#">用户协议</a>》
					</div>
					 <div  class="login-btn-group">
					    <input type="button" value="注册" id="regBtn" class="btn orange-btn col-xs-12  " />
					</div>
				</form>
		
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
				$(".agreement-group .iconfont").click(function(){
					$(this).toggleClass("icon-checkboxactive");
					$(this).toggleClass("icon-checkbox");
				});
				
				
				//单击注册按钮
				$("#regBtn").click(function() {
					var phoneNumber = $("#regPhoneNumber").val();
					var password1 = $("#regPassword1").val();
					var password2 = $("#regPassword2").val();
					var md5pwd=$.md5(password1);
					var regcode = $("#regcode").val();
					
					if(checkSubmitMobil("regPhoneNumber")){
						if(password1==""){
                            errMsg("请填写密码！");
							return;
						}
						if(password2==""){
                            errMsg("请填写确认密码！");
							return;
						}
						if(password1!=password2){
                            errMsg("两次输入的密码不相同！");
							return;
						}
						if(regcode==""){
                            errMsg("请填写验证码！");
							return;
						}
						var mydata = '{"phoneNumber":"' + phoneNumber + '","password":"' + md5pwd + '","code":"' + regcode + '"}'; 
						//跳转地址
						var url='${basePath}/shop';
						//发送post请求
						ajaxPost('${basePath}/user',mydata,url);
					}
					
				});
				
				
				//获取手机验证码
				$('#regcodebtn').click(function () {
	            	var phoneNumber = $("#regPhoneNumber").val();
	            	if(checkSubmitMobil("regPhoneNumber")){
	            	if(phoneNumber==""){
                        errMsg("请填写手机号码！");
	            		return;
	            	}
	            	
	            	var mydata = '{"phoneNumber":"' + phoneNumber + '"}'; 
	            	//调后台
	            	$.ajax({  
	    		        type : 'POST',  
	    		        contentType : 'application/json',  
	    		        url:'${basePath}/sendmsg',
	    		        processData : false,  
	    		        dataType : 'json',  
	    		        data : mydata,  
	    		        success : function(data) {  
	    		        	if(data.code==0){
	    		        		var count = 60;
	    		                var countdown = setInterval(CountDown, 1000);

	    		                function CountDown() {
	    		                    $("#regcodebtn").attr("disabled", true);
	    		                    $("#regcodebtn").val(count + " 秒后重新获取");
	    		                    if (count == 0) {
	    		                        $("#regcodebtn").val("免费获取手机验证码").removeAttr("disabled");
	    		                        clearInterval(countdown);
	    		                    }
	    		                    count--;
	    		                }
	    		        	}else{
	    		        		alert(data.cause);
	    		        	}
	    		            
	    		        },  
	    		        error : function() {
                            errMsg('Err...');
	    		        }  
	    		    }); 
	            	}
	                
	            });
				
			});
			
			
			 function ajaxPost(url,data,url2){
				 $.ajax({  
				        type : 'POST',  
				        contentType : 'application/json',  
				        url:url, 
				        processData : false,  
				        dataType : 'json',  
				        data : data,  
				        success : function(data) {  
				        	if(data.code==0){
				        		//alert("操作成功！");
				        		window.location.href=url2;
				        	}else{
                                errMsg(data.cause);
				        	}
				            
				        },  
				        error : function() {  
				            alert('Err...');  
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
