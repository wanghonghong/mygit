/**
 * Created by BenRay on 16/8/8.
 * 官方图文
 */
CommonUtils.regNamespace("soft", "test");
soft.test = (function(){
    var ajaxUrl = {
        url1:CONTEXT_PATH+'/zb/shop_list',

    };
    var _init = function(){
        _bind();
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
        $("#platform").select2({
            theme: "jumi"
        });
        $("#status").select2({
            theme: "jumi"
        });
        _timeTimepicker();

        var url = ajaxUrl.url1;
        var params = {
            pageSize: 10,
        };
        jumi.pagination('#softToolbar', url, params, function (res, curPage) {
            if (res.code === 0) {
                var data = {
                    items: res.data.items
                };
                jumi.template('zb/review/soft_test_list', data, function (tpl) {
                    $('#softList').html(tpl);
                    $(".dialog-use").click(function() {
                        var elem = document.getElementById('dialoginfo-use');
                        dialog({
                            title: "软件授权",
                            content: elem,
                        }).width(760).showModal();
                    });
                })
            }
        });
        //点击查询
        $('#search-btn').click( function(){
            if($("#startTime").val()){
                var startTime = $("#startTime").val()+" 00:00:00";
            }
            if ($("#endTime").val()){
                var endTime =  $("#endTime").val()+" 23:59:59";
            }
            var userName = $("#userName").val();
            var phoneNumber = $("#phoneNumber").val();
            var shopName = $("#shopName").val();
            var params = {
                pageSize:10,
                starTime:startTime,
                endTime:endTime,
                userName : userName,
                phoneNumber : phoneNumber,
                shopName:shopName
            };
            jumi.pagination('#softToolbar', url, params, function (res, curPage) {
                if (res.code === 0) {
                    var data = {
                        items: res.data.items
                    };
                    jumi.template('zb/review/soft_test_list', data, function (tpl) {
                        $('#softList').html(tpl);
                    })
                }
            });
        });


    }
    return {
        init :_init,
    };
})();

