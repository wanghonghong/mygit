CommonUtils.regNamespace("product","area");

product.area=(function(){
    var pageSize=10;
    var curPage=0;
    var ajaxUrl = {
        url1:CONTEXT_PATH+"/good/offer_role",//获取供货角色
        url2:CONTEXT_PATH+"/good/area_producs",//获取地区供货配置商品
        url3:CONTEXT_PATH+"/good/product_plant_offer",//设置供货属性
    }
    var _init=function () {
        _bindEvent();
        _initPagination();
    }

    var _edit=function (pid) {
        var  url=ajaxUrl.url1;
        $.ajaxJsonGet(url,null,{
            "done":function (res) {
                if(res.code===0){
                    console.log(res.data);
                    var offerType=$("#offerTyle_"+pid).val();
                    var data={
                        offerRoles:res.data,
                        pid:pid,
                        offerType:offerType
                    }
                    var html = jumi.templateHtml("/tpl/product/area/product_area_dialog.html",data);
                    dialog({
                        content: html,
                        title: '地区展销及供应商',
                    }).width(700).height(680).showModal();
                }
            }
        });
    }

    var _bindEvent = function () {
        common_area.init();
        $("#provincebset").select2({
            theme: "jumi"
        });
        $("#citybset").select2({
            theme: "jumi"
        });
        $("#districtbset").select2({
            theme: "jumi"
        });
        $("#product_offer_type").select2({
            theme: "jumi"
        });
        $(".btn-slide").click(function(){
            $("#m-search").slideToggle("fast");
            $(this).toggleClass("btn-slide1");
            return false;
        })
    }
    var _initPagination = function(){
        var url = ajaxUrl.url2;
        var productQo = {};
        productQo.pageSize=pageSize;
        productQo.curPage=curPage;
        productQo.name = $("#name").val();
        productQo.phoneNumber= $("#phoneNumber").val();
        productQo.offerType= $("#product_offer_type").val();
        var provincebset = $("#provincebset").find("option:selected").attr('data-code');
        var citybset = $("#citybset").find("option:selected").attr('data-code');
        var districtbset = $("#districtbset").find("option:selected").attr('data-code');
        if(districtbset){
            productQo.areaCode=districtbset;
        }else if(citybset){
            productQo.areaCode=citybset;
        }else if(provincebset){
            productQo.areaCode=provincebset;
        }
        var jsonStr = JSON.stringify(productQo);
        jumi.pagination("#pageToolbar",url,productQo,function (res,curPage) {
            if(res.code===0){
                //判断是否第一页
                var data = {
                    items:res.data.items,
                };
                if(curPage===0){
                    data.isFirstPage = 1;
                }else{
                    data.isFirstPage = 0;
                }
                curPage=curPage;
                jumi.template("product/area/product_area_item",data,function(tpl){
                    $("#tableBody").empty();
                    $("#tableBody").html(tpl);
                })
            }
        })
    }
    
    var _cancel=function (pid,offerType) {
        var d = dialog({
            title: '提示',
            content: "是否确认取消指定",
            width: 300,
            okValue: '确定',
            ok: function () {
                var url=ajaxUrl.url3+"/"+pid+"/"+offerType;
                $.ajaxJsonPut(url,null,{
                    "done":function (res) {
                        if(res.code==0){
                            var dm = new dialogMessage({
                                type:1,
                                fixed:true,
                                msg:'操作成功',
                                isAutoDisplay:true,
                                time:1500
                            });
                            dm.render();
                        }else{
                            var dm = new dialogMessage({
                                type:2,
                                fixed:true,
                                msg:'操作失败',
                                isAutoDisplay:true,
                                time:1500
                            });
                            dm.render();
                        }
                        _initPagination();
                    }
                })
            },
            cancelValue: '取消',
            cancel: function () {
            }
        });
        d.show();

    }
    return {
        init:_init,//列表初始化
        search:_initPagination,
        edit:_edit,//编辑弹窗初始化
        cancel:_cancel//取消指定
    };
})();
