
CommonUtils.regNamespace("frame");
frame=(function () {
	//var ajaxframeurl=CONTEXT_PATH+"/resource/0";
	var ajaxframeurl=CONTEXT_PATH+"/resource/role_resource/0";

	var frameMemuData =[];

	var _init=function(){
		getframeMemu().then(function() {
			initFrameMemu();
			bindFrameEvent();
			goMenu()
		}) ;
	}
	var goMenu=	function (){
		var hash=window.location.hash;
		if(hash){
			var hasharr=hash.split("#");
			if(hasharr.length>0){
				$("#jumiheader .m-nav ul  [resourceId='" + hasharr[1]+ "']").addClass("z-sel").siblings().removeClass("z-sel");
				$(".g-sd-left  .m-memu").hide();
				$(".g-sd-left  .m-memu[resourceId='" +  hasharr[1] + "']").show();
				if(hasharr[2]){
					$(".g-sd-left  .m-memu[resourceId='"+hasharr[1]+"'] .memu-panel [resourceId='"+hasharr[2]+"'] ").click();
				}else {
					$(".g-sd-left  .m-memu[resourceId='"+hasharr[1]+"'] .memu-panel  .one-level-memu:first ").click();
				}
			}else{
				$("#jumiheader .m-nav ul li:first").click();
			}
		}else{
			$("#jumiheader .m-nav ul li:first").click();
		}
	}
	var getframeMemu=function () {
		var dfdPlay = $.Deferred();
		//$.ajaxJson(ajaxframeurl,null,null);
		var data = $.ajaxJsonGet(ajaxframeurl);
		if(data.code=="0"){
			frameMemuData = data.data;
			dfdPlay.resolve(); // 动画完成
		}
		return dfdPlay;
	}
	var initFrameMemu=function (){
		var shopId=$("#shopId").val();
		if(frameMemuData!=null&frameMemuData.length>0){
			for (var i=0;i<frameMemuData.length;i++) {
				var item=frameMemuData[i];
				if(item.parentRsourceId===0){
					$("<li>",{
						"resourceId":frameMemuData[i].resourceId,
					}).data(frameMemuData[i]).html("<a href='#"+frameMemuData[i].resourceId+"'>"+frameMemuData[i].resourceName+"</a>").appendTo("#jumiheader .m-nav ul ");
					$("<div>",{
						"class":"m-memu",
						"resourceId":frameMemuData[i].resourceId
					}).appendTo(".g-sd-left");
				}else{
					var url = frameMemuData[i].url;
					if(null != frameMemuData[i].url){
						if(frameMemuData[i].url.indexOf("\{\shopId\}")>=0){
							var newUrl = url.substring(0,url.indexOf("\{\shopId\}"));
							url = newUrl+shopId;
						}
					}
					if($(".g-sd-left .m-memu[resourceId='"+frameMemuData[i].parentRsourceId+"']").length>0){
						$("<div>",{
							"class":"memu-panel",
							"resourceId":frameMemuData[i].resourceId
						}).appendTo($(".g-sd-left .m-memu[resourceId='"+frameMemuData[i].parentRsourceId+"']"));
						var imgpath=frameMemuData[i].imgPath||"icon-menuset";
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
						}).data(frameMemuData[i]).html("<i class='iconfont  "+imgpath+"'></i>  "+frameMemuData[i].resourceName+ihtml).appendTo($(".g-sd-left .memu-panel[resourceId='"+frameMemuData[i].resourceId+"']"));
					}else{
						$("<div>",{
							"class":"two-level-memu",
							//"+CONTEXT_PATH+"/product/to_product_page/1
							// "onclick":"shop.commonClick('"+CONTEXT_PATH+url+"')",
							"resourceId":frameMemuData[i].resourceId
						}).data(frameMemuData[i]).html(frameMemuData[i].resourceName +"<i class='iconfont  icon-righttriangle'></i>").appendTo($(".g-sd-left .memu-panel[resourceId='"+frameMemuData[i].parentRsourceId+"']"));
					}
				}
			}
		}
	}
	var bindFrameEvent=function () {
		// $(".memu-panel-group").sortable({
		// 	placeholder: "memu-panel-highlight"
		// }).disableSelection();
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
			if(data.tplName=='OPEN'){
				$(this).toggleClass('z-close');
				$('.arrow',this).toggleClass('icon-right').toggleClass('icon-down');
				$('.arrow',this).toggleClass('icon-bottom')
			}else{
				memuClick(data);
			}
		});
		$("#jumiheader .m-nav ul li").click(function() {
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
		var shopId=$("#shopId").val();
		var herf=window.location.href;
		var lastsign=herf.indexOf("#");
		if(lastsign>0){
			herf=herf.substr(0,lastsign);
		}
		var resourceId=	$(".g-sd-left [resourceId="+data.resourceId+"]").closest(".m-memu").attr("resourceId");
		window.location.href=herf+"#"+resourceId+"#"+data.resourceId;
		if(tpl){
			if(url){

				url=url.replace("{\shopId\}",shopId);
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
								shop.cleanData();
								$("#shop_content").html(html);
							})
						}
					},
					fail:function(){
					}
				})
			}else{
				jumi.template(tpl,function(html){
					shop.cleanData();
					$("#shop_content").html(html);
				})

			}
		}else if(url){
			url=url.replace("{\shopId\}",shopId);
			url=CONTEXT_PATH+url;
			shop.commonClick(url);
		}else{
			jumi.template('system/working',function(html){
				$("#shop_content").html(html);
			})
		}
	}
	return {
		init:_init
	}
})();

