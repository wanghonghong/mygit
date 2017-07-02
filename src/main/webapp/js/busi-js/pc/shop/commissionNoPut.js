CommonUtils.regNamespace("commission");
commission.dio;
commission.sendDig = function (commissionId) {
	var elem = $('#dialoginfo'+commissionId);
	commission.dio = dialog({
		title : "佣金发放",
		content : elem,
	}).show();
}
commission.closeDig=function(commissionId){
	commission.dio.close().remove();
	$('#dialoginfo'+commissionId).remove();
}
//手动发放红包
commission.sendCommission = function(commissionId){
	var url = CONTEXT_PATH+"/commission/commission_send/"+commissionId;
	$.ajaxJsonGet(url,null,{
		"done":function (res) {
			if(res.code==0){
				var data=res.data;
				if(data.code==0){
					var dm = new dialogMessage({
						type:1,
						fixed:true,
						msg:data.msg,
						isAutoDisplay:true,
						time:1500
					});dm.render();
				}else if(data.code==1){
					var dm = new dialogMessage({
						type:2,
						fixed:true,
						msg:data.msg,
						isAutoDisplay:true,
						time:1500
					});dm.render();
				}else if(data.code==2){
					var dm = new dialogMessage({
						type:2,
						fixed:true,
						msg:data.msg,
						isAutoDisplay:true,
						time:1500
					});dm.render();
				}else if(data.code==4){
					var dm = new dialogMessage({
						type:2,
						fixed:true,
						msg:data.msg,
						isAutoDisplay:true,
						time:1500
					});dm.render();
				}else if(data.code==5){
					var dm = new dialogMessage({
						type:2,
						fixed:true,
						msg:data.msg,
						isAutoDisplay:true,
						time:1500
					});dm.render();
				}else if(data.code==-1){
					var dm = new dialogMessage({
						type:2,
						fixed:true,
						msg:data.msg,
						isAutoDisplay:true,
						time:1500
					});dm.render();
				}else{
					var dm = new dialogMessage({
						type:2,
						fixed:true,
						msg:"发放失败，原因其他",
						isAutoDisplay:true,
						time:1500
					});dm.render();
				}
				// commission.dio.close().remove();
				commission.closeDig(commissionId);
				//$('#dialoginfo'+commissionId).close();
				//$(".two-level-memu[resourceid='26']").click();

				$("#searchBtn").click();
			}else{
				alert(res.msg)
				//$('#dialoginfo'+commissionId).close();
				loadHtmlData(pageparam[0],true);
			}
		}
	});

}

/**操作请求(查找)*/
commission.ajaxPost=function(jsonStr,url){
	$.ajaxHtml(url,jsonStr,{
		"done":function(res){
			$("#commission_list").empty();
        	$("#commission_list").append(res.data);
		}
	});
};

var pageparam = [{
	url: CONTEXT_PATH+"/commission/commission_list/query/3",//佣金清单
	pageSize: 10,
	curPage: 0,
	pageload:false,
	name: null,
	countObj: "count",
	pageToolbarObj: "pageToolbar1",
	tableBodyObj: "commission_list"
}];

function loadPageData(){
	loadHtmlData(pageparam[0],false);
	$("#"+pageparam[tabindex].pageToolbarObj).children().remove();
	initPagination(pageparam[tabindex]);
}

function reloadPage(){

}
$("#searchBtn").on("click",function(){
	pageparam[0].curPage=0;
	loadHtmlData(pageparam[0],true);

//		  reloadData();
});
//		//此demo通过Ajax加载分页元素
var initPagination = function(param ) {
	if( $("#"+param.pageToolbarObj).children().length>0){
		$("#"+param.pageToolbarObj).children().remove();
	}
	var count=$("#"+param.countObj).val();
	$("<div>",{
		"class":"pagination"
	}).pagination(count, {
		num_edge_entries: 1, //边缘页数
		num_display_entries: 3, //主体页数
		current_page:param.curpage,
		prev_text:"<li class='iconfont icon-left  '></li>",
		next_text:"<li class='iconfont icon-right  '></li>",
		callback: function(page_index, jq){
			param.curPage=page_index;
			loadHtmlData(param);
			return false;
		},
		items_per_page:10 //每页显示1项
	}).appendTo("#"+param.pageToolbarObj);
};
function loadHtmlData(param,sign){
	if(sign){
		$("#"+param.pageToolbarObj).children().remove();
		param.curPage=0;
	}

	var commissionManagerForQueryVo ={};
	commissionManagerForQueryVo.phoneNumber = $("#phoneNumber").val();
	commissionManagerForQueryVo.userName = $("#userName").val();
	commissionManagerForQueryVo.platForm = $("#platForm").val();
	commissionManagerForQueryVo.commissionId = $("#commissionId").val();
	commissionManagerForQueryVo.weChatName = $("#weChatName").val();
	commissionManagerForQueryVo.status = 3;
	commissionManagerForQueryVo.commissionPutDate = $("#commissionPutDate").val();
	commissionManagerForQueryVo.commissionPutDate1 = $("#commissionPutDate1").val();
	commissionManagerForQueryVo.pageSize=param.pageSize;
		commissionManagerForQueryVo.curPage=param.curPage;
	var jsonStr = JSON.stringify(commissionManagerForQueryVo);

	$.ajaxHtml(param.url,jsonStr,{
		"done":function(res){
			$("#"+param.tableBodyObj).empty();
			$("#"+param.tableBodyObj).append(res.data);
			if(sign){
				initPagination(param);
			}
		}
	});
}
function init(){
	initPagination(pageparam[0]);
}

init();

//图片载入失败显示默认图片
commission.nofind=function(){
	var img=event.srcElement;
	//javascript:this.src=${basePath}/css/pc/img/no_picture.png
	//F:\code\msa\src\main\webapp\img\pc\logo2.png
	img.src=CONTEXT_PATH+"/img/pc/logo.png";
	//控制不要一直跳动
	img.onerror=null;
};