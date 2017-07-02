/**
 * Created by whh on 2016/12/15.
 */
CommonUtils.regNamespace("community", "context");
community.context = (function() {
    var ajaxUrl = {
        url1: CONTEXT_PATH + '/community/reply',
        url2: CONTEXT_PATH + '/community/replys',
        url3: CONTEXT_PATH + '/community/post',
    };
    var cmt={
        reply:{}
    }
    var _init = function () {
        _bind();
    };

    var _bind = function(){
        _replyConfig();
        _showReplys();
    }
    var _replyConfig = function (replyContext) {
        var ue = null;
        var richtextId = "replyContext" + new Date().getTime();
        $(".replyContext").attr("id", richtextId);
        ue = UE.getEditor(richtextId, {
            enableAutoSave: false,
            initialFrameHeight: 200,
            imageScaleEnabled: false
        });
        ue.ready(function () {
            //设置编辑器的内容
            if (replyContext){
                cmt.reply.context = replyContext;
            }else {
                ue.setContent("");
            }
            if (cmt.reply.context != null && cmt.reply.context != undefined) {
                ue.setContent(cmt.reply.context);
            }
            ue.addListener('contentchange', function () {
                cmt.reply.context = ue.getContent();
            })
        });
    }
    //保存回复
    var _saveRepley = function (id) {
        var replyCo =  cmt.reply;
        if($('#checkbox1').is(':checked')){
            replyCo.isHide = 1;
        }else {
            replyCo.isHide = 2;
        }
        replyCo.postId = id;
        var jsonData = JSON.stringify(replyCo);
        var url = ajaxUrl.url1;
        $.ajaxJson(url,jsonData,{
            "done" : function (res) {
                if(res.code==0){
                    var dm = new dialogMessage({
                        type:1,
                        fixed:true,
                        msg:'回复成功',
                        isAutoDisplay:true,
                        time:2000,
                    });
                    dm.render();
                    _showReplys();
                }
            }
        })
    }
    //快速回复
    var _saveQuickRepley = function (id) {
        debugger;
        var replyCo =  {};
        replyCo.isHide = 2;
        replyCo.postId = id;
        replyCo.context = $('#quickReply').val();
        if ($('#quickReply').val()==""){
            return;
        }
        var jsonData = JSON.stringify(replyCo);
        var url = ajaxUrl.url1;
        $.ajaxJson(url,jsonData,{
            "done" : function (res) {
                if(res.code==0){
                    var dm = new dialogMessage({
                        type:1,
                        fixed:true,
                        msg:'回复成功',
                        isAutoDisplay:false,
                    });
                    dm.render();
                    $('#quickReply').val("");
                }
            }
        })
    }

    var _showReplys = function () {
        var url = ajaxUrl.url2;
        var postId = $('#postId').val();
        var params = {
            pageSize:5,
            postId:postId,
        };
        jumi.pagination('#pageToolbar',url,params,function(res,curPage){
            if(res.code===0){
                var data = {
                    items:res.data.items
                };
                jumi.template('community/reply_list',data,function(tpl){
                    $('#replyList').html(tpl);
                })
            }
        })
    }
    //点赞效果begin
    var _saveZan = function (id) {
        var target = $(event.target );
        var A=target.attr("id");
        var B=A.split("like");
        var messageID=B[1];
        var C=parseInt($("#likeCount"+messageID).html());
        target.css("background-position","")
        var D=target.attr("rel");
        if(D === 'like')
        {
            $("#likeCount"+messageID).html(C+1);
            target.addClass("heartAnimation").attr("rel","unlike");
        }
        var readZanCo = {};
        readZanCo.postId = Number(id);
        readZanCo.type = 2;
        var data = JSON.stringify(readZanCo);
        var url = ajaxUrl.url3+'/zan';
        $.ajaxJson(url, data, {
            done: function (res) {
            }
        })

    }

    var _showScore = function () {
        jumi.template('community/context_score',function (tpl) {
            var d = dialog({
                title: '评分',
                content: tpl,
                onshow: function () {
                }
            }).showModal();
        })
        // var data = {};
        // data.id = id;
        // var jsonData = JSON.stringify(data);
        // var url = ajaxUrl.url2;
        // $.ajaxJson(url,jsonData, {
        //     "done": function (res) {
        //         var data = {
        //             item: res.data,
        //         };
        //         jumi.template('community/context_score',data,function (tpl) {
        //             var d = dialog({
        //                 title: '评分',
        //                 content: tpl,
        //                 onshow: function () {
        //                 }
        //             }).showModal();
        //         })
        //     }
        // })
    }

    return {
        init :_init,
        replyConfig:_replyConfig,
        saveRepley:_saveRepley,
        showReplys:_showReplys,
        saveZan:_saveZan,
        saveQuickRepley:_saveQuickRepley,
        showScore:_showScore,
    };
})();