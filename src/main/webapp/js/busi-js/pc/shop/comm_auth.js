CommonUtils.regNamespace("comm", "auth");

comm.auth = (function(){
    var _init = function(){
        _goodsData();
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


    var _query = function(){
        var phoneNumber =  $("#chanPhoneNumber").val();
        if(phoneNumber ==false){
            _errMsg("请输入电话号码！");
            return;
        }
        var params = {
            phoneNumber:phoneNumber
        };
        var newUrl = CONTEXT_PATH+'/commonQrcode/userData';
        $.ajaxJson(newUrl, params, {
            "done": function (res) {
                if(res.data.length>0){
                    console.log(res);
                    var data = {
                        items:res.data,
                        CONTEXT_PATH : CONTEXT_PATH
                    };

                    jumi.template('shop/commQrcode/comm_auth_user',data,function(tpl){
                        $('#allDiv').empty();
                        $('#allDiv').html(tpl);
                    });
                }else{
                    _errMsg("未查询到该用户！");
                }

            }
        });

    };


    /*商品弹窗列表*/
    var _goodsData = function(){
        var url = CONTEXT_PATH+"/good/productList/4";
        var params = {
            pageSize:2,
            curPage:0
        };
        jumi.pagination('#goodsToolbar',url,params,function(res,curPage){
            if(res.code===0){
                //判断是否第一页
                var data = {
                    items:res.data.items
                };
                jumi.template('shop/commQrcode/goods_data_list',data,function(tpl){
                    $('#goodsBody').empty();
                    $('#goodsBody').html(tpl);
                });
            }
        });

    };

    /*商品弹窗列表*/
    var _goodsDataDialog = function() {
        var elemgoodsbox = document.getElementById('dialoginfo-goodsbox');
        dialog({
            title: "商品选择",
            content: elemgoodsbox,
            id:'goodsDialog'
        }).width(755).showModal();
    };



    /*确定授权按钮*/
    var _confirmAuth = function() {
        var userId =  $('input[name="rdate"]:checked').val();
        var goodsId = $("#goodsId").val();
        var ids = $("#commAuthIds").val();
        var type =  $('input[name="authType"]:checked').val();
        if(userId == false || userId == undefined){
            _errMsg("选择用户");
            return;
        }
        if(type == 1){
            if(goodsId == false){
                _errMsg("请选择商品");
                return;
            }
        }

        if(ids == false){
            _errMsg("请扫描商品二维码");
            return;
        }

        var commonAuthUo = {
        };
        commonAuthUo.commonQrcodeDetailIds = ids;
        commonAuthUo.goodsId = goodsId;
        commonAuthUo.userId = userId;
        commonAuthUo.type = type;
        var jsonData = JSON.stringify(commonAuthUo);
        var url = CONTEXT_PATH+'/commonQrcode/commonAuth';
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
                }
            }
        });


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

    /*授权弹窗*/
    var _authDialog = function() {
        var userId =  $('input[name="rdate"]:checked').val();
       //var userId = $("#authUserId").val();
        var goodsId = $("#goodsId").val();
        var type =  $('input[name="authType"]:checked').val();
        if(userId == false || userId == undefined){
            _errMsg("选择用户");
            return;
        }
        if(type == 1){
            if(goodsId == false){
                _errMsg("请选择商品");
                return;
            }
        }

        var elemgoodsbox = document.getElementById('startAuthDialog');
        dialog({
            title: "授权",
            content: elemgoodsbox,
            id:'authDialog'
        }).width(650).showModal();
        $("#authText").focus();
    };

    /*页面调用二维码授权窗口*/
    var _callAuthDialog = function(userId) {
        var data = {
            userId:userId
        };
        jumi.template('shop/commQrcode/order_comm_auth',data,function(tpl){
            $('#commAuthDialog').empty();
            $('#commAuthDialog').html(tpl);
        });
        var elemgoodsbox = document.getElementById('commAuthDialog');
        dialog({
            title: "授权",
            content: elemgoodsbox,
            id:'authDialog'
        }).width(755).showModal();
    };


    return {
        init :_init,
        query:_query,
        goodsData:_goodsData,
        goodsDataDialog:_goodsDataDialog,
        confirmAuth:_confirmAuth,
        goodsBoxData:_goodsBoxData,
        authDialog:_authDialog,
        callAuthDialog:_callAuthDialog
    };
})();

