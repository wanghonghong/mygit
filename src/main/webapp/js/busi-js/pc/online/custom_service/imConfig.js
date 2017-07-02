CommonUtils.regNamespace("WEB_IM_CONFIG");
WEB_IM_CONFIG = (function(){
    var MODEL ={
        SELF:"SELF",
        HX:"HX"
    };

    var CHAT_MODEL = {
        WE_CHAT:"WE_CHAT",
        WEB_SOCKET:"WEB_SOCKET"
    };

    var SAVE_MODEL = {
        LOCAL:"LOCAL",
        SERVER:"SERVER"
    };

    var LOCAL_DATA_MODEL = {
        INDEX_DB:" INDEX_DB",
        WEB_SQL:"WEB_SQL"
    };

    return {
        MODEL:MODEL,
        CHAT_MODEL:CHAT_MODEL,
        SAVE_MODEL:SAVE_MODEL,
        LOCAL_DATA_MODEL:LOCAL_DATA_MODEL
    }
})();
