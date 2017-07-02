CommonUtils.regNamespace("index");
index = (function(){
    $(function(){
        $("#u-logo").click(function(){
            $("#othermemu").toggleClass("z-change");
        })
    })
	//初始化数据
	var _init = function(){

		//头部菜单单击事件
		$("#topmenuUl li").click(function () {
			var parentId=$(this).attr("data-id");
			$("#topmenuUl li").removeClass("active");
			$(this).addClass("active");
			var url= CONTEXT_PATH +"/left_menu/"+parentId;
			$.ajaxHtmlGet(url, null, {
				done: function (res) {
					$("#leftmenu").empty();
					$("#leftmenu").append(res.data);
				}
			});
		});
        _clickLeftMenu();
        $("#leftMenuUl").find('.one-level-memu').eq(0).trigger('click');
	};

	var _clickLeftMenu = function(){

            $('#leftMenuUl').find('.one-level-memu').click(function(){
                 var element = $(this);
                $('#leftMenuUl').find('.one-level-memu').removeClass("z-sel");
                $('#leftMenuUl').find('.two-level-memu').removeClass("z-sel");
                 element.addClass("z-sel");
                 element.next().addClass("z-sel");
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

            $('#leftMenuUl').find('.two-level-memu').click(function(){
                var element = $(this);
                $('#leftMenuUl').find('.one-level-memu').removeClass("z-sel");
                $('#leftMenuUl').find('.two-level-memu').removeClass("z-sel");
                element.addClass("z-sel");
                element.siblings(".one-level-memu").addClass("z-sel");
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

	}
	return {
		init :_init, //初始化
		clickLeftMenu :_clickLeftMenu, //_clickLeftMenu
	};
})();

$(function() {
	index.init();
});
