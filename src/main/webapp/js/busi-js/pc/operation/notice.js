/**
 * Created by whh on 2016/12/15.
 */
CommonUtils.regNamespace("agent", "notice");
agent.notice = (function() {
    var ajaxUrl = {
        url1: CONTEXT_PATH + '/join/notices',
        url2: CONTEXT_PATH + '/join/notice',
    };
    var _init = function () {
        _bind();
    };
    var _bind = function(){
        $(".btn-slide").click(function(){
            $("#m-search").slideToggle("fast");
            $(this).toggleClass("btn-slide1"); return false;
        });
        getNoticePage();

    }
    //派单列表
    var getNoticePage = function () {
        var url = ajaxUrl.url1;
        var params = {
            pageSize:10,
            lookType:2,
            status:2,
            departments:"[1]",
        };
        jumi.pagination('#noticeToolbar',url,params,function(res,curPage){
            if(res.code===0){
                var data = {
                    items:res.data.items,
                };
                jumi.template('software_agent/notice_list',data,function(tpl){
                    $('#noticeList').html(tpl);
                })
            }
        })
    }
    var _getNoticeContext = function (id) {
        var data = {};
        data.id = id;
        var jsonData = JSON.stringify(data);
        var url = ajaxUrl.url2;
        $.ajaxJson(url,jsonData, {
            "done": function (res) {
                var data = {
                    item: res.data,
                };
                jumi.template('software_agent/notice_dialog',data,function (tpl) {
                    var d = dialog({
                        title: '系统公告',
                        content: tpl,
                        width: 800,
                        onshow: function () {
                        }
                    }).showModal();
                })
            }
        })
    }
    return {
        init :_init,
        getNoticeContext:_getNoticeContext,
    };
})();