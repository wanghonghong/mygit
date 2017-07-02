/**
 * 商品配置
 */
CommonUtils.regNamespace("product", "config");

product.config=(function () {
    var GOODS_MANAGEMENT=0;  //商品管理
    var MALL_BUILDING=1;  //商城搭建
    var opt={
        pid:0,
        mainContainer:'goods-details',
        Project_Path:CONTEXT_PATH,
        getGoodsAjax:CONTEXT_PATH+"/good/",
        data:{ product:{curVersion : 'standard', detailJson:[]},groupRelationList:[]}
    }
    var _init=function (optsions) {
        opt.pid=optsions.pid || 0;
        opt.mainContainer='goods-details';
        opt.data= { product:{curVersion : 'standard', detailJson:[]},groupRelationList:[]};
        _getGoodsInfo();
    };
    var _getGoodsInfo=function () {
        var url=opt.getGoodsAjax;
        if(opt.pid){
            url=url+opt.pid
        }
        $.ajaxJsonGet(url,null,{
            "done":function (res) {
                $.extend(opt.data,res.data);
                initProductDetails();
                initGroupList().then(function () {
                    initProductBasic();
                }).then(function () {
                    _bindEvent();
                    if(opt.pid){
                        $("#shop_content").find(".m-step-bar-box li:eq(2)").click();
                    }
                })
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
    var initGroupList=function () {
        var dfdPlay = $.Deferred();
        var groupjson={
            groupRelationList:opt.data.groupRelationList,
            groupListJson:opt.data.groupList,
            ischeck:true
        }
        product.groups.init(groupjson);
        dfdPlay.resolve(); // 动画完成
        return dfdPlay;
    }
    var initProductBasic=function () {
        // 初始化页面元素
         product.basic.init(opt.data);
    }

    var initProductDetails=function () {
        // var dfdPlay = $.Deferred();
        // jumi.file('product/moduleTypes.json',function (moduleTypes) {
        //
        //     dfdPlay.resolve(); // 动画完成
        //
        // })
        // return dfdPlay;
        var data={
            curVersion:opt.data.product.curVersion
        }
        // data.curVersion=opt.data.product.curVersion;
        if(opt.data.product.detailJson && opt.data.product.detailJson.length>0){
            var   detailJson= $.parseJSON(opt.data.product.detailJson);
            data.versionList= detailJson.versionList;
        }else{
            data.versionList= [];
        }
        product.details.init(data);
    }

    var _bindEvent=function () {
        $("#shop_content").find(".m-step-bar-box li").click(function () {
            if($(this).hasClass("current-step")){
                return;
            }
          var oldstep=  $("#shop_content .m-step-bar-box .current-step");
           var sign=true;
           if( oldstep.attr("data-toggle")=='goodsGroups'){
               opt.data.groupList= product.groups.getGroupJson()
               opt.data.groupRelationList= product.groups.getGroupRelationList()
               product.basic.initGroupList(opt.data);
               // if(product.groups.validate()){
               //     sign=false;
               //     // $("#shop_content").find(".m-step-bar-box li:eq(0)").click();
               // }
           }else if( oldstep.attr("data-toggle")=='goodsBasic'){
               opt.data.groupRelationList= product.basic.getGroupRelationList();
               // if(product.basic.validate()){
               //     sign=false;
               //     // $("#shop_content").find(".m-step-bar-box li:eq(1)").click();
               // }
           }else if( oldstep.attr("data-toggle")=='goodsdetails'){
               // if(product.details.validate()){
               //     sign=false;
               //     // $("#shop_content").find(".m-step-bar-box li:eq(2)").click();
               // }
           }
           if(sign){
               $(this).addClass("current-step").siblings().removeClass('current-step');
               $('.m-step-panel').removeClass('z-sel');
               $( "#"+$(this).attr("data-toggle")).addClass("z-sel");
               if($(this).attr("data-toggle")=='goodsGroups'){
                   product.groups.selectGroup(opt.data);
               }else if($(this).attr("data-toggle")=='goodsBasic'){
                   product.basic.initGroupList(opt.data);
                   product.basic.mergeRow();
               }else if($(this).attr("data-toggle")=='goodsdetails'){
                   module.main.locationWindow();
               }
           }
        })

        $("#detailprevbtn").click(function () {
            //商品详情上一步
            $("#shop_content").find(".m-step-bar-box li:eq(1)").click();
        })
        $("#detailshelfbtn").click(function () {
             if(_validate(1)){
                 return;
             }
            //上架
            var data=getsaveData();
            data.product.status=0;
            if(opt.pid){
                data.product.pid=opt.pid;
                var url=opt.getGoodsAjax+opt.pid;
                var data = JSON.stringify(data);
                $.ajaxJsonPut(url,data,{
                    "done":function(res){
                        if(res.code==0){
                            var dm = new dialogMessage({
                                type:1,
                                fixed:true,
                                msg:'修改商品上架成功！',
                                isAutoDisplay:true,
                                time:3000,
                                onclose:function(){
                                    _toProductList();
                                }
                            });
                            dm.render();
                        }else{
                            var dm = new dialogMessage({
                                type:2,
                                fixed:true,
                                msg:'修改商品上架失败！',
                                isAutoDisplay:true,
                                time:3000
                            });
                            dm.render();
                        }
                    }
                });
            }else{
                delete data.delId;
                var url=opt.getGoodsAjax;
                var jsonStr = JSON.stringify(data)
                $.ajaxJson(url,jsonStr,{
                    "done":function(res){
                        if(res.code==0){
                            var dm = new dialogMessage({
                                type:1,
                                fixed:true,
                                msg:'添加商品上架成功！',
                                isAutoDisplay:true,
                                time:3000,
                                onclose:function(){
                                    _toProductList();
                                }
                            });
                            dm.render();
                        }else{
                            var dm = new dialogMessage({
                                type:2,
                                fixed:true,
                                msg:'添加商品上架失败！',
                                isAutoDisplay:true,
                                time:3000
                            });
                            dm.render();
                        }
                    }
                });
            }
        })
        $("#detailsavebtn").click(function () {
            if(_validate(0)){
                return;
            }
            var data=getsaveData();
            data.product.status=1;
            if(opt.pid){
                data.product.pid=opt.pid;
                var url=opt.getGoodsAjax+opt.pid;
                var data = JSON.stringify(data);
                $.ajaxJsonPut(url,data,{
                    "done":function(res){
                        if(res.code==0){
                            var dm = new dialogMessage({
                                type:1,
                                fixed:true,
                                msg:'修改商品保存成功！',
                                isAutoDisplay:true,
                                time:3000,
                                onclose:function(){
                                    _toProductList();
                                }
                            });
                            dm.render();
                        }else{
                            var dm = new dialogMessage({
                                type:2,
                                fixed:true,
                                msg:'修改商品保存失败！',
                                isAutoDisplay:true,
                                time:3000
                            });
                            dm.render();
                        }
                    }
                });
            }else{
                delete data.delId;
                var url=opt.getGoodsAjax;
                var data = JSON.stringify(data);
                $.ajaxJson(url,data,{
                    "done":function(res){
                        if(res.code==0){
                            var dm = new dialogMessage({
                                type:1,
                                fixed:true,
                                msg:'添加商品保存成功！',
                                isAutoDisplay:true,
                                time:3000,
                                onclose:function(){
                                    _toProductList();
                                }
                            });
                            dm.render();
                        }else{
                            var dm = new dialogMessage({
                                type:2,
                                fixed:true,
                                msg:'添加商品保存失败！',
                                isAutoDisplay:true,
                                time:3000
                            });
                            dm.render();
                        }
                    }
                });
            }
        })
        $("#basicPrevBtn").click(function () {
            $("#shop_content").find(".m-step-bar-box li:eq(0)").click();
        })
        $("#basicNextBtn").click(function () {
            $("#shop_content").find(".m-step-bar-box li:eq(2)").click();
        })
        //商品分组下一步按钮事件
        $("#groupNextBtn").click(function () {
            $("#shop_content").find(".m-step-bar-box li:eq(1)").click();
        })
    }
    var _validate=function (type) {
        var sign=false;
        if(product.groups.validate()){
            $("#shop_content").find(".m-step-bar-box li:eq(0)").click();
            return true;
        }
        if(product.basic.validate()){
            $("#shop_content").find(".m-step-bar-box li:eq(1)").click();
            return true;
        }
        if(!module.main.verifyData()){
            $("#shop_content").find(".m-step-bar-box li:eq(2)").click();
            return true;
        }
        if(type==0){  //保存 另外验证

        }else{   //上架 附加验证
          var goodstotalCount=  $("#goodstotalCount").val();
           if(goodstotalCount<=0) {
               alertinfo("请修改库存数量或暂存下架！");
               $("#shop_content").find(".m-step-bar-box li:eq(1)").click();
               return true;
           }
        }
    }

    var _toProductList=function () {
        $(".memu-panel .two-level-memu.z-sel").click();
    }
    var getsaveData=function () {
        var beseInfo=module.main.getBaseInfo();
        var product1=product.basic.getProduct();
        product1.detailJson=module.main.getjson();
        product1.curVersion=module.main.getCurVersion();
        product1.share=beseInfo.share;
        product1.likes=beseInfo.reward;;
        var redata=product.basic.getProductSpecList();
        var data ={
            product: product1,
            productSpecList:redata.productSpecList,
            groupRelationList:product.basic.getGroupRelationList(),
            productRoleList:product.basic.getProductRoleList(),
            delId:redata.delId
        }
        return data;
    }


    return {
        init:_init
    };

})();

