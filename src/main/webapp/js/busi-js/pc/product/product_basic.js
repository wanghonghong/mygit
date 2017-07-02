/**
 * 商品基本信息
 */
CommonUtils.regNamespace("product", "basic");

product.basic = (function () {
    var productTypeList = [{id: "0", text: "请选择"}, {id: "1", text: "男人"}, {id: "2", text: "女人"},
        {id: "3",text: "化妆品"}, {id: "4", text: "日用品"}, {id: "5", text: "母婴用品"},
        {id: "6", text: "服装鞋帽"}, {id: "7", text: "进口食品"}, {id: "8",text: "食品食材"},
        {id: "9", "isMore": "0", text: "电子产品"}, {id: "10", text: "家居饰品"}, {id: "11", text: "礼品鲜花"},
        {id: "12",text: "餐饮外卖"}, {id: "13", "isMore": "1", text: "休闲娱乐"}, {id: "14", text: "家装建材"},
        {id: "15", text: "酒店客栈"}, {id: "16",text: "婚庆摄影"}, {id: "17", text: "门票卡券"},
        {id: "18", text: "健身"}, {id: "19", text: "箱包"}, {id: "20", text: "汽车"}, {id: "21",text: "地产"},
        {id: "22", text: "宠物"}, {id: "23", text: "代购"}, {id: "24", text: "媒体"}, {id: "25", text: "家政"},
        {id: "26", text: "亲子"}, {id: "27", text: "教育培训"}, {id: "28", text: "文化收藏"}];
    var specJson = [
        {id: 1, text: '规格'},{id: 2, text: '颜色'},{id: 3, text: '尺码'},{id: 4, text: '尺寸'},{id: 5, text: '款式'},
        {id: 6, text: '材质'},{id: 7, text: '品种'},{id: 8, text: '口味'},{id: 9, text: '产地'},{id: 10, text: '包装'},
        {id: 11, text: '适用'},{id: 12, text: '重量'},{id: 13, text: '容量'},{id: 14, text: '体积'},{id: 15, text: '净含量'},
        {id: 16, text: '套餐'},{id: 17, text: '系列'},{id: 18, text: '版本'},{id: 19, text: '内存'},{id: 20, text: '机芯'},
        {id: 21, text: '有效期'},{id: 22, text: '上市时间'},{id: 23, text: '房型'},{id: 24, text: '入住时段'},{id: 25, text: '出行日期'},
        {id: 26, text: '出行人群'}
    ];
    var defaults = {
        containerId: "goodsBasic",
        product: {},
        productTypeList: productTypeList,
        transList: [],
        groupList: [],
        groupRelationList: [],
        productSpecList: [],
        tmpProductSpecList: []
    };

    var opt = {
        grouplistselect: null,
        Project_Path: CONTEXT_PATH,
        tplPath: STATIC_URL + '/tpl/product/basic/',
        ajax_transTemp_list:CONTEXT_PATH +'/trans_temp/get_transTemp_list',
        timingStart: null,
        timeLimitStart: null,
        timeLimitEnd: null,
        transIdSelect: null
    }
    var foo = function (str) {
        str = '000' + str;
        return str.substring(str.length - 3, str.length);
    }
    var _getid = function () {
        var id = new Date().getTime() + foo(_.random(0, 999));
        return id;
    }
    var _init = function (data) {
        $.extend(defaults, data);
        console.log(data);
        defaults.productSpecList=data.productSpecList;
        defaults.productRoleList=data.productRoleList;
        defaults.tmpProductSpecList=[];
        _loadhtml();
    }
    var _addImg = function (imgtype, url) {
        var classname = "base-info-img-square";
        var typename = "点击换图";
        if (imgtype == 1) {
            classname = "base-info-img-rectangle";
            typename = "点击换图";
        }
        var imgdata = {
            classname: classname,
            typename: typename,
            imgurl: url
        }
        var html = jumi.templateHtml('img-box.html',imgdata,opt.tplPath);
        var imgitem = $(html).data({imgurl: url});
        if (imgtype == 1) {
            $("#addImgRectangle").before(imgitem);
        } else {
            $("#addImgSquare").before(imgitem)
        }
    }
    var _changeImgPath = function (imgtype) {
        if (imgtype == 0) {
            var imgarr = []
            $("#img-square-box .imgitem").each(function () {
                var imgurl=jumi.picParse($(this).data().imgurl,0);
                imgarr.push(imgurl)
            })
            if (!$.isEmptyObject(imgarr)) {
                $("#picSquare").val(imgarr[0]);
                $("#detailSquarePic").val(imgarr.join(","));
            } else {
                $("#picSquare").val("");
                $("#detailSquarePic").val("");
            }
        } else {
            var imgarr = []
            $("#img-rectangle-box .imgitem").each(function () {
                var imgurl=jumi.picParse($(this).data().imgurl,0);
                imgarr.push(imgurl)
            })
            if (!$.isEmptyObject(imgarr)) {
                $("#picRectangle").val(imgarr[0]);
                $("#detailRectanglePic").val(imgarr.join(","));
            } else {
                $("#picRectangle").val("");
                $("#detailRectanglePic").val("");
            }
        }
    }
    var convert_url=  function (url) {
        if (!url) return '';
        url =$.trim(url)
            .replace(/v\.youku\.com\/v_show\/id_([\w\-=]+)\.html/i, 'player.youku.com/player.php/sid/$1/v.swf')
            .replace(/(www\.)?youtube\.com\/watch\?v=([\w\-]+)/i, "www.youtube.com/v/$2")
            .replace(/youtu.be\/(\w+)$/i, "www.youtube.com/v/$1")
            .replace(/v\.ku6\.com\/.+\/([\w\.]+)\.html.*$/i, "player.ku6.com/refer/$1/v.swf")
            .replace(/www\.56\.com\/u\d+\/v_([\w\-]+)\.html/i, "player.56.com/v_$1.swf")
            .replace(/www.56.com\/w\d+\/play_album\-aid\-\d+_vid\-([^.]+)\.html/i, "player.56.com/v_$1.swf")
            .replace(/v\.pps\.tv\/play_([\w]+)\.html.*$/i, "player.pps.tv/player/sid/$1/v.swf")
            .replace(/www\.letv\.com\/ptv\/vplay\/([\d]+)\.html.*$/i, "i7.imgs.letv.com/player/swfPlayer.swf?id=$1&autoplay=0")
            .replace(/www\.tudou\.com\/programs\/view\/([\w\-]+)\/?/i, "www.tudou.com/v/$1")
            .replace(/v\.qq\.com\/cover\/[\w]+\/[\w]+\/([\w]+)\.html/i, "static.video.qq.com/TPout.swf?vid=$1")
            .replace(/v\.qq\.com\/.+[\?\&]vid=([^&]+).*$/i, "static.video.qq.com/TPout.swf?vid=$1")
            .replace(/v\.qq\.com\/[\w]+\/cover\/[\w]+\/([\w]+)\.html/i, "imgcache.qq.com/tencentvideo_v1/playerv3/TPout.swf?max_age=86400&v=20161117&vid=$1&auto=0")
            .replace(/v\.qq\.com\/([\w]+\/)?page\/([\w]+\/[\w]+\/[\w]+\/)?([\w]+)\.html/i, "imgcache.qq.com/tencentvideo_v1/playerv3/TPout.swf?max_age=86400&v=20161117&vid=$3")
            .replace(/my\.tv\.sohu\.com\/[\w]+\/[\d]+\/([\d]+)\.shtml.*$/i, "share.vrs.sohu.com/my/v.swf&id=$1");
        return url;
    }
    var  _changeVideoPath=function () {
        var d = new Dialog({
            context_path: opt.Project_Path,
            resType: 2,
            callback: function (url,originalUrl,data) {
                if(!$.isEmptyObject(url)){
                    url=convert_url(originalUrl)
                    // $("#productembed").attr("src",url);
                    $("#productembed").remove();
                    $("<embed>",{
                        "src":url,
                        "id":"productembed",
                        "allowFullScreen":"true",
                        "allowScriptAccess":"always",
                        "type":"application/x-shockwave-flash"
                    }).appendTo("#productVideo-box .video-box");

                   $("#productVideo").val(url);
                    $("#productVideo-box .video-box").removeClass("z-hide");
                    $("#productVideo-box .video-add").addClass("z-hide");
                }
            }
        });
        d.render();
    }
    var _loadhtml = function () {
        var data = {};
        var html = jumi.templateHtml('product_basic.html',defaults,opt.tplPath);
        $(html).appendTo($("#" + defaults.containerId));
        if (defaults.product.typeId) {
            $("#typeId2").attr("checked", true);
        } else {
            $("#typeId1").attr("checked", true);
        }

        $("#tagId").select2({
            theme: "jumi",
            data: defaults.productTypeList
        });
        if (defaults.product.tagId) {
            $("#tagId").val(defaults.product.tagId).trigger("change");
        }


        opt.groupselect = $("#grouplist").select2({
            multiple: true
        }).on('change', function (evt) {
            setgroupRelationList();
        });

        //图片插入
        if (defaults.product.detailSquarePic) {
            var squareArr = defaults.product.detailSquarePic.split(",");
            $.each(squareArr, function (i) {
                _addImg(0, squareArr[i]);
            })
            _changeImgPath(0);
        }
        if (defaults.product.detailRectanglePic) {
            var rectangleArr = defaults.product.detailRectanglePic.split(",");
            $.each(rectangleArr, function (i) {
                _addImg(1, rectangleArr[i]);
            })
            _changeImgPath(1);
        }
        //商品规格
        _initSpecBox();
        if (!$.isEmptyObject(defaults.productSpecList)) {
            _initSpec(defaults.productSpecList);
        }
        // 销售设置
        opt.timingStart = $('#timingStart').datetimepicker({
            timeFormat:'hh:mm:ss'
        });
        opt.timeLimitStart = $('#timeLimitStart').datetimepicker({
            timeFormat:'hh:mm:ss'
        });
        opt.timeLimitEnd = $('#timeLimitEnd').datetimepicker({
            timeFormat:'hh:mm:ss'
        });
        if(defaults.product.pid){
            $("#goodstotalCount").val(defaults.product.totalCount);
        }
        if (defaults.product.isPerSale == 0) {
            //立即销售
            $("#isPerSale1").attr("checked", true);
        } else if (defaults.product.isPerSale == 1) {
            //定时销售
            $("#isPerSale2").attr("checked", true);
            opt.timingStart.datetimepicker('setDate', (defaults.product.perSaleStartTime));
        } else if (defaults.product.isPerSale == 2) {
            //限时销售
            $("#isPerSale3").attr("checked", true);
            opt.timeLimitStart.datetimepicker('setDate', (defaults.product.perSaleStartTime));
            opt.timeLimitEnd.datetimepicker('setDate', (defaults.product.perSaleEndTime));
        }
        //运费模板
        if (defaults.product.isUseTrans == 1) {
            //统一邮费
            $("#isUseTrans1").attr("checked", true);
        } else if (defaults.product.isUseTrans == 2) {
            //运费模板
            $("#isUseTrans2").attr("checked", true);
        }
        opt.transIdSelect = $("#transId").select2({
            theme: "jumi"
        });
        _loadTransList();
        _loadGiftSetList();
        _bindEvent();
    }

    var _loadGiftSetList=function () {
        var productRoleList=[];
        if(defaults.brokerageSet==null){
            defaults.brokerageSet={ type:0  };
        }
        productRoleList.push({ "agentRole":0, "agentName":"关注用户","limitCount":0,"ischange":"0"});
        if(defaults.brokerageSet.type=='1'|| defaults.brokerageSet.type=='3'||defaults.brokerageSet.type=='5'||defaults.brokerageSet.type=='6'){
            productRoleList.push({ "agentRole":8, "agentName":"分享客","limitCount":0 ,"ischange":"0"});
        }
        if(  defaults.brokerageSet.type=='3'||defaults.brokerageSet.type=='5'||defaults.brokerageSet.type=='6'){
            productRoleList.push({ "agentRole":5, "agentName":"分销1档","limitCount":0,"ischange":"0"});
            productRoleList.push({ "agentRole":6, "agentName":"分销2档","limitCount":0,"ischange":"0" });
            productRoleList.push({ "agentRole":7, "agentName":"分销3档","limitCount":0 ,"ischange":"0"});
        }
        if(  defaults.brokerageSet.type=='2'||defaults.brokerageSet.type=='5'||defaults.brokerageSet.type=='6'){
            productRoleList.push({ "agentRole":1, "agentName":"代理1档","limitCount":0, "ischange":"0"});
            productRoleList.push({ "agentRole":2, "agentName":"代理2档","limitCount":0 ,"ischange":"0"});
            productRoleList.push({ "agentRole":3, "agentName":"代理3档","limitCount":0 ,"ischange":"0"});
            productRoleList.push({ "agentRole":4, "agentName":"代理4档","limitCount":0 ,"ischange":"0"});
        }
        if(defaults.productRoleList){
            var tmpList=defaults.productRoleList;
            $(tmpList).each(function (i) {
                 console.log(tmpList[i])
                $(productRoleList).each(function (j) {
                    if(productRoleList[j].agentRole==tmpList[i].agentRole){
                        productRoleList[j].limitCount=tmpList[i].limitCount;
                        productRoleList[j].ischange=1;
                    }
                })
            })
        }
        var  data={
            "productRoleList":productRoleList
        }
        var html = jumi.templateHtml('productRoleList.html',data,opt.tplPath);
        $(html).appendTo($("#giftsetDody"));
    }
    var _initSpec = function (productSpecList) {
        if (productSpecList[0].specIdOne) {
            var list = [];
            var isImg = false;
            $.each(productSpecList, function (o) {
                var item = {
                    specId: productSpecList[o].specIdOne,
                    specName: productSpecList[o].specNameOne,
                    specPic: productSpecList[o].specPic,
                    specValue: productSpecList[o].specValueOne,
                }
                if (productSpecList[o].specPic) {
                    isImg = true;
                }
                list.push(item);
            })
            _loadSpecConfig(productSpecList[0].specIdOne, isImg, list);
            ;
        }
        if (productSpecList[0].specIdTwo) {
            var list = [];
            $.each(productSpecList, function (o) {
                var item = {
                    specId: productSpecList[o].specIdTwo,
                    specName: productSpecList[o].specNameTwo,
                    specPic: productSpecList[o].specPic,
                    specValue: productSpecList[o].specValueTwo,
                }
                list.push(item);
            })
            _loadSpecConfig(productSpecList[0].specIdTwo, false, list);
        }
        if (productSpecList[0].specIdThree) {
            var list = [];
            $.each(productSpecList, function (o) {
                var item = {
                    specId: productSpecList[o].specIdThree,
                    specName: productSpecList[o].specNameThree,
                    specPic: productSpecList[o].specPic,
                    specValue: productSpecList[o].specValueThree,
                }
                list.push(item);
            });
            _loadSpecConfig(productSpecList[0].specIdThree, false, list);
        }
        _loadSpecTable(productSpecList);
        $.extend(defaults.tmpProductSpecList, productSpecList);
    }
    var _loadSpecConfig = function (specId, isImg, list) {
        var json = [];
        var arr = [];
        var j = 0;
        var datalist = {
            id: _getid(),
            specId: specId,
            specJson: specJson,
            isImg: isImg
        }
        // nunjucks.configure(opt.tplPath);//配置模版路径
        // var html = nunjucks.render('spec-config.html', datalist);
        var html = jumi.templateHtml('spec-config.html',datalist,opt.tplPath);
        var tmphtml = $(html).data(datalist);
        $(tmphtml).appendTo($("#specBox"));
        var infodetails = $(tmphtml).find(".stock-info-details-add-img");
        if (!$.isEmptyObject(list)) {
            $.each(list, function (o) {
                if (!_.contains(arr, list[o].specValue)) {
                    // nunjucks.configure(opt.tplPath);//配置模版路径
                    // var itemhtml = nunjucks.render('spec-stock-img.html', list[o]);
                    var itemhtml = jumi.templateHtml('spec-stock-img.html',list[o],opt.tplPath);
                    var item = $(itemhtml).data(list[o]);
                    $(item).appendTo($(infodetails));
                    arr[j] = list[o].specValue;
                    j++;
                }
            })
        }
    }
    var _loadSpecBox = function (datalist) {
        // nunjucks.configure(opt.tplPath);//配置模版路径
        // var html = nunjucks.render('spec-config.html', datalist);
        var html = jumi.templateHtml('spec-config.html',datalist,opt.tplPath);
        var tmphtml = $(html).data(datalist);
        $(tmphtml).appendTo($("#specBox"));
    }
    var _initSpecBox=function () {
        $("#specTable").empty();
        $("#specBox").empty();
        defaults.tmpProductSpecList=[];
    }
    var _loadSpecTable = function (productSpecList) {
        $("#specTable").empty();
        if ($.isEmptyObject(productSpecList)) {
            defaults.productSpecList = productSpecList;
            return;
        }
        var heandarr = [];
        heandarr[0] = productSpecList[0].specNameOne;
        heandarr[1] = productSpecList[0].specNameTwo;
        heandarr[2] = productSpecList[0].specNameThree;
        heandarr[3] = "价格(元)";
        heandarr[4] = "库存";
        heandarr[5] = "商家货号";
        heandarr = _.compact(heandarr);
        var data = {
            heand: heandarr
        }
        // nunjucks.configure(opt.tplPath);//配置模版路径
        // var html = nunjucks.render('spec-table.html', data);
        var html = jumi.templateHtml('spec-table.html',data,opt.tplPath);

        $(html).prependTo("#specTable");
        $.each(productSpecList, function (i) {
            productSpecList[i].id = productSpecList[i].id || _getid();
            productSpecList[i].specPrice = productSpecList[i].specPrice || 0;
            productSpecList[i].fspecPrice = parseInt(productSpecList[i].specPrice) / 100 ||0.0;
            productSpecList[i].totalCount = productSpecList[i].totalCount || 0;
            productSpecList[i].productCode = productSpecList[i].productCode || "";
            // var html = nunjucks.render('spec-table-item.html', productSpecList[i]);
            var html = jumi.templateHtml('spec-table-item.html', productSpecList[i],opt.tplPath);
            var item = $(html).data(productSpecList[i]);
            $(item).prependTo("#spectbody");
        })
        if (heandarr.length == 5) {
            $("#specTable").rowspan([0, 1]);
        } else if (heandarr.length == 6) {
            $("#specTable").rowspan([0, 1, 2]);
        }
        $.each(productSpecList, function (x) {
            var item1 = productSpecList[x];
            $.each(defaults.tmpProductSpecList, function (y) {
                var item2 = defaults.tmpProductSpecList[y];
                if (item1.id == item2.id) {
                    defaults.tmpProductSpecList[y] = $.extend(item2, item1);
                }
            })
        })
        defaults.productSpecList = productSpecList;
        _updataGoodstotalCount();
    }

    var _updataGoodstotalCount=function () {
        var goodstotalCount=0;
        $("#specTable  .spectbody tr").each(function () {
            var trdata = $(this).data();
            goodstotalCount=goodstotalCount+trdata.totalCount*1;
        })
        $("#goodstotalCount").val(goodstotalCount);
    }

    var _loadTransList=function(){
        opt.transIdSelect.empty();
        $.map(defaults.transList, function (obj) {
            obj.id = obj.id || obj.templatesId; // replace pk with your identifier
            obj.text = obj.text || obj.templatesName; // replace pk with your identifier
            return obj;
        });
        opt.transIdSelect = $("#transId").select2({
            theme: "jumi",
            data: defaults.transList
        });
        if (defaults.product.transId) {
            opt.transIdSelect.val(defaults.product.transId).trigger("change");
        }
    }

    var _loadGroupSelect = function (data) {
        defaults.groupList = data.groupList;
        defaults.groupRelationList = data.groupRelationList;
        opt.groupselect.empty();
        $.map(defaults.groupList, function (obj) {
            obj.id = obj.id || obj.groupId; // replace pk with your identifier
            obj.text = obj.text || obj.groupName; // replace pk with your identifier
            return obj;
        });

        function formatState (state) {
            if (!state.id) { return state.text; }
            var $state = $(
                '<span><img src='+state.groupImagePath+' align="absmiddle" style="width:70px;height: 70px;" /> ' + state.text + '</span>'
            );
            return $state;
        };
        function selectionformatState (state) {
            if (!state.id) { return state.text; }
            var $state = $(
                '<span><img src='+state.groupImagePath+' align="absmiddle" style="width:16px;height:16px;" /> ' + state.text + '</span>'
            );
            return $state;
        };
        opt.groupselect.select2({
            templateSelection:selectionformatState,
            templateResult: formatState,
            theme: "jumi",
            data: defaults.groupList
        })
        //填写选中的值
        if (!$.isEmptyObject(defaults.groupRelationList)) {
            var arr = [];
            $.each(defaults.groupRelationList, function (i) {
                arr.push(defaults.groupRelationList[i].groupId);
            })
            opt.groupselect.val(arr).trigger("change");
        }
    }
    var setgroupRelationList = function () {
        var tmparr = opt.groupselect.val();
        var groupRelationList = [];
        if (!$.isEmptyObject(tmparr)) {
            for (var i = 0; i < tmparr.length; i++) {
                groupRelationList.push({"groupId": tmparr[i]}
                )
            }
        }
        defaults.groupRelationList = groupRelationList;
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
    var _bindEvent = function () {
        _loadGroupSelect(defaults);
        $("#goodsname").change(function () {
            var    goodname=$(this).val().trim();
            $("#goodsname").val(goodname);
         $("#tmpfontsize").html(goodname.length);
        }).keyup(function () {
            var    goodname=$(this).val().trim();
            $("#tmpfontsize").html(goodname.length);
        })

        $("#addImgSquare").click(function () {
           var len= $("#img-square-box .imgitem").length;
            if(len>=8){
                alertinfo("商品方图不能超过8张!");
                return;
            }
            var d = new Dialog({
                context_path: opt.Project_Path,
                resType: 1,
                callback: function (url) {
                    if (!$.isEmptyObject(url)) {
                        _addImg(0, url);
                        _changeImgPath(0);
                    }
                }
            });
            d.render();
        });
        $("#addImgRectangle").click(function () {
            var len= $("#img-rectangle-box .imgitem").length;
            if(len>=8){
                alertinfo("商品长图不能超过8张!");
                return;
            }
            var d = new Dialog({
                context_path: opt.Project_Path,
                resType: 1,
                callback: function (url) {
                    if (!$.isEmptyObject(url)) {
                        _addImg(1, url);
                        _changeImgPath(1);
                    }
                }
            });
            d.render();
        });
        $("#goodstotalCount").change(function(){
            defaults.product.totalCount=$(this).val();
1        });
        $("#img-square-box ").sortable({
            opacity: "0.6",
            connectWith: ".imgitem",
            placeholder: "field-placeHolder-square",
            stop: function() {
                $("#addImgSquare").appendTo("#img-square-box");
                _changeImgPath(0);
            }
        }).disableSelection();
        $("#img-rectangle-box").sortable({
            opacity: "0.6",
            connectWith: ".imgitem",
            placeholder: "field-placeHolder-rectangle",
            stop: function() {
                $("#addImgRectangle").appendTo("#img-rectangle-box");
                _changeImgPath(1);

            }
        }).disableSelection();


        $("#img-square-box ").delegate(".imgitem .icon-delete2", "click", function () {
            $(this).parent().remove();
            _changeImgPath(0);
        })
        $("#img-rectangle-box ").delegate(".imgitem .icon-delete2", "click", function () {
            $(this).parent().remove();
            _changeImgPath(1);
        })

        $("#productVideo-box").delegate(".video-add", "click", function () {
            _changeVideoPath();
        })
        $("#productVideo-box").delegate(".video-box span", "click", function () {
            _changeVideoPath();
        })
        $("#productVideo-box").delegate(".video-box .icon-delete2", "click", function () {
            $("#productembed").attr("src","");
            $("#productVideo").val("");
            $("#productVideo-box .video-box").addClass("z-hide");
            $("#productVideo-box .video-add").removeClass("z-hide");
        })

        $("#img-square-box ").delegate(".imgitem span", "click", function () {
            var _this = $(this);
            var d = new Dialog({
                context_path: opt.Project_Path,
                resType: 1,
                callback: function (url) {
                    if (!$.isEmptyObject(url)) {
                        var parent=_this.parent();
                        parent.data().imgurl = url;
                        parent.find("img").attr("src", url);
                        _changeImgPath(0);
                    }
                }
            });
            d.render();
        })
        $("#img-rectangle-box ").delegate(".imgitem span", "click", function () {
            var _this = $(this);
            var d = new Dialog({
                context_path: opt.Project_Path,
                resType: 1,
                callback: function (url) {
                    if (!$.isEmptyObject(url)) {
                        var parent=_this.parent();
                        parent.data().imgurl = url;
                        parent.find("img").attr("src", url);
                        _changeImgPath(1);
                    }
                }
            });
            d.render();
        })
        //规格区域事件绑定
        $("#specBox").delegate(".m-spec-config .u-cb :checkbox", "click", function () {
            var specconfig = $(this).closest(".m-spec-config");
            specconfig.toggleClass("showimg");
        })
        $("#specBox").delegate(".m-spec-config .stock-img-show1 .test", "click", function () {
            $("#specBox .stock-img-show1-absolute").addClass("z-hide");
            $(this).next(".stock-img-show1-absolute").removeClass("z-hide");
        })
        $("#specBox").delegate(".m-spec-config  .stock-img-show1 .stock-img-show1-absolute .u-btn-smltorg", "click", function () {
            var show1 = $(this).closest(".stock-img-show1");
            var specconfig = $(this).closest(".m-spec-config");
            var specValue = $(this).prev(".ipt-txt").val();
            var sign = false;
            $(show1).siblings(".stock-img-show1").each(function () {
                var tepdata = $(this).data();
                if (tepdata.specValue == specValue) {
                    sign = true;
                }
            })
            if (!specValue) {
                alertinfo("请输入规格名称");
                return;
            }
            if (sign) {
                alertinfo("同一规格类型内不能有同名属性");
                return;
            }
            var showdata = show1.data();
            showdata.specValue = $(this).prev(".ipt-txt").val();
            show1.find(".test .tagtext").html(showdata.specValue);
            $(this).parent().addClass("z-hide");
            updateroductSpecList();
        })
        $("#specBox").delegate(".m-spec-config .stock-img-show1 .test .icon-delete2", "click", function () {
            var show1 = $(this).closest(".stock-img-show1");
            show1.remove();
            updateroductSpecList();
        })
        $("#specBox").delegate(".m-spec-config .stock-img-show1 .add-img-hide", "click", function () {
            var _this = $(this);
            var d = new Dialog({
                context_path: opt.Project_Path,
                resType: 1,
                callback: function (url) {
                    if (!$.isEmptyObject(url)) {
                        url=  jumi.picParse(url,0);
                        _this.find("img").attr("src", url).removeClass("z-hide");
                        _this.find(".icon-add").addClass("z-hide");
                        var show1 = _this.closest(".stock-img-show1");
                        show1.data().specPic = url;
                        updateroductSpecList();
                    }
                }
            });
            d.render();
        })
        $("#specBox").delegate(".m-spec-config .stock-img-show2 .showbtn", "click", function () {
            $("#specBox .stock-img-show1-absolute").addClass("z-hide");
            $(this).next(".stock-img-show1-absolute").removeClass("z-hide");
        })

        $("#specBox").delegate(".m-spec-config .stock-img-show2 .cancelbtn", "click", function () {
            $("#specBox .stock-img-show1-absolute").addClass("z-hide");
        })
        $("#specBox").delegate(".m-spec-config .stock-img-show2 .savebtn", "click", function () {
            var txt = $(this).siblings(".ipt-txt");
            var specValue = $.trim(txt.val());
            var specconfig = $(this).closest(".m-spec-config");
            var sign = false;
            $(specconfig).find(".stock-info-details-add-img .stock-img-show1 ").each(function () {
                var showdata = $(this).data();
                if (showdata.specValue == specValue) {
                    sign = true;
                }
            })
            if (!specValue) {
                alertinfo("请输入规格名称");
                return;
            }
            if (sign) {
                alertinfo("同一规格类型内不能有同名属性");
                return;
            }
            txt.val("");
            var infodetails = $(this).closest(".stock-info-details-add-img");
            $("#specBox .stock-img-show1-absolute").addClass("z-hide");
            var specIdselect = specconfig.find("[name='specId']");
            var specId = specIdselect.val();
            var specName = specIdselect.find("option:selected").text();
            var data = {
                specId: specId,
                specName: specName,
                specPic: null,
                specValue: specValue
            }
            // nunjucks.configure(opt.tplPath);//配置模版路径
            // var itemhtml = nunjucks.render('spec-stock-img.html', data);
            var itemhtml = jumi.templateHtml('spec-stock-img.html',data,opt.tplPath);
            var item = $(itemhtml).data(data);
            $(item).appendTo($(infodetails));
            updateroductSpecList();
        })
        $("#addSpecbtn").click(function () {
            if ($("#specBox .m-spec-config").length < 3) {
                var specId = 1;
                $("#specBox .m-spec-config select[name='specId']").each(function () {
                    if (specId == $(this).val()) {
                        specId++
                    }
                })
                var datalist = {
                    id: _getid(),
                    specId: specId,
                    specJson: specJson,
                    isImg: false
                }
                _loadSpecBox(datalist);
            } else {
                alertinfo('规格类型不能超过3类');
            }
        })
        $("#specBox").delegate(".m-spec-config .stock-info-details-add-info [name='specId']", "change", function () {
            var _this = $(this);
            var specId = $(this).val();
            var oldValue = $(this).attr("oldValue");
            var sign = true;
            $("#specBox .m-spec-config select[name='specId'] ").each(function () {
                var tmpold = $(this).attr("oldValue");
                if (tmpold != oldValue) {
                    if (specId == $(this).val()) {
                        $(_this).val(oldValue)
                        alertinfo('规格类型不能重复');
                        sign = false;
                    }
                }
            })
            if (sign) {
                $(_this).attr("oldValue", specId);
                var specconfig = $(this).closest(".m-spec-config");
                $(specconfig).find(".stock-info-details-add-img .stock-img-show1").remove();
                updateroductSpecList();
            }
        })

        $("#specTable").delegate(".spectbody .u-txt input", "change", function () {
            var parent = $(this).parent().parent().parent();
            var parentdata = parent.data();
            if($(this).prop("name")=='specPrice'){
                parentdata.specPrice = Math.ceil(parseFloat($(this).val(),2) *100)  ;
            }else if($(this).prop("name")=='totalCount') {
                parentdata.totalCount =Math.ceil( $(this).val());
                $(this).val( parentdata.totalCount);
            }
            var productSpecList = [];
            $("#specTable  .spectbody tr").each(function () {
                productSpecList.push($(this).data());
            })
            if($(this).prop("name")=='totalCount'){
                _updataGoodstotalCount();
            }
            console.log(parentdata);
            updataTableSpecData(productSpecList);
        })
        $("#specBox").delegate(".m-spec-config .stock-info-details-add-info .icon-delete1 ", "click", function () {
            $(this).closest(".m-spec-config").remove();
            updateroductSpecList();
        })
        $("#specTable").delegate("tfoot .u-btn-smltgry", "click", function () {
            var specPrice = $("#specTable tfoot [name=specPrice]").val();
            specPrice = $.trim(specPrice);
            if (!specPrice) {
                alertinfo("请输入批量修改的价格");
                return;
            }
            var totalCount = $("#specTable tfoot [name=totalCount]").val();
            totalCount = $.trim(totalCount);
            if (!totalCount) {
                alertinfo("请输入批量修改的库存");
                return;
            }
            var productSpecList = [];
            $("#specTable  .spectbody tr").each(function () {
                var trdata = $(this).data();
                trdata.specPrice =Math.ceil(parseFloat(specPrice,2) *100)  ;
                trdata.totalCount =Math.ceil( totalCount*1);
                $(this).find("[name='specPrice']").val(specPrice);
                $(this).find("[name='totalCount']").val(trdata.totalCount);
                productSpecList.push(trdata);
            })
            _updataGoodstotalCount();
            updataTableSpecData(productSpecList);
        })
        $("#refreshBtn").click(function () {
            var url=opt.ajax_transTemp_list;
            $.ajaxJsonGet(url,null,{
                "done":function(res){
                    console.log(res);
                    if(res.code==0){
                        defaults.transList=res.data;
                        _loadTransList();
                    }
                }
            })
        })
        $("#gift").click(function () {
            $("#giftsetBox").slideToggle();
        })

        product.transtemplate.shop_trans_open();
    }
    var updataTableSpecData = function (productSpecList) {
        $.each(productSpecList, function (x) {
            var item1 = productSpecList[x];
            var item2 = _.where(defaults.tmpProductSpecList, {"id": productSpecList[x].id});
            if ($.isEmptyObject(item2)) {
                defaults.tmpProductSpecList.push(item1);
            } else {
                $.each(defaults.tmpProductSpecList, function (y) {
                    var item3 = defaults.tmpProductSpecList[y];
                    if (item1.id == item3.id) {
                        defaults.tmpProductSpecList[y] = $.extend(item3, item1);
                    }
                })
            }
        })
        defaults.productSpecList=productSpecList;
    }
    //修改规格列表数据
    var updateroductSpecList = function () {
        var productSpecList = [];
        var specdata = [];
        $("#specBox").find(".stock-info-details-add-img").each(function () {
            var items = [];
            $(this).find(".stock-img-show1").each(function () {
                items.push($(this).data())
            })
            if (items.length > 0) {
                specdata.push(items);
            }
        })
        productSpecList = _gettmpproductSpecList(specdata);
        $.each(productSpecList, function (i) {
            var item1 = productSpecList[i];
            $.each(defaults.tmpProductSpecList, function (j) {
                var item2 = defaults.tmpProductSpecList[j];
                if (item1.specIdOne == item2.specIdOne &&
                    item1.specIdTwo == item2.specIdTwo &&
                    item1.specIdThree == item2.specIdThree &&
                    item1.specValueOne == item2.specValueOne &&
                    item1.specValueTwo == item2.specValueTwo &&
                    item1.specValueThree == item2.specValueThree
                ) {
                    productSpecList[i] = $.extend(item2, item1);
                }
            })
        })

        _loadSpecTable(productSpecList);
    }
    var _gettmpproductSpecList = function (specdata) {
        var productSpecList = [];
        var productSpecList1 = [];
        if (!$.isEmptyObject(specdata[0])) {
            $.each(specdata[0], function (x) {
                var specItem = {
                    "specIdOne": specdata[0][x].specId + "",
                    "specNameOne": specdata[0][x].specName,
                    "specPic": specdata[0][x].specPic,
                    "specValueOne": specdata[0][x].specValue
                }
                productSpecList1.push(specItem);
            })
        } else {
            return productSpecList1;
        }
        var productSpecList2 = [];
        if (!$.isEmptyObject(specdata[1])) {
            $.each(productSpecList1, function (o) {
                $.each(specdata[1], function (x) {
                    var specItem = {
                        "specIdTwo": specdata[1][x].specId + "",
                        "specNameTwo": specdata[1][x].specName,
                        "specValueTwo": specdata[1][x].specValue
                    }
                    var tmp = $.extend(specItem, productSpecList1[o]);
                    productSpecList2.push(tmp);
                })
            });
        } else {
            return productSpecList1;
        }
        var productSpecList3 = [];
        if (!$.isEmptyObject(specdata[2])) {
            $.each(productSpecList2, function (o) {
                $.each(specdata[2], function (x) {
                    var specItem = {
                        "specIdThree": specdata[2][x].specId + "",
                        "specNameThree": specdata[2][x].specName,
                        "specValueThree": specdata[2][x].specValue
                    }
                    var tmp = $.extend(specItem, productSpecList2[o]);
                    productSpecList3.push(tmp);
                })
            });
        } else {
            return productSpecList2;
        }
        return productSpecList3;
    }
    var _mergeRow=function () {
        _loadSpecTable(defaults.productSpecList);
        if(defaults.product.pid){
            $("#goodstotalCount").val(defaults.product.totalCount);
        }
    }

    var _getGroupRelationList = function () {
        return defaults.groupRelationList;
    }
    var _getProductRoleList=function () {
        var productRoleList=[];
        if($("#gift").prop("checked")){
            $("#giftsetDody ul .u-cb input:checkbox[name=agentRole]:checked").each(function(i){
                var agentRole=$(this).val();
               var pobj= $(this).closest(".table-container");
               var limitCount= $(".limitCountNum input[type=number]",pobj).val();
                productRoleList.push({"agentRole":agentRole,"limitCount":limitCount})
            })
        }
        return productRoleList;
    }

    var _getProductSpecList = function () {
        var productSpecList=[];
        var delid=[];
        var j=0;
        var redata={};
        var sign=$("#specBox .showimg").length>0?true:false;
        if($.isEmptyObject(defaults.productSpecList)){
            redata.productSpecList=productSpecList;
            redata.delId="";
            $.each(defaults.tmpProductSpecList,function(i){
                if(defaults.tmpProductSpecList[i].productSpecId){
                        delid[j++]=defaults.tmpProductSpecList[i].productSpecId;
                }
            })
            redata.delId=delid.join(",");
            return redata;
        }
        $.each(defaults.productSpecList,function(i){
            var item =defaults.productSpecList[i];
            if(!sign){
                item.specPic=null;
            }
            productSpecList.push(item);
        })
        redata.productSpecList=productSpecList;
        $.each(defaults.tmpProductSpecList,function(i){
            if(defaults.tmpProductSpecList[i].productSpecId){
               var tmp= _.where(productSpecList, {productSpecId:defaults.tmpProductSpecList[i].productSpecId})
                if(tmp.length==0){
                    delid[j++]=defaults.tmpProductSpecList[i].productSpecId;
                }
            }
        })
        var delIds=delid.join(",");
        redata.delId=delIds;
        return redata;
    }


    var _getProduct=function () {
        var product={};
        $(" #basicBox input:text").each(function(){
            product[$(this).prop("name")]=$(this).val();
        })
        product.picSquare=$("#picSquare").val();
        product.detailSquarePic=$("#detailSquarePic").val();
        product.picRectangle=$("#picRectangle").val();
        product.detailRectanglePic=$("#detailRectanglePic").val();
        product.productVideo=$("#productVideo").val();
        product.tagId=$("#tagId").val();
        product.price=$("#goodsPrice").val();
        product.price= Math.ceil(parseFloat(product.price,2) *100) ;
        product.sort= Math.ceil(product.sort) ;
        $("#specform  input").each(function(){
            product[$(this).prop("name")]=$(this).val();
        })
        product.typeId=$("#basicBox input:radio[name=typeId]:checked").val();

        // $(" #saleBox input:text").each(function(){
        //     product[$(this).prop("name")]=$(this).val();
        // })
        $("#saleBox input:checkbox").each(function(){
            product[$(this).prop("name")]=$(this).prop("checked")?1:0;
        })
        if(product.isLimitCount){
            product.limitCount= $('#limitCount').val();
            product.limitCount= Math.ceil(product.limitCount) ;
        }else{
            product.limitCount=0;
        }

        product.isPerSale=$("#saleBox input:radio[name=isPerSale]:checked").val();
        if(product.isPerSale==1){
            product.perSaleStartTime= $('#timingStart').val();
        }else if(product.isPerSale==2){
            product.perSaleStartTime= $('#timeLimitStart').val();
            product.perSaleEndTime= $('#timeLimitEnd').val();
        }
        product.isUseTrans=$("#freightBox input:radio[name=isUseTrans]:checked").val();
        if(product.isUseTrans==1){
            product.transFare=  Math.ceil(parseFloat($('#transFare').val(),2) *100) ;

        }else{
            product.transId=$('#transId').val();
        }
        return product;
    }
    var _validate=function () {
        //处理模块验证信息
        var sign=false;
        var goodsname=$("#goodsname").val();
        if($.isEmptyObject(goodsname)){
            alertinfo("请输入商品名称");
            return true;
        }
        // var reg  =/[\.@#\$%\^&\*\(\)\\?\\\/\|\-~`\+\=\,\r\n\:\'\"]+/;
        var reg  =/[\.@#\$%\^&\*\(\)\\?\\\/\|\~`\=\,\r\n\:\'\"]+/;
        if(reg.test(goodsname)){
            alertinfo("商品名称不能输入单引号双引号等特殊符号");
            return true;
        }

        if($("#tagId").val()==0){
            alertinfo("请选择一个商品标签");
            return true;
        }
        if(defaults.groupRelationList.length==0){
            alertinfo("请选择一个商品分类");
            return true;
        }
        if($("#picSquare").val().length==0){
            alertinfo("请选择个商品方图")
            return true;
        }
        if($("#picRectangle").val().length==0){
            alertinfo("请选择个商品长图")
            return true;
        }
       var price=  $("#goodsPrice").val();
        if(price==""||price<0){
            alertinfo("商品价格不能为空或小于0");
            return true;
        }
       var goodstotalCount=  $("#goodstotalCount").val();
        $("#goodstotalCount").val(Math.ceil(goodstotalCount) );
        if(_.isFinite(goodstotalCount) ){
            alertinfo("库存请输入数字！");
            return true;
        }

        return sign;
    }
    return {
        init: _init,
        initGroupList: _loadGroupSelect,
        getGroupRelationList: _getGroupRelationList,
        getProductSpecList:_getProductSpecList,
        getProductRoleList:_getProductRoleList,
        getProduct:_getProduct,
        mergeRow:_mergeRow,
        validate:_validate
    }
})();