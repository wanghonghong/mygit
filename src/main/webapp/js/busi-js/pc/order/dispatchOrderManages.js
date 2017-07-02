/***<===========================================================订单管理============================================================***/

/***
 * 订单查询
 */

CommonUtils.regNamespace("dispatchOrder","list");
dispatchOrder.list = (function(){
    var tabindex=0;
    var ajaxUrl = {
        url1:CONTEXT_PATH+"/order/dispatch_order_list/", //初始化订单数据
        url2:CONTEXT_PATH+"/order/export_order/", //导出订单报表
    };
    var pageparam = [{
        url: ajaxUrl.url1, //初始化订单数据
        pageSize: 10,
        curPage: 0,
        countObj: "count",
        pageToolbarObj: "pageTypeToolbar",
        tableBodyObj: "dispatchOrderItemList",
        template:"/order/pc/dispatchOrderItemList"
    }];

    //初始化数据
    var _init = function(){
        debugger;
        _initPagination(pageparam[0]);
    };

    var _initPagination = function(pageparam){
        debugger;
        var status = $("#status").val();
        if(status==11){
            status = $('#orderStatus option:selected').val();
            if(!status || status==-1){
                status = 11;
            }
        }
        var orderManageVo = {};
        orderManageVo.consigneePhone = $("#consigneePhone").val();
        orderManageVo.consigneeName = $("#consigneeName").val();
        orderManageVo.nickname = $("#nickname").val();
        orderManageVo.orderNum = $("#orderNum").val();
        orderManageVo.transactionId = $("#transactionId").val();
        orderManageVo.status = status;
        orderManageVo.orderBeginDate = $("#orderBeginDate").val();
        orderManageVo.orderEndDate = $("#orderEndDate").val();
        orderManageVo.goodStatus = $("#goodStatus").val();
        orderManageVo.refundId = $("#refundId").val();
        var platform1 = $('#select1 option:selected').val();
        var platform3 = $('#select3 option:selected').val();
        jumi.pagination("#"+pageparam.pageToolbarObj,pageparam.url,orderManageVo,function (res,curPage) {
            if (res.code === 0) {
                //判断是否第一页
                var data = {
                    items: res.data.items,
                    ext: res.data.ext
                };
                jumi.template(pageparam.template, data, function (tpl) {
                    $("#" + pageparam.tableBodyObj).empty();
                    $("#" + pageparam.tableBodyObj).html(tpl);
                })
            }
        })
    }

    //刷新当前页
    var _refreshPage = function(){
        var pageTypeToolbar_page = $("#pageTypeToolbar_page").val();
        var orderManageVo = {};
        orderManageVo.curPage = pageTypeToolbar_page;
        orderManageVo.pageSize = 10;
        orderManageVo.status = $("#status").val();
        var newUrl = CONTEXT_PATH+"/order/dispatch_order_list/";
        $.ajaxJson(newUrl,orderManageVo, {
            "done": function (res) {
                if(res.code===0) {
                    //判断是否第一页
                    var data = {
                        items: res.data.items,
                        ext:res.data.ext
                    };
                    var template = "/order/pc/dispatchOrderItemList";
                    var tableBodyObj = "dispatchOrderItemList";
                    jumi.template(template, data, function (tpl) {
                        $("#" + tableBodyObj).empty();
                        $("#" + tableBodyObj).html(tpl);
                    })
                }
            }
        })
    }

    //条件查询
    var _query = function(){
        _initPagination(pageparam[tabindex]);
    };

    var _export = function(){
        var orderManageVo = {};
        var status = $("#status").val();
        orderManageVo.consigneePhone = $("#consigneePhone").val();
        orderManageVo.consigneeName = $("#consigneeName").val();
        orderManageVo.nickname = $("#nickname").val();
        orderManageVo.orderNum = $("#orderNum").val();
        orderManageVo.transactionId = $("#transactionId").val();
        orderManageVo.status = status;
        orderManageVo.orderBeginDate = $("#orderBeginDate").val();
        orderManageVo.orderEndDate = $("#orderEndDate").val();
        orderManageVo.goodStatus = $("#goodStatus").val();
        orderManageVo.refundId = $("#refundId").val();
        var platform1 = $('#select1 option:selected').val();
        var platform3 = $('#select3 option:selected').val();
        var jsonStr = JSON.stringify(orderManageVo);
        window.location.href =ajaxUrl.url12+"?orderManageVo="+jsonStr;
    }

    return {
        init :_init, //初始化
        query:_query,//条件查询
        initPagination:_initPagination, //分页查询列表
        refreshPage:_refreshPage, //刷新当前页
        export:_export, //导出订单报表
    };
})();