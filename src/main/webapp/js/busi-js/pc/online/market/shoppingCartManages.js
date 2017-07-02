/***<===========================================================订单管理============================================================***/

/***
 * 客服营销下购物车查询
 */

CommonUtils.regNamespace("shoppingCart","list");
shoppingCart.list = (function(){
    var tabindex=0;
    var ajaxUrl = {
        url1:CONTEXT_PATH+"/shopCart/list/", //初始化购物车数据
    };
    var pageparam = [{
        url: ajaxUrl.url1, //初始化订单数据
        pageSize: 10,
        curPage: 0,
        countObj: "count",
        pageToolbarObj: "pageTypeToolbar",
        tableBodyObj: "shoppingCartList",
        template:"/online/market/shoppingCartList"
    }];

    //初始化数据
    var _init = function(){
        _initPagination(pageparam[0]);
    };

    var _initPagination = function(pageparam){
        var type = $("#type").val();
        var shoppingCartManageVo = {};
        shoppingCartManageVo.type = type;
        shoppingCartManageVo.phoneNumber = $("#phoneNumber").val();
        shoppingCartManageVo.userName = $("#userName").val();
        shoppingCartManageVo.nickname = $("#nickname").val();
        shoppingCartManageVo.beginDate = $("#beginDate").val();
        shoppingCartManageVo.endDate = $("#endDate").val();
        jumi.pagination("#"+pageparam.pageToolbarObj,pageparam.url,shoppingCartManageVo,function (res,curPage) {
            if (res.code === 0) {
                //判断是否第一页
                var data = {
                    items: res.data.items,
                    ext: res.data.ext
                };
                jumi.template(pageparam.template, data, function (tpl) {
                    $("#" + pageparam.tableBodyObj).empty();
                    $("#" + pageparam.tableBodyObj).html(tpl);
                   /* $("#" + pageparam.tableBodyObj + " table ").each(function () {
                        $(this).rowspan([1, 2, 3, 4]);
                    });*/
                })
            }
        })
    }

    //条件查询
    var _query = function(){
        _initPagination(pageparam[tabindex]);
    };

    return {
        init :_init, //初始化
        query:_query,//条件查询
        initPagination:_initPagination, //分页查询列表
    };
})();