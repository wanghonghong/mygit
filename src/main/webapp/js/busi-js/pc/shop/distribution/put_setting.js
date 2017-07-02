CommonUtils.regNamespace("sales", "putsetting");


sales.putsetting=(function () {
    var opt={
        Project_Path:CONTEXT_PATH,
        getinfoAjax:CONTEXT_PATH+'/brokerage/put_setting/1',
        addAjax: CONTEXT_PATH+'/brokerage/put_setting/add',
        updateAjax: CONTEXT_PATH+'/brokerage/put_setting/update',
        tplPath:'tpl/shop/distribution/brokerage/',
        putsetting:{}
    }
    var _init=function () {
        _loadData().then(function () {
            _initConfig();
        }).then(function () {
            bindEvent();
        })
    }

    var _loadData=function(){
        var dfdPlay = $.Deferred();
        var url=opt.getinfoAjax;
        $.ajaxJsonGet(url,null,{
            "done":function (res) {
                if(res.code==0){
                    opt.putsetting=res.data;
                }
                dfdPlay.resolve();
            }
        });
        return dfdPlay;
    }
    var alertinfo = function (msg) {
        var dm = new dialogMessage({
            type:3,
            msg:msg,
            isAutoDisplay:true,
            time:1500
        });
        dm.render();
    }
    var _initConfig=function () {
        var selectdata=[];
        for(var i=1;i<=28;i++){
            selectdata.push({
                id:i,
                text:i+'日 10点'
            })
        }
       var sendTime=  $("#sendTime").select2({
            theme: "jumi",
            data:selectdata
        }) ;
        opt.putsetting.sendTime=opt.putsetting.sendTime||1;
            sendTime.val(opt.putsetting.sendTime).trigger("change");

        if(opt.putsetting.id){
            //  if(opt.putsetting.putType==1){
            //     //商家发放
            //     $("#autoType"+opt.putsetting.autoType).click();
            // }else if(opt.putsetting.putType==2){
            //     //客户提现
            //     $("#autoType"+opt.putsetting.review).click();
            // }
            $("#autoType"+opt.putsetting.autoType).click();
            $("#sendTime").val(opt.putsetting.sendTime);
            $("#fullPut").val(opt.putsetting.fullPut/100);
            if(opt.putsetting.minMoney>0){
                $("#minMoneycheckbox").click();
                $("#minMoney").val(opt.putsetting.minMoney/100);
            }
            if(opt.putsetting.kitNum>0){
                $("#kitNumcheckbox").click();
                $("#kitNum").val(opt.putsetting.kitNum);
            }
        }else{
            opt.putsetting.payType=1;  //默认赋值为 微信类型
            opt.putsetting.autoType=opt.putsetting.autoType||4;
            $("#autoType"+opt.putsetting.autoType).click();
        }
        if(opt.putsetting.cashKit){
            $("#cashKitcheckbox").click();
        }
        if(opt.putsetting.integralKit){
            $("#integralKitcheckbox").click();
        }

    }
    var _validate=function () {
        var sign=true;
        if(opt.putsetting.putType==1){
            var autoType= $("#weixinbox :radio[name='autoType']:checked").val();
            if(autoType==3){
                var fullPut=$("#fullPut").val();
                if(fullPut<10||fullPut>200){
                    sign=false;
                    alertinfo('满额自动发放，金额应在10~200之间')
                }
                  // sign=false;
            }
            $("#cashBox :checkbox").each(function(){
               if( $(this).is(":checked")){
                   $(this).click();
               }
            })
        }else if(opt.putsetting.putType==2){

           if($("#minMoneycheckbox").is(":checked")){
               if($("#minMoney").val()>=1){
               }else{
                   sign=false;
                   alertinfo('每次最低限额必须大于1');
               }
           }
           if($("#kitNumcheckbox").is(":checked")){
               if($("#kitNum").val()>=1&&$("#kitNum").val()<=100){
               }else{
                   sign=false;
                   alertinfo('每日提现次数必须1~100范围内');
               }
           }
        }
        return sign;
    }

    var _save=function () {
        opt.putsetting.sendTime=$("#sendTime").val();
        var data=opt.putsetting;
        if(data.id){
            var url=opt.updateAjax;
            $.ajaxJsonPut(url,data,{
                "done":function (res) {
                    if(res.code==0){
                        alertinfo("设置成功");
                    }
                }
            })
        }else {
            var url=opt.addAjax;
            delete  data.id;
            $.ajaxJson(url,data,{
                "done":function (res) {
                    if(res.code==0){
                        opt.putsetting=res.data;
                        alertinfo("设置成功");
                    }
                }
            })
        }
    }

    var bindEvent=function(){
        $("#weixinbox :radio").click(function(){
            opt.putsetting.autoType=0;
            // opt.putsetting.review=0;
            // if($(this).attr("name")=="autoType"){
            //     opt.putsetting.putType=1;
            // }else if($(this).attr("name")=="review"){
            //     opt.putsetting.putType=3;
            // }
            if($(this).val()<=3){
                opt.putsetting.putType=1;
                $("#weixinbox :checkbox").removeAttr("checked");
                $("#kitNum,#minMoney").val(0);
                opt.putsetting.kitNum=0;
                opt.putsetting.minMoney=0;
                opt.putsetting.integralKit=0;
                opt.putsetting.cashKit=0;
            }else{
                opt.putsetting.putType=2;
            }
            // console.log(opt);
            opt.putsetting[$(this).attr("name")]=$(this).val();
            $("#weixinbox :radio").not(this).removeAttr("checked");
        })
        $("#infotip").click(function(){
            var d = dialog({
                align: 'right',
                content: '进入微信支付商户平台---产品中心---运营工具（现金红包）<br>---产品设置---用户领取上限---<br>修改每日同一用户领取红包上限数次设置（0-100）',
                quickClose: true// 点击空白处快速关闭
            });
            d.show(document.getElementById('infotip'));

        })

        $("#fullPut,#minMoney").change(function(){
            opt.putsetting[$(this).attr("name")]=Math.floor($(this).val() *100)||0;
            $(this).val(opt.putsetting[$(this).attr("name")]/100);
            if( opt.putsetting[$(this).attr("name")]>0){
                if( !$("#"+$(this).attr("name")+"checkbox").is(":checked")){
                    $("#"+$(this).attr("name")+"checkbox").click();
                }
                // $("#"+$(this).attr("name")+"checkbox").attr("checked",'checked');
            }else{
                if( $("#"+$(this).attr("name")+"checkbox").is(":checked")){
                    $("#"+$(this).attr("name")+"checkbox").click();
                }
                // $("#"+$(this).attr("name")+"checkbox").removeAttr("checked");
            }
        });
        $("#kitNum").change(function(){
            opt.putsetting[$(this).attr("name")]= Math.ceil($(this).val())||0;
            $(this).val(opt.putsetting[$(this).attr("name")]);
            if( opt.putsetting[$(this).attr("name")]>0){
               if(!$("#kitNumcheckbox").is(":checked")){
                   $("#kitNumcheckbox").click();
               }
            }else{
                if($("#kitNumcheckbox").is(":checked")){
                    $("#kitNumcheckbox").click();
                }
                // $("#"+$(this).attr("name")+"checkbox").removeAttr("checked");
            }
        });
        $("#minMoneycheckbox").click(function () {
            if($(this).is(":checked")){
               if($("#"+$(this).attr("name")).val()<1){
                   $("#"+$(this).attr("name")).val(1);
                   opt.putsetting[$(this).attr("name")]= 100;
               }
            }else{
                $("#"+$(this).attr("name")).val(0);
                opt.putsetting[$(this).attr("name")]= 0;
            }
        })
        $("#kitNumcheckbox").click(function () {
            if($(this).is(":checked")){
               if($("#"+$(this).attr("name")).val()<1){
                   $("#"+$(this).attr("name")).val(1);
                   opt.putsetting[$(this).attr("name")]= 1;
               }
            }else{
                $("#"+$(this).attr("name")).val(0);
                opt.putsetting[$(this).attr("name")]= 0;
            }
        })
        $("#integralKitcheckbox,#cashKitcheckbox").click(function () {
            if($(this).is(":checked")){
                opt.putsetting[$(this).attr("name")]= 1;
            }else{
                opt.putsetting[$(this).attr("name")]= 0;
            }
        })
        $("#savebtn").click(function(){
            if(_validate()){
                _save();
            }

        })
    }
    return {
        init:_init
    };
})();