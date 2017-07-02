/**
 * Created by ray on 16/9/12.
 */
//活动推广- 现金红包
//注意要求!
//红包金额只支持到个位数
//百分比只支持个位数不支持小数点
//单人红包金额限定小数点2位,最大数值不超过分段金额
//中奖人数到个位数
CommonUtils.regNamespace("activity", "redPaper");
activity.redPaper = (function() {
    var ajaxUrl = {
        url1:CONTEXT_PATH+'/activity/',  // 新增红包活动接口
        url2:CONTEXT_PATH+'/activity/list',//查询活动列表
        url3:CONTEXT_PATH+'/activity/user_count',//粉丝数量统计
        url4:CONTEXT_PATH+'/activity/money_list',//粉丝统计列表
        url5:CONTEXT_PATH+'/activity/money_count'//红包发放记录统计
    };
    var percentCount = 100;
    var percentSum = 0;
    var _init = function () {
        _bind();
        _query();//查询
        _queryList(0);
    };
    //红包tab函数调用
    var _queryList = function(status){
        switch(status)
        {
            case 0:
                _createActivity();
                break;
            case 1:
                _alreadyActivitying();
                break;
            case 2:
                _startedActivitying();
                break;
            case 3:
                _confirmActivitying();
            default:

        }
    };
    //根据类型返回红包信息
    var _getTitle = function(type){
        switch(type){
            case 1:
                return '首次关注发红包';
                break;
            case 2:
                return '已关注粉丝发红包';
                break;
            case 3:
                return '购买商品发红包';
                break;
            case 4:
                return '确认收货发红包';
                break;
        }
    }

    //查询红包列表
    var _query = function () {
        $('#activity_search_btn').click(function(){
            var status = $('#activity_tab').find('.z-sel').data('index');
            _queryList(status);
        })
    };
    //粉丝计算模块
    var _initCalculate = function(typeValue){
        jumi.template('activity/activity_calculate?status='+typeValue,function(tpl){
            $('#activity_box').html(tpl);
            _validate();
            if(typeValue==2){
                _computingOld();
            }else{
                _computing();
            }
        })
    }
    //已关注的粉丝
    var _computingOld = function(){
        //分配
        $('#fansNum').blur(function(){
            var fmount = $(this).val()||0;
            $('#fansNum').text(fmount);
            _calculate_fmount(fmount);
            _clearRedFPackets();
        });
        $('input[data-id^="single_"]').unbind('blur').bind('blur',function(){
            var obj = $(this);
            var id = obj.attr('id');
            var index = $(this).data('index');
            var value = obj.val()||'-'//输入框的值
            _calculate_f_o_single(index,value);
        })
        //获奖百分比设置重新计算
        $('input[data-id^="percent_"]').blur(function(){
            var obj = $(this);
            var index = obj.data('index');
            var value = $('li[data-id^="batches_'+index+'"]').text();
            var percentageValue = $('select[data-id^="percentage_'+index+'"]').val();
            var fmount = $('#fansNum').val()||0;
            var singlefmount = $('input[data-id^="single_'+index+'"]').val();
            _calculate_fmount(fmount);
            _calculate_f_o_single(index,singlefmount);
        });
    }
    //老粉丝单组红包金额
    var _calculate_f_o_single = function(index,value){
        var winnner = $('li[data-id^="old_winners_'+index+'"]').text();
        var groupFmount = winnner*value;
        $('li[data-id^="consume_'+index+'"]').text(groupFmount.toFixed(2));
        _calculate_f_amount();
    }
    //计算单组红包金额
    var _calculate_f_single = function(index,value){
        var winnner = $('li[data-id^="winners_'+index+'"]').text();
        var groupFmount = winnner*value;
        $('li[data-id^="consume_'+index+'"]').text(groupFmount);
        _calculate_f_amount();
    }
    //计算红包总金额
    var _calculate_f_amount= function(){
        var amount = 0;
        $('li[data-id^="consume_"]').each(function(i){
            var obj = $(this);
            var value = obj.text();//输入框的值
            amount=amount+Number(value)//保留两位小数
        })
        $('#red_amount').val(amount.toFixed(2));
    }
    //重新计算分批个数
    var _calculate_fmount = function(fmount){
        $('li[data-id^="batches_"]').each(function(i){
            var k = i+1;
            var v = $('input[data-id^="percent_'+k+'"]').val();
            var val = (v==='')?'-':parseInt(v,10)/100;
            var batchesFmount = (fmount*val===0||isNaN(fmount*val))?'0':parseInt(fmount*val,10);
            $(this).text(batchesFmount);
        })
        $('li[data-id^="old_winners_"]').each(function(i){
            var k = i+1;
            var v = $('input[data-id^="percent_'+k+'"]').val();
            var val = (v==='')?'-':parseInt(v,10)/100;
            var batchesFmount = (fmount*val===0||isNaN(fmount*val))?'0':parseInt(fmount*val,10);
            $(this).text(batchesFmount);
        })
    }
    //单个计算中奖人数和非中奖人数
    var _calculate_f_winners = function(fmount,index,percentageValue){
        var value = $('li[data-id^="batches_'+index+'"]').text();
        if(percentageValue==='0'){
            var percentage = 1/(Number(percentageValue));
        }else{
            var percentage = 1/(Number(percentageValue)+1);
        }
        var no_winner = (percentage===Infinity)?0:parseInt(percentage*value,10);
        if(no_winner===0){
            var winner = value-no_winner;
        }else{
            var winner = parseInt(no_winner*Number(percentageValue),10);
        }
        $('li[data-id^="no_winning_'+index+'"]').text(no_winner);
        $('li[data-id^="winners_'+index+'"]').text(winner);

    }
    //首次关注新粉的计算方法
    var _computing = function(){
        //总人数设置重新计算
        $('#red_amount').unbind('blur').bind('blur',function(){
            var amount = $(this).val()||0;
            $('#amount').text(amount);
            _calculate_amount(amount);
            _calculate_winners(amount);
            _calculate_no_winners(amount);
            _calculate_fans();
        });
        //获奖百分比设置重新计算
        $('input[data-id^="percent_"]').unbind('blur').bind('blur',function(){
            var amount = $('#red_amount').val()||0;
            _calculate_amount(amount);
            _calculate_winners(amount);
            _calculate_no_winners(amount);
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
            _calculate_no_winners(amount);
            _calculate_fans();
        });
        $('select[data-id^="percentage_"]').change(function(){
            var amount = $('#red_amount').val()||0;
            _calculate_amount(amount);
            _calculate_winners(amount);
            _calculate_no_winners(amount);
            _calculate_fans();
        })
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
    //计算不中奖人数
    var _calculate_no_winners = function(amount){

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
    };
    //清空单人红包金额和中奖人数
    var _clearRedFPackets = function(){
        $('input[data-id^="single_"]').each(function(){
            var element = $(this);
            element.val('');
        });
        $('li[data-id^="winners_"]').each(function(){
            var element = $(this);
            element.text('-');
        });
        $('li[data-id^="no_winning_"]').each(function(){
            var element = $(this);
            element.text('-');
        })
        $('li[data-id^="consume_"]').each(function(){
            var element = $(this);
            element.text('-');
        })
        $('select[data-id^="percentage_"]').each(function(){
            var element = $(this);
            var id = element.attr('id');
            $('#'+id).find("option[text='请选择']").attr("selected",true);
        })
    }
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
        $('li[data-id^="no_winning_"]').each(function(){
            var element = $(this);
            element.text('-');
        })
    };
    //计算粉丝总人数
    var _calculate_fans = function(){
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
            $('#activity_amount_num').text(fansCount);
        }
    };
    //错误显示
    var _error = function(msg){
        $('#error').show();
        $('body').scrollTop(0);
        $('#tipError').text(msg);
    }
    //修改活动方法
    var _doEditSubmit = function(){
        $('#error').hide();
        var activityUo = {};
        var activityConditionUo = {};
        var typeArray = [];
        activityUo.takeType = $('input[name="show_form"]:checked').val();
        activityUo.shakeCount = $('input[name="showgl"]:checked').val();
        activityUo.platform = $('input[name="activity_space"]:checked').val();
        activityUo.blessings = $('#blessingName').val();
        activityUo.activityName = $('#actionName').val();
        activityUo.totalMoney = Number($('#red_amount').val())*100;
        activityUo.noWinInfo = $('#activity_nowinning_txt').val();
        activityUo.winInfo = $('#activity_winning_txt').val();
        activityUo.startTime = $('#startTime').val();
        activityUo.endTime = $('#overTime').val();
        activityUo.type = 1;
        activityUo.subType = $('input[name="subType"]').val();
        activityUo.status = $('input[name="subStatus"]').val();
        activityUo.id = $('input[name="activityId"]').val();
        if(activityUo.subType==='2'){
            for(var i=1;i<5;i++){
                var SubActivity = {};
                SubActivity.id = $('input[data-id^="percent_'+i+'"]').parent().find(":hidden").attr('id');
                SubActivity.winScale = $('input[data-id^="percent_'+i+'"]').val();
                SubActivity.consumeMoney = Number($('li[data-id^="consume_'+i+'"]').text())*100;
                SubActivity.redMoney = Number($('input[data-id^="single_'+i+'"]').val())*100;
                SubActivity.winCount = $('li[data-id^="old_winners_'+i+'"]').text();;
                SubActivity.noWinScale = 0;
                SubActivity.noWinCount = 0;
                SubActivity.oldFansCount = $('li[data-id^="batches_'+i+'"]').text();
                SubActivity.seq = i;
                typeArray.push(SubActivity);
            }
        }else{
            for(var i=1;i<5;i++){
                var SubActivity = {};
                SubActivity.id = $('input[data-id^="percent_'+i+'"]').parent().find(":hidden").attr('id');
                SubActivity.winScale = $('input[data-id^="percent_'+i+'"]').val();
                SubActivity.consumeMoney = Number($('li[data-id^="consume_'+i+'"]').text())*100;
                SubActivity.redMoney = Number($('input[data-id^="single_'+i+'"]').val())*100;
                SubActivity.winCount = $('li[data-id^="winners_'+i+'"]').text();
                SubActivity.noWinScale = $('select[data-id^="percentage_'+i+'"]').find("option:selected").val();
                SubActivity.noWinCount = $('li[data-id^="no_winning_'+i+'"]').text();
                SubActivity.seq = i;
                SubActivity.preFansCount = $('#activity_amount_num').text();
                typeArray.push(SubActivity);
            }
        }
        if($('input[name="activity_prescribed"]').is(":checked")){
            activityConditionUo.isBuy = 1;
        }else{
            activityConditionUo.isBuy = 0;
        }
        activityUo.activityConditionUo = activityConditionUo;
        activityUo.activitySubList =typeArray;
        //高级设置
        activityConditionUo.sex = $('input[name="activity_sex"]:checked').val()||0;
        activityConditionUo.buyMoney = Number($('input[name="activity_shop"]').val()*100);
        activityConditionUo.areaNames = $('input[name="activity_area_name"]').val();
        activityConditionUo.areaIds = $('input[name="activity_area_ids"]').val();
        if(activityUo.subType==='2'){
            activityUo.preFansCount = $('#fansNum').val();
        }else{
            activityUo.preFansCount = $('#activity_amount_num').text();
        }

        if($('input[name="activity_role"]').is(":checked")){
            activityConditionUo.roleLimit = 1;
        }else{
            activityConditionUo.roleLimit = 0;
        }
        if($('input[name="activity_downs_role"]').is(":checked")){
            activityConditionUo.downRoleLimit = 1;
        }else{
            activityConditionUo.downRoleLimit = 0;
        }
        if($('input[name="activity_area"]').is(":checked")){
            activityConditionUo.areaLimit = 1;
        }else{
            activityConditionUo.areaLimit = 0;
        }
        if(activityUo.subType==='3'){
            activityConditionUo.buyMoney = Number($('input[name="activity_shop"]').val())*100;
            flag = $('#form2').valid();
        }
        if(activityUo.subType==='1'){
            var downRoles = [];
            $('#choose_activity_roles').find('.role-del').each(function(){
                var id = $(this).data('id');
                downRoles.push(id);
            })
            downRoles = downRoles.join(',');
            activityConditionUo.downRoles = downRoles;
        }else{
            var roles = [];
            $('#choose_activity_roles').find('.role-del').each(function(){
                var id = $(this).data('id');
                if(id=='99'){
                    activityConditionUo.isBuy = id;
                }else{
                    activityConditionUo.isBuy = 0;
                    roles.push(id);
                }
            })
            roles = roles.join(',');
            activityConditionUo.roles = roles;
        }
        if(activityUo.subType === '2'){
            var flag = $('#fansCal').prop("disabled");
            if(!flag){
                jumi.tipDialog('fansCal','老粉丝还未筛选！请筛选',true);
                return;
            }
        }
        if(activityUo.totalMoney===0){
            _error('输入的金额必须大于0，请重新设置百分比');
            return;
        }
        if(!activityUo.blessings||!activityUo.activityName){
            _error('活动名称或者祝福语未设定,请重新设置');
            return;
        }
        if(!activityUo.noWinInfo){
            _error('未中奖提示语未设定,请重新设置');
            return;
        }
        if(!activityUo.platform){
            _error('活动平台未选择');
            return;
        }

        if(!activityUo.winInfo){
            _error('中奖提示语未设定,请重新设置');
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
        if(activityUo.subType==='2'){
            if(!activityUo.startTime){
                _error('活动时间未设定,请重新设置');
                return;
            }
        }else{
            if(!activityUo.startTime || !activityUo.endTime){
                _error('活动时间未设定,请重新设置');
                return;
            }
        }

        if(activityUo.subType==='3'){
            var flag = _isFillingFull(typeArray)&&($('#form2').valid());
        }else{
            var flag = _isFillingFull(typeArray);
        }
        var jsonData = JSON.stringify(activityUo);
        var status = $('#activity_tab').find('.z-sel').data('index');
        if(flag){
            _activity_data_update(jsonData,status);
        }else{
            _error('数据未填写完整,请重新设置');
        }

    };

    var _activity_data_update = function (jsonData,status) {
        $.ajaxJsonPut(ajaxUrl.url1,jsonData,{
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
                    }else if(res.data.code===2){
                        var tempData = JSON.parse(jsonData);
                        tempData.operationState="1";
                        jsonData = JSON.stringify(tempData);
                        _activity_data_makesure(jsonData,status,res.data.msg,"edit");
                    }else{
                        var dm = new dialogMessage({
                            type:1,
                            fixed:true,
                            msg:res.data.msg,
                            isAutoDisplay:true,
                            time:1000
                        });
                        dm.render();
                        _queryList(status);
                        setTimeout(function(){
                            dialog.get('editDialogAction').close().remove();
                        },1500);
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
    }


    /**
     * 二次确认保存
     *jsonData 数据源
     * status 刷新参数
     * msg 返回信息提示
     * tag  新增:"add"  修改:"edit"
     */
    var _activity_data_makesure = function (jsonData,status,msg,tag) {
        var args = {};
        args.fn1 = function(){
            if(tag==="add"){
                _activity_data_post(jsonData,status);
            }else if(tag==="edit"){
                _activity_data_update(jsonData,status);
            }

        };
        args.fn2 = function(){
        };
        jumi.dialogSure(msg,args);
    };

    //红包数据采集提交服务端
    var _doModelSubmit = function () {
        $('#error').hide();
        var activityCo = {};
        var activityConditionCo = {};
        var activity = {};
        var typeArray = [];
        activityCo.takeType = $('input[name="show_form"]:checked').val();
        activityCo.shakeCount = $('input[name="showgl"]:checked').val();
        activityCo.platform = $('input[name="activity_space"]:checked').val();
        activityCo.blessings = $('#blessingName').val();
        activityCo.activityName = $('#actionName').val();
        activityCo.totalMoney = Number($('#red_amount').val())*100;
        activityCo.noWinInfo = $('#activity_nowinning_txt').val();
        activityCo.winInfo = $('#activity_winning_txt').val();
        activityCo.startTime = $('#startTime').val();
        activityCo.endTime = $('#overTime').val();
        activityCo.type = 1;
        activityCo.subType = $('input[name="subType"]').val();
        activityCo.status = $('input[name="subStatus"]').val();
        //高级设置
        activityConditionCo.sex = $('input[name="activity_sex"]:checked').val()||0;
        if(activityCo.subType==='2'){
            activityCo.preFansCount = $('#fansNum').val();
        }else{
            activityCo.preFansCount = $('#activity_amount_num').text();
        }
        if($('input[name="activity_role"]').is(":checked")){
            activityConditionCo.roleLimit = 1;

        }else{
            activityConditionCo.roleLimit = 0;
        }
        if($('input[name="activity_downs_role"]').is(":checked")){
            activityConditionCo.downRoleLimit = 1;
        }else{
            activityConditionCo.downRoleLimit = 0;
        }
        if($('input[name="activity_area"]').is(":checked")){
            activityConditionCo.areaLimit = 1;
        }else{
            activityConditionCo.areaLimit = 0;
        }
        activityConditionCo.areaNames = $('input[name="activity_area_name"]').val();
        activityConditionCo.areaIds = $('input[name="activity_area_ids"]').val();
        if(activityCo.subType==='3'){
            activityConditionCo.buyMoney = Number($('input[name="activity_shop"]').val()*100);
        }
        if(activityCo.subType==='1'){
            var downRoles = [];
            $('#choose_activity_roles').find('.role-del').each(function(){
                var id = $(this).data('id');
                downRoles.push(id);
            })
            downRoles = downRoles.join(',');
            activityConditionCo.downRoles = downRoles;
        } else{
            var roles = [];
            $('#choose_activity_roles').find('.role-del').each(function(){
                var id = $(this).data('id');
                if(id=='99'){
                    activityConditionCo.isBuy = id;
                }else{
                    activityConditionCo.isBuy = 0;
                    roles.push(id);
                }
            })
            roles = roles.join(',');
            activityConditionCo.roles = roles;
        }
        if(activityCo.totalMoney===0&&activityCo.subType==='3'){
            _error('发送的红包金额必须大于0,请重新设置百分比');
            return;
        }
        if(activityCo.subType === '2'){
            var flag = $('#fansCal').prop("disabled");
            if(!flag){
                jumi.tipDialog('fansCal','老粉丝还未筛选！请筛选',true);
                return;
            }
        }
        if(!activityCo.blessings||!activityCo.activityName){
            _error('活动名称或者祝福语未设定,请重新设置');
            return;
        }
        if(!activityCo.noWinInfo){
            _error('未中奖提示语未设定,请重新设置');
            return;
        }
        if(!activityCo.platform){
            _error('活动平台未选择');
            return;
        }

        if(!activityCo.winInfo){
            _error('中奖提示语未设定,请重新设置');
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
        if(activityCo.subType==='2'){
            if(!activityCo.startTime){
                _error('活动时间未设定,请重新设置');
                return;
            }
        }else{
            if(!activityCo.startTime || !activityCo.endTime){
                _error('活动时间未设定,请重新设置');
                return;
            }
        }
        if(activityCo.subType==='2'){
            for(var i=1;i<5;i++){
                var SubActivity = {};
                SubActivity.winScale = $('input[data-id^="percent_'+i+'"]').val();
                SubActivity.consumeMoney = parseInt($('li[data-id^="consume_'+i+'"]').text())*100;
                SubActivity.redMoney = Number($('input[data-id^="single_'+i+'"]').val())*100;
                SubActivity.winCount = $('li[data-id^="old_winners_'+i+'"]').text();
                SubActivity.noWinScale = 0;
                SubActivity.noWinCount = 0;
                SubActivity.oldFansCount = $('li[data-id^="batches_'+i+'"]').text();
                SubActivity.seq = i;
                typeArray.push(SubActivity);
            }
        }else{
            for(var i=1;i<5;i++){
                var SubActivity = {};
                SubActivity.winScale = $('input[data-id^="percent_'+i+'"]').val();
                SubActivity.consumeMoney = parseInt($('li[data-id^="consume_'+i+'"]').text())*100;
                SubActivity.redMoney = Number($('input[data-id^="single_'+i+'"]').val())*100;
                SubActivity.winCount = $('li[data-id^="winners_'+i+'"]').text();
                SubActivity.noWinScale = $('select[data-id^="percentage_'+i+'"]').find("option:selected").val();
                SubActivity.noWinCount = $('li[data-id^="no_winning_'+i+'"]').text();
                SubActivity.seq = i;
                SubActivity.preFansCount = $('#activity_amount_num').text();
                typeArray.push(SubActivity);
            }
        }

        activityCo.activityConditionCo = activityConditionCo;
        activityCo.subActivityList =typeArray;
        if(activityCo.subType==='3'){
            var flag = _isFillingFull(typeArray)&&($('#form2').valid());
        }else{
            var flag = _isFillingFull(typeArray);
        }
        var jsonData = JSON.stringify(activityCo);
        var status = $('#activity_tab').find('.z-sel').data('index');
        if(flag){
            _activity_data_post(jsonData,status);
        }else{
            _error('数据未填写完整,请重新设置');
        }
    };

    var _activity_data_post = function (jsonData,status) {
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
                    }else if(res.data.code===2){
                        var tempData = JSON.parse(jsonData);
                        tempData.operationState="1";
                        jsonData = JSON.stringify(tempData);
                        _activity_data_makesure(jsonData,status,res.data.msg,"add");
                    }else{
                        var dm = new dialogMessage({
                            type:1,
                            fixed:true,
                            msg:res.data.msg,
                            isAutoDisplay:true,
                            time:1000
                        });
                        dm.render();
                        _queryList(status);
                        setTimeout(function(){dialog.get('createDialogAction').close().remove();},1500)
                    }
                }else{
                    var dm = new dialogMessage({
                        type:2,
                        fixed:true,
                        msg:res.data.msg,
                        width:200,
                        height:200,
                        isAutoDisplay:true,
                        time:3000,
                        left: 100,
                        top: 200
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
    }

    //判断是否满足数据提交要求
    _isFillingFull = function(arrayData){
        var flag = true;
        $.each(arrayData,function(k,v){
            for(i in v){
                if(v[i]===''||v[i]==='-'||isNaN(v[i])){
                    flag = false;
                    return true;
                }
            }
        });
        return flag;
    };
    //判断启用
    var _startBuyShop = function(){
        $('#checkBox_start').click(function(){
            var flag = $('input[name="checkBox_start"]').is(":checked");
            if(flag){
                $('input[name="activity_shop"]').prop('disabled',false);
            }else{
                $('input[name="activity_shop"]').prop('disabled',true);
                $('input[name="activity_shop"]').val('');
            }

        })
    }
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

    //验证数据函数
    var _validate = function(){
        $('#form2').validate({
            rules: {
                activity_shop:{
                    digits:true,
                    isIntGtZero:true
                }
            },
            messages:{
                activity_shop:{
                    isIntGtZero:'金额必须是一个大于零的整数',
                    digits:'金额必须是一个大于零的整数'
                }
            }
        })
        $("#form1").validate({
            rules: {
                red_amount:{
                    required: true,
                    digits:true,
                    isIntGtZero:true
                },
                percent:{
                    required: true,
                    digits:true,
                    isIntGtZero:true
                }
            },
            messages:{
                red_amount: {
                    required: "请输入金额",
                    isIntGtZero:'金额必须是一个大于零的整数',
                    digits:'金额必须是一个大于零的整数'
                },
                percent:{
                    required: "请输入百分比",
                    isIntGtZero:'百分比必须是一个大于零的整数',
                    digits:'百分比必须是一个大于零的整数'
                }
            }
        })
        $('input[data-id^="single_"]').on('keypress',function(event){
            var value = $(this).val();
            if((event.keyCode<48 || event.keyCode>57) && event.keyCode!=46 || /[1-9]\.\d\d$/.test(value)){
                event.preventDefault();
            }
        });
        // //设置百分比必须是数字
        $('input[data-id^="percent_"]').on('keypress',function(event){
            var value = $(this).val();
            if((event.keyCode<48 || event.keyCode>57) || /\.\d\d$/.test(value)){
                event.preventDefault();
            }
        })

    };
    //回填角色
    var _fillFoles = function(array){
        var tpl = '';
        _.map(array,function(k,v){
            switch(k)
            {
                case '1':
                    tpl+='<div class="u-btn-smltgry u-btn-smltgry-1 role-del" data-id="1"><span>代理商1档</span><i class="iconfont icon-delete1"></i></div>';
                    break;
                case '2':
                    tpl+='<div class="u-btn-smltgry u-btn-smltgry-1 role-del" data-id="2"><span>代理商2档</span><i class="iconfont icon-delete1"></i></div>';
                    break;
                case '3':
                    tpl+='<div class="u-btn-smltgry u-btn-smltgry-1 role-del" data-id="3"><span>代理商3档</span><i class="iconfont icon-delete1"></i></div>';
                    break;
                case '4':
                    tpl+='<div class="u-btn-smltgry u-btn-smltgry-1 role-del" data-id="4"><span>代理商4档</span><i class="iconfont icon-delete1"></i></div>';
                    break;
                case '5':
                    tpl+='<div class="u-btn-smltgry u-btn-smltgry-1 role-del" data-id="5"><span>分销商1档</span><i class="iconfont icon-delete1"></i></div>';
                    break;
                case '6':
                    tpl+='<div class="u-btn-smltgry u-btn-smltgry-1 role-del" data-id="6"><span>分销商2档</span><i class="iconfont icon-delete1"></i></div>';
                    break;
                case '7':
                    tpl+='<div class="u-btn-smltgry u-btn-smltgry-1 role-del" data-id="7"><span>分销商3档</span><i class="iconfont icon-delete1"></i></div>';
                    break;
                case '8':
                    tpl+='<div class="u-btn-smltgry u-btn-smltgry-1 role-del" data-id="8"><span>分享客</span><i class="iconfont icon-delete1"></i></div>';
                    break;
                case '99':
                    tpl+='<div class="u-btn-smltgry u-btn-smltgry-1 role-del" data-id="99"><span>已经关注购买粉丝</span><i class="iconfont icon-delete1"></i></div>';
                    break;
                default:
            }
        })
        $('#choose_activity_roles').html(tpl);
        $('.role-del').click(function(){
            $(this).remove();
            _openFan();
        })
    }
    //数据回填红包基本信息
    var _fillData = function (data) {

        var array = [];
        var tpl = '';
        var flag = $('input[name="edit"]').val();
        $('#red_amount').val(data['totalMoney']/100);
        $('#actionName').val(data['activityName']);
        $('#blessingName').val(data['blessings']);
        $('#amount').text(data['preFansCount']);
        $('#fansNum').val(data['preFansCount']);
        $('input[name="activity_space"][value="'+data['platform']+'"]').prop('checked',true);
        $('#activity_winning_txt').val(data['winInfo']);
        $('#activity_nowinning_txt').val(data['noWinInfo']);
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
            if(data.subType===2){
                $('li[data-id^="batches_'+j+'"]').text(data.activitySubList[i].oldFansCount);
                $('li[data-id^="old_winners_'+j+'"]').text(data.activitySubList[i].winCount);
            }else{
                $('li[data-id^="winners_'+j+'"]').text(data.activitySubList[i].winCount);
                $('li[data-id^="no_winning_'+j+'"]').text(data.activitySubList[i].noWinCount);
                $('select[data-id^="percentage_'+j+'"').find("option[value='"+data.activitySubList[i].noWinScale+"']").attr("selected",true);
            }
            $('input[data-id^="percent_'+j+'"]').parent().find(":hidden").attr('id',data.activitySubList[i].id);
            $('input[data-id^="percent_'+j+'"]').val(data.activitySubList[i].winScale);
            $('li[data-id^="consume_'+j+'"]').text(cm);
            $('input[data-id^="single_'+j+'"]').val(single);
        })

        //高级设置
        $('input[name="activity_sex"][value="'+data['activityCondition']['sex']+'"]').prop('checked',true);
        if(data['activityCondition']['areaLimit']==1){
            $('input[name="activity_area"]').prop('checked',true);
        }else{
            $('input[name="activity_area"]').prop('checked',false);
        }

        $('input[name="show_form"][value="'+data['takeType']+'"]').prop('checked',true);
        $('input[name="showgl"][value="'+data['shakeCount']+'"]').prop('checked',true);
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
        if(data.subType===2){
            _disabledFan();
        }
        if(data.subType!=1){
            if(data['activityCondition']['roles']!=null){
                array = data['activityCondition']['roles'].split(',');
                if(data['activityCondition']['isBuy']===99) {
                    if(array.length>0){
                        array[array.length] = '99';
                    }else{
                        array.push('99').split(',');
                    }
                }
                _fillFoles(array);
            }
        }else{
            if(data['activityCondition']['downRoles']!=null){
                array = data['activityCondition']['downRoles'].split(',');
                _fillFoles(array);
            }
        }
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
        $('input[name="activity_sex"]').click(function(){
            _openFan();
        })
        $('input[name="activity_role"]').click(function(){
            _openFan();
        })
        $('input[name="activity_area"]').click(function(){
            _openFan();
        })
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
                    _openFan();
                }
            })
            re.render();
        })
    }
    //选择角色信息函数
    var _advancedSetting = function (subType) {

        $('#activity_roles').click(function() {
            var doc = $('#choose_activity_roles').find('.role-del');
            var array = [];
            doc.each(function(){
                var id = $(this).data('id');
                array.push(id);
            })
            jumi.file('role.json',function(res){
                if(res.code===0){
                    var data = {
                        items:res.data
                    }
                    _.each(array,function(k,v){
                        var d = _.where(res.data,{id:k});
                        d[0].ischecked = 1;
                    })
                    var subType = $('input[name="subType"]').val();
                    jumi.template('activity/activity_role?subType='+subType,data,function (tpl) {
                        var d = dialog({
                            title: '选取角色',
                            content:tpl,
                            width:400,
                            id:'activityRoles',
                            onshow:function(){
                                $('#allcheckbox').click(function(){
                                    var flag = $(this).is(":checked");
                                    if(flag){
                                        $('input[name="role"]').prop('checked',true);
                                    }else{
                                        $('input[name="role"]').prop('checked',false);
                                    }
                                })
                                $('#activity_role_btn').click(function(){
                                    var roles = [],tpl='';
                                    $('input[id^="role_checkBox_"]:checked').each(function(){
                                        var id = $(this).val();
                                        var tip = $(this).data('tip');
                                        tpl+='<div class="u-btn-smltgry u-btn-smltgry-1 role-del" data-id="'+id+'"><span>'+tip+'</span></div>'
                                    })
                                    $('#choose_activity_roles').html(tpl);
                                    _openFan();
                                    $('.role-del').click(function(){
                                        $(this).remove();
                                        _openFan();
                                    })
                                    dialog.get('activityRoles').close().remove();
                                })
                            }
                        });
                        d.showModal();
                    })
                }
            })
        })
    }
    //绑定事件
    var _bind = function(){
        //红包活动tab切换页
        $('#activity_tab li').click(function () {
            var index = $(this).data('index');
            $(this).addClass('z-sel').siblings().removeClass('z-sel');
            _queryList(index);
        });

    };
    //粉丝筛选
    var _disabledFan = function(){
        $('#fansCal').attr('disabled',true);
    }
    var _openFan = function(){
        $('#fansCal').attr('disabled',false);
    }
    //粉丝筛选
    var _fanFilter = function(){
        $('#fansCal').click(function () {
            var qo = {};
            qo.sex = $('input[name="activity_sex"]:checked').val()||0;
            if($('input[name="activity_role"]').is(":checked")){
                qo.roleLimit = 1;
            }else{
                qo.roleLimit = 0;
            }
            if($('input[name="activity_area"]').is(":checked")){
                qo.areaLimit = 1;
            }else{
                qo.areaLimit = 0;
            }
            var roles = [];
            $('#choose_activity_roles').find('.role-del').each(function(){
                var id = $(this).data('id');
                if(id=='99'){
                    qo.isBuy = id;
                }else{
                    qo.isBuy = 0;
                    roles.push(id);
                }
            })
            roles = roles.join(',');
            qo.roles = roles;
            qo.areaNames = $('input[name="activity_area_name"]').val();
            qo.areaIds = $('input[name="activity_area_ids"]').val();
            $.ajaxJson(ajaxUrl.url3,qo,{
                "done":function(res){
                    if(res.code===0){
                        var count = res.data;
                        $('#fansNum').val(count);
                        $('#fansNum').blur();
                        _disabledFan();
                    }
                }
            })
        })
    }

    //活动进行中
    var _alreadyActivitying = function(){
        jumi.template('/activity/activity_list',function (tpl) {
            $('#activity_content').html(tpl);
            _queryActivityList(1);
        })
    }
    //红包活动刷新列表
    var _queryActivityList = function(redType){
        var activityQo = {},types;
        activityQo.pageSize = 10;
        activityQo.type = 1;
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
                jumi.template('activity/activity_list_content?status='+redType,data,function(tpl){
                    $('#redPaperList').html(tpl);
                })
            }
        })

    }
    //活动未开始
    var _startedActivitying = function(){
        jumi.template('/activity/activity_list',function (tpl) {
            $('#activity_content').html(tpl);
            _queryActivityList(0);
        })
    }
    //活动已结束
    var _confirmActivitying = function(){
        jumi.template('/activity/activity_list',function (tpl) {
            $('#activity_content').html(tpl);
            _queryActivityList(3);
        })
    }
    //新建红包活动
    var _createActivity = function () {
        jumi.template('/activity/activity_create',function (tpl) {
            $('#activity_content').html(tpl);
            $('#dialog_red_bag li').click(function(){
                var type = $(this).data('type'),title;
                var title = _getTitle(type);
                jumi.template('activity/activity_c_redPaper?type='+type+'&edit=0&status=0',function (tpl) {
                    var d = dialog({
                        title: title,
                        content:tpl,
                        width:820,
                        id:'createDialogAction',
                        onshow:function () {
                            var subType = $('input[name="subType"]').val();
                            if(subType==='3'){
                                _startBuyShop();
                            }
                            _initCalculate(type);
                            _advancedSetting(subType);//角色限定
                            _advancedRegion(subType);//角色限定
                            _timeTimepicker();//时间设定
                            _addEventAction();
                            _fanFilter();

                        }
                    });
                    d.showModal();
                })
            })
        })
    }
    //修改红包活动
    var _modify = function(id,type,status){
        var url = ajaxUrl.url1+id;
        $.ajaxJsonGet(url,null,{
            "done":function(res){
                if(res.code===0){
                    var data = res.data;
                    jumi.template('activity/activity_c_redPaper?type='+type+'&edit=1&status='+status,data,function (tpl) {
                        var d = dialog({
                            title: '修改红包活动',
                            content:tpl,
                            id:'editDialogAction',
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
                                       _initCalculate(type)
                                    )).done(function(){
                                    _fillData(data);
                                    if(type==='3'){
                                        _startBuyShop();
                                    }
                                    _timeTimepicker();//初始化时间插件
                                    _validate();
                                    _addEventAction();
                                    _advancedSetting();
                                    _advancedRegion();
                                    _fanFilter();
                                })
                            }
                        });
                        d.showModal();
                    })
                }
            }
        })
    }
    //取消活动
    var _del = function(id,name){
        var url = ajaxUrl.url1+id;
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

    //再来一场红包活动
    var _moreOne = function(id,type){
        var url = ajaxUrl.url1+id;
        $.ajaxJsonGet(url,null,{
            "done":function(res){
                if(res.code===0){
                    var data = res.data;
                    jumi.template('activity/activity_c_redPaper?type='+type+'&edit=1&status='+status,data,function (tpl) {
                        var d = dialog({
                            title: '再来一场红包活动',
                            content:tpl,
                            id:'createDialogAction',
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
                                        _initCalculate(type)
                                    )).done(function(){
                                    $('input[name="edit"]').val(2);
                                    _fillData(data);
                                    if(type===3){
                                        _startBuyShop();
                                    }
                                    _computing();
                                    _timeTimepicker();
                                    _validate();
                                    _addEventAction();
                                    _advancedSetting();
                                    _advancedRegion();
                                })
                            }
                        });
                        d.showModal();
                    })
                }
            }
        })
    }
    //控制活动启动暂停
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
    //查看红包效果
    var _effect = function(id){
        var url = ajaxUrl.url5+'/'+id;
        $.ajaxJsonGet(url,null,{
            "done":function(res){
                if(res.code===0){
                    var data = res.data;
                    jumi.template('activity/activity_effect',data,function (tpl) {
                        var d = dialog({
                            title: '查看红包效果',
                            content:tpl,
                            width:820,
                            onshow:function(){
                                var params = {
                                    pageSize:10,
                                    activityId:id
                                };
                                var url = ajaxUrl.url4;
                                jumi.pagination('#pageToolbar',url,params,function(res,curPage){
                                    if(res.code===0){
                                        var data = {
                                            items:res.data.items
                                        };
                                        jumi.template('activity/activity_effect_list',data,function(tpl){
                                            $('#table_grid').html(tpl);
                                        })
                                    }

                                })
                            }
                        });
                        d.showModal();
                    })
                }
            }
        })
    }
    //查看红包活动
    var _watch = function(id,type){
        var url = ajaxUrl.url1+id;
        $.ajaxJsonGet(url,null,{
            "done":function(res){
                if(res.code===0){
                    var data = res.data;
                    jumi.template('activity/activity_c_redPaper?type='+type+'&edit=0',data,function (tpl) {
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
                                        _initCalculate(type)
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
    return {
        init: _init,
        watch:_watch,
        effect:_effect,
        del:_del,
        modify:_modify,
        moreOne:_moreOne,
        set:_set
    };
})();
