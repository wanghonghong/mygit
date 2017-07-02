
<div class="content-pc " id="transTempList">

    <div class="main-content-pc" style="padding: 23px 24px 23px 5px;">
        <div class="mallBuilding">
            <div class="col-xs-12" style="padding-left: 0;">
                <div class="form-group col-xs-6 zhyh-mb zhyh-pl" style="padding-left:0">
                    <label style="padding-left:0">模板名称:</label>
					<input type="text" id="transTemName"
					   class="form-control distance" value="${transTempListVo.transTemplates.templatesName}" />
					<input type="hidden" id="transTemId" value="${transTempListVo.transTemplates.templatesId}"/>
					<input type="hidden" id="shopId" value="${transTempListVo.transTemplates.shopId}"/>
                    <input type="hidden" id="creatTime" value=" ${transTempListVo.transTemplates.creatTime?datetime}"/>
                </div>
            </div>

			<div class="jm-table" style="min-height: 100px;font-size:12px" id="transTEM0">
				<ul class="table-hander">
					<li style="width: 35%; text-align: left;  padding-left: 10px;s">可配送至</li>
					<li>首件（个）</li>
					<li>运费（元）</li>
					<li>续件（个）</li>
					<li>续件（元）</li>
					<li>操作</li>
				</ul>
                <ul class="table-container" id="first-table-container">
                    <li style="width: 35%;text-align: left;  padding-left: 10px;">
                        <div class="page-left" style="margin-top: 0;">
                            <div  class="btn  btn-lightgray btn-sm dialog" style="float: left;" onclick="addPS()"> 指定可配送区域和运费 </div>
                            <div  class="text-center" id="onloadmsg" style="float: left; margin-left: 50px;display: none;"><i class="iconfont icon-loading" style="float: left;"></i><div style="float: left;color:#f78211;">&nbsp;请稍后...</div></div>
                        </div>

                        <div id="dialogAreainfo" class="dialoginfo dhk" >

                            <div class="jm-table jm-table1">
                                <ul class="table-hander">
                                    <li>
                                        <i class="iconfont icon-classify1"></i>
                                        可选省、市、区
                                    </li>
                                </ul>
                                <ul id="area_treedit" class="ztree" style="border: 1px solid #617775;overflow-y: scroll;height: 500px;">

                                </ul>
                            </div>
                            <div class="sell-third-checkbox" style="margin-top: 10px;">
                                <div class="checkBox">    <input type="checkbox" name="checkbox" id="selectAll"/>
                                    <label class="iconfont icon-avoid"  for="selectAll"></label></div><span class="authfont"> 全选</span>&nbsp;&nbsp; <div class="checkBox"><input type="checkbox" name="checkbox" id="sltReverse"/>
                                <label class="iconfont icon-avoid"  for="sltReverse"></label></div> <span  class="authfont">反选</span>
                            </div>
                        </div>

                    </li>
                    <li style="display: none;" class="form-group form-group-zwmb">
                        <input  type="text" style="float:none;width:50%" class="form-control distance" placeholder="" />
                    </li>
                    <li style="display: none;" class="form-group form-group-zwmb">
                        <input  type="text" style="float:none;width:50%" class="form-control distance" placeholder="" />
                    </li>
                    <li style="display: none;" class="form-group form-group-zwmb">
                        <input  type="text" style="float:none;width:50%" class="form-control distance" placeholder="" />
                    </li>
                    <li style="display: none;" class="form-group form-group-zwmb">
                        <input  type="text" style="float:none;width:50%" class="form-control distance" placeholder="" />
                    </li>
                    <li style="display: none;" class="form-group form-group-zwmb">
                        <span  class='delete' >删除</span>
                    </li>

                </ul>
				<div>
					<#if transTempListVo.transRelationList??>
						<#list transTempListVo.transRelationList as transRelation>
						<ul class="table-container">
							<li class="form-group form-group-zwmb" style="width: 35%;text-align: left;  padding-left: 10px;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;cursor: pointer;" ><span  areaIds="${transRelation.sendAreaId}"  class='defaultransnode' >${transRelation.sendArea!''}</span></li>
							<li class="form-group form-group-zwmb"><input  type="text" class="form-control distance" style="float: none;width:50%" value="${transRelation.firstNumber!0}"/></li>
							<li class="form-group form-group-zwmb"><input  type="text" class="form-control distance" style="float: none;width:50%" value="${(transRelation.transFare/100)?string("0.##")}"/></li>
							<li class="form-group form-group-zwmb"><input  type="text" class="form-control distance" style="float: none;width:50%" value="${transRelation.nextNumber!0}"/></li>
							<li class="form-group form-group-zwmb"><input  type="text" class="form-control distance" style="float: none;width:50%" value="${(transRelation.nextTransFare/100)?string("0.##")}"/></li>
							<li class="form-group form-group-zwmb"><span  class='delete'  style="cursor: pointer;">删除</span></li>
						</ul>
						</#list>
					</#if>
                </div>
    		</div>
            <div style="margin: 61px 0 69px;">
                <div style="" class="btn btn-lightgray btn-radius-5 btn-lg col-xs-offset-4" id="back">返回</div>
                <div id="updateTransTempBtn" style="margin-left:8px;" class="btn btn-darkorange btn-radius-5 btn-lg">保存</div>
            </div>
            <div id="selectednodesedit" style="display:none;width:100%;"></div>
            <div id="unselectednodesedit" style="display:none;"></div>
            <div id="newselectednodesedit" style="display:none;"></div>
		</div>
	</div>


</div>

<script>

    $(document).ready(function(){

        onLoadZTreedit();
        $("#unselectednodesedit").text("");
        $("#selectednodesedit").text("");
        clearZtreeStatus();
    });

    function addPS()  {
        $("#onloadmsg").fadeIn('slow', function() {
        var treeObj = $.fn.zTree.getZTreeObj("area_treedit");
        if (treeObj !== null) {
            var nodesAll = treeObj.getNodes();  //清除disabled
            for (var j = 0, num = nodesAll.length; j < num; j++) {
                treeObj.setChkDisabled(nodesAll[j], false, true, true);
            }
            treeObj.checkAllNodes(false); // 清除上次操作

            $("div[class='dialoginfo dhk']:gt(0)").each(function(){  // 清除对话框缓存
                $(this).remove();
            });
             showDefaultArea(treeObj);//设置已选项
            // setParentNodeStatus(treeObj);
            showAreaTreeWindow();
            $('#onloadmsg').fadeOut('100',function() {
                // Animation complete.
            });
        }
        });
    }

    function showDefaultArea(treeObj){

        var defaultObj=$("ul[class='table-container'] li span[class='defaultransnode']");
        if(defaultObj.length>0){
            defaultObj.each(function(){
                var pnodes="";
               var str = $(this).attr("areaIds");
               var nodesArr= str.split(",");
               for(var i=0,l=nodesArr.length;i<l;i++){
                   var node = treeObj.getNodeByParam("id",nodesArr[i], null);
                   treeObj.checkNode(node, true, false, false);
                   treeObj.setChkDisabled(node, true);
                   var temp =nodesArr[i].substring(2,nodesArr[i].length);
                   if(temp==="0000")
                   {
                       var parentnode =  treeObj.transformToArray(node);
                       for(var j=0,num=parentnode.length;j<num;j++){
                           pnodes +=parentnode[j].id+",";
                       }
                   }
               }
                if(pnodes.length>0) {
                    var unsel = $("#unselectednodesedit").text();
                    pnodes = pnodes.substring(0, pnodes.length - 1);
                    var newval = _.difference(pnodes.split(","), nodesArr);
                    var temp = newval.join(",");
                    unsel+=temp;
                    $("#unselectednodesedit").text("");
                    $("#unselectednodesedit").text(unsel);
                }
            });
        }

        trans_setChkDisabled(treeObj);//设置选中节点状态
    }

    function trans_setChkDisabled(treeObj){

        var pnodes="";
        var objval = $("#selectednodesedit").text();

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
            if(pnodes.length>0) {
               var unsel = $("#unselectednodesedit").text();
                pnodes = pnodes.substring(0, pnodes.length - 1);
                var newval = _.difference(pnodes.split(","), nodestr.split(","));
                var temp = newval.join(",");
                unsel+=temp;
                $("#unselectednodesedit").text("");
                $("#unselectednodesedit").text(temp);
            }

        }

    }

    function showAreaTreeWindow() {
        $("#sltReverse").prop("checked","");
        $("#selectAll").prop("checked","");
        var elem = $('#dialogAreainfo');
        var dg =  dialog({
            title:"选择可配送区域",
            content:elem,
            width :500,
            id:'dialog_infoedit',
            okValue:"点击确认",
            ok:function(){
                addSendArea();
            },
            cancelValue:"点击取消",
            cancel:function(){
                dialog.get('dialog_infoedit').close().remove();
            },
            onclose: function () {
                dialog.get('dialog_infoedit').close().remove();
            }

        });
        dg.showModal();
    }
    function clearZtreeStatus(){
        var treeObj = $.fn.zTree.getZTreeObj("area_treedit");
        if (treeObj !== null) {
            var nodesAll = treeObj.getNodes();  //清除disabled
            for (var j = 0, num = nodesAll.length; j < num; j++) {
                treeObj.setChkDisabled(nodesAll[j], false, true, true);
            }
            treeObj.checkAllNodes(false); // 清除上次操作
        }
    }
    $(document).on("click",".delete",function(){
        var objval = $("#selectednodesedit").text();
        if (objval.length > 0) {
            var areaIds = $(this).parent().parent().find("li:first").find(".areaidhid").text(); //删除选中节点
            var newval = _.difference(objval.split(","), areaIds.split(","));
            var temp=newval.join(",");
            $("#selectednodesedit").text("");
            $("#selectednodesedit").text(temp);
        }
        $(this).parent().parent().remove(); //删除记录

    });

    // 新增配送地区
    function addSendArea(){
        var treeObj = $.fn.zTree.getZTreeObj("area_treedit");
        var nodes = treeObj.getCheckedNodes(true);
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
        var areaIds ="";
        var areaNames ="";
        var areatIds="";
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

        var names = getAreaNames();
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
      //  html.find("li:first").append("<span style='display: block;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;cursor:pointer;'  class='showtitle' areaIds="+areaIds+">"+areaNames+"</span><span class='transnode' style='display:none ;'>"+areatIds+"</span>");
        html.find("li:first").append("<span style='display: block;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;cursor:pointer;'  class='showtitle' areaIds="+areaIds+">"+areaNames+"</span> <span class='areaidhid' style='display: none;'>"+areaIds+"</span>");
        $("#transTEM0").append(html);
        var idstr= $("#selectednodesedit").text();
        idstr+=areatIds;
        $("#selectednodesedit").text("");
        $("#selectednodesedit").text(idstr);


    }

    //保存编辑后的运费模板
    var arry = [];
    $("#updateTransTempBtn").click(function(){
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
        $(".table-container:gt(0)").each(function(){
            var lines = [];//行数组
            var values = [];//input值数组
            $(this).find("li").each(function(index){
                if(index === 0){//遍历列，找出第一列
                    var ids = $(this).find("span").attr("areaids");
                    var names = $(this).find("span").html();
                    lines.push(ids.split(','));
                    lines.push(names.split(','));
                }else{
                    if(index<5){
                        values.push($(this).find("input").val());
                    }

                }
            });
            lines.push(values);
            arrays.push(lines);
        });

        var shopId = $("#shopId").val();
        var transTempId = $("#transTemId").val();
        var creatTime = $("#creatTime").val();

//判断首件和续件个数不能为0 start
        var sum = 0;
        $(".table-container:gt(0)").each(function(i){

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
            trans.temp.updateTransTemp(transTempId,shopId,transTempName,creatTime,arrays);
        }
//判断首件和续件个数不能为0 end

    });
    $("#back").click(function () {
        url = CONTEXT_PATH + "/get_trans_templates_list";
        // window.location.href=url;
        $.ajaxHtmlGet(url, null, {
            done: function (res) {
                if(res.code==0){
                    $("#contentBox").empty();
                    $("#contentBox").html(res.data);
                }
            }
        });
    })


        $(document).on("click",".defaultransnode",function(){
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

        })



        $("#selectAll").click(function () {
            var tag=  $("#selectAll").prop("checked");
            var treeObj = $.fn.zTree.getZTreeObj("area_treedit");
            $("#sltReverse").prop("checked",false);
            if(tag) {
                treeObj.checkAllNodes(true);
            }else{
                treeObj.checkAllNodes(false);
            }
            var unsltednodes =  $("#unselectednodesedit").text();
            if (unsltednodes.length>0) {
                var nodearr = unsltednodes.split(",");
                for (var k = 0, num = nodearr.length; k < num; k++) {
                    var node = treeObj.getNodeByParam("id", nodearr[k], null);
                    treeObj.checkNode(node, tag);
                }
            }
        });



        $("#sltReverse").click(function () {
            var tag = $("#sltReverse").prop("checked");
            $("#selectAll").prop("checked",false);
            var treeObj = $.fn.zTree.getZTreeObj("area_treedit");
            var nodes = treeObj.getCheckedNodes(true);
            var unsltednodes =  $("#unselectednodesedit").text();
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


    function edittrans_temp_writeNodes(treeNode,treeObj){
       // setNodeStatus(treeNode,treeObj);
    }

    function setParentNodeStatus(treeObj){
        var idarr="";
        var parentsNodeId="";
        var defaultObj=$("ul[class='table-container'] li span[class='defaultransnode']");
        var sltval = $("#selectednodesedit").text();
        if(defaultObj.length>0){
            defaultObj.each(function(){
                var str = $(this).attr("areaIds");
                sltval+=str+",";
            });
        }
        if (sltval.length > 0) {

            sltval=sltval.substring(0,sltval.length-1);
            idarr=sltval.split(",");
            for (var i = 0, l = idarr.length; i < l; i++) {
                var temp =idarr[i].substring(2,idarr[i].length);
                if(temp==="0000"){
                    parentsNodeId+=idarr[i]+",";

                }else{continue;}
            }
            if(parentsNodeId.length>0) {
                parentsNodeId = parentsNodeId.substring(0, parentsNodeId.length - 1);
                var parentsIdarr=parentsNodeId.split(",");
                for (var k = 0, num = parentsIdarr.length; k < num; k++) {
                    var node = treeObj.getNodeByParam("id", parentsIdarr[k], null);
                    treeObj.checkNode(node, true, false, false);
                    treeObj.setChkDisabled(node, true);
                }
            }

            treeObj.expandAll(false);
            sltval=sltval+",";
        }
        $("#newselectednodesedit").text("");
        $("#newselectednodesedit").text(sltval);
    }

    function setNodeStatus(treeNode,treeObj){

        var sltval = $("#newselectednodesedit").text();
        if (sltval.length > 0) {
            var nodeid = "";
            var pid = treeNode.id+"";
           var pidstr = pid.substring(0,2);
            sltval = sltval.substring(0, sltval.length - 1);
            idarr = sltval.split(",");
            for (var i = 0, l = idarr.length; i < l; i++) {
                var temp = idarr[i].substring(0, 2);
                if (pidstr === temp) {
                    nodeid += idarr[i] + ",";
                }
            }
            if (nodeid.length > 0) {
                nodeid = nodeid.substring(0, nodeid.length-1);
                var nodeidarr = nodeid.split(",");
                for (var j = 0, num = nodeidarr.length; j < num; j++) {
                    var node = treeObj.getNodeByParam("id", nodeidarr[j], null);
                    treeObj.checkNode(node, true, false, false);
                    treeObj.setChkDisabled(node, true);
                }

            }
        }
    }
    function getAreaNames(){
        var names =[];
        var treeObj = $.fn.zTree.getZTreeObj("area_treedit");
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

    product.transtemplate.showareafortitle();

</script>