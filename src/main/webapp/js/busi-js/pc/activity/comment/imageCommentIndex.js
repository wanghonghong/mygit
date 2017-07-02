/**
 * Created by zx on 2017/5/3.
 */
CommonUtils.regNamespace("imagecomment","index");

imagecomment.index=(function(){
    var tabindex=0;
    var ajaxUrl={
        comment: CONTEXT_PATH+"/comment/comment/",
        commentlist:CONTEXT_PATH+"/comment/commentlist",
        commentsort:CONTEXT_PATH+"/comment/comment_sort"
    }
    var pageparam=  [{
        url: CONTEXT_PATH+"/comment/image_comments",//对象列表
        pageSize: 10,
        curPage: 0,
        pageToolbarObj: "pageToolbar1",
        tableBodyObj: "tableBody1",
        searchFrom:"searchform1",
        fristSearch:true,
        template:"/tpl/activity/comment/imageItem.html"
    },{
        url: CONTEXT_PATH+"/comment/comments",//对象列表
        pageSize: 10,
        curPage: 0,
        pageToolbarObj: "pageToolbar2",
        tableBodyObj: "tableBody2",
        searchFrom:"searchform2",
        fristSearch:true,
        template:"/tpl/activity/comment/commentItem.html"
    }];
    var _init=function () {
         console.log("init")
        tabindex=0;
        _bindEvent();
        _queryItemList();
    }

    var _bindEvent=function () {
        $("#commentBox [name$='Time']").datetimepicker({ timeFormat:'hh:mm:ss'  });

        $("#querybtn").click(function () {
            _queryItemList();
        })

    }

    var _queryItemList=function () {
        _search(pageparam[0]);
    }
    var _queryCommentList=function () {
        _search(pageparam[1],true);
    }
    var _search=function (pageparam,sign) {
        var dfdPlay = $.Deferred();
        var data={
            pageSize: pageparam.pageSize,
            curPage: pageparam.curPage
        }
        if(pageparam.searchFrom){
            $("input[type='text'],input[type='hidden'],select","#"+pageparam.searchFrom ).each(function () {
                if( $(this).attr("name")){
                    if($(this).val()){
                        data[$(this).attr("name")]=$(this).val();
                    }
                }
            })
        }
        // var data = JSON.stringify(data);
        jumi.pagination("#"+pageparam.pageToolbarObj,pageparam.url,data,function (res,curPage) {
            pageparam.curPage=curPage;
            $("#"+pageparam.tableBodyObj).empty();
            // var itemhtml = jumi.templateHtml(pageparam.template,res.data);
            // $(itemhtml).appendTo("#"+pageparam.tableBodyObj);

            if(res.data.items && res.data.items.length>0){
                for(var i=0;i<res.data.items.length;i++){
                    var itemhtml = jumi.templateHtml(pageparam.template,res.data.items[i]);
                    var item = $(itemhtml).data(res.data.items[i]);
                    $(item).appendTo("#"+pageparam.tableBodyObj);
                }
                if(sign){
                    loadCommentList();
                }
            }else{
                $("<div>",{
                    'class':'m-jm-err'
                }).html(" <img src='"+jumi.config.cssPath+"/css/pc/img/jm-nodb.png' >").appendTo("#"+pageparam.tableBodyObj);
            }
            dfdPlay.resolve(); // 动画完成
        })
        return dfdPlay;
    }
    var _viewItem=function (el) {
        openCommentView($(el).data());
        pageparam[1].curPage=0;
        _queryCommentList();
    }
    var bindCommentEvent=function () {
        $("#tableBody2 .commentUi  .replyBtn").click(function(){
            var commentUi= $(this).closest(".commentUi");
            var addCommentBox= $( ".addCommentBox",commentUi);
            console.log(addCommentBox.data());
            var commontdata=addCommentBox.data();
            var plreplybox= $( ".m-plreplybox",commentUi);
            $(plreplybox).slideToggle();
            if(!$.isEmptyObject(commontdata)){
                $("textarea[name='shared']",plreplybox).val(commontdata.comment);
            }
        })

        $("#tableBody2 .commentUi .m-plreplybox  .cancelbtn").click(function(){
            var plreplybox= $(this).closest(".m-plreplybox");
            $(plreplybox).slideToggle();
        })
        $("#tableBody2 .commentUi .m-plreplybox  .savebtn").click(function(){
            var commentUi= $(this).closest(".commentUi");
            var pdata=commentUi.data();
            var addCommentBox= $( ".addCommentBox",commentUi);
            var commontdata=addCommentBox.data();
            var plreplybox= $( ".m-plreplybox",commentUi);
            $(plreplybox).slideToggle();
            var shared=$("textarea[name='shared']",plreplybox);
            var data={};
            if(!$.isEmptyObject(commontdata)){
                data=commontdata;
            }else {
                data.pid=pdata.id;
                data.targetId=pdata.targetId;
            }
            data.comment=$(shared).val();
            saveComment(data);
        });
        $("#tableBody2 .commentUi .delBtn").click(function(){
           var commentUi= $(this).closest(".commentUi");
            var pdata=commentUi.data();
            console.log(commentUi.data());
            var url = ajaxUrl.comment+pdata.id;
            var args = {};
            args.fn1 = function(){
                $.ajaxJsonDel(url,null,{
                    "done":function (res) {
                        if(res.code==0){
                            if(res.data.code==0){
                                var dm = new dialogMessage({
                                    type:2,
                                    fixed:true,
                                    msg:'删除该评论成功',
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
            jumi.dialogSure('确定删除该条评论吗?',args);
        })

        $("#tableBody2 .commentUi  .sortBtn").click(function(){
            var commentUi= $(this).closest(".commentUi");
            console.log(commentUi.data());
            var sortbox= $( ".sortbox",commentUi);
            $(sortbox).slideToggle();
        })
        $("#tableBody2 .commentUi .sortbox  .cancelbtn").click(function(){
            var sortbox= $(this).closest(".sortbox");
            $(sortbox).slideToggle();
        })
        $("#tableBody2 .commentUi .sortbox  .savebtn").click(function(){
            var commentUi= $(this).closest(".commentUi");
            var pdata=commentUi.data();
            var sortbox= $( ".sortbox",commentUi);
            var sort=$("input[name='sort']",sortbox);
            $(sortbox).slideToggle();
            var data=pdata;
            data.sort=$(sort).val();
            saveComment(pdata);
        });


    }

    var saveComment=function (data) {
        console.log(data);
        var jsonData = JSON.stringify(data);
        var url = ajaxUrl.comment;
        if(data.id){
            //修改
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
                    _queryCommentList();
                },
                "fail": function (res) {
                }
            });
        }else{
            //添加
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
                    _queryCommentList();
                },
                "fail": function (res) {
                }
            });
        }


    }

    var loadCommentList=function () {
        var pidarr=[];
            $("#tableBody2 .commentUi ").each(function () {
                console.log($(this).data())
                pidarr.push($(this).data().id);
            })
        if($.isEmptyObject(pidarr)){
                return;
        }
        var pids=pidarr.join(",");
        var url=ajaxUrl.commentlist;
        var data={
            pids:pids,
            targetType:1

        }
        $.ajaxJson(url, data, {
            "done": function (res) {
                if (res.code == '0') {
                 console.log(res)
                   for(var j=0;j<res.data.length;j++){
                        $("#addCommentBox_"+res.data[j].pid).data(res.data[j]).show();
                       $("#addComment_"+res.data[j].pid).html(res.data[j].comment);
                   }
                    bindCommentEvent();
                }
            }
        });
    }

    var openCommentView=function (data) {
        var itemhtml = jumi.templateHtml("/tpl/activity/comment/commentView.html", data);
        dialog({
            title: "评论列表",
            content: itemhtml,
        }).width(970).showModal();
    }

    return {
        init:_init,
        viewItem:_viewItem
    }

})();
