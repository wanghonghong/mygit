CommonUtils.regNamespace("imageTextTip", "tip");

imageTextTip.tip = (function () {
    var pageSize=10;
    var ajaxUrl = {
        url1:CONTEXT_PATH+'/image_text/allTip',
        url2:CONTEXT_PATH+'/image_text/tipList',
        url3:CONTEXT_PATH+'/image_text',
        url4:CONTEXT_PATH+'/image_text/tip',
        url5:CONTEXT_PATH+'/image_text/tipSet',
        csvUrl:CONTEXT_PATH+'/data_analysis/rpCsv',
        tplUrl1:'shop/imageTextTip/imageTextTipData',
        tplUrl2:'shop/imageTextTip/tipData',
        tplUrl3:'shop/imageTextTip/tipSetData',
        tplUrl4:'shop/imageTextTip/tipDetailData'
    };

    var _init = function(n){
        var obj = $('#tip_'+n);
        obj.addClass('z-sel').siblings().removeClass('z-sel');
        $(".panel-hidden_tip"+n).show().siblings().hide();
        $("#startDate1").val("");
        $("#endDate1").val("");
        $("#startDate2").val("");
        $("#endDate2").val("");
        _initBase(n);
    }

    var _tipDetail = function(imageTextId,platForm){
        var elemsee = document.getElementById('dialoginfo-see');
        var imageTextTipQo={};
        imageTextTipQo.platForm = platForm;
        imageTextTipQo.imageTextId = imageTextId;
        _dialogPagination(imageTextTipQo);
        dialog({
            title: "图文收入明细",
            content: elemsee,
        }).width(900).showModal();
    }

    var _initBase = function(tabIndex) {
        var imageTextTipQo={};
        imageTextTipQo.tabIndex=tabIndex;
        var tplUrl = "";
        if(1==tabIndex){
            imageTextTipQo.beginTime = $("#startDate1").val();
            imageTextTipQo.endTime = $("#endDate1").val();
            imageTextTipQo.platForm = $("#platForm").val();
            imageTextTipQo.status = $("#status").val();
            imageTextTipQo.imageTextTitle = $("#imageTextTitle").val();
            tplUrl = ajaxUrl.tplUrl1;
        }else if(2==tabIndex){
            imageTextTipQo.beginTime = $("#startDate2").val();
            imageTextTipQo.endTime = $("#endDate2").val();
            imageTextTipQo.imageTextTitle = $("#imageTextTitle2").val();
            imageTextTipQo.telPhone = $("#telPhone").val();
            imageTextTipQo.userName = $("#userName").val();
            imageTextTipQo.nickName = $("#nickName").val();
            imageTextTipQo.platForm = $("#platForm2").val();
            tplUrl = ajaxUrl.tplUrl2;
        }else if(3==tabIndex){
            tplUrl = ajaxUrl.tplUrl3;
        }
        _getHtml(tabIndex,imageTextTipQo,tplUrl)
    }
    var _getHtml = function(tabIndex,imageTextTipQo,tplUrl){

        var ajaxurl = "";
        if(3==tabIndex){

            ajaxurl = ajaxUrl.url3;
            $.ajaxJsonGet(ajaxurl,null,{
                done:function (res) {
                    if(res.code===0){
                        var data = {
                            shopTipSetInfo:res.data.shopTipSetInfo
                        }
                        jumi.template(tplUrl,data,function (tpl) {
                            $('#u-tab3').empty();
                            $('#u-tab3').html(tpl);
                            _validateset();
                             var showIndexs = data.shopTipSetInfo.tipShowIndex;
                             var arr = showIndexs.split(",");
                             for(var item in arr) {
                                $("#div_"+arr[item]).attr("class","m-rewardmoney u-txt active");
                             }
                        })
                    }else{
                        var dm = new dialogMessage({
                            type:2,
                            fixed:true,
                            msg:'赏金设置获取失败',
                            isAutoDisplay:true,
                            time:3000
                        });
                        dm.render();
                    }
                }
            })
        }else{
            _initPagination(imageTextTipQo);
        }
    }
    var _initPagination = function(paramsQo){
        var url;
        var n = paramsQo.tabIndex;
        if(1==n){
            url = ajaxUrl.url1;
        }else if(2==n){
            url = ajaxUrl.url2;
        }

        paramsQo.pageSize=pageSize;
        jumi.pagination("#pageToolbar"+n,url,paramsQo,function (res,curPage) {
            if(res.code===0){
                //判断是否第一页
                if(n==1){
                    var data = {
                        items:res.data.imageTextTipInfo.items
                    };
                    jumi.template(ajaxUrl.tplUrl1,data,function(tpl){
                        $("#u-tab1").empty();
                        $("#u-tab1").html(tpl);
                    })
                }else if(n==2){
                    var data = {
                        items:res.data.shopTipInfo.items
                    };
                    jumi.template(ajaxUrl.tplUrl2,data,function(tpl){
                        $("#u-tab2").empty();
                        $("#u-tab2").html(tpl);
                    })
                }
            }
        })
    }
    var _dialogPagination = function(paramsQo){
        var url = ajaxUrl.url4;
        paramsQo.pageSize=pageSize;
        jumi.pagination("#pageToolbar4",url,paramsQo,function (res,curPage) {
            if(res.code===0){
                var data = {
                    items:res.data.tipInfo.items
                };
                jumi.template(ajaxUrl.tplUrl4,data,function(tpl){
                    $("#u-tab4").empty();
                    $("#u-tab4").html(tpl);
                })
            }
        })
    }
    var _isCheck = function (n){
        var b = $("#div_"+n).attr("class");
        if(b=="m-rewardmoney u-txt active"){
            $("#div_"+n).attr("class","m-rewardmoney u-txt ");
        }else{
            $("#div_"+n).attr("class","m-rewardmoney u-txt active");
        }
    }
    var _validateset = function(){
        $('#tip_set_form').validate({
            rules: {
                tip_integral:{
                    isPlusTwo:true
                }
            },
            messages:{
                tip_integral:{
                    isPlusTwo:'金额必须为小数或整数,小数点后不超过两位!!'
                }
            }
        })
    }
    var _saveTipSet = function () {
        var form = $('#tip_set_form');
        if(form.valid()){
        }else{
            return;
        }
        var shopTipSet={};
         shopTipSet.tipMoney1 = $("#tip1").val()*100;
         shopTipSet.tipMoney2 = $("#tip2").val()*100;
         shopTipSet.tipMoney3 = $("#tip3").val()*100;
         shopTipSet.tipMoney4 = $("#tip4").val()*100;
         shopTipSet.tipMoney5 = $("#tip5").val()*100;
         shopTipSet.tipMoney6 = $("#tip6").val()*100;
         var _showIndexs = [];
         for(var i =1;i<7;i++){
             var b = $("#div_"+i).attr("class");
             if(b=="m-rewardmoney u-txt active"){
                 _showIndexs.push(i);
             }
         }
        var tipShowIndex = _showIndexs.join(',');
        shopTipSet.tipShowIndex = tipShowIndex;
        $.ajaxJson(ajaxUrl.url5,shopTipSet,{
            done:function (res) {
                if(res.code===0){
                }else{
                    var dm = new dialogMessage({
                        type:2,
                        fixed:true,
                        msg:'赏金设置保存失败',
                        isAutoDisplay:true,
                        time:3000
                    });
                    dm.render();
                }
            }
        })
    }
    return {
        init: _init,
        tipDetail:_tipDetail,
        initBase:_initBase,
        isCheck:_isCheck,
        saveTipSet:_saveTipSet
    };
})();