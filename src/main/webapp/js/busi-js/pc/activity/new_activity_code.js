/**
 * Created by htp on 17/03/22.
 * modify by pray on 17/03/23
 * 二维码海报列表新版功能
 */
CommonUtils.regNamespace("activity", "newcode");
activity.newcode = (function() {
    var ajaxUrl = {
        url1:CONTEXT_PATH+'/qrcodePoster/new_list',//二维码海报列表展示
        url2:CONTEXT_PATH+'/qrcodePoster/setDefault/',//二维码海报列表设置默认
        url3:CONTEXT_PATH+'/qrcodePoster/del/',//二维码海报列表删除
        url4:CONTEXT_PATH+'/qrcodePoster/setSel/',//二维码海报列表轮播选用
        url5:CONTEXT_PATH+'/qrcodePoster/setSort/',//二维码海报列表轮播设置排序
        url6:CONTEXT_PATH+'/qrcodePoster/new_save',//新增修改单张二维码海报
        url7:CONTEXT_PATH+'/qrcodePoster/edit/'//二维码海报编辑数据回填
    };
    var _init = function(){
        _list();
    };
    /**
     * 海报列表
     */
    var _list = function(){
        var url = ajaxUrl.url1;
        var checked;
        $.ajaxJson(url,"", {
            "done": function (res) {
                var data = {
                    items:res.data
                };
                jumi.template('activity/new_activity_code_list',data,function(tpl){
                    $('#posterList').empty();
                    $('#posterList').html(tpl);
                    var checked = $('input[name="firstt"]:checked').val();
                    $('input[name="new_code_list"]').val(checked);
                });
            }
        });
    };
    //设置图片路径
    var _setImage = function(url){
        var imgWidth = $('#code_wrapper').width()-8;
        var Height = parseInt(imgWidth,10)*1334/750;
        $("#code_wrapper_img").attr("src",jumi.picParse(url,750));
        $("#code_wrapper_img").css({
            width:imgWidth,
            height:Height
        });
        $('#code_wrapper_img').attr('src',url);
        $('#add_code_hidden').data('src',url);
    };
    var _addImage = function(){
        $('#add_picture').click(function(){
            var d = new Dialog({
                context_path:CONTEXT_PATH, //请求路径,  必填
                resType:1 ,//图片1，视频2，语音3   必填
                callback:function(url){
                    var imgObj = new Image();
                    var url = jumi.picParse(url,750);
                    $('#add_picture').attr('src',url);
                    imgObj.src = url;
                    imgObj.onload = function() {
                        if (imgObj.width != 750 || imgObj.height != 1334) {
                            var dm = new dialogMessage({
                                type: 2,
                                fixed: true,
                                msg: '海报模板尺寸不对',
                                isAutoDisplay: true,
                                time: 3000
                            });
                            dm.render();
                            return;
                        }
                        _setImage(url);
                        $('#add_code_hidden').data('uptime',jumi.format('yyyy-MM-dd hh:mm:ss'));
                    }
                }
            });
            d.render();
        })
    };
    /**
     * 设为默认
     */
    var _setDefault = function(id){
        var url = ajaxUrl.url2+id;
        var args = {};
        args.fn1 = function(){
            $.ajaxJsonGet(url,"", {
                "done": function (res) {
                    if(res.data.code==0){
                        var dm = new dialogMessage({
                            type:1,
                            title:'操作提醒',
                            fixed:true,
                            msg:res.data.msg,
                            isAutoDisplay:false
                        });
                        dm.render();
                    }
                }
            });
        }
        args.fn2 = function(){
            var value = $('input[name="new_code_list"]').val();
            $('input[name="firstt"][value="'+value+'"]').prop('checked',true);
        };
        jumi.dialogSure('确定设置为主推?',args);
    };
    var _setDrag = function(leftPosition,topPosition){
        $("#drag").css({
            left:leftPosition||0,
            top:topPosition||0
        })
        $('#add_code_hidden').data('uptime',jumi.format('yyyy-MM-dd hh:mm:ss'));
    };

    var _setIconDrag = function(leftPosition,topPosition,uleftPosition,utopPosition,nleftPostison,ntopPosition){
        $("#drag").css({
            left:leftPosition||0,
            top:topPosition||0
        });
        $("#u-drag").css({
            left:uleftPosition||0,
            top:utopPosition||0
        });
        $("#u-move-drag").css({
            left:nleftPostison||0,
            top:ntopPosition||0
        })
        $('#add_code_hidden').data('uptime',jumi.format('yyyy-MM-dd hh:mm:ss'));
    };
    var _setIconCDrag = function(leftPosition,topPosition,uleftPosition,utopPosition,nleftPostison,ntopPosition,userCountleftPosition,userCounttopPosition){
        $("#drag").css({
            left:leftPosition||0,
            top:topPosition||0
        });
        $("#u-drag").css({
            left:uleftPosition||0,
            top:utopPosition||0
        });
        $("#u-move-drag").css({
            left:nleftPostison||0,
            top:ntopPosition||0
        })
        $("#u-move-drag-number").css({
            left:userCountleftPosition||0,
            top:userCounttopPosition||0
        })
        $('#add_code_hidden').data('uptime',jumi.format('yyyy-MM-dd hh:mm:ss'));
    };

    /**
     * 新增海报保存
     */
    var _addPostCode = function(){
        var qrcodePosters = {},msg;
        var doc = $('#add_code_hidden');
        var colorValue = doc.data('color').replace('#','')||'#22c2fe';
        qrcodePosters.qrcodeId = doc.data('qrcodeid');
        qrcodePosters.type = doc.data('type')||0;
        qrcodePosters.sort = doc.data('sort');
        qrcodePosters.isSel = doc.data('issel');
        qrcodePosters.leftPosition =doc.data('left')||0;
        qrcodePosters.topPosition = doc.data('top')||0;
        qrcodePosters.userBoxleftPosition = doc.data('uleft')||0;
        qrcodePosters.userBoxtopPosition = doc.data('utop')||0;
        qrcodePosters.nickNameleftPosition = doc.data('nleft')||0;
        qrcodePosters.nickNametopPosition = doc.data('ntop')||0;
        qrcodePosters.userCountleftPosition = doc.data('cleft')||0;
        qrcodePosters.userCounttopPosition = doc.data('ctop')||0;
        qrcodePosters.posterName = $('input[name="postName"]').val()||'';
        qrcodePosters.imageSrc = doc.data('src');
        qrcodePosters.fontColor = parseInt(colorValue,16);
        qrcodePosters.fontType =  doc.data('font')||'宋体';
        qrcodePosters.codeFormat = doc.data('format');
        qrcodePosters.uploadTime = doc.data('uptime');
        if(!qrcodePosters.imageSrc){
            var dm = new dialogMessage({
                type:1,
                fixed:true,
                msg:'二维码图片未上传,无法保存',
                isAutoDisplay:true,
                time:3000
            });
            dm.render();
        }else{
            if(qrcodePosters.qrcodeId){
                msg = '成功修改二维码海报';
            }else{
                msg = '成功新增二维码海报';
            }
            var jsonData = JSON.stringify(qrcodePosters);
            $.ajaxJson(ajaxUrl.url6,jsonData,{
                "done":function(res){
                    if(res.data.code===0){
                        var dm = new dialogMessage({
                            type:1,
                            fixed:true,
                            msg:msg,
                            isAutoDisplay:true,
                            time:3000
                        });
                        dm.render();
                        dialog.get('dialog_add_code').close().remove();
                        _list();
                    }else{
                        var dm = new dialogMessage({
                            type:2,
                            fixed:true,
                            msg:res.data.msg,
                            isAutoDisplay:true,
                            time:3000
                        });
                        dm.render();
                    }
                }
            })
        }
    };

    /**
     * 编辑二维码
     */
    var _edit = function(id){
        $.ajaxJson(ajaxUrl.url7+id,"",{
            "done":function(res){
                if(res.code===0){
                    var data = res.data;
                    jumi.template('activity/new_create_code?edit=1',data,function(tpl){
                        var d = dialog({
                            title: '编辑海报',
                            content:tpl,
                            width:750,
                            id:'dialog_add_code',
                            onshow:function () {
                                _bind();
                                _renderData();
                                _setImage(data.imageSrc);
                            }
                        });
                        d.showModal();
                    })
                }else{

                }
            }
        })
    };
    /**
     *
     */
    var _renderData = function () {
        var left = $('#add_code_hidden').data('left');
        var top = $('#add_code_hidden').data('top');
        var nleft = $('#add_code_hidden').data('nleft');
        var ntop = $('#add_code_hidden').data('ntop');
        var uleft = $('#add_code_hidden').data('uleft');
        var utop = $('#add_code_hidden').data('utop');
        var cleft = $('#add_code_hidden').data('cleft');
        var ctop = $('#add_code_hidden').data('ctop');
        var format = $('#add_code_hidden').data('format');
        var fontType = $('#add_code_hidden').data('font');
        var fontColor = $('#add_code_hidden').data('color');
        _displayCodeElement(format);
        _drag();
        if(format===0){
            _setDrag(left,top);
        }else if(format===1){
            _setColor(fontColor);
            _setFont(fontType);
            _setIconDrag(left,top,uleft,utop,nleft,ntop);
        }else{
            _setColor(fontColor);
            _setFont(fontType);
            _setIconCDrag(left,top,uleft,utop,nleft,ntop,cleft,ctop);
        }
    };
    /**
     * 设置二维码、昵称、粉丝头像初始化坐标
     */
     var _initPositon = function (url) {
        var url = url ||'';
        _setDrag(114,405);
        $('#add_code_hidden').data('left',114);
        $('#add_code_hidden').data('top',405);
        _setIconDrag(114,405,0,0,0,0);
        _setIconCDrag(114,405,0,0,0,0,0,0);
        //设置默认图的路径!
        if(url){
            _setImage(url);
        }else{
            _setImage("https://image.jumiweigu.com/20170401/dfbdf6d110524a47be52f76e50fabffe.jpg");
        }
     };
     var _lookImg = function(imgSrc){
         var url = jumi.picParse(imgSrc);
            

     }
    /**
     * 事件绑定
     */
    var _bind = function(){
        _addImage();
        $('#btn_close').click(function(){
            dialog.get('dialog_add_code').close().remove();
        })
        $('#btn_save').click(function(){
            _addPostCode();
        });
        $('#SelectFormat').change(function(){
            var code = $(this).val();
            var url = $('#add_code_hidden').data('src');
            _initPositon(url);
            if(code==='1'){
                $("#u-drag").show();
                $("#u-move-drag").show();
                $('#codeFormatB').show();
                $('#u-move-drag-number').hide();
                $('#add_code_hidden').data('format',1);
            }else if(code==='2'){
                $("#u-drag").show();
                $("#u-move-drag").show();
                $('#codeFormatB').show();
                $("#u-drag").show();
                $('#u-move-drag-number').show();
                $('#add_code_hidden').data('format',2);
            }
            else{
                $('#u-move-drag-number').hide();
                $('#codeFormatB').hide();
                $("#u-drag").hide();
                $("#u-move-drag").hide();
                $('#add_code_hidden').data('format',0);
            }
        });
        $('#fontBtn').click(function(event){
            event.stopPropagation();
            var flag = $('#fontColor').is(":hidden");
            if(!flag){
                $('#fontColor').hide();
            }else{
                $('#fontColor').show();
            }
        });
        $('#fontFamBtn').click(function(event){
            event.stopPropagation();
            var flag = $('#setFont').is(":hidden");
            if(!flag){
                $('#setFont').hide();
            }else{
                $('#setFont').show();
            }
        })
    };
    /**
     * 事件绑定
     */
     var _displayCodeElement = function(format){
        if(format===0){
            $("#u-drag").hide();
            $("#u-move-drag").hide();
            $('#codeFormatB').hide();
            $('#u-move-drag-number').hide();
        }
        else if(format===1){
            $("#u-drag").show();
            $("#u-move-drag").show();
            $('#codeFormatB').show();
            $('#u-move-drag-number').hide();
        }
        else{
            $("#u-drag").show();
            $("#u-move-drag").show();
            $('#codeFormatB').show();
            $('#u-move-drag-number').show();
        }
     };
    /**
     * 删除
     */
    var _del = function(id){
        var url = ajaxUrl.url3+id;
        var args = {};
        args.fn1 = function(){
            $.ajaxJsonDel(url,"", {
                "done": function (res) {
                    if(res.data.code==0){
                        $('#qrcode_'+id).remove();
                        var dm = new dialogMessage({
                            type:1,
                            title:'操作提醒',
                            fixed:true,
                            msg:res.data.msg,
                            isAutoDisplay:false
                        });
                        dm.render();
                    }
                }
            });
        };
        args.fn2 = function(){

        };
        jumi.dialogSure('确定删除该海报吗?',args);
    };

    /***
     * 轮播选用
     */
    var _setSel = function(id){
        var url = ajaxUrl.url4+id;
        var flag = $('#checkhb'+id).is(':checked');
        var selTxt = (flag) ?"该海报轮播启用吗？":"该海报轮播禁用吗?";
        var args = {};
        args.fn1 = function(){
            $.ajaxJsonGet(url,"", {
                "done": function (res) {
                    if(res.data.code==0){
                        var dm = new dialogMessage({
                            type:1,
                            title:'操作提醒',
                            fixed:true,
                            msg:res.data.msg,
                            isAutoDisplay:false
                        });
                        dm.render();
                    }
                }
            });
        };
        args.fn2 = function(){
            var flag = $('#checkhb'+id).is(':checked');
            if(flag){
                $('#checkhb'+id).prop('checked',false);
            }else{
                $('#checkhb'+id).prop('checked',true);
            }
        };
        jumi.dialogSure(selTxt,args);

    };

    /**
     * 设置顺序
     */
    var _setSort = function(id,vthis){
        var sort = $(vthis).val();
        var url = ajaxUrl.url5+id+"?sort="+sort;
        if(sort){
            $.ajaxJsonGet(url,"", {
                "done": function (res) {
                    if(res.data.code==0){
                        var dm = new dialogMessage({
                            type:1,
                            title:'操作提醒',
                            fixed:true,
                            msg:res.data.msg,
                            isAutoDisplay:false
                        });
                        dm.render();
                        _list();
                    }
                }
            });
        }else{
            return;
        }

    };
    /**
     * 控制二维码元素显隐
     */



    var _setFont = function (fontStyle) {
        $('#labelFont').css({
            "font-family":fontStyle||'宋体'
        });
        $("#add_code_hidden").data('font',fontStyle);
        $('#fontFamilySpan').text(fontStyle||'宋体');
        $('#setFont').hide();
    };

    var _addCode = function(){
        jumi.template('activity/new_create_code?edit=0',function(tpl){
            var d = dialog({
                title: '新建海报',
                content:tpl,
                width:750,
                id:'dialog_add_code',
                onshow:function () {
                    _bind();
                    _drag();
                    _initPositon();
                    _displayCodeElement(0);
                }
            });
            d.showModal();
        })
    };
    //拖动共用方法
    var _drag = function(){
        $("#drag").draggable({
            stop: function( event, ui ) {
                $('#add_code_hidden').data('left', ui.position.left);
                $('#add_code_hidden').data('top',ui.position.top);
            },
            containment: "#code_wrap",
            cursor: "crosshair",
            scrollSpeed: 100
        });
        $("#u-drag").draggable({
            stop: function( event, ui ) {
                $('#add_code_hidden').data('uleft', ui.position.left);
                $('#add_code_hidden').data('utop',ui.position.top);
            },
            containment: "#code_wrap",
            cursor: "crosshair",
            scrollSpeed: 100
        });
        $("#u-move-drag").draggable({
            stop: function( event, ui ) {
                $('#add_code_hidden').data('nleft', ui.position.left);
                $('#add_code_hidden').data('ntop',ui.position.top);
            },
            containment: "#code_wrap",
            cursor: "crosshair",
            scrollSpeed: 100
        });
        $("#u-move-drag-number").draggable({
            stop: function( event, ui ) {
                $('#add_code_hidden').data('cleft', ui.position.left);
                $('#add_code_hidden').data('ctop',ui.position.top);
            },
            containment: "#code_wrap",
            cursor: "crosshair",
            scrollSpeed: 100
        });
    };
    var _setColor = function (colorValue) {
        $('#labelFont').css({
            "color":colorValue||'#252525'
        });
        $("#add_code_hidden").data('color',colorValue);
        $('#fontSpan').css({
            "background-color":colorValue||'#252525'
        });
        $('#fontColor').hide();
    };
    return {
        init: _init,
        list:_list,
        setFont:_setFont,
        setDefault:_setDefault,
        del:_del,
        add:_addCode,
        setSel:_setSel,
        setColor:_setColor,
        edit:_edit,
        setSort:_setSort,
        lookImg:_lookImg
    };
})();