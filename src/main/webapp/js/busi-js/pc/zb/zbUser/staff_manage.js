/**
 * Created by BenRay on 16/8/8.
 * 官方图文
 */
CommonUtils.regNamespace("staff", "manage");
staff.manage = (function(){
    var ajaxUrl = {
        url1:CONTEXT_PATH+'/zb/userlist',
        url2:CONTEXT_PATH+'/zb/user',
        url3:CONTEXT_PATH+'/zb/staff_user',
        url4:CONTEXT_PATH+'/zb/user_list',
    };
    var _init = function(){
        _bind();
    };
    var _valid = function () {
        //表单验证
        $("#form1").validate({
            rules:{
                userName:{
                    required:true,
                    minlength:2
                },
                wxnum:"required",
            },
            messages:{
                userName:{
                    required:"请输入联系姓名",
                    minlength:"姓名不能少于2位"
                },
                wxnum:"请输入微信号",
            }
        });
    }
    var _bind = function(){
        //选择框插件
        $("#sex").select2({
            theme: "jumi"
        });
        $("#post").select2({
            theme: "jumi"
        });
        //敲击回车绑定事件
        $(document).keydown(function(e){
            if(e.keyCode==13){
                $("#search-btns").click();
            }
        });
        $('#department_').on('change',function () {
            $('#department_error').hide();
        })
        $('#post_').on('change',function () {
            $('#post_error').hide();
        })
        $('#role_').on('change',function () {
            $('#role_error').hide();
        })
        var url = ajaxUrl.url1;
        var params = {
            pageSize: 10,
            status:1
        };
        jumi.pagination('#staffToolbar', url, params, function (res, curPage) {
            if (res.code === 0) {
                var data = {
                    items: res.data.items
                };
                jumi.template('zb/user/staff_manage_list', data, function (tpl) {
                    $('#staffManageList').html(tpl);
                })
            }
        });
        $('#delete').click(function () {
            var args = {};
            var users = [];
            $('input[name="user"]:checked').each(function(){
                users.push($(this).val());
            })
            var length = users.length;
            var url = ajaxUrl.url4;
            var jsonData = JSON.stringify(users);
            if(length>0){
                args.fn1 = function(){
                    $.ajaxJsonDel(url,jsonData,{
                        "done": function (res) {
                            $(".m-memu").find('.two-level-memu.z-sel').trigger('click');
                        }
                    })
                };
                args.fn2 = function(){
                };
                jumi.dialogSure('是否确认删除当前角色?',args);
            }else {
                var dm = new dialogMessage({
                    type:2,
                    fixed:true,
                    msg:"请勾选要删除的角色",
                    isAutoDisplay:true,
                    time:1500,
                });
                dm.render();
            }
        })

        $('#search-btns').click( function(){
            var userName = $("#userName").val();
            var phoneNumber = $("#phoneNumber").val();
            var staffCode = $("#staffCode").val();
            var sex = $('#sex').find('option:selected').val();
            var wxnum = $("#wxnum").val();
            var params = {
                pageSize:10,
                userName : userName,
                phoneNumber : phoneNumber,
                staffCode : staffCode,
                sex : sex,
                wxnum : wxnum,
                status:1
            };
            jumi.pagination('#staffToolbar', url, params, function (res, curPage) {
                if (res.code === 0) {
                    var data = {
                        items: res.data.items
                    };
                    jumi.template('zb/user/staff_manage_list', data, function (tpl) {
                        $('#staffManageList').html(tpl);
                    })
                }
            });
        });
        $('#staffManageList').on('click','.edit',function(){
            var self = $(this);
            var userId = self.attr('data-type-id');
            var url = ajaxUrl.url2+'/'+userId;
            $.ajaxJson(url,{
                "done":function (res) {
                    var data = {
                        item:res.data,
                        zbDepartmentList:res.data.zbDepartmentList,
                        zbPostList:res.data.zbPostList,
                        zbRoleList:res.data.zbRoleList,
                    };
                    jumi.template('zb/user/staff_manage_dialog',data,function (tpl) {
                        var d = dialog({
                            title: '员工审核',
                            content:tpl,
                            width:430,
                            onshow:function () {
                                $("#post_").select2({
                                    theme: "jumi"
                                });
                                $("#department_").select2({
                                    theme: "jumi"
                                });
                                $("#sex_").select2({
                                    theme: "jumi"
                                });
                                $("#role_").select2({
                                    theme: "jumi"
                                });
                                $('#update_btn').click(function () {
                                    _valid();
                                    if ($('#form1').valid()){
                                        var userUo = {};
                                        userUo.userName = $('#userName_').val();
                                        userUo.sex = $('#sex_').find('option:selected').val();
                                        userUo.phoneNumber = $('#phoneNumber_').val();
                                        userUo.wxnum = $('#wxnum_').val();
                                        userUo.staffCode = $('#staffCode_').val();
                                        userUo.department = $('#department_').find('option:selected').val();
                                        if (!userUo.department){
                                            $('#department_error').css('display','block');
                                            return;
                                        }
                                        userUo.post = $('#post_').find('option:selected').val();
                                        if (!userUo.post){
                                            $('#post_error').css('display','block');
                                            return;
                                        }
                                        userUo.roleId = $('#role_').find('option:selected').val();
                                        if (!userUo.roleId){
                                            $('#role_error').css('display','block');
                                            return;
                                        }
                                        userUo.userId = userId;
                                        var url = ajaxUrl.url3;
                                        var jsonData = JSON.stringify(userUo);
                                        $.ajaxJsonPut(url,jsonData,{
                                            "done":function (res) {
                                                if(res.code===0){
                                                    var dm = new dialogMessage({
                                                        type:1,
                                                        fixed:true,
                                                        msg:"保存成功",
                                                        isAutoDisplay:false,
                                                    });
                                                    dm.render();
                                                    $(".m-memu").find('.two-level-memu.z-sel').trigger('click');
                                                    d.close().remove();
                                                }
                                            }
                                        });
                                    }
                                });
                            },
                            onremove: function () {
                                jumi.msg('对话框已销毁');
                            }
                        });
                        d.showModal();
                    })
                }
            });
        });
        $('#staffManageList').on('click','.delete_btn',function(){
            var args = {};
            var userId = $(this).attr('data-type-id');
            var url = ajaxUrl.url2+'/'+userId;
            args.fn1 = function(){
                $.ajaxJsonDel(url,null,{
                    "done":function (res) {
                        if(res.code===0) {
                            $(".m-memu").find('.two-level-memu.z-sel').trigger('click');
                        }
                    }
                })
            };
            args.fn2 = function(){
            };
            jumi.dialogSure('是否确认删除?',args);
        });




    }

    return {
        init :_init
    };
})();

