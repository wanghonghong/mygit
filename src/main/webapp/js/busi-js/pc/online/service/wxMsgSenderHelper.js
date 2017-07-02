CommonUtils.regNamespace('wxMsgHelper');
wxMsgHelper = (function(){
    var wxTxtSendUrl = CONTEXT_PATH+'/kf/message';
    var wxFileSendUrl = '';

    var _sendTxt = function(appid,openid,type,content){
        var msgVo = {
            openid:openid,
            msg:encodeURIComponent(content),
            type:type,
            appid:appid
        }
        $.ajaxJson(wxTxtSendUrl,msgVo,{
            done:function(res){
                if(res.code==='0'||res.code===0){
                    console.log('success');
                }else{
                    ImInfo.sendToHx(content);
                }
            }
        });
    };
    var _sendFile = function(appid,openid,chatType,content,inputId){

    };



    return {
        sendTxt:_sendTxt,
        sendFile:_sendFile
    }
}());
