
CommonUtils.regNamespace("module", "edit");

module.edit=(function(){
    var GOODS_MANAGEMENT=0;  //商品管理
    var MALL_BUILDING=1;  //商城搭建
    var OFFICIAL_IMAGE=2;  //官方图文
    var VOTE_THEME=3;  //投票主题
    var opt={
        versionId:"standard",
        Project_Path:CONTEXT_PATH,
        editPath:STATIC_URL+'/tpl/product/detail/edit/',
        goodslistAjax:CONTEXT_PATH+"/good/productList/0",
        activityGoodslistAjax:CONTEXT_PATH+"/product/trad/trad_products",
        syngoodsAjax:CONTEXT_PATH+"/good/products/",
        imageTextTpyeAjax:CONTEXT_PATH+'/image_text_type/type_id/',
        giftListAjax:CONTEXT_PATH +'/card/byDateList', //礼券列表--有效期内
        likes:358     //临时点赞数量
    }
    var  _getid= function(){
        var id =  new Date().getTime();
        return id;
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
    //绑定title编辑窗口事件
    var bindtitleEvent=function(element,configPanel){
        var elData=$(element).data();
        $(configPanel).find(".ipt-txt").change(function () {
            elData.content = $(this).val().replace(/[\r\n]/g,"");
            module.view.updataPageModular($(element));
        });
    }
    //绑定title编辑窗口事件
    var bindtitleInfoEvent=function(element,configPanel){
    }
    //绑定微信头部编辑窗口事件
    var bindweixintopEvent=function (element,configPanel) {
        var elData=$(element).data();
        $(configPanel).find(".ipt-txt").focusout(function () {
            elData[$(this).attr("name")] = $(this).val();
            module.view.updataPageModular($(element));
        });
    }
    //绑定微信底部编辑窗口事件
    var bindweixinbottomEvent =function (element,configPanel) {
        var elData=$(element).data();
        $(configPanel).find("[name='likes']").focusout(function () {
            elData.likes = $(this).val();
            opt.likes= elData.likes;
            module.view.updataPageModular($(element));
        });
        $(configPanel).find(".linkset").click(function () {
            var tmpinput=$(this).next();
            var shopId = $("#shopId").val();
            menuDialog.Menu.initPage({
                shop_id:shopId,
            },function(menu){
                menu.getUrl(function(link){
                    var url;
                    switch(link.link_type){
                        case '1':
                            url = DOMAIN+link.link_url+"?shopId="+link.shop_id;
                            break;
                        case '2':
                            url = DOMAIN+link.link_url+'/'+link.link_id+"?shopId="+link.shop_id;
                            break;
                        case '4':
                            url=link.link_url;
                            break;
                    }
                    $(tmpinput).val(url);
                    elData.linkTxt = $(tmpinput).val();
                    module.view.updataPageModular($(element));
                });
            });
        });

    }
    var bindrichtextEvent =function (element,configPanel) {
            var elData=$(element).data();
            var richtextId= "richtext" +_getid();
             $(configPanel).find(".richtextContainer").attr("id",richtextId);
            var ue =null;
            ue = UE.getEditor(richtextId, {
                enableAutoSave: false,
                initialFrameHeight: 500,
                imageScaleEnabled: false
            });
            ue.ready(function () {
                //设置编辑器的内容
                if (elData.content != null && elData.content != undefined) {
                    ue.setContent(elData.content);
                }
            });
            ue.addListener('contentchange', function () {
                elData.content = ue.getContent();
                module.view.updataPageModular($(element));
            })
        };
    var bindrichtextlimitEvent =function (element,configPanel) {
        var elData=$(element).data();
        var richtextId= "richtextlimit" +_getid();
        $(configPanel).find(".richtextContainer").attr("id",richtextId);
        var ue =null;
        ue = UE.getEditor(richtextId, {
            enableAutoSave: false,
            initialFrameHeight: 500,
            imageScaleEnabled: false
        });
        ue.ready(function () {
            //设置编辑器的内容
            if (elData.content != null && elData.content != undefined) {
                ue.setContent(elData.content);
            }
        });
        ue.addListener('contentchange', function () {
            elData.content = ue.getContent();
            module.view.updataPageModular($(element));
        })
    };

    var bindQRcodeEvent =function (element,configPanel) {
        var elData=$(element).data();
        $("[name='ewmCode']",configPanel).click(function(){
            elData.ewmCode=$(this).val();
            module.view.updataPageModular(element);
        })
    };
    var bindtitleBarEvent =function (element,configPanel) {
        var elData=$(element).data();
        var titleBarId= "titleBar" +_getid();
        $(configPanel).find(".titleBarContainer").attr("id",titleBarId);
        var ue =null;
        ue = UE.getEditor(titleBarId, {
            enableAutoSave: false,
            initialFrameHeight: 50,
            imageScaleEnabled: false ,
            toolbars: [
                ['bold', 'italic', 'underline' , 'forecolor', 'backcolor','fontsize']
            ]
        });
        ue.ready(function () {
            //设置编辑器的内容
            if (elData.content != null && elData.content != undefined) {
                ue.setContent(elData.content);
            }
        });
        ue.addListener('selectionchange', function () {
            $(".edui-for-jmlink,.edui-for-image,.edui-for-hotspot",configPanel).remove();
            elData.content = ue.getContent();
            module.view.updataPageModular($(element));
        })
        ue.addListener( 'ready', function( editor ) {
            $(".edui-for-jmlink,.edui-for-image,.edui-for-hotspot",configPanel).remove();
        } );
        // ue.execCommand( 'inserthtml', "");
    };
    var bindbottomPicEvent =function (element,configPanel) {

    };
    var bindimageTextTitleEvent=function (element,configPanel) {
        var elData=$(element).data();
        var  typeurl=opt.imageTextTpyeAjax+elData.typeId;//图文分类
        var _initConfigPanel=function(){
            if(elData.isAward==1 && elData.awardType.indexOf("0")>-1){
                $("#"+elData.randomNum+"-awardType1").prop("checked",true);
            }
            if(elData.isAward==1 && elData.awardType.indexOf("1")>-1){
                $("#"+elData.randomNum+"-awardType2").prop("checked",true);
            }
            _getTypeList().then(function () {
                _bindEvent();
            })
        }
        var _getTypeList=function () {
            var dfdPlay = $.Deferred();
            $.ajaxJsonGet(typeurl,null,{
                "done":function (res) {
                    var tpl = '';
                    var data = res.data;
                    _.map(data.imageTextTypeList,function(k,v){
                        tpl+='<option value="'+k.id+'">'+k.typeName+'</option>'
                    });
                    $("#"+elData.randomNum+"-typeName").html(tpl);
                    var selecttmp= $("#"+elData.randomNum+"-typeName",configPanel).select2({
                        theme: "jumi"
                    }).on('change', function (evt) {
                        elData.imageTextType=selecttmp.val();
                    });
                    if(elData.imageTextType){
                        selecttmp.val(elData.imageTextType).trigger("change");
                    }else{
                        selecttmp.val(data.imageTextTypeList[0].id).trigger("change");
                    }
                    dfdPlay.resolve(); // 动画完成
                }
            });
            return dfdPlay;
        }
        var _bindEvent=function () {
            $("#"+elData.randomNum+"-imageTextTile").change(function () {
                elData.imageTextTile=$(this).val();
                module.view.updataPageModular(element);
            })
            $("#"+elData.randomNum+"-shareText").change(function () {
                elData.shareText=$(this).val();
                module.view.updataPageModular(element);
            })
            var imageurlobj=$("#"+elData.randomNum+"-imageUrl");
            imageurlobj.click(function () {
                var d = new Dialog({
                    context_path: opt.Project_Path,
                    resType: 1,
                    callback: function (url) {
                        if(!$.isEmptyObject(url)){
                            url=  jumi.picParse(url,720);
                            elData.imageUrl=url;
                            imageurlobj.attr("src",url);
                            module.view.updataPageModular(element);
                        }
                    }
                });
                d.render();
            })
            $("[name='awardSecond']",configPanel).change(function(){
                elData[$(this).prop("name")] = $(this).val();
                module.view.updataPageModular(element);
            })
            $("[name='formattype']",configPanel).click(function(){
                elData.formatCode = $(this).val();
                module.view.updataPageModular(element);
            })
            $("[name='integralType']",configPanel).click(function(){
                elData[$(this).prop("name")] = $(this).val();
                module.view.updataPageModular(element);
            })
            $("[name='awardType']",configPanel).click(function(){
                // elData[$(this).prop("name")] = $(this).val();
                // $("[name='awardType']",configPanel).each(function(){
                //     if( $(this).is(":checked")){
                //         $(this).click();
                //     }
                // })
              var  awardTypeArr=[];
             $("[name='awardType']:checked",configPanel).each(function(){
                 awardTypeArr.push($(this).val());
              });

               if(awardTypeArr.length>0){
                   elData.isAward=1;
                   elData.awardType=awardTypeArr.join(",");
               }else{
                   elData.awardType='';
                   elData.isAward=0;
               }
                console.log(elData)
                module.view.updataPageModular(element);
            })

            $("#"+elData.randomNum+"-giftbtn").click(function(){
                openGiftWin(function (tmpdata) {
                    elData.cardId=tmpdata.id;
                    elData.cardName=tmpdata.cardName;
                    $("[name='cardName']",configPanel).val(elData.cardName);
                    module.view.updataPageModular(element);
                });
            })
        }
        _initConfigPanel();
    }
    var openGiftWin=function (callback) {
        var giftpageparam=  {
            url: CONTEXT_PATH+"/card/byDateList",//对象列表
            pageSize: 10,
            curPage: 0,
            pageToolbarObj: "pageagcardToolbar",
            tableBodyObj: "agcardselect_list",
            template:"/tpl/product/detail/edit/actgift_item.html"
        };
        var html = jumi.templateHtml('actgift_list.html',null,opt.editPath);
       var  dg_cs_list = dialog({
            title: "礼券列表",
            content: html,
            width: 1135,
            id: 'dialog_cslist',
            okValue: '确定',
            ok: function() {
                var radio=$("#agcardselect_list :radio:checked");
                var parent=$(radio).closest(".table-container");
                callback($(parent).data());
            }
        }) .showModal();
        var _giftsearch=function ( pageparam) {
            var data={};
            data.pageSize=pageparam.pageSize;
            data.curPage=pageparam.curPage;
            // var data = JSON.stringify(data);
            jumi.pagination("#"+pageparam.pageToolbarObj,pageparam.url,data,function (res,curPage) {
                pageparam.curPage=curPage;
                $("#"+pageparam.tableBodyObj).empty();
                // var itemhtml = jumi.templateHtml(pageparam.template,res.data);
                // $(itemhtml).appendTo("#"+pageparam.tableBodyObj);
                if(res.data.items && res.data.items.length>0){
                    for(var i=0;i<res.data.items.length;i++){

                        var itemhtml = jumi.templateHtml(pageparam.template,res.data.items[i]);
                        var item = $(itemhtml).data(res.data.items[i]);
                        $(item).appendTo("#"+pageparam.tableBodyObj);
                    }
                }else{
                    $("<div>",{
                        'class':'m-jm-err'
                    }).html(" <img src='"+jumi.config.cssPath+"/css/pc/img/jm-nodb.png' >").appendTo("#"+pageparam.tableBodyObj);
                }
            })
        }
        _giftsearch(giftpageparam);

    }
       var bindrewardModuleEvent=function (element,configPanel) {
            var elData=$(element).data();
            $("[name='rewardCode']",configPanel).click(function(){
                elData.rewardCode=$(this).val();
                module.view.updataPageModular(element);
            })
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
    var bindvideoEvent =function (element,configPanel) {
        var elData=$(element).data();
        var txbtn=$(".txbtn",configPanel);
        var txResName=$(".txResName",configPanel);
        var chooseimg=$(".chooseimg",configPanel);
        var delimg=$(".delimg",configPanel);
        var videoImg=$(".videoImg",configPanel);
        txbtn.click(function () {
            var d = new Dialog({
                context_path: opt.Project_Path,
                resType: 2,
                callback: function (url,originalUrl,data) {
                    if(!$.isEmptyObject(url)){
                        url=convert_url(originalUrl)
                        txResName.val(data.resName);
                        elData.vid=data.vid;
                        elData.txResUrl=url;
                        elData.txResName=data.resName;
                        module.view.updataPageModular(element);
                    }
                }
            });
            d.render();
        })
        delimg.click(function () {
            delete   elData.txImgUrl ;
            videoImg.attr("src", jumi.config.cssPath+"/css/pc/img/noc_picture.png");
            module.view.updataPageModular(element);
        })
        chooseimg.click(function () {
            var d = new Dialog({
                context_path: opt.Project_Path,
                resType: 1,
                callback: function (url,originalUrl,data) {
                    if(!$.isEmptyObject(url)){
                        if(!$.isEmptyObject(url)){
                            url=  jumi.picParse(url,720);
                            elData.txImgUrl = url;
                            videoImg.attr("src",url);
                            module.view.updataPageModular(element);
                        }
                    }
                }
            });
            d.render();
        })


    };
    //空白条编辑窗口
    var bindwhiteEvent=function (element,configPanel) {
        var elData=$(element).data();
        var jmsliderId= "jmslider" +_getid();
        $(configPanel).find(".jmslider").attr("id",jmsliderId);
        var pixel= $(configPanel).find(".pixel");
        var jmslider1 = new sliderControl("#"+jmsliderId, {
            width: "65%",
            max: 100,
            min: 0,
            initialValue: elData.height,
            onSliderChange: function (v) {
                pixel.html(v + "像素");
                elData.height = v;
                module.view.updataPageModular($(element));
            }
        });
        jmslider1.render();
    }
    var bindlineEvent=function () {
    }
    var bindsearchEvent=function () {
    }
    //语音选择编辑窗口
    var bindaudioEvent=function (element,configPanel) {
        var elData=$(element).data();
        var audioGroup= $(configPanel).find(".audio-group");
        var audioGroupspan= audioGroup.find("span");
        var delbtn= audioGroup.find(".iconfont");
        audioGroupspan.click(function(){
            var d = new Dialog({
                context_path: opt.Project_Path,
                resType: 3,
                callback: function (url,data) {
                    if(!$.isEmptyObject(url)){
                        var item={
                            resUrl:data.resUrl,
                            resName:data.resName
                        }
                        audioGroup.data(item);
                        audioGroupspan.html(item.resName);
                        delbtn.removeClass("z-hide");
                        _updateData();
                    }
                }
            });
            d.render();
        })
        delbtn.click(function () {
            audioGroup.removeData();
            audioGroupspan.html("请选择一个音频文件");
            delbtn.addClass("z-hide");
            _updateData();
        })
        var  _updateData=function () {
            var subEntry = [];
            $(configPanel).find('.audio-group').each(function () {
                var tmpdata = $(this).data();
                if(tmpdata.resUrl){
                    subEntry.push(tmpdata);
                }
            })
            elData.subEntry = subEntry;
            if(subEntry.length==0){
                delete elData.subEntry;
            }
            module.view.updataPageModular(element);
        }
    }
    //轮播窗口编辑窗口
    var bindimageAdEvent=function (element,configPanel) {
        var elData=$(element).data();
        var addbtn= $(configPanel).find(".u-btn-lgltgry1")
        function _updateData(){
            var subEntry = [];
            $(configPanel).find('.imageAd-details').each(function () {
                var tmpdata = $(this).data();
                subEntry.push(tmpdata);
            })
            elData.subEntry = subEntry;
            module.view.updataPageModular($(element));
        }
        function _delEvent(item) {
            $(item).find(".delete-btn").click(function () {
                item.remove();
                _updateData();
            })
        }
        function _cheangeImg(item){
            var _imgitem=$(item).find(".imageAd-left img");
            $(item).find(".imageAd-left img").click(function () {
                var d = new Dialog({
                    context_path: opt.Project_Path,
                    resType: 1,
                    callback: function (url) {
                        if(!$.isEmptyObject(url)){
                            url=  jumi.picParse(url,720);
                            var imgdata= item.data();
                            _imgitem.attr("src",url).removeClass("z-hide");
                            imgdata.imageUrl = url;
                            _updateData();
                        }
                    }
                });
                d.render();
            })
        }
        function _changeUrl(item) {
            $(item).find(".imageAd-add-text .linkspan").click(function () {
                var tmpinput=$(this).next();

                // var data= item.data();
                // data.linkUrl = $(this).val();
                // _updateData();

                var shopId = $("#shopId").val();
                menuDialog.Menu.initPage({
                    shop_id:shopId,
                },function(menu){
                    menu.getUrl(function(link){
                        var url;
                        switch(link.link_type){
                            case '1':
                                url = DOMAIN+link.link_url+"?shopId="+link.shop_id;
                                break;
                            case '2':
                                url = DOMAIN+link.link_url+'/'+link.link_id+"?shopId="+link.shop_id;
                                break;
                            case '4':
                                url=link.link_url;
                                break;
                        }
                        var data= item.data();
                        $(tmpinput).val(url);
                        data.linkUrl = $(tmpinput).val();
                        _updateData();
                    });
                });
            })
        }
        function _loadItem(itemData){
            var html = jumi.templateHtml('imageAd-details.html',itemData,opt.editPath);
            var imageDetails=$(html).data(itemData);
            $(imageDetails).insertBefore(addbtn);
            _cheangeImg(imageDetails);
            _changeUrl(imageDetails);
            _delEvent(imageDetails);
        }
        if(!$.isEmptyObject(elData.subEntry)){
            $.each(elData.subEntry, function (i) {
                _loadItem(elData.subEntry[i])
            });
        }
        addbtn.click(function () {
            if($(configPanel).find('.imageAd-details').length>=6){
                alertinfo("商品轮播图不能超过6张！");
                return;
            }
            _loadItem({});
        })
    }

    //图片列表编辑窗口
    var bindimageListEvent=function(element,configPanel){
        var elData=$(element).data();
        var addbtn= $(configPanel).find(".imageListConfig .addimg");
        function _updateData(){
            var subEntry = [];
            $(configPanel).find('.item').each(function () {
                var tmpdata = $(this).data();
                subEntry.push(tmpdata);
            })
            elData.subEntry = subEntry;
            module.view.updataPageModular($(element));
        }
        function  _delEvent(item) {
            $(item).find(".opt-btns .del").click(function () {
                item.remove();
                _updateData();
            })
        }
        function _cheangeImg(item){
            var _img=$(item).find("img");
            $(item).find(".opt-btns .eidt").click(function () {
                var _this=_img;
                var d = new Dialog({
                    context_path: opt.Project_Path,
                    resType: 1,
                    callback: function (url) {
                        if(!$.isEmptyObject(url)){
                            url=  jumi.picParse(url,720);
                            var imgdata= item.data();
                            _this.attr("src",url);
                            imgdata.imageUrl = url;
                            _updateData();
                        }
                    }
                });
                d.render();
            })
        }
        function _pointeidt(item){
            $(item).find(".opt-btns .pointeidt").click(function () {
                $(".hotSpotConfig",configPanel).empty();
                var itemdata=item.data();
                var html = jumi.templateHtml('hotSpot.html',itemdata,opt.editPath);
                var hotSpotConfig=$(html).data(itemdata);
                $(hotSpotConfig).appendTo(".hotSpotConfig",configPanel);
                $(".imageListConfig",configPanel).hide();
                $(hotSpotConfig).show();
                var refun=function () {
                    $(hotSpotConfig).empty().hide();
                    $(item).closest(".imageListConfig").show();
                    _updateData();
                }
                hotSpot.config.initHotSpot(hotSpotConfig,item,refun);
            })
        }

        function _loadItem(itemData){
            var html = jumi.templateHtml('image-item.html',itemData,opt.editPath);
            var imageDetails=$(html).data(itemData);
            $(imageDetails).insertBefore(addbtn);
            _cheangeImg(imageDetails);
            _delEvent(imageDetails);
            _pointeidt(imageDetails);
        }

        if(!$.isEmptyObject(elData.subEntry)){
            $.each(elData.subEntry, function (i) {
                _loadItem(elData.subEntry[i]);
            });
        }
        addbtn.click(function () {
            if($(configPanel).find('.item').length>=10){
                alertinfo("图片列表不能超过10张！");
                return;
            }
            var d = new Dialog({
                context_path: opt.Project_Path,
                resType: 1,
                callback: function (url) {
                    if(!$.isEmptyObject(url)){
                        url=  jumi.picParse(url,720);
                        var imgdata={};
                        imgdata.imageUrl = url;
                        _loadItem(imgdata);
                        _updateData();
                    }
                }
            });
            d.render();
        })
    }

    //商品列表编辑窗口
    var bindgoodslistEvent=function(element,configPanel){
        var elData=$(element).data();
        function loadGoodsList(element) {
            var data = $(element).data();
            var url = opt.goodslistAjax ;
            var productVo = {};
            if(data.goodsTypeId){
                productVo.pageSize = elData.size||24;
                productVo.curPage = 0;
                productVo.groupId = data.goodsTypeId;
                var jsonStr = JSON.stringify(productVo);
                $.ajaxJson(url, jsonStr, {
                    "done": function (res) {
                        if (res.code == 0) {
                            data.items = res.data.items;
                            module.view.updataPageModular($(element));
                        }
                    }
                });
            }

        }
        $(configPanel).find(":radio").click(function () {
            elData[$(this).prop("name")] = $(this).val();
            loadGoodsList($(element));
            module.view.updataPageModular($(element));
        });
        $(configPanel).find(".chooseType").click(function () {
            var _this=this;
            var goodsTypeDialog = new GoodsTypeDialog({
                context_path: opt.Project_Path
            }, function (goodsType) {
                elData.goodsTypeName = goodsType.group_name;
                elData.goodsTypeId = goodsType.group_id;
                elData.items = [];
                $(_this).html(elData.goodsTypeName);
                loadGoodsList($(element));
                module.view.updataPageModular($(element));
            })
            goodsTypeDialog.render();
        });
    }
    //添加具体商品
    var bindgoodsEvent=function(element,configPanel){
        var elData=$(element).data();
        var   addbtn =  $(configPanel).find(".goodsItemGroup .addbtn");
        $(configPanel).find(":radio").click(function () {
            elData[$(this).prop("name")] = $(this).val();
            module.view.updataPageModular($(element));
        });
        function _updateData(){
            var items = [];
            $(configPanel).find('.item').each(function () {
                var tmpdata = $(this).data();
                items.push(tmpdata);
            })
            elData.items = items;
            module.view.updataPageModular($(element));
        }
        function  _delEvent(item) {
            $(item).find(".delete-btn").click(function () {
                item.remove();
                _updateData();
            })
        }
        function _loadItem(itemData){
            var html = jumi.templateHtml('goods-item.html',itemData,opt.editPath);
            var goodsItem=$(html).data(itemData);
            $(goodsItem).insertBefore(addbtn);
            _delEvent(goodsItem);
        }
        if(!$.isEmptyObject(elData.items)){
            $.each(elData.items, function (i) {
                _loadItem(elData.items[i]);
            });
        }
        addbtn.click(function () {
            var _this=$(this);
            var goodsDialog = new GoodsDialog({
                context_path: Project_Path
            }, function (goodslist) {
                if (goodslist != null && goodslist.length > 0) {
                    $(goodslist).each(function (j) {
                        _loadItem(goodslist[j]);
                    })
                    _updateData();
                }
            })
            goodsDialog.render();
        })
    }

    var synGoods=function (element) {
        var goodsArr=[];
        var tmpdata=$(element).data();
        if(tmpdata.items.length>0){
            $(tmpdata.items).each(function (i) {
                 goodsArr.push(tmpdata.items[i].pid);
            })
            var url=opt.syngoodsAjax+goodsArr.join(",");
            $.ajaxJsonGet(url,null,{
                "done":function (res) {
                    if(res.code==0){
                        var numarr=[];
                        $(tmpdata.items).each(function (i) {
                            $(res.data).each(function (j) {
                                if(tmpdata.items[i].pid==res.data[j].pid){
                                    tmpdata.items[i].pic_rectangle=res.data[j].picRectangle;
                                    tmpdata.items[i].pic_square=res.data[j].picSquare;
                                    tmpdata.items[i].name=res.data[j].name;
                                    tmpdata.items[i].price=res.data[j].price;
                                    tmpdata.items[i].share=res.data[j].share;
                                    numarr.push(i);
                                }
                            })
                        })
                        var items=[];
                        for(var k=0;k<numarr.length;k++){
                            items.push(tmpdata.items[numarr[k]]);
                        }
                        tmpdata.items=items;
                        module.view.updataPageModular($(element));
                    }
                }
            })
        }
    }



    //魔方编辑窗口
    var bindmagicSquareEvent=function (element,configPanel) {
        var elData=$(element).data();
        elData.magicNum=elData.randomNum;
        var rmax=4;
        var cmax=4;

        /**
         *  递归方法 获取可选区域数据
         * @param data
         * @param rindex 坐标r 起始r
         * @param cindex 坐标c
         * @param rstart  起步
         * @param cstart  控制c
         * @param rmax    最大循列
         */
        function  reTableData(data,rindex,cindex,rstart,cstart,rmax) {
            var _this;
            if(rmax>rindex){
                _this=$("#"+elData.magicNum+"-view .magic-view-table tr td[data-x='"+rindex+"'][data-y='"+cindex+"']");
            }
            if(_this&&_this.length>0){
                rindex++;
                reTableData(data,rindex,cindex,rstart,cstart,rmax);
                data[cstart].push(_this.attr("class"));
            }else{
                if(rmax>rindex){
                    rmax=rindex;
                }
                if(rindex>rstart){
                    cindex++;
                    cstart++;
                    rindex=rstart;
                    var _nextthis=$("#"+elData.magicNum+"-view .magic-view-table tr td[data-x='"+rindex+"'][data-y='"+cindex+"']");
                    if(_nextthis&&_nextthis.length>0){
                        data.push([]);
                        reTableData(data,rindex,cindex,rstart,cstart,rmax);
                    }
                }
            }
        }
        function returnSelectTableData(selectdata,item,cindex,rindex,rstart,cmax){
            var _this;
            if(cmax>cindex){
                if(cindex<=(item.x+item.col-1)&&rindex<=(item.y+item.row-1)){
                    cindex=item.x+item.col;
                }
                if(cmax>cindex){
                    _this=$("#"+elData.magicNum+"-view .magic-view-table tr td[data-x='"+cindex+"'][data-y='"+rindex+"']");
                }
            }
            if(_this&&_this.length>0){
                selectdata.push({row:(rindex+1-item.y),col:(cindex+1-item.x),sign:'select'});
                cindex++;
                returnSelectTableData(selectdata,item,cindex,rindex,rstart,cmax);
            }else{
                if(cmax>cindex){
                    cmax=cindex;
                }
                if(cindex>item.x){
                    cindex=item.x;
                    rindex++;
                    returnSelectTableData(selectdata,item,cindex,rindex,rstart,cmax);
                }
            }
        }

        function initViewTable(sign ,selx,sely){
            $("#"+elData.magicNum+"-view").empty();
            var html = jumi.templateHtml('magic-view-table.html',elData,opt.editPath);
            var viewitem=$(html);
            $("#"+elData.magicNum+"-view").append(viewitem);
            if(elData.subEntry&&elData.subEntry.length>0){
                elData.subEntry= _.sortBy(elData.subEntry,'y');
                elData.subEntry= _.sortBy(elData.subEntry,'x');
                _.each(elData.subEntry,function (item,k) {
                    for(var i=0;i<item.col;i++){
                        for(var j=0;j<item.row;j++){
                            if(i==0&&j==0){
                            }else{
                                $("#"+elData.magicNum+"-view .magic-view-table tr td[data-x='"+(item.x+i)+"'][data-y='"+(item.y+j)+"']").remove();
                            }
                        }
                    }
                    $("#"+elData.magicNum+"-view .magic-view-table tr td[data-x='"+selx+"'][data-y='"+sely+"']").addClass("z-sel");
                    var itemhtml="<sppan>"+item.row+" * "+item.col+"</span>";
                    var colorclass=" f-color-"+(k+1);
                    if(item.imageUrl){
                        itemhtml="<img src='"+item.imageUrl+"'/>";
                        colorclass="";
                    }
                    delete item.selectdata;
                    $("#"+elData.magicNum+"-view .magic-view-table tr td[data-x='"+item.x+"'][data-y='"+item.y+"']").attr({
                        "rowSpan":item.row,
                        "colSpan":item.col
                    }).data(item).addClass("f-cols-"+item.col+" f-rows-"+item.row+" no-empty "+colorclass)
                        .removeClass("empty").removeAttr("data-x").removeAttr("data-y").html(itemhtml);

                })
            }
            bindViewTableEvent ();
            if(sign){
                module.view.updataPageModular($(element));
            }
        }
        function bindViewTableEvent (){
            $(".magic-view-table tr .empty","#"+elData.magicNum+"-view").click(function () {

                var rindex=parseInt( $(this).attr("data-x"));
                var cindex=parseInt($(this).attr("data-y"));
                var data=[[]];
                reTableData(data,rindex,cindex,rindex,0,rmax);
                var itemdata={tabledata:data };
                if(itemdata.tabledata.length==1&&itemdata.tabledata[0].length==1){
                    var tmpdata={x:rindex,y:cindex,col:1,row:1};
                    if(!elData.subEntry){
                        elData.subEntry=[];
                    }
                    elData.subEntry.push(tmpdata);
                    initViewTable(true );
                    $(".magic-view-table tr .no-empty","#"+elData.magicNum+"-view").each(function () {
                        var data=$(this).data();
                        if(tmpdata.x==data.x&&tmpdata.y==data.y){
                            $(this).click();
                        }
                    })
                }else{
                    var html = jumi.templateHtml('magic-edit-table.html',itemdata,opt.editPath);
                    var edititem=$(html).data( {x:rindex,y:cindex} );
                    $("#"+elData.magicNum+"-edit").append(edititem);
                    edititem.animate({  top: 25 }, 500, "linear", function() {
                        if($("#"+elData.magicNum+"-edit .edit-window").length>1){
                            $("#"+elData.magicNum+"-edit .edit-window:first").remove();
                        }
                        bindEditTableEven();
                    });
                }
            })
            $(".magic-view-table tr .no-empty","#"+elData.magicNum+"-view").click(function () {
                var itemdata=$(this).data();
                $(".magic-view-table tr .no-empty","#"+elData.magicNum+"-view").removeClass("z-sel");
                $(this).addClass("z-sel");
                delete itemdata.selectdata;
                itemdata.selectdata=[];
                for(var r=1;r<=itemdata.row;r++){
                    for(var c=1;c<=itemdata.col;c++){
                        itemdata.selectdata.push({row:r,col:c});
                    }
                }
                returnSelectTableData(itemdata.selectdata,itemdata,itemdata.x,itemdata.y,itemdata.x,cmax);
                itemdata.selectdata= _.sortBy(itemdata.selectdata,'y');
                var html = jumi.templateHtml('magic-edit-panel.html',itemdata,opt.editPath);
                var edititem=$(html).data( itemdata );
                $("#"+elData.magicNum+"-edit").append(edititem);
                edititem.animate({  top: 25 }, 500, "linear", function() {
                    if($("#"+elData.magicNum+"-edit .edit-window").length>1){
                        $("#"+elData.magicNum+"-edit .edit-window:first").remove();
                    }
                    bindEditTableEven();
                });

            })
        }
        function bindEditTableEven(){
            $(".edit-window .icon-delete1","#"+elData.magicNum+"-edit").click(function () {
                $(this).closest(".edit-window").animate({  top: 400 }, 500, "linear", function() {
                    if(elData.subEntry){
                        var data=$(this).closest(".edit-window").data();
                        _.each(elData.subEntry,function (item,k) {
                            if(data.x==item.x&&data.y==item.y){
                                delete  elData.subEntry[k];
                                elData.subEntry=_.compact(elData.subEntry);
                                initViewTable(true,data.x,data.y);
                            }
                        })
                    }
                    $(this).remove();
                });
            })
            $(".edit-window .magic-edit-table tr td","#"+elData.magicNum+"-edit").mouseenter(function () {
                $(".edit-window .magic-edit-table tr td","#"+elData.magicNum+"-edit").removeClass("z-sel");
                var _this=  $(this);
                var col=$(this).index()+1;
                var row=$(this).parent().index()+1;
                $(".edit-window .magic-edit-table tr:lt("+row+") ","#"+elData.magicNum+"-edit").each(function (i) {
                    $(this).find("td:lt("+col+")").addClass("z-sel");
                });
            }).click(function () {
                var col=$(this).index()+1;
                var row=$(this).parent().index()+1;
                var data=$(this).closest(".edit-window").data();
                data.col=col;
                data.row=row;
                if(!elData.subEntry){
                    elData.subEntry=[];
                }
                elData.subEntry.push(data);
                initViewTable( true);
                // $(".edit-window .icon-delete1","#"+elData.randomNum+"-edit").click();
                $(this).closest(".edit-window").animate({  top: 400 }, 500, "linear", function() {
                    $(this).remove();
                    $(".magic-view-table tr .no-empty","#"+elData.magicNum+"-view").each(function () {
                        var tmpdata=$(this).data();
                        if(tmpdata.x==data.x&&tmpdata.y==data.y){
                            $(this).click();
                        }
                    })
                });
            })
            $(".edit-window .m-add-img","#"+elData.magicNum+"-edit").click(function(){
                var _editWindow=$(this).closest(".edit-window");
                var _this=$(this);
                var d = new Dialog({
                    context_path: opt.Project_Path,
                    resType: 1,
                    callback: function (url) {
                        if(!$.isEmptyObject(url)){
                            url=  jumi.picParse(url,720);
                            // alert(url);
                            _this.find("img").attr("src",url);
                            var data=_editWindow.data();
                            data.imageUrl=url;
                            _.each(elData.subEntry,function (item,k) {
                                if(data.x==item.x&&data.y==item.y){
                                    elData.subEntry[k]=data;
                                }
                            })
                            initViewTable(true,data.x,data.y);
                        }
                    }
                });
                d.render();
            })
            $(".edit-window .link-span","#"+elData.magicNum+"-edit").click(function(){
                var _editWindow=$(this).closest(".edit-window");
                var _this=$(this);
                var shopId = $("#shopId").val();
                menuDialog.Menu.initPage({
                    shop_id:shopId,
                },function(menu){
                    menu.getUrl(function(link){
                        var url;
                        switch(link.link_type){
                            case '1':
                                url = DOMAIN+link.link_url+"?shopId="+link.shop_id;
                                break;
                            case '2':
                                url = DOMAIN+link.link_url+'/'+link.link_id+"?shopId="+link.shop_id;
                                break;
                            case '4':
                                url=link.link_url;
                                break;
                        }
                        var data=_editWindow.data();
                        data.linkName=link.link_name;
                        data.linkUrl=url;
                        _this.html(data.linkName);
                        _.each(elData.subEntry,function (item,k) {
                            if(data.x==item.x&&data.y==item.y){
                                elData.subEntry[k]=data;
                                initViewTable(false,data.x,data.y);
                            }
                        })
                    });
                });
            })
            $(".edit-window .placeholder","#"+elData.magicNum+"-edit").change(function(){
                var _editWindow=$(this).closest(".edit-window");
                var _this=$(this);
                var opt=$(".edit-window .placeholder option:selected","#"+elData.magicNum+"-edit");
                var data=_editWindow.data();
                data.col=opt.attr("col")*1;
                data.row=opt.attr("row")*1;
                _.each(elData.subEntry,function (item,k) {
                    if(data.x==item.x&&data.y==item.y){
                        elData.subEntry[k]=data;
                        initViewTable(true,data.x,data.y);
                    }
                })
            })
        }
        initViewTable();
    }
    //绑定title编辑窗口事件
    var bindvoteEvent=function(element,configPanel){
        var elData=$(element).data();
        console.log(elData);
        $("[name$='Time']",configPanel).datetimepicker({ timeFormat:'hh:mm:ss'})
             .on( "change", function() {
                 elData[$(this).prop("name")]=$(this).val();
             });
         $(configPanel).find("[name='themeName']").change(function () {
            elData[$(this).prop("name")]=$(this).val();
            module.view.updataPageModular($(element));
        })
        $(configPanel).find(":radio").click(function () {
            elData[$(this).prop("name")] = $(this).val();
            module.view.updataPageModular($(element));
        });
        var chooseItem= $(configPanel).find(".chooseItem");
        chooseItem.click(function () {
            vote.item.choiceWindow({
                voteType:elData.voteType
            },function (list) {
                if(!$.isEmptyObject(list)&&list.length>0){
                    for(var v=0;v<list.length;v++){
                         console.log(list[v]);
                        var sgin=true;
                        for(var k=0;k<elData.voteItemList.length;k++){
                            if(list[v].id==elData.voteItemList[k].itemId){
                                sgin=false;
                                break;
                            }
                        }
                        if(sgin){
                            list[v].itemId=list[v].id;
                            delete list[v].id;
                            addVoteItem(list[v]);
                        }

                    }
                    _updateData();
                }
                showHideBox();
            })
        })
        var listbody=$(configPanel).find(".listbody");
        var showHideBox=function () {
            var len= $(".table-container",listbody).length;
            if(len>0){
                $(listbody).show();
            }else{
                $(listbody).hide();
            }
        }
        var tablebody= $(configPanel).find(".table-body");
        var addVoteItem=function (item) {
            var html = jumi.templateHtml('vote-item.html',item,opt.editPath);

            var item=$(html).data(item);
            $(item).appendTo(tablebody);
            $(".delbtn",item).click(function () {
              $(this).closest(".table-container").remove();
                _updateData();
            })
        }
        var _updateData=function () {
            elData.voteItemList=[];
            $(".table-container",tablebody).each(function () {
                var data=$(this).data();
                elData.voteItemList.push(data)
            })
            module.view.updataPageModular($(element));
        }

        if(!$.isEmptyObject(elData.voteItemList)){
            $.each(elData.voteItemList, function (i) {
                addVoteItem(elData.voteItemList[i]);
            });
            showHideBox();
        }

    }
    var bindsignupEvent=function (element,configPanel) {
        var elData=$(element).data();
        console.log(elData);
        var signUpJson=signup.data.getdata();
        if(elData.attrList){
            for (var i=0;i<elData.attrList.length;i++){
            $(".m-enrollbody",configPanel).append("<div class='u-sort'>" + elData.attrList[i].name + "</div>");
            }
        }

        $("[name$='Date']",configPanel).datetimepicker({ timeFormat:'hh:mm:ss'})
            .on( "change", function() {
                elData[$(this).prop("name")]=$(this).val();
            });
        $(configPanel).find("input,textarea").change(function () {
            elData[$(this).prop("name")]=$(this).val();
            module.view.updataPageModular($(element));
        })
        $("img",configPanel).click(function () {
            var _imgitem=$(this);
            var d = new Dialog({
                context_path: opt.Project_Path,
                resType: 1,
                callback: function (url) {
                    if(!$.isEmptyObject(url)){
                        url=  jumi.picParse(url,720);
                        _imgitem.attr("src",url) ;
                        elData.img = url;
                    }
                }
            });
            d.render();
        })
        $(".dialog-infonew",configPanel).click(function () {
            signup.index.choiceWindow( function (data) {
                elData.confId = data.id;
                elData.setInfo=data.setInfo;
                elData.titleName=data.titleName;
               console.log(data)
                var setInfoArr=data.setInfo.split(",");
                $(".titleName",configPanel).html(elData.titleName);
                $(".setInfonum",configPanel).html(setInfoArr.length);
                elData.attrList=[];
                $(".m-enrollbody",configPanel).empty();
                for (var i=0;i<setInfoArr.length;i++){
                    for(var j=0;j<signUpJson.length;j++){
                        if(signUpJson[j].id==setInfoArr[i]){
                            elData.attrList.push(signUpJson[j]);
                            $(".m-enrollbody",configPanel).append("<div class='u-sort'>" + signUpJson[j].name + "</div>");
                            break;
                        }
                    }
                }
                module.view.updataPageModular($(element));
            })
        })

    }
    var bindcommentEvent=function () {
    }

    var bindactivityGoods=function (element,configPanel) {
        var elData=$(element).data();
        console.log(elData)
        $("#"+elData.randomNum+"-activityType").select2({
            theme: "jumi"
        }).on('change', function (evt) {
            elData.activityType=$(this).val();
            $(configPanel).find('.item').remove();
            elData.goodsitems=[];
            loadActivityGoodslist();
            _updateData();
        });
        var loadActivityGoodslist=function () {
            if(elData.activityType!=0){
                var url = opt.activityGoodslistAjax ;
                var productQo = {};
                productQo.pageSize = elData.size||10;
                productQo.curPage = 0;
                productQo.type = elData.activityType;
                var jsonStr = JSON.stringify(productQo);
                $.ajaxJson(url, jsonStr, {
                    "done": function (res) {
                        if (res.code == 0) {
                            elData.goodsitems = res.data.items;
                            module.view.updataPageModular($(element));
                        }
                    }
                });
            }
        }
        var   addbtn =  $(configPanel).find(".goodsItemGroup .addbtn");
        $(configPanel).find(":radio").click(function () {
            elData[$(this).prop("name")] = $(this).val();
            module.view.updataPageModular($(element));
        });
        function _updateData(){
            var items = [];
            if( $(configPanel).find('.item').length>0){
                $(configPanel).find('.item').each(function () {
                    var tmpdata = $(this).data();
                    items.push(tmpdata);
                })
            }else{
                loadActivityGoodslist();
            }
            elData.items = items;
            module.view.updataPageModular($(element));
        }
        function  _delEvent(item) {
            $(item).find(".delete-btn").click(function () {
                item.remove();
                _updateData();
            })
        }
        function _loadItem(itemData){
            var html = jumi.templateHtml('activity-good-item.html',itemData,opt.editPath);
            var goodsItem=$(html).data(itemData);
            $(goodsItem).insertBefore(addbtn);
            _delEvent(goodsItem);
        }
        if(!$.isEmptyObject(elData.items)){
            $.each(elData.items, function (i) {
                _loadItem(elData.items[i]);
            });
        }
        addbtn.click(function () {
            var _this=$(this);
            var activityGoodsDialog = new ActivityGoodsDialog({
                context_path: Project_Path,
                activityType:elData.activityType
            }, function (goodslist) {
                if (goodslist != null && goodslist.length > 0) {
                    elData.goodsitems=[];
                    $(goodslist).each(function (j) {
                        _loadItem(goodslist[j]);
                    })
                    _updateData();
                }
            })
            activityGoodsDialog.render();
        })


    }
    return {
        "title":bindtitleEvent,
        "titleInfo":bindtitleInfoEvent,
        "weixintop":bindweixintopEvent,
        "weixinbottom":bindweixinbottomEvent,
        "richtext":bindrichtextEvent,
        "richtextlimit":bindrichtextlimitEvent,
        "QRcode":bindQRcodeEvent,
        "titleBar":bindtitleBarEvent,
        "bottomPic":bindbottomPicEvent,
        "imageTextTitle":bindimageTextTitleEvent,
        "rewardModule":bindrewardModuleEvent,
        "video":bindvideoEvent,
        "white":bindwhiteEvent,
        "search":bindsearchEvent,
        "audio":bindaudioEvent,
        "imageAd":bindimageAdEvent,
        "imageList":bindimageListEvent,
        "goodslist":bindgoodslistEvent,
        "goods":bindgoodsEvent,
        "magicSquare":bindmagicSquareEvent,
        "line":bindlineEvent,
        "vote":bindvoteEvent,
        "synGoods":synGoods,
        "convertUrl":convert_url,
        "signup":bindsignupEvent,
        "activityGoods":bindactivityGoods,
        "comment":bindcommentEvent
    }

})();