CommonUtils.regNamespace('ImInfo','roster');
ImInfo.roster = (function(){


    var _getRostersAccount = function(roster,type,callback){
        try{
            var names = [];
            for(var i in roster){
                names.push(roster[i].name);
            }
            _getWxUserByHxAccount(names,type,callback);
        }catch (e){
            console.error(e);
        }
    };
    var _creatRoster = function(roster,type){

        _getRostersAccount(roster,type,function(users){
            try{
                var html = [];
                for(var i in roster){
                    var rosterDiv='';
                    var target ;
                    switch (type){
                        case 'friends':
                            target = roster[i].name;
                            break;
                        case 'groups':
                            target = roster[i].roomId;
                            break;
                        case 'chatrooms':
                            break;
                    }
                    rosterDiv ='<div target="'+target+'" class="webim-contact-item">';
                    if(type==='groups'){
                        rosterDiv ='<div target="'+target+'" target-name="'+roster[i].name+'" class="webim-contact-item">';
                    }
                    rosterDiv +='<div class="webim-avatar-icon">';
                    var targetname;
                    if(users[roster[i].name]){
                        ImInfo.usersDetail[roster[i].name] = users[roster[i].name];
                        targetname = users[roster[i].name].nickname;
                        rosterDiv ='<div target="'+target+'" target-name="'+targetname+'" class="webim-contact-item" road="weixin">';
                        if(type==='groups'){
                            rosterDiv ='<div target="'+target+'" target-name="'+roster[i].name+'" class="webim-contact-item">';
                        }
                        rosterDiv +='<div class="webim-avatar-icon">';

                        rosterDiv +='<img class="w100" src="'+users[roster[i].name].headimgurl+'">';
                        rosterDiv +='</div>';
                        rosterDiv +='<span>'+targetname+'</span>';
                    }else{
                        rosterDiv ='<div target="'+target+'" class="webim-contact-item" road="webim">';
                        if(type==='groups'){
                            rosterDiv ='<div target="'+target+'" target-name="'+roster[i].name+'"  class="webim-contact-item">';
                        }
                        rosterDiv +='<div class="webim-avatar-icon">';
                        targetname = roster[i].name;
                        rosterDiv +='<img class="w100" src="'+CONTEXT_PATH+'/img/pc/default.png">';
                        rosterDiv +='</div>';
                        rosterDiv +='<span>'+roster[i].name+'</span>';
                    }
                    rosterDiv +='<em class="offlineMsg"></em>';
                    rosterDiv +='<i data-count="0" class="webim-msg-prompt" count="0" style="display: none;"></i>';
                    rosterDiv += '</div>';
                    html.push(rosterDiv);
                    if(i ==4){
                        break;
                    }
                }
                switch(type){
                    case 'friends':
                        $(".roster").empty();
                        //$(".roster").append(rosterDiv);
                        for(var j in html){
                            $(".roster").append(html[j]);
                        }
                        break;
                    case 'groups':
                        $(".groups").empty();
                        for(var j in html){
                            $(".groups").append(html[j]);
                        }
                        //$(".groups").append(rosterDiv);
                        break;
                    case 'chatrooms':
                        $(".chatrooms").empty();
                        $(".chatrooms").append(rosterDiv);
                        break;
                    case 'strangers':
                        $(".strangers").empty();
                        $(".strangers").append(rosterDiv);
                        break;
                }
                //$(".roster").append();
                _initBindEvent(type);
            }catch (e){
                console.log(e);
            }
        });
    };
    var _initBindEvent = function(type){
        try{//绑定好友聊天室列表点击事件
            switch(type){
                case 'friends':
                    var roster = $(".roster > div");
                    $.each(roster,function(i,friend){
                        $(friend).click(function(){
                            var id = $(friend).attr("target");
                            var title = $(this).attr("target-name");
                            _select(type,id,title,function(name){
                                ChatWin.gotoBottom(name);
                            });

                        });
                    });
                    break;
                case 'groups':
                    var groups = $(".groups >div");
                    $.each(groups,function(i,group){
                        $(group).click(function(){
                            var id = $(this).attr("target");
                            var title = $(this).attr("target-name");
                            _select(type,id,title,function(name){
                                ChatWin.gotoBottom(name);
                            });
                        });
                    });
                    break;
                case 'chatrooms':

                    break;
                case 'strangers':

                    break;
            }
        }catch(e){
            console.log(e);
        }
    };

    var _bandEvent = function(div,callback){
        $(div).bind('click',callback());
    };
    var _select = function(type,id,title,ext,callback){

        var roster;
        var chatType;
        var parent ;
        switch(type){

            case'friends'||'chat':
                roster = $(".roster > div");
                parent = $('.roster');
                chatType = 'chat';
                break;
            case'groups'||'groupchat':
                roster = $(".groups > div");
                parent = $('.groups');
                chatType='groupchat';
                break;
            case'chatrooms':
                break;
            case'strangers':
                break;
            default:
                roster = $(".roster > div");
                parent = $('.roster');
                chatType = 'chat';
                break;
        }
        if(!title){
            title = id;
        }
        if(ext&&ext.type==='weixin'){
            title +='[来自微信]';
        }
        $('.webim-chatwindow-title').html(title);
        $.each(roster,function(i,friend){
            $(friend).removeClass("selected");
            var friendName = $(friend).attr("target");
            if(id == friendName ){
                $(friend).addClass("selected");
                /*
                 $(friend).fadeOut().fadeIn();
                 $(friend).find('.webim-msg-prompt').css('display','none');
                 $(parent).prepend(friend);
                 */
                $(friend).find('.webim-msg-prompt').css('display','none');
                $('#wrapper'+id).removeClass('hide');

            }else{
                $('#wrapper'+friendName).addClass('hide');
            }
            // 显示对应的聊天框
        });
        ChatWin.createChatWin(id,type,title,ext,function(name){
            if(typeof callback==='function'){
                callback(name);
            }
        });


    };
    var _getCurrentChat = function(type){
        var divs = _getRosters(type);
        var target = $(divs).find('.selected');
        return target;
    };

    var _isExists = function(type,name){
        var divs = _getRosters(type);
        var flag = false;
        $.each(divs,function(i,user){
           var target= $(user).attr('target');
           if(target ===name){
               flag= true;
           }
        });
        return flag;
    };
    var _getRosters = function(type){
        var divs;
        switch(type){
            case 'chat':
                divs = $('.webim-contact-wrapper').find('.friends ');
                break;
            case 'groupchat':
                divs = $('.webim-contact-wrapper ').find('.groups ');
                break;
        };
        return divs;
    };
    //messageType:file , pic , txt,face
    var _createRosterUser = function(name,tt,chattype,ext,callback){
        var wrapper;
        var title;
        switch(chattype){
            case 'chat':
                wrapper = $('.friends');
                title = name;
                break;
            case 'groupchat':
                wrapper = $('.groups');
                title = tt;
                break;

        }
        var div = $(wrapper).find('div[target="'+name+'"]');
        var isExists = false;
        if($(div)[0]&&$(div).length>0){
            console.log('------------div 存在-----------');
            isExists = true;
        }
        if(!isExists){
            _getWxUserByHxAccount([name],chattype,function(){
                var from = name;
                var headimg;
                var nickname;
                //添加聊天对象信息到缓存中并且添加索引
                if(ImInfo.usersDetail[name]){
                    headimg = ImInfo.usersDetail[name].headimgurl;
                    nickname = ImInfo.usersDetail[name].nickname;
                }
                var data = {
                    BASE_PATH:CONTEXT_PATH,
                    offLineMsg:'',
                    target:from,
                    target_name:nickname,
                    headimgurl:headimg,
                    shop_name:ext?ext.shopName:'',
                    staticPath:STATIC_URL
                }
                var first = $(wrapper).children(":first");
                jumi.template('online/service/roster',data,function(tpl){
                    if(first.length<1){
                        console.log('do append roster');
                        $(wrapper).append(tpl);
                        first = $(wrapper).children(":first");
                        first.bind('click',function(){
                            _select(chattype,from,nickname,ext,function(name){
                                ChatWin.gotoBottom(name);
                            });
                        });
                    }else{
                        first.after(tpl);
                        var next = first.next();
                        $(next).bind('click',function(){
                            _select(chattype,from,nickname,ext,function(name){
                                ChatWin.gotoBottom(name);
                            });
                        });
                    }
                    if(!$("#wrapper"+name)[0]){
                        console.log('do append main win');
                        ChatWin.createChatWin(from,chattype,title,ext,function(name){
                            if(typeof callback ==='function'){
                                callback(name);
                            }
                        });
                    }else{
                        if(typeof callback ==='function'){
                            callback(name);
                        }
                    }
                });
            });
        }else{
            if(typeof callback ==='function'){
                callback(name);
            }
        }
    };
    var _offlineMsgHandle = function(type,message){
        try{
            var html= '';
            if(message||typeof message ==='object') {
                switch (type) {
                    case 'txt':
                        html = message.data;
                        break;
                    case 'face':
                        for (var i in message.data) {
                            if (message.data[i].type === 'txt') {
                                html += message.data[i].data;
                            } else if (message.data[i].type === 'emotion') {
                                html += '[表情]';
                            }
                        }
                        break;
                    case 'file':
                        html = '[文件]';
                        break;
                    case 'pic':
                        html = '[图片]';
                        break;
                }
            }
            return html;
        }catch(e){
            console.log('创建离线消息');
        }
    };

    //向服务器请求用户信息
    var _getWxUserByHxAccount = function(names,type,callback){
        if(names.length<1){
            return;
        }
        var accounts = "accounts=";
        for(var i in names ){
            var name = names[i];
            if(!ImInfo.usersDetail[name]){
                if(i==0){
                    accounts += name;
                }else{
                    accounts += ("&accounts="+name);
                }
            }
        }
        if(accounts==='accounts='){
            if(typeof callback ==='function'){
                callback();
            }
            return;
        }
        var url = CONTEXT_PATH+"/online/wxUsers";
        $.ajaxJsonGet(url,accounts,{
            done:function(res){
                if(res.code===0||res.code==="0"){
                    var users = res.data.data;
                    _adduserIndex(users);
                    if(typeof callback ==='function'){
                        callback(users);
                    }
                }
            }
        });

    };

    //添加用户与环信账号的映射
    var _adduserIndex = function(users){
        for(var i in users){
            if(ImInfo.usersDetail[i]){
                var userObj = ImInfo.usersDetail[i];
                for(var v in users[i]){
                    userObj[v] = users[i][v] ;
                }
                ImInfo.usersDetail[i] = userObj;
            }else{
                ImInfo.usersDetail[i] = users[i];
                var userid = users[i].userid;
                ImInfo.userIndex[userid] = users[i].hxAcct;
            }
        }
        _saveUserIndex();
    };
    //保存用户映射
    var _saveUserIndex = function(){
        var storage = window.localStorage;
        var userDetail = JSON.stringify(ImInfo.usersDetail);
        storage.setItem('users',userDetail);
        var userIndex =JSON.stringify(ImInfo.userIndex);
        storage.setItem('userIndex',userIndex);
    };
    //初始化用户映射
    var _initUserIndex = function(){
        var storage = window.localStorage;
        var usersStr = storage.getItem("users");
        var usersIndexStr = storage.getItem("userIndex");
        if(usersStr){
            var users = JSON.parse(usersStr);
            ImInfo.usersDetail = $.extend(users,ImInfo.usersDetail);
        }
        if(usersIndexStr){
            var index = JSON.parse(usersIndexStr);
            ImInfo.userIndex = $.extend(index,ImInfo.userIndex);
        }
    };

    return {
        createRoster:_creatRoster,
        select:_select,
        bandEvent:_bandEvent,
        getCurrentChat:_getCurrentChat,
        isUserExists:_isExists,
        createRosterModul:_createRosterUser,
        createOffLineMsg:_offlineMsgHandle,
        initUserIndex:_initUserIndex,
        saveUserDetail:_adduserIndex
    }
})();