CommonUtils.regNamespace("dataAnalysis", "good");

dataAnalysis.good = (function(){
    var pageSize=10;
    var ajaxUrl = {
        url1:CONTEXT_PATH+'/data_analysis/good',
        tplUrl1:'shop/dataAnalysis/goods/basic3',
        tplUrl2:'shop/dataAnalysis/goods/basic4',
        csvUrl:CONTEXT_PATH+'/data_analysis/productCsv'
    }
    var _exportCsv = function(n){
        var paramsQo={};
        paramsQo.tabIndex=n;
        paramsQo.productName= $("#productName").val();
        paramsQo.groupId= $("#groupId").val();
        paramsQo.status = $("#status").val();
        paramsQo.sort = $("#sort").val();
        var params = JSON.stringify(paramsQo);
        window.location.href =ajaxUrl.csvUrl+"?paramsQo="+params+"&beginTime="+$("#beginDate2").val()+"&endTime="+$("#endDate2").val();
    }
    var _init = function(n){
        var obj = $('#good_'+n);
        obj.addClass('z-sel').siblings().removeClass('z-sel');
        $(".panel-hidden_g"+n).show().siblings().hide();
        $("#beginDate"+n).val("");
        $("#endDate"+n).val("");
        _initBase(n);
        if(n===2){
            _initPagination();
        }
    }

    var _initBase = function(tabIndex,beginTime,endTime) {
        var paramsQo={};
        paramsQo.tabIndex=tabIndex;//基础数据，效果分析
        paramsQo.beginTime=beginTime;
        paramsQo.endTime=endTime;
        paramsQo.platForm = $("#platForm"+tabIndex).val();
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
                        productInfo:res.data.productInfo,
                        pVisitInfo:res.data.pVisitInfo
                    }
                    jumi.template(tplUrl,data,function (tpl) {
                        $('#u-tab3').empty();
                        $('#u-tab3').html(tpl);
                    })
                }
            }
        })
    }
    //商品分类
    var _queryProductGroup = function(){
        $("#groupId").empty();
        var url = CONTEXT_PATH+"/product/group";
        $.ajax({
            url:url,
            method:"GET",
            success:function(res){
                var html ="<option value=''>全部</option>"
                $("#groupId").append(html);
                $.each(res,function(i,group){
                    var html_ = "<option value='"+group.groupId+"'>";
                    html_+=group.groupName+"</option>";
                    $("#groupId").append(html_);
                });
                $("#groupId").select2({
                    theme: "jumi"
                });
            }
        })
    }
    var _initPagination = function(){
        var url = ajaxUrl.url1;
        var paramsQo={};
        paramsQo.tabIndex=2;
        paramsQo.pageSize=pageSize;
        paramsQo.productName= $("#productName").val();
        paramsQo.beginTime= $("#beginDate2").val();
        paramsQo.endTime= $("#endDate2").val();
        paramsQo.groupId= $("#groupId").val();
        paramsQo.status = $("#status").val();
        paramsQo.sort = $("#sort").val();
        paramsQo.platForm = $("#platForm2").val();
        jumi.pagination("#pageToolbar",url,paramsQo,function (res,curPage) {
            if(res.code===0){
                //判断是否第一页
                var data = {
                    items:res.data.productsAnalysisInfo.items,
                };
                jumi.template(ajaxUrl.tplUrl2,data,function(tpl){
                    $("#u-tab4").empty();
                    $("#u-tab4").html(tpl);
                })
            }
        })
    }
    return {
        init: _init,
        initBase:_initBase,
        initPagination:_initPagination,
        exportCsv:_exportCsv
    };
})();
