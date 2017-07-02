/**
 * zx
 */
CommonUtils.regNamespace("vote","item");
vote.item=(function () {

    var opt={
        itemtpl:"/tpl/activity/vote/voteItem.html",
        choisewindowtpl:"/tpl/activity/vote/choiceWindow.html",
        viewResWindowTpl:"/tpl/activity/vote/viewResWindow.html",
        phoneWindowWindowTpl:"/tpl/activity/vote/phoneWindow.html",
        itemAjax:CONTEXT_PATH+"/vote/item/",
        Project_Path:CONTEXT_PATH,
        voteItem:{}
    }

    var pageparam=  [{
        url: CONTEXT_PATH+"/vote/items",//对象列表
        pageSize: 10,
        curPage: 0,
        pageToolbarObj: "windowPageToolbar",
        tableBodyObj: "windowTableBody",
        template:"/tpl/activity/vote/choiceItem.html"
    },{
        url: CONTEXT_PATH+"/vote/rels",//对象列表
        pageSize: 10,
        curPage: 0,
        pageToolbarObj: "viewPageToolbar",
        tableBodyObj: "viewTableBody",
        template:"/tpl/activity/vote/viewItem.html"
    }];

    var _addItem=function () {
        var data={
            voteType:1,
            detail:"请投我神圣的一票"
        }
        opt.voteItem=data;
        _openEditWindow(data);
    }

    var _openEditWindow=function (data) {
        var html = jumi.templateHtml(opt.itemtpl,data);
        var titleStr="修改对象";
        var tag='update';
        if($.isEmptyObject(data.id)){
            titleStr="添加对象";
            tag='add';
        }
       var dl= dialog({
            content: html,
            title: titleStr,
            okValue: '确定',
            ok: function() {
                _dosave(dl);
                return false;
            },
            cancelValue:'取消',
            cancel:function () {
            }
        }).width(1050).showModal();
        initPage();
        bindEvent();
    }

    var initPage=function () {
        var ue =null;
        var richtextId= "richtextContainer" +new Date().getTime();
        $("#voteItemConfig").find(".richtextContainer").attr("id",richtextId);
        ue = UE.getEditor(richtextId, {
            enableAutoSave: false,
            initialFrameHeight: 300,
            imageScaleEnabled: false,
            autoHeightEnabled:false
        });
        ue.addListener('selectionchange', function () {
            $(".edui-for-hotspot","#voteItemConfig").remove();
            opt.voteItem.detail=ue.getContent();
            viewItem(opt.voteItem);
        })
        ue.addListener( 'ready', function( editor ) {
            $(".edui-for-hotspot","#voteItemConfig").remove();
            //设置编辑器的内容
            ue.setContent(opt.voteItem.detail);
        } );
        if(opt.voteItem.voteType==1){
            //图片
            if(opt.voteItem.resUrl){
                $("#imageResUrl").attr("src",opt.voteItem.resUrl);
                $("#imageResId").val( opt.voteItem.resId);
            }
        }else if(opt.voteItem.voteType==2){
            //视频
            $("#videoLabel").html(opt.voteItem.resName||"选择视频");
            $("#videoResId").val( opt.voteItem.resId);
        }else if(opt.voteItem.voteType==3){
            //音频
            $("#audioLabel").html(opt.voteItem.resName||"选择音频");
            $("#audioResId").val( opt.voteItem.resId);
        }
        if(opt.voteItem.voteType==1){
            opt.voteItem.imageResUrl=opt.voteItem.resUrl;
        }else if(opt.voteItem.voteType==2){
            //视频修改地址
            opt.voteItem.videoResUrl=module.edit.convertUrl(opt.voteItem.resUrl);
        }else if(opt.voteItem.voteType==3){
            opt.voteItem.audioResUrl=opt.voteItem.resUrl;
            opt.voteItem.audioResName=opt.voteItem.resName;
        }
        viewItem(opt.voteItem);
    }

    var bindEvent=function () {
        $("#voteItemConfig [name='voteType']").click(function () {
            var tmp=$(this).val();
            $("#votetype"+tmp).removeClass("z-hide");
            $("#votetype"+tmp).siblings().addClass("z-hide");
            opt.voteItem.voteType=tmp;
            viewItem(opt.voteItem);
        })


        $("#voteItemName").change(function () {
            var tmp=$(this).val();
            opt.voteItem.name=tmp;
            viewItem(opt.voteItem);
        })
        $("#imageLabel,#imageResUrl").click(function () {
            var d = new Dialog({
                context_path: opt.Project_Path,
                resType: 1,
                callback: function (url,originalUrl,data) {
                    if(!$.isEmptyObject(url)){
                        $("#imageResId").val(originalUrl.id);
                        $("#imageResUrl").attr("src",originalUrl.resUrl);
                        opt.voteItem.imageResUrl=url;
                        viewItem(opt.voteItem);
                    }
                }
            });
            d.render();
        })
        $("#videoLabel,#videoResUrl").click(function () {
            var d = new Dialog({
                context_path: opt.Project_Path,
                resType: 2,
                callback: function (url,originalUrl,data) {
                    if(!$.isEmptyObject(url)){
                        $("#videoResId").val(data.id);
                        $("#videoLabel").html( data.resName);
                        url=module.edit.convertUrl(url);
                        opt.voteItem.videoResUrl=url;
                        viewItem(opt.voteItem);
                    }
                }
            });
            d.render();
        })
        $("#audioLabel,#audioResUrl").click(function () {
            var d = new Dialog({
                context_path: opt.Project_Path,
                resType: 3,
                callback: function (url,originalUrl,data) {
                    if(!$.isEmptyObject(url)){
                        $("#audioResId").val(originalUrl.id);
                        $("#audioLabel").html( originalUrl.resName);
                        opt.voteItem.audioResUrl=url;
                        opt.voteItem.audioResName=originalUrl.resName;
                        viewItem(opt.voteItem);
                    }
                }
            });
            d.render();
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
    var _validate=function () {
        var sign=false;
        var voteItemName=$("#voteItemName").val();
        if($.isEmptyObject(voteItemName)){
            alertinfo("请输入标题名称");
            sign=true;
        } else if($.isEmptyObject(opt.voteItem.detail)){
            alertinfo("请输入详情编辑");
            sign=true;
        }else{
            if(opt.voteItem.voteType==1 && $.isEmptyObject($("#imageResId").val())){
                alertinfo("请选择图片");
                sign=true;
            }else if(opt.voteItem.voteType==2  && $.isEmptyObject($("#videoResId").val())) {
                alertinfo("请选择视频");
                sign=true;
            }else if(opt.voteItem.voteType==3  && $.isEmptyObject($("#audioResId").val())){
                alertinfo("请选择音频");
                sign=true;
            }
        }
        return sign;
    }
    var _dosave=function (dl) {
        if(_validate()){
            return false;
        }else{
            var data=opt.voteItem;
            data.name=$("#voteItemName").val();
            data.platform=0;
            if(data.voteType==1){
                data.resId=$("#imageResId").val();
            }else if(data.voteType==2){
                data.resId=$("#videoResId").val();
            }else if(data.voteType==3){
                data.resId=$("#audioResId").val();
            }
            delete  data.imageResUrl;
            delete  data.audioResUrl;
            delete  data.videoResUrl;
            delete  data.audioResName;

            var url=opt.itemAjax;
            if(!data.id){
                //新增
                $.ajaxJson(url,data,{
                    "done":function(res){
                        if(res.code==0){
                            var dm = new dialogMessage({
                                type:2,
                                fixed:true,
                                msg:res.data.msg,
                                isAutoDisplay:true,
                                time:3000
                            });
                            dm.render();
                            vote.index.queryItemList();
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
                        dl.close().remove();
                    }
                });
            }else{
                //修改
                $.ajaxJsonPut(url,data,{
                    "done":function(res){
                        if(res.code==0){
                            var dm = new dialogMessage({
                                type:2,
                                fixed:true,
                                msg:res.data.msg,
                                isAutoDisplay:true,
                                time:3000
                            });
                            dm.render();
                            vote.index.queryItemList();
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
                        dl.close().remove();
                    }
                });
            }
        }
    }
    var _updateItem=function (el) {
        var data=$(el).data();
        var url=opt.itemAjax+data.id;
        $.ajaxJsonGet(url,data,{
            "done":function(res){
                if(res.code==0){
                    opt.voteItem=res.data;
                    _openEditWindow(res.data);
                }
            }
        });
    }
    var _delItem=function (el) {
        var data=$(el).data();
        var url=opt.itemAjax+data.id;

        var args = {};
        args.fn1 = function(){
            $.ajaxJsonDel(url,null,{
                "done":function (res) {
                    if(res.code==0){
                        if(res.data.code==0){
                            var dm = new dialogMessage({
                                type:2,
                                fixed:true,
                                msg:res.data.msg,
                                isAutoDisplay:true,
                                time:3000
                            });
                            dm.render();
                            vote.index.queryItemList();
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
        jumi.dialogSure('确定删除该对象吗?',args);

    }
    
    var _choiceWindow=function (data,callback) {
        pageparam[0].curPage=0;
        pageparam[0].pageSize=10;
        initChoiceWindow(callback);
        _search(data,pageparam[0]);
    }


    var initChoiceWindow=function (callback) {
        var html = jumi.templateHtml(opt.choisewindowtpl,null);
        dialog({
            title: "投票选择",
            content: html,
            okValue: '确定',
            ok: function() {
                var voteItemList=[];
                $("#windowTableBody .table-container .u-cb1 :checkbox:checked").each(function (i) {
                      var data=$(this).closest(".table-container").data();
                    voteItemList.push(data);
                })
                callback(voteItemList);
            },
            cancelValue:'取消',
            cancel:function () {
            }
        }).width(750).showModal();
    }

    var _search=function (data,pageparam) {
        data.pageSize=pageparam.pageSize;
        data.curPage=pageparam.curPage;
        // var data = JSON.stringify(data);
        jumi.pagination("#"+pageparam.pageToolbarObj,pageparam.url,data,function (res,curPage) {
            pageparam.curPage=curPage;
            $("#"+pageparam.tableBodyObj).empty();
            // var itemhtml = jumi.templateHtml(pageparam.template,res.data);
            // $(itemhtml).appendTo("#"+pageparam.tableBodyObj);

            if(res.data.items && res.data.items.length>0){
                for(var i=0;i<res.data.items.length;i++){
                    res.data.items[i].num=i+1;
                    var itemhtml = jumi.templateHtml(pageparam.template,res.data.items[i]);
                    var item = $(itemhtml).data(res.data.items[i]);
                    $(item).appendTo("#"+pageparam.tableBodyObj);
                }
            }else{
                $("<div>",{
                    'class':'m-jm-err'
                }).html(" <img src='"+jumi.config.cssPath+"/css/pc/img/jm-nodb.png' >").appendTo("#"+pageparam.tableBodyObj);
            }
        })
    }
    var _viewResults=function (el) {
        initViewWindow(el);
        var data=$(el).data();
        data.themeId=data.id;
        pageparam[1].curPage=0;
        pageparam[1].pageSize=10;
        _search(data,pageparam[1]);
    }
    var initViewWindow=function (el) {
        var html = jumi.templateHtml(opt.viewResWindowTpl,null);
        dialog({
            title: "投票选择",
            content: html,
            okValue: '确定',
            ok: function() {
            }
        }).width(1060).showModal();
    }
    var _queryItem=function (el) {
       var parent =$(el).closest(".table-container");
        var data=parent.data();
        if(data.voteType==1){
            data.imageResUrl=data.resUrl;
        }else if(data.voteType==2){
            //视频修改地址
            data.videoResUrl=module.edit.convertUrl(data.resUrl);
        }else if(data.voteType==3){
            data.audioResUrl=data.resUrl;
            data.audioResName=data.resName;
        }
        viewItem(data);
    }

    var viewItem=function (data) {

        $("#phoneWindow").empty();
        var html = jumi.templateHtml(opt.phoneWindowWindowTpl,data);
        $("#phoneWindow").append(html);
    }
    
    return{
        addItem:_addItem,
        updateItem:_updateItem,
        delItem:_delItem,
        viewResults:_viewResults,
        choiceWindow:_choiceWindow,
        queryItem:_queryItem
    }
})();
