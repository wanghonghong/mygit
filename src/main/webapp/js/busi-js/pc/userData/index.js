CommonUtils.regNamespace("index");
index = (function(){
    $(function(){
        $("#u-logo").click(function(){
            $("#othermemu").toggleClass("z-change");
        })
    })
	//初始化数据
	var _init = function(){
        _clickLeftMenu();
        $("#leftMenuUl").find('.memu-panel').eq(0).trigger('click');
	};

	var _clickLeftMenu = function(){

            $('#leftMenuUl').find('.memu-panel').click(function(){
                 var element = $(this);
                $('#leftMenuUl').find('.one-level-memu').removeClass("active");
                 element.find('.one-level-memu').addClass("active");
                 var tpl_name = element.attr('data-tpl-name');
                 var url = element.data('url');
                 if(tpl_name){
                     if(url){
                         url=CONTEXT_PATH+url;
                         $.ajaxJsonGet(url,null,{
                             done:function (res) {
                                 if(res.code===0){
                                     if($.isArray(res.data)){
                                         var data = {
                                             items:res.data
                                         }
                                     }else{
                                         var data = res.data;
                                     }
                                     jumi.template(tpl_name,data,function(html){
                                         $("#erp_content").html(html);
                                     })
                                 }
                             }
                         })
                     }else{
                         jumi.template(tpl_name,function(html){
                             $("#erp_content").html(html);
                         })
                     }
                 }else{
                         var dm = new dialogMessage({
                             type:2,
                             fixed:true,
                             msg:'功能还在开发中!',
                             isAutoDisplay:false,
                         });
                         dm.render();
                     }
            })
                // jumi.template(tplname,function(html){
                //     $("#erp_content").html(html);
                // })

	}
	return {
		init :_init, //初始化
		clickLeftMenu :_clickLeftMenu, //_clickLeftMenu
	};
})();

$(function() {
	index.init();
});
