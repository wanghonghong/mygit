/**
 * Created by BenRay on 16/8/8.
 * 官方图文
 */
CommonUtils.regNamespace("review", "notice");
review.notice = (function(){
    var ajaxUrl = {
        url1:CONTEXT_PATH+'/zb/notice',
    };
    var opt={
        notice:{}
    }
    var _init = function(){
        _bind();
    };

    //时间空间渲染函数
    var _timeTimepicker = function () {
        var dateConfig = {
            showSecond : true,
            timeFormat : 'hh:mm:ss',
            dateFormat : 'yy-mm-dd',
            stepHour : 1,
            stepMinute : 1,
            stepSecond : 1,
        };
        $("#startTime").datepicker(dateConfig);
        $("#endTime").datepicker(dateConfig);
    };
    $("#form1").validate({
        rules:{
            title:"required",
        },
        messages:{
            title:"请输入标题",
        }
    });
    var _bind = function(){

        _timeTimepicker();
        $("#type").select2({
            theme: "jumi"
        });
        $("#joinType").select2({
            theme: "jumi"
        });

        var noticeContext = $('#noticeContext').val();
        noticeConfig(noticeContext);
        $("#joinType").change(function () {
            $("#selectJoin").hide();
        })
        $("#title").change(function () {
            $("#title-error").hide();
        })

    }
    var noticeConfig = function (noticeContext) {
        var ue =null;
        var richtextId= "noticeContext" +new Date().getTime();
        $(".noticeContext").attr("id",richtextId);
        ue = UE.getEditor(richtextId, {
            enableAutoSave: false,
            initialFrameHeight: 420,
            imageScaleEnabled: false
        });
        ue.ready(function () {
            //设置编辑器的内容
            if (noticeContext){
                opt.notice.noticeContext = noticeContext;
            }else {
                ue.setContent("");
            }
            if (opt.notice.noticeContext != null && opt.notice.noticeContext != undefined) {
                ue.setContent(opt.notice.noticeContext);
            }
        });
        ue.addListener('contentchange', function () {
            opt.notice.noticeContext = ue.getContent();
        })
    }
    //点击返回按钮
    var _backClick = function () {
        $('.two-level-memu.z-sel').trigger('click');
    }
    //点击保存草稿箱(新增)
    var _draftClick = function () {
        var noticeCo =  opt.notice;
        noticeCo.status = 1;
        if ($('#joinType').val()){
            if ($("option[value='[0]']:selected")){
                noticeCo.joins = '[0]';
            }else {
                noticeCo.joins = $('#joinType').val().toString();
            }
        }else {
            $('#selectJoin').css("display","block");
            $('#title').focus();
            return;
        }
        if ($('#title').val()){
            noticeCo.title = $('#title').val();
        }else {
            $('#title-error').css("display","block");
            $('#title').focus();
            return;
        }
        noticeCo.type = $('#type').find('option:selected').val();
        noticeCo.lookType = 2;
        var data = JSON.stringify(noticeCo);
        var url = ajaxUrl.url1;
        $.ajaxJson(url,data,{
            "done":function (res) {
                if(res.code==0){
                    var dm = new dialogMessage({
                        type:1,
                        fixed:true,
                        msg:"保存成功",
                        isAutoDisplay:false,
                    });
                    dm.render();
                    $('button[i-id="ok"]').click(function () {
                        $('.two-level-memu.z-sel').trigger('click');
                    })
                    $('button[i="close"]').click(function () {
                        $('.two-level-memu.z-sel').trigger('click');
                    })
                }
            }
        });
    }
    //点击保存草稿箱(更新)
    var _updateDraft = function () {
        var noticeUo =  opt.notice;
        noticeUo.id = $('#noticeId').val();
        noticeUo.status = 1;
        if ($('#joinType').val()){
            if ($("option[value='[0]']:selected")){
                noticeUo.joins = '[0]';
            }else {
                noticeUo.joins = $('#joinType').val().toString();
            }
        }else {
            $('#selectJoin').css("display","block");
            $('#title').focus();
            return;
        }
        if ($('#title').val()){
            noticeUo.title = $('#title').val();
        }else {
            $('#title-error').css("display","block");
            $('#title').focus();
            return;
        }
        noticeUo.type = $('#type').find('option:selected').val();
        var data = JSON.stringify(noticeUo);
        var url = ajaxUrl.url1;
        $.ajaxJsonPut(url,data,{
            "done":function (res) {
                if(res.code==0){
                    var dm = new dialogMessage({
                        type:1,
                        fixed:true,
                        msg:"保存成功",
                        isAutoDisplay:false,
                    });
                    dm.render();
                    $('button[i-id="ok"]').click(function () {
                        $('.one-level-memu.z-sel').trigger('click');
                    })
                    $('button[i="close"]').click(function () {
                        $('.one-level-memu.z-sel').trigger('click');
                    })
                }
            }
        });
    }
    //点击发布公告(新增)
    var _issueNotice = function () {
        var noticeCo =  opt.notice;
        noticeCo.status = 2;
        if ($('#joinType').val()){
            if ($("option[value='[0]']:selected")){
                noticeCo.joins = '[0]';
            }else {
                noticeCo.joins = $('#joinType').val().toString();
            }
        }else {
            $('#selectJoin').css("display","block");
            $('#title').focus();
            return;
        }
        if ($('#title').val()){
            noticeCo.title = $('#title').val();
        }else {
            $('#title-error').css("display","block");
            $('#title').focus();
            return;
        }
        noticeCo.type = $('#type').find('option:selected').val();
        noticeCo.lookType = 2;
        var data = JSON.stringify(noticeCo);
        var url = ajaxUrl.url1;
        $.ajaxJson(url,data,{
            "done":function (res) {
                if(res.code==0){
                    var dm = new dialogMessage({
                        type:1,
                        fixed:true,
                        msg:"发布成功",
                        isAutoDisplay:false,
                    });
                    dm.render();
                    $('button[i-id="ok"]').click(function () {
                        $('.two-level-memu.z-sel').trigger('click');
                    })
                    $('button[i="close"]').click(function () {
                        $('.two-level-memu.z-sel').trigger('click');
                    })
                }
            }
        });
    }
    //点击发布公告(更新)
    var _updateNotice = function () {
        var noticeUo =  opt.notice;
        noticeUo.id = $('#noticeId').val();
        noticeUo.status = 2;
        if ($('#joinType').val()){
            if ($("option[value='[0]']:selected")){
                noticeUo.joins = '[0]';
            }else {
                noticeUo.joins = $('#joinType').val().toString();
            }
        }else {
            $('#selectJoin').css("display","block");
            $('#title').focus();
            return;
        }
        if ($('#title').val()){
            noticeUo.title = $('#title').val();
        }else {
            $('#title-error').css("display","block");
            $('#title').focus();
            return;
        }
        noticeUo.type = $('#type').find('option:selected').val();
        noticeUo.lookType = 2;
        var data = JSON.stringify(noticeUo);
        var url = ajaxUrl.url1;
        $.ajaxJsonPut(url,data,{
            "done":function (res) {
                if(res.code==0){
                    var dm = new dialogMessage({
                        type:1,
                        fixed:true,
                        msg:"发布成功",
                        isAutoDisplay:false,
                    });
                    dm.render();
                    $('button[i-id="ok"]').click(function () {
                        $('.two-level-memu.z-sel').trigger('click');
                    })
                    $('button[i="close"]').click(function () {
                        $('.two-level-memu.z-sel').trigger('click');
                    })
                }
            }
        });
    }
    return {
        init :_init,
        backClick:_backClick,
        draftClick:_draftClick,
        issueNotice:_issueNotice,
        updateNotice:_updateNotice,
        updateDraft:_updateDraft,
    };
})();

