/**
 * 命名空间
 */
CommonUtils.regNamespace("agreeRefund");
$(function(){
	$("#tableBody .m-table-control").each(function(i){
		console.log(i)
		$(this).rowspan([1,2,3,4,5]);
	});

	$(".btn-slide").click(function(){
		$("#m-search").slideToggle("fast");
		$(this).toggleClass("btn-slide1"); return false;
	})
	$(".m-refund").hide().eq(0).show();
	var tabul = $(".m-tab2 ul li");
	tabul.click(function(){
		tabul.eq($(this).index()).addClass("z-sel").siblings().removeClass('z-sel');
		$(".m-refund").hide().eq($(this).index()).show();
	});

})

agreeRefund.changePW = function(){
	var preferentialWay = $("input[name='buy-r']:checked").val();
	if(preferentialWay==1){
		var realPrice = $("#realPrice").val();
		$("#consultRefund").attr("readOnly",true);
		$("#refuseReason").attr("readOnly",true);
		$("#applyRefund").val(realPrice);
		$("#consultRefund").val('');
		$("#refuseReason").val('');
		$("#radioBox7").click();
	}else if(preferentialWay==2){
		$("#applyRefund").attr("readOnly",true);
		$("#refuseReason").attr("readOnly",true);
		$("#consultRefund").attr("readOnly",false);
		$("#applyRefund").val('');
		$("#refuseReason").val('');
		$("#radioBox7").click();
	}else if(preferentialWay==3){
		$("#consultRefund").attr("readOnly",true);
		$("#refuseReason").attr("readOnly",false);
		$("#applyRefund").val('');
		$("#consultRefund").val('');
		$("#radioBox7").removeAttr("checked");
	}
};


agreeRefund.saveAgreeRefund = function (orderInfoId,statue){
	var preferentialWay = $("input[name='buy-r']:checked").val();
	var refuseReason = $("#refuseReason").val();
	var refundType = $("#refundType").val();
	var id = $("#id").val();
	var status = 1;
	var agreeRefundVo = {};
	if(preferentialWay==1){
		agreeRefundVo.refundMoney = accMul($("#applyRefund").val(),100);
		agreeRefundVo.operateWay = 1;
	}else if(preferentialWay==2){
	    var consultRefund = accMul($("#consultRefund").val(),100);
        var refundMoney = accMul($("#refundMoney").val(),100);
        if(Number(consultRefund)>Number(refundMoney)){
            var d = dialog({
                title: '提示',
                content:'退款金额大于订单金额！'
            });
            d.showModal();
            setTimeout(function() {
                d.close().remove();
            }, 2000);
            return ;
        }
		agreeRefundVo.refundMoney = Number(consultRefund);
		agreeRefundVo.operateWay = 2;
	}else if(preferentialWay==3){
		if(!refuseReason){
			var d = dialog({
				title: '提示',
				content:'请填写拒绝退款原因！'
			});
			d.showModal();
			setTimeout(function() {
				d.close().remove();
			}, 2000);
			return ;
		}
		status = 2;
		agreeRefundVo.refuseReason = refuseReason;
	}else{
		var d = dialog({
			title: '提示',
			content:'请选择退款方式！'
		});
		d.showModal();
		setTimeout(function() {
			d.close().remove();
		}, 2000);
		return ;
	}
	agreeRefundVo.refundStatus = status;
	var jsonStr = JSON.stringify(agreeRefundVo);
	var url = CONTEXT_PATH+"/refund/refund_update/"+id;
	var tabindex=0;
	var pageparam = [{
		url: CONTEXT_PATH+"/order/order_info_list/1", //初始化订单数据,
		pageSize: 10,
		curPage: 0,
		countObj: "count",
		pageToolbarObj: "pageTypeToolbar",
		tableBodyObj: "orderItemList",
		template:"/order/pc/orderItemList"
	}];
	var res = $.ajaxJsonPut(url,jsonStr, {
		"done": function (res) {
            if(res.data.code===-1){
                var dm = new dialogMessage({
                    type: 1,
                    fixed: true,
                    msg: res.data.cause,
                    isAutoDisplay: true,
                    time: 1500
                });
                dm.render();
            }
            if(res.data.code===0){
                order.list.refreshPage(pageparam[0]);
                var dm = new dialogMessage({
                    type: 1,
                    fixed: true,
                    msg: '操作成功',
                    isAutoDisplay: true,
                    time: 1500
                });
                dm.render();
            }
			if(res.data.code===1){
				order.list.refreshPage(pageparam[0]);
				var dm = new dialogMessage({
					type: 1,
					fixed: true,
					msg: '操作成功',
					isAutoDisplay: true,
					time: 1500
				});
				dm.render();
			}
		}
	});
	dialog.get('orderManager_agreeRefund').close().remove();
}


agreeRefund.closeRefund = function(){
	dialog.get('orderManager_agreeRefund').close().remove();
}

function clearNoNum(obj){
	obj.value = obj.value.replace(/[^\d.]/g,""); //清除"数字"和"."以外的字符
	obj.value = obj.value.replace(/^\./g,""); //验证第一个字符是数字而不是
	obj.value = obj.value.replace(/\.{2,}/g,"."); //只保留第一个. 清除多余的
	obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
	obj.value = obj.value.replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3'); //只能输入两个小数
}

//乘法
function accMul(arg1,arg2)
{
    var m=0,s1=arg1.toString(),s2=arg2.toString();
    try{m+=s1.split(".")[1].length}catch(e){}
    try{m+=s2.split(".")[1].length}catch(e){}
    return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m)
}