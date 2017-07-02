CommonUtils.regNamespace("dataAnalysis", "menu");

dataAnalysis.menu = (function(){
    var menuRes;
    var pageSize=10;
    var ajaxUrl = {
        url1:CONTEXT_PATH+'/data_analysis/menu',
        csvUrl:CONTEXT_PATH+'/data_analysis/menuCsv',
        tplUrl1:'shop/dataAnalysis/menu/basic1',
        tplUrl2:'shop/dataAnalysis/menu/basic2',
        tplUrl3:'shop/dataAnalysis/menu/basic3'
    };

    var _exportCsv = function(n){
        var paramsQo={};
        paramsQo.flag = "A";
        var params = JSON.stringify(paramsQo);
        window.location.href =ajaxUrl.csvUrl+"?paramsQo="+params+"&beginTime="+$("#startDate1").val()+"&endTime="+$("#endDate1").val();
    }
    var _init = function(n,flag){
        _initMenuName();
        _initBase(n,flag);
    }

    var _initBase = function(n,flag) {
        var obj = $('#menu_'+n);
        obj.addClass('z-sel').siblings().removeClass('z-sel');
        $(".panel-hidden_menu"+n).show().siblings().hide();
        var obj2 = $('#m_'+n);
        obj2.addClass('z-sel').siblings().removeClass('z-sel');
        if(flag == 'B'){
            $("#startDate1").val(new Date().toISOString().substr(0,10)+" 00:00:00");
            $("#endDate1").val(new Date().toISOString().substr(0,10)+" 23:59:59");
        }
        var paramsQo={};
        paramsQo.tabIndex=n;
        if(n===1){
            paramsQo.platForm = $("#platForm1").val();
            paramsQo.flag = flag;
            paramsQo.beginTime = $("#startDate1").val();
            paramsQo.endTime = $("#endDate1").val();
        }else if(n===2){
            paramsQo.menuId = $('#menuName2').val() //菜单id
            paramsQo.flag="A";
        }

        $.ajaxJson(ajaxUrl.url1,paramsQo,{
            "done":function (res) {
                if(res.code===0){
                    if(n===1){
                        var data = {
                            menuAnalysisInfo:res.data.menuAnalysisInfo
                        }
                        jumi.template(ajaxUrl.tplUrl3,data,function (tpl) {
                            $('#u-tab1').empty();
                            $('#u-tab1').html(tpl);
                        })
                    }else if(n===2){
                        $('#selectDate').val("A");
                        var data = {
                            dateInfo:res.data.date,
                            menuOnclickInfo:res.data.menuOnclicks,
                            menuOnclickUsersInfo:res.data.menuOnclickUsers,
                            perCapitaInfo:res.data.perCapita
                        }
                        _lineMenuAnalysis(data,0); //订单分析线性图(数值)
                    }

                }
            }
        })
    }
    var _initBase2 = function(n,flag) {
        var obj = $('#menu_'+1);
        obj.addClass('z-sel').siblings().removeClass('z-sel');
        $(".panel-hidden_menu"+1).show().siblings().hide();
        _changeTab1(n);
        var paramsQo={};
        paramsQo.tabIndex=1;
        paramsQo.platForm = $("#platForm1").val();
        paramsQo.flag = flag;
        paramsQo.beginTime = $("#startDate1").val();
        paramsQo.endTime = $("#endDate1").val();
        $.ajaxJson(ajaxUrl.url1,paramsQo,{
            "done":function (res) {
                if(res.code===0){
                    var data = {
                        menuAnalysisInfo:res.data.menuAnalysisInfo
                    }
                    jumi.template(ajaxUrl.tplUrl3,data,function (tpl) {
                        $('#u-tab1').empty();
                        $('#u-tab1').html(tpl);
                    })
                }
            }
        })
    }
    var _changeTab1 = function (n){
        if(n===0){
            var obj = $('#m_'+5);
            obj.addClass('z-sel').siblings().removeClass('z-sel');
        }else{
            var obj = $('#m_'+n);
            obj.addClass('z-sel').siblings().removeClass('z-sel');
        }
    };
    // 初始化该店铺下的所有活动名称
    var _initMenuName = function () {
        var url = CONTEXT_PATH+"/data_analysis/menuInfo";
        $.ajax({
            url:url,
            method:"GET",
            success:function(res){
                menuRes = res;
                _queryMenuName();
            }
        })
    };
    
    //菜单名称
    var _queryMenuName = function(){
        $("#menuName2").empty();
        $.each(menuRes,function(i,menu){
                var html_ = "<option value='"+menu.id+"'>";
                html_+=menu.name+"</option>";
                $("#menuName2").append(html_);
        });
        var count = $("#menuName2")[0].options.length;
        if(count==0){
            var html_ = "<option value='-1'>";
            html_+="</option>";
            $("#menuName2").append(html_);
        }
        $("#menuName2").select2({
            theme: "jumi"
        });
    };

// 线性分析
    var _lineAnalysis = function(n,menuId){
        var paramsQo={};
        var sell = 0;
        paramsQo.tabIndex=2;
        paramsQo.menuId=menuId;
        if(n == 'A'){ // 10天
            paramsQo.flag="A";
        }else{
            paramsQo.flag="B";
            sell = 2;
        }
        var params = JSON.stringify(paramsQo)
        $.ajaxJson(ajaxUrl.url1,params,{
            "done":function (res) {
                if(res.code===0){
                    var data = {
                        dateInfo:res.data.date,
                        menuOnclickInfo:res.data.menuOnclicks,
                        menuOnclickUsersInfo:res.data.menuOnclickUsers,
                        perCapitaInfo:res.data.perCapita
                    }
                    _lineMenuAnalysis(data,sell); //订单分析线性图(数值)
                }
            }
        })
    };
    var _lineMenuAnalysis = function (data,n){
        //菜单分析
        var myChartmenuanalyse = echarts.init(document.getElementById('chartmenuanalyse'));
        var optionmenuanalyse = {
            tooltip: {
                trigger: 'axis'
            },
            legend: {
                data:['菜单点击次数','菜单点击人数','人均点击次数']
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            toolbox: {
                right: '20',
                feature: {
                    saveAsImage: {}
                }
            },
            xAxis: {
                type: 'category',
                boundaryGap: false,
                data: data.dateInfo,
                axisLabel: {
                    show: true,
                    interval: n,//隔几个刻度显示
                    inside: false,
                    rotate: 0,//坐标显示角度
                    formatter: null,//坐标单位
                }
            },
            yAxis: {
                type: 'value',
                name:"数值"
            },
            color:['#5d9cec', '#f15755','#66cc99'],
            series: [
                {
                    name:'菜单点击次数',
                    type:'line',
                    symbol: 'circle',
                    symbolSize: '7',
                    data:data.menuOnclickInfo
                },
                {
                    name:'菜单点击人数',
                    type:'line',
                    symbol: 'circle',
                    symbolSize: '7',
                    data:data.menuOnclickUsersInfo
                },
                {
                    name:'人均点击次数',
                    type:'line',
                    symbol: 'circle',
                    symbolSize: '7',
                    data:data.perCapitaInfo
                }
            ]
        };
        myChartmenuanalyse.setOption(optionmenuanalyse);
        //菜单分析end
    }

    return {
        init: _init,
        initBase:_initBase,
        lineMenuAnalysis:_lineMenuAnalysis,
        lineAnalysis:_lineAnalysis,
        initBase2:_initBase2,
        exportCsv:_exportCsv
    };
})();
