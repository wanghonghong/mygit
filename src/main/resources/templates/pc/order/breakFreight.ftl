<div id="breakFreight" align="center" style="display: none">
<form id="breakFreigthtForm" action="" method="post">
	<div>减免运费后的实收总价</div><br>
	<div><input class="text" style='border:1px solid #000;height:24px' type="text" id="totalPrices" name="totalPrices" readonly="readonly" value="${(orderInfo.totalPrice/100)?string("0.00")}"/>元</div>
	<div style="margin-top: 102px;">
		<div style="margin-left:25px" class="btn goods-add-btn-next btn-darkorange btn-radius-5 btn-lg"  onclick="order.saveBreakFreight(${orderInfo.orderInfoId!'' })">确认</div>
	</div>
</form>
</div>


<style>
 input.text{text-align:center;} 
 </style>