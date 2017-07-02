//webim 引导程序,决定使用哪种im来进行沟通
CommonUtils.regNamespace("imBoot");
imBoot = (function(){

    var app,imApp;
    var default_setting = {
        MODEL:WEB_IM_CONFIG.MODEL.HX,
        CHAT_MODEL:WEB_IM_CONFIG.CHAT_MODEL.WE_CHAT,
        SAVE_MODEL:WEB_IM_CONFIG.SAVE_MODEL.LOCAL
    };

    var _startUp = function(data,options){
        default_setting = $.extend(true,default_setting,options);
        switch (default_setting.MODEL){
            case WEB_IM_CONFIG.MODEL.SELF:
                app = webSocket;
                break;
            case WEB_IM_CONFIG.MODEL.HX:
                app = hxIm;
                break;
        }
        imApp = app.init(data,default_setting);
        console.log(imApp);
        components.init(imApp);
        return app;
    };



    return {
        startUp:_startUp
    }
})();