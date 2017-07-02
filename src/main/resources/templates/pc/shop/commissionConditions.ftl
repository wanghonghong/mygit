<div style="height: 120px;">
	<div class="col-xs-12 zhyh-pl">
		<div class="form-group col-xs-3 zhyh-pl">
			<label>手机</label> <input type="text" class="form-control distance"  placeholder="" id="phoneNumber"/>
		</div>
		<div class="form-group col-xs-3 zhyh-pl">
			<label>姓名</label> <input type="text" class="form-control distance"  placeholder="" id="userName"/>
		</div>
		<div class="form-group col-xs-3 zhyh-pl">
			<label>平台选择</label> <select class="form-control" name="platForm" id="platForm">
				<option selected="selected" value="">全部</option>
				<option value="0">微信</option>
				<option value="1">微博</option>
			</select>
		</div>
		<div class="form-group col-xs-3 zhyh-pl">
			<label style="min-width: 100px;">佣金支付号</label> <input type="text" class="form-control distance" placeholder="" id="commissionId"/>
		</div>
	</div>
	<div class="col-xs-12 zhyh-pl">
		<div class="form-group col-xs-3 zhyh-pl">
			<label>昵称</label> <input type="text" class="form-control distance"  placeholder="" id="weChatName"/>
		</div>
		<div class="form-group col-xs-3 zhyh-pl">
			<label>佣金</label> <select name="status" id="status" selectValue="4">
				<option value="4">全部</option>
				<option value="0">成功</option>
				<option value="1">失败</option>
				<option value="2">取消</option>
				<option value="3">未发放</option>
			</select>
		
		
		
		
			<!-- <select class="form-control" id="status">
				<option value="4" selected="selected">全部</option>
				<option value="0">成功</option>
				<option value="1">失败</option>
				<option value="2">取消</option>
				<option value="3">未发放</option>
			</select> -->
		</div>
		<div class="col-xs-6 zhyh-pl">
		  	<label  class="form-label floatleft col-xs-2">订单日期</label>
			<div class="form-group    col-xs-4" >
					<input type="text" class="laydate-icon form-control" id="commissionPutDate" name="commissionPutDate" placeholder="开始时间" onclick="laydate({istime: true, format: 'YYYY-MM-DD hh:mm:ss'})"/>
			</div>
			 <span class="form-label floatleft col-xs-1">_</span>
				<div class="form-group   col-xs-4" >
					<input type="text" class="laydate-icon form-control" id="commissionPutDate1" name="commissionPutDate1" placeholder="结束时间" onclick="laydate({istime: true, format: 'YYYY-MM-DD hh:mm:ss'})" />
				</div>
					</div>
		</div>
	</div>
<div style="margin: 0 0 30px;">
	<div class="btn btn-lightorange btn-radius-5 btn-lg col-xs-offset-5"
		id="searchBtn">查询</div>
	<div style="margin-left:8px" class="btn btn-blue btn-radius-5 btn-lg">导出</div>
</div>