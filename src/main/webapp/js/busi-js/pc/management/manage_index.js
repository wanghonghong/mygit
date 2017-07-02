CommonUtils.regNamespace("manage", "material");
manage.material = (function() {
    var ajaxUrl = {
        url1: CONTEXT_PATH + '/upload_file',//上传多文件
        url2: CONTEXT_PATH +'/upload_file/findAll', //获取资源 1 图片 2 视频 3音频
        url3: CONTEXT_PATH +'/shop_res_group',//获取分组
        url4: CONTEXT_PATH+'/resource'//文件资源查询 上传
    };
    var _init = function () {
        _bind();
        _query();
        _initQuery(1,0,true);//查询素材内容
        _initGroup(1,0);//查询分组方法
        _update();
        _upload();
        _musicplay();
    };


    var _bind = function(){
        $('#management-left').on('click','#manage_group li',function(event){
            event.stopPropagation();
            var id = $(this).data('id');
            var parent = $(this).parent();
            var index = $('#manage_tab').find('.z-sel').data('index');
            if(id!==0){
                if(index===1){
                    $('#manage_add_1').show();
                }else{
                    $('#manage_add_1').hide();
                }
                if(index===2){
                    $('#manage_add_2').show();
                }else{
                    $('#manage_add_2').hide();
                }
                if(index===3){
                    $('#manage_add_3').show();
                }else{
                    $('#manage_add_3').hide();
                }
            }else{
                $('#manage_add_1').hide();
                $('#manage_add_2').hide();
                $('#manage_add_3').hide();
            }
            parent.siblings().find('li').removeClass('active');
            $(this).addClass('active');
            $('#manage_group').find('.icon-delete1').hide();
            $('#manage_delete_'+id).show();
            _initQuery(index,id,false);
        });
        $('#management-left').on('click','#manage_add_group',function(event){
            $('#m_add_group').show();
        });
        $('#management-left').on('click','#m_del_cancel',function(){
            $('#m_remove_group').hide();
        });

        $('#management-left').on('click','#manage_remove_group',function () {
            var name = $('#manage_group').find('.active').data('tip');
            var id = $('#manage_group').find('.active').data('id');
            //判断是否是默认分组
            if(id===0){
                var dm = new dialogMessage({
                    type:1,
                    fixed:true,
                    msg:'这个分组无法删除',
                    isAutoDisplay:true,
                    time:3000
                });
                dm.render();
                return;
            }
            $('#m_remove_group').show();
            $('#g_name').text(name);
        });
        //取消分组
        $('#management-left').on('click','#m-add-cancel',function(){
            $('#m_add_group').hide();
        });
        //重命名素材分组
        $('#management-left').on('click','#manage_modify_group',function(){
            var index = $('#manage_tab').find('.z-sel').data('index');
            var id = $('#manage_group').find('.active').data('id');
            var flag = $('#manage_group').find('.active').data('flag');
            if(flag==='N'){
                var dm = new dialogMessage({
                    type:3,
                    fixed:true,
                    msg:'这个分组无法重命名!',
                    isAutoDisplay:true,
                    time:3000
                });
                dm.render();
                return;
            }
            if(id===0){
                var dm = new dialogMessage({
                    type:3,
                    fixed:true,
                    msg:'这个分组无法重命名!',
                    isAutoDisplay:true,
                    time:3000
                });
                dm.render();
                return;
            }
            $('#m_modify_group_'+id).show();
        });
        //取消选择分组按钮
        $('#m_modify_group_cancel').click(function(){
            $('#m_modify-groups').hide();
            $('#m_choose_group').empty();
        });
        $('#m_modify_group_cancel-t').click(function(){
            $('#m_choose_group-t').empty();
            $('#m_modify-groups-t').hide();
        });

        //查看视频
        $('#management-right').on('click','div[id^="enlarge_"]',function(){
            var url = $(this).data('url');
            var tpl = '<iframe height=498 width=510 src="'+url+'" frameborder=0 "allowfullscreen"></iframe>';
            var d = dialog({
                title: '视频预览',
                content: tpl
            });
            d.showModal();
        });

        //确认分组
        $('#m_modify_group_share-t').click(function(){
            var id = $('#manage_group').find('.active').data('id');
            var index = $('#manage_tab').find('.z-sel').data('index');
            var ids = [];
            $('input[name="m_checkbox_resource"]:checked').each(function(){
                var val = $(this).data('id');
                ids.push(val);
            });
            var idsString = ids.join(',');
            if(!idsString){
                var dm = new dialogMessage({
                    type:3,
                    fixed:true,
                    msg:'请选择需要分组的素材!',
                    isAutoDisplay:true,
                    time:2000
                });
                dm.render();
                return;
            }
            var groupId = $('input[name="manage_group_radio"]:checked').data('id');
            var url = ajaxUrl.url3+'/changeGroup/'+idsString+'/'+groupId;
            $.ajaxJsonGet(url,null,{
                done:function (res) {
                    if(res.code===0){
                        _initQuery(index,id,true);
                        var dm = new dialogMessage({
                            type:1,
                            fixed:true,
                            msg:res.data.msg,
                            isAutoDisplay:true,
                            time:2000
                        });
                        dm.render();
                    }
                }
            })

        });
        $('#m_modify_group_share').click(function(){
            var id = $('#manage_group').find('.active').data('id');
            var index = $('#manage_tab').find('.z-sel').data('index');
            var ids = [];
            $('input[name="m_checkbox_resource"]:checked').each(function(){
                var val = $(this).data('id');
                ids.push(val);
            });
            var idsString = ids.join(',');
            if(!idsString){
                var dm = new dialogMessage({
                    type:3,
                    fixed:true,
                    msg:'请选择需要分组的素材!',
                    isAutoDisplay:true,
                    time:2000
                });
                dm.render();
                return;
            }
            var groupId = $('input[name="manage_group_radio"]:checked').data('id');
            var url = ajaxUrl.url3+'/changeGroup/'+idsString+'/'+groupId;
            $.ajaxJsonGet(url,null,{
                done:function (res) {
                    if(res.code===0){
                        _initQuery(index,id,true);
                        var dm = new dialogMessage({
                            type:1,
                            fixed:true,
                            msg:res.data.msg,
                            isAutoDisplay:true,
                            time:2000
                        });
                        dm.render();
                    }
                }
            })

        });
        //修改分组全选
        $('input[name="m_checkAll_group"]').click(function(){
            if(this.checked){
                $('input[name="m_checkbox_resource"]').prop('checked',true);
                $('input[name="m_checkAll_group-t"]').prop('checked',true);
            }else{
                $('input[name="m_checkbox_resource"]').prop('checked',false);
                $('input[name="m_checkAll_group-t"]').prop('checked',false);
            }
        });
        $('input[name="m_checkAll_group-t"]').click(function(){
            if(this.checked){
                $('input[name="m_checkAll_group"]').prop('checked',true);
                $('input[name="m_checkbox_resource"]').prop('checked',true);
            }else{
                $('input[name="m_checkAll_group"]').prop('checked',false);
                $('input[name="m_checkbox_resource"]').prop('checked',false);
            }
        });
        //删除分组
        $('#m_delete_group-t').click(function(){
            var id = $('#manage_group').find('.active').data('id');
            var index = $('#manage_tab').find('.z-sel').data('index');
            var ids = [];
            $('input[name="m_checkbox_resource"]:checked').each(function(){
                var val = $(this).data('id');
                ids.push(val);
            });
            var idsString = ids.join(',');
            var args = {};
            if(!idsString){
                var dm = new dialogMessage({
                    type:3,
                    fixed:true,
                    msg:'未选择需要删除的素材',
                    isAutoDisplay:true,
                    time:3000
                });
                dm.render();

                return;
            }
            args.fn1 = function(){
                var url = ajaxUrl.url4+'/'+idsString;
                $.ajaxJsonDel(url,{
                    "done":function (res) {
                        if(res.code===0){
                            var dm = new dialogMessage({
                                type:1,
                                fixed:true,
                                msg:res.data.msg,
                                isAutoDisplay:true,
                                time:3000
                            });
                            dm.render();
                            _initQuery(index,id,true);//初始化到聚米分组
                        }
                    }
                });
            };
            //关闭的时候初始化方法
            args.fn2 = function(){

            };
            jumi.dialogSure('确定删除图片素材吗?',args);
        });
        $('#m_delete_group').click(function(){
            var id = $('#manage_group').find('.active').data('id');
            var index = $('#manage_tab').find('.z-sel').data('index');
            var ids = [];
            $('input[name="m_checkbox_resource"]:checked').each(function(){
                var val = $(this).data('id');
                ids.push(val);
            });
            var idsString = ids.join(',');
            var args = {};
            if(!idsString){
                var dm = new dialogMessage({
                    type:3,
                    fixed:true,
                    msg:'未选择需要删除的素材',
                    isAutoDisplay:true,
                    time:3000
                });
                dm.render();
                return;
            }
            args.fn1 = function(){
                var url = ajaxUrl.url4+'/'+idsString;
                $.ajaxJsonDel(url,{
                    "done":function (res) {
                        if(res.code===0){
                            var dm = new dialogMessage({
                                type:1,
                                fixed:true,
                                msg:res.data.msg,
                                isAutoDisplay:true,
                                time:3000
                            });
                            dm.render();
                            _initQuery(index,id,true);//初始化到聚米分组

                        }
                    }
                });
            };
            //关闭的时候初始化方法
            args.fn2 = function(){

            };
            jumi.dialogSure('确定删除图片素材吗?',args);
        });
        //修改分组
        $('#m_modify_group').click(function(){
            $('#m_modify_group_cancel-t').trigger('click');
            $('#m_modify-groups').show();
            var index = $('#manage_tab').find('.z-sel').data('index');
            $.ajaxJsonGet(ajaxUrl.url3+'/'+index,null,{
                done:function (res) {
                    if(res.code===0){
                        var data = {
                            items:res.data
                        };
                        jumi.template('management/manage_group_list',data,function(tpl){
                            $('#m_choose_group').html(tpl);
                        })
                    }
                }
            })
        });
        $('#m_modify_group-t').click(function(){
            $('#m_modify_group_cancel').trigger('click');
            $('#m_modify-groups-t').show();
            var index = $('#manage_tab').find('.z-sel').data('index');
            $.ajaxJsonGet(ajaxUrl.url3+'/'+index,null,{
                done:function (res) {
                    if(res.code===0){
                        var data = {
                            items:res.data
                        };
                        jumi.template('management/manage_group_list',data,function(tpl){
                            $('#m_choose_group-t').html(tpl);
                        })
                    }
                }
            })
        });
        //保存新增加的分组
        $('#management-left').on('click','#m-add-save',function(){
            var groupName = $('input[name="group_name"]').val();
            var resType = $('#manage_tab').find('.z-sel').data('index');
            var id = $('#manage_group').find('.active').data('id');
            if($.trim(groupName).length<=0){
                var dm = new dialogMessage({
                    type:3,
                    fixed:true,
                    msg:'新建分组失败,未填写分组名称!',
                    isAutoDisplay:true,
                    time:3000
                });
                dm.render();
                return;
            }
            var params = {
                groupName:groupName,
                resType:resType
            };
            $.ajaxJson(ajaxUrl.url3,params,{
                "done":function (res) {
                    if(res.code===0){
                        var dm = new dialogMessage({
                            type:1,
                            fixed:true,
                            msg:res.data.msg,
                            isAutoDisplay:true,
                            time:3000
                        });
                        dm.render();
                        _initGroup(resType,id);
                        _initQuery(resType,id,false);
                    }
                },
                "fail":function (res) {

                }

            });

        });

        //删除单个分组方法
        $('#management-left').on('click','i[id^="manage_delete_"]',function(event){
            event.stopPropagation();
            var index = $('#manage_tab').find('.z-sel').data('index');
            var name = $('#manage_group').find('.active').data('tip');
            var id = $('#manage_group').find('.active').data('id');
            var args = {};
            //判断是否是默认分组
            if(id===0){
                var dm = new dialogMessage({
                    type:1,
                    fixed:true,
                    msg:'这个分组无法删除',
                    isAutoDisplay:true,
                    time:3000
                });
                dm.render();
                return;
            }
            args.fn1 = function(){
                $.ajaxJsonDel(ajaxUrl.url3+'/'+id,{
                    "done":function (res) {
                        if(res.code===0){
                            var dm = new dialogMessage({
                                type:1,
                                fixed:true,
                                msg:res.data.msg,
                                isAutoDisplay:true,
                                time:3000
                            });
                            dm.render();
                            _initGroup(index,0);
                            _initQuery(index,0,true);//初始化到聚米为谷
                        }
                    }
                });
            };
            args.fn2 = function(){
            };
            jumi.dialogSure('是否删除'+name+'这个分类',args);


        });
        //素材中心切换选项卡方法
        $('#manage_tab').find('li').click(function(){
            var index = $(this).data('index');
            $('#manage_add_'+index).show().siblings().hide();
            $(this).addClass('z-sel').siblings().removeClass('z-sel');
            _initQuery(index,0,true);
            _initGroup(index,0);
            _clearModifyGroup();
        })
    };
    //清空修改分组按钮
    var _clearModifyGroup = function(){
        $('#m_modify-groups').hide();
        $('#m_modify-groups-t').hide();
        $('#m_choose_group').children().remove();
        $('#m_choose_group-t').children().remove();
        $('input[name="m_checkAll_group-t"]').attr('checked',false);
        $('input[name="m_checkAll_group"]').attr('checked',false);
    };
    //素材分组
    var _initGroup = function(index,groupId){
        if(index===1){
            $('#manage_add_1').hide();
        }
        if(index===2){
            $('#manage_add_2').hide();
        }
        if(index===3){
            $('#manage_add_3').hide();
        }
        $.ajaxJsonGet(ajaxUrl.url3+'/'+index,null,{
            done:function (res) {
                if(res.code===0){
                    var data = {
                        items:res.data
                    };
                    var item = _.where(res.data, {'groupFlag':'N'});
                    jumi.template('management/manage_type_list',data,function(tpl){
                        $('#management-left').html(tpl);
                        $('li[id^="group_"]').removeClass('active');
                        if(groupId===0){
                            $('#group_'+item[0].id).addClass('active').click();
                        }else{
                            $('#group_'+groupId).addClass('active');
                        }
                    })
                }
            }
        })
    };
    //刷新列表
    var _refreshPage = function(){

    }
    //初始化查询
    var _initQuery = function(index,groupId,isFirstLoad){
        _clearModifyGroup();
        if(groupId===0 && isFirstLoad){
            return;
        }
        //var url = ajaxUrl.url2+'/resType/'+index+'/resGroupId/'+groupId;
        var url = ajaxUrl.url4+'/query';
        var params = {
            pageSize:18,
            resType:index,
            groupId:groupId
        };
        jumi.pagination('#pageToolbar',url,params,function(res,curPage){
            if(res.code===0){
                var data = {
                    items:res.data.items,
                    resType:index,
                    groupId:groupId
                };
                jumi.template('management/manage_image_list',data,function(tpl){
                    $('#management-right').html(tpl);
                    $("#management-left").show();
                })
            }
        })

    };
    //素材管理搜索框统一调用方法
    var searchBtnClick = function(){
        var name = $('input[name="goodsName"]').val();
        var index = $('#manage_tab').find('.z-sel').data('index');
        var groupId = $('input[name="manage_group_radio"]:checked').data('id');
        //var url = ajaxUrl.url1+'/findAll/resType/'+index;
        var url = ajaxUrl.url4+'/query';
        if(!$.trim(name)){
            _initGroup(index,0);
            _initQuery(index,0,false);
            $("#management-left").show();
            return;
        }
        var params = {
            pageSize:18,
            resName:name,
            resType:index,
            groupId:groupId
        };
        jumi.pagination('#pageToolbar',url,params,function(res,curPage){
            if(res.code===0){
                var data = {
                    items:res.data.items,
                    resType:index,
                    SearchBar:true
                };
                jumi.template('management/manage_image_list',data,function(tpl){
                    $('#management-right').html(tpl);
                    $("#management-left").hide();
                    $('#manage_add_1').hide();
                    $('#manage_add_2').hide();
                    $('#manage_add_3').hide();
                })
            }
        })
    };
    var _query = function(){
        //搜索框
        $('input[name="goodsName"]').keydown(function (event) {
            if(event.which===13){
                searchBtnClick();
            }
        });
        //搜索框点击事件
        $('#searchBtn').click(function(){
            searchBtnClick();
        });
    };
    var _update = function(){
        //关闭图片分组修改弹出框
        $('#management-left').on('click','div[id^="m_modify_cancel_"]',function(){
            var id = $(this).data('id');
            $('#m_modify_group_'+id).hide();
        });

        // //修改图片分组弹出框
        $('#management-left').on('click','div[id^="m_modify_share_"]',function(){
            var id = $(this).data('id');
            var name = $('#m_modify_group_'+id).find('input[name="m_modify_name"]').val();
            var resType = $('#manage_tab').find('.z-sel').data('index');
            var id = $('#manage_group').find('.active').data('id');
            if($.trim(name).length<=0){
                var dm = new dialogMessage({
                    type:3,
                    fixed:true,
                    msg:'分组名称不能为空!',
                    isAutoDisplay:true,
                    time:3000
                });
                dm.render();
                return;
            }
            $('#m_modify_group_'+id).hide();
            var params = {
                id:id,
                groupName:name
            };
            $.ajaxJsonPut(ajaxUrl.url3,params,{
                "done":function (res) {
                    if(res.code===0){
                        var dm = new dialogMessage({
                            type:1,
                            fixed:true,
                            msg:res.data.msg,
                            isAutoDisplay:true,
                            time:3000
                        });
                        dm.render();
                        _initGroup(resType,id);
                    }
                },
                "fail":function (res) {

                }

            });
        })
    };
    //音频播放
    var _musicplay = function(){
        $('#management-right').on('click','audio[id^="m_audio_"]',function(){
            var id = $(this).data('id');
            var playId = this.id;
            var doc = document.getElementById(playId);
            var flag = $('#checkBox_'+id).is(':checked');
            if(!flag){
                _musicStop();
                $('#checkBox_'+id).prop('checked',true);
                doc.play();
                $('#m_img_url_'+id).attr('src',jumi.config.cssPath+'/css/pc/img/music_play.jpg');
            }else{
                $('#checkBox_'+id).prop('checked',false);
                doc.pause();
                $('#m_img_url_'+id).attr('src',jumi.config.cssPath+'/css/pc/img/music_stop.png');
            }
        })
    };
    //停止所有的音频音乐
    var _musicStop = function(){
        $('audio[id^="m_audio_"]').each(function(){
            var id = $(this).data('id');
            var playId = this.id;
            $('#checkBox_'+id).prop('checked',false);
            var doc = document.getElementById(playId);
            doc.pause();
            $('#m_img_url_'+id).attr('src',jumi.config.cssPath+'/css/pc/img/music_stop.png');
        })
    };
    //上传图片方法
    var _upload = function () {
        $('#manage_add_1').click(function(){
            var id = $('#manage_group').find('.active').data('id');
            var index = $('#manage_tab').find('.z-sel').data('index');
            var filesList = [];
            jumi.template('management/manage_file_upload',function(tpl){
                var d = dialog({
                    title: '上传图片',
                    content:tpl,
                    id:'dialog_add_file',
                    width:720,
                    onshow:function () {
                        if(index===1){
                            var url = ajaxUrl.url4+"?group_id="+id+"&res_type=1";
                        }else{
                            var url = ajaxUrl.url1+'/uploadFiles/resType/'+index+'/resGroupId/'+id;
                            //var linkUrl = ajaxUrl.url1+'/upload_link_url/resType/'+index+'/resGroupId/'+id;
                        }
                        $('#m_pick').click(function(){
                            var form = $("#pick_form");
                            if (!form.valid()) {
                                $('body').animate({scrollTop:0},1000);
                            }else{
                                var url_name = $('input[name="network_picture"]').val();
                                var linkUrl = ajaxUrl.url4+"/url";
                                $('#m_pick').text('上传中');
                                var params = {
                                    resUrl:url_name,
                                    groupId:id,
                                    resType:1
                                };
                                $('#display_img').show();
                                $('#display_img').find('img').attr('src',url_name);
                                $.ajaxJson(linkUrl,params,{
                                    "done":function (res) {
                                        if(res.code===0){
                                            var dm = new dialogMessage({
                                                type:1,
                                                fixed:true,
                                                msg:res.data.msg,
                                                isAutoDisplay:true,
                                                time:3000
                                            });
                                            dm.render();
                                            d.close().remove();
                                            _initQuery(index,id,false);
                                        }
                                    },
                                    "fail":function (res) {
                                        var dm = new dialogMessage({
                                            type:2,
                                            fixed:true,
                                            msg:res.data.msg,
                                            isAutoDisplay:true,
                                            time:3000
                                        });
                                        dm.render();
                                    }

                                });
                            }
                        });
                        $('#m_manage_file').click(function(){
                            var filesListData = $('#files').data();
                            if($.isEmptyObject(filesListData)){
                                filesListData.files = [];
                            }
                            if(filesListData.files.length>0){
                                $('#m_manage_file').attr('disabled',true);
                                $('#m_manage_file').val('正在上传中');
                                $("#fileupload_add_1").fileupload('send', {files:filesListData.files,url:url}).always(function(res){
                                    if(res.code===1){
                                        var dm = new dialogMessage({
                                            type:2,
                                            fixed:true,
                                            msg:res.msg,
                                            isAutoDisplay:true,
                                            time:2000
                                        });
                                        dm.render();
                                        $('#m_manage_file').attr('disabled',false);
                                        $('#m_manage_file').val('确定');
                                    }else if(res.code===0){
                                        var dm = new dialogMessage({
                                            type:1,
                                            fixed:true,
                                            msg:'上传文件成功',
                                            isAutoDisplay:true,
                                            time:1000
                                        });
                                        dm.render();
                                        if(dialog.get('dialog_add_file')){
                                            dialog.get('dialog_add_file').close().remove();
                                        }
                                        _initQuery(index,id,false);
                                    } else{
                                        var dm = new dialogMessage({
                                            type:2,
                                            fixed:true,
                                            msg:'上传失败,格式错误or文件超过1M!!',
                                            isAutoDisplay:true,
                                            time:1000
                                        });
                                        dm.render();
                                        $('#m_manage_file').attr('disabled',false);
                                        $('#m_manage_file').val('确定');
                                    }

                                })

                            }else{
                                var dm = new dialogMessage({
                                    type:3,
                                    fixed:true,
                                    msg:'请选择要上传的图片!',
                                    isAutoDisplay:true,
                                    time:1000
                                });
                                dm.render();
                            }
                        });

                        $("#fileupload_add_1").fileupload({
                            dataType: 'json',
                            autoUpload: true,
                            add:function(e,data){
                                var tpl = '';
                                var reader = new FileReader();
                                reader.onload = (function(e) {
                                    if(data.files[0].size<1024*1024){
                                        tpl+='<li class="img" style="background-image:url('+this.result+')"><span class="iconfont icon-delete1 m_del" data-id="'+data.files[0].lastModified+'"></span></li>';
                                        $('#m_manage_upload').find('.add').before(tpl);
                                    }else{
                                        var dm = new dialogMessage({
                                            type:3,
                                            fixed:true,
                                            msg:'已过滤图片大小超过1m的图片!',
                                            isAutoDisplay:true,
                                            time:1000
                                        });
                                        dm.render();
                                    }
                                    $('.m_del').unbind('click').click(function(){
                                        var id = $(this).data('id');
                                        filesList = [];
                                        $(this).parent().remove();
                                        var fileDoList = [];
                                        var filesListN = $('#files').data();
                                        $.each(filesListN.files,function(k,v){
                                            if(v.lastModified == id){
                                                filesListN.files.splice(k,1);
                                                return false;
                                            }
                                        });
                                        $.each(filesListN.files,function(k,v){
                                            fileDoList.push(v);
                                            filesList.push(v);
                                        });
                                        $('#files').data('files',fileDoList);
                                    })
                                });
                                reader.readAsDataURL(data.files[0]);
                                if(data.files[0].size<1024*1024){
                                    filesList.push(data.files[0]);
                                }
                                $('#files').data('files',filesList);
                            }
                        })
                    }
                });
                d.showModal();
            })

        });
        //上传视频
        $('#manage_add_2').click(function(){
            jumi.template('management/manage_add_video',function(tpl){
                var d = dialog({
                    title: '上传视频',
                    content:tpl,
                    id:'dialog_add_video',
                    width:720,
                    height:300,
                    onshow:function () {
                        $('#m_video_btn').click(function(){
                            var form = $("#videoForm");
                            if (!form.valid()) {
                                $('body').animate({scrollTop:0},1000);
                            } else {
                                var id = $('#manage_group').find('.active').data('id');
                                var index = $('#manage_tab').find('.z-sel').data('index');
                                var url = ajaxUrl.url4+'/url';
                                var resName = $('input[name="video_name"]').val();
                                var txResUrl = $('input[name="tx_video_name"]').val();
                                var params = {
                                    resSubType:0,
                                    resName:resName,
                                    resUrl:txResUrl,
                                    resType:index,
                                    groupId:id
                                }
                                var json = JSON.stringify(params);
                                $.ajaxJson(url,json,{
                                    "done":function (res) {
                                        if(res.code===0){
                                            var dm = new dialogMessage({
                                                type:1,
                                                fixed:true,
                                                msg:res.data.msg,
                                                isAutoDisplay:true,
                                                time:3000
                                            });
                                            dm.render();
                                            dialog.get('dialog_add_video').close().remove();
                                            _initQuery(index,id,false);
                                        }
                                    },
                                    "fail":function (res) {
                                        var dm = new dialogMessage({
                                            type:2,
                                            fixed:true,
                                            msg:res.data.msg,
                                            isAutoDisplay:true,
                                            time:3000
                                        });
                                        dm.render();
                                    }

                                });
                            }

                        })
                    }
                });
                d.showModal();
            })
        });
        //上传音频
        $('#manage_add_3').click(function(){
            var id = $('#manage_group').find('.active').data('id');
            var index = $('#manage_tab').find('.z-sel').data('index');
            var url = ajaxUrl.url4+'?compress=0&res_type='+index+'&group_id='+id;
            $("#fileupload_add_3").fileupload({
                url: url,
                dataType: 'json',
                singleFileUploads:true,
                multipart: true,
                add: function (e, data) {
                    data.submit().always(function(res){
                        if(res.code===1){
                            var dm = new dialogMessage({
                                type:2,
                                fixed:true,
                                msg:res.msg,
                                isAutoDisplay:true,
                                time:2000
                            });
                            dm.render();
                        }else{
                            var dm = new dialogMessage({
                                type:1,
                                fixed:true,
                                msg:'上传音频文件成功',
                                isAutoDisplay:true,
                                time:1000
                            });
                            dm.render();
                            _initQuery(index,id,false);
                        }
                    })

                }
            })
        });

    };
    return {
        init: _init
    };
})();