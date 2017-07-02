/**
 * 商品管理
 * @class product
 * @author zww
 * @date 2016/7/1
 */
CommonUtils.regNamespace("product","list");
product.list = (function(){
    var tabindex=0;
    var pageparam = [{
        url: CONTEXT_PATH+"/good/productList/0",//上架商品
        pageSize: 10,
        curPage: 0,
        countObj: "count",
        pageToolbarObj: "pageToolbar",
        tableBodyObj: "tableBody1",
        template:"product/productOnSale"
    },{
        url: CONTEXT_PATH+"/good/productList/1",//手动下架
        pageSize:10,
        curPage: 0,
        countObj: "count",
        pageToolbarObj: "pageToolbar",
        tableBodyObj: "tableBody2",
        template:"product/productHandOff"
    }, {
        url: CONTEXT_PATH+"/good/productList/2",//售完下架
        pageSize: 10,
        curPage: 0,
        countObj: "count",
        pageToolbarObj: "pageToolbar",
        tableBodyObj: "tableBody3",
        template:"product/productSoldOff"
    }, {
        url: CONTEXT_PATH+"/good/productList/4",//全部商品
        pageSize: 10,
        curPage: 0,
        countObj: "count",
        pageToolbarObj: "pageToolbar",
        tableBodyObj: "tableBody4",
        template:"product/productAllGoods"
    }];

    var _queryProductGroup = function(){
        var url = CONTEXT_PATH+"/product/group";
        $.ajax({
            url:url,
            method:"GET",
            success:function(res){
                console.log(res);
                $.each(res,function(i,group){
                    var html = "<option value='"+group.groupId+"'>";
                    html+=group.groupName+"</option>";
                    $("#groupId").append(html);
                });
                $("#groupId").select2({
                    theme: "jumi"
                });
            }
        })
    }

    //初始化数据
    var _init = function(){
        _queryProductGroup();
        _band();
        _initPagination(pageparam[0]);
        _locationUrl();

    };

    var _locationUrl = function(){
        var url =CONTEXT_PATH+"/good/locationUrl"
        var res = $.ajaxJsonGet(url);
        return res.data;
    }
    var _band = function(){
        //tab切换

        $("#startDate").datetimepicker({ timeFormat:'hh:mm:ss'  });
        $("#endDate").datetimepicker({ timeFormat:'hh:mm:ss'  });

        $("#productList").on("click",".tab li",function(){
            $(this).addClass("active").siblings().removeClass("active");
            var target = $(this).attr("data-target");

            $("#"+target).addClass("active").siblings().removeClass("active");
        });
        $("#productList").on("click",".tab .goodstab",function(){
            tabindex=$(this).index();
            //切换分页
            _initPagination(pageparam[tabindex]);
        });

        $("#searchBtn").on("click",function(){
            _initPagination(pageparam[tabindex]);
        });
    }

    var _initPagination = function(pageparam){
        var productQo = {};
        productQo.pageSize=pageparam.pageSize;
        productQo.curPage=pageparam.curPage;
        productQo.name = $("#name").val();
        productQo.groupId= $("#groupId").val();
        productQo.startDate=$("#startDate").val();
        productQo.endDate=$("#endDate").val();
        productQo.minPrice=$("#minPrice").val()*100;
        productQo.maxPrice=$("#maxPrice").val()*100;
        var jsonStr = JSON.stringify(productQo);
        jumi.pagination("#"+pageparam.pageToolbarObj,pageparam.url,productQo,function (res,curPage) {
            if(res.code===0){
                //判断是否第一页
                var  location= _locationUrl();
                var data = {
                    items:res.data.items,
                    locationUrl:location.locationUrl,
                    shopId:location.shopId
                };
                console.log(curPage);
                if(curPage===0){
                    data.isFirstPage = 1;
                }else{
                    data.isFirstPage = 0;
                }
                pageparam.curPage=curPage;
                jumi.template(pageparam.template,data,function(tpl){
                    $("#"+pageparam.tableBodyObj).empty();
                    $("#"+pageparam.tableBodyObj).html(tpl);
                    _copyLink();
                })
            }
        })
    }
    //查询
    var _query = function(){

    };
    var _getQrcode = function(code){
        var pid=$(code).attr('data');
        var url= CONTEXT_PATH +"/good/qrcode/"+pid;
        $.ajaxJson(url,"",{
            "done":function(res){
                if(res.data.code==0){
                    $(code).text('查看二维码');
                    _showQrcode(res.data.msg);
                }
            }
        });
    }
    var _showQrcode = function(imgurl){
        $("#qrcode").attr("src","");
        $("#qrcode").attr("src",imgurl);
        _qrcodeDialog("商品二维码");
    }

    var _qrcodeDialog = function (title){
        var elem = $('#dialoginfo2');
        memudialog=dialog({
            id:'show-dialog',
            width: 350,
            height: 350,
            title: title,
            content: elem
        });
        memudialog.show();
    };
    var _updateStatus = function(code,status,cont){
        var ids=_pids(code);
        var url = CONTEXT_PATH + "/good/status"
        var productStatus={};
        productStatus.ids=ids;
        productStatus.status=status;
        var jsonStr = JSON.stringify(productStatus);
        var d = dialog({
            title: '提示',
            content: cont,
            width: 300,
            okValue: '确定',
            ok: function () {
                $.ajaxJsonPut(url,jsonStr,{
                    "done":function(res){
                        var dm = new dialogMessage({
                            type:1,
                            fixed:true,
                            msg:'操作成功',
                            isAutoDisplay:true,
                            time:1500
                        });
                        dm.render();
                        _initPagination(pageparam[tabindex]);
                    }
                });
            },
            cancelValue: '取消',
            cancel: function () {
            }
        });
        d.show();
    }
    //获取商品ids参数
    var _pids = function (code) {
        var pids = "";
        $("input[name='pid_check" + code + "']").each(function () {
            if ($(this).is(":checked")) {
                pids += $(this).val() + ",";
            };
        });
        return pids;
    }



    var _oneStatus = function(pid,status,cont){
        var id=pid;
        var url = CONTEXT_PATH + "/good/status"
        var productStatus={};
        productStatus.ids = id;
        productStatus.status=status;
        var jsonStr = JSON.stringify(productStatus);
        var d = dialog({
            title: '提示',
            content: cont,
            width: 300,
            okValue: '确定',
            ok: function () {
                $.ajaxJsonPut(url,jsonStr,{
                    "done":function(res){
                        var dm = new dialogMessage({
                            type:1,
                            fixed:true,
                            msg:'操作成功',
                            isAutoDisplay:true,
                            time:1500
                        });
                        dm.render();
                        _initPagination(pageparam[tabindex]);
                    }
                });
            },
            cancelValue: '取消',
            cancel: function () {
            }
        });
        d.show();
    }
    _copy = function(pid) {
        var url = CONTEXT_PATH + "/good/copy/" + pid;
        var res = $.ajaxJsonPut(url);
        console.log(res);
        if (res.code == 0) {
            var dm = new dialogMessage({
                type: 1,
                fixed: true,
                msg: '复制商品成功',
                isAutoDisplay: true,
                time: 1500
            });
            dm.render();
            _initPagination(pageparam[tabindex]);
        } else {
            var dm = new dialogMessage({
                type: 2,
                fixed: true,
                msg: '复制商品失败',
                isAutoDisplay: true,
                time: 1500
            });
            dm.render();
            _initPagination(pageparam[tabindex]);
        }
    }
    var _copyLink = function (){
        var btns = document.querySelectorAll('button');
        var clipboard = new Clipboard(btns);
        clipboard.on('success', function(e) {
            console.log(e);
            var dm = new dialogMessage({
                type: 1,
                fixed: true,
                msg: '该商品链接地址复制成功：）',
                isAutoDisplay: true,
                time: 1500
            });
            dm.render();
        });
        clipboard.on('error', function(e) {
            console.log(e);
        });
    }

    var _sort = function(pid,newSort,sort) {
        if(newSort==sort){
            return;
        }
        if(newSort==null || newSort=="" || newSort==undefined){
            var dm = new dialogMessage({
                type: 1,
                fixed: true,
                msg: '排序不能为空',
                isAutoDisplay: true,
                time: 1500
            });
            dm.render();
            return;
        }
        var productSortUo = {};
        productSortUo.pid = pid;
        productSortUo.sort = newSort;
        var url = CONTEXT_PATH + "/good/sort";
        var res = $.ajaxJsonPut(url, productSortUo);
        if (res.data.code == 0) {
            var dm = new dialogMessage({
                type: 1,
                fixed: true,
                msg: '修改成功',
                isAutoDisplay: true,
                time: 1500
            });
            dm.render();
            _initPagination(pageparam[tabindex]);
        } else {
            var dm = new dialogMessage({
                type: 2,
                fixed: true,
                msg: '修改失败',
                isAutoDisplay: true,
                time: 1500
            });
            dm.render();
            _initPagination(pageparam[tabindex]);
        }
    }

    var _toProductEdit =  function(pid){
        shop.cleanData();
        var data={
            pid:pid
        }
       var  tpl = "product/product_config"
        jumi.template(tpl,data,function(html){
            $("#shop_content").html(html);
        })
    }

    // var _toProductGroup=function (status,itmp) {
    //     $(".addgoods-btn").removeClass("active");
    //     itmp.addClass("active");
    //     $("#contentBox").empty();
    //     var  tpl = "product/group/product_groups";
    //     jumi.template(tpl,function(html){
    //         $("#contentBox").html(html);
    //         product.groups.init([]);
    //     })
    // }

    var _toTransTemplates=function () {
      /*  $(".addgoods-btn").removeClass("active");
        itmp.addClass("active");*/
        $("#contentBox").empty();
        var url=CONTEXT_PATH+'/get_trans_templates_list';
       $.ajaxHtmlGet(url,null,{
           "done":function(res){
               if(res.code==0){
                   $("#contentBox").html(res.data);
               }
           }
       })
    }
    var _allcheck=function (el,tableBody) {
        var tmpi= $(".checkbox",el);
        var $tableBody=$("#"+tableBody);
        if(tmpi.hasClass("icon-checkbox")){
            $tableBody.find("input:checkbox").each(function () {
                var _this=$(this);
                if(!_this.is(":checked")){
                    _this.click();
                }
            })
        }else{
            $tableBody.find("input:checkbox").each(function () {
                var _this=$(this);
                if(_this.is(":checked")){
                    _this.click();
                }
            })
        }
        tmpi.toggleClass("icon-checkbox icon-checkboxactive");
    }

    return {
        init :_init, //初始化
        band:_band,  //绑定
        query:_query,//条件查询
        initPagination:_initPagination, //分页查询列表
        updateStatus:_updateStatus,//批量修改状态，（批量上下架、批量删除）
        oneStatus:_oneStatus,//单个商品修改状态，（批量上下架、批量删除）
        getQrcode:_getQrcode,//生成二维码
        showQrcode:_showQrcode,//查看二维码
        qrcodeDialog:_qrcodeDialog,//二维码弹出框
        copy:_copy,//复制商品
        copyLink:_copyLink,//复制链接
        sort:_sort,//修改顺序
        //toProductGroup:_toProductGroup,//跳转到商品分组页面
        toProductEdit:_toProductEdit,//跳转到修改商品页面
        toTransTemplates:_toTransTemplates,//跳转到运费模板列表页面
        locationUrl:_locationUrl,  //获取全地址
        allcheck:_allcheck
    };
})();