
<#--<div id="immediateDelivery" align="center" style="display: none">
	<br><br>
	<form id="immediateDelivery" action="" method="post">
		买家留言<div><textarea name="content" rows="6" cols="80"  disabled="disabled" onpropertychange="if(this.scrollHeight>80) this.style.posHeight=this.scrollHeight+5">${orderInfo.remark!'' }</textarea></div>
		卖家备注<div><textarea name="content" rows="6" cols="80"  disabled="disabled" onpropertychange="if(this.scrollHeight>80) this.style.posHeight=this.scrollHeight+5">${orderInfo.sellerNote!'' }</textarea></div><br>
		<div align="center" >物流公司<input style='border:1px solid #000;height:24px' type="text" id="transCompany" name="transCompany" value="" /></div><br>
		<div align="center" >物流单号<input style='border:1px solid #000;height:24px' type="text" id="transNumber" name="transNumber" value="" /></div><br>
		<div align="center" >发货备注<input style='border:1px solid #000;height:24px' type="text" id="deliveryNote" name="deliveryNote" value="" /></div>
		<div style="margin-top: 52px;">
			<div style="margin-left:25px" class="btn goods-add-btn-next btn-darkorange btn-radius-3 btn-lg" onclick="order.saveOrderDelivery(${orderInfo.orderInfoId!'' })">提交发货</div>
		</div>
	</form>
</div>-->
<div id="immediateDelivery" class="dialoginfo">
    <div class="m-info-box">
			<span class="info-cott">
				<div class="g-table">
					<span class="g-table-cell">
     	 				<img class="u-thumbnail-circ" src="${THIRD_URL}/css/pc/img/icon_buy.png" />
     	 			</span>
     	 			<span class="g-table-cell">买家留言：<font color="#6A6A6A">${orderInfo.remark!'' }</font></span>
				</div>
				<div>
					<span class="g-table-cell">
     	 				<img class="u-thumbnail-circ" src="${THIRD_URL}/css/pc/img/icon_sale.png" />
     	 			</span>
     	 			<span class="g-table-cell">卖家留言：<font color="#6A6A6A">${orderInfo.sellerNote!'' }</font></span>
				</div>
			</span>
        <div class="logistics">
            <h3 class="">物流信息</h3>

            <label class="u-lb-sm">物流公司</label>
            <div class="u-txt">
                <input class="ipt-txt" type="text" id="transCompany" name="transCompany" value="" />
            </div>
            <label class="u-lb-sm">物流单号</label>
            <div class="u-txt">
                <input class="ipt-txt" type="text" id="transNumber" name="transNumber" value="" />
            </div>
            <label class="u-lb-sm">物流备注</label>
            <div class="u-txt">
                <textarea name="shared" class="ipt-txt txtarea" id="deliveryNote" name="deliveryNote" placeholder=""></textarea>
            </div>

        </div>
    </div>
    <div class="u-btn-box1 f-mt-m">
        <input type="button" class="u-btn-mddkgry f-mr-xs" value="取消" onclick="order.closeRefund()" />
        <input type="button" class="u-btn-mddkorg" value="确认发货" onclick="order.saveOrderDelivery(${orderInfo.orderInfoId!'' })" />
    </div>
</div>