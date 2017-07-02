/**
 * 命名空间
 */
CommonUtils.regNamespace("orderBook","list");
orderBook.list = (function(){
	var csvBool = false;
	var ajaxUrl = {
		url1: CONTEXT_PATH + "/orderBook/order_book_list/", //微信初始化订单数据
		url2: CONTEXT_PATH + "/orderBook/", //确认收单
		url3: CONTEXT_PATH + "/orderBook/trigger_reward/", // 触发奖励
		url4: CONTEXT_PATH + "/orderBook/export_order/", //导出订单
		url5: CONTEXT_PATH + "/orderBook/queryRecycleDetail/", //查看客服备注
		url6: CONTEXT_PATH + "/orderBook/saveRecycleDetail/", //保存客服备注
		url7: CONTEXT_PATH + "/orderBook/import_order", // 导入回收订单
	}
	var pageparam = [{
		url: ajaxUrl.url1, //微信初始化订单数据
		pageSize: 10,
		curPage: 0,
		countObj: "count",
		pageToolbarObj: "pageTypeToolbar",
		tableBodyObj: "orderBookItem",
		firstSearch:true,
		template:"/order/pc/orderBookItem"
	}]

	//初始化数据
	var _init = function(){
		_initPagination(pageparam[0]);
		_import();
	};

	var _initPagination = function(pageparam){
		var status1 =  $("#status").val();
		var orderBookQo = {};
		orderBookQo.userNumber = $("#userNumber").val();
		orderBookQo.userName = $("#userName").val();
		orderBookQo.orderBeginDate = $("#orderBookBeginDate").val();
		orderBookQo.orderEndDate = $("#orderBookEndDate").val();
		orderBookQo.orderBeginDate1 = $("#orderBookBeginDate1").val();
		orderBookQo.orderEndDate1 = $("#orderBookEndDate1").val();
		orderBookQo.phoneNumber = $("#phoneNumber").val();
		var type = $('#orderBookType option:selected').val();
		var status = $('#orderBookStatus option:selected').val();
		if(Number(type)!=-1){
			orderBookQo.type = type;
		}
		if(Number(status)!=-1){
			orderBookQo.status = status;
		}
		jumi.pagination("#"+pageparam.pageToolbarObj,pageparam.url+status1,orderBookQo,function (res,curPage) {
			if (res.code === 0) {
				//判断是否第一页
				var data = {
					items: res.data.items,
				};
				jumi.template(pageparam.template, data, function (tpl) {
					$("#" + pageparam.tableBodyObj).empty();
					$("#" + pageparam.tableBodyObj).html(tpl);
				})
			}
		})
	};

	//刷新当前页
	var _refreshPage = function(pageparam){
		var status1 =  $("#status").val();
		var pageTypeToolbar_page = $("#pageTypeToolbar_page").val();
		var orderBookVo = {};
		orderBookVo.curPage = pageTypeToolbar_page;
		orderBookVo.pageSize = 10;
		var newUrl = pageparam.url+status1;
		$.ajaxJson(newUrl,orderBookVo, {
			"done": function (res) {
				if(res.code===0) {
					//判断是否第一页
					var data = {
						items: res.data.items,
					};
					var template = "/order/pc/orderBookItem";
					var tableBodyObj = "orderBookItem";
					jumi.template(template, data, function (tpl) {
						$("#" + tableBodyObj).empty();
						$("#" + tableBodyObj).html(tpl);
					})
				}
			}
		})
	};

	//条件查询
	var _query = function(){
		_initPagination(pageparam[0]);
	};

	//确认收货
	var _confirm = function(orderId,status,cont){
		var url = ajaxUrl.url2+orderId;
		var OrderBookUo={};
		OrderBookUo.status=status;
		//var jsonStr = JSON.stringify(orderBookVo);
		var d = dialog({
			title: '确认收单',
			content: cont,
			width: 300,
			okValue: '确定',
			ok: function () {
				$.ajaxJsonPut(url,OrderBookUo,{
					"done":function(res){
						if(res.code==0) {
							_refreshPage(pageparam[0]);
							var dm = new dialogMessage({
								type: 1,
								fixed: true,
								msg: '确认收单成功',
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
	};

	var doReward = function(){

	};

	var _triggerReward = function(orderId,cont){
		var url = ajaxUrl.url3+orderId;
		var d = dialog({
			title: '确认收单',
			content: cont,
			width: 300,
			okValue: '确定',
			ok: function () {
				$.ajaxJsonPut(url,null,{
					"done":function(res){
						if(res.code==0) {
							_refreshPage(pageparam[0]);
							var dm = new dialogMessage({
								type: 1,
								fixed: true,
								msg: '触发成功',
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
	};

	var _close = function(orderId,status,cont){
		var url = ajaxUrl.url2+orderId;
		var OrderBookUo={};
		OrderBookUo.status=4;
		var d = dialog({
			title: '取消收单',
			content: cont,
			width: 300,
			okValue: '确定',
			ok: function () {
				$.ajaxJsonPut(url,OrderBookUo,{
					"done":function(res){
						if(res.code==0) {
							_refreshPage(pageparam[0]);
							var dm = new dialogMessage({
								type: 1,
								fixed: true,
								msg: '取消收单成功',
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
	};

	//查看客服备注
	var _queryCustomerNote = function(orderId){
		var url = ajaxUrl.url5+orderId;
		$.ajaxJsonGet(url,null,{
			"done":function (res) {
				if(res.code===0){
					_openCustomerNote(res.data);
				}
			}
		});
	};

	//打开客服备注
	var _openCustomerNote = function(data){
		var data = {
			data:data
		};
		var html = jumi.templateHtml('customerOrderBookNote.html',data,STATIC_URL+"/tpl/order/pc/");
		var titleStr="客服备注";
		dialog({
			content: html,
			title: titleStr,
			okValue: '确定',
			ok: function() {
				var validateForm = $("#customerOrderBookNoteForm");
				var form = _validate(validateForm);
				if (form.valid()) {
					var orderId = $("#orderId").val();
					_saveCustomer(orderId);
				}else {
					return false;
				}
			},
			cancelValue:'取消',
			cancel:function () {
			}
		}).width(400).showModal();
	};

	//查看客服备注
	var _queryReceiveNote = function(orderId){
		var url = ajaxUrl.url5+orderId;
		$.ajaxJsonGet(url,null,{
			"done":function (res) {
				if(res.code===0){
					_openReceiveNote(res.data);
				}
			}
		});
	}

	//打开收货备注
	var _openReceiveNote = function(data){
		var data = {
			data:data
		};
		var html = jumi.templateHtml('receiveOrderBookNote.html',data,STATIC_URL+"/tpl/order/pc/");
		var titleStr="收货备注";
		dialog({
			content: html,
			title: titleStr,
			okValue: '确定',
			ok: function() {
				var validateForm = $("#receiveOrderBookNoteForm");
				var form = _validate(validateForm);
				if (form.valid()) {
					var orderId = $("#orderId").val();
					_saveReceive(orderId);
				}else {
					return false;
				}
			},
			cancelValue:'取消',
			cancel:function () {
			}
		}).width(400).showModal();
	};

	//保存客服备注内容
	var _saveCustomer=function(orderId){
		var orderVo={}
		$("#customerOrderBookNoteForm").find("input,textarea").each(function () {
			orderVo[$(this).prop("name")]=$(this).val();
		})
		var url=ajaxUrl.url6+orderId;
		$.ajaxJsonPut(url,orderVo,{
			"done":function(res){
				if(res.code===0){
					_refreshPage(pageparam[0]);
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

	//保存收货备注内容
	var _saveReceive=function(orderId){
		var orderVo={}
		$("#receiveOrderBookNoteForm").find("input,textarea").each(function () {
			orderVo[$(this).prop("name")]=$(this).val();
		})
		var url=ajaxUrl.url6+orderId;
		$.ajaxJsonPut(url,orderVo,{
			"done":function(res){
				if(res.code===0){
					_refreshPage(pageparam[0]);
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

	//校验客服备注的提交内容
	var _validate=function(validateForm){
		var form=validateForm;
		form.validate({
			rules: {
				customRemark:{
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

	var _export = function(){
		var orderBookQo = {};
		orderBookQo.userNumber = $("#userNumber").val();
		orderBookQo.userName = $("#userName").val();
		orderBookQo.orderBeginDate = $("#orderBookBeginDate").val();
		orderBookQo.orderEndDate = $("#orderBookEndDate").val();
		orderBookQo.orderBeginDate1 = $("#orderBookBeginDate1").val();
		orderBookQo.orderEndDate1 = $("#orderBookEndDate1").val();
		orderBookQo.phoneNumber = $("#phoneNumber").val();
		var type = $('#orderBookType option:selected').val();
		var status = $('#orderBookStatus option:selected').val();
		if(Number(type)!=-1){
			orderBookQo.type = type;
		}
		if(Number(status)!=-1){
			orderBookQo.status = status;
		}
		var jsonStr = JSON.stringify(orderBookQo);
		var status1 = $("#status").val();
		window.location.href =ajaxUrl.url4+"?status="+status1+"&orderBookQo="+jsonStr;
	};

	var _import = function(){
		var url = ajaxUrl.url7;
		$("#fileupload_csv").fileupload({
			url: url,
			dataType: 'json',
			singleFileUploads:true,
			multipart: true,
			add: function (e, data) {
				data.submit().always(function(res){
					if(res.code==1){
						_refreshPage(pageparam[0]);
						var dm = new dialogMessage({
							fixed:true,
							msg:'操作成功！',
							isAutoDisplay:true,
							time:1500
						});
						dm.render();
					}else{
						var dm = new dialogMessage({
							type:2,
							fixed:true,
							msg:'操作失败！',
							isAutoDisplay:true,
							time:1500
						});
						dm.render();
					}
				})
			}
		})
	};

	var _import1 = function(){
		var url = ajaxUrl.url7;
		$.ajaxJson(url,null,{
			"done":function (res) {
				if(res.data.code===1){
					_refreshPage(pageparam[0]);
					var dm = new dialogMessage({
						fixed:true,
						msg:'操作成功！',
						isAutoDisplay:true,
						time:1500
					});
					dm.render();
				}else if(res.data.code==2){
					var dm = new dialogMessage({
						type:2,
						fixed:true,
						msg:'操作失败！文件路径格式可能错误，正确文件路径格式(D:/test.xls)',
						isAutoDisplay:true,
						time:3000
					});
					dm.render();
				}else{
					var dm = new dialogMessage({
						type:2,
						fixed:true,
						msg:'操作失败！',
						isAutoDisplay:true,
						time:3000
					});
					dm.render();
				}
			}
		});
	}

	return {
		init:_init, //初始化加载订单
		refreshPage:_refreshPage, //刷新
		confirm:_confirm, //确认收单
		query:_query, //查询
		triggerReward:_triggerReward, //触发奖励
		close:_close, //关闭订单
		queryCustomerNote:_queryCustomerNote, //客服备注
		queryReceiveNote:_queryReceiveNote, //收货备注
		exportOrderBook:_export, //导出回收单
		importOrderBook:_import1, //导入回收单
	};

})();
