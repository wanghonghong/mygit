
CommonUtils.regNamespace("zb","login");
zb.login = (function(){
	var ajaxUrl = {
		url1:CONTEXT_PATH+'/zb/sendmsg/0',
		url2:CONTEXT_PATH+'/zb/index',
		url3:CONTEXT_PATH+'/zb/sendmsg/1',
		url4:CONTEXT_PATH+'/zb/system/backPwd',
		url5:CONTEXT_PATH+'/zb/register',
		url6:CONTEXT_PATH+'/zb/register/focus_scan',
		url7:CONTEXT_PATH+'/zb/login',
        url8:CONTEXT_PATH+'/login'
    }

	var _init = function(){
		bind();
		dolog();
		register();
        _one();
	};

	//弹窗插件
	var Dialog = function (type,msg) {
		var dm = new dialogMessage({
			type:type,
			title:'操作提醒',
			fixed:true,
			msg:msg,
			isAutoDisplay:false
		});
		dm.render();
	}


    $('#form2').validate({
        rules : {
            phoneNumber :{
                required:true,
                isMobile:true
            },
            password : "required",
            password2 : "required",
            code:"required"
        },
        messages : {
            phoneNumber:{
                required:"请输入手机号码",
                isMobile:"手机号码输入有误"
            },
            password:"请输入密码",
            password2:"请再次输入密码",
            code:"请输入验证码"
        }
    });
    $('#form3').validate({
        rules : {
            phoneNumber :{
                required:true,
                isMobile:true
            },
            password : "required",
            password2 : "required",
            code:"required"
        },
        messages : {
            phoneNumber:{
                required:"请输入手机号码",
                isMobile:"手机号码输入有误"
            },
            password:"请输入密码",
            password2:"请再次输入密码",
            code:"请输入验证码"
        }
    });
	var bind = function(){



		$('#password2').keyup(function () {
			$('#password2-error1').css("display","none");
		});
		$('#code').on('keyup',function () {
			$('#error1').remove();
		});
        $('#phoneNumber').on('keyup',function () {
            $('#error1').remove();
        });

		$(document).keydown(function(event){
			if(event.keyCode==13){
				$("#login").click();
				$("#save-btn").click();
			}
		});

        $('#form1').validate({
            rules : {
                phoneNumber :{
                    required:true,
                    isMobile:true
                },
                password : "required",
                code:"required"
            },
            messages : {
                phoneNumber:{
                    required:"请输入手机号码",
                    isMobile:"手机号码输入有误"
                },
                password:"请输入密码",
                code:"请输入验证码"
            }
        });
		$('#login').click(function () {
			if($('#form1').valid()){
				if (validate1()){
					_doSubmit();
				}
			}
		})

		createCode();

		var _doSubmit = function(){
			var loginVo = {};
			loginVo.phoneNumber = $("#phoneNumber").val();
			var password = $("#password").val();
			loginVo.password=$.md5(password);
			var PIN = $("#PIN").val();
			var jsonData = JSON.stringify(loginVo);
			ajaxPostlg(CONTEXT_PATH+'/zb/login', jsonData,ajaxUrl.url2);
		}

		function ajaxPostlg(url,data,url2){
			$.ajaxJson(url,data,{
				done:function(respone){
					if(respone.data.code==0){
						window.location.href=url2;
					}else{
                        $('#code-error1').html('<div id="error1" class="error" >用户名或密码不正确!</div>');
					}
				}
			});
		}
		$('#checkCode').click(function () {
			createCode();
		})

		var code ; //在全局 定义验证码
		function createCode()
		{
			code = "";
			var codeLength = 4;//验证码的长度
			var checkCode = document.getElementById("checkCode");
			var selectChar = new Array(0,1,2,3,4,5,6,7,8,9,'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z');//所有候选组成验证码的字符，当然也可以用中文的
			for(var i=0;i<codeLength;i++)
			{
				var charIndex = Math.floor(Math.random()*36);
				code +=selectChar[charIndex];
			}
			if(checkCode)
			{
				checkCode.className="yzm";
				checkCode.innerHTML = code;
			}
		}

		//判断验证码
		function validate1 ()
		{
			var inputCode = document.getElementById("code").value;
			if( inputCode.toLowerCase() != code.toLowerCase() )
			{
                $('#code-error1').html('<div id="error1" class="error" >验证码输入错误！</div>');
				createCode();//刷新验证码
				return false;
			}
			else
			{
				return true;
			}
		}

	}
	//jquery验证手机号码
	function checkSubmitMobil(phoneId) {
		if ($("#"+phoneId).val() == "") {
		    $('#phone-error1').html('<div id="error1" class="error" >手机号码不能为空！</div>');
			$("#"+phoneId).focus();
			return false;
		}
		if (!$("#"+phoneId).val().match(/^[1][34578][0-9]{9}$/)) {
            $('#phone-error1').html('<div id="error1" class="error" >手机号码格式不正确！</div>');
			$("#"+phoneId).focus();
			return false;
		}
		return true;
	}
	var getCodeForRegister = function () {
		$('#codeForRegister').click(function () {
			var phoneNumber = $("#phoneNumber").val();
			if(checkSubmitMobil("phoneNumber")){
				var userForCreateCo ={};
				userForCreateCo.phoneNumber=phoneNumber;
				var mydata = JSON.stringify(userForCreateCo);
				//调后台
				var url = ajaxUrl.url1;
				$.ajaxJson(url,mydata,{
					done:function (res) {
						if(res.code==0){
							//点击获取验证码后改变验证码的样式
							var count = 60;
							var countdown = setInterval(CountDown, 1000);
							function CountDown() {
								$("#codeForRegister").attr("disabled", true);
								$("#codeForRegister").html(count + " 秒后重新获取");
								if (count == 0) {
									$("#codeForRegister").val("获取手机验证码").removeAttr("disabled");
									clearInterval(countdown);
									$("#codeForRegister").html("发送验证码");
								}
								count--;
							}
						}else{
							alert(res.cause);
						}
					},
					error : function() {
						alert('Err...');
					}
				})
			}
		});
	}

	var getCode = function () {
		$('#codeFor').click(function () {
			var phoneNumber = $("#phoneNumber").val();
			if(checkSubmitMobil("phoneNumber")){
				var userForCreateCo ={};
				userForCreateCo.phoneNumber=phoneNumber;
				var mydata = JSON.stringify(userForCreateCo);
				//调后台
				var url = ajaxUrl.url3;
				$.ajaxJson(url,mydata,{
					done:function (res) {
						if(res.code==0){
							//点击获取验证码后改变验证码的样式
							var count = 60;
							var countdown = setInterval(CountDown, 1000);
							function CountDown() {
								$("#codeFor").attr("disabled", true);
								$("#codeFor").html(count + " 秒后重新获取");
								if (count == 0) {
									$("#codeFor").val("获取手机验证码").removeAttr("disabled");
									clearInterval(countdown);
									$("#codeFor").html("发送验证码");
								}
								count--;
							}
						}else{
							alert(res.cause);
						}
					},
					error : function() {
						alert('Err...');
					}
				})
			}
		});
	}
	var dolog = function(){
		getCode();
		//单击保存按钮
		$("#save-btn").click(function() {
			if($('#form3').valid()){
				var phoneNumber = $("#phoneNumber").val();
				var password = $("#password").val();
				var password2 = $("#password2").val();
				var code = $("#code").val();
				var md5pwd=$.md5(password);
				var mydata = '{"phoneNumber":"' + phoneNumber + '","password":"' + md5pwd + '","code":"' + code + '"}';
				//验证码
				if(password!=password2){
                    $('#password2-error1').css("display","block");
                    return;
				}
				var url = ajaxUrl.url4;
				var url7 = ajaxUrl.url7;
				$.ajaxJson(url,mydata,{
					done:function (res) {
						if(res.data.code==0){
							var d = dialog({
								title: '操作提醒',
								content:'密码修改成功，请重新登录！',
								width:300,
								height:30,
								ok:function () {
									window.location.href=url7;
								},
								okValue:"确定",
								cancelValue: '取消',
								cancel:function(){
								}
							});
							d.showModal();
						}else{
                            var cause = res.data.cause;
                            var box = '<div id="error1" class="error" >'+cause+'</div>';
                            if(cause=='验证码输入错误！' || cause== '请先发送验证码！'){
                                $('#code-error1').html(box);
                            } else{
								$('#phone-error1').html(box);
							}}

					},
					error : function() {
						alert('Err...');
					}
				})
			}
		});

	}

	var register = function () {
		getCodeForRegister();
		//单击保存按钮
		$("#register_btn").click(function() {
			if($('#form2').valid()){
				var phoneNumber = $("#phoneNumber").val();
				var password = $("#password").val();
				var password2 = $("#password2").val();
				var code = $("#code").val();
				var md5pwd=$.md5(password);
				var mydata = '{"phoneNumber":"' + phoneNumber + '","password":"' + md5pwd + '","code":"' + code + '"}';
				//验证码
				if(password!=password2){
					$('#password2-error1').css("display","block");
					return;
				}
				var url = ajaxUrl.url5;
				var url3 = ajaxUrl.url6;
				$.ajaxJson(url,mydata,{
					done:function (res) {
						if(res.data.code==0){
							window.location.href=url3;
						}else{
							var cause = res.data.cause;
							var box = '<div id="error1" class="error" >'+cause+'</div>';
							if(cause=='验证码输入错误！' || cause== '请先发送验证码！'){
								$('#code-error1').html(box);
							}else if(cause=='发送验证码的手机与注册填写的手机号不匹配！' || cause== '手机号已被注册！'){
								$('#phone-error1').html(box);
							}
						}
					},
					error : function() {
						alert('Err...');
					}
				})
			}
		});
	}

	var countTime = function () {
		var count = 3;
		var countdown = setInterval(CountDown, 1000);
		function CountDown() {
			$("#count").html(count);
			if (count == 0) {
				window.location.href=ajaxUrl.url8;
			}
			count--;
		}
	}

	var _one = function () {
        if(!isSupportPlaceholder()) {
            // 遍历所有input对象, 除了密码框
            $('input').not("input[type='password']").each(
                function() {
                    var self = $(this);
                    var val = self.attr("placeholder");
                    input(self, val);
                }
            );

			/**/
			/* 对password框的特殊处理
			 * 1.创建一个text框
			 * 2.获取焦点和失去焦点的时候切换
			 */
            $('input[type="password"]').each(
                function() {
                    var pwdField = $(this);
                    var pwdVal = pwdField.attr('placeholder');
                    var pwdId = pwdField.attr('id');
                    // 重命名该input的id为原id后跟1
                    pwdField.after('<input id="' + pwdId + '1" type="text" value=' + pwdVal + ' autocomplete="off" />');
                    var pwdPlaceholder = $('#' + pwdId + '1');
                    pwdPlaceholder.show();
                    pwdField.hide();

                    pwdPlaceholder.focus(function() {
                        pwdPlaceholder.hide();
                        pwdField.show();
                        pwdField.focus();
                    });

                    pwdField.blur(function() {
                        if(pwdField.val() == '') {
                            pwdPlaceholder.show();
                            pwdField.hide();
                        }
                    });
                }
            );
        }

        // 判断浏览器是否支持placeholder属性
        function isSupportPlaceholder() {
            var input = document.createElement('input');
            return 'placeholder' in input;
        }
        // jQuery替换placeholder的处理
        function input(obj, val) {
            var $input = obj;
            var val = val;
            $input.attr({
                value: val
            });
            $input.focus(function() {
                if($input.val() == val) {
                    $(this).attr({
                        value: ""
                    });
                }
            }).blur(function() {
                if($input.val() == "") {
                    $(this).attr({
                        value: val
                    });
                }
            });
        }
    }
	return {
		init :_init, //初始化
		dolog:dolog,
		register:register,
		countTime:countTime,
	};
})();