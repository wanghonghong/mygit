/**
 * Created by ray on 16/10/31.
 */
CommonUtils.regNamespace("activity", "focus");

activity.focus = (function() {
    var ajaxUrl = {
        url1:CONTEXT_PATH+'/activity/push'//活动推送增删改查
    }
    var activityNum = 0;//活动数量,初始化为0;
    var _initActivityPage = function(){
        jumi.file('/activity/follow.json',function(res){
            if(res.code===0){
                var data = {
                    items:res.data
                }
                jumi.template('activity/activity_focus_menu',data,function(tpl){
                    $('#add_Content').append(tpl);
                })
            }
        })
    }
    var _delSub = function (id) {
        var args = {};
        var msg = '是否移除该推送任务?';
        args.fn1 = function(){
            $('#u-related_'+id).remove();
        };
        args.fn2 = function(){

        };
        jumi.dialogSure(msg,args);
    }
    //选择任务
    var _selectTask = function (pushType,id) {
        switch(pushType){
            case '0':
                var t = new task({
                    config:'activity/imagetext_ar.json',
                    taskname:'自动回复',
                    pushType:0,
                    btnsfun:function(){
                        t._getbeaddreply(function(pid){
                            $('#focus_id_'+id).val(pid);
                            var name = activity.reply.act_beaddreply_text();
                            $('#focus_content_'+id).text(name);
                        })
                        return 1;
                    }
                });
                t.render();
                break;
            case '1':
                var t = new task({
                    config:'activity/imagetextcode.json',
                    taskname:'二维码海报',
                    pushType:1,
                    btnsfun:function(){
                        var codeId = $('input[name="activity-code"]:checked').data('id');
                        var name = $('input[name="activity-code"]:checked').data('name')
                        $('#focus_id_'+id).val(codeId);
                        $('#focus_content_'+id).text(name);
                        return codeId;
                    }
                });
                t.render();
                break;
            case '2':
                $('#focus_id_'+id).val(pushType);
                break;
            case '4':
                var goodsTypeDialog = new GoodsTypeDialog({
                    context_path: CONTEXT_PATH
                }, function (goodsType) {
                    $('#focus_id_'+id).val(goodsType.group_id);
                    $('#focus_content_'+id).text(goodsType.group_name);
                })
                goodsTypeDialog.render();
            break;
            case '5':
                var goodsDialog = new GoodsDialog({
                    context_path: CONTEXT_PATH
                }, function (goodslist) {
                    $('#focus_id_'+id).val(goodslist[0].pid);
                    $('#focus_content_'+id).text(goodslist[0].name);
                })
                goodsDialog.render();
                break;
            case '6':
                var t = new task({
                    config:'activity/imagetext.json',
                    taskname:'分类图文',
                    pushType:6,
                    btnsfun:function(){
                        var twId = $('input[name="activity-rd"]:checked').data('id');
                        var name = $('input[name="activity-rd"]:checked').data('name')
                        $('#focus_id_'+id).val(twId);
                        $('#focus_content_'+id).text(name);
                        return twId;
                    }
                });
                t.render();
                break;
            case '7':
                var t = new task({
                    config:'activity/imagetext_details.json',
                    taskname:'具体图文',
                    pushType:7,
                    btnsfun:function(){
                        var twxqId = $('input[name="activity-detail"]:checked').data('id');
                        var name = $('input[name="activity-detail"]:checked').data('name');
                        $('#focus_id_'+id).val(twxqId);
                        $('#focus_content_'+id).text(name);
                        return twxqId;
                    }
                });
                t.render();
            case '8':
                $('#focus_id_'+id).val(pushType);
                break;
            case '10':
                var t = new task({
                    config:'activity/imagetext_weixin.json',
                    taskname:'微信图文',
                    pushType:10,
                    btnsfun:function(){
                        var twxqId = $('input[name="activity-detail"]:checked').data('id');
                        var name = $('input[name="activity-detail"]:checked').data('name')
                        $('#focus_id_'+id).val(twxqId);
                        $('#focus_content_'+id).text(name);
                        return twxqId;
                    }
                });
                t.render();
        }
    }


    //时间验证
    var _validate = function(){
        $('#activity_form').validate({
            rules: {
                Intervaltime:{
                    required: true,
                    isIntGteZero:true,
                    digits:true
                }
            },
            messages:{
                Intervaltime:{
                    required: "请输入数值",
                    isIntGteZero:'数值必须是一个大于或者等于零的整数',
                    digits:'只能输入整数'
                }
            }
        })
    }
    //添加任务
    var _addSub = function(pushType,title,isOpen){
        var pushType = pushType;
        var title = title;
        var isOpen = isOpen;//判断是否开通
        activityNum = activityNum+1;
        if(isOpen==='0'){
            var dm = new dialogMessage({
                type:1,
                fixed:true,
                msg:'暂未开通',
                isAutoDisplay:true,
                time:3000
            });
            dm.render();
            return;
        }
        var data = {
            type:pushType,
            title:title,
            num:activityNum
        }
        jumi.template('activity/activity_add_focus?status=1',data,function(tpl){
            $('#activity_name').append(tpl);
            _validate();
        })

    }
    var _initSwitchButton = function( id,isOpen){
        var id = id;
        var isOpen = isOpen;
        var switchButton = new switchControl(id, {
            state: isOpen,
            onSwitchChange:function(v){
                if(v){
                    $(id).val(1);
                }
                else{
                    $(id).val(0);
                }
            }
        });
        if(isOpen){
            $(id).val(1);
        }else{
            $(id).val(0);
        }
        switchButton.render();
    }
    var _getTitle = function(pushType){
        switch(pushType)
        {
            case 0:
                return '自动回复'
                break;
            case 1:
                return '二维码海报'
                break;
            case 2:
                return '关注现金红包'
                break;
            case 4:
                return '分类商品'
                break;
            case 5:
                return '具体商品'
                break;
            case 6:
                return '分类图文'
                break;
            case 7:
                return '具体图文'
                break;
            case 8:
                return '商城首页'
                break;
            case 9:
                return '提示语'
                break;
            case 10:
                return '微信图文'
                break;
            default:
        }
    }
    var query = function(){
        $.ajaxJsonGet(ajaxUrl.url1,null,{
            'done':function(res){
                if(res.code===0){
                    var data = {
                        items:res.data.subscribePushVos
                    }
                    _.each(data.items,function(k,v){
                            k.title = _getTitle(k.pushType)
                    })
                    if(res.data.isOpen==null){
                        _initSwitchButton('#activity_action',0);
                    }else{
                        _initSwitchButton('#activity_action',res.data.isOpen);
                    }
                    if(data.items.length>0){
                        $('#edit').val(1);
                        activityNum = data.items.length;
                    }
                    jumi.template('activity/activity_add_focus?status=2',data,function(tpl){
                        $('#activity_name').html(tpl);
                        $( "#activity_name" ).sortable({
                            stop:function(event, ui){
                                console.log(ui);

                            },
                            placeholder: "portlet-placeholder"
                        });
                        $( "#activity_name" ).disableSelection();
                        _validate();
                    })
                }
            }
        })
    }
    var _doEditSubmit = function(){
        var subscribePushCo = {};
        subscribePushCo.isOpen = $('input[name="activity_action"]').val();
        var focusArray = [];
        var IntervaltimeArray = [];
        var Intervaltimearray = [];
        $('div[id^="u-related_"]').each(function(i){
            var id = $(this).data('id');
            var type = $(this).data('type');
            var subscribepushvos = {};
            subscribepushvos.sort = i+1;
            subscribepushvos.pushType = type;
            subscribepushvos.pushContext = $('#focus_id_'+id).val();
            if(type===9){
                subscribepushvos.pushContext = $('#focus_content_'+id).val();
                subscribepushvos.pushName = "";
            }else{
                subscribepushvos.pushContext = $('#focus_id_'+id).val();
                subscribepushvos.pushName = $('#focus_content_'+id).text();
            }
            subscribepushvos.intervalSecond = $('#focus_time_'+id).val();
            focusArray.push(subscribepushvos);
        })
        $('input[name="Intervaltime"]').each(function(i){
            var v = 0;
            var doc = $(this);
            var value = Number(doc.val());
            IntervaltimeArray.push(value);
        })
        _.map(IntervaltimeArray,function(k,v){
            var num = 0;
            for(var i=0;i<=v;i++){
                num = num+(IntervaltimeArray[i]);
            }
            Intervaltimearray.push(num);
        })
        _.each(focusArray,function(k,v){
            k.totalSecond = Intervaltimearray[v];
        })
        subscribePushCo.subscribePushVos = focusArray;
        $.ajaxJsonPut(ajaxUrl.url1,subscribePushCo,{
            "done":function(res){
                if(res.code===0){
                    var dm = new dialogMessage({
                        type:1,
                        fixed:true,
                        msg:'修改成功',
                        isAutoDisplay:true,
                        time:3000
                    });
                    dm.render();
                    query();
                }
            }
        })
    }
    var _doSubmit = function () {
        var subscribePushCo = {};
        subscribePushCo.isOpen = $('input[name="activity_action"]').val();
        var focusArray = [];
        var IntervaltimeArray = [];
        var Intervaltimearray = [];
        $('div[id^="u-related_"]').each(function(i){
            var id = $(this).data('id');
            var type = $(this).data('type');
            var subscribepushvos = {};
            subscribepushvos.sort = i+1;
            subscribepushvos.pushType = type;
            subscribepushvos.pushContext = $('#focus_id_'+id).val();
            if(type===9){
                subscribepushvos.pushContext = $('#focus_content_'+id).val();
                subscribepushvos.pushName = "";
            }else{
                subscribepushvos.pushContext = $('#focus_id_'+id).val();
                subscribepushvos.pushName = $('#focus_content_'+id).text();
            }
            subscribepushvos.intervalSecond = $('#focus_time_'+id).val();
            focusArray.push(subscribepushvos);
        })
        $('input[name="Intervaltime"]').each(function(i){
            var v = 0;
            var doc = $(this);
            var value = Number(doc.val());
            IntervaltimeArray.push(value);
        })
        _.map(IntervaltimeArray,function(k,v){
            var num = 0;
            for(var i=0;i<=v;i++){
                num = num+(IntervaltimeArray[i]);
            }
            Intervaltimearray.push(num);
        })
        _.each(focusArray,function(k,v){
            k.totalSecond = Intervaltimearray[v];
        })
        subscribePushCo.subscribePushVos = focusArray;
        $.ajaxJson(ajaxUrl.url1,subscribePushCo,{
            "done":function(res){
                if(res.code===0){
                    var dm = new dialogMessage({
                        type:1,
                        fixed:true,
                        msg:'保存成功',
                        isAutoDisplay:true,
                        time:3000
                    });
                    dm.render();
                    query();
                }
            }
        })
    }
    var _init = function(){
        _initActivityPage();//获取左边菜单
        query();
        _bind();
    }

    var _bind = function(){
        var flag = true;
        $('#activity_save').click(function(){
            var value = $('#edit').val();
            var isOpen = $('input[name="activity_action"]').val();
            var form = $('#activity_form');
            $('input[id^="focus_id_"]').each(function(){
                var id = $(this).attr('id');
                var nid = $(this).data('id');
                var contentId = 'focus_content_'+nid;
                var value = $(this).val();
                if(!value){
                    jumi.tipDialog(contentId,'关注推送的任务还未设置');
                    flag = false;
                    return false;
                }else{
                    flag = true;
                }
            })
            if(!flag){
                return;
            }
            if(form.valid()){
                if(value==='1'){
                    _doEditSubmit();
                }else{
                    _doSubmit();
                }
            }
        })
    };


    
    return {
        init: _init,
        addSub:_addSub,
        delSub:_delSub,
        selectTask:_selectTask
    };
})();