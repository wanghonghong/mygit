CommonUtils.regNamespace("repository","historyHelper");

repository.historyHelper= (function(){

    var _historyCreator,_historyLoader;

    var HistoryHelper = function (setting) {

    };

    HistoryHelper.prototype.createTextHistory = function(){

    };
    HistoryHelper.prototype.createFileHistory = function () {

    };

    HistoryHelper.prototype.createImgHistory = function(){

    };

    var HistoryLoader = function (setting) {

    };

    HistoryLoader.prototype.loadHistory = function(){

    };

    HistoryLoader.prototype.loadMoreHistory =function (appid) {

    };

    var _getHistory = function(setting){
        if(_historyCreator){
            return _historyCreator;
        }else{
            return _historyCreator = new HistoryHelper(setting);
        }
    };

    var _getHistoryLoader = function(setting){
        if(_historyLoader){
            return _historyLoader;
        }else{
            return _historyLoader = new HistoryLoader(setting);
        }
    };

    return {
        getHistory:_getHistory,
        getHistoryLoader:_getHistoryLoader
    }

})();