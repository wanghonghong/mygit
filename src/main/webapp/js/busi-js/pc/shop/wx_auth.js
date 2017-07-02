CommonUtils.regNamespace("shop", "wxauth");
shop.wxauth = (function(){

    var _init = function(){
        $(".panel-hidden").hide().eq(0).show();
        var tabul = $(".m-tab ul li");
        tabul.click(function(){
            tabul.eq($(this).index()).addClass("z-sel").siblings().removeClass('z-sel');
            $(".panel-hidden").hide().eq($(this).index()).show();
        });
        $("#help").click(function() {
            var elem = document.getElementById('dialoginfo');
            dialog({
                title: "公众号微信支付配置说明",
                content: elem,

            }).width(865).showModal();
        });

        //初始化支付方式
        $.ajaxJsonGet(CONTEXT_PATH+"/wxauth/pay","",{
            "done":function (res) {
                var value = res.data.issub;
                $("#mchId").val(res.data.mchId);
                $("#appKey").val(res.data.appKey);
                if(value == 1){
                    $("#payway").find("option[value='2']").attr("selected",true);
                    $("#dl1").show();
                    $("#dl2").hide();
                    $("#div1").show();
                    $("#div2").hide();
                }
                if(value == 0){
                    $("#payway").find("option[value='3']").attr("selected",true);
                    $("#dl1").hide();
                    $("#dl2").show();
                    $("#div1").show();
                    $("#div2").hide();
                }
                if(value == 2){
                    $("#payway").find("option[value='4']").attr("selected",true);
                    $("#dl1").hide();
                    $("#dl2").show();
                    $("#div1").hide();
                    $("#div2").show();
                }
            }
        });

    };

    var _save = function(){
            var issub = $('#payway option:selected') .val();//支付方式
            var mchId = $("#mchId").val();
            var appKey = $("#appKey").val();
            if(mchId==""){
                var dm = new dialogMessage({
                    type:2,
                    title:'操作提醒',
                    fixed:true,
                    msg:'请填写商户号',
                    isAutoDisplay:false

                });
                dm.render();
                return;
            }
            if(appKey==""){
                var dm = new dialogMessage({
                    type:2,
                    title:'操作提醒',
                    fixed:true,
                    msg:'请填写秘钥',
                    isAutoDisplay:false

                });
                dm.render();
                return;
            }


            var url = CONTEXT_PATH+'/wxauth/pay';
            var params = {
                issub:issub,
                mchId:mchId,
                appKey:appKey
            };
            var data = JSON.stringify(params);
            $.ajaxJson(url,data,{
                "done":function (res) {
                    if(res.data.code==0){
                        var dm = new dialogMessage({
                            type: 1,
                            title: '操作提醒',
                            fixed: true,
                            msg: '恭喜您，操作成功',
                            isAutoDisplay: false

                        });
                        dm.render();
                    }
                }
            });
    };



    var _changeWay = function(v){
        var value=$(v).val();
        if(value==1){
            $("#dl1").hide();
            $("#dl2").hide();
            $("#div1").hide();
            $("#div2").hide();
        }else if(value == 2){
            $("#dl1").show();
            $("#dl2").hide();
            $("#div1").show();
            $("#div2").hide();
        }else if(value == 3){
            $("#dl1").hide();
            $("#dl2").show();
            $("#div1").show();
            $("#div2").hide();
        }else{
            $("#dl1").show();
            $("#dl2").hide();
            $("#div2").show();
            $("#div1").hide();
        }
    }




    return {
        init :_init,
        save :_save,
        changeWay:_changeWay
    };
})();
