//环信im
CommonUtils.regNamespace("hxIm");
hxIm = (function () {

    var hxImApp;

    var HxIm = function (data,options) {
        this.setting = options;
        this.data = data;
        var that = this;
        this.conn = new Easemob.im.Connection({
            multiResources: Easemob.im.config.multiResources,
            https: true,
            url: Easemob.im.config.xmppURL
        });
        this.conn.listen({
            wait: '60',
            onOpened: function () {
                console.log('open connect');
                that.conn.setPresence();
                if (that.conn.isOpened()) {
                    console.log('on open heartBeat......');
                    that.conn.heartBeat(that.conn);
                    //console.log(conn);
                }
                //_createChatMainWin();
            },
            //当连接关闭时的回调方法
            onClosed: function () {

            },
            //收到文本消息时的回调方法
            onTextMessage: function (message) {
                console.log(message);
                that.handler.handle(message);
            },
            //收到表情消息时的回调方法
            onEmotionMessage: function (message) {
                that.handler.handle(message);
            },
            //收到图片消息时的回调方法
            onPictureMessage: function (message) {
                that.handler.handle(message);
            },
            //收到音频消息的回调方法
            onAudioMessage: function (message) {
                that.handler.handle(message);
            },
            //收到位置消息的回调方法
            onLocationMessage: function (message) {
                that.handler.handle(message);
            },
            //收到文件消息的回调方法
            onFileMessage: function (message) {
                that.handler.handle(message);
            },
            //收到视频消息的回调方法
            onVideoMessage: function (message) {
                that.handler.handle(message);
            },
            //收到联系人订阅请求的回调方法
            onPresence: function (message) {

            },
            //收到联系人信息的回调方法
            onRoster: function (message) {

            },
            //收到群组邀请时的回调方法
            onInviteMessage: function (message) {

            },
            onOffline: function () {
                console.log('off line');
            },
            //异常时的回调方法
            onError: function (message) {
                console.log(message);
                if (message.type === 1) {
                    that.components.hintWin.offLine();
                }
            }
        });
        this.online();
        this.handler;
        this.components;
    };
    HxIm.prototype.openWin = function () {
        this.components.mainWin.creatMainWin();
    };
    HxIm.prototype.closeWin = function () {

    };

    HxIm.prototype.offline = function () {

    };
    //打开环信 链接 /在线
    HxIm.prototype.online = function () {
        openConfig.user = this.data.account;
        openConfig.appKey = Easemob.im.config.appkey;
        openConfig.apiUrl = Easemob.im.config.apiURL;
        openConfig.pwd = '123456';
        console.log(openConfig);
        this.conn.open(openConfig);
    };
    HxIm.prototype.busyness = function () {

    };

    HxIm.prototype.addComponents = function(components){
        this.components = components;
    };



    var openConfig = {
        user: '',
        pwd: '123456'
    };

    //初始化链接
    var _init = function (data,options) {
        if(hxImApp){
            return hxImApp;
        }else{
            hxImApp = new HxIm(data,options);
            return hxImApp;
        }
    };




















    HxIm.prototype.getData = function(){
        return this.data;
    };
    HxIm.prototype.getConnection = function(){
        return this.conn;
    };
    HxIm.prototype.getSetting = function () {
        return this.setting;
    };
    HxIm.prototype.setHandler = function(handler){
        this.handler = handler;
    };

    return {
        init: _init
    }
})();