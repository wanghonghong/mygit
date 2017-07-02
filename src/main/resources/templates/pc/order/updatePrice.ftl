<div id="updatePrice" style="display: none">
	<br>
	<form id="updatePriceForm" action="" method="post"> 
		<input type="hidden" id="totalPrice" name="totalPrice" value="${orderInfo.totalPrice!'' }" />
		<input type="hidden" id="sendFee" name="sendFee" value="${orderInfo.sendFee!'' }"/>
		<div>
			<label><input id="preferentialWay" name="preferentialWay" type="radio" value="1" onchange="updatePrice.changePW();" checked="checked" />商品减额 </label>&nbsp&nbsp&nbsp&nbsp&nbsp
			减收<input class="text" style='border:1px solid #000;height:24px' type="text" id="discountAmount1" name="discountAmount1" onkeyup="clearNoNum(this)" value="" />元&nbsp&nbsp&nbsp&nbsp&nbsp
			实收总价<input class="x" style='border:1px,solid' type="text" id="totalPrice1" name="totalPrice1" value="" readonly="readonly" />元
		</div><br>
		<div>
			<label><input id="preferentialWay" name="preferentialWay" type="radio" value="2" onchange="updatePrice.changePW();" />商品折扣 </label>&nbsp&nbsp&nbsp&nbsp&nbsp
			折扣<input class="text" style='border:1px solid #000;height:24px' type="text" id="discountAmount2" name="discountAmount2" value="" onkeyup="clearNoNum(this)" readonly="readonly"/>折&nbsp&nbsp&nbsp&nbsp&nbsp
			实收总价<input class="x" style='border:1px,solid' type="text" id="totalPrice2" name="totalPrice2" value="" readonly="readonly"/>元
		</div><br>
		<div style="margin-left:-4px;text-align:left">
			&nbsp<label><input class="text" id="preferentialWay" name="preferentialWay" type="radio" value="3" onchange="updatePrice.changePW();" />减免运费</label>&nbsp&nbsp&nbsp
			实收总价<input class="x" type="text" id="totalPrice3" name="totalPrice3" value="" onchange="updatePrice.changeAD();" readonly="readonly"/>元
		</div>
		<div style="margin-top: 102px;">
			<div style="margin-left:210px" class="btn goods-add-btn-next btn-darkorange btn-radius-5 btn-lg"  onclick="order.savePrice(${orderInfo.orderInfoId!'' })">确认</div>
		</div>
	</form>
</div>

 <style>
 .x{
   border-left:none;
   border-right:none;
   border-top:none;
   border-bottom:1px solid #0F2543;
   text-align:center;
 }
 input.text{text-align:center;} 
 </style>

 <!-- 修改价格-->
 <script src="${basePath}/js/busi-js/pc/order/updatePrice.js" type="text/javascript" charset="utf-8"></script>
