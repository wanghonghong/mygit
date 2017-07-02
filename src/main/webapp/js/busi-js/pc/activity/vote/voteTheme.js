/**
 * zx
 */
CommonUtils.regNamespace("vote","theme");
vote.theme=(function () {
    var opt={
        Project_Path:CONTEXT_PATH,
        voteTheme:{}
    }
    var ajaxUrl={
        themeAjax:CONTEXT_PATH+"/vote/theme/",
        themeListAjax:CONTEXT_PATH+"/vote/themes/"
    }
    var VOTE_THEME=3;  //投票主题

    var _addTheme=function () {
        opt.voteTheme={id:0};
        _init();
    }
    var _updataTheme=function (el) {
        var data=$(el).data();
        opt.voteTheme={id:data.id};
        _init();
    }
    var _init=function () {
        opt.mainContainer = "voteThemeEdit";
        opt.pagetype=VOTE_THEME;
        opt.curVersion = "standard";
        _loadhtml().then(function () {
            return _loadVoteTheme();
        }).then(function () {
            _loadmoduleTypes();
            _correctData();
        }).then(function () {
            module.main.init(opt);
            locationWindow();
            // module.main.locationWindow();
        })
    }
    var locationWindow=function () {
      // $("#standard .m-app-main .app-config .app-field:first ").each(function () {
      //     module.view.updataPageModular($(this));
      // })
        $("#standard .app-config  .app-field:first .control-mask").click();
    }

    var _loadVoteTheme=function () {
        var dfdPlay = $.Deferred();

        if(opt.voteTheme.id){
            var url = ajaxUrl.themeAjax + "/" + opt.voteTheme.id;
            $.ajaxJsonGet(url, "", {
                "done": function (res) {
                    if (res.code == '0') {
                         console.log(res.data)
                        opt.voteTheme=res.data;
                        dfdPlay.resolve(); // 动画完成
                    }
                }
            });
        }else{
            dfdPlay.resolve(); // 动画完成
        }
        return dfdPlay;
        
    }
    var _loadmoduleTypes = function () {
        opt.moduleTypes=module.types.getTypesJson();
    }
    var _correctData=function () {

        if(opt.voteTheme.id){
            //修改
            console.log(opt.detailJson);
            opt.curVersion = "standard";
            var json = $.parseJSON(opt.voteTheme.detailJson);
            opt.versionList = json.versionList;
            $(opt.versionList).each(function (j) {
                if (opt.versionList[j].versionId == 'standard') {
                    $(opt.versionList).each(function (j) {
                        $(opt.versionList[j].versionContent).each(function (k) {
                            if (opt.versionList[j].versionContent[k].type == 'vote') {
                                opt.versionList[j].versionContent[k].themeName=opt.voteTheme.name;
                                opt.versionList[j].versionContent[k].startTime=opt.voteTheme.startTime;
                                opt.versionList[j].versionContent[k].endTime=opt.voteTheme.endTime;
                                opt.versionList[j].versionContent[k].voteItemList=opt.voteTheme.voteRelVos;
                                // opt.versionList[j].versionContent[k].likes = imgTextRo.reward;
                            }
                        })
                    })
                }
            })
        }else{
            //添加
            var standardArray = {"versionName": "标准版", "versionId": "standard", "versionContent": []};
            standardArray.versionContent.push({"type": "titleInfo"});
            standardArray.versionContent.push({"type": "vote"});
            standardArray.versionContent.push({ "type": "richtext"  });
            var versionList = [];
            versionList.push(standardArray);
            opt.versionList = versionList;
        }
    }

    var _loadhtml=function () {
        var dfdPlay = $.Deferred();
        $("#voteThemeEdit").empty();
        var itemhtml = jumi.templateHtml("/tpl/activity/vote/voteTheme.html", {});
        $("#voteBox").addClass("z-hide");
        $("#voteThemeEdit").append(itemhtml).removeClass("z-hide");
        dfdPlay.resolve(); // 动画完成
        return dfdPlay;
    }
    var _closeEditWindow=function () {
        $("#voteThemeEdit").empty();
        $("#voteBox").removeClass("z-hide");
        $("#voteThemeEdit").addClass("z-hide");
        // vote.index.queryThemeList();
    }

    var _getsaveData=function () {
        var data=module.main.getBaseInfo();
        data.detailJson=module.main.getjson();
        if(opt.voteTheme.id){
            //修改投票主题需要进行数据修正
            data.id=opt.voteTheme.id;
            data.voteRelList=data.voteItemList;
            delete data.voteItemList
            var voteRelDelList=[];
            for(var i=0;i<opt.voteTheme.voteRelVos.length;i++){
                var flag=true;
                 for(var j=0;j<data.voteRelList.length;j++){
                     if(opt.voteTheme.voteRelVos[i].itemId==data.voteRelList[j].itemId){
                         data.voteRelList[j].id=opt.voteTheme.voteRelVos[i].id;
                         data.voteRelList[j].votesNum=opt.voteTheme.voteRelVos[i].votesNum;
                         flag=false;
                        break;
                     }
                 }
                 if(flag){
                     voteRelDelList.push(opt.voteTheme.voteRelVos[i]);
                 }
            }
            data.voteRelDelList=voteRelDelList;
            // console.log( data.voteRelList);
            // console.log(  opt.voteTheme.voteRelVos);
            // console.log(  data.voteRelDelList);
        }else{
            data.voteRelList=data.voteItemList;
            delete data.voteItemList
        }
        console.log(data);
        return data;
    }
    var _saveTheme=function () {
        var sign = module.main.verifyData();
        if(!sign){
            return;
        }
         var data=_getsaveData();
        var jsonData = JSON.stringify(data);
        var url = ajaxUrl.themeAjax;
          // return null;
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
                     vote.index.queryThemeList();
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
                    vote.index.queryThemeList();
                    _closeEditWindow();
                },
                "fail": function (res) {
                }
            });
        }

    }

    var _delTheme=function (el) {
        var data=$(el).data();
        var url=ajaxUrl.themeAjax+data.id;
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
                            vote.index.queryThemeList();
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
    }


    return {
        addTheme:_addTheme,
        updataTheme:_updataTheme,
        saveTheme:_saveTheme,
        delTheme:_delTheme,
        closeEditWindow:_closeEditWindow
    }
})();



