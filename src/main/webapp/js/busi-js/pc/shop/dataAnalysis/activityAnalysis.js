CommonUtils.regNamespace("dataAnalysis", "activity");

dataAnalysis.activity = (function(){
    var activityRes;
    var pageSize=5;
    var ajaxUrl = {
        url1:CONTEXT_PATH+'/data_analysis/activity',
        tplUrl1:'shop/dataAnalysis/activity/basic3',
        tplUrl2:'shop/dataAnalysis/activity/basic4',
        tplUrl3:'shop/dataAnalysis/activity/basic5',
        tplUrl4:'shop/dataAnalysis/activity/basic6',
        tplUrl5:'shop/dataAnalysis/activity/basic7'
    }

    var _redirectAnalysis = function (id,subTypeId) {
        var obj = $('#activity_'+2);
        obj.addClass('z-sel').siblings().removeClass('z-sel');
        $(".panel-hidden_a"+2).show().siblings().hide();
        _queryActivityName();
        var paramsQo={};
        paramsQo.tabIndex=2;//效果分析
        paramsQo.activityId=id;
        _getHtml(2,paramsQo,ajaxUrl.url1);
    }

    // tab==2 根据小类来刷新模板
    var _returnTpl = function (subTypeId,data){
        var tplUrl;
        if(subTypeId==1){
            tplUrl = ajaxUrl.tplUrl2;
        }else if(subTypeId==3){
            tplUrl = ajaxUrl.tplUrl3;
        }else if(subTypeId==5){
            tplUrl = ajaxUrl.tplUrl4;
        }
        jumi.template(ajaxUrl.tplUrl5,data,function (tpl) {
            $('#viewInfo').empty();
            $('#viewInfo').html(tpl);
        })
        jumi.template(tplUrl,data,function (tpl) {
            $('#u-tab2').empty();
            $('#u-tab2').html(tpl);
        })
        _initActivityDetail(subTypeId,data);
    }
    var _init = function(n){
        var obj = $('#activity_'+n);
        obj.addClass('z-sel').siblings().removeClass('z-sel');
        $(".panel-hidden_a"+n).show().siblings().hide();
        _initActivityName();
        _initBase(n);
    }


    var _initBase = function(tabIndex) {
        var paramsQo={};
        paramsQo.tabIndex=tabIndex;//基础数据，效果分析
        if(tabIndex===1){
            paramsQo.pageSize=pageSize;
            _initPagination(paramsQo);
        }else if(tabIndex===2){
            paramsQo.activityId= $("#activityName2").val();
            _getHtml(tabIndex,paramsQo,ajaxUrl.url1);
        }
    }
    var _search = function (n) {
        var paramsQo={};
        paramsQo.tabIndex=n;
        if(n===1){
            paramsQo.pageSize=pageSize;
            paramsQo.activityName= $("#activityName1").val();
            paramsQo.beginTime= $("#startDate1").val();
            paramsQo.endTime= $("#endDate1").val();
            paramsQo.activityTypeId= $("#typeId1").val();
            paramsQo.activityStatus= $("#status").val();
            _initPagination(paramsQo);
        }else if(n===2){
            paramsQo.activityId= $("#activityName2").val();
            _getHtml(n,paramsQo,ajaxUrl.url1);
        }
    }
    // 活动效果--获取
    var _getHtml = function(tabIndex,params,tplUrl){
        $.ajaxJson(ajaxUrl.url1,params,{
            "done":function (res) {
                if(res.code===0){
                    if(!res.data.activityInfo){
                        $('#isOcDivOne').hide();
                        $('#isOcDivTwo').show();
                        return;
                    }
                    $('#isOcDivOne').show();
                    $('#isOcDivTwo').hide();
                    var data = {
                        activityInfo:res.data.activityInfo,
                        sexInfo:res.data.sexInfo,
                        addUserInfo:res.data.addUserInfo,
                        buyUserInfo:res.data.buyUserInfo,
                        activityAnalysisInfo:res.data.activityAnalysisInfo,
                        orderInfo:res.data.orderInfo,
                        orderMoneyInfo:res.data.orderMoneyInfo
                    }
                    _returnTpl(res.data.activityInfo.subType,data);

                }
            }
        })
    }
    //活动效果分析
    var _initActivityDetail = function (subTypeId,data) {
        var sexInfo = data.sexInfo;
        var addUserInfo = data.addUserInfo;
        var buyUserInfo = data.buyUserInfo;
        var orderInfo = data.orderInfo;
        var orderMoneyInfo = data.orderMoneyInfo;
        if(sexInfo.length===0){
            sexInfo = [
                {value:0, name:'女性'},
                {value:0, name:'男性'},
                {value:0, name:'未知'}
            ]
        }
        //性别比例begin
        var myChartsex = echarts.init(document.getElementById('chartsex'));
        var optionsex = {
            tooltip: {
                trigger: 'item',
                formatter: "{a} <br/>{b}: {c} ({d}%)"
            },
            legend: {
                orient: 'vertical',
                x: '80%',
                y: 'middle',
                selectedMode: false,
                data:[
                    {
                        name: '女性',
                        icon: 'circle'
                    },
                    {
                        name: '男性',
                        icon: 'circle'
                    },
                    {
                        name: '未知',
                        icon: 'circle'
                    },
                    '女性','男性','未知']
            },
            color:['#f15755', '#62c87f','#5d9cec'] ,
            series: [
                {
                    name:'性别比例',
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
                    data:sexInfo
                }
            ]
        };
        myChartsex.setOption(optionsex);
        //性别比例end

        //地区分布-粉丝数begin
        var myChartmap = echarts.init(document.getElementById('chartmap'));
        function randomData() {
            return;
        }
        if(subTypeId==1){
            optionmap = {
                tooltip: {
                    trigger: 'item',
                    formatter: function(params) {
                        var res = params.name;
                        var myseries = optionmap.series;
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
                    data:['新增粉丝','转购买人']
                },
                color: ['#5d9cec'],
                visualMap: {
                    min: 0,
                    max: addUserInfo[0].value+buyUserInfo[0].value,
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
                        name: '新增粉丝',
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
                        data:addUserInfo
                    },
                    {
                        name: '转购买人',
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
                        data:buyUserInfo
                    }
                ]
            };
        }else if(subTypeId==3){
            optionmap = {
                tooltip: {
                    trigger: 'item',
                    formatter: function(params) {
                        var res = params.name;
                        var myseries = optionmap.series;
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
                    data:['订单量','订单金额(元)']
                },
                color: ['#5d9cec'],
                visualMap: {
                    min: 0,
                    max: orderInfo[0].value+orderMoneyInfo[0].value,
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
                        name: '订单量',
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
                        data:orderInfo
                    },
                    {
                        name: '订单金额(元)',
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
                        data:orderMoneyInfo
                    }
                ]
            };
        }

        myChartmap.setOption(optionmap);
        //地区分布-粉丝数end
    }

    var _initPagination = function(paramsQo){
        var url = ajaxUrl.url1;
        jumi.pagination("#pageToolbar",url,paramsQo,function (res,curPage) {
            if(res.code===0){
                //判断是否第一页
                var data = {
                    items:res.data.activityAnalysisInfo.items,
                };
                jumi.template(ajaxUrl.tplUrl1,data,function(tpl){
                    $("#u-tab1").empty();
                    $("#u-tab1").html(tpl);
                })
            }
        })
    }

    // 初始化该店铺下的所有活动名称
    var _initActivityName = function () {
        var url = CONTEXT_PATH+"/activity/list/0";
        $.ajax({
            url:url,
            method:"GET",
            success:function(res){
                activityRes = res;
                _queryActivityName();
            }
        })
    }

    //活动名称
    var _queryActivityName = function(){
        var typeId = $("#typeId2").val();
        $("#activityName2").empty();
        $.each(activityRes,function(i,activity){
            if(activity.type == typeId){
                var html_ = "<option value='"+activity.id+"'>";
                html_+=activity.activityName+"</option>";
                $("#activityName2").append(html_);
            }
        });
        var count = $("#activityName2")[0].options.length;
        if(count==0){
                var html_ = "<option value='-1'>";
                html_+="</option>";
                $("#activityName2").append(html_);
        }
        $("#activityName2").select2({
            theme: "jumi"
        });
    }

    return {
        init: _init,
        initBase:_initBase,
        initPagination:_initPagination,
        queryActivityName:_queryActivityName,
        search:_search,
        redirectAnalysis:_redirectAnalysis
    };
})();
