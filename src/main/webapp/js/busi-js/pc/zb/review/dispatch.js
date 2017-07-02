/**
 * Created by BenRay on 16/11/16.
 * 总部派单
 */
CommonUtils.regNamespace("dispatch", "review");
dispatch.review = (function(){
    var ajaxUrl = {
        url1:CONTEXT_PATH+'/zb/dispatch',
        url2:CONTEXT_PATH+'/zb/dispatch/list',
        url3:CONTEXT_PATH+'/zb/dispatch/joins',
        url4:CONTEXT_PATH+'/zb/dispatch/roles',
        url5:CONTEXT_PATH+'/zb/dispatch_history',
        url6:CONTEXT_PATH+'/zb/dispatch_history/list',
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
    //派单操作弹出框选择不同渠道商角色等级变化
    var selectJoin = function () {
        var allRole = $('#allRole');
        var agentRole = $('#agentRole');
        var serviceRole = $('#serviceRole');
        $('#joinType').on('change',function () {
            if( $(this).val()==1){
                agentRole.show();
                serviceRole.hide();
                allRole.hide();
            }else if( $(this).val()==2){
                agentRole.hide();
                serviceRole.show();
                allRole.hide();
            }else {
                agentRole.hide();
                serviceRole.hide();
                allRole.show();
            }
        })
    }
    //表单验证
    var validate = function () {
        $("#form1").validate({
            rules:{
                businessName:{
                    required:true,
                    minlength:2
                },
                phoneNumber:{
                    isMobile:true,
                    required:true,
                },
                companyName:"required",
                address:"required",
                need:"required"
            },
            messages:{
                businessName:{
                    required:"请输入商家姓名",
                    minlength:"商家姓名不能少于2位"
                },
                phoneNumber:{
                    isMobile:"手机号码格式不正确",
                    required:"请输入手机号码",
                },
                companyName:"请输入公司名称",
                address:"请输入详细地址",
                need:"请输入需求留言"
            }
        });
    }
    var _bind = function(){
        $("#source").select2({
            theme: "jumi"
        });
        $("#status").select2({
            theme: "jumi"
        });
        $("#subType").select2({
            theme: "jumi"
        });

        //改派
        $(".dialog-changeorder").click(function() {
            var elem = document.getElementById('dialoginfo-changeorder');
            dialog({
                title: "改派操作",
                content: elem,
            }).width(1015).showModal();

        });

        //审核
        $(".dialog-auditorder").click(function() {
            var elem = document.getElementById('dialoginfo-auditorder');
            dialog({
                title: "审核操作",
                content: elem,
            }).width(1015).showModal();

        });
        $(".panel-hid1").hide().eq(0).show();
        var tabul1 = $("#m-tab3 ul li");
        tabul1.click(function(){
            tabul1.eq($(this).index()).addClass("z-sel").siblings().removeClass('z-sel');
            $(".panel-hid1").hide().eq($(this).index()).show();
        });
        $(".panel-hid2").hide().eq(0).show();
        var tabul1 = $("#m-tab4 ul li");
        tabul1.click(function(){
            tabul1.eq($(this).index()).addClass("z-sel").siblings().removeClass('z-sel');
            $(".panel-hid2").hide().eq($(this).index()).show();
        });
        $('.company-more').on('click',function(){
            if($('.company-more').html() == "全文"){
                $('.company-text').css('height','auto');
                $('.company-more').html('收起');
            }
            else{
                $('.company-text').css('height','42px');
                $('.company-more').html('全文');
            }
        });

        _timeTimepicker();
        //客服增单
        $(".dialog-addorder").click(function() {
            jumi.template('zb/review/dispatch_save_dialog',function (tpl) {
                var d =dialog({
                    title: "创建工单",
                    content: tpl,
                    onshow:function () {
                        $("#select_subType").select2({
                            theme: "jumi"
                        });

                        selectArea();
                        validate();
                        saveDispatch(d);
                    }
                }).width(650);
                d.showModal();
            })
        });
        var saveDispatch = function (d) {
            $('#saveDispatch').click(function () {
                if($('#form1').valid()){
                    var url = ajaxUrl.url1;
                    var dispatchCo = {};
                    dispatchCo.type = 1;
                    dispatchCo.source = 1;
                    dispatchCo.status = 1;
                    dispatchCo.subType = $('#select_subType').find('option:selected').val();
                    dispatchCo.businessName = $('#businessName').val();
                    dispatchCo.phoneNumber = $('#phoneNumber').val();
                    dispatchCo.companyName = $('#companyName').val();
                    dispatchCo.province = $("#province").find("option:selected").val();
                    dispatchCo.city = $("#city").find("option:selected").val();
                    dispatchCo.district = $("#district").find("option:selected").val();
                    dispatchCo.address = $('#address').val();
                    dispatchCo.need = $('#need').val();
                    var data = JSON.stringify(dispatchCo);
                    $.ajaxJson(url,data, {
                        done: function (res) {
                            if(res.code==0){
                                var dm = new dialogMessage({
                                    type:1,
                                    fixed:true,
                                    msg:"保存成功",
                                    isAutoDisplay:false,
                                });
                                dm.render();
                                $('button[i-id="ok"]').click(function () {
                                    d.close().remove();
                                    $('.m-memu .two-level-memu.z-sel').trigger('click');
                                })
                                $('button[i="close"]').click(function () {
                                    d.close().remove();
                                    $('.m-memu .two-level-memu.z-sel').trigger('click');
                                })
                            }

                        }
                    })
                }
            })
        }
        //派单列表
        var url = ajaxUrl.url2;
        var params = {
            pageSize:10,
            type:1,
        };
        jumi.pagination('#dispatchToolbar',url,params,function(res,curPage){
            if(res.code===0){
                var data = {
                    items:res.data.items
                };
                jumi.template('zb/review/dispatch_list',data,function(tpl){
                    $('#dispatchList').html(tpl);
                })
            }
        })
        $('#query').click( function(){
            var source = $("#source").find('Option:selected').val();
            var status = $('#status').find('option:selected').val();
            var subType = $('#subType').find('option:selected').val();
            var type = 1;
            if($("#startTime").val()){
                var startTime = $("#startTime").val()+" 00:00:00";
            }
            if ($("#endTime").val()){
                var endTime =  $("#endTime").val()+" 23:59:59";
            }
            var params = {
                pageSize:10,
                source:source,
                status:status,
                subType : subType,
                type : type,
                startTime : startTime,
                endTime : endTime,
            };
            var url = ajaxUrl.url2;
            jumi.pagination('#dispatchToolbar',url,params,function(res,curPage){
                if(res.code===0){
                    var data = {
                        items:res.data.items,
                    };
                    jumi.template('zb/review/dispatch_list',data,function(tpl){
                        $('#dispatchList').html(tpl);
                    })
                }
            });
        });
        //点击派单按钮
        $('#dispatchList').on('click','div[id^="dispatch_"],div[id^="change_dispatch_"]',function(){
            var dispatchId = $(this).attr('data-type-id');
            var url = ajaxUrl.url4;
            var dispatchId = JSON.stringify(dispatchId);
            $.ajaxJson(url,dispatchId, {
                "done": function (res) {
                    var data = {
                        item: res.data,
                        roles: res.data.roles,
                        zbDispatchHistoryList : res.data.zbDispatchHistoryList,
                        dispatchId:dispatchId
                    };
                    jumi.template('zb/review/dispatch_select_dialog',data,function (tpl) {
                        var d = dialog({
                            title: '派单操作',
                            id:'dispatchDialog',
                            content:tpl,
                            width:1015,
                            height:560,
                            onshow:function () {

                                $(".panel-hidden").hide().eq(0).show();
                                var tabul1 = $("#m-tab1 ul li");
                                tabul1.click(function(){
                                    tabul1.eq($(this).index()).addClass("z-sel").siblings().removeClass('z-sel');
                                    $(".panel-hidden").hide().eq($(this).index()).show();
                                });
                                selectArea();
                                selectJoin();
                                $("#joinType").select2({
                                    theme: "jumi"
                                });
                                $("#roleClass").select2({
                                    theme: "jumi"
                                });
                                $("#roleClass2").select2({
                                    theme: "jumi"
                                });
                                $("#allRoleClass").select2({
                                    theme: "jumi"
                                })
                                //渠道商列表
                                var url = ajaxUrl.url3;
                                var params = {
                                    pageSize:5,
                                };
                                jumi.pagination('#joinPageToolbar',url,params,function(res,curPage){
                                    if(res.code===0){
                                        var data = {
                                            items:res.data.items
                                        };
                                        jumi.template('zb/review/dispatch_join_list',data,function(tpl){
                                            $('#joinList').html(tpl);
                                        })
                                    }
                                })
                                //派单操作点击查询加盟信息
                                $('#queryJoins').click( function(){
                                    var type = $("#joinType").find('Option:selected').val();
                                    if ($('#joinType').val()==1){
                                        var applyRoleId = $('#roleClass').find('option:selected').val();
                                    }else if($('#joinType').val()==2) {
                                        var applyRoleId = $('#serviceRole').find('option:selected').val();
                                    }else {
                                        var applyRoleId = $('.selectRole').find('option:selected').val();
                                    }
                                    var phoneNumber = $("#phoneNumber_").val();
                                    var userName = $("#userName_").val();
                                    var province = $('#province_').find('option:selected').val();
                                    var city = $('#city_').find('option:selected').val();
                                    var district = $('#district_').find('option:selected').val();
                                    var params = {
                                        pageSize:5,
                                        type : type,
                                        applyRoleId:applyRoleId,
                                        phoneNumber : phoneNumber,
                                        userName : userName,
                                        province: province,
                                        city : city,
                                        district : district,
                                    };
                                    var url = ajaxUrl.url3;
                                    jumi.pagination('#joinPageToolbar',url,params,function(res,curPage){
                                        if(res.code===0){
                                            var data = {
                                                items:res.data.items,
                                            };
                                            jumi.template('zb/review/dispatch_join_list',data,function(tpl){
                                                $('#joinTypeList').html(tpl);
                                            })
                                        }
                                    });
                                });
                                //点击派单操作选择框
                                $('#joinList').on('click','label[id^="label_"]',function() {
                                    var dispatchId = $('#dispatchId').val();
                                    var joinId = $(this).attr('joinId');
                                    var province = $(this).attr('province');
                                    var city = $(this).attr('city');
                                    var district = $(this).attr('district');
                                    var roleName = $(this).attr('roleName');
                                    var userName =  $(this).attr('userName');
                                    var data = {};
                                    data.userName = $(this).attr('userName');
                                    data.companyName = $(this).attr('companyName');
                                    data.phoneNumber = $(this).attr('phoneNumber');
                                    jumi.template('zb/review/dispatch_last_dialog', data, function (tpl) {
                                        var d = dialog({
                                            title: '操作提醒',
                                            content: tpl,
                                            width: 400,
                                            onshow: function () {
                                                $('#close-btn').click(function(){
                                                    d.close().remove();
                                                });
                                                $('#sure1').click(function(){
                                                    var dispatchHistory = {};
                                                    dispatchHistory.dispatchId = dispatchId;
                                                    dispatchHistory.type = 1;
                                                    dispatchHistory.joinId = joinId;
                                                    dispatchHistory.province = province;
                                                    dispatchHistory.city = city;
                                                    dispatchHistory.district = district;
                                                    dispatchHistory.roleName = roleName;
                                                    dispatchHistory.userName = userName;
                                                    dispatchHistory.companyName = $('#companyName-last').data('name');
                                                    dispatchHistory.phoneNumber = $('#phoneNumber-last').data('number');
                                                    var url = ajaxUrl.url5;
                                                    var data = JSON.stringify(dispatchHistory);
                                                    $.ajaxJson(url,data, {
                                                        done: function (res) {
                                                            if(res.code==0){
                                                                d.close().remove();
                                                                dialog.get('dispatchDialog').close().remove();
                                                                $('.m-memu .two-level-memu.z-sel').trigger('click');
                                                            }
                                                        }
                                                    })
                                                });
                                            }
                                        }).showModal();
                                    })
                                });
                            },
                            onremove: function () {
                                jumi.msg('对话框已销毁');
                            }
                        });
                        d.showModal();
                    })
                }
            })
        });
        //点击查看按钮
        $('#dispatchList').on('click','div[id^="search_"]',function(){
            var status = $(this).data('status');
            var url = ajaxUrl.url6;
            var dispatchQo = {};
            dispatchQo.dispatchId = $(this).attr('data-type-id');
            var data = JSON.stringify(dispatchQo);
            $.ajaxJson(url,data, {
                "done": function (res) {
                    var data = {
                        status:status,
                        zbDispatchHistoryList: res.data,
                        lastDispatchHistory: res.data[res.data.length-1]
                    };
                    jumi.template('zb/review/dispatch_search_dialog',data,function (tpl) {
                        var d = dialog({
                            title: '查看操作',
                            content:tpl,
                            width:1015,
                            height:420,
                            onshow:function () {
                                $("#search-dialog1").show();
                                $("#search-dialog2").hide();
                                var tabul2 = $("#m-tab2 ul li");
                                tabul2.click(function(){
                                    $(this).addClass("z-sel").siblings().removeClass('z-sel');
                                    $('div[name="search-dialog"]').hide().eq($(this).index()).show();
                                });
                            },
                            onremove: function () {
                                jumi.msg('对话框已销毁');
                            }
                        });
                        d.showModal();
                    })
                }
            })
        });
        //点击审核按钮
        $('#dispatchList').on('click','div[id^="check_dispatch_"]',function(){
            var url = ajaxUrl.url6;
            var dispatchQo = {};
            dispatchQo.dispatchId = $(this).attr('data-type-id');
            var datas = JSON.stringify(dispatchQo);
            $.ajaxJson(url,datas, {
                "done": function (res) {
                    var data = {
                        dispatchId : dispatchQo.dispatchId,
                        zbDispatchHistoryList: res.data,
                        lastDispatchHistory: res.data[res.data.length-1],
                    };
                    jumi.template('zb/review/dispatch_review_dialog',data,function (tpl) {
                        var d = dialog({
                            title: '审核操作',
                            content:tpl,
                            width:1030,
                            height:420,
                            onshow:function () {
                                $("#review-dialog1").show();
                                $("#review-dialog2").hide();
                                var tabul4 = $("#m-tab4 ul li");
                                tabul4.click(function(){
                                    $(this).addClass("z-sel").siblings().removeClass('z-sel');
                                    $('div[name="review-dialog"]').hide().eq($(this).index()).show();
                                });
                                $('#saveReview').click(function(){
                                    var url = ajaxUrl.url1;
                                    $.ajaxJsonPut(url,datas, {
                                        done: function (res) {
                                            if(res.code==0){
                                                d.close().remove();
                                                dialog.get('dispatchDialog').close().remove();
                                                $('.m-memu .two-level-memu.z-sel').trigger('click');
                                            }
                                        }
                                    })
                                });
                            },
                            onremove: function () {
                                jumi.msg('对话框已销毁');
                            }
                        });
                        d.showModal();
                    })
                }
            })
        });
    }
    return {
        init :_init,
    };
})();

