CommonUtils.regNamespace("product", "groups");

product.groups=(function () {
    var defaults = {
        Project_Path:CONTEXT_PATH,
        goodsGroupsAjax:CONTEXT_PATH+"/product/group/",
        tplPath:STATIC_URL+'/tpl/product/group/',
        groupListJson:[],
        groupRelationList:[],
        containerId:"goodsGroups",
        tableBody:"groupBody",
        window:null,
        windowId:0,
        ischeck:false
    }
    var  _getid= function(){
        var id =  new Date().getTime();
        return id;
    }
    var _init=function (data) {
        defaults.ischeck=data.ischeck;
        defaults.groupRelationList=data.groupRelationList;
        defaults.groupListJson=data.groupListJson;
        if(!$.isEmptyObject( defaults.groupListJson)){
            _loadHtml();
            _bindEvent();
        }else{
            _loadData().then(function () {
                _bindEvent();
            });
        }


    };
    var _loadData=function () {
        var dfdPlay = $.Deferred();
        var url=defaults.goodsGroupsAjax;
        $.ajaxJsonGet(url,null,{
           "done":function (res) {
               if(res.code==0){
                   defaults.groupListJson=res.data;
                   _loadHtml();
               }
               dfdPlay.resolve(); // 动画完成
           }
        });
        return dfdPlay;
    };
    var _openWindow=function (data) {
        // nunjucks.configure(defaults.tplPath);//配置模版路径
        // var html = nunjucks.render('group-item.html',data);
        var html = jumi.templateHtml('group-item.html',data,defaults.tplPath);
        var titleStr="修改分类";
        var tag='update';
        if($.isEmptyObject(data)){
            titleStr="添加分类";
            tag='add';
        }
        defaults.id=_getid();
        defaults.window=dialog({
            content: html,
            title: titleStr,
            okValue: '确定',
            id:defaults.id,
            ok: function() {
                var form = _validate();
                if (form.valid()) {
                    if(!$("#groupform input[name='groupImagePath']").val()){
                        var dm = new dialogMessage({
                            type:2,
                            fixed:true,
                            msg:'请选择分类图片！',
                            isAutoDisplay:true,
                            time:3000
                        });
                        dm.render();
                        return false;
                    }
                    if(tag=='add'){
                        _add();
                    }else {
                        _update(data.groupId);
                    }
                }else {
                    return false;
                }
            },
            cancelValue:'取消',
            cancel:function () {
            }
        }).width(500).height(315).showModal();
        _addImgEvent("#group_img");
    }

    var _validate=function(){
        var form=$("#groupform");
        form.validate({
            rules: {
                groupName: {
                    required: true,
                    maxlength:6
                },
                groupSort: {
                    required: true,
                    number: true,
                    min:1
                },
                groupSlogan:{
                    required: true,
                    maxlength:30
                }
            },
            messages: {
                groupName: {
                    required: '请输入类型名称',
                    maxlength:'类型名称不能超过6个字符'
                },
                groupSort: {
                    required: '请输入数字',
                    number: '请输入数字',
                    min:'输入的数字必须大等于1'
                },
                groupSlogan:{
                    required: '请输入分享语',
                    maxlength:'分享语不能超过30个字符'
                }

            }
        });
        return form;
    }


    var _add=function(){
        var groupVo={}
        $("#groupform").find("input,textarea").each(function () {
            groupVo[$(this).prop("name")]=$(this).val();
        })
        var url=defaults.goodsGroupsAjax;
        $.ajaxJson(url,groupVo,{
            "done":function(res){
                if(res.code==0){
                    _loadData();
                }else{
                    var dm = new dialogMessage({
                        type:2,
                        fixed:true,
                        msg:'添加分类失败！',
                        isAutoDisplay:true,
                        time:3000
                    });
                    dm.render();
                }
            }
        });
    }
    var _update=function(groupId){
        var groupVo={}
        $("#groupform").find("input,textarea").each(function () {
            groupVo[$(this).prop("name")]=$(this).val();
        })
        var url=defaults.goodsGroupsAjax+groupId;
        $.ajaxJsonPut(url,groupVo,{
            "done":function(res){
                if(res.code==0){
                    _loadData();
                }else{
                    var dm = new dialogMessage({
                        type:2,
                        fixed:true,
                        msg:'添加分类失败！',
                        isAutoDisplay:true,
                        time:3000
                    });
                    dm.render();
                }
            }
        });

    }
    var _del=function(groupId){
        var args = {};
        args.fn1 = function(){
            var url=defaults.goodsGroupsAjax;
            url=url+groupId;
            $.ajaxJsonDel(url,null,{
                "done":function (res) {
                    if(res.code==0){
                        if(res.data.code==0){
                            var dm = new dialogMessage({
                                type:2,
                                fixed:true,
                                msg:'删除分组成功',
                                isAutoDisplay:true,
                                time:3000
                            });
                            dm.render();
                            _loadData();
                        }else{
                            var dm = new dialogMessage({
                                type:2,
                                fixed:true,
                                msg:res.data.msg,
                                isAutoDisplay:true,
                                time:3000
                            });
                            dm.render();
                        }
                    }
                }
            })
        };
        args.fn2 = function(){};
        jumi.dialogSure('确定删除该商品分类吗?',args);
    }
    var _addImgEvent=function (id) {
        $(id).click(function(){
            var d = new Dialog({
                context_path:Project_Path, //请求路径,  必填
                resType:1 ,//图片1，视频2，语音3   必填
                callback:function(url){
                    if(url){
                        $(id).find(".add").addClass("z-hide");
                        $(id).find("img").attr("src",url).removeClass("z-hide");
                        $(id).find("[name='groupImagePath']").val(url);
                    }
                }
            });
            d.render();
        })
    }
    var _bindEvent=function () {
        $("#addGroup").click(function () {
            _openWindow();
        })
        $("#"+defaults.tableBody).delegate(".modifybtn","click",function () {
            var url=defaults.goodsGroupsAjax;
            url=url+$(this).attr("groupId");
            $.ajaxJsonGet(url,null,{
                "done":function (res) {
                    if(res.code==0){
                        _openWindow(res.data);
                    }
                }
            });
        })
        $("#"+defaults.tableBody).delegate(".deletebtn","click",function () {
            var groupId=$(this).attr("groupId");
            _del(groupId);
        })
        $("#"+defaults.tableBody).delegate(".u-cb1 input","click",function () {
            var groupRelationList=[];
            $("#"+defaults.tableBody+" .u-cb1 input:checked").each(function () {
                groupRelationList.push({ "groupId":$(this).val()});
            })
            defaults.groupRelationList=groupRelationList;
        })

    }
    var _loadHtml=function () {
        // nunjucks.configure(defaults.tplPath);//配置模版路径
        // var html = nunjucks.render('group-body.html',defaults);
        var html = jumi.templateHtml('group-body.html',defaults,defaults.tplPath);
        $("#"+defaults.tableBody).children().remove();
        $("#"+defaults.tableBody).append(html);
        _selectGroup(defaults);
    }
    var _getGroupJson=function(){
        return defaults.groupListJson
    }
    var _selectGroup=function(data){
        defaults.groupRelationList=data.groupRelationList;
        $("#"+defaults.tableBody+" .u-cb1 input:checked").prop("checked", false);
        if(!$.isEmptyObject(defaults.groupRelationList)){
            $.each(defaults.groupRelationList,function(i){
                $("#"+defaults.tableBody).find("input[name=groupId]").each(function(){
                    if($(this).val()+""==defaults.groupRelationList[i].groupId){
                        $(this).prop("checked",true);
                    }
                })
            })
        }
    }
    var  _getGroupRelationList=function () {
        return defaults.groupRelationList;
    }
    var _validate1=function () {
         //处理模块验证信息
        return false;
    }
    return {
        init:_init,
        add:_add,
        update:_update,
        getGroupJson:_getGroupJson,
         del:_del,
        getGroupRelationList:_getGroupRelationList,
        selectGroup:_selectGroup,
        validate:_validate1
    }
})();