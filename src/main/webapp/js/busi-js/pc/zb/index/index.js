CommonUtils.regNamespace("index");
index = (function(){
	//初始化数据
	var _init = function(){
		// goMenu();
		//头部菜单单击事件
		$("#topMenus li").click(function () {
			var parentId=$(this).attr("data-id");
			$("#topMenus li").removeClass("z-sel");
			$(this).addClass("z-sel");
			var url= CONTEXT_PATH +"/left_menu/"+parentId;
			$.ajaxHtmlGet(url, null, {
				done: function (res) {
					$("#leftmenu").empty();
					$("#leftmenu").append(res.data);
					$('.left-menus').eq(0).trigger('click');
				}
			});

		});
		$('.left-menus').eq(0).trigger('click');
		// if($('#topMenus li').hasClass('z-sel')){
		// 	var index = $(this).attr('data-id')-1;
		// 	$('.left-menus').eq(index).trigger('click');
		// }

	};

	var _clickLeftMenu = function(ths){
		$('.left-menus').removeClass('z-sel');
		if ($(ths).hasClass('one-level-memu')){
			$(ths).next().addClass('z-sel');
		}
		$(ths).addClass('z-sel').siblings(".one-level-memu").addClass("z-sel");
		refreshPage(ths);
		var tplname = $(ths).attr("data-tpl-name");
		var url = $(ths).attr("data-url");
		if (tplname) {
			if (url) {
				url = CONTEXT_PATH + url;
				$.ajaxJsonGet(url, null, {
					done: function (res) {
						if (res.code === 0) {
							if ($.isArray(res.data)) {
								var data = {
									items: res.data
								}
							} else {
								var data = res.data;
							}
							jumi.template(tplname, data, function (html) {
								$("#erp_content").html(html);
							})
						}
					}
				})
			} else {
				jumi.template(tplname, function (html) {
					$("#erp_content").html(html);

				})
			}
		} else {
			$("#erp_content").empty();
			var dm = new dialogMessage({
				type: 2,
				title: '操作提醒',
				fixed: true,
				msg: '功能还在开发中！',
				isAutoDisplay: true,
				time:1500,
			});
			dm.render();
		}
	}
	//页面刷新保存原先页面不变
	var refreshPage = function (ths) {
		var herf=window.location.href;
		var lastsign=herf.indexOf("#");
		if(lastsign>0){
			herf=herf.substr(0,lastsign);
		}
		var resourceId = $(ths).attr('data-id');
		var parentId=	$(ths).closest(".memu-panel").attr('data-id');
		window.location.href=herf+"#"+parentId+"#"+resourceId;
	}
	var goMenu=	function (){
		var hash=window.location.hash;
		if(hash){
			var hasharr=hash.split("#");
			if(hasharr.length>0){
				$("#topMenus li[data-id='" + hasharr[1]+ "']").addClass("z-sel").siblings().removeClass("z-sel");
				$(".m-memu  .one-level-memu").hide();
				$(".m-memu  .one-level-memu[data-id='" +  hasharr[1] + "']").show();
				if(hasharr[2]){
					$(".m-memu  .two-level-memu[resourceId='"+hasharr[2]+"'] ").click();
				}else {
					$(".m-memu  .one-level-memu:first ").click();
				}
			}else{
				$("#topMenus li").eq(1).click();
			}
		}else{
			$("#topMenus li").eq(1).click();
		}
	}
	return {
		init :_init, //初始化
		clickLeftMenu :_clickLeftMenu, //_clickLeftMenu
		refreshPage:refreshPage,
		// goMenu:goMenu,
	};
})();

$(function() {
	index.init();
});