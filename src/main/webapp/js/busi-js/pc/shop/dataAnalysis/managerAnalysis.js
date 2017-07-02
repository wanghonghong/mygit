/**
 * Created by cj on 2016/11/21.
 */
CommonUtils.regNamespace("dataAnalysis", "manager");

dataAnalysis.manager = (function(){
    var ajaxUrl = {
        url1:CONTEXT_PATH+'/data_analysis/manager',
        tplUrl1:'shop/dataAnalysis/manager/basic1',
        tplUrl2:'shop/dataAnalysis/manager/basic2',
        tplUrl3:'shop/dataAnalysis/manager/basic3'
    }
    var _platForm = function (n) {
        if(n===1){  // wx
            $(".u-tab1").addClass("active");
            $(".u-tab2").removeClass("active");
        }else if(2===n){ //wb
            $(".u-tab2").addClass("active");
            $(".u-tab1").removeClass("active");
        }
        var tabIndex = $(".z-sel.manager").val(); // 1:今日 2：昨日 3：本月
        _initBase(tabIndex,n);
    }
    var _init = function(n){
        var obj = $('#m_'+n);
        obj.addClass('z-sel manager').siblings().removeClass('z-sel manager');
        $(".panel-hidden_m"+n).show().siblings().hide();
        var wx = $('.u-tab1.active').length;
        var wb = $('.u-tab2.active').length;
        var platForm ;
        if(wx===1){
            platForm = 1;
        }else if(wb===1){
            platForm = 2;
        }
        _initBase(n,platForm);

    }
    var _initBase = function(tabIndex,platForm) {
        var paramsQo={};
        paramsQo.tabIndex = tabIndex;
        paramsQo.platForm = platForm;
        var params;
        if(tabIndex===1){
            params = JSON.stringify(paramsQo);
            _getHtml(tabIndex,params,ajaxUrl.tplUrl1);
        }else if(tabIndex===2){
            params = JSON.stringify(paramsQo);
            _getHtml(tabIndex,params,ajaxUrl.tplUrl2);
        }else{
            paramsQo.flag='D';
            params = JSON.stringify(paramsQo);
            _getHtml(tabIndex,params,ajaxUrl.tplUrl3);
        }
    }
    var _getHtml = function(tabIndex,params,tplUrl){
        $.ajaxJson(ajaxUrl.url1,params,{
            "done":function (res) {
                if(res.code===0){
                    if(tabIndex===1){
                        var data = {
                            date:_date(0),
                            funsInfo:res.data.funsInfo,
                            productInfo:res.data.productInfo,
                            orderInfo:res.data.orderInfo,
                            rpInfo:res.data.rpInfo
                        }
                        jumi.template(tplUrl,data,function (tpl) {
                            $('#u-tab1').empty();
                            $('#u-tab1').html(tpl);
                        })
                    }else if(tabIndex===2){
                        var data = {
                            date:_date(-1),
                            rpInfo:res.data.rpInfo,
                            orderInfo:res.data.orderInfo,
                            funsInfo:res.data.funsInfo,
                            productInfo:res.data.productInfo
                        }
                        jumi.template(tplUrl,data,function (tpl) {
                            $('#u-tab2').empty();
                            $('#u-tab2').html(tpl);
                        })
                    }else{
                        var data = {
                            date:_date(30),
                            rpInfo:res.data.rpInfo,
                            orderInfo:res.data.orderInfo,
                            funsInfo:res.data.funsInfo,
                            productInfo:res.data.productInfo
                        }
                        jumi.template(tplUrl,data,function (tpl) {
                            $('#u-tab3').empty();
                            $('#u-tab3').html(tpl);
                        })
                    }
                }
            }
        })
    }
   var _date = function(n){
       var myDate = new Date();

       if(n===0){
           myDate.setDate(myDate.getDate()+n);
           var date = myDate.toLocaleString();
           return date;
          // $('#currenDate').text(date);
       }else if(n=== -1){
           myDate.setDate(myDate.getDate()+n);
           var y = myDate.getFullYear();
           var m = myDate.getMonth()+1;
           var d = myDate.getDate();
           var day = ' 星期'+'日一二三四五六'.charAt(myDate.getDay());
           var date = y+"-"+m+"-"+d+day;
           return date;
       }else{
           myDate.setDate(myDate.getDate());
           var y = myDate.getFullYear();
           var m = myDate.getMonth()+1;
           var date = y+" 年 "+m+" 月";
           return date;
       }

   }

    return {
        init: _init,
        initBase:_initBase,
        platForm:_platForm
    };
})();
