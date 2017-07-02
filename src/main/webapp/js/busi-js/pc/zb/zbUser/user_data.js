/**
 * Created by BenRay on 16/8/8.
 * 官方图文
 */
CommonUtils.regNamespace("user", "userdata");
user.userdata = (function(){
    var ajaxUrl = {
        url1:CONTEXT_PATH+'/zb/user/',
        url2:CONTEXT_PATH+'/zb/sendmsg/1',
        url3:CONTEXT_PATH+'/zb/system/backPwd',
        url4:CONTEXT_PATH+'/login',
        url5:CONTEXT_PATH+'/zb/login/log',
    };
    var _init = function(){
        _bind()
        _doSubmit()
    };

    var Dialog1 = function (type,msg) {
        var dm = new dialogMessage({
            type:type,
            title:'操作提醒',
            fixed:true,
            msg:msg,
            isAutoDisplay:false
        });
        dm.render();
    }

    var _bind = function () {
        //登录日志分页
        var url = ajaxUrl.url5;
        var params = {
            pageSize: 20,
        };
        jumi.pagination('#loginLogbar', url, params, function (res, curPage) {
            if (res.code === 0) {
                var data = {
                    items: res.data.items
                };
                jumi.template('zb/user/login_log_list', data, function (tpl) {
                    $('#loginLogList').html(tpl);
                })
            }
        });

        $("#sex").select2({
            theme: "jumi"
        });
        $('#password2').keyup(function () {
            $('#password2-error1').css("display","none");
        });
        $('#code').on('keyup',function () {
            $('#error1').remove();
        });
        $('#phoneNumber2').on('keyup',function () {
            $('#error1').remove();
        });
        $("#form1").validate({
            rules:{
                userName:{
                    required:true,
                    minlength:2
                },
                email:{
                    email:true,
                    required:true,
                },
                wxnum:"required",
            },
            messages:{
                userName:{
                    required:"请输入联系姓名",
                    minlength:"联系姓名不能少于2位"
                },
                email:{
                    email:"邮箱格式不正确",
                    required:"请输入邮箱"
                },
                wxnum:"请输入微信号",
            }
        });
        $('#form2').validate({
            rules : {
                phoneNumber2 :{
                    required:true,
                    isMobile:true
                },
                password : "required",
                password2 : "required",
                code:"required"
            },
            messages : {
                phoneNumber2:{
                    required:"请输入手机号码",
                    isMobile:"手机号码输入有误"
                },
                password:"请输入密码",
                password2:"请再次输入密码",
                code:"请输入验证码"
            }
        });
        $('#password2').change(function () {
            $('#password2-error1').css("display","none");
        });
        $('#code').on('change',function () {
            $('#error1').remove();
        });
        $('#phoneNumber').on('change',function () {
            $('#error1').remove();
        });
        //
        $('#m-add-img').click(function () {
            var d = new Dialog({
                multifile:false,
                context_path:CONTEXT_PATH, //请求路径,  必填
                resType:5 ,//图片1，视频2，语音3   必填
                height:400,
                width:600,
                callback:function(url){
                    if(url){
                        $("#headImg").find(".add").addClass("z-hide");
                        $("#headImg").attr("src",url).removeClass("z-hide");
                        $("#headImg").find("[name='headImg']").val(url);
                    }
                }
            });
            d.render();
        })
        // //个人资料基础资料-修改密码-登录记录tab页切换
        $(".panel-hidden").hide().eq(0).show();
        var tabul = $("#m-tab ul li");
        tabul.click(function(){
            tabul.eq($(this).index()).addClass("z-sel").siblings().removeClass('z-sel');
            $(".panel-hidden").hide().eq($(this).index()).show();
        });
        $('#save1').click(function () {
            if($('#form1').valid()){
                var userName = $("#userName").val();
                var phoneNumber = $("#phoneNumber").val();
                var sex = $('#sex').find('option:selected').val();
                var mail = $("#mail").val();
                var wxnum = $("#wxnum").val();
                var staffCode = $("#staffCode").val();
                var department = $("#department").data('id');
                var userId = $("#userId").val();
                var headImg = $("#headImg").attr("src");
                if(headImg.indexOf("no_picture")!=-1){
                    headImg = null;
                }
                var data = {
                    userName : userName,
                    phoneNumber : phoneNumber,
                    sex : sex,
                    mail : mail,
                    wxnum : wxnum,
                    staffCode : staffCode,
                    department : department,
                    headImg:headImg
                }
                var jsonData = JSON.stringify(data);
                var url = ajaxUrl.url1+userId;
                $.ajaxJsonPut(url, jsonData, {
                    "done": function (res) {
                        if (res.code === 0) {
                            Dialog1(1,'保存成功');
                        } else {
                            Dialog1(3,'保存失败');
                        }
                    }
                });
            }
        });
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
    var _doSubmit = function(){
        $('#codeFor').click(function () {

            var phoneNumber = $("#phoneNumber2").val();
            if(checkSubmitMobil("phoneNumber2")){
                var userForCreateCo ={};
                userForCreateCo.phoneNumber=phoneNumber;
                var mydata = JSON.stringify(userForCreateCo);
                //调后台
                var url = ajaxUrl.url2;
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

        //单击保存按钮
        $("#save2").click(function() {
            if($('#form2').valid()){
                var phoneNumber = $("#phoneNumber2").val();
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
                var url2 = ajaxUrl.url3;
                $.ajaxJson(url2,mydata,{
                    done:function (res) {
                        if(res.data.code==0){
                            var d = dialog({
                                title: '操作提醒',
                                content:'密码修改成功，请重新登录！',
                                width:300,
                                height:30,
                                ok:function () {
                                    window.location.href=url;
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
                            }else if(cause=='发送验证码的手机号与填写的手机号不匹配！' || cause== '当前手机号未注册！'){
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
    return {
        init :_init,
    };
})();


