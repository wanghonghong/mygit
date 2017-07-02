/***<===========================================================退货管理============================================================***/

/***
 * 退货订单查询
 */

CommonUtils.regNamespace("refundGood","list");
refundGood.list = (function(){
    var tabindex=0;
    var ajaxUrl = {
        url1:CONTEXT_PATH+"/order/order_info_list/1", //查看退货信息
        url2:CONTEXT_PATH+"/refundGood/good_list/", //入库备注信息
        url3:CONTEXT_PATH+"/refundGood/save_storage_note/", //保存入库备注
        url4:CONTEXT_PATH+"/refundGood/refund_update/", //确认收货
        url5:CONTEXT_PATH+"/send_company_kd100/refundGoodSend/",//打开查看物流页面
        url6:CONTEXT_PATH+"/order/export_delivery/", //导出订单报表
    };
    var pageparam = [{
        url: ajaxUrl.url1, //初始化数据
        pageSize: 10,
        curPage: 0,
        countObj: "count",
        pageToolbarObj: "pageTypeToolbar1",
        tableBodyObj: "refundGoodItemList",
        template:"/order/pc/refundGoodItemList"
    }];

    //初始化订单数据
    var _init = function(){
        _initPagination(pageparam[0]);
    };

    var _initPagination = function(pageparam){
        var orderManageVo = {};
        orderManageVo.consigneePhone = $("#consigneePhone").val();
        orderManageVo.consigneeName = $("#consigneeName").val();
        orderManageVo.nickname = $("#nickname").val();
        orderManageVo.orderNum = $("#orderNum").val();
        orderManageVo.orderBeginDate = $("#orderBeginDate").val();
        orderManageVo.orderEndDate = $("#orderEndDate").val();
        orderManageVo.goodStatus = $("#goodStatus").val();
        var platform1 = $('#select1 option:selected').val();
        var platform2 = $('#select2 option:selected').val();
        var platform3 = $('#select3 option:selected').val();
        jumi.pagination("#"+pageparam.pageToolbarObj,pageparam.url,orderManageVo,function (res,curPage) {
            if (res.code === 0) {
                    //判断是否第一页
                    var data = {
                        items: res.data.items
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
        orderManageVo.goodStatus = $("#goodStatus").val();
        var newUrl = CONTEXT_PATH+"/order/order_info_list/1";
        $.ajaxJson(newUrl,orderManageVo, {
            "done": function (res) {
                if(res.code===0) {
                    //判断是否第一页
                    var data = {
                        items: res.data.items
                    };
                    var template = "/order/pc/refundGoodItemList";
                    var tableBodyObj = "refundGoodItemList";
                    jumi.template(template, data, function (tpl) {
                        $("#" + tableBodyObj).empty();
                        $("#" + tableBodyObj).html(tpl);
                        $("#" + tableBodyObj + " table ").each(function () {
                            $(this).rowspan([1, 2, 3, 4]);
                        });
                    })
                }
            }
        })
    }

    //退货订单条件查询
    var _query = function(){
        _initPagination(pageparam[tabindex]);
    };

    //打开入库备注
    var _storageNote = function(refundGoodId){
        var url = ajaxUrl.url2+refundGoodId;
        $.ajaxJsonGet(url,null,{
            "done":function (res) {
                if(res.code===0){
                    _openStorageNote(res.data);
                }
            }
        });
    }

    var _openStorageNote = function(data){
        var goodStatus = data.goodStatus;
        var data = {
            data:data
        };
        var html = jumi.templateHtml('storageNote.html',data,STATIC_URL+"/tpl/order/pc/");
        var titleStr="入库备注";
        if(goodStatus===0){
            dialog({
                content: html,
                title: titleStr,
                okValue: '确定',
                ok: function() {
                    var form = _validate();
                    if (form.valid()) {
                        var orderGoodId = $("#orderGoodId").val();
                        _saveStorageNote(orderGoodId);
                    }else {
                        return false;
                    }
                },
                cancelValue:'取消',
                cancel:function () {
                }
            }).width(400).showModal();
        }else{
            dialog({
                content: html,
                title: titleStr,
            }).width(400).showModal();
        }
    }

    //校验客服备注的提交内容
    var _validate=function(){
        var form=$("#storageNoteForm");
        form.validate({
            rules: {
                sellerNote:{
                    required: true,
                    maxlength:150
                }
            },
            messages: {
                deliveryNote:{
                    required: '请输入库内容',
                    maxlength:'入库内容不能超过150个字符'
                }
            }
        });
        return form;
    }

    var _saveStorageNote = function(orderGoodId){
        var goodVo={}
        $("#storageNoteForm").find("input,textarea").each(function () {
            goodVo[$(this).prop("name")]=$(this).val();
        })
        var url=ajaxUrl.url3+orderGoodId;
        $.ajaxJsonPut(url,goodVo,{
            "done":function(res){
                if(res.code==0){
                    _refreshPage();
                    var dm = new dialogMessage({
                        fixed:true,
                        msg:'保存入库备注成功！',
                        isAutoDisplay:true,
                        time:1500
                    });
                    dm.render();
                }else{
                    var dm = new dialogMessage({
                        fixed:true,
                        msg:'保存入库备注失败！',
                        isAutoDisplay:true,
                        time:1500
                    });
                    dm.render();
                }
            }
        });
    }

    var _refundGoodConfirm = function(orderInfoId,goodStatus,cont){
        var url = ajaxUrl.url4+orderInfoId;
        var refundGoodVo = {};
        refundGoodVo.orderInfoId = orderInfoId;
        refundGoodVo.goodStatus = goodStatus;
        var jsonStr = JSON.stringify(refundGoodVo);
        var d = dialog({
            title: '确认收货',
            content: cont,
            width: 300,
            okValue: '确定',
            ok: function () {
                $.ajaxJsonPut(url,jsonStr,{
                    "done":function(res){
                        _refreshPage();
                        var dm = new dialogMessage({
                            type:1,
                            fixed:true,
                            msg:'操作成功',
                            isAutoDisplay:true,
                            time:1500
                        });
                        dm.render();
                    }
                });
            },
            cancelValue: '取消',
            cancel: function () {
            }
        });
        d.show();
    }

    //查看物流页面
    var _querySend = function(orderInfoId){
        var url = ajaxUrl.url5+orderInfoId;
        $.ajaxJsonGet(url,null,{
            "done":function (res) {
                if(res.code===0){
                    _openSendInformation(res.data.data);
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
        var orderManageVo = {};
        var status = $("#status").val();
        orderManageVo.consigneePhone = $("#consigneePhone").val();
        orderManageVo.consigneeName = $("#consigneeName").val();
        orderManageVo.nickname = $("#nickname").val();
        orderManageVo.orderNum = $("#orderNum").val();
        orderManageVo.status = status;
        orderManageVo.orderBeginDate = $("#orderBeginDate").val();
        orderManageVo.orderEndDate = $("#orderEndDate").val();
        orderManageVo.goodStatus = $("#goodStatus").val();
        var platform1 = $('#select1 option:selected').val();
        var platform3 = $('#select3 option:selected').val();
        var jsonStr = JSON.stringify(orderManageVo);
        window.location.href =ajaxUrl.url6+"?orderManageVo="+jsonStr;
    }

    return {
        init :_init, //初始化数据
        query:_query,//条件查询
        initPagination:_initPagination, //分页查询列表
        storageNote:_storageNote, //打开入库备注
        refundGoodConfirm:_refundGoodConfirm, //确认收货（退货）
        querySend:_querySend, //查看物流
        exportRefundGood:_export, //导出订单报表
    };
})();