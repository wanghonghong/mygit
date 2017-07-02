/**
 * Created by ray on 16/9/5.
 */
/**
 * Created by BenRay on 16/8/8.
 * 官方图文
 */
CommonUtils.regNamespace("op", "trainingnotice");
op.trainingnotice = (function(){
    var count = 3;
    var sumH5img = 0;
    var ajaxUrl = {
        url1:CONTEXT_PATH+'/image_text',//新增图文
        url2:CONTEXT_PATH+'/image_text_type'//图文分类
    };
    var _init = function(){
        _initImageType();
        _query();
        _validate();
        _initRender();
    };
    var _initRender = function(){
        $('#op_content').hide();
        $('#h5-link').hide();
    }
    var _refreshPage = function( id ){
        var url = ajaxUrl.url1+'/findAll/3';
        var curPage = $(id+"_page").val();
        var params = {
            pageSize:10,
            curPage:curPage||0
        };
        $.ajaxJson(url,params,{
            "done":function(res){
                if(res.code===0){
                    var data = {
                        items:res.data.items
                    }
                    jumi.template('op/px_list_img',data,function(tpl){
                        $('#table-body').empty();
                        $('#table-body').html(tpl);
                    })
                }
            }
        })
    }

    //初始化编辑器
    var _initEditor = function(id,data,zIndex){
        var data = data||'';
        var zIndex = zIndex||0;
        UE.delEditor(id);
        var ue = UE.getEditor(id,{
            zIndex:zIndex
        });
        ue.ready(function() {
            ue.setContent(data);
        })
    };


    //图片选择
    var _addImgEvent = function(id){
        $(id).click(function(){
            var d = new Dialog({
                context_path:CONTEXT_PATH, //请求路径,  必填
                resType:1 ,//图片1，视频2，语音3   必填
                callback:function(url){
                    $(id).attr('src',url);
                }
            });
            d.render();
        })

    };
    var _addEditEvent = function(){
        var len = $('input[name="imgResListImg"]').val();
        sumH5img = parseInt(len,10);
        $('#add_icon_edit').click(function(){
            var d = new Dialog({
                context_path:CONTEXT_PATH, //请求路径,  必填
                resType:1 ,//图片1，视频2，语音3   必填,
                callback:function(url,node){
                    var data = {
                        items:node
                    }
                    sumH5img = sumH5img+1;
                    jumi.template('op/create_music_img?sum='+sumH5img,data,function(tpl){
                        $('#add_icon_edit').before(tpl);
                        $('i[id^="del_img_"]').click(function(){
                            $(this).parent().remove();
                        })
                    })
                }
            });
            d.render();
        })
        $('#op_btn_del_one,#op_btn_del_two').click(function(){
            $(this).parent().find('input[name="op_btn_link"]').val('');
            $(this).parent().find('input[name="op_btn_link"]').attr('disabled',false);
        })
        $('#op_del').click(function(event){
            event.stopPropagation();
            $('input[name="op_video_s"]').val('');
            $('input[name="op_video"]').data('url','');
            $('input[name="op_video"]').data('vid','');
            $('input[name="op_video"]').data('id','');
        })
        $('#op_video').click(function(){
            var d = new Dialog({
                context_path:CONTEXT_PATH, //请求路径,  必填
                resType:2 ,//图片1，视频2，语音3   必填
                callback:function(url,node1,node2){
                    $('input[name="op_video"]').data('vid',node2.vid);
                    $('input[name="op_video"]').data('url',node1);
                    $('input[name="op_video"]').data('id',node2.id);
                    $('input[name="op_video_s"]').val(node1);
                }
            });
            d.render();
        })
        $('#op_mContent').click(function(){
            var musicContent = $('input[name="op_music"]').data('name'),tpl='';
            if(musicContent){
                tpl = '<div class="u-btn-smltgry u-btn-smltgry-1 role-del"><span>'+musicContent+'</span><i class="iconfont icon-delete1" id="music_icon_del"></i></div>';
            }else{
                tpl = '<div class="u-btn-smltgry u-btn-smltgry-1 role-del">暂无内容</div>';
            }
            jumi.tipDialog('op_mContent',tpl);
            $('#music_icon_del').click(function(){
                var parent = $(this).parent();
                parent.children().remove();
                parent.append('<div>暂无内容</div>');
                $('input[name="op_music"]').val('');
                $('input[name="op_music"]').data('name','');
                $('input[name="op_music_s"]').val('');
                $('#op_mContent').css('color','#adadad');
            })
        })
        $('input[name="stylecolor"]').click(function(){
            $('input[name="stylecolor"]').prop('checked',false);
            $(this).prop('checked',true);
        })

        $('#op_set_btn_one,#op_set_btn_two').click(function(){
            var shopId = $('#shopId').val();
            var element = $(this);
            menuDialog.Menu.initPage({
                shop_id:shopId,
            },function(menu){
                menu.getUrl(function(link){
                    var url;
                    switch(link.link_type){
                        case '1':
                            url = DOMAIN+link.link_url+"?shopId="+link.shop_id;
                            break;
                        case '2':
                            url = DOMAIN+link.link_url+'/'+link.link_id+"?shopId="+link.shop_id;
                            break;
                        case '3':
                            url = CONTEXT_PATH+link.link_url+"?shopId="+link.shop_id;
                    }
                    element.parent().find('input[name="op_btn_link"]').val(url);
                });
            });
        })
        $('i[id^="del_img_"]').click(function(){
            $(this).parent().remove();
        })
        $('#op_c_music').click(function(){
            var d = new Dialog({
                context_path:CONTEXT_PATH, //请求路径,  必填
                resType:3 ,//图片1，视频2，语音3   必填
                callback:function(url,node){
                    $('input[name="op_music"]').val(node.id);
                    $('input[name="op_music"]').data('name',node.resName);
                    $('input[name="op_music_s"]').val(node.resName);
                    $('#op_mContent').css('color','#f8143d');
                }
            });
            d.render();
        })
        $('#op_img_video').click(function(){
            var d = new Dialog({
                context_path:CONTEXT_PATH, //请求路径,  必填
                resType:1 ,//图片1，视频2，语音3   必填
                callback:function(url,node){
                    $('#op_img_video').attr('src',url);
                    $('input[name="op_img_video_id"]').val(node.id);
                }
            });
            d.render();
        })
        $('input[name="op_edit_lbradio"]').click(function(){
            var obj = $(this);
            var value = parseInt(obj.val(),10);
            _control(value);
        });
        $('#op_edit_save').click(function(){
            var form = $("#jsEditForm");
            if (form.valid()) {
                _doEditSubmit();
            } else {
                $('body').animate({scrollTop:0},1000);
            }
        });

        _addImgEvent('#op_edit_img');
        $('input[name="op_edit_ewradio"]').click(function(){
            var doc = $(this);
            var id = doc.attr('id');
            var value = doc.val();
            if(value==='1'){
                $("input[name='op_edit_ewradio'][value='2']").prop('checked',false);
            }else{
                $("input[name='op_edit_ewradio'][value='1']").prop('checked',false);
            }
        })
        $('input[name="op_ds_edit_radio"]').click(function(){
            var doc = $(this);
            var id = doc.attr('id');
            var value = doc.val();
            if(value==='1'){
                $("input[name='op_ds_edit_radio'][value='2']").prop('checked',false);
            }else{
                $("input[name='op_ds_edit_radio'][value='1']").prop('checked',false);
            }
        })
    };
    //初始化选择框
    var _initSelect = function (id) {
        $.fn.select2.defaults.set('language', 'zh-CN');
        $(id).select2({
            theme: "jumi",
            language: "en"
        });
    };
    var _add = function(){
        $('#op_add_type').click(function () {
            count = count+1;
            var data = {
                count:count,
                type:1
            };
            //type为1表示新增 2表示编辑
            jumi.template('op/create_type',data,function (tpl) {
                var d = dialog({
                    title: '新建图文分类',
                    content:tpl,
                    width:500,
                    height:400,
                    onshow:function () {
                        jumi.wordNumber('ph_shared',60);
                        $('#m_ph_type_cancel').click(function(){
                            d.close().remove();
                        });
                        $('#m_op_img').click(function(){
                            var obj = $(this);
                            var d = new Dialog({
                                context_path:CONTEXT_PATH, //请求路径,  必填
                                resType:1 ,//图片1，视频2，语音3   必填
                                callback:function(url){
                                    obj.attr('src',url);
                                    $('input[name="op_img_hidden"]').val(url);
                                }
                            });
                            d.render();
                        });
                        $('#m_ph_type_share').click(function(){
                            var form = $("#typeForm");
                            if (form.valid()) {
                                var imageTextTypeCo = {};
                                imageTextTypeCo.typeName = $('input[name="typename"]').val();
                                imageTextTypeCo.imageUrl = $('input[name="op_img_hidden"]').val();
                                imageTextTypeCo.shareText = $("textarea[name=ph_shared]").val();
                                imageTextTypeCo.typeId = 3;
                                var jsonData = JSON.stringify(imageTextTypeCo);
                                $.ajaxJson(ajaxUrl.url2,jsonData,{
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
                                            _queryImgType();
                                        }
                                    },
                                    "fail":function (res) {

                                    }

                                });
                            } else {
                                $('body').animate({scrollTop:0},1000);
                            }

                        })
                    }
                });
                d.showModal();
            })

        });
    };

    var _validate = function(){
        var form = $("#jsForm");
        form.validate();//验证指定的表单
        return form;
    };
    var _doEditSubmit = function () {
        var imageTextUo_ = {};
        var imageTextUo ={};
        var imgResIdsArray = [];
        var imageTextRelateListArray = [];
        var ue = UE.getEditor('edit_container');
        imageTextUo_.formatCode = $('input[name="op_edit_lbradio"]:checked').val();
        imageTextUo_.imageTextType = $('#select_edit').val();
        imageTextUo_.imageTextTile = $('input[name="img_edit_text"]').val();
        imageTextUo_.authorName = $('input[name="edit_author"]').val();
        imageTextUo_.shareText = $('#op_edit_shared').val();
        imageTextUo_.imageUrl = $('#op_edit_img').attr('src');
        imageTextUo_.readLinkAdd = $('input[name="op_edit_address"]').val();
        imageTextUo_.bottomQrCode = $('input[name="op_edit_ewradio"]:checked').val()||0;
        imageTextUo_.imageTextDetail = ue.getContent();
        imageTextUo_.rewardFormatCode = $('input[name="op_ds_edit_radio"]:checked').val()||0;
        imageTextUo_.id = $('input[name="edit"]').val();
        imageTextUo_.audioResId = $('input[name="op_music"]').val();
        imageTextUo_.videoId = $('input[name="op_video"]').data('id');
        imageTextUo_.videoUrl = $('input[name="op_video"]').data('vid');
        imageTextUo_.videoImg = $('input[name="op_img_video_id"]').val();
        if(imageTextUo_.videoUrl&&imageTextUo_.videoId){
            if(!imageTextUo_.videoImg){
                var dm = new dialogMessage({
                    type:2,
                    fixed:true,
                    msg:'视频主图必须上传',
                    isAutoDisplay:true,
                    time:3000
                });
                dm.render();
                return;
            }
        }
        $('input[name="op_img_hidden"]').each(function(){
            var id = $(this).val();
            imgResIdsArray.push(id);
        })
        imageTextUo_.colorId = $('input[name="stylecolor"]:checked').val();
        imageTextUo_.imgResIds = imgResIdsArray.join(',');
        imageTextUo_.audioTitle = $('input[name="op_music_s"]').val();
        imageTextUo_.reward = $('input[name="op_dz_num"]').val();
        if(imageTextUo_.formatCode==='3'){
            $('div[id^="op_btn_"]').each(function(i){
                var obj = {};
                var k = i+1;
                obj.buttonName = $(this).find('input[name="op_btn"]').val();
                obj.linkPath = $(this).find('input[name="op_btn_link"]').val();
                obj.id = $(this).find('input[name="op_hi_hidden_'+k+'"]').val();
                imageTextRelateListArray.push(obj);
            })
        }
        imageTextUo_.typeId = 3;
        imageTextUo_.reward = $('input[name="dz_num"]').val();
        imageTextUo.imgTextUo = imageTextUo_;
        imageTextUo.imageTextRelateList = imageTextRelateListArray;
        var jsonData = JSON.stringify(imageTextUo);
        var url = ajaxUrl.url1;
        $.ajaxJsonPut(url,jsonData,{
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
                    dialog.get('dialog_edit').close().remove();
                    _refreshPage("#pageToolbar");
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
    };
    var _doSubmit = function(){
        var imgTextCo = {};
        var imageTextCo ={};
        var imgResIdsArray = [];
        var imageTextRelateListArray = [];
        var ue = UE.getEditor('container');
        imgTextCo.formatCode = $('input[name="op_lbradio"]:checked').val();
        imgTextCo.imageTextType = $('#select1').val();
        imgTextCo.imageTextTile = $('input[name="imgtext"]').val();
        imgTextCo.authorName = $('input[name="author"]').val();
        imgTextCo.shareText = $('#op_shared').val();
        imgTextCo.imageUrl = $('#op_img').attr('src');
        imgTextCo.readLinkAdd = $('input[name="op_address"]').val();
        imgTextCo.bottomQrCode = $('input[name="op_ewradio"]:checked').val()||0;
        imgTextCo.imageTextDetail = ue.getContent();
        imgTextCo.rewardFormatCode = $('input[name="op_dsradio"]:checked').val()||0;
        imgTextCo.typeId = 3;
        imgTextCo.videoId = $('input[name="op_video"]').data('id');
        imgTextCo.videoUrl = $('input[name="op_video"]').data('vid');
        imgTextCo.videoImg = $('input[name="op_img_video_id"]').val();
        if(imgTextCo.videoUrl&&imgTextCo.videoId){
            if(!imgTextCo.videoImg){
                var dm = new dialogMessage({
                    type:2,
                    fixed:true,
                    msg:'视频主图必须上传',
                    isAutoDisplay:true,
                    time:3000
                });
                dm.render();
                return;
            }
        }
        if(imgTextCo.formatCode==='3'){
            $('div[id^="op_btn_"]').each(function(){
                var obj = {};
                obj.buttonName = $(this).find('input[name="op_btn"]').val();
                obj.linkPath = $(this).find('input[name="op_btn_link"]').val();
                imageTextRelateListArray.push(obj);
            })
        }
        imgTextCo.audioResId = $('input[name="op_music"]').val();
        $('input[name="op_img_hidden"]').each(function(){
            var id = $(this).val();
            imgResIdsArray.push(id);
        })
        imgTextCo.colorId = $('input[name="stylecolor"]:checked').val();
        imgTextCo.imgResIds = imgResIdsArray.join(',');
        imgTextCo.audioTitle = $('input[name="op_music_s"]').val();
        imgTextCo.reward = $('input[name="op_dz_num"]').val();
        imageTextCo.imgTextCo = imgTextCo;
        imageTextCo.imageTextRelateList = imageTextRelateListArray;
        var jsonData = JSON.stringify(imageTextCo);
        $.ajaxJson(ajaxUrl.url1,jsonData,{
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
                }
            },
            "fail":function (res) {

            }

        });
    };
    var _control = function(value){
        if(value===1){
            $('#author_group').show();
            $('#op_content').hide();
            $('#h5-link').hide();
            $('#op_details').show();
            $('#op_code').show();
            $('#op_ds_code').show();
            $('#op_dz').show();
            $('#link_group').show();
            $('#op_video').show();
            $('#op_video_img').show();
        }else if(value===2){
            $('#author_group').hide();
            $('#op_content').hide();
            $('#op_details').show();
            $('#h5-link').hide();
            $('#op_code').show();
            $('#op_ds_code').show();
            $('#op_dz').show();
            $('#link_group').show();
            $('#op_video').show();
            $('#op_video_img').show();
        }else if(value===3){
            $('#author_group').hide();
            $('#link_group').hide();
            $('#op_details').hide();
            $('#op_content').show();
            $('#h5-link').show();
            $('#op_code').hide();
            $('#op_ds_code').hide();
            $('#op_dz').hide();
            $('#op_video').hide();
            $('#op_video_img').hide();
        }
    }
    var _setUpDown = function(status,id,name){
        var msg = (status===1)?'确定上架【'+name+'】的活动吗?':'确定下架【'+name+'】的活动吗?';
        var p = (status===0)?1:0;
        var txt = (status===0)?'上架':'下架';
        var url = ajaxUrl.url1+'/id/'+id+'/status/'+p;
        var args = {};
        args.fn1 = function(){
            $.ajaxJsonPut(url,null,{
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
                        $('#op_img_grounp_'+id).text(txt);
                        $('#op_img_grounp_'+id).data('status',p);
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
        };
        args.fn2 = function(){

        };
        jumi.dialogSure(msg,args);
    }
    var _bind = function(){

        $('input[name="stylecolor"]').click(function(){
            $('input[name="stylecolor"]').prop('checked',false);
            $(this).prop('checked',true);
        })
        $('#op_del').click(function(event){
            event.stopPropagation();
            $('input[name="op_video_s"]').val('');
            $('input[name="op_video"]').data('url','');
            $('input[name="op_video"]').data('vid','');
            $('input[name="op_video"]').data('id','');
        })
        $('#op_save').click(function () {
            var form = _validate();
            if (form.valid()) {
                var args = {};
                args.fn1 = function(){
                    _doSubmit();
                };
                args.fn2 = function(){

                };
                jumi.dialogSure('确定保存该培训通知的信息吗?',args);
            } else {
                $('body').animate({scrollTop:0},1000);
            }
        })
        $('#op_mContent').click(function(){
            var musicContent = $('input[name="op_music"]').data('name'),tpl='';
            if(musicContent){
                tpl = '<div class="u-btn-smltgry u-btn-smltgry-1 role-del"><span>'+musicContent+'</span><i class="iconfont icon-delete1" id="music_icon_del"></i></div>';
            }else{
                tpl = '<div class="u-btn-smltgry u-btn-smltgry-1 role-del">暂无内容</div>';
            }
            jumi.tipDialog('op_mContent',tpl);
            $('#music_icon_del').click(function(){
                var parent = $(this).parent();
                parent.children().remove();
                parent.append('<div>暂无内容</div>');
                $('input[name="op_music"]').val('');
                $('input[name="op_music"]').data('name','');
                $('input[name="op_music_s"]').val('');
                $('#op_mContent').css('color','#adadad');
            })
        })
        $('input[name="op_ewradio"]').click(function(){
            var doc = $(this);
            var id = doc.attr('id');
            var value = doc.val();
            if(value==='1'){
                $("input[name='op_ewradio'][value='2']").prop('checked',false);
            }else{
                $("input[name='op_ewradio'][value='1']").prop('checked',false);
            }

        })
        $('#op_video').click(function(){
            var d = new Dialog({
                context_path:CONTEXT_PATH, //请求路径,  必填
                resType:2 ,//图片1，视频2，语音3   必填
                callback:function(url,node1,node2){
                    $('input[name="op_video"]').data('vid',node2.vid);
                    $('input[name="op_video"]').data('url',node1);
                    $('input[name="op_video"]').data('id',node2.id);
                    $('input[name="op_video_s"]').val(node1);
                }
            });
            d.render();
        })
        $('#op_btn_del_one,#op_btn_del_two').click(function(){
            $(this).parent().find('input[name="op_btn_link"]').val('');
            $(this).parent().find('input[name="op_btn_link"]').attr('disabled',false);
        })
        $('#op_del').click(function(event){
            event.stopPropagation();
            $('input[name="op_video_s"]').val('');
            $('input[name="op_video"]').data('url','');
            $('input[name="op_video"]').data('vid','');
            $('input[name="op_video"]').data('id','');
        })
        $('#op_more_link').click(function(){
            var shopId = $('#shopId').val();
            var element = $(this);
            menuDialog.Menu.initPage({
                shop_id:shopId,
            },function(menu){
                menu.getUrl(function(link){
                    var url;
                    switch(link.link_type){
                        case '1':
                            url = DOMAIN+link.link_url+"?shopId="+link.shop_id;
                            break;
                        case '2':
                            url = DOMAIN+link.link_url+'/'+link.link_id+"?shopId="+link.shop_id;
                            break;
                        case '3':
                            url = CONTEXT_PATH+link.link_url+"?shopId="+link.shop_id;
                    }
                    element.parent().find('input[name="op_address"]').val(url);
                    $('input[name="op_address"]').attr('disabled',"disabled");
                });
            });
        })
        $('#add_icon').click(function(){
            var d = new Dialog({
                context_path:CONTEXT_PATH, //请求路径,  必填
                resType:1 ,//图片1，视频2，语音3   必填,
                callback:function(url,node){
                    var data = {
                        items:node
                    }
                    sumH5img = sumH5img+1;
                    jumi.template('op/create_music_img?sum='+sumH5img,data,function(tpl){
                        $('#add_icon').before(tpl);
                    })
                }
            });
            d.render();
        })
        $('#op_more_material').click(function(){
            $('input[name="op_address"]').val('');
            $('input[name="op_address"]').attr('disabled',false);
        })
        $('#op_img_video').click(function(){
            var d = new Dialog({
                context_path:CONTEXT_PATH, //请求路径,  必填
                resType:1 ,//图片1，视频2，语音3   必填
                callback:function(url,node){
                    $('#op_img_video').attr('src',url);
                    $('input[name="op_img_video_id"]').val(node.id);
                }
            });
            d.render();
        })
        $('#op_c_music').click(function(){
            var d = new Dialog({
                context_path:CONTEXT_PATH, //请求路径,  必填
                resType:3 ,//图片1，视频2，语音3   必填
                callback:function(url,node){
                    $('input[name="op_music"]').val(node.id);
                    $('input[name="op_music"]').data('name',node.resName);
                    $('input[name="op_music_s"]').val(node.resName);
                    $('#op_mContent').css('color','#f8143d');
                }
            });
            d.render();
        })
        $('#op_set_btn_one,#op_set_btn_two').click(function(){
            var shopId = $('#shopId').val();
            var element = $(this);
            menuDialog.Menu.initPage({
                shop_id:shopId,
            },function(menu){
                menu.getUrl(function(link){
                    var url;
                    switch(link.link_type){
                        case '1':
                            url = DOMAIN+link.link_url+"?shopId="+link.shop_id;
                            break;
                        case '2':
                            url = DOMAIN+link.link_url+'/'+link.link_id+"?shopId="+link.shop_id;
                            break;
                        case '3':
                            url = CONTEXT_PATH+link.link_url+"?shopId="+link.shop_id;
                    }
                    element.parent().find('input[name="op_btn_link"]').val(url);
                });
            });
        })
        $('input[name="op_dsradio"]').click(function(){
            var doc = $(this);
            var id = doc.attr('id');
            var value = doc.val();
            if(value==='1'){
                $("input[name='op_dsradio'][value='2']").prop('checked',false);
            }else{
                $("input[name='op_dsradio'][value='1']").prop('checked',false);
            }
        })
        $('input[name="image_showFormat"]').click(function(){
            var formatDiv = $(this);
            var value = formatDiv.val();
            var url = ajaxUrl.url2+'/saveShowFormat/'+value+'/type_id/3';
            $.ajaxJsonPut(url,'',{
                "done":function (res) {
                    if(res.code===0){
                        var dm = new dialogMessage({
                            type:1,
                            fixed:true,
                            msg:'设置成功',
                            isAutoDisplay:true,
                            time:3000
                        });
                        dm.render();
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
        });
        $('input[name="op_lbradio"]').click(function(){
            var obj = $(this);
            var value = parseInt(obj.val(),10);
            _control(value);
        });
        $('#op_tab').find('li').unbind('click').bind('click',function(){
            var index = $(this).index();
            $(this).addClass('active').siblings().removeClass('active');
            $('#op_container_'+index).show().siblings().hide();
            if(index===0){
                _initImageType();
                _control(1);
            }
            if(index === 1){
                $('#op_container_0').empty();
                var url = ajaxUrl.url2+'/list/type_id/3';
                var params = {
                    pageSize:10
                };
                jumi.pagination('#pageTypeToolbar',url,params,function(res,curPage){
                    if(res.code===0){
                        var showFormat = res.data.showFormat;
                        $("input[name=image_showFormat][value='"+showFormat+"']").prop("checked",true);
                        //判断是否第一页
                        var data = {
                            items:res.data.imageTextTypes.items
                        };
                        if(curPage===0){
                            data.isFirstPage = 1;
                        }else{
                            data.isFirstPage = 0;
                        }
                        jumi.template('op/px_list_type',data,function(tpl){
                            $('#m_type_list').html(tpl);
                        })
                    }
                })
            }
            if(index===2){
                $('#op_container_0').empty();
                var url = ajaxUrl.url1+'/findAll/3';
                var params = {
                    pageSize:10
                };
                jumi.pagination('#pageToolbar',url,params,function(res,curPage){

                    if(res.code===0){
                        //判断是否第一页
                        var data = {
                            items:res.data.items
                        };
                        if(curPage===0){
                            data.isFirstPage = 1;
                        }else{
                            data.isFirstPage = 0;
                        }

                        jumi.template('op/px_list_img',data,function(tpl){
                            $('#table-body').empty();
                            $('#table-body').html(tpl);
                        })


                    }
                })
            }
        });

        $('#op_existing-classify-choose').on('click','div[id^="op_save_"]',function(){
            var obj = $(this);
            var index = $(this).attr('data-index');
            var parent = $('#op_choose_'+index);
            var delId = obj.attr('data-save-id');
            var imageTextTypeCo = {};
            imageTextTypeCo.typeName = parent.find('input[name="fwx1"]').val();
            imageTextTypeCo.imageUrl = parent.find('img').attr('src');
            imageTextTypeCo.shareText = $("#op_textarea_"+index).val();
            if(delId){
                imageTextTypeCo.id = delId;
                var jsonData = JSON.stringify(imageTextTypeCo);
                $.ajaxJsonPut(ajaxUrl.url2,jsonData,{
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
                        }
                    },
                    "fail":function (res) {

                    }

                });
            }else{
                var jsonData = JSON.stringify(imageTextTypeCo);
                $.ajaxJson(ajaxUrl.url2,jsonData,{
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
                        }
                    },
                    "fail":function (res) {

                    }

                });
            }

        });
        $('#op_existing-classify-choose').on('click','img[id^="op_img_"]',function(){
            var obj = $(this);
            var d = new Dialog({
                context_path:CONTEXT_PATH, //请求路径,  必填
                resType:1 ,//图片1，视频2，语音3   必填
                width:'',//自定义弹出框宽度
                height:'',//自定义弹出框高度
                callback:function(url){
                    obj.attr('src',url);
                }
            });
            d.render();
        });
        _addImgEvent('#op_img');
    };


    var _del = function(){
        $('#m_type_list').on('click','div[id^="op_del_"]',function(){
            var self = $(this);
            var index = self.data('index');
            var delId = self.attr('data-type-id');
            var args = {};
            var checkUrl = ajaxUrl.url2+'/checkIsDel/'+delId;
            var url = ajaxUrl.url2+'/'+delId;
            if(delId){
                $.ajaxJsonGet(checkUrl,{
                    "done":function (res) {
                        jumi.dialogSure(res.data.msg,args);
                    }
                });
                args.fn1 = function(){
                    $.ajaxJsonDel(url,{
                        "done":function (res) {
                            if(res.code===0){
                                var dm = new dialogMessage({
                                    type:1,
                                    fixed:true,
                                    msg:'删除成功',
                                    isAutoDisplay:true,
                                    time:3000
                                });
                                dm.render();
                            }
                        }
                    });
                    self.parent().parent().remove();
                };
                args.fn2 = function(){

                }
            }else{
                args.fn1 = function(){
                    self.parent().parent().remove();
                };
                args.fn2 = function(){
                };
                jumi.dialogSure('是否删除分类',args);

            }


        });
        //删除图文分类
        $('#table-body').on('click','div[id^="op_img_list_del_"]',function(){
            var delId = $(this).attr('data-del-id');
            var ids = [];
            ids.push(delId);
            var args = {};
            args.fn1 = function(){
                var url = ajaxUrl.url1+'/'+ids.join(',');
                $.ajaxJsonDel(url,{
                    "done":function (res) {
                        if(res.code===0){
                            var dm = new dialogMessage({
                                type:1,
                                fixed:true,
                                msg:'删除成功',
                                isAutoDisplay:true,
                                time:3000
                            });
                            dm.render();
                        }
                    }
                });
                $('#op_table_container_'+delId).remove();
            };
            //关闭的时候初始化方法
            args.fn2 = function(){

            };
            jumi.dialogSure('确定删除图文列表吗?',args);

        })
    };
    var _update = function(){
        //单条分类查询
        $('#m_type_list').on('click','div[id^="op_edit_"]',function(){
            var self = $(this);
            var index = self.data('index');
            var typeId = self.attr('data-type-id');
            var url = ajaxUrl.url2+'/'+typeId;
            $.ajaxJsonGet(url,{
                "done":function (res) {
                    var data = {
                        type:2,
                        item:res.data
                    };
                    jumi.template('op/create_type',data,function (tpl) {
                        var d = dialog({
                            title: '编辑图文分类',
                            content:tpl,
                            width:500,
                            onshow:function () {
                                jumi.wordNumber('ph_shared',60);
                                $('#m_ph_type_cancel').click(function(){
                                    d.close().remove();
                                });
                                $('#m_ph_type_share').click(function () {
                                    var form = $("#typeForm");
                                    if (form.valid()) {
                                        var imageTextTypeCo = {};
                                        imageTextTypeCo.id = typeId;
                                        imageTextTypeCo.typeName = $('input[name="typename"]').val();
                                        imageTextTypeCo.imageUrl = $('input[name="op_img_hidden"]').val();
                                        imageTextTypeCo.shareText = $("textarea[name=ph_shared]").val();
                                        imageTextTypeCo.sort = $('input[name="sortname"]').val()||0;
                                        var jsonData = JSON.stringify(imageTextTypeCo);
                                        $.ajaxJsonPut(ajaxUrl.url2,jsonData,{
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
                                                    _queryImgType();
                                                }
                                            },
                                            "fail":function (res) {

                                            }

                                        });
                                    } else {
                                        $('body').animate({scrollTop:0},1000);
                                    }
                                });
                                $('#m_op_img').click(function(){
                                    var obj = $(this);
                                    var d = new Dialog({
                                        context_path:CONTEXT_PATH, //请求路径,  必填
                                        resType:1 ,//图片1，视频2，语音3   必填
                                        callback:function(url){
                                            obj.attr('src',url);
                                            $('input[name="op_img_hidden"]').val(url);
                                        }
                                    });
                                    d.render();
                                })

                            }

                        });
                        d.showModal();
                    })
                }
            });


        });
        $('#table-body').on('click','div[id^="op_img_list_edit_"]',function(){
            var id = $(this).data('id');
            var url = ajaxUrl.url1+'/'+id;
            var index = $(this).data('index');
            $.ajaxJsonGet(url,"",{
                "done":function (res) {
                    if(res.code===0){
                        var data = {
                            items:res.data.imgTextRo,
                            itemsType:res.data.imageTextTypes,
                            type:1//编辑图文
                        };
                        var imageTextDetail = res.data.imgTextRo.imageTextDetail;
                        var code = res.data.imgTextRo.formatCode;
                        var colorId = res.data.imgTextRo.colorId;
                        data.itemsAudio = res.data.audioRes;
                        data.itemsVideo = res.data.videoResoure;
                        data.itemsVideoImg = res.data.videoRes;
                        if(code===3){
                            data.imgResList = res.data.imgResList;
                            data.buttonList = res.data.buttonList;
                        }
                        jumi.template('op/px_edit_img',data,function(tpl){
                            var d = dialog({
                                title: '图文编辑',
                                content:tpl,
                                width:1200,
                                id:'dialog_edit',
                                onshow:function () {
                                    _initSelect('#select_edit');
                                    if(index<=3){
                                        $("#select_edit").attr('disabled',true);
                                    }
                                    $('input[name="stylecolor"][value="'+colorId+'"]').prop('checked',true);
                                    _initEditor('edit_container',imageTextDetail,1200);
                                    _control(code);
                                    _addEditEvent();
                                }
                            });
                            d.showModal();
                        })
                    }
                },
                "fail":function (res) {

                }

            });

        })
    };
    var _queryImgType = function(){
        var url = ajaxUrl.url2+'/list/type_id/3';
        var params = {
            pageSize:10
        };
        jumi.pagination('#pageTypeToolbar',url,params,function(res,curPage){
            if(res.code===0){
                var showFormat = res.data.showFormat;
                $("input[name=image_showFormat][value='"+showFormat+"']").prop("checked",true);
                //判断是否第一页
                var data = {
                    items:res.data.imageTextTypes.items
                };
                if(curPage===0){
                    data.isFirstPage = 1;
                }else{
                    data.isFirstPage = 0;
                }
                jumi.template('op/px_list_type',data,function(tpl){
                    $('#m_type_list').html(tpl);
                })
            }
        })
    };
    var _query = function(){

        //图文上下架功能
        $('#table-body').on('click','div[id^="op_img_grounp_"]',function(){
            var status = $(this).data('status');
            var id = $(this).data('id');
            var name = $(this).data('name');
            _setUpDown(status,id,name);
        })
        $('#table-body').on('click','div[id^="op_img_watch_edit_"]',function(){
            var id = $(this).data('id');
            var url = ajaxUrl.url1+'/'+id;
            var index = $(this).index();
            $.ajaxJsonGet(url,"",{
                "done":function (res) {
                    if(res.code===0){
                        var data = {
                            items:res.data.imgTextRo,
                            itemsType:res.data.imageTextTypes,
                            type:2//预览图文
                        };
                        var code = res.data.imgTextRo.formatCode;
                        var colorId = res.data.imgTextRo.colorId;
                        data.itemsAudio = res.data.audioRes;
                        data.itemsVideo = res.data.videoResoure;
                        data.itemsVideoImg = res.data.videoRes;
                        if(code===3){
                            data.imgResList = res.data.imgResList;
                            data.buttonList = res.data.buttonList;
                        }
                        var imageTextDetail = res.data.imgTextRo.imageTextDetail;
                        jumi.template('op/px_edit_img',data,function(tpl){
                            var d = dialog({
                                title: '图文预览',
                                content:tpl,
                                width:1200,
                                onshow:function () {
                                    _initSelect('#select_edit');
                                    if(index<=3){
                                        $('#select_edit').attr('disabled',true);
                                    }
                                    $('input[name="stylecolor"][value="'+colorId+'"]').prop('checked',true);
                                    _initEditor('edit_container',imageTextDetail,1200);
                                    _control(code);
                                }

                            });
                            d.showModal();
                        })
                    }
                },
                "fail":function (res) {

                }

            });

        })
    };


    var _initImageType = function(){
        jumi.template('op/index',function(html){
            $('#op_container_0').html(html);
            _initEditor('container');
            _bind();
            var tpl = '<option value="">--请选择--</option>';
            var str =[];
            var url = ajaxUrl.url2+'/type_id/3';
            $.ajaxJsonGet(url,null,{
                done:function (res) {
                    if(res.code===0){
                        var data = res.data;
                        _.map(data.imageTextTypeList,function(k,v){
                            tpl+='<option value="'+k.id+'">'+k.typeName+'</option>'
                        });
                        $('#select1').html(tpl);
                        _initSelect('#select1');

                    }
                }
            })
        })
    };
    return {
        init :_init,
        add:_add,
        del:_del,
        update:_update,
        query:_query
    };
})();

