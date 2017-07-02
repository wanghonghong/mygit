/**
 * Created by chenyy on 2016/12/08
 */
CommonUtils.regNamespace("juke", "jukeRed");

//请求订单状态，判断是否已经支付成功
var _ajaxstatus = function () {
	var orderInfoId = $("#orderInfoId").val();
	var ajaxStatusUrl = CONTEXT_PATH+ "/jk/order_status/"+orderInfoId;
	$.ajax({
		url:ajaxStatusUrl,
		type:"GET",
		dataType:"json",
		success:function(res){
			if(res.code==0){
				if(res.msg==1){
					 window.location.reload();
				}
			}
	        //请求成功时处理
	    }
	});

};

juke.jukeRed = (function(){
	var percentCount = 100;
	var percentSum = 0;
	var ajaxUrl = {
		url1:CONTEXT_PATH+'/activity/',  // 新增红包活动接口
		url2:CONTEXT_PATH+'/activity/list',//查询活动列表
		url3:CONTEXT_PATH+'/activity/user_count'//粉丝数量统计
	};
	var _init = function(){
		_getAccountHtml();
	};
	
	var timer1 ;
	//公众号列表
	var _getAccountHtml = function() {
		   var data = {};
	        var jklisturl = CONTEXT_PATH + "/jk/list";
	        $.ajaxJsonGet(jklisturl, null, {
	            "done": function (res) {
	            	data.account_list = res.data;
					data.CONTEXT_PATH = CONTEXT_PATH;
	                jumi.template('jk/jk_account_list',data,function(tpl){
	                    $('#account_list').empty();
	                    $('#account_list').html(tpl);
	                });

	            }
	        });
	 };

	 
	 //充值记录列表
   var _getrechargeHtml = function(){
	   var jmRechargeOrderQo = {};
	   jmRechargeOrderQo.pageSize = 10;
	   var jklisturl = CONTEXT_PATH + "/jk/recharge_list";
       jumi.pagination('#rechargePaperToolbar',jklisturl,jmRechargeOrderQo,function(res,curPage){
           if(res.code===0){
               var data = {
                   items:res.data.items
               };
               jumi.template('jk/jk_recharge_list',data,function(tpl){
                   $('#jk_recharg_list').html(tpl);
               });
           }
       });
   };
	//判断是否满足数据提交要求
	var _isFillFull = function(arrayData){
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
	};
	var _modify = function(){

	}
	var _watch = function(id,type){
		var url = ajaxUrl.url1+id;
		$.ajaxJsonGet(url,null,{
			"done":function(res){
				if(res.code===0){
					var data = res.data;
					jumi.template('jk/jk_account_c_redPaper?type='+type+'&edit=0',data,function (tpl) {
						var d = dialog({
							title: '查看红包活动',
							content:tpl,
							width:820,
							onshow:function(){
								var dtd = $.Deferred();
								var wait = function(dtd){
									var tasks = function(){
										dtd.reject();
									};
									return dtd;
								};
								$.when(
									wait(

									)).done(function(){
									_fillData(data);
								})
								$('#activity_ensure_button').hide();
							}
						});
						d.showModal();
					})
				}
			}
		})
	}

	var _fillData = function(data){
		var array = [];
		var tpl = '';
		var flag = $('input[name="edit"]').val();
		$('#red_amount').val(data['totalMoney']/100);
		$('#actionName').val(data['activityName']);
		$('#blessingName').val(data['blessings']);
		$('#amount').text(data['preFansCount']);
		$('#fansNum').val(data['preFansCount']);
		$('input[name="activity_space"][value="'+data['platform']+'"]').prop('checked',true);
		if(flag!=2){
			$('#startTime').val(data['startTime']);
			$('#overTime').val(data['endTime']);
		}
		$('#activity_amount_num').text(data['preFansCount']);
		$('input[name="activityId"]').val(data.id);
		//金额设置
		$.each(data.activitySubList,function(i){
			var j = i+1;
			var cm = data.activitySubList[i].consumeMoney/100;
			var single = data.activitySubList[i].redMoney/100;
			$('input[data-id^="percent_'+j+'"]').parent().find(":hidden").attr('id',data.activitySubList[i].id);
			$('input[data-id^="percent_'+j+'"]').val(data.activitySubList[i].winScale);
			$('li[data-id^="consume_'+j+'"]').text(cm);
			$('input[data-id^="single_'+j+'"]').val(single);
			$('li[data-id^="winners_'+j+'"]').text(data.activitySubList[i].winCount);
		})
		//高级设置
		$('input[name="activity_sex"][value="'+data['activityCondition']['sex']+'"]').prop('checked',true);
		if(data['activityCondition']['areaLimit']==1){
			$('input[name="activity_area"]').prop('checked',true);
		}else{
			$('input[name="activity_area"]').prop('checked',false);
		}

		if(data['activityCondition']['roleLimit']==1){
			$('input[name="activity_role"]').prop('checked',true);
		}else{
			$('input[name="activity_role"]').prop('checked',false);
		}
		if(data['activityCondition']['downRoleLimit']==1){
			$('input[name="activity_downs_role"]').prop('checked',true);
		}else{
			$('input[name="activity_downs_role"]').prop('checked',false);
		}
		$('input[name="activity_area_ids"]').val(data['activityCondition']['areaIds']);
		$('input[name="activity_area_name"]').val(data['activityCondition']['areaNames']||'');
		if(data['activityCondition']['areaNames']){
			var names = data['activityCondition']['areaNames'].split(',');
			_.map(names,function(k,v){
				tpl+='<div class="u-btn-smltgry u-btn-smltgry-1"><span>'+k+'</span></div>';
			})
			$('#choose_activity_areas').append(tpl);
		}
		if(data.subType===3){
			$('input[name="activity_shop"]').val(data['activityCondition']['buyMoney']/100);
		}
		if(data.subType!=1){
			if(data['activityCondition']['roles']!=null){
				array = data['activityCondition']['roles'].split(',');
				_fillFoles(array);
			}
		}else{
			if(data['activityCondition']['downRoles']!=null){
				array = data['activityCondition']['downRoles'].split(',');
				_fillFoles(array);
			}
		}
	}
	var _computing = function(){
		//总人数设置重新计算
		$('#red_amount').unbind('blur').bind('blur',function(){
			var amount = $(this).val()||0;
			$('#amount').text(amount);
			_calculate_amount(amount);
			_calculate_winners(amount);
			_calculate_fans();
		});
		//获奖百分比设置重新计算
		$('input[data-id^="percent_"]').unbind('blur').bind('blur',function(){
			var amount = $('#red_amount').val()||0;
			_calculate_amount(amount);
			_calculate_winners(amount);
			_calculate_fans();
		});
		$('input[data-id^="single_"]').unbind('blur').bind('blur',function(){
			var obj = $(this);
			var index = obj.data('index');
			var ang = /^((?!0)\d+(\.\d{0,2})?)$/;
			var value = obj.val();//输入框的值
			var single_consume_value = $('li[data-id^="consume_'+index+'"]').text();//分配金额大小
			if(Number(single_consume_value)<Number(value)&&single_consume_value){
				var id = 'single_'+index;
				jumi.tipDialog(id,'单人红包的价格不能大于消费金额');
			}else if(!ang.test(value)&&single_consume_value){
				var id = 'single_'+index;
				jumi.tipDialog(id,'单人红包的价格不能小于1元');
			}
			var amount = $('#red_amount').val()||0;
			_calculate_amount(amount);
			_calculate_winners(amount);
			_calculate_fans();
		});


	}


	//计算中奖人数
	var _calculate_winners = function(amount){
		$('li[data-id^="winners_"]').each(function(i){
			var k = i+1;
			var v = $('input[data-id^="percent_'+k+'"]').val();
			var val = parseInt(v.replace('%',''),10)/100;
			var consumeAmount = amount*val;
			var singleAmount = Number($('input[data-id^="single_'+k+'"]').val());//单个红包金额
			var winnersAmount = (consumeAmount/singleAmount === Infinity||isNaN(consumeAmount/singleAmount)||consumeAmount/singleAmount === 0) ? '-':parseInt(consumeAmount/singleAmount,10);
			$(this).text(winnersAmount);
		})
	};
	//清空单人红包金额和中奖人数
	var _clearRedPackets = function(){
		$('input[data-id^="single_"]').each(function(){
			var element = $(this);
			element.val('');
		});
		$('li[data-id^="winners_"]').each(function(){
			var element = $(this);
			element.text('-');
		});
	};
	//计算粉丝总人数
	var _calculate_fans = function(){
		var fansCount = 0;
		$('li[data-id^="winners_"]').each(function(i){
			var value = $(this).text();
			var val = (value==='NaN'||value==='-')?0:value;
			fansCount = fansCount+Number(val);
		});
		if(!isNaN(fansCount)){
			$('#activity_amount_num').text(fansCount);
		}
	};

	//重新计算消费金额
	var _calculate_amount = function(amount){
		$('li[data-id^="consume_"]').each(function(i){
			var k = i+1;
			var v = $('input[data-id^="percent_'+k+'"]').val();
			var val = (v==='')?'-':parseInt(v.replace('%',''),10)/100;
			var consumeAmount = (amount*val===0||isNaN(amount*val))?'-':parseInt(amount*val,10);
			$(this).text(consumeAmount);
		})
	};

	//红包tab函数调用
	var _queryList = function(status){
		switch(status)
		{
			case 0:
				_createActivitylist();
				break;
			case 1:
				_queryActivityList(1);
				break;
			case 2:
				_queryActivityList(0);
				break;
			case 3:
				_queryActivityList(3);
			default:

		}
	};
	//创建红包活动
	var _createActivitylist = function(){
		jumi.template('jk/jk_account_create',function(tpl){
			$('#jm_div').empty();
			$('#jm_div').html(tpl);
		})
	}
	//创造活动
	var _createActivity = function(appid){
		var sessionUrl = CONTEXT_PATH + "/jk/account_session";
		var wxPubAccountVo={};
		wxPubAccountVo.appid=appid;
		  $.ajaxJson(sessionUrl, wxPubAccountVo, {
	            "done": function (res) {
	                if (res.code === 0) {
	                	jumi.template('jk/jk_manage_index',function(tpl){
	            			$('#shopIndexContent').empty();
	            			$('#shopIndexContent').html(tpl);
							_queryList(0);
							_bind();
	            		});
	                } else {
	                }
	            },
	            "fail": function (res) {
	            }
	        });

	}
	//已经在进行中的活动
	var _queryActivityList = function(redType){
		var activityQo = {},types;
		activityQo.pageSize = 10;
		activityQo.type = 5;
		if(redType===1){
			activityQo.status = '1,2';
		}else{
			activityQo.status = redType;
		}
		jumi.pagination('#pageRedPaperToolbar',ajaxUrl.url2,activityQo,function(res,curPage){
			if(res.code===0){
				var data = {
					items:res.data.items
				};
				jumi.template('jk/jk_manage_list?status='+redType,data,function(tpl){
					$('#jm_div').empty();
					$('#jm_div').html(tpl);
				})
			}
		})
	}
	//红包数据采集提交服务端
	var _doModelSubmit = function () {
		$('#error').hide();
		var activityCo = {};
		var activityConditionCo = {};
		var activity = {};
		var typeArray = [];
		activityCo.platform = $('input[name="activity_space"]:checked').val();
		activityCo.blessings = $('#blessingName').val();
		activityCo.activityName = $('#actionName').val();
		activityCo.totalMoney = Number($('#red_amount').val())*100;
		activityCo.noWinInfo = $('#activity_nowinning_txt').val();
		activityCo.winInfo = $('#activity_winning_txt').val();
		activityCo.startTime = $('#startTime').val();
		activityCo.endTime = $('#overTime').val();
		activityCo.type = 5;
		activityCo.shakeCount = $('input[name="yaoy"]:checked').val();
		activityCo.subType = $('input[name="subType"]').val();
		activityConditionCo.roleLimit = 0;
		activityCo.status = $('input[name="subStatus"]').val();
		//高级设置
		activityConditionCo.sex = $('input[name="activity_sex"]:checked').val()||0;
		activityCo.preFansCount = $('#activity_amount_num').text();
		if($('input[name="activity_area"]').is(":checked")){
			activityConditionCo.areaLimit = 1;
		}else{
			activityConditionCo.areaLimit = 0;
		}
		activityConditionCo.areaNames = $('input[name="activity_area_name"]').val();
		activityConditionCo.areaIds = $('input[name="activity_area_ids"]').val();
		activityConditionCo.buyMoney = Number($('input[name="activity_shop"]').val()*100);
		if(!activityCo.activityName){
			_error('活动名称未设定,请重新设置');
			return;
		}

		if(!activityCo.platform){
			_error('活动平台未选择');
			return;
		}
		//验证百分比相加是否为100%
		$('input[data-id^="percent_"]').each(function(){
			var value = $(this).val();
			v = Number(value);
			percentSum = percentSum+v
		});
		if(percentSum!==percentCount){
			_error('设置百分比有误,请重新设置');
			percentSum = 0;//重置
			return;
		}else{
			percentSum = 0;//重置
		}
		if(!activityCo.startTime || !activityCo.endTime){
			_error('活动时间未设定,请重新设置');
			return;
		}
		for(var i=1;i<5;i++){
			var SubActivity = {};
			SubActivity.winScale = $('input[data-id^="percent_'+i+'"]').val();
			SubActivity.consumeMoney = Number($('li[data-id^="consume_'+i+'"]').text())*100;
			SubActivity.redMoney = Number($('input[data-id^="single_'+i+'"]').val())*100;
			SubActivity.winCount = $('li[data-id^="winners_'+i+'"]').text();
			SubActivity.seq = i;
			typeArray.push(SubActivity);
		}
		activityCo.activityConditionCo = activityConditionCo;
		activityCo.subActivityList =typeArray;
		var flag = _isFillFull(typeArray);
		var jsonData = JSON.stringify(activityCo);
		var status = $('#activity_tab').find('.z-sel').data('index');
		if(flag){
			$.ajaxJson(ajaxUrl.url1,jsonData,{
				"done":function (res) {
					if(res.code===0){
						if(res.data.code===1){
							var dm = new dialogMessage({
								type:2,
								fixed:true,
								msg:res.data.msg,
								isAutoDisplay:true,
								time:3000
							});
							dm.render();
						}else{
							var dm = new dialogMessage({
								type:1,
								fixed:true,
								msg:res.data.msg,
								isAutoDisplay:true,
								time:3000
							});
							dm.render();
							dialog.get('createDialogAction').close().remove();
							_queryList(status);
						}
					}else{
						var dm = new dialogMessage({
							type:2,
							fixed:true,
							msg:res.data.msg,
							isAutoDisplay:true,
							time:3000
						});
						dm.render();
					}
				},
				"fail":function (res) {
					var dm = new dialogMessage({
						type:2,
						fixed:true,
						msg:res.data.msg,
						isAutoDisplay:true,
						time:3000
					});
					dm.render();
				}

			});
		}else{
			_error('数据未填写完整,请重新设置');
		}
	}
	//错误显示
	var _error = function(msg){
		$('#error').show();
		$('body').scrollTop(0);
		$('#tipError').text(msg);
	}
	//创建充值订单,调用支付接口返回支付二维码
	var _create_order = function(){
		  var ajaxUrl = CONTEXT_PATH + "/jk/recharge_order";
		  var orderCo ={};
		  orderCo.money = _getRechargeMoney()*100;
		  orderCo.type=0;
		  $.ajaxJson(ajaxUrl, orderCo, {
	            "done": function (res) {
	                if (res.code === 0) {
	                	_showRechargeDiglog(res.data.msg);
	                } else {
	                }
	            },
	            "fail": function (res) {
	            }
	        });
	};

	//时间空间渲染函数
	var _timeTimepicker = function () {
		$("#startTime").datetimepicker({
			showSecond : true,
			timeFormat : 'hh:mm:ss',
			dateFormat : 'yy-mm-dd',
			timeOnly:false,
			stepHour : 1,
			stepMinute : 1,
			stepSecond : 1,
			minDate: new Date()
		});
		$("#overTime").datetimepicker({
			showSecond : true,
			timeFormat : 'hh:mm:ss',
			dateFormat : 'yy-mm-dd',
			timeOnly:false,
			stepHour : 1,
			stepMinute : 1,
			stepSecond : 1,
			minDate: new Date()
		});
	};
	//拉取二维码并且弹出充值框
	var _showRechargeDiglog = function(orderInfoId){
		var payUrl = CONTEXT_PATH +"/pay/code_pay?orderInfoId="+orderInfoId;
		 $.ajaxJsonGet(payUrl,null, {
	            done: function (res) {
	                if (res.code == 0) {
	                	$("#codePayImg").prop("src",res.data.msg);
	                	_showDialog();
	                	$("#orderInfoId").val(orderInfoId);
	                } else {
	                    //  alert(res.msg);
	                }
	            }
	        });
	};
	
	//取值
	var _getRechargeMoney = function (){
		var rechargeMoneys = document.getElementsByName("rechargeMoney");
		var money = 0.0;
		for (var i = 0; i < rechargeMoneys.length; i++) {
			if(rechargeMoneys[i].checked){
				money = parseFloat(rechargeMoneys[i].value);
			}
		}
		if(money==0){
			money = parseFloat($("#otherMoney").val().trim());
		}
		return money;
	};
	var _bind = function(){
		//红包活动tab切换页
		$('#activity_tab li').click(function () {
			var index = $(this).data('index');
			$(this).addClass('z-sel').siblings().removeClass('z-sel');
			_queryList(index);
		});

		$('#activity_ensure_button').click(function(){
			var isEdit = Number($('input[name="edit"]').val());
			if(isEdit&&isEdit!=2){
				_doEditSubmit();
			}
			else{
				_doModelSubmit();
			}
		})
		$('#jk_code_list').click(function(){
			jumi.template('jk/jk_code',function(tpl){
				$('#shopIndexContent').html(tpl);
			})
		})
	}
	//清除选中的金额
	var _clearChekedMoney = function() {
		var tabul = $(".select-money .u-sort");
		tabul.siblings().removeClass('active');
		tabul.find("input").prop("checked",false);
	};

	//支付弹窗
	var _showDialog = function(){
			var elem = document.getElementById('dialoginfo');
			dialog({
				title: "微信扫码支付",
				content: elem,
				onclose:function(){
					clearInterval(timer1);//回调关闭定时请求
				},
			}).width(300).showModal();
			timer1 = setInterval("_ajaxstatus()", 3000);
	};
	

	var _gotoJkList = function(appid){
		_createActivity(appid);
	};
	//选择地区函数
	var _advancedRegion = function(){
		$('#activity_region').click(function(){
			var areaId = $('input[name="activity_area_ids"]').val();
			var re = new region({
				numberProvince:3,
				checkedNode:areaId,
				callback:function(nodeString,nodeAreaFont){
					var tpl = '';
					$('#choose_activity_areas').empty();
					$('input[name="activity_area_ids"]').val(nodeString);
					$('input[name="activity_area_name"]').val(nodeAreaFont);//保存名字
					var names = nodeAreaFont.split(',');
					_.map(names,function(k,v){
						tpl+='<div class="u-btn-smltgry u-btn-smltgry-1"><span>'+k+'</span></div>';
					})
					$('#choose_activity_areas').append(tpl);
				}
			})
			re.render();
		})
	}
	var _newredbag = function(){
		jumi.template('jk/jk_account_c_redPaper?subType=1',function (tpl) {
			var d = dialog({
				title: '创建红包',
				content:tpl,
				id:'createDialogAction',
				width:820,
				onshow:function(){
					_timeTimepicker();
					_advancedRegion();
					_addEventAction();
					_computing();
				}
			});
			d.showModal();
		})
	}
	//采集数据
	var _addEventAction = function(){
		$('#activity_ensure_button').click(function(){
			var isEdit = Number($('input[name="edit"]').val());
			if(isEdit&&isEdit!=2){
				_doEditSubmit();
			}
			else{
				_doModelSubmit();
			}
		})
	};
	//开启或暂停活动
	var _Action = function(url,id){
		$.ajaxJsonGet(url,null,{
			done:function (res) {
				if(res.code===0){
					var dm = new dialogMessage({
						type:1,
						fixed:true,
						msg:res.data.msg,
						isAutoDisplay:true,
						time:3000
					});
					dm.render();
					_queryActivityList(1);
				}else{
					var dm = new dialogMessage({
						type:2,
						fixed:true,
						msg:'活动设置失败',
						isAutoDisplay:true,
						time:3000
					});
					dm.render();
				}
			}
		})
	};
	//暂停取消活动
	var _set = function(id,name,status){
		var url = ajaxUrl.url1+'pause/'+id;
		msg = (status==='1')?'确定暂停【'+name+'】的活动吗?':'确定重新启动【'+name+'】的活动吗?';
		var args = {};
		args.fn1 = function(){
			_Action(url,id);
		};
		args.fn2 = function(){

		};
		jumi.dialogSure(msg,args);
	}
	//取消活动
	var _del = function(id,name){
		var url = ajaxUrl.url1+'/stop/'+id;
		var status = $('#activity_tab').find('.z-sel').data('index');
		var args = {};
		args.fn1 = function(){
			$.ajaxJsonDel(url,{
				"done":function (res) {
					if(res.code===0){
						var dm = new dialogMessage({
							type:1,
							fixed:true,
							msg:res.data.msg,
							isAutoDisplay:true,
							time:3000
						});
						dm.render();
						_queryList(status);
					}
				}
			});
		};
		args.fn2 = function(){

		};
		jumi.dialogSure('是否取消【'+name+'】的活动',args);

	}
	//获取已发送紅包列表
	var _getSendRecord = function(){
		 var activityUserQo = {};
		 activityUserQo.pageSize = 10;
		 activityUserQo.status = $("#status").val();
		 activityUserQo.startDate=$("#startDate2").val();
		 activityUserQo.endDate=$("#endDate2").val();
		 activityUserQo.platform=$("#platform").val();
		 activityUserQo.pubNickName=$("#pubNickName").val();
		   var sendRecordUrl = CONTEXT_PATH + "/jk/sen_record";
	       jumi.pagination('#sendRecordPaperToolbar',sendRecordUrl,activityUserQo,function(res,curPage){
	           if(res.code===0){
	               var data = {
	                   items:res.data.items
	               };
	               jumi.template('jk/jk_send_detail_list',data,function(tpl){
	            	   $('#send_list').empty();
	                   $('#send_list').html(tpl);
	               });
	           }
	       });
	};

	return{
		init:_init,
		getrecharge:_getrechargeHtml,
		clearCheckedMoney:_clearChekedMoney,
		createOrder:_create_order,
		gotoJkList:_gotoJkList,
		getSendRedord:_getSendRecord,
		newredbag:_newredbag,
		queryList:_queryList,
		watch:_watch,
		bind:_bind,
		set:_set,
		del:_del
	};
	
})();