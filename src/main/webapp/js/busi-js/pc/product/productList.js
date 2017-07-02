CommonUtils.regNamespace("product", "manager");

//新增商品
product.manager.addgoods = function () {
    $(".two-level-memu[resourceid='8']").click();
};

//新增分类
product.manager.addType = function (url) {
    $.ajaxHtmlGet(url, null, {
        done: function (res) {
            $("#productList").empty();
            $("#productList").append(res.data);
        }
    });
}
//到运费模板列表
product.manager.getTransTemp = function (url) {
    $.ajaxHtmlGet(url, null, {
        done: function (res) {
            $("#productList").empty();
            $("#productList").append(res.data);
        }
    });
}
//修改顺序
product.manager.changeSeq = function(pid,seq){
    //alert(pid+"------------------"+seq);
    var url= CONTEXT_PATH +"/product/change_seq/"+pid;
    var productForUpdateSeqVo = {};
    productForUpdateSeqVo.pid = pid;
    productForUpdateSeqVo.productSeq=seq;
    var jsonStr = JSON.stringify(productForUpdateSeqVo);
    $.ajaxJson(url,jsonStr,{
        "done":function(res){
            var dm = new dialogMessage({
                type:1,
                fixed:true,
                msg:'修改成功',
                isAutoDisplay:true,
                time:1500
            });dm.render();
            reloadPage();
        }
    });
}
//手动上架
product.manager.down = function (pid){
    var url = CONTEXT_PATH + "/product/down/" + pid
    var d = dialog({
        title: '提示',
        content: '是否确认上架',
        width: 300,
        okValue: '确定',
        ok: function () {
            $.ajaxJson(url,null,{
                "done":function(res){
                    var dm = new dialogMessage({
                        type:1,
                        fixed:true,
                        msg:'上架成功',
                        isAutoDisplay:true,
                        time:1500
                    });dm.render();
                    reloadPage();
                }
            });
        },
        cancelValue: '取消',
        cancel: function () {
            var dm = new dialogMessage({
                type:2,
                fixed:true,
                msg:'上架失败',
                isAutoDisplay:true,
                time:1500
            });dm.render();
            reloadPage();
        }
    });
    d.show();
}
//批量手动下架
product.manager.isDown = function (isUpOrDown) {
    var pids = getPid(isUpOrDown);
    var url = CONTEXT_PATH + "/product/product_down/" + pids;
    var d = dialog({
        title: '提示',
        content: '是否确认下架',
        width: 300,
        okValue: '确定',
        ok: function () {
            $.ajaxJson(url,null,{
                "done":function(res){
                    var dm = new dialogMessage({
                        type:1,
                        fixed:true,
                        msg:'下架成功',
                        isAutoDisplay:true,
                        time:1500
                    });dm.render();
                    reloadPage();
                }
            });
        },
        cancelValue: '取消',
        cancel: function () {
            var dm = new dialogMessage({
                type:1,
                fixed:true,
                msg:'下架失败',
                isAutoDisplay:true,
                time:1500
            });dm.render();
            reloadPage();
        }
    });
    d.show();
};

//批量手动上架
product.manager.isUp = function (isUpOrDown) {
    var pids = getPid(isUpOrDown);
    var url = CONTEXT_PATH + "/product/product_up/" + pids;
    var d = dialog({
        title: '提示',
        content: '是否确认上架',
        width: 300,
        okValue: '确定',
        ok: function () {
            $.ajaxJson(url,null,{
                "done":function(res){
                    var dm = new dialogMessage({
                        type:1,
                        fixed:true,
                        msg:'上架成功',
                        isAutoDisplay:true,
                        time:1500
                    });dm.render();
                        reloadPage();
                }
            });
        },
        cancelValue: '取消',
        cancel: function () {
            var dm = new dialogMessage({
                type:1,
                fixed:true,
                msg:'下架失败',
                isAutoDisplay:true,
                time:1500
            });dm.render();
            reloadPage();
        }
    });
    d.show();
};

//批量删除商品
product.manager.del = function (isUpOrDown) {
    //获取商品id，以","分割
    var pids = getPid(isUpOrDown);

    var url = CONTEXT_PATH + "/product/del_good/" + pids;
    var d = dialog({
        title: '提示',
        content: '是否确认删除',
        width: 300,
        okValue: '确定',
        ok: function () {
            $.ajax({
                type: "DELETE",
                contentType: 'application/json',
                url: url,
                processData: false,
                dataType: 'json',
                data: null,
                success: function (data) {
                    if (data.code == 0) {
                        var dm = new dialogMessage({
                            type:1,
                            fixed:true,
                            msg:'删除成功',
                            isAutoDisplay:true,
                            time:1500
                        });dm.render();
                        reloadPage();
                    }
                },
                error: function () {
                    alert('Err...222222222');
                }
            });
            /*$.ajaxJsonDel(url,null,{
             "done":function(res){
             reloadPage();
             }
             });*/

        },
        cancelValue: '取消',
        cancel: function () {
        }
    });
    d.show();
};

//获取商品ids参数
getPid = function (isUpOrDown) {
    var pids = "";
    $("input[name='pid_check" + isUpOrDown + "']").each(function () {
        if ($(this).is(":checked")) {
            pids += $(this).val() + ",";
        };
    });
    return pids;
}


product.manager.getQrcode = function (code) {
	var pid=$(code).attr('data');
	 var url= CONTEXT_PATH +"/product/goodQrcode/"+pid;
	 $.ajaxJson(url,"",{
	        "done":function(res){
	        	if(res.data.code==0){
	        		$(code).text('查看二维码');
	        	}
	        }
	    });
}

product.manager.checkQrcode = function (imgurl) {
	$("#qrcode").attr("src","");
	$("#qrcode").attr("src",imgurl);
	popMsg("商品二维码"); 
}

function popMsg(title){
	var elem = $('#dialoginfo2');
	memudialog=dialog({
		id:'show-dialog',
		width: 300,
		height: 300,
		title: title,
		content: elem 
	});
	memudialog.show();
};


