CommonUtils.regNamespace("components");
components = (function(){
    var _roster,_leftBar,_mainWin,_hintWin,_messageHandler;

    var _init = function(app){



        var _componenets = {

        };
        _messageHandler = repository.msgHandler.getHandler(app);


        //_leftBar = components.leftBar.getLeftBar();
        _hintWin = repository.hintWin.getHintWin(app);
        _componenets['hintWin'] = _hintWin;
        _mainWin = repository.mainWin.getMainWin(app);
        _componenets['mainWin'] = _mainWin;
        _roster = repository.roster.getRoster(app);
        _componenets['roster'] = _roster;

        _mainWin.creatMainWin().then(function () {
            return _roster.init();
        }).then(function (customer) {
            return _roster.createLeftBar(customer);
        }).then(function (customers) {
            return _roster.createRosters(customers);
        }).then(function () {
            return _roster.myRoster();
        });
        app.setHandler(_messageHandler);
        app.addComponents(_componenets);
    };



    return {
        init:_init
    }
}());