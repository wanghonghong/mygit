CommonUtils.regNamespace("channel", "qrcode");

channel.qrcode = (function(){
    var _init = function(){
        _channelData();
        _goodsData();
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

    /*渠道商弹窗列表*/
    var _channelData = function(){
        var url = CONTEXT_PATH+'/channelQrcode/channerlData';
        var phoneNumber =  $("#chanPhoneNumber").val();
        var params = {
            pageSize:10,
            curPage:0,
            phoneNumber:phoneNumber
        };
        jumi.pagination('#channelToolbar',url,params,function(res,curPage){
            if(res.code===0){
                //判断是否第一页
                var data = {
                    items:res.data.items
                };
                jumi.template('shop/channelQrcode/channel_data_list',data,function(tpl){
                    $('#channelBody').empty();
                    $('#channelBody').html(tpl);
                });
            }
        });

    };

    /*渠道商确定按钮*/
    var _channelBoxData = function(){
        var boxData =  $('input[name="channelBox"]:checked').val();
        var vals = boxData.split(",");
        var rolename = "";
        if(vals[3]==1){
            rolename="代理商1档";
        }else if(vals[3]==2){
            rolename="代理商2档";
        }
        else if(vals[3]==3){
            rolename="代理商3档";
        }
        else if(vals[3]==4){
            rolename="代理商4档";
        }
        else if(vals[3]==5){
            rolename="分销商1档";
        }
        else if(vals[3]==6){
            rolename="分销商2档";
        }
        else if(vals[3]==7){
            rolename="分销商3档";
        }
        $("#chanUserId").val(vals[0]);
        $("#chanData").text(vals[1]+"    "+vals[2]+"    "+rolename);
        dialog.get('channelDialog').close().remove();
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

    /* 生成条码按钮 */
    var _addGoodsQrcode = function(){
        var a = $("#validType").is(':checked');
        var validType = 1;
        if(a){
            validType = 0
        }

        if($("#chanUserId").val()==""){
            _errMsg("请选择渠道用户！");
            return;
        }
        if($("#goodsId").val()==""){
            _errMsg("请选择商品！");
            return;
        }
        if($("#qrcodeName").val()==""){
            _errMsg("请填写名称！");
            return;
        }
        if(validType == 0){
            if($("#startTime").val()==""){
                _errMsg("请选择有效开始时间！");
                return;
            }
            if($("#endTime").val()==""){
                _errMsg("请填有效结束时间！");
                return;
            }
        }

        var url  = CONTEXT_PATH +'/channelQrcode';
        var goodsQrcodeCo = {};
        goodsQrcodeCo.userId=$("#chanUserId").val();
        goodsQrcodeCo.productId=$("#goodsId").val();
        goodsQrcodeCo.name = $("#qrcodeName").val();
        goodsQrcodeCo.codeType = $("#qrcodeType").find("option:selected").val();
        goodsQrcodeCo.startTime = $("#startTime").val();
        goodsQrcodeCo.endTime = $("#endTime").val();
        goodsQrcodeCo.remark = $("#remark").val();
        goodsQrcodeCo.validType = validType;
        var jsonData = JSON.stringify(goodsQrcodeCo);
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
        var url = CONTEXT_PATH+'/channelQrcode/findAll';
        var startEndTime =  $("#startEndTime").val();
        var endEndTime =  $("#endEndTime").val();
        var startCreateTime = $("#startCreateTime").val();
        var endCreateTime = $("#endCreateTime").val();
        var name =  $("#channelName").val();
        var productName = $("#downProductName").val();
        var channelName = $("#downChannelName").val();
        var fansType =  $("#downFansType").val();
        var params = {
            pageSize:10,
            curPage:0,
            name:name,
            startEndTime:startEndTime,
            endEndTime:endEndTime,
            startCreateTime:startCreateTime,
            endCreateTime:endCreateTime,
            productName:productName,
            channelName:channelName,
            fansType:fansType
        };
        jumi.pagination('#downToolbar',url,params,function(res,curPage){
            if(res.code===0){
                //判断是否第一页
                var data = {
                    items:res.data.items
                };
                jumi.template('shop/channelQrcode/channel_qrcode_down_list',data,function(tpl){
                    $('#downBody').empty();
                    $('#downBody').html(tpl);
                });
            }
        });
    };


    //查看下载
    var _checkdown = function(goodsQrcodeId){
        var url = CONTEXT_PATH + "/channelQrcode/"+goodsQrcodeId;
        $.ajaxJsonGet(url,null,{
            done:function(res) {
                jumi.template('shop/channelQrcode/check_down',res.data,function(tpl){
                    $('#dialoginfo-seedown').empty();
                    $('#dialoginfo-seedown').html(tpl);
                });
            }
        });

        var elemseedown = document.getElementById('dialoginfo-seedown');
        dialog({
            title: "查看下载",
            content: elemseedown,
        }).width(755).showModal();
    };


    //使用效果 列表分页数据
    var _effectListData = function(){
        var url = CONTEXT_PATH+'/channelQrcode/findAll';
        var name =  $("#effectName").val();
        var startEndTime =  $("#effectStartEndTime").val();
        var endEndTime =  $("#effectEndEndTime").val();
        var startCreateTime = $("#effectStartCreateTime").val();
        var endCreateTime = $("#effectEndCreateTime").val();
        var productName = $("#effectProductName").val();
        var channelName = $("#effectChannelName").val();
        var fansType =  $("#effFansType").val();
        var params = {
            pageSize:10,
            curPage:0,
            name:name,
            startEndTime:startEndTime,
            endEndTime:endEndTime,
            startCreateTime:startCreateTime,
            endCreateTime:endCreateTime,
            productName:productName,
            channelName:channelName,
            fansType:fansType
        };
        jumi.pagination('#effectToolbar',url,params,function(res,curPage){
            if(res.code===0){
                //判断是否第一页
                var data = {
                    items:res.data.items
                };
                jumi.template('shop/channelQrcode/channel_qrcode_effect_list',data,function(tpl){
                    $('#effectBody').empty();
                    $('#effectBody').html(tpl);
                });
            }
        });

    };



    //查看效果
    var _checkEffect = function(goodsQrcodeId){
        var url = CONTEXT_PATH + "/channelQrcode/"+goodsQrcodeId;
        $.ajaxJsonGet(url,null,{
            done:function(res) {
                jumi.template('shop/channelQrcode/check_effect',res.data,function(tpl){
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
    }



    //使用效果用户列表数据 分页
    var _effectUserList = function(goodsQrcodeId){

        var counturl = CONTEXT_PATH + "/channelQrcode/frequency/"+goodsQrcodeId;
        $.get(counturl, function(result){
            $("#frequencyUserCount").text(result);
        });

        var url = CONTEXT_PATH+'/channelQrcode/findEffectList/'+goodsQrcodeId;
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
                jumi.template('shop/channelQrcode/check_effect_list',data,function(tpl){
                    $('#effectUserList').empty();
                    $('#effectUserList').html(tpl);
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
        var url = CONTEXT_PATH + "/channelQrcode/cancelAuth/"+id;
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
        var url = CONTEXT_PATH + "/channelQrcode/saveRemark/"+id;
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

    /**
     * 效果备注弹窗
     * @private
     */
    var _effRemarkDiv = function(id){
        $(".m-btn-layer").hide();
        $("#effectRemarkDiv"+id).show();
    };

    var _effRemarkDivHide = function(id){
        $("#effectRemarkDiv"+id).hide();
    };


    var _effSaveRemark = function(id){
        var url = CONTEXT_PATH + "/channelQrcode/saveRemark/"+id;
        $.ajaxJsonGet(url,"remark="+$("#effRemarkVal"+id).val(),{
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
                _effectListData();
            }
        });
    };


    return {
        init :_init,
        channelData:_channelData,
        channelBoxData:_channelBoxData,
        goodsData:_goodsData,
        goodsBoxData:_goodsBoxData,
        addGoodsQrcode:_addGoodsQrcode,
        downListData:_downListData,
        checkdown:_checkdown,
        effectListData:_effectListData,
        checkEffect:_checkEffect,
        effectUserList:_effectUserList,
        cancelAuthDialog:_cancelAuthDialog,
        cancelAuth:_cancelAuth,
        closeCancelAuth:_closeCancelAuth,
        remarkDiv:_remarkDiv,
        remarkDivHide:_remarkDivHide,
        saveRemark:_saveRemark,
        effRemarkDiv:_effRemarkDiv,
        effRemarkDivHide:_effRemarkDivHide,
        effSaveRemark:_effSaveRemark,
        colseCheckDialog:_colseCheckDialog
    };
})();

