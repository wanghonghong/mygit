/**
 * Created by whh on 2016/12/15.
 */
CommonUtils.regNamespace("agent", "business");
agent.business = (function() {
    var ajaxUrl = {
        url1: CONTEXT_PATH + '/join/dispatchs',
    };
    var _init = function () {
        _bind();
    };
    var _bind = function(){
        $(".btn-slide").click(function(){
            $("#m-search").slideToggle("fast");
            $(this).toggleClass("btn-slide1"); return false;
        });
        getDispatchPage();

    }
    //派单列表
    var getDispatchPage = function () {
        var url = ajaxUrl.url1+'/1';
        var params = {
            pageSize:10,
            type:1,
        };
        jumi.pagination('#needsToolbar',url,params,function(res,curPage){
            if(res.code===0){
                var data = {
                    items:res.data.items,
                };
                jumi.template('software_agent/business_needs_list',data,function(tpl){
                    $('#needsList').html(tpl);
                })
            }
        })
    }
    return {
        init :_init,

    };
})();