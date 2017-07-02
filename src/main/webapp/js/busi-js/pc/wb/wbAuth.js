/**
 * Created by whh on 2017/3/4
 */
CommonUtils.regNamespace("wb", "wbAuth");
wb.wbAuth = (function() {
    var ajaxUrl = {
        url1: CONTEXT_PATH + '/community/reply'
    };
    var cmt={
        reply:{}
    }
    var _init = function () {
        _bind();
    };

    var _bind = function(){

    }

    return {
        init :_init,
        replyConfig:_replyConfig,

    };
})();