CommonUtils.regNamespace("customer", "wblastUser");
customer.wblastUser = (function(){
    var _init = function(id,lastType){
        _query(id,lastType);
    };

    var _query = function(id,lastType,d){
        var type = Number(lastType);
        if(d!=undefined){
            if(type==0){
                $("#lastUserUlD li").removeClass('z-sel');
                $(d).addClass("z-sel");
            }
            if(type==1){
                $("#lastUserUlD li").removeClass('z-sel');
                $(d).addClass("z-sel");
            }
            if(type==2){
                $("#lastUserUlD li").removeClass('z-sel');
                $(d).addClass("z-sel");
            }
        }
        var url = CONTEXT_PATH +'/customer/getWbLastUser/'+id;
        var params = {
            pageSize:5,
            lastType:lastType
        };
        jumi.pagination('#lastuserToolbar',url,params,function(res,curPage){
            if(res.code===0){
                //判断是否第一页
                var data = {
                    items:res.data.items,
                    id:id
                };
                $("#lastcountD").text(res.data.count);
                jumi.template('new_customer/wb/wb_lastuser_list',data,function(tpl){
                    $('#lastUserBody').empty();
                    $('#lastUserBody').html(tpl);
                });
            }
        });

    };

    return {
        init :_init,
        query:_query
    };
})();




