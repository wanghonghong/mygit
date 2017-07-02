/**
 * Created by BenRay on 16/8/8.
 * 官方图文
 */
CommonUtils.regNamespace("department", "post");
department.post = (function(){
    var ajaxUrl = {
        url1:CONTEXT_PATH+'/zb/department',
        url2:CONTEXT_PATH+'/zb/departments',
        url3:CONTEXT_PATH+'/zb/post',
        url4:CONTEXT_PATH+'/zb/posts',
    };
    var _init = function(){
        _bind();
    };
    var _bind = function () {
        $(".panel-hidden").hide().eq(0).show();
        var tabul = $("#m-tab ul li");
        tabul.click(function () {
            tabul.eq($(this).index()).addClass("z-sel").siblings().removeClass('z-sel');
            $(".panel-hidden").hide().eq($(this).index()).show();
        });
        //部门分页
        var url2 = ajaxUrl.url2;
        var params = {
            pageSize: 10,
        };
        jumi.pagination('#departmentToolbar', url2, params, function (res, curPage) {
            if (res.code === 0) {
                var data = {
                    items: res.data.items
                };
                data.items.type = 1;
                jumi.template('zb/user/department_post_list', data, function (tpl) {
                    $('#departmentList').html(tpl);
                })
            }
        });
        //岗位分页
        var url4 = ajaxUrl.url4;
        var params = {
            pageSize: 10,
        };
        jumi.pagination('#postToolbar', url4, params, function (res, curPage) {
            if (res.code === 0) {
                var data = {
                    items: res.data.items
                };
                data.items.type = 2;
                jumi.template('zb/user/department_post_list', data, function (tpl) {
                    $('#postList').html(tpl);
                })
            }
        });

        $('#departmentList').on('click','.deleteDepartment',function () {
            var args = {};
            var name = $(this).data('name');
            var id = $(this).data('id')
            var url = ajaxUrl.url1+'/'+id;
            args.fn1 = function(){
                $.ajaxJsonDel(url,{
                    "done":function (res) {
                        if(res.data.code===0) {
                            $('.two-level-memu.z-sel').trigger('click');
                        }else {
                            var dm = new dialogMessage({
                                type:1,
                                fixed:true,
                                msg:res.data.msg,
                                isAutoDisplay:false,
                            });
                            dm.render();
                            $('button[i-id="ok"]').click(function () {
                                d.close().remove();
                            })
                            $('button[i="close"]').click(function () {
                                d.close().remove();
                            })
                        }
                    }
                })
            };
            args.fn2 = function(){
            };
            jumi.dialogSure('是否确认删除部门-'+name+'?',args);
        })

        $('#postList').on('click','.deletePost',function () {
            var args = {};
            var name = $(this).data('name');
            var id = $(this).data('id')
            var url = ajaxUrl.url3+'/'+id;
            args.fn1 = function(){
                $.ajaxJsonDel(url,{
                    "done":function (res) {
                        if(res.data.code===0) {
                            $('.two-level-memu.z-sel').trigger('click');
                        }else {
                            var dm = new dialogMessage({
                                type:1,
                                fixed:true,
                                msg:res.data.msg,
                                isAutoDisplay:false,
                            });
                            dm.render();
                            $('button[i-id="ok"]').click(function () {
                                d.close().remove();
                            })
                            $('button[i="close"]').click(function () {
                                d.close().remove();
                            })
                        }
                    }
                })
            };
            args.fn2 = function(){
            };
            jumi.dialogSure('是否确认删除岗位-'+name+'?',args);
        })
    }
    //更新部门
    var _updateDepartment = function (id) {
        var data = {};
        data.type = 1;
        jumi.template('zb/user/department_post_update_dialog',data,function (tpl) {
            var d = dialog({
                title: '修改部门',
                content: tpl,
                width: 280,
                onshow: function () {
                    $('#cancel2').click(function () {
                        d.remove().close();
                    })
                    $('#updateDepartment').click(function () {
                        var departmentUo = {};
                        departmentUo.departmentId = id;
                        departmentUo.departmentName =  $('#departmentName').val();
                        var jsonData = JSON.stringify(departmentUo);
                        var url = ajaxUrl.url1;
                        $.ajaxJsonPut(url,jsonData,{
                            "done":function (res) {
                                if(res.code===0) {
                                    var dm = new dialogMessage({
                                        type:1,
                                        fixed:true,
                                        msg:'保存成功',
                                        isAutoDisplay:false,
                                    });
                                    dm.render();
                                    $('button[i-id="ok"]').click(function () {
                                        d.close().remove();
                                        $('.two-level-memu.z-sel').trigger('click');
                                    })
                                    $('button[i="close"]').click(function () {
                                        d.close().remove();
                                        $('.two-level-memu.z-sel').trigger('click');
                                    })
                                }
                            }
                        })
                    })
                }
            }).showModal();
        })
    }
    //新增部门
    var _saveDepartment = function () {
        var data = {};
        data.type = 1;
        jumi.template('zb/user/department_post_save_dialog',data,function (tpl) {
            var d = dialog({
                title: '新增部门',
                content: tpl,
                width: 280,
                onshow: function () {
                    $('#cancel1').click(function () {
                        d.remove().close();
                    })
                    $('#saveDepartment').click(function () {
                        var url = ajaxUrl.url1;
                        var departmentName = $('#addDepartmentName').val();
                        var data = {
                            departmentName : departmentName,
                        }
                        var jsonData = JSON.stringify(data);
                        $.ajaxJson(url,jsonData,{
                            "done": function (res) {
                                if (res.code === 0) {
                                    var dm = new dialogMessage({
                                        type:1,
                                        fixed:true,
                                        msg:'保存成功',
                                        isAutoDisplay:false,
                                    });
                                    dm.render();
                                    $('button[i-id="ok"]').click(function () {
                                        d.close().remove();
                                        $('.two-level-memu.z-sel').trigger('click');
                                    })
                                    $('button[i="close"]').click(function () {
                                        d.close().remove();
                                        $('.two-level-memu.z-sel').trigger('click');
                                    })
                                }
                            }
                        })
                    })
                }
            }).showModal();
        })
    }

    //新增岗位
    var _savePost = function () {
        var data = {};
        data.type = 2;
        jumi.template('zb/user/department_post_save_dialog',data,function (tpl) {
            var d = dialog({
                title: '新增岗位',
                content: tpl,
                width: 280,
                onshow: function () {
                    $('#cancel1').click(function () {
                        d.remove().close();
                    })
                    $('#savePost').click(function () {
                        var url = ajaxUrl.url3;
                        var postName = $('#addPostName').val();
                        var data = {
                            postName : postName,
                        }
                        var jsonData = JSON.stringify(data);
                        $.ajaxJson(url,jsonData,{
                            "done": function (res) {
                                if (res.code === 0) {
                                    var dm = new dialogMessage({
                                        type:1,
                                        fixed:true,
                                        msg:'保存成功',
                                        isAutoDisplay:false,
                                    });
                                    dm.render();
                                    $('button[i-id="ok"]').click(function () {
                                        d.close().remove();
                                        $('.two-level-memu.z-sel').trigger('click');
                                    })
                                    $('button[i="close"]').click(function () {
                                        d.close().remove();
                                        $('.two-level-memu.z-sel').trigger('click');
                                    })
                                }
                            }
                        })
                    })
                }
            }).showModal();
        })
    }
    //更新岗位
    var _updatePost = function (id) {
        var data = {};
        data.type = 2;
        jumi.template('zb/user/department_post_update_dialog',data,function (tpl) {
            var d = dialog({
                title: '修改岗位',
                content: tpl,
                width: 280,
                onshow: function () {
                    $('#cancel2').click(function () {
                        d.remove().close();
                    })
                    $('#updatePost').click(function () {
                        var postUo = {};
                        postUo.postId = id;
                        postUo.postName =  $('#postName').val();
                        var jsonData = JSON.stringify(postUo);
                        var url = ajaxUrl.url3;
                        $.ajaxJsonPut(url,jsonData,{
                            "done":function (res) {
                                if(res.code===0) {
                                    var dm = new dialogMessage({
                                        type:1,
                                        fixed:true,
                                        msg:'保存成功',
                                        isAutoDisplay:false,
                                    });
                                    dm.render();
                                    $('button[i-id="ok"]').click(function () {
                                        d.close().remove();
                                        $('.two-level-memu.z-sel').trigger('click');
                                    })
                                    $('button[i="close"]').click(function () {
                                        d.close().remove();
                                        $('.two-level-memu.z-sel').trigger('click');
                                    })
                                }
                            }
                        })
                    })
                }
            }).showModal();
        })
    }
    return {
        init :_init,
        bind:_bind,
        saveDepartment:_saveDepartment,
        updateDepartment:_updateDepartment,
        savePost:_savePost,
        updatePost:_updatePost,
    };
})();


