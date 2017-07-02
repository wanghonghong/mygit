
/***<===========================================================订单管理============================================================***/

/***
 * 根据不同条件查询
 */

CommonUtils.regNamespace("order");

order.queryOrder = function(){
	var orderManageVo = {};
	orderManageVo.consigneePhone = $("#consigneePhone").val();
	orderManageVo.consigneeName = $("#consigneeName").val();
	orderManageVo.weChatName = $("#weChatName").val();
	orderManageVo.orderNum = $("#orderNum").val();
	orderManageVo.payOrderNum = $("#payOrderNum").val();
	orderManageVo.status = $("#status").val();
	orderManageVo.orderBeginDate = $("#orderBeginDate").val();
	orderManageVo.orderEndDate = $("#orderEndDate").val();
    orderManageVo.goodStatus = $("#goodStatus").val();
	var platform = $('#select1 option:selected').val();
	var jsonStr = JSON.stringify(orderManageVo);
	var type = $("#type").val();
	url=CONTEXT_PATH+'/order/order_info/query/'+type;
	order.ajaxPost(jsonStr,url);
};

/**操作请求(查找)*/
order.ajaxPost=function(jsonStr,url){
	$.ajaxHtml(url,jsonStr,{
		"done":function(res){
			$("#orderManage").empty();
        	$("#deliveryItem").empty();
        	$("#orderManage").append(res.data);
        	$("#deliveryItem").append(res.data);
        	$("#tableBody .jm-table-control").rowspan([3,4,5,6]);
		}
	});
};



/***
*订单管理
*不同订单状态的页面输出
**/
order.selectStatus=function(status){
	var url = CONTEXT_PATH+'/order/order_info/'+status;
	$.ajaxHtml(url,jsonStr,{
		"done":function(res){
			$("#orderManage").empty();
        	$("#deliveryItem").empty();
        	$("#orderManage").append(res.data);
		}
	});
};

/***
 * 订单详情弹出框
 * @param orderInfoId
 */

order.queryOrderDetail = function(orderInfoId){
	var url = CONTEXT_PATH+'/order/order_detail/'+orderInfoId;
	$.ajaxHtmlGet(url,'',{
		"done":function(res) { 
        	$("#orderDetail").remove();
        	$("body").append(res.data);
            order.popMsg();
        }
	});
};


order.popMsg = function(){
	var elem = document.getElementById('orderDetail');
	memudialog=dialog({
		id:'show-dialog',
		width: 500,
		height: 400,
		title: '订单详情',
		content: elem 
	});
	memudialog.show();
	memudialog.onremove = function () {
		$('#orderDetail').remove();
	};	
};

/***
 * 取消订单
 * @param orderInfoId
 */
order.cancel = function(orderInfoId){
	var orderManageVo = {};
	orderManageVo.status = 7;
	dialog({
		width: 200,
		height: 50,
	    content: '确定要取消订单?',
	    okValue:'确定',
	    cancelValue:'取消',
	    ok: function () {
	    	order.updates(orderInfoId,orderManageVo);
	    },
	    cancel: function () {
	    }
	}).show();
}


/***
 * 恢复订单
 * @param orderInfoId
 */
order.restore = function(orderInfoId){
	var orderManageVo = {};
	orderManageVo.status = 0;
	dialog({
		width: 200,
		height: 50,
	    content: '确定要恢复订单?',
	    okValue:'确定',
	    cancelValue:'取消',
	    ok: function () {
	    	order.updates(orderInfoId,orderManageVo);
	    },
	    cancel: function () {
	    }
	}).show();
}


/***
 * 修改价格
 * @param orderInfoId
 */
order.updatePrice = function(orderInfoId){
	var url = CONTEXT_PATH+'/order/update_price/'+orderInfoId;
	$.ajaxHtmlGet(url,'',{
		"done":function(res) { 
        	$("#updatePrice").remove();
        	$("body").append(res.data);
            order.updatePriceMsg();
        }
	});
}

order.updatePriceMsg = function(){
	var elem = document.getElementById('updatePrice');
	memudialog=dialog({
		id:'show-dialog',
		width: 600,
		height: 350,
		title: '修改价格',
		content: elem 
	});
	memudialog.show();
	memudialog.onremove = function () {
		$('#updatePrice').remove();
	};	
};

/***
 * 保存价格
 * @param orderInfoId
 */
order.savePrice = function(orderInfoId){
	var url = CONTEXT_PATH+'/order/save_price/'+orderInfoId;
	var orderManageVo = {};
	var price = "";
	var discount = "";
	var totalPrice1 = $("#totalPrice1").val();
	var totalPrice2 = $("#totalPrice2").val();
	var totalPrice3 = $("#totalPrice3").val();
	var sendFee = $("#sendFee").val();
	if(totalPrice1!=""){
		price = Number(totalPrice1*100)-Number(sendFee);
	}
	if(totalPrice2!=""){
		price = Number(totalPrice2*100)-Number(sendFee);
	}
	if(totalPrice3!=""){
		price = Number(totalPrice3)*100;
		sendFee = 0;
	}
	var discountAmount1 = $("#discountAmount1").val();
	var discountAmount2 = $("#discountAmount2").val();
	if(discountAmount1!=""){
		discount = discountAmount1;
	}
	if(discountAmount2!=""){
		discount = discountAmount2;
	}
	orderManageVo.preferentialWay = $("input[type='radio']:checked").val();
	if(!price){
		alert("金额不能为空！");
		return false;
	}
	orderManageVo.totalPrice = Number(price);
	orderManageVo.discountAmount = Number(discount)*100;
	orderManageVo.sendFee = Number(sendFee);
	var jsonStr = JSON.stringify(orderManageVo);
	var res = $.ajaxJson(url,jsonStr);
	var orderVo = {};
	var status = $("#status").val();
	orderVo.status = status;
	var orderUrl = CONTEXT_PATH+'/order/order_item/'+status;
	if(res.code==0){
		var res = $.ajaxHtml(orderUrl,orderVo);
		if(res.code==0){
			$("#tableBody").empty();
			$("#tableBody").append(res.data);
			memudialog.close().remove();
			$("#tableBody .jm-table-control").rowspan([3,4,5,6]);
		}else{
			alert(res.msg);
		}
	}
}



/***
 * 订单管理确认收货弹出框
 * @param orderInfoId
 */
order.confirm = function(orderInfoId){
	var orderManageVo = {};
	orderManageVo.status = 3;
	dialog({
		width: 260,
		height: 100,
	    content: '请确认是否帮客户提交确认收货?',
	    okValue:'确定',
	    cancelValue:'取消',
	    ok: function () {
	    	order.updates(orderInfoId,orderManageVo);
	    },
	    cancel: function () {
	    }
	}).show();
}

/***
 * 卖家备注
 * @param orderInfoId
 */
order.customerService = function(orderInfoId){
	var url = CONTEXT_PATH+'/order/customer_Service/'+orderInfoId;
	$.ajaxHtmlGet(url,'',{
		"done":function(res) { 
			$("#customerService").remove();
            $(res.data).appendTo("body");
            order.customerServiceMsg();
        }
	});
}
order.customerServiceMsg = function(){
	var elem = document.getElementById('customerService');
	memudialog=dialog({
		id:'show-dialog',
		width: 600,
		height: 400,
		title: '卖家备注',
		content: elem 
	});
	memudialog.show();
	memudialog.onremove = function () {
		$('#customerService').remove();
	};	
};

/***
 * 保存卖家备注信息
 * @param orderInfoId
 */
order.saveCustomer = function(orderInfoId){
	var url = CONTEXT_PATH+'/order/save_customer/'+orderInfoId;
	var orderManageVo = {};
	orderManageVo.sellerNote = $("#sellerNote").val();
	var res = $.ajaxHtml(url,orderManageVo);
	if(res.code==0){
		memudialog.close().remove();
	}
}

/***
 * 减免运费
 * @param orderInfoId
 */
order.breakFreight = function(orderInfoId){
	var url = CONTEXT_PATH+'/order/break_freight/'+orderInfoId;
	$.ajaxHtmlGet(url,'',{
		"done":function(res) { 
			$("#breakFreight").remove();
            $(res.data).appendTo("body");
            order.breakFreightMsg();
        }
	});
};

order.breakFreightMsg = function(){
	var elem = document.getElementById('breakFreight');
	memudialog=dialog({
		width: 600,
		height: 400,
		title: '减免运费',
		content: elem 
	});
	memudialog.show();
	memudialog.onremove = function () {
		$('#breakFreight').remove();
	};
};


/***
 * 保存减免运费信息
 * @param orderInfoId
 */
order.saveBreakFreight = function(orderInfoId){
	var url = CONTEXT_PATH+'/order/save_breakFreight/'+orderInfoId;
	var orderManageVo = {};
	var tatalPrice =  $("#totalPrices").val();
	orderManageVo.totalPrice = Number(tatalPrice)*100;
	orderManageVo.sendFee = 0;
	var jsonStr = JSON.stringify(orderManageVo);
	var res = $.ajaxHtml(url,jsonStr);
	var orderVo = {};
	var status = $("#status").val();
	orderVo.status = status;
	var orderUrl = CONTEXT_PATH+'/order/order_item/'+status;
	if(res.code==0){
		var res = $.ajaxHtml(orderUrl,orderVo);
		if(res.code==0){
			$("#tableBody").empty();
			$("#tableBody").append(res.data);
			memudialog.close().remove();
			// $("#tableBody .jm-table-control").rowspan([3,4,5,6]);
		}else{
			alert(res.msg);
		}
	}
};

/***
 * 二次发货
 * @param orderInfoId
 */
order.secondShipment = function(orderInfoId){
	var url = CONTEXT_PATH+'/order/second_Shipment/'+orderInfoId;
	$.ajaxHtmlGet(url,'',{
		"done":function(res) { 
            $("#secondShipment").remove();
            $(res.data).appendTo("body");
            order.secondShipmentMsg();
        }
	});
}


order.secondShipmentMsg = function(){
	var elem = document.getElementById('secondShipment');
	memudialog=dialog({
		width: 600,
		height: 600,
		title: '二次发货',
		content: elem 
	});
	memudialog.show();
	memudialog.onremove = function () {
		$('#secondShipment').remove();
	};
};



/***
 * 退款操作
 * @param orderInfoId
 */

order.agreeRefund = function(orderInfoId,statue){
    var url = CONTEXT_PATH+'/order/agreeForRefund/'+orderInfoId+"/"+statue;
    $.ajaxHtmlGet(url,'',{
        "done":function(res) {
            $("#agreeRefund").remove();
            $("body").append(res.data);
            order.agreeRefundMsg();
        }
    });
};
/***
 * 退款操作
 */
order.agreeRefundMsg = function(orderInfoId){
    var elem = document.getElementById('agreeRefund');
    memudialog=dialog({
        id:'show-dialog',
        width: 430,
        height: 600,
        title: '退款操作',
        content: elem
    });
    memudialog.show();
    memudialog.onremove = function () {
        $('#agreeRefund').remove();
    };
}



/***<===========================================================发货管理============================================================***/

/***
*发货管理
*不同订单状态的页面输出
**/
order.deliveryStatus=function(status){
	var url = CONTEXT_PATH+'/order/order_delivery/'+status;
	$.ajaxHtmlGet(url,'',{
		"done":function(res) { 
			$("#deliveryItem").empty();
        	$("#deliveryItem").append(res.data);
        	$("#orderManage").empty();
        }
	});
};

/***
 * 立即发货页面
 * @param orderInfoId
 */
order.immediateDelivery = function(orderInfoId){
	var url = CONTEXT_PATH+'/order/immediate_delivery/'+orderInfoId;
	$.ajaxHtmlGet(url,'',{
		"done":function(res) { 
			$("#immediateDelivery").remove();
            $(res.data).appendTo("body");
            order.immediateDeliveryMsg();
        }
	});
};


order.immediateDeliveryMsg = function(){
	var elem = document.getElementById('immediateDelivery');
	memudialog=dialog({
		width: 430,
		height: 450,
		title: '立即发货',
		content: elem 
	});
	memudialog.show();
	memudialog.onremove = function () {
		$('#immediateDelivery').remove();
	};
};

//关闭发货页面
order.closeRefund = function(){
	memudialog.close().remove();
}

order.refundGoodConfirm = function(orderGoodId){
	var refundGoodVo = {};
	refundGoodVo.goodstatus = 1;
	dialog({
		width: 260,
		height: 100,
		content: '买家已退货，请确认是否收货?',
		okValue:'确定',
		cancelValue:'取消',
		ok: function () {
			order.updateGood(orderGoodId,refundGoodVo);
		},
		cancel: function () {
		}
	}).show();
};

/***
 * 查看物流页面
 * @param orderInfoId
 */
order.queryLogistics = function(orderInfoId){
	var url = CONTEXT_PATH+'/order/logistics/'+orderInfoId;
	$.ajaxHtmlGet(url,'',{
		"done":function(res) { 
			$("#logistics").remove();
            $(res.data).appendTo("body");
            order.logisticsMsg();
        }
	});
};


order.logisticsMsg = function(){
	var elem = document.getElementById('logistics');
	memudialog=dialog({
		id:'show-dialog',
		width: 600,
		height: 600,
		title: '查看物流',
		content: elem 
	});
	memudialog.show();
	memudialog.onremove = function () {
		$('#logistics').remove();
	};	
};

/***
 * 提交发货
 */
order.saveOrderDelivery = function(orderInfoId){
	var url = CONTEXT_PATH+'/orderDelivery/order_delivery/'+orderInfoId;
	var orderDeliveryVo = {};
	var transCompany = $("#transCompany").val();
	var transNumber = $("#transNumber").val();
	var deliveryNote = $("#deliveryNote").val();
	orderDeliveryVo.transCompany = transCompany;
	orderDeliveryVo.transNumber = transNumber;
	orderDeliveryVo.deliveryNote = deliveryNote;
	var orderInfoVo = {};
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
	if(!deliveryNote){
		var d = dialog({
			title: '提示',
			content:'请填写物流备注！'
		});
		d.showModal();
		setTimeout(function() {
			d.close().remove();
		}, 1000);
		return ;
	}
	var res = $.ajaxJson(url,orderDeliveryVo);
	if(res.code==0){
		var orderUrl = CONTEXT_PATH+'/order/order_deliveryItem/'+$("#status").val();
		var res = $.ajaxHtml(orderUrl,orderInfoVo);
		if(res.code==0){
			$("#tableBody").empty();
			$("#tableBody").append(res.data);
			memudialog.close().remove();
			$("#tableBody .jm-table-control").rowspan([3,4,5,6]);
		}else{
			alert(res.msg);
		}
	}else{
		alert(res.msg);
	}
}


/***<===========================================================公用js方法============================================================***/


/***
 * 修改订单管理状态的公用方法
 * @param jsonStr
 */
order.updates = function(orderInfoId,orderManageVo){
	var url = CONTEXT_PATH+'/order/order_update/'+orderInfoId;
	var res = $.ajaxJson(url,orderManageVo);
	var status = $("#status").val();
	if(res.code==0){
		var res = "";
		var orderVo = {};
		orderVo.status = status;
		if(orderManageVo.status==6){
			var deliveryUrl = CONTEXT_PATH+'/order/order_deliveryItem/'+status;
			res = $.ajaxHtml(deliveryUrl,orderVo);
		}else{
			var orderUrl = CONTEXT_PATH+'/order/order_item/'+status;
			res = $.ajaxHtml(orderUrl,orderVo);
		}
		if(res.code==0){
			$("#tableBody").empty().append(res.data);
		}else{
			alert(res.msg);
		}
		$("#tableBody .jm-table-control").rowspan([3,4,5,6]);
	}
};

order.updateGood = function(orderGoodId,refundGoodVo){
	var url = CONTEXT_PATH+'/refundGood/refund_update/'+orderGoodId;
	var goodStatus = 1;
	refundGoodVo.goodStatus = goodStatus;
	var res = $.ajaxJson(url,refundGoodVo);
	if(res.code==0){
		var res = "";
		var orderVo = {};
		orderVo.goodStatus = goodStatus;
		var refundUrl = CONTEXT_PATH+'/order/refund_goodItem/'+goodStatus;
		res = $.ajaxHtml(refundUrl,orderVo);
		if(res.code==0){
			$("#tableBody").empty().append(res.data);
		}else{
			alert(res.msg);
		}
		$("#tableBody .jm-table-control").rowspan([3,4,5,6]);
	}
};


/***================================================================分页js========================================================****/
//--------------------分页----------------------

//每页显示条数
var pagesize=10;
//Ajax加载分页元素
order.initPagination = function() {
	var num_entries =$("#count").val();
	// 创建分页
	$("#pagina").pagination(num_entries, {
	    num_edge_entries: 2,
	    num_display_entries: 4,
	    callback: pageselectCallback,
	    items_per_page:pagesize,
	    prev_text:"<li class='iconfont icon-left  '></li>",
		next_text:"<li class='iconfont icon-right  '></li>"
	});
 };
 //分页回调函数
function pageselectCallback(page_index, jq){
	loadhtmlOrder(page_index);
	var new_content = $("#hiddenresult div.result:eq("+page_index+")").clone();
	
	return false;
}


//第二次点击分页数加载数据
function loadhtmlOrder(page_index){
	var orderManageVo = {};
	orderManageVo.consigneePhone = $("#consigneePhone").val();
	orderManageVo.consigneeName = $("#consigneeName").val();
	orderManageVo.weChatName = $("#weChatName").val();
	orderManageVo.orderNum = $("#orderNum").val();
	orderManageVo.payOrderNum = $("#payOrderNum").val();
	var state = $("#status").val();
	if(state!=11){
		orderManageVo.status = $("#status").val();
	}
	orderManageVo.orderBeginDate = $("#orderBeginDate").val();
	orderManageVo.orderEndDate = $("#orderEndDate").val();
	orderManageVo.curPage = page_index;
	orderManageVo.pageSize = pagesize;
	
	var jsonStr = JSON.stringify(orderManageVo);
	var type = $("#type").val();
	var url = "";
	if(type==1){
		url = CONTEXT_PATH+'/order/order_item/'+state;
	}else if(type==2){
		url = CONTEXT_PATH+'/order/order_deliveryItem/'+state;
	}else{
	    var goodStatus = 1;
	    url = CONTEXT_PATH+'/order/refund_goodItem/'+goodStatus;
    }
	$.ajaxHtml(url,jsonStr,{
		"done":function(res){
			$("#tableBody").empty().append(res.data); //装载对应分页的内容
			$("#tableBody .jm-table-control").rowspan([3,4,5,6]);
		}
	});
	
};

//初始化分页
//initPagination();

//ajax加载
//---------------------分页END----------------------------

 
 