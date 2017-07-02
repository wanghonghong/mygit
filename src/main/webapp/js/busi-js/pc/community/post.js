/**
 * Created by whh on 2016/12/15.
 */
CommonUtils.regNamespace("community", "post");
community.post = (function() {
    var ajaxUrl = {
        url1: CONTEXT_PATH + '/community/post',
    };
    var cmt={
        post:{}
    }
    var _init = function () {
        _bind();
    };

    var _bind = function(){
        postConfig();
        jumi.Select('#type');
        var validate = $("#form1").validate({
            rules:{title:"required",},
            messages:{title:"请输入标题",}
        })
    }
    var postConfig = function (postContext) {
        var ue = null;
        var richtextId = "postContext" + new Date().getTime();
        $(".postContext").attr("id", richtextId);
        ue = UE.getEditor(richtextId, {
            enableAutoSave: false,
            initialFrameHeight: 420,
            imageScaleEnabled: false
        });
        ue.ready(function () {
            //设置编辑器的内容
            if (postContext){
                cmt.post.postContext = postContext;
            }else {
                ue.setContent("");
            }
            if (cmt.post.postContext != null && cmt.post.postContext != undefined) {
                ue.setContent(cmt.post.postContext);
            }
            ue.addListener('contentchange', function () {
                cmt.post.postContext = ue.getContent();
            })
        });
    }

    var _getContext  = function (title,postContext) {
        $('#title').val(title);
        $('.postContext').empty();
        postConfig(postContext);
    }
    var _save = function (status) {
        if($('#title').valid()){
            var postCo =  cmt.post;
            postCo.title = $('#title').val();
            postCo.type = $('#type').find('option:selected').val();
            if($('#checkbox1').is(':checked')){
                postCo.isHide = 1;
            }else {
                postCo.isHide = 2;
            }
            postCo.status = status;
            var msg ;
            if (status==1){
                msg = "发布成功";
            }else {
                msg = "保存成功";
            }
            var jsonData = JSON.stringify(postCo);
            var url = ajaxUrl.url1;
            $.ajaxJson(url,jsonData,{
                "done" : function (res) {
                    if(res.code==0){
                        var dm = new dialogMessage({
                            type:1,
                            fixed:true,
                            msg:msg,
                            isAutoDisplay:false,
                        });
                        dm.render();
                    }
                }
            })
        }
        else{
            $('#title').focus;
            return;
        }

    }

    return {
        init :_init,
        save:_save,
        getContext:_getContext,
    };
})();