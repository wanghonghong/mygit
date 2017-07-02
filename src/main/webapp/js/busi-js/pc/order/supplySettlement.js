/***<===========================================================供货配置============================================================***/

/***
 * 订单查询
 */

CommonUtils.regNamespace("supplySettlement","list");
supplySettlement.list = (function(){
    var tabindex=0;
    $(".m-tab ul li").click(function () {
        tabindex = $(this).index();
    })
    var ajaxUrl = {
        url1:CONTEXT_PATH+"/order/supply_configure_list/", //供货配置
        url2:CONTEXT_PATH+"/order/query_shopkeeper/", //查看店主信息
        url3:CONTEXT_PATH+"/order/query_supply_user/", //查看供货商信息
        url4:CONTEXT_PATH+"/order/export_supply_order/", //导出订单报表
    };
    var pageparam = [{
        url: ajaxUrl.url1, //初始化订单数据
        pageSize: 10,
        curPage: 0,
        countObj: "count",
        pageToolbarObj: "pageTypeToolbar",
        searchFrom:"searchform1",
        tableBodyObj: "supplySettlementItemList",
        template:"/order/pc/supplySettlementItemList"
    }/*,{
        url: ajaxUrl.url1, //初始化订单数据
        pageSize: 10,
        curPage: 0,
        countObj: "count",
        pageToolbarObj: "pageTypeToolbar1",
        searchFrom:"searchform2",
        tableBodyObj: "supplyConfigureItemList",
        template:"/order/pc/supplyConfigureItemList"
    }*/];

    /*var _band = function(){
        $(".panel-hidden").hide().eq(0).show();
        var tabul = $(".m-tab ul li");
        tabul.click(function(){
            tabul.eq($(this).index()).addClass("z-sel").siblings().removeClass('z-sel');
            $(".panel-hidden").hide().eq($(this).index()).show();
        });
        $(".btn-slide").click(function(){
            $("#m-search").slideToggle("fast");
            $(this).toggleClass("btn-slide1"); return false;
        })
        $(".btn-slide2").click(function(){
            $("#m-search1").slideToggle("fast");
            $(this).toggleClass("btn-slide3"); return false;
        })
        //tab切换
        $(".m-tab ul li").click(function () {
            tabindex = $(this).index();
            _initPagination(pageparam[tabindex]);
        })
    }*/

    //初始化订单数据
    var _init = function(){
        //_band();
        _initPagination(pageparam[0]);
    };

    var _initPagination = function(pageparam){
        var status = $('#s_statusList option:selected').val();
        var supplyForQueryVo = {};
        if(pageparam.searchFrom){
            $("input[type='text'],select","#"+pageparam.searchFrom ).each(function () {
                if( $(this).attr("name")){
                    if($(this).val()){
                        supplyForQueryVo[$(this).attr("name")]=$(this).val();
                    }
                }
            })
        }
        if(Number(status)!=12){
            supplyForQueryVo.status = status;
        }
        jumi.pagination("#"+pageparam.pageToolbarObj,pageparam.url,supplyForQueryVo,function (res,curPage) {
            if (res.code === 0) {
                //判断是否第一页
                var data = {
                    items: res.data.items,
                    ext: res.data.ext,
                };
                jumi.template(pageparam.template, data, function (tpl) {
                    $("#" + pageparam.tableBodyObj).empty();
                    $("#" + pageparam.tableBodyObj).html(tpl);
                    $("#" + pageparam.tableBodyObj + " table ").each(function () {
                        $(this).rowspan([1, 2, 3, 4]);
                    });
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
        orderManageVo.goodStatus = $("#goodStatus").val();
        var newUrl = ajaxUrl.url1;
        $.ajaxJson(newUrl,orderManageVo, {
            "done": function (res) {
                if(res.code===0) {
                    //判断是否第一页
                    var data = {
                        items: res.data.items,
                        ext:res.data.ext
                    };
                    var template = "/order/pc/supplySettlementItemList";
                    var tableBodyObj = "supplySettlementItemList";
                    jumi.template(template, data, function (tpl) {
                        $("#" + tableBodyObj).empty();
                        $("#" + tableBodyObj).html(tpl);
                        $("#supply_settlement").show();
                        $("#supply_settlement_content").empty();
                        $("#" + tableBodyObj + " table ").each(function () {
                            $(this).rowspan([1, 2, 3, 4]);
                        });
                    })
                }
            }
        })
    }

    //订单条件查询
    var _query = function(){
        _initPagination(pageparam[0]);
    };

    //查看店主信息
    var _queryShop = function(shopId){
        var url = ajaxUrl.url2+shopId;
        $.ajaxJsonGet(url,null,{
            "done":function (res) {
                if(res.code===0){
                    _openShop(res.data);
                }
            }
        });
    }

    //打开店主信息页面
    var _openShop=function (data) {
        var data = {
            data:data,
        };
        var html = jumi.templateHtml('shopkeeper.html',data,STATIC_URL+"/tpl/order/pc/");
        var titleStr="店主详情";
        dialog({
            content: html,
            title: titleStr,
            cancelValue:'关闭',
            cancel:function () {
            }
        }).width(400).showModal();
    }

    var _queryUser = function(userId){
        var url = ajaxUrl.url3+userId;
        $.ajaxJsonGet(url,null,{
            "done":function (res) {
                if(res.code===0){
                    _openUser(res.data);
                }
            }
        });
    }

    //打开店主信息页面
    var _openUser=function (data) {
        var data = {
            data:data,
        };
        var html = jumi.templateHtml('userDetail.html',data,STATIC_URL+"/tpl/order/pc/");
        var titleStr="供货商详情";
        dialog({
            content: html,
            title: titleStr,
            cancelValue:'关闭',
            cancel:function () {
            }
        }).width(400).showModal();
    }

    var _export = function(){
        var status = $('#s_statusList option:selected').val();
        var supplyForQueryVo = {};
        if(pageparam.searchFrom){
            $("input[type='text'],select","#"+pageparam.searchFrom ).each(function () {
                if( $(this).attr("name")){
                    if($(this).val()){
                        supplyForQueryVo[$(this).attr("name")]=$(this).val();
                    }
                }
            })
        }
        if(Number(status)!=12){
            supplyForQueryVo.status = status;
        }
        var jsonStr = JSON.stringify(supplyForQueryVo);
        window.location.href =ajaxUrl.url4+"?supplyForQueryVo="+jsonStr;
    }

    return {
        init :_init, //初始化供货结算数据
        query:_query,//订单条件查询
        initPagination:_initPagination, //订单分页查询列表
        queryShop:_queryShop, //查看店铺信息
        queryUser:_queryUser, //查看供货商信息
        exportSupply:_export, //导出订单报表
    };
})();