<#list orderlist as item>
	    <table  class="jm-table-control"  >
		     <tbody>
		     	<tr class="bg-gray">
		     		<td colspan="7">
			     		<ul  >
							<li class="floatleft padding-left-s">订单编号：${item.orderNum!'' }&nbsp&nbsp&nbsp支付流水号：${item.payOrderNum!'' }   </li>
							<li class="service floatright padding-right-s">${item.weChatName!'' }</li>
						</ul>	
		     		</td>
		     	</tr>
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
		     		<td style="width:25%;">
			     		 <div class="table">
							<div class="table-cell vertical-middle">
							    <span class="font-color-black" >${orderDetails.name!''  } </span><br>
							    <span >${orderDetails.specValueTwo! } ${orderDetails.specValueOne! } ${orderDetails.specValueThree! }</span>
							</div>
						 </div>
		     		</td>
		     		<td style="width:15%;">
		     			<div class="table">
			     			<div class="table-cell vertical-middle">
								<span class="font-color-black" >￥${(orderDetails.price/100!'')?string("0.00")}</span><br>
							    <span>（${orderDetails.count!''  }件）</span>
							</div> 
						</div>
		     		</td>
		     		<#--<td  style="width:10%;"  >
						<#if item.refundMoney?? >
                            &nbsp;&nbsp;<font>退款金额：</font>${item.refundMoney!'' }<br>
						</#if>
						<#if item.refundReason?? >
                            &nbsp;<font>退款原因：</font>${item.refundReason!'' }
						</#if>
					</td>-->
		     		<td class="font-color-black" style="width:10%;"  >
					    ${item.createDate!'' } 
		     		</td>
		     		<td style="width:10%;" > 
					<#if item.status==0>
						<span class="font-color-black" >待付款</span><br>
						<div  class="btn  btn-lightgray btn-sm" onclick="order.queryOrderDetail(${item.orderInfoId!'' });">订单详情</div><br>
						<div  class="btn  btn-lightgray btn-sm" onclick="order.cancel(${item.orderInfoId!'' });">取消订单</div><br>
					<#elseif item.status==1>
						<span class="font-color-black" >待发货</span><br>
						<div  class="btn  btn-lightgray btn-sm"  onclick="order.queryOrderDetail(${item.orderInfoId!'' });" >订单详情</div><br>
					<#elseif item.status==2>
						<span class="font-color-black" >已发货</span><br>
						<div  class="btn  btn-lightgray btn-sm" onclick="order.queryOrderDetail(${item.orderInfoId!'' });">订单详情</div><br>
					<#elseif item.status==3>
						<span class="font-color-black" >已收货</span><br>
						<div  class="btn  btn-lightgray btn-sm" onclick="order.queryOrderDetail(${item.orderInfoId!'' });">订单详情</div><br>
					<#elseif item.status==4>
                        <span class="font-color-black" >订单关闭</span><br>
                        <div  class="btn  btn-lightgray btn-sm" onclick="order.queryOrderDetail(${item.orderInfoId!'' });">订单详情</div><br>
					<#else>
					</#if>
					<#if orderDetails.refundStatus==0>
						<span class="font-color-black" >申请退款</span><br>
						<div  class="btn  btn-lightgray btn-sm" onclick="order.queryOrderDetail(${item.orderInfoId!'' });">订单详情</div><br>
					<#elseif orderDetails.refundStatus==1>
						<span class="font-color-black" >已退款</span><br>
						<div  class="btn  btn-lightgray btn-sm" onclick="order.queryOrderDetail(${item.orderInfoId!'' });">订单详情</div><br>
					<#elseif orderDetails.refundStatus==2>
						<span class="font-color-black" >拒绝退款</span><br>
						<div  class="btn  btn-lightgray btn-sm" onclick="order.queryOrderDetail(${item.orderInfoId!'' });">订单详情</div><br>
					<#elseif orderDetails.refundStatus==3>
						<span class="font-color-black" >退款中</span><br>
						<div  class="btn  btn-lightgray btn-sm" onclick="order.queryOrderDetail(${item.orderInfoId!'' });">订单详情</div><br>
					<#else>
					</#if>
		     		</td>
		     			<td style="width:10%;"  >
		     				<span class="font-color-black" >${((item.totalPrice+item.sendFee!0)/100)?string("0.00")}</span><br>
						    <span >（含运费：${((item.sendFee!0)/100)?string("0.00")}）</span><br>
							<div  class="btn  btn-lightgray btn-sm" onclick="order.customerService(${item.orderInfoId!''});">卖家备注</div><br>
							<#if item.status==0>
								<div  class="btn  btn-lightgray btn-sm" onclick="order.updatePrice(${item.orderInfoId!''});">修改价格</div><br>
								<div  class="btn  btn-lightgray btn-sm" onclick="order.breakFreight(${item.orderInfoId!''});">减免运费</div><br>
							</#if>
							<#if item.status==2>
								<div  class="btn  btn-lightgray btn-sm" onclick="order.confirm(${item.orderInfoId!'' });">确认收货</div><br>
							</#if>
							<#if item.status==5>
								<div  class="btn  btn-lightgray btn-sm" onclick="order.agreeRefund(${item.orderInfoId!'' },${statue});">退款操作</div><br>
							</#if>
				     	</td>
		     	</tr>
		     	</#list>
		     	<tr><td colspan="7"><p class="text-left padding-s">	<b>买家留言：</b>${item.remark!'' }</p></td></tr>
		    </table>
		</#list>
<input type="hidden" id="count" name="count" value="${counts}" />
<div align="right">一共有${counts}条数据</div>