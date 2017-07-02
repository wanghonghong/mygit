CommonUtils.regNamespace("templateLoader");
templateLoader = (function(){

    var TEMP_VERSION="100";

    var _tempNames = ['chatWin','leftMsg','main','myCustomer','rightBar','rightMsg','waitRoster'];

    var _templates = [];

    var _initTemplates = function(){

    };



    var queue = {
       level:[]
    };
    queue.addLoadTemp = function(fun,args,level){
        if(level>10){
            level = 10;
        }
        if(!this.level[level]){
            this.level[level] = [];
        }
        this.level[level].add({
            fun:fun,
            arg:args
        });
    };
    queue.startLoop = function () {
        for(var i=0;i<10;i++){
            if(this.level[i]){
                var funs = this.level[i];
                for(var j in funs){
                    var funObj = funs[j];
                    var fun = funObj.fun;
                    var arg = funObj.arg;
                    fun.call(arg);
                }
            }
        }
    };

    (function(){
        if(window.localStorage ){
            var clearSwitch = window.localStorage.getItem("TEMP_VERSION");
            if(!clearSwitch||clearSwitch!==TEMP_VERSION){
                for(var i in _tempNames){
                    window.localStorage.removeItem(_tempNames[i]);
                }
                window.localStorage.setItem("clearSwitch",TEMP_VERSION);
            }
        }
    })();

    var _loader = function(url,data,target,level){

        jumi.template(url,data,function(tpl){
            $(target).append(tpl);
        });
    };

    return {
        templates:templates,
        queue:queue
    }
})();