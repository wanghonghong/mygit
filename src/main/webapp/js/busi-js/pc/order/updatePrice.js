/**
 * 命名空间
 */
CommonUtils.regNamespace("updatePrice","list");

updatePrice.list = (function(){

	var _changeAD = function(){
		var totalPrice = $("#u_totalPrice").val();
		var sendFee = $("#u_sendFee").val();
		var discount = $("#u_discount").val();
		var benefits = $("#u_benefits").val();
		var coupon = $("#u_coupon").val();
		var discountAmount1 = $("#u_discountAmount1").val();
		var totalMoney = Number(totalPrice) + Number(sendFee);
		var totalPrice1 = "";
		if(Number(discount)===0){
			//totalPrice1 = Number(totalMoney)-Number(benefits)-Number(coupon)-Number(discountAmount1);
			totalPrice1 = accSub(accSub(accSub(Number(totalMoney),Number(benefits)),Number(coupon)),Number(discountAmount1));
		}else{
			//totalPrice1 = Number(totalPrice)*Number(discount)/10+Number(sendFee)-Number(benefits)-Number(coupon)-Number(discountAmount1);
			totalPrice1 = accSub(accSub(accSub(accAdd(accDiv(accMul(Number(totalPrice),Number(discount)),10),Number(sendFee)),Number(benefits)),Number(coupon)),Number(discountAmount1));
		}
		if(Number(totalPrice1)>0){
			$("#realPrice").val(totalPrice1);
			$("#prompt").html("");
			$("#u_totalPrice1").html(totalPrice1);
		}else{
			$("#realPrice").val(0.01);
			$("#prompt").html("已经优惠的不能再优惠啦！！！");
			$("#u_totalPrice1").html("0.01");
		}
	};

	var _clearNoNum = function(obj) {
		obj.value = obj.value.replace(/[^\d.]/g, ""); //清除"数字"和"."以外的字符
		obj.value = obj.value.replace(/^\./g, ""); //验证第一个字符是数字而不是
		obj.value = obj.value.replace(/\.{2,}/g, "."); //只保留第一个. 清除多余的
		obj.value = obj.value.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".");
		obj.value = obj.value.replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3'); //只能输入两个小数
		_changeAD();
	};

	//加法
	function accAdd(num1,num2){
		var r1,r2,m;
		try{
			r1 = num1.toString().split('.')[1].length;
		}catch(e){
			r1 = 0;
		}
		try{
			r2=num2.toString().split(".")[1].length;
		}catch(e){
			r2=0;
		}
		m=Math.pow(10,Math.max(r1,r2));
		// return (num1*m+num2*m)/m;
		return Math.round(num1*m+num2*m)/m;
	};

	// 两个浮点数相减
	function accSub(num1,num2){
		var r1,r2,m;
		try{
			r1 = num1.toString().split('.')[1].length;
		}catch(e){
			r1 = 0;
		}
		try{
			r2=num2.toString().split(".")[1].length;
		}catch(e){
			r2=0;
		}
		m=Math.pow(10,Math.max(r1,r2));
		n=(r1>=r2)?r1:r2;
		return (Math.round(num1*m-num2*m)/m).toFixed(n);
	};

	//乘法
	function accMul(arg1,arg2)
	{
		var m=0,s1=arg1.toString(),s2=arg2.toString();
		try{m+=s1.split(".")[1].length}catch(e){}
		try{m+=s2.split(".")[1].length}catch(e){}
		return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m)
	}

	// 两数相除
	function accDiv(num1,num2){
		var t1,t2,r1,r2;
		try{
			t1 = num1.toString().split('.')[1].length;
		}catch(e){
			t1 = 0;
		}
		try{
			t2=num2.toString().split(".")[1].length;
		}catch(e){
			t2=0;
		}
		r1=Number(num1.toString().replace(".",""));
		r2=Number(num2.toString().replace(".",""));
		return (r1/r2)*Math.pow(10,t2-t1);
	};

	return {
		clearNoNum:_clearNoNum,
	}

})();

