</script>
<div class="jm-table" >
	 <table  id="jmtable" class="jm-table-control">
   		<thead>
	   		<tr >
	   			<th style="width:10%;">主图</th>
	   			<th style="width:25%;">名称-颜色-尺寸</th>
	   			<th style="width:15%;">单价/数量</th>
	   			<th style="width:10%;" >下单时间</th>
	   			<th style="width:10%;" >订单状态</th>
	   			<th style="width:10%;" >实付金额</th>
	   		</tr>
   		</thead>
    </table>
	<div id="tableBody"  class="table-body padding-top-s">
		<#include "orderItemList.ftl">
	</div>
	<div id="pagina" class="pagination" align="right"></div>
	<div id="hiddenresult" style="display:none;">
	<div id="pageToolbar1" class="page-toolbar"></div>
</div>

<!-- 订单管理 -->
<script type="text/javascript" charset="utf-8">
	$(function(){
		//初始化分页
		order.initPagination();
	});
</script>

