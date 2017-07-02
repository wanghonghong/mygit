/**
 * Created by whh on 2016/12/12.
 */

CommonUtils.regNamespace("agent", "join");
agent.join = (function(){
    var ajaxUrl = {
        url1:CONTEXT_PATH+'/join/add',
        url2:CONTEXT_PATH+'/join/roles',
    };
    var _init = function(){
        _bind();
    };

    //省市区插件
    var selectArea = function () {
        $('.distpicker').distpicker({
            province:'-- 省 --',
            city: '-- 市 --',
            district:'-- 区 --',
            autoSelect: false
        });
    }
    var _bind = function(){
        selectArea();
        $('label[id^="ability_"]').click(function(){
            var element = $(this);
            var id = element.data('id');
            $('.select-tt').removeClass("active");
            $('#roles_select_'+id).addClass("active");
        })
        $('i[id^="icon_"]').click(function(){
            $(this).parent().remove();
        })
        $("#u-logo").click(function(){
            $("#othermemu").toggleClass("z-change");
        })
        $(".m-indl-aplica").hide().eq(0).show();
        var tabul = $(".m-tab ul li");
        tabul.click(function(){
            tabul.eq($(this).index()).addClass("z-sel").siblings().removeClass('z-sel');
            $(".m-indl-aplica").hide().eq($(this).index()).show();
        });
        $("m-handlewait").hide().eq(0).show();
        var tabu2 = $(".m-tab ul li");
        tabu2.click(function(){
            tabu2.eq($(this).index()).addClass("z-sel").siblings().removeClass('z-sel');
            $(".m-handlewait").hide().eq($(this).index()).show();
        });

        $(".op-dialog").click(function() {
            var url = ajaxUrl.url2;
            $.ajaxJson(url,null,{
                "done":function (res) {
                    var data = {
                        roles:res.data,
                    };
                    jumi.template('operation/role_dialog',data,function (tpl) {
                        var d = dialog({
                            title: '现有角色',
                            id:'dialog_box1',
                            content: tpl,
                            width: 500,
                            onshow:function () {
                                $('.select-tt').click(function () {
                                    var element = $(this);
                                    $('.select-tt').removeClass("active");
                                    element.addClass("active");
                                })
                            }
                        }).showModal();
                    })
                }
             })
        });
        $("#applyRole").change(function () {
            $("#apply-error").hide();
        })
        _addImgEvent("#m-add-img")
        $('#submitApply1').click(function () {
            var url = ajaxUrl.url1;
            var form = $("#signupForm1");
            if (form.valid()) {
                submitApply1(url);
            } else {
                $('body').animate({scrollTop:0},1000);
            }
        });
        $('#submitApply2').click(function () {
            var applyRoleAgent = $("#applyRole").find("option:selected").val();
            if (!applyRoleAgent){
                $("#apply-error").show();
            }
            var url = ajaxUrl.url1;
            var form = $("#signupForm2");
            if (form.valid()) {
                submitApply2(url);
            } else {
                $('body').animate({scrollTop:0},1000);
            }
        });
        $('#submitApply3').click(function () {
            var url = ajaxUrl.url1;
            var form = $("#signupForm1");
            if (form.valid()) {
                submitApply3(url);
            } else {
                $('body').animate({scrollTop:0},1000);
            }
        });
        $('#submitApply4').click(function () {
            var url = ajaxUrl.url1;
            var form = $("#signupForm2");
            if (form.valid()) {
                submitApply4(url);
            } else {
                $('body').animate({scrollTop:0},1000);
            }
        });

        var validate = $("#signupForm1").validate({
            rules:{
                userName:{
                    required:true,
                    minlength:2
                },
                phoneNumber:{
                    isMobile:true,
                    required:true,
                },
                email:{
                    email:true,
                    required:true,
                },
                performance:"required",
                address:"required"
            },
            messages:{
                userName:{
                    required:"请输入联系姓名",
                    minlength:"联系姓名不能少于2位"
                },
                phoneNumber:{
                    isMobile:"手机号码格式不正确",
                    required:"请输入手机号码",
                },
                email:{
                    email:"邮箱格式不正确",
                    required:"请输入邮箱"
                },
                performance:"请输入业绩",
                address:"请输入地址"
            }
        });
        var validate2 = $("#signupForm2").validate({
            rules:{
                userName:{
                    required:true,
                    minlength:2
                },
                phoneNumber:{
                    isMobile:true,
                    required:true,
                },
                email:{
                    email:true,
                    required:true,
                },
                performance:"required",
                address:"required",
                companyName:"required",
                manageNum:{
                    isInteger:true,
                    isIntGtZero:true,
                    required:true,
                },
                url:"required",
                companyDesc:"required",
            },
            messages:{
                userName:{
                    required:"请输入联系姓名",
                    minlength:"联系姓名不能少于2位"
                },
                phoneNumber:{
                    isMobile:"手机号码格式不正确",
                    required:"请输入手机号码",
                },
                email:{
                    email:"邮箱格式不正确",
                    required:"请输入邮箱"
                },
                performance:"请输入业绩",
                address:"请输入地址",
                companyName:"请输入公司名称",
                manageNum:{
                    isInteger:"请输入正确人数",
                    isIntGtZero:"人数必须大于0",
                    required:"请输入经营人数",
                },
                url:"请输入公司网址",
                companyDesc:"请输入公司简介",
            }
        });
    }
    //软件代理个人加盟保存
    var submitApply1 = function(Url) {
        var joinCo = {};
        joinCo.userName = $("#userName").val();
        joinCo.phoneNumber = $("#phoneNumber").val();
        joinCo.email = $("#email").val();
        joinCo.performance = $("#performance").val();
        joinCo.address = $("#address").val();
        joinCo.type = 1;
        joinCo.subType = 1;
        joinCo.status = 1;
        joinCo.province = $("#province").find("option:selected").val();
        joinCo.city = $("#city").find("option:selected").val();
        joinCo.district = $("#district").find("option:selected").val();
        joinCo.applyRole = 10;
        var data =JSON.stringify(joinCo);
        $.ajaxJson(Url,data,{
            done:function(res){
                if(res.code==0){
                    var elem = document.getElementById('dialoginfo');
                    dialog({
                        title: "弹窗",
                        content: elem,
                        id:'p_wait',
                        onclose:function () {
                            jumi.template('software_agent/c_join_wait',function(tpl){
                                $('#agentBody').empty();
                                $('#agentBody').html(tpl);
                            });
                        }
                    }).width(500).showModal();
                }else {
                    var dm = new dialogMessage({
                        type:2,
                        fixed:true,
                        msg:'提交失败',
                        isAutoDisplay:true,
                        time:3000
                    });
                    dm.render();
                }
            }
        })
    }
    //软件代理平台公司加盟保存
    var submitApply2 = function(Url) {
        var joinCo = {};
        joinCo.userName = $("#userName").val();
        joinCo.phoneNumber = $("#phoneNumber").val();
        joinCo.email = $("#email").val();
        joinCo.address = $("#address").val();
        joinCo.manageNum = $("#manageNum").val();
        joinCo.businessLicense = $("#m-add-img img").attr("src");
        if (joinCo.businessLicense == "css/pc/img/no_picture.png" || joinCo.businessLicense=="" ){
            $('#businessLicense_span').css("display","block");
            return;
        }
        joinCo.companyName = $("#companyName").val();
        joinCo.companyDesc = $("#companyDesc").val();
        joinCo.companyUrl = $("#url").val();
        joinCo.applyRole = $("#applyRole").find("option:selected").val();
        joinCo.province = $("#province").find("option:selected").val();
        joinCo.city = $("#city").find("option:selected").val();
        joinCo.district = $("#district").find("option:selected").val();
        joinCo.type = 1;
        joinCo.subType = 2;
        joinCo.status = 1;
        var data = JSON.stringify(joinCo);
        $.ajaxJson(Url,data,{
            done:function(res){
                if(res.code==0){
                    var elem = document.getElementById('dialoginfo');
                    dialog({
                        title: "弹窗",
                        content: elem,
                        id:'c_wait',
                        onclose:function () {
                            jumi.template('software_agent/c_join_wait',function(tpl){
                                $('#agentBody').empty();
                                $('#agentBody').html(tpl);
                            });
                        }
                    }).width(500).showModal();
                }else {
                    var dm = new dialogMessage({
                        type:3,
                        fixed:true,
                        msg:'提交失败！',
                        isAutoDisplay:true,
                        time:3000
                    });
                    dm.render();
                }
            }
        })
    };
    //运营服务平台个人加盟保存
    var submitApply3 = function(Url) {
        var joinCo = {};
        joinCo.userName = $("#userName").val();
        joinCo.phoneNumber = $("#phoneNumber").val();
        joinCo.email = $("#email").val();
        joinCo.performance = $("#performance").val();
        joinCo.address = $("#address").val();
        joinCo.type = 2;
        joinCo.subType = 1;
        joinCo.status = 1;
        joinCo.applyRole = $('.u-btn-smltgry').attr('data-id');
        if(!joinCo.applyRole){
            $('#selectRole').css("display","block");
            return;
        }
        joinCo.province = $("#province").find("option:selected").val();
        joinCo.city = $("#city").find("option:selected").val();
        joinCo.district = $("#district").find("option:selected").val();
        var data = JSON.stringify(joinCo);
        $.ajaxJson(Url,data,{
            done:function(res){
                if(res.code==0){
                    var elem = document.getElementById('dialoginfo');
                    dialog({
                        title: "弹窗",
                        content: elem,
                        id:'op-p-wait',
                        onclose:function () {
                            jumi.template('operation/p_join_wait',function(tpl){
                                $('#operationBody').empty();
                                $('#operationBody').html(tpl);
                            });
                        }
                    }).width(500).showModal();
                }else {
                    var dm = new dialogMessage({
                        type:3,
                        fixed:true,
                        msg:'提交失败！',
                        isAutoDisplay:true,
                        time:3000
                    });
                    dm.render();
                }
            }
        })
    };
    //运营服务平台公司加盟保存
    var submitApply4 = function(Url) {
        var joinCo = {};
        joinCo.userName = $("#userName").val();
        joinCo.phoneNumber = $("#phoneNumber").val();
        joinCo.email = $("#email").val();
        joinCo.address = $("#address").val();
        joinCo.manageNum = $("#manageNum").val();
        joinCo.businessLicense = $("#m-add-img img").attr("src");
        if (joinCo.businessLicense == "css/pc/img/no_picture.png"){
            $('#businessLicense_span').css("display","block");
            return;
        }
        joinCo.companyName = $("#companyName").val();
        joinCo.companyDesc = $("#companyDesc").val();
        joinCo.companyUrl = $("#url").val();
        joinCo.province = $("#province").find("option:selected").val();
        joinCo.city = $("#city").find("option:selected").val();
        joinCo.district = $("#district").find("option:selected").val();
        joinCo.type = 2;
        joinCo.subType = 2;
        joinCo.status = 1;
        joinCo.applyRole = $('.u-btn-smltgry').attr('data-id');
        if(!joinCo.applyRole){
            $('#businessLicense_span').css("display","none");
            $('#selectRole').css("display","block");
            return;
        }
        var data = JSON.stringify(joinCo);
        $.ajaxJson(Url,data,{
            done:function(res){
                if(res.code==0){
                    var elem = document.getElementById('dialoginfo');
                    dialog({
                        title: "弹窗",
                        content: elem,
                        id:'op-c-wait',
                        onclose:function () {
                            jumi.template('operation/c_join_wait',function(tpl){
                                $('#operationBody').empty();
                                $('#operationBody').html(tpl);
                            });
                        }
                    }).width(500).showModal();
                }else {
                    var dm = new dialogMessage({
                        type:3,
                        fixed:true,
                        msg:'提交失败！',
                        isAutoDisplay:true,
                        time:3000
                    });
                    dm.render();
                }
            }
        })
    };
    //点击个人加盟我知道了
    var _konw1Click = function () {
        jumi.template('software_agent/p_join_wait',function(tpl){
            dialog.get('p_wait').remove().close();
            $('#agentBody').empty();
            $('#agentBody').html(tpl);
        });
    }
    var _konw2Click = function () {
        dialog.get('c_wait').remove().close();
        jumi.template('software_agent/c_join_wait',function(tpl){
            $('#agentBody').empty();
            $('#agentBody').html(tpl);
        });
    }
    var _konw3Click = function () {
        dialog.get('op-p-wait').remove().close();
        jumi.template('operation/p_join_wait',function(tpl){
            $('#operationBody').empty();
            $('#operationBody').html(tpl);
        });
    }
    var _konw4Click = function () {
        dialog.get('op-c-wait').remove().close();
        jumi.template('operation/c_join_wait',function(tpl){
            $('#operationBody').empty();
            $('#operationBody').html(tpl);
        });
    }
    var _addImgEvent=function (id) {
        $(id).click(function(){
            var d = new Dialog({
                multifile:false,
                context_path:CONTEXT_PATH, //请求路径,  必填
                resType:5 ,//图片1，视频2，语音3   必填
                height:380,
                width:600,
                callback:function(url){
                    if(url){
                        $(id).find(".add").addClass("z-hide");
                        $(id).find("img").attr("src",url).removeClass("z-hide");
                        $(id).find("[name='groupImagePath']").val(url);
                        $('#businessLicense_span').css("display","none");
                    }
                }
            });
            d.render();
        })
    }
    var _closeReview = function () {
        dialog.get('closeReview').remove().close();
    }
    //跳转代理平台个人加盟申请页面
    var _pJoin = function () {
        dialog.get('closeReview').remove().close();
        var data = {};
        var joinVoUrl = CONTEXT_PATH + "/join/apply/1";
        $.ajaxJsonGet(joinVoUrl, null, {
            "done": function (res) {
                data.jmJoin = res.data;
                jumi.template('software_agent/personal_join',data,function(tpl){
                    $('#agentBody').empty();
                    $('#agentBody').html(tpl);
                });
            }
        })
    }
    //跳转代理平台公司加盟申请页面
    var _cJoin = function () {
        dialog.get('closeReview').remove().close();
        var data = {};
        var joinVoUrl = CONTEXT_PATH + "/join/apply/1";
        $.ajaxJsonGet(joinVoUrl, null, {
            "done": function (res) {
                data.jmJoin = res.data;
                jumi.template('software_agent/company_join',data,function(tpl){
                    $('#agentBody').empty();
                    $('#agentBody').html(tpl);
                });
            }
        })
    }
    //跳转运营服务平台个人加盟申请页面
    var _oppJoin = function () {
        dialog.get('closeReview').remove().close();
        var data = {};
        var joinVoUrl = CONTEXT_PATH + "/join/apply/2";
        $.ajaxJsonGet(joinVoUrl, null, {
            "done": function (res) {
                data.jmJoin = res.data;
                jumi.template('operation/personal_join',data,function(tpl){
                    $('#operationBody').empty();
                    $('#operationBody').html(tpl);
                });
            }
        })
    }
    //跳转运营服务平台公司加盟申请页面
    var _opcJoin = function () {
        dialog.get('closeReview').remove().close();
        var data = {};
        var joinVoUrl = CONTEXT_PATH + "/join/apply/2";
        $.ajaxJsonGet(joinVoUrl, null, {
            "done": function (res) {
                data.jmJoin = res.data;
                jumi.template('operation/company_join',data,function(tpl){
                    $('#operationBody').empty();
                    $('#operationBody').html(tpl);
                });
            }
        })
    }

    var _sure = function () {
        var roles = [];
        $('input[name="operation"]:checked').each(function(){
            var obj = {};
            obj.id = $(this).val();
            obj.name = $(this).data('name');
            roles.push(obj);
        })
        if(roles.length<1){
           var dm = new dialogMessage({
                type:3,
                fixed:true,
                msg:'请选择角色！',
                isAutoDisplay:true,
                time:3000
            });
            dm.render();
        }else{
            var tpl = '';
            $.each(roles,function(k,v){
                tpl+='<div class="u-btn-smltgry" data-id="'+v.id+'" data-name="'+v.name+'"><span>'+v.name+'</span><i class="iconfont icon-delete1 f-ml-m" id="icon_'+v.id+'"></i></div>'
            })
            $('#m-tip-box1').html(tpl);
            $('#selectRole').css("display","none");
            $('i[id^="icon_"]').click(function(){
                $(this).parent().remove();
            })
            dialog.get('dialog_box1').remove().close();
        }
    }

    return {
        init :_init,
        konw1Click:_konw1Click,
        konw2Click:_konw2Click,
        konw3Click:_konw3Click,
        konw4Click:_konw4Click,
        closeReview:_closeReview,
        pJoin:_pJoin,
        cJoin:_cJoin,
        oppJoin:_oppJoin,
        opcJoin:_opcJoin,
        sure:_sure,
    };
})();
