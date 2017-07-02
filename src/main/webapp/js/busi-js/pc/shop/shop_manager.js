CommonUtils.regNamespace("shop");

/*菜单访问通用js*/
shop.commonClick=function(url){
    shop.ajaxGET(url);
};
/*点击菜单加载前  清理掉原有页面上 一些 无关的 元素 和事件*/
shop.cleanData=function(){
    // 清空 原有页面 处理业务时生成的 临时弹窗
    $("body .dialoginfo").remove();
    //清空中间区域的内容
    $("#shop_content").empty();
}
shop.ajaxGET=function(url){
    $.ajaxHtmlGet(url,null,{
        done:function(res) {
            shop.cleanData();
            // $("#shop_content").empty();
            $("#shop_content").append(res.data);
            if(url.indexOf("order/order_info")>0 || url.indexOf("order/order_delivery")>0){
            	$("#tableBody .jm-table-control").rowspan([3,4,5,6]);
            }
        }
    });
	/*$.ajax({
        type : 'GET',  
        contentType : 'application/html',  
        url:url, 
        processData : false,  
        dataType : 'html',  
        data : null,  
        success : function(data) {  
        	$("#shop_content").empty();
        	$("#shop_content").append(data);
        },  
        error : function() {  
            alert('Err...');  
        }  
    });  */
};

