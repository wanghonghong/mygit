/**
 * Created by BenRay on 16/8/8.
 * 官方图文
 */
CommonUtils.regNamespace("shop", "review");
shop.review = (function(){
    var ajaxUrl = {
        url1:CONTEXT_PATH+'/zb/shop_list',
        url2:CONTEXT_PATH+'/zb/shop_set',
    };
    var _init = function(){
        _tab();
        _bind();
    };

    var _tab = function () {
        $(".m-jm-table").hide().eq(0).show();
        var tabul = $("#m-tab ul li");
        tabul.click(function(){
            tabul.eq($(this).index()).addClass("z-sel").siblings().removeClass('z-sel');
            $(".m-jm-table").hide().eq($(this).index()).show();
        });
        $(".dialog").click(function() {
            var elem = document.getElementById('dialoginfo');
            dialog({
                title: "审核操作",
                content: elem,
            }).width(890).showModal();

        });
        $(".panel-hidden").hide().eq(0).show();
        var tabul1 = $("#m-tab1 ul li");
        tabul1.click(function(){
            tabul1.eq($(this).index()).addClass("z-sel").siblings().removeClass('z-sel');
            $(".panel-hidden").hide().eq($(this).index()).show();
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

    var _bind = function(){
        var url = ajaxUrl.url1;
        var params = {
            pageSize: 10,
            shopStatus:0,
        };
        jumi.pagination('#shopToolbar', url, params, function (res, curPage) {
            if (res.code === 0) {
                var data = {
                    items: res.data.items
                };
                jumi.template('zb/review/shop_review_list', data, function (tpl) {
                    $('#shopList').html(tpl);
                })
            }
        });

        $("#platform").select2({
            theme: "jumi"
        });
        $("#status").select2({
            theme: "jumi"
        });

        _timeTimepicker();

        //点击查询
        $('#search-btn').click( function(){
            if($("#startTime").val()){
                var startTime = $("#startTime").val()+" 00:00:00";
            }
            if ($("#endTime").val()){
                var endTime =  $("#endTime").val()+" 23:59:59";
            }
            var shopStatus = $('#status').val();
            var userName = $("#userName").val();
            var phoneNumber = $("#phoneNumber").val();
            var shopName = $("#shopName").val();
            var params = {
                pageSize:10,
                starTime:startTime,
                endTime:endTime,
                userName : userName,
                phoneNumber : phoneNumber,
                shopName:shopName,
                shopStatus:shopStatus,
            };
            jumi.pagination('#shopToolbar', url, params, function (res, curPage) {
                if (res.code === 0) {
                    var data = {
                        items: res.data.items
                    };
                    jumi.template('zb/review/shop_review_list', data, function (tpl) {
                        $('#shopList').html(tpl);
                    })
                }
            });
        });


    }
    var _closeShop = function (shopId,shopName) {
        var args = {};
        var url = ajaxUrl.url2;
        var shopUo = {};
        shopUo.shopId = shopId;
        shopUo.shopStatus = 1;
        var data = JSON.stringify(shopUo);
        args.fn1 = function(){
            $.ajaxJson(url,data,{
                "done":function (res) {
                    if(res.code===0) {
                        $('.one-level-memu.z-sel').trigger('click');
                    }
                }
            })
        };
        args.fn2 = function(){
        };
        jumi.dialogSure('是否确认关停店铺-'+shopName+'?',args);
    }
    var _openShop = function (shopId,shopName) {
        var args = {};
        var url = ajaxUrl.url2;
        var shopUo = {};
        shopUo.shopId = shopId;
        shopUo.shopStatus = 0;
        var data = JSON.stringify(shopUo);
        args.fn1 = function(){
            $.ajaxJson(url,data,{
                "done":function (res) {
                    if(res.code===0) {
                        $('.one-level-memu.z-sel').trigger('click');
                    }
                }
            })
        };
        args.fn2 = function(){
        };
        jumi.dialogSure('是否确认开通店铺-'+shopName+'?',args);
    }
    return {
        init :_init,
        closeShop:_closeShop,
        openShop:_openShop,
    };
})();

