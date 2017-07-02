<style>
    .sell-third-checkbox span{
        color: #999;
    }
    .sell-third-checkbox .checkBox label {

        font-size: 16px;
        top: -2px;
        left: 2px;

    }
</style>
<div class="main-content-pc" style="padding: 23px 24px 23px 5px;">
    <div class="mallBuilding manage">
        <div class="col-xs-12" style="padding-left: 0;">
            <div class="form-group col-xs-6 zhyh-mb zhyh-pl" style="padding-left:0">
                <label style="padding-left:0;min-width:0">模板名称:</label><input type="text" id="transTemName" class="form-control distance" placeholder="请输模板名称" />

            </div>
        </div>
        <div class="jm-table" id="transTEM0" style="font-size: 12px;">
            <ul class="table-hander">
                <li style="width: 35%;text-align: left;  padding-left: 10px;">可配送至</li>
                <li>首件（个）</li>
                <li>运费（元）</li>
                <li>续件（个）</li>
                <li>续件（元）</li>
                <li>操作</li>
            </ul>
            <ul class="table-container" id="first-table-container">
                <li style="width: 35%;text-align: left;  padding-left: 10px;">
                    <div class="page-left" style="margin-top: 0;">
                        <div class="btn  btn-lightgray btn-sm dialog"  style="float: left;"  onclick="addPS()">指定可配送区域和运费</div>
                        <div  class="text-center" id="onloadmsg" style="float: left; margin-left: 50px;display: none;"><i class="iconfont icon-loading" style="float: left;"></i><div style="float: left;color:#f78211;">&nbsp;请稍后...</div></div>
                    </div>

                    <div id="dialogAreainfoadd" class="dialoginfo dhk" >

                        <div class="jm-table jm-table1">
                            <ul class="table-hander">
                                <li>
                                    <i class="iconfont icon-classify1"></i>
                                    可选省、市、区
                                </li>
                            </ul>
                            <ul id="area_tree" class="ztree" style="border: 1px solid #617775;overflow-y: scroll;height: 500px;">

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
                    <input style="float: none;width:50%;" type="text" class="form-control distance" placeholder="" />
                </li>
                <li style="display: none;" class="form-group form-group-zwmb">
                    <input style="float: none;width:50%;" type="text" class="form-control distance" placeholder="" />
                </li>
                <li style="display: none;" class="form-group form-group-zwmb">
                    <input style="float: none;width:50%;" type="text" class="form-control distance" placeholder="" />
                </li>
                <li style="display: none;" class="form-group form-group-zwmb">
                    <input style="float: none;width:50%;" type="text" class="form-control distance" placeholder="" />
                </li>
                <li style="display: none;" class="form-group form-group-zwmb">
                    <span  class='delete' style="cursor: pointer;">删除</span>
                </li>

            </ul>

        </div>
        <div style="margin: 61px 0 69px;">
            <div style="padding:0 30px;" class="btn btn-lightgray btn-radius-5 btn-lg col-xs-offset-4" id="back">返回</div>
            <div id="addTransTempBtn" style="margin-left:8px;" class="btn btn-darkorange btn-radius-5 btn-lg">保存</div>
        </div>
        <div id="selectednodes" style="display:none;"></div>
        <div id="unselectednodes" style="display:none;"></div>

    </div>
</div>

	 <script type="text/javascript">

	    $(document).ready(function(){

			onLoadZTree();
            $("#unselectednodes").text("");
            $("#selectednodes").text("");
            clearZtreeStatus();
		});

	 	function addPS()  {
            $("#onloadmsg").fadeIn('slow', function() {
            var treeObj = $.fn.zTree.getZTreeObj("area_tree");
            if (treeObj !== null) {
                var nodesAll = treeObj.getNodes();  //清除disabled
                for (var j = 0, num = nodesAll.length; j < num; j++) {
                    treeObj.setChkDisabled(nodesAll[j], false, true, true);
                }

                treeObj.checkAllNodes(false); // 清除上次操作

                $("div[class='dialoginfo dhk']:gt(0)").each(function(){  // 清除对话框缓存
                        $(this).remove();
                });

                shop_setChkDisabled(treeObj);//设置选中节点状态
                showAreaTreeWindow();
                $('#onloadmsg').fadeOut('100',function() {
                    // Animation complete.
                });
            }
            });
        }

        function shop_setChkDisabled(treeObj){

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
                if(pnodes.length>0) {
                    pnodes = pnodes.substring(0, pnodes.length - 1);
                    var newval = _.difference(pnodes.split(","), nodestr.split(","));
                    var temp = newval.join(",");
                    $("#unselectednodes").text("");
                    $("#unselectednodes").text(temp);
                }
            }

        }
        function showAreaTreeWindow() {
            $("#sltReverse").prop("checked","");
            $("#selectAll").prop("checked","");
			var elem = $('#dialogAreainfoadd');
            var dg = dialog({
				title:"选择可配送区域",
			    content:elem,
			    width :500,
                id:'dialog_info',
			    okValue:"点击确认",
				ok:function(){
				   saveTransTemp();
				},
				cancelValue:"点击取消",
				cancel:function(){
                    dialog.get('dialog_info').close().remove();
				},
                onclose: function () {
                    dialog.get('dialog_info').close().remove();
                }
			});
            dg.showModal();
	 }

	 function clearZtreeStatus(){
         var treeObj = $.fn.zTree.getZTreeObj("area_tree");
         if (treeObj !== null) {
             var nodesAll = treeObj.getNodes();  //清除disabled
             for (var j = 0, num = nodesAll.length; j < num; j++) {
                 treeObj.setChkDisabled(nodesAll[j], false, true, true);
             }
             treeObj.checkAllNodes(false); // 清除上次操作
         }
     }

	     $(document).on("click",".delete",function(){
             var objval = $("#selectednodes").text();
             if (objval.length > 0) {
                 var areaIds = $(this).parent().parent().find("li:first").find(".areaidhid").text(); //删除选中节点
                 var newval = _.difference(objval.split(","), areaIds.split(","));
                 var temp=newval.join(",");
                 $("#selectednodes").text("");
                 $("#selectednodes").text(temp);
             }
             $(this).parent().parent().remove(); //删除记录

	     });

		 $("#back").click(function () {
             url = CONTEXT_PATH + "/get_trans_templates_list";
             // window.location.href=url;
             $.ajaxHtmlGet(url, null, {
                 done: function (res) {
                     $("#contentBox").empty();
                     $("#contentBox").append(res.data);
                 }
             });
	     });

	    function saveTransTemp(){
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
                }else{
                    areaIds += nodes[i].id+",";
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
		    html.find("#dialogAreainfoadd").remove();
		    html.find("li").css("display","static");
		    html.removeAttr("id");
            html.find("li:first").append("<span style='display: block;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;cursor:pointer;'  class='showtitle' areaIds="+areaIds+">"+areaNames+"</span> <span class='areaidhid' style='display: none;'>"+areaIds+"</span>");
         //   html.find("li:first").append("<span style='display: block;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;cursor:pointer;'  class='showtitle' areaIds="+areaIds+">"+areaNames+"</span><span class='transnode' style='display:none ;'>"+areatIds+"</span>");
			$("#transTEM0").append(html);
            var idstr= $("#selectednodes").text();
            idstr+=areatIds;
            $("#selectednodes").text("");
            $("#selectednodes").text(idstr);


	    }

		//保存运费模板

		var arry = [];
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
                trans.temp.addTransTemp(transTempName,arrays);
			}
//判断首件和续件个数不能为0 end

		})

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
        product.transtemplate.showareafortitle();

        function getAreaNames(){
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

	   </script>


	</body>

</html>