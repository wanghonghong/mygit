CommonUtils.regNamespace("system", "login");

system.login = (function(){

	var _init = function(){

	};

	var _chageCode = function(){
		$('#codeImage').attr('src',CONTEXT_PATH+'/captcha-image?abc='+Math.random());//链接后添加Math.random，确保每次产生新的验证码，避免缓存问题。
	};

	var _toRegister = function () {
		$("#login-form").addClass("scale");
		$("#pagegroup").animate({
			left: "-100%",
		}, 1000);
		$("#register-form").removeClass("scale");
		$("#login-jump-link").hide();
		$("#register-jump-link").show();
	};

	var _toLoginDiv = function () {
		$("#register-form").addClass("scale");
		$("#pagegroup").animate({
			left: 0,
		}, 1000);
		$("#login-form").removeClass("scale");
		$("#login-jump-link").show();
		$("#register-jump-link").hide();
	};


	/*错误提示*/
	var _errMsg = function(msg){
		var dm = new dialogMessage({
			type:3,
			title:'操作提醒',
			fixed:true,
			msg:msg,
			isAutoDisplay:false
		});
		dm.render();
	};



	//验证手机号码
	var checkSubmitMobil = function(phoneId){
			if ($("#"+phoneId).val() == "") {
				_errMsg('请填写手机号码！');
				$("#"+phoneId).focus();
				return false;
			}

			if (!$("#"+phoneId).val().match(/^[1][34578][0-9]{9}$/)) {
				_errMsg('手机号码格式不正确！');
				$("#"+phoneId).focus();
				return false;
			}
			return true;
		};



	var _toLogin = function(){
		if(checkSubmitMobil("phoneNumber")){
			var phoneNumber = $("#phoneNumber").val();
			var password = $("#password").val();
			var code = $("#codeN").val();
			var md5pwd=$.md5(password);
			if(password==""){
				_errMsg('请输入密码！');
				return;
			}
			if(code==""){
				_errMsg('请输入验证码！');
				return;
			}
			var loginVo = {};
			loginVo.phoneNumber = phoneNumber;
			loginVo.password = md5pwd;
			loginVo.code = code;
			var jsonData = JSON.stringify(loginVo);
			var url = CONTEXT_PATH+"/login";
			$.ajaxJson(url,jsonData,{
				done:function(res) {
					if(res.data.code==1){
						_chageCode();
						_errMsg(res.data.msg);
					}else{
						window.location.href=CONTEXT_PATH+'/shop';
					}
				}
			});
		}
	};
	
	var _register = function () {
		var phoneNumber = $("#regPhoneNumber").val();
		var password1 = $("#regPassword1").val();
		var password2 = $("#regPassword2").val();
		var regcode = $("#regcode").val();
		var md5pwd=$.md5(password1);
		if(checkSubmitMobil("regPhoneNumber")){
			if(password1==""){
				_errMsg('请填写密码！');
				return;
			}
			if(password2==""){
				_errMsg('请填写确认密码！');
				return;
			}
			if(password1!=password2){
				_errMsg('两次输入的密码不相同！');
				return;
			}
			if(regcode==""){
				_errMsg('请填写验证码！');
				return;
			}
			var userCo = {};
			userCo.phoneNumber = phoneNumber;
			userCo.password = md5pwd;
			userCo.code = regcode;
			var jsonData = JSON.stringify(userCo);
			var url = CONTEXT_PATH+"/user";
			$.ajaxJson(url,jsonData,{
				done:function(res) {
					if(res.data.code==0){
						window.location.href=CONTEXT_PATH+'/shop';
					}else{
						_errMsg(res.data.cause);
					}
				}
			});

		}
	};


	//发送验证码
	var _sendCode = function () {
		var phoneNumber = $("#regPhoneNumber").val();
		if(checkSubmitMobil("regPhoneNumber")){
			var userVo = {};
			userVo.phoneNumber = phoneNumber;
			var jsonData = JSON.stringify(userVo);
			var url = CONTEXT_PATH+'/sendmsg/0';
			$.ajaxJson(url,jsonData,{
				done:function(res) {
					if(res.data.code==0){
						//点击获取验证码后改变验证码的样式
						document.getElementById('regcodebtn').style.cssText="background-color:#c2c1c1;";
						var count = 60;
						var countdown = setInterval(CountDown, 1000);
						function CountDown() {
							$("#regcodebtn").attr("disabled", true);
							$("#regcodebtn").val(count + " 秒后重新获取");
							if (count == 0) {
								$("#regcodebtn").val("获取手机验证码").removeAttr("disabled");
								document.getElementById('regcodebtn').style.cssText="background-color:#f3ba58;";
								clearInterval(countdown);
							}
							count--;
						}
					}else{
						_errMsg(res.data.cause)
					}
				}
			});
		}
	};



	return {
		init :_init,
		chageCode:_chageCode,
		toLogin:_toLogin,
		toRegister:_toRegister,
		toLoginDiv:_toLoginDiv,
		register:_register,
		sendCode:_sendCode
	};
})();




