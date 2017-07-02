CommonUtils.regNamespace("repository","roster");
repository.roster = (function(){

    var roster;
    var waitQueueUrl = CONTEXT_PATH+"/online/wait/";
    var templateUrl = "/online/customer/waitRoster";

    var _customers = {};
    var _myCust = {};

    var Roster = function(app){
        this.data = app.getData();
        this.setting = app.getSetting();

    };
    Roster.prototype.init = function(){
        var def = $.Deferred();
        var that = this;
        $.ajax({
            url:waitQueueUrl,
            data:"shopId="+that.data.shopId,
            type:"GET",
            success:function(customers){
               def.resolve(customers);
            }
        });
        return def;
    };

    Roster.prototype.createLeftBar = function(customers){
        var def = $.Deferred();
        var waitCount = {count:customers.length};
        jumi.template(templateUrl,waitCount,function(tpl){
            $('#leftBar').append(tpl);
            _bindEvent();
            def.resolve(customers);
        });
        return def;
    };

    Roster.prototype.createRosters = function(customers){
        for(var i in customers){
            var cust = customers[i];
            var flag = false;
            for(var j in _customers){
                if(_customers[j]===cust.openid){
                    flag = true;
                }
            }
            if(flag){
                continue;
            }
            var customer = new waitCustomer.Customer(cust);
            customer.init();
            _customers[cust.openid] = customer;
        }
    };



    var _bindEvent = function () {

        $('#myCustomerList').click(function () {
            $(this).addClass('z-sel');
            $('#waitList').removeClass('z-sel');
            $('.myCustomerBar').css('display','');
            $('.waitBar').css('display','none');
        });
    };


    Roster.prototype.getWaitRoster = function(){

    };

    Roster.prototype.addToMyRoster = function(openid){
        var cust = _customers[openid];
        _myCust[openid] = cust;
    };

    Roster.prototype.removeWaitUser = function(openid){
        var cust = _customers[openid];
        cust.remove();
    };

    Roster.prototype.addMyRoster = function(message){

    };
    Roster.prototype.addWaitRoster = function(message){
        console.log(message);
        var openid = message.ext.customer.openid;
        if(_customers[openid]){
            var customer = _customers[openid];
            customer.changeMsg(message.data);
            customer.float();
        }else{
            var customer = message.ext.customer;
            var cust = new waitCustomer.Customer(customer);
            cust.init();
            cust.float();
            _customers[openid] = cust;
        }
    };

    Roster.prototype.removeMyUser = function(openid){

    };

    Roster.prototype.clearMyUser = function(){

    };


    var _getRoster = function(app){
        if(roster){
            return roster;
        }else{
            roster = new Roster(app);
            return roster;
        }
    };

    return {
        getRoster:_getRoster
    }

})();