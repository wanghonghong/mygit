/**
 * Created by ray on 16/10/19.
 */
CommonUtils.regNamespace("awardpoint", "seniorSetting");
awardpoint.seniorSetting = (function() {
    var ajaxUrl = {
        url1:CONTEXT_PATH+'/integral/set'
    };
    var _init = function(){
        _bind();
        _loadSetup();
    };
    var _initSwitchButton = function( id,isOpen){
        var id = id;
        var isOpen = isOpen;
        var switchButton = new switchControl(id, {
            state: isOpen,
            onSwitchChange:function(v){
                if(v){
                    $(id).val(1);
                }
                else{
                    $(id).val(0);
                }
            }
        });
        if(isOpen){
            $(id).val(1);
        }else{
            $(id).val(0);
        }
        switchButton.render();
    }
    var _validate = function(){
        $("#award_senior_form").validate({
            rules: {
                pay_one: {
                    required: true,
                    digits:true,
                    isIntGtZero:true
                },
                pay_two: {
                    required: true,
                    digits:true,
                    isIntGtZero:true
                },
                pay_three: {
                    required: true,
                    digits:true,
                    isIntGtZero:true
                },
                awardpoint_minimum:{
                    required:true,
                    digits:true
                },
                award_buymoney:{
                    required: true,
                    digits:true,
                    isDivisible:true
                },
                award_Integral:{
                    required: true,
                    digits:true,
                    isIntGtZero:true
                }
            },
            messages: {
                pay_one: {
                    required: "请输入充值金额",
                    isIntGtZero:'金额必须是一个大于零的整数',
                    digits:'金额必须是一个大于零的整数'
                },
                pay_two: {
                    required: "请输入充值金额",
                    isIntGtZero:'金额必须是一个大于零的整数',
                    digits:'金额必须是一个大于零的整数'
                },
                pay_three: {
                    required:"请输入充值金额",
                    isIntGtZero:'金额必须是一个大于零的整数',
                    digits:'金额必须是一个大于零的整数'
                },
                awardpoint_minimum:{
                    required:"请输入提现金额",
                    digits:'提现金额必须是一个大于零的整数',
                },
                award_buymoney:{
                    required:"请输入返利金额",
                    isIntGtZero:'返利金额必须是一个大于零的整数',
                    digits:'返利金额必须是一个大于零的整数',
                    isDivisible:'返利金额必须是1、10、100的倍数'
                },
                award_Integral:{
                    required:"请输入返还积分数值",
                    isIntGtZero:'返还积分数值必须是一个大于零的整数',
                    digits:'返还积分数值必须是一个大于零的整数'
                }
            }
        })
    }
    var _bind = function(){
        $('#awardpoint_senior_setup li').click(function () {
            var index = $(this).data('index');
            $(this).addClass('z-sel').siblings().removeClass('z-sel');
            _queryPage(index);//加载不同的选项卡模版
        });
        $('#awardpoint_content').on('click','#award_setting_save',function(){
            var args = {};
            var isplay = $('#awardpoint_ispay').val();
            var form = $( "#award_senior_form" );
            if(form.valid()){
                args.fn1 = function(){
                    var id = $('#award_senior_point_id').val();
                    if(id){
                        _doEditSubmit();
                    }else{
                        _doSubmit();
                    }
                }
                args.fn2 = function(){

                };
                jumi.dialogSure('确定保存积分高级设置吗?',args);
            }




        })

    }
    var _loadSetup = function(){
        $.ajaxJsonGet(ajaxUrl.url1,null,{
            "done":function(res){
                if(res.code===0){
                    var data = res.data.data;
                    var id = res.data.data.id;
                    if(!id){
                        jumi.template('awardpoint/awardpoint_senior_setting',function(html){
                            $('#awardpoint_content').html(html);
                            _validate();
                            _initSwitchButton('#awardpoint_iscash',0);//开通提现功能
                            _initSwitchButton('#awardpoint_ispay',0);//开通积分功能
                            _initSwitchButton('#awardpoint_isbuy',0);//开通积分可商品换购
                        })
                    }else{
                        var isPay = data.isPay;
                        var isBuy = data.isBuy;
                        var uName = data.unitName;
                        jumi.template('awardpoint/awardpoint_senior_setting',data,function(html){
                            $('#awardpoint_content').html(html);
                            _validate();
                            $('.award-unit').text(uName);
                            $('input[name="pay_one"]').val(data.onePayMoney);
                            $('input[name="pay_two"]').val(data.twoPayMoney);
                            $('input[name="pay_three"]').val(data.threePayMoney);
                            $('input[name="award_buymoney"]').val(data.buyMoney);
                            $('input[name="award_Integral"]').val(data.returnIntegral);
                            _initSwitchButton('#awardpoint_ispay',isPay);//开通积分功能
                            _initSwitchButton('#awardpoint_isbuy',isBuy);//开通积分可商品换购
                        })
                    }
                }
            }
        })
    }
    //数据编辑
    var _doEditSubmit = function(){
        var integralSetUo = {};
        integralSetUo.id = $('#award_senior_point_id').val();
        integralSetUo.isPay = $('#awardpoint_ispay').val();
        integralSetUo.isBuy = $('#awardpoint_isbuy').val();
        integralSetUo.onePayMoney = $('input[name="pay_one"]').val();
        integralSetUo.twoPayMoney = $('input[name="pay_two"]').val();
        integralSetUo.threePayMoney = $('input[name="pay_three"]').val();
        integralSetUo.buyMoney = $('input[name="award_buymoney"]').val();
        integralSetUo.returnIntegral = $('input[name="award_Integral"]').val();
        var jsonData = JSON.stringify(integralSetUo);
        $.ajaxJsonPut(ajaxUrl.url1,jsonData,{
            "done":function(res){
                if(res.code===0){
                    var dm = new dialogMessage({
                        type:1,
                        fixed:true,
                        msg:'修改成功',
                        isAutoDisplay:true,
                        time:3000
                    });
                    dm.render();
                }else{
                    var dm = new dialogMessage({
                        type:2,
                        fixed:true,
                        msg:'修改失败',
                        isAutoDisplay:true,
                        time:3000
                    });
                    dm.render();
                }
            }
        })
    }
    //数据采集
    var _doSubmit = function(){
        var integralSetCo = {};
        integralSetCo.isPay = $('#awardpoint_ispay').val();
        integralSetCo.isBuy = $('#awardpoint_isbuy').val();
        integralSetCo.onePayMoney = $('input[name="pay_one"]').val();
        integralSetCo.twoPayMoney = $('input[name="pay_two"]').val();
        integralSetCo.threePayMoney = $('input[name="pay_three"]').val();
        integralSetCo.buyMoney = $('input[name="award_buymoney"]').val();
        integralSetCo.returnIntegral = $('input[name="award_Integral"]').val();
        var jsonData = JSON.stringify(integralSetCo);
        $.ajaxJson(ajaxUrl.url1,jsonData,{
            "done":function(res){
                if(res.code===0){

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
            }
        })
    }

    var _queryPage = function(index){
        switch(index)
        {
            case 1:
                _loadSetup();
                break;
            case 2:
                _rewardList();
                break;
        }
    };
    return {
        init: _init
    };
})()