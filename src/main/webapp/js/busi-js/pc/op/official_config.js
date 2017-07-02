/**
 * Created by Administrator on 2017/1/18.
 */
CommonUtils.regNamespace("official", "config");

official.config = (function () {
    var OFFICIAL_IMAGE = 2;  //官方图文
    var ajaxUrl = {
        moduleTypesAjax: "product/moduleTypes.json",
        saveAjax: CONTEXT_PATH + "/image_text"
    }
    var opt = {
        id: 0,
        pagetype: OFFICIAL_IMAGE,
        mainContainer: "image-text-details"
    }

    var _init = function (optsions) {

        opt.isEdit = optsions.isEdit||null;
        opt.id = optsions.id || 0;
        opt.typeId = optsions.typeId;
        opt.pagetype = OFFICIAL_IMAGE;
        opt.mainContainer = optsions.mainContainer;
        delete opt.imgTextRo;
        delete opt.audioRes;
        delete opt.buttonList;
        delete opt.imageTextTypes;
        delete opt.imgResList;
        delete opt.videoRes;
        delete opt.videoResoure;
        _loadhtml().then(function () {
            return _loadofficial();
        }).then(function () {
            _loadmoduleTypes()
        }).then(function () {
            _correctData();
            _bandEvent();
        }).then(function () {
            module.main.init(opt);
            module.main.locationWindow();
        }).then(function () {
            imageText.oldh5.init(opt);
        })
    };
    var _loadmoduleTypes = function () {
        // var dfdPlay = $.Deferred();
        // jumi.file(ajaxUrl.moduleTypesAjax, function (moduleTypes) {
        //     opt.moduleTypes = moduleTypes.data;
        //     dfdPlay.resolve(); // 动画完成
        // })
        // return dfdPlay;
        opt.moduleTypes=module.types.getTypesJson();
    }

    var _loadhtml = function () {
        var dfdPlay = $.Deferred();
        $("#officicalEidt").empty();
        var itemhtml = jumi.templateHtml("/tpl/op/new/imageText.html", {});
        $("#officicalList").hide();
        $("#officicalEidt").append(itemhtml).show();
        dfdPlay.resolve(); // 动画完成
        return dfdPlay;
    }


    //获取图文信息
    _loadofficial = function () {
        var dfdPlay = $.Deferred();
        if (opt.id) {
            opt.mainContainer = "image-text-details";
            var url = ajaxUrl.saveAjax + "/" + opt.id;
            $.ajaxJsonGet(url, "", {
                "done": function (res) {
                    if (res.code == '0') {
                        opt.imgTextRo = res.data.imgTextRo;
                        opt.audioRes = res.data.audioRes;
                        opt.buttonList = res.data.buttonList;
                        opt.imageTextTypes = res.data.imageTextTypes;
                        opt.imgResList = res.data.imgResList;
                        opt.videoRes = res.data.videoRes;
                        opt.videoResoure = res.data.videoResoure;
                        dfdPlay.resolve(); // 动画完成
                    }
                }
            });
        } else {
            dfdPlay.resolve(); // 动画完成
        }
        return dfdPlay;
    }

    var _correctData = function () {

        if (opt.id) {
            var imgTextRo = opt.imgTextRo;
            if (imgTextRo.editionSign == '1'&& !$.isEmptyObject(imgTextRo.detailJson)) {
                //新版
                if (imgTextRo.formatCode == '1' || imgTextRo.formatCode == '2') {
                    opt.curVersion = "weixinImage";
                    var json = $.parseJSON(imgTextRo.detailJson);
                    opt.versionList = json.versionList;
                    $(opt.versionList).each(function (j) {
                        if (opt.versionList[j].versionId == 'weixinImage') {
                            $(opt.versionList).each(function (j) {
                                $(opt.versionList[j].versionContent).each(function (k) {
                                    if (opt.versionList[j].versionContent[k].type == 'weixinbottom') {
                                        opt.versionList[j].versionContent[k].likes = imgTextRo.reward;
                                    }
                                })
                            })
                        }
                    })
                } else if (imgTextRo.formatCode == '3') {
                    opt.curVersion = "Imageh5version";
                }
            } else {
                //如果是旧版数据 进行转换处理
                var weixinImageArray = {
                    "versionName": "仿微信页",
                    "versionId": "weixinImage",
                    "versionContent": []
                };
                var h5versionArray = {
                    "versionName": "H5海报",
                    "versionId": "Imageh5version",
                    "versionContent": []
                };
                var versionList = [];
                weixinImageArray.versionContent.push({
                    "type": "imageTextTitle",
                    "formatCode": "1",
                    "imageTextTile": imgTextRo.imageTextTile,
                    "shareText": imgTextRo.shareText,
                    "imageTextType": imgTextRo.imageTextType,
                    "imageUrl": imgTextRo.imageUrl,
                    "fixedTop": true,
                    "typeId": opt.typeId
                });
                weixinImageArray.versionContent.push({
                    "type": "richtext",
                    "content": imgTextRo.imageTextDetail
                });
                weixinImageArray.versionContent.push({
                    "type": "weixinbottom",
                    "linkTxt": imgTextRo.readLinkAdd,
                    "likes": imgTextRo.reward
                });
                if (imgTextRo.formatCode == '1' || imgTextRo.formatCode == '2') {
                    opt.curVersion = "weixinImage";
                } else if (imgTextRo.formatCode == '3') {
                    opt.curVersion = "Imageh5version";
                }
                versionList.push(weixinImageArray);
                versionList.push(h5versionArray);
                opt.versionList = versionList;
            }

        } else {
            var weixinImageArray = {"versionName": "仿微信页", "versionId": "weixinImage", "versionContent": []};
            weixinImageArray.versionContent.push({
                "type": "imageTextTitle",
                "formatCode": "1",
                "fixedTop": true,
                "typeId": opt.typeId
            });
            weixinImageArray.versionContent.push({"type": "weixinbottom"});
            var h5versionArray = {"versionName": "H5海报", "versionId": "Imageh5version", "versionContent": []};
            opt.curVersion = "weixinImage";
            opt.mainContainer = "image-text-details"
            var versionList = [];
            versionList.push(weixinImageArray);
            versionList.push(h5versionArray);
            opt.versionList = versionList;

        }

    }

    var _bandEvent = function () {
        $("#" + opt.mainContainer + " .details-hander").delegate(".u-rb label,label", "click", function () {
            $(".m-config-panel").hide();
            var togglepage = $(this).attr("data-toggle");
            $("#" + togglepage).show();
            opt.curVersion = $(this).attr("data-toggle");
            module.main.setCurVersion(opt.curVersion);
            if (opt.curVersion == 'weixinImage') {
                $(" .app-config .app-field:first", "#" + opt.curVersion).each(function () {
                    module.view.updataPageModular($(this));
                })
                var len = $(".m-app-config-region .editContent", "#" + opt.curVersion).children().length;
                if (len == 0) {
                    $(".app-config  .app-field:first .control-mask", "#" + opt.curVersion).click();
                }
            }
        });


        $("#saveimagebtn").click(function () {
            var data = {};
            if(opt.curVersion=='weixinImage'){
                var sign = module.main.verifyData();
                if(!sign){
                    return;
                }
            }else if(opt.curVersion=='Imageh5version'){
                var sign = imageText.oldh5.verifyData();
               if(!sign){
                   return;
               }
            }
            var url = ajaxUrl.saveAjax;
            if (opt.id) {
                data = _getsaveData(2);
                var jsonData = JSON.stringify(data);
                $.ajaxJsonPut(url, jsonData, {
                    "done": function (res) {
                        var dm = new dialogMessage({
                            type: 1,
                            fixed: true,
                            msg: res.data.msg,
                            isAutoDisplay: true,
                            time: 1000
                        });
                        dm.render();
                        setTimeout(function(){
                            $("#officicalEidt").empty();
                            $("#officicalList").show();
                            $("#officicalEidt").hide();
                            official.imglist.querylist();
                        },1500)
                    },
                    "fail": function (res) {
                    }
                });
            } else {
                data = _getsaveData(1);
                var jsonData = JSON.stringify(data);
                $.ajaxJson(url, jsonData, {
                    "done": function (res) {
                        var dm = new dialogMessage({
                            type: 1,
                            fixed: true,
                            msg: res.data.msg,
                            isAutoDisplay: true,
                            time: 1000
                        });
                        dm.render();
                        setTimeout(function(){
                            $("#officicalEidt").empty();
                            $("#officicalList").show();
                            $("#officicalEidt").hide();
                            official.imglist.querylist();
                        },1500);
                    },
                    "fail": function (res) {
                    }
                });

            }
        })
        $("#returnbtn").click(function () {
            $("#officicalEidt").empty();
            $("#officicalList").show();
            $("#officicalEidt").hide();
        })
    }

    var _getsaveData = function (tmpsign) {
        var data = {};
        var imgText ={};

        var  oldH5=imageText.oldh5.getH5Info();
        var  base=module.main.getBaseInfo();
        if(opt.curVersion=='weixinImage'){
            imgText =   $.extend(oldH5, base);
            imgText.editionSign = 1;
        }else if(opt.curVersion=='Imageh5version'){
            imgText =   $.extend(base, oldH5);
            imgText.editionSign = 0;
        }
        imgText.detailJson = module.main.getjson();
        imgText.typeId = opt.typeId;

        data.imageTextRelateList=imageText.oldh5.getImageTextRelateList();
        if (tmpsign == 1) {
            imgText.isEdit = 1;
            imgText.status = 1;
            data.imgTextCo = imgText;
        } else {
            imgText.id = opt.id;
            data.imgTextUo = imgText;
        }

        return data;
    }

    return {
        init: _init
    };
})();