CommonUtils.regNamespace("sales", "setting");

sales.setting=(function(){
    var opt={
        Project_Path:CONTEXT_PATH,
       setAjax: CONTEXT_PATH+'/brokerage/setting',
       setaddAjax: CONTEXT_PATH+'/brokerage/setting/add',
       setupdateAjax: CONTEXT_PATH+'/brokerage/setting/update',
       tplPath:'tpl/shop/distribution/brokerage/',
        brokerageSetting:{}
    }
    var _init=function () {
        // distribution.smallshop.init();
        _loadData().then(function () {
            _loadHtml();
            _changeTab();
        }).then(function () {
            bindEvent();
            initValue();
        })
    }
    var _loadData=function(){
        var dfdPlay = $.Deferred();
        var url=opt.setAjax;
        $.ajaxJsonGet(url,null,{
           "done":function (res) {
               console.log(res);
               if(res.code==0){
                   opt.brokerageSetting=res.data;
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
    var _loadHtml=function () {
        if(!$.isEmptyObject(opt.brokerageSetting)){
            opt.brokerageSetting.type=opt.brokerageSetting.type==0?1:opt.brokerageSetting.type;
            var html = jumi.templateHtml(opt.tplPath+'settingArea.html',opt.brokerageSetting);
            $("#settingArea").append(html);
            var isOpen=opt.brokerageSetting.isOpen==1?true:false;
            var isOpenswitch = new switchControl("#isOpen",{
                state:isOpen,
                onSwitchChange:function(v){
                    if(v){
                        $("#settingArea .m-moresale .sale-content").show();
                        opt.brokerageSetting.isOpen=1;
                        $("#settingArea [name='pricesort']").addClass("f-active").removeClass("f-active");
                    }else{
                        $("#settingArea .m-moresale .sale-content").hide();
                        opt.brokerageSetting.isOpen=0;
                        $("#settingArea [name='pricesort']").addClass("f-active").removeClass("f-active");
                    }
                }
            });
            isOpenswitch.render();
            if(isOpen){
              // $("#settingArea .m-moresale").show();
                $("#settingArea [name='pricesort']").addClass("f-active").removeClass("f-active");
            }else{
                // $("#settingArea .m-moresale").hide();
                $("#settingArea [name='pricesort']").addClass("f-active").removeClass("f-active");
            }
            var isFansShop=opt.brokerageSetting.isFansShop==1?true:false;
            var isFansShopswitch = new switchControl("#isFansShop",{
                state:isFansShop,
                onSwitchChange:function(v){
                    if(v){
                        $("#settingArea .m-moresale .fansShop").show();
                        opt.brokerageSetting.isFansShop=1;
                    }else{
                        $("#settingArea .m-moresale .fansShop").hide();
                        opt.brokerageSetting.isFansShop=2;
                    }
                }
            });
            isFansShopswitch.render();
            // $("#settleType"+opt.brokerageSetting.settleType).click();
            // $("#mode"+opt.brokerageSetting.mode).click();
            // $("#backStyle"+opt.brokerageSetting.backStyle).click();
            // $("#preStyle"+opt.brokerageSetting.preStyle).click();
        }
    }

    var _changeTab=function () {
        if( opt.brokerageSetting.isOpen==0){
            $("#upgradeTab").addClass("f-disable");
            $("#agentTab").addClass("f-disable");
        }else{
            $("#upgradeTab").addClass("f-disable");
            $("#agentTab").addClass("f-disable");
            if (opt.brokerageSetting.type == 4 || opt.brokerageSetting.type == 5 ||  opt.brokerageSetting.type == 6 ||  opt.brokerageSetting.type == 7  ) {
                //代理
                $("#agentTab").removeClass("f-disable");
            }
            if (opt.brokerageSetting.type == 3||opt.brokerageSetting.type == 5 ||  opt.brokerageSetting.type == 6 || opt.brokerageSetting.type == 7) {
               //升级
                $("#upgradeTab").removeClass("f-disable");
            }
        }
    }


    var _getBrokerageSetting=function () {
        var brokerageSetting={};
        // $(" #basicBox input:text").each(function(){
        //     product[$(this).prop("name")]=$(this).val();
        // })
        $(" #settingArea  [type='number']").each(function(){
            brokerageSetting[$(this).prop("name")]= Math.ceil($(this).val()*100);
        })
        brokerageSetting.settleType= $("#settingArea  input[name='settleType']:checked").val()||0;
        brokerageSetting.isOpen=opt.brokerageSetting.isOpen;
        brokerageSetting.isFansShop=opt.brokerageSetting.isFansShop;
        brokerageSetting.type=opt.brokerageSetting.type;
        brokerageSetting.backStyle=opt.brokerageSetting.backStyle||0;
        brokerageSetting.preStyle=opt.brokerageSetting.preStyle||0;
        brokerageSetting.mode=opt.brokerageSetting.mode||0;
        brokerageSetting.backUrl=opt.brokerageSetting.backUrl||'';
        brokerageSetting.status=0;
        return brokerageSetting;
    }

    var _save=function(){
       var  brokerageSetting=  _getBrokerageSetting();
        console.log(brokerageSetting);
        if(opt.brokerageSetting.id){
            brokerageSetting.id=opt.brokerageSetting.id;
            var url=opt.setupdateAjax;
            $.ajaxJsonPut(url,brokerageSetting,{
               "done":function (res) {
                   if(res.code==0){
                       _changeTab();
                       alertinfo("设置成功");
                   }
               }
            })
        }else{
            var url=opt.setaddAjax;
            $.ajaxJson(url,brokerageSetting,{
              "done":function (res) {
                  if(res.code==0){
                      opt.brokerageSetting=res.data;
                      _changeTab();
                      alertinfo("设置成功");
                  }
              }
            })
        }
    }
    var _validate=function () {
        var form=$("#form1");
        form.validate({
            rules:{
                one:{
                    required: true,
                    number: true
                },
                oneAup:{
                    required: true,
                    number: true
                },
                oneBup:{
                    required: true,
                    number: true
                },
                oneCup:{
                    required: true,
                    number: true
                },
                two:{
                    required: true,
                    number: true
                },
                twoAup:{
                    required: true,
                    number: true
                },
                twoBup:{
                    required: true,
                    number: true
                },
                twoCup:{
                    required: true,
                    number: true
                },
                afee:{
                    required: true,
                    number: true
                },
                bfee:{
                    required: true,
                    number: true
                },
                cfee:{
                    required: true,
                    number: true
                },
                agentA:{
                    required: true,
                    number: true
                },
                agentB:{
                    required: true,
                    number: true
                },
                agentC:{
                    required: true,
                    number: true
                },
                agentD:{
                    required: true,
                    number: true
                },
                agentAfee:{
                    required: true,
                    number: true
                },
                agentBfee:{
                    required: true,
                    number: true
                },
                agentCfee:{
                    required: true,
                    number: true
                },
                agentDfee:{
                    required: true,
                    number: true
                },
                differentKit:{
                    required: true,
                    number: true
                }
            }
        });
        return form;
    }
    /**
     * 处理特殊验证需求
     */
    var specialVerification=function () {
        var  sign=false;
        var one= $(" #settingArea  [name='one']").val()*1;
        var two= $(" #settingArea  [name='two']").val()*1;
        if(one+two>100){
            alertinfo("1级佣金和2级佣金相加不能大于100%");
            sign=true;
            return sign;
        }
        var minagent=10;
        $(" #settingArea  input[name^='agent']").each(function () {
            if($(this).attr("name").indexOf("fee")==-1){
                console.log($(this).val());
                if($(this).val()>0&&minagent>$(this).val()){
                    minagent=$(this).val();
                }
            }
            // console.log(minagent);
            // sign=true;
        })
        var tmpagent=(minagent)*10;
        var aupnum=0;

        $("#settingArea   input[name$='Aup']").each(function () {
            aupnum=aupnum+$(this).val()*1;
        })
        if(aupnum>tmpagent){
            console.log(aupnum,tmpagent,"Aup");
            alertinfo("you想要亏本me都拦不住啊!");
            sign=true;
            return sign;
        }
        var bupnum=0;
        $("#settingArea   input[name$='Bup']").each(function () {
            bupnum=bupnum+$(this).val()*1;
        })
        if(bupnum>tmpagent){
            console.log(bupnum,tmpagent,"Bup");
            alertinfo("you想要亏本me都拦不住啊!");
            sign=true;
            return sign;
        }
        var cupnum=0;
        $("#settingArea   input[name$='Cup']").each(function () {
            cupnum=cupnum+$(this).val()*1;
        })
        if(cupnum>tmpagent){
            console.log(cupnum,tmpagent,"Cup");
            alertinfo("you想要亏本me都拦不住啊!");
            sign=true;
            return sign;
        }
        console.log(tmpagent);
        return sign;
    }



    var bindEvent=function(){
        $("#backUrlImg").click(function(){

            var d = new Dialog({
                context_path: opt.Project_Path,
                resType: 1,
                callback: function (url) {
                    if(!$.isEmptyObject(url)){
                        url=  jumi.picParse(url,720);
                        console.log(url);
                        $("#backUrlImg").attr("src",url);
                        $("#backUrl").val(url);
                        opt.brokerageSetting.backUrl=url;
                    }
                }
            });
            d.render();
        });
        $(" #settingArea  [type='number']").change(function(){
            var tmpval=$(this).val()?$(this).val():0;
            opt.brokerageSetting[$(this).prop("name")]= Math.ceil(tmpval*100);
            $(this).val(opt.brokerageSetting[$(this).prop("name")]/100);
        })

        $("#settingBox .savebtn").click(function(){
            if(_validate().valid()){
                if(specialVerification()){
                    return;
                }else{
                    _save();
                }
            }else{
                $(".error[for]","#form1").removeAttr("style");
            }
        })
        $(" #settingArea  [name='backStyle']").click(function () {
                var _this=$(this);
              opt.brokerageSetting.backStyle=_this.val();
            var target= _this.attr("data-target");
             $(" #settingArea  .u-shoptlp1").hide();
            $("#"+target).show();
            if(_this.val()==2){
                //如果是自定义类型就设预设背景图为0
                opt.brokerageSetting.preStyle=0;
            }else{
                var preStyle= $(" #backStyleBox1 .u-sort.active").index()+1;
                opt.brokerageSetting.preStyle=preStyle;
                console.log(preStyle);
            }
        })
        $(" #backStyleBox1 .u-sort").click(function () {
            var _this=$(this);
            _this.addClass("active").siblings().removeClass("active");
            var $preStyle= _this.find("input[name='preStyle']");
            opt.brokerageSetting.preStyle=$preStyle.val();
            console.log(opt.brokerageSetting)
        })

        $(" #settingArea  [name='mode']").click(function () {
            var _this=$(this);
            var tmpvar=_this.val();
            opt.brokerageSetting.mode=_this.val();
            if(tmpvar==1){
                $("#m-tip1").show();
                $("#m-tip2").hide();
                $("#money").attr("readonly","readonly").val(0);
            }else{
                $("#m-tip1").hide();
                $("#m-tip2").show();
                $("#money").removeAttr("readonly");
            }
        })


        $("#salestab li").click(function(){
            var _this= $(this);
            if(_this.hasClass("f-disable")){
                alertinfo(_this.attr("title"));
            }else{
                _this.addClass("z-sel").siblings().removeClass("z-sel");
                var target = _this.attr("data-target");
                $("#"+target).removeClass("z-hide").siblings().addClass("z-hide");
                 console.log( $("#"+target).children().length);
                if($("#"+target+" .viewWindow").children().length>0){
                    if(target=='upgradeBox'){
                        sales.upgrade.loadView( );
                    }else if(target=='agentBox'){
                        sales.agent.loadView( );
                    }
                }else{
                    if(target=='upgradeBox'){
                        sales.upgrade.init(target);
                    }else if(target=='agentBox'){
                        sales.agent.init(target);
                    }
                }
            }
        })
    }

    var initValue=function(){
        $("#settleType"+opt.brokerageSetting.settleType).click();
        $("#mode"+opt.brokerageSetting.mode).click();
        $("#preStyle"+opt.brokerageSetting.preStyle).parent().click();
        $("#backStyle"+opt.brokerageSetting.backStyle).click();
    }
    return {
      init:_init
    };

})();