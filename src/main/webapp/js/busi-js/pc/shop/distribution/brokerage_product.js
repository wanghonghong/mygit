CommonUtils.regNamespace("brokerage","product");

brokerage.product=(function(){
    var pageSize=10;
    var curPage=0;
    var _init=function () {
        _initPagination();
    }

    var _initPagination = function(){
        var url = CONTEXT_PATH+"/brokerage/brokerage_product"
        var brokerageProductQo = {};
        brokerageProductQo.pageSize=pageSize;
        brokerageProductQo.curPage=curPage;
        var jsonStr = JSON.stringify(brokerageProductQo);
        jumi.pagination("#pageToolbar",url,brokerageProductQo,function (res,curPage) {
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
                for(var i in data.items){
                    var item =data.items[i];
                    if(item.excludeRole != null && item.excludeRole.indexOf("1")>=0){
                        item.haveOne = true;
                    }else{
                        item.haveOne = false;
                    }
                    if(item.excludeRole != null && item.excludeRole.indexOf("2")>=0){
                        item.haveTwo = true;
                    }else{
                        item.haveTwo = false;
                    }
                }
                jumi.template("shop/distribution/brokerage_product/brokerage_product_item",data,function(tpl){
                    $("#tableBody").empty();
                    $("#tableBody").html(tpl);
                })
            }
        })
    }

    var _save = function () {
        var brokerageProductCos = {};
        var brokerageProductCoList=[]
        $("#tableBody > ul").each(function () {
            var value = $(this).attr("id");
            var brokerageProductCo = {};
            brokerageProductCo.id=$("#"+value+"_id").val();
            brokerageProductCo.pid=value;
            brokerageProductCo.oneBrokerage=$("#"+value+"_one").val()*100;
            brokerageProductCo.twoBrokerage=$("#"+value+"_two").val()*100;
            var excludeRole = "";
            if ($("#"+value+"_1").is(':checked')==true){
                excludeRole = 1+",";
            }
            if ($("#"+value+"_2").is(':checked')==true){
                excludeRole += 2+",";
            }
            brokerageProductCo.excludeRole=excludeRole;
            brokerageProductCoList.push(brokerageProductCo);
        });
        brokerageProductCos.brokerageProductCos=brokerageProductCoList;
        var jsonStr = JSON.stringify(brokerageProductCos);
        console.log(jsonStr);
        var url = CONTEXT_PATH + "/brokerage/brokerage_product/add";
        $.ajaxJson(url, jsonStr, {
            "done": function (res) {
                if (res.code == 0) {
                    var dm = new dialogMessage({
                        type: 1,
                        fixed: true,
                        msg: '操作成功',
                        isAutoDisplay: true,
                        time: 1500
                    });
                    dm.render();
                    _initPagination();
                } else {
                    var dm = new dialogMessage({
                        type: 2,
                        fixed: true,
                        msg: '操作失败',
                        isAutoDisplay: true,
                        time: 1500
                    });
                    dm.render();
                }
            }
        })
    }
    return {
        init:_init,
        save:_save
    };
})();
