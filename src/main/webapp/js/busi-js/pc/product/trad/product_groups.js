/**
 * 拼团
 */

CommonUtils.regNamespace("trad","groups");

trad.groups=(function(){
    var pageSize=10;
    var curPage=0;
    var ajaxUrl = {
        url1:CONTEXT_PATH+"/product/trad",//增删改查
        url2:CONTEXT_PATH+"/product/trad/trad_products",//活动商品列表查询
        url3:CONTEXT_PATH+"/product/trad/status"//删除和启动
    }
    var _init=function () {
        _bindEvent();
        _initPagination();
    }

    /**
     * 验证信息
     */
    var _validateset = function(){
        $('#trad_set_mofify_form').validate({
            rules: {
                action_name:{
                    required: true
                },
                product_price:{
                    required: true,
                    isPlusTwo:true
                }
            },
            messages:{
                action_name:{
                    required: "请输入活动名称"
                },
                product_price:{
                    required: "请输入商品拼团价",
                    isPlusTwo:"输入金额必须保留两位小数"
                }
            }
        })
    }
    /**
     * 获取活动
     * @private
     */
    var _getAction = function (args) {
        $.ajaxJsonGet(ajaxUrl.url1,{
            "done":function (res) {
                if(res.code===0){
                    var data = res.data;
                    data.sign = args.sign;
                    _getImgType(data,2,args);
                }
            },
            "fail":function () {

            }
        });
    }
    /**
     * 活动分类
     */
    var _getImgType = function (json,type,args) {
        var t = Number(type),isHasCustomType;
        var jsonData = _.where(json,{type:t});
        var imgType =  _.where(json,{imgType:1});
        if(imgType.length>0){
            isHasCustomType = 1;
        }else{
            isHasCustomType = 0;
        }
        var data = {
            items:jsonData,
            isHasCustomType:isHasCustomType
        }
        jumi.template('product/trad/trad_img_type',data,function (tpl) {
            $('#trad_img_type').html(tpl);
            $('input[name="yangs"]').click(function () {
                var self = $(this);
                var url=self.data('imgurl');
                $('#img_position').attr('src',url);
            })
            if(args.sign===99){
                $('#diy_img').attr('src',args.imgUrl);
            }

            $("input[name='yangs'][value='" + args.sign + "']").prop("checked", "checked");
            var url = $("input[name='yangs'][value='" + args.sign + "']").data('imgurl');
            $('#img_position').attr('src',url);
            $('#diy_img').click(function () {
                var d = new Dialog({
                    context_path:CONTEXT_PATH, //请求路径,  必填
                    resType:1 ,//图片1，视频2，语音3 必填
                    callback:function(url){
                        $('#diy_img').attr('src',url);
                    }
                });
                d.render();
            })
        })
    }
    var _bindEvent = function () {
        $("#status").select2({
            theme: "jumi"
        });
        $('#action_search').click(function () {
            _initPagination();
        })
        $('#trad_del').click(function () {
            var array = [];
            var nodeInputCheck = $('input[name="product_ac"]:checked');
            var tradActivityStatusUo = {};
            var args = {};
            tradActivityStatusUo.status = 9;
            if(nodeInputCheck.length>0){
                nodeInputCheck.each(function () {
                    var v = $(this).val();
                    array.push(v);
                })
                tradActivityStatusUo.ids = array.join(',');
                var json = JSON.stringify(tradActivityStatusUo);
                args.fn1 = function(){
                    $.ajaxJsonPut(ajaxUrl.url3,json,{
                        "done":function (res) {
                            if(res.code===0){
                                var dm = new dialogMessage({
                                    type:1,
                                    fixed:true,
                                    msg:'删除成功',
                                    isAutoDisplay:true,
                                    time:3000
                                });
                                dm.render();
                                _initPagination();
                            }
                        }
                    });
                };
                args.fn2 = function(){

                };
                jumi.dialogSure('是否批量删除所勾选的活动',args);
            }else{
                var dm = new dialogMessage({
                    type:1,
                    fixed:true,
                    msg:'还未选择活动名称',
                    isAutoDisplay:true,
                    time:3000
                });
                dm.render();
            }

        })
        $('#trad_all').click(function () {
            var flag = $('input[name="trad_all"]').is(":checked");
            if(flag){
                $('input[name="product_ac"]').prop('checked',false);
            }else{
                $('input[name="product_ac"]').prop('checked',true);
            }
        })
    }
    /**
     * 拼团修改文件
     * @private
     */
    var _doEditSubmit = function () {
        var tradActivityUo = {};
        var price = $('input[name="product_price"]').val();
        var productPrice = $('#chooseProductBox').find('p').data('goodprice');
        tradActivityUo.id = $('input[name="ac_id"]').val();
        tradActivityUo.type = 2;
        tradActivityUo.name = $('input[name="action_name"]').val();
        tradActivityUo.indianaType = $('input[name="activity_space"]:checked').val();
        tradActivityUo.sign = $('input[name="yangs"]:checked').val();
        if(tradActivityUo.sign==='99'){
            var url = $('#diy_img').attr('src');
            var purl = jumi.picParse(url,0);
            tradActivityUo.imageUrl = purl;
        }
        tradActivityUo.groupType = $('#trad_type').val();
        tradActivityUo.pid = $('#chooseProductBox').find('p').data('pid');
        tradActivityUo.price = parseInt(price,10)*100;
        tradActivityUo.position = $('input[name="weiz"]:checked').val();
        var flag = $('#trad_set_mofify_form').valid();
        if(price>productPrice){
            var dm = new dialogMessage({
                type:1,
                fixed:true,
                msg:'拼团价格不能大于商品价格',
                isAutoDisplay:true,
                time:3000
            });
            dm.render();
            return;
        }
        if(!tradActivityUo.pid){
            var dm = new dialogMessage({
                type:1,
                fixed:true,
                msg:'请选择活动商品',
                isAutoDisplay:true,
                time:3000
            });
            dm.render();
            return;
        }
        if(!flag){
            return;
        }
        var jsonData = JSON.stringify(tradActivityUo);
        $.ajaxJsonPut(ajaxUrl.url1,jsonData,{
            "done":function (res) {
                if(res.code===0){
                    var dm = new dialogMessage({
                        type:1,
                        fixed:true,
                        msg:'修改活动成功',
                        isAutoDisplay:true,
                        time:3000
                    });
                    dm.render();
                    _initPagination();
                }
            }
        })
        setTimeout(function () {
               dialog.get('modifyDialogAction').close().remove();
        },1000)
    }
    var _deleteAction = function (productName,id) {
        var args = {};
        var array = [];
        array.push(id);
        var tradActivityStatusUo = {};
        tradActivityStatusUo.status = 9;
        tradActivityStatusUo.ids = array.join(',');
        var json = JSON.stringify(tradActivityStatusUo);
        args.fn1 = function(){
            $.ajaxJsonPut(ajaxUrl.url3,json,{
                "done":function (res) {
                    if(res.code===0){
                        var dm = new dialogMessage({
                            type:1,
                            fixed:true,
                            msg:'删除成功',
                            isAutoDisplay:true,
                            time:3000
                        });
                        dm.render();
                        _initPagination();
                    }
                }
            });
        };
        args.fn2 = function(){

        };
        jumi.dialogSure('是否删除【'+productName+'】的活动',args);
    }
    var _startAction = function (productName,id) {
        var args = {};
        var array = [];
        array.push(id);
        var tradActivityStatusUo = {};
        tradActivityStatusUo.status = 1;
        tradActivityStatusUo.ids = array.join(',');
        var json = JSON.stringify(tradActivityStatusUo);
        args.fn1 = function(){
            $.ajaxJsonPut(ajaxUrl.url3,json,{
                "done":function (res) {
                    if(res.code===0){
                        var dm = new dialogMessage({
                            type:1,
                            fixed:true,
                            msg:'启动成功',
                            isAutoDisplay:true,
                            time:3000
                        });
                        dm.render();
                        _initPagination();
                    }
                }
            });
        };
        args.fn2 = function(){

        };
        jumi.dialogSure('是否启动【'+productName+'】的活动',args);
    }
    var _stopAction = function (productName,id) {
        var args = {};
        var array = [];
        array.push(id);
        var tradActivityStatusUo = {};
        tradActivityStatusUo.status = 2;
        tradActivityStatusUo.ids = array.join(',');
        var json = JSON.stringify(tradActivityStatusUo);
        args.fn1 = function(){
            $.ajaxJsonPut(ajaxUrl.url3,json,{
                "done":function (res) {
                    if(res.code===0){
                        var dm = new dialogMessage({
                            type:1,
                            fixed:true,
                            msg:'暂停成功',
                            isAutoDisplay:true,
                            time:3000
                        });
                        dm.render();
                        _initPagination();
                    }
                }
            });
        };
        args.fn2 = function(){

        };
        jumi.dialogSure('是否暂停【'+productName+'】的活动',args);
    }
    var _watchAction = function (id) {
        $.ajaxJsonGet(ajaxUrl.url1+'/'+id,{
            "done":function (res) {
                if(res.code===0){
                    var data = res.data;
                    jumi.template("product/trad/trad_action_xq",data,function(tpl){
                        var d = dialog({
                            title: '活动详情',
                            content:tpl,
                            id:'createDialogAction',
                            width:500,
                            onshow:function(){
                                $('#action_close').click(function () {
                                    dialog.get('createDialogAction').close().remove();
                                })

                            }
                        });
                        d.showModal();
                    })

                }
            },
            "fail":function () {

            }
        });
    }
    var _modifyEvent = function () {
        $('#ac_modify_close').click(function () {
            dialog.get('modifyDialogAction').close().remove();
        })
        $('#ac_modify_save').click(function () {
            _doEditSubmit();
        })
        $('#chooseProduct').click(function () {
            var pid = $('input[name="chooseProductBoxName"]').val();
            var goodsDialog = new GoodsDialog({
                context_path: CONTEXT_PATH,
                isSingle:true
            }, function (good) {
                var tpl = '<p data-goodPrice="'+good.price/100+'" data-pid="'+good.pid+'" title="'+good.name+'" style="max-height: 36px; line-height: 34px;">'+good.name+'</p>'
                $('#chooseProductBox').html(tpl);
                $('#xg_mofify_preview').attr('src',good.pic_square);
            })
            goodsDialog.render();
        })
        $('input[name="weiz"]').click(function () {
            var self = $(this);
            var position = self.data('positon');
            var positionArray = position.split(',');
            $("#img_position").removeAttr('style');
            _.map(positionArray,function (k,v) {
                $("#img_position").css(positionArray[v],0+ "px");
            })

        })
    }
    var _selectedEvent = function (data) {
        var groupType = data.groupType;
        var sign = data.sign;
        $("#trad_type").find("option[value='"+groupType+"']").attr("selected",true);
        $("input[name='yangs']").eq(1).prop("checked",true);
    }
    var _modifyAction = function (id) {
        $.ajaxJsonGet(ajaxUrl.url1+'/'+id,{
            "done":function (res) {
                if(res.code===0){
                    var data = res.data;
                    var args = {};
                    args.sign = data.sign;
                    if(args.sign===99){
                        args.imgUrl = data.imageUrl;
                    }
                    jumi.template("product/trad/trad_modify",data,function(tpl){
                        var d = dialog({
                            title: '修改活动',
                            content:tpl,
                            id:'modifyDialogAction',
                            width:850,
                            onshow:function(){
                                _modifyEvent();
                                _getAction(args);
                                _validateset();
                                _selectedEvent(data);
                            }
                        });
                        d.showModal();
                    })

                }
            },
            "fail":function () {

            }
        });
    }
    var _initPagination = function(){
        var url = ajaxUrl.url2;
        var tradProductQo = {};
        tradProductQo.pageSize=pageSize;
        tradProductQo.curPage=curPage;
        tradProductQo.name = $("#name").val();
        tradProductQo.productName = $("#productName").val();
        tradProductQo.status= $("#status").val();
        tradProductQo.type=2;
        var jsonStr = JSON.stringify(tradProductQo);
        jumi.pagination("#pageToolbar",url,jsonStr,function (res,curPage) {
            if(res.code===0){
                //判断是否第一页
                var data = {
                    items:res.data.items
                };
                if(curPage===0){
                    data.isFirstPage = 1;
                }else{
                    data.isFirstPage = 0;
                }
                jumi.template("product/trad/product_groups_item",data,function(tpl){
                    $("#tableBody").html(tpl);
                })
            }
        })
    }
    
    return {
        init:_init,//列表初始化
        search:_initPagination,
        deleteAction:_deleteAction,
        startAction:_startAction,
        stopAction:_stopAction,
        watchAction:_watchAction,
        modifyAction:_modifyAction
    };
})();
