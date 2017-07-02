CommonUtils.regNamespace("channel");

channel=(function(){
    var pageSize=10;
    var curPage=0;
    var _init=function () {
        _bindEvent();
        _initPagination();
    }
    var _bindEvent = function () {
        $("#startDate").datetimepicker({ timeFormat:'hh:mm:ss'  });
        $("#endDate").datetimepicker({ timeFormat:'hh:mm:ss'  });
        $("#agentRole").select2({
            theme: "jumi"
        });
        $("#platForm").select2({
            theme: "jumi"
        });
        $(".btn-slide").click(function(){
            $("#m-search").slideToggle("fast");
            $(this).toggleClass("btn-slide1");
            return false;
        })
    }
    var _initPagination = function(){
        var url = CONTEXT_PATH+"/brokerage/channel"
        var channelRecordQo = {};
        channelRecordQo.pageSize=pageSize;
        channelRecordQo.curPage=curPage;
        channelRecordQo.phoneNumber = $("#phoneNumber").val();
        channelRecordQo.userName= $("#userName").val();
        channelRecordQo.agentRole = $("#agentRole").val();
        channelRecordQo.nickname= $("#nickname").val();
        channelRecordQo.startDate = $("#startDate").val();
        channelRecordQo.endDate= $("#endDate").val();
        var jsonStr = JSON.stringify(channelRecordQo);
        jumi.pagination("#pageToolbar",url,channelRecordQo,function (res,curPage) {
            if(res.code===0){
                //判断是否第一页
                var data = {
                    items:res.data.items,
                };
                if(curPage===0){
                    data.isFirstPage = 1;
                }else{
                    data.isFirstPage = 0;
                }
                curPage=curPage;
                jumi.template("shop/distribution/brokerage/channel_item",data,function(tpl){
                    $("#tableBody").empty();
                    $("#tableBody").html(tpl);
                })
            }
        })
    }
    return {
        init:_init,
        search:_initPagination
    };
})();
