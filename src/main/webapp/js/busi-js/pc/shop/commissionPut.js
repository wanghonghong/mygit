CommonUtils.regNamespace("commission");

//佣金详情
commission.detail = function(orderInfoId,commissionId){
    url=CONTEXT_PATH+'/commission/commission_list/queryDetail/'+orderInfoId;
    commission.ajaxGetDetail(url,commissionId);
};
//填充详情模板
commission.ajaxGetDetail=function(url,commissionId){
    $.ajaxHtmlGet(url,null,{
        done:function(res) {
            $("#dialoginfo"+commissionId).empty();
            $("#dialoginfo"+commissionId).append(res.data);
            commission.showRoleWindow(commissionId);
        }
    });
};
//详情弹窗
commission.showRoleWindow = function(commissionId) {
    var elem = $('#dialoginfo'+commissionId);
    dialog({
        title : "订单详情",
        content : elem,
        width : 1000
    }).show();
    $("#jmtable").rowspan([1,2,3,4,5]);
};

var pageparam = [{
    url: CONTEXT_PATH+"/commission/commission_list/query/0",//佣金清单
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
    commissionManagerForQueryVo.status = 0;
    commissionManagerForQueryVo.commissionPutDate = $("#commissionPutDate").val();
    commissionManagerForQueryVo.commissionPutDate1 = $("#commissionPutDate1").val();
    commissionManagerForQueryVo.pageSize=param.pageSize;
    commissionManagerForQueryVo.curPage=param.curPage;
    var jsonStr = JSON.stringify(commissionManagerForQueryVo);

    $.ajaxHtml(param.url,jsonStr,{
        "done":function(res){
            $("body .dialoginfo").remove();
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

commission.nofind=function(){
    var img=event.srcElement;
    //javascript:this.src=${basePath}/css/pc/img/no_picture.png
    //F:\code\msa\src\main\webapp\img\pc\logo2.png
    img.src=CONTEXT_PATH+"/img/pc/logo.png";
    //控制不要一直跳动
    img.onerror=null;
};