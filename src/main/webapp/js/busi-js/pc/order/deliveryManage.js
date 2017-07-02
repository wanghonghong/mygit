/***<===========================================================发货管理============================================================***/

/***
 * 订单查询
 */

CommonUtils.regNamespace("delivery","list");
delivery.list = (function(){

    var _companyCodes = {};
    var _companyNames = {};
    var tabindex=0;
    var _isSendMsgCommit = false; //发送短信防止重复提交
    var _is_sendAllCommit = false; //确认发货防止重复提交
    var ajaxUrl = {
        url1:CONTEXT_PATH+"/order/order_info_list/1", //微信初始化订单数据
        url2:CONTEXT_PATH+"/order/order_detail/", //打开立即发货页面
        url3:CONTEXT_PATH+"/orderDelivery/delivery_list/", //打开物流备注页面
        url4:CONTEXT_PATH+"/orderDelivery/order_delivery/", //提交发货信息
        url5:CONTEXT_PATH+"/orderDelivery/save_send_note/", //保存物流备注信息
        url6:CONTEXT_PATH+"/send_company_kd100/send_list/",
        url7:CONTEXT_PATH+"/order/query_orders/", //查看订单详情
        url8:CONTEXT_PATH+"/order/export_delivery/", //导出订单报表
        url9:CONTEXT_PATH+"/send_company_kd100/send_dispatch_list/", //送礼订单发货流程
        url10:CONTEXT_PATH+"/orderDelivery/sendmsg/", //发送短信
        url11:CONTEXT_PATH+"/orderDelivery/order_delivery_dispatch/", //送礼订单确认发货
        url12:CONTEXT_PATH+"/order/queryDispatchAndDelivery/", //送礼母订单详情查看
        url13:CONTEXT_PATH+"/order/order_info_list/2", //微博初始化订单数据
    };
    var pageparam = [{
        url: ajaxUrl.url1, //微信初始化订单数据
        pageSize: 10,
        curPage: 0,
        countObj: "count",
        pageToolbarObj: "pageTypeToolbar",
        tableBodyObj: "deliveryItemList",
        template:"/order/pc/deliveryItemList"
    },{
        url: ajaxUrl.url13, //微博初始化订单数据
        pageSize: 10,
        curPage: 0,
        countObj: "count",
        pageToolbarObj: "pageTypeToolbar",
        tableBodyObj: "deliveryItemList",
        template:"/order/pc/deliveryItemList"
    }];

    //初始化订单数据
    var _init = function(){
        tabindex=0;
        pageparam[0].fristSearch=true;
        pageparam[1].fristSearch=true;
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
            if(pageparam[index].fristSearch){
                pageparam[index].fristSearch=false;
                _initPagination(pageparam[index]);
            }
            $("#"+target).removeClass("z-hide").siblings(".panel-hidden").addClass("z-hide");
        })
    }

    var _initPagination = function(pageparam){
        var status = $("#status").val();
        var type = $('#orderType option:selected').val();
        var orderManageVo = {};
        orderManageVo.consigneePhone = $("#consigneePhone").val();
        orderManageVo.consigneeName = $("#consigneeName").val();
        orderManageVo.nickname = $("#nickname").val();
        orderManageVo.orderNum = $("#orderNum").val();
        orderManageVo.status = status;
        orderManageVo.orderBeginDate = $("#orderBeginDate").val();
        orderManageVo.orderEndDate = $("#orderEndDate").val();
        orderManageVo.goodStatus = $("#goodStatus").val();
        if(Number(type)!=0){
            orderManageVo.type = type;
        }
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
                    var template = "/order/pc/deliveryItemList";
                    var tableBodyObj = "deliveryItemList";
                    jumi.template(template, data, function (tpl) {
                        $("#" + tableBodyObj).empty();
                        $("#" + tableBodyObj).html(tpl);
                        $("#delivery_detatil").show();
                        $("#delivery_detail_content").empty();
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


    var _deliver = function(orderInfoId,userId,type,el){
        var remark = $(el).attr("remark");
        var sellerNote = $(el).attr("sellerNote");
        var orderNum = $(el).attr("orderNum");
        var giverName = $(el).attr("giverName");
        var url = "";
        if(Number(type)==5 || Number(type)==6){
            url = ajaxUrl.url9+orderInfoId;
        }else{
            url = ajaxUrl.url6+orderInfoId;
        }
        $.ajaxJsonGet(url,null,{
            "done":function (res) {
                if(res.code==0){
                    _openDeliver(orderInfoId,userId,type,giverName,remark,sellerNote,orderNum,res.data);
                }
            }
        });
    }

    var _openDeliver = function(orderInfoId,userId,type,giverName,remark,sellerNote,orderNum,data){
        $("#delivery_detatil").hide();
        var data = {
            orderInfoId:orderInfoId,
            userId: userId,
            remark:remark,
            sellerNote:sellerNote,
            orderNum:orderNum,
            giverName:giverName,
            orderDetails:data.orderDetails,
            companys:data.sendCompanysKd100s,
            orderInfoDispatchVos:data.orderInfoDispatchVos,
            status : 1,
        };
        var tpl = "";
        if(Number(type)==5 || Number(type)==6){
            tpl = "order/pc/immediateDispatch";
        }else{
            tpl = "order/pc/immediateDeliveryDialog";
        }
        jumi.template(tpl,data,function(html){
            $("#delivery_detail_content").show();
            $("#delivery_detail_content").html(html);
            delivery.packages.reset();
            delivery.packages.initData(data);
            data.first = true;
            $('.addPackages').bind('click',addPackage);
            addPackage(data);
            if(data.orderDetails.length<=1){
                $('.addPackages').unbind('click');
                $('.addPackages').hide();
            }
            $('.sendAll').bind('click',sendPackages);
            $('.send').bind('click',sendPackages);
            $('.dispatchSendAll').bind('click',_dispatchSendAll); //送礼单发货
            for(var i in data.orderInfoDispatchVos){
                //初始化select2标签
                $("#transCompany"+data.orderInfoDispatchVos[i].orderInfoId).select2({
                    theme: 'jumi'
                });
                //把物流公司和物流代码的值放入集合中
                $("#transCompany"+data.orderInfoDispatchVos[i].orderInfoId).on("change",function(e){
                    var dataId = $(this).attr("data-id");
                    var val = $(this).val();
                    var vals = val.split(',');
                    var transCompany = vals[0];
                    var transCode = vals[1];
                    _companyNames[dataId] = transCompany;
                    _companyCodes[dataId] = transCode;
                });
                var mailBtn = $('.mailBtn');
                $.each(mailBtn,function(i,mail){
                    $(mail).bind('click',function(){
                        var dataId = $(mail).attr("data-id");
                        _sendDispatch(dataId,mail);
                    });
                });
            }
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
        $.ajaxJson(path,packs,{
            done:function(res){
                if(res.data.code===1){
                    var dm = new dialogMessage({
                        type:2,
                        fixed:true,
                        msg:'勾选包裹中有商品已经发货了！',
                        isAutoDisplay:true,
                        time:2000
                    });
                    dm.render();
                    return;
                }
                _refreshPage(pageparam[tabindex]);
            }
        });
    };

    var addPackage = function(data){
        var pack = new delivery.packages.Package(data);
        pack.addPackage();
    };

    var _saveDeliver = function(orderInfoId){
        var url = ajaxUrl.url4+orderInfoId;
        var orderDeliveryVo = {};
        var strs= new Array()
        var Str = $("#d_transCompany").val();
        strs = Str.split(",");
        var transCompany = strs[0];
        var transCode = strs[1];
        var transNumber = $("#d_transNumber").val();
        var deliveryNote = $("#d_deliveryNote").val();
        orderDeliveryVo.orderInfoId = orderInfoId;
        orderDeliveryVo.transCompany = transCompany;
        orderDeliveryVo.transCode = transCode;
        orderDeliveryVo.transNumber = transNumber;
        orderDeliveryVo.deliveryNote = deliveryNote;
        if(!transCompany){
            var d = dialog({
                title: '提示',
                content:'请填写物流公司！'
            });
            d.showModal();
            setTimeout(function() {
                d.close().remove();
            }, 1000);
            return ;
        }
        if(!transNumber){
            var d = dialog({
                title: '提示',
                content:'请填写物流单号！'
            });
            d.showModal();
            setTimeout(function() {
                d.close().remove();
            }, 1000);
            return ;
        }
        var res = $.ajaxJson(url,orderDeliveryVo,{
            "done":function(res){
                _refreshPage(pageparam[tabindex]);
            }
        });
    }

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
                    _refreshPage(pageparam[tabindex]);
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
        $("#delivery_detatil").hide();
        var tpl = "/order/pc/deliveryDetails";
        var data = {
            data:data,
        };
        jumi.template(tpl,data,function(html){
            $("#delivery_detail_content").show();
            $("#delivery_detail_content").html(html);
        })
    }

    //查看送礼母订单详情
    var _queryDeliveryDispatchDetail = function(orderInfoId){
        var url = ajaxUrl.url12+orderInfoId;
        $.ajaxJsonGet(url,null,{
            "done":function (res) {
                if(res.code===0){
                    _openDeliveryDispatchDetail(res.data);
                }
            }
        });
    }

    //打开送礼母订单详情页面
    var _openDeliveryDispatchDetail = function(data){
        $("#delivery_detatil").hide();
        var tpl = "/order/pc/deliveryDispatchDetails";
        var data = {
            data:data,
        };
        jumi.template(tpl,data,function(html){
            $("#delivery_detail_content").show();
            $("#delivery_detail_content").html(html);
            console.log($.slide);
            $(".picScroll-left").slide({titCell:".superslide-hd ul",mainCell:".superslide-bd ul",autoPage:true,effect:"left",scroll:5,vis:5});
            console.log("123");
            $(".panel-hidden").hide().eq(0).show();
            $(".orderinfonum").hide().eq(0).show();
            var tabul = $(".superslide-bd ul li");
            tabul.click(function(){
                tabul.eq($(this).index()).addClass("z-sel").siblings().removeClass('z-sel');
                $(".panel-hidden").hide().eq($(this).index()).show();
                $(".orderinfonum").hide().eq($(this).index()).show();
            });
        });
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
        var orderManageVo = {};
        var status = $("#status").val();
        orderManageVo.consigneePhone = $("#consigneePhone").val();
        orderManageVo.consigneeName = $("#consigneeName").val();
        orderManageVo.nickname = $("#nickname").val();
        orderManageVo.orderNum = $("#orderNum").val();
        orderManageVo.status = status;
        orderManageVo.orderBeginDate = $("#orderBeginDate").val();
        orderManageVo.orderEndDate = $("#orderEndDate").val();
        var type = $('#orderType option:selected').val();
        if(Number(type)!=0){
            orderManageVo.type = type;
        }
        var platform1 = $('#select1 option:selected').val();
        var platform3 = $('#select3 option:selected').val();
        var jsonStr = JSON.stringify(orderManageVo);
        window.location.href =ajaxUrl.url8+"?orderManageVo="+jsonStr;
    }

    var _closeD = function (){
        $("#delivery_detail_content").hide();
        $("#delivery_detatil").show();
    }

    var _sendDispatch = function(orderInfoId,vals){
        if(_isSendMsgCommit){
            return false;
        }
        _isSendMsgCommit = true;
        var productName = $("#productName").val();
        var giverName = $("#giverName").val();
        var parentOrderInfoId = $("#orderInfoId").val();
        var sendMsgOrderDispatchCo = {};
        sendMsgOrderDispatchCo.sendsDispatchVos=[];
        var sendsDispatchVo = {
            orderInfoId:orderInfoId,
            userName:$("#userName"+orderInfoId).val(),
            transNumber:$("#transNumber"+orderInfoId).val(),
            phoneNumber:$("#phoneNumber"+orderInfoId).val(),
            detailAddress:$("#detailAddress"+orderInfoId).val(),
            transCode:_companyCodes[orderInfoId],
            transCompany:_companyNames[orderInfoId],
            deliveryNote:$("#deliveryNote"+orderInfoId).val(),
        }
        console.log(sendMsgOrderDispatchCo.sendsDispatchVos);
        sendMsgOrderDispatchCo.sendsDispatchVos.push(sendsDispatchVo);
        sendMsgOrderDispatchCo.orderInfoId = parentOrderInfoId;
        sendMsgOrderDispatchCo.productName = productName;
        sendMsgOrderDispatchCo.giverName = giverName;
        var res = $.ajaxJson(ajaxUrl.url11,sendMsgOrderDispatchCo,{
            "done":function(res){
                _isSendMsgCommit = false;
                if(res.data.code===0){
                    _isDisabled(orderInfoId,vals);
                    var dm = new dialogMessage({
                        type:2,
                        fixed:true,
                        msg:'短信发送成功！',
                        isAutoDisplay:true,
                        time:2000
                    });
                    dm.render();

                }else if(res.data.code===1){
                    var dm = new dialogMessage({
                        type:2,
                        fixed:true,
                        msg:'未包裹勾选！',
                        isAutoDisplay:true,
                        time:2000
                    });
                    dm.render();
                }else if(res.data.code===2){
                    var dm = new dialogMessage({
                        type:2,
                        fixed:true,
                        msg:'勾选包裹没有选择物流公司！',
                        isAutoDisplay:true,
                        time:2000
                    });
                    dm.render();
                }else if(res.data.code===3){
                    var dm = new dialogMessage({
                        type:2,
                        fixed:true,
                        msg:'勾选包裹没有填写物流单号！',
                        isAutoDisplay:true,
                        time:2000
                    });
                    dm.render();
                } else if(res.data.code===5){
                    var dm = new dialogMessage({
                        type:2,
                        fixed:true,
                        msg:'没有送礼人！',
                        isAutoDisplay:true,
                        time:2000
                    });
                    dm.render();
                }else if(res.data.code===6){
                    var dm = new dialogMessage({
                        type:2,
                        fixed:true,
                        msg:'没有商品名！',
                        isAutoDisplay:true,
                        time:2000
                    });
                    dm.render();
                }
            }
        });
    }

    var _isDisabled = function(orderInfoId,vals){
        $(vals).text("已发货");
        $(vals).unbind("click");
        $("#pack"+orderInfoId).attr("checked", false);
        $("#pack"+orderInfoId).attr("disabled", true);
        $("#deliveryNote"+orderInfoId).attr("disabled", true);
        $("#transNumber"+orderInfoId).attr("disabled", true);
        $("#transCompany"+orderInfoId).prop("disabled", true);//设置下拉框不可用
    }

    //送礼单发货
    var _dispatchSendAll = function(){
        if(_is_sendAllCommit){
            return false;
        }
        _is_sendAllCommit = true;
        var productName = $("#productName").val();
        var giverName = $("#giverName").val();
        var orderInfoId = $("#orderInfoId").val();
        var sendMsgOrderDispatchCo = {};
        sendMsgOrderDispatchCo.sendsDispatchVos=[];
        var packages = $("input[type='checkbox']");
        $.each(packages,function(i,packa){
            if($(packa)[0]&&$(packa)[0].checked){
                var packId = $(packa).attr("pack-id");
                var sendsDispatchVo = {
                    orderInfoId:packId,
                    userName:$("#userName"+packId).val(),
                    transNumber:$("#transNumber"+packId).val(),
                    phoneNumber:$("#phoneNumber"+packId).val(),
                    detailAddress:$("#detailAddress"+packId).val(),
                    transCode:_companyCodes[packId],
                    transCompany:_companyNames[packId],
                    deliveryNote:$("#deliveryNote"+packId).val(),
                }
                sendMsgOrderDispatchCo.sendsDispatchVos.push(sendsDispatchVo);
            }
        })
        sendMsgOrderDispatchCo.orderInfoId = orderInfoId;
        sendMsgOrderDispatchCo.productName = productName;
        sendMsgOrderDispatchCo.giverName = giverName;
        var res = $.ajaxJson(ajaxUrl.url11,sendMsgOrderDispatchCo,{
            "done":function(res){
                _is_sendAllCommit = false;
                _callback(res);
            }
        });
    }

    var _callback = function(res){
        if(res.data.code===0){
            var dm = new dialogMessage({
                type:2,
                fixed:true,
                msg:'短信发送成功！',
                isAutoDisplay:true,
                time:2000
            });
            dm.render();
            _refreshPage(pageparam[tabindex]);
        }else if(res.data.code===1){
            var dm = new dialogMessage({
                type:2,
                fixed:true,
                msg:'未包裹勾选！',
                isAutoDisplay:true,
                time:2000
            });
            dm.render();
        }else if(res.data.code===2){
            var dm = new dialogMessage({
                type:2,
                fixed:true,
                msg:'勾选包裹没有选择物流公司！',
                isAutoDisplay:true,
                time:2000
            });
            dm.render();
        }else if(res.data.code===3){
            var dm = new dialogMessage({
                type:2,
                fixed:true,
                msg:'勾选包裹没有填写物流单号！',
                isAutoDisplay:true,
                time:2000
            });
            dm.render();
        } else if(res.data.code===5){
            var dm = new dialogMessage({
                type:2,
                fixed:true,
                msg:'没有送礼人！',
                isAutoDisplay:true,
                time:2000
            });
            dm.render();
        }else if(res.data.code===6){
            var dm = new dialogMessage({
                type:2,
                fixed:true,
                msg:'没有商品名！',
                isAutoDisplay:true,
                time:2000
            });
            dm.render();
        }
    }

    return {
        init :_init, //初始化订单数据
        query:_query,//订单条件查询
        initPagination:_initPagination, //订单分页查询列表
        deliver:_deliver, //打开发货页面
        sendNote:_sendNote, //打开物流备注
        querySend:_querySend,//查看物流
        queryOrderDetail:_queryOrderDetail, //查看普通订单详情
        queryDeliveryDispatchDetail:_queryDeliveryDispatchDetail, //查看送礼母订单详情
        exportDelivery:_export, //导出订单报表
        closeD : _closeD, //发货信息返回到发货列表
    };
})();