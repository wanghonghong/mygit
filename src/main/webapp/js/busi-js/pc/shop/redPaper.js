//author pr
//time 2016-6-27
//发送红包任务模块
//注意要求!
//红包金额只支持到个位数
//百分比只支持个位数不支持小数点
//单人红包金额限定小数点2位,最大数值不超过分段金额
//中奖人数到个位数

var redPaperAction = function(){};
redPaperAction.prototype = {
	openButton:true,
	isSubmitServer:true,//是否可提交服务器
	percentCount:100,//百分比之和
	percentSum:0,
	ajaxConfig:{
		'addRedAction':CONTEXT_PATH+'/activity/addRedPackActivity',
		'renderRedAction':CONTEXT_PATH+'/activity/showActivity',
		'updateAction' :CONTEXT_PATH+'/activity/updateRedPackActivity'
	},
	render:function(){
		this._onEvent();
		this._validateForm();
		this._initAction();
	},
	_initAction:function () {

		var self = this;
		var url = this.ajaxConfig.renderRedAction;
		$("#txt").val("：（\n您的手气还不够旺哦，本轮红包木有抢到！\n不要灰心，下周同一时间还有1场红包活动:)");
		$.ajaxJsonGet(url,{
			"done":function (res) {
				if(res.code===0){

					if(res.data.code===0){

						$(document).find('input[name="edit"]').val(1);
						self._fillInputData(res.data.data);
					}
					if(res.data.code===2){
						self._fillInputData(res.data.data);
						$('#saveButton').hide();
					}
				}
			},
			"fail":function (res) {

			}

		});
	},
	_fillInputData:function (data) {

		var json = data;

		var jsonDataActivity = json.activity;
		var jsonDataActivityList = json.subActivityList;
		$('#actionName').val(jsonDataActivity.activityName);
		$('#blessingName').val(jsonDataActivity.blessings);
		$('#red_amount').val(jsonDataActivity.totalMoney/100);
		$('#amount_num').text(jsonDataActivity.yjFunsCount);

		$('#p_appid').val(jsonDataActivity.appid);
		$('#activity_id').val(jsonDataActivity.id);
		$('#activity_status').val(jsonDataActivity.status);
		$('#activity_shopId').val(jsonDataActivity.shopId);
		$('input[name="start"]').val(jsonDataActivity.startDt);
		$('input[name="over"]').val(jsonDataActivity.endDt);
		$('#txt').val(jsonDataActivity.noWinInfo);
		$.each(jsonDataActivityList,function(i){

			var j = i+1;

			var cm = jsonDataActivityList[i].consumeMoney/100;
			var single = jsonDataActivityList[i].singleRedPacket/100;
			var activity_id = "activity_id_"+j;
			var appid_id = "appid_"+j;
			var id = "id_"+j;
			var status_id = "status_"+j;
			var shopId_id = "shopId_"+j;
			$("#"+activity_id).val(jsonDataActivityList[i].activityId);
			$("#"+appid_id).val(jsonDataActivityList[i].appid);
			$("#"+id).val(jsonDataActivityList[i].id);
			$("#"+status_id).val(jsonDataActivityList[i].status);
			$("#"+shopId_id).val(jsonDataActivityList[i].shopId);
			$('input[data-id^="percent_'+j+'"]').val(jsonDataActivityList[i].winScale);
			$('li[data-id^="consume_'+j+'"]').text(cm);
			$('input[data-id^="single_'+j+'"]').val(single);
			$('li[data-id^="winners_'+j+'"]').text(jsonDataActivityList[i].winCount);
			$('li[data-id^="no_winning_'+j+'"]').text(jsonDataActivityList[i].noWinCount);
			$('select[data-id^="percentage_'+j+'"').find("option[value='"+jsonDataActivityList[i].noWinScale+"']").attr("selected",true);
		})
	},
	_onEvent:function(){
		var self = this;
		
		//总人数设置重新计算
		$(document).on('blur','#red_amount',function(){
			var amount = $(this).val()||0;
			self._calculate_amount(amount);
			self._calculate_winners(amount);
			self._calculate_no_winners(amount);
			self._calculate_fans();
		});
		//获奖百分比设置重新计算
		$(document).on('blur','input[data-id^="percent_"]',function(){
			var amount = $('#red_amount').val()||0;
			self._calculate_amount(amount);
			self._calculate_winners(amount);
			self._calculate_no_winners(amount);
			self._calculate_fans();
		});
		//
		$(document).on('blur','input[data-id^="single_"]',function(){
			var amount = $('#red_amount').val()||0;
			self._calculate_amount(amount);
			self._calculate_winners(amount);
			self._calculate_no_winners(amount);
			self._calculate_fans();
		});
		$(document).on('change','select[data-id^="percentage_"]',function(){
			var amount = $('#red_amount').val()||0;
			self._calculate_amount(amount);
			self._calculate_winners(amount);
			self._calculate_no_winners(amount);
			self._calculate_fans();
		});
		$(document).off('click').on('click','#saveButton',function(){
			var edit = $(document).find('input[name="edit"]').val();
			if(edit==1){
				self._ajaxEditServer();
			}else{
				self._ajaxModeServer();//不处于编辑状态的提交
			}

		})
		
	},
	_offEvent:function(){
		$(document).off('blur','#red_amount');
		$(document).off('blur','input[data-id^="percent_"]');
		$(document).off('change','select[data-id^="percentage_"]');
		$(document).off('click','#saveButton');
	},
	
	//重新计算消费金额
	_calculate_amount:function(amount){
		$('li[data-id^="consume_"]').each(function(i){
			 var k = i+1;
			 var v = $('input[data-id^="percent_'+k+'"]').val();
			 var val = (v==='')?'-':parseInt(v.replace('%',''),10)/100;
			 var consumeAmount = (amount*val===0||isNaN(amount*val))?'-':parseInt(amount*val,10);
			 $(this).text(consumeAmount);
		})	
	},
	//计算中奖人数
	_calculate_winners:function(amount){
		$('li[data-id^="winners_"]').each(function(i){
			var k = i+1;
			var v = $('input[data-id^="percent_'+k+'"]').val();
			var val = parseInt(v.replace('%',''),10)/100;		
			var consumeAmount = amount*val;	
			var singleAmount = Number($('input[data-id^="single_'+k+'"]').val());//单个红包金额
			var winnersAmount = (consumeAmount/singleAmount === Infinity||isNaN(consumeAmount/singleAmount)||consumeAmount/singleAmount === 0) ? '-':parseInt(consumeAmount/singleAmount,10);
			$(this).text(winnersAmount);
		})
	},
	//计算不中奖人数
	_calculate_no_winners:function(amount){
		$('li[data-id^="no_winning_"]').each(function(i){
			var k = i+1;
			var v = $('input[data-id^="percent_'+k+'"]').val();
			var val = parseInt(v.replace('%',''),10)/100;		
			var consumeAmount = amount*val;
			var singleAmount = Number($('input[data-id^="single_'+k+'"]').val());//单个红包金额
			var winnersAmount = (consumeAmount/singleAmount === Infinity||isNaN(consumeAmount/singleAmount)||consumeAmount/singleAmount === 0) ? '-':parseInt(consumeAmount/singleAmount,10);		
			var PercentageValue = $('select[data-id^="percentage_'+k+'"]').find("option:selected").val();				
			var no_winnersAmount = (PercentageValue===''||winnersAmount==='-')?'-':parseInt(PercentageValue*winnersAmount,10);
			$(this).text(no_winnersAmount);
		})
	},
	//计算粉丝总人数
	_calculate_fans:function(){
		var self = this;
		var fansCount = 0;
		$('li[data-id^="winners_"]').each(function(i){
			var value = $(this).text();
			var val = (value==='NaN'||value==='-')?0:value;
			fansCount = fansCount+Number(val);
		});
		$('li[data-id^="no_winning_"]').each(function(i){
			var value = $(this).text();
			var val = (value==='NaN'||value==='-')?0:value;
			fansCount = fansCount+Number(val);
		});
		if(!isNaN(fansCount)){
			$('#amount_num').text(fansCount);
		}
	},
	//编辑数据采集给服务端
	_ajaxEditServer:function () {
		this._validateForm();
		var url = this.ajaxConfig.updateAction;
		var txt = $('#txt').val();
		var startTime = $('input[name="start"]').val();
		var overTime = $('input[name="over"]').val();
		var active = $('#actionName').val();
		var blessing = $('#blessingName').val();

		$('.error').hide();
		if(!startTime || !overTime){
			$('.error').show();
			$(document).scrollTop(0);
			$('#tipError').text('活动时间未设定,请重新设置');
			return;
		}

		if(this.percentSum!==this.percentCount){
			$('.error').show();
			$(document).scrollTop(0);
			$('#tipError').text('设置百分比有误,请重新设置');
			return;
		}
		if(!txt){
			$('.error').show();
			$(document).scrollTop(0);
			$('#tipError').text('请填写未中奖推送语');
			return;
		}
		if(!blessing||!active){
			$('.error').show();
			$(document).scrollTop(0);
			$('#tipError').text('活动名称或者祝福语未设定,请重新设置');
			return;
		}
		var activityVo ={};
		var activity = {};
		var typeArray = [];
		activity.yjFunsCount = $('#amount_num').text();
		activity.id = $("#activity_id").val();
		activity.appid = $("#p_appid").val();
		activity.shopId = $("#activity_shopId").val();
		activity.noWinInfo = txt;
		activity.startDt = startTime;
		activity.status = $("#activity_status").val();
		activity.endDt = overTime;
		activity.typeId = 1;
		activity.blessings = blessing;
		activity.activityName = active;
		activity.totalMoney = Number($('#red_amount').val())*100;
		for(var i=1;i<6;i++){
			var subActivity = {};
			var activity_id = "activity_id_"+i;
			var appid_id = "appid_"+i;
			var id = "id_"+i;
			var status_id = "status_"+i;
			var shopId_id = "shopId_"+i;
			var parent = $('input[data-id^="percent_'+i+'"]');
			subActivity.winScale = $('input[data-id^="percent_'+i+'"]').val().replace('%','');
			subActivity.consumeMoney = Number($('li[data-id^="consume_'+i+'"]').text())*100;
			subActivity.singleRedPacket = Number($('input[data-id^="single_'+i+'"]').val())*100;
			subActivity.winCount = $('li[data-id^="winners_'+i+'"]').text();
			subActivity.noWinScale = $('select[data-id^="percentage_'+i+'"]').find("option:selected").val();
			subActivity.noWinCount = $('li[data-id^="no_winning_'+i+'"]').text();
			subActivity.seq = i;
			subActivity.startDt = startTime;
			subActivity.endDt = overTime;
			subActivity.id = $("#"+id).val();
			subActivity.appid =  $("#"+appid_id).val();
			subActivity.status =  $("#"+status_id).val();
			subActivity.shopId =  $("#"+shopId_id).val();
			subActivity.activityId = $("#"+activity_id).val();
			typeArray.push(subActivity);
		}
		var flag = this._isFill(typeArray);
		if(flag){

			activityVo.activity = activity;
			activityVo.subActivityList =typeArray;
			var jsonData = JSON.stringify(activityVo);

			$.ajaxJson(url,jsonData,{
				"done":function (res) {
					alert(res.data.msg);
				},
				"fail":function (res) {
					alert(res.data.msg);
				}

			});


		}else{
			$('.error').show();
			$(document).scrollTop(0);
			$('#tipError').text('数据未填写完整,请重新设置');

		}

	},
	//数据采集给服务端
	_ajaxModeServer:function(){
		this._validateForm();
		var url = this.ajaxConfig.addRedAction;
		var txt = $('#txt').val();
		var startTime = $('input[name="start"]').val();
		var overTime = $('input[name="over"]').val();
		var active = $('#actionName').val();
		var blessing = $('#blessingName').val();
		$('.error').hide();
		if(!startTime || !overTime){
			$('.error').show();
			$(document).scrollTop(0);
			$('#tipError').text('活动时间未设定,请重新设置');
			return;
		}

		if(this.percentSum!==this.percentCount){
			$('.error').show();
			$(document).scrollTop(0);
			$('#tipError').text('设置百分比有误,请重新设置');
			return;
		}
		if(!txt){
			$('.error').show();
			$(document).scrollTop(0);
			$('#tipError').text('请填写未中奖推送语');
			return;
		}
		if(!blessing||!active){
			$('.error').show();
			$(document).scrollTop(0);
			$('#tipError').text('活动名称或者祝福语未设定,请重新设置');
			return;
		}
		var activityVo ={};
		var activity = {};		
		var typeArray = [];
		activity.yjFunsCount = $('#amount_num').text();
		activity.noWinInfo = txt;
		activity.startDt = startTime;
		activity.endDt = overTime;
		activity.typeId = 1;
		activity.blessings = blessing;
		activity.activityName = active;
		activity.totalMoney = Number($('#red_amount').val())*100;
		for(var i=1;i<6;i++){
			var subActivity = {};
			subActivity.winScale = $('input[data-id^="percent_'+i+'"]').val().replace('%','');
			subActivity.consumeMoney = Number($('li[data-id^="consume_'+i+'"]').text())*100;
			subActivity.singleRedPacket = Number($('input[data-id^="single_'+i+'"]').val())*100;
			subActivity.winCount = $('li[data-id^="winners_'+i+'"]').text();
			subActivity.noWinScale = $('select[data-id^="percentage_'+i+'"]').find("option:selected").val();
			subActivity.noWinCount = $('li[data-id^="no_winning_'+i+'"]').text();
			subActivity.seq = i;
			subActivity.startDt = startTime;
			typeArray.push(subActivity);
		}
		var flag = this._isFill(typeArray);
		if(flag){
			activityVo.activity = activity;
			activityVo.subActivityList =typeArray;
			var jsonData = JSON.stringify(activityVo);
			$.ajaxJson(url,jsonData,{
				"done":function (res) {
					alert(res.data.msg);
				},
				"fail":function (res) {
					alert(res.data.msg);
				}

			});


		}else{
			$('.error').show();
			$(document).scrollTop(0);
			$('#tipError').text('数据未填写完整,请重新设置');

		}
	},
	//表单输入验证 判断方式正则表达式
	_validateForm:function(){
		var self = this;
		this.percentSum = 0;
		//验证红包金额限定个位数
		$('#red_amount').on('keypress',function(event){
			var value = $(this).val();		
			if((event.keyCode<48 || event.keyCode>57)|| /\D/g.test(value)){			
				event.preventDefault();
			}
		});
		//设置百分比必须是数字
		$('input[data-id^="percent_"]').on('keypress',function(){
			var value = $(this).val();
			if((event.keyCode<48 || event.keyCode>57) && event.keyCode!=37 || /\.\d\d$/.test(value)){			
				event.preventDefault();
			}
		});
		//单人红包金额保留两位小数
		$('input[data-id^="single_"]').on('keypress',function(event){
			var value = $(this).val();
			if((event.keyCode<48 || event.keyCode>57) && event.keyCode!=46 || /\.\d\d$/.test(value)){
				event.preventDefault();
			}
		});
		//验证百分比相加是否为100%
		$('input[data-id^="percent_"]').each(function(){
			var value = $(this).val();
			v = Number(value.replace('%',''));		
			self.percentSum = self.percentSum+v
		})

	},
	_isFill:function(arrayData){
		var flag = true;
		$.each(arrayData,function(k,v){
			for(i in v){
				if(v[i]===''||v[i]==='-'){
					flag = false;
					return true;
				}
			}
		});
		return flag;
	},
	dispose:function(){
		this._offEvent();
	}
};
var redpaper = new redPaperAction();
redpaper.render();
