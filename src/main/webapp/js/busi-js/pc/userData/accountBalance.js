/**
 * Created by whh on 2016/9/1.
 */
CommonUtils.regNamespace("account", "balance");
var  tabindex=0;
account.balance = (function(){
    var ajaxUrl = {
        url1:'',
        url2:''
    }
    var _init = function(){
        _bind();
    }
    var _bind = function () {
        $(function(){
            $(".dialog").click(function() {
                var elem = document.getElementById('dialoginfo');
                dialog({
                    title: "购买支付",
                    content: elem,

                }).width(500).height(240).showModal();

            });
        });
        $(".tab li").click(function(){
            $(this).addClass("active").siblings().removeClass("active");
            var target = $(this).attr("data-target");
            $("#"+target).addClass("active show").removeClass("showNone").siblings().removeClass("active show").addClass("showNone");
        })

    }
    return {
        init :_init
    };
})()