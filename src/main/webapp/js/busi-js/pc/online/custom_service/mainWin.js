CommonUtils.regNamespace("repository","mainWin");
repository.mainWin = (function () {

    var _mainWin;


     var MainWin = function (app) {
         this.data = app.getData();
         this.historyCreater = repository.historyHelper.getHistory(app);
         this.historyLoader = repository.historyHelper.getHistoryLoader(app);
         //creatMainWin(data);
    };

    MainWin.prototype.creatMainWin = function () {
        var def = $.Deferred();
        var data = this.data;
        var url = '/online/customer/main';
        var mainWin = $("#webim_chat");
        if(mainWin&&mainWin.html().length>0){
            def.resolve();
        }
        jumi.template(url,data,function (tpl) {
            $("#webim_chat").append(tpl);
            _bindEvent();
            def.resolve();
        });
        return def;
    };

    var _bindEvent = function () {
        var chatBox =  $("#webim_chat > .jm-chatbox");
        var userName = $(chatBox).find(".m-chattab > ul > .username");
        $(userName).click(function(){
            $(".statelist").fadeToggle();
            var stateList = $(".statelist").find("p");
            $.each(stateList,function (i,state) {
                $(state).unbind('click');
                $(state).bind('click',function () {
                    $(".statelist").fadeOut();
                });
            });
        });
    };

    var _getMainWin = function (app) {
      if(_mainWin){
          return _mainWin;
      }else{
          return _mainWin = new MainWin(app);
      }
    };

    return{
        getMainWin:_getMainWin
    }


})();