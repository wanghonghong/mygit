/**
 * Created by BenRay on 16/11/22.
 * 商家类
 */
CommonUtils.regNamespace("business", "class");
business.class = (function(){
    var ajaxUrl = {
        url1:CONTEXT_PATH+'/zb/business_class/list',
        url2:CONTEXT_PATH+'/zb/business_class/manage_list',
        url3:CONTEXT_PATH+'/zb/business_class/shop_list',
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
        $("#area_").select2({
            theme: "jumi"
        });
        $("#sex_").select2({
            theme: "jumi"
        });
        $("#role_").select2({
            theme: "jumi"
        });

        var url = ajaxUrl.url1;
        var params = {
            pageSize: 10,
        };
        jumi.pagination('#businessToolbar', url, params, function (res, curPage) {
            if (res.code === 0) {
                var data = {
                    items: res.data.items
                };
                jumi.template('zb/user/business_class_list', data, function (tpl) {
                    $('#businessList').html(tpl);
                })
            }
        });
        $(".dialog-see").click(function() {
            var elem = document.getElementById('dialoginfo-see');
            dialog({
                title: "查看开店状态",
                content: elem,
            }).width(760).showModal();

        });
        $(".dialog-see2").click(function() {
            var elem = document.getElementById('dialoginfo-see2');
            dialog({
                title: "查看经营状态",
                content: elem,
            }).width(1150).showModal();

        });

        $('#search_btn').click( function(){
            if($("#startTime").val()){
                var startTime = $("#startTime").val()+" 00:00:00";
            }
            if ($("#endTime").val()){
                var endTime =  $("#endTime").val()+" 23:59:59";
            }
            var userName = $("#userName_").val();
            var phoneNumber = $("#phoneNumber_").val();
            var nikeName = $("#nikeName_").val();
            var agent = $("#agent_").val();
            var area = $('#area_').find('option:selected').val();
            var sex = $('#sex_').find('option:selected').val();
            var role = $('#role_').find('option:selected').val();
            var params = {
                pageSize:10,
                starTime:startTime,
                endTime:endTime,
                userName : userName,
                phoneNumber : phoneNumber,
                nikeName : nikeName,
                sex : sex,
                agent : agent,
                role:role,
                area:area,
            };
            jumi.pagination('#businessToolbar', url, params, function (res, curPage) {
                if (res.code === 0) {
                    var data = {
                        items: res.data.items
                    };
                    jumi.template('zb/user/business_class_list', data, function (tpl) {
                        $('#businessList').html(tpl);
                    })
                }
            });
        });
        //点击开店状态查看
        $('#businessList').on('click','.searchShop',function(){
            var self = $(this);
            var userId = self.attr('data-id');
            var params = {
                pageSize:4,
                userId:userId,
            };
            var url = ajaxUrl.url3;
            var data = {};
            data.globalUrl = THIRD_URL;
            jumi.template('zb/user/business_shop_dialog',data,function (tpl) {
                var d = dialog({
                    title: '开店状态',
                    content: tpl,
                    width: 760,
                    height:440,
                    onshow: function () {
                        jumi.pagination('#shopToolbar', url, params, function (res, curPage) {
                            if (res.code === 0) {
                                var data = {
                                    items: res.data.items
                                };
                                jumi.template('zb/user/business_shop_list', data, function (tpl) {
                                    $('#shopList').html(tpl);
                                })
                            }
                        });
                    }
                }).showModal();
            })
        });
        //点击经营状态查看
        $('#businessList').on('click','.searchManage',function(){
            var self = $(this);
            var userId = self.attr('data-id');
            var params = {
                pageSize:4,
                userId:userId,
            };
            var url = ajaxUrl.url2;
            var data = {};
            data.globalUrl = THIRD_URL;
            jumi.template('zb/user/business_manage_dialog',data,function (tpl) {
                var d = dialog({
                    title: '经营状态',
                    content: tpl,
                    width: 1150,
                    height:440,
                    onshow: function () {
                        jumi.pagination('#manageToolbar', url, params, function (res, curPage) {
                            if (res.code === 0) {
                                var data = {
                                    items: res.data.items
                                };
                                jumi.template('zb/user/business_manage_list', data, function (tpl) {
                                    $('#manageList').html(tpl);
                                })
                            }
                        });
                    }
                }).showModal();
            })
        });
    }
    return {
        init :_init
    };
})();

