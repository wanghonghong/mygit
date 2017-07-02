
CommonUtils.regNamespace('ChatWin');
ChatWin = (function (){
    "use strict";
    //var that = this;
    var _scrollY;
    var emoji ={
        path: THIRD_URL+"/third/util/web-im/",
        map: {
            '[):]': 'ee_1.png',
            '[:D]': 'ee_2.png',
            '[;)]': 'ee_3.png',
            '[:-o]': 'ee_4.png',
            '[:p]': 'ee_5.png',
            '[(H)]': 'ee_6.png',
            '[:@]': 'ee_7.png',
            '[:s]': 'ee_8.png',
            '[:$]': 'ee_9.png',
            '[:(]': 'ee_10.png',
            '[:\'(]': 'ee_11.png',
            '[:|]': 'ee_12.png',
            '[(a)]': 'ee_13.png',
            '[8o|]': 'ee_14.png',
            '[8-|]': 'ee_15.png',
            '[+o(]': 'ee_16.png',
            '[<o)]': 'ee_17.png',
            '[|-)]': 'ee_18.png',
            '[*-)]': 'ee_19.png',
            '[:-#]': 'ee_20.png',
            '[:-*]': 'ee_21.png',
            '[^o)]': 'ee_22.png',
            '[8-)]': 'ee_23.png',
            '[(|)]': 'ee_24.png',
            '[(u)]': 'ee_25.png',
            '[(S)]': 'ee_26.png',
            '[(*)]': 'ee_27.png',
            '[(#)]': 'ee_28.png',
            '[(R)]': 'ee_29.png',
            '[({)]': 'ee_30.png',
            '[(})]': 'ee_31.png',
            '[(k)]': 'ee_32.png',
            '[(F)]': 'ee_33.png',
            '[(W)]': 'ee_34.png',
            '[(D)]': 'ee_35.png'
        }
    };
    var filetype = {
        "mp3" : true,
        "wma" : true,
        "wav" : true,
        "amr" : true,
        "avi" : true,
        "jpg" : true,
        "jpeg" : true,
        "gif" : true,
        "png" : true,
        "bmp" : true,
        "zip" : true,
        "rar" : true,
        "doc" : true,
        "docx" : true,
        "txt" : true,
        "pdf" : true
    };
    var _time =function(d){
        var date = new Date(d);
        return date.getHours()+':'+date.getMinutes()+':'+date.getSeconds();
    };
    // 获取 Date 对象的 年月日
    var _date = function(d){
        var date = new Date(d);
        return date.getFullYear()+"-"+date.getMonth()+"-"+date.getDate();
    };

    var _faceCodeMatch = function(str){
        var reg = /\[[\s\S]{2,3}\]/;
        while(str.search(reg)>=0){
            var face = str.match(reg);
            var facePath =emoji.path+'static/img/faces/'+emoji.map[face];
            var faceImg = '<img  src="'+facePath+'" />';
            str = str.replace(reg,faceImg);
        }
        return str;
    }

    var _showChatWin = function(name){
        try{
            name ='wrapper'+name;
            var chatWins = $(".webim-chat").find('.webim-chatwindow');
            $.each(chatWins,function(i,chatWin){
                $(chatWin).addClass('hide');
                var id = $(chatWin).attr("id");
                if(id === name){
                    $(chatWin).removeClass('hide');
                }
            });
        }catch (e){
            console.log(e);
        }
    };
    var _createChatWin = function(name,type,title,ext,callback){
        var wapper =  $("#wrapper"+name);
        if(wapper&&wapper.length){
            if(typeof callback==='function'){
                callback(name);
            }
        }else{
            var imChat = $(".webim-chat");
            //start
            var data = {
                BASE_PATH:CONTEXT_PATH,
                hxAccount:name,
                title:title||name,
                THIRD_URL:THIRD_URL,
                STATIC_URL:STATIC_URL
            };
            jumi.template('online/service/main_win',data,function(tpl){
                imChat.append(tpl);
                _bandEvent(name,ext);
                var suser = ImInfo.getSuser();
                try{
                    historyHelper.loadHistorys(name,suser,function(historys){
                        for(var rownum = 0;rownum<historys.length;rownum++){
                            var history = historys[rownum];
                            var model = history.to_user==name?'right':'left';
                            //var img =history.from_user == name?userImgUrl:shopImg;
                            switch(history.type){
                                case 'face':
                                case 'txt':
                                case 'text':
                                    var msg = {
                                        msg:history.msg,
                                        from:history.from_user,
                                        create_date:history.create_date
                                    }
                                    _createTextChatHistory(msg,model,name,false);
                                    break;
                                case 'pic':
                                    var sendUser = history.from_user;
                                    var msg ={
                                        url:history.file_url,
                                        from:sendUser,
                                        file_source:history.file_source,
                                        create_date:history.create_date
                                    };
                                    _createPicHistory(msg,model,name,sendUser,null,false);
                                    break;
                                case 'file':
                                    break;
                            }
                        }
                        var lastMsg = historys[historys.length-1];

                        if(typeof(callback)==="function"){
                            callback(name);
                        }
                    });
                }catch (e){
                    console.error(e);
                }
            });
        }
    };

    var _createSendWrapper = function(name,type){

        var sendWrapper = '<div class="webim-send-wrapper">';
        //options
        var options = '<div class="webim-chatwindow-options">';
        var emotion = '<span class="webim-emoji-icon font smaller" chat-type="'+type+'">J</span>';
        options += emotion;
        //$(options).append(emotion);
        var audio = '<span class="webim-picture-icon font smaller" chat-type="'+type+'">K</span>';
        options += audio;
        //$(options).append(audio);
        var picture = '<span class="webim-audio-icon font smaller" chat-type="'+type+'">R</span>';
        options += picture;
        //$(options).append(picture);
        var file = '<span  class="webim-file-icon font smaller" chat-type="'+type+'">S</span>';
        options += file;
        options+='</div>';
        sendWrapper += options;
        //$(sendWrapper).append(options);
        sendWrapper += _emotion();
        //content
        var content = '<textarea id="content'+name+'"></textarea>';
        sendWrapper += content;
        //send button
        var sendBtn;
        switch (type){
            case 'friends':
                sendBtn = '<button class="webim-button bg-color webim-send-btn base-bgcolor disabled" chat-type="chat"  source="'+name+'">发送</button>';
                break;
            case 'groups':
                sendBtn = '<button class="webim-button bg-color webim-send-btn base-bgcolor disabled" chat-type="groupchat"  source="'+name+'">发送</button>';
                break;
            case 'chatrooms':
                break;
            case '':
                break;
        }

        sendWrapper += sendBtn;
        sendWrapper +='</div>';

        return sendWrapper;
    }
    var _emotion = function(){
        var html ='<ul class="faces hide">';
        html+='<li key="[):]" class="webim-emoji-item"><img src="'+emoji.path+'/static/img/faces/ee_1.png"></li>';
        html+='<li key="[:D]" class="webim-emoji-item"><img src="'+emoji.path+'/static/img/faces/ee_2.png"></li>';
        html+='<li key="[;)]" class="webim-emoji-item"><img src="'+emoji.path+'/static/img/faces/ee_3.png"></li>';
        html+='<li key="[:-o]" class="webim-emoji-item"><img src="'+emoji.path+'/static/img/faces/ee_4.png"></li>';
        html+='<li key="[:p]" class="webim-emoji-item"><img src="'+emoji.path+'/static/img/faces/ee_5.png"></li>';
        html+='<li key="[(H)]" class="webim-emoji-item"><img src="'+emoji.path+'/static/img/faces/ee_6.png"></li>';
        html+='<li key="[:@]" class="webim-emoji-item"><img src="'+emoji.path+'/static/img/faces/ee_7.png"></li>';
        html+='<li key="[:s]" class="webim-emoji-item"><img src="'+emoji.path+'/static/img/faces/ee_8.png"></li>';
        html+='<li key="[:$]" class="webim-emoji-item"><img src="'+emoji.path+'/static/img/faces/ee_9.png"></li>';
        html+='<li key="[:(]" class="webim-emoji-item"><img src="'+emoji.path+'/static/img/faces/ee_10.png"></li>';
        html+='<li key="[:\'(]" class="webim-emoji-item"><img src="'+emoji.path+'/static/img/faces/ee_11.png"></li>';
        html+='<li key="[:|]" class="webim-emoji-item"><img src="'+emoji.path+'/static/img/faces/ee_12.png"></li>';
        html+='<li key="[(a)]" class="webim-emoji-item"><img src="'+emoji.path+'/static/img/faces/ee_13.png"></li>';
        html+='<li key="[8o|]" class="webim-emoji-item"><img src="'+emoji.path+'/static/img/faces/ee_14.png"></li>';
        html+='<li key="[8-|]" class="webim-emoji-item"><img src="'+emoji.path+'/static/img/faces/ee_15.png"></li>';
        html+='<li key="[+o(]" class="webim-emoji-item"><img src="'+emoji.path+'/static/img/faces/ee_16.png"></li>';
        html+='<li key="[<o)]" class="webim-emoji-item"><img src="'+emoji.path+'/static/img/faces/ee_17.png"></li>';
        html+='<li key="[|-)]" class="webim-emoji-item"><img src="'+emoji.path+'/static/img/faces/ee_18.png"></li>';
        html+='<li key="[*-)]" class="webim-emoji-item"><img src="'+emoji.path+'/static/img/faces/ee_19.png"></li>';
        html+='<li key="[:-#]" class="webim-emoji-item"><img src="'+emoji.path+'/static/img/faces/ee_20.png"></li>';
        html+='<li key="[:-*]" class="webim-emoji-item"><img src="'+emoji.path+'/static/img/faces/ee_21.png"></li>';
        html+='<li key="[^o)]" class="webim-emoji-item"><img src="'+emoji.path+'/static/img/faces/ee_22.png"></li>';
        html+='<li key="[8-)]" class="webim-emoji-item"><img src="'+emoji.path+'/static/img/faces/ee_23.png"></li>';
        html+='<li key="[(|)]" class="webim-emoji-item"><img src="'+emoji.path+'/static/img/faces/ee_24.png"></li>';
        html+='<li key="[(u)]" class="webim-emoji-item"><img src="'+emoji.path+'/static/img/faces/ee_25.png"></li>';
        html+='<li key="[(S)]" class="webim-emoji-item"><img src="'+emoji.path+'/static/img/faces/ee_26.png"></li>';
        html+='<li key="[(*)]" class="webim-emoji-item"><img src="'+emoji.path+'/static/img/faces/ee_27.png"></li>';
        html+='<li key="[(#)]" class="webim-emoji-item"><img src="'+emoji.path+'/static/img/faces/ee_28.png"></li>';
        html+='<li key="[(R)]" class="webim-emoji-item"><img src="'+emoji.path+'/static/img/faces/ee_29.png"></li>';
        html+='<li key="[({)]" class="webim-emoji-item"><img src="'+emoji.path+'/static/img/faces/ee_30.png"></li>';
        html+='<li key="[(})]" class="webim-emoji-item"><img src="'+emoji.path+'/static/img/faces/ee_31.png"></li>';
        html+='<li key="[(k)]" class="webim-emoji-item"><img src="'+emoji.path+'/static/img/faces/ee_32.png"></li>';
        html+='<li key="[(F)]" class="webim-emoji-item"><img src="'+emoji.path+'/static/img/faces/ee_33.png"></li>';
        html+='<li key="[(W)]" class="webim-emoji-item"><img src="'+emoji.path+'/static/img/faces/ee_34.png"></li>';
        html+='<li key="[(D)]" class="webim-emoji-item"><img src="'+emoji.path+'/static/img/faces/ee_35.png"></li>';
        html+='</ul>';
        return html;
    }
    var _createChatTitle = function(name,tt){
        if(tt){
            name =tt;
        }
        var title = '<p class="webim-chatwindow-title">'+name;
        title += '<i class="webim-down-icon font smallest closeBtn right"></i></p>';
        return title;
    };
    var _createChatWrapperhx = function(name){
        var wrapper = '<div class="webim-chatwindow-msg" id="msg'+name+'"></div>';
        return wrapper;
    };
    var _createTextChatHistory = function(message,model,name,saveFlag,callback){
        try{
            var msg ;
            if(!message.data||message.data===''){//当消息为自己发送时候message.data 为空
                msg = message.msg;
                msg = _getFullUrl(msg);
                msg = qq.face.match(msg);
                msg = _faceCodeMatch(msg)
            }else{
                if(typeof(message.data)==='string'){
                    msg = _getFullUrl(message.data);
                    msg = qq.face.match(msg);
                }else if(typeof(message.data)==='object'){
                    msg='';
                    for(var i in message.data){
                        var temp = Easemob.im.Utils.parseEmotions(message.data[i].data);
                        if(temp.indexOf('.png')){
                            msg +='<img src="'+emoji.path+temp+'" />';
                        }else{
                            msg +=_getFullUrl(temp);
                        }
                    }
                }
            }
            var userDetails = ImInfo.usersDetail;
            var userDetail = userDetails[message.from];
            var user_img = userDetail.headimgurl||'';
            var user_name = userDetail.nickname||'';
            var data = {
                BASE_PATH:CONTEXT_PATH,
                type:'txt',
                model:model,
                name:user_name||message.from,
                time:message.create_date||new Date().Format('yyyy-MM-dd hh:mm:ss'),
                msg:msg,
                userimg:user_img,
                hxAccount:message.from,
                THIRD_URL:THIRD_URL,
                STATIC_URL:STATIC_URL
            }
            if(_isExtData(message)){
                _extDataHandle(message,data);
                return;
            }else{
                jumi.template('online/service/history',data,function(tpl){
                    $('#msg'+name).append(tpl);
                    _clearChat(name);
                    var sendBtn = $("#wrapper"+name).find('.webim-send-btn');
                    $(sendBtn).attr("road","webim");
                    if(saveFlag){
                        var entity = historyHelper.entityFactory(message,'text',model,null);
                        historyHelper.saveHistory(entity);
                    }
                    if(typeof(callback)==='function'){
                        callback(message);
                    }
                });
            }
        }catch(e){
            console.error(e);
        }

    };

    var _createLastMsg = function(name){
        try{
            var localStorage = window.localStorage;
            if(localStorage){
                var str = localStorage['userMsgs'];
                if(str&&str.length>0){
                    var userMsgs = JSON.parse(str);
                    var userDetail = userMsgs[name];
                    if(userDetail){
                        var message = {
                            from:name,
                            type:userDetail.type,
                            data:userDetail.lastMsg,
                            ext:userDetail.ext
                        }
                        //_createTextChatHistory(message,'left',name);
                    }
                }
            }
        }catch (e){
            console.log(e);
        }
    };

    var _bandEvent = function(name,ext){
        try{

            var chatWin = $("#wrapper"+name);
            var closeBtn = $(chatWin).find('.closeBtn');
            //console.log(closeBtn);
            $(closeBtn).bind('click',function(){
                //console.log('click closeBtn');
                ImInfo.closeWin();
            });
            var sendBtn = $(chatWin).find('.webim-send-btn');
            //(sendBtn);
            $(sendBtn).click(function(){
                var name = $(this).attr('source');
                var chatType= $(this).attr('chat-type');
                ImInfo.sendMsg(name,chatType,ext);
                _gotoBottom(name);
            });
            var emotion = $(chatWin).find('.webim-emoji-icon');
            $(emotion).click(function(){
                var faces = $(this).parent().parent().next();
                if($(faces).hasClass('hide')){
                    $(faces).removeClass('hide');
                }else{
                    $(faces).addClass('hide');
                }
            });
            var face = $(chatWin).find('.faces > li');
            $(face).click(function(){
                var content =  $(this).parent().next();
                var key = $(this).attr('key');
                var value = content.val();
                value += key;
                content.val(value);
                $(face).parent().addClass('hide');
            });
            var picInput = $(chatWin).find('.webim-picture-icon');
            var fileInput = $(chatWin).find('.webim-file-icon');
            var target = $('.friends').find('div[target="'+name+'"]');
            $(picInput).click(function(){
                target = $('.friends ').find('div[class="webim-contact-item selected"]');
                var road;
                name = $(target).attr('target');
                var appid;
                var openid;
                var userDetail = ImInfo.usersDetail[name];
                if(userDetail){
                    appid = userDetail.appid;
                    openid = userDetail.openid;
                    road = userDetail.road;
                }
                road = road|| $(target).attr('road');
                switch(road){
                    case 'weixin':
                        initWxUpter(target,'image',appid,openid);
                        break;
                    case 'webim':
                        var chatType = $(this).attr('chat-type');
                        // _uploadShim('uploadShim','pic',name,chatType);
                        $("#uploadShim").bind('change',function(){
                            $(this).unbind('change');
                            _sendFiles(name,'uploadShim',chatType,'pic',function(data,fileObj){
                                data.from = ImInfo.getSuser();
                                data.to = name;
                                _createPicHistory(data,'right',name,ImInfo.getSuser(),fileObj,true);
                               /* var history =  historyHelper.entityFactory(data,'pic','right',fileObj);
                                historyHelper.saveHistory(history);*/
                                _gotoBottom(name);
                            });
                        });
                        $("#uploadShim").click();
                        break;
                }
            });
            $(fileInput).click(function(){
                var road = $(target).attr('road');
                switch(road){
                    case 'weixin':
                        var dm = new dialogMessage({
                            type:2,
                            fixed:true,
                            msg:'微信文件功能还未完成',
                            isAutoDisplay:true,
                            time:3000
                        });
                        dm.render();
                        break;
                    case 'webim':
                        var chatType = $(this).attr('chat-type');
                        $("#uploadShim").bind('change',function(){
                            $(this).unbind('change');
                            _sendFiles(name,'uploadShim',chatType,'file',function(data,file){
                                _createFileHistory(data,'right',name,file);
                                data.from = ImInfo.getSuser();
                                data.to = name;
                                _gotoBottom(name);
                            });
                        });
                        $('#uploadShim').click();
                        break;
                }
            });
            var linkBtn = $('.webim-link-icon');
            $(linkBtn).click(_mallLink);
            //微信表情按钮
            var wxFaceBtn = $(chatWin).find('.wx-emoji-icon');
            $(wxFaceBtn).bind('click',function(){
                var target = $('.friends ').find('div[class="webim-contact-item selected"]');
                var hxAccont = $(target).attr('target');
                qq.face.faceDialog(this,{top:'-220px'},function(face){
                    if(face){
                        var value = $('#content'+hxAccont).val();
                        $('#content'+hxAccont).val(value+face.code);
                    };
                });

            });
        }catch (e){
            console.log(e);
        }
    };

    var initWxUpter = function(target,fileType,appid,openid){
        $("#wxUpload").fileupload({
            url:CONTEXT_PATH+'/kf/file',
            formData:{
                openid:openid||$(target).attr('openid'),
                appid:appid||$(target).attr('appid'),
                type:fileType
            },
            done:function(e,data){
                if(data.result.code===0||data.result.code==="0"){
                    var files = data.files;
                    var targetAcc = $(target).attr('target');
                    _savePic(targetAcc,files[0]);
                    _gotoBottom(targetAcc);
                }else{
                    var dm = new dialogMessage({
                        type:2,
                        fixed:true,
                        msg:'图片发送失败',
                        isAutoDisplay:true,
                        time:3000
                    });
                    dm.render();
                }
            }
        });
        $("#wxUpload").click();
    };

    var _downFile = function(message,model,bindId){
        try{
            $("#"+bindId).bind('click',function(){
                var opt = {
                    method:'GET',
                    responseType:'blob',
                    mimeType:'text/plain; charset=x-user-defined',
                    uri:'',
                    secret:'',
                    accessToken:'',
                    onFileUploadComplete:function(data){
                        //upload file success
                    },
                    onFileUploadError:function(e){
                        //upload file error ,
                    }
                };
                //console.log(message);
                switch(model){
                    case 'right':
                        opt.secret = message.entities[0]['share-secret'];
                        opt.uri = opt.uri+message.entities[0].uuid;
                        break;
                    case 'left':
                        opt.secret = message.secret;
                        opt.accessToken = message.accessToken;
                        opt.uri = message.url;
                        break;
                }
                console.log(opt);
                Easemob.im.Helper.download(opt);
            });
        }catch (e){
            console.log(e);
        }
    };
    var _clearChat = function(name){
        $("#content"+name)[0].value = '';
    };
    var _sendFiles = function(sendTo,fileId,chatType,fileType,callback){

        try{
            var fileObj = Easemob.im.Helper.getFileUrl(fileId);
            if (fileObj.url == null || fileObj.url == '') {
                alert("请选择发送图片");
                return;
            }
            var sendfiletype = fileObj.filetype;
            var filename = fileObj.filename;
            if(!_checkFileType(sendfiletype)){
                alert("不支持该类型文件");
            }
            var type;
            switch(chatType){
                case 'friends':
                    type='chat';
                    break;
                case 'groups':
                    type='groupchat';
                    break;
            }
            var opt = {
                type:type,
                fileInputId :fileId,
                to : sendTo,
                onFileUploadError : function(error) {
                    console.log('发送失败');
                },
                onFileUploadComplete : function(data) {
                    if(typeof (callback)==="function" ){
                        callback(data,fileObj);
                    }
                }
            };
            var conn = ImInfo.conn();
            if(conn){
                switch(fileType){
                    case "pic":
                        conn.sendPicture(opt);
                        break;
                    case "file":
                        conn.sendFile(opt);
                        break;
                    case "aud":
                        break;
                }

            }
        }catch (e){
            console.log(e);
        }
        return;
    };
    var _checkFileType = function(fType){
        return filetype[fType];
    };
    //id:聊天对象 sendUser 发送者
    var _createPicHistory = function(message,model,id,sendUser,fileObj,saveFlag){
        try{
            var src='';
            switch(model){
                case "left":
                    src = message.url;
                    id = message.from;
                    break;
                case "right":
                    if(message.uri){
                        src=message.uri+'/'+message.entities[0].uuid;
                    }else{
                        src=message.url;
                    }
                    break;
            };
            var userDetail = ImInfo.usersDetail[sendUser];
            var user_img;
            var user_name;
            if(userDetail){
                user_img = userDetail.headimgurl||'';
                user_name = userDetail.nickname||'';
            }
            var source = (message.ext&&message.ext.type==='weixin')||message.file_source==='wx'?'wx':'hx';
            var data = {
                BASE_PATH:CONTEXT_PATH,
                type:'pic',
                model:model,
                name:user_name||sendUser,
                time:message.create_date||new Date().Format('yyyy-MM-dd hh:mm:ss'),
                userimg:user_img,
                hxAccount:sendUser,
                PIC_SRC:src,
                source:source,
                THIRD_URL:THIRD_URL,
                STATIC_URL:STATIC_URL
            };
            if(source=='wx'){
                data.frameid = 'frameimg' + Math.random();
            }
            jumi.template('online/service/history',data,function(tpl){
               $("#msg"+id).append(tpl);
                if(saveFlag){
                    message.msg = '[图片]';
                    message.file_source = source;
                    var entity = historyHelper.entityFactory(message,'pic',model,fileObj);
                    historyHelper.saveHistory(entity);
                }
            });
        }catch (e){
            console.log(e);
        }
    };
    var _createFileHistory = function(message,model,id,file){
        try{
            var downLoadId ;
            var fileName;
            switch(model){
                case 'right':
                    downLoadId = message.uri+message.entities[0].uuid;
                    fileName = file.filename;
                    break;
                case 'left':
                    downLoadId = message.url;
                    fileName = message.filename;
                    break;
            }
            var data ={
                BASE_PATH:CONTEXT_PATH,
                type:'file',
                model:model,
                hxAccount:id,
                time:new Date().Format('yyyy-MM-dd hh:mm:ss'),
                href:downLoadId,
                fileName:fileName
            };
            jumi.template('online/service/history',data,function(tpl){
                $("#msg"+id).append(tpl);
                message.msg = '[文件]';
                var entity = historyHelper.entityFactory(message,'file',model,file);
                historyHelper.saveHistory(entity);
            });
        }catch (e){
            console.log(e);
        }
    };

    var _getFullUrl = function (str){
        var reg = /((http|ftp|https):\/\/)(([a-zA-Z0-9\._-]+\.[a-zA-Z]{2,6})|([0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}))(:[0-9]{1,4})*(\/[a-zA-Z0-9\&%_\./-~-]*)?/g;
        var url = str.match(reg);
        //console.log(url);
        var a = '<a class="link" target="_blank" href="'+url+'" >'+url+'</a>';
        str = str.replace(url,a);
        return str;
    };

    var _gotoBottom = function(msgId){
        var scrollY =0;
        if($('#msg'+msgId)[0]){
            scrollY = $('#msg'+msgId)[0].scrollHeight;
        }
        $('#msg'+msgId).animate({"scrollTop":scrollY},{speed:1000});
    };


    var _isExtData = function (message) {
        return message.ext&&message.ext.type?true:false;
    };
    var _extDataHandle = function(message,data){
        try{
            if(message.ext&&message.ext.type){
                var type = message.ext.type;
                switch(type){
                    case 'product_info':
                        console.log('product_info..........');
                        extDataUtils.createProductInfo(message.from,message.ext.pid,function(ext){
                            data.ext = ext;
                            jumi.template('online/service/history',data,function(tpl){
                                $("#msg"+data.hxAccount).append(tpl);
                                var sendBtn = $("#wrapper"+data.hxAccount).find('.webim-send-btn');
                                $(sendBtn).attr("road","webim");
                            });
                        });
                        break;
                    case 'weixin':
                        jumi.template('online/service/history',data,function(tpl){
                            $("#msg"+data.hxAccount).append(tpl);
                            var sendBtn = $("#wrapper"+data.hxAccount).find('.webim-send-btn');
                            $(sendBtn).attr("road","weixin");
                            message.source ='weixin';
                            var entity = historyHelper.entityFactory(message,data.type,'right',null);
                            historyHelper.saveHistory(entity);
                        });
                        break;
                }
            }
        }catch (e){
            console.error(e);
        }
    };

    var _savePic = function(target,file){
        try{
            switch(fileHelper.storageType){
                case 'LOCAL':
                    fileHelper.fileStorage.saveFile(file,function(filename){
                        console.log(filename);
                        fileHelper.fileStorage.loadFile(filename,function(url){
                            console.log(url);
                            //message,model,id,sendUser,fileObj
                            var msg = {
                                from:ImInfo.getSuser(),
                                to:target,
                                url:url
                            };
                            var fileObj = {
                                filename:filename
                            };
                            var model = 'right';
                            var id = target;
                            var sendUser = ImInfo.getSuser();
                            _createPicHistory(msg,model,id,sendUser,fileObj,true);
                        })
                    });
                    break;
                case 'TEMP':
                    break;
                case 'SERVICE':
                    break;
            }
        }catch(e){
            console.log(e);
        }
    };

    var _mallLink = function(){
        var shopId = ImInfo.getShopId();
        menuDialog.Menu.initPage({
            shop_id:shopId,
        },function(menu){
            menu.getUrl(function(link){
                var url;
                console.log(link);
                switch(link.link_type){
                    case '1':
                        url = DOMAIN+link.link_url+"?shopId="+link.shop_id;
                        break;
                    case '2':
                        url = DOMAIN+link.link_url+'/'+link.link_id+"?shopId="+link.shop_id;
                        break;
                    case '3':
                        url = CONTEXT_PATH+link.link_url+"?shopId="+link.shop_id;
                    case '4':
                        url = link.link_url;
                }
                var target = $('.friends ').find('div[class="webim-contact-item selected"]');
                var name;
                if(target[0]){
                    name = $(target).attr('target');
                }
                if(link.type=='view'){
                    $('#content'+name).val(link.link_name+":"+url);
                }else if(link.type=='qrcode'){

                }else if(link.type =='custom'){
                    $('#content'+name).val(url);
                }
            });
        });
    };

    //根据最后一条记录改变用户发送渠道
    var changeRoadByLastMsg = function(lastMsg){
        var source = lastMsg.source;

    }

    return{
        createTextChatHistory:_createTextChatHistory,
        clear:_clearChat,
        createChatWin:_createChatWin,
        createPicHistory:_createPicHistory,
        createFileHistory:_createFileHistory,
        createLastMsg:_createLastMsg,
        showChatWin:_showChatWin,
        gotoBottom:_gotoBottom
    };
})();
