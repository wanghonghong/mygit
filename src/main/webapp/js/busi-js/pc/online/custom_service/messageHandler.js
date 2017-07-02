CommonUtils.regNamespace("repository","msgHandler");
repository.msgHandler =(function(){
    var handler;


    var Handler = function(app){
        var setting = app.getSetting();
        this._model = setting.MODEL;
        this.app = app;
    };

    Handler.prototype.handle = function(message){
        if(this.isExtend(message)){
            this.extendHandle(message);
        }
        switch (this._model){
            case WEB_IM_CONFIG.CHAT_MODEL.WE_CHAT:
                this.hxMessageHandler(message);
                break;
            case WEB_IM_CONFIG.CHAT_MODEL.WEB_SOCKET:
                break;
        }
    };

    Handler.prototype.isExtend = function(message){
        if(message.ext){
            return true;
        }else{
            return false;
        }
    };

    Handler.prototype.textHistory = function(){

    };

    Handler.prototype.fileHistory = function(){

    };

    Handler.prototype.extendHandle = function(message){
        if(message.ext){
            var extType= message.ext.type;
            switch (extType){
                case 'ADD_WAIT_QUEUE':
                    this.app.components.roster.addWaitRoster(message);
                    break;
                case 'DEL_WAIT_QUEUE':
                    break;
                case 'PRODUCT_INFO':
                    break;
            }
        }
    };
    Handler.prototype.hxMessageHandler = function(message){

    };



    var _getHandler = function(app){
        if(handler){
            return handler;
        }else{
            handler = new Handler(app);
            return handler;
        }
    };

    return {
        getHandler :_getHandler
    }

})();