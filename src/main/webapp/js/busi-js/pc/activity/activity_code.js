/**
 * Created by ray on 16/10/21.
 */
CommonUtils.regNamespace("activity", "code");
activity.code = (function() {
    var CodeNumber = 6;//二维码海报总数
    var ajaxUrl = {
        url1:CONTEXT_PATH+'/qrcodePoster/save',//二维码海报保存
        url2:CONTEXT_PATH+'/qrcodePoster/data'//二维码海报回填
    }
    var _init = function(){
        _drag();
        _getCode();
        _bind();
    }
    var _displayBox = function(){
        $(document).click(function(event){
            var flag1 = $('#fontColor').is(":hidden");
            var flag2 = $('#setFont').is(":hidden");
            if(!flag1){
                $("#fontColor").hide();
            }
            if(!flag2){
                $("#setFont").hide();
            }
        })
    }
    var _getJsonCode = function(){
        jumi.file('/activity/code_config.json',function(res){
            if(res.code===0){
                var data = {
                    items:res.data
                }
                jumi.template('activity/activity_code_list',data,function(tpl){
                    $('#code_list').html(tpl);
                    _setDrag(0,0);
                    _setIconDrag(0,0,0,0,0,0);
                    _displayBox();
                })
            }
        })
    }
    //旧数据修正函数 n表示修正数量
    var _completeData = function(codenumber,data){
        var n = data.items.length;
        var newData = [];
        var fillNumber = codenumber-n;//需要补其的数量
        var A_data = {
            "qrcodeId":"",
            "posterName": "",
            "type":"",
            "imageSrc": "",
            "shopId": "",
            "uploadTime": "",
            "leftPosition":"",
            "topPosition":"",
            "fontColor":"",
            "fontType": "",
            "userBoxleftPosition":"",
            "userBoxtopPosition":"",
            "codeFormat":0,
            "nickNameleftPosition":"",
            "nickNametopPosition":""
        }
        var B_data = {
            "qrcodeId":"",
            "posterName": "",
            "type":"",
            "imageSrc": "",
            "shopId": "",
            "uploadTime": "",
            "leftPosition":"",
            "topPosition":"",
            "fontColor":"",
            "fontType": "",
            "userBoxleftPosition":"",
            "userBoxtopPosition":"",
            "codeFormat":1,
            "nickNameleftPosition":"",
            "nickNametopPosition":""
        }
        _.each(data.items,function(k,v){
            if(k.codeFormat===1){
                for(var i=0;i<fillNumber;i++){
                    newData.push(A_data);
                }
                newData.push(k);
            }else{
                newData.push(k);
            }
        })
        data.items = newData;
    }
    //数据修正函数,目的是把第二种类型的二维码放到最后
    var _getCode = function(){
        var params = {};
        $.ajaxJson(ajaxUrl.url2,params,{
            "done":function(res){
                if(res.code===0){
                    var data = {
                        items:res.data
                    }
                    if(data.items.length<CodeNumber){
                        _completeData(CodeNumber,data);
                    }
                    if(data.items.length==0){
                        _getJsonCode();
                    }
                    else{
                        var _items = _.sortBy(data.items,'codeFormat');
                        data.items = _items;
                        jumi.template('activity/activity_code_list?qrcode=1',data,function(tpl){
                            $('#code_list').html(tpl);
                            _displayBox();
                            var left;
                            var chooseData = _.where(data.items,{type:1});
                            var thirdData = _.where(data.items,{codeFormat:1})
                            var top = chooseData[0].topPosition||0;
                            var t = chooseData[0].userBoxtopPosition||0;
                            var nt = chooseData[0].nickNametopPosition||0;
                            var left = chooseData[0].leftPosition||0;
                            var src = chooseData[0].imageSrc;
                            if(chooseData[0].codeFormat===1){
                                $('#u-drag').show();
                                $('#u-move-drag').show();
                                var l = chooseData[0].userBoxleftPosition||0;
                                var nl = chooseData[0].nickNameleftPosition||0;
                                _setFont(chooseData[0].fontType);
                                _setColor('#'+chooseData[0].fontColor);
                                _setIconDrag(left,top,l,t,nl,nt);
                            }else{
                                $('#u-drag').hide();
                                $('#u-move-drag').hide();
                                _setFont(thirdData[0].fontType);
                                _setColor('#'+thirdData[0].fontColor);
                            }
                            _setImage(src);
                            _setDrag(left,top);
                        })
                    }

                }
            }
        })
    }
    var _selectInit = function(){
        jumi.Select('#font');
    }
    var _setFont = function(fontStyle){
        $('#labelFont').css({
            "font-family":fontStyle||'宋体'
        })
        $('#fontFamilySpan').text(fontStyle||'宋体');
        $("#qr_hidden_3").data('font',fontStyle);
        $('#setFont').hide();
    }
    var _setColor = function (colorValue) {
        $('#labelFont').css({
            "color":colorValue||'#252525'
        })
        $('#fontSpan').css({
            "background-color":colorValue||'#252525'
        })
        $("#qr_hidden_3").data('color',colorValue);
        $('#fontColor').hide();
    }
    var _setIconDrag = function(leftPosition,topPosition,uleftPosition,utopPosition,nleftPostison,ntopPosition){
        $("#drag").css({
            left:leftPosition||0,
            top:topPosition||0
        })
        $("#u-drag").css({
            left:uleftPosition||0,
            top:utopPosition||0
        })
        $("#u-move-drag").css({
            left:nleftPostison||0,
            top:ntopPosition||0
        })
    }
    var _setDrag = function(leftPosition,topPosition){
        $("#drag").css({
            left:leftPosition||0,
            top:topPosition||0
        })
    }
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
    }
    var _bind = function(){
        $('#code_list').on('click','input[name="activity_code"]',function(){
            var id = $(this).data('id');
            var dom = $('#qr_hidden_'+id);
            var src = dom.data('src');
            var left = dom.data('left');
            var top = dom.data('top');
            if(id.indexOf('0')>=0){
                $("#u-drag").hide();
                $("#u-move-drag").hide();
                _setDrag(left,top);
            }else{
                $("#u-drag").show();
                $("#u-move-drag").show();
                var uleft = dom.data('uleft');
                var utop = dom.data('utop');
                var nleft = dom.data('nleft');
                var ntop = dom.data('ntop');
                var font = dom.data('font');
                var color = dom.data('color');
                _setFont(font);
                _setColor(color);
                _setIconDrag(left,top,uleft,utop,nleft,ntop);
            }
            _setImage(src);

        })

        $('#code_list').on('keyup','input[id^="activity_name_"]',function(){
            var id = $(this).data('id');
            var value = $(this).val();
            $("#qr_hidden_"+id).data('name',value);
        })
        $('#code_list').on('click','#fontBtn',function(event){
            event.stopPropagation();
             var flag = $('#fontColor').is(":hidden");
             if(!flag){
                 $('#fontColor').hide();
             }else{
                 $('#fontColor').show();
             }
        })
        $('#code_list').on('click','#fontFamBtn',function(event){
            event.stopPropagation();
            var flag = $('#setFont').is(":hidden");
            if(!flag){
                $('#setFont').hide();
            }else{
                $('#setFont').show();
            }
        })
        $('#code_list').on('click','div[id^="send_"]',function(){
            var id = $(this).data('id');
            var d = new Dialog({
                context_path:CONTEXT_PATH, //请求路径,  必填
                resType:1 ,//图片1，视频2，语音3   必填
                callback:function(url){
                    var imgObj = new Image();
                    var url = jumi.picParse(url,750);
                    imgObj.src = url;
                    imgObj.onload = function(){
                        if(imgObj.width!=750||imgObj.height!=1334){
                            var dm = new dialogMessage({
                                type:2,
                                fixed:true,
                                msg:'海报模板尺寸不对',
                                isAutoDisplay:true,
                                time:3000
                            });
                            dm.render();
                            return;
                        }
                        _setImage(url);
                        $("#qr_hidden_"+id).data('src',url);
                        $("#qr_hidden_"+id).data('uptime',jumi.format('yyyy-MM-dd hh:mm:ss'));
                    }
                }
            });
            d.render();
        })
        $('#activity_code_save').click(function(){
            var qrcodeVo = {};
            var array = [];
            var isChecked = false;
            $('input[id^="qr_hidden_"]').each(function(){
                var id = $(this).data('id');
                var flag = $("input[id='radioBox_"+id+"']").is(':checked');
                var colorValue = $(this).data('color').replace('#','')||'22c2fe';
                var qrcodePosters = {};
                qrcodePosters.qrcodeId = $(this).data('qrcodeid');
                qrcodePosters.leftPosition = $(this).data('left')||0;
                qrcodePosters.topPosition = $(this).data('top')||0;
                qrcodePosters.userBoxleftPosition = $(this).data('uleft')||0;
                qrcodePosters.userBoxtopPosition = $(this).data('utop')||0;
                qrcodePosters.nickNameleftPosition = $(this).data('nleft')||0;
                qrcodePosters.nickNametopPosition = $(this).data('ntop')||0;
                qrcodePosters.posterName = $(this).data('name')||'';
                qrcodePosters.imageSrc = $(this).data('src');
                qrcodePosters.fontColor = parseInt(colorValue,16);
                qrcodePosters.fontType =  $(this).data('font')||'宋体';
                qrcodePosters.codeFormat = $(this).data('format');
                qrcodePosters.uploadTime = $(this).data('uptime');
                if(flag){
                    qrcodePosters.type = 1;
                    isChecked = true;
                    if(!qrcodePosters.imageSrc){
                        var dm = new dialogMessage({
                            type:1,
                            fixed:true,
                            msg:'二维码图片未上传,无法保存',
                            isAutoDisplay:true,
                            time:3000
                        });
                        dm.render();
                    }
                }else{
                    qrcodePosters.type = 0;
                }
                array.push(qrcodePosters);
            })
            if(!isChecked){
                var dm = new dialogMessage({
                    type:1,
                    fixed:true,
                    msg:'请选择一张二维码',
                    isAutoDisplay:true,
                    time:3000
                });
                dm.render();
                return;
            }
            qrcodeVo.qrcodePosters = array;
            var jsonData = JSON.stringify(qrcodeVo);
            $.ajaxJson(ajaxUrl.url1,jsonData,{
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
        })

    }
    var _drag = function(){
        $("#drag").draggable({
            stop: function( event, ui ) {
                var id = $('input[name="activity_code"]:checked').data('id');
                $('#qr_hidden_'+id).data('left', ui.position.left);
                $('#qr_hidden_'+id).data('top',ui.position.top);
            },
            containment: "#code_wrap",
            cursor: "crosshair",
            scrollSpeed: 100
        });
        $("#u-drag").draggable({
            stop: function( event, ui ) {
                var id = $('input[name="activity_code"]:checked').data('id');
                $('#qr_hidden_'+id).data('uleft', ui.position.left);
                $('#qr_hidden_'+id).data('utop',ui.position.top);
            },
            containment: "#code_wrap",
            cursor: "crosshair",
            scrollSpeed: 100
        });
        $("#u-move-drag").draggable({
            stop: function( event, ui ) {
                var id = $('input[name="activity_code"]:checked').data('id');
                $('#qr_hidden_'+id).data('nleft', ui.position.left);
                $('#qr_hidden_'+id).data('ntop',ui.position.top);
            },
            containment: "#code_wrap",
            cursor: "crosshair",
            scrollSpeed: 100
        });
    }
    return {
        init: _init,
        setColor:_setColor,
        setFont:_setFont
    };
})();