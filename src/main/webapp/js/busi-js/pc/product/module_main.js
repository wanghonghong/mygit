/**
 * Created by zx on 2017/2/28.
 */
CommonUtils.regNamespace("module", "main");
module.main=(function () {
    var GOODS_MANAGEMENT=0;  //商品管理
    var MALL_BUILDING=1;  //商城搭建
    var OFFICIAL_IMAGE=2;  //官方图文
    var VOTE_THEME=3;  //投票主题
    var SIGN_UP =4;  //报名
    var opt={
        mainContainer:'goods-details',
        curVersion : 'standard',
        editPath:STATIC_URL+'/tpl/product/detail/edit/',
        pagetype:GOODS_MANAGEMENT,
        moduleTypes:[],
        versionList:[],
        goodsArr:[]
    }
    var _init=function (data) {
        $.extend(opt,data);
         initPage();
          bindEvent();
        //同步一次具体商品的信息
         synData();
    };
    var synData=function () {
        opt.goodsArr=[];
        $(" .js-fields-region .app-field ","#"+opt.mainContainer).each(function(i) {
            var tmpdata=$(this).data();
            if(tmpdata.type=='goods'){
                var _this=$(this);
                module.edit.synGoods(_this);
            }
        })
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
    var _setCurVersion=function (curVersion) {
        opt.curVersion=curVersion;
    }
    var _getCurVersion=function () {
        return opt.curVersion;
    }
    //定位窗口位置
    var _locationWindow=function () {
        $("#"+opt.mainContainer+" .details-hander .u-rb label").each(function() {
            var togglepage = $(this).attr("data-toggle");
            if (togglepage == opt.curVersion) {
                $(this).click();
            }
        });
    }
    //拖动事件绑定
    var resortableApp=function () {
        $("#"+opt.mainContainer+" .m-config-panel .js-fields-region").sortable({
            opacity: "0.6",
            connectWith: ".app-field",
            placeholder: "field-placeHolder"
        }).disableSelection();
    }
    //注销拖动事件
    var destroyResortable=function () {
        $("#"+opt.mainContainer+" .m-config-panel .js-fields-region").sortable("destroy");
    }

    var _getAllJson=function(){
        var json={"curVersion":opt.curVersion,"versionList":[]};
        if(opt.pagetype==GOODS_MANAGEMENT){
            var standardArray = {"versionName": "标准版","versionId": "standard","versionContent": []};
            var weixinArray = {"versionName": "仿微信版","versionId": "weixin","versionContent": []};
            json.versionList.push(standardArray);
            _returnBindData("standard",standardArray);
            json.versionList.push(weixinArray);
            _returnBindData("weixin",weixinArray);
        }else  if(opt.pagetype==MALL_BUILDING){
            var standardArray = {"versionName": "标准版","versionId": "standard","versionContent": []};
            json.versionList.push(standardArray);
            _returnBindData("standard",standardArray);
        }else if (opt.pagetype==OFFICIAL_IMAGE){
            var weixinImageArray = {"versionName": "仿微信版","versionId": "weixinImage","versionContent": []};
            json.versionList.push(weixinImageArray);
            _returnBindData("weixinImage",weixinImageArray);
        }else if(opt.pagetype==VOTE_THEME){
            var standardArray = {"versionName": "标准版","versionId": "standard","versionContent": []};
            json.versionList.push(standardArray);
            _returnBindData("standard",standardArray);
        }else if(opt.pagetype==SIGN_UP){
            var standardArray = {"versionName": "标准版","versionId": "standard","versionContent": []};
            json.versionList.push(standardArray);
            _returnBindData("standard",standardArray);
        }
        json = JSON.stringify(json);
        return json;
    }

    var   _returnBindData=function(pageid,data){
        $(".app-config  .app-field, .js-fields-region .app-field, .app-bottom  .app-field","#"+pageid).each(function(i) {
            var tmpdata = $.extend({}, $(this).data());
            delete tmpdata.sortableItem;
            delete tmpdata.componentList;
            delete tmpdata.randomNum;
            delete tmpdata.title;
            delete tmpdata.typeDescribe;
            delete tmpdata.verifyArray;
            data.versionContent.push(tmpdata);
        })
    }
    var bindEvent=function () {

        $(".m-js-add-region .add-region .u-btn-mdltgry").click(function () {
            var moduleType = $(this).attr("moduleType");
            for (var i = 0; i < opt.moduleTypes.length; i++) {
                if (opt.moduleTypes[i].type === moduleType) {
                    var pageEl = $(this).closest(".m-config-panel");
                    module.view.insertPageModular(opt.moduleTypes[i], pageEl);
                }
            }
        });
        //向上
        $("#"+opt.mainContainer+" .m-config-panel .js-fields-region ").delegate(".up", "click", function () {
            var appfield = $(this).closest(".app-field");
            var bapp=appfield.prev();
            if(bapp.length){
                $(appfield).insertBefore(bapp);
                destroyResortable();
                resortableApp();
                editConfig(appfield);
            }
        });
        //向下
        $("#"+opt.mainContainer+"  .m-config-panel .js-fields-region ").delegate(".down", "click", function () {
            var appfield = $(this).closest(".app-field");
            var aapp=appfield.next();
            if(aapp.length){
                $(appfield).insertAfter(aapp);
                destroyResortable();
                resortableApp();
                editConfig(appfield);
            }
        });
        //编辑
        $("#"+opt.mainContainer+" .m-config-panel .js-fields-region ").delegate(".editspan", "click", function () {
            var appfield = $(this).closest(".app-field");
            if(!$(appfield).hasClass("z-edit")){
                var panel=appfield.closest(".m-config-panel");
                $(".app-field", panel).removeClass("z-edit");
                $(appfield).addClass("z-edit");
                editConfig(appfield);
            }
        });
        //删除
        $("#"+opt.mainContainer+" .m-config-panel .js-fields-region ").delegate(".delspan", "click", function () {
            $(this).closest(".app-field").remove();
            //定位 标题编辑
            $(".app-config  .app-field:first .control-mask","#"+opt.curVersion).click();
        });
        $("#"+opt.mainContainer+" .m-config-panel ").delegate(".control-mask", "click", function () {
            var appfield = $(this).closest(".app-field");
            if(!$(appfield).hasClass("z-edit")){
                var panel=appfield.closest(".m-config-panel");
                $(".app-field", panel).removeClass("z-edit");
                $(appfield).addClass("z-edit");
                editConfig(appfield);
            }
        });
        resortableApp();
    };
    var initPage=function () {

        var versionList=opt.versionList;

        $.each(versionList,function (v) {
            var pageEl = $("#" + versionList[v].versionId);
            var moduleData = versionList[v].versionContent;
            $.each(moduleData, function (j) {
                var moduleType = {};
                $.each(opt.moduleTypes, function (o) {
                    if (opt.moduleTypes[o].type === moduleData[j].type) {
                        moduleType = opt.moduleTypes[o];
                    }
                });
                var elData = $.extend({}, {}, moduleType, moduleData[j]);
                elData.isEdit = opt.isEdit;
                module.view.insertPageModular(elData, pageEl);
            })
        })
    };
    //修改配置窗口
    var editConfig=function(element){
        var offset = $(element).offset();
        var elData = $(element).data();
        var positionTop = $(element).position().top - 20;
        var  position= "relative";
        var  top="0px";
        if(elData.type=='richtext'){
            positionTop = $(element).position().top - 105;
        }else if(elData.type=='goodslist'){
            position= "absolute";
        }
        $(".config-panel", "#" + opt.curVersion).css({
            "margin-top": positionTop,
            "position":position
        });
        $(".editTitle", "#" + opt.curVersion).html(elData.title);
        $(".editContent", "#" + opt.curVersion).children().remove();
        var html = jumi.templateHtml(elData.type+'.html',elData,opt.editPath);
        var configPanel=$(html);
        $(".editContent", "#"+opt.curVersion).append(configPanel);
        bindEditEvent(element,configPanel);
    };
    //绑定修改窗口事件
    var bindEditEvent=function (element,configPanel) {
        var data = $(element).data();
        module.edit[data.type](element,configPanel);
    };
    var  _getBaseInfo=function () {
        //获取除详情json串以外的其他基本信息
        var data={};
        $(".m-app-main  .app-field","#"+opt.curVersion).each(function(i) {
            var tmpdata = $.extend({}, $(this).data());

            if(tmpdata.type=="title"){
                data.share=tmpdata.content;
            }else if(tmpdata.type=="imageTextTitle"){
                data.formatCode=tmpdata.formatCode;
                data.imageTextTile=tmpdata.imageTextTile;
                data.imageTextType=tmpdata.imageTextType;
                data.imageUrl=tmpdata.imageUrl;
                data.shareText=tmpdata.shareText;
                data.isAward=tmpdata.isAward;
                data.awardType=tmpdata.awardType;
                data.integralType=tmpdata.integralType;
                data.awardSecond=tmpdata.awardSecond;
                data.cardId=tmpdata.cardId;
            }else if(tmpdata.type=="QRcode"){
                data.bottomQrCode=tmpdata.ewmCode||1;
            }else if(tmpdata.type=="weixinbottom"){
                data.reward=tmpdata.likes||0;
            }else if(tmpdata.type=="vote"){
                // data=tmpdata;
                data.name=tmpdata.themeName;
                data.voteType=tmpdata.voteType;
                data.startTime=tmpdata.startTime;
                data.endTime=tmpdata.endTime;
                data.voteItemList=tmpdata.voteItemList;
            }else if(tmpdata.type=="signup"){
                // data=tmpdata;
                data.img=tmpdata.img;
                data.activityName=tmpdata.activityName;
                data.startDate=tmpdata.startDate;
                data.endDate=tmpdata.endDate;
                data.confId=tmpdata.confId;
                data.sms=tmpdata.sms;
                data.noticeDate=tmpdata.noticeDate;

            }
        })
        return data;
    }
    var  _verifyFun=function (data,verify) {
        var  sign=true;
        var tmpvar=data[verify.name];
        for (var i=0;i<verify.rules.length;i++){
            var rule=verify.rules[i];
            if(rule.ruleType=="required"){
                if(!tmpvar){
                    alertinfo(rule.ruleTip);
                    sign=false;
                    break;
                }
            }else if(rule.ruleType=="compare"){
                if(rule.compareType=="<"){
                    if(data[rule.compareObj]<tmpvar){
                        alertinfo(rule.ruleTip);
                        sign=false;
                        break;
                    }
                }
            }else if(rule.ruleType=="custom"){
                if(rule.customName=="voteItemList"){
                    var sign2=false;
                    data.voteItemList.forEach(function(value, index, array){
                       console.log(value)
                        if(value.resType!=data.voteType){
                            sign2=true;
                        }
                    })
                    if(sign2){
                        if(data.voteType=='1'){
                            alertinfo("你所选择的投票活动非图文类");
                        }else if(data.voteType=='2'){
                            alertinfo("你所选择的投票活动非视频类");
                        }else if(data.voteType=='3'){
                            alertinfo("你所选择的投票活动非音频类");
                        }
                        sign=false;
                        break;
                    }
                }else if(rule.customName=="dependCardId"){
                    if(data.awardType&&data.awardType.indexOf("1")>-1){
                        if( !data.cardId){
                            alertinfo("请选择一张礼券");
                            sign=false;
                            break;
                        }
                    }

                }
            }
        }
        return sign;
    }

    var  _verifyData=function () {
        // 验证模块数据
        var sign=true;
        $(".m-app-main  .app-field","#"+opt.curVersion).each(function(i) {
            var tmpdata = $.extend({}, $(this).data());
            var _this=this;
            if(tmpdata.verifyArray){
                $(tmpdata.verifyArray).each(function (i) {
                    var tmp=_verifyFun(tmpdata,tmpdata.verifyArray[i]);
                    if(!tmp){
                        sign=tmp;
                        $(".control-mask",_this).click();
                        return false;
                    }
                })
            }
            if(!sign){
                return false;
            }
        })
        return sign;
    }
    return{
        init:_init,
        getjson:_getAllJson,
        getBaseInfo:_getBaseInfo,
        setCurVersion:_setCurVersion,
        getCurVersion:_getCurVersion,
        locationWindow:_locationWindow,
        verifyData:_verifyData
    }
})();