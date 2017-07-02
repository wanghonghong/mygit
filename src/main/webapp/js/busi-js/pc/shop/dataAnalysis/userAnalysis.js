CommonUtils.regNamespace("dataAnalysis", "user");
dataAnalysis.user = (function() {
    var ajaxUrl = {
        url1:CONTEXT_PATH+'/data_analysis/user',
        tplUrl1:'shop/dataAnalysis/user/basicF',
        tplUrl2:'shop/dataAnalysis/user/basic4',
        tplUrl3:'shop/dataAnalysis/user/basic3'
    }

    //日常增减 ,分类资料，访问记录
    var _init =function (n) {
        var obj = $('#p_'+n);
        obj.addClass('z-sel').siblings().removeClass('z-sel');
        $(".panel-hidden_"+n).show().siblings().hide(); //日常增减 ,分类资料，访问记录
        if(n===1){//日常增减
            $("#beginDate").val("");
            $("#endDate").val("");
            _initBase(n,'F','B');
        }else{//分类资料，访问记录
            $("#beginDate"+n).val(new Date().toISOString().substr(0,10)+" 00:00:00");
            $("#endDate"+n).val(new Date().toISOString().substr(0,10)+" 23:59:59");
            _initBase2(n,null,null,'B');
        }
    }

    var _initBase = function(n,sign,flag,beginTime,endTime){
        if(flag == 'B'){
            $("#beginDate").val(new Date().toISOString().substr(0,10)+" 00:00:00");
            $("#endDate").val(new Date().toISOString().substr(0,10)+" 23:59:59");
        }

        if(sign==''){
            sign = $("#show_f").attr('flag');
        }
      if(n===0){
            var obj = $('#f_'+5);
            obj.addClass('z-sel').siblings().removeClass('z-sel');
       }else{
            var obj = $('#f_'+n);
            obj.addClass('z-sel').siblings().removeClass('z-sel');
        }

        var paramsQo={};
        paramsQo.tabIndex=1;
        paramsQo.flag=flag;
        paramsQo.beginTime=beginTime;
        paramsQo.endTime=endTime;
        paramsQo.platForm = $("#platForm1").val();
        var params = JSON.stringify(paramsQo)
        _getHtml(sign,params,ajaxUrl.tplUrl1);
    }
    var _getHtml = function(sign,params,tplUrl){
        $.ajaxJson(ajaxUrl.url1,params,{
            "done":function (res) {
                if(res.code===0){
                    var data = {
                        items:res.data.userBaseInfo
                    }
                    if(sign=='F'){//表格
                        jumi.template(tplUrl,data,function (tpl) {
                            $('#u-tab1').empty();
                            $('#u-tab1').html(tpl);
                        })
                    }else if(sign=='G' ){//柱状图
                        _initGraph(data);
                    }
                }
            }
        })
    }
    var _initBase2 = function(n,beginTime,endTime,flag){
        var paramsQo={};
        paramsQo.tabIndex=n;
        paramsQo.beginTime=beginTime;
        paramsQo.endTime=endTime;
        paramsQo.flag = flag;
        paramsQo.platForm = $("#platForm"+n).val();
        var params = JSON.stringify(paramsQo);
        if(n===2){
            _getHtml2(n,params,ajaxUrl.tplUrl2);
        }else if(n===3){
            _getHtml2(n,params,ajaxUrl.tplUrl3);
        }

    }
    var _getHtml2 = function(tabIndex,params,tplUrl){
        $.ajaxJson(ajaxUrl.url1,params,{
            "done":function (res) {
                if(res.code===0){
                    if(tabIndex===2){
                        var data = {
                            userSexInfo:res.data.userSexInfo,
                            levelInfo:res.data.levelInfo,
                            funsInfo:res.data.funsInfo,
                            roleInfo:res.data.roleInfo
                        }
                        jumi.template(tplUrl,data,function (tpl) {
                            $('#u-tab3').empty();
                            $('#u-tab3').html(tpl);
                            _initClassify(data);
                        })
                    }else{
                        var data = {
                            visitersInfo:res.data.visitersInfo,
                            initVisitersInfo:res.data.initVisitersInfo
                        }
                            _initVisit(data)
                    }

                }
            }
        })
    }
    // 表格；柱状
    var _showFormat = function(n){
        var sign ='';
        if(n===1){ //表格
                $(".u-tab1").addClass("active");
                $(".u-tab2").removeClass("active");
                $("#u-tab1").show();  //表格html
                $("#u-tab2").hide();
                $("#show_f").attr("flag","F");
                sign = 'F';
        }else if(n===2){//柱状
                $(".u-tab2").addClass("active");
                $(".u-tab1").removeClass("active");
                $("#u-tab2").show(); //柱状html
                $("#u-tab1").hide();
                $("#show_f").attr("flag","G");
                sign = 'G';
        }
        _initBase(1,sign,'B');
    }
    
    var _renderTableDataN = function (jsonData) {
        var jsonArray = [];
        if(jsonData.length>0){
            _.each(jsonData,function (k,v) {
                var va = _.values(k[0]);
                jsonArray.push(va);
            })
            if(jsonArray.length===1){
                var jsonD = _.zip(jsonArray[0]);
            }else{
                var jsonD = _.zip(jsonArray[0],jsonArray[1]);
            }
            return jsonD;
        }else{
            return [];
        }
    }
    
     var _renderTableData = function(jsonData){
        var json = [];
        if(jsonData.length>0){
            for(var i=0;i<jsonData.length;i++){
                var data = jsonData[i];
                for(i in jsonData[i]){
                    var nameJson = _.pluck(jsonData,i);
                    json.push(nameJson);
                    continue;
                }

            }
            return json;
        }else{
            return [];
        }
    }
    //柱状数据
    var _initGraph = function (data) {
        var arr = _renderTableDataN(data.items);
        var colData = arr[0];
        var colorData =['#ff6666','#66cc66'];
        //柱状图-新增粉丝begin
        var myChartnewfans = echarts.init(document.getElementById('chartnewfans'));
        var optionnewfans = {
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'shadow'
                }
            },
            grid: {
                left: '10%',
                right: '10%',
                bottom: '25%',
                top: '30%',
                containLabel: true
            },
            xAxis: {
                show : false,
                type: 'value',
                boundaryGap: [0, 0.01]
            },
            yAxis: {
                type: 'category',
                boundaryGap: false,
                data: colData
            },
            series: [{
                name: '新增粉丝',
                type: 'bar',
                barCategoryGap: '30%',
                data: arr[1],
                itemStyle: {
                    normal: {
                        color: function(params) {
                            var colorList = colorData;
                            return colorList[params.dataIndex]
                        }
                    }
                }
            }]
        }
        myChartnewfans.setOption(optionnewfans);
        //柱状图-新增粉丝end

        //柱状图-跑路粉丝begin
        var myChartrunfans = echarts.init(document.getElementById('chartrunfans'));
        var optionrunfans = {
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'shadow'
                }
            },
            grid: {
                left: '10%',
                right: '10%',
                bottom: '25%',
                top: '30%',
                containLabel: true
            },
            xAxis: {
                show : false,
                type: 'value',
                boundaryGap: [0, 0.01]
            },
            yAxis: {
                type: 'category',
                boundaryGap: false,
                data: colData
            },
            series: [{
                name: '跑路粉丝',
                type: 'bar',
                barCategoryGap: '30%',
                data: arr[2],
                itemStyle: {
                    normal: {
                        color: function(params) {
                            var colorList = colorData;
                            return colorList[params.dataIndex]
                        }
                    }
                }
            }]
        }
        myChartrunfans.setOption(optionrunfans);
        //柱状图-跑路粉丝end

        //柱状图-净增粉丝begin
        var myChartcleanfans = echarts.init(document.getElementById('chartcleanfans'));
        var optioncleanfans = {
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'shadow'
                }
            },
            grid: {
                left: '10%',
                right: '10%',
                bottom: '25%',
                top: '30%',
                containLabel: true
            },
            xAxis: {
                show : false,
                type: 'value',
                boundaryGap: [0, 0.01]
            },
            yAxis: {
                type: 'category',
                boundaryGap: false,
                data: colData
            },
            series: [{
                name: '净增粉丝',
                type: 'bar',
                barCategoryGap: '30%',
                data: [parseInt(arr[1][0])-parseInt(arr[2][0]),parseInt(arr[1][1])-parseInt(arr[2][1])],
                itemStyle: {
                    normal: {
                        color: function(params) {
                            var colorList = colorData;
                            return colorList[params.dataIndex]
                        }
                    }
                }
            }]
        }
        myChartcleanfans.setOption(optioncleanfans);
        //柱状图-净增粉丝end

        //柱状图-新增购买begin
        var myChartnewbuy = echarts.init(document.getElementById('chartnewbuy'));
        var optionnewbuy = {
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'shadow'
                }
            },
            grid: {
                left: '10%',
                right: '10%',
                bottom: '25%',
                top: '30%',
                containLabel: true
            },
            xAxis: {
                show : false,
                type: 'value',
                boundaryGap: [0, 0.01]
            },
            yAxis: {
                type: 'category',
                boundaryGap: false,
                data: colData
            },
            series: [{
                name: '新增购买',
                type: 'bar',
                barCategoryGap: '30%',
                data: arr[4],
                itemStyle: {
                    normal: {
                        color: function(params) {
                            var colorList = colorData;
                            return colorList[params.dataIndex]
                        }
                    }
                }
            }]
        }
        myChartnewbuy.setOption(optionnewbuy);
        //柱状图-新增购买end

        //柱状图-新增分销begin
        var myChartnewsale = echarts.init(document.getElementById('chartnewsale'));
        var optionnewsale = {
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'shadow'
                }
            },
            grid: {
                left: '10%',
                right: '10%',
                bottom: '25%',
                top: '30%',
                containLabel: true
            },
            xAxis: {
                show : false,
                type: 'value',
                boundaryGap: [0, 0.01]
            },
            yAxis: {
                type: 'category',
                boundaryGap: false,
                data: colData
            },
            series: [{
                name: '新增分销',
                type: 'bar',
                barCategoryGap: '30%',
                data: arr[5],
                itemStyle: {
                    normal: {
                        color: function(params) {
                            var colorList = colorData;
                            return colorList[params.dataIndex]
                        }
                    }
                }
            }]
        }
        myChartnewsale.setOption(optionnewsale);
        //柱状图-新增分销end

        //柱状图-新增代理begin
        var myChartnewagent = echarts.init(document.getElementById('chartnewagent'));
        var optionnewagent = {
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'shadow'
                }
            },
            grid: {
                left: '10%',
                right: '10%',
                bottom: '25%',
                top: '30%',
                containLabel: true
            },
            xAxis: {
                show : false,
                type: 'value',
                boundaryGap: [0, 0.01]
            },
            yAxis: {
                type: 'category',
                boundaryGap: false,
                data: colData
            },
            series: [{
                name: '新增代理',
                type: 'bar',
                barCategoryGap: '30%',
                data:arr[6],
                itemStyle: {
                    normal: {
                        color: function(params) {
                            var colorList =colorData;
                            return colorList[params.dataIndex]
                        }
                    }
                }
            }]
        }
        myChartnewagent.setOption(optionnewagent);
        //柱状图-新增代理end

    }
    //分类资料
    var _initClassify = function (data) {
        var sexInfo = data.userSexInfo;
        var levelInfo = data.levelInfo;
        var funsInfo = data.funsInfo;
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

        //地区分布-粉丝数begin
        var myChartmap = echarts.init(document.getElementById('chartmap'));
        function randomData() {
            return;
        }
        optionmap = {
            tooltip: {
                trigger: 'item',
                formatter: "{a} <br/>{b}: {c}"
            },
            legend: {
                orient: 'vertical',
                left: 'left',
                selectedMode: false,
                data:['粉丝数']
            },
            color: ['#5d9cec'],
            visualMap: {
                min: 0,
                max: funsInfo[0].value,
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
                    name: '粉丝数',
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
                    data:funsInfo
                }
            ]
        };
        myChartmap.setOption(optionmap);
        //地区分布-粉丝数end
    }
    //访问行为
    var _initVisit = function (data) {
        var visitersInfo = data.visitersInfo;
        var initVisitersInfo = data.initVisitersInfo;
        var itemStyle = {
            normal: {
                label: {
                    show: true,
                    position: 'inside'
                }
            }
        }
       /* _.each(initVisitersInfo,function(k,v){
         k['itemStyle'] = itemStyle;
         })*/

/*        console.log(initVisitersInfo);*/
       /* //页面深度begin
         var myChartpage = echarts.init(document.getElementById('chartpage'));
         var optionpage = {
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
         data : ['1页', '2页', '3页', '4页', '5页', '6页', '7页', '8页', '9页', '10页', '11-20'],
         axisTick: {
         alignWithLabel: true
         }
         }
         ],
         yAxis : [
         {
         type : 'value'
         }
         ],
         series : [
         {
         name:'访问数',
         type:'bar',
         barWidth: '60%',
         data:[10, 52, 200, 334, 390, 330, 220, 52, 200, 334, 390]
         }
         ]
         };
         myChartpage.setOption(optionpage);
         //页面深度end*/

         //地区分布-地图begin
         var myChartareaplace = echarts.init(document.getElementById('chartareaplace'));
         function randomData() {
         return;
         }
         optionareaplace = {
         tooltip: {
         trigger: 'item',
         formatter: "{a} <br/>{b}: {c}"
         },
         legend: {
         orient: 'vertical',
         left: 'left',
         selectedMode: false,
         data:['访问人数']
         },
         color: ['#5d9cec'],
         visualMap: {
         min: 0,
         max: visitersInfo[0].value,
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
         data:visitersInfo
         }
         ]
         };
         myChartareaplace.setOption(optionareaplace);
         //地区分布-地图end

         //地区分布-数据begin
         var myChartareadata = echarts.init(document.getElementById('chartareadata'));
         var optionareadata = {
         tooltip: {
         trigger: 'item',
         formatter: "{a} <br/>{b}: {c} ({d}%)"
         },
         legend: {
         orient: 'vertical',
         x: 'right',
         y: 'top',
         selectedMode: false,
         data:['福建','山东','浙江','江苏','重庆','广东']
         },
         color:['#5d9cec', '#62c87f','#f15755','#fc863f','#947ec8','#ffce55'],
         series: [
         {
         name:'地区分布',
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
         data:initVisitersInfo
         }
         ]
         };
         myChartareadata.setOption(optionareadata);
         //地区分布-数据end

         /*//使用系统-系统begin
         var myChartsys = echarts.init(document.getElementById('chartsys'));
         var optionsys = {
         tooltip: {
         trigger: 'item',
         formatter: "{a} <br/>{b}: {c} ({d}%)"
         },
         legend: {
         orient: 'vertical',
         x: 'left',
         y: 'top',
         selectedMode: false,
         data:['iOS','Android','其他']
         },
         color:['#5d9cec', '#62c87f','#f15755'],
         series: [
         {
         name:'使用系统',
         type:'pie',
         radius: ['35%', '90%'],
         avoidLabelOverlap: false,
         minAngle: 1,
         label: {
         normal: {
         show: false,
         position: 'center'
         },
         emphasis: {
         show: true,
         textStyle: {
         fontSize: '16'
         }
         }
         },
         labelLine: {
         normal: {
         show: false
         }
         },
         data:[
         {value:85, name:'iOS'},
         {value:60, name:'Android'},
         {value:0, name:'其他'}
         ],
         }
         ]
         };
         myChartsys.setOption(optionsys);
         //使用系统-系统end*/

         /*//使用系统-浏览器begin
         var myChartbrowser = echarts.init(document.getElementById('chartbrowser'));
         var optionbrowser = {
         tooltip: {
         trigger: 'item',
         formatter: "{a} <br/>{b}: {c} ({d}%)"
         },
         legend: {
         orient: 'vertical',
         x: 'left',
         y: 'top',
         selectedMode: false,
         data:['微信浏览器','微博浏览器','其他']
         },
         color:['#5d9cec', '#62c87f','#f15755'] ,
         series: [
         {
         name:'使用系统浏览器',
         type:'pie',
         radius: ['40%', '90%'],
         avoidLabelOverlap: false,
         minAngle: 1,
         label: {
         normal: {
         show: false,
         position: 'center'
         },
         emphasis: {
         show: true,
         textStyle: {
         fontSize: '14'
         }
         }
         },
         labelLine: {
         normal: {
         show: false
         }
         },
         data:[
         {value:85, name:'微信浏览器'},
         {value:60, name:'微博浏览器'},
         {value:0, name:'其他'}
         ],
         }
         ]
         };
         myChartbrowser.setOption(optionbrowser);
         //使用系统-浏览器end*/
    }

    return {
        init: _init,
        initBase:_initBase,
        initBase2:_initBase2,
        showFormat:_showFormat,
        initGraph:_initGraph
    };
})();