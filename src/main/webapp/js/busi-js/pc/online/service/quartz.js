CommonUtils.regNamespace('quartz');
quartz = (function(){
    var time_interval =5000;
    var url = CONTEXT_PATH+'/online/lastMsg';
    var _upUserLastMsg = function(username){
        var msgStrs = window.localStorage.getItem('userMsgs');
        var msgDetails = JSON.parse(msgStrs);
        $.ajaxJsonPut(url,msgDetails,{
            done:function(res){
                console.log(res);
            }
        });
    }

    var _init = function(){

    }


})();
