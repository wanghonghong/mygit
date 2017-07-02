/***<===========================================================订单管理============================================================***/

/***
 * 订单查询
 */

CommonUtils.regNamespace("orderBookArea","list");
orderBookArea.list = (function(){
    var tabindex=0;
    var ajaxUrl = {
        url1:CONTEXT_PATH+"", //微信初始化订单数据
    };

    //初始化数据
    var _init = function(){

    };

    var _edit=function (pid) {
        var url=ajaxUrl.url1;
        $.ajaxJsonGet(url,null,{
            "done":function (res) {
                if(res.code===0){
                    var offerType=$("#offerTyle_"+pid).val();
                    var data={
                        offerRoles:res.data,
                        pid:pid,
                        offerType:offerType
                    }
                    var html = jumi.templateHtml("/tpl/product/area/product_area_dialog.html",data);
                    dialog({
                        content: html,
                        title: '地区展销及供应商',
                    }).width(700).height(680).showModal();
                }
            }
        });
    }

    return {
        init :_init, //初始化
    };
})();