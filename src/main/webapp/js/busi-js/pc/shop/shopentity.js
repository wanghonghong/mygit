/**
 * Created by wxz on 16/8/12
 * 实体门店
 */
CommonUtils.regNamespace("shop", "entitystore");
shop.entitystore = (function(){
    var pagesize = 10;
    var curpage = 0;
    /* var queryVo={};
     queryVo.curPage = curpage;
     queryVo.pageSize =pagesize;
     var jsonData =JSON.stringify(queryVo);*/
    var ajaxUrl = {
        url1:CONTEXT_PATH+'/shopEntity/findAll',//实体门店查询列表
        url2:CONTEXT_PATH+'/shopEntity/',//实体门店编辑预览
        url3:CONTEXT_PATH+'/shopEntity/is_entity/'//实体门店编辑预览
    };
    var _init = function(){

        _shopList();
        _shop_entity_del();


    };


    ///////所属地区start
    var chooseShopArea = function(){
    };
    chooseShopArea.prototype = {
        init:function(){
            this._renderArea();
        },
        _renderArea:function(){
            var addrval =$('#addresshid').text();
            var $distpicker = $('#distpickerentt');
            if(addrval!==''&&addrval!==null){
                var addrarr = addrval.split(',');
                $distpicker.distpicker({
                    province:addrarr[0],
                    city: addrarr[1],
                    district:addrarr[2],
                    autoSelect: false
                });
            }else{
                $distpicker.distpicker({
                    province:'',
                    city: '',
                    district:'',
                    autoSelect: false
                });
            }
        },
        _onBindClick:function(){


        },
        _offBindClick:function(){

        }
    };

    ///////所属地区end
    ///////新增对话start
    var _shop_entity_add=function(){

        $("#shop_entity_add").click(function() {

            jumi.template('shop/shop_entity_dialog',function(tpl){
                var dg = dialog({
                    title: '门店新增',
                    content: tpl,
                    width:1000,
                    id:'dialog_m',
                    onshow: function () {
                        _binddatetime();
                        ///////门店照片
                        _shop_entity_photo();
                        ///////所属地区
                        var chooseShopAreaobj = new chooseShopArea();
                        chooseShopAreaobj.init();
                        ///////地图初始化并定位
                        initMapConverseReAddress("allmap",11,"pointll");
                        ///////文本失焦定位地图
                        _shop_addr_search();
                    },
                    cancelValue: '关闭',
                    cancel: function () {
                        _shop_page_bar();
                    },
                    okValue: '保存',
                    ok: function() {
                        this.title('提交中…');
                        var form = _validate();
                        if (form.valid()) {
                            _shop_item_add();
                        } else {
                            $('body').animate({scrollTop:0},1000);
                        }
                        return false;
                    },
                    onclose: function () {
                        // _shop_page_barupdate();//保持当前页
                        _shop_clear_it();
                        dialog.get('dialog_m').close().remove();
                    },
                    onremove: function () {
                        //jumi.msg('对话框已销毁');
                    }

                });
                dg.showModal();
            })
        });

    }
    ///////新增对话end


    ///////删除start
    var _shop_entity_del=function(){
        $("div[id^='shopendel']").click(function() {
            var itemid= $(this).attr("id").replace("shopendel","");

            var msg ='<div class="m-info-box" ><p class="color: #7f7f7f;"> 确定删除当前记录？</p></div>';
            dialog({
                title:'操作提醒',
                content: msg,
                cancelValue: '取消',
                cancel: function () {
                },
                okValue: '&nbsp;确定&nbsp;',
                ok: function() {
                    this.title('提交中…');
                    $.ajaxJsonDel(ajaxUrl.url2+itemid,{
                        "done":function (res) {
                            // _shop_page_bar();
                            _shop_page_barupdate();
                        },
                        "fail":function (res) {
                        }
                    });
                    return true;
                }
            }).width(400).show();

        });
    }
    ///////删除end


    var _shopList =function(){
        ///////编辑对话start
        $("div[id^='shopenupdate']").click(function() {
            var itemid= $(this).attr("id").replace("shopenupdate","");

            $.ajaxJson(ajaxUrl.url2+itemid,{
                "done":function (res) {
                    if(!isNull(res.data.storeImg)){
                        res.data.storeImg=CONTEXT_PATH+"/css/pc/img/no_picture.png";
                    }
                    jumi.template('shop/shop_entity_dialog',res.data,function(tpl){
                        var dg = dialog({
                            title: '门店编辑',
                            content: tpl,
                            width:1000,
                            id:'dialog_m',
                            onshow: function () {
                                _binddatetime();
                                ///////门店照片
                                _shop_entity_photo();
                                ///////所属地区
                                var chooseShopAreaobj = new chooseShopArea();
                                chooseShopAreaobj.init();
                                ///地图初始化并定位start
                                initMapConverseReAddress("allmap",11,"pointll");
                                var lnglat =$('#defaultlnglat').text();
                                var lnglatarr = lnglat.split(',');
                                if(lnglatarr[0]!==''&&lnglatarr[1]!==''){
                                    theLocationBylnglat(13,lnglatarr[0],lnglatarr[1],"pointll");
                                }
                                ///文本失焦定位地图
                                _shop_addr_search();
                            },
                            cancelValue: '关闭',
                            cancel: function () {
                              //  _shop_page_bar();
                                _shop_clear_it();

                            },
                            okValue: '保存',
                            ok: function() {
                                this.title('提交中…');

                                var form = _validate();
                                if (form.valid()) {
                                    _shopitemedit(itemid);
                                } else {
                                    $('body').animate({scrollTop:0},1000);
                                }

                                return false;
                            },
                            onclose: function () {
                                _shop_clear_it();
                                dialog.get('dialog_m').close().remove();
                            },
                            onremove: function () {
                                // jumi.msg('对话框已销毁');
                            }
                        });
                        dg.showModal();
                    })
                    //var elem = $('#dialoginfo');
                },
                "fail":function (res) {

                }
            });

        });
        ///////编辑对话end

        ///////预览对话start
        $("div[id^='shopview']").click(function() {

            var itemid= $(this).attr("id").replace("shopview","");

            $.ajaxJson(ajaxUrl.url2+itemid,{
                "done":function (res) {
                    if(!isNull(res.data.storeImg)){
                        res.data.storeImg=CONTEXT_PATH+"/css/pc/img/no_picture.png";
                    }
                    jumi.template('shop/shop_entity_dialog',res.data,function(tpl){
                        var dg = dialog({
                            title: '门店预览',
                            content: tpl,
                            width:1000,
                            id:'dialog_m',
                            onshow: function () {
                                ///////门店照片start
                                _shop_entity_photo();
                                ///////门店照片end
                                ///////所属地区start
                                var chooseShopAreaobj = new chooseShopArea();
                                chooseShopAreaobj.init();
                                ///////所属地区end
                                ///地图初始化并定位start
                                initMapConverseReAddress("allmap",11,"pointll");
                                var lnglat =$('#defaultlnglat').text();
                                var lnglatarr = lnglat.split(',');
                                if(lnglatarr[0]!==''&&lnglatarr[1]!==''){

                                    theLocationBylnglat(13,lnglatarr[0],lnglatarr[1],"pointll");
                                }
                                ///地图初始化并定位end
                            },
                            cancelValue: '关闭',
                            cancel: function () {

                            },
                            onclose: function () {
                                _shop_clear_it();
                                dialog.get('dialog_m').close().remove();
                            },
                            onremove: function () {
                                jumi.msg('对话框已销毁');
                            }
                        });
                        dg.showModal();
                    })
                },
                "fail":function (res) {

                }
            });
        });
        ///////预览对话end
    };

    var _validate = function(){
        var form = $("#shop_Entity_Form");
        form.validate();//验证指定的表单

        return form;
    };

    var _shop_item_add=function(){
        var pointll = $("#pointll").text();
        var arrll = pointll.split(",");

        var shop_dataVo = {};
        shop_dataVo.storeName=$("#storeName").val();
        shop_dataVo.phoneNumber=$("#phoneNumberentity").val();
        shop_dataVo.provinceCode=$("#provinceentt").find("option:selected").attr('data-code');
        shop_dataVo.cityCode=$("#cityentt").find("option:selected").attr('data-code');
        shop_dataVo.districtCode=$("#districtentt").find("option:selected").attr('data-code');
        shop_dataVo.province=$("#provinceentt").val();
        shop_dataVo.city=$("#cityentt").val();
        shop_dataVo.district=$("#districtentt").val();
        shop_dataVo.address=$("#address").val();
        shop_dataVo.officeHoursStart=$("#officeHoursStart").val();
        shop_dataVo.officeHoursEnd=$("#officeHoursEnd").val();
        shop_dataVo.storeImg=$("#shopentit_img").attr("src");

        shop_dataVo.longitude=arrll[0];   //经度
        shop_dataVo.latitude=arrll[1];//纬度

        $.ajaxJson(ajaxUrl.url2,shop_dataVo,{
            "done":function (res) {
                if(res.code===0){
                    var msg = '<div class="m-err-box" ><img class="floatleft" src="'+CONTEXT_PATH+'/css/pc/img/icon_success.png" /><span class="jm-font-yahei-lg">恭喜您，操作成功 </span><span></span></div>';
                    var childrend = dialog({
                        title: '操作提醒',
                        content: msg,
                        id:'dialog_makesure',
                        width:400,
                        cancelValue: '取消',
                        cancel: function () {
                        },
                        okValue: '&nbsp;确定&nbsp;',
                        ok: function () {
                            dialog.get('dialog_m').close().remove();
                            _shop_page_barupdate();
                        }

                    });
                    childrend.showModal();
                }
            },
            "fail":function (res) {
            }
        });
    };


    ///////编辑对话end
    var _shopitemedit = function(itemid){
        var shop_dataVo = {};
        shop_dataVo.storeName=$("#storeName").val();
        shop_dataVo.phoneNumber=$("#phoneNumberentity").val();
        shop_dataVo.provinceCode=$("#provinceentt").find("option:selected").attr('data-code');
        shop_dataVo.cityCode=$("#cityentt").find("option:selected").attr('data-code');
        shop_dataVo.districtCode=$("#districtentt").find("option:selected").attr('data-code');
        shop_dataVo.province=$("#provinceentt").val();
        shop_dataVo.city=$("#cityentt").val();
        shop_dataVo.district=$("#districtentt").val();
        shop_dataVo.address=$("#address").val();
        shop_dataVo.officeHoursStart=$("#officeHoursStart").val();
        shop_dataVo.officeHoursEnd=$("#officeHoursEnd").val();
        shop_dataVo.storeImg=$("#shopentit_img").attr("src");
        var arrll = $("#pointll").text().split(',');
        shop_dataVo.longitude=arrll[0];   //经度
        shop_dataVo.latitude=arrll[1];//纬度

        $.ajaxJsonPut(ajaxUrl.url2+itemid,shop_dataVo,{
            "done":function (res) {
                if(res.code===0){
                    var msg = '<div class="m-err-box" ><img class="floatleft" src="'+CONTEXT_PATH+'/css/pc/img/icon_success.png" /><span class="jm-font-yahei-lg">恭喜您，操作成功 </span><span></span></div>';
                    var childrend = dialog({
                        title: '操作提醒',
                        content: msg,
                        id:'dialog_makesure',
                        width:400,
                        cancelValue: '取消',
                        cancel: function () {
                        },
                        okValue: '&nbsp;确定&nbsp;',
                        ok: function () {
                            dialog.get('dialog_m').close().remove();
                            _shop_page_barupdate();
                        }

                    });
                    childrend.showModal();
                }
            },
            "fail":function (res) {

            }
        });

    };
///////门店照片start
    var _shop_entity_photo=function(){
        $('#shopentit_xy').click(function(){
            var d = new Dialog({
                context_path:CONTEXT_PATH, //请求路径,  必填
                resType:1 ,//图片1，视频2，语音3   必填
                callback:function(url){
                    $('#shopentit_img').attr('src',url);
                }
            });
            d.render();
        });
    }
///////门店照片end


    var _shop_clear_it=function(){
        $("#storeName").val("");
        $("#phoneNumberentity").val("");
        $("#provinceentt").find("option:selected").attr('data-code',"");
        $("#cityentt").find("option:selected").attr('data-code',"");
        $("#districtentt").find("option:selected").attr('data-code',"");
        $("#provinceentt").val("");
        $("#cityentt").val("");
        $("#districtentt").val("");
        $("#address").val("");
        $("#officeHoursStart").val("");
        $("#officeHoursEnd").val("");
        $("#shopentit_img").attr("src","");
        $("#pointll").text("");

    };

///时间控件
    var _binddatetime = function () {

        $("#officeHoursStart").timepicker({
            timeOnly:true,
            showSecond : false,
            timeFormat : 'hh:mm',
            dateFormat : 'yy-mm-dd',
            stepHour : 1,
            stepMinute : 1,
            stepSecond : 1
        });
        $("#officeHoursEnd").timepicker({
            timeOnly:true,
            showSecond : false,
            timeFormat : 'hh:mm',
            dateFormat : 'yy-mm-dd',
            stepHour : 1,
            stepMinute : 1,
            stepSecond : 1
        });
    }

////实体门店分页

    var _shop_page_bar=function(){
        var queryVo = {
            pageSize:10
        };

        jumi.pagination('#pageToolbar',ajaxUrl.url1,queryVo,function(res,curPage){

            if(res.code===0){
                //判断是否第一页
                var data = {
                    items:res.data.items,
                    isentity:res.data.isEntity
                };
                if(curPage===0){
                    data.isFirstPage = 1;
                }else{
                    data.isFirstPage = 0;
                }

                if(data.isentity===0) {
                    $("#isEntity1").prop('checked','checked');
                }
                else {
                    $("#isEntity2").prop('checked','checked');
                }
                for(var i=0;i<data.items.length;i++){
                    if(!isNull(data.items[i].storeImg)){
                        data.items[i].storeImg=CONTEXT_PATH+"/css/pc/img/no_picture.png";
                    }
                }


                jumi.template('shop/shop_entity_listnew',data,function(tpl){
                    $('#table-body').empty();
                    $('#table-body').html(tpl);
                })
            }
        })
    };

    var _shop_page_barupdate = function () {
        var curPage = $("#pageToolbar_page").val();

        var queryVo = {
            pageSize:10,
            curPage:curPage||0
        };
        $.ajaxJson(ajaxUrl.url1,queryVo,{
            "done":function(res){
                if(res.code===0){
                    var data = {
                        items:res.data.items
                    }
                    if(data.isentity===0) {
                        $("#isEntity1").prop('checked','checked');
                    }
                    else {
                        $("#isEntity2").prop('checked','checked');
                    }
                    for(var i=0;i<data.items.length;i++){
                        if(!isNull(data.items[i].storeImg)){
                            data.items[i].storeImg=CONTEXT_PATH+"/css/pc/img/no_picture.png";
                        }
                    }

                    jumi.template('shop/shop_entity_listnew',data,function(tpl){
                        $('#table-body').empty();
                        $('#table-body').html(tpl);
                    })
                }
            }
        })
    };

    ////实体门店分页
    ////失去焦点定位地图位置start
    var _shop_addr_search=function () {
        $('#address').blur(function (){
            var province =$("#provinceentt").val();
            var city =$("#cityentt").val();
            var district=$("#districtentt").val()
            var address=$("#address").val();
            var pcdaddress =  province+city+district+address;
            initMapReAddress("allmap",11,pcdaddress,city,"pointll");
        });
    };
    //暂无实体门店
    var _shop_ishas=function(){
    //    $("#isEntity").click(function () {
        $("#entitysh_show").find("input[type='radio']").bind('click',function(){
            var num;
            var isstatus= $(this).prop('checked');
            if(isstatus){  //1有 0无
                num=$(this).val();

            }

            $.ajaxJson(ajaxUrl.url3+num,{
                "done":function (res) {
                    if(res.data.code===0){
                        var dm = new dialogMessage({
                            type:1,
                            title:'操作提醒',
                            fixed:true,
                            msg:'恭喜您，操作成功',
                            isAutoDisplay:false

                        });
                        dm.render();
                    }
                    if(res.data.code===1){
                        var dm = new dialogMessage({
                            type:2,
                            title:'操作提醒',
                            fixed:true,
                            msg:'操作失败',
                            isAutoDisplay:false
                        });
                        dm.render();
                    }
                },
                "fail":function (res) {

                }
            });
        });
    }
    function isNull(data) {
        return (data == "" || data == undefined || data == null) ? false : true;
    };
    return {
        init :_init,
        shop_page_bar:_shop_page_bar,
        shop_entity_add: _shop_entity_add,//新增
        shop_ishas:_shop_ishas

    };
})();

