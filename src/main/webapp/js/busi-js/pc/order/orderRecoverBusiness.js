/***<===========================================================订单管理============================================================***/

/***
 * 订单查询
 */

CommonUtils.regNamespace("orderRecoverBusiness","list");
orderRecoverBusiness.list = (function(){
    var orderBookConfigFirstSearch = true;
    var ue={
        ueA:null,
        ueB:null
    };
    var ajaxUrl = {
        url1:CONTEXT_PATH+"/orderBookConfig/list", //地区限定
        url2:CONTEXT_PATH+"", //修改订单状态
        url3:CONTEXT_PATH+"/recycleExplain/list", //查看订单详情
        url4:CONTEXT_PATH+"/recycleNode/list", //回收网点
        url5:CONTEXT_PATH+"/recycleNode/list", //公益网点
        url6:CONTEXT_PATH+"/recycleReward/list/", //奖励方式
        url7:CONTEXT_PATH+"/orderBookConfig", //保存地区限定
        url8:CONTEXT_PATH+"/recycleNode", //保存回收网点
        url10:CONTEXT_PATH+"/recycleReward", //保存奖励方式
        url11:CONTEXT_PATH+"/recycleNode/", //回收网点
        url12:CONTEXT_PATH+"/recycleNode/query/", //弹出修改回收网点页面
        url13:CONTEXT_PATH+"/recycleExplain", //保存回收说明
        url14:CONTEXT_PATH+"/recycleNode/", //保存修改公益网点
        url15:CONTEXT_PATH+"/recycleNode/update_status/", //修改上下架
        url16:CONTEXT_PATH+"/orderBookArea/service_list", //地区列表
    };

    var pageparam = [{
        url: ajaxUrl.url4, //回收网点
        pageSize: 10,
        curPage: 0,
        type:1,
        countObj: "count",
        pageToolbarObj: "pageTypeToolbar",
        tableBodyObj: "contentBox",
        firstSearch:true,
        template:"/order/pc/recoveryPoint"
    },{
        url: ajaxUrl.url5, //公益网点
        pageSize: 10,
        curPage: 0,
        type:2,
        countObj: "count",
        pageToolbarObj: "pageTypeToolbar",
        tableBodyObj: "contentBox",
        firstSearch:true,
        template:"/order/pc/publicNetwork"
    },{
        url: ajaxUrl.url16, //地区列表
        pageSize: 10,
        curPage: 0,
        type:2,
        countObj: "count",
        pageToolbarObj: "pageTypeToolbar",
        tableBodyObj: "contentBox",
        firstSearch:true,
        template:"/order/pc/orderBookAreaAdd"
    }]

    //初始化数据
    var _init = function(){
        _bindEvent();
    };

    var  _bindEvent=function () {
        _order_book_area();//<!--地区限定-->
        $("#m-tab li").click(function () {
            var index = $(this).index();
            var _this= $(this);
            var target = _this.attr("data-target");
            _this.addClass("z-sel").siblings().removeClass("z-sel");
            switch(index)
            {
                case 0:
                    _order_book_area();//<!--地区限定-->
                    break;
                case 1:
                    _order_book_area_add(pageparam[2]);//<!--地区服务-->
                    break;
                case 2:
                    _recovery_explain();//<!--回收说明-->
                    break;
                case 3:
                    _recovery_point(pageparam[0]);//<!--回收网点-->
                    break;
                case 4:
                    _public_network(pageparam[1]);//<!--公益网点-->
                    break;
                case 5:
                    _reward_modes();//<!--奖励方式-->
                    break;
                default:
                    _order_book_area();//<!--地区限定-->
            }
        })
    }

    var _order_book_area = function(){
        var url = ajaxUrl.url1;
        $.ajaxJsonGet(url,null,{
            "done":function (res) {
                openOrderBookArea(res.data);
            }
        })
    };

    var openOrderBookArea = function(data){
        var data = {
            areaLimit:data.areaLimit,
        };
        jumi.template('/order/pc/orderBookArea',data, function (tpl) {
            $("#contentBox").empty();
            $("#contentBox").html(tpl);
            $('.addOrderBookArea').bind('click',addOrderBookArea);
        });
    }

    //<!--地区服务-->
    var _order_book_area_add = function(pageparam){
        var OrderBookAreaQo = {};
        jumi.pagination("#"+pageparam.pageToolbarObj,pageparam.url,OrderBookAreaQo,function (res,curPage) {
            if (res.code === 0) {
                //判断是否第一页
                var data = {
                    items:res.data.items,
                };
                jumi.template(pageparam.template, data, function (tpl) {
                    $("#" + pageparam.tableBodyObj).empty();
                    $("#" + pageparam.tableBodyObj).html(tpl);
                })
            }
        })

    };

    //<!--回收说明-->
    var _recovery_explain = function(){
        var url = ajaxUrl.url3;
        $.ajaxJsonGet(url,null,{
            "done":function (res) {
                openRecoveryExplain(res.data);
            }
        })
    };

    var openRecoveryExplain = function(data){
        var data = {
            module1:data.module1,
            module2:data.module2,
            usingModule:data.usingModule,
            usingModule1:data.usingModule1,
            usingModule2:data.usingModule2,
            data: data,
        }
        jumi.template('/order/pc/orderRecoveryExplain', data,function (tpl) {
            $("#contentBox").empty();
            $("#contentBox").html(tpl);
            if(data.data.id){
                initValue(data.data);
            }else{
                orderRecoverBusiness.list.ueEditA();
                orderRecoverBusiness.list.ueEditA1();
            }
        });
    };
    var initValue=function (data) {
        var imgUrlArr = data.imgUrl.split(",");
        for(var i=0;i<imgUrlArr.length;i++){
            addImg(imgUrlArr[i]);
        }
        orderRecoverBusiness.list.ueEditA(data.node);
        orderRecoverBusiness.list.ueEditA1(data.node1);
    }

    var addImg=function (url) {
        $("<li>",{
            "class":"img"
        }).html("<img src='"+url+"' /><i class='iconfont icon-delete3 del'></i>").insertBefore("#imgResAdd")
    }


    var _recoveryExplainAddImg = function(){
        $("#imgResAdd").click(function () {
            if( $("#h5Template .imgResList li.img").length>=12){
                var dm = new dialogMessage({
                    type:3,
                    msg:"图片最多选择12张！",
                    isAutoDisplay:true,
                    time:1500
                });
                dm.render();
                return;
            }
            var d = new Dialog({
                context_path: CONTEXT_PATH,
                resType: 1,
                callback: function (url,res) {
                    if(!$.isEmptyObject(url)){
                        addImg(url);
                    }
                }
            });
            d.render();
        });

        $("#explainTemplate ").delegate(".imgResList li img", "click", function () {
            var li = $(this).parent();
            var _this = $(this);
            var d = new Dialog({
                context_path: CONTEXT_PATH,
                resType: 1,
                callback: function (url,res) {
                    if(!$.isEmptyObject(url)){
                        var  url=  jumi.picParse(url,720);
                        li.data(res);
                        _this.attr("src",url);
                    }
                }
            });
            d.render();
        });

        $("#explainTemplate ").delegate(".imgResList li .del", "click", function () {
            var li = $(this).parent();
            li.remove();
        });
    };

    //<!-- 保存回收说明 -->//
    var _addRecycleExplain = function(){
        debugger;
        var img = $(".img");
        var imgUrl = "";
        for (var i =0;i<img.length;i++){
            imgUrl += $(img[i]).find("img").attr("src")+",";
        }
        var explainContainer = ue.ueA.getContent();
        var explainContainer1 = ue.ueA1.getContent();
        var recycleExplainCo = {};
        recycleExplainCo.imgUrl = imgUrl;
        recycleExplainCo.module1 = $("#module1").val();
        recycleExplainCo.module2 = $("#module2").val();
        recycleExplainCo.node = explainContainer;
        recycleExplainCo.usingModule1 = $('input:checkbox[name="usingModule1"]:checked').val();
        recycleExplainCo.usingModule2 = $('input:checkbox[name="usingModule2"]:checked').val();
        recycleExplainCo.node1 = explainContainer1;
        var explainStr = JSON.stringify(recycleExplainCo);
        var url = ajaxUrl.url13 ;
        $.ajaxJson(url,explainStr,{
            "done":function(res){
                if(res.code===0){
                    var dm = new dialogMessage({
                        fixed:true,
                        msg:'保存成功！',
                        isAutoDisplay:true,
                        time:1500
                    });
                    dm.render();
                }else{
                    var dm = new dialogMessage({
                        fixed:true,
                        msg:'保存失败！',
                        isAutoDisplay:true,
                        time:1500
                    });
                    dm.render();
                }
            }
        })
    };

    //<!--回收网点-->
    var _recovery_point = function(pageparam){
        var RecycleNodeQo = {};
        RecycleNodeQo.type = 1;
        jumi.pagination("#"+pageparam.pageToolbarObj,pageparam.url,RecycleNodeQo,function (res,curPage) {
            if (res.code === 0) {
                //判断是否第一页
                var data = {
                    items:res.data.items,
                };
                jumi.template(pageparam.template, data, function (tpl) {
                    $("#" + pageparam.tableBodyObj).empty();
                    $("#" + pageparam.tableBodyObj).html(tpl);
                })
            }
        })
    };

    var openRecoveryPoint = function(data){
        var data = {
            items:data.items,
        }
        jumi.template('/order/pc/recoveryPoint',data, function (tpl) {
            $("#contentBox").empty();
            $("#contentBox").html(tpl);
        });
    };

    //<!--公益网点-->
    var _public_network = function(pageparam){
        var RecycleNodeQo = {};
        RecycleNodeQo.type = 2;
        jumi.pagination("#"+pageparam.pageToolbarObj,pageparam.url,RecycleNodeQo,function (res,curPage) {
            if (res.code === 0) {
                //判断是否第一页
                var data = {
                    items:res.data.items,
                };
                jumi.template(pageparam.template, data, function (tpl) {
                    $("#" + pageparam.tableBodyObj).empty();
                    $("#" + pageparam.tableBodyObj).html(tpl);
                })
            }
        })
    };

    var openPublicNetwork = function(data){
        var data = {
            items:data.items,
        }
        jumi.template('/order/pc/publicNetwork',data, function (tpl) {
            $("#contentBox").empty();
            $("#contentBox").html(tpl);
        });
    };

    //<!--奖励方式-->
    var _reward_modes = function(){
        var url=ajaxUrl.url6;
        $.ajaxJsonGet(url,null,{
            "done":function (res) {
                if(res.code===0){
                    openRewardModes(res.data);
                }
            }
        });
    };

    var openRewardModes = function(data){
        var data = {
            items:data,
        };
        jumi.template('/order/pc/rewardMode', data, function (tpl) {
            $("#contentBox").empty();
            $("#contentBox").html(tpl);
            $('.addRewardMode').bind('click',addRewardMode);
            $('.addSpecData').bind('click',addSpecData);
        });
    };
    /////////////////////////////////////////回收网点//////////////////////////////////////////

    ///////所属地区start
    var chooseShopArea = function(){
    };
    chooseShopArea.prototype = {
        init:function(){
            this._renderArea();
        },
        _renderArea:function(){
            var addrval =$('#addresshid').text();
            var $distpicker = $('#distpickerentt');
            if(addrval!==''&&addrval!==null){
                var addrarr = addrval.split(',');
                $distpicker.distpicker({
                    province:addrarr[0],
                    city: addrarr[1],
                    district:addrarr[2],
                    autoSelect: false
                });
            }else{
                $distpicker.distpicker({
                    province:'',
                    city: '',
                    district:'',
                    autoSelect: false
                });
            }
        },
        _onBindClick:function(){


        },
        _offBindClick:function(){

        }
    };

    //新增回收网点(弹出窗)
    var _addRecoveryPoint=function () {
        jumi.template('order/pc/recoveryPointEdit', function (tpl){
            var dg = dialog({
                title: '回收网点',
                content: tpl,
                width:1000,
                id:'dialog_m',
                onshow: function () {
                    _binddatetime();
                    ///////门店照片
                    _shop_entity_photo();
                    ///////所属地区
                    var chooseShopAreaobj = new chooseShopArea();
                    chooseShopAreaobj.init();
                    ///////地图初始化并定位
                    initMapConverseReAddress("allmap",11,"pointll");
                    ///////文本失焦定位地图
                    _recovery_addr_search();
                },
                cancelValue: '关闭',
                cancel: function () {
                    dialog.get('dialog_m').close().remove();
                },
                okValue: '保存',
                ok: function() {
                    this.title('提交中…');
                    var form = _validate1();
                    if (form.valid()) {
                        _order_recovery_item_add();
                    } else {
                        $('body').animate({scrollTop:0},1000);
                    }
                    return false;
                },
                onclose: function () {
                    dialog.get('dialog_m').close().remove();
                },
                onremove: function () {
                    //jumi.msg('对话框已销毁');
                }
            });
            dg.showModal();
        });
    };

    ///时间控件
    var _binddatetime = function () {
        $("#beginTimeNode").timepicker({
            timeOnly:true,
            showSecond : false,
            timeFormat : 'hh:mm',
            dateFormat : 'yy-mm-dd',
            stepHour : 1,
            stepMinute : 1,
            stepSecond : 1
        });
        $("#endTimeNode").timepicker({
            timeOnly:true,
            showSecond : false,
            timeFormat : 'hh:mm',
            dateFormat : 'yy-mm-dd',
            stepHour : 1,
            stepMinute : 1,
            stepSecond : 1
        });
    };

    var _setDate = function(){
        $("#publicNetworkDate").datetimepicker({
            showSecond : true,
            timeFormat : 'hh:mm:ss',
            dateFormat : 'yy-mm-dd',
            stepHour : 1,
            stepMinute : 1,
            stepSecond : 1
        });
    };

    ///////门店照片start
    var _shop_entity_photo=function(){
        $('#recovery_xy').click(function(){
            var d = new Dialog({
                context_path:CONTEXT_PATH, //请求路径,  必填
                resType:1 ,//图片1，视频2，语音3   必填
                callback:function(url){
                    $('#imgUrl').attr('src',url);
                }
            });
            d.render();
        });
    }
    ///////门店照片end

    ////失去焦点定位地图位置start
    var _recovery_addr_search=function () {
        $('#address').blur(function (){
            var province =$("#provinceentt").val();
            var city =$("#cityenttt").val();
            var district=$("#districtentt").val()
            var address=$("#address").val();
            var pcdaddress =  province+city+district+address;
            initMapReAddress("allmap",11,pcdaddress,city,"pointll");
        });
    };

    //获取页面数据保存入库
    var _order_recovery_item_add=function(){
        var pointll = $("#pointll").text();
        var arrll = pointll.split(",");
        var RecycleNodeCo = {};
        var type = $('#selectType option:selected') .val();
        RecycleNodeCo.type = type;
        RecycleNodeCo.name=$("#name").val();
        RecycleNodeCo.linkPhone=$("#linkPhone").val();
        RecycleNodeCo.provinceCode=$("#provinceentt").find("option:selected").attr('data-code');
        RecycleNodeCo.cityCode=$("#cityentt").find("option:selected").attr('data-code');
        RecycleNodeCo.districtCode=$("#districtentt").find("option:selected").attr('data-code');
        RecycleNodeCo.province=$("#provinceentt").val();
        RecycleNodeCo.city=$("#cityentt").val();
        RecycleNodeCo.district=$("#districtentt").val();
        RecycleNodeCo.address=$("#address").val();
        RecycleNodeCo.businessTimeBegin=$("#beginTimeNode").val();
        RecycleNodeCo.businessTimeEnd=$("#endTimeNode").val();
        RecycleNodeCo.imgUrl=$("#imgUrl").attr("src");
        RecycleNodeCo.longitude=arrll[0];//经度
        RecycleNodeCo.latitude=arrll[1];//纬度
        RecycleNodeCo.title = $("#title").val(); //标题
        var url = ajaxUrl.url8;
        $.ajaxJson(url,RecycleNodeCo,{
            "done":function (res) {
                if(res.code===0){
                    var dm = new dialogMessage({
                        fixed:true,
                        msg:'保存成功！',
                        isAutoDisplay:true,
                        time:1500
                    });
                    dm.render();
                    dialog.get('dialog_m').close().remove();
                    _refreshPage(pageparam[0]);
                }else{
                    var dm = new dialogMessage({
                        fixed:true,
                        msg:'保存失败！',
                        isAutoDisplay:true,
                        time:1500
                    });
                    dm.render();
                }
            }
        });
    };

    //删除回收网点
    var _delRecoveryNode = function(id){
        var args = {};
        args.fn1 = function(){
            var url = ajaxUrl.url11+id;
            $.ajaxJsonDel(url,null,{
                "done":function(res){
                    if(res.code === 0){
                        var dm = new dialogMessage({
                            fixed:true,
                            msg:'删除成功！',
                            isAutoDisplay:true,
                            time:1500
                        });
                        dm.render();
                        _refreshPage(pageparam[0]);
                    }
                }
            })
        };
        args.fn2 = function(){

        };
        jumi.dialogSure('确定删除该回收网点吗?',args);
    }

    //删除公益网点
    var _delPublicNetworkNode = function(id){
        var args = {};
        args.fn1 = function(){
            var url = ajaxUrl.url11+id;
            $.ajaxJsonDel(url,null,{
                "done":function(res){
                    if(res.code === 0){
                        var dm = new dialogMessage({
                            fixed:true,
                            msg:'删除成功！',
                            isAutoDisplay:true,
                            time:1500
                        });
                        dm.render();
                        _refreshPage(pageparam[1]);
                    }
                }
            })
        };
        args.fn2 = function(){

        };
        jumi.dialogSure('确定删除该公益网点吗?',args);
    }

    //修改回收网点
    var _updateRecoveryNode = function(id){
        var url = ajaxUrl.url12+id;
        $.ajaxJsonGet(url,null,{
            "done":function(res){
                openRecoveryNode(res.data);
            }
        })
    };

    var openRecoveryNode = function(data){
        var data = {
            item: data,
        }
        jumi.template('/order/pc/recoveryPointEdit',data, function (tpl) {
            var dg = dialog({
                title: '回收网点',
                content: tpl,
                width:1000,
                id:'dialog_m',
                onshow: function () {
                    _binddatetime();
                    ///////门店照片
                    _shop_entity_photo();
                    ///////所属地区
                    var chooseShopAreaobj = new chooseShopArea();
                    chooseShopAreaobj.init();
                    ///////地图初始化并定位
                    initMapConverseReAddress("allmap",11,"pointll");
                    var lnglat =$('#defaultlnglat').text();
                    var lnglatarr = lnglat.split(',');
                    if(lnglatarr[0]!==''&&lnglatarr[1]!==''){
                        theLocationBylnglat(13,lnglatarr[0],lnglatarr[1],"pointll");
                    }
                    ///////文本失焦定位地图
                    _recovery_addr_search();
                },
                cancelValue: '关闭',
                cancel: function () {
                    dialog.get('dialog_m').close().remove();
                },
                okValue: '保存',
                ok: function() {
                    this.title('提交中…');
                    var form = _validate1();
                    if (form.valid()) {
                        _order_recovery_item_update();
                    } else {
                        $('body').animate({scrollTop:0},1000);
                    }
                    return false;
                },
                onclose: function () {
                    dialog.get('dialog_m').close().remove();
                },
                onremove: function () {
                    //jumi.msg('对话框已销毁');
                }
            });
            dg.showModal();
        });
    };

    //修改完回收网点保存数据
    var _order_recovery_item_update=function(){
        var pointll = $("#pointll").text();
        var arrll = pointll.split(",");
        var RecycleNodeUo = {};
        var type = $('#selectType option:selected') .val();
        RecycleNodeUo.id = $("#id").val();
        RecycleNodeUo.shopId = $("#shopId").val();
        RecycleNodeUo.type = type;
        RecycleNodeUo.name=$("#name").val();
        RecycleNodeUo.linkPhone=$("#linkPhone").val();
        RecycleNodeUo.provinceCode=$("#provinceentt").find("option:selected").attr('data-code');
        RecycleNodeUo.cityCode=$("#cityentt").find("option:selected").attr('data-code');
        RecycleNodeUo.districtCode=$("#districtentt").find("option:selected").attr('data-code');
        RecycleNodeUo.province=$("#provinceentt").val();
        RecycleNodeUo.city=$("#cityentt").val();
        RecycleNodeUo.district=$("#districtentt").val();
        RecycleNodeUo.address=$("#address").val();
        RecycleNodeUo.businessTimeBegin=$("#beginTimeNode").val();
        RecycleNodeUo.businessTimeEnd=$("#endTimeNode").val();
        RecycleNodeUo.imgUrl=$("#imgUrl").attr("src");
        RecycleNodeUo.longitude=arrll[0];//经度
        RecycleNodeUo.latitude=arrll[1];//纬度
        RecycleNodeUo.status = $("#status").val(); //
        RecycleNodeUo.title = $("#title").val(); //标题
        if(!arrll[0]){
            RecycleNodeUo.longitude = $("#longitude").val();
        }
        if(!arrll[1]){
            RecycleNodeUo.latitude = $("#latitude").val();
        }
        var url = ajaxUrl.url11+$("#id").val();
        $.ajaxJsonPut(url,RecycleNodeUo,{
            "done":function (res) {
                if(res.code===0){
                    var dm = new dialogMessage({
                        fixed:true,
                        msg:'保存成功！',
                        isAutoDisplay:true,
                        time:1500
                    });
                    dm.render();
                    dialog.get('dialog_m').close().remove();
                    _refreshPage(pageparam[0]);
                }else{
                    var dm = new dialogMessage({
                        fixed:true,
                        msg:'保存失败！',
                        isAutoDisplay:true,
                        time:1500
                    });
                    dm.render();
                }
            }
        });
    };

    //////////////////////////////////////////////////////////////////////////////

    //刷新当前页
    var _refreshPage = function(pageparam){
        var pageTypeToolbar_page = $("#pageTypeToolbar_page").val();
        var RecycleNodeQo = {};
        RecycleNodeQo.curPage = pageTypeToolbar_page;
        RecycleNodeQo.pageSize = 10;
        RecycleNodeQo.type = pageparam.type;
        jumi.pagination("#"+pageparam.pageToolbarObj,pageparam.url,RecycleNodeQo,function (res,curPage) {
            if (res.code === 0) {
                //判断是否第一页
                var data = {
                    items:res.data.items,
                };
                jumi.template(pageparam.template, data, function (tpl) {
                    $("#" + pageparam.tableBodyObj).empty();
                    $("#" + pageparam.tableBodyObj).html(tpl);
                })
            }
        })
    }


    /////////////////////////////////////////公益网点//////////////////////////////////////////

    var _ueEditB = function(content){
        ue.ueB =null;
        var richtextId= "publicNetworkContainer" +_getid();
        $("#publicNetworkContainerId").find(".publicNetworkContainer").attr("id",richtextId);
        ue.ueB = UE.getEditor(richtextId, {
            enableAutoSave: false,
            initialFrameHeight: 500,
            imageScaleEnabled: false
        });
        ue.ueB.ready(function () {
            //设置编辑器的内容
        });
    };

    //新增公益网点(弹出窗)
    var _addPublicNetwork=function () {
        jumi.template('order/pc/publicNetworkEdit', function (tpl){
            var dg = dialog({
                title: '捐赠网点',
                content: tpl,
                width:600,
                id:'dialog_m',
                onshow: function () {
                    _setDate();
                    _ueEditB("");
                    ///////门店照片
                    _shop_entity_photo();
                    ///////所属地区
                    var chooseShopAreaobj = new chooseShopArea();
                    chooseShopAreaobj.init();
                },
                cancelValue: '关闭',
                cancel: function () {
                    dialog.get('dialog_m').close().remove();
                },
                okValue: '保存',
                ok: function() {
                    this.title('提交中…');
                    var form = _validate2();
                    if (form.valid()) {
                        _order_public_network_item_add();
                    } else {
                        $('body').animate({scrollTop:0},1000);
                    }
                    return false;
                },
                onclose: function () {
                    dialog.get('dialog_m').close().remove();
                },
            });
            dg.showModal();
        });

    };

    var _order_public_network_item_add = function(){
        var RecycleNodeCo = {};
        RecycleNodeCo.type = 2;
        RecycleNodeCo.name=$("#name").val();
        RecycleNodeCo.linkUser=$("#linkUser").val();
        RecycleNodeCo.linkPhone=$("#linkPhone").val();
        RecycleNodeCo.provinceCode=$("#provinceentt").find("option:selected").attr('data-code');
        RecycleNodeCo.cityCode=$("#cityentt").find("option:selected").attr('data-code');
        RecycleNodeCo.districtCode=$("#districtentt").find("option:selected").attr('data-code');
        RecycleNodeCo.province=$("#provinceentt").val();
        RecycleNodeCo.city=$("#cityentt").val();
        RecycleNodeCo.district=$("#districtentt").val();
        RecycleNodeCo.address=$("#address").val();
        RecycleNodeCo.publicNetworkDate=$("#publicNetworkDate").val();
        RecycleNodeCo.imgUrl=$("#imgUrl").attr("src");
        RecycleNodeCo.instruction = ue.ueB.getContent();
        RecycleNodeCo.node = $("#node").val();
        var recycleNodeCoStr = JSON.stringify(RecycleNodeCo);
        var url = ajaxUrl.url8;
        $.ajaxJson(url,recycleNodeCoStr,{
            "done":function (res) {
                if(res.code===0){
                    var dm = new dialogMessage({
                        fixed:true,
                        msg:'保存成功！',
                        isAutoDisplay:true,
                        time:1500
                    });
                    dm.render();
                    dialog.get('dialog_m').close().remove();
                    _refreshPage(pageparam[1]);
                }else{
                    var dm = new dialogMessage({
                        fixed:true,
                        msg:'保存失败！',
                        isAutoDisplay:true,
                        time:1500
                    });
                    dm.render();
                }
            }
        });
    }

    //////修改保存公益网点
    var _order_public_network_item_update = function(){
        var RecycleNodeUo = {};
        RecycleNodeUo.type = 2;
        RecycleNodeUo.id = $("#id").val();
        RecycleNodeUo.shopId = $("#shopId").val();
        RecycleNodeUo.name=$("#name").val();
        RecycleNodeUo.linkUser=$("#linkUser").val();
        RecycleNodeUo.linkPhone=$("#linkPhone").val();
        RecycleNodeUo.provinceCode=$("#provinceentt").find("option:selected").attr('data-code');
        RecycleNodeUo.cityCode=$("#cityentt").find("option:selected").attr('data-code');
        RecycleNodeUo.districtCode=$("#districtentt").find("option:selected").attr('data-code');
        RecycleNodeUo.province=$("#provinceentt").val();
        RecycleNodeUo.city=$("#cityentt").val();
        RecycleNodeUo.district=$("#districtentt").val();
        RecycleNodeUo.address=$("#address").val();
        RecycleNodeUo.publicNetworkDate=$("#publicNetworkDate").val();
        RecycleNodeUo.imgUrl=$("#imgUrl").attr("src");
        RecycleNodeUo.instruction = ue.ueB.getContent();
        RecycleNodeUo.status = $("#status").val();
        RecycleNodeUo.node = $("#node").val();
        var recycleNodeUoStr = JSON.stringify(RecycleNodeUo);
        var url = ajaxUrl.url14+$("#id").val();
        $.ajaxJsonPut(url,recycleNodeUoStr,{
            "done":function (res) {
                if(res.code===0){
                    var dm = new dialogMessage({
                        fixed:true,
                        msg:'保存成功！',
                        isAutoDisplay:true,
                        time:1500
                    });
                    dm.render();
                    dialog.get('dialog_m').close().remove();
                    _public_network(pageparam[1]);
                }else{
                    var dm = new dialogMessage({
                        fixed:true,
                        msg:'保存失败！',
                        isAutoDisplay:true,
                        time:1500
                    });
                    dm.render();
                }
            }
        });
    }

    ////修改公益网点/////
    var _updatePublicNetwork=function (id) {
        var url = ajaxUrl.url12+id;
        $.ajaxJsonGet(url,null,{
            "done":function(res){
                openUpdateaPublicNetwork(res.data);
            }
        })
    };

    var openUpdateaPublicNetwork = function(data){
        var data = {
            item:data,
        }
        jumi.template('order/pc/publicNetworkEdit',data, function (tpl){
            var dg = dialog({
                title: '公益网点',
                content: tpl,
                width:600,
                id:'dialog_m',
                onshow: function () {
                    _setDate();
                    /////ue显示
                    _ueEditB(data.item.instruction);
                    ///////门店照片
                    _shop_entity_photo();
                    ///////所属地区
                    var chooseShopAreaobj = new chooseShopArea();
                    chooseShopAreaobj.init();
                },
                cancelValue: '关闭',
                cancel: function () {
                    dialog.get('dialog_m').close().remove();
                },
                okValue: '保存',
                ok: function() {
                    this.title('提交中…');
                    var form = _validate2();
                    if (form.valid()) {
                        _order_public_network_item_update();
                    } else {
                        $('body').animate({scrollTop:0},1000);
                    }
                    return false;
                },
                onclose: function () {
                    dialog.get('dialog_m').close().remove();
                },
            });
            dg.showModal();
        });
    };
    /////////////////////////////////////////////////////////////////////////////////////////////


    var  _getid= function(){
        var id =  new Date().getTime();
        return id;
    }

    var _ueEditA = function(content){
        ue.ueA =null;
        var richtextId= "explainContainer" +_getid();
        $("#explainTemplate").find(".explainContainer").attr("id",richtextId);
        ue.ueA = UE.getEditor(richtextId, {
            enableAutoSave: false,
            initialFrameHeight: 500,
            imageScaleEnabled: false
        });
        ue.ueA.ready(function () {
            ue.ueA.setContent(content);
            //设置编辑器的内容
        });
    };

    var _ueEditA1 = function(content){
        ue.ueA1 =null;
        var richtextId= "explainContainer1" +_getid();
        $("#explainTemplate").find(".explainContainer1").attr("id",richtextId);
        ue.ueA1 = UE.getEditor(richtextId, {
            enableAutoSave: false,
            initialFrameHeight: 500,
            imageScaleEnabled: false
        });
        ue.ueA1.ready(function () {
            ue.ueA1.setContent(content);
            //设置编辑器的内容
        });
    };

    var _ueEditB = function(content){
        ue.ueB =null;
        var richtextId= "publicNetworkContainer" +_getid();
        $("#publicNetworkContainerId").find(".publicNetworkContainer").attr("id",richtextId);
        ue.ueB = UE.getEditor(richtextId, {
            enableAutoSave: false,
            initialFrameHeight: 500,
            imageScaleEnabled: false
        });
        ue.ueB.ready(function () {
            ue.ueB.setContent(content);
            //设置编辑器的内容
        });
    };

    //保存地区限定
    var addOrderBookArea = function () {
        var areaLimit = $('input[name="areaLimit"]:checked').val();
        var OrderBookAreaCo = {};
        OrderBookAreaCo.areaLimit = areaLimit;
        var url=ajaxUrl.url7;
        $.ajaxJson(url,OrderBookAreaCo,{
            "done":function(res){
                if(res.code===0){
                    var dm = new dialogMessage({
                        fixed:true,
                        msg:'保存成功！',
                        isAutoDisplay:true,
                        time:1500
                    });
                    dm.render();
                }else{
                    var dm = new dialogMessage({
                        fixed:true,
                        msg:'保存失败！',
                        isAutoDisplay:true,
                        time:1500
                    });
                    dm.render();
                }
            }
        });
    };

    var addSpecData = function() {
        jumi.template("order/pc/rewardModeAdd",function (tpl) {
            $("#specData").append(tpl);
        });
    };

    var _delSpecData = function(v) {
        $(v).parent().parent().remove();
    };

    //保存奖励方式
    var addRewardMode = function(){
        var RewardModeCo = {};
        var recycleWeightConfigVoList = [];
        var rewardType = $('input[name="rewardType"]:checked').val();
        if(rewardType==1){
            _errMsg("红包奖励方式暂未开放！")
            return ;
        }
        var showType = $('input[name="showType"]:checked').val();
        var allData = $(".table-container");
        for (var i=0 ;i<allData.length;i++){
            var recycleWeightConfigVo = {};
            var weight = $(allData[i]).find("[name='weight']").val();
            var userCount = $(allData[i]).find("[name='reward']").val();
            var upperCount = $(allData[i]).find("[name='upReward']").val();
            recycleWeightConfigVo.weight = weight;
            recycleWeightConfigVo.userCount = userCount;
            recycleWeightConfigVo.upperCount = upperCount;
            recycleWeightConfigVoList.push(recycleWeightConfigVo);
        }
        RewardModeCo.rewardType = rewardType;
        RewardModeCo.showType = showType;
        RewardModeCo.recycleWeightConfigVoList = recycleWeightConfigVoList;
        var url=ajaxUrl.url10;
        $.ajaxJson(url,RewardModeCo,{
            "done":function(res){
                if(res.data.code===1){
                    var dm = new dialogMessage({
                        fixed:true,
                        msg:'保存成功！',
                        isAutoDisplay:true,
                        time:1500
                    });
                    dm.render();
                }else{
                    var dm = new dialogMessage({
                        fixed:true,
                        msg:'保存失败！',
                        isAutoDisplay:true,
                        time:1500
                    });
                    dm.render();
                }
            }
        });
    };

    /********************************************/
    /**
     * 新增服务地区
     * @private
     */
    var _editServiceArea = function(id,areaCode,recAddress,kdId){
        var url = CONTEXT_PATH+"/orderBookArea/used_area"; //查询所有已用的地区
        $.ajaxJsonGet(url,null,{
            done:function(res) {
                if(res.data.code==0){
                    if(areaCode!=undefined){
                        var area = res.data.msg; //已用地址
                        if(area.substring(0,1)==","){  //假如第一位是逗号
                            area = area.substring(1,area.length);
                        }
                        if(area.substring(area.length-1,area.length)==","){  //假如最后一位逗号
                            area = area.substring(0,area.length-1);
                        }
                        common_area.setDisabledAreaSelect(area); //设置不可选择
                        _cannelDisabled(areaCode);
                    }else{
                        common_area.setDisabledAreaSelect(res.data.msg);
                    }
                }
            }});
        var elem = $('#serviceArea');
        dialog({
            id:'areaDialogId',
            content: elem,
            title: '地区选择',/*
            cancel:function(){
                $('#serviceArea').remove();
                dialog.get('areaDialogId').close().remove();
            },*/
            onshow:function(){
                $("#orderBookAreaId").val(id);
                $("#recAddress").val(recAddress);
                $("#kdSelect").val(kdId);
            }
        }).width(700).height(620).showModal();
    };


    /**
     * 保存服务地区
     * @private
     */
    var _saveServiceArea = function(){
        var treeObj = $.fn.zTree.getZTreeObj("area_tree");
        var nodes = treeObj.getCheckedNodes(true); //获取选中的数据
        var allNames = "";
        for (var i = 0; i < nodes.length; i++) {
            allNames += nodes[i].name+ ",";
        }
        var areaIds = common_area.getAreaIds(nodes);
        var areaNames = common_area.getAreaNames(nodes);
        var id = $("#orderBookAreaId").val();
        var recAddress = $("#recAddress").val();
        var kdId = $("#kdSelect option:selected").val();
        var kdName = $("#kdSelect option:selected").text();
        if(kdId==""){
            _errMsg("请选择快递");
            return
        }
        if(recAddress==""){
            _errMsg("请填写收货地址");
            return
        }
        if(areaIds==false ||areaNames==false){
            _errMsg("请选择地区");
            return
        }
        var co ={};
        co.areaCode = areaIds;
        co.areaName = areaNames;
        co.allNames = allNames.substring(0,allNames.length-1);
        co.recAddress = recAddress;
        co.kdId = kdId;
        co.kdName = kdName;
        if(id!=false){
            co.id = id;
        }
        var url = CONTEXT_PATH+"/orderBookArea/save_service_area";
        var data = JSON.stringify(co);
        $.ajaxJson(url,data, {
            done: function (res) {
                if(res.data.code==0){
                    var dm = new dialogMessage({
                        type:1,
                        fixed:true,
                        msg:res.data.msg,
                        isAutoDisplay:true,
                        time:3000
                    });
                    dm.render();
                    dialog.get('areaDialogId').close().remove();
                    $('#serviceArea').remove();
                    _order_book_area_add(pageparam[2]);
                }
            }
        });

    };


    /**
     * 删除服务地区
     * @private
     */
    var _delServiceArea = function(id){
        var url = CONTEXT_PATH+"/orderBookArea/del_service_area/"+id;
        $.ajaxJsonDel(url,null, {
            "done": function (res) {
                if(res.data.code==0){
                    var dm = new dialogMessage({
                        type:1,
                        fixed:true,
                        msg:res.data.msg,
                        isAutoDisplay:true,
                        time:3000
                    });
                    dm.render();
                    _order_book_area_add(pageparam[2]);
                }
            }
        });
    };


    /**
     * 保存快递
     * @private
     */
    var _saveKd = function(id){
        var kdId = $("#kdSelect"+id+" option:selected").val();
        var kdName = $("#kdSelect"+id+" option:selected").text();
        var url = CONTEXT_PATH+"/orderBookArea/update_service_kd/"+id;
        var uo = {};
        uo.kdId = kdId;
        uo.kdName = kdName;
        $.ajaxJsonPut(url,uo, {
            "done": function (res) {
                if(res.data.code==0){
                    var dm = new dialogMessage({
                        type:1,
                        fixed:true,
                        msg:res.data.msg,
                        isAutoDisplay:true,
                        time:3000
                    });
                    dm.render();
                    $("#kdName"+id).text(kdName);
                    _hideKdDialog(id);
                }
            }
        });
    };

    var _showDetail = function(vthis,title){
        var val = $(vthis).text();
        var dg = dialog({
            title: title,
            content: val,
            id: 'dialog_title',
            cancelValue: '关闭',
            cancel: function () {
                dialog.get('dialog_title').close().remove();
            },
            width: 600
        });
        dg.show();
    };

    var _showKdDialog = function(id){
        $(".m-btn-layernew").hide();
        $("#kdDialog"+id).show();
    };

    var _hideKdDialog = function(id){
        $("#kdDialog"+id).hide();
    };

    //取消禁用
    var _cannelDisabled = function(str){
        var treeObj = $.fn.zTree.getZTreeObj("area_tree");
        if (str!=null&&str!="") {
            var nodesArr = str.split(",");
            for (var i = 0, l = nodesArr.length; i < l; i++) {
                var node = treeObj.getNodeByParam("id", nodesArr[i], null);
                treeObj.checkNode(node, true, false, false);
                treeObj.setChkDisabled(node, false);
            }
        }
    };

    /*错误提示*/
    var _errMsg = function(msg){
        var dm = new dialogMessage({
            type:2,
            title:'操作提醒',
            fixed:true,
            msg:msg,
            isAutoDisplay:false

        });
        dm.render();
    };

    /*********************************************************/

    var _updateRecoveryStatus = function(id,status,type){
        var url = ajaxUrl.url15+id;
        var RecycleNodeUo = {};
        RecycleNodeUo.status = status;
        $.ajaxJsonPut(url,RecycleNodeUo,{
            "done":function (res) {
                if(res.code===0){
                    var dm = new dialogMessage({
                        fixed:true,
                        msg:'保存成功！',
                        isAutoDisplay:true,
                        time:1500
                    });
                    dm.render();
                    if(Number(type)===0){
                        _refreshPage(pageparam[0]);
                    }else{
                        _refreshPage(pageparam[1]);
                    }
                }else{
                    var dm = new dialogMessage({
                        fixed:true,
                        msg:'保存失败！',
                        isAutoDisplay:true,
                        time:1500
                    });
                    dm.render();
                }
            }
        });
    };

    var _validate1=function () {
        var form = $("#recycle_Node_Entity_Form");
        //处理模块验证信息
        form.validate({
            rules: {
                name: {
                    required: true,
                },
                title:{
                    required: true,
                },
                linkPhone: {
                    required: true,
                },
                address:{
                    required: true,
                },
                beginTimeNode:{
                    required: true,
                },
                endTimeNode:{
                    required: true,
                },
                imgUrl:{
                    required: true,
                }
            },
            messages: {
                name: {
                    required: '请输入类型名称',
                },
                title:{
                    required: '请输入标题',
                },
                linkPhone: {
                    required: '请输入手机号',
                },
                address:{
                    required: '请输入详细地址',
                },
                beginTimeNode:{
                    required: '请输入营业时间',
                },
                endTimeNode:{
                    required: '请输入营业时间',
                },
                imgUrl:{
                    required:'请选择图片',
                }

            }
        });
        return form;
    }

    var _validate2=function () {
        var form = $("#public_Node_Entity_Form");
        //处理模块验证信息
        form.validate({
            rules: {
                name: {
                    required: true,
                },
                linkUser:{
                    required: true,
                },
                linkPhone: {
                    required: true,
                },
                address:{
                    required: true,
                },
                node:{
                    required: true,
                },
                publicNetworkDate:{
                    required: true,
                },
                imgUrl:{
                    required: true,
                }
            },
            messages: {
                name: {
                    required: '请输入网点名称',
                },
                linkUser:{
                    required:'请输入联系姓名',
                },
                linkPhone: {
                    required: '请输入手机号',
                },
                address:{
                    required: '请输入详细地址',
                },
                node:{
                    required:'请输入简述',
                },
                publicNetworkDate:{
                    required: '请输入设立日期',
                },
                imgUrl:{
                    required:'请选择图片',
                }
            }
        });
        return form;
    }

    return {
        init :_init, //初始化
        editServiceArea:_editServiceArea,
        saveServiceArea:_saveServiceArea,
        delServiceArea:_delServiceArea,
        saveKd:_saveKd,
        showDetail:_showDetail,
        showKdDialog:_showKdDialog,
        hideKdDialog:_hideKdDialog,
        addRecoveryPoint:_addRecoveryPoint, //新增回收网点
        delRecoveryNode:_delRecoveryNode, //删除回收网点
        updateRecoveryNode:_updateRecoveryNode, //编辑回收网点弹窗
        addPublicNetwork:_addPublicNetwork, //新增公益网点
        recoveryExplainAddImg:_recoveryExplainAddImg, //网点新增图片
        updatePublicNetwork:_updatePublicNetwork, //编辑公益网点弹窗
        ueEditA:_ueEditA, //UE编辑器
        ueEditA1:_ueEditA1, //UE编辑器
        ueEditB:_ueEditB, //UE编辑器
        addRecycleExplain:_addRecycleExplain, //保存回收说明
        delPublicNetworkNode:_delPublicNetworkNode, // 删除公益网点
        updateRecoveryStatus:_updateRecoveryStatus, //上下架
        delSpecData:_delSpecData,
    };
})();