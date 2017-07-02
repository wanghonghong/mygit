/**
 * Created by wxz on 2016/8/25
 */
CommonUtils.regNamespace("product", "transtemplate");
product.transtemplate=(function () {
    var _init = function(){

    };

    var _shop_trans_open=function(){


        $("#new_template").click(function(){
            jumi.template('extends/create_trans_open',function (tpl) {
                var d = dialog({
                    title: '新建运费模板',
                    content:tpl,
                    width:1200,
                    height:500,
                    id:'dialog_open',
                    onshow:function () {
                        jumi.template("extends/shop_trans",function(tpl){
                            $("#transcontent").empty();
                            $("#transcontent").html(tpl);
                        })
                    }
                })
                d.showModal();
            })
        });
    };

    var _shop_trans_inneropen=function () {
        jumi.template("extends/shop_trans",function(tpl){
            $("#transcontent").empty();
            $("#transcontent").html(tpl);

        })
    }

    var _shop_forareatrans=function(){
        $("#forareatrans").click(function() {
            $("#onloadmsg").fadeIn('slow', function() {
                // Animation complete
                var treeObj = $.fn.zTree.getZTreeObj("area_tree");
                if (treeObj !== null) {
                    var nodesAll = treeObj.getNodes();  //清除disabled
                    for (var j = 0, num = nodesAll.length; j < num; j++) {
                        treeObj.setChkDisabled(nodesAll[j], false, true, true);
                    }
                    treeObj.checkAllNodes(false); // 清除上次操作
                    _shop_setChkDisabled(treeObj);//设置选中节点状态
                    _shop_showAreaTreeWindow();
                    $('#onloadmsg').fadeOut('100',function() {
                        // Animation complete.
                    });

                }
            });

        });
    };


    var _shop_setChkDisabled = function(treeObj){

        var pnodes="";
        var objval = $("#selectednodes").text();

        if (objval.length > 0) {
            var nodestr =objval.substring(0,objval.length-1);
            var nodesarr = nodestr.split(",");
            for (var i = 0, l = nodesarr.length; i < l; i++) {
                var node = treeObj.getNodeByParam("id", nodesarr[i], null);
                treeObj.checkNode(node, true, false, false);
                treeObj.setChkDisabled(node, true);
                var temp =nodesarr[i].substring(2,nodesarr[i].length);
                if(temp==="0000")
                {
                    var parentnode =  treeObj.transformToArray(node);
                    for(var j=0,num=parentnode.length;j<num;j++){
                        pnodes +=parentnode[j].id+",";
                    }
                }
            }
            pnodes =pnodes.substring(0,pnodes.length-1);
            var newval = _.difference(pnodes.split(","), nodestr.split(","));
            var temp=newval.join(",");
            $("#unselectednodes").text("");
            $("#unselectednodes").text(temp);

        }

    }

    var _shop_showAreaTreeWindow=function () {
        $("#sltReverse").prop("checked","");
        $("#selectAll").prop("checked","");
        var elem = $('#dialogAreainfo');
        dialog({
            title:"选择可配送区域",
            content:elem,
            width :500,
            okValue:"确认",
            ok:function(){
                _saveTransTemp();
            },
            cancelValue:"取消",
            cancel:function(){

            }
        }).showModal();
    };

    var _saveTransTemp = function(){
        var treeObj = $.fn.zTree.getZTreeObj("area_tree");
        var nodes = treeObj.getCheckedNodes(true);
        var areaIds ="";
        var areaNames ="";
        var areatIds="";
        if(nodes.length===0){
            var d = dialog({
                content: '请勾选配送地区！'
            });
            d.show();
            setTimeout(function () {
                d.close().remove();
            }, 1000);
            return;
        }
        for(var i=0;i<nodes.length;i++){

            if(i==nodes.length-1){
                areaIds += nodes[i].id;
                /*areaNames += nodes[i].name;*/
            }else{
                areaIds += nodes[i].id+",";
                /*areaNames += nodes[i].name+",";*/
            }
            areatIds+=nodes[i].id+",";
        }
        var names = _getAreaNames();

        for(var i=0;i<names.length;i++){
            if(i==names.length-1){
                areaNames += names[i];
            }else{
                areaNames += names[i]+",";
            }
        }

        var html=$("#first-table-container").clone();
        html.find(".page-left").remove();
        html.find("#dialogAreainfo").remove();
        html.find("li").css("display","static");
        html.removeAttr("id");
        html.find("li:first").append("<span style='display: block;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;cursor:pointer;'  class='showtitle' areaIds="+areaIds+">"+areaNames+"</span> <span class='areaidhid' style='display: none;'>"+areaIds+"</span>");

        $("#transTEM0").append(html);
        var idstr= $("#selectednodes").text();
        idstr+=areatIds;
        $("#selectednodes").text("");
        $("#selectednodes").text(idstr);
    };

    //保存运费模板
    var _addTransTemplate=function(){
        var arry = [];
        $('#close').click(function(){
            dialog.get('dialog_open').close();
        })
        $("#addTransTempBtn").click(function () {
            var transTempName = $("#transTemName").val();
            if(transTempName.length===0){
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
            $(".table-container-model:gt(0)").each(function(){
                var lines = [];//行数组
                var values = [];//input值数组
                $(this).find("li").each(function(index){
                    if(index === 0){//遍历列，找出第一列
                        var idsArr=[];
                        var namesArr=[];
                        var ids = $(this).find("span").attr("areaids");
                        var names = $(this).find("span").html();

                        if(ids.length>0){
                            idsArr =ids.split(',');
                        }
                        if(names.length>0){
                            namesArr =names.split(',');
                        }
                        lines.push(idsArr);
                        lines.push(namesArr);
                    }else{
                        if(index<5){
                            values.push($(this).find("input").val());
                        }

                    }
                });
                lines.push(values);
                arrays.push(lines);
            });


//判断首件和续件个数不能为0 start
            var sum = 0;
            $(".table-container-model:gt(0)").each(function(i){

                var text = $(this).find("li:eq(1) input").val();
                var text1 = $(this).find("li:eq(3) input").val();
                if(text==0||text1==0){
                    sum += 1;
                }
            });
            if(sum>=1){
                var d = dialog({
                    content: '首件和续件个数不能为0'
                });
                d.show();
                setTimeout(function () {
                    d.close().remove();
                }, 1000);

            }
            else{
                _addTransTemplateDatas(transTempName,arrays);
            }
//判断首件和续件个数不能为0 end

        })
    }
    var _addTransTemplateDatas=function(transTempName,transTarrays){

        var transTemplatesCo ={};
        var transRelationList_ = new Array();
        transTemplatesCo.templatesName = transTempName;
        for(var n=0;n<transTarrays.length;n++){
            var transTemplatesRelationCo ={};
            var sendareaIds_ = transTarrays[n][0].join(',');
            var sendAreaName_ = transTarrays[n][1].join(',');
            var  firstNumber_ = transTarrays[n][2][0];
            var  transFare_ = Number(transTarrays[n][2][1])*100;
            var  nextNumber_ = transTarrays[n][2][2];
            var  nextTransFare_ =Number(transTarrays[n][2][3])*100;
            transTemplatesRelationCo.sendAreaId = sendareaIds_;
            transTemplatesRelationCo.sendArea = sendAreaName_;
            transTemplatesRelationCo.firstNumber = firstNumber_;
            transTemplatesRelationCo.transFare = transFare_;
            transTemplatesRelationCo.nextNumber = nextNumber_;
            transTemplatesRelationCo.nextTransFare =nextTransFare_;
            transRelationList_.push(transTemplatesRelationCo);
        }

        transTemplatesCo.transRelationList = transRelationList_;
        var jsonStr = JSON.stringify(transTemplatesCo);

        var url =CONTEXT_PATH+"/trans_temp";
        _postdatasobj(url,transTemplatesCo);


    };

    var _postdatasobj=function (url,transTemplatesForCreateVo) {
        $.ajaxJson(url,transTemplatesForCreateVo,{
            "done":function (res) {
                if(res.data.code=="0"){
                    var dm = new dialogMessage({
                        type: 1,
                        fixed: true,
                        msg:'新增模板成功!',
                        isAutoDisplay: true,
                        time: 1500
                    });
                    dm.render();
                    setTimeout(function () {
                        dialog.get('dialog_open').close().remove();
                        window.close();
                    }, 1000);

                }else{
                    var dm = new dialogMessage({
                        type: 1,
                        fixed: true,
                        msg:'新增模板失败!',
                        isAutoDisplay: true,
                        time: 1500
                    });
                    dm.render();
                    setTimeout(function () {
                        dialog.get('dialog_open').close().remove();
                        window.close();
                    }, 1000);
                }
            },
            "fail":function (res) {
                alert('Err...');
            }
        });
    }

    var _deltranstemplate=function(){
        $(document).on("click",".delete",function(){
            var objval = $("#selectednodes").text();
            if (objval.length > 0) {
                var areaIds = $(this).parent().parent().parent().find("li:first").find(".areaidhid").text(); //删除选中节点
                var newval = _.difference(objval.split(","), areaIds.split(","));
                var temp=newval.join(",");
                $("#selectednodes").text("");
                $("#selectednodes").text(temp);
            }
            $(this).parent().parent().parent().remove(); //删除记录
        });
    }

    var _showareafortitle=function () {

        $(document).on("click",".showtitle",function(){
            var val =$(this).text();

            var dg = dialog({
                title: '地区',
                content: val,
                id:'dialog_title',
                cancelValue: '关闭',
                cancel: function () {
                    dialog.get('dialog_title').close().remove();
                },
                width:600
            });
            dg.show();

        });

    }

    var _backparent=function () {
        $("#back").click(function () {
            window.close();
        });
    }

    var _shop_stran_selectAll=function () {
        $("#selectAll").click(function () {
            var tag=  $("#selectAll").prop("checked");
            var treeObj = $.fn.zTree.getZTreeObj("area_tree");
            $("#sltReverse").prop("checked",false);
            if(tag) {
                treeObj.checkAllNodes(true);
            }else{
                treeObj.checkAllNodes(false);
            }
            var unsltednodes =  $("#unselectednodes").text();
            if (unsltednodes.length>0) {
                var nodearr = unsltednodes.split(",");
                for (var k = 0, num = nodearr.length; k < num; k++) {
                    var node = treeObj.getNodeByParam("id", nodearr[k], null);
                    treeObj.checkNode(node, tag);
                }
            }
        });
    }

    var _shop_stran_selectReverse=function () {
        $("#sltReverse").click(function () {
            var tag = $("#sltReverse").prop("checked");
            $("#selectAll").prop("checked",false);
            var treeObj = $.fn.zTree.getZTreeObj("area_tree");
            var nodes = treeObj.getCheckedNodes(true);
            var unsltednodes =  $("#unselectednodes").text();
            if (unsltednodes.length>0) {
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



    //获取Url参数
    var _getUrlParms=function(url) {
        if (!url) {
            url = location.search.substring(1);
        } else {
            url = url.substr(url.indexOf('?') + 1);
        }
        var args = new Object();   // 声明并初始化一个 "类"
        // 获得地址(URL)"?"后面的字符串.
        var query = decodeURI(url);
        var pairs = query.split("&");  // 分割URL(别忘了'&'是用来连接下一个参数)
        for (var i = 0; i < pairs.length; i++) {
            var pos = pairs[i].indexOf('=');
            if (pos == -1) continue; // 它在找有等号的 数组[i]
            var argname = pairs[i].substring(0, pos); // 参数名字
            var value = pairs[i].substring(pos + 1);  // 参数值
            // 以键值对的形式存放到"args"对象中
            args[argname] = decodeURI(value);
        }
        return args;
    }
    var _getAreaNames = function(){
        var names =[];
        var treeObj = $.fn.zTree.getZTreeObj("area_tree");
        var nodes = treeObj.getCheckedNodes(true);
        for(var i=0;i<nodes.length;i++){
            if(nodes[i].isParent){//本身是父节点,并且是全选的父节点 则不管子节点

                if(nodes[i].getCheckStatus().half){//半选父节点

                }else{//全选节点(本身是父节点) 查看他的父节点是不是全选

                    var parent = nodes[i].getParentNode();
                    if(parent==null){//本身父节点为空 表示是全选根节点 应该存储
                        //ids.push(nodes[i].id);
                        names.push(nodes[i].name);
                    }else{//判断父节点不为空
                        var checkStatus = parent.getCheckStatus();
                        if(checkStatus==null){
                            //ids.push(nodes[i].id);
                            names.push(nodes[i].name);
                        }else{
                            if(!checkStatus.half){
                            }else{
                                //ids.push(nodes[i].id);
                                names.push(nodes[i].name);
                            }
                        }

                    }
                }
            }else{//本身是叶子节点 父节点选中，则不存 父节点半选或者不选 则存下来
                var parent = nodes[i].getParentNode();
                if(parent==null){//全选叶子节点 父节点为空 即只有一个根节点 保存
                    //ids.push(nodes[i].id);
                    names.push(nodes[i].name);
                }else{//全选叶子节点 有父节点
                    var checkStatus = parent.getCheckStatus();
                    if(checkStatus==null){
                        //ids.push(nodes[i].id);
                        names.push(nodes[i].name);
                    }else{
                        if(checkStatus.half){//父节点半选 存储
                            //ids.push(nodes[i].id);
                            names.push(nodes[i].name);
                        }else{//父节点全选 不存储
                        }
                    }

                }
            }
        }
        return names;
    }
    return {
        init :_init,
        shop_trans_open:_shop_trans_open,
        shop_forareatrans:_shop_forareatrans,
        shop_trans_inneropen:_shop_trans_inneropen,
        getUrlParms:_getUrlParms,
        addTransTemplate:_addTransTemplate,
        deltranstemplate:_deltranstemplate,
        backparent:_backparent,
        shop_stran_selectAll:_shop_stran_selectAll,
        shop_stran_selectReverse:_shop_stran_selectReverse,
        showareafortitle:_showareafortitle,
        shop_setChkDisabled:_shop_setChkDisabled,
        getAreaNames:_getAreaNames

    };
})();