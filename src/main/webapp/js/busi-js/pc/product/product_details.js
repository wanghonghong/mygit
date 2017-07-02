/**
 * 商品配置
 */
CommonUtils.regNamespace("product", "details");
product.details=(function () {
    var GOODS_MANAGEMENT=0;  //商品管理
    var MALL_BUILDING=1;  //商城搭建
    var OFFICIAL_IMAGE=2;  //官方图文
    var opt={
        mainContainer:'goods-details',
        curVersion : 'standard',
        editPath:STATIC_URL+'/tpl/product/detail/edit/',
        pagetype:GOODS_MANAGEMENT,
        moduleTypes:[],
        versionList:[]
    }
    var _init=function (data) {
         $.extend(opt,data);
        opt.mainContainer='goods-details';
        opt.pagetype=GOODS_MANAGEMENT;
        _loadmoduleTypes().then(function () {
            _correctData();
        }).then(function () {
            module.main.init(opt);
        }).then(function () {
            _bindEvent();
        }).then(function () {
            module.main.locationWindow();
        })
    };
    var _loadmoduleTypes=function(){
        var dfdPlay = $.Deferred();
        // jumi.file('product/moduleTypes.json',function (moduleTypes) {
        //     opt.moduleTypes=moduleTypes.data;
        //     dfdPlay.resolve(); // 动画完成
        // })
        opt.moduleTypes=module.types.getTypesJson();
        dfdPlay.resolve(); // 动画完成
        return dfdPlay;
    }
    
    var _correctData=function () {
        var versionList=opt.versionList;
        if($.isEmptyObject(versionList)){
            var standardArray = {"versionName": "标准版", "versionId": "standard", "versionContent": []};
            var weixinArray = {"versionName": "仿微信版", "versionId": "weixin", "versionContent": []};
            //商品详情
            standardArray.versionContent.push({"type": "title"});
            standardArray.versionContent.push({"type": "richtext", "fixedTop": true});
            weixinArray.versionContent.push({"type": "title"});
            weixinArray.versionContent.push({"type": "weixintop"});
            weixinArray.versionContent.push({"type": "weixinbottom"});
            versionList.push(standardArray);
            versionList.push(weixinArray);
        }
        opt.versionList=versionList;
    }

    var _bindEvent=function () {
        $("#"+opt.mainContainer+" .details-hander").delegate(".u-rb label,label", "click", function () {
            $(".m-config-panel").hide();
            var togglepage = $(this).attr("data-toggle");
            $("#" + togglepage).show();
            opt.curVersion = $(this).attr("data-toggle");
            module.main.setCurVersion(opt.curVersion );
            $(" .app-config .app-field:first" ,"#"+opt.curVersion).each(function () {
               module.view.updataPageModular($(this));
            })
            var len=$(".m-app-config-region .editContent","#"+opt.curVersion).children().length;
            if(len==0){
                $(".app-config  .app-field:first .control-mask","#"+opt.curVersion).click();
            }
        });
    };
    return {
        init:_init
        // getjson:_getAllJson,
        // getBaseInfo:_getBaseInfo,
        // verifyData:_verifyData,
        // getshare:_getshare,
        // locationWindow:_locationWindow,
        // getCurVersion:_getCurVersion
        // validate:_validate,
        // getlikes:_getlikes
    };
})();

