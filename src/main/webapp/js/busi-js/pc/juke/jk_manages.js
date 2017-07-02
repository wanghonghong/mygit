/**
 * 管理模块
 * Created by ray on 17/5/6.
 */

CommonUtils.regNamespace("juke", "manages");

juke.manages.common =(function () {
    var ajaxUrl = {
        url1: CONTEXT_PATH + '/jkmanage/jk_pub_list'//获取公众号列表
    };
    var _initTime = function () {
        $("#startDate2").datetimepicker({
            showSecond : true,
            timeFormat : 'hh:mm:ss',
            dateFormat : 'yy-mm-dd',
            timeOnly:false,
            stepHour : 1,
            stepMinute : 1,
            stepSecond : 1
        });
        $("#endDate2").datetimepicker({
            showSecond : true,
            timeFormat : 'hh:mm:ss',
            dateFormat : 'yy-mm-dd',
            timeOnly:false,
            stepHour : 1,
            stepMinute : 1,
            stepSecond : 1
        });
    }
    var _getPublic = function () {
        var dfdPlay = $.Deferred();
        var wxPubAccountRo = {},tpl='';
        var json = JSON.stringify(wxPubAccountRo);
        $.ajaxJson(ajaxUrl.url1, json, {
            "done": function (res) {
                if (res.code === 0) {
                    var data = res.data.items;
                    _.each(data,function (k,v) {
                       tpl += '<option value="'+k.appid+'">'+k.nickName+'</option>';
                    });
                    $('#publicList').append(tpl);
                    dfdPlay.resolve();
                } else {
                    var dm = new dialogMessage({
                        type: 2,
                        fixed: true,
                        msg: res.data.msg,
                        isAutoDisplay: true,
                        time: 3000
                    });
                    dm.render();
                }
            },
            "fail": function (res) {

            }

        });
        return dfdPlay;
    }
    return {
        getPublic: _getPublic,
        initTime:_initTime
    }
})();
//用户订单模块
juke.manages.order= (function(){
    var ajaxUrl = {
        url1: CONTEXT_PATH + '/jkmanage/jk_order'//查询订单列表
    };
    var _init = function(){

        juke.manages.common.getPublic().then(function(){
            juke.manages.order.initTime();
            _queryList();
        })


    };
    //绑定事件
    var _getSearch = function () {
        _queryList();
    }

    var _queryList = function () {
        var appid = $('#publicList').val();
        var startTime = $("startTime2").val();
        var endTime = $("endTime2").val();
        var status = $("#status").val();
        var activityUserQo = {
            pageSize:10,
            appid:appid,
            startDate:startTime,
            endtDate:endTime,
            status:status
        };
        jumi.pagination('#sendRecordPaperToolbar',ajaxUrl.url1,activityUserQo,function(res,curPage){
            if(res.code===0){
                var data = {
                    items:res.data.items
                };
                jumi.template('jk/jk_manage_order_list',data,function(tpl){
                    $('#send_list').html(tpl);
                })
            }
        })
    }

    var _initTime = function () {
        $("#startTime2").datetimepicker({
            showSecond : true,
            timeFormat : 'hh:mm:ss',
            dateFormat : 'yy-mm-dd',
            timeOnly:false,
            stepHour : 1,
            stepMinute : 1,
            stepSecond : 1
        });
        $("#endTime2").datetimepicker({
            showSecond : true,
            timeFormat : 'hh:mm:ss',
            dateFormat : 'yy-mm-dd',
            timeOnly:false,
            stepHour : 1,
            stepMinute : 1,
            stepSecond : 1
        });
    }
    return{
        init:_init,
        getSearch:_getSearch,
        initTime:_initTime
    };
})();

//充值管理模块
juke.manages.recharge = (function () {
    var ajaxUrl = {
        url1:CONTEXT_PATH+'/jkmanage/recharge_list'//查询充值记录
    };
    var _init = function(){
        _queryList();
        juke.manages.common.initTime();
    };
    //绑定事件
    var _bind = function(){

    };
    var _queryList = function () {
        var appid = $('#publicList').val();
        var startDate = $('#startDate2').val();
        var overDate = $('#endDate2').val();
        var phoneNumber = $('#phoneNumber').val();
        var nickname = $('#nickname').val();
        var integralProductQo = {
            pageSize:10,
            startDate:startDate,
            endtDate:overDate,
            phoneNumber:phoneNumber,
            nickname:nickname
        };
        jumi.pagination('#sendRecordPaperToolbar',ajaxUrl.url1,integralProductQo,function(res,curPage){
            if(res.code===0){
                var data = {
                    items:res.data.items
                },money=0;
                _.each(data.items,function (k,v) {
                    money+=k.money/100;
                })
                $('#cout_j').text(money);
                //计算本页合计金额
                jumi.template('jk/jk_recharge_manage_list',data,function(tpl){
                    $('#send_list').html(tpl);
                })
            }
        })
    }
    return{
        init:_init,
        queryList:_queryList
    };
})();

//商家管理模块
juke.manages.busManagement = (function () {
    var ajaxUrl = {
        url1: CONTEXT_PATH + '/jkmanage/jk_pub_list'//获取公众号列表
    };
    var _init = function(){
        _queryList();
    };
    var _queryList = function () {
        var pubNickName = $('input[name="pubNickName"]').val();
        var wxPubAccountRo = {
            pageSize:10,
            pubNickName:pubNickName
        };
        var tpl = '';
        jumi.pagination('#sendRecordPaperToolbar',ajaxUrl.url1,wxPubAccountRo,function(res,curPage){
            if(res.code===0){
                var data = {
                    items:res.data.items
                };
                jumi.template('jk/jk_wechat_list',data,function(tpl){
                    $('#send_list').html(tpl);
                })
            }
        })

    }
    //绑定事件

    return{
        init:_init,
        queryList:_queryList
    };
})();


