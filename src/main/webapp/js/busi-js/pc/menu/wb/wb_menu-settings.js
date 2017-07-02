var memudata = [];

var ajaxurl=CONTEXT_PATH+"/wb/menu";
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
	// twomemu.find(" .link-input").val(data.linkName);
	// twomemu.find(" .link-input").click(function(){
	// 	window.open(data.url);
	// });
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
//checkeURL();
	    var sign=true;
		$(".menu-settings .menu-settings-panel").each(function(i) {
			var onedata = $(this).data();
			console.log(onedata);
			delete onedata.mediaId ;
			if ($(this).find(".two-level").length > 0) {
				delete onedata.type ;
				delete onedata.url ;
				delete onedata.key ;
				onedata.subButton = [];
				$(this).find(".two-level").each(function(j) {
					var tmpdata = ($(this).data());
					console.log(tmpdata);
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
						var result = data.result;
						var msg = "保存失败";
						if("true"==result){
							msg ="保存成功";
						}
						var dm = new dialogMessage({
							type:1,
							title:'操作提醒',
							fixed:true,
							msg:msg,
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
//		$(this).val(url);
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
//		$(this).val(url);
     
		var pindex = $(this).closest(".menu-settings-panel").index();
		$("#app-bottom-memu .main-li:eq(" + pindex + ")").addClass("selected").siblings().removeClass("selected");
	});

	$(".menu-settings").delegate(".menu-settings-panel .two-level .link-set", "click", function() {
		memuObj = $(this).closest(".two-level");
		/*var elem = document.getElementById('memudialog');
		memudialog = dialog({
			title: "菜单选择",
			content: elem,
			width: 600,
			height: 500,
		}).show();*/
		setMenuLink();
	});

	$(".menu-settings").delegate(".menu-settings-panel .one-level .link-set", "click", function() {
		memuObj = $(this).closest(".menu-settings-panel");
	    var len= $('.two-level',memuObj).length;
	    if(len==0){
	    	setMenuLink();
	    }else{
	    	showTip("有子级菜单不能进行配置该项",$(".one-level",memuObj).attr("id"));
	    }
	     
		/*var elem = document.getElementById('memudialog');
		memudialog = dialog({
			title: "菜单选择",
			content: elem,
			width: 600,
			height: 500,
		}).show();*/
		
	});

	//	 $("#memudialog").delegate(".addgoods-btn", "click", function() {
	//	 
	//	});
	init();
});


/**
 * 设置菜单值
 * @param {Object} type
 * @param {Object} name
 * @param {Object} key
 * @param {Object} url
 */
var setMemuLink = function(type, name, key, url, shopId ) {
	var data = memuObj.data();
	data.type = type;
	data.linkName = name; /*新加的*/
	data.key = key;
	data.url = prefixUrl+url+"?shopId="+shopId;
	if ($(memuObj).hasClass("menu-settings-panel")) {
		$(memuObj).find(".one-level .memu-name").val(data.name);
		// if(!data.key || data.key=='null'){ //判断key是空或者非GetCart才设置为名字，即排除掉二维码，因为原来显示url时，微信是无url
		//
		// }
		// $(memuObj).find(".link-input").val(name);
		if(data.linkName&&data.key!="GetCart"){
			$(memuObj).find(".one-level .link-tag-add").remove();
			$(memuObj).find(".one-level .link-input").prop("disabled","disabled");
			$(memuObj).find(".one-level .link-input").removeAttr("placeholder");
			$(memuObj).find(".one-level .link-input").before('<div class="link-tag-add" style="color:#696868;position:absolute;left:90px"><span>[链接]</span><a target="_blank" href="' + data.url + '" style="padding-left:5px;color:#3399ff;text-decoration:underline;">'+name+'</a></div>');
		}else if(!url&&data.key=='GetCart'){
			$(memuObj).find(".one-level .link-tag-add").remove();
			$(memuObj).find(".one-level .link-input").prop("disabled","disabled");
			$(memuObj).find(".one-level .link-input").removeAttr("placeholder");
			$(memuObj).find(".one-level .link-input").before('<div class="link-tag-add" style="color:#696868;position:absolute;left:90px"><span>[非链接]</span><span style="padding-left:5px;color:#3399ff;">'+name+'</span></div>');
		}
		// $(memuObj).find(".one-level .link-input").prop("disabled","disabled"); /*原来是data.url*/
		var pindex = memuObj.index();
		$("#app-bottom-memu .main-li:eq(" + pindex + ") span").html(name);
		$("#app-bottom-memu .main-li:eq(" + pindex + ")").addClass("selected").siblings().removeClass("selected");
	
	} else if ($(memuObj).hasClass("two-level")) {
		$(memuObj).find(".memu-name").val(data.name);
		// if(!data.key || data.key=='null'){
		//   $(memuObj).find(".link-input").val(name);/*原来是data.url*/
		if(data.linkName&&data.key!="GetCart"){
			$(memuObj).find(".link-tag-add").remove();
			$(memuObj).find(".link-input").prop("disabled","disabled");
			$(memuObj).find(".link-input").removeAttr("placeholder");
			// $(memuObj).find(".link-input").before('<a target="_blank" href="'+data.url+'" style="color:#3399ff;text-decoration:underline;position:absolute;left:90px">'+"[链接] "+name+'</a>');
			$(memuObj).find(".link-input").before('<div class="link-tag-add" style="color:#696868;position:absolute;left:90px"><span>[链接]</span><a target="_blank" href="' + data.url + '" style="padding-left:5px;color:#3399ff;text-decoration:underline;">'+name+'</a></div>');
		}else if(!url&&data.key=='GetCart'){
			$(memuObj).find(".link-tag-add").remove();
			$(memuObj).find(".link-input").prop("disabled","disabled");
			$(memuObj).find(".link-input").removeAttr("placeholder");
			$(memuObj).find(".link-input").before('<div class="link-tag-add" style="color:#696868;position:absolute;left:90px"><span>[非链接]</span><span style="padding-left:5px;color:#3399ff;">'+name+'</span></div>');
		}
		// }
		var pindex = $(memuObj).closest(".menu-settings-panel").index();
		var index = $(memuObj).closest(".two-level").index();
		$("#app-bottom-memu .main-li:eq(" + pindex + ") .sub-menu li:eq(" + index + ") a").html(name);
		$("#app-bottom-memu .main-li:eq(" + pindex + ")").addClass("selected").siblings().removeClass("selected");
	}
	memudialog.close().remove();
};

/***
 * 点击菜单设置链接弹出选择菜单链接设置页面
 * 
 */
/*function setMenuLink(){
	var url = CONTEXT_PATH+'/menu/setMenuLink';
	$.ajaxHtmlGet(url,null,{
		done:function(res) {
			if(res.code==0){
				$("#memudialog").empty();
				console.log(res.data);
				$(res.data).appendTo("#memudialog");
				popMsg();
			}else{
				alert(res.msg);
			}
		}
	});
}*/

// function popMsg(){
// 	var elem = document.getElementById('setMenuLink');
// 	memudialog=dialog({
// 		id:'show-dialog',
// 		width: 702,
// 		height: 600,
// 		title: '菜单链接设置',
// 		content: elem
// 	});
// 	memudialog.show();
// 	memudialog.onremove = function () {
// 		$('#setMenuLink').remove();
// 	};
// };

function setMenuLink(){
	jumi.template('menu/setMenulink',function(tpl){
		popMsgtpl(tpl);
	});
}

//ajax获取后端菜单数据
function getMenulink(){
	// localStorage.clear();
	var url = CONTEXT_PATH+'/menu/setMenuLink';
	var json_data = JSON.parse(localStorage.getItem("json"));
	 if(!json_data){
		 $.ajaxJsonGet(url,'',{
			 done:function(res) {
				 if(res.code==0){
					 var json = JSON.stringify(res.data);
					 var json = localStorage.setItem("json",json);
					 var json_data = JSON.parse(localStorage.getItem("json"));
					 menuDataRender(json_data);
				 }else{
					 alert(res.msg);
				 }
			 }
		 })
	 }
	else{
		 menuDataRender(json_data);
	 }

}


function popMsgtpl(tpl){
	memudialog=dialog({
		id:'show-dialog',
		width: 702,
		height: 800,
		title: '菜单链接设置',
		content: tpl,
		onshow:function(){
			getMenulink();
		}
	});
	memudialog.showModal();
	memudialog.onremove = function () {
		$('#setMenuLink').remove();
	};
}

//后台菜单数据渲染
function menuDataRender(json_data) {
	var shopId = $("#shopId").val();
	var array = _.where(json_data,{'parent_id':0});
	var array_1 = _.where(json_data,{'parent_id':1});
	var array_2 = _.where(json_data,{'parent_id':7});
	var array_3 = _.where(json_data,{'parent_id':16});
	var array_4 = _.where(json_data,{'parent_id':20});
	var array_5 = _.where(json_data,{'parent_id':24});
	var length = array.length;
	var len1 = array_1.length;
	var len2 = array_2.length;
	var len3 = array_3.length;
	var len4 = array_4.length;
	var len5 = array_5.length;
	var tpl1 = '',tpl2='',tpl3='',tpl4='',tpl5='';
	var ulTpl = '';
	for(var i=0;i<length;i++){
		ulTpl+='<ul class="tag-details" id="tag_'+i+'"></ul>';
	}
	$('#tag-details-content').append(ulTpl);
	for(var j=0;j<len1;j++){
		if(array_1[j].link_type=='1'&&array_1[j].link_name!='二维码海报'){
			tpl1+="<li class='sort' onClick=\"setMemuLink('"+array_1[j].type+"','"+array_1[j].link_name+"','"+array_1[j].link_key+"','"+array_1[j].link_url+"','"+shopId+"')\" >"+
				array_1[j].link_name+
				"<input type='checkbox'  />"+
				"<label class='iconfont icon-avoid' ></label>"+
				"</li>";
		}
		if(array_1[j].link_name=='二维码海报' && array_1[j].link_type=='1'){
			tpl1+="<li class='addgoods-btn' onClick=\"setMemuLink('"+array_1[j].type+"','"+array_1[j].link_name+"','"+array_1[j].link_key+"','"+array_1[j].link_url+"','"+shopId+"')\" >"+
				array_1[j].link_name+
				"<span></span>"+
				"</li>";
		}
        if(array_1[j].link_name!='二维码海报' && array_1[j].link_type=='2'){
            tpl1+="<li class='addgoods-btn' onclick=\"getUrlValueRequest('"+array_1[j].type+"','"+array_1[j].link_name+"','"+array_1[j].link_key+"','"+shopId+"')\">"+
                array_1[j].link_name+
                "<span></span>"+
                "</li>";
        }
	}
	for(var j=0;j<len2;j++){
		if(array_2[j].link_type=='1'&&array_2[j].link_name!='二维码海报'){
			tpl2+="<li class='sort' onClick=\"setMemuLink('"+array_2[j].type+"','"+array_2[j].link_name+"','"+array_2[j].link_key+"','"+array_2[j].link_url+"','"+shopId+"')\" >"+
				array_2[j].link_name+
				"<input type='checkbox'  />"+
				"<label class='iconfont icon-avoid' ></label>"+
				"</li>";
		}
		if(array_2[j].link_name=='二维码海报' && array_2[j].link_type=='1'){
			tpl2+="<li class='addgoods-btn' onClick=\"setMemuLink('"+array_2[j].type+"','"+array_2[j].link_name+"','"+array_2[j].link_key+"','"+array_2[j].link_url+"','"+shopId+"')\" >"+
				array_2[j].link_name+
				"<span></span>"+
				"</li>";
		}
		if(array_2[j].link_name!='二维码海报' && array_2[j].link_type=='2'){
			tpl2+="<li class='addgoods-btn' onclick=\"getUrlValueRequest('"+array_2[j].type+"','"+array_2[j].link_name+"','"+array_2[j].link_key+"','"+shopId+"')\">"+
				array_2[j].link_name+
				"<span></span>"+
				"</li>";
		}
	}

	for(var j=0;j<len3;j++){

		if(array_3[j].link_type=='1'&&array_3[j].link_name!='二维码海报'){
			tpl3+="<li class='sort' onClick=\"setMemuLink('"+array_3[j].type+"','"+array_3[j].link_name+"','"+array_3[j].link_key+"','"+array_3[j].link_url+"','"+shopId+"')\" >"+
				array_3[j].link_name+
				"<input type='checkbox'  />"+
				"<label class='iconfont icon-avoid' ></label>"+
				"</li>";
		}
		if(array_3[j].link_name=='二维码海报' && array_3[j].link_type=='1'){
			tpl3+="<li class='addgoods-btn' onClick=\"setMemuLink('"+array_3[j].type+"','"+array_3[j].link_name+"','"+array_3[j].link_key+"','"+array_3[j].link_url+"','"+shopId+"')\" >"+
				array_3[j].link_name+
				"<span></span>"+
				"</li>";
		}
		if(array_3[j].link_name!='二维码海报' && array_3[j].link_type=='2'){
			tpl3+="<li class='addgoods-btn' onclick=\"getUrlValueRequest('"+array_3[j].type+"','"+array_3[j].link_name+"','"+array_3[j].link_key+"','"+shopId+"')\">"+
				array_3[j].link_name+
				"<span></span>"+
				"</li>";
		}
	}

	for(var j=0;j<len4;j++){

		if(array_4[j].link_type=='1'&&array_4[j].link_name!='二维码海报'){
			tpl4+="<li class='sort' onClick=\"setMemuLink('"+array_4[j].type+"','"+array_4[j].link_name+"','"+array_4[j].link_key+"','"+array_4[j].link_url+"','"+shopId+"')\" >"+
				array_4[j].link_name+
				"<input type='checkbox'  />"+
				"<label class='iconfont icon-avoid' ></label>"+
				"</li>";
		}
		if(array_4[j].link_name=='二维码海报' && array_4[j].link_type=='1'){
			tpl4+="<li class='addgoods-btn' onClick=\"setMemuLink('"+array_4[j].type+"','"+array_4[j].link_name+"','"+array_4[j].link_key+"','"+array_4[j].link_url+"','"+shopId+"')\" >"+
				array_4[j].link_name+
				"<span></span>"+
				"</li>";
		}
		if(array_4[j].link_name!='二维码海报' && array_4[j].link_type=='2'){
			tpl4+="<li class='addgoods-btn' onclick=\"getUrlValueRequest('"+array_4[j].type+"','"+array_4[j].link_name+"','"+array_4[j].link_key+"','"+shopId+"')\">"+
				array_4[j].link_name+
				"<span></span>"+
				"</li>";
		}
	}

	for(var j=0;j<len5;j++){

		if(array_5[j].link_type=='1'&&array_5[j].link_name!='二维码海报'){
			tpl5+="<li class='sort' onClick=\"setMemuLink('"+array_5[j].type+"','"+array_5[j].link_name+"','"+array_5[j].link_key+"','"+array_5[j].link_url+"','"+shopId+"')\" >"+
				array_5[j].link_name+
				"<input type='checkbox'  />"+
				"<label class='iconfont icon-avoid' ></label>"+
				"</li>";
		}
		if(array_5[j].link_name=='二维码海报' && array_5[j].link_type=='1'){
			tpl5+="<li class='addgoods-btn' onClick=\"setMemuLink('"+array_5[j].type+"','"+array_5[j].link_name+"','"+array_5[j].link_key+"','"+array_5[j].link_url+"','"+shopId+"')\" >"+
				array_5[j].link_name+
				"<span></span>"+
				"</li>";
		}
		if(array_5[j].link_name!='二维码海报' && array_5[j].link_type=='2'){
			tpl5+="<li class='addgoods-btn' onclick=\"getUrlValueRequest('"+array_5[j].type+"','"+array_5[j].link_name+"','"+array_5[j].link_key+"','"+shopId+"')\">"+
				array_5[j].link_name+
				"<span></span>"+
				"</li>";
		}
	}

	$('#tag_0').append(tpl1);
	$('#tag_1').append(tpl2);
	$('#tag_2').append(tpl3);
	$('#tag_3').append(tpl4);
	$('#tag_4').append(tpl5);
	$('#setMenuLink').find('.sort').on('mouseover',function(){
		$('.sort').removeClass('sort-active');
		$(this).addClass('sort-active');
	});
	$('#setMenuLink').find('.sort').on('mouseout',function(){
		$('.sort').removeClass('sort-active');

	})
}
/*/*//*/*//*/*//*/*//*/*//*/*//*/*//*/*//*/*//*/*以下是具体业务js*//*/*//*/*//*/*//*/*//*/*//*/*//*/*//*/*//*/*//*/*//*/*/
var tableparam={};
var nweUrl = " ";
/***
 * 根据不同的选择项显示不同的列表
 */
function getUrlValueRequest(type,name,key,shopId){
	$('#text_pageToolbar').hide();
	memudialog.height(850);
	if(name=="商品分类"){
		// tableparam={
		// 	 url:CONTEXT_PATH+'/product/group/groups',
		// 	 pageSize:5,
		// 	 curPage:0,
		// 	 tabledata:[{
		// 	  title:"商品主图",
		// 	  width:"20%",
		// 	   type:"img",
		// 	   name:"group_image_path"
		// 	},{
		// 	  title:"分类名称",
		// 	  width:"20%",
		// 	   type:"data",
		// 	   name:"group_name"
		// 	},{
		// 	  title:"选择",
		// 	  width:"20%",
		// 	   type:"opt",
		// 	   name:"opt"
		// 	}]
		// };
		// var newurl = "/app/groupList/";
		// loadHtmlData(type,name,tableparam,newurl,key,shopId,true);
		var newurl = "/app/groupList/";
		_goods_classify_bar(type,name,newurl,key,shopId);
	}else if(name=="具体商品"){
		// tableparam={
		// 		 url:CONTEXT_PATH+'/good/productList/0',
		// 		 pageSize:5,
		// 		 curPage:0,
		// 		 tabledata:[{
		// 		  title:"商品名称",
		// 		  width:"25%",
		// 		   type:"data",
		// 		   name:"name"
		// 		},{
		// 		  title:"价格",
		// 		  width:"25%",
		// 		   type:"float",
		// 		   name:"price"
		// 		},{
		// 		  title:"库存",
		// 		  width:"25%",
		// 		   type:"data",
		// 		   name:"total_count"
		// 		},{
		// 		  title:"选择",
		// 		  width:"25%",
		// 		   type:"opt",
		// 		   name:"opt"
		// 		}]
		// 	};
		// var newurl = "/product/";
		// loadHtmlData(type,name,tableparam,newurl,key,shopId,true);
		var newurl = "/product/";
		_goods_definite_bar(type,name,newurl,key,shopId);
	}
	else if(name=="官方图文列表"){
		var newurl = "/imageText/";
		_itemtext_page_bar(type,name,newurl,key,shopId);
	}
	else if(name=="乐享图文列表"){
		var newurl = "/imageText/";
		_enjoytext_page_bar(type,name,newurl,key,shopId);
	}
	else if(name=="官方图文分类"){
		var newurl = "/shop/image_text_type/";
		_itemtext_page_bar1(type,name,newurl,key,shopId);
	}
	else if(name=="乐享图文分类"){
		var newurl = "/shop/image_text_type/";
		_enjoytext_page_bar1(type,name,newurl,key,shopId);
	}
	else if(name=="培训通知列表"){
		var newurl = "/imageText/";
		_enjoytext_page_bar2(type,name,newurl,key,shopId);
	}
	else if(name=="培训通知分类"){
		var newurl = "/shop/image_text_type/";
		_itemtext_page_bar2(type,name,newurl,key,shopId);
	}
}
 // function loadHtmlData(type,urlname,param,newurl,key,shopId,sign){
	//  	if(sign){
	//  	    $("#page").children().remove();
	//  	}
	//  	var tmpdata={};
	//  	tmpdata.pageSize=param.pageSize;
	//  	tmpdata.curPage=param.curPage;
	// 	var jsonStr = JSON.stringify(tmpdata);
	// 	$.ajaxJson(param.url,jsonStr,{
	// 		"done":function(res){
	// 			$("#products").empty();
	// 			if(urlname=="商品分类"){
	// 				productGroup(type,urlname,param,res,newurl,key,shopId);
	// 			}else if(urlname=="具体商品"){
	// 				productDetails(type,urlname,param,res,newurl,key,shopId);
	// 			}
	// 			if(sign){
	// 	 			 initPagination(param,param.count);
	// 	 		}
	// 		}
	// 	});
	//
	//  }
	 
	// var initPagination = function(param,count) {
	// 	$("<div>",{
	// 		"class":"pagination"
	// 	}).pagination(count, {
	// 		num_edge_entries: 1, //边缘页数
	// 		num_display_entries: 3, //主体页数
	// 		prev_text:"<li class='iconfont icon-left  '></li>",
	// 		next_text:"<li class='iconfont icon-right  '></li>",
	// 		callback: function(page_index, jq){
	// 			    param.curpage=page_index;
	// 			    loadHtmlData(param);
	// 			  return false;
	// 		},
	// 		items_per_page:10 //每页显示10项
	// 	}).appendTo("#page");
	//  };
		 
		 
	 /***
	  * 
	  * 商品分类
	  * @author liangrs
	  * 
	  */
	 // var productGroup = function(type,urlname,param,res,newurl,key,shopId){
		//  console.log(res);
		//  var tabledata= param.tabledata;
		// 	var jmtable=$("<div style='height:200px;overflow:auto' class='jm-table'>",{
		// 		"class":"jm-table"
		// 	}).appendTo("#products");
     //
		// 	var tableUl=$("<ul>",{
		// 		"class":"table-hander"
		// 	}).appendTo(jmtable);
     //
		// 	for(var i=0;tabledata.length>i;i++){
		// 		$("<li>").html("<span>"+tabledata[i].title+"</span>").appendTo(tableUl);
		// 	}
     //
		// var itemData = res.data.items;
		// for(var j=0;itemData.length>j;j++){
	 //    	 var contaiinerUl=$("<ul>",{
		// 			"class":"table-container"
		// 	 }).appendTo(jmtable);
     //
	 //    	for(var k=0;tabledata.length-1>k;k++){
	 //    		 if(tabledata[k].type=="img"){
	 //    				if(itemData[j][tabledata[k].name]=="null"||itemData[j][tabledata[k].name]==" "){
		//     				$("<li>").html('<img src="http://localhost:8080/msa/css/pc/img/no_picture.png" height="50" width="50" alt="">' ).appendTo(contaiinerUl);
		//     			}else{
		//     				$("<li>").html('<img src="'+itemData[j][tabledata[k].name]+'" height="50" width="50" alt="暂无图片">' ).appendTo(contaiinerUl);
		//     			}
	 //    		 }else if(tabledata[k].type=="data"){
	 //    			 $("<li>").html(itemData[j][tabledata[k].name] ).appendTo(contaiinerUl);
	 //    		 }
		// 	}
	 //    	//console.log(itemData[j]);
	 //     	//console.log("itemData[j].pid: "+itemData[j].group_id);
	 //    	$("<li>").html("<div class='radioBox'> <label onClick=\"setMemuLink('"+type+"','"+urlname+"','"+key+"','"+newurl+itemData[j].group_id+"','"+shopId+"')\"  ></label></div>").appendTo(contaiinerUl);
	 //    }
	 // };
	 var _goods_classify_bar=function(type,name,newurl,key,shopId){
		 var curl=CONTEXT_PATH+'/product/group/groups';//ajax请求的链接
		 var tmpdata={};
		 tmpdata.pageSize=5;
		 tmpdata.curPage=0;
		 $('#text_pageToolbar').show();
		 $("#page").children().remove();
		 jumi.pagination('#text_pageToolbar',curl,tmpdata,function(res,curPage){
               console.log(res);
			 if(res.code===0){
				 //判断是否第一页
				 var data = {
					 items:res.data.items
				 };
				 if(curPage===0){
					 data.isFirstPage = 1;
				 }else{
					 data.isFirstPage = 0;
				 }
				 for(var i=0;i<data.items.length;i++){
					 data.items[i].ultype=type;
					 data.items[i].ulurlname=name;
					 data.items[i].ulnewurl=newurl;
					 data.items[i].ulkey=key;
					 data.items[i].ulshopId=shopId;
				 }
				 jumi.template('menu/goodsclassify_list',data,function(tpl){
					 $('#products').empty();
					 $('#products').html(tpl);
				 })
			 }
		 })
	 }
		 
	 /***
	  * 
	  * 具体商品
	  * @author liangrs
	  * 
	  */
	 // var productDetails = function(type,urlname,param,res,newurl,key,shopId){
		//  var tabledata= param.tabledata;
		//  var jmtable=$("<div>",{
		// 		"class":"jm-table"
		// 	}).appendTo("#products");
		//
		// 	var tableUl=$("<ul>",{
		// 		"class":"table-hander"
		// 	}).appendTo(jmtable);
		//
		// 	for(var i=0;tabledata.length>i;i++){
		// 		$("<li>").html("<span>"+tabledata[i].title+"</span>").appendTo(tableUl);
		// 	}
		//
		// var itemData = res.data.items;
		// for(var j=0;itemData.length>j;j++){
	 //    	 var contaiinerUl=$("<ul>",{
		// 			"class":"table-container"
		// 	 }).appendTo(jmtable);
	 //
	 //    	for(var k=0;tabledata.length-1>k;k++){
	 //
	 //    	 	  if(tabledata[k].type=="float"){
	 //    			 $("<li>").html((parseFloat(itemData[j][tabledata[k].name])/100).toFixed(2) ).appendTo(contaiinerUl);
	 //
	 //    		 }else{
	 //    			 $("<li>").html(itemData[j][tabledata[k].name] ).appendTo(contaiinerUl);
	 //
	 //    		 }
	 //
		// 	}
	 //    	//console.log(itemData[j]);
	 //     	//console.log("itemData[j].group_id: "+itemData[j].pid);
	 //    	$("<li>").html("<div class='radioBox'> <label onClick=\"setMemuLink('"+type+"','"+urlname+"','"+key+"','"+newurl+itemData[j].pid+"','"+shopId+"')\"  ></label></div>").appendTo(contaiinerUl);
	 //    }
	 // };
	 var _goods_definite_bar=function(type,name,newurl,key,shopId){
		 var curl=CONTEXT_PATH+'/good/productList/0';//ajax请求的链接
		 var tmpdata={};
		 tmpdata.pageSize=5;
		 tmpdata.curPage=0;
		 $('#text_pageToolbar').show();
		 $("#page").children().remove();
		 jumi.pagination('#text_pageToolbar',curl,tmpdata,function(res,curPage){
			 if(res.code===0){
				 //判断是否第一页
				 var data = {
					 items:res.data.items
				 };
				 if(curPage===0){
					 data.isFirstPage = 1;
				 }else{
					 data.isFirstPage = 0;
				 }
				 // console.log(data);
				 for(var i=0;i<data.items.length;i++){
                     data.items[i].ulprice=(parseFloat(data.items[i].price)/100).toFixed(2);
					 data.items[i].ultype=type;
					 data.items[i].ulurlname=name;
					 data.items[i].ulnewurl=newurl;
					 data.items[i].ulkey=key;
					 data.items[i].ulshopId=shopId;
				 }

				 jumi.template('menu/definitegoods_list',data,function(tpl){
					 $('#products').empty();
					 $('#products').html(tpl);
				 })
			 }
		 })
	 }

////项目图文分类分页start
var _itemtext_page_bar1=function(type,name,newurl,key,shopId){
	var curl=CONTEXT_PATH + '/image_text_type/list/type_id/1';//ajax请求的链接
	var tmpdata={};
	tmpdata.pageSize=5;
	tmpdata.curPage=0;
	tmpdata.flag="Y";//上架
	$('#text_pageToolbar').show();
	$("#page").children().remove();
	jumi.pagination('#text_pageToolbar',curl,tmpdata,function(res,curPage){
		if(res.code===0){
			//判断是否第一页
			var data = {
				items:res.data.imageTextTypes
			};
			if(curPage===0){
				data.isFirstPage = 1;
			}else{
				data.isFirstPage = 0;
			}
			// console.log(data);
			for(var i=0;i<data.items.items.length;i++){
				data.items.items[i].ultype=type;
				data.items.items[i].ulurlname=name;
				data.items.items[i].ulnewurl=newurl;
				data.items.items[i].ulkey=key;
				data.items.items[i].ulshopId=shopId;
			}
			jumi.template('menu/itemtext_list1',data,function(tpl){
				$('#products').empty();
				$('#products').html(tpl);
			})
		}
	})
}
////乐享图文分类分页start
var _enjoytext_page_bar1=function(type,name,newurl,key,shopId){
	var curl=CONTEXT_PATH + '/image_text_type/list/type_id/2';//ajax请求的链接
	var tmpdata={};
	tmpdata.pageSize=5;
	tmpdata.curPage=0;
	tmpdata.flag="Y";//上架
	$('#text_pageToolbar').show();
	$("#page").children().remove();
	jumi.pagination('#text_pageToolbar',curl,tmpdata,function(res,curPage){

		if(res.code===0){
			//判断是否第一页
			var data = {
				items:res.data.imageTextTypes
			};
			if(curPage===0){
				data.isFirstPage = 1;
			}else{
				data.isFirstPage = 0;
			}
			// console.log(data);
			for(var i=0;i<data.items.items.length;i++){
				data.items.items[i].ultype=type;
				data.items.items[i].ulurlname=name;
				data.items.items[i].ulnewurl=newurl;
				data.items.items[i].ulkey=key;
				data.items.items[i].ulshopId=shopId;
			}
			jumi.template('menu/enjoytext_list1',data,function(tpl){
				$('#products').empty();
				$('#products').html(tpl);
			})
		}
	})
}

////项目图文列表分页start
var _itemtext_page_bar=function(type,name,newurl,key,shopId){
	var curl=CONTEXT_PATH + '/image_text/findAll/1';//ajax请求的链接
	var tmpdata={};
	tmpdata.pageSize=5;
	tmpdata.curPage=0;
	tmpdata.flag="Y";//上架
	$('#text_pageToolbar').show();
	$("#page").children().remove();
	jumi.pagination('#text_pageToolbar',curl,tmpdata,function(res,curPage){

		if(res.code===0){
			//判断是否第一页
			var data = {
				items:res.data.items
			};
			if(curPage===0){
				data.isFirstPage = 1;
			}else{
				data.isFirstPage = 0;
			}
			// console.log(data);
			for(var i=0;i<data.items.length;i++){
				data.items[i].ultype=type;
				data.items[i].ulurlname=name;
				data.items[i].ulnewurl=newurl;
				data.items[i].ulkey=key;
				data.items[i].ulshopId=shopId;
			}
			jumi.template('menu/itemtext_list',data,function(tpl){
				$('#products').empty();
				$('#products').html(tpl);
			})
		}
	})
}
////乐享图文列表分页start
var _enjoytext_page_bar=function(type,name,newurl,key,shopId){
	var curl=CONTEXT_PATH + '/image_text/findAll/2';//ajax请求的链接
	var tmpdata={};
	tmpdata.pageSize=5;
	tmpdata.curPage=0;
	tmpdata.flag="Y";//上架
	$('#text_pageToolbar').show();
	$("#page").children().remove();
	jumi.pagination('#text_pageToolbar',curl,tmpdata,function(res,curPage){

		if(res.code===0){
			//判断是否第一页
			var data = {
				items:res.data.items
			};
			if(curPage===0){
				data.isFirstPage = 1;
			}else{
				data.isFirstPage = 0;
			}
			// console.log(data);
			for(var i=0;i<data.items.length;i++){
				data.items[i].ultype=type;
				data.items[i].ulurlname=name;
				data.items[i].ulnewurl=newurl;
				data.items[i].ulkey=key;
				data.items[i].ulshopId=shopId;
			}
			jumi.template('menu/enjoytext_list',data,function(tpl){
				$('#products').empty();
				$('#products').html(tpl);
			})
		}
	})
}

////培训通知分类分页start
var _itemtext_page_bar2=function(type,name,newurl,key,shopId){
	var curl=CONTEXT_PATH + '/image_text_type/list/type_id/3';//ajax请求的链接
	var tmpdata={};
	tmpdata.pageSize=5;
	tmpdata.curPage=0;
	tmpdata.flag="Y";//上架
	$('#text_pageToolbar').show();
	$("#page").children().remove();
	jumi.pagination('#text_pageToolbar',curl,tmpdata,function(res,curPage){
		if(res.code===0){
			//判断是否第一页
			var data = {
				items:res.data.imageTextTypes
			};
			if(curPage===0){
				data.isFirstPage = 1;
			}else{
				data.isFirstPage = 0;
			}
			// console.log(data);
			for(var i=0;i<data.items.items.length;i++){
				data.items.items[i].ultype=type;
				data.items.items[i].ulurlname=name;
				data.items.items[i].ulnewurl=newurl;
				data.items.items[i].ulkey=key;
				data.items.items[i].ulshopId=shopId;
			}
			jumi.template('menu/itemtext_list1',data,function(tpl){
				$('#products').empty();
				$('#products').html(tpl);
			})
		}
	})
}

////培训通知列表分页start
var _enjoytext_page_bar2=function(type,name,newurl,key,shopId){
	var curl=CONTEXT_PATH + '/image_text/findAll/3';//ajax请求的链接
	var tmpdata={};
	tmpdata.pageSize=5;
	tmpdata.curPage=0;
	tmpdata.flag="Y";//上架
	$('#text_pageToolbar').show();
	$("#page").children().remove();
	jumi.pagination('#text_pageToolbar',curl,tmpdata,function(res,curPage){

		if(res.code===0){
			//判断是否第一页
			var data = {
				items:res.data.items
			};
			if(curPage===0){
				data.isFirstPage = 1;
			}else{
				data.isFirstPage = 0;
			}
			for(var i=0;i<data.items.length;i++){
				data.items[i].ultype=type;
				data.items[i].ulurlname=name;
				data.items[i].ulnewurl=newurl;
				data.items[i].ulkey=key;
				data.items[i].ulshopId=shopId;
			}
			jumi.template('menu/enjoytext_list',data,function(tpl){
				$('#products').empty();
				$('#products').html(tpl);
			})
		}
	})
}
////乐享图文分页end
