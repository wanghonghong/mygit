/**
 * Created by ray on 16/9/28.
 * 积分管理功能
 */
CommonUtils.regNamespace("awardpoint", "management");
awardpoint.management = (function() {
    var ajaxUrl = {
        url1:CONTEXT_PATH+'/integral/set',
        url2:CONTEXT_PATH+'/integral/award',
        url3:CONTEXT_PATH+'/good/productList/0',//获取在售商品
        url4:CONTEXT_PATH+'/integral/product',
        url5:CONTEXT_PATH+'/integral/products',
        url6:CONTEXT_PATH+'/integral/product/list'
    };
    var _init = function(){
        _bind();
        _loadSetup();//初始化加载基础设置模版
    };
    var _initUnits = function (unitName) {
        var unitName = unitName ||'',arrayValue = ['币','米'],array,tpl='';
        if(unitName){
            arrayValue.push(unitName);
            array = _.uniq(arrayValue);
            _.map(array,function (k,v) {
                if(k==unitName){
                    tpl = '<option selected>'+k+'</option>';
                }else{
                    tpl = '<option>'+k+'</option>';
                }
                $('#units_setup').append(tpl);
            })
        }else{
            _.map(arrayValue,function (k,v) {
                var tpl = '<option>'+k+'</option>';
                $('#units_setup').append(tpl);
            })
        }

    }
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
    var _validateset = function(){
        $('#awardpoint_set_form').validate({
            rules: {
                award_integral:{
                    required: true,
                    digits:true,
                    isIntGtZero:true
                }
            },
            messages:{
                award_integral:{
                    required: "请输入积分数值",
                    isIntGtZero:'积分数值必须是一个大于零的整数',
                    digits:'积分数值必须是一个大于零的整数'
                }
            }
        })
    }
    var _validate = function() {
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

        $("#awardpoint_form").validate({
            rules: {
                award_amount:{
                    required: true,
                    digits:true,
                    isIntGtZero:true
                },
                times:{
                    required: true,
                    isIntGtZero:true
                },
                award_award_one:{
                    required: true,
                    digits:true,
                    isIntGtZero:true
                },
                award_award_second:{
                    digits:true,
                    isIntGtZero:true
                },
                randomIntegral:{
                    isIntGtZero:true,
                    digits:true
                },
                fixedIntegral:{
                    isIntGtZero:true
                },
                units_name:{
                    required: true,
                    byteRangeLength:[0,5]
                }
            },
            messages:{
                fixedIntegral:{
                    isIntGtZero:'固定积分必须是一个大于零的整数'
                },
                award_amount: {
                    required: "请输入奖励积分",
                    isIntGtZero:'奖励积分必须是一个大于零的整数',
                    digits:'奖励积分必须是一个大于零的整数'
                },
                award_award_one:{
                    required: "请输入奖励积分",
                    isIntGtZero:'奖励积分必须是一个大于零的整数',
                    digits:'奖励积分必须是一个大于零的整数'
                },
                randomIntegral:{
                    isIntGtZero:'随机积分必须是一个大于零的整数',
                    digits:'随机积分必须是一个大于零的整数'
                },
                award_award_second:{
                    isIntGtZero:'奖励积分必须是一个大于零的整数',
                    digits:'奖励积分必须是一个大于零的整数'
                },
                times:{
                    required: "请输入次数",
                    isIntGtZero:'次数必须是一个大于零的整数'
                },
                units_name:{
                    required:'积分单位必须设置',
                    byteRangeLength:'积分单位长度必须介于{0}-{1}之间的字符串'
                }

            }
        })
    }

    var _refreshPage = function(){
        var curPage = $('#pagePointToolbar_page').val();
        var integralProductQo = {
            pageSize:10,
            curPage:curPage
        };
        $.ajaxJson(ajaxUrl.url5,integralProductQo,{
            "done":function(res){
                if(res.code===0){
                    var data = {
                        items:res.data.items
                    };
                    jumi.template('awardpoint/awardpoint_exchange_list',data,function(html){
                        $('#exchange_list').html(html);
                    })
                }
            },
            "fail":function(){

            }
        })
    }
    var _bind = function(){
        $('#awardpoint_content').on('click','#custom_units',function(){
            var flag = $(this).data('flag');
            var unitName = $('input[name="units_name"]').val();
            var tpl1 = '<input type="text" id="units_setup" class="ipt-txt" name="units_name">';
            var tpl2 = '<select class="ipt-txt c-gray1 valid" id="units_setup" name="units_select" aria-invalid="false" style="display: block;">'+

                       '</select>';
            if(!flag){
                $('#jf_setup').html(tpl1);
                $(this).data('flag',1);
                $('#custom_units').text('列表选择');
                _initUnits(unitName);
            }else{
                $(this).data('flag',0);
                $('#jf_setup').html(tpl2);
                $('#custom_units').text('自定义单位');
                _initUnits(unitName);
            }
            $('#units_setup').change(function(){
                var value = $(this).val();
                $('.award-unit').text(value);
            })
        })
        $('#awardpoint_content').on('click','input[name="allChose"]',function(){
            var flag = $(this).is(':checked');
            if(flag){
                $('input[name="checkall"]').prop('checked',true);
            }else{
                $('input[name="checkall"]').prop('checked',false);
            }
        })
        $('#awardpoint_content').on('click','#awardpoint_all',function(){
            var integralProductCos = [];
            $('input[name="checkall"]:checked').each(function(){
                var _object = {};
                var id = $(this).data('id');
                var pid = $(this).val();
                _object.id = id;
                _object.pid = pid;
                integralProductCos.push(_object);
            })
            if(integralProductCos.length>0){
                $.ajaxJson(ajaxUrl.url6,integralProductCos,{
                    "done":function (res) {
                        if(res.code===0){
                            _refreshPage();
                        }
                    }
                })
            }else{
                var dm = new dialogMessage({
                    type:2,
                    fixed:true,
                    msg:'还未勾选商品',
                    isAutoDisplay:true,
                    time:3000
                });
                dm.render();
            }
        })
        //商品换购删除
        $('#awardpoint_content').on('click','div[id^="awardpoint_del_"]',function(){
            var args = {};
            var id = $(this).data('id');
            args.fn1 = function(){
                var url = ajaxUrl.url4+'/'+id;
                $.ajaxJsonDel(url,{
                    "done":function (res) {
                        if(res.code===0){
                            var dm = new dialogMessage({
                                type:1,
                                fixed:true,
                                msg:'清空商品换购成功',
                                isAutoDisplay:true,
                                time:3000
                            });
                            dm.render();
                            _refreshPage();
                        }
                    }
                });

            };
            //关闭的时候初始化方法
            args.fn2 = function(){

            };
            jumi.dialogSure('确定清空该商品换购吗?',args);

        })
        //商品换购编辑按钮
        $('#awardpoint_content').on('click','div[id^="awardpoint_edit_"]',function(){
            var id = $(this).data('id'),type;
            var pid = $(this).data('pid');
            var price = $(this).data('price');
            var integral = $(this).data('integral');
            var state = $(this).data('type');
            var data = {};
            data.price = price;
            data.id = id;
            data.pid = pid;
            data.type = state;
            data.integral = integral;
            if(id){
                type=1;//如果id存在表示设置过积分
            }
            jumi.template('awardpoint/awardpoint_set?type=1',data,function(tpl){
                var d = dialog({
                    title: '商品积分设置',
                    content:tpl,
                    id:'awardpoint_set_dialog',
                    width:500,
                    height:190,
                    onshow:function () {
                        _validateset();
                        $('#awardpoint_save').click(function(){
                            var form = $('#awardpoint_set_form');
                            var type = $('input[name="award_buy-r"]:checked').val();
                            var integralProductCo = {};
                            if(type==='1'){
                                if(form.valid()){
                                    integralProductCo.integral = $('input[name="award_integral"]').val();
                                }else{
                                    return;
                                }
                            }
                            var id = $('#award_set').val();
                            integralProductCo.pid = $(this).data('pid');
                            integralProductCo.type = type;
                            if(id){
                                integralProductCo.id = id;
                            }
                            var jsonData = JSON.stringify(integralProductCo);
                            $.ajaxJson(ajaxUrl.url4,jsonData,{
                                "done":function(res){
                                    if(res.code===0){
                                        _refreshPage();
                                        dialog.get('awardpoint_set_dialog').close().remove();
                                    }
                                },
                                "fail":function(){
                                    var dm = new dialogMessage({
                                        type:2,
                                        fixed:true,
                                        msg:'设置失败',
                                        isAutoDisplay:true,
                                        time:3000
                                    });
                                    dm.render();
                                }
                            })
                        })
                    }

                });
                d.showModal();
            })

        })
        $('#awardpoint_content').on('click','#award_setting_save',function(){
            var args = {};
            var isplay = $('#awardpoint_ispay').val();
            var form = $( "#award_senior_form" );
            if(form.valid()){
                args.fn1 = function(){
                    var id = $('#award_senior_point_id').val();
                    if(id){
                        _doEditSetSubmit();
                    }else{
                        _doSetSubmit();
                    }
                }
                args.fn2 = function(){

                };
                jumi.dialogSure('确定保存积分高级设置吗?',args);
            }
        })
        $('#awardpoint_content').on('click','input[name="award_setup"]',function(){
            var value = $(this).val();
            if(value==='0'){

                $('#award_reward_box').hide();
                $('#award_exchange_box').hide();
                $('#award_content_box').hide();
            }else{
                $('input[name="award_pointswitch"][value="1"]').attr('disabled',false);
                $('input[name="on1"][value="1"]').attr('disabled',false);
                $('input[name="on_content"][value="1"]').attr('disabled',false);
                var flag = $('input[name="award_pointswitch"]:checked').val();
                if(Number(flag)){
                    $('input[name="checkBox1"]').attr('disabled',false);
                    $('input[name="checkBox2"]').attr('disabled',false);
                }
                $('#award_reward_box').show();
                $('#award_exchange_box').show();
                $('#award_content_box').show();
            }
        })
        $('#awardpoint_content').on('click','input[name="award_pointswitch"]',function(){
            var value = $(this).val();
            if(value==='0'){
                $('input[name="checkBox1"]').prop('checked',false);
                $('input[name="checkBox2"]').prop('checked',false);
                $('input[name="checkBox1"]').attr('disabled',true);
                $('input[name="checkBox2"]').attr('disabled',true);
                $('input[name="award_amount"]').attr('disabled',true);
                $('input[name="award_award_one"]').attr('disabled',true);
                $('input[name="award_award_second"]').attr('disabled',true);

            }else{
                $('input[name="checkBox1"]').attr('disabled',false);
                $('input[name="checkBox2"]').attr('disabled',false);
                $('input[name="award_amount"]').attr('disabled',false);
                $('input[name="award_award_one"]').attr('disabled',false);
                $('input[name="award_award_second"]').attr('disabled',false);
            }
        })

        $('#awardpoint_content').on('click','input[name="on_content"]',function(){
            var value = $(this).val();
            if(value==='0'){
                $('input[name="times"]').attr('disabled',true);
                $('input[name="fixedIntegral"]').attr('disabled',true);
                $('input[name="randomIntegral"]').attr('disabled',true);
            }else{
                $('input[name="randomIntegral"]').attr('disabled',false);
                $('input[name="times"]').attr('disabled',false);
                $('input[name="fixedIntegral"]').attr('disabled',false);
            }
        })


        //tab切换页
        $('#awardpoint_setup li').click(function () {
            var index = $(this).data('index');
            $(this).addClass('z-sel').siblings().removeClass('z-sel');
            _queryPage(index);//加载不同的选项卡模版
        });


        $('#awardpoint_content').on('click','#awardpoint_save',function(){
            var form = $( "#awardpoint_form" );
            if(form.valid()){
                var id = $('#awardpoint_id').val();
                if(id){
                    _doEditSubmit();
                }else{
                    _doSubmit();
                }
            }

        })
    };
    //高级设置修改
    var _doSetSubmit = function(){
        var integralSetCo = {};
        integralSetCo.isPay = $('#awardpoint_ispay').val();
        integralSetCo.isBuy = $('#awardpoint_isbuy').val();
        integralSetCo.onePayMoney = parseInt($('input[name="pay_one"]').val(),10)*100;
        integralSetCo.twoPayMoney = parseInt($('input[name="pay_two"]').val(),10)*100;
        integralSetCo.threePayMoney = parseInt($('input[name="pay_three"]').val(),10)*100;
        integralSetCo.buyMoney = parseInt($('input[name="award_buymoney"]').val(),10)*100;
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
    //高级设置编辑修改
    var _doEditSetSubmit = function(){
        var integralSetUo = {};
        integralSetUo.id = $('#award_senior_point_id').val();
        integralSetUo.isPay = $('#awardpoint_ispay').val();
        integralSetUo.isBuy = $('#awardpoint_isbuy').val();
        integralSetUo.onePayMoney = parseInt($('input[name="pay_one"]').val(),10)*100;
        integralSetUo.twoPayMoney = parseInt($('input[name="pay_two"]').val(),10)*100;
        integralSetUo.threePayMoney = parseInt($('input[name="pay_three"]').val(),10)*100;
        integralSetUo.buyMoney = parseInt($('input[name="award_buymoney"]').val(),10)*100;
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
    //设置修改
    var _doEditSubmit = function(){
        var integralSetUo = {};
        integralSetUo.id = $('#awardpoint_id').val();
        integralSetUo.isOpen = $('input[name="award_setup"]:checked').val();
        integralSetUo.isAward = $('input[name="award_pointswitch"]:checked').val();
        integralSetUo.unit = $('#awardpoint_unit').val();
        integralSetUo.unitName = $('#units_setup').val();
        integralSetUo.loginAward = $('#award_amount').val();
        integralSetUo.oneRecommendAward = $('#award_award_one').val();
        integralSetUo.twoRecommendAward = $('#award_award_second').val()||0;
        integralSetUo.isExchange = $('input[name="on1"]:checked').val();
        integralSetUo.isContent = $('input[name="on_content"]:checked').val();
        integralSetUo.contentCount = $('input[name="fixedIntegral"]').val();
        integralSetUo.times = $('input[name="times"]').val();
        integralSetUo.contentCount = $('input[name="fixedIntegral"]').val();
        var randomArray = [];
        $('input[name="randomIntegral"]').each(function(i){
            var value = $(this).val();
            if(value){
                randomArray.push(value);
            }
        })
        if(randomArray.length<2&&integralSetUo.isContent==='1'){
            var dm = new dialogMessage({
                type:1,
                fixed:true,
                msg:'随机积分至少要填写两个',
                isAutoDisplay:true,
                time:3000
            });
            dm.render();
            return;
        }
        integralSetUo.randomCount = randomArray.join(',');
        var checkBoxflag = $('input[name="checkBox1"]').is(':checked');
        var checkBoxflag2 = $('input[name="checkBox2"]').is(':checked');
        if(checkBoxflag){
            integralSetUo.isLogin = 1;
        }else{
            integralSetUo.isLogin = 0;
        }
        if(checkBoxflag2){
            integralSetUo.isRecommend = 1;
        }else{
            integralSetUo.isRecommend = 0;
        }
        var args = {};
        args.fn1 = function(){
            var jsonData = JSON.stringify(integralSetUo);
            $.ajaxJsonPut(ajaxUrl.url1,jsonData,{
                "done":function(res){
                    if(res.code===0){
                        var dm = new dialogMessage({
                            type:1,
                            fixed:true,
                            msg:'积分设置修改成功',
                            isAutoDisplay:true,
                            time:3000
                        });
                        dm.render();
                        _loadSetup();
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
        args.fn2 = function () {
            
        }
        jumi.dialogSure('确定保存积分设置吗?',args);
    }
    //数据采集
    var _doSubmit = function(){
        var integralSetCo = {};
        integralSetCo.isOpen = $('input[name="award_setup"]:checked').val();
        integralSetCo.isAward = $('input[name="award_pointswitch"]:checked').val();
        integralSetCo.unit = $('#awardpoint_unit').val();
        integralSetCo.unitName = $('#units_setup').val();
        integralSetCo.loginAward = $('#award_amount').val();
        integralSetCo.oneRecommendAward = $('#award_award_one').val();
        integralSetCo.twoRecommendAward = $('#award_award_second').val();
        integralSetCo.isExchange = $('input[name="on1"]:checked').val();
        integralSetCo.isContent = $('input[name="on_content"]:checked').val();
        integralSetCo.times = $('input[name="times"]').val();
        integralSetCo.contentCount = $('input[name="fixedIntegral"]').val();
        var randomArray = [];
        $('input[name="randomIntegral"]').each(function(i){
            var value = $(this).val();
            randomArray.push(value);
        })

        if(randomArray.length<2&&integralSetCo.isContent==='1'){
            var dm = new dialogMessage({
                type:1,
                fixed:true,
                msg:'随机积分至少要填写两个',
                isAutoDisplay:true,
                time:3000
            });
            dm.render();
            return;
        }
        integralSetCo.randomCount = randomArray.join(',');
        var checkBoxflag = $('input[name="checkBox1"]').is(':checked');
        var checkBoxflag2 = $('input[name="checkBox2"]').is(':checked');
        if(checkBoxflag){
            integralSetCo.isLogin = 1;
        }else{
            integralSetCo.isLogin = 0;
        }
        if(checkBoxflag2){
            integralSetCo.isRecommend = 1;
        }else{
            integralSetCo.isRecommend = 0;
        }
        var args = {};
        args.fn1 = function(){
            var jsonData = JSON.stringify(integralSetCo);
            $.ajaxJson(ajaxUrl.url1,jsonData,{
                "done":function(res){
                    if(res.code===0){
                        var dm = new dialogMessage({
                            type:1,
                            fixed:true,
                            msg:res.data.msg,
                            isAutoDisplay:true,
                            time:3000
                        });
                        dm.render();
                        _loadSetup();
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
        args.fn2 = function(){

        }
        jumi.dialogSure('确定保存积分设置吗?',args);


    }

    //点击伸缩
    var _clickSlideToggle = function(){
        $(".btn-slide").click(function(){
            $("#m-search1").slideToggle("fast");
            $(this).toggleClass("btn-slide1"); return false;
        });
    };
    //高级设置
    var _advanced_setting = function(){
        $.ajaxJsonGet(ajaxUrl.url1,null,{
            "done":function(res){
                if(res.code===0){
                    var data = res.data.data;
                    var id = res.data.data.id;
                    if(!id){
                        jumi.template('awardpoint/awardpoint_senior_setting',function(html){
                            $('#awardpoint_content').html(html);
                            _validate();
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
                            $('input[name="pay_one"]').val(data.onePayMoney/100);
                            $('input[name="pay_two"]').val(data.twoPayMoney/100);
                            $('input[name="pay_three"]').val(data.threePayMoney/100);
                            $('input[name="award_buymoney"]').val(data.buyMoney/100);
                            $('input[name="award_Integral"]').val(data.returnIntegral);
                            _initSwitchButton('#awardpoint_ispay',isPay);//开通积分功能
                            _initSwitchButton('#awardpoint_isbuy',isBuy);//开通积分可商品换购
                        })
                    }
                }
            }
        })
    }
    //基础设置
    var _loadSetup= function(){
        $.ajaxJsonGet(ajaxUrl.url1,null,{
            "done":function(res){
                if(res.code===0){
                    var data = res.data.data;
                    var id = res.data.data.id;
                    if(!id){
                        jumi.template('awardpoint/awardpoint_setup',function(html){
                            $('#awardpoint_content').html(html);
                            _validate();
                            _initUnits();

                            if(!data.isAward){
                                $('input[name="checkBox1"]').attr('disabled',true);
                                $('input[name="checkBox2"]').attr('disabled',true);
                                $('input[name="award_amount"]').attr('disabled',true);
                                $('input[name="award_award_one"]').attr('disabled',true);
                                $('input[name="award_award_second"]').attr('disabled',true);
                            }
                            if(!data.isContent){
                                $('input[name="times"]').attr('disabled',true);
                                $('input[name="fixedIntegral"]').attr('disabled',true);
                                $('input[name="randomIntegral"]').each(function(){
                                    $(this).attr('disabled',true);
                                })

                            }
                            $('#units_setup').change(function(){
                                var value = $(this).val();
                                $('.award-unit').text(value);
                            })

                        })
                    }else{
                        var isOpen = data.isOpen;
                        var isAward = data.isAward;
                        var isExchange = data.isExchange;
                        var isContent = data.isContent;
                        var uName = data.unitName;
                        if(data.randomCount){
                            var random = data.randomCount.split(',');
                        }
                        $('#awardpoint_integral').val(data.unit);
                        jumi.template('awardpoint/awardpoint_setup',data,function(html){
                            $('#awardpoint_content').html(html);
                            _validate();
                            _initUnits(uName);
                            $('#awardpoint_unit').attr("disabled",true);
                            if(!isOpen){
                                $('#award_reward_box').hide();
                                $('#award_exchange_box').hide();
                                $('#award_content_box').hide();
                            }
                            if(isOpen){
                                $('#award_reward_box').show();
                                $('#award_exchange_box').show();
                                $('#award_content_box').show();
                            }
                            if(!isAward){
                                $('input[name="checkBox1"]').attr('disabled',true);
                                $('input[name="checkBox2"]').attr('disabled',true);
                                $('input[name="award_amount"]').attr('disabled',true);
                                $('input[name="award_award_one"]').attr('disabled',true);
                                $('input[name="award_award_second"]').attr('disabled',true);
                            }
                            if(!isContent){
                                $('input[name="times"]').attr('disabled',true);
                                $('input[name="fixedIntegral"]').attr('disabled',true);
                                $('input[name="randomIntegral"]').each(function () {
                                    $(this).attr('disabled',true);
                                })
                            }
                            $('.award-unit').text(uName);
                            $("#awardpoint_unit").find("option[value='"+data.unit+"']").attr("selected",true);
                            $("#units_setup").find("option[value='"+data.unitName+"']").attr("selected",true);
                            $('#units_setup').change(function(){
                                var value = $(this).val();
                                $('.award-unit').text(value);
                            })
                            _.map(random,function(k,v){
                                $('input[name="randomIntegral"]').eq(v).val(k);
                            })
                        })
                    }

                }
            }
        })
    };

    //换购设置
    var _loadExchange = function(){
        jumi.template('awardpoint/awardpoint_exchange',function(html){
            $('#awardpoint_content').html(html);
            _queryAwardpointList();
        })
    };
    //商品换购列表
    var _queryAwardpointList = function(){
        var url = ajaxUrl.url5;
        var integralProductQo = {
            pageSize:10
        };
        jumi.pagination('#pagePointToolbar',url,integralProductQo,function(res,curPage){
            if(res.code===0){
                var data = {
                    items:res.data.items
                };
                jumi.template('awardpoint/awardpoint_exchange_list',data,function(tpl){
                    $('#exchange_list').html(tpl);
                })
            }
        })
    }
    //在售商品列表
    var _querySaleList = function(){
        var url = ajaxUrl.url3;
        var params = {
            pageSize:10
        };
        jumi.pagination('#pagePointToolbar',url,params,function(res,curPage){
            if(res.code===0){
                var data = {
                    items:res.data.items
                };
                jumi.template('awardpoint/awardpoint_sale_list',data,function(tpl){
                    $('#sale_list').html(tpl);
                })
            }
        })
    }
    //奖励清单列表
    var _queryRewardList = function(){
        var project = $('#awardpoint_project').val();
        var integralRecordQo = {};
        integralRecordQo.pageSize = 10;
        integralRecordQo.phoneNumber = $('input[name="phoneNumber"]').val();
        integralRecordQo.nickname = $('input[name="award_nic"]').val();
        integralRecordQo.userName = $('input[name="award_username"]').val();
        integralRecordQo.beginTime = $('#startTime').val();
        integralRecordQo.endTime = $('#overTime').val();
        if(project){
            integralRecordQo.integralType = project;
        }
        jumi.pagination('#pageAwardToolbar',ajaxUrl.url2,integralRecordQo,function(res,curPage){
            if(res.code===0){
                //判断是否第一页
                var data = {
                    items:res.data.items
                };
                jumi.template('awardpoint/awardpoint_reward_list',data,function(tpl){
                    $('#reward_list').html(tpl);
                })
            }
        })
    };
    //在售商品
    var _loadSaleList = function(){
        jumi.template('awardpoint/awardpoint_sale',function(html){
            $('#awardpoint_content').html(html);
            _querySaleList();
        })
    }
    //奖励清单
    var _rewardList = function () {
        jumi.template('awardpoint/awardpoint_reward',function(html){
            $('#awardpoint_content').html(html);
            _queryRewardList();
            _clickSlideToggle();
            _timeTimepicker();
            _initSelect();
        })
    };
    var _initSelect = function () {
        jumi.Select('#awardpoint_project');
        jumi.Select('#awardpoint_platform');
        jumi.Select('#awardpoint_customer');
    };
    var _timeTimepicker = function () {
        $("#startTime").datetimepicker({
            timeFormat : 'hh:mm:ss',
            dateFormat : 'yy-mm-dd'
        });
        $("#overTime").datetimepicker({
            timeFormat : 'hh:mm:ss',
            dateFormat : 'yy-mm-dd'
        });
    };

    var _queryPage = function(index){
        switch(index)
        {
            case 1:
                _loadSetup();
                break;
            case 2:
                _advanced_setting();
                break;
            case 3:
                _loadExchange();
                break;
            // case 4:
            //     _loadSaleList();
            //     break;
        }
    };
    return {
        init: _init,
        reward:_rewardList
    };
})();