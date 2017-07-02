/**
 * Created by BenRay on 16/8/8.
 * 官方图文
 */
CommonUtils.regNamespace("server", "class");
server.class = (function(){
    var ajaxUrl = {
        url1:CONTEXT_PATH+'/zb/customer_center/list',
        url2:CONTEXT_PATH+'/zb/class_data/update',
        url3:CONTEXT_PATH+'/zb/class_data',
    };
    var _init = function(){
        _bind();
        _timeTimepicker();
    };
    //时间空间渲染函数
    var _timeTimepicker = function () {
        var dateConfig = {
            showSecond : true,
            timeFormat : 'hh:mm:ss',
            dateFormat : 'yy-mm-dd',
            stepHour : 1,
            stepMinute : 1,
            stepSecond : 1,
        };
        $("#startTime").datepicker(dateConfig);
        $("#endTime").datepicker(dateConfig);
    };

    var _bind = function(){
        $("#area").select2({
            theme: "jumi"
        });
        $("#sex").select2({
            theme: "jumi"
        });
        $("#applyRole").select2({
            theme: "jumi"
        });

        var url = ajaxUrl.url1;
        var params = {
            pageSize: 10,
            type:2,
        };
        jumi.pagination('#serviceToolbar', url, params, function (res, curPage) {
            if (res.code === 0) {
                var data = {
                    items: res.data.items
                };
                jumi.template('zb/user/service_class_list', data, function (tpl) {
                    $('#serviceClassList').html(tpl);
                })
            }
        });

        //点击查询按钮
        $('#search-btn').click( function(){
            if($("#startTime").val()){
                var startTime = $("#startTime").val()+" 00:00:00";
            }
            if ($("#endTime").val()){
                var endTime =  $("#endTime").val()+" 23:59:59";
            }
            var userName = $("#userName").val();
            var phoneNumber = $("#phoneNumber").val();
            var sex = $('#sex').val();
            if(sex=='男'){
                sex=1;
            }else if(sex=='女'){
                sex=2;
            }
            var applyRole = $("#applyRole").val();
            var area = $("#area").val();
            var companyName = $("#companyName").val();
            var params = {
                pageSize:10,
                type:2,
                userName : userName,
                phoneNumber : phoneNumber,
                applyRole : applyRole,
                sex : sex,
                area : area,
                startTime:startTime,
                endTime:endTime,
                companyName:companyName,
            };
            jumi.pagination('#serviceToolbar', url, params, function (res, curPage) {
                if (res.code === 0) {
                    var data = {
                        items: res.data.items
                    };
                    jumi.template('zb/user/service_class_list', data, function (tpl) {
                        $('#serviceClassList').html(tpl);
                    })
                }
            });
        });
        //点击查看按钮
        $('#serviceClassList').on('click','.lookShop',function(){
            jumi.template('zb/user/service_class_shop',null,function (tpl) {
                var d = dialog({
                    title: '　',
                    content:tpl,
                    width:900,
                    onshow:function () {
                        $('.close-btn').click(function(){
                            d.close().remove();
                        });
                    },
                    onremove: function () {
                        jumi.msg('对话框已销毁');
                    }
                });
                d.showModal();
            })
        });
        //点击编辑按钮
        $('#serviceClassList').on('click','.edit',function(){
            var self = $(this);
            var userId = self.attr('data-type-id');
            var type = self.attr('data-type');
            var data = {
                userId:userId,
                type:type,
            };
            var url3 = ajaxUrl.url3;
            $.ajaxJson(url3, data, {
                done: function (res) {
                    if(res.code===0){
                        var data = res.data;
                        _classDialog(data);
                    }
                }
            })
        });
        var _classDialog = function (data) {
            jumi.template('zb/user/service_class_dialog',data,function (tpl) {
                var d = dialog({
                    title: '　',
                    content:tpl,
                    width:900,
                    onshow:function () {
                        $('#menus').find('li').click(function () {
                            $(this).addClass('z-sel').siblings().removeClass("z-sel");
                            var index = $(this).attr('data-index');
                            var tpl_name = $(this).attr('data-tpl-name');
                            var url = $(this).attr('data-url');
                            _queryListData(data,tpl_name,url);
                        })
                        $('.close-btn').click(function(){
                            d.close().remove();
                        });
                        $('#menus').find('li').eq(0).trigger('click');
                    },
                    onremove: function () {
                        jumi.msg('对话框已销毁');
                    }
                });
                d.showModal();
            })
        }
        //渲染数据
        var _queryListData = function(data,tpl_name,url){
            if (url) {
                url = CONTEXT_PATH + url;
                $.ajaxJson(url, data, {
                    done: function (res) {
                        if(res.code===0){
                            var data = res.data;
                            jumi.template(tpl_name, data, function (html) {
                                $("#context").html(html);
                                validForm1();
                                _initAreaDistrict();
                                _addImgEvent("#businessLicense");
                                updateclass();
                            })
                        }
                    }
                })
            }
            else{
                jumi.template(tpl_name,function(html){
                    $("#context").html(html);
                })
            }
        }
        //表单验证
        var validForm1 =  function(){
            $("#form1").validate({
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
                    manageNum:{
                        isInteger:true,
                        isIntGtZero:true,
                        required:true,
                    },
                    wxnum:"required",
                    companyName:"required",
                    companyUrl:"required",
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
                    wxnum:"请输入微信号",
                    companyName:"请输入公司名称",
                    manageNum:{
                        isInteger:"请输入正确人数",
                        isIntGtZero:"人数必须大于0",
                        required:"请输入经营人数",
                    },
                    companyUrl:"请输入公司网址",
                    address:"请输入地址"
                }
            });
        }
        var _initAreaDistrict = function(){
            var province = $("#_province").val();
            var city = $("#_city").val();
            var district = $("#_district").val();
            $(".distpicker").distpicker({
                province: province ,
                city: city ,
                district: district ,
            });
        }
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
        //点击保存
        var updateclass = function () {
            $('#context').unbind().on('click','#update_btn',function () {
                if($('#form1').valid()){
                    var classVo = {};
                    classVo.userId = $('#userId').val();
                    classVo.type = 2;
                    classVo.userName = $('#userName_').val();
                    classVo.phoneNumber = $('#phoneNumber_').val();
                    classVo.wxnum= $('#wxnum').val();
                    classVo.email = $('#email').val();
                    classVo.province = $("#province").find("option:selected").val();
                    classVo.city = $("#city").find("option:selected").val();
                    classVo.district = $("#district").find("option:selected").val();
                    classVo.address = $('#address').val();
                    classVo.companyName = $('#companyName').val();
                    classVo.manageNum = $('#manageNum').val();
                    classVo.companyUrl = $('#companyUrl').val();
                    classVo.manageNum = $('#manageNum').val();
                    classVo.manageNum = $('#manageNum').val();
                    classVo.businessLicense = $("#businessLicense").attr("src");
                    var jsonData = JSON.stringify(classVo);
                    var url = ajaxUrl.url2;
                    $.ajaxJson(url, jsonData, {
                        "done": function (res) {
                            if (res.code === 0) {
                                Dialog1(1,'保存成功');
                            } else {
                                Dialog1(3,'保存失败');
                            }
                        }
                    });
                }
            })
        }
        //图片弹出插件
        var _addImgEvent=function (id) {
            $(id).click(function () {
                var d = new Dialog({
                    multifile:false,
                    context_path:CONTEXT_PATH, //请求路径,  必填
                    resType:5 ,//图片1，视频2，语音3   必填
                    height:400,
                    width:600,
                    callback:function(url){
                        if(url){
                            $(id).addClass("z-hide");
                            $(id).attr("src",url).removeClass("z-hide");
                            $(id).val(url);
                        }
                    }
                });
                d.render();
            })
        }

    }
    return {
        init :_init
    };
})();

