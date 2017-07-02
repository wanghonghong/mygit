CommonUtils.regNamespace("area", "dialog");

area.dialog = (function () {
    var pageSize = 10;
    var curPage = 0;

    var _init = function () {
        _dialogBind();
        _getProductAreaRel(_selectEvent);
    }
    var _dialogBind = function () {
        $(".panel-hidden").hide().eq(0).show();
        var tabul = $(".m-tab ul li");
        tabul.click(function () {
            tabul.eq($(this).index()).addClass("z-sel").siblings().removeClass('z-sel');
            $(".panel-hidden").hide().eq($(this).index()).show();
        });
        $("#offerRoles").select2({
            theme: "jumi"
        });
        _initCheck();
        common_area.initArea();
    }

    var _initCheck = function () {
        var offerType = $("#offer_type").val();
        if (offerType == 1) {
            _plantOffer();
            $("#radioBox1").attr("checked", "checked").click();
        } else if (offerType == 2) {
            _areaOffer();
            $("#radioBox2").attr("checked", "checked").click();
        } else {
            _areaOffer();
            $("#radioBox2").attr("checked", "checked").click();
        }
    }
    var _selectEvent = function (data) {
        var $eventSelect = $("#offerRoles");
        $eventSelect.on("change", function (e) {
            var id = $(this).val();
            // common_area.initArea();
            $("#selectAll").removeAttr("checked");
            $("#selectAll").removeAttr("sltReverse")
            var treeObj = $.fn.zTree.getZTreeObj("area_tree");
            treeObj.checkAllNodes(false);
            var nodesAll = treeObj.getNodes();  //清除disabled
            for (var j = 0, num = nodesAll.length; j < num; j++) {
                treeObj.setChkDisabled(nodesAll[j], false, true, true);
            }
            $("#offer_area_id").val("");
            $("#supply_price").val("");
            for (var i = 0, l = data.length; i < l; i++) {
                if (data[i].userId == id) {
                    $("#supply_price").val((parseFloat(data[i].supplyPrice))/100);
                    $("#offer_area_id").val(data[i].id);
                    common_area.setAreaSelect(data[i].areaCode);
                } else {
                    common_area.setDisabledAreaSelect(data[i].areaCode)
                }
            }
            //common_area.setAreaSelect(str);//获取选中可选的id
            // common_area.setDisabledAreaSelect("350000,350100,350101,350102,350103,350104,350105,350111,350121,350122,350123,350124,350125,350128,350181,350182,350200,350201,350203,350205,350206,350211,350212,350213,350300,350301,350302,350303,350304,350305,350322,350400,350401,350402,350403,350421,350423,350424,350425,350426,350427,350428,350429,350430,350481,350500,350501,350502,350503,350504,350505,350521,350524,350525,350526,350527,350581,350582,350583,350600,350601,350602,350603,350622,350623,350624,350625,350626,350627,350628,350629,350681,350700,350701,350702,350721,350722,350723,350724,350725,350781,350782,350783,350784,350800,350801,350802,350821,350822,350823,350824,350825,350881,350900,350901,350902,350921,350922,350923,350924,350925,350926,350981,350982");
            console.log(data);
        });
    }

    var _getProductAreaRel = function (callback) {
        var url = CONTEXT_PATH + "/good/product_area_offer/" + $("#dio_pid").val();
        $.ajaxJsonGet(url, null, {
            "done": function (res) {
                // console.log();
                if (typeof callback === "function") {
                    callback(res.data);
                }
            }
        })
    }
    var _initPagination = function () {
        var url = CONTEXT_PATH + "/good/product_area_offer"
        var productQo = {};
        productQo.pageSize = pageSize;
        productQo.curPage = curPage;
        productQo.pid = $("#dio_pid").val();
        var jsonStr = JSON.stringify(productQo);
        jumi.pagination("#pageToolbar_dialog", url, productQo, function (res, curPage) {
            if (res.code === 0) {
                //判断是否第一页
                var data = {
                    items: res.data.items,
                };
                if (curPage === 0) {
                    data.isFirstPage = 1;
                } else {
                    data.isFirstPage = 0;
                }
                curPage = curPage;
                jumi.template("product/area/offer_list_item", data, function (tpl) {
                    $("#tableBody_dialog").empty();
                    $("#tableBody_dialog").html(tpl);
                })
            }
        })
    }
    var _plantOffer = function () {
        $("#area_offer_div").hide();
        $("#plant_offer_div").show();
    }
    var _areaOffer = function () {
        $("#area_offer_div").show();
        $("#plant_offer_div").hide();
    }
    var _show_diolag = function (id) {
        var content = $("#" + id).html();
        var d = dialog({
            align: 'bottom',
            content: content,
            quickClose: true
        });
        d.show(document.getElementById(id));
    }

    var  _productarea_showMessage = function (info) {
        var dm = new dialogMessage({
            type: 3,
            title: '操作提醒',
            fixed: true,
            msg: info,
            isAutoDisplay: true,
            time: 2000
        });
        dm.render();
    }


    var _saveAreaOffer = function () {
        var treeObj = $.fn.zTree.getZTreeObj("area_tree");
        var nodes = treeObj.getCheckedNodes(true);
        var areaIds = common_area.getAreaIds(nodes);
        var areaNames = common_area.getAreaNames();

        if(areaIds==null || areaIds =="" || areaIds==undefined){
            _productarea_showMessage("请选择供货地区");
            return;
        }
        if(areaNames==null || areaNames =="" || areaNames==undefined){
            _productarea_showMessage("请选择供货地区");
            return;
        }

        var supplyPrice = $("#supply_price").val()*100;
        if(supplyPrice==null || supplyPrice =="" || supplyPrice==undefined){
            _productarea_showMessage("请输入供货价");
            return;
        }
        var id = $("#offer_area_id").val();
        var offerType = $("input[name='offerType']:checked").val();
        $("#offer_type").val(offerType);
        var userId = $("#offerRoles").val();
        var pid = $("#dio_pid").val();
        var data = {
            pid: pid,
            areaCode: areaIds,
            areaName: areaNames,
            userId: userId,
            supplyPrice:supplyPrice
        }
        if (userId == null || userId == "") {
            _productarea_showMessage("请选择供货商");
            return;
        }
        console.log(data);
        if (id && id != "") {//修改
            data.id = id;
            var url = CONTEXT_PATH + "/good/product_area_offer/" + offerType;
            $.ajaxJsonPut(url, data, {
                "done": function (res) {
                    if (res.code == 0) {
                        var dm = new dialogMessage({
                            type: 1,
                            fixed: true,
                            msg: '操作成功',
                            isAutoDisplay: true,
                            time: 1500
                        });
                        dm.render();
                        _initData(userId);
                    } else {
                        var dm = new dialogMessage({
                            type: 2,
                            fixed: true,
                            msg: '操作失败',
                            isAutoDisplay: true,
                            time: 1500
                        });
                        dm.render();

                    }
                }
            })
        } else {//新增
            var url = CONTEXT_PATH + "/good/product_area_offer/" + offerType;
            $.ajaxJson(url, data, {
                "done": function (res) {
                    if (res.code == 0) {
                        var dm = new dialogMessage({
                            type: 1,
                            fixed: true,
                            msg: '操作成功',
                            isAutoDisplay: true,
                            time: 1500
                        });
                        dm.render();
                        _initData(userId);
                    } else {
                        var dm = new dialogMessage({
                            type: 2,
                            fixed: true,
                            msg: '操作失败',
                            isAutoDisplay: true,
                            time: 1500
                        });
                        dm.render();
                    }
                }
            })
        }

    }
    var _initData = function(userId){
        _getProductAreaRel(_selectEvent);
        $("#offerRoles").on("click", function () {
            var $eventSelect = $("#offerRoles");
            $eventSelect.val(userId).trigger("change");
        });
        product.area.search();
    }
    var _delete=function (id) {
        var url = CONTEXT_PATH+"/good/product_area_delete/"+id;
        $.ajaxJsonDel(url,null,{
            "done": function (res) {
                if (res.code == 0) {
                    var dm = new dialogMessage({
                        type: 1,
                        fixed: true,
                        msg: '操作成功',
                        isAutoDisplay: true,
                        time: 1500
                    });
                    dm.render();
                    _initPagination();
                } else {
                    var dm = new dialogMessage({
                        type: 2,
                        fixed: true,
                        msg: '操作失败',
                        isAutoDisplay: true,
                        time: 1500
                    });
                    dm.render();
                }
            }
        })
    }

    return {
        init: _init,//列表初始化
        plantOffer: _plantOffer,
        areaOffer: _areaOffer,
        getAreaOffer: _initPagination,//地区供货列表
        show_diolag: _show_diolag,//列表超出字数弹窗展示
        saveAreaOffer: _saveAreaOffer,
        selectEvent: _selectEvent,//下拉选中回填
        delete:_delete
        //selectEvent:_getProductAreaRel//获取当前商品供应商信息
    };
})();
