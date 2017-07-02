CommonUtils.regNamespace("shop", "indexMenu");

shop.indexMenu = (function(){
	var _init = function(){
		var herf=window.location.href;
		var ids =  herf.split("#");
		if(ids[1]!=undefined){
			$("#resourceD"+ids[1]).addClass("z-sel");
			var url  = CONTEXT_PATH+"/shop/shop_menu/"+ids[1];
			$.ajaxJsonGet(url,null, {
				"done": function (res) {
					$("#leftMenuUl").empty();
					var div ="";
					var count = res.data.length;
					for(var i=0;i<count;i++){
						var active = "";
						var active1 = "";
						if(ids[2]==res.data[i].resourceId){
							active = " z-sel"
						}
						if(ids[3]==res.data[i].resourceId){
							active1 = " z-sel"
						}
						if(ids[1]==res.data[i].parentResourceId){
							var div='<div class="memu-panel" id="leftMemuPanel'+res.data[i].resourceId+'" >'+
								'<div class="one-level-memu'+ active +'" id="menu'+res.data[i].resourceId+'"' +
								'data-tpl-pid="'+res.data[i].parentResourceId+'" data-tpl-id="'+res.data[i].resourceId+'" data-tpl-name="'+res.data[i].tplName+'" ' +
								'data-tpl-url="'+res.data[i].url+'" data-url="" onclick="getTpl(this)">' +
								'<i class="'+res.data[i].img+'"></i> '+
								res.data[i].resourceName+
								'</div>'+
								'<ul class="dropdown-menu">'+
								'</ul>'+
								'</div>';
							$("#leftMenuUl").append(div);
						}else{
							var div =	'<div class="two-level-memu'+active1+'"  id="menu'+res.data[i].resourceId+'"' +
								'data-tpl-pid="'+res.data[i].parentResourceId+'" data-tpl-id="'+res.data[i].resourceId+'" data-tpl-name="'+res.data[i].tplName+'" ' +
								'data-tpl-url="'+res.data[i].url+'" data-url="" onclick="getTpl(this,1)">'+
								res.data[i].resourceName+
								'<i class="iconfont icon-righttriangle"></i>'+
								'</div>';
							$("#leftMemuPanel"+res.data[i].parentResourceId+"").append(div);
						}

					}
					if(count>0){
						$("#shopIndexLeftmenu").show()
					}else{
						$("#shopIndexLeftmenu").hide()
					}
				}
			});

			if(ids[2]==undefined ){
				tplToContentById(ids[1]);
			}

		}else{
			$("#resourceD2").addClass("z-sel");
			jumi.template("shop/shop_index_content",function(tpl){
				$("#shopIndexContent").empty();
				$("#shopIndexContent").html(tpl);
			});
		}


		if(ids[3]!=undefined ){
			tplToContentById(ids[3]);
			return;
		}

		if(ids[2]!=undefined ){
			tplToContentById(ids[2]);
			return;
		}
	};


	return {
		init :_init
	};
})();

//根据菜单Id 填充页面到内容中
function tplToContentById(resourceId){
    var url  = CONTEXT_PATH+"/shop/shop_menu2/"+resourceId;
    //1.获取菜单对象
    $.ajaxJsonGet(url,null, {
        "done": function (res) {
            var tplname = res.data.tplName;
            var tplurl = res.data.url;
            if(tplurl=="" ||tplurl==null ||tplurl=="null"){
                var alldata = {
					CONTEXT_PATH:CONTEXT_PATH,
					STATIC_URL:STATIC_URL,
					THIRD_URL:THIRD_URL
                };
                jumi.template(tplname,alldata,function(tpl){
                    $("#shopIndexContent").empty();
                    $("#shopIndexContent").html(tpl);
                });
            }else{
                $.ajaxJsonGet(CONTEXT_PATH+tplurl, null, {
                    "done": function (res) {
                        if(res.code===0){
                            if($.isArray(res.data)){
                                var alldata = {
                                    items:res.data,
									CONTEXT_PATH:CONTEXT_PATH,
									STATIC_URL:STATIC_URL,
									THIRD_URL:THIRD_URL
                                }
                            }else{
                                var alldata = res.data;
								alldata.CONTEXT_PATH=CONTEXT_PATH;
								alldata.STATIC_URL=STATIC_URL;
								alldata.THIRD_URL=THIRD_URL;
                            }
                            jumi.template(tplname,alldata,function(tpl){
                                $("#shopIndexContent").empty();
                                $("#shopIndexContent").html(tpl);
                            });
                        }
                    }
                });
            }

        }
    });
}


//点击左菜单
function getTpl(dD,tD) {
	var herf=window.location.href;
	var tplpid = $(dD).attr("data-tpl-pid");
	var tplid = $(dD).attr("data-tpl-id");
	if(tD==1){
		var linkurl = herf.split("#");
		window.location.href=linkurl[0]+"#"+linkurl[1]+"#"+tplpid+"#"+tplid;
	}else{
		var lastsign=herf.indexOf("#");
		if(lastsign>0){
			herf=herf.substr(0,lastsign);
		}
		window.location.href=herf+"#"+tplpid+"#"+tplid;
	}
	$('div[id^="menu"]').removeClass("z-sel");
	$(dD).siblings(".one-level-memu").addClass("z-sel");
	$(dD).addClass("z-sel");
	var tplname = $(dD).attr("data-tpl-name");
	var tplurl = $(dD).attr("data-tpl-url");
	if(tplurl=="" || tplurl==null || tplurl=='null'){
		jumi.template(tplname,function(tpl){
			$("#shopIndexContent").empty();
			$("#shopIndexContent").html(tpl);
		});
	}else{
		$.ajaxJsonGet(CONTEXT_PATH+tplurl, null, {
			"done": function (res) {
				if(res.code===0){
					if($.isArray(res.data)){
						var alldata = {
							items:res.data,
							CONTEXT_PATH:CONTEXT_PATH,
							STATIC_URL:STATIC_URL,
							THIRD_URL:THIRD_URL
						}
					}else{
						var alldata = res.data;
						alldata.CONTEXT_PATH=CONTEXT_PATH;
						alldata.STATIC_URL=STATIC_URL;
						alldata.THIRD_URL = THIRD_URL;
					}
					jumi.template(tplname,alldata,function(tpl){
						$("#shopIndexContent").empty();
						$("#shopIndexContent").html(tpl);
					});
				}
			}
		});
	}
};


//点击顶部菜单
function toTpl(dD) {
	var herf=window.location.href;
	var lastsign=herf.indexOf("#");
	if(lastsign>0){
		herf=herf.substr(0,lastsign);
	}
	var tplid = $(dD).attr("data-tpl-id");
    loadHead(tplid); //加载头部
	window.location.href=herf+"#"+tplid;

	$(".z-sel").removeClass("z-sel");
	$(dD).parent().addClass("z-sel");
	var tplname = $(dD).attr("data-tpl-name");
	var tplurl = $(dD).attr("data-tpl-url");
	var id = $(dD).attr("data-id");
	var url  = CONTEXT_PATH+"/shop/shop_menu/"+id;
	$.ajaxJsonGet(url,null, {
		"done": function (res) {
			$("#leftMenuUl").empty();
			var count = res.data.length;
			for(var i=0;i<count;i++){

				if(tplid == res.data[i].parentResourceId){
					var div ='<div class="memu-panel" id="leftMemuPanel'+res.data[i].resourceId+'" >'+
						'<div class="one-level-memu" id="menu'+res.data[i].resourceId+'"' +
						'data-tpl-pid="'+res.data[i].parentResourceId+'" data-tpl-id="'+res.data[i].resourceId+'" data-tpl-name="'+res.data[i].tplName+'" ' +
						'data-tpl-url="'+res.data[i].url+'" data-url="" onclick="getTpl(this)">' +
						'<i class="'+res.data[i].img+'"></i> '+
						res.data[i].resourceName+
						'</div>'+
						'<ul class="dropdown-menu">'+
						'</ul>'+
						'</div>';
					$("#leftMenuUl").append(div);
				}else{
					var div =	'<div class="two-level-memu" id="menu'+res.data[i].resourceId+'"' +
								'data-tpl-pid="'+res.data[i].parentResourceId+'" data-tpl-id="'+res.data[i].resourceId+'" data-tpl-name="'+res.data[i].tplName+'" ' +
								'data-tpl-url="'+res.data[i].url+'" data-url="" onclick="getTpl(this,1)">'+
								res.data[i].resourceName+
								'<i class="iconfont icon-righttriangle"></i>'+
								'</div>';
					$("#leftMemuPanel"+res.data[i].parentResourceId+"").append(div);
				}

			}
			if(count>0){
				$("#shopIndexLeftmenu").show()
			}else{
				$("#shopIndexLeftmenu").hide()
			}

		}
	});

	if(tplurl=="" ||tplurl==null ||tplurl=="null"){
		var alldata = {
			CONTEXT_PATH:CONTEXT_PATH,
			STATIC_URL:STATIC_URL,
			THIRD_URL : THIRD_URL
		};
		jumi.template(tplname,alldata,function(tpl){
			$("#shopIndexContent").empty();
			$("#shopIndexContent").html(tpl);
		});
	}else{
		$.ajaxJsonGet(CONTEXT_PATH+tplurl, null, {
			"done": function (res) {
				if(res.code===0){
					if($.isArray(res.data)){
						var alldata = {
							items:res.data,
							CONTEXT_PATH:CONTEXT_PATH,
							STATIC_URL:STATIC_URL,
							THIRD_URL:THIRD_URL
						}
					}else{
						var alldata = res.data;
						alldata.CONTEXT_PATH=CONTEXT_PATH;
						alldata.STATIC_URL = STATIC_URL;
						alldata.THIRD_URL = THIRD_URL;
					}
					jumi.template(tplname,alldata,function(tpl){
						$("#shopIndexContent").empty();
						$("#shopIndexContent").html(tpl);
					});
				}
			}
		});
	}

}

var loadHead = function (tplid) {
	if(tplid==9){
        var li = '<li onclick="community.index.data()"> <a>论坛资料</a> </li> ' +
            '<li> <a>@我<sup>·</sup></a> </li> ' +
            '<li> <a>消息</a> </li> ' +
            '<a href=/loginout><li> <a>退出</a> </li></a>';
        $('.g-memu ul').empty();
        $('.g-memu ul').html(li);
	}else{
        var li = '<a href="'+CONTEXT_PATH+'/shop"> <li>店铺管理</li> </a> ' +
			'<a href="'+CONTEXT_PATH+'/shop#3" target="_blank"> <li>个人资料</li> </a>' +
			' <a href="http://jumiweigu.com/" target="_blank"> <li>官网</li> </a>' +
			' <a href="'+CONTEXT_PATH+'/loginout"> <li>退出</li> </a>';
        $('.g-memu ul').empty();
        $('.g-memu ul').html(li);
	}
}