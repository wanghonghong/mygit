CommonUtils.regNamespace("mall", "main");

mall.main=(function(){
    var opt ={
        choiceSign:true,
        homeBuildSign:true,
        memberCenterSign:true
    };
    var _init=function(){
        opt ={
            choiceSign:true,
            homeBuildSign:true,
            memberCenterSign:true
        }
        mall.choice.init();
        _bandEvent();
    }
    var _bandEvent=function(){
        $("#mallTab ul li").click(function () {
            $(this).addClass("z-sel").siblings().removeClass("z-sel");
            var target = $(this).attr("data-target");
            $("#mallContent > div").removeClass("z-sel").addClass("z-hide");
            $("#"+target).removeClass("z-hide").addClass("z-sel");
            if(target=='choiceTemplate'){
                if(opt.choicesign){
                    opt.choicesign=false;
                }
            }else if(target=='homeBuild'){
                if(opt.homeBuildSign){
                    opt.homeBuildSign=false;
                    mall.homeBuild.init();
                }
                module.main.locationWindow();
            }else if(target=='memberCenter'){
                if(opt.memberCenterSign){
                    opt.memberCenterSign=false;
                    mall.memberCenter.init();
                }
            }
        });
    }
    return {
        init:_init
    }

})();