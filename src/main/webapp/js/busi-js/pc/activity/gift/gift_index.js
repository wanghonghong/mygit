/**
 * Created by wxz on 16/10/31.
 */
CommonUtils.regNamespace("activity", "gift");

activity.gift = (function() {
    var  dg_giftadd = null;
    var  dg_productlst = null;
    var  dg_giftradbag = null;
    var  dg_cs_list =null; //礼券选择
    var optStatus = "";
    var ajaxUrl={
        url1: CONTEXT_PATH + '/card',   //礼券新建
        url2: CONTEXT_PATH+'/good/productList/0',//商品列表
        url3: CONTEXT_PATH + '/card/list', //礼券列表
        url4: CONTEXT_PATH + '/good/products/', //商品详情多条逗号,分隔
        url5:CONTEXT_PATH+'/activity/',  // 新增礼券活动接口
        url6:CONTEXT_PATH+'/activity/list',//查询活动列表
        url7:CONTEXT_PATH +'/card/byDateList', //礼券列表--有效期内
        url8:CONTEXT_PATH+'/activity/money_list',//粉丝统计列表
        url9:CONTEXT_PATH+'/activity/money_count'//红包发放记录统计
    };

    //初始化
    var _init = function(){
        _index_tab();

    }
     var _index_tab=function () {
         var tab = $("div[id='activity_gift_tab'] .m-goodsList-tab .m-tab ul li");
         var contab =  $(".panel-hid");
         tab.eq(0).addClass("z-sel").siblings().removeClass('z-sel');
         contab.hide().eq(0).show();
         _gift_data_list();
         tab.click(function () {
             var index = $(this).index();
             tab.eq(index).addClass("z-sel").siblings().removeClass('z-sel');
             contab.hide();
             if(index===0){
                 $("#gift_new").show();
                 _gift_data_list();
             }
             if(index===1){
                 $("#gift_new").show();
                 _actgift_redbags();
             }
             if(index===2){
                 $("#gift_status").show();
                 _actgift_starting();
             }
             if(index===3){
                 $("#gift_status").show();
                 _actgift_notstart();
             }
             if(index===4){
                 $("#gift_status").show();
                 _actgift_end();
             }
         });
     };

    //已结束
    var _actgift_end = function () {
        jumi.template('activity/gift/actgift_list', function (tpl) {
            $("#gift_status").html(tpl);
            _redbag_queryActivityList(3);
        });
    };
    //未开始
    var _actgift_notstart = function () {
        jumi.template('activity/gift/actgift_list', function (tpl) {
            $("#gift_status").html(tpl);
            _redbag_queryActivityList(0);
        });
    };
    //进行中
    var _actgift_starting = function () {
        jumi.template('activity/gift/actgift_list', function (tpl) {
            $("#gift_status").html(tpl);
            _redbag_queryActivityList(1);
        });
    };
    //新建活动
    var _actgift_redbags = function () {
        jumi.template('activity/gift/actgift_redbags', function (tpl) {
            $("#gift_new").html(tpl);
            _redbag_tab();
        });
    };

    //礼券列表
     var _gift_data_list =function () {
         jumi.template('activity/gift/gift_list', function (tpl) {
             $("#gift_new").html(tpl);
             _gift_data_list_con();
             _gift_data_add();
         });

     };

    var _gift_data_list_con =function () {
        var url = ajaxUrl.url3;
        var shopCardQo = {//  status:0,   //未发送
            pageSize: 10
        };
        jumi.pagination('#pageToolbar', url, shopCardQo, function (res, curPage) {
           
            if (res.code === 0) {
                //判断是否第一页
                var data = {
                    items: res.data.items
                };
                if (curPage === 0) {
                    data.isFirstPage = 1;
                } else {
                    data.isFirstPage = 0;
                }

                jumi.template('activity/gift/gift_list_content',data, function (tpl) {
                    $("#table_body").html(tpl);
                });
            }
        });
    }

    //礼券列表刷新
    var _gift_data_listrefresh = function () {
        var curPage = $("#pageToolbar_page").val();
        var shopCardQo = {
            pageSize:10,
            curPage:curPage||0
        };
        $.ajaxJson(ajaxUrl.url3,shopCardQo,{
            "done":function(res){
                if(res.code===0){
                    var data = {
                        items:res.data.items
                    }
                    jumi.template('activity/gift/gift_list_content',data,function(tpl){
                        $("#table_body").html(tpl);

                    })
                }
            }
        })
    };
     
     var _gift_data_edit= function () {
         $("div[id^='edit_']").unbind('click').bind('click',function () {
             optStatus = "edit";
             var id = $(this).attr("id").replace("edit_","");
             $.ajaxJsonGet(ajaxUrl.url1+"/"+id, {
                 "done": function (res) {
                     if (res.code === 0) {
                         var data = res.data;
                         var surdata = res.data.cardProducts;
                         var pids = "";
                         for(var i=0,count=surdata.length;i<count;i++){
                             pids +=surdata[i].pid+",";
                         }
                         if(pids.length>0){
                             pids = pids.substring(0,pids.length-1);
                             $.ajaxJsonGet(ajaxUrl.url4+pids, {  //取商品详情
                                 "done": function (resdetail) {
                                     if (resdetail.code === 0) {
                                         data.cardProductsDetail = resdetail.data;
                                         _gift_editdata_show(data);
                                     }
                                 },
                                 "fail": function (resdetail) {
                                 }
                             });
                         }else{
                             _gift_editdata_show(data);
                         }

                     } else {
                         _gift_optMsg(2,"对不起，操作失败");
                     }
                 },
                 "fail": function (res) {
                 }
             });
         });
     };


    var _gift_data_view= function () {
        $("div[id^='view_']").unbind('click').bind('click',function () {
            var id = $(this).attr("id").replace("view_","");
            $.ajaxJsonGet(ajaxUrl.url1+"/"+id, {
                "done": function (res) {
                    if (res.code === 0) {
                        var data = res.data;
                        var surdata = res.data.cardProducts;
                        var pids = "";
                        for(var i=0,count=surdata.length;i<count;i++){
                            pids +=surdata[i].pid+",";
                        }
                        if(pids.length>0){
                            pids = pids.substring(0,pids.length-1);
                            $.ajaxJsonGet(ajaxUrl.url4+pids, {  //取商品详情
                                "done": function (resdetail) {
                                    if (resdetail.code === 0) {
                                        data.cardProductsDetail = resdetail.data;
                                        _gift_viewdata_show(data);
                                    }
                                },
                                "fail": function (resdetail) {
                                }
                            });
                        }else{
                            _gift_viewdata_show(data);
                        }

                    } else {
                        _gift_optMsg(2,"对不起，操作失败");
                    }
                },
                "fail": function (res) {
                }
            });
        });
    };

    var _gift_data_del=function () {
        $("div[id^='del_']").unbind('click').bind('click',function () {
            var id = $(this).attr("id").replace("del_","");
            var args = {};
            args.fn1 = function(){
                _gift_data_delete(id);
            };
            args.fn2 = function(){

            };
            jumi.dialogSure("您确定要删除该礼券信息？", args);

        });
    };

    var _gift_data_delete=function (id) {
        $.ajaxJsonDel(ajaxUrl.url1+"/"+id, {
            "done": function (res) {
                if (res.data.code === 0) {
                    _gift_optMsg(1,res.data.msg);
                    _gift_data_listrefresh();
                } else {
                    _gift_optMsg(2,res.data.msg);
                }
            },
            "fail": function (res) {
            }
        });
    }

    var _gift_viewdata_show = function (data) {
        jumi.template('activity/gift/gift_add', data,function (tpl) {
            dg_giftadd = dialog({
                title: "查看礼券",
                content: tpl,
                width: 1150,
                id: 'dialog_edit',
                onclose: function () {
                    dialog.get('dialog_edit').close().remove();
                },
                onshow:function () {
                    $("#add_product").hide();
                   $("#gift_save_btn").hide();
                    _gift_dialog_cancel();//取消
                }

            });
            dg_giftadd.showModal();
        });
    }

     var _gift_editdata_show = function (data) {
         jumi.template('activity/gift/gift_add', data,function (tpl) {
             dg_giftadd = dialog({
                 title: "编辑礼券",
                 content: tpl,
                 width: 1150,
                 id: 'dialog_edit',
                 onclose: function () {
                     dialog.get('dialog_edit').close().remove();
                 },
                 onshow:function () {
                     _gift_color_list();//礼券颜色下拉框--点击其它区域隐藏
                     jumi.setData("#startTime");
                     jumi.setData("#endTime");
                     _gift_productList();//商品列表
                     _gift_data_get();//添加
                     _gift_dialog_cancel();//取消
                    // _gift_bind_totalMoney();//总额小计
                     _gift_bind_userLimit();//指定商品
                     _gift_product_setback_del();
                     _gift_validate();
                 }

             });
             dg_giftadd.showModal();
         });
     }
     //礼券添加
     var _gift_data_add=function () {
         $("#dg_gift_add").unbind('click').bind('click',function () {
             optStatus = "add";
             jumi.template('activity/gift/gift_add', function (tpl) {
              dg_giftadd = dialog({
                     title: "新建礼券",
                     content: tpl,
                     width: 1150,
                     id: 'dialog_add',
                     onclose: function () {
                         dialog.get('dialog_add').close().remove();
                     },
                     onshow:function () {
                         _gift_color_list();//礼券颜色下拉框--点击其它区域隐藏
                         jumi.setData("#startTime");
                         jumi.setData("#endTime");
                         _gift_productList();//商品列表
                         _gift_dialog_cancel();//取消
                        // _gift_bind_totalMoney();//总额小计
                         _gift_bind_userLimit();//指定商品
                         _gift_validate();
                         _gift_data_get();//添加
                     }
                     
                 });
                 dg_giftadd.showModal();
             });
         });
     };

     //礼券颜色列表事件
     var _gift_color_list = function () {
         //$("#giftColorDiv").on("click",function(e){
         $('#gift_container').on('click','#giftColorDiv',function(e){
             var flag = $('#giftColor').is(":hidden");
             if(!flag){
                 $('#giftColor').hide();
             }else{
                 $('#giftColor').show();
             }
             $(document).on("click", function(){
                 $("#giftColor").hide();
             });
             e.stopPropagation();
         });
         $("#giftColor").on("click", function(e){
             e.stopPropagation();
         });
     };

     //礼券颜色设置
    var _setGiftColor=function (colorValue) {
        $('#giftColorSpan').css({
            "background-color":colorValue||'#252525'
        });
        var val = colorValue||'#252525';
        $("#hiddeColor").val(val);
        $("#giftColor").hide();
    };
    //新建礼券对话框取消
    var _gift_dialog_cancel= function () {
        $("#gift_cancel_btn").click(function () {
            dg_giftadd.close().remove();
        });
    }


    //礼券添加
    var _gift_data_get = function () {
        $("#gift_save_btn").click(function () {
            if(optStatus === "add"){
                var form = $( "#gift_form" );
                if(form.valid()) {
                    _gift_data_getForAdd();
                }
            }
            if(optStatus === "edit"){
                var form = $( "#gift_form" );
                if(form.valid()) {
                    _gift_data_getForEdit();
                }
             }
        });
    };
    var _gift_data_getForEdit = function () {
        var startTime = $("#startTime").val();
        var endTime = $("#endTime").val();
        var fixDate = $("#fixDate").val();
        var buyMoney = $("#buyMoney").val();
        var userCondition = _get_radioVal("userCondition");
        var periodType = _get_radioVal("periodType");
        var userLimit = _get_radioVal("userLimit");
        var pids =_gift_pid();
        if(periodType==="2" &&(!isNull(startTime)||!isNull(endTime))) {
            _error("请填写有效日期");
            return;
        }
        if(periodType==="2" &&!_giftact_checkdata(startTime,endTime)) {
            _error("自定义有效期的 失效日期需大于生效日期");
            return;
        }
        if(periodType==="1" &&(!isNull(fixDate))) {
            _error("请填写有效日期");
            return;
        }
        if(userCondition==="1"&&(!isNull(buyMoney))){
            _error("请填写使用门槛");
            return;
        }
        if(userLimit==="1"&&pids===""){ //使用限定
            _error("请选择使用限定");
            return;
        }
        var shopCardUo={};
        shopCardUo.id = $("#cardId").val();
        shopCardUo.cardName = $("#cardName").val();
        shopCardUo.cardMoney = $("#cardMoney").val()||0;
        shopCardUo.colour = $("#hiddeColor").val();
        shopCardUo.totalCount = $("#totalCount").val()||0;
        shopCardUo.totalMoney = $("#totalMoney").val()||0;
        shopCardUo.periodType = _get_radioVal("periodType");
        shopCardUo.startTime = "";
        shopCardUo.endTime = "";
        shopCardUo.fixDate = 0;
        if(shopCardUo.periodType==="2") { // 固定有效期  自定义有效期
            shopCardUo.startTime = startTime;
            shopCardUo.endTime = endTime;
        }else if(shopCardUo.periodType==="1"){
            shopCardUo.fixDate = fixDate||0;
        }
        shopCardUo.expireWarn = _get_radioVal("expireWarn")||0;
        shopCardUo.userCondition = userCondition;//使用条件
        shopCardUo.buyMoney =0;
        if(shopCardUo.userCondition==="1"){
            shopCardUo.buyMoney = buyMoney||0;
        }
        shopCardUo.userLimit = userLimit;
        shopCardUo.pids ="";
        if(shopCardUo.userLimit==="1"){ //使用限定
            shopCardUo.pids =pids;
        }
        shopCardUo.userNote = $("#userNote").val();
        var data = JSON.stringify(shopCardUo);
        _gift_data_postForEdit(data);
    }

    var _gift_data_getForAdd = function () {
        var startTime = $("#startTime").val();
        var endTime = $("#endTime").val();
        var fixDate = $("#fixDate").val();
        var buyMoney = $("#buyMoney").val();
        var userCondition = _get_radioVal("userCondition");
        var periodType = _get_radioVal("periodType");
        var userLimit = _get_radioVal("userLimit");
        var pids =_gift_pid();
        var shopCardCo={};
        if(periodType==="2" &&(!isNull(startTime)||!isNull(endTime))) {
            _error("请填写有效日期");
            return;
        }
        if(periodType==="2" &&!_giftact_checkdata(startTime,endTime)) {
            _error("自定义有效期的 失效日期需大于生效日期");
            return;
        }
        if(periodType==="1" &&(!isNull(fixDate))) {
            _error("请填写有效日期");
            return;
        }
        if(userCondition==="1"&&(!isNull(buyMoney))){
            _error("请填写使用门槛");
            return;
        }
        if(userLimit==="1"&&pids===""){ //使用限定
            _error("请选择使用限定");
            return;
        }
        shopCardCo.cardName = $("#cardName").val();
        shopCardCo.cardMoney = $("#cardMoney").val()||0;
        shopCardCo.colour = $("#hiddeColor").val();
     //   shopCardCo.totalCount = $("#totalCount").val()||0;
     //   shopCardCo.totalMoney = $("#totalMoney").val()||0;
        shopCardCo.periodType = periodType;
        shopCardCo.startTime = "";
        shopCardCo.endTime = "";
        shopCardCo.fixDate = 0;

        if(shopCardCo.periodType==="2") { // 固定有效期  自定义有效期
            shopCardCo.startTime = startTime;
            shopCardCo.endTime = endTime;
        }else if(shopCardCo.periodType==="1"){
            shopCardCo.fixDate = fixDate||0;
        }
        shopCardCo.expireWarn = _get_radioVal("expireWarn")||0;
        shopCardCo.userCondition =userCondition;//使用条件
        shopCardCo.buyMoney =0;

        if(shopCardCo.userCondition==="1"){
            shopCardCo.buyMoney = buyMoney||0;
        }

        shopCardCo.userLimit = userLimit;
        shopCardCo.pids ="";
        if(shopCardCo.userLimit==="1"){ //使用限定
            shopCardCo.pids =pids;
        }
        shopCardCo.userNote = $("#userNote").val();
        var data = JSON.stringify(shopCardCo);
        _gift_data_postForAdd(data);
    }

    var _gift_data_postForAdd = function (data) {
        $.ajaxJson(ajaxUrl.url1, data, {
            "done": function (res) {
                if (res.data.code === 0) {
                    _gift_optMsg(1,res.data.msg);
                    if(isNull(dg_giftadd)){
                        optStatus = "";//清空操作状态
                        dg_giftadd.close().remove();
                        _gift_data_listrefresh();//刷新列表
                    }
                } else {
                    _gift_optMsg(2,"对不起，操作失败");
                }
            },
            "fail": function (res) {
            }
        });
    };

    var _gift_data_postForEdit = function (data) {
        $.ajaxJsonPut(ajaxUrl.url1, data, {
            "done": function (res) {
                if (res.data.code === 0) {
                    _gift_optMsg(1,"恭喜您，操作成功");
                    if(isNull(dg_giftadd)){
                        optStatus = "";//清空操作状态
                        dg_giftadd.close().remove();
                        _gift_data_listrefresh();//刷新列表
                    }
                } else {
                    _gift_optMsg(2,res.data.msg);
                }
            },
            "fail": function (res) {
            }
        });
    };
    var _gift_pid = function () {
        var idstr ="";
        $("div[id^='productlst_item_']").each(function () {
            idstr+=$(this).attr("id").replace("productlst_item_","")+",";
        });
        if(idstr.length>0){
            idstr = idstr.substring(0,idstr.length-1);
        }
        return idstr;
    }

    var _gift_optMsg = function (typeVal,info) {

        var dm = new dialogMessage({
            type: typeVal,
            title: '操作提醒',
            fixed: true,
            msg: info,
            isAutoDisplay:true,
            time:3000
        });
        dm.render();
    };
    //错误显示
    var _error = function(msg){
        $('#error').show();
        $('body').scrollTop(0);
        $('#tipError').text(msg);
        _error_close();
    }

    var _error_close = function () {
        $("#error").click(function () {
            $(this).hide();
        });
    }

    //礼券radio值获取
    var _get_radioVal= function (val) {
        var idVal= "";
        $("input[id^='"+val+"']").each(function () {  //发送类别
            var isstatus = $(this).prop('checked');
            if (isstatus) {  //1是 0否
                idVal = $(this).val();
            }
        });
        return idVal;
    }
    //商品列表
    var _gift_productList = function () {
        $("#add_product").click(function () {
            jumi.template('activity/gift/gift_forproduct',function (tpl) {
                 dg_productlst = dialog({
                    title: "商品列表",
                    content: tpl,
                    width: 700,
                    id: 'dg_product_lst',
                    onclose: function () {
                        dialog.get('dg_product_lst').close().remove();
                    },
                    onshow:function () {
                        _queryAwardpointList();
                        _gift_product_setback();
                    }
                });
                dg_productlst.showModal();
            });
        });
    };

    //商品换购列表
    var _queryAwardpointList = function(){
        var url = ajaxUrl.url2;
        var integralProductQo = {
            pageSize:10
        };
        jumi.pagination('#pageproductToolbar',url,integralProductQo,function(res,curPage){
            if(res.code===0){
                var data = {
                    items:res.data.items
                };
                if (curPage === 0) {
                    data.isFirstPage = 1;
                } else {
                    data.isFirstPage = 0;
                }

                jumi.template('activity/gift/gift_forproduct_list', data,function (tpl) {
                   $('#gift_product_list').empty();
                   $("#gift_product_list").html(tpl);
                });
            }
        });


    };
    ///选择指定商品
    var _gift_product_setback=function () {
        $("#product_list_btn").click(function () {
            $("input[id^='checkproduct_']").each(function () {
                var isstatus = $(this).prop('checked');
                if (isstatus) {
                    var id = $(this).attr("id").replace("checkproduct_", "");
                    if(_gift_product_canceldouble(id)) {
                        var name = $(this).val();
                        var html = $("#porductlst_templatte").clone();
                        html.css("display", "static");
                        html.find("#porductlst_templatte").remove();
                        html.attr("id", "productlst_item_" + id);
                        html.find("#porductlst_del").attr("id", "productlst_del_" + id);
                        html.find("#productName_").text(name);
                        html.find("#productName_").attr("id", "productName_" + id);
                        $("#productlst_obj").append(html);
                    }
                }
            });
            _gift_product_setback_del();
            if(isNull(dg_productlst)) {
                dg_productlst.close().remove();
            }
        });
    };

    //指定商品--剔除重复
    var _gift_product_canceldouble = function (id) {
        var tag = 0;
        $("div[id='productlst_container'] div[id^='productlst_item_']").each(function () {
            var curId = $(this).attr("id").replace("productlst_item_","");
            if(curId!==id){
                tag += 0;
            }else{
                tag += 1; //重复数量
            }
        });
        if(tag>0){
            return false;
        }else{
            return true;
        }
    };

    var _gift_product_setback_del= function () {
        $("div[id^='productlst_del_'] >i").unbind('click').bind('click',function() {
            $(this).parent().parent().remove();
        });
    };

    function isNull(data) {
        return (data == "" || data == undefined || data == null) ? false : true;
    };



    //指定商品隐藏
    var _gift_bind_userLimit = function () {
        $("input[id^='userLimit']").change(function () {

            if($(this).val()==="1"){
                $("#productlst_container").show();
            }else{
                $("#productlst_container").hide();
            }
        });
    };
    //验证
    var _gift_validate = function() {
        $("#gift_form").validate({
            rules: {
                cardMoney: {
                    required: true,
                    isIntGtZero:true,
                    min:1,
                    max:999
                },
                totalCount: {
                    required: true,
                    isIntGtZero:true
                },
                fixDate:{
                    isIntGtZero:true
                },
                buyMoney:{
                    isFloatGtZero:true
                }
            },
            messages: {
                cardMoney: {
                    required: "请输入礼券面值",
                    isIntGtZero:'礼券面值必须是一个大于零的整数',
                    min:"请输入一个 最小为1的值",
                    max:"请输入一个 最大为999的值"
                },
                totalCount: {
                    required: "请输入摘要",
                    isIntGtZero:'发放总量必须是一个大于零的整数'
                },
                fixDate:{
                    isIntGtZero:'固定有效期必须是一个大于零的整数'
                },
                 buyMoney:{
                     isFloatGtZero:'订单金额必须是一个大于零的数字'
                }
            }
        });
    };


/////////////活动开始
    var _redbag_tab = function () {
       var tab = $("div[id='redbag_list'] ul li");
        tab.click(function () {
            var index = $(this).index();
            var type = $(this).data('type');
            var title=_redbag_title(index);
            _redbag_putOut(title,index,type);
        });
    };
    
    var _redbag_title = function (index) {
        var title="";
        if(index===0){
            title = "首次关注发红包";
        }
        if(index===1){
            title = "已关注粉丝发红包";
        }
        if(index===2){
            title = "购买商品发红包";
        }
        if(index===3){
            title = "确认收货发红包";
        }
        if(index===4){
            title = "礼卷转赠发红包";
        }
        return title;
    }

    //发红包
    var _redbag_putOut = function (title,index,type) {
        jumi.template('activity/gift/actgift_create?type='+type, function (tpl) {
            dg_giftradbag = dialog({
                title: title,
                content: tpl,
                width: 1150,
                id: 'dialog_giftradbag',
                onclose: function () {
                    dialog.get('dialog_giftradbag').close().remove();
                },
                onshow:function () {
                    _roleSetting();//角色选择
                    activity.gold.advancedRegion();
                    if(index!==1) {
                        jumi.setDataTime("#startTime");
                        jumi.setDataTime("#endTime");
                    }else{
                        jumi.setDataTime("#startTime");
                        $("#endTimeDiv").hide();
                    }
                    _redbag_cardProperty(); //礼券属性
                    _redbag_tabProperty();
                    _redbag_dialog_cancel();//取消
                    //_redbag_roleLimit();// 角色限定 显示按钮
                    _redbag_buylimit_Index(index);
                    _redbag_dialog_save(index);//保存
                }
            });
            dg_giftradbag.showModal();
        });
    };

    var _redbag_buylimit_Index = function (index) {
        $("#condition").hide();
        if(index===2){
            $("#condition").show();
            $("#buyMoney_product").attr("placeholder","购买商品达多少");
        }
        if(index===4){
            $("#condition").show();
            $("#buyMoney_product").attr("placeholder","礼券转赠达多少");
        }
    }
    
    //选择角色信息函数
    var _roleSetting = function () {

        $('#actgift_role_btn').click(function() {
            var doc = $('#choose_actgift_roles').find('.role-del');
            var array = [];
            var subType = $('input[name="subType"]').val();
            doc.each(function(){
                var id = $(this).data('id');
                array.push(id);
            })
            jumi.file('role.json',function(res){
                if(res.code===0){
                    var data = {
                        items:res.data
                    }
                    _.each(array,function(k,v){
                        var d = _.where(res.data,{id:k});
                        d[0].ischecked = 1;
                    })
                    var subType = $('input[name="subType"]').val();
                    jumi.template('activity/activity_role?subType='+subType,data,function (tpl) {
                        var d = dialog({
                            title: '选取角色',
                            content:tpl,
                            width:400,
                            id:'actgiftRoles',
                            onshow:function(){
                                $('#allcheckbox').click(function(){
                                    var flag = $(this).is(":checked");
                                    if(flag){
                                        $('input[name="role"]').prop('checked',true);
                                    }else{
                                        $('input[name="role"]').prop('checked',false);
                                    }
                                })
                                $('#activity_role_btn').click(function(){
                                    var roles = [],tpl='';
                                    $('input[id^="role_checkBox_"]:checked').each(function(){

                                        var id = $(this).val();console.log(id);
                                        var tip = $(this).data('tip');
                                        tpl+='<div class="u-btn-smltgry u-btn-smltgry-1 role-del" data-id="'+id+'"><span>'+tip+'</span><i class="iconfont icon-delete1"></i></div>'
                                    })
                                    $('#choose_actgift_roles').html(tpl);
                                    $('.role-del').click(function(){
                                        $(this).remove();
                                    })
                                    dialog.get('actgiftRoles').close().remove();
                                })
                            }
                        });
                        d.showModal();
                    })
                }
            })
        })
    };

    var _redbag_cardselect_dialog =function (typeVal) {
                jumi.template('activity/gift/actgift_cardselect_list', function (tpl) {
                    dg_cs_list = dialog({
                        title: "礼券列表",
                        content: tpl,
                        width: 1135,
                        id: 'dialog_cslist',
                        onclose: function () {
                            dialog.get('dialog_cslist').close().remove();
                        },
                        onshow:function () {
                            _redbag_cardselect_list(typeVal);
                        }
                    });
                    dg_cs_list.showModal();
                });
    };

    var _redbag_cardselect_list =function (typeVal) {
        var url = ajaxUrl.url7;
        var shopCardQo = {//  status:0,   //未发送
            pageSize: 10
        };
        jumi.pagination('#pageagcardToolbar', url, shopCardQo, function (res, curPage) {
            if (res.code === 0) {
                //判断是否第一页
                var data = {
                    items: res.data.items
                };
                if (curPage === 0) {
                    data.isFirstPage = 1;
                } else {
                    data.isFirstPage = 0;
                }
                jumi.template('activity/gift/actgift_cardselect_list_con',data, function (tpl) {
                    $('#agcardselect_list').empty();
                    $('#agcardselect_list').html(tpl);
                    _redbag_gift_setback(typeVal);
                });
            }
        });
    };

   var _redbag_tabProperty = function () {
       $('input[id^="cardType_"]').click(function(){
           var status = $(this).prop('checked');
           var typeVal = $(this).val();
           if(status){
               _redbag_cardProperty_panel(typeVal); //礼券属性面板
           }
       });
   }

    //选择
    var _redbag_cardProperty = function () {
        $("#cardselect_btn").click(function () {
            var typeVal = "";
            $('input[id^="cardType_"]:checked').each(function(){
                typeVal = $(this).val();
                _redbag_cardProperty_panel(typeVal); //礼券属性面板
            });
           var count = $("div[id='cardItems_1'] ul").length;
            if(typeVal==="1" &&  count===1){
                _gift_optMsg(2,"限定面值的礼券已选择!");
                return;
            }
            _redbag_cardselect_dialog(typeVal);

        });
    };

    var _redbag_cardProperty_panel = function (typeVal) {
        $("div[id^='cardItems_']").hide();
        $("div[id^='cardType_Bottom_']").hide();
        $("#cardItems_"+typeVal).show();
        $("#cardType_Bottom_"+typeVal).show();
    }

    //礼券选中对象设值---添加
    var _redbag_gift_setback=function (typeVal) {
        $("#agcard_btn").click(function () {
           var count = $("input[id^='agcard_check_']:checked").length;

           if(typeVal==="1" && count>1){
               _gift_optMsg(3,"限定面值 只能选择一项礼券!");
               return;
           }
           var btm_count=0;
            var btm_money=0;
            $("input[id^='agcard_check_']:checked").each(function () {
                var id = $(this).attr("id").replace("agcard_check_", "");
                var tag = _redbag_canceldouble(typeVal,id);
                if(tag){ //剔除重复项
                    var data = JSON.parse($("#agcard_item_dt_"+id).val());
                    var html = $("#cardItem").clone();
                    html.css("display","static");
                    html.find("#cardItem").remove();
                    html.attr("id", "cardItem_" + id);
                    html.find("li:eq(0)").text(data.cardName);
                    html.find("li:eq(1)").text(data.cardMoney);
                    if(typeVal==="3"){
                        html.find("li:eq(2) input").attr("readonly","readonly");
                    }
                    //限制输入

                    html.find("li:eq(2) input").attr("id","totalCount_"+typeVal+"_"+id);
                    html.find("li:eq(2) input").val(1);
                    html.find("li:eq(2) input").on('keypress',function(event){
                        var value = $(this).val();
                        if((event.keyCode<48 || event.keyCode>57) && event.keyCode!=46 || /[1-9]\.\d\d$/.test(value)){
                            event.preventDefault();
                        }
                    })
                    btm_count +=1;
                    //数量绑定事件
                    html.find("li:eq(2) input").bind('input propertychange', function () {
                        _bind_data($(this));
                    });
                    html.find("li:eq(3)").attr("id","totalMoney_"+typeVal+"_"+id);
                    html.find("li:eq(3)").text(data.cardMoney);
                    btm_money +=data.cardMoney;
                    html.find("#cardItem_del").attr("id","cardItem_del_"+typeVal+"_"+id);
                    $("#cardItems_"+typeVal).append(html);
                }
            });
            $("#count_"+typeVal).text(btm_count);
            $("#total_"+typeVal).text(btm_money);
            _redbag_product_setdel();
            if(isNull(dg_cs_list)) {
                dg_cs_list.close().remove();
            }
        });
    };
//正整数
    function isPInt(str) {
     /*   var g = /^[1-9]*[1-9][0-9]*$/;
        var re = new RegExp(g);
        if(re.test(str))
        {
            alert( "请输入大于零的整数!");
            return;
        }*/


       /* var Expression=/[\u4e00-\u9fa5]/;//中文正式表达示
        var objExp=new RegExp(Expression);

        var wxhao = $("#wxhao").val();
        if(objExp.test(wxhao)) {
            $("#sCodeMsg").show();//关注提示
            return;
        }*/
    }

    var _bind_data = function (obj) {
        var id ="";
        var divobj = obj.parent().parent().parent();
        var typeVal = divobj.attr("id").replace("cardItems_","");
        var ulobj = obj.parent().parent();
        var count = parseInt(obj.val())||0;
        var cardMoney = parseInt(ulobj.find("li:eq(1)").text())||0;
        var totalMoney = count*cardMoney;
        var total = 0;
        count =0;
        if(isNaN(parseInt(obj.val()))) { //数量为正整数
           obj.val("");
        }
        ulobj.find("li:eq(3)").text(totalMoney);
        id = "totalCount_"+typeVal;  //总数量
        divobj.find("input[id^='"+id+"']").each(function () {
            count +=parseInt($(this).val())||0;
        });
        $("#count_"+typeVal).text(count);
        id = "totalMoney_"+typeVal;   //总额
        divobj.find("li[id^='"+id+"']").each(function () {
            total +=parseInt($(this).text())||0;
        });
        $("#total_"+typeVal).text(total);
    }
    
    var _redbag_canceldouble = function (typeVal,id) {
        var tag = 0;
        $("div[id='cardItems_"+typeVal+"'] div[id^='cardItem_del_']").each(function () {
            var curId = $(this).attr("id").replace("cardItem_del_"+typeVal+"_","");
            if(curId!==id){
                tag += 0;
            }else{
                tag += 1; //重复数量
            }
        });
        if(tag>0){
            return false;
        }else{
            return true;
        }
    };

    var _redbag_product_setdel= function () {
        $("div[id^='cardItem_del_']").unbind('click').bind('click',function() {
            $(this).parent().parent().remove();
        });
    };
    //删除并保存记录
    var _redbag_product_setdeldatasave= function () {
        $("div[id^='cardItem_del_']").unbind('click').bind('click',function() {
             var typeId = $(this).parent().parent().parent().attr("id").replace("cardItems_","");
             var actcardid= $(this).parent().parent().data("id");
             var delObj =$("#delids_"+typeId);
             var oldid = delObj.text();
             oldid +=actcardid+",";
             delObj.text(oldid);
            $(this).parent().parent().remove();
        });
    };

    var _redbag_dialog_cancel = function () {
        $("#actgift_cancel_btn").click(function () {
            if(isNull(dg_giftradbag)) {
                dg_giftradbag.close().remove();
            }
        });
    };
    var _redbag_dialog_save = function (index) {
        $("#actgift_save_btn").click(function () {
            var count =0;
            var idStr ="";
            var activityCo = {};
            var activityConditionCo = {};
            var cardTpArray = [];
            var cardTpIdsArray = [];
            var activityName = $('#activityName').val();
            var startTime = $('#startTime').val();
            var endTime = $('#endTime').val();
            if(!isNull(activityName)) {
                _error("请填写活动名称");
                return;
            }
            if(index===1){
                if (!isNull(startTime)) {
                    _error("请填写活动时间");
                    return;
                }
            }else {
                if ((!isNull(startTime) || !isNull(endTime))) {
                    _error("请填写活动时间");
                    return;
                }
            }
            activityCo.takeType = $('input[name="show_form"]:checked').val();
            activityCo.shakeCount = $('input[name="showgl"]:checked').val();
            activityCo.platform = 1;
            activityCo.subType= parseInt(index)+1;
            activityCo.type = 2;
            activityCo.activityName = activityName;
            activityCo.startTime = startTime;
            activityCo.endTime = endTime;
            activityCo.cardType =$('input[name="cardType"]:checked').val()||1;
            activityCo.totalMoney =parseInt($("#total_"+activityCo.cardType).text())*100;
            activityCo.status = 0;

            $("div[id='cardItems_"+activityCo.cardType+"'] >ul").each(function () {
                count +=1;
                var activityCardCo = {};
                var id = $(this).attr("id").replace("cardItem_","");
                var totalCount = parseInt($(this).find("#totalCount_"+activityCo.cardType+"_"+id).val())||0;
                idStr +=id+",";
                activityCardCo.cardId=id;
                activityCardCo.totalCount =totalCount;
                cardTpArray.push(activityCardCo);
            });
            if(count===0){
                _error("请选择礼券");
                return;
            }
            if(activityCo.cardType==="3"){   //礼包
                var activityCardCo = {};
                idStr = idStr.substring(0,idStr.length-1);
                activityCardCo.cardIds = idStr;
                activityCardCo.cardId = 0;
                activityCardCo.totalCount = $('#cardType_bags_3').val();
                cardTpIdsArray.push(activityCardCo);
                activityCo.activityCardList =cardTpIdsArray;
            }else {
                activityCo.activityCardList = cardTpArray; //随机、限定面值
            }
            activityConditionCo.buyMoney = 0;
            if(index===2){
                activityConditionCo.buyMoney = Number($("#buyMoney_product").val())||0; //购买商品达多少
            }
            if(index===4){
                activityConditionCo.buyMoney = Number($("#buyMoney_product").val())||0; //礼券转赠达多少
            }
            activityConditionCo.sex = $('input[name="giftsex"]:checked').val()||0;
            var roles = [];
            if($('input[name="roleLimit"]').is(":checked")){
                activityConditionCo.roleLimit = 1;
                $('#choose_actgift_roles').find('.role-del').each(function(){
                    var id = $(this).data('id');
                    if(id=='99'){
                        activityConditionCo.isBuy = id;
                    }else{
                        activityConditionCo.isBuy = 0;
                        roles.push(id);
                    }
                });
            }else{
                activityConditionCo.roleLimit = 0;
            }
            if(activityConditionCo.roleLimit === 1&&roles.length===0){
                _error("请选择角色");
                return;
            }
            roles = roles.join(',');
            activityConditionCo.roles = roles;

            if($('input[name="activity_area"]').is(":checked")){
                activityConditionCo.areaLimit = 1;
            }else{
                activityConditionCo.areaLimit = 0;
            }
            activityConditionCo.areaNames = $('input[name="activity_area_name"]').val();
            activityConditionCo.areaIds = $('input[name="activity_area_ids"]').val();
            activityCo.activityConditionCo =activityConditionCo;
            if(activityConditionCo.areaLimit === 1 && !isNull(activityConditionCo.areaNames)){
                _error("请选择地区");
                return;
            }
            var data = JSON.stringify(activityCo);
            _gift_data_post(data);
        });
    };

    var _gift_data_post = function (jsonData) {

        $.ajaxJson(ajaxUrl.url5,jsonData,{
            "done":function (res) {
                if(res.code===0){
                    if(res.data.code===1){
                        var dm = new dialogMessage({
                            type:2,
                            fixed:true,
                            msg:res.data.msg,
                            isAutoDisplay:true,
                            time:3000
                        });
                        dm.render();
                    }else if(res.data.code===2){
                        var tempData = JSON.parse(jsonData);
                        tempData.operationState="1";
                        jsonData = JSON.stringify(tempData);
                        _gift_data_makesure(jsonData,res.data.msg,"add");
                    }else{
                        var dm = new dialogMessage({
                            type: 1,
                            fixed: true,
                            msg: res.data.msg,
                            isAutoDisplay: true,
                            time: 1000
                        });
                        dm.render();
                        setTimeout(function(){
                            dg_giftradbag.close().remove();
                        },1500);

                    }

                }else{
                    var dm = new dialogMessage({
                        type:2,
                        fixed:true,
                        msg:res.data.msg,
                        isAutoDisplay:true,
                        time:3000
                    });
                    dm.render();
                }
            },
            "fail":function (res) {
                var dm = new dialogMessage({
                    type:2,
                    fixed:true,
                    msg:res.data.msg,
                    isAutoDisplay:true,
                    time:3000
                });
                dm.render();
            }

        });
    };

    /**
     * 二次确认保存
     *jsonData 数据源
     * status 刷新参数
     * msg 返回信息提示
     * tag  新增:"add"  修改:"edit"
     */
    var _gift_data_makesure = function (jsonData,msg,tag) {
        var args = {};
        args.fn1 = function(){
            if(tag==="add"){
                _gift_data_post(jsonData);
            }else if(tag==="edit"){
                _gift_data_update(jsonData);
            }
        };
        args.fn2 = function(){
        };
        jumi.dialogSure(msg,args);
    };

    var _redbag_roleLimit= function () {
        $("#roleLimit").change(function () {
           if($(this).prop('checked')){
               $("#role_container").show();
           }else{
               $("#role_container").hide();
           }
        });
    };
    //礼券红包列表
    var _redbag_queryActivityList = function(redType){
        var activityQo = {};
        activityQo.pageSize = 10;
        activityQo.type = 2;
        if(redType===1||redType===2){
            activityQo.status = '1,2';
        }else{
            activityQo.status = redType;
        }
        jumi.pagination('#actgift_starting_pageToolbar',ajaxUrl.url6,activityQo,function(res,curPage){
            if(res.code===0){
                var data = {
                    items:res.data.items
                };
                jumi.template('activity/gift/actgift_list_content?status='+redType,data,function(tpl){
                    $('#actgift_starting_list').html(tpl);
                })
            }
        })
    };
    //礼券红包列表刷新
    var _redbag_queryActivityList_refresh = function(redType){
        var curPage = $("#pageToolbar_page").val();
        var activityQo = {};
        activityQo.pageSize = 10;
        activityQo.type = 2;
        activityQo.curPage=curPage || 0;
        if(redType===1||redType===2){
            activityQo.status = '1,2';
        }else{
            activityQo.status = redType;
        }
        $.ajaxJson(ajaxUrl.url6,activityQo, {
            "done": function (res) {
                if (res.code === 0) {
                    var data = {
                        items: res.data.items
                    }
                    jumi.template('activity/gift/actgift_list_content?status='+redType,data,function(tpl){
                        $('#actgift_starting_list').html(tpl);
                    })
                }
            }
        })
    };
    //删除活动
    var _redbag_del = function(id,name,status){
        var url = ajaxUrl.url5+id;
      //  var status = $('#activity_tab').find('.z-sel').data('index');
        var tab = $("div[id='activity_gift_tab'] .m-goodsList-tab .m-tab ul").find('.z-sel').data('index');
        var args = {};
        args.fn1 = function(){
            $.ajaxJsonDel(url,{
                "done":function (res) {
                    if(res.code===0){
                        var dm = new dialogMessage({
                            type:1,
                            fixed:true,
                            msg:res.data.msg,
                            isAutoDisplay:true,
                            time:3000
                        });
                        dm.render();
                        _refresh_list(status);
                    }
                }
            });
        };
        args.fn2 = function(){

        };
        jumi.dialogSure('是否取消【'+name+'】的活动',args);
    };

    //控制活动启动暂停
    var _redbag_set = function(id,name,status){
        var url = ajaxUrl.url5+'pause/'+id;
        msg = (status==='1')?'确定暂停【'+name+'】的活动吗?':'确定重新启动【'+name+'】的活动吗?';
        var args = {};
        args.fn1 = function(){
            _redbag_Action(url,id);
        };
        args.fn2 = function(){

        };
        jumi.dialogSure(msg,args);
    }
    //开启或暂停活动
    var _redbag_Action = function(url,id){
        $.ajaxJsonGet(url,null,{
            done:function (res) {
                if(res.code===0){
                    var dm = new dialogMessage({
                        type:1,
                        fixed:true,
                        msg:res.data.msg,
                        isAutoDisplay:true,
                        time:3000
                    });
                    dm.render();
                    _actgift_starting();
                }else{
                    var dm = new dialogMessage({
                        type:2,
                        fixed:true,
                        msg:'活动设置失败',
                        isAutoDisplay:true,
                        time:3000
                    });
                    dm.render();
                }
            }
        })
    };

    var _refresh_list = function (status) {
        if(status==="1"||status==="2"){
            $("#gift_status").show();
            jumi.template('activity/gift/actgift_list', function (tpl) {
                $("#gift_status").html(tpl);
                _redbag_queryActivityList_refresh(1);
            });
        }
        if(status==="0"){
            $("#gift_status").show();
            jumi.template('activity/gift/actgift_list', function (tpl) {
                $("#gift_status").html(tpl);
                _redbag_queryActivityList_refresh(0);
            });
        }
        if(status==="3"){
            $("#gift_status").show();
            jumi.template('activity/gift/actgift_list', function (tpl) {
                $("#gift_status").html(tpl);
                _redbag_queryActivityList_refresh(3);
            });
        }
    };

    //修改红包活动
    var _refresh_modify = function(id,type,status){
        var url = ajaxUrl.url5+id;
        $.ajaxJsonGet(url,null,{
            "done":function(res){
                if(res.code===0){
                    var data = res.data;
                    jumi.template('activity/activity_c_redPaper?type='+type+'&edit=1&status='+status,data,function (tpl) {
                        var d = dialog({
                            title: '修改红包活动',
                            content:tpl,
                            id:'editDialogAction',
                            width:820,
                            onshow:function(){
                                var dtd = $.Deferred();
                                var wait = function(dtd){
                                    var tasks = function(){
                                        dtd.reject();
                                    };
                                    return dtd;
                                };
                                $.when(
                                    wait(
                                        _initCalculate(type)
                                    )).done(function(){
                                    _fillData(data);
                                    _computing();
                                    _timeTimepicker();//初始化时间插件
                                    _validate();
                                    _addEventAction();
                                    _advancedSetting();
                                    _advancedRegion();
                                })
                            }
                        });
                        d.showModal();
                    })
                }
            }
        })
    };
    
var _redbag_modify = function (id,type,status) {

    var index =type-1;
    var subType = type;
    var title = _redbag_title(index);
    _redbag_putOut_edit(id,title,index,subType);
};

    //发红包
    var _redbag_putOut_edit = function (id,title,index,subType) {
        var url = ajaxUrl.url5 + id;
        $.ajaxJsonGet(url, null, {
            "done": function (res) {
                if (res.code === 0) {
                    var data = res.data;
                    jumi.template('activity/gift/actgift_create?type='+subType,data,function (tpl) {
                        dg_giftradbag = dialog({
                            title: title,
                            content: tpl,
                            width: 1150,
                            id: 'dialog_giftradbag',
                            onclose: function () {
                                dialog.get('dialog_giftradbag').close().remove();
                            },
                            onshow: function () {
                                _roleSetting();//角色选择
                                activity.gold.advancedRegion();//地区选择
                                if(index!==1) {
                                    jumi.setDataTime("#startTime");
                                    jumi.setDataTime("#endTime");
                                }else{
                                    jumi.setDataTime("#startTime");
                                    $("#endTimeDiv").hide();
                                }
                                _redbag_cardProperty(); //礼券属性
                                _redbag_tabProperty();
                                _redbag_dialog_cancel();//取消
                                //_redbag_roleLimit();// 角色限定 显示按钮
                                _redbag_buylimit_Index(index);
                                _redbag_dialog_dataBack(data,"0",index,"edit"); //回填---修改
                                _redbag_dialog_editSave(id,index);//保存
                            }

                        });
                        dg_giftradbag.showModal();
                    });

                } else {
                    _gift_optMsg(2, "对不起，操作失败");
                }
            },
            "fail": function (res) {
            }
        });
    };

    var _redbag_dialog_dataBack = function (data,optStatus,index,optview) {
        var tpl ="";
        $('input[name="show_form"][value="'+data['subType']+'"]').prop('checked',true);
        $('input[name="showgl"][value="'+data['shakeCount']+'"]').prop('checked',true);
        $("#activityName").val(data.activityName);
        $("input[id^='giftsex']").prop("checked","");
        $("input[id='giftsex"+data.activityCondition.sex+"']").prop("checked","checked");
        if(index===2){ //购买商品发红包
            $("#condition").show();
            $("#buyMoney_product").val(data.activityCondition.buyMoney);
        }
        if(index===4){ //礼券转赠发红包
            $("#condition").show();
            $("#buyMoney_product").val(data.activityCondition.buyMoney);
        }
        //角色回填
        if(data.activityCondition.roles!=null){
            var limitStt = data.activityCondition.roleLimit;
            if(limitStt===1) {
                $("#roleLimit").prop("checked", "checked");
                $("#role_container").show();
                array = data.activityCondition.roles.split(',');
                if(data['activityCondition']['isBuy']===99) {
                    if(array.length>0){
                        array[array.length] = '99';
                    }else{
                        array.push('99').split(',');
                    }
                }
                _fillFoles(array);
            }
        }
        $('input[name="activity_area_ids"]').val(data.activityCondition.areaIds);
        $('input[name="activity_area_name"]').val(data.activityCondition.areaNames || '');
        if(data.activityCondition.areaNames){
            $('input[name="activity_area"]').prop('checked',true);
            var names = data.activityCondition.areaNames.split(',');
            _.map(names,function(k,v){
                tpl+='<div class="u-btn-smltgry u-btn-smltgry-1"><span>'+k+'</span></div>';
            })
            $('#choose_activity_areas').append(tpl);
        }
        $("input[id^='cardType_']").prop("checked","");
        $("input[id='cardType_"+data.cardType+"']").prop("checked","checked");
        _redbag_gift_setbackbyedit(data,optview);//礼券调用--回填
        if(optStatus!=="1"){
            if(index!==1) {
                $("#startTime").val(data.startTime);
                $("#endTime").val(data.endTime);
            }else{
                $("#startTime").val(data.startTime);
                $("#endTimeDiv").hide();
            }

        }
    };

    //回填角色
    var _fillFoles = function(array){
        var tpl = '';
        _.map(array,function(k,v){
            switch(k)
            {
                case '1':
                    tpl+='<div class="u-btn-smltgry u-btn-smltgry-1 role-del" data-id="1"><span>代理商1档</span><i class="iconfont icon-delete1"></i></div>';
                    break;
                case '2':
                    tpl+='<div class="u-btn-smltgry u-btn-smltgry-1 role-del" data-id="2"><span>代理商2档</span><i class="iconfont icon-delete1"></i></div>';
                    break;
                case '3':
                    tpl+='<div class="u-btn-smltgry u-btn-smltgry-1 role-del" data-id="3"><span>代理商3档</span><i class="iconfont icon-delete1"></i></div>';
                    break;
                case '4':
                    tpl+='<div class="u-btn-smltgry u-btn-smltgry-1 role-del" data-id="4"><span>代理商4档</span><i class="iconfont icon-delete1"></i></div>';
                    break;
                case '5':
                    tpl+='<div class="u-btn-smltgry u-btn-smltgry-1 role-del" data-id="5"><span>分销商1档</span><i class="iconfont icon-delete1"></i></div>';
                    break;
                case '6':
                    tpl+='<div class="u-btn-smltgry u-btn-smltgry-1 role-del" data-id="6"><span>分销商2档</span><i class="iconfont icon-delete1"></i></div>';
                    break;
                case '7':
                    tpl+='<div class="u-btn-smltgry u-btn-smltgry-1 role-del" data-id="7"><span>分销商3档</span><i class="iconfont icon-delete1"></i></div>';
                    break;
                case '8':
                    tpl+='<div class="u-btn-smltgry u-btn-smltgry-1 role-del" data-id="8"><span>分享客</span><i class="iconfont icon-delete1"></i></div>';
                    break;
                case '99':
                    tpl+='<div class="u-btn-smltgry u-btn-smltgry-1 role-del" data-id="99"><span>已经关注购买粉丝</span><i class="iconfont icon-delete1"></i></div>';
                    break;
                default:
            }
        })
        $('#choose_actgift_roles').html(tpl);
        $('.role-del').click(function(){
            $(this).remove();
        })
    };

    //礼券调用回填
    var _redbag_gift_setbackbyedit=function (ptdata,optview) {
            var btm_count=0;
            var btm_money=0;
            var typeVal = ptdata.cardType;
            var cardList = ptdata.activityCardList;

            if(cardList.length>0) {
                $("div[id^='cardType_Bottom_']").hide();
                $("#cardItems_"+typeVal).show();
                $("#cardType_Bottom_"+typeVal).show();
                if(typeVal!==3){   // 单张， 随机
                    for (var i = 0, count = cardList.length; i < count; i++) {
                        var id = cardList[i].cardId;
                        var actCardId = cardList[i].id;
                        _redbag_getCard(actCardId,id, typeVal,cardList[i].totalCount,optview);
                    }
                }else {  //大礼包
                    var ids = cardList[0].cardIds;
                    var idsArr = ids.split(',');
                    for (var j = 0, num = idsArr.length; j < num; j++) {
                        var id = idsArr[j];
                        var actCardId = cardList[0].id;
                        _redbag_getCard(actCardId,id, typeVal,cardList[0].totalCount,optview);
                    }

                }

            }

    };

    var _redbag_card_totalcount = function (typeVal) {
        var count =0;
        var total = 0;
        var divobj = $("#cardItems_"+typeVal);
        var id ="totalCount_"+typeVal;
        divobj.find("input[id^='"+id+"']").each(function () {
            count +=parseInt($(this).val())||0;
        });
        id = "totalMoney_"+typeVal;   //总额
        divobj.find("li[id^='"+id+"']").each(function () {
            total +=parseInt($(this).text())||0;
        });
        $("#count_"+typeVal).text(count);
        $("#total_"+typeVal).text(total);
    }

    var _redbag_getCard = function (actCardId,id,typeVal,totalCount,optview) {
        $.ajaxJsonGet(ajaxUrl.url1+"/"+id, {
            "done": function (res) {
                if (res.code === 0) {
                    var data = res.data;
                    $("#preTypeVal").text(typeVal);//礼券属性
                    if(data.periodType===2 && _giftact_checkStartEndTime(data.startTime,data.endTime) && optview!=="view") { // 自定义有效期
                        _redbag_getCard_table(actCardId,id,typeVal,totalCount,optview,data);
                    }else if(data.periodType===2 && optview==="view"){ //查看不过滤过期时间
                        _redbag_getCard_table(actCardId,id,typeVal,totalCount,optview,data);
                    }else if(data.periodType===1 ) { // 固定有效期
                        _redbag_getCard_table(actCardId,id,typeVal,totalCount,optview,data);
                    }
                } else {
                    _gift_optMsg(2,"对不起，操作失败");
                }
            },
            "fail": function (res) {
            }
        });
    };
//礼券回填---编辑、查看、再来一次
    var _redbag_getCard_table =function (actCardId,id,typeVal,totalCount,optview,data) {
        var html = $("#cardItem").clone();
        html.css("display", "static");
        html.find("#cardItem").remove();
        html.attr("id", "cardItem_" + id);
        html.data("id", actCardId); //关系id
        html.find("li:eq(0)").text(data.cardName);
        html.find("li:eq(1)").text(data.cardMoney);
        if (typeVal === 3) {
            html.find("li:eq(2) input").attr("readonly", "readonly");
        }
        html.find("li:eq(2) input").attr("id", "totalCount_" + typeVal + "_" + id);
        if(typeVal!==3) {
            html.find("li:eq(2) input").val(totalCount);
        }else{
            html.find("li:eq(2) input").val(1);
        }
        //数量绑定事件
        html.find("li:eq(2) input").bind('input propertychange', function () {
            _bind_data($(this));
        });
        html.find("li:eq(3)").attr("id", "totalMoney_" + typeVal + "_" + id);
        if(typeVal!==3) {
            html.find("li:eq(3)").text(data.cardMoney * totalCount);
        }else{
            html.find("li:eq(3)").text(data.cardMoney);
        }
        if (optview !== "view") { //判断查看
            html.find("#cardItem_del").attr("id", "cardItem_del_" + typeVal + "_" + id);
        } else {
            $("#cardselect_btn").hide();
            html.find("li:eq(2) input").attr("readonly", "readonly");
            html.find("#cardItem_del").remove();
        }
        $("#cardItems_" + typeVal).append(html);
        if (optview !== "view") {//判断查看
            if(optview !== "edit"){
              _redbag_product_setdel();
            }else{
                _redbag_product_setdeldatasave();
            }
        }
        if (typeVal === 3) {
            $("#cardType_bags_3").val(totalCount);
        }
        _redbag_card_totalcount(typeVal);//总数量，总额
    }

    var _redbag_dialog_editSave = function (id,index) {
        $("#actgift_save_btn").click(function () {
            var delId ="";
            var idStr ="";
            var actCardId_bag= "";
            var activityUo = {};
            var activityConditionUo = {};
            var cardTpArray = [];
            var cardTpIdsArray = [];
            var activityName = $('#activityName').val();
            var startTime = $('#startTime').val();
            var endTime = $('#endTime').val();
            if(!isNull(activityName)) {
                _error("请填写活动名称");
                return;
            }
            if(index===1){
                if (!isNull(startTime)) {
                    _error("请填写活动时间");
                    return;
                }
            }else {
                if ((!isNull(startTime) || !isNull(endTime))) {
                    _error("请填写活动时间");
                    return;
                }
            }
            activityUo.takeType = $('input[name="show_form"]:checked').val();
            activityUo.shakeCount = $('input[name="showgl"]:checked').val();
            activityUo.platform = 1;
            activityUo.subType= parseInt(index)+1;
            activityUo.type = 2;
            activityUo.activityName = activityName;
            activityUo.startTime = startTime;
            activityUo.endTime = endTime;
            activityUo.cardType =$('input[name="cardType"]:checked').val()||1;
            activityUo.totalMoney =parseInt($("#total_"+activityUo.cardType).text())*100;
            activityUo.id = id;
            var preTypeVal = $("#preTypeVal").text();
            if(activityUo.cardType!==preTypeVal){//礼券属性改变
                delId = _redbag_cardType_Change(preTypeVal);
            }else {
                delId = $("#delids_" + activityUo.cardType).text();
            }
            if(isNull(delId)){
                delId = delId.substring(0,delId.length-1);
                activityUo.delIds = delId; //删除
            }
            $("div[id='cardItems_"+activityUo.cardType+"'] >ul").each(function () {
                var activityCardUo = {};
                var id = $(this).attr("id").replace("cardItem_","");
                var actCardId = $(this).data('id');
                var totalCount = parseInt($(this).find("#totalCount_"+activityUo.cardType+"_"+id).val())||0;
                idStr +=id+",";
                actCardId_bag = actCardId;  //修改
                if(isNull(actCardId_bag)) {
                    activityCardUo.id = actCardId;
                }

                activityCardUo.cardId=id;
                activityCardUo.totalCount =totalCount;
                cardTpArray.push(activityCardUo);
            });
            if(activityUo.cardType==="3"){   //礼包
                var activityCardUo = {};
                idStr = idStr.substring(0,idStr.length-1);
                if(isNull(actCardId_bag)){
                    activityCardUo.id = actCardId_bag;
                }
                activityCardUo.cardIds = idStr;
                activityCardUo.cardId = 0;
                activityCardUo.totalCount = $('#cardType_bags_3').val();
                cardTpIdsArray.push(activityCardUo);
                activityUo.activityCardList =cardTpIdsArray;
            }else {
                activityUo.activityCardList = cardTpArray; //随机、限定面值
            }
            activityConditionUo.buyMoney = 0;
            if(index===2){
                activityConditionUo.buyMoney = Number($("#buyMoney_product").val())||0; //购买商品达多少
            }
            if(index===4){
                activityConditionUo.buyMoney = Number($("#buyMoney_product").val())||0; //礼券转赠达多少
            }
            activityConditionUo.sex = $('input[name="giftsex"]:checked').val()||0;
            var roles = [];
            if($('input[name="roleLimit"]').is(":checked")){
                activityConditionUo.roleLimit = 1;
                $('#choose_actgift_roles').find('.role-del').each(function(){
                    var id = $(this).data('id');
                    roles.push(id);
                });
            }else{
                activityConditionUo.roleLimit = 0;
            }
            roles = roles.join(',');
            activityConditionUo.roles = roles;
            //地区选择
            if($('input[name="activity_area"]').is(":checked")){
                activityConditionUo.areaLimit = 1;
            }else{
                activityConditionUo.areaLimit = 0;
            }
            activityConditionUo.areaNames = $('input[name="activity_area_name"]').val();
            activityConditionUo.areaIds = $('input[name="activity_area_ids"]').val();
            activityUo.activityConditionUo =activityConditionUo;
            if(activityConditionUo.areaLimit === 1 && !isNull(activityConditionUo.areaNames)){
                _error("请选择地区");
                return;
            }
            var data = JSON.stringify(activityUo);
            _gift_data_update(data);
        });
    };

    var _gift_data_update = function (jsonData) {
        $.ajaxJsonPut(ajaxUrl.url5,jsonData,{
            "done":function (res) {
                if(res.code===0){
                    if(res.data.code===1){
                        var dm = new dialogMessage({
                            type:2,
                            fixed:true,
                            msg:res.data.msg,
                            isAutoDisplay:true,
                            time:3000
                        });
                        dm.render();
                    }else if(res.data.code===2){
                        var tempData = JSON.parse(jsonData);
                        tempData.operationState="1";
                        jsonData = JSON.stringify(tempData);
                        _gift_data_makesure(jsonData,res.data.msg,"edit");
                    }else {
                        var dm = new dialogMessage({
                            type: 1,
                            fixed: true,
                            msg: res.data.msg,
                            isAutoDisplay: true,
                            time: 1000
                        });
                        dm.render();
                        setTimeout(function(){
                            dg_giftradbag.close().remove();
                        },1500);
                        _actgift_notstart();
                    }
                }else{
                    var dm = new dialogMessage({
                        type:2,
                        fixed:true,
                        msg:res.data.msg,
                        isAutoDisplay:true,
                        time:3000
                    });
                    dm.render();
                }
            },
            "fail":function (res) {
                var dm = new dialogMessage({
                    type:2,
                    fixed:true,
                    msg:res.data.msg,
                    isAutoDisplay:true,
                    time:3000
                });
                dm.render();
            }

        });
    };



    //编辑---礼券属性修改，提取旧属性下的礼券
    var _redbag_cardType_Change= function (preTypeVal) {
        var delid="";
        var actCardId ="";
        $("div[id='cardItems_"+preTypeVal+"'] >ul").each(function () {
            actCardId = $(this).data('id');
            if(preTypeVal!=="3"){
                delid += actCardId + ",";
            }else {
                delid = actCardId + ",";
            }
        });
        return delid;
    }

    var _redbag_againOne = function (id,type) {
        var index =type-1;
        var title = _redbag_title(index);
        var url = ajaxUrl.url5 + id;
        $.ajaxJsonGet(url, null, {
            "done": function (res) {
                if (res.code === 0) {
                    var data = res.data;
                    _redbag_againOne_dg(id,title,index,data);
                } else {
                    _gift_optMsg(2, "对不起，操作失败");
                }
            },
            "fail": function (res) {
            }
        });
    };
    var _redbag_againOne_dg = function (id,title,index,data) {
        jumi.template('activity/gift/actgift_create', function (tpl) {
            dg_giftradbag = dialog({
                title: title,
                content: tpl,
                width: 1150,
                id: 'dialog_giftradbag',
                onclose: function () {
                    dialog.get('dialog_giftradbag').close().remove();
                },
                onshow:function () {
                    _roleSetting();//角色选择
                    if(index!==1) {
                        jumi.setDataTime("#startTime");
                        jumi.setDataTime("#endTime");
                    }else{
                        jumi.setDataTime("#startTime");
                        $("#endTimeDiv").hide();
                    }
                    _redbag_cardProperty(); //礼券属性
                    _redbag_tabProperty();
                    activity.gold.advancedRegion();
                    _redbag_dialog_cancel();//取消
                    //_redbag_roleLimit();// 角色限定 显示按钮
                    _redbag_buylimit_Index(index);
                    _redbag_dialog_dataBack(data,"1",index,"again"); //再来一次数据回填
                    _redbag_dialog_save(index);//保存
                }

            });
            dg_giftradbag.showModal();
        });
    };

    var _redbag_view = function (id,type) {
        var index =type-1;
        var title = _redbag_title(index);
        var url = ajaxUrl.url5 + id;
        $.ajaxJsonGet(url, null, {
            "done": function (res) {
                if (res.code === 0) {
                    var data = res.data;
                    _redbag_view_dg(id,title,index,data);
                } else {
                    _gift_optMsg(2, "对不起，操作失败");
                }
            },
            "fail": function (res) {
            }
        });
    };
    var _redbag_view_dg = function (id,title,index,data) {
        jumi.template('activity/gift/actgift_create', function (tpl) {
            dg_giftradbag = dialog({
                title: title,
                content: tpl,
                width: 1150,
                id: 'dialog_giftradbag',
                onclose: function () {
                    dialog.get('dialog_giftradbag').close().remove();
                },
                onshow:function () {
                    _redbag_tabProperty();
                    _redbag_dialog_cancel();//取消
                    _redbag_buylimit_Index(index);
                    _redbag_dialog_dataBack(data,"0",index,"view");//查看数据回填
                    $("#actgift_save_btn").hide();

                }
            });
            dg_giftradbag.showModal();
        });
    };

    //时间比较 判断时间是否有效
    function _giftact_checkStartEndTime(startTime,endTime){
        var curr= new Date();
        if(isNull(endTime)) {
            var end = new Date(endTime.replace("-", "/").replace("-", "/"));
            if (curr > end) {
                return false;
            }
        }else{
            return false;
        }
        return true;
    };
    //时间比较 比较时间大小
    function _giftact_checkdata(startTime, endTime) {
        var curr = new Date();
        var start = new Date(startTime.replace("-", "/").replace("-", "/"));
        var end = new Date(endTime.replace("-", "/").replace("-", "/"));
        if (start > end) {
            return false;
        }
        return true;
    }
/////////////活动结束

//查看红包效果
    var _actgift_effect = function(id){
        var url = ajaxUrl.url9+'/'+id;
        $.ajaxJsonGet(url,null,{
            "done":function(res){
                if(res.code===0){
                    var data = res.data;
                    jumi.template('activity/gift/actgift_effect',data,function (tpl) {
                        var d = dialog({
                            title: '查看红包效果',
                            content:tpl,
                            width:820,
                            onshow:function(){
                                _actgift_effect_list(id);
                            }
                        });
                        d.showModal();
                    })
                }
            }
        })
    }

    var _actgift_effect_list =function (id) {
        var url = ajaxUrl.url8;
        var activityUserEo = {//  status:0,   //未发送
            pageSize:10,
            activityId:id
        };
        jumi.pagination('#actgift_pageToolbar', url, activityUserEo, function (res, curPage) {
console.log(res);
            if (res.code === 0) {
                //判断是否第一页
                var data = {
                    items: res.data.items
                };
                if (curPage === 0) {
                    data.isFirstPage = 1;
                } else {
                    data.isFirstPage = 0;
                }

                jumi.template('activity/gift/actgift_effect_list',data, function (tpl) {
                    $("#actgift_table_grid").html(tpl);
                });
            }
        });
    }
    return {
        init:_init,
        redbag_title:_redbag_title,
        gift_data_list:_gift_data_list,
        roleSetting:_roleSetting,
        redbag_cardProperty:_redbag_cardProperty,
        setGiftColor:_setGiftColor,
        fillFoles:_fillFoles,
        giftact_checkStartEndTime:_giftact_checkStartEndTime,
        redbag_del:_redbag_del,
        redbag_set:_redbag_set,
        redbag_modify:_redbag_modify,
        redbag_againOne:_redbag_againOne,
        redbag_view:_redbag_view,
        gift_data_edit:_gift_data_edit,
        gift_data_view:_gift_data_view,
        gift_data_del:_gift_data_del,
        actgift_effect:_actgift_effect
    };
})();