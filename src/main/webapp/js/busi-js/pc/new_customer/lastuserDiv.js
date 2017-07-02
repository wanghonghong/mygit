CommonUtils.regNamespace("customer", "lastUser");
customer.lastUser = (function(){
    var _init = function(wxuserId,lastType){
        _query(wxuserId,lastType);
    };

    var _query = function(wxuserId,lastType,d){
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
        var url = CONTEXT_PATH +'/customer/laseuser/'+wxuserId;
        var params = {
            pageSize:5,
            lastType:lastType
        };
        jumi.pagination('#lastuserToolbar',url,params,function(res,curPage){
            if(res.code===0){
                //判断是否第一页
                var data = {
                    items:res.data.items,
                    wxuserId:wxuserId
                };
                $("#lastcountD").text(res.data.count);
                jumi.template('new_customer/lastuserDiv_list',data,function(tpl){
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




