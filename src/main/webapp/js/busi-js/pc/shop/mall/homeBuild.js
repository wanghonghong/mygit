CommonUtils.regNamespace("mall", "homeBuild");

mall.homeBuild=(function () {
    var GOODS_MANAGEMENT=0;  //商品管理
    var MALL_BUILDING=1;  //商城搭建
    var opt={
        shopSetAjax:CONTEXT_PATH +"/shopsetting/shopIndex",
        detailJson:[],
        moduleTypes:[],
        mainContainer:'goods-details',
        pagetype:MALL_BUILDING,
        curVersion:"standard",
        id:0
    }
    var _init=function(){
        _loadShopSet().then(function(){
           _loadmoduleTypes();
        }).then(function () {
            _correctData();
        }).then(function () {
            module.main.init(opt);
        }).then(function () {
            _bindEvent();
        }).then(function () {
            module.main.locationWindow();
        })
    }

    var _loadShopSet=function () {
        var dfdPlay = $.Deferred();
        var url=opt.shopSetAjax;
        $.ajaxJsonGet(url,null,{
            "done":function(res){
                if(res.code==0){
                    opt.curVersion=res.data.curVersion||"standard";
                    opt.detailJson=res.data.detailJson||[];
                    opt.id=res.data.id;
                    $("#shopname").val(res.data.shopName)
                }
                dfdPlay.resolve(); // 动画完成
            }
        })
        return dfdPlay;
    }
    var _loadmoduleTypes=function(){
        // var dfdPlay = $.Deferred();
        // jumi.file('product/moduleTypes.json',function (moduleTypes) {
        //     opt.moduleTypes=moduleTypes.data;
        //     dfdPlay.resolve(); // 动画完成
        // })
        // return dfdPlay;
        opt.moduleTypes=module.types.getTypesJson();
    }
    //修正数据
    var _correctData=function () {
        if($.isEmptyObject( opt.detailJson)){
            opt.detailJson=[];
            var versionList=[];
            var standardArray = {"versionName": "标准版", "versionId": "standard", "versionContent":[]};
            standardArray.versionContent.push({"type": "title", "fixedTop": true});
            standardArray.versionContent.push({"type": "imageAd", "fixedTop": true});
            standardArray.versionContent.push({"type": "search", "fixedTop": true});
            versionList.push(standardArray);
            opt.detailJson.versionList=versionList;
        }else{
            opt.detailJson=$.parseJSON(opt.detailJson);
        }
        if($.isEmptyObject(opt.detailJson.versionList)){
            var versionList=[];
            var standardArray = {"versionName": "标准版", "versionId": "standard", "versionContent":opt.detailJson};
            versionList.push(standardArray);
            opt.versionList=versionList;
        }else{
            opt.versionList=opt.detailJson.versionList;
        }
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
        $("#savedetailbtn").click(function(){
            _saveetail();
        })
    }
    var _saveetail=function(){
        var url=opt.shopSetAjax;
        if(module.main.getCurVersion()=='h5version'){
            var dm = new dialogMessage({
                type:3,
                msg:"h5版正在开发中。。。。",
                isAutoDisplay:true,
                time:1500
            });
            dm.render();
            return;
        }
        var baseinfo= module.main.getBaseInfo();
        console.log(baseinfo);
        var data={
            detailJson: module.main.getjson(),
            curVersion:module.main.getCurVersion(),
            share:baseinfo.share
        }
        if(opt.id){
            data.id=opt.id;
            data = JSON.stringify(data);
            $.ajaxJsonPut(url,data,{
                "done":function (res) {
                    if(res.code==0){
                       var dm = new dialogMessage({
                           type:3,
                           msg:"设置成功",
                           isAutoDisplay:true,
                           time:1500
                       });
                       dm.render();
                   }else{
                       var dm = new dialogMessage({
                           type:3,
                           msg:"设置失败",
                           isAutoDisplay:true,
                           time:1500
                       });
                       dm.render();
                   }
                }
            })
        }else{
            data = JSON.stringify(data);
            $.ajaxJson(url,data,{
                "done":function (res) {
                    if(res.code==0){
                        opt.id= res.data.id;
                        var dm = new dialogMessage({
                            type:3,
                            msg:"设置成功",
                            isAutoDisplay:true,
                            time:1500
                        });
                        dm.render();
                    }else{
                        var dm = new dialogMessage({
                            type:3,
                            msg:"设置失败",
                            isAutoDisplay:true,
                            time:1500
                        });
                        dm.render();
                    }
                }
            })

        }


    }
    return {
        init:_init
    }
})();