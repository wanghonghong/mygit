CommonUtils.regNamespace("mall", "choice");

mall.choice = (function () {
    var opt = {
        getTempAjax: CONTEXT_PATH + "/shopsetting/templates",
        getCurTemplate: CONTEXT_PATH + "/shopsetting/cur_template",
        saveTemplate: CONTEXT_PATH + "/shopsetting/template",
        containerId: "",
        tempId: "0"
    }
    var _init = function () {
        _loadTemplates().then(function(){
            _bindEvent();
        }).then(function () {
            _getCurTemplate();
        })

    }
    var _loadTemplates = function () {
        var dfdPlay = $.Deferred();
        var url = opt.getTempAjax;
        $.ajaxJsonGet(url, null, {
                "done": function (res) {
                   console.log(res);
                    if(res.code==0){
                        var data=res.data;
                        $.each(data,function(i){
                           var $li= $("<li>",{
                               "tempId":data[i].tempId
                           }).data(data[i]);
                            $("<label>",{
                                "class":"tmp-icon tmp-"+data[i].tempCode
                            }).appendTo($li);
                            $("<span>",{
                                "class":"tmp-name",
                                "text":data[i].tempName
                            }).appendTo($li);
                            $li.appendTo("#tmptype"+data[i].tempType);
                        })
                    }
                    dfdPlay.resolve();
                }
            }
        )
        return dfdPlay;
    }
    var  _getCurTemplate=function(){
        var url = opt.getCurTemplate;
        $.ajaxJsonGet(url, null, {
            "done": function (res) {
                if(res.code==0){
                    $("#choiceTemplate .template-choice-box ul li[tempId="+res.data.tempId+"]").click();
                }else{
                    $("#choiceTemplate .template-choice-box ul li[tempId=1]").click();
                }
            }
        })
    }


    var alertinfo = function (msg) {
        var dm = new dialogMessage({
            type:3,
            msg:msg,
            isAutoDisplay:true,
            time:1500
        });
        dm.render();
    }
    var _saveTemp=function () {
        var url=opt.saveTemplate+"?tempId="+opt.tempId;
         // var data={
         //     tempId:opt.tempId
         // }
         $.ajaxJsonPut(url,null,{
            "done":function(res){
                console.log(res)
                if(res.code==0){
                    alertinfo('设置模板成功')
                }else{
                    alertinfo('设置模板失败')
                }
            }
         });
    }


    var _bindEvent=function () {
        $("#choiceTemplate").delegate(".template-choice-box ul li", "click", function () {
            var data=$(this).data();
            if(!$.isEmptyObject(data.mainJson)){
                $("#choiceTemplate .template-choice-box ul li").removeClass("z-active");
                $(this).addClass("z-active");
                $("#tempImg").attr("src",jumi.config.cssPath+"/css/pc/img/homeBuild/mobile_template_"+data.tempCode+".png");
                opt.tempId=data.tempId;
            }else{
                alertinfo('该主题正在玩命开发中。。。。');
            }
        });
        $("#saveTempBtn").click(function(){
            _saveTemp();
        })





        // $("#choiceTemplate .template-choice-box ul li").click(function(){
        //
        // })

    }

    return {
        init: _init
    }
})();
