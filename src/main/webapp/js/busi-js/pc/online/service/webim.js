CommonUtils.regNamespace("ImInfo");
ImInfo = (function () {
    var usersDetail = {};
    var userIndex = {};
    var that = this;
    var from = 'hx_01';
    var conn;
    var saveTag = false;
    var uid;
    var storage = window.localStorage;
    var hxAccountSavePath = CONTEXT_PATH + '/online/HX/hxAccount';
    var roster = ImInfo.roster;
    var leftBar = ImInfo.leftBar;
    var suser;
    var _shopId;
    var _buffer = {};
    var messageCount = 0;
    var hintWin;

    var _filter = {};

    //var extDataUtils = extDataUtils;
    var openConfig = {
        user: '',
        pwd: '123456'
    };
    var _closeChatWin = function () {
        $("#webim_chat").empty();
        $("#webim_chat").addClass('hide');
        hintWin.show();
    };
    var _hideChatWin = function () {
        $("#webim_chat").addClass('hide');
        hintWin.showWin();
    };
    var _saveInLocalStore = function (msgObject, type, model, fileObj) {
        var entity = historyHelper.entityFactory(msgObject, type, model, fileObj);
        historyHelper.saveHistory(entity);
    };
    //设置弹出框位置
    var _setWinCss = function (id) {
        var width = $('body').width();
        var height = $('body').height();
        $(id).css({
            left: width / 2,
            top: height / 2
        })
    };
    var _connInit = function (callback) {
        try {
            console.log('conn init.....');
            if (!conn || conn === null) {
                conn = new Easemob.im.Connection({
                    multiResources: Easemob.im.config.multiResources,
                    https: true,
                    url: Easemob.im.config.xmppURL
                });
                conn.listen({
                    wait: '60',
                    onOpened: function () {
                        console.log('open connect');
                        conn.setPresence();
                        if (conn.isOpened()) {
                            console.log('on open heartBeat......');
                            conn.heartBeat(conn);
                            //console.log(conn);
                        }
                        //_createChatMainWin();
                    },
                    //当连接关闭时的回调方法
                    onClosed: function () {
                        /*var dm = new dialogMessage({
                         type:2,
                         fixed:true,
                         msg:'您的聊天账号已经离线',
                         isAutoDisplay:true,
                         time:10000
                         });
                         dm.render();*/
                        hintWin.offLine();
                    },
                    //收到文本消息时的回调方法
                    onTextMessage: function (message) {
                        console.log('收到文本消息:');
                        console.log(message);
                        var chatType = message.type;
                        _showMsgHandle(message, 'txt');
                        if (chatType === 'chat') {
                            // _saveInLocalStore(message,'text','left',null);
                        }
                    },
                    //收到表情消息时的回调方法
                    onEmotionMessage: function (message) {
                        //handleEmotion(message);
                        console.log('收到表情信息');
                        console.log(message, 'face');
                        _showMsgHandle(message, 'face');
                        var chatType = message.type;
                        if (chatType === 'chat') {
                            // _saveInLocalStore(message,'face','left',null);
                        }
                    },
                    //收到图片消息时的回调方法
                    onPictureMessage: function (message) {
                        // handlePictureMessage(message);
                        console.log('收到图片信息');
                        console.log(message);
                        _showMsgHandle(message, 'pic');
                        if (message.type === 'chat') {
                            // _saveInLocalStore(message,'pic','left',null);
                        }
                        //ChatWin.createPicHistory(message,'left','');
                    },
                    //收到音频消息的回调方法
                    onAudioMessage: function (message) {
                        // handleAudioMessage(message);
                        console.log('收到音频信息');
                        console.log(message);
                    },
                    //收到位置消息的回调方法
                    onLocationMessage: function (message) {
                        //  handleLocationMessage(message);
                    },
                    //收到文件消息的回调方法
                    onFileMessage: function (message) {
                        //  handleFileMessage(message);
                        console.log('收到文件');
                        console.log(message);
                        _showMsgHandle(message, 'file');
                        if (message.type === 'chat') {
                            //  _saveInLocalStore(message,'file','left',null);
                        }
                        //ChatWin.createFileHistory(message,'left','');
                    },
                    //收到视频消息的回调方法
                    onVideoMessage: function (message) {
                        // handleVideoMessage(message);
                    },
                    //收到联系人订阅请求的回调方法
                    onPresence: function (message) {
                        console.log('收到好友请求');
                        console.log(message);
                        conn.subscribed({
                            to: message.from,
                            message: "[resp:true]"//同意后发送反加对方为好友的消息，反加消息标识[resp:true]
                        });
                        roster.createRosterModul(message.from, message.from, 'chat');
                    },
                    //收到联系人信息的回调方法
                    onRoster: function (message) {
                        // handleRoster(message);
                    },
                    //收到群组邀请时的回调方法
                    onInviteMessage: function (message) {
                        //  handleInviteMessage(message);
                    },
                    onOffline: function () {
                        hintWin.offLine();
                    },
                    //异常时的回调方法
                    onError: function (message) {
                        console.log(message);
                        if (message.type === 1) {
                            hintWin.offLine();
                        }
                    }
                });
            }

            if (typeof(callback) === "function") {
                console.log('do callback');
                callback();
            } else {
                console.log('not do callback');
            }

        } catch (e) {
            console.log(e);
        }

    };
    var _init = function (user) {
        try {
            _buffer = historyHelper.loadBuffer() || {};
            console.log('buffer', _buffer);
            var length = 0;
            for (var i in _buffer) {
                length += _buffer[i].length;
            }
            if (!hintWin) {
                hintWin = new HintWin(length);
                hintWin.init();
            }
            _connInit(conn);
            //upUserLastMsg();
            $(window).bind('beforeunload', function () {
                //curChatRoomId = null;
                console.log('调用关闭');
                upUserLastMsg = null;
                if (conn) {
                    conn.stopHeartBeat();
                    conn.close();
                }
            });
            if (window.localStorage) {
                saveTag = true;
                roster.initUserIndex();
            } else {
                //alert('您的浏览器不支持localStorage');
            }

            _bindShortcutKey();
            historyHelper.init();
        } catch (e) {
            console.log(e);
        }
    };

    var _sendQrCodeMessage = function (name, chatType, url) {
        //sendPicture
        var options = {
            to: name,
            from: _getSuser(),
            type: chatType,
            ext: {
                url: url,
                type: 'qrcode'
            }
        }
    };

    var _sendMessage = function (name, chatType, ext) {
        var msg = $("#content" + name).val().trim();
        if (!msg || msg.length < 1) {
            return;
        }
        var to = name;
        var options = {
            to: to,
            msg: msg,
            type: chatType,
            from: _getSuser()
        };
        switch (chatType) {
            case 'chat':
                var target = $('.friends').find('div[target="' + name + '"]');
                if ($(target)[0]) {
                    var appid;
                    var openid;
                    var road;
                    var detail = ImInfo.usersDetail[name];
                    if (detail) {
                        appid = detail.appid;
                        openid = detail.openid;
                        road = detail.road;
                    }
                    road = road || $(target).attr('road');
                    switch (road) {
                        case 'weixin':
                            appid = appid || $(target).attr('appid');
                            openid = openid || $(target).attr('openid');
                            wxMsgHelper.sendTxt(appid, openid, 'text', msg);
                            break;
                        default:
                        case 'webim':

                            conn.sendTextMessage(options);
                            break;
                    }
                }
                break;
            case 'groupchat':

                break;
        }

        //_saveInLocalStore(options,sendType,'right',null);
        //ChatWin.clear();
        ChatWin.createTextChatHistory(options, 'right', name, true, function (message) {

        });
    };
    var _doUserReg = function (userId, callback) {
        var opt = {
            success: function (account) {
                _saveHxAccount(userId, account, 'C', function () {
                    if (typeof  callback === 'function') {
                        callback();
                    }
                });
            },
            error: function (res) {
                if (res.error === 'duplicate_unique_property_exists') {
                    console.log('do reg error....');
                    _saveHxAccount(userId, 'hx_' + userId, 'C', function () {
                        if (typeof  callback === 'function') {
                            callback();
                        }
                    });
                }
            }
        };
        _hxReg('hx_' + userId, opt);
    };
    var Webim = function (userId) {
        this.userId = userId;
    };
    Webim.prototype.wxChatModel = function (appid, openid, headimgurl, nickname) {
        ImInfo.usersDetail['hx_' + this.userId] = {
            appid: appid,
            openid: openid,
            headimgurl: headimgurl,
            nickname: nickname,
            road: 'weixin',
            hxAcc: 'hx_' + this.userId
        };
        userIndex[this.userId] = 'hx_' + this.userId;
    };

    Webim.prototype.wbChatModel = function () {

    };


    var _openChatWindow = function (obj, userId) {
        try {
            var html = $("#webim_chat").html().trim();
            _bind();
            if (!html || html === '') {
                if (obj) {
                    $(obj)[0].onclick = null;
                    $(obj).bind('click', function (event) {
                        var dataId = $(this).attr('data-id');
                        ImInfo.openChatWin(this, dataId);
                    });

                }
                _createChatMainWin(function () {
                    setTimeout(function () {
                        _setWinCss('#webim_win');
                        $('#webim_win').draggable({handle:'#webim-title',containment: "#shop_content", cursor: "move", scroll: false});
                    }, 1000);
                    debugger;
                    if (userId) {
                        _doUserReg(userId, function () {
                            roster.createRosterModul('hx_' + userId, '', 'chat', null, function () {
                                var friend = $('.friends').find('div[target="hx_' + userId + '"]');
                                //console.log('friend:', friend);
                                //读取内存中的userDetail 信息
                                $(friend).addClass("selected");
                                $(friend).fadeOut().fadeIn();
                                $(friend).find('.webim-msg-prompt').css('display', 'none');
                                $(friend).attr('road', 'webim');
                                $('#webim_chat').removeClass('hide');
                                $(friend).click();
                            });
                        });
                    }
                    _flush();
                    // 初始化聊天框
                });

                $("#webim_chat").removeClass('hide');
            } else {
                _setWinCss('#webim_win');
                if (userId) {
                    _doUserReg(userId, function () {
                        roster.createRosterModul('hx_' + userId, '', 'chat', null, function () {
                            var friend = $('.friends').find('div[target="hx_' + userId + '"]');
                            console.log('friend:', friend);
                            $(friend).addClass("selected");
                            $(friend).fadeOut().fadeIn();
                            $(friend).attr('road', 'webim');
                            $(friend).find('.webim-msg-prompt').css('display', 'none');
                            $('#webim_chat').removeClass('hide');
                            $(friend).click();
                        });
                    });
                }
                _flush();
                $('#webim_chat').removeClass('hide');
            }
            hintWin.clear();
            hintWin.hideWin();
        } catch (e) {
            console.error(e);
        }
        return new Webim(userId);
    };

    var _flush = function () {
        //循环离线消息
        for (var username in _buffer) {
            flushBuffer(_buffer[username], function (messages) {
                //删除本次用户的离线消息
                delete _buffer[username];
                historyHelper.cleanBuffer(username);
                for (var i in messages) {
                    switch (messages[i].msgType) {
                        case 'txt':
                        case 'text':
                        case 'face':
                            ChatWin.createTextChatHistory(messages[i], 'left', messages[i].from, true);
                            break;
                        case 'pic':
                            ChatWin.createPicHistory(messages[i], 'left', messages[i].from, messages[i].from, null, true);
                            break;
                        case 'file':
                            ChatWin.createFileHistory(messages[i], 'left', messages[i].from, messages[i].from);
                            break;
                    }
                }
            });
        }

    };

    var flushBuffer = function (messages, callback) {
        try {
            var lastMsg = messages[messages.length - 1];
            roster.createRosterModul(lastMsg.from, '', 'chat', lastMsg.ext, function () {
                var userModeul = $(".friends").find("div[target='" + lastMsg.from + "']");
                if ($(userModeul)[0]) {
                    var offlineHtml = roster.createOffLineMsg(lastMsg.msgType, lastMsg);
                    console.log('创建离线消息', offlineHtml);
                    $(userModeul).find('em').html(offlineHtml);
                }
                var custormer = ImInfo.usersDetail[lastMsg.from];
                if (lastMsg.ext && lastMsg.type && lastMsg.ext.type === 'weixin') {
                    $(userModeul).attr('road', 'weixin');
                    $(userModeul).attr('appid', lastMsg.ext.appid);
                    $(userModeul).attr('openid', lastMsg.ext.openid);
                    custormer.road = 'weixin';
                    custormer.appid = lastMsg.ext.appid;
                    custormer.openid = lastMsg.ext.openid;
                } else {
                    $(userModeul).attr('road', 'webim');
                    custormer.road = 'webim';
                }
                ImInfo.usersDetail[lastMsg.from] = custormer;
                var tempMessage = [];
                for (var i in  messages) {
                    tempMessage.push(messages[i]);
                }
                if (typeof callback === 'function') {
                    callback(tempMessage);
                }
            });
        } catch (e) {
            console.error(e);
        }
    };
    var _showMsgHandle = function (message, msgType) {
        try {

            var html = $("#webim_chat").html().trim();
            if ($("#webim_chat").hasClass('hide') || !html) {
                hintWin.plusMsg();
            }
            if (!html || html === '' || $("#webim_chat").hasClass('hide')) {
                console.log('离线消息推送..........');
                message.msgType = msgType;
                var userBuffer = _buffer[message.from];
                if (!userBuffer) {
                    userBuffer = [];
                }
                userBuffer.push(message);
                _buffer[message.from] = userBuffer;
                historyHelper.saveBuffer(_buffer);
            } else {
                hintWin.clear();
                switch (message.type) {
                    case 'chat':
                        _createHistoryHandle(message, msgType);
                        break;
                    case 'groupchat':
                        ChatWin.createTextChatHistory(message, 'left', message.to, true);
                        break;
                    case '':

                        break;
                    case '':
                        break;
                }
            }
        } catch (e) {
            console.error(e);
        }
    };
    var _createHistoryHandle = function (message, msgType) {
        //1.判断列表上是否有聊天对象，没有则创建聊天对象，并且创建隐藏聊天框
        //2.如果有聊天对象则 显示对象框下的聊天信息 并且判断是否为当前聊天者。否：显示提示
        //3.判断消息来源是否为微信
        roster.createRosterModul(message.from, message.name, 'chat', message.ext, function (from_user) {
            var str = roster.createOffLineMsg(msgType, message);
            var divs;
            var parent;
            switch (message.type) {
                case 'chat':
                    divs = $('.friends').find('div');
                    parent = $('.friends');
                    break;
                case 'groupchat':
                    divs = $('.groups').find('div');
                    parent = $('.groups');
                    break;
            }
            var target = $(parent).find('div[target="' + message.from + '"]');
            if (target && target.length > 0) {
                //收到消息的用户浮动到第一个
                var first = $(parent).find("div:first");
                if (first[0]) {
                    $(target).insertBefore(first);
                }
                $(target).find('em').html(str);
                if (!$(target).hasClass('selected')) {
                    $(target).find('i').show();
                }
                var customer = ImInfo.usersDetail[message.from];
                if (message.ext && message.ext.type === 'weixin' && message.ext.appid) {
                    customer.road = 'weixin';
                    customer.appid = message.ext.appid;
                    customer.openid = message.ext.openid;
                    $(target).attr('road', 'weixin');
                    $(target).attr('appid', message.ext.appid);
                    $(target).attr('openid', message.ext.openid);
                } else {
                    customer.road = 'webim';
                    $(target).attr('road', 'webim');
                }
                //ImInfo.usersDetail[message] = customer;
            }
            switch (msgType) {
                case 'txt':
                case 'face':
                    ChatWin.createTextChatHistory(message, 'left', message.from, true);
                    break;
                case 'pic':
                    ChatWin.createPicHistory(message, 'left', message.from, message.from, null, true);
                    break;
                case 'file':
                    ChatWin.createFileHistory(message, 'left', message.from, message.from);
                    break;
            }
            ChatWin.gotoBottom(message.from);
            return;
        });
    };

    // 打开与环信的通讯连接
    var _open = function (username) {
        console.log('open.....');
        openConfig.user = username;
        openConfig.appKey = Easemob.im.config.appkey;
        openConfig.apiUrl = Easemob.im.config.apiURL;
        console.log(openConfig);

        conn.open(openConfig);

    };
    // 保存 环信账号
    var _saveHxAccount = function (userid, hxAccount, type, callback) {
        console.log('userId:', userid, "account:", hxAccount);
        var userUo = {
            userId: userid,
            hxAccount: hxAccount,
            accountFlag: type
        };
        $.ajaxJsonPut(hxAccountSavePath, userUo, {
            done: function (res) {
                if (typeof callback === 'function') {
                    callback(res);
                }
            }
        })
    };
    // 初始化数据，未有环信账号用户则自动注册登录
    var _initData = function (hxAccount, userid, nickname, headImg, shopId) {
        var suser = _getSuser();
        that._shopId = shopId;
        if (!suser || suser === '') {
            console.log('first.......');
            if (!hxAccount || hxAccount === '') {
                var opt = {
                    success: function (account) {
                        suser = hxAccount;
                        _saveHxAccount(userid, account, 'S');
                        that.suser = hxAccount;
                        _open(account);
                        ImInfo.usersDetail[hxAccount] = {
                            headimgurl: headImg,
                            nickname: nickname
                        }
                    },
                    error: function (res) {
                        console.log(res.error);
                        if (res.error === 'duplicate_unique_property_exists') {
                            console.log('do error');
                            _open('hxs_' + shopId + '_' + userid, conn);
                            that.suser = hxAccount;
                            _saveHxAccount(userid, 'hxs_' + shopId + '_' + userid, 'S');
                            ImInfo.usersDetail[hxAccount] = {
                                headimgurl: headImg,
                                nickname: nickname
                            }
                        }
                    }
                };
                _hxReg('hxs_' + shopId + '_' + userid, opt);
            } else {
                console.log('has account', hxAccount);
                that.suser = hxAccount;
                _open(hxAccount);
                ImInfo.usersDetail[hxAccount] = {
                    headimgurl: headImg,
                    nickname: nickname
                }
            }
        } else {
            console.log('not first.....', suser);
            that.suser = hxAccount;
            ImInfo.usersDetail[hxAccount] = {
                headimgurl: headImg,
                nickname: nickname
            }
        }
    };
    //环信账号注册
    var _hxReg = function (userid, opt, callback) {
        try {
            //var user;
            console.log('do reg...');
            var options = {
                username: userid,
                password: '123456',
                appKey: Easemob.im.config.appkey,
                https: Easemob.im.config.https,
                success: function (res) {
                    console.log('注册成功');
                    if (typeof (opt.success) === "function") {
                        console.log('callback .....');
                        opt.success(options.username);
                    }
                },
                error: function (res) {
                    //console.log('注册失败', e);
                    if (typeof (opt.error) === "function") {
                        opt.error(res);
                    }
                }
            }
        } catch (e) {
            console.log(e);
        }
        Easemob.im.Helper.registerUser(options);
    };
    var _getRoster = function (conn, callback) {
        conn.getRoster({
            success: function (roster) {
                console.log(roster);
                callback(roster);
            }
        });
    };
    var _getChatRooms = function (conn, roomIdList, callback) {
        var occupants = [];
        conn.listRooms({
            success: function (rooms) {
                //console.log(rooms);
                callback(rooms);
            }
        });
        /*conn.queryRoomInfo({
         roomId:'239089675824791984',
         success:function(occs){
         console.log(occs);
         if(occs){
         for ( var i = 0; i < occs.length; i++) {
         occupants.push(occs[i]);
         }
         conn.queryRoomMember({
         roomId : '239089675824791984',
         success : function(members) {
         if (members) {
         for ( var i = 0; i < members.length; i++) {
         occupants.push(members[i]);
         }
         callback(occupants);
         }
         }
         });

         }
         }
         });*/
    };
    var _conn = function () {
        return conn;
    };
    var _user = function () {
        return openConfig.user;
    };
    var _addFriends = function (account) {

    };

    var _bindShortcutKey = function () {
        document.onkeydown = function (event) {
            var e = event || window.event || arguments.callee.caller.arguments[0];
            var focusedElement = document.activeElement;
            if (e && e.keyCode == 13 && focusedElement.tagName === 'TEXTAREA' && !e.shiftKey) { // enter 键
                $(focusedElement).next().click();
            }
        };
    };

    var _getSuser = function () {
        return that.suser;
    };
    var _getShopId = function () {

        return that._shopId;
    };

    var _createChatMainWin = function (callback) {
        var data = {
            BASE_PATH: CONTEXT_PATH,
            staticPath: STATIC_URL,
            THIRD_URL: THIRD_URL
        };
        try {
            jumi.template('online/service/chat_win', data, function (tpl) {
                $("#webim_chat").html(tpl);
                leftBar.bandEvent();
                if (typeof callback === 'function') {
                    callback();
                }
            });
        } catch (e) {
            console.log(e);
        }
    };

    var _sendToHx = function (content) {
        var currentUser = roster.getCurrentChat('chat').attr('target');
        var shopUser = _getSuser();
        var options = {
            to: currentUser,
            msg: content,
            type: 'chat',
            from: shopUser
        };
        conn.sendTextMessage(options);
    };


    var upUserLastMsg = function () {
        historyHelper.loadLastHistory(function (list) {
            var userUo = {};
            var wxUserList = [];
            for (var i in list) {
                var history = list[i];
                var user = {};
                //{"hx_2698":{"lastMsg":[{"type":"emotion","data":"static/img/faces/ee_6.png"}],"lastChatDate":"2016-09-27 09:49:44","hxAccount":"hx_2698","type":"chat","isReply":0}
                user.lastMsg = history.msg;
                user.lastChatDate = history.create_date;
                user.hxAccount = history.from_user;
                user.type = history.type;
                user.isReply = history.isReply || 0;
                wxUserList.push(user);
            }
            if (wxUserList != null && wxUserList.length > 0) {
                userUo.wxUserList = wxUserList;
                var data = JSON.stringify(userUo);
                var lastMsgSavePath = CONTEXT_PATH + '/online/lastMsg';
                $.ajax({
                    url: lastMsgSavePath,
                    contentType: 'application/json',
                    processData: false,
                    dataType: 'json',
                    method: 'POST',
                    data: data,
                    success: function (res) {
                        setTimeout(upUserLastMsg, 300000);
                    }
                });

            }
        });
    };


    var HintWin = function (count) {
        this.model = "z-hide";
        this.count = count;
    };
    HintWin.prototype.init = function () {
        var html = '<div id="hint" v-on:click="openWin" class="m-replymsg {{model}} "   v-bind:class="{hide:isHide,notreplymsg:offLine}" ><message-hint :count="msgCount"></message-hint> </div>';
        $('#webim_chat').after(html);
        Vue.component('message-hint', {
            props: ['count', 'model'],
            template: '<span><b>{{count}}</b>条</span>'
        });
        this.hint = new Vue({
            el: '#hint',
            data: {
                msgCount: this.count || 0,
                isHide: false,
                offLine: false,
                model: this.model
            },
            methods: {
                openWin: function () {
                    _openChatWindow();
                }
            }
        });
        var body = $('body')[0];
        window.onhashchange = function () {
            var hash = window.location.hash;
            if (hintWin && _filter[hash]) {
                //console.log('hintWin:',_filter[hash]);
                _getHintWin().setting({
                    model: _filter[hash]
                });
            } else {
                _getHintWin().setting({
                    model: "z-hide"
                });
            }
        }
    };
    HintWin.prototype.hideWin = function () {
        //$('#hint').addClass('hide');
        this.hint.isHide = true;
    };
    HintWin.prototype.showWin = function () {
        //$('#hint').removeClass('hide');
        this.hint.isHide = false;
    };
    HintWin.prototype.clear = function () {
        this.count = 0;
        this.hint.msgCount = 0;
        historyHelper.cleanBuffer();
    };
    HintWin.prototype.plusMsg = function () {
        this.count++;
        this.hint.msgCount = this.count;
    };
    HintWin.prototype.offLine = function () {
        this.hint.offLine = true;
    }
    HintWin.prototype.onLine = function () {
        this.hint.offLine = false;
    };
    HintWin.prototype.setting = function (options) {
        this.hint.model = options.model || "z-hide";
        var hash = window.location.hash;
        _filter[hash] = this.hint.model;
    };


    var _getHintWin = function () {
        return hintWin;
    };
    var _bind = function () {
        $(document).on('keydown', escClose);
    }
    var escClose = function (event) {
        var keyCode = event.keyCode;
        var type = event.type;
        if (keyCode === 27 && type === 'keydown') {
            ImInfo.hideWin();
        }
    };
    var _offline = function () {
        window.location.href = CONTEXT_PATH + '/loginout';
    };
    return {
        sendMsg: _sendMessage,
        init: _init,
        bind: _bind,
        openChatWin: _openChatWindow,
        closeWin: _closeChatWin,
        conn: _conn,
        initData: _initData,
        getUser: _user,
        getHintWin: _getHintWin,
        hideWin: _hideChatWin,
        getSuser: _getSuser,
        getShopId: _getShopId,
        sendToHx: _sendToHx,
        offline: _offline,
        usersDetail: usersDetail,
        userIndex: userIndex
    };
})();