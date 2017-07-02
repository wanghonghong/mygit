CommonUtils.regNamespace("customer", "user");


var userId = 0;
var userRoleId = 0;
customer.user = (function(){

    var _authDialog = function(id,roleid,userroleid){
        userId = id;
        userRoleId = userroleid;
        if(roleid!=null) {
            $(":radio[name='sexradio'][value='" + roleid + "']").prop("checked", "checked");
        }else{
            $("input:radio[name='sexradio']").attr("checked",false);
        }
        var elem = document.getElementById('dialoginfo');
        dialog({
            title: "员工授权",
            content: elem,
            id:'userDialog'
        }).width(400).showModal();
    };

    var ajaxUrl = {
        url1:CONTEXT_PATH+'/customer'
    };
    var _init = function(){
        //查询角色列表
        $.ajaxJson(CONTEXT_PATH+'/customer/find_role',"",{
            done:function(res){
                var roles=res.data;
                var count =roles.length;
                var li="";
                for (var i=0;i<count;i++){
                    li+='<li> '+
                        '<div class="u-rb"> '+
                        '<input onclick="sel(this);" type="radio" data-type="'+roles[i].type+'" name="sexradio" id="radioBox1'+roles[i].roleId+'" value="'+roles[i].roleId+'" /> '+
                        '<label  for="radioBox1'+roles[i].roleId+'"></label> '+
                        '</div> '+
                        '<label onclick="sel(this);"  for="radioBox1'+roles[i].roleId+'">'+roles[i].roleName+'</label> '+
                        '</li> ';
                }
                $("#roleUl").append(li);

            }
        });
        _query();

    };

    var _query = function(){
        var phoneNum = $("#phoneNumber").val();
        var url = ajaxUrl.url1+'/findUsers/0';
        var params = {
            pageSize:10,
            phoneNumber:phoneNum
        };
        jumi.pagination('#pageToolbar',url,params,function(res,curPage){
            if(res.code===0){
                //判断是否第一页
                var data = {
                    items:res.data.items
                };
                if(curPage===0){
                    data.isFirstPage = 1;
                }else{
                    data.isFirstPage = 0;
                }

                $("#userCount").text(res.data.count);
                jumi.template('new_customer/user_list',data,function(tpl){
                    $('#table-body').empty();
                    $('#table-body').html(tpl);
                });
            }
        });


    };


    var _queryUser = function(){
        var phoneNum = $("#phoneNumber").val();
        var mydata = '{"phoneNumber":"'+phoneNum+'"}';
        $.ajaxJson(CONTEXT_PATH+"/customer/queryuser",mydata,{
            done:function(res){
                if(res.data.code==0){
                    var headImg = res.data.data.headImg;
                    $("#searchDiv").show();
                    var li =
                        '<li>'+
                        '<img class="u-thumbnail-circ" src="'+headImg+'"/>'+
                        '</li>'+
                        '<li>'+res.data.data.userName+'</li>'+
                        '<li>'+res.data.data.phoneNumber+'</li>'+
                        '<li>'+
                        '<div class="u-btn-smltgry f-mb-xs dialog" onclick="customer.user.authDialog('+res.data.data.userId+','+res.data.data.roleId+',0)"> 授权 </div>'+
                        '</li>';
                    $("#userUl").empty();
                    $("#userUl").append(li)
                }else{
                    var dm = new dialogMessage({
                        type:2,
                        title:'操作提醒',
                        fixed:true,
                        msg:res.data.msg,
                        isAutoDisplay:false
                    });
                    dm.render();
                }
            }
        });

    };

    var _del = function(id){
        var args = {};
        args.fn1 = function(){
            $.ajaxJson(CONTEXT_PATH+'/customer/delroleuser/'+id,"",{
                done:function(res){
                    if(res.data.code==0){
                        var dm = new dialogMessage({
                            type:1,
                            title:'操作提醒',
                            fixed:true,
                            msg:'删除成功',
                            isAutoDisplay:false
                        });
                        dm.render();
                        $("#phoneNumber").val("");
                        _query();
                    }
                }
            });
        };
        args.fn2 = function(){

        };
        jumi.dialogSure('确定删除该用户角色吗?',args);

    };

    var _save = function(){
        var roleid = $("input[name='sexradio']:checked").val();
        var type = $("input[name='sexradio']:checked").attr("data-type");
        var mydata = '{"roleId":"' + roleid + '","userId":"' + userId + '","userRoleId":"' + userRoleId + '","type":"'+ type + '"}';
        $.ajaxJson(CONTEXT_PATH+'/customer/update_user_role',mydata, {
            done: function (res) {
                if(res.data.code==0){
                    var dm = new dialogMessage({
                        type:1,
                        title:'操作提醒',
                        fixed:true,
                        msg:'授权成功',
                        isAutoDisplay:false
                    });
                    dm.render();
                }else{
                    var dm = new dialogMessage({
                        type:2,
                        title:'操作提醒',
                        fixed:true,
                        msg:res.data.msg,
                        isAutoDisplay:false
                    });
                    dm.render();
                }

                dialog.get('userDialog').close().remove();
                $("#phoneNumber").val("");
                _query();

            }}
        );
    };


    return {
        init :_init,
        query:_query,
        queryUser:_queryUser,
        del:_del,
        save:_save,
        authDialog:_authDialog
    };
})();

function sel(lable){
    $(this).find("lable");
};