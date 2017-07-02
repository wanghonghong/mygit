CommonUtils.regNamespace("comm", "qrcode");

comm.qrcode = (function(){
    var _init = function(){
        _goodsData();
    };


    /*商品弹窗*/
    var _openGoodsDialog = function(){
        var elemgoodsbox = document.getElementById('dialoginfo-goodsbox');
        dialog({
            title: "商品选择",
            content: elemgoodsbox,
            id:'goodsDialog'
        }).width(755).showModal();
    };

   /*商品弹窗列表*/
    var _goodsData = function(){
        var url = CONTEXT_PATH+"/good/productList/4";
        var params = {
            pageSize:10,
            curPage:0
        };
        jumi.pagination('#goodsToolbar',url,params,function(res,curPage){
            if(res.code===0){
                //判断是否第一页
                var data = {
                    items:res.data.items
                };
                jumi.template('shop/channelQrcode/goods_data_list',data,function(tpl){
                    $('#goodsBody').empty();
                    $('#goodsBody').html(tpl);
                });
            }
        });

    };

    /*错误提示*/
    var _errMsg = function(msg){
        var dm = new dialogMessage({
            type:2,
            title:'操作提醒',
            fixed:true,
            msg:msg,
            isAutoDisplay:false

        });
        dm.render();
    };


    /*商品列表弹窗确定按钮*/
    var _goodsBoxData = function(){
        var boxData =  $('input[name="goodsBox"]:checked').val();
        var vals = boxData.split(",");
        $("#goodsId").val(vals[0]);
        $("#goodsName").text(vals[1]);
        $("#goodsName").attr("title",vals[1]);
        dialog.get('goodsDialog').close().remove();
    };

    var _addCommQrcode = function(){
        var a = $("#validType").is(':checked');
        var validType = 1;
        if(a){
            validType = 0
        }

        if($("#goodsId").val()==""){
            _errMsg("请选择商品！");
            return;
        }
        if($("#commName").val()==""){
            _errMsg("请填写名称！");
            return;
        }
        if($("#commCount").val()==""){
            _errMsg("请填写条码数量！");
            return;
        }

        if(validType == 0) {
            if ($("#startTime").val() == "") {
                _errMsg("请选择有效开始时间！");
                return;
            }
            if ($("#endTime").val() == "") {
                _errMsg("请填有效结束时间！");
                return;
            }
        }


        var url  = CONTEXT_PATH +'/commonQrcode';
        var commCo = {};
        commCo.productId=$("#goodsId").val();
        commCo.name = $("#commName").val();
        commCo.codeType = $("#commQrcodeType").find("option:selected").val();
        commCo.startTime = $("#startTime").val();
        commCo.endTime = $("#endTime").val();
        commCo.remark = $("#remark").val();
        commCo.count = $("#commCount").val();
        commCo.validType = validType;
        var jsonData = JSON.stringify(commCo);
        $.ajaxJson(url,jsonData,{
            "done":function (res) {
                if(res.code===0){
                    var dm = new dialogMessage({
                        type:1,
                        fixed:true,
                        msg:res.data.msg,
                        isAutoDisplay:true,
                        time:3000
                    });
                    dm.render();
                    $('#createForm')[0].reset();
                }
            },
            "fail":function (res) {

            }

        });

    };



    /*下载条码列表分页数据*/
    var _downListData = function(){
        var url = CONTEXT_PATH+'/commonQrcode/findAll';
        var name =  $("#downName").val();
        var startEndTime =  $("#downStartEndTime").val();
        var endEndTime =  $("#downEndEndTime").val();
        var startCreateTime =  $("#downStartCreateTime").val();
        var endCreateTime =  $("#downEndCreateTime").val();
        var productName =  $("#downProductName").val();
        var fansType =  $("#downFansType").val();
        var params = {
            pageSize:10,
            curPage:0,
            name:name,
            productName:productName,
            startTime:startEndTime,
            endTime:endEndTime,
            startCreateTime:startCreateTime,
            endCreateTime:endCreateTime,
            fansType:fansType
        };
        jumi.pagination('#downToolbar',url,params,function(res,curPage){
            if(res.code===0){
                //判断是否第一页
                var data = {
                    items:res.data.items
                };
                jumi.template('shop/commQrcode/comm_qrcode_down_list',data,function(tpl){
                    $('#downBody').empty();
                    $('#downBody').html(tpl);
                });
            }
        });
    };


    //查看下载
    var _checkdown = function(id){
        var url = CONTEXT_PATH + "/commonQrcode/"+id;
        $.ajaxJsonGet(url,null,{
            done:function(res) {
                jumi.template('shop/commQrcode/check_down',res.data,function(tpl){
                    if(res.data.zipUrl!=''){
                        $('#dialoginfo-seedown').empty();
                        $('#dialoginfo-seedown').html(tpl);
                        var elemseedown = document.getElementById('dialoginfo-seedown');
                        dialog({
                            title: "查看下载",
                            content: elemseedown,
                        }).width(755).showModal();
                    }else if(res.data.zipUrl==false){
                        var elemseedown = document.getElementById('noZipUrl');
                        dialog({
                            title: "操作提醒",
                            content: elemseedown,
                            id:'noZipUrlDialog'
                        }).width(380).showModal();
                    }

                });
            }
        });

    };


    /*使用效果列表分页数据*/
    var _effectListData = function(){
        var url = CONTEXT_PATH+'/commonQrcode/findDetailAll';
        var name =  $("#effectName").val();
        var startEndTime =  $("#effectStartEndTime").val();
        var endEndTime =  $("#effectEndEndTime").val();
        var startCreateTime =  $("#effectStartCreateTime").val();
        var endCreateTime =  $("#effectEndCreateTime").val();
        var productName =  $("#effectProductName").val();
        var nickname = $("#effectNickname").val();
        var fansType =  $("#effectFansType").val();
        var params = {
            pageSize:10,
            curPage:0,
            name:name,
            productName:productName,
            startTime:startEndTime,
            endTime:endEndTime,
            startCreateTime:startCreateTime,
            endCreateTime:endCreateTime,
            nickname : nickname,
            fansType:fansType
        };
        jumi.pagination('#effectToolbar',url,params,function(res,curPage){
            if(res.code===0){
                //判断是否第一页
                var data = {
                    items:res.data.items
                };
                jumi.template('shop/commQrcode/comm_qrcode_effect_list',data,function(tpl){
                    $('#effectBody').empty();
                    $('#effectBody').html(tpl);
                });
            }
        });
    };



    //查看效果
    var _checkEffect = function(id) {
        var url = CONTEXT_PATH + "/commonQrcode/detail/"+id;
        $.ajaxJsonGet(url,null,{
            done:function(res) {
                jumi.template('shop/commQrcode/check_effect',res.data,function(tpl){
                    $('#dialoginfo-see').empty();
                    $('#dialoginfo-see').html(tpl);
                });
            }
        });
        var elemsee = document.getElementById('dialoginfo-see');
        dialog({
            title: "查看效果",
            content: elemsee,
            id:"checkDialog"
        }).width(755).showModal();

    };

    //关闭效果弹窗
    var _colseCheckDialog = function(){
        dialog.get('checkDialog').close().remove();
    };


    //使用效果用户列表数据 分页
    var _effectUserList = function(id){

        var counturl = CONTEXT_PATH + "/commonQrcode/frequency/"+id;
        $.get(counturl, function(result){
            $("#frequencyUserCount").text(result);
        });

        var url = CONTEXT_PATH+'/commonQrcode/findEffectList/'+id;
        var params = {
            pageSize:10,
            curPage:0
        };
        jumi.pagination('#effectPageToolbar',url,params,function(res,curPage){
            if(res.code===0){
                //判断是否第一页
                var data = {
                    items:res.data.items
                };

                $("#effectUserCount").text(res.data.items.length);
                jumi.template('shop/commQrcode/check_effect_list',data,function(tpl){
                    $('#checkEffectBody').empty();
                    $('#checkEffectBody').html(tpl);
                });
            }
        });

    };



    //取消授权弹窗
    var _cancelAuthDialog = function(id){
        $("#cancelAuthImg").attr("src",THIRD_URL+"/css/pc/img/jmtool1.png");
        $("#cancelAuthId").val(id);
        var elemclear = document.getElementById('dialoginfo-clear');
        dialog({
            title: "操作提醒",
            content: elemclear,
            id:'cancelAuthDialog'
        }).width(420).showModal();
    };

    //取消授权
    var _cancelAuth = function(){
        var id = $("#cancelAuthId").val();
        var url = CONTEXT_PATH + "/commonQrcode/cancelAuth/"+id;
        $.ajaxJsonGet(url,null,{
            done:function(res) {
                if(res.code===0){
                    var dm = new dialogMessage({
                        type:1,
                        fixed:true,
                        msg:res.data.msg,
                        isAutoDisplay:true,
                        time:3000
                    });
                    dm.render();
                    dialog.get('cancelAuthDialog').close().remove();
                }
            }
        });
    };

    var _closeCancelAuth = function(){
        dialog.get('cancelAuthDialog').close().remove();
    };


    var _selAll = function (check) {
        var checkboxs=document.getElementsByName("listDetailId");
        for (var i=0;i<checkboxs.length;i++) {
            var e=checkboxs[i];
            e.checked=!e.checked;
        }
    };

    /**
     * 下载备注弹窗
     * @private
     */
    var _remarkDiv = function(id){
        $(".m-btn-layer").hide();
        $("#remarkDiv"+id).show();
    };

    var _remarkDivHide = function(id){
        $("#remarkDiv"+id).hide();
    };

    var _saveRemark = function(id){
        var url = CONTEXT_PATH + "/commonQrcode/saveRemark/"+id;
        $.ajaxJsonGet(url,"remark="+$("#remarkVal"+id).val(),{
            done:function(res) {
                if(res.code===0){
                    var dm = new dialogMessage({
                        type:1,
                        fixed:true,
                        msg:res.data.msg,
                        isAutoDisplay:true,
                        time:3000
                    });
                    dm.render();
                }
                _downListData();
            }
        });
    };


    return {
        init :_init,
        goodsData:_goodsData,
        goodsBoxData:_goodsBoxData,
        openGoodsDialog:_openGoodsDialog,
        addCommQrcode:_addCommQrcode,
        downListData:_downListData,
        checkdown:_checkdown,
        effectListData:_effectListData,
        checkEffect:_checkEffect,
        effectUserList:_effectUserList,
        cancelAuthDialog:_cancelAuthDialog,
        cancelAuth:_cancelAuth,
        closeCancelAuth:_closeCancelAuth,
        selAll:_selAll,
        remarkDiv:_remarkDiv,
        remarkDivHide:_remarkDivHide,
        saveRemark:_saveRemark,
        colseCheckDialog:_colseCheckDialog
    };
})();

