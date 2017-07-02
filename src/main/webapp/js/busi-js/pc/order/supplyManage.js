/***<===========================================================发货管理============================================================***/

/***
 * 订单查询
 */

CommonUtils.regNamespace("supply","list");
supply.list = (function(){
    var tabindex=0;
    var ajaxUrl = {
        url1:CONTEXT_PATH+"/order/order_supply_list/", //初始化订单数据
        url2:CONTEXT_PATH+"/order/order_detail/", //打开立即发货页面
        url3:CONTEXT_PATH+"/orderDelivery/delivery_list/", //打开物流备注页面
        url4:CONTEXT_PATH+"/orderDelivery/order_delivery/", //提交发货信息
        url5:CONTEXT_PATH+"/orderDelivery/save_send_note/", //保存物流备注信息
        url6:CONTEXT_PATH+"/send_company_kd100/supply_send_list/",
        url7:CONTEXT_PATH+"/order/query_supply_orders/", //查看订单详情
        url8:CONTEXT_PATH+"/order/query_shopkeeper/", //查看店主信息
        url9:CONTEXT_PATH+"/order/supply_configure_list/", //供货配置
        url10:CONTEXT_PATH+"/order/export_supply/", //导出订单报表
    };
    var pageparam = [{
        url: ajaxUrl.url1, //初始化订单数据
        pageSize: 10,
        curPage: 0,
        countObj: "count",
        pageToolbarObj: "pageTypeToolbar",
        tableBodyObj: "supplyItemList",
        template:"/order/pc/supplyItemList"
    }];

    //初始化订单数据
    var _init = function(){
        _initPagination(pageparam[0]);
    };

    var _initPagination = function(pageparam){
        var status = $("#status").val();
        var orderManageVo = {};
        orderManageVo.status = status;
        orderManageVo.consigneePhone = $("#consigneePhone").val();
        orderManageVo.consigneeName = $("#consigneeName").val();
        orderManageVo.nickname = $("#nickname").val();
        orderManageVo.orderNum = $("#orderNum").val();
        orderManageVo.orderBeginDate = $("#supplyBeginDate").val();
        orderManageVo.orderEndDate = $("#supplyEndDate").val();
        orderManageVo.goodStatus = $("#goodStatus").val();
        var platform1 = $('#select1 option:selected').val();
        var platform2 = $('#select2 option:selected').val();
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
        var newUrl = CONTEXT_PATH+"/order/order_supply_list/";
        $.ajaxJson(newUrl,orderManageVo, {
            "done": function (res) {
                if(res.code===0) {
                    //判断是否第一页
                    var data = {
                        items: res.data.items,
                        ext:res.data.ext
                    };
                    var template = "/order/pc/supplyItemList";
                    var tableBodyObj = "supplyItemList";
                    jumi.template(template, data, function (tpl) {
                        $("#" + tableBodyObj).empty();
                        $("#" + tableBodyObj).html(tpl);
                        $("#supply_detatil").show();
                        $("#supply_detail_content").empty();
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
        _initPagination(pageparam[tabindex]);
    };


    var _deliver = function(orderInfoId,el){
        var remark =$(el).attr("remark");
        var sellerNote=$(el).attr("sellerNote");
        var url = ajaxUrl.url6+orderInfoId;
        $.ajaxJsonGet(url,null,{
            "done":function (res) {
                if(res.code==0){
                    _openDeliver(orderInfoId,remark,sellerNote,res.data);
                }
            }
        });
    }

    var _openDeliver = function(orderInfoId,remark,sellerNote,data){
        $("#supply_detatil").hide();
        var data = {
            orderInfoId:orderInfoId,
            remark:remark,
            sellerNote:sellerNote,
            orderDetails:data.orderDetails,
            companys:data.sendCompanysKd100s,
        };
        console.log('productList',data.orderDetails);
        var tpl = "/order/pc/immediateSupplyDialog"
        jumi.template(tpl,data,function(html){
            $("#supply_detail_content").show();
            $("#supply_detail_content").html(html);
            delivery.packages.reset();
            delivery.packages.initData(data);
            data.first = true;
            $('.addPackages').bind('click',addPackage);
            addPackage(data);
            $('.sendAll').bind('click',sendPackages);
            $('.send').bind('click',sendPackages);
        });
    };

    var sendPackages = function(){
        var packs = delivery.packages.savePackage();
        if(packs.orderDetailAndSendsVos.length<1){
            var dm = new dialogMessage({
                type:2,
                fixed:true,
                msg:'未勾选任何包裹',
                isAutoDisplay:true,
                time:3000
            });
            dm.render();
            return;
        }
        var path = CONTEXT_PATH + '/orderDelivery/order_delivery/'+packs.orderInfoId;
        var param = JSON.stringify(packs);
        $.ajaxJson(path,param,{
            done:function(res){
                _refreshPage();
            }
        });
    };

    var addPackage = function(data){
        var pack = new delivery.packages.Package(data);
        pack.addPackage();
    };

    var _sendNote = function(orderInfoId){
        var url = ajaxUrl.url3+orderInfoId;
        $.ajaxJsonGet(url,null,{
            "done":function (res) {
                if(res.code===0){
                    _openSendNote(res.data);
                }
            }
        });
    }

    var _openSendNote = function(data){
        var data = {
            data:data
        };
        var html = jumi.templateHtml('sendNote.html',data,STATIC_URL+"/tpl/order/pc/");
        var titleStr="物流备注";
        dialog({
            content: html,
            title: titleStr,
        }).width(400).showModal();
    }

    //校验物流备注的提交内容
    var _validate=function(){
        var form=$("#sendNoteForm");
        form.validate({
            rules: {
                deliveryNote:{
                    required: true,
                    maxlength:150
                }
            },
            messages: {
                deliveryNote:{
                    required: '请输入备注内容',
                    maxlength:'备注内容不能超过150个字符'
                }
            }
        });
        return form;
    }

    var _saveSendNote = function(orderDeliveryId){
        var orderVo={}
        $("#sendNoteForm").find("input,textarea").each(function () {
            orderVo[$(this).prop("name")]=$(this).val();
        })
        var url=ajaxUrl.url5+orderDeliveryId;
        $.ajaxJsonPut(url,orderVo,{
            "done":function(res){
                if(res.code==0){
                    _refreshPage();
                    var dm = new dialogMessage({
                        fixed:true,
                        msg:'保存物流备注成功！',
                        isAutoDisplay:true,
                        time:1500
                    });
                    dm.render();
                }else{
                    var dm = new dialogMessage({
                        fixed:true,
                        msg:'保存物流备注失败！',
                        isAutoDisplay:true,
                        time:1500
                    });
                    dm.render();
                }
            }
        });
    }


    //查看订单详情
    var _queryOrderDetail = function(orderInfoId){
        var url = ajaxUrl.url7+orderInfoId;
        $.ajaxJsonGet(url,null,{
            "done":function (res) {
                if(res.code===0){
                    _openOrderDetail(res.data);
                }
            }
        });
    }

    //打开订单详情页面
    var _openOrderDetail=function (data) {
        $("#supply_detatil").hide();
        var tpl = "/order/pc/supplyDetails";
        var data = {
            data:data,
        };
        jumi.template(tpl,data,function(html){
            $("#supply_detail_content").show();
            $("#supply_detail_content").html(html);
        })
    }

    //查看店主信息
    var _queryShop = function(shopId){
        var url = ajaxUrl.url8+shopId;
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

    var _querySend = function(orderInfoId){
        var url = ajaxUrl.url7+orderInfoId;
        $.ajaxJsonGet(url,null,{
            "done":function (res) {
                if(res.data.code===0){
                    _openSendInformation(res.data.data);
                }else{
                    var dm = new dialogMessage({
                        type:2,
                        fixed:true,
                        msg:'暂无物流信息',
                        isAutoDisplay:true,
                        time:2000
                    });
                    dm.render();
                }
            }
        });
    }

    var _openSendInformation = function(data){
        var data = {
            url:data
        };
        var html = jumi.templateHtml('sendInformation.html',data,STATIC_URL+"/tpl/order/pc/");
        var titleStr="查看物流";
        dialog({
            content: html,
            title: titleStr,
            id:'sendInformationId',
        }).width(575).showModal();
    }

    var _export = function(){
        var status = $("#status").val();
        var orderManageVo = {};
        orderManageVo.status = status;
        orderManageVo.consigneePhone = $("#consigneePhone").val();
        orderManageVo.consigneeName = $("#consigneeName").val();
        orderManageVo.nickname = $("#nickname").val();
        orderManageVo.orderNum = $("#orderNum").val();
        orderManageVo.orderBeginDate = $("#supplyBeginDate").val();
        orderManageVo.orderEndDate = $("#supplyEndDate").val();
        orderManageVo.goodStatus = $("#goodStatus").val();
        var jsonStr = JSON.stringify(orderManageVo);
        window.location.href =ajaxUrl.url10+"?orderManageVo="+jsonStr;
    }

    return {
        init :_init, //初始化订单数据
        query:_query,//订单条件查询
        initPagination:_initPagination, //订单分页查询列表
        deliver:_deliver, //打开发货页面
        sendNote:_sendNote, //打开物流备注
        querySend:_querySend,//查看物流
        queryOrderDetail:_queryOrderDetail, //查看订单详情
        queryShop:_queryShop, //查看店铺信息
        exportSupply:_export, //导出订单报表
    };
})();