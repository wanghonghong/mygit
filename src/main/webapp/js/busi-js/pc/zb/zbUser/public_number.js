
CommonUtils.regNamespace("gzh", "number");
gzh.number = (function(){
    var ajaxUrl = {
        url1:CONTEXT_PATH+'/zb/public_number/list',
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
        $("#role").select2({
            theme: "jumi"
        });

        var url = ajaxUrl.url1;
        var params = {
            pageSize: 10,
        };
        jumi.pagination('#publicToolbar', url, params, function (res, curPage) {
            if (res.code === 0) {
                var data = {
                    items: res.data.items
                };
                jumi.template('zb/user/public_number_list', data, function (tpl) {
                    $('#publicList').html(tpl);
                })
            }
        });
        $('#search-btn').click( function(){
            if($("#startTime").val()){
                var startTime = $("#startTime").val()+" 00:00:00";
            }
            if ($("#endTime").val()){
                var endTime =  $("#endTime").val()+" 23:59:59";
            }
            var userName = $("#userName").val();
            var phoneNumber = $("#phoneNumber").val();
            var nickname = $("#nickname").val();
            var area = $('#area').find('option:selected').val();
            var sex = $('#sex').find('option:selected').val();
            var role = $('#role').find('option:selected').val();
            var params = {
                pageSize:10,
                starTime:startTime,
                endTime:endTime,
                userName : userName,
                phoneNumber : phoneNumber,
                nickname : nickname,
                sex : sex,
                role:role,
                area:area,
                role:role,
            };
            jumi.pagination('#publicToolbar', url, params, function (res, curPage) {
                if (res.code === 0) {
                    var data = {
                        items: res.data.items
                    };
                    jumi.template('zb/user/public_number_list', data, function (tpl) {
                        $('#publicList').html(tpl);
                    })
                }
            });
        });

    }
    return {
        init :_init
    };
})();

