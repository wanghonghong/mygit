/**
 * Created by wxz on 2016/11/2
 * 运费模板
 */
CommonUtils.regNamespace("transtemp", "end");
transtemp.end = (function () {
    var optStatus = "add";  // add新增状态 edit编辑状态
    var ajaxUrl = {
        url1: CONTEXT_PATH + '/trans_temp',  //运费模板列表
        url2: CONTEXT_PATH + '/trans_temp/tempId',  //获取运费模板
        url3: CONTEXT_PATH + '/trans_temp/tempId', //删除大项
        url4: CONTEXT_PATH + '/trans_temp/trans_set'  //运费算法可选条件
    };

    //初始化
    var _init = function () {

    };

//选项卡切换
    var _transtemp_tabchange = function () {
        var tabul = $(".m-tab ul li");
        tabul.eq(0).removeClass('z-sel').addClass("z-sel");
        _temp_add();//新增初始化
        tabul.click(function () {
            var index = $(this).index();
            tabul.eq($(this).index()).addClass("z-sel").siblings().removeClass('z-sel');
            if(index===0){
                _temp_add();
            }
            if(index===1){
                _temp_list();
            }
            if(index===2){
                _temp_config();//配置页

            }
            _reply_friendmsg(index);

        });
    };

    //运费模板新增
    var _temp_add = function () {
            optStatus = "add";
        jumi.template('trans/transtemp_add', function (tpl) {
                $("#contentBox").empty();
                $("#contentBox").html(tpl);
        });
    };
    //模板配置项
    var _temp_config = function () {
        $.ajaxJsonGet(ajaxUrl.url4, {
            "done": function (res) {
                if (res.code == 0) {

                    jumi.template('trans/transtemp_config', res.data, function (tpl) {
                        $("#contentBox").empty();
                        $("#contentBox").html(tpl);
                    });
                }
            }
        });

    };


    var _reply_friendmsg = function (index) {
        switch (index) {
            case 3:
                $("#contentBox").html('<div class="m-jm-err"><img src="'+jumi.config.cssPath+'/css/pc/img/jm-indlp.png"> </div>');
                $("#contentBox").show();
                break;
        }
    };
//运费管理选项卡切换
    //初始化指定可配送区域
    var _forareatrans = function () {
        $("#forareatrans").click(function () {
            $("#onloadmsg").fadeIn('slow', function () {
                // Animation complete
                var treeObj = $.fn.zTree.getZTreeObj("area_tree");
                if (treeObj !== null) {
                    var nodesAll = treeObj.getNodes();  //清除disabled
                    for (var j = 0, num = nodesAll.length; j < num; j++) {
                        treeObj.setChkDisabled(nodesAll[j], false, true, true);
                    }
                    treeObj.checkAllNodes(false); // 清除上次操作
                    // _shop_setChkDisabled(treeObj);//设置选中节点状态
                    showDefaultArea(treeObj);//设置选中节点状态
                    _trans_showAreaTreeWindow();
                    $('#onloadmsg').fadeOut('100', function () {
                        // Animation complete.
                    });

                }
            });

        });
    };

    //获取已保存的值--回填
    function showDefaultArea(treeObj) {

        var defaultObj = $("div[id='transtemp_add'] ul[class$='transtempitemobj'] li p[class='showtitle']");

        if (defaultObj.length > 0) {
            defaultObj.each(function () {
                var pnodes = "";
                var str = $(this).attr("areaIds");
                if (isNull(str)&&str!=="-1") { //排除 未选地区统一运费模板
                    var nodesArr = str.split(",");
                    for (var i = 0, l = nodesArr.length; i < l; i++) {
                        var node = treeObj.getNodeByParam("id", nodesArr[i], null);
                        treeObj.checkNode(node, true, false, false);

                        treeObj.setChkDisabled(node, true);
                        var temp = nodesArr[i].substring(2, nodesArr[i].length);
                        if (temp === "0000") {
                            var parentnode = treeObj.transformToArray(node);
                            for (var j = 0, num = parentnode.length; j < num; j++) {
                                pnodes += parentnode[j].id + ",";
                            }
                        }
                    }
                    if (pnodes.length > 0) {
                        var unsel = $("#unselectednodes").text();
                        pnodes = pnodes.substring(0, pnodes.length - 1);
                        var newval = _.difference(pnodes.split(","), nodesArr);
                        var temp = newval.join(",");
                        unsel += temp;
                        $("#unselectednodes").text("");
                        $("#unselectednodes").text(unsel);
                    }
                }
            });
        }

        trans_setChkDisabled(treeObj);//设置选中节点状态
    }
    /**
     * 判断是否 为空
     * @param {Object} data
     */
    function isNull(data) {
        return (data == "" || data == undefined || data == null) ? false : true;
    }
    //设置选中
    var trans_setChkDisabled = function (treeObj) {

        var pnodes = "";
        var objval = $("#selectednodes").text();

        if (objval.length > 0) {
            var nodestr = objval.substring(0, objval.length - 1);
            var nodesarr = nodestr.split(",");
            for (var i = 0, l = nodesarr.length; i < l; i++) {
                var node = treeObj.getNodeByParam("id", nodesarr[i], null);
                treeObj.checkNode(node, true, false, false);
                treeObj.setChkDisabled(node, true);
                var temp = nodesarr[i].substring(2, nodesarr[i].length);
                if (temp === "0000") {
                    var parentnode = treeObj.transformToArray(node);
                    for (var j = 0, num = parentnode.length; j < num; j++) {
                        pnodes += parentnode[j].id + ",";
                    }
                }
            }
            pnodes = pnodes.substring(0, pnodes.length - 1);
            var newval = _.difference(pnodes.split(","), nodestr.split(","));
            var temp = newval.join(",");
            $("#unselectednodes").text("");
            $("#unselectednodes").text(temp);

        }
    }
    //全选
    var _stran_selectAll = function () {
        $("#selectAll").click(function () {
            var tag = $("#selectAll").prop("checked");
            var treeObj = $.fn.zTree.getZTreeObj("area_tree");
            $("#sltReverse").prop("checked", false);
            if (tag) {
                treeObj.checkAllNodes(true);
            } else {
                treeObj.checkAllNodes(false);
            }
            var unsltednodes = $("#unselectednodes").text();
            if (unsltednodes.length > 0) {
                var nodearr = unsltednodes.split(",");
                for (var k = 0, num = nodearr.length; k < num; k++) {
                    var node = treeObj.getNodeByParam("id", nodearr[k], null);
                    treeObj.checkNode(node, tag);
                }
            }
        });
    }
    //反选
    var _stran_selectReverse = function () {
        $("#sltReverse").click(function () {
            var tag = $("#sltReverse").prop("checked");
            $("#selectAll").prop("checked", false);
            var treeObj = $.fn.zTree.getZTreeObj("area_tree");
            var nodes = treeObj.getCheckedNodes(true);
            var unsltednodes = $("#unselectednodes").text();
            if (unsltednodes.length > 0) {
                var nodearr = unsltednodes.split(",");
                for (var k = 0, num = nodearr.length; k < num; k++) {
                    var node = treeObj.getNodeByParam("id", nodearr[k], null);
                    treeObj.checkNode(node, !node.checked);
                }
            }
            treeObj.checkAllNodes(true);
            if (nodes.length > 0) {
                for (var i = 0; i < nodes.length; i++) {
                    treeObj.checkNode(nodes[i], false, false, false);
                }
            }

        });
    }

    var _trans_showAreaTreeWindow = function () {
        $("#sltReverse").prop("checked", "");
        $("#selectAll").prop("checked", "");
        var elem = $('#dialogAreainfo');
        var dg = dialog({
            title: "选择可配送区域",
            content: elem,
            width: 500,
            id: "dg_tree",
            okValue: "确认",
            ok: function () {
                _save_TransTemp();
            },
            cancelValue: "取消",
            cancel: function () {

            }
        });
        dg.showModal();
    };

//保存可配送区域和运费
    var _save_TransTemp = function () {
        var treeObj = $.fn.zTree.getZTreeObj("area_tree");
        var nodes = treeObj.getCheckedNodes(true);
        var areaIds = "";
        var areaNames = "";
        var areatIds = "";
        if (nodes.length === 0) {
            var d = dialog({
                content: '请勾选配送地区！'
            });
            d.show();
            setTimeout(function () {
                d.close().remove();
            }, 1000);
            return;
        }
        for (var i = 0; i < nodes.length; i++) {
            if (i == nodes.length - 1) {
                areaIds += nodes[i].id;
            } else {
                areaIds += nodes[i].id + ",";
            }
            areatIds += nodes[i].id + ",";
        }
        var names = _getAreaNames();

        for (var i = 0; i < names.length; i++) {
            if (i == names.length - 1) {
                areaNames += names[i];
            } else {
                areaNames += names[i] + ",";
            }
        }
        var html = $("#first-table-container").clone();
        html.find("#dialogAreainfo").remove();
        html.css("display", "static");
        html.find("li").css("display", "static");
        html.removeAttr("id");
        html.find("li:first").append("<p class='showtitle' areaIds=" + areaIds + ">" + areaNames + "</p> <span class='areaidhid' style='display: none;'>" + areaIds + "</span>");

        $("#transTEM0").append(html);
        var idstr = $("#selectednodes").text();
        idstr += areatIds;
        $("#selectednodes").text("");
        $("#selectednodes").text(idstr);
        _picturemss_smallimg_up();//删除对话框小项
    };


    var _getAreaNames = function () {
        var names = [];
        var treeObj = $.fn.zTree.getZTreeObj("area_tree");
        var nodes = treeObj.getCheckedNodes(true);
        for (var i = 0; i < nodes.length; i++) {
            if (nodes[i].isParent) {//本身是父节点,并且是全选的父节点 则不管子节点

                if (nodes[i].getCheckStatus().half) {//半选父节点

                } else {//全选节点(本身是父节点) 查看他的父节点是不是全选

                    var parent = nodes[i].getParentNode();
                    if (parent == null) {//本身父节点为空 表示是全选根节点 应该存储
                        //ids.push(nodes[i].id);
                        names.push(nodes[i].name);
                    } else {//判断父节点不为空
                        var checkStatus = parent.getCheckStatus();
                        if (checkStatus == null) {
                            //ids.push(nodes[i].id);
                            names.push(nodes[i].name);
                        } else {
                            if (!checkStatus.half) {
                            } else {
                                //ids.push(nodes[i].id);
                                names.push(nodes[i].name);
                            }
                        }

                    }
                }
            } else {//本身是叶子节点 父节点选中，则不存 父节点半选或者不选 则存下来
                var parent = nodes[i].getParentNode();
                if (parent == null) {//全选叶子节点 父节点为空 即只有一个根节点 保存
                    //ids.push(nodes[i].id);
                    names.push(nodes[i].name);
                } else {//全选叶子节点 有父节点
                    var checkStatus = parent.getCheckStatus();
                    if (checkStatus == null) {
                        //ids.push(nodes[i].id);
                        names.push(nodes[i].name);
                    } else {
                        if (checkStatus.half) {//父节点半选 存储
                            //ids.push(nodes[i].id);
                            names.push(nodes[i].name);
                        } else {//父节点全选 不存储
                        }
                    }

                }
            }
        }
        return names;
    };
    var _picturemss_smallimg_up = function () {
        $('.delete').unbind('click').bind('click', function () {
            var objval = $("#selectednodes").text();
            if (objval.length > 0) {
                var areaIds = $(this).parent().parent().parent().find("li:first").find(".areaidhid").text(); //删除选中节点
                var newval = _.difference(objval.split(","), areaIds.split(","));
                var temp = newval.join(",");
                $("#selectednodes").text("");
                $("#selectednodes").text(temp);
            }
            $(this).parent().parent().parent().remove(); //删除记录
        });
    };

    var _temp_showareafortitle = function () {
        $(document).on("mouseover", ".showtitle", function () {
            $("p[class='showtitle']").parent().removeClass("c-orange1");
            $(this).parent().addClass("c-orange1");
            $(this).parent().css("cursor", "pointer");
        });
        $(document).on("mouseout", ".showtitle", function () {
            $("p[class='showtitle']").parent().removeClass("c-orange1");
        });

        $(document).on("click", ".showtitle", function () {
            var val = $(this).text();

            var dg = dialog({
                title: '地区',
                content: val,
                id: 'dialog_title',
                cancelValue: '关闭',
                cancel: function () {
                    dialog.get('dialog_title').close().remove();
                },
                width: 600
            });
            dg.show();

        });

    };
    //保存运费模板
    var _add_TransTemplate = function () {

        var arry = [];
        $("#addTransTempBtn").click(function () {
            var transTempName = $("#templatesName").val();
            if (transTempName.length === 0) {
                var d = dialog({
                    content: '请填写模板名称!'
                });
                d.show();
                setTimeout(function () {
                    d.close().remove();
                }, 1000);
                return;
            }
            var arrays = [];//总行数组
            $(".table-container:gt(0)").each(function () {
                var lines = [];//行数组
                var values = [];//input值数组
                $(this).find("li").each(function (index) {
                    if (index === 0) {//遍历列，找出第一列
                        var idsArr=[];
                        var namesArr=[];
                        var ids = $(this).find("p").attr("areaids");
                        var names = $(this).find("p").html();

                        if(ids.length>0){
                            idsArr =ids.split(',');
                        }
                        if(names.length>0){
                            namesArr =names.split(',');
                        }
                        lines.push(idsArr);
                        lines.push(namesArr);
                    } else {
                        if (index < 5) {
                            values.push($(this).find("input").val());
                        }

                    }
                });
                lines.push(values);
                arrays.push(lines);
            });


//判断首件和续件个数不能为0 start
            var sum = 0;
            $(".table-container:gt(0)").each(function (i) {

                var text = $(this).find("li:eq(1) input").val();
                var text1 = $(this).find("li:eq(3) input").val();
                if (text == 0 || text1 == 0) {
                    sum += 1;
                }
            });
            if (sum >= 1) {
                var d = dialog({
                    content: '首件和续件个数不能为0'
                });
                d.show();
                setTimeout(function () {
                    d.close().remove();
                }, 1000);

            }
            else {
                if (optStatus !== "edit") {
                    _addTransTemplateDatas(transTempName, arrays);
                } else {
                    _editTransTemplateDatas(transTempName, arrays);
                }
            }
//判断首件和续件个数不能为0 end

        })
    };

    var _addTransTemplateDatas = function (transTempName, transTarrays) {

        var transTemplatesCo = {};
        var transRelationList_ = new Array();
        transTemplatesCo.templatesName = transTempName;
        for (var n = 0; n < transTarrays.length; n++) {
            var transTemplatesRelationCo = {};
            var sendareaIds_ = transTarrays[n][0].join(',');
            var sendAreaName_ = transTarrays[n][1].join(',');
            var firstNumber_ = transTarrays[n][2][0];
            var transFare_ = Number(transTarrays[n][2][1]) * 100;
            var nextNumber_ = transTarrays[n][2][2];
            var nextTransFare_ = Number(transTarrays[n][2][3]) * 100;
            transTemplatesRelationCo.sendAreaId = sendareaIds_;
            transTemplatesRelationCo.sendArea = sendAreaName_;
            transTemplatesRelationCo.firstNumber = firstNumber_;
            transTemplatesRelationCo.transFare = transFare_;
            transTemplatesRelationCo.nextNumber = nextNumber_;
            transTemplatesRelationCo.nextTransFare = nextTransFare_;
            transRelationList_.push(transTemplatesRelationCo);
        }

        transTemplatesCo.transRelationList = transRelationList_;
        var jsonStr = JSON.stringify(transTemplatesCo);

        var url = CONTEXT_PATH + "/trans_temp";
        _postdatasobj(url, jsonStr);


    };

    var _postdatasobj = function (url, transTemplatesForCreateVo) {
        $.ajaxJson(url, transTemplatesForCreateVo, {
            "done": function (res) {
                if (res.data.code == "0") {
                    _temp_refresh();//新增跳转到已发送
                    var dm = new dialogMessage({
                        type: 1,
                        fixed: true,
                        msg: '恭喜您，操作成功！',
                        isAutoDisplay: true,
                        time: 1000
                    });
                    dm.render();

                } else {
                    var dm = new dialogMessage({
                        type: 2,
                        fixed: true,
                        msg: '对不起，操作失败！',
                        isAutoDisplay: true,
                        time: 1000
                    });
                    dm.render();
                }
            },
            "fail": function (res) {
                var dm = new dialogMessage({
                    type: 2,
                    fixed: true,
                    msg: '对不起，系统操作失败！',
                    isAutoDisplay: true,
                    time: 1000
                });
                dm.render();

            }
        });
    };
    var _editTransTemplateDatas = function (transTempName, transTarrays) {

        var transTemplatesUo = {};
        var transRelationList_ = new Array();
        var tid = $("#edit_templatesId").text();//模板Id  编辑
        transTemplatesUo.templatesId = tid;
        transTemplatesUo.templatesName = transTempName;
        for (var n = 0; n < transTarrays.length; n++) {
            var TransTemplatesRelationUo = {};
            var sendareaIds_ = transTarrays[n][0].join(',');
            var sendAreaName_ = transTarrays[n][1].join(',');
            var firstNumber_ = transTarrays[n][2][0];
            var transFare_ = Number(transTarrays[n][2][1]) * 100;
            var nextNumber_ = transTarrays[n][2][2];
            var nextTransFare_ = Number(transTarrays[n][2][3]) * 100;
            TransTemplatesRelationUo.sendAreaId = sendareaIds_;
            TransTemplatesRelationUo.sendArea = sendAreaName_;
            TransTemplatesRelationUo.firstNumber = firstNumber_;
            TransTemplatesRelationUo.transFare = transFare_;
            TransTemplatesRelationUo.nextNumber = nextNumber_;
            TransTemplatesRelationUo.nextTransFare = nextTransFare_;
            TransTemplatesRelationUo.templatesId = tid;//模板Id  编辑
            transRelationList_.push(TransTemplatesRelationUo);
        }

        transTemplatesUo.transRelationList = transRelationList_;
        var jsonStr = JSON.stringify(transTemplatesUo);

        var url = CONTEXT_PATH + "/trans_temp";
        _put_postdatasobj(url, jsonStr);


    };
    var _put_postdatasobj = function (url, transTemplatesForUpdateUo) {
        $.ajaxJsonPut(url, transTemplatesForUpdateUo, {
            "done": function (res) {
                if (res.data.code == "0") {
                    _temp_list(); //返回并刷新列表
                    var dm = new dialogMessage({
                        type: 1,
                        fixed: true,
                        msg: '恭喜您，操作成功！',
                        isAutoDisplay: true,
                        time: 1000
                    });
                    dm.render();

                } else {
                    var dm = new dialogMessage({
                        type: 2,
                        fixed: true,
                        msg: '对不起，操作失败！',
                        isAutoDisplay: true,
                        time: 1000
                    });
                    dm.render();
                }
            },
            "fail": function (res) {
                alert('Err...');
            }
        });
    };
    //运费模板列表
    var _temp_list = function () {

        $.ajaxHtmlGet(ajaxUrl.url1, {
            "done": function (res) {
                if (res.code == 0) {

                    var data = {
                        items: res.data[0]
                    };

                    jumi.template('trans/transtemp_list', data, function (tpl) {
                        $("#contentBox").empty();
                        $("#contentBox").html(tpl);
                        $("#dialogAreainfo").remove();//移除指定配送区域对话框
                    });
                }
            }
        });
    };
    //返回 （编辑、新增状态）
    var _temp_back = function () {
        $("#backTransTempBtn").css("display","inline-block");
        $("#backTransTempBtn").click(function () {
            _temp_list();
        });
    }

    //保存可选条件
    var _temp_option = function () {
        $("#saveTransOptBtn").click(function () {
            //ajaxUrl.url4
            var transShopUo = {};
            var one = $("#transCondition0ne");
            var two = $("#transConditionTwo");
            var three = $("#transConditionThree");
            if (one.prop("checked")) {
                transShopUo.transCondition0ne = 1;
            } else
                transShopUo.transCondition0ne = 0;
            if (two.prop("checked")) {
                transShopUo.transConditionTwo = 1;
            } else
                transShopUo.transConditionTwo = 0;
            if (three.prop("checked")) {
                transShopUo.transConditionThree = 1;
            } else
                transShopUo.transConditionThree = 0;
            var data = JSON.stringify(transShopUo);
            $.ajaxJsonPut(ajaxUrl.url4, data, {
                "done": function (res) {
                    if (res.data.code == "0") {
                        var dm = new dialogMessage({
                            type: 1,
                            fixed: true,
                            msg: '恭喜您，操作成功！',
                            isAutoDisplay: true,
                            time: 1000
                        });
                        dm.render();
                    } else {
                        var dm = new dialogMessage({
                            type: 2,
                            fixed: true,
                            msg: '对不起，操作失败！',
                            isAutoDisplay: true,
                            time: 1000
                        });
                        dm.render();
                    }
                },
                "fail": function (res) {
                    alert('Err...');
                }
            });
        });
    };
    //编辑运费模板
    var _temp_edit = function () {
        $("div[id^='edit_']").click(function () {
            optStatus = "edit";
            var id = $(this).attr("id").replace("edit_", "");
            $.ajaxHtmlGet(ajaxUrl.url2 + "/" + id, null, {
                done: function (res) {
                    if (res.code === 0) {
                        var data = {
                            items: res.data
                        };
                        $("#transtemp_list").empty();//隐藏列表并清空
                        $("#transtemp_list").hide();
                        $("#transtemp_add").show(); //显示编辑模板
                        jumi.template('trans/transtemp_add', data, function (tpl) {
                            $("#transtemp_add").empty();
                            $("#transtemp_add").html(tpl);
                            document.body.scrollTop = 0;//滚动条复位
                            _temp_back();//返回绑定
                        });
                        _picturemss_smallimg_up();//删除对话框小项
                    }
                }
            });
        });
    };
    //删除运费模板
    var _temp_del = function () {
        $("div[id^='del_']").click(function () {
            var id = $(this).attr("id").replace("del_", "");

            jumi.template('common/delmsg/del_message', function (tpl) {
                var wx_del = dialog({
                    title: '操作提醒',
                    content: tpl,
                    zIndex: 10000,
                    width: 400,
                    id: 'dialog_wxdel',
                    onclose: function () {
                        dialog.get("dialog_wxdel").close().remove();
                    }
                });
                wx_del.showModal();
                _temp_del_operation(id);
                _temp_del_delcancel();

            });

        });
    };
    //删除操作
    var _temp_del_operation = function (itemId) {
        $("#makesure_del").click(function () {
            dialog.get("dialog_wxdel").close().remove();
            $.ajaxJsonDel(ajaxUrl.url3 + "/" + itemId, {
                "done": function (res) {
                    if (res.code === 0) {
                        if (res.data.code === 0) {
                            _temp_list();
                            var dm = new dialogMessage({
                                type: 1,
                                title: '操作提醒',
                                fixed: true,
                                msg: '恭喜您，操作成功',
                                isAutoDisplay: false

                            });
                            dm.render();
                        } else {
                            var dm = new dialogMessage({
                                type: 2,
                                title: '操作提醒',
                                fixed: true,
                                msg: res.data.msg,
                                isAutoDisplay: false

                            });
                            dm.render();
                        }
                    }
                },
                "fail": function (res) {
                }
            });
        });
    };
    //取消操作
    var _temp_del_delcancel = function () {
        $("#makesure_canl").click(function () {
            dialog.get("dialog_wxdel").close().remove();
        });
    };

    //新增后跳转到列表
    var _temp_refresh = function () {
        var tabul = $(".m-tab ul li");
        tabul.removeClass('z-sel').eq(1).addClass("z-sel");
        _temp_list(); //返回并刷新列表
    };

return {
    init: _init,
    transtemp_tabchange: _transtemp_tabchange, //运费管理选项卡切换
    forareatrans:_forareatrans,
    stran_selectAll:_stran_selectAll,
    stran_selectReverse:_stran_selectReverse,
    add_TransTemplate:_add_TransTemplate,
    temp_back:_temp_back,
    temp_edit: _temp_edit, //修改运费模板
    temp_del: _temp_del, //删除运费模板
    temp_showareafortitle:_temp_showareafortitle,
    temp_option:_temp_option //配置保存
};
})();