/***<===========================================================订单管理============================================================***/

/***
 * 订单查询
 */

CommonUtils.regNamespace("order","list");
order.list = (function(){
    var tabindex=0;
    var ajaxUrl = {
        url1:CONTEXT_PATH+"/order/order_info_list/1", //微信初始化订单数据
        url2:CONTEXT_PATH+"/order/order_update/", //修改订单状态
        url3:CONTEXT_PATH+"/order/query_orders/", //查看订单详情
        url4:CONTEXT_PATH+"/order/order_detail/", //客服备注弹出框
        url5:CONTEXT_PATH+"/order/save_customer/", //保存客服备注
        url6:CONTEXT_PATH+"/order/order_detail/", //查看修改价格页面
        url7:CONTEXT_PATH+"/order/save_price/", //保存价格修改
        url8:CONTEXT_PATH+"/order/order_detail/", //查看减免运费页面
        url9:CONTEXT_PATH+"/order/agreeForRefund/", //查看退款页面
        url10:CONTEXT_PATH+"/order/save_price/", //保存减免运费
        url11:CONTEXT_PATH+"/order/query_order/", //保存减免运费
        url12:CONTEXT_PATH+"/order/export_order/", //导出订单报表
        url13:CONTEXT_PATH+"/order/queryDispatchAndDelivery/", //送礼母订单详情查看
        url14:CONTEXT_PATH+"/order/order_info_list/2", //微博初始化订单数据
        url15:CONTEXT_PATH+"/order/query_order_form/" //报表查询订单详情，根据ordernum
    };
    var pageparam = [{
        url: ajaxUrl.url1, //微信初始化订单数据
        pageSize: 10,
        curPage: 0,
        countObj: "count",
        pageToolbarObj: "pageTypeToolbar",
        tableBodyObj: "orderItemList",
        template:"/order/pc/orderItemList"
    },{
        url: ajaxUrl.url14, //微博初始化订单数据
        pageSize: 10,
        curPage: 0,
        countObj: "count",
        pageToolbarObj: "pageTypeToolbar",
        tableBodyObj: "orderItemList",
        template:"/order/pc/orderItemList"
    }];

    //初始化数据
    var _init = function(){
        tabindex=0;
        _initPagination(pageparam[0]);
        _bindEvent();
    };

    var  _bindEvent=function () {
        $("#waiting .m-tab li").click(function () {
            var _this= $(this);
            _this.addClass("z-sel").siblings().removeClass("z-sel");
            var target = _this.attr("data-target");
            var index=_this.index();
            tabindex=index;
            _initPagination(pageparam[index]);
            $("#"+target).removeClass("z-hide").siblings(".panel-hidden").addClass("z-hide");
        })
    }

    var _initPagination = function(pageparam){
        var status = $("#status").val();
        if(status==11){
            status = $('#orderStatus option:selected').val();
            if(!status || status==-1){
                status = 11;
            }
        }
        var type = $('#orderType option:selected').val();
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
        if(Number(type)!=0){
            orderManageVo.type = type;
        }
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
                    $("#" + pageparam.tableBodyObj + " table ").each(function () {
                        $(this).rowspan([1, 2, 3, 4]);
                    });
                })
            }
        })
    }

    //刷新当前页
    var _refreshPage = function(pageparam){
        var pageTypeToolbar_page = $("#pageTypeToolbar_page").val();
        var orderManageVo = {};
        orderManageVo.curPage = pageTypeToolbar_page;
        orderManageVo.pageSize = 10;
        orderManageVo.status = $("#status").val();
        var newUrl = pageparam.url;
        $.ajaxJson(newUrl,orderManageVo, {
            "done": function (res) {
                if(res.code===0) {
                    //判断是否第一页

                    var data = {
                        items: res.data.items,
                        ext:res.data.ext
                    };
                    var template = "/order/pc/orderItemList";
                    var tableBodyObj = "orderItemList";
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

    //条件查询
    var _query = function(){
        _initPagination(pageparam[tabindex]);
    };

    //关闭订单
    var _closeOrder = function(orderInfoId,status,cont){
        var url = ajaxUrl.url2+orderInfoId;
        var orderVo={};
        orderVo.status=status;
        var jsonStr = JSON.stringify(orderVo);
        var d = dialog({
            title: '关闭订单',
            content: cont,
            width: 300,
            okValue: '确定',
            ok: function () {
                $.ajaxJsonPut(url,jsonStr,{
                    "done":function(res){
                        if(res.code===0) {
                            _refreshPage(pageparam[tabindex]);
                            var dm = new dialogMessage({
                                type: 1,
                                fixed: true,
                                msg: '操作成功',
                                isAutoDisplay: true,
                                time: 1500
                            });
                            dm.render();
                        }
                    }
                });
            },
            cancelValue: '取消',
            cancel: function () {
            }
        });
        d.show();
    }

    //查看订单详情
    var _queryOrderDetail = function(orderInfoId){
        var url = ajaxUrl.url3+orderInfoId;
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
        $("#order_info_detatil").hide();
        var data = {
            data:data,
        };
        var tpl = "/order/pc/orderDetail"
        jumi.template(tpl,data,function(html){
            $("#order_detail_content").show();
            $("#order_detail_content").html(html);
        })
    };

    //查看订单详情(弹出窗)
    var _queryDialogOrderDetail = function(orderInfoId){
        var url = ajaxUrl.url11+orderInfoId;
        $.ajaxJsonGet(url,null,{
            "done":function (res) {
                if(res.code===0){
                    _openDialogOrderDetail(res.data);
                }
            }
        });
    }
    //cj add 2017-04-11收支对账报表查看订单详情(弹出窗)
    var _queryDialogOrderDetailForm = function(orderNum){
        var url = ajaxUrl.url15+orderNum;
        $.ajaxJsonGet(url,null,{
            "done":function (res) {
                if(res.code===0){
                    _openDialogOrderDetail(res.data);
                }
            }
        });
    }

    //打开订单详情页面(弹出窗)
    var _openDialogOrderDetail=function (data) {
        var data = {
            data:data,
        };
        var html = jumi.templateHtml('orderDetailDialog.html',data,STATIC_URL+"/tpl/order/pc/");
        var titleStr="订单详情";
        dialog({
            content: html,
            title: titleStr,
            cancelValue:'关闭',
            cancel:function () {
            }
        }).width(580).showModal();
    }


    //查看送礼母订单详情
    var _queryOrderDispatchDetail = function(orderInfoId){
        var url = ajaxUrl.url13+orderInfoId;
        $.ajaxJsonGet(url,null,{
            "done":function (res) {
                if(res.code===0){
                    _openOrderDispatchDetail(res.data);
                }
            }
        });
    }

    //打开送礼母订单详情页面
    var _openOrderDispatchDetail = function(data){
        $("#order_info_detatil").hide();
        var tpl = "/order/pc/orderDispatchDetails";
        var data = {
            data:data,
        };
        jumi.template(tpl,data,function(html){
            $("#order_detail_content").show();
            $("#order_detail_content").html(html);
            $(".picScroll-left").slide({titCell:".superslide-hd ul",mainCell:".superslide-bd ul",autoPage:true,effect:"left",scroll:5,vis:5});
            $(".panel-hidden").hide().eq(0).show();
            $(".orderinfonum").hide().eq(0).show();
            var tabul = $(".superslide-bd ul li");
            tabul.click(function(){
                tabul.eq($(this).index()).addClass("z-sel").siblings().removeClass('z-sel');
                $(".panel-hidden").hide().eq($(this).index()).show();
                $(".orderinfonum").hide().eq($(this).index()).show();
            });
        })
    }

    //查看客服备注
    var _queryCustomerNote = function(orderInfoId){
        var url = ajaxUrl.url4+orderInfoId;
        $.ajaxJsonGet(url,null,{
            "done":function (res) {
                if(res.code===0){
                    _openCustomerNote(res.data);
                }
            }
        });
    }

    //打开客服备注
    var _openCustomerNote = function(data){
        var data = {
            data:data
        };
        var html = jumi.templateHtml('customerNote.html',data,STATIC_URL+"/tpl/order/pc/");
        var titleStr="客服备注";
        dialog({
            content: html,
            title: titleStr,
            okValue: '确定',
            ok: function() {
                var form = _validate();
                if (form.valid()) {
                    var orderInfoId = $("#orderInfoId").val();
                    _saveCustomer(orderInfoId);
                }else {
                    return false;
                }
            },
            cancelValue:'取消',
            cancel:function () {
            }
        }).width(400).showModal();
    };

    //校验客服备注的提交内容
    var _validate=function(){
        var form=$("#customerNoteForm");
        form.validate({
            rules: {
                sellerNote:{
                    required: true,
                    maxlength:150
                }
            },
            messages: {
                sellerNote:{
                    required: '请输入备注内容',
                    maxlength:'备注内容不能超过150个字符'
                }
            }
        });
        return form;
    }

    //保存客服备注内容
    var _saveCustomer=function(orderInfoId){
        var orderVo={}
        $("#customerNoteForm").find("input,textarea").each(function () {
            orderVo[$(this).prop("name")]=$(this).val();
        })
        var url=ajaxUrl.url5+orderInfoId;
        $.ajaxJsonPut(url,orderVo,{
            "done":function(res){
                if(res.code===0){
                    _refreshPage(pageparam[tabindex]);
                    var dm = new dialogMessage({
                        fixed:true,
                        msg:'保存客服备注成功！',
                        isAutoDisplay:true,
                        time:1500
                    });
                    dm.render();
                }else{
                    var dm = new dialogMessage({
                        fixed:true,
                        msg:'保存客服备注失败！',
                        isAutoDisplay:true,
                        time:1500
                    });
                    dm.render();
                }
            }
        });
    };

    var _queryUpdatePrice = function(orderInfoId){
        var url = ajaxUrl.url6+orderInfoId;
        $.ajaxJsonGet(url,null,{
            "done":function (res) {
                if(res.code===0){
                    _openUpdatePrice(res.data);
                }
            }
        });
    }

    var _openUpdatePrice = function(data){
        var orderInfoId = data.orderInfoId;
        var data = {
            data:data,
            basePath:STATIC_URL
        };
        var html = jumi.templateHtml('updatePrice.html',data,STATIC_URL+"/tpl/order/pc/");
        var titleStr="修改价格";
        dialog({
            content: html,
            title: titleStr,
            okValue: '确定',
            ok: function() {
                _savePrice(orderInfoId);
            },
            cancelValue:'取消',
            cancel:function () {
            }
        }).width(680).showModal();
    }

    var _validatePrice = function(){
        var discountAmount1 = $("#u_discountAmount1").val();
        if(!discountAmount1){
            var dm = new dialogMessage({
                fixed:true,
                msg:'请填写减收金额！',
                isAutoDisplay:true,
                time:1500
            });
            dm.render();
            return false;
        }
        return true;
    }

    var _closeD = function (){
        $("#order_detail_content").hide();
        $("#order_detatil").show();
    }


    //获取orderVo
    function _getOrderVo(){
        var totalPrice = accMul($("#u_totalPrice").val(),100);
        var sendFee = accMul($("#u_sendFee").val(),100);
        var discount = $("#u_discount").val();
        var benefits = $("#u_benefits").val();
        var coupon = $("#u_coupon").val();
        var discountAmount1 = (accMul($("#u_discountAmount1").val(),100).toFixed(2));
        var realPrice = "";
        if(Number(discount)===0){
            realPrice = accSub(accAdd(Number(totalPrice),Number(sendFee)),Number(discountAmount1));
        }else{
            var bcTotal = accAdd(Number(benefits),Number(coupon));
            realPrice = accSub(accSub(accAdd(accDiv(accMul(Number(totalPrice),Number(discount)),10),Number(sendFee)),Number(discountAmount1)),Number(bcTotal));
        }
        if(Number(realPrice)<=0){
            realPrice = 1;
        }
        var orderVo={};
        orderVo.sendFee = Number(sendFee);
        orderVo.totalPrice = Number(totalPrice);
        orderVo.realPrice = Number(realPrice);
        return orderVo;
    }

    //保存价格修改
    var _savePrice = function(orderInfoId){
        var bl = _validatePrice();
        if(bl){
            var orderVo = _getOrderVo();
            if(orderVo.realPrice<=0){
                var dm = new dialogMessage({
                    fixed:true,
                    msg:'保存价格失败，修改后价格不能为0！',
                    isAutoDisplay:true,
                    time:1500
                });
                dm.render();
                return ;
            }
            var url=ajaxUrl.url7+orderInfoId;
            $.ajaxJsonPut(url,orderVo,{
                "done":function(res){
                    if(res.data.code===0){
                        var dm = new dialogMessage({
                            fixed:true,
                            msg:'保存价格修改失败！',
                            isAutoDisplay:true,
                            time:1500
                        });
                        dm.render();
                    }else if (res.data.code===1){
                        var dm = new dialogMessage({
                            fixed:true,
                            msg:'保存价格修改成功！',
                            isAutoDisplay:true,
                            time:1500
                        });
                        dm.render();
                        _refreshPage(pageparam[tabindex]);
                    }else{
                        var dm = new dialogMessage({
                            fixed:true,
                            msg:'修改后的价格不能为0!',
                            isAutoDisplay:true,
                            time:1500
                        });
                        dm.render();
                    }
                }
            });
        }
    }

    //查看减免运费页面
    var _queryReduceSendFee = function(orderInfoId){
        var url = ajaxUrl.url8+orderInfoId;
        $.ajaxJsonGet(url,null,{
            "done":function (res) {
                if(res.code===0){
                    _openReduceSendFee(res.data);
                }
            }
        });
    }

    var _openReduceSendFee = function(data){
        var orderInfoId = data.orderInfoId;
        var data = {
            data:data,
            basePath:STATIC_URL
        };
        var html = jumi.templateHtml('reduceSendFee.html',data,STATIC_URL+"/tpl/order/pc/");
        var titleStr="减免运费";
        dialog({
            content: html,
            title: titleStr,
            okValue: '确定',
            ok: function() {
                var discountAmount = $("#r_discountAmount").val();
                if(!discountAmount){
                    var d = dialog({
                        title: '提示',
                        content:'请填写减收金额！'
                    });
                    d.showModal();
                    setTimeout(function() {
                        d.close().remove();
                    }, 1500);
                    return ;
                }
                _saveReduceSendFee(orderInfoId);
            },
            cancelValue:'取消',
            cancel:function () {
            }
        }).width(540).showModal();
    }

    var _saveReduceSendFee = function(orderInfoId){
        var orderVo={};
        var sendFee = accMul($("#r_sendFee").val(),100);
        var realPrice = accMul($("#r_realPrice").val(),100);
        orderVo.totalPrice = accMul($("#r_productPrice").val(),100);
        orderVo.sendFee = 0;
        var finalMoney = accSub(Number(realPrice),Number(sendFee));
        if(Number(finalMoney)<0){
            finalMoney = 0.01;
        }
        orderVo.realPrice = Number(finalMoney);
        var url=ajaxUrl.url10+orderInfoId;
        $.ajaxJsonPut(url,orderVo,{
            "done":function(res){
                if(res.code===0){
                    _refreshPage(pageparam[tabindex]);
                    var dm = new dialogMessage({
                        fixed:true,
                        msg:'保存减免运费成功！',
                        isAutoDisplay:true,
                        time:1500
                    });
                    dm.render();
                }else{
                    var dm = new dialogMessage({
                        fixed:true,
                        msg:'保存减免运费失败！',
                        isAutoDisplay:true,
                        time:1500
                    });
                    dm.render();
                }
            }
        });
    }


    //确认收货
    var _confirm = function(orderInfoId,status,cont){
        var url = ajaxUrl.url2+orderInfoId;
        var orderVo={};
        orderVo.status=status;
        var jsonStr = JSON.stringify(orderVo);
        var d = dialog({
            title: '确认收货',
            content: cont,
            width: 300,
            okValue: '确定',
            ok: function () {
                $.ajaxJsonPut(url,jsonStr,{
                    "done":function(res){
                        if(res.code==0) {
                            _refreshPage(pageparam[tabindex]);
                            var dm = new dialogMessage({
                                type: 1,
                                fixed: true,
                                msg: '确认收货成功',
                                isAutoDisplay: true,
                                time: 1500
                            });
                            dm.render();
                        }
                    }
                });
            },
            cancelValue: '取消',
            cancel: function () {
            }
        });
        d.show();
    }

    //退款操作
    var _refundOperation = function(orderInfoId){
        var url = ajaxUrl.url9+orderInfoId;
        $.ajaxJsonGet(url,null,{
            "done":function (res) {
                if(res.code===0){
                    _openRefundOperation(res.data);
                }
            }
        });
    }


    //打开退款页面
    var _openRefundOperation = function(data){
        var orderInfoId = data.orderInfoId;
        var refundStatus = data.refundStatus;
        var data = {
            data:data,
            basePath:STATIC_URL
        };
        var html = "";
        if(refundStatus===0) {
            html = jumi.templateHtml('agreeRefund.html',data,STATIC_URL+"/tpl/order/pc/");
        }else{
            html = jumi.templateHtml('agreeRefundView.html',data,STATIC_URL+"/tpl/order/pc/");
        }
        var titleStr="退款操作";
        dialog({
            content: html,
            title: titleStr,
            id:'orderManager_agreeRefund'
        }).width(470).showModal();
    }

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
        var type = $('#orderType option:selected').val();
        if(Number(type)!=0){
            orderManageVo.type = type;
        }
        var platform1 = $('#select1 option:selected').val();
        var platform3 = $('#select3 option:selected').val();
        var jsonStr = JSON.stringify(orderManageVo);
        window.location.href =ajaxUrl.url12+"?orderManageVo="+jsonStr;
    }


    //加法
    function accAdd(num1,num2){
        var r1,r2,m;
        try{
            r1 = num1.toString().split('.')[1].length;
        }catch(e){
            r1 = 0;
        }
        try{
            r2=num2.toString().split(".")[1].length;
        }catch(e){
            r2=0;
        }
        m=Math.pow(10,Math.max(r1,r2));
        // return (num1*m+num2*m)/m;
        return Math.round(num1*m+num2*m)/m;
    };

    // 两个浮点数相减
    function accSub(num1,num2){
        var r1,r2,m;
        try{
            r1 = num1.toString().split('.')[1].length;
        }catch(e){
            r1 = 0;
        }
        try{
            r2=num2.toString().split(".")[1].length;
        }catch(e){
            r2=0;
        }
        m=Math.pow(10,Math.max(r1,r2));
        n=(r1>=r2)?r1:r2;
        return (Math.round(num1*m-num2*m)/m).toFixed(n);
    };

    //乘法
    function accMul(arg1,arg2)
    {
        var m=0,s1=arg1.toString(),s2=arg2.toString();
        try{m+=s1.split(".")[1].length}catch(e){}
        try{m+=s2.split(".")[1].length}catch(e){}
        return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m)
    }

    // 两数相除
    function accDiv(num1,num2){
        var t1,t2,r1,r2;
        try{
            t1 = num1.toString().split('.')[1].length;
        }catch(e){
            t1 = 0;
        }
        try{
            t2=num2.toString().split(".")[1].length;
        }catch(e){
            t2=0;
        }
        r1=Number(num1.toString().replace(".",""));
        r2=Number(num2.toString().replace(".",""));
        return (r1/r2)*Math.pow(10,t2-t1);
    };

    return {
        init :_init, //初始化
        query:_query,//条件查询
        initPagination:_initPagination, //分页查询列表
        closeOrder:_closeOrder, //关闭订单
        queryDialogOrderDetail:_queryDialogOrderDetail, //查看订单详情页面(弹出窗)
        queryOrderDetail:_queryOrderDetail, //查看订单详情页面
        queryOrderDispatchDetail:_queryOrderDispatchDetail, //查看送礼母订单详情
        queryCustomerNote:_queryCustomerNote, //查看客服备注页面
        queryUpdatePrice:_queryUpdatePrice, //查看价格修改页面
        savePrice:_savePrice, //保存价格修改
        queryReduceSendFee:_queryReduceSendFee, //查看减免运费页面
        confirm:_confirm, //确认收货
        refundOperation:_refundOperation, //退款操作页面
        refreshPage:_refreshPage, //刷新当前页
        exportOrder:_export, //导出订单报表
        closeD:_closeD, //关闭详情
        queryDialogOrderDetailForm:_queryDialogOrderDetailForm // 报表订单详情
    };
})();