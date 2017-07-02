/**
 * Created by zx on 2017/5/11.
 */
CommonUtils.regNamespace("signup","activity");
signup.activity=(function () {
    var opt={
        Project_Path:CONTEXT_PATH,
        activity:{}
    }
    var ajaxUrl={
        enrolmentActivityAjax:CONTEXT_PATH+"/enrolmentActivity/"
    }
    var SIGN_UP =4;  //报名
    var _addActivity=function () {
        opt.activity={id:0}
        _init();
    }
    var _updataActivity=function (el) {
        var data=$(el).data();
        opt.activity={id:data.id};
        _init();
    }
    var _init=function(){
        opt.mainContainer = "activityEdit";
        opt.pagetype=SIGN_UP;
        opt.curVersion = "standard"

        _loadhtml().then(function () {
            return _loadSignUp();
        }).then(function () {
            _loadmoduleTypes();
            _correctData();
        }).then(function () {
            module.main.init(opt);
            locationWindow();
        })
    }
    var locationWindow=function () {
        $("#standard .app-config  .app-field:first .control-mask").click();
    }
    var _loadmoduleTypes = function () {
        opt.moduleTypes=module.types.getTypesJson();
    }

    var _correctData=function () {
        if(opt.activity.id){
            //修改
            console.log(opt.detailJson)
            opt.curVersion = "standard";
            var json = $.parseJSON(opt.activity.detailJson);
            opt.versionList = json.versionList;

        }else{
            //添加
            var standardArray = {"versionName": "标准版", "versionId": "standard", "versionContent": []};
            standardArray.versionContent.push({"type": "titleInfo"});
            standardArray.versionContent.push({"type": "signup"});
            standardArray.versionContent.push({ "type": "richtext"  });
            var versionList = [];
            versionList.push(standardArray);
            opt.versionList = versionList;
        }
    }

    var _loadSignUp=function () {
        var dfdPlay = $.Deferred();
        if(opt.activity.id){
            var url = ajaxUrl.enrolmentActivityAjax + "/" + opt.activity.id;
            $.ajaxJsonGet(url, "", {
                "done": function (res) {
                    if (res.code == '0') {
                        console.log(res.data)
                        opt.activity=res.data;
                        dfdPlay.resolve(); // 动画完成
                    }
                }
            });
        }else{
            dfdPlay.resolve(); // 动画完成
        }
        return dfdPlay;
    }

    var _loadhtml=function () {
        var dfdPlay = $.Deferred();
        $("#activityEdit").empty();
        var itemhtml = jumi.templateHtml("/tpl/activity/signup/activityView.html", {});
        $("#activityBox").addClass("z-hide");
        $("#activityEdit").append(itemhtml).removeClass("z-hide");
        dfdPlay.resolve(); // 动画完成
        return dfdPlay;
    }

    var _closeEditWindow=function () {
        $("#activityEdit").empty();
        $("#activityBox").removeClass("z-hide");
        $("#activityEdit").addClass("z-hide");
        // vote.index.queryThemeList();
    }
    
    var _saveActivity=function () {
        var sign = module.main.verifyData();
        if(!sign){
            return;
        }
        var data=_getsaveData();
        var jsonData = JSON.stringify(data);
        var url = ajaxUrl.enrolmentActivityAjax;
        console.log(data);
        // return ;
        if(data.id){
            $.ajaxJsonPut(url, jsonData, {
                "done": function (res) {
                    var dm = new dialogMessage({
                        type: 1,
                        fixed: true,
                        msg: res.data.msg,
                        isAutoDisplay: true,
                        time: 3000
                    });
                    dm.render();
                    signup.index.queryActivityList();
                    _closeEditWindow();
                },
                "fail": function (res) {
                }
            });
        }else {
            $.ajaxJson(url, jsonData, {
                "done": function (res) {
                    var dm = new dialogMessage({
                        type: 1,
                        fixed: true,
                        msg: res.data.msg,
                        isAutoDisplay: true,
                        time: 3000
                    });
                    dm.render();
                    signup.index.queryActivityList();
                    _closeEditWindow();
                },
                "fail": function (res) {
                }
            });
        }
        
    }
    var _getsaveData=function () {
        var data=module.main.getBaseInfo();
        data.detailJson=module.main.getjson();
        if(opt.activity.id){
            data.id=opt.activity.id;
        }
         console.log(data)
        return data;
    }
    
    var _returnActivity=function () {
        _closeEditWindow();
    }

    var _removeActivity=function (el) {
        var data=$(el).data();
        var url=ajaxUrl.enrolmentActivityAjax+data.id;
        var args = {};
        args.fn1 = function(){
            $.ajaxJsonDel(url,null,{
                "done":function (res) {
                    if(res.code==0){
                        if(res.data.code==0){
                            var dm = new dialogMessage({
                                type:2,
                                fixed:true,
                                msg:'删除该活动成功',
                                isAutoDisplay:true,
                                time:3000
                            });
                            dm.render();
                            signup.index.queryActivityList();
                        }else{
                            var dm = new dialogMessage({
                                type:2,
                                fixed:true,
                                msg:res.data.msg,
                                isAutoDisplay:true,
                                time:3000
                            });
                            dm.render();
                        }
                    }
                }
            })
        };
        args.fn2 = function(){};
        jumi.dialogSure('确定删除该活动吗?',args);
        console.log(el)
    }
    


    return{
         addActivity:_addActivity,
        updataActivity:_updataActivity,
        saveActivity:_saveActivity,
        returnActivity:_returnActivity,
       removeActivity:_removeActivity
    }
})();
