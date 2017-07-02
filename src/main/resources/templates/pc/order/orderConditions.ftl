 	<div class="manage">
       	<div class="pd10 row">
		 	<div class="col-xs-12">
				<div class="form-group col-xs-4 padding-left-zero"    >
					<label style="min-width: 80px;">收货人手机</label>
					<input type="text" id="consigneePhone" name="consigneePhone" class="form-control" />
			    </div>
				<div class="form-group col-xs-4 padding-left-zero"   >
					<label style="min-width: 80px;">收货人姓名</label>
					<input type="text" id="consigneeName" name="consigneeName" class="form-control"  />
				</div>
				<div class="form-group col-xs-4 padding-left-zero"   >
					<label style="min-width: 80px;">微信昵称</label>
					<input type="text" id="weChatName" name="weChatName" class="form-control"  />
				</div>
				<div class="form-group col-xs-4 padding-left-zero"   >
					<label style="min-width: 80px;">订单号</label>
					<input type="text" id="orderNum" name="orderNum" class="form-control"  />
				</div>
				<div class="form-group col-xs-4 padding-left-zero"   >
					<label style="min-width: 80px;">支付流水号</label>
					<input type="text" id="payOrderNum" name="payOrderNum" class="form-control"  />
				</div>
				<div class="form-group col-xs-3">
					<label class="distance">平台选择</label>
					<select name="test" id="select1" selectValue="1">
						<option value="1">请选择平台</option>
						<option value="2">微信平台</option>
						<option value="3">微博平台</option>
					</select>
				</div>
				<div class="form-group col-xs-5 date">
					<label style="min-width: 80px;">订单日期</label>
					<div class="form-select-control" >
						<input type="text" class="laydate-icon" style="min-width: 268px;" id="orderBeginDate" name="orderBeginDate" placeholder="开始时间" onclick="laydate({istime: true, format: 'YYYY-MM-DD hh:mm:ss'})" />
						  <!-- <i class="iconfont icon-belowtriangle "></i> -->
					</div>
					<span>_</span>
					<div class="form-select-control">
						<input type="text"  class="laydate-icon" style="min-width: 268px;" id="orderEndDate" name="orderEndDate" placeholder="结束时间" onclick="laydate({istime: true, format: 'YYYY-MM-DD hh:mm:ss'})" />
						  <!-- <i class="iconfont icon-belowtriangle "></i> -->
					</div>
				 </div>
	 		</div>
 		</div>
	</div>
	 <input type="hidden" id="status" name="status" value="${statue!''}" />
	 <input type="hidden" id="type" name="type" value="${type }" />
	 <input type="hidden" id="goodStatus" name="goodStatus" value="${goodStatus}"/>
	 <div style="clear:both;padding-top:20px;margin-bottom:20px">
		<div  class="btn btn-darkorange btn-radius-5 col-xs-offset-4 btn-lg" onclick="order.queryOrder();"> 查询 </div>
		<div  class="btn btn-blue btn-radius-5 btn-lg"> 导出 </div>
	 </div>

	<script>
		$(function(){
			 var select1=new selectControl("#select1",{
	         	  queryItem:false
		 });
		 select1.render();
		});
	</script>
