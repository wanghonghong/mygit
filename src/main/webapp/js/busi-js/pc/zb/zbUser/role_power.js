/**
 * Created by BenRay on 16/8/8.
 * 官方图文
 */
CommonUtils.regNamespace("zbRole", "power");
zbRole.power = (function(){
    var ajaxUrl = {
        url1:CONTEXT_PATH+'/zb/role',
        url2:CONTEXT_PATH+'/zb/roles',
        url3:CONTEXT_PATH+'/zb/resource_role',
        url4:CONTEXT_PATH+'/zb/role_type',
        url5:CONTEXT_PATH+'/zb/role_types',
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
        //角色管理分页
        var url2 = ajaxUrl.url2;
        var params = {
            pageSize: 10,
        };
        jumi.pagination('#roleToolbar', url2, params, function (res, curPage) {
            if (res.code === 0) {
                var data = {
                    items: res.data.items
                };
                jumi.template('zb/user/role_power_list', data, function (tpl) {
                    $('#zbRoleList').html(tpl);
                })
            }
        });

        //修改
        $(".dialog-xg").click(function() {
            var elemxg = document.getElementById('dialoginfo-xg');
            dialog({
                title: "修改角色",
                content: elemxg,
            }).width(260).showModal();
        });
        //删除
        $(".dialog-sc").click(function() {
            var elemsc = document.getElementById('dialoginfo-sc');
            dialog({
                title: "操作提醒",
                content: elemsc,
            }).width(280).showModal();
        });
        //查看权限
        $(".dialog-ckqx").click(function() {
            var elemckqx = document.getElementById('dialoginfo-ckqx');
            dialog({
                title: "权限配置",
                content: elemckqx,
            }).width(240).showModal();
        });

        $('#delete').click(function () {
            var args = {};
            var roles = [];
            $('input[name="role"]:checked').each(function(){
                roles.push($(this).val());
            })
            var length = roles.length;
            var url = ajaxUrl.url1;
            var jsonData = JSON.stringify(roles);
            if(length>0){
                args.fn1 = function(){
                    $.ajaxJsonDel(url,jsonData,{
                        "done": function (res) {
                            if (res.code === 0) {
                                $('.one-level-memu.z-sel').trigger('click');
                            }
                        }
                    })
                };
                args.fn2 = function(){
                };
                jumi.dialogSure('是否确认删除当前角色?',args);
            }else {
                var dm = new dialogMessage({
                    type:1,
                    fixed:true,
                    msg:'请勾选要删除的角色',
                    isAutoDisplay:true,
                    time:1500,
                });
                dm.render();
            }

        })
        $('#zbRoleList').on('click','.deleteRole',function () {
            var args = {};
            var roles = [];
            roles.push($(this).data('id'));
            var name = $(this).data('name')
            var url = ajaxUrl.url1;
            var jsonData = JSON.stringify(roles);
            args.fn1 = function(){
                $.ajaxJsonDel(url,jsonData,{
                    "done":function (res) {
                        if(res.code===0) {
                            $('.one-level-memu.z-sel').trigger('click');
                        }
                    }
                })
            };
            args.fn2 = function(){
            };
            jumi.dialogSure('是否确认删除角色-'+name+'?',args);
        })
        //更新角色
        $('#zbRoleList').on('click','.updateRole',function () {
            $('.g-popup-box2').removeClass('dis-none');
            $('#role-btn').addClass('dis-none');
            $('#roleName').val("");
            var id = $(this).data('id');
            $('#sure4').unbind('click').bind('click',function () {
                var roleUo = {};
                roleUo.roleId = id;
                roleUo.roleName =  $('#roleName').val();
                if(roleUo.roleName){
                    var jsonData = JSON.stringify(roleUo);
                    var url = ajaxUrl.url1;
                    $.ajaxJsonPut(url,jsonData,{
                        "done":function (res) {
                            if(res.code===0) {
                                $("#leftMenuUl").find('.dropdown-toggle').eq(2).trigger('click');
                            }
                        }
                    })
                }else {
                    $('#role-btn').removeClass('dis-none');
                }
            })
            $('#cancle4').click(function () {
                $('.g-popup-box2').addClass('dis-none');
            })
        })
        $('#zbRoleList').on('click','.readPower',function () {
            $('.g-popup-box1').removeClass('dis-none');
        });

        //类型管理js
        var url5 = ajaxUrl.url5;
        var params = {
            pageSize: 10,
        };
        jumi.pagination('#typeToolbar', url5, params, function (res, curPage) {
            if (res.code === 0) {
                var data = {
                    items: res.data.items
                };
                jumi.template('zb/user/role_type_list', data, function (tpl) {
                    $('#typeList').html(tpl);
                })
            }
        });

        $('#typeList').on('click','.deleteType',function () {
            var args = {};
            var name = $(this).data('name');
            var id = $(this).data('id')
            var url = ajaxUrl.url4+'/'+id;
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
                                isAutoDisplay:true,
                                time:1500,
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
            jumi.dialogSure('是否确认删除类型-'+name+'?',args);
        })
    }
    //新增角色
    var _saveRole = function () {
        var url = ajaxUrl.url4;
        $.ajaxJsonGet(url,null,{
            "done":function (res) {
                var data = {
                    item : res.data,
                }
                jumi.template('zb/user/role_save_dialog',data,function (tpl) {
                    var d = dialog({
                        title: '新增角色',
                        content: tpl,
                        width: 280,
                        onshow: function () {
                            $("#type").select2({
                                theme: "jumi"
                            });
                            $('#cancel1').click(function () {
                                d.remove().close();
                            })
                            $('#saveRole').click(function () {
                                var url = ajaxUrl.url1;
                                var roleName = $('#addRoleName').val();
                                var type = $('#type').find('option:selected').val();
                                var data = {
                                    roleName : roleName,
                                    roleType:type,
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
                                                $('.one-level-memu.z-sel').trigger('click');
                                            })
                                            $('button[i="close"]').click(function () {
                                                d.close().remove();
                                                $('.one-level-memu.z-sel').trigger('click');
                                            })
                                        }
                                    }
                                })
                            })
                        }
                    }).showModal();
                })
            }
        })
    }
    //更新角色
    var _updateRole = function (id) {
        var url = ajaxUrl.url1+'/'+id;
        $.ajaxJsonGet(url,null,{
            "done":function (res) {
              var data = {
                  item : res.data,
                  types : res.data.zbRoleTypes,
              }
              jumi.template('zb/user/role_update_dialog',data,function (tpl) {
                    var d = dialog({
                        title: '修改角色',
                        content: tpl,
                        width: 280,
                        onshow: function () {
                            $("#type2").select2({
                                theme: "jumi"
                            });
                            $('#cancel2').click(function () {
                                d.remove().close();
                            })
                            $('#updateRole').click(function () {
                                var roleUo = {};
                                roleUo.id = id;
                                roleUo.roleName =  $('#roleName').val();
                                roleUo.roleType = $('#type2').find('option:selected').val();
                                var jsonData = JSON.stringify(roleUo);
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
                                                $('.one-level-memu.z-sel').trigger('click');
                                            })
                                            $('button[i="close"]').click(function () {
                                                d.close().remove();
                                                $('.one-level-memu.z-sel').trigger('click');
                                            })
                                        }
                                    }
                                })
                            })
                        }
                    }).showModal();
                })
            }
        })
    }
    //配置权限
    var _setRole = function (id) {
        var data = {
            roleId : id,
        }
        jumi.template('zb/user/role_set_dialog',data,function (tpl) {
            var d = dialog({
                title: '配置角色',
                id:'role_set_dialog',
                content: tpl,
                width: 320,
                onshow:function () {
                    $('#cancelPower').click(function () {
                        d.remove().close();
                    })
                }
            }).showModal();
        })
    }
    //点击保存权限
    var _savePower = function (id) {
        var treeObj = $.fn.zTree.getZTreeObj("resource_tree");
        var nodes = treeObj.getCheckedNodes(true);
        var resourceIds = _getResourceIds(nodes);
        var data = {
            resourceIds:resourceIds,
            roleId:id,
        }
        var url = ajaxUrl.url3;
        $.ajaxJson(url,data,{
            "done":function (res) {
                if (res.code===0){
                    var dm = new dialogMessage({
                        type:1,
                        fixed:true,
                        msg:'保存成功',
                        isAutoDisplay:false,
                    });
                    dm.render();

                    $('button[i-id="ok"]').click(function () {
                        dialog.get('role_set_dialog').close().remove();
                    })
                    $('button[i="close"]').click(function () {
                        dialog.get('role_set_dialog').close().remove();
                    })
                }
            }
        })
    }

    var zTree;
    var setting = {
        view: {
            dblClickExpand: false,//双击节点时，是否自动展开父节点的标识
            showLine: true,//是否显示节点之间的连线
            fontCss:{'color':'black','font-weight':'bold'},//字体样式函数
            selectedMulti: false //设置是否允许同时选中多个节点
        },
        check:{
            chkboxType: { "Y": "ps", "N": "ps" },
            chkStyle: "checkbox",//复选框类型
            enable: true //每个节点上是否显示 CheckBox
        },
        data: {
            simpleData: {//简单数据模式
                enable:true,
                idKey: "id",
                pIdKey: "pid",
                rootPId: "-1"
            }
        },
        callback: {
            beforeClick: function(treeId, treeNode) {
                zTree = $.fn.zTree.getZTreeObj("resource_tree");
                if (treeNode.isParent) {
                    zTree.expandNode(treeNode);//如果是父节点，则展开该节点
                }else{
                    zTree.checkNode(treeNode, !treeNode.checked, true, true);//单击勾选，再次单击取消勾选
                }
            }
        }
    };
    /**---------------------- 请求菜单数据------------------------*/
    var _initResource = function(){
        var treeNodes;
        var url=CONTEXT_PATH+"/zb/get_resource_list";
        $.ajaxJson(url,null,{
            "done":function (res) {
                if(res.code===0){
                    treeNodes = res.data;//把后台封装好的简单Json格式赋给treeNodes
                    var t = $("#resource_tree");
                    $.fn.zTree.init(t, setting, treeNodes);
                    _searchResourceIds();

                }
            }
        })
        _selectAll();
    }
    var _selectAll = function () {
        $("#selectAll").click(function () {
            var tag = $("#selectAll").prop("checked");
            var treeObj = $.fn.zTree.getZTreeObj("resource_tree");
            if (tag) {
                treeObj.checkAllNodes(true);
            } else {
                treeObj.checkAllNodes(false);
            }
        });
    }
    /**获取配置菜单id*/
    var _getResourceIds=function(nodes){
        var resourceIds=[];
        if (nodes.length === 0) {
            var dm = new dialogMessage({
                type:1,
                fixed:true,
                msg:'请勾选配置菜单！',
                isAutoDisplay:false,
            });
            dm.render();
            return;
        }
        $.each(nodes,function () {
            resourceIds.push(this.id);
        })
        return resourceIds;
    }
    /**获取对应角色配置菜单ids*/
    var _searchResourceIds=function(){
        var url = ajaxUrl.url3+'/'+$('#getRoleId').val();
        $.ajaxJsonGet(url,null,{
            "done":function (res) {
                var str = res.data.cause;
                var treeObj = $.fn.zTree.getZTreeObj("resource_tree");
                if (str!=null&&str!="" &&str!=="-1") {
                    var nodesArr = str.split(",");
                    for (var i = 0, l = nodesArr.length; i < l; i++) {
                        var node = treeObj.getNodeByParam("id", nodesArr[i], null);
                        treeObj.checkNode(node, true, false, false);
                    }
                }
            }
        })
    }
    //新增类型
    var _saveType = function () {
        jumi.template('zb/user/role_type_save_dialog',function (tpl) {
            var d = dialog({
                title: '新增类型',
                content: tpl,
                width: 280,
                onshow: function () {
                    $('#cancel1').click(function () {
                        d.remove().close();
                    })
                    $('#saveType').click(function () {
                        var url = ajaxUrl.url4;
                        var typeName = $('#addTypeName').val();
                        var data = {
                            typeName : typeName,
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
    //更新类型
    var _updateType = function (id) {
        jumi.template('zb/user/role_type_update_dialog',function (tpl) {
            var d = dialog({
                title: '修改类型',
                content: tpl,
                width: 280,
                onshow: function () {
                    $('#cancel2').click(function () {
                        d.remove().close();
                    })
                    $('#updateType').click(function () {
                        var typeUo = {};
                        typeUo.id = id;
                        typeUo.typeName =  $('#typeName').val();
                        var jsonData = JSON.stringify(typeUo);
                        var url = ajaxUrl.url4;
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
        saveRole:_saveRole,
        updateRole:_updateRole,
        setRole:_setRole,
        getResourceIds:_getResourceIds,
        savePower:_savePower,
        initResource:_initResource,
        searchResourceIds:_searchResourceIds,
        saveType:_saveType,
        updateType:_updateType,
    };
})();


