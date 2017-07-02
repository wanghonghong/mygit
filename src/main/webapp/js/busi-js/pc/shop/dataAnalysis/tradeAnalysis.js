/**
 * Created by cj on 2016/11/18.
 */
CommonUtils.regNamespace("dataAnalysis", "trade");

dataAnalysis.trade = (function(){
    var ajaxUrl = {
        url1:CONTEXT_PATH+'/data_analysis/trade',
        tplUrl1:'shop/dataAnalysis/trade/basic3',
        tplUrl2:'shop/dataAnalysis/trade/basic4'
    }
    var _init = function(n,flag){
        var obj = $('#trade_'+n);
        obj.addClass('z-sel').siblings().removeClass('z-sel');
        $(".panel-hidden_t"+n).show().siblings().hide();
        if(n===1 || n===2){
            $("#beginDate"+n).val(new Date().toISOString().substr(0,10)+" 00:00:00");
            $("#endDate"+n).val(new Date().toISOString().substr(0,10)+" 23:59:59");
        }
        if(flag==='B'){ //初始化当天数据
            _initBase2(n,flag);
        }else{
            _initBase(n);
        }
    }
    var _initBase = function(tabIndex,beginTime,endTime) {
        var paramsQo={};
        paramsQo.tabIndex=tabIndex;//转化分析，客户分析
        paramsQo.beginTime=beginTime;
        paramsQo.endTime=endTime;
        paramsQo.platForm = $("#platForm"+tabIndex).val();
        if(tabIndex===1){
            _getHtml(tabIndex,paramsQo,ajaxUrl.tplUrl1);
        }else if(tabIndex===2){
            _getHtml(tabIndex,paramsQo,ajaxUrl.tplUrl2);
        }else if(tabIndex===3){
            paramsQo.flag = 'A';
            _getHtml(tabIndex,paramsQo);
        }
    }
    var _initBase2 = function(tabIndex,flag) {
        var paramsQo={};
        paramsQo.flag = flag;
        paramsQo.tabIndex=tabIndex;//转化分析，客户分析
        paramsQo.platForm = $("#platForm"+tabIndex).val();
        if(tabIndex===1){
            _getHtml(tabIndex,paramsQo,ajaxUrl.tplUrl1);
        }else if(tabIndex===2){
            _getHtml(tabIndex,paramsQo,ajaxUrl.tplUrl2);
        }
    }
    var _getHtml = function(tabIndex,params,tplUrl){
        $.ajaxJson(ajaxUrl.url1,params,{
            "done":function (res) {
                if(res.code===0){
                    if(tabIndex===1){
                        var data = {
                            visitUsers:res.data.visitUsers,
                            placeOrderInfo:res.data.placeOrderInfo,
                            returnOrderInfo:res.data.returnOrderInfo,
                            payOrderInfo:res.data.payOrderInfo
                        }
                        jumi.template(tplUrl,data,function (tpl) {
                            $('#u-tab3').empty();
                            $('#u-tab3').html(tpl);
                        })
                    }else if(tabIndex===2){
                        var data = {
                            customerInfo:res.data.customerInfo,
                            ordersInfo:res.data.ordersInfo,
                            levelInfo:res.data.levelInfo,
                            buyInfo:res.data.buyInfo,
                            bvInfo:res.data.bvInfo,
                            visitInfo:res.data.visitInfo
                        }
                        jumi.template(tplUrl,data,function (tpl) {
                            $('#u-tab4').empty();
                            $('#u-tab4').html(tpl);
                            _customerAnalysis(data);
                        })
                    }else if(tabIndex===3){
                        $('#selectDate').val("A");
                        var data = {
                            dateInfo:res.data.date,
                            visitersInfo:res.data.visiters,
                            buyUsersInfo:res.data.buyUsers,
                            buyOrdersInfo:res.data.buyOrders,
                            buyCountInfo:res.data.buyCount,
                            backCountInfo:res.data.backCount,
                            rInfo:res.data.rMoney,
                            rwInfo:res.data.rwMoney,
                            wbInfo:res.data.wbMoney,
                            bInfo:res.data.bMoney
                        }
                        _lineAnalysisCount(data,0); //订单分析线性图(数值)
                        _lineAnalysisMoney(data,0); //交易分析线性图(金额)
                    }

                }
            }
        })
    }
    // 线性分析
    var _lineAnalysis = function(n){
        var paramsQo={};
        var sell = 0;
        paramsQo.platForm = $("#platForm3").val();
        paramsQo.tabIndex=3;
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
                        visitersInfo:res.data.visiters,
                        buyUsersInfo:res.data.buyUsers,
                        buyOrdersInfo:res.data.buyOrders,
                        buyCountInfo:res.data.buyCount,
                        backCountInfo:res.data.backCount,
                        rInfo:res.data.rMoney,
                        rwInfo:res.data.rwMoney,
                        wbInfo:res.data.wbMoney,
                        bInfo:res.data.bMoney
                    }
                    _lineAnalysisCount(data,sell); //订单分析线性图(数值)
                    _lineAnalysisMoney(data,sell); //交易分析线性图(金额)
                }
            }
        })
    }
    var _lineAnalysisCount = function (data,n){
            //交易分析begin
            var myChartbuyanalyse = echarts.init(document.getElementById('chartbuyanalyse'));
            /*var itemStyle = {
                name:'浏览人数',
                type:'line',
                stack: '总量',
                symbol: 'circle',
                symbolSize: '7'
            }
            var b = [];
            var arry = new Array();
            arry.push([1,2,3]);
            arry.push([3,4,5]);
             for (var i = 0; i < arry.length; i++){
                 itemStyle.data = arry[i];
                 b.push(itemStyle);
             }*/
            var optionbuyanalyse = {
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    data:['浏览人数','付款人数','付款单数','付款件数','退单件数']
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
                color:['#5d9cec', '#f15755','#66cc99','#ff99cc','#ff9933'],
                series: [
                    {
                        name:'浏览人数',
                        type:'line',
                        symbol: 'circle',
                        symbolSize: '7',
                        data:data.visitersInfo
                    },
                    {
                        name:'付款人数',
                        type:'line',
                        symbol: 'circle',
                        symbolSize: '7',
                        data:data.buyUsersInfo
                    },
                    {
                        name:'付款单数',
                        type:'line',
                        symbol: 'circle',
                        symbolSize: '7',
                        data:data.buyOrdersInfo
                    },
                    {
                        name:'付款件数',
                        type:'line',
                        symbol: 'circle',
                        symbolSize: '7',
                        data:data.buyCountInfo
                    },
                    {
                        name:'退单件数',
                        type:'line',
                        symbol: 'circle',
                        symbolSize: '7',
                        data:data.backCountInfo
                    }
                ]
            };
            myChartbuyanalyse.setOption(optionbuyanalyse);
            //交易分析end
    }
    var _lineAnalysisMoney = function (data,n){
            //交易分析begin
            var myChartbuyanalyse = echarts.init(document.getElementById('chartMoneyanalyse'));
            var optionbuyanalyse = {
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    data:['商品交易额','待付款额','待退款额','已退款额']
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
                    name:'金额'
                },
                color:['#5d9cec', '#f15755','#66cc99','#ff99cc'],
                series: [
                    {
                        name:'商品交易额',
                        type:'line',
                        symbol: 'circle',
                        symbolSize: '7',
                        data:data.rInfo
                    },
                    {
                        name:'待付款额',
                        type:'line',
                        symbol: 'circle',
                        symbolSize: '7',
                        data:data.rwInfo
                    },
                    {
                        name:'待退款额',
                        type:'line',
                        symbol: 'circle',
                        symbolSize: '7',
                        data:data.wbInfo
                    },
                    {
                        name:'已退款额',
                        type:'line',
                        symbol: 'circle',
                        symbolSize: '7',
                        data:data.bInfo
                    }
                ]
            };
            myChartbuyanalyse.setOption(optionbuyanalyse);
            //交易分析end
    }

    var _customerAnalysis = function (data){
        var customerInfo = data.customerInfo;
        var ordersInfo = data.ordersInfo;
        var levelInfo = data.levelInfo;
        var buyInfo = data.buyInfo;
        var visitInfo = data.visitInfo;
        //地区分布begin
        var myChartareaplace = echarts.init(document.getElementById('chartareaplace'));
        optionareaplace = {
            tooltip: {
                trigger: 'item',
                formatter: function(params) {
                    var res = params.name;
                    var myseries = optionareaplace.series;
                    for (var i = 0; i < myseries.length; i++) {
                        res+= '<br/>'+myseries[i].name+' : ';
                        for(var j=0;j<myseries[i].data.length;j++){
                            if(myseries[i].data[j].name==params.name){
                                res+=myseries[i].data[j].value;
                            }
                        }
                    }
                    return res;
                }
            },
            legend: {
                orient: 'vertical',
                left: 'left',
                selectedMode: false,
                data:['购买人数','访问人数']
            },
            color: ['#5d9cec'],
            visualMap: {
                min: 0,
                max: buyInfo[0].value+visitInfo[0].value,
                left: 'left',
                top: 'bottom',
                text: ['高','低'],
                calculable: true,
                inRange: {
                    color: ['#dceffc', '#9cc5f4', '#5d9cec']
                }
            },
            series: [
                {
                    name: '购买人数',
                    type: 'map',
                    mapType: 'china',
                    roam: false,
                    showLegendSymbol: false,
                    itemStyle: {
                        normal: {
                            areaColor: '#cccccc',
                            borderColor: '#ffffff',
                        },
                        emphasis: {
                            areaColor: 'gold'
                        }
                    },
                    label: {
                        show: false
                    },
                    data:buyInfo
                },
                {
                    name: '访问人数',
                    type: 'map',
                    mapType: 'china',
                    roam: false,
                    showLegendSymbol: false,
                    itemStyle: {
                        normal: {
                            areaColor: '#cccccc',
                            borderColor: '#ffffff',
                        },
                        emphasis: {
                            areaColor: 'gold'
                        }
                    },
                    label: {
                        show: false
                    },
                    data:visitInfo
                }
            ]
        };
        myChartareaplace.setOption(optionareaplace);
        //地区分布end

        //新老客户begin
        var myChartcustomer = echarts.init(document.getElementById('chartcustomer'));
        var optioncustomer = {
            tooltip: {
                trigger: 'item',
                formatter: "{a} <br/>{b}: {c} ({d}%)"
            },
            legend: {
                orient: 'vertical',
                x: 'left',
                selectedMode: false,
                data:['新客户','老客户']
            },
            color:['#f15755','#66cc99'],
            series: [
                {
                    name:'客户构成',
                    type:'pie',
                    radius: ['35%', '90%'],
                    avoidLabelOverlap: false,
                    minAngle: 5,
                    label: {
                        normal: {
                            show: false,
                            position: 'center'
                        },
                        emphasis: {
                            show: true,
                            textStyle: {
                                fontSize: '18',
                                fontWeight: 'bold'
                            }
                        }
                    },
                    labelLine: {
                        normal: {
                            show: false
                        }
                    },
                    data:[
                        {value:customerInfo.newCount, name:'新客户'},
                        {value:customerInfo.oldCount, name:'老客户'}
                    ]
                }
            ]
        };
        myChartcustomer.setOption(optioncustomer);
        //新老客户end

        //金额区间begin
        var myChartmoney = echarts.init(document.getElementById('chartmoney'));
        var optionmoney = {
            color: ['#3398DB'],
            tooltip : {
                trigger: 'axis',
                axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                    type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis : [
                {
                    type : 'category',
                    data : ['0.0-10.00', '10.00-50.00', '50.00-100.00','100.00-200.00','200.00-300.00','300.00-500.00', '500.00元以上'],
                    axisTick: {
                        alignWithLabel: true
                    }
                }
            ],
            yAxis : [
                {
                    name : '订单数',
                    type : 'value',
                    nameTextStyle: {
                        color: '#999999'
                    }
                }
            ],
            series : [
                {
                    name:'订单数',
                    type:'bar',
                    barWidth: '60%',
                    data:[ordersInfo.count1, ordersInfo.count2, ordersInfo.count3, ordersInfo.count4,ordersInfo.count5,ordersInfo.count6,ordersInfo.count7]
                }
            ]
        };
        myChartmoney.setOption(optionmoney);
        //金额区间end

        //等级比例begin
        var myChartgrade = echarts.init(document.getElementById('chartgrade'));
        var optiongrade = {
            tooltip: {
                trigger: 'item',
                formatter: "{a} <br/>{b}: {c} ({d}%)"
            },
            color:['#c6a830','silver','gold'],
            series: [
                {
                    name:'等级比例',
                    type:'pie',
                    radius: ['35%', '90%'],
                    avoidLabelOverlap: false,
                    minAngle: 5,
                    label: {
                        normal: {
                            show: false,
                            position: 'center'
                        },
                        emphasis: {
                            show: true,
                            textStyle: {
                                fontSize: '18',
                                fontWeight: 'bold'
                            }
                        }
                    },
                    labelLine: {
                        normal: {
                            show: false
                        }
                    },
                    data:[
                        {value:levelInfo.copper, name:'铜卡'},
                        {value:levelInfo.silver, name:'银卡'},
                        {value:levelInfo.gold, name:'金卡'}
                    ]
                }
            ]
        };
        myChartgrade.setOption(optiongrade);
        //等级比例end
    }
    return {
        init: _init,
        initBase:_initBase,
        lineAnalysisCount:_lineAnalysisCount,
        lineAnalysisMoney:_lineAnalysisMoney,
        lineAnalysis:_lineAnalysis
    };
})();
