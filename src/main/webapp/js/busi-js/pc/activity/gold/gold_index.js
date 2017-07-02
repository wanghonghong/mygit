/**
 * Created by wxz on 16/12/13.
 */
CommonUtils.regNamespace("activity", "gold");

activity.gold = (function() {
    var ajaxUrl={
        url1:CONTEXT_PATH +'/card/byDateList', //礼券列表--有效期内
        url2:CONTEXT_PATH+'/activity/',  // 新增礼券活动接口
        url3:CONTEXT_PATH+'/activity/list',//查询活动列表
        url4: CONTEXT_PATH + '/card',   //礼券新建
        url5:CONTEXT_PATH+'/activity/user_count',//粉丝数量统计
        url6:CONTEXT_PATH+'/activity/money_list',//粉丝统计列表
        url7:CONTEXT_PATH+'/activity/money_count'//红包发放记录统计
    };
    var percentCount = 100;
    var percentSum = 0;
    var dg_goldredbag = null;
    var dg_cs_list = null;
    //初始化
    var _init = function(){
        _index_tab();
    };
    var _index_tab=function () {
        var tab = $("div[id='activity_gold_tab'] .m-goodsList-tab .m-tab ul li");
        var contab =  $(".panel-hid");
        tab.eq(0).addClass("z-sel").siblings().removeClass('z-sel');
        contab.hide().eq(0).show();
        activity.gift.gift_data_list();
        tab.click(function () {
            var index = $(this).index();
            tab.eq(index).addClass("z-sel").siblings().removeClass('z-sel');
            contab.hide();
            if(index===0){
                $("#gift_new").show();
                activity.gift.gift_data_list();
            }
            if(index===1){
                $("#gift_new").show();
                _actgold_redbags();
            }
            if(index===2){
                _actgold_list(1);
            }
            if(index===3){
                _actgold_list(0);
            }
            if(index===4){
                _actgold_list(3);
            }
        });
    };
    //新建活动
    var _actgold_redbags = function () {
        jumi.template('activity/gold/gold_redbags', function (tpl) {
            $("#gift_new").html(tpl);
            _redbag_tab();
        });
    };

    var _actgold_list = function (type) {
        jumi.template('activity/gold/gold_list', function (tpl) {
            $("#gold_status").show();
            $("#gold_status").html(tpl);
            _redbag_queryActivityList(type);
        });
    }

    var _redbag_tab = function () {
        var tab = $("div[id='gold_redbag_list'] ul li");
        tab.click(function () {
            var index = $(this).index();
            var type = $(this).data('type');
            var title=activity.gift.redbag_title(index);
            _gold_redbag_putOut(title,index,type);

        });
    };




//发红包
    var _gold_redbag_putOut = function (title,index,type) {
        jumi.template('activity/gold/gold_create?type='+type, function (tpl) {
            dg_goldredbag = dialog({
                title: title,
                content: tpl,
                width: 780,
                id: 'dialog_giftradbag',
                onclose: function () {
                    dialog.get('dialog_giftradbag').close().remove();
                },
                onshow:function () {
                    fansCanDiv(index);//筛选粉丝
                    _gold_calculate_template(index);
                    if(index!==1) {
                        jumi.setDataTime("#startTime");
                        jumi.setDataTime("#endTime");
                    }else{
                        jumi.setDataTime("#startTime");
                        $("#endTimeDiv").hide();
                    }
                    activity.gift.roleSetting();//角色选择
                    _advancedRegion();
                    _redbag_cardProperty();//选择礼券
                    _special_part(index); //首次关注发红包 发放限定
                    _validate();
                    _gold_data_save(index);
                }
            });
            dg_goldredbag.showModal();
        });
    };

    var fansCanDiv = function (index) {
        $("#fansCalDiv").hide();
        if(index===1){
            $("#fansCalDiv").show();
            _fanFilter();
        }

    };
    //粉丝筛选
    var _fanFilter = function(){
        $('#fansCal').click(function () {
            var qo = {};
            qo.sex = $('input[name="gold_sex"]:checked').val()||0;
            if($('input[name="roleLimit"]').is(":checked")){
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
            $('#choose_actgift_roles').find('.role-del').each(function(){
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
            $.ajaxJson(ajaxUrl.url5,qo,{
                "done":function(res){
                    if(res.code===0){
                        var count = res.data;
                        $('#fansNum').val(count);
                        $('#fansNum').blur();
                    }
                }
            })
        })
    }

    //0  2 4
    var _special_part = function (index) {
        var roletxt1 = $("#rolemsg_1");
        var roletxt2 = $("#rolemsg_2");
        var outlimitxt = $("#putout_limit");
        outlimitxt.hide();
        roletxt1.css("display", "none");
        roletxt2.css("display", "inline-block");
        switch(index){
            case 0:  //首次关注发红包
                roletxt1.css("display", "inline-block");
                roletxt2.css("display", "none");
                break;
            case 2:  //发放限定
                outlimitxt.show();
                $("#putout_unit").text("元");
                $("#putout_txt").attr("placeholder","购买商品达多少元");
                break;
            case 4:  //发放限定
                outlimitxt.show();
                $("#putout_unit").text("张");
                $("#putout_txt").attr("placeholder","礼券转赠达多少张");
                break;
        }
    }

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
    };

    //金额设定模板加载
    var _gold_calculate_template = function (index) {
        jumi.template('activity/gold/gold_calculate?status='+index, function (tpl) {
            $("#goldset_container").html(tpl);
            _validate();
            if(index==1){
                _computingOld();
            }else{
                _computing();
            }

        });
    };

    //已关注的粉丝
    var _computingOld = function(){
        //分配
        $('#fansNum').blur(function(){
            var fmount = $(this).val()||0;
            $('#fansNum').text(fmount);
            _calculate_fmount(fmount);
            _calculate_f_no_winners(fmount);
            _calculate_f_o_t_single();
            _calculate_fans();

        });
        $('input[data-id^="single_"]').unbind('blur').bind('blur',function(){
            var obj = $(this);
            var id = obj.attr('id');
            var index = $(this).data('index');
            var value = obj.val()||'-'//输入框的值
            var fmount = $('#fansNum').val()||0;
            _calculate_fmount(fmount);
            _calculate_f_o_single(index,value);
        })
        //获奖百分比设置重新计算
        $('input[data-id^="percent_"]').unbind('focus').bind('focus',function(){
            $('input[data-id^="single_"]').each(function () {
                $(this).parent().siblings().removeClass("u-txt-er1");
                $(".gift_percentShowTemp").remove();
            });
            $(this).parent().siblings().removeClass("u-txt-er1");
            $(".gift_percentShowTemp").remove();
        });
        $('input[data-id^="percent_"]').unbind('blur').bind('blur',function(){

            var fmount = $('#fansNum').val()||0;
            var total = _validatepercent();
            var ang = /^((?!0)\d+(\.\d{0,1})?)$/;
            var value = $(this).val();
            if(!ang.test(value)){
                _validateshow($(this),"百分比必须是一个大于零的整数");
            }else if(total!==percentCount){
                _validateshow($(this),"百分比总和需为100%");
            }
            _calculate_fmount(fmount);
            _calculate_f_no_winners(fmount);
            _calculate_f_o_t_single();
            _calculate_fans();
        });
        $('input[data-id^="single_"]').unbind('focus').bind('focus',function(){
            $('input[data-id^="percent_"]').each(function () {
                $(this).parent().siblings().removeClass("u-txt-er1");
                $(".gift_percentShowTemp").remove();
            });
            $(this).parent().siblings().removeClass("u-txt-er1");
            $(".gift_percentShowTemp").remove();
        });
        $('input[data-id^="single_"]').unbind('blur').bind('blur',function(){
            var obj = $(this);
            var index = obj.data('index');
            var ang = /^((?!0)\d+(\.\d{0,2})?)$/;
            var value = obj.val();//输入框的值
            var single_consume_value = $('li[data-id^="consume_'+index+'"]').text();//分配金额大小

            if(Number(single_consume_value)<Number(value)&&single_consume_value){
                var id = 'single_'+index;
                _validateshow(obj,"单人红包的价格不能大于消费金额");
            }else if(!ang.test(value)){
                var id = 'single_'+index;
                _validateshow(obj,"单人红包的价格最小需为1元");
            }else{
                obj.parent().removeClass("u-txt-er1");
            }
            var fmount = $('#fansNum').val()||0;
            _calculate_fmount(fmount);
            _calculate_f_no_winners(fmount);
            _calculate_f_o_t_single();
            _calculate_fans();
        });
        $('select[data-id^="percentage_"]').change(function(){
            var fmount = $('#fansNum').val()||0;
            _calculate_fmount(fmount);
            _calculate_f_no_winners(fmount);
            _calculate_fans();
        })

    };
    //计算不中奖人数
    var _calculate_f_no_winners = function(famount){

        $('li[data-id^="no_winning_"]').each(function(i){
            var k = i+1;
            var v = $('input[data-id^="percent_'+k+'"]').val();
            var val = parseInt(v.replace('%',''),10)/100;
            var consumeAmount = parseInt(famount*val,10);
            var PercentageValue = $('select[data-id^="percentage_'+k+'"]').find("option:selected").val();
            var no_winnersAmount = (PercentageValue===''||consumeAmount==='-')?'-':parseInt(PercentageValue*consumeAmount,10);
            $(this).text(no_winnersAmount);
        })
    };
    //老粉丝单组红包金额
    var _calculate_f_o_single = function(index,value){

        var winnner = $('li[data-id^="old_winners_'+index+'"]').text();
        var groupFmount = (value===''||value==='-')? '-': winnner*value;
        $('li[data-id^="consume_'+index+'"]').text(groupFmount);
        _calculate_f_amount();
    }

    var _calculate_f_o_t_single =  function() {
        $('input[data-id^="single_"]').each( function () {
            var obj = $(this);
            var id = obj.attr('id');
            var index = $(this).data('index');
            var value = obj.val() || '-'//输入框的值
            var fmount = $('#fansNum').val() || 0;
            _calculate_fmount(fmount);
            _calculate_f_o_single(index, value);
        })
    }
//老粉丝计算红包总金额
    var _calculate_f_amount= function(){
        var amount = 0;
        $('li[data-id^="consume_"]').each(function(i){
            var obj = $(this);
            var objValue = obj.text();
            var value = (objValue===''||objValue==='-')? 0:objValue;//输入框的值
            amount=amount+Number(value);
        })
        $('#red_amount').val(amount);
    }

    //老粉丝重新计算分批个数
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
    //老粉丝 计算礼券总数
    var _calculate_fans = function(){
        var cardfansCount = 0;
        $('li[data-id^="no_winning_"]').each(function(i){
            var value = $(this).text();
            var val = (value==='NaN'||value==='-')?0:value;
            fansCount = fansCount+Number(val);
            cardfansCount = cardfansCount + Number(val);
        });
        if(!isNaN(cardfansCount)){
            $('#totalCount').val(cardfansCount);
        }
    };
    //老粉丝清空单人红包金额和中奖人数
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
    //验证百分比
    var _validatepercent = function () {
        var percentSum =0;
        $('input[data-id^="percent_"]').each(function(){
            var value = $(this).val();
            var newvalue = Number(value);
            percentSum = percentSum+newvalue;
        });
        return percentSum;
    }

    var _validateshow = function (obj,msg) {
        obj.parent().addClass("u-txt-er1");
        $(".gift_percentShowTemp").remove();
        var html = $(".gift_percentShow").clone();
        html.css("display","static");
        html.removeClass("gift_percentShow");
        html.addClass("gift_percentShowTemp");
        html.find(".m-tip").append(msg);
        obj.parent().append(html);
    }
    
    //首次关注新粉的计算方法
    var _computing = function(){
        //礼券投放
      /*  $('#totalCount').unbind('blur').bind('blur',function(){
            var tCount = $(this).val()||0;
            $("#card_totalCount").text(tCount);
        });*/
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
        $('input[data-id^="percent_"]').unbind('focus').bind('focus',function(){
            $('input[data-id^="single_"]').each(function () {
                $(this).parent().siblings().removeClass("u-txt-er1");
                $(".gift_percentShowTemp").remove();
            });
            $(this).parent().siblings().removeClass("u-txt-er1");
            $(".gift_percentShowTemp").remove();
        });
        $('input[data-id^="percent_"]').unbind('blur').bind('blur',function(){

            var amount = $('#red_amount').val()||0;
            var total = _validatepercent();
            var ang = /^((?!0)\d+(\.\d{0,1})?)$/;
            var value = $(this).val();
            if(!ang.test(value)){
                _validateshow($(this),"百分比必须是一个大于零的整数");
            }else if(total!==percentCount){
                _validateshow($(this),"百分比总和需为100%");
            }
            _calculate_amount(amount);
            _calculate_winners(amount);
            _calculate_no_winners(amount);
            _calculate_fans();
        });
        $('input[data-id^="single_"]').unbind('focus').bind('focus',function(){
            $('input[data-id^="percent_"]').each(function () {
                $(this).parent().siblings().removeClass("u-txt-er1");
                $(".gift_percentShowTemp").remove();
            });
            $(this).parent().siblings().removeClass("u-txt-er1");
            $(".gift_percentShowTemp").remove();
        });
        $('input[data-id^="single_"]').unbind('blur').bind('blur',function(){
            var obj = $(this);
            var index = obj.data('index');
            var ang = /^((?!0)\d+(\.\d{0,2})?)$/;
            var value = obj.val();//输入框的值
            var single_consume_value = $('li[data-id^="consume_'+index+'"]').text();//分配金额大小

            if(Number(single_consume_value)<Number(value)&&single_consume_value){
                var id = 'single_'+index;
                _validateshow(obj,"单人红包的价格不能大于消费金额");
            }else if(!ang.test(value)){
                var id = 'single_'+index;
                _validateshow(obj,"单人红包的价格最小需为1元");
            }else{
                obj.parent().removeClass("u-txt-er1");
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
    //计算粉丝总人数
    var _calculate_fans = function(){
        var fansCount = 0;
        var cardfansCount = 0;
        $('li[data-id^="winners_"]').each(function(i){
            var value = $(this).text();
            var val = (value==='NaN'||value==='-')?0:value;
            fansCount = fansCount+Number(val);
        });
        $('li[data-id^="no_winning_"]').each(function(i){
            var value = $(this).text();
            var val = (value==='NaN'||value==='-')?0:value;
            fansCount = fansCount+Number(val);
            cardfansCount = cardfansCount + Number(val);
        });
        if(!isNaN(fansCount)){
            $('#activity_amount_num').text(fansCount);
        }
        if(!isNaN(cardfansCount)){
            $('#totalCount').val(cardfansCount);
            $("#card_totalCount").text(cardfansCount);

        }
    };
    //错误显示
    var _error = function(msg){
        $('#error').show();
        $('body').scrollTop(0);
        $('#tipError').text(msg);
    };
    //选择
    var _redbag_cardProperty = function () {
        $("#cardselect_btn").click(function () {
            _redbag_cardselect_dialog();

        });
    };
    var _redbag_cardselect_dialog =function () {
        jumi.template('activity/gold/gold_cardselect_list', function (tpl) {
            dg_cs_list = dialog({
                title: "礼券列表",
                content: tpl,
                width: 1135,
                id: 'dialog_cslist',
                onclose: function () {
                    dialog.get('dialog_cslist').close().remove();
                },
                onshow:function () {
                    _redbag_cardselect_list();
                }
            });
            dg_cs_list.showModal();
        });
    };

    var _redbag_cardselect_list =function () {
        var url = ajaxUrl.url1;
        var shopCardQo = {//  status:0,   //未发送
            pageSize: 10
        };
        jumi.pagination('#pageagcardToolbar', url, shopCardQo, function (res, curPage) {
            if (res.code === 0) {
                //判断是否第一页
                var data = {
                    items: res.data.items
                };
                if (curPage === 0) {
                    data.isFirstPage = 1;
                } else {
                    data.isFirstPage = 0;
                }
                jumi.template('activity/gold/gold_cardselect_list_con',data, function (tpl) {
                    $('#agcardselect_list').empty();
                    $('#agcardselect_list').html(tpl);
                    _redbag_gift_setback();
                    _card_item_del();
                });
            }
        });
    };
    //礼券选中对象设值---添加
    var _redbag_gift_setback=function () {
        $("#agcard_btn").click(function () {
            $("input[id^='agcard_check_']:checked").each(function () {
                var id = $(this).attr("id").replace("agcard_check_", "");
                var data = JSON.parse($("#agcard_item_dt_"+id).val());

                var divobj = $("div[id='card_content']");
                divobj.find("p").text(data.cardName);
                divobj.find("font").text(data.cardMoney);
                divobj.data("data-id",data.id);
                divobj.show();
            });
            if(_isNull(dg_cs_list)) {
                dg_cs_list.close().remove();
            }
        });

    };

    var _gold_data_save = function (index) {
        $("#actGold_save").click(function () {
            var activityCo = {};
            var activityConditionCo = {};
            var activityCardCo = {};
            var cardTpArray = [];
            var cardtypeArray = [];
            var activityName = $('#activityName').val();
            var startTime = $('#startTime').val();
            var endTime = $('#endTime').val();
            var blessings = $("#blessings").val();
            var noWinInfo = $('#noWinInfo').val();
            var winInfo = $('#winInfo').val();
            var cardId = $("div[id='card_content']").data("data-id");
            if(!_isNull(activityName)) {
                _error("请填写 活动标题");
                return;
            }
            if(!_isNull(blessings)) {
                _error("请填写 祝福语");
                return;
            }
            //验证百分比相加是否为100%
            $('input[data-id^="percent_"]').each(function(){
                var value = $(this).val();
                var newvalue = Number(value);
                percentSum = percentSum+newvalue;
            });
            if(percentSum!==percentCount){
                _error('设置获奖百分比有误,请重新设置');
                percentSum = 0;//重置
                return;
            }else{
                percentSum = 0;//重置
            }
            if(!_isNull(winInfo)) {
                _error("请填写 现金红包提示语");
                return;
            }
            if(!_isNull(noWinInfo)) {
                _error("请填写 礼券红包提示语");
                return;
            }
            if(index===1){
                if (!_isNull(startTime)) {
                    _error("请填写活动时间");
                    return;
                }
            }else {
                if ((!_isNull(startTime) || !_isNull(endTime))) {
                    _error("请设定 开始与结束时间");
                    return;
                }
            }
            activityCo.takeType = $('input[name="show_form"]:checked').val();
            activityCo.shakeCount = $('input[name="showgl"]:checked').val();
            activityCo.activityName = activityName;
            activityCo.blessings = blessings;
            activityCo.startTime=startTime;
            activityCo.endTime=endTime;
            activityCo.platform = 1;
            activityCo.subType = parseInt(index)+1;
            activityCo.type = 3;
            activityCo.cardType = 1;//单
            activityCo.totalMoney = Number($('#red_amount').val())*100;
            if(index!==1){
                activityCo.preFansCount = $('#activity_amount_num').text();
            }else {
                activityCo.preFansCount = $('#fansNum').text();
            }
            activityCo.noWinInfo = noWinInfo;
            activityCo.winInfo = winInfo;
            activityCo.status = 0;

         //高级设置
            activityConditionCo.buyMoney = 0;
            if(index===2){
                activityConditionCo.buyMoney = Number($("#putout_txt").val())||0; //购买商品达多少
            }
            if(index===4){
                activityConditionCo.buyMoney = Number($("#putout_txt").val())||0; //礼券转赠达多少
            }
            activityConditionCo.sex = $('input[name="gold_sex"]:checked').val()||0;
            var roles = [];
            var isRoleLimit = $('input[name="roleLimit"]').is(":checked");
            if(isRoleLimit){
                $('#choose_actgift_roles').find('.role-del').each(function(){
                    var id = $(this).data('id');
                    if(id=='99'){
                        activityConditionCo.isBuy = id;
                    }else{
                        activityConditionCo.isBuy = 0;
                        roles.push(id);
                    }
                });
                roles = roles.join(',');
            }
            if(isRoleLimit){
                if(index!==0){
                    activityConditionCo.roleLimit = 1;
                    activityConditionCo.roles = roles;
                    activityConditionCo.downRoleLimit = 0;
                }
                else{
                    activityConditionCo.downRoleLimit = 1;
                    activityConditionCo.downRoles = roles;
                    activityConditionCo.roleLimit = 0;
                }
            }else{
                activityConditionCo.roleLimit = 0;
                activityConditionCo.downRoleLimit = 0;
            }

            if(activityConditionCo.roleLimit === 1 && activityConditionCo.roles.length===0){
                _error("请选择角色");
                return;
            }
            if(activityConditionCo.downRoleLimit === 1 && activityConditionCo.downRoles.length===0){
                _error("请选择角色");
                return;
            }

            if($('input[name="activity_area"]').is(":checked")){
                activityConditionCo.areaLimit = 1;
            }else{
                activityConditionCo.areaLimit = 0;
            }
            activityConditionCo.areaNames = $('input[name="activity_area_name"]').val();
            activityConditionCo.areaIds = $('input[name="activity_area_ids"]').val();
            activityCo.activityConditionCo =activityConditionCo;
            if(activityConditionCo.areaLimit === 1 && !_isNull(activityConditionCo.areaNames)){
                _error("请选择地区");
                return;
            }
            //礼券
            if(!_isNull(cardId)){
                _error("请选择礼券");
                return;
            }
            activityCardCo.cardId = cardId;
            activityCardCo.totalCount =$("#totalCount").val()||0;
            cardTpArray.push(activityCardCo);
            activityCo.activityCardList = cardTpArray;
            //金额设定
            for(var i=1;i<5;i++){
                var activitySub = {};
                activitySub.winScale = $('input[data-id^="percent_'+i+'"]').val();
                activitySub.consumeMoney = Number($('li[data-id^="consume_'+i+'"]').text())*100;
                activitySub.redMoney = Number($('input[data-id^="single_'+i+'"]').val())*100;
                if(index!==1){
                    activitySub.winCount = $('li[data-id^="winners_'+i+'"]').text();
                }else{
                    activitySub.winCount = $('li[data-id^="old_winners_'+i+'"]').text();
                    activitySub.oldFansCount =$('li[data-id^="batches_'+i+'"]').text();
                }
                activitySub.noWinScale = $('select[data-id^="percentage_'+i+'"]').find("option:selected").val();
                activitySub.noWinCount = $('li[data-id^="no_winning_'+i+'"]').text();
                activitySub.seq = i;
               // activitySub.preFansCount = $('#activity_amount_num').text();
                cardtypeArray.push(activitySub);
            }
            activityCo.subActivityList =cardtypeArray;
            var flag = _isFillFull(cardtypeArray);
            var jsonData = JSON.stringify(activityCo);
            if(flag){
                _gold_data_post(jsonData);
            }else{
                _error('金额设定未填写完整,请重新设置');
            }
        });
    };

    var _gold_data_post = function (jsonData) {
        $.ajaxJson(ajaxUrl.url2,jsonData,{
            "done":function (res) { console.log(res);
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
                        _gold_data_makesure(jsonData,"",res.data.msg,"add");
                    }else{
                        var dm = new dialogMessage({
                            type:1,
                            fixed:true,
                            msg:res.data.msg,
                            isAutoDisplay:true,
                            time:1000
                        });
                        dm.render();
                        setTimeout(function(){
                            dialog.get('dialog_giftradbag').close().remove();
                        },1500)
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
    };

    /**
     * 二次确认保存
     *jsonData 数据源
     * status 刷新参数
     * msg 返回信息提示
     * tag  新增:"add"  修改:"edit"
     */
    var _gold_data_makesure = function (jsonData,status,msg,tag) {
        var args = {};
        args.fn1 = function(){
            if(tag==="add"){
                _gold_data_post(jsonData);
            }else if(tag==="edit"){
                _gold_data_udpate(jsonData,status);
            }

        };
        args.fn2 = function(){
        };
        jumi.dialogSure(msg,args);
    };

    //判断是否满足数据提交要求
    _isFillFull = function(arrayData){
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


    
    //礼券红包列表
    var _redbag_queryActivityList = function(redType){
        var activityQo = {};
        activityQo.pageSize = 10;
        activityQo.type = 3;
        if(redType===1){
            activityQo.status = '1,2';
        }else{
            activityQo.status = redType;
        }
        jumi.pagination('#gold_redbag_pageToolbar',ajaxUrl.url3,activityQo,function(res,curPage){
            if(res.code===0){
                var data = {
                    items:res.data.items
                };
                for(var i=0,count = data.length;i<count;i++){
                  var total =  data.items[i].totalMoney;
                    data.items[i].totalMoney = total/100;
                }
                jumi.template('activity/gold/gold_list_content?status='+redType,data,function(tpl){
                    $('#gold_status_list').html(tpl);
                })
            }
        })
    };

    var _redbag_modify = function (id,subtype,status) {
        var index =subtype-1;
        var title = activity.gift.redbag_title(index);;
      _redbag_putOut_edit(id,title,index,status,subtype);

    }

    //发红包
    var _redbag_putOut_edit = function (id,title,index,status,subtype) {
        var url = ajaxUrl.url2 + id;
        $.ajaxJsonGet(url, null, {
            "done": function (res) {
                if (res.code === 0) {
                    var data = res.data;
                    jumi.template('activity/gold/gold_create?type='+subtype,data,function (tpl) {
                        dg_goldredbag = dialog({
                            title: title,
                            content: tpl,
                            width: 780,
                            id: 'dialog_giftradbag',
                            onclose: function () {
                                dialog.get('dialog_giftradbag').close().remove();
                            },
                            onshow: function () {
                                var dtd = $.Deferred();
                                var wait = function(dtd){
                                    var tasks = function(){
                                        dtd.reject();
                                    };
                                    return dtd;
                                };
                                $.when(
                                    wait(
                                        _gold_calculate_template(index)
                                    )).done(function(){
                                    fansCanDiv(index);//筛选粉丝
                                    if(index!==1) {
                                        jumi.setDataTime("#startTime");
                                        jumi.setDataTime("#endTime");
                                    }else{
                                        jumi.setDataTime("#startTime");
                                        $("#endTimeDiv").hide();
                                    }
                                    activity.gift.roleSetting();//角色选择
                                    _advancedRegion();
                                    _redbag_cardProperty();//选择礼券
                                    _special_part(index); //首次关注发红包 发放限定
                                    _setData(data,index,"edit");
                                    _validate();
                                    _gold_data_edit(id,index,status);//编辑
                                })
                            }
                        });
                        dg_goldredbag.showModal();
                    });

                } else {
                    _gift_optMsg(2, "对不起，操作失败");
                }
            },
            "fail": function (res) {
            }
        });
    };
//optTag :"view" "edit" "again"
    var _setData = function (data,index,optTag) {

        var array = [];
        var tpl = '';
        $('input[name="show_form"][value="'+data['subType']+'"]').prop('checked',true);
        $('input[name="showgl"][value="'+data['shakeCount']+'"]').prop('checked',true);
        //红包设置
        $("#activityName").val(data.activityName);
        $("#blessings").val(data.blessings);
        //高级设置
        if(index===2){ //购买商品发红包
            $("#putout_limit").show();
            $("#putout_unit").text("元");
            $("#putout_txt").val(data.activityCondition.buyMoney);
        }
        if(index===4){ //礼券转赠发红包
            $("#putout_limit").show();
            $("#putout_unit").text("张");
            $("#putout_txt").val(data.activityCondition.buyMoney);
        }
        $("input[id^='sex_']").prop("checked","");
        $("input[id='sex_"+data.activityCondition.sex+"']").prop("checked","checked");

        if(index!==0){
            if(data['activityCondition']['roleLimit']==1){
                $('input[name="roleLimit"]').prop('checked',true);
                array = data.activityCondition.roles.split(',');
                if(data['activityCondition']['isBuy']===99) {
                    if(array.length>0){
                        array[array.length] = '99';
                    }else{
                        array.push('99').split(',');
                    }
                }
                activity.gift.fillFoles(array);
            }else{
                $('input[name="roleLimit"]').prop('checked',false);
            }
        }else{
            if(data['activityCondition']['downRoleLimit']==1){
                $('input[name="roleLimit"]').prop('checked',true);
                array = data.activityCondition.downRoles.split(',');
                activity.gift.fillFoles(array);
            }else{
                $('input[name="roleLimit"]').prop('checked',false);
            }
        }

        $('input[name="activity_area_ids"]').val(data['activityCondition']['areaIds']);
        $('input[name="activity_area_name"]').val(data['activityCondition']['areaNames']||'');
        if(data['activityCondition']['areaNames']){
            $('input[name="activity_area"]').prop('checked',true);
            var names = data['activityCondition']['areaNames'].split(',');
            _.map(names,function(k,v){
                tpl+='<div class="u-btn-smltgry u-btn-smltgry-1"><span>'+k+'</span></div>';
            })
            $('#choose_activity_areas').append(tpl);
        }
        //金额设定
        if(data.activitySubList) {
            $.each(data.activitySubList, function (i) {
                var j = i + 1;
                var cm = data.activitySubList[i].consumeMoney / 100;
                var single = data.activitySubList[i].redMoney / 100;
                if (data.subType === 2) {
                    $('li[data-id^="batches_' + j + '"]').text(data.activitySubList[i].winCount + data.activitySubList[i].noWinCount);
                }
                $('input[data-id^="percent_' + j + '"]').parent().attr('id', data.activitySubList[i].id);
                $('input[data-id^="percent_' + j + '"]').val(data.activitySubList[i].winScale);
                $('li[data-id^="consume_' + j + '"]').text(cm);
                $('input[data-id^="single_' + j + '"]').val(single);
                if(index!==1){  //老粉丝
                    $('li[data-id^="winners_' + j + '"]').text(data.activitySubList[i].winCount);
                }else{
                    $('li[data-id^="old_winners_'+j+'"]').text(data.activitySubList[i].winCount);
                    $('li[data-id^="batches_'+j+'"]').text(data.activitySubList[i].oldFansCount);
                }
                $('li[data-id^="no_winning_' + j + '"]').text(data.activitySubList[i].noWinCount);
                $('select[data-id^="percentage_' + j + '"').find("option[value='" + data.activitySubList[i].noWinScale + "']").attr("selected", true);
            });
            $("#amount").text(data.totalMoney / 100);
            if(_isNull(data.activityCardList)&&data.activityCardList.length>0){
                $("#card_totalCount").text(data.activityCardList[0].totalCount);
            }

            if(index!==1){
                $('#activity_amount_num').text(data.preFansCount);
            }else {
                $('#fansNum').val(data.preFansCount);
            }
        }
        $("#red_amount").val(data.totalMoney/100);
        //礼券调用
        if(_isNull(data.activityCardList)&&data.activityCardList.length>0){
            $("#totalCount").val(data.activityCardList[0].totalCount);
            _redbag_gift_setbackbyedit(data,optTag);//礼券调用--回填
        }
        $('#winInfo').val(data.winInfo);
        $('#noWinInfo').val(data.noWinInfo);
        if(optTag!=="again") {
            $('#startTime').val(data.startTime);
            $('#endTime').val(data.endTime);
        }
    }

    var _redbag_gift_setbackbyedit = function (dtdata,optTag) {
       var activityCard = dtdata.activityCardList[0];
        var actCardId = activityCard.id;
        $.ajaxJsonGet(ajaxUrl.url4+"/"+activityCard.cardId, {
            "done": function (res) {
                if (res.code === 0) {
                    var data = res.data;
                    if(data.periodType===2 && activity.gift.giftact_checkStartEndTime(data.startTime,data.endTime) && optTag!=="view") { // 自定义有效期
                        _gold_setCard(data,actCardId);
                    }else if(data.periodType===2 && optTag==="view"){ //查看不过滤过期时间
                        _gold_setCard(data,actCardId);
                    }else if(data.periodType===1 ) { // 固定有效期
                        _gold_setCard(data,actCardId);
                    }
                } else {
                    _gift_optMsg(2,"对不起，操作失败");
                }
            },
            "fail": function (res) {
            }
        });
    };

    var _gold_setCard = function (data,actCardId) {
        var divobj = $("div[id='card_content']");
        divobj.find("p").text(data.cardName);
        divobj.find("font").text(data.cardMoney);
        divobj.data("data-id",data.id);
        divobj.data("data-relationId",actCardId);
        divobj.show();
        _card_item_delbyEdit();
    }
    var _card_item_delbyEdit = function () {
        $("#card_icon").click(function () {
            var pntDiv = $(this).parent();
            var relationId = pntDiv.data("data-relationId");
            if(relationId) {//若存在
                $("#delstr").text(relationId);
            }
            var divobj = $(this).parent();
            divobj.find("p").text("");
            divobj.find("font").text("");
            divobj.data("data-id","");
            divobj.data("data-relationId","");
            divobj.hide();
        });
    };

    var _card_item_del = function () {
        $("#card_icon").click(function () {
            var divobj = $(this).parent();
            divobj.find("p").text("");
            divobj.find("font").text("");
            divobj.data("data-id","");
            divobj.data("data-relationId","");
            divobj.hide();
        });
    };
   //金券编辑
    var _gold_data_edit = function (id,index,status) {
        $("#actGold_save").click(function () {
            var activityUo = {};
            var activityConditionUo = {};
            var activityCardUo = {};
            var cardTpArray = [];
            var cardtypeArray = [];
            var activityName = $('#activityName').val();
            var startTime = $('#startTime').val();
            var endTime = $('#endTime').val();
            var blessings = $("#blessings").val();
            var noWinInfo = $('#noWinInfo').val();
            var winInfo = $('#winInfo').val();
            var cardobj = $("div[id='card_content']");
            var cardId = cardobj.data("data-id");
            var relationid = cardobj.data("data-relationId");
            var delId = $("#delstr").text();

            if(!_isNull(activityName)) {
                _error("请填写 活动标题");
                return;
            }
            if(!_isNull(blessings)) {
                _error("请填写 祝福语");
                return;
            }
            //验证百分比相加是否为100%
            $('input[data-id^="percent_"]').each(function(){
                var value = $(this).val();
                var newvalue = Number(value);
                percentSum = percentSum+newvalue;
            });
            if(percentSum!==percentCount){
                _error('设置获奖百分比有误,请重新设置');
                percentSum = 0;//重置
                return;
            }else{
                percentSum = 0;//重置
            }
            if(!_isNull(winInfo)) {
                _error("请填写 现金红包提示语");
                return;
            }
            if(!_isNull(noWinInfo)) {
                _error("请填写 礼券红包提示语");
                return;
            }
            if(index===1){
                if (!_isNull(startTime)) {
                    _error("请填写活动时间");
                    return;
                }
            }else {
                if ((!_isNull(startTime) || !_isNull(endTime))) {
                    _error("请设定 开始与结束时间");
                    return;
                }
            }
            activityUo.takeType = $('input[name="show_form"]:checked').val();
            activityUo.shakeCount = $('input[name="showgl"]:checked').val();
            activityUo.activityName = activityName;
            activityUo.blessings = blessings;
            activityUo.startTime=startTime;
            activityUo.endTime=endTime;
            activityUo.platform = 1;
            activityUo.subType = parseInt(index)+1;
            activityUo.type = 3;
            activityUo.cardType = 1;//单
            activityUo.totalMoney = Number($('#red_amount').val())*100;
            if(index!==1){
                activityUo.preFansCount = $('#activity_amount_num').text();
            }else {
                activityUo.preFansCount = $('#fansNum').text();
            }
            activityUo.noWinInfo = noWinInfo;
            activityUo.winInfo = winInfo;
            activityUo.id = id;
            //高级设置
            activityConditionUo.buyMoney = 0;
            if(index===2){
                activityConditionUo.buyMoney = Number($("#putout_txt").val())||0; //购买商品达多少
            }
            if(index===4){
                activityConditionUo.buyMoney = Number($("#putout_txt").val())||0; //礼券转赠达多少
            }
            activityConditionUo.sex = $('input[name="gold_sex"]:checked').val()||0;
            var roles = [];
            var isRoleLimit = $('input[name="roleLimit"]').is(":checked");
            if(isRoleLimit){
                $('#choose_actgift_roles').find('.role-del').each(function(){
                    var id = $(this).data('id');
                    if(id=='99'){
                        activityConditionUo.isBuy = id;
                    }else{
                        activityConditionUo.isBuy = 0;
                        roles.push(id);
                    }
                });
                roles = roles.join(',');
            }
            if(isRoleLimit){
                if(index!==0){
                    activityConditionUo.roleLimit = 1;
                    activityConditionUo.roles = roles;
                    activityConditionUo.downRoleLimit = 0;
                }
                else{
                    activityConditionUo.downRoleLimit = 1;
                    activityConditionUo.downRoles = roles;
                    activityConditionUo.roleLimit = 0;
                }
            }else{
                activityConditionUo.roleLimit = 0;
                activityConditionUo.downRoleLimit = 0;
            }

            if(activityConditionUo.roleLimit === 1 && activityConditionUo.roles.length===0){
                _error("请选择角色");
                return;
            }
            if(activityConditionUo.downRoleLimit === 1 && activityConditionUo.downRoles.length===0){
                _error("请选择角色");
                return;
            }

            if($('input[name="activity_area"]').is(":checked")){
                activityConditionUo.areaLimit = 1;
            }else{
                activityConditionUo.areaLimit = 0;
            }
            activityConditionUo.areaNames = $('input[name="activity_area_name"]').val();
            activityConditionUo.areaIds = $('input[name="activity_area_ids"]').val();
            activityUo.activityConditionUo =activityConditionUo;
            if(activityConditionUo.areaLimit === 1 && !_isNull(activityConditionUo.areaNames)){
                _error("请选择地区");
                return;
            }
            //礼券
            if(_isNull(delId)){
                activityUo.delIds = delId; //删除
            }else{
                if(_isNull(relationid)){
                    activityCardUo.id =relationid;
                }
            }
            if(!_isNull(cardId)){
                _error("请选择礼券");
                return;
            }
            activityCardUo.cardId=cardId;
            activityCardUo.totalCount =$("#totalCount").val()||0;
            cardTpArray.push(activityCardUo);
            activityUo.activityCardList = cardTpArray;
            //金额设定
            for(var i=1;i<5;i++){
                var activitySub = {};
                activitySub.id=$('input[data-id^="percent_' + i + '"]').parent().attr('id');
                activitySub.winScale = $('input[data-id^="percent_'+i+'"]').val();
                activitySub.consumeMoney = Number($('li[data-id^="consume_'+i+'"]').text())*100;
                activitySub.redMoney = Number($('input[data-id^="single_'+i+'"]').val())*100;

                if(index!==1){
                    activitySub.winCount = $('li[data-id^="winners_'+i+'"]').text();
                }else{
                    activitySub.winCount = $('li[data-id^="old_winners_'+i+'"]').text();
                    activitySub.oldFansCount =$('li[data-id^="batches_'+i+'"]').text();
                }

                activitySub.noWinScale = $('select[data-id^="percentage_'+i+'"]').find("option:selected").val();
                activitySub.noWinCount = $('li[data-id^="no_winning_'+i+'"]').text();
                activitySub.seq = i;
                //activitySub.preFansCount = $('#activity_amount_num').text();
                cardtypeArray.push(activitySub);
            }
            activityUo.activitySubList =cardtypeArray;
            var flag = _isFillFull(cardtypeArray);
            var jsonData = JSON.stringify(activityUo);
            if(flag){
                _gold_data_udpate(jsonData,status);
            }else{
                _error('数据未填写完整,请重新设置');
            }
        });
    };
    var _gold_data_udpate = function (jsonData,status) {
        $.ajaxJsonPut(ajaxUrl.url2,jsonData,{
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
                        _gold_data_makesure(jsonData,status,res.data.msg,"edit");
                    }else{
                        var dm = new dialogMessage({
                            type:1,
                            fixed:true,
                            msg:res.data.msg,
                            isAutoDisplay:true,
                            time:1000
                        });
                        dm.render();
                        setTimeout(function(){
                            dialog.get('dialog_giftradbag').close().remove();
                        },1500)
                        _gold_List_refresh(status);
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
    };
    //删除活动
    var _redbag_del = function(id,name,status){
        var url = ajaxUrl.url2+id;
        //  var status = $('#activity_tab').find('.z-sel').data('index');
       // var tab = $("div[id='activity_gift_tab'] .m-goodsList-tab .m-tab ul").find('.z-sel').data('index');
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
                        _gold_List_refresh(status);
                    }
                }
            });
        };
        args.fn2 = function(){
        };
        jumi.dialogSure('是否取消【'+name+'】的活动',args);
    };

    var _redbag_set = function (id,name,status) {
        var url = ajaxUrl.url2+'pause/'+id;
        msg = (status==='1')?'确定暂停【'+name+'】的活动吗?':'确定重新启动【'+name+'】的活动吗?';
        var args = {};
        args.fn1 = function(){
            _redbag_Action(url,id,status);
        };
        args.fn2 = function(){
        };
        jumi.dialogSure(msg,args);
    };

    //开启或暂停活动
    var _redbag_Action = function(url,id,status){
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
                    _gold_List_refresh(Number(status));
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

    var _redbag_view = function (id,type) {
        var index =type-1;
        var title = activity.gift.redbag_title(index);
        var url = ajaxUrl.url2 + id;
        $.ajaxJsonGet(url, null, {
            "done": function (res) {
                if (res.code === 0) {
                    var data = res.data;
                    _redbag_view_dg(id,title,index,data);
                } else {
                    _gift_optMsg(2, "对不起，操作失败");
                }
            },
            "fail": function (res) {
            }
        });
    };
    var _redbag_view_dg = function (id,title,index,data) {
        jumi.template('activity/gold/gold_create', function (tpl) {
            dg_goldredbag = dialog({
                title: title,
                content: tpl,
                width: 780,
                id: 'dialog_giftradbag',
                onclose: function () {
                    dialog.get('dialog_giftradbag').close().remove();
                },
                onshow: function () {
                    var dtd = $.Deferred();
                    var wait = function(dtd){
                        var tasks = function(){
                            dtd.reject();
                        };
                        return dtd;
                    };
                    $.when(
                        wait(
                            _gold_calculate_template(index)
                        )).done(function(){
                        if(index!==1) {
                            jumi.setDataTime("#startTime");
                            jumi.setDataTime("#endTime");
                        }else{
                            jumi.setDataTime("#startTime");
                            $("#endTimeDiv").hide();
                        }
                        //activity.gift.roleSetting();//角色选择
                        //_advancedRegion();
                       // _redbag_cardProperty();//选择礼券
                        _special_part(index); //首次关注发红包 发放限定
                        _setData(data,index,"view");
                        $("#actGold_save").hide();
                    })
                }
            });
            dg_goldredbag.showModal();
        });
    };

    var _redbag_againOne = function (id,type) {
        var index =type-1;
        var title = activity.gift.redbag_title(index);
        var url = ajaxUrl.url2 + id;
        $.ajaxJsonGet(url, null, {
            "done": function (res) {
                if (res.code === 0) {
                    var data = res.data;
                    _redbag_againOne_dg(id,title,index,data);
                } else {
                    _gift_optMsg(2, "对不起，操作失败");
                }
            },
            "fail": function (res) {
            }
        });
    };
    var _redbag_againOne_dg = function (id,title,index,data) {
        jumi.template('activity/gold/gold_create', function (tpl) {
            dg_goldredbag = dialog({
                title: title,
                content: tpl,
                width: 780,
                id: 'dialog_giftradbag',
                onclose: function () {
                    dialog.get('dialog_giftradbag').close().remove();
                },
                onshow: function () {
                    var dtd = $.Deferred();
                    var wait = function(dtd){
                        var tasks = function(){
                            dtd.reject();
                        };
                        return dtd;
                    };
                    $.when(
                        wait(
                            _gold_calculate_template(index)
                        )).done(function(){
                        fansCanDiv(index);//筛选粉丝
                        if(index!==1) {
                            jumi.setDataTime("#startTime");
                            jumi.setDataTime("#endTime");
                        }else{
                            jumi.setDataTime("#startTime");
                            $("#endTimeDiv").hide();
                        }
                        activity.gift.roleSetting();//角色选择
                        _advancedRegion();
                        _redbag_cardProperty();//选择礼券
                        _special_part(index); //首次关注发红包 发放限定
                        _setData(data,index,"again");
                        _validate();
                        _gold_data_save(index);//保存
                    })
                }
            });
            dg_goldredbag.showModal();
        });
    };

//礼券红包列表刷新
    var _gold_List_refresh = function(redType){
        var curPage = $("#gold_redbag_pageToolbar_page").val();
        var activityQo = {};
        activityQo.pageSize = 10;
        activityQo.type = 3;
        activityQo.curPage=curPage || 0;
        if(redType===1||redType===2){
            activityQo.status = '1,2';
        }else{
            activityQo.status = redType;
        }
        $.ajaxJson(ajaxUrl.url3,activityQo, {
            "done": function (res) {
                if (res.code === 0) {
                    var data = {
                        items: res.data.items
                    }
                    jumi.template('activity/gold/gold_list_content?status='+redType,data,function(tpl){
                        $('#gold_status_list').html(tpl);
                    })
                }
            }
        })
    };

    //验证数据函数
    var _validate = function(){
        $("#form1").validate({
            rules: {
                red_amount:{
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
        });

    };
    function _isNull(data) {
        return (data == "" || data == undefined || data == null) ? false : true;
    };
    //查看红包效果
    var _gold_effect = function(id){
        var url = ajaxUrl.url7+'/'+id;
        $.ajaxJsonGet(url,null,{
            "done":function(res){
                if(res.code===0){
                    var data = res.data;
                    jumi.template('activity/gold/gold_effect',data,function (tpl) {
                        var d = dialog({
                            title: '查看红包效果',
                            content:tpl,
                            width:820,
                            onshow:function(){
                                _gold_effect_list(1,'activity/gold/gold_effetct_card_list',id);
                            }
                        });
                        d.showModal();
                        _gold_effect_tab(id);
                    })
                }
            }
        });

        var _gold_effect_tab=function (id) {
            var tab = $("div[id='m-tab1'] ul li");
            var con = $("div[id='gold_effect_tab'] .table-hander");
            tab.eq(0).addClass("z-sel").siblings().removeClass('z-sel');
            tab.click(function () {
                var index = $(this).index();
                tab.eq(index).addClass("z-sel").siblings().removeClass('z-sel');
                con.hide();
                con.eq(index).show();
                if(index===0){
                    var flag = 1;
                    _gold_effect_list(flag,'activity/gold/gold_effetct_card_list',id);
                }
                if(index===1){
                    var flag = 2;
                    _gold_effect_list(flag,'activity/gold/gold_effect_money_list',id);
                }

            });
        };

        var _gold_effect_list = function (flag,tpl,id) {
            var activityUserEo = {
                pageSize:10,
                activityId:id
            };
            var url = ajaxUrl.url6+'?flag='+flag;
            jumi.pagination('#gold_pageToolbar', url, activityUserEo, function (res, curPage) {
                if (res.code === 0) {
                    //判断是否第一页
                    var data = {
                        items: res.data.items
                    };
                    if (curPage === 0) {
                        data.isFirstPage = 1;
                    } else {
                        data.isFirstPage = 0;
                    }

                    jumi.template(tpl,data, function (tpl) {
                        $("#gold_table_grid").html(tpl);
                    });
                }
            });
        }
    };
    return {
        init:_init,
        redbag_modify:_redbag_modify,
        redbag_del:_redbag_del,
        redbag_set:_redbag_set,
        redbag_view:_redbag_view,
        redbag_againOne:_redbag_againOne,
        advancedRegion:_advancedRegion,
        gold_effect:_gold_effect
    };
})();