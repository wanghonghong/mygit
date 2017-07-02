<div id="customerService" align="center" style="display: none">
	<br><br>
	<form id="saveCustomerForm" action="" method="post">
		<div><textarea id="sellerNote" name="sellerNote" rows="6" cols="75" onpropertychange="if(this.scrollHeight>80) this.style.posHeight=this.scrollHeight+5">${orderInfo.sellerNote!'' }</textarea></div>
		<div style="margin-top: 52px;">
			<div style="margin-left:25px" class="btn goods-add-btn-next btn-darkorange btn-radius-3 btn-lg" onclick="order.saveCustomer(${orderInfo.orderInfoId!'' });">保存</div>
		</div>
	</form>
</div>