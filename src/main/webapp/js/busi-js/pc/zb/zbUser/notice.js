/**
 * Created by BenRay on 16/8/8.
 * 官方图文
 */
CommonUtils.regNamespace("notice", "search");
notice.search = (function(){
    var ajaxUrl = {
        url1:CONTEXT_PATH+'/zb/notice/',
        url2:CONTEXT_PATH+'/zb/notice/list',
    };
    var opt={
        notice:{}
    }
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
        $("#type").select2({
            theme: "jumi"
        });

        _timeTimepicker();
        getNoticePage();

        $('#search-btn').click( function(){
            var type = $('#type').find('option:selected').val();
            if($("#startTime").val()){
                var startTime = $("#startTime").val()+" 00:00:00";
            }
            if ($("#endTime").val()){
                var endTime =  $("#endTime").val()+" 23:59:59";
            }
            var params = {
                pageSize:10,
                status:2,
                type:type,
                startTime : startTime,
                endTime : endTime,
                bigType : 1,
                lookType:1,
            };
            var url = ajaxUrl.url2;
            jumi.pagination('#noticeToolbar',url,params,function(res,curPage){
                if(res.code===0){
                    var data = {
                        items:res.data.items
                    };
                    jumi.template('zb/user/notice_list',data,function(tpl){
                        $('#noticeList').html(tpl);
                    })
                }
            })
        });

        $('#noticeList').on('click','li[id^="notice_"]',function() {
            var id = $(this).data('id');
            var url = ajaxUrl.url1+"/"+id;
            $.ajaxJsonGet(url,null, {
                "done": function (res) {
                    var data = {
                        item: res.data,
                    };
                    jumi.template('zb/user/notice_dialog',data,function (tpl) {
                        var d = dialog({
                            title: '系统公告',
                            content: tpl,
                            width: 830,
                            onshow: function () {

                            }
                        }).showModal();
                    })
                }
            })
        }
        )

    }
    var getNoticePage = function () {
        var url = ajaxUrl.url2;
        var params = {
            pageSize:10,
            status:2,
            bigType : 1,
            lookType:1,
        };
        jumi.pagination('#noticeToolbar',url,params,function(res,curPage){
            if(res.code===0){
                var data = {
                    items:res.data.items
                };
                jumi.template('zb/user/notice_list',data,function(tpl){
                    $('#noticeList').html(tpl);
                })
            }
        })
    }
    return {
        init :_init
    };
})();

