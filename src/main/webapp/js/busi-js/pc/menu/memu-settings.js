//modify by pray 2017-5-23


var memudata = [];
//var ajaxurl = "js/busi-js/system/memu.json"; //

var ajaxurl=CONTEXT_PATH+"/menu";
var defaultMemu = {
	"type": "view",
	"name": "菜单",
	"key": "",
	"url": "" 
};
var memudialog = null;
var memuObj = null;
/**
 * 判断是否 为空
 * @param {Object} data
 */
function isNull(data) {
	return (data == "" || data == undefined || data == null) ? false : true;
}
/**
 * 判断是否是合法的url地址
 */
function checkeURL(str){ 
        //在JavaScript中，正则表达式只能使用"/"开头和结束，不能使用双引号 
        var Expression=/http(s)?:\/\/([\w-]+\.)+[\w-]+(\/[\w- .\/?%&=]*)?/; 
        var objExp=new RegExp(Expression); 
        if(str.indexOf("localhost")){ 
            str = str.replace("localhost","127.0.0.1"); 
        } 
        if(objExp.test(str)==true){ 
            return true; 
        }else{ 
            return false; 
        } 
    }

/**
 * 初始化方法
 */
function init() {
	getMemulist().done(function() {
		initmemu();
	});
	//		initmemu();
}
/**
 * 获取菜单数据
 */
function getMemulist() {
	var dfdPlay = $.Deferred();
	$.ajax({
		type: "get",
		url: ajaxurl,
		dataType: 'json',
		success: function(data) {
			memudata = data;
			dfdPlay.resolve(); // 动画完成
		}
	});
	return dfdPlay;
}
/**
 * 初始化菜单项目
 */
function initmemu() {
	if (memudata != null && memudata.length > 0) {
		for (var i = 0; i < memudata.length && i < 3; i++) {
			var data = $.extend({}, defaultMemu, memudata[i]);
			var memugroup = addMemuGroup(data, i);
			memugroup.appendTo(".menu-settings");
			isShowTwoAddBtn(i);
		}
	} else {
		var memugroup = addMemuGroup(defaultMemu, 0);
		memugroup.appendTo(".menu-settings");
	}
	$("#app-bottom-memu  .main-li:first-child ").addClass("selected").siblings().removeClass("selected");
	sortAddBtn();
	sortableMemu();
	var index = $(".menu-settings .menu-settings-panel").length;
	if (index >2) {
		$(".one-level-add .btn").hide();
	}
}
//重新排序一级菜单添加按钮 
function sortAddBtn() {
	$(".one-level-add").insertAfter(".menu-settings .menu-settings-panel:last-child");
	$(".menu-settings .one-level label").each(function(i) {
		$(this).html(i + 1);
		$(".menu-settings .menu-settings-panel:eq(" + i + ") .two-level-panel label").each(function(j) {
			$(this).html("菜单" + (j + 1));
		});
	});
}
var panel = [];
var beforeObj = {
		index: 0,
		twoindex: 0
	},
	afterObj = {
		index: 0,
		twoindex: 0
	};
/**
 *  绑定菜单拖动方法
 */
function sortableMemu() {
	$(".menu-settings .two-level-panel").each(function(i) {
		panel.push($(this));
	});
	$(panel).sortable({
		opacity: "0.6",
		connectWith: ".two-level-panel",
		placeholder: "placeHolder",
		start: function(event, ui) {
			var pobj = $(ui.item.context).closest(".menu-settings-panel ");
			pindex = pobj.index();
			beforeObj.index = pindex;
			beforeObj.twoindex = $(ui.item.context).index();
		},
		receive: function(event, ui) {
			var pobj = $(ui.item.context).closest(".menu-settings-panel ");
			var sortedIDs = $(this).sortable("toArray");
			if (sortedIDs.length > 5) {
				$(panel).sortable("cancel");
				sortAddBtn();
			}
		},
		stop: function(event, ui) {
			var pobj = $(ui.item.context).closest(".menu-settings-panel ");
			pindex = pobj.index();
			afterObj.index = pindex;
			afterObj.twoindex = $(ui.item.context).index();
			if (beforeObj.index == afterObj.index && beforeObj.twoindex == afterObj.twoindex) {} else {
				reloadMemu();
				changeOneMemuEditState(beforeObj.index);
				changeOneMemuEditState(afterObj.index);
				isShowTwoAddBtn(beforeObj.index);
				isShowTwoAddBtn(afterObj.index);
			}
		}
	}).disableSelection();
}
/**
 * 重新排序 手机中的菜单顺序
 */
function reloadMemu() {
	sortAddBtn();
	if (beforeObj.index == afterObj.index) {
		if (beforeObj.twoindex - 1 == afterObj.twoindex) {
			$("#app-bottom-memu .main-li:eq(" + beforeObj.index + ") .sub-menu li:eq(" + beforeObj.twoindex + ") ")
				.insertBefore("#app-bottom-memu .main-li:eq(" + afterObj.index + ") .sub-menu li:eq(" + (afterObj.twoindex) + ") ");
		} else if (beforeObj.twoindex + 1 == afterObj.twoindex) {
			$("#app-bottom-memu .main-li:eq(" + beforeObj.index + ") .sub-menu li:eq(" + beforeObj.twoindex + ") ")
				.insertAfter("#app-bottom-memu .main-li:eq(" + afterObj.index + ") .sub-menu li:eq(" + (afterObj.twoindex) + ") ");
		} else if (beforeObj.twoindex < afterObj.twoindex) {
			$("#app-bottom-memu .main-li:eq(" + beforeObj.index + ") .sub-menu li:eq(" + beforeObj.twoindex + ") ")
				.insertAfter("#app-bottom-memu .main-li:eq(" + afterObj.index + ") .sub-menu li:eq(" + (afterObj.twoindex) + ") ");
		} else {
			$("#app-bottom-memu .main-li:eq(" + beforeObj.index + ") .sub-menu li:eq(" + beforeObj.twoindex + ") ")
				.insertBefore("#app-bottom-memu .main-li:eq(" + afterObj.index + ") .sub-menu li:eq(" + (afterObj.twoindex) + ") ");
		}
	} else {
		var len = $("#app-bottom-memu .main-li:eq(" + afterObj.index + ") .sub-menu li").length;
		if (len == 0) {
			$("<ul>", {
				"class": "sub-menu"
			}).html("<div class='arrow'>").appendTo($("#app-bottom-memu .main-li:eq(" + afterObj.index + ")"));
			$("#app-bottom-memu .main-li:eq(" + beforeObj.index + ") .sub-menu li:eq(" + beforeObj.twoindex + ") ")
				.appendTo("#app-bottom-memu .main-li:eq(" + afterObj.index + ") .sub-menu");
		} else if (len == afterObj.twoindex) {
			$("#app-bottom-memu .main-li:eq(" + beforeObj.index + ") .sub-menu li:eq(" + beforeObj.twoindex + ") ")
				.insertAfter("#app-bottom-memu .main-li:eq(" + afterObj.index + ") .sub-menu li:eq(" + (afterObj.twoindex - 1) + ") ");

		} else {
			$("#app-bottom-memu .main-li:eq(" + beforeObj.index + ") .sub-menu li:eq(" + beforeObj.twoindex + ") ")
				.insertBefore("#app-bottom-memu .main-li:eq(" + afterObj.index + ") .sub-menu li:eq(" + (afterObj.twoindex) + ") ");
		}
		var beforelen = $("#app-bottom-memu .main-li:eq(" + beforeObj.index + ") .sub-menu li").length;
		if (beforelen == 0) {
			$("#app-bottom-memu .main-li:eq(" + beforeObj.index + ") .sub-menu   ").remove();
		}
	}

	$("#app-bottom-memu .main-li:eq(" + afterObj.index + ")").addClass("selected").siblings().removeClass("selected");
	resortableMemu();
}
/**
 * 重新绑定 菜单拖动事件
 */
function resortableMemu() {
	$(panel).sortable("destroy");
	panel = [];
	sortableMemu();
}
/**
 * 添加一级菜单组
 * @param {Object} data
 * @param {Object} index
 */
function addMemuGroup(data, index) {
	var memugroup = $(".clone-div .menu-settings-panel").clone();
	memugroup.data(data);
	memugroup.find(".one-level").attr("id","onememu"+new Date().getTime());
	memugroup.find(".one-level  label").html(index + 1);
	memugroup.find(".one-level  .memu-name").val(data.name);

	// memugroup.find(".one-level  .link-input").val(data.linkName);
	if(data.url&&data.key!='GetCart'&&data.linkName) {
		memugroup.find(".one-level  .link-input").prop("disabled","disabled");
		memugroup.find(".one-level  .link-input").removeAttr("placeholder");
		memugroup.find(".one-level  .link-input").before('<div class="link-tag-add" style="color:#696868;position:absolute;left:90px"><span>[链接]</span><a target="_blank" href="' + data.url + '" style="padding-left:5px;color:#3399ff;text-decoration:underline;">'+data.linkName+'</a></div>');
	}else if(!data.url&&data.key=='GetCart'){
		memugroup.find(".one-level  .link-input").prop("disabled","disabled");
		memugroup.find(".one-level  .link-input").removeAttr("placeholder");
		memugroup.find(".one-level  .link-input").before('<div class="link-tag-add" style="color:#696868;position:absolute;left:90px"><span>[非链接]</span><span style="padding-left:5px;color:#3399ff;">'+data.linkName+'</span></div>');
	}else if(data.url&&!data.linkName){
		memugroup.find(".one-level  .link-input").prop("disabled","disabled");
		memugroup.find(".one-level  .link-input").removeAttr("placeholder");
		memugroup.find(".one-level  .link-input").before('<div class="link-tag-add" style="color:#696868;position:absolute;left:90px"><span style="float: left">[外链]</span><a target="_blank" href="' + data.url + '" style="padding-left:5px;color:#3399ff;text-decoration:underline;display:block;white-space:nowrap; overflow:hidden; text-overflow:ellipsis;width:140px;float:left">'+data.url+'</a></div>');
	}
		memugroup.appendTo(".menu-settings");
	//手机菜单添加
	var tmpli = $("<li>", {
		"class": "main-li"
	}).html("<span>" + data.name + "</span>").appendTo("#app-bottom-memu");
	tmpli.addClass("selected").siblings().removeClass("selected");
	var subButton = data.subButton;
	if (subButton) {
		for (var j = 0; j < subButton.length && j < 5; j++) {
			var data = $.extend({}, defaultMemu, subButton[j]);
			var twomemu = addTwoMemu(data, index, j);
			twomemu.appendTo(memugroup.find(".two-level-panel"));
		}

	}
	return memugroup;
}
/**
 * 删除一级菜单组
 * @param {Object} index
 */
function delMemuGroup(index) {
	$(".menu-settings .menu-settings-panel:eq(" + index + ")").remove();
	$("#app-bottom-memu .main-li:eq(" + index + ")").remove();

	sortAddBtn();
}

/**
 * 改变一级菜单的是否可以编辑地址的状态
 * @param {Object} index
 */
function changeOneMemuEditState(index){
	 try{
	    var onememu=$(".menu-settings .menu-settings-panel:eq(" + index + ") ");
	 	if($(".two-level",onememu).length>0){
		 	var onememuData= onememu.data();
			var memugroup = $(".clone-div .menu-settings-panel").clone();
			onememuData.type="click";
			onememuData.url=null;
			$(".one-level .link-input",onememu).val("").attr({readonly:"true",disabled:"disabled"});
			$(".one-level .link-tag-add",onememu).remove();
			$(".one-level .link-input",onememu).removeAttr("placeholder");
	 	}else{
	 		$(".one-level .link-input",onememu).removeAttr("readonly");
			$(".one-level .link-input",onememu).removeAttr("disabled");
	 	}
	 }catch(e){
	 }
	return false;
}


/**
 * 添加二级菜单
 * @param {Object} data
 * @param {Object} index
 * @param {Object} twoindex
 */
function addTwoMemu(data, index, twoindex) {
	if ($("#app-bottom-memu .main-li:eq(" + index + ") .sub-menu").length == 0) {
		$("<ul>", {
			"class": "sub-menu"
		}).html("<div class='arrow'>").appendTo($("#app-bottom-memu .main-li:eq(" + index + ")"));
	}
	var twomemu = $(".clone-div .two-level").clone();
	twomemu.data(data).attr("id","twomemu"+new Date().getTime());
	twomemu.find("  label").html("菜单" + (twoindex + 1));
	twomemu.find(" .memu-name").val(data.name);
	if(data.url&&data.key!='GetCart'&&data.linkName){
		twomemu.find(" .link-input").prop("disabled","disabled");
		twomemu.find(" .link-input").removeAttr("placeholder");
	    twomemu.find(" .link-input").before('<div class="link-tag-add" style="color:#696868;position:absolute;left:90px"><span>[链接]</span><a target="_blank" href="' + data.url + '" style="padding-left:5px;color:#3399ff;text-decoration:underline;">'+data.linkName+'</a></div>');
	}else if(!data.url&&data.key=='GetCart'){
		twomemu.find(" .link-input").prop("disabled","disabled");
		twomemu.find(" .link-input").removeAttr("placeholder");
		twomemu.find(" .link-input").before('<div class="link-tag-add" style="color:#696868;position:absolute;left:90px"><span>[非链接]</span><span style="padding-left:5px;color:#3399ff;">'+data.linkName+'<span/></div>');
	}else if(data.url&&!data.linkName){
		twomemu.find(" .link-input").prop("disabled","disabled");
		twomemu.find(" .link-input").removeAttr("placeholder");
		twomemu.find(" .link-input").before('<div class="link-tag-add" style="color:#696868;position:absolute;left:90px"><span style="float: left">[外链]</span><a target="_blank" href="' + data.url + '" style="padding-left:5px;color:#3399ff;text-decoration:underline;display:block;white-space:nowrap; overflow:hidden; text-overflow:ellipsis;width:140px;float:left">'+data.url+'</a></div>');
	}
		//添加 子菜单
	$("<li>", {}).html("<a>" + data.name + "</a>").appendTo($("#app-bottom-memu .main-li:eq(" + index + ") .sub-menu"));
	return twomemu;
}
/**
 * 删除二级菜单
 * @param {Object} index
 * @param {Object} twoindex
 */
function delTwoMemu(index, twoindex) {
	$(".menu-settings .menu-settings-panel:eq(" + index + ") .two-level-panel .two-level:eq(" + twoindex + ")").remove();
	$("#app-bottom-memu .main-li:eq(" + index + ") .sub-menu li:eq(" + twoindex + ")").remove();
	$("#app-bottom-memu .main-li:eq(" + index + ")").addClass("selected").siblings().removeClass("selected");
	$(".menu-settings .menu-settings-panel:eq(" + index + ") .two-level-panel label").each(function(i) {
		$(this).html("菜单" + (i + 1));
	});

	var len = $("#app-bottom-memu .main-li:eq(" + index + ") .sub-menu li").length;
	if (len == 0) {
		$("#app-bottom-memu .main-li:eq(" + index + ") .sub-menu   ").remove();
	}
	changeOneMemuEditState(index);
}

function isShowTwoAddBtn(index){
	var panel=$(".menu-settings .menu-settings-panel:eq("+index+")");
	var len = $(".two-level",panel).length;
	if(len<5){
		$(".two-level-add",panel).show();
	}else{
		$(".two-level-add",panel).hide();
	}
}



/**
 * 提示信息 3秒 消失
 * @param {Object} str
 * @param {Object} id
 */
function showTip(str, id) {
	var d = dialog({
		align: 'bottom',
		content: str,
		quickClose: true
	});
	if (isNull(id)) {
		d.show(document.getElementById(id));
	} else {
		d.show();
	}
	setTimeout(function() {
		d.close().remove();
	}, 2000);
}

$(function() {
	$(".app-bottom-memu").delegate("li", "click", function() {
		$(this).toggleClass("selected").siblings().removeClass("selected");
	});

	$("#nextStep").click(function() {
		memudata =[];
	    var sign=true;
		$(".menu-settings .menu-settings-panel").each(function(i) {
			var onedata = $(this).data();
			delete onedata.mediaId;
			if ($(this).find(".two-level").length > 0) {
				delete onedata.type ;
				delete onedata.url ;
				delete onedata.key ;
				onedata.subButton = [];
				$(this).find(".two-level").each(function(j) {
					var tmpdata = ($(this).data());
					if(tmpdata.type=="view"){
						if(!checkeURL(tmpdata.url)){
							showTip("地址格式不正确",$(this).attr("id"));
							sign=false;
						}
					   delete  tmpdata.key;
					}else{
					   delete  tmpdata.url;
					}
					delete tmpdata.mediaId ;
					delete tmpdata.sortableItem ;
					delete tmpdata.subButton ;
					onedata.subButton.push(tmpdata);
				});
			} else {
				if(!isNull(onedata.type)){
					onedata.type="view";
				}
				if(onedata.type=="view"){
						if(!isNull(onedata.url)||!checkeURL(onedata.url)){
					    	showTip("地址格式不正确",$(".one-level",this).attr("id"));
							sign=false;
						}
					   delete  onedata.key;
					}else if(onedata.type=="click"){
						if(!isNull(onedata.key)){
							showTip("请配置key值",$(".one-level",this).attr("id"));
							sign=false;
						}
						
					   delete  onedata.url;
					} 
				delete onedata.subButton;
			}
			memudata.push(onedata);
		});
		if(sign){
			var args = {};
			args.fn1 = function(){
				dialog({
					id: 'id-dialog',
					content:"<div   class='text-center'><i class='iconfont icon-loading'></i></div><div class='text-center'>请稍候......</div>",
				}).showModal();
				$.ajax({
					contentType:"application/json",
					type: "post",
					url: ajaxurl,
					data:JSON.stringify(memudata),
					dataType: 'json',
					success: function(data) {
						dialog.get('id-dialog').close().remove();

						var dm = new dialogMessage({
							type:1,
							title:'操作提醒',
							fixed:true,
							msg:'保存成功',
							isAutoDisplay:false
						});
						dm.render();
					}
				});
			};
			jumi.dialogSure('确定保存该菜单配置吗?',args);

	}



	});
	//添加一级菜单 
	$(".one-level-add .btn").click(function() {
		var index = $(".menu-settings .menu-settings-panel").length;
			var memugroup = addMemuGroup(defaultMemu, index);
			sortAddBtn();
			resortableMemu();

		var index = $(".menu-settings .menu-settings-panel").length;
		if (index >2) {
			$(".one-level-add .btn").hide();
		}

	});
	$(".menu-settings").delegate(".menu-settings-panel .two-level-add .btn", "click", function() {
		var el = $(this).closest(".menu-settings-panel ");
		var pindex = $(this).closest(".menu-settings-panel").index();
		var index = el.find(".two-level").length;
		var twomemu = addTwoMemu(defaultMemu, pindex, index);
		twomemu.appendTo(el.find(".two-level-panel"));
		 changeOneMemuEditState(pindex);
		$("#app-bottom-memu  .main-li:eq(" + pindex + ") ").addClass("selected").siblings().removeClass("selected");
		resortableMemu();
		isShowTwoAddBtn(pindex);
	});

	$(".menu-settings").delegate(".menu-settings-panel .one-level .iconfont", "click", function() {
		var pindex = $(this).closest(".menu-settings-panel").index();
		var index = $(".menu-settings .menu-settings-panel").length;
		$(".one-level-add .btn").show();
		if (index == 1) {
			var d = dialog({
				content: "至少保留1个一级菜单"
			}).show();
			setTimeout(function() {
				d.close().remove();
			}, 2000);
 
			return false;
		}
		delMemuGroup(pindex);
		resortableMemu();
	});

	$(".menu-settings").delegate(".menu-settings-panel .two-level .iconfont", "click", function() {
		var pindex = $(this).closest(".menu-settings-panel").index();
		var index = $(this).closest(".two-level").index();
		delTwoMemu(pindex, index);
		isShowTwoAddBtn(pindex);
	});

	$(".menu-settings").delegate(".menu-settings-panel .two-level .memu-name", "focusout", function() {
		var name = $(this).val();
		var data = $(this).closest(".two-level").data();
		data.name = name;
		var pindex = $(this).closest(".menu-settings-panel").index();
		var index = $(this).closest(".two-level").index();
		$("#app-bottom-memu .main-li:eq(" + pindex + ") .sub-menu li:eq(" + index + ") a").html(name);
		$("#app-bottom-memu .main-li:eq(" + pindex + ")").addClass("selected").siblings().removeClass("selected");
	});
	$(".menu-settings").delegate(".menu-settings-panel .one-level .memu-name", "focusout", function() {
		var name = $(this).val();
		var data = $(this).closest(".menu-settings-panel").data();
		data.name = name;
		var pindex = $(this).closest(".menu-settings-panel").index();
		$("#app-bottom-memu .main-li:eq(" + pindex + ") span").html(name);
		$("#app-bottom-memu .main-li:eq(" + pindex + ")").addClass("selected").siblings().removeClass("selected");
	});
	$(".menu-settings").delegate(".menu-settings-panel .one-level .link-input", "focusout", function() {
		var url = $(this).val();
		var data = $(this).closest(".menu-settings-panel").data();
	 	if(isNull(url)){
		    data.type="view";
		}
		data.url = url;
		var pindex = $(this).closest(".menu-settings-panel").index();
		$("#app-bottom-memu .main-li:eq(" + pindex + ")").addClass("selected").siblings().removeClass("selected");
	});
	$(".menu-settings").delegate(".menu-settings-panel .two-level .link-input", "focusout", function() {
		var url = $(this).val();
		var data = $(this).closest(".two-level").data();
		if(isNull(url)){
		    data.type="view";
		}
		data.url = url;
		var pindex = $(this).closest(".menu-settings-panel").index();
		$("#app-bottom-memu .main-li:eq(" + pindex + ")").addClass("selected").siblings().removeClass("selected");
	});

	$(".menu-settings").delegate(".menu-settings-panel .two-level .link-set", "click", function() {
		memuObj = $(this).closest(".two-level");
		setMenuLinkAction(memuObj);
	});

	$(".menu-settings").delegate(".menu-settings-panel .one-level .link-set", "click", function() {
		memuObj = $(this).closest(".menu-settings-panel");
	    var len= $('.two-level',memuObj).length;
	    if(len==0){
			setMenuLinkAction(memuObj);
	    	//setMenuLink();
	    }else{
	    	showTip("有子级菜单不能进行配置该项",$(".one-level",memuObj).attr("id"));
	    }

		
	});

	init();
});

//add by pray 
function  setMenuLinkAction(memuObj) {
	var shopId = $('#shopId').val();
	var data = memuObj.data();
	menuDialog.Menu.initPage({
		shop_id:shopId,
		modal:true,
	},function(menu){
		menu.getUrl(function(link){
			var url;
			switch(link.link_type){
				case '1':
					url = DOMAIN+link.link_url+"?shopId="+link.shop_id;
					break;
				case '2':
					url = DOMAIN+link.link_url+'/'+link.link_id+"?shopId="+link.shop_id;
					break;
				case '3':
					url = CONTEXT_PATH+link.link_url+"?shopId="+link.shop_id;
			}
			data.type = link.type;
			data.name = link.link_name;
			data.linkName = link.link_name; /*新加的*/
			data.key = link.link_key||null;
			data.url = url;
			if ($(memuObj).hasClass("menu-settings-panel")) {
				$(memuObj).find(".one-level .memu-name").val(link.link_name);

				if(link.link_name&&link.link_key!="GetCart"){
					$(memuObj).find(".one-level .link-tag-add").remove();
					$(memuObj).find(".one-level .link-input").prop("disabled","disabled");
					$(memuObj).find(".one-level .link-input").removeAttr("placeholder");
					$(memuObj).find(".one-level .link-input").before('<div class="link-tag-add" style="color:#696868;position:absolute;left:90px"><span>[链接]</span><a target="_blank" href="' + url + '" style="padding-left:5px;color:#3399ff;text-decoration:underline;">'+link.link_name+'</a></div>');
				}else if(!link.link_url&&link.link_key=='GetCart'){
					$(memuObj).find(".one-level .link-tag-add").remove();
					$(memuObj).find(".one-level .link-input").prop("disabled","disabled");
					$(memuObj).find(".one-level .link-input").removeAttr("placeholder");
					$(memuObj).find(".one-level .link-input").before('<div class="link-tag-add" style="color:#696868;position:absolute;left:90px"><span>[非链接]</span><span style="padding-left:5px;color:#3399ff;">'+link.link_name+'</span></div>');
				}
				var pindex = memuObj.index();

				$("#app-bottom-memu .main-li:eq(" + pindex + ") span").html(link.link_name);
				$("#app-bottom-memu .main-li:eq(" + pindex + ")").addClass("selected").siblings().removeClass("selected");

			} else if ($(memuObj).hasClass("two-level")) {
				$(memuObj).find(".memu-name").val(link.link_name);
				if(link.link_name&&link.link_key!="GetCart"){
					$(memuObj).find(".link-tag-add").remove();
					$(memuObj).find(".link-input").prop("disabled","disabled");
					$(memuObj).find(".link-input").removeAttr("placeholder");
					$(memuObj).find(".link-input").before('<div class="link-tag-add" style="color:#696868;position:absolute;left:90px"><span>[链接]</span><a target="_blank" href="' + url + '" style="padding-left:5px;color:#3399ff;text-decoration:underline;">'+link.link_name+'</a></div>');
				}else if(!link.link_url&&link.link_key=='GetCart'){
					$(memuObj).find(".link-tag-add").remove();
					$(memuObj).find(".link-input").prop("disabled","disabled");
					$(memuObj).find(".link-input").removeAttr("placeholder");
					$(memuObj).find(".link-input").before('<div class="link-tag-add" style="color:#696868;position:absolute;left:90px"><span>[非链接]</span><span style="padding-left:5px;color:#3399ff;">'+link.link_name+'</span></div>');
				}
				// }

				var pindex = $(memuObj).closest(".menu-settings-panel").index();
				var index = $(memuObj).closest(".two-level").index();
				$("#app-bottom-memu .main-li:eq(" + pindex + ") .sub-menu li:eq(" + index + ") a").html(link.link_name);
				$("#app-bottom-memu .main-li:eq(" + pindex + ")").addClass("selected").siblings().removeClass("selected");
			}
		});
	});
}






