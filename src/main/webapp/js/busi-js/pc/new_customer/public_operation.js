CommonUtils.regNamespace("customer", "operation");
customer.operation = (function(){

    var _init = function(){
        var url = CONTEXT_PATH+'/wx/level';
        //查询等级列表
        $.ajaxHtmlGet(url,null,{
            done:function(res) {
                $("#customerAllD").empty();
                for (var i=0;i<res.data.length;i++){
                    var levelName= res.data[i].levelName;
                    var id =  res.data[i].id;
                    var div = '<div class="u-txt">' +
                        ' <div class="u-rb">' +
                        '<input type="radio" name="levelNameD" id="radioBoxD'+id+'" value="'+id+'" />' +
                        '<label for="radioBoxD'+id+'"></label>' +
                        '</div>' +
                        ' <label for="radioBoxD'+id+'" class="u-grade">'+levelName+'</label>' +
                        '</div>';
                    $("#customerAllD").append(div);
                }
            }
        });

        //查询分组列表
        var url1=CONTEXT_PATH+'/wx/groups';
        var groups;
        $.ajaxHtmlGet(url1,null,{
            done:function(res) {
                $("#groupDD").empty();
                for (var i=0;i<res.data.length;i++){
                    var name= res.data[i].name;
                    var id =  res.data[i].id;
                    var groupid =  res.data[i].groupid;
                    var div ='<div class="u-txt" onclick="customer.operation.labelInput(this)">' +
                                '<div class="u-rb">' +
                                    '<input type="radio" name="groupNameD" id="radioBox'+groupid+'" value="'+groupid+'" />' +
                                    '<label for="radioBox'+groupid+'"></label>' +
                                '</div>' +
                                '<input type="text" class="u-grade" value="'+name+'" disabled />' +
                                '<div class="u-layer-operate">' +
                                    '<i class="iconfont icon-modified" onclick="customer.operation.modifyGroup(this)"></i>' +
                                    '<i class="iconfont icon-delete" onclick="customer.operation.delGroup('+groupid+')"></i>' +
                                '</div>' +
                            '</div>';
                    $("#groupDD").append(div);
                }

            }
        });

        $(".layer-1").hide();
        $(".m-btn-layer").hide();
        $("#newGroupNameDD").val("");
        $("#newGroupDD").attr("checked",false);
        $("#check4").attr("checked",false);
    };

    //等级/分组Div
    var _plLevelDialog = function (d) {
        $(".layer-1").hide();
        $(".m-btn-layer").hide();
        $(d).next().show();
    };


    //批量修改分组
    var _plSaveGroup = function (d) {
        var userIds =[];
        $('input[name="userIdCheckBox"]:checked').each(function(){
            userIds.push($(this).val());
        });
        var newgroup= $('input[name="groupNameD"]:checked').val();
        var newgroupName= $('#newGroupNameDD').val();
        //新增分组
        if(newgroupName!=''){
            var url_path = CONTEXT_PATH+'/wx/group';
            var data= {
                name:newgroupName
            };
            if(newgroup=='new'){
                if(userIds.length==0){
                    alert("请选择用户！");
                    return;
                }
                data.userIds = userIds.toString();
            }
            $.ajaxJson(url_path,data,{
                done:function(res){
                    reload();
                    _init();
                }
            });
            return;
        }

        if(userIds.length==0){
            alert("请选择用户！");
            return;
        }
        var groupId= $('input[name="groupNameD"]:checked').val();
        if(groupId==undefined){
            var dm = new dialogMessage({
                type:2,
                fixed:true,
                msg:'请选择分组！',
                isAutoDisplay:true,
                time:1500
            });
            dm.render();
            return;
        }


        //用户分组更新地址
        var groupUpdateUrl= CONTEXT_PATH+'/wx/group/move';
        var checkDiaro = $('input[name="groupNameD"]:checked ');
        var groupName =  $(checkDiaro).parent().parent().find('input[type="text"]').val();
        var data = {
            groupid:groupId,
            userIds:userIds.toString()
        };
        _updater(groupUpdateUrl,data,function(res){
            console.log(res);
            if(res.data.errcode==="0"||res.data.errcode===0){
                reload();
                _init();
            }else{
                var dm = new dialogMessage({
                    type:2,
                    fixed:true,
                    msg:'操作失败',
                    isAutoDisplay:true,
                    time:1500
                });
                dm.render();
            }
        });


    };


    //批量修改等级
    var _plSaveLevel = function (d) {
        var userIds =[];
        $('input[name="userIdCheckBox"]:checked').each(function(){
            userIds.push($(this).val());
        });
        if(userIds.length==0){
            alert("请选择用户！");
            return;
        }
       var levelId= $('input[name="levelNameD"]:checked').val();
        if(levelId==undefined){
            alert("请选择等级！");
            return;
        }

        // 用户等级更新地址
        var levelUrl = CONTEXT_PATH+'/wx/level/move_user';
        var data ={
            id:levelId,
            wxUserIds:userIds.toString()
        };
        _updater(levelUrl,data,function(res){
            if(res.data.code==="0"||res.data.code===0){
                reload();
                _init();
            }else{
                var dm = new dialogMessage({
                    type:2,
                    fixed:true,
                    msg:'操作失败',
                    isAutoDisplay:true,
                    time:1500
                });
                dm.render();
            }
        });

    };

    var _levelDialog = function (userid) {
        $(".layer-1").hide();
        $(".m-btn-layer").hide();
        $("#levelDialog"+userid).show();
    };

    // 通用更新方法
    var _updater = function(url,params,callback){
        $.ajaxHtmlPut(url,params,{
            done:function(res){
                if(typeof(callback)==='function'){
                    callback(res);
                }
            }
        });
    };

    //保存等级
    var _saveLevel = function (userid) {
        // 用户等级更新地址
        var levelUrl = CONTEXT_PATH+'/wx/level/move_user';
        var levelId = $('input[name="levelName'+userid+'"]:checked ').val();
        var data ={
            id:levelId,
            wxUserId:userid
        };
        _updater(levelUrl,data,function(res){
            if(res.data.code==="0"||res.data.code===0){
                reload();
                _init();
            }else{
                var dm = new dialogMessage({
                    type:2,
                    fixed:true,
                    msg:'操作失败',
                    isAutoDisplay:true,
                    time:1500
                });
                dm.render();
            }
        });

    };


    var _groupDialog = function (userid) {
        $(".layer-1").hide();
        $(".m-btn-layer").hide();
        $("#groupDialog"+userid).show();
    };

    //删除分组
    var _delGroup = function (groupid) {
        var del_url = CONTEXT_PATH+'/wx/group/'+groupid;
        $.ajaxJsonDel(del_url,{
            done:function(){
                reload();
                _init();
            }
        });
    };

    //微博删除分组
    var _wbDelGroup = function (groupid) {
        var del_url = CONTEXT_PATH+'/wbUserGroup/group/'+groupid;
        $.ajaxJsonDel(del_url,{
            done:function(res){
                if(res.data.code==0){
                    reload();
                    _init();
                }else{
                    var dm = new dialogMessage({
                        type:2,
                        fixed:true,
                        msg:res.data.msg,
                        isAutoDisplay:true,
                        time:1500
                    });
                    dm.render();
                }
            }
        });
    };

    var _modifyGroup = function (d) {
        var input = $(d).parent().prev();
        $(input).removeAttr("disabled");
        $(input)[0].focus();
        $(input).blur(function(){
            $(this).attr("disabled","disabled");
            var groupName = $(this).val();
            var groupid = $(this).prev().find('input[type="radio"]').val();
            var url = CONTEXT_PATH+"/wx/group";
            var group = {
                name:groupName,
                groupid:groupid
            }
            $.ajaxJsonPut(url,group,function(res){
                console.log(res);
            })
        });
    };


    //保存分组
    var _saveGroup = function (userid,obj) {
        var top = $(obj).parent().parent();
        var newGroupVal = $(top).find(".h-scroll").find(".f-pb-s").find('input[type="text"]').val();
        var input = $(top).find('input[type="radio"]:checked').val();
        //新增分组
        if(newGroupVal!=''){
            var url_path = CONTEXT_PATH+'/wx/group';
            var data= {
                name:newGroupVal
            };
            if(input=='new'){
                data.userId = userid;
            }
            $.ajaxJson(url_path,data,{
                done:function(res){
                    reload();
                    _init();
                }
            });
            return;
        }

        //用户分组更新地址
        var groupUpdateUrl= CONTEXT_PATH+'/wx/group/move';
        var checkDiaro = $('input[name="groupName'+userid+'"]:checked ');
        var groupId = $(checkDiaro).val();
        var groupName =  $(checkDiaro).parent().parent().find('input[type="text"]').val();
        if(groupId=='undefined'||groupId==undefined||groupName==undefined||groupName=='undefined'){
            alert("请选择分组！");
            return;
        }
        var data = {
            groupid:groupId,
            userId:userid
        };
        _updater(groupUpdateUrl,data,function(res){
            console.log(res);
            if(res.data.errcode==="0"||res.data.errcode===0){
                reload();
                _init();
            }else{
                var dm = new dialogMessage({
                    type:2,
                    fixed:true,
                    msg:'操作失败',
                    isAutoDisplay:true,
                    time:1500
                });
                dm.render();
            }
        });
    };


    var _remarkDialog = function (userid) {
        $(".layer-1").hide();
        $(".m-btn-layer").hide();
        $("#remarkDialog"+userid).show();
    };

    var _saveRemark = function (userid) {
        // 用户备注更新地址
        var remarkUpdateUrl = CONTEXT_PATH+'/wx/user_remark';
        var remark = $("#remark"+userid).val();
        if(!remark||remark===''){
            _showCloseDialog(btn);
            return;
        }
        if(remark.replace(/[^\u0000-\u00ff]/g,"aa").length>22){
            var dm = new dialogMessage({
                type:2,
                fixed:true,
                msg:'备注名不能超过22个字符',
                isAutoDisplay:true,
                time:1500
            });
            dm.render();
            return ;
        }
        var data = {
            remark:remark,
            userId:userid
        };
        _updater(remarkUpdateUrl,data,function(res){
            if(res.data.errcode==="0"||res.data.errcode===0){
                reload();
                _init();
            }else{
                var dm = new dialogMessage({
                    type:2,
                    fixed:true,
                    msg:'操作失败',
                    isAutoDisplay:true,
                    time:1500
                });
                dm.render();
            }
        });
    };

    /**
     * 上级用户修改弹出框
     */
    var _modifyTopUser = function(userid){
        var url = CONTEXT_PATH+'/customer/wx_topuser/'+userid;
        $.ajaxJsonGet(url,null,{
            done:function(res) {
                var data = {
                    items:res.data,
                    userid:userid
                };
                jumi.template('new_customer/modify_wx_topuser',data,function(tpl){
                    $("#dialoginfo33").empty();
                    $('#dialoginfo33').html(tpl);
                    popMsg("上级客户");
                });
            }
        });
    };


    /**
     * 上级用户弹出框
     */
    var _topUserPopMsg = function(userid){
        var url = CONTEXT_PATH+'/customer/wx_topuser/'+userid;
        $.ajaxJsonGet(url,null,{
            done:function(res) {
                if(res.data.length == 0){
                    var dm = new dialogMessage({
                        type:2,
                        fixed:true,
                        msg:'该用户暂无上级',
                        isAutoDisplay:true,
                        time:1500
                    });
                    dm.render();
                    return;
                }
                var data = {
                    items:res.data
                };
                jumi.template('new_customer/wx_topuser',data,function(tpl){
                    $("#dialoginfo33").empty();
                    $('#dialoginfo33').html(tpl);
                    popMsg("上级客户");
                });
            }
        });
    };

    /**
     * 下级用户弹出框
     */
    var _lastUserPopMsg = function(wxuserid){
        var data = {
            wxuserid:wxuserid
        };
        jumi.template('new_customer/lastuser',data,function(tpl){
            $("#dialoginfo33").empty();
            $('#dialoginfo33').html(tpl);
            popMsg("下级客户");
        });
    };

    /**
     * 用户角色弹出框
     */
    var _userRolePopMsg = function(userid){
        var url = CONTEXT_PATH+'/customer/userRole/'+userid;
        $.ajaxHtmlGet(url,null,{
            done:function(res) {
                $("#dialoginfo33").empty();
                $(res.data).appendTo("#dialoginfo33");
                popMsg("升级角色");
            }
        });
    }


    function popMsg(title){
        var elem = $('#dialoginfo33');
        memudialog=dialog({
            id:'show-dialog',
            width: 880,
            // height: 500,
            title: title,
            content: elem
        });
        memudialog.show();
    };

    var _cancel = function(){
            $(".m-btn-layer").hide();
    };

    //永久二维码
    var _qrcode = function(userId){
        var url = CONTEXT_PATH+'/customer/forever_qrcode/'+userId;
        $.ajaxJson(url,null,{
            done:function(res) {
                if(res.data.code==0){
                    $("#qrcodeImg").attr("src",res.data.msg)
                    var elem = $('#dialoginfo22');
                    memudialog=dialog({
                        id:'show-dialog',
                        width: 300,
                        height: 300,
                        title: "永久二维码",
                        content: elem
                    });
                    memudialog.show();
                }
            }
        });
    };

    var _selAll = function (check) {
        var checkboxs=document.getElementsByName("userIdCheckBox");
        for (var i=0;i<checkboxs.length;i++) {
            var e=checkboxs[i];
            e.checked=!e.checked;
        }
    };
    var _labelInput = function(btn){
        var input = $(btn);
        input.find('input:radio').prop('checked',true);
    };

    /**
     * 微博用户上级用户弹出框
     */
    var _wbTopUserPopMsg = function(userid){
        var url = CONTEXT_PATH+'/customer/getWbUperUser/'+userid;
        $.ajaxJsonGet(url,null,{
            done:function(res) {
                if(res.data.length == 0){
                    var dm = new dialogMessage({
                        type:2,
                        fixed:true,
                        msg:'该用户暂无上级',
                        isAutoDisplay:true,
                        time:1500
                    });
                    dm.render();
                    return;
                }
                var data = {
                    items:res.data
                };
                jumi.template('new_customer/wb/wb_topuser',data,function(tpl){
                    $("#dialoginfo33").empty();
                    $('#dialoginfo33').html(tpl);
                    popMsg("上级客户");
                });
            }
        });
    };

    /**
     * 微博下级用户弹出框
     */
    var _wbLastUserPopMsg = function(id){
        var data = {
            id:id
        };
        jumi.template('new_customer/wb/wb_lastuser',data,function(tpl){
            $("#dialoginfo33").empty();
            $('#dialoginfo33').html(tpl);
            popMsg("下级客户");
        });
    };


    //保存微博用户分组
    var _saveWbGroup = function (userid,obj) {
        var top = $(obj).parent().parent();
        var newGroupVal = $(top).find(".h-scroll").find(".f-pb-s").find('input[type="text"]').val();
        var input = $(top).find('input[type="radio"]:checked').val();
        //新增分组
        if(newGroupVal!=''){
            var url_path = CONTEXT_PATH+'/wbUserGroup/group';
            var data= {
                name:newGroupVal
            };
            if(input=='new'){
                data.relId = userid;
            }
            $.ajaxJson(url_path,data,{
                done:function(res){
                    if(res.data.code==0){
                        reload();
                        _init();
                    }else{
                        var dm = new dialogMessage({
                            type:2,
                            fixed:true,
                            msg:res.data.msg,
                            isAutoDisplay:true,
                            time:1500
                        });
                        dm.render();
                    }

                }
            });
            return;
        }

        //用户分组更新地址
        var groupUpdateUrl= CONTEXT_PATH+'/wbUserGroup/group/move';
        var checkDiaro = $('input[name="groupName'+userid+'"]:checked ');
        var groupId = $(checkDiaro).val();
        var groupName =  $(checkDiaro).parent().parent().find('input[type="text"]').val();
        if(groupId=='undefined'||groupId==undefined||groupName==undefined||groupName=='undefined'){
            var dm = new dialogMessage({
                type:2,
                fixed:true,
                msg:"请选择分组！",
                isAutoDisplay:true,
                time:1500
            });
            dm.render();
            return;
        }
        var data = {
            groupid:groupId,
            relId:userid
        };
        _updater(groupUpdateUrl,data,function(res){
            if(res.data.code==="0"||res.data.code===0){
                reload();
                _init();
            }else{
                var dm = new dialogMessage({
                    type:2,
                    fixed:true,
                    msg:res.data.msg,
                    isAutoDisplay:true,
                    time:1500
                });
                dm.render();
            }
        });
    };


    //保存等级
    var _saveWbLevel = function (userid) {
        // 用户等级更新地址
        var levelUrl = CONTEXT_PATH+'/wx/level/move_wb_user';
        var levelId = $('input[name="levelName'+userid+'"]:checked ').val();
        var data ={
            id:levelId,
            wbUserRelId:userid
        };
        _updater(levelUrl,data,function(res){
            if(res.data.code==="0"||res.data.code===0){
                reload();
                _init();
            }else{
                var dm = new dialogMessage({
                    type:2,
                    fixed:true,
                    msg:'操作失败',
                    isAutoDisplay:true,
                    time:1500
                });
                dm.render();
            }
        });

    };

    var _saveWbRemark = function (userid) {
        // 用户备注更新地址
        var remarkUpdateUrl = CONTEXT_PATH+'/wbUserGroup/user_remark';
        var remark = $("#remark"+userid).val();
        if(!remark||remark===''){
            _showCloseDialog(btn);
            return;
        }
        if(remark.replace(/[^\u0000-\u00ff]/g,"aa").length>22){
            var dm = new dialogMessage({
                type:2,
                fixed:true,
                msg:'备注名不能超过22个字符',
                isAutoDisplay:true,
                time:1500
            });
            dm.render();
            return ;
        }
        var data = {
            remark:remark,
            relId:userid
        };
        _updater(remarkUpdateUrl,data,function(res){
            if(res.data.code==="0"||res.data.code===0){
                reload();
                _init();
            }else{
                var dm = new dialogMessage({
                    type:2,
                    fixed:true,
                    msg:'操作失败',
                    isAutoDisplay:true,
                    time:1500
                });
                dm.render();
            }
        });
    };
    
    var _userDetail = function (userid) {
        var url = CONTEXT_PATH+'/customer/userDetail/'+userid;
        $.ajaxJsonGet(url,null, {
            done: function (res) {
                if(res.code==0){
                    var data = res.data;
                    console.log(data);
                    jumi.template('new_customer/userDetail',data,function(tpl){
                        $("#userDetail").empty();
                        $('#userDetail').html(tpl);
                        var elem = $('#userDetail');
                        memudialog=dialog({
                            id:'show-dialog88',
                            width: 880,
                            // height: 500,
                            title: "账户信息",
                            content: elem
                        });
                        memudialog.show();
                    });
                }
            }
        });


    };


    return {
        lastUserPopMsg:_lastUserPopMsg,
        topUserPopMsg:_topUserPopMsg,
        levelDialog:_levelDialog,
        saveLevel:_saveLevel,
        groupDialog :_groupDialog,
        saveGroup:_saveGroup,
        saveRemark:_saveRemark,
        remarkDialog:_remarkDialog,
        cancel:_cancel,
        delGroup:_delGroup,
        qrcode:_qrcode,
        selAll:_selAll,
        plLevelDialog:_plLevelDialog,
        plSaveLevel:_plSaveLevel,
        plSaveGroup:_plSaveGroup,
        labelInput:_labelInput,
        modifyGroup:_modifyGroup,
        userRolePopMsg:_userRolePopMsg,
        init:_init,
        wbTopUserPopMsg:_wbTopUserPopMsg,
        wbLastUserPopMsg:_wbLastUserPopMsg,
        modifyTopUser:_modifyTopUser,
        saveWbGroup:_saveWbGroup,
        wbDelGroup:_wbDelGroup,
        saveWbLevel:_saveWbLevel,
        saveWbRemark:_saveWbRemark,
        userDetail:_userDetail
    };
})();


