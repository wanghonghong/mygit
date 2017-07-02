CommonUtils.regNamespace('waitCustomer');
waitCustomer = (function(){

    var waitRosterUrl = "/online/customer/waitCustomer";

    var Customer = function (customer) {
        this.customer = customer;
    };

    Customer.prototype.init = function () {
        var that = this;
        var customer = {
            openid:this.customer.openid,
            appid:this.customer.appid,
            message:this.customer.messages,
            headImgUrl:this.customer.headImgUrl,
            nickname:this.customer.nickname
        };
        jumi.template(waitRosterUrl,customer,function(tpl) {
            var roster = $("#leftBar").find(".waitRoster");
            $(roster).append(tpl);
            var li = $(roster).find("li[target='"+customer.openid+"']");
            bindEvent();
            $(li).find("div").bind('click',that.receive);
        });
    };



    Customer.prototype.float = function(){
        var roster = $("#leftBar").find(".waitRoster");
        var li = $(roster).find("li[target='"+this.customer.openid+"']");
        var childs = $(roster).children();
        if(childs.length>1){
            console.log(childs);
            var dataId = $(childs[0]).attr("target");
            if(dataId!==this.customer.openid){
                $(roster).prepend(li);
                return ;
            }
        }
    };
    Customer.prototype.hintSound = function () {

    };

    Customer.prototype.changeMsg = function (msg) {
        var roster = $("#leftBar").find(".waitRoster");
        var li = $(roster).find("li[target='"+this.customer.openid+"']");
        $(li).find("span").html(msg);
    };

    Customer.prototype.receive = function () {
        var openid = this.customer.openid;
        var roster = $("#leftBar").find(".waitRoster");
        var li = $(roster).find("li[target='"+openid+"']");
        var myRoster = $("#leftBar").find(".myRoster");
        $(myRoster).prepend($(li));
        //this.remove();
        var url = CONTEXT_PATH+"/online/server/"+openid;
        $.ajaxJson(url,{
            done:function(res){

            }
        });
    };

    Customer.prototype.remove = function () {
        var roster = $("#leftBar").find(".waitRoster");
        var li = $(roster).find("li[target='"+this.customer.openid+"']");

    };

    var bindEvent = function () {
        $('#waitList').click(function () {
            $(this).addClass('z-sel');
            $('#myCustomerList').removeClass('z-sel');
            $('.myCustomerBar').css('display','none');
            $('.waitBar').css('display','');
        });
    };



    return {
        Customer:Customer
    }
})();