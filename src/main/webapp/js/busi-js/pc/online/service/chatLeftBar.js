CommonUtils.regNamespace("ImInfo",'leftBar');
ImInfo.leftBar = (function(){

    var _bindEvent = function(){
        try{
            //.单聊
            var privateChat = $("#friends").find(".webim-leftbar-icon");
            if(privateChat&&privateChat.length>0){
                $(privateChat).bind('click',function(){
                    _selectContact('friends');
                });
            }
            //群聊
            var groupChat = $("#groups").find('.webim-leftbar-icon');
            if(groupChat&&groupChat.length>0){
                $(groupChat).bind('click',function(){
                    _selectContact('groups');
                });
            }
            //聊天室
            var chatRoom = $("#chatrooms").find('.webim-leftbar-icon');
            if(chatRoom&&chatRoom.length>0){
                $(chatRoom).bind('click',function(){
                    _selectContact('chatrooms');

                });
            }
            //陌生人
            var strangers = $("#strangers").find('.webim-leftbar-icon');
            if(strangers&&strangers.length>0){
                $(strangers).bind('click',function(){
                    _selectContact('strangers');
                });
            }
        }catch(e){
            console.log(e);
        }
    };
    var _selectContact = function(id){
        try{
            var iconBtn = $("#"+id).find(".webim-leftbar-icon");
            $(iconBtn).addClass('selected');
            var others = $("#"+id).siblings();
            $.each(others,function(i,contect){
                $(contect).find('.webim-leftbar-icon').removeClass('selected');
            });
            _changeContact(id);
        }catch (e){
            console.log(e);
        }
    }
    var _changeContact = function(id){
        $("#"+id).siblings().removeClass("selected");
        var contacts =  $(".webim-contact-wrapper >div ");
        $.each(contacts,function(i,contact){
            $(contact).addClass('hide');
            if($(contact).hasClass(id)){
                $(contact).removeClass('hide');
            }
        });
    };

    return {
        bandEvent:_bindEvent
    }
})();