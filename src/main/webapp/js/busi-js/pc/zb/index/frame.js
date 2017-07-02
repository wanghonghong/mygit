
CommonUtils.regNamespace("frame");
frame=(function () {
	var ajaxframeurl=CONTEXT_PATH+"/zb/role_resource/0";

	var frameMemuData =[];

	var _init=function(){
		getframeMemu().then(function() {
			initFrameMemu();
			bindFrameEvent();
			goMenu();
		}) ;
	}
	var goMenu=	function (){
		var hash=window.location.hash;
		if(hash){
			var hasharr=hash.split("#");
			if(hasharr.length>0){
				$("#topMenus     [resourceId='" + hasharr[1]+ "']").addClass("z-sel").siblings().removeClass("z-sel");
				$(".g-sd-left  .m-memu").hide();
				$(".g-sd-left  .m-memu[resourceId='" +  hasharr[1] + "']").show();
				if(hasharr[2]){
					$(".g-sd-left  .m-memu[resourceId='"+hasharr[1]+"'] .memu-panel [resourceId='"+hasharr[2]+"'] ").click();
				}else {
					$(".g-sd-left  .m-memu[resourceId='"+hasharr[1]+"'] .memu-panel  .one-level-memu:first ").click();
				}
			}else{
				$("#jumiheader .m-nav ul li:nth-child(1)").click();
			}
		}else{
			$("#jumiheader .m-nav ul li:nth-child(1)").click();
		}
	}
	var getframeMemu=function () {
		var dfdPlay = $.Deferred();
		var data = $.ajaxJsonGet(ajaxframeurl);
		if(data.code=="0"){
			frameMemuData = data.data;
			dfdPlay.resolve(); // 动画完成
		}
		return dfdPlay;
	}
	var initFrameMemu=function (){
		// var shopId=$("#shopId").val();
		if(frameMemuData!=null&frameMemuData.length>0){
			for (var i=0;i<frameMemuData.length;i++) {
				var item=frameMemuData[i];
				if(item.parentResourceId==0){
					$("<li>",{
						"resourceId":frameMemuData[i].resourceId,
					}).data(frameMemuData[i]).html("<a href='#"+frameMemuData[i].resourceId+"'>"+frameMemuData[i].resourceName+"</a>").appendTo("#topMenus");
					$("<div>",{
						"class":"m-memu",
						"resourceId":frameMemuData[i].resourceId
					}).appendTo(".g-sd-left");
				}else{
					var url = frameMemuData[i].url;
					if($(".g-sd-left .m-memu[resourceId='"+frameMemuData[i].parentResourceId+"']").length>0){
						$("<div>",{
							"class":"memu-panel",
							"resourceId":frameMemuData[i].resourceId
						}).appendTo($(".g-sd-left .m-memu[resourceId='"+frameMemuData[i].parentResourceId+"']"));
						var imgpath=frameMemuData[i].imgPath||"665";
						var zclose='';
						var ihtml='';
						if(frameMemuData[i].tplName=='OPEN'){
							zclose='  z-close';
							ihtml=' <i class="iconfont arrow icon-down"></i>';
						}
						$("<div>",{
							"class":"one-level-memu "+zclose,
							// "onclick":"shop.commonClick('"+CONTEXT_PATH+url+"')",
							"resourceId":frameMemuData[i].resourceId
						}).data(frameMemuData[i]).html("<i class='icon iconfont'>&#xe"+imgpath+";</i>  "+frameMemuData[i].resourceName+ihtml).appendTo($(".g-sd-left .memu-panel[resourceId='"+frameMemuData[i].resourceId+"']"));
					}else{
						$("<div>",{
							"class":"two-level-memu",
							"resourceId":frameMemuData[i].resourceId
						}).data(frameMemuData[i]).html(frameMemuData[i].resourceName +"<i class='iconfont  icon-righttriangle'></i>").appendTo($(".g-sd-left .memu-panel[resourceId='"+frameMemuData[i].parentResourceId+"']"));
					}
				}
			}
		}
	}
	var bindFrameEvent=function () {
		$(".g-sd-left .two-level-memu").click(function() {
			$(".g-sd-left .one-level-memu").removeClass("z-sel");
			$(".g-sd-left .two-level-memu").removeClass("z-sel");
			$(this).addClass("z-sel");
			$(this).siblings(".one-level-memu").addClass("z-sel");
			var data=$(this).data();
			memuClick(data);
		});
		$(".g-sd-left .one-level-memu").click(function() {
			$(".g-sd-left .one-level-memu").removeClass("z-sel");
			$(".g-sd-left .two-level-memu").removeClass("z-sel");
			$(this).addClass("z-sel");
			$(this).next().addClass("z-sel");
			var data=$(this).data();
			console.log(data);
			if(data.tplName=='OPEN'){
				$(this).toggleClass('z-close');
				$('.arrow',this).toggleClass('icon-right').toggleClass('icon-down').toggleClass('icon-bottom')
			}else{
				memuClick(data);
			}
		});
		$("#topMenus li").click(function() {
			$(this).siblings().removeClass("z-sel");
			$(this).addClass("z-sel");
			var resourceId = $(this).attr("resourceId");
			$(".g-sd-left  .m-memu").hide();
			$(".g-sd-left  .m-memu[resourceId='" + resourceId + "']").show();
			$(".g-sd-left  .m-memu[resourceId='" + resourceId + "'] .one-level-memu:first").click();

		});
	}
	var memuClick=function  (data) {
		var url =data.url;
		var tpl =data.tplName;
		var herf=window.location.href;
		var lastsign=herf.indexOf("#");
		if(lastsign>0){
			herf=herf.substr(0,lastsign);
		}
		var resourceId=	$(".g-sd-left [resourceId="+data.resourceId+"]").closest(".m-memu").attr("resourceId");
		window.location.href=herf+"#"+resourceId+"#"+data.resourceId;
		if(tpl){
			if(url){
				// url=url.replace("{\shopId\}",shopId);
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
							jumi.template(tpl,data,function(html){
								$("#shop_content").html(html);
								//搜索栏点击缩放
								$(".btn-slide").click(function(){
									$("#m-search").slideToggle("fast");
									$(this).toggleClass("btn-slide1"); return false;
								})
							})
						}
					},
					fail:function(){
					}
				})
			}else{
				jumi.template(tpl,function(html){
					$("#shop_content").html(html);
					//搜索栏点击缩放
					$(".btn-slide").click(function(){
						$("#m-search").slideToggle("fast");
						$(this).toggleClass("btn-slide1"); return false;
					})
				})
			}
		}else if(url){
			url=CONTEXT_PATH+url;
			shop.commonClick(url);
		}else{
			$("#shop_content").empty();
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
	return {
		init:_init
	}
})();

