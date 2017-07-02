CommonUtils.regNamespace("dataAnalysis", "rp");

dataAnalysis.rp = (function(){
    var pageSize=10;
    var ajaxUrl = {
        url1:CONTEXT_PATH+'/data_analysis/rp',
        csvUrl:CONTEXT_PATH+'/data_analysis/rpCsv',
        tplUrl1:'shop/dataAnalysis/rp/basic1',
        tplUrl2:'shop/dataAnalysis/rp/basic4',
        tplUrl3:'shop/dataAnalysis/rp/basic6',
        tplUrl4:'shop/dataAnalysis/rp/basic5',
        tplUrl5:'shop/dataAnalysis/rp/basic7'

    };
    var _exportCsv = function(n){
        var paramsQo={};
        paramsQo.platForm = $("#platForm"+n).val();
        if(n==2){
            paramsQo.tabIndex=n;
            paramsQo.flag = "A";
            if(1==$("#platForm"+n).val()){ //wx
                paramsQo.revenueTypeId = $("#rTypeId").val();
                if($("#rTypeId").val()==1){
                    paramsQo.orderStatus = $("#orderStatus").val();
                }
            }else if(2==$("#platForm"+n).val()){
                paramsQo.revenueTypeId = $("#rTypeId2").val();
                if($("#rTypeId2").val()==1){
                    paramsQo.orderStatus = $("#orderStatus").val();
                }
            }
            paramsQo.orderNum = $("#orderNum").val();
            paramsQo.transactionId = $("#transactionId").val();
            paramsQo.showTotalMoney ='N';
        }else if(n==3){
            paramsQo.tabIndex=n;
            paramsQo.flag = "A";
            if(1==$("#platForm"+n).val()) { //wx
                paramsQo.payTypeId = $("#pTypeId").val();
            }else if(2==$("#platForm"+n).val()){ //wb
                paramsQo.payTypeId = $("#pTypeId2").val();
            }
            paramsQo.orderNum = $("#orderNumP").val();
            paramsQo.transactionId = $("#transactionIdP").val();
            paramsQo.showTotalMoney ='N';
        }
        var params = JSON.stringify(paramsQo);
        window.location.href =ajaxUrl.csvUrl+"?paramsQo="+params+"&beginTime="+$("#startDate2").val()+"&endTime="+$("#endDate2").val();
    }
    var _init = function(n){
        var obj = $('#rp_'+n);
        obj.addClass('z-sel').siblings().removeClass('z-sel');
        $(".panel-hidden_rp"+n).show().siblings().hide();
        if(n===1){
            _initBase(n);
        }
    }

    var _initBase = function(tabIndex) {
        var paramsQo={};
        paramsQo.tabIndex=tabIndex;
        var params = JSON.stringify(paramsQo)
        if(tabIndex===1){
            _getHtml(tabIndex,params,ajaxUrl.tplUrl1);
        }
    }
    var _getHtml = function(tabIndex,params,tplUrl){
        $.ajaxJson(ajaxUrl.url1,params,{
            "done":function (res) {
                if(res.code===0){
                    var data = {
                        revenue:res.data.revenue,
                        pay:res.data.pay,
                        revenueWb:res.data.revenueWb,
                        payWb:res.data.payWb
                    }
                    jumi.template(tplUrl,data,function (tpl) {
                        $('#u-tab1').empty();
                        $('#u-tab1').html(tpl);
                    })
                }
            }
        })
    }
    //tabIndex ===2 收入对账
    var _initBase2 = function(n,flag) {
        _changeTab2(n);
        if(flag == 'B'){
            $("#startDate2").val(new Date().toISOString().substr(0,10)+" 00:00:00");
            $("#endDate2").val(new Date().toISOString().substr(0,10)+" 23:59:59");
        }
        var paramsQo={};
        paramsQo.tabIndex=2;
        paramsQo.platForm = $("#platForm2").val();
        paramsQo.flag = flag;
        paramsQo.beginTime = $("#startDate2").val();
        paramsQo.endTime = $("#endDate2").val();
        if(1==$("#platForm2").val()){ //wx
            paramsQo.revenueTypeId = $("#rTypeId").val();
            if($("#rTypeId").val()==1){
                paramsQo.orderStatus = $("#orderStatus").val();
            }
        }else if(2==$("#platForm2").val()){
            paramsQo.revenueTypeId = $("#rTypeId2").val();
            if($("#rTypeId2").val()==1){
                paramsQo.orderStatus = $("#orderStatus").val();
            }
        }
        paramsQo.orderNum = $("#orderNum").val();
        paramsQo.transactionId = $("#transactionId").val();
        paramsQo.showTotalMoney ='Y';
        $.ajaxJson(ajaxUrl.url1,paramsQo,{
            "done":function (res) {
                if(res.code===0){
                    var data = {
                        rTotalMoney:res.data.rTotalMoney
                    }
                    jumi.template(ajaxUrl.tplUrl3,data,function (tpl) {
                        $('#u-tab2-1').empty();
                        $('#u-tab2-1').html(tpl);
                    })
                }
            }
        })
        _initPagination(paramsQo);
    }
    //tabIndex ===3 支出对账
    var _initBase3 = function(n,flag) {
        _changeTab3(n);
        if(flag == 'B'){
            $("#startDate3").val(new Date().toISOString().substr(0,10)+" 00:00:00");
            $("#endDate3").val(new Date().toISOString().substr(0,10)+" 23:59:59");
        }
        var paramsQo={};
        paramsQo.tabIndex=3;
        paramsQo.platForm = $("#platForm3").val();
        paramsQo.flag = flag;
        paramsQo.beginTime = $("#startDate3").val();
        paramsQo.endTime = $("#endDate3").val();
        if(1==$("#platForm3").val()) { //wx
            paramsQo.payTypeId = $("#pTypeId").val();
        }else if(2==$("#platForm3").val()){ //wb
            paramsQo.payTypeId = $("#pTypeId2").val();
        }
        paramsQo.orderNum = $("#orderNumP").val();
        paramsQo.transactionId = $("#transactionIdP").val();
        paramsQo.showTotalMoney ='Y';

        $.ajaxJson(ajaxUrl.url1,paramsQo,{
            "done":function (res) {
                if(res.code===0){
                    var data = {
                        pTotalMoney:res.data.pTotalMoney
                    }
                    jumi.template(ajaxUrl.tplUrl5,data,function (tpl) {
                        $('#u-tab3-1').empty();
                        $('#u-tab3-1').html(tpl);
                    })
                }
            }
        })
        _initPagination(paramsQo);
    }

    var _initPagination = function(paramsQo){
        paramsQo.showTotalMoney ='N';
        var url = ajaxUrl.url1;
        var n = paramsQo.tabIndex;
        paramsQo.pageSize=pageSize;
        jumi.pagination("#pageToolbar"+n,url,paramsQo,function (res,curPage) {
            if(res.code===0){
                //判断是否第一页
                if(n==2){
                    var data = {
                        items:res.data.rAnalysisInfo.items
                    };
                    jumi.template(ajaxUrl.tplUrl2,data,function(tpl){
                        $("#u-tab2").empty();
                        $("#u-tab2").html(tpl);
                    })
                }else if(n==3){
                    var data = {
                        items:res.data.pAnalysisInfo.items
                    };
                    jumi.template(ajaxUrl.tplUrl4,data,function(tpl){
                        $("#u-tab3").empty();
                        $("#u-tab3").html(tpl);
                    })
                }

            }
        })
    }
    var _changeTab2 = function (n){
        var obj = $('#rp_'+2);
        obj.addClass('z-sel').siblings().removeClass('z-sel');
        $(".panel-hidden_rp"+2).show().siblings().hide();
        if(n===0){
            var obj = $('#r_'+5);
            obj.addClass('z-sel').siblings().removeClass('z-sel');
        }else{
            var obj = $('#r_'+n);
            obj.addClass('z-sel').siblings().removeClass('z-sel');
        }
    }
    var _changeTab3 = function (n){
        var obj = $('#rp_'+3);
        obj.addClass('z-sel').siblings().removeClass('z-sel');
        $(".panel-hidden_rp"+3).show().siblings().hide();
        if(n===0){
            var obj = $('#p_'+5);
            obj.addClass('z-sel').siblings().removeClass('z-sel');
        }else{
            var obj = $('#p_'+n);
            obj.addClass('z-sel').siblings().removeClass('z-sel');
        }
    }
    return {
        init: _init,
        initBase:_initBase,
        initBase2:_initBase2,
        initBase3:_initBase3,
        exportCsv:_exportCsv
    };
})();
