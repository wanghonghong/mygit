<#list orderlist as item>
	    <table  class="jm-table-control"  >
		     <tbody>
		     	<tr class="bg-gray">
		     		<td colspan="7">
		     			
		     		<ul  >
						<li class="floatleft padding-left-s">订单编号：${item.orderNum!'' }   </li>
					<li class="service floatright padding-right-s"></li>
				</ul>	
	     		</td>
	     	</tr>
			<tr><td colspan="3"><p class="text-left padding-s">	<b>买家留言：</b>${item.remark!'' }</p></td><td colspan="4"><p class="text-left padding-s">	<b>卖家备注：</b>${item.sellerNote!''}</p></td></tr>
	     	  	<#list item.orderDetails as orderDetails>
	     	  	<tr>
	     		<td style="width:10%;">
	     		  <div class="table">
					<div class="table-cell vertical-middle">
					  <a href="#" class="thumbnail">
						  <#if orderDetails.pic??>
						  	  <img src="${orderDetails.pic!''}" alt="" class="avt">
						  <#else>
							 <img src="${THIRD_URL}/css/pc/img/no_picture.png" alt="暂无图片" class="avt">
						  </#if>
					  </a>
					</div>
				</div>
	     		</td>
				<td style="width:15%;">
	     		  <div class="table">
					<div class="table-cell vertical-middle">
					    <span class="font-color-black" >${orderDetails.name!'' }  </span><br>
					    <span >${orderDetails.specValueTwo!''  } ${orderDetails.specValueOne!''  } ${orderDetails.specValueThree!''  }</span>
					</div>
				</div>
	     		</td>
	     		<td class="font-color-black" style="width:10%">
	     			<div class="table">
		     			<div class="table-cell vertical-middle">
							<span class="font-color-black" >${orderDetails.count!'' }</span><br>
						</div> 
					</div>
	     		</td>
	     		<td class="font-color-black" style="width:10%;" >
				    ${item.createDate!'' }
	     		</td>
				<td  class="font-color-black" style="width:10%;" >${item.consigneeName!'' } ${item.consigneePhone!'' }</td>
				<td  class="font-color-black" style="width:10%;" >${item.address!'' }</td>
	     		<td style="width:10%;" > 
				<#if item.status==1>
					<div class="btn  btn-lightgray btn-sm" onclick="order.immediateDelivery(${item.orderInfoId!'' });">立即发货</div>
				</#if>
				<#if item.status==2>
                    <span>待收货</span><br>
					<div class="btn  btn-lightgray btn-sm" onclick="order.queryLogistics(${item.orderInfoId!'' });">查看物流</div>
				</#if>
				<#if orderDetails.goodStatus==0>
					<div>退货中</div>
                    <div class="btn  btn-lightgray btn-sm" onclick="order.queryLogistics(${item.orderInfoId!'' });">查看物流</div><br>
					<div class="btn  btn-lightgray btn-sm" onclick="order.deliveryConfirm(${item.orderInfoId!'' });">确认收货</div>
				</#if>
				<#if orderDetails.goodStatus==1>
					<div>已入库</div>
				</#if>
	     		</td>
	     	</tr>
	     	</#list>
			</table>
		</#list>
		<input type="hidden" id="count" name="count" value="${counts}" />
		<div align="right">一共有${counts}条数据</div>