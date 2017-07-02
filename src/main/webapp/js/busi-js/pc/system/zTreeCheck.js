        var zTree;
		var zTreedit;
		var setting = {

		    view: {
		        dblClickExpand: false,//双击节点时，是否自动展开父节点的标识
		        showLine: true,//是否显示节点之间的连线
		        fontCss:{'color':'black','font-weight':'bold'},//字体样式函数
		        selectedMulti: false //设置是否允许同时选中多个节点

		    },
		    check:{
		        chkboxType: { "Y": "ps", "N": "ps" },
		        chkStyle: "checkbox",//复选框类型
		        enable: true //每个节点上是否显示 CheckBox
		    },
		    data: {
		        simpleData: {//简单数据模式
		            enable:true,
		            idKey: "id",
		            pIdKey: "pid",
		            rootPId: "-1"

		        }
		    },
		    callback: {
		        beforeClick: function(treeId, treeNode) {
		            zTree = $.fn.zTree.getZTreeObj("area_tree");
		            if (treeNode.isParent) {
		                zTree.expandNode(treeNode);//如果是父节点，则展开该节点
		            }else{
		                zTree.checkNode(treeNode, !treeNode.checked, true, true);//单击勾选，再次单击取消勾选
		            }
		        }
		    }
		};
	
		
	function onLoadZTree(){
		    var treeNodes;
		    $.ajax({
		        async:true,//是否异步
		        cache:false,//是否使用缓存
		        type:'POST',//请求方式：post
		        dataType:'json',//数据传输格式：json
		        url:window.location.origin+"/msa/trans_temp/get_area_list",//请求的action路径
		        error:function(){
		            //请求失败处理函数
					var dm = new dialogMessage({
						type: 2,
						fixed: true,
						msg: '对不起，操作失败！',
						isAutoDisplay: true,
						time: 1000
					});
					dm.render();
		        },
		        success:function(data){         		
		            treeNodes = data;//把后台封装好的简单Json格式赋给treeNodes
					var t = $("#area_tree");
					// var t = $("#area_tree", setting.treeObj.get(0).ownerDocument);
		            t = $.fn.zTree.init(t, setting, treeNodes);

		        }
		    });			
		}

		var settingedit = {

			view: {
				dblClickExpand: false,//双击节点时，是否自动展开父节点的标识
				showLine: true,//是否显示节点之间的连线
				fontCss:{'color':'black','font-weight':'bold'},//字体样式函数
				selectedMulti: false //设置是否允许同时选中多个节点

			},
			check:{
				chkboxType: { "Y": "ps", "N": "ps" },
				chkStyle: "checkbox",//复选框类型
				enable: true //每个节点上是否显示 CheckBox
			},
			data: {
				simpleData: {//简单数据模式
					enable:true,
					idKey: "id",
					pIdKey: "pid",
					rootPId: "-1"

				}
			},
			callback: {
				beforeClick: function(treeId, treeNode) {
					zTree = $.fn.zTree.getZTreeObj("area_treedit");
					if (treeNode.isParent) {
						zTree.expandNode(treeNode);//如果是父节点，则展开该节点

					//	edittrans_temp_writeNodes(treeNode,zTree);
					}else{
						zTree.checkNode(treeNode, !treeNode.checked, true, true);//单击勾选，再次单击取消勾选
					}
				},
				onExpand: zTreeOnExpand
			}
		};
		function zTreeOnExpand(event, treeId, treeNode) {
			/*var treeObj = $.fn.zTree.getZTreeObj(treeId);
			setNodeStatus(treeNode,treeObj);*/
		};

		function onLoadZTreedit(){
			var treeNodes;

			$.ajax({
				async:true,//是否异步
				cache:false,//是否使用缓存
				type:'POST',//请求方式：post
				dataType:'json',//数据传输格式：json
				url:window.location.origin+"/msa/get_area_list",//请求的action路径
				error:function(){
					//请求失败处理函数
					alert('亲，请求失败！');
				},
				success:function(data){
					treeNodes = data;//把后台封装好的简单Json格式赋给treeNodes
					var t = $("#area_treedit");
					t = $.fn.zTree.init(t, settingedit, treeNodes);
				}
			});
		};

