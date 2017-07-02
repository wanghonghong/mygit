CommonUtils.regNamespace("mall", "memberCenter");

mall.memberCenter=(function(){
    var memberData = {};
    var memberCollectionCustom = {};
    var memberCollection = {};
    var isFlag = 0;//判断全部订单是否显示
    var config = {};
    var opt={
        Project_Path:CONTEXT_PATH,
        userCenterAjax :CONTEXT_PATH+"/shopsetting/user_center",
        userCenterFunAjax :CONTEXT_PATH+"/shopsetting/user_center_fun",//旧的接口
        saveSetMemberAjax:CONTEXT_PATH+"/shopsetting/saveUserCenterCustom",
        getuserCenterAjaxNew:CONTEXT_PATH+"/shopsetting/getUserCenterFuns",
        userCenterFunItem:"/shop/mall/userCenterFunItem",
        userCenterFunBtn:"/shop/mall/userCenterFunBtn",
        funlist:[]
    }
    var _get = function () {
        return{
            memberCollectionCustom:memberCollectionCustom,
            memberCollection:memberCollection,
            config:config,
            isFlag:isFlag
        }
    }
    var usercenter={};

    var _init=function () {
        _newLoadUserCenter().then(function(){
            //兼容旧数据的判断
            if(!memberData.config.funItems){
                _loadhtml(memberData);
            }else{
                _loadUserCenterFun();
            }
        }).then(function () {
            _valid();
           return _loadUserCenter();
        }).then(function () {
            _setUserCenter();
        }).then(function(){
            _bandEvent()

        })
    }
    //修改集合的别名 传入name和funsId
    var _modifyCustomCollection = function (name,funsId) {
        var args = {};
        args.fn1 = function(){
            _.each(memberData.memberCollectionCustom,function (k,v) {
                if(k.funsId == funsId){
                    k.name = name;
                }
            })
            _loadhtml(memberData);
            _saveUserCenter(memberData);
            $('#set_member_name').hide();
        };
        args.fn2 = function(){

        };
        jumi.dialogSure('是否修改别名',args);
    }
    //验证
    var _valid = function () {
        $("#form_member").validate({
            rules: {
                member_name:{
                    required: true,
                    byteRangeLength:[0,15]
                }
            },
            messages:{
                member_name:{
                    required:'必须输入别名',
                    byteRangeLength:'别名的长度必须介于{0}-{1}之间的字符串'
                }
            }
        })
    }
    //会员中心上架增加 传入增加的集合对象
    var _addCustomCollection = function (collection) {
        var custom = {};
        custom.funsId = collection.funId;
        custom.name = collection.funName;
        custom.itemIcon = collection.itemIcon;
        custom.group = collection.group;
        custom.funName = collection.funName;
        custom.id = collection.id||'';
        memberData.memberCollectionCustom.push(custom);
        //让全部订单排到第一位
        for(var k=0;k<memberData.memberCollectionCustom.length;k++){
            var t;
            if(memberData.memberCollectionCustom[k].funsId==1){
                t = memberData.memberCollectionCustom[k];
                memberData.memberCollectionCustom[k]=memberData.memberCollectionCustom[0];
                memberData.memberCollectionCustom[0] = t;
            }
        }

        _.each(memberData.memberCollection,function (k,v) {
            if(k.funId == collection.funId){
                k.isShow = 1;
            }
        })
        _loadhtml(memberData);

    }
    //会员中心上架删除
    var _removeCustomCollection = function (collection) {
        var custom = {};
        custom.funId = collection.funsId;
        custom.name = collection.funName;
        custom.itemIcon = collection.itemIcon;
        custom.group = collection.group;
        custom.funName = collection.funName;
        custom.id = collection.id||'';
        //如果移除的是全部订单
        if(collection.funsId===1){
            memberData.isFlag = 0;
        }
        memberData.memberCollectionCustom = _.filter(memberData.memberCollectionCustom, function(item) {
            return item.funsId !== collection.funsId
        });
        _.each(memberData.memberCollection,function (k,v) {
            if(k.funId == collection.funsId){
                k.isShow = 0;
                k.id = collection.id||'';
            }
        })
        _loadhtml(memberData);

    }
    //旧的接口
    var _loadUserCenterFun=function(){
        var dfdPlay = $.Deferred();
        var url=opt.userCenterFunAjax;
        $.ajaxJsonGet(url,null,{
            "done":function (res) {
                if(res.code==0){
                    memberData.memberCollectionCustom = [];
                    memberData.memberCollection = [];
                    opt.funlist=res.data;
                    //把旧数据分拆成新的数据集合
                    _.each(res.data,function (k,v) {
                        var custom = {};
                        var fans = {};
                        if(k.status===1){
                            custom.id = k.id;
                            custom.funsId = k.funId;
                            custom.name = k.funName;
                            custom.funName = k.funName;
                            custom.itemIcon = k.itemIcon;
                            custom.group = k.group;
                            fans.funId = k.funId;
                            fans.funName = k.funName;
                            fans.itemIcon = k.itemIcon;
                            fans.group = k.group;
                            fans.isShow = 1;
                            memberData.memberCollectionCustom.push(custom);
                        }else{
                            fans.funId = k.funId;
                            fans.funName = k.funName;
                            fans.itemIcon = k.itemIcon;
                            fans.group = k.group;
                            fans.isShow = 0;
                        }
                        memberData.memberCollection.push(fans);
                    })
                }
                _loadhtml(memberData);
                dfdPlay.resolve();
            }
        })
        return dfdPlay;
    }
    //拖动方法
    var _sortEvent = function () {
        $("ul[id^=fungroupId]").each(function () {
            $(this).sortable({
                cursor: "move",
                items: "li:not('.order')",
                axis: "y",
                scrollSpeed: 0,
                placeholder: "ui-state-highlight",
                stop:function (event,ui) {
                    _sortDataBuild();
                }
            })
            $(this).disableSelection();
        })
    }
    //新的接口
    var _newLoadUserCenter = function () {
        var dfdPlay = $.Deferred();
        $.ajaxJsonGet(opt.getuserCenterAjaxNew,null,{
            "done":function (res) {
                if(res.code===0){
                    memberData = _get();
                    opt.funlist=res.data;
                    memberData.memberCollection  = res.data.funs;
                    memberData.memberCollectionCustom = res.data.customs;
                    memberData.config = res.data.config;
                }
                dfdPlay.resolve();
            }
        })

        return dfdPlay;
    }



    var _loadhtml=function(memberData){
        //注释:优化代码 modify by pray,减少请求次数
        $('#set_member_name').hide();
        if(!$.isEmptyObject(opt.funlist)){
            var CollectionCustom = _.sortBy(memberData.memberCollectionCustom, function(item) {
                return item.group;
            });
            _.each(CollectionCustom,function (k,v) {
                if(k.funsId==1){
                    memberData.isFlag = 1;
                }
            })

            _.extend(memberData.memberCollectionCustom,CollectionCustom);
            var groups = _.pluck(memberData.memberCollectionCustom,'group');
            var types = _.uniq(groups);
            var array = [];
            _.map(types,function(a,b){
                var str = [];
                str[b] = _.where(memberData.memberCollectionCustom,{group:a})
                array.push(str[b]);
            })
            var data = {
                items:array
            }
            jumi.template(opt.userCenterFunItem,data,function (tpl) {
                $('#editregion').html(tpl);
                if(memberData.isFlag==1){
                    $('#hyzx-dd').show();
                }else{
                    $('#hyzx-dd').hide();
                }
                _sortEvent();
            })
        }
        var funsList = memberData.memberCollection;
        var data = {
            items:funsList
        }
        jumi.template(opt.userCenterFunBtn,data,function (tpl) {
            $("#mall_bottom").html(tpl);
        })
    }
    var _loadUserCenter=function () {
        var dfdPlay = $.Deferred();
        var url=opt.userCenterAjax;
        $.ajaxJsonGet(url,null, {
            "done":function(res) {
                if(res.code==0){
                    usercenter=res.data;
                }
                dfdPlay.resolve();
            }
        })
        return dfdPlay;
    }

    //增加可用的会员集合
    var _addCollection = function (dom) {
        var dom = $(dom);
        var jsonCustom = dom.data('item');
        _addCustomCollection(jsonCustom);
    }

    //删除可用得会员集合
    var _removeCollection = function (dom,event) {
        event.stopPropagation();
        var dom = $(dom).parent();
        var jsonCustom = dom.data('item');
        _removeCustomCollection(jsonCustom);
    }
    var _setUserCenter=function(){
        var config = memberData.config;
        $("#topName").val(config.topName||'尊敬的');
        $("#topNameSpan").html(config.topName);
        if(config.topPic){
            $("#topPic").attr("src",config.topPic);
            $("#topPic2").attr("src",config.topPic);
        }
    }

    var _setMemberName = function (dom) {
        var liHeight = $(dom).outerHeight();
        var item = $(dom).data('item');
        var funname = item.funName;
        $('#member_name').text(funname);
        $('input[name="member_name"]').val(item.name);
        $('#member_btn').data('funsId',item.funsId);
        var funindex = $(dom).data('funindex');
        var groupindex = $(dom).parent().data('groupindex');
        var marginTop = _getPosition(liHeight,groupindex,funindex);
        $('#set_member').hide();
        $('#set_member_name').show();
        $('#set_member_panel').css('marginTop',marginTop);
    }
    var _sortDataBuild = function () {
        memberData.memberCollectionCustom = [];
        $("ul[id^=fungroupId] li").each(function () {
            var custom = {};
            var item = $(this).data('item');
            custom.funName = item.funName;
            custom.name = item.name;
            custom.itemIcon = item.itemIcon;
            custom.funsId = item.funsId;
            custom.group = item.group;
            custom.id = item.id;
            memberData.memberCollectionCustom.push(custom);
        })
        _loadhtml(memberData);
    }
    var _getPosition = function (x,y,z) {
        var ulfungroupId = y+1,height=0,d=0;
        var ddheight = $('#hyzx-dd').height();
        for(var i=1;i<ulfungroupId;i++){
            height = height+parseInt($('#fungroupId'+i).height(),10);
        }
        if(memberData.isFlag==1){
            d = 190;
        }else{
            d = 190-ddheight;
        }
        height=height+parseInt(y*20,10)+parseInt(x*z,10)+d;
        return height;
    }
    var _bandEvent=function(){
        $('#member_btn').click(function () {
            var form = $('#form_member');
            if(form.valid()){
                var name = $('input[name="member_name"]').val();
                var funsId= $('#member_btn').data('funsId');
                _modifyCustomCollection(name,funsId);
            }

        })





        $('#setMember').click(function () {
            $('body').scrollTop(0);
            $('#set_member').show();
            $('#set_member_name').hide();
        })
        $("#topName").change(function(){
           var topName=  $(this).val();
            memberData.config.topName=topName;
            $("#topNameSpan").html(topName);
        })
        $("#topPic").click(function(){
            var d = new Dialog({
                context_path: opt.Project_Path,
                resType: 1,
                callback: function (url) {
                    if(!$.isEmptyObject(url)){
                        url=  jumi.picParse(url,0);
                        memberData.config.topPic=url;
                        $("#topPic").attr("src",memberData.config.topPic);
                        $("#topPic2").attr("src",memberData.config.topPic);
                    }
                }
            });
            d.render();
        });
        $("#saveUserCenter").click(function(){
            var args = {};
            args.fn1 = function () {
                _saveUserCenter();
            }
            args.fn2 = function () {
                
            }
            jumi.dialogSure('是否保存会员设置',args);

        });
    }
    var _saveUserCenter=function(){
        var co = {};
        co.customs = [];
        _.each(memberData.memberCollectionCustom,function (k,v) {
            var custom = {};
            custom.id = k.id;
            custom.funsId = k.funsId;
            custom.name = k.name;
            co.customs.push(custom);
        })

        var url=opt.saveSetMemberAjax;
        co.config = memberData.config;
        co.config.funItems='';
        if(!$.isEmptyObject(co.config.topPic)){
            co.config.topPic=jumi.picParse(co.config.topPic,0);
        }

        var jsonData = JSON.stringify(co);
        if(co.config.id){
            $.ajaxJson(url,jsonData,{
                "done":function (res) {
                    if(res.code==0){
                        var dm = new dialogMessage({
                            type:3,
                            msg:"保存成功",
                            isAutoDisplay:true,
                            time:1500
                        });
                        dm.render();
                    }else{
                        var dm = new dialogMessage({
                            type:3,
                            msg:"保存失败",
                            isAutoDisplay:true,
                            time:1500
                        });
                        dm.render();
                    }
                }
            })
        }else{
            var jsonData = JSON.stringify(co);
            $.ajaxJson(url,jsonData,{
                "done":function (res) {
                    if(res.code==0){
                        var dm = new dialogMessage({
                            type:3,
                            msg:"保存成功",
                            isAutoDisplay:true,
                            time:1500
                        });
                        dm.render();
                    }else{
                        var dm = new dialogMessage({
                            type:3,
                            msg:"保存失败",
                            isAutoDisplay:true,
                            time:1500
                        });
                        dm.render();
                    }
                }
            })
        }

    }
    return {
         init:_init,
         get:_get,
         setMemberName:_setMemberName,
         addCollection:_addCollection,
         removeCollection:_removeCollection,
         modifyCustomCollection:_modifyCustomCollection
    }
})();

