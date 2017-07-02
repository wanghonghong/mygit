CommonUtils.regNamespace("service");
service = (function(){
    // 用户数据 查询地址
    var url = CONTEXT_PATH+'/online/wxUserList';
    // 用户等级更新地址
    var levelUrl = CONTEXT_PATH+'/wx/level/move_user';
    // 用户备注更新地址
    var remarkUpdateUrl = CONTEXT_PATH+'/wx/user_remark';
    //用户分组更新地址
    var groupUpdateUrl= CONTEXT_PATH+'/wx/group/move';
    var saveMsgUrl =  CONTEXT_PATH+'/online/lastMsg';
    var updateHxAccountUrl = CONTEXT_PATH+'/online/HX/hxAccount';



    var that = this;// 对象本身
    // 获取Date 对象的 时分秒
    var _time =function(d){
        var date = new Date(d);
        return date.getHours()+':'+date.getMinutes()+':'+date.getSeconds();
    };
    // 获取 Date 对象的 年月日
    var _date = function(d){
        var date = new Date(d);
        return date.getFullYear()+"-"+date.getMonth()+"-"+date.getDate();
    };
    var _timeFomat = function(id,model,dateStr){
        try{
            if(dateStr&&dateStr!==''){
                switch(model){
                    case "time":
                        var date = new Date(dateStr).Format('hh:mm:ss');
                        $("#"+id).html(date);
                        break;
                    case "date":
                        var date = new Date(dateStr).Format('yyyy-MM-dd');
                        $("#"+id).html(date);
                        break;
                    default:
                        if(!dateStr||dateStr===''){
                            $("#"+id).html("0000-00-00");
                        }
                }
            }else{
                if(model==="date"){
                    $("#"+id).html("0000-00-00");
                }else if(model==="time"){
                    $("#"+id).html("00:00:00");
                }
            }

        }catch (e){
            $("#"+id).html("时间格式错误");
        }
    };
    // 显示关闭 dialog
    var _showCloseDialog = function (btn){
        var parent = $(btn).parent();
        var id = $(parent).attr('id');
        if(id){
            $("#table-body").find('div[source="dialog"]').hide();
            $(parent).next().show();
        }else{
            for(var i =0;i<5;i++){
                parent = $(parent).parent();
                var source = $(parent).attr("source");
                if(source){
                    $(parent).hide();
                }else{
                    continue;
                }
            }
        }
    };

    //查询对象
    var queryData = {
        nikename:'',//昵称
        name:'',//姓名
        phoneNum:'',//手机
        starSubscribeTime:'',//关注开始时间
        endSubscribeTime:'',//关注结束时间
        starBuyTime:'',//开始购买时间
        endBuyTime:'',//结束购买时间
        pageSize:10,
        curPage:0
    };
    // 更新对象
    // 用户等级修改
    var _updateUserLevel = function(uid,obj){
        var top = $(obj).parent().parent();
        var input = $(top).find('input[name="level"]:checked');
        var text = $(input).parent().next().html();
        var data ={
            id:$(input).val(),
            wxUserId:uid
        };
       _updater(levelUrl,data,function(res){
            if(res.data.code==="0"||res.data.code===0){
                $("#levelBtn"+uid).empty();
                var txt = '<a class="u-btn-smltgry f-mb-xs">'+text+'</a>';
                $("#levelBtn"+uid).append(txt);
                var a = $("#levelBtn"+uid).find('a');
                $(a).bind('click',function(){

                    _showCloseDialog(this);
                });
            }else{
                var dm = new dialogMessage({
                    type:2,
                    fixed:true,
                    msg:'操作失败',
                    isAutoDisplay:true,
                    time:1500
                });
                dm.render();
            }
           _showCloseDialog(obj);
        });
    };
    // 用户分组
    var _groupUpdate = function(uid,obj){
        var top = $(obj).parent().parent();
        var input = $(top).find('input[name="groupid"]:checked');
        var text =  $(input).parent().parent().find('input[type="text"]').val();
        var groupid = $(input).val();
        if(groupid==='new'){
            var url_path = CONTEXT_PATH+'/wx/group';
            var data= {
                name:text
            }
            $.ajaxJson(url_path,data,{
                done:function(res){
                    _query();
                }
            })
        }else{
            var data = {
                groupid:groupid,
                userId:uid
            }
            _updater(groupUpdateUrl,data,function(res){
                console.log(res);
                if(res.data.errcode==="0"||res.data.errcode===0){
                    $("#groupBtn"+uid).empty();
                    var txt = '<a class="u-btn-smltgry f-mb-xs">'+text+'</a>';
                    $("#groupBtn"+uid).append(txt);
                    var a = $("#groupBtn"+uid).find('a');
                    $(a).bind('click',function(){
                        _showCloseDialog(this);
                    })
                }else{
                    var dm = new dialogMessage({
                        type:2,
                        fixed:true,
                        msg:'操作失败',
                        isAutoDisplay:true,
                        time:1500
                    });
                    dm.render();
                }

            });
        }
        _showCloseDialog(obj);
    }



    // 用户备注
    var _remarkUpdate = function(uid,btn){
        var input = $(btn).parent().parent().find("input[type='text']");
        var remark = $(input).val().trim();
        if(!remark||remark===''){
            _showCloseDialog(btn);
            return;
        }
        //console.log('字符长度：',remark.replace(/[^\u0000-\u00ff]/g,"aa").length);
        if(remark.replace(/[^\u0000-\u00ff]/g,"aa").length>22){
            var dm = new dialogMessage({
                type:2,
                fixed:true,
                msg:'备注名不能超过22个字符',
                isAutoDisplay:true,
                time:1500
            });
            dm.render();
            return ;
        }
        var data = {
            remark:remark,
            userId:uid
        };
        _updater(remarkUpdateUrl,data,function(res){
            if(res.data.errcode==="0"||res.data.errcode===0){
                var txt = '<a>'+remark+'</a>';
                var remarker = $("#remarkBtn"+uid);
                $(remarker).empty();
                $(remarker).append(txt);
                $(remarker).find('a').bind('click',function(){
                    _showCloseDialog(this);
                });
            }else{
                var dm = new dialogMessage({
                    type:2,
                    fixed:true,
                    msg:'操作失败',
                    isAutoDisplay:true,
                    time:1500
                });
                dm.render();
            }
            _showCloseDialog(btn);
        });
    };
    // 通用更新方法
    var _updater = function(url,params,callback){
        $.ajaxHtmlPut(url,params,{
            done:function(res){
                if(typeof(callback)==='function'){
                    callback(res);
                }
            }
        });
    };
    //查询用户

    //初始化
    var _init = function(){

        $("#queryBtn").click(_query);
        var dateConfig = {
            closeText:'确定',
            showSecond : true,
            timeFormat : 'hh:mm:ss',
            dateFormat : 'yy-mm-dd',
            stepHour : 1,
            stepMinute : 1,
            stepSecond : 1
        };
        $("#starSubscribeTime").datepicker(dateConfig);
        $("#endSubscribeTime").datepicker(dateConfig);
        $("#starBuyTime").datepicker(dateConfig);
        $("#endBuyTime").datepicker(dateConfig);
        $("#group").select2({
            theme: "jumi"
        });
        $("#service").select2({
            theme: "jumi"
        });
        _query();
    };
    // 返回对象

    // 页面数据填充
    var _pageData = function(items){
        $.each(items,function(index,item){
            var html = "";
            html+="<ul class='table-container table-wxhf' >";
            html+='<li><div class="table">';
            html+='<div class="table-cell vertical-middle" style="width:0%;display: none;">';
            html+='<div class="checkBox">';
            html+='<input type="checkbox" name="" id="checkbox1" value="" />';
            html+='<label class="iconfont icon-avoid"  for="checkbox1"></label>';
            html+='</div></div>';
            html+='<div class="table-cell vertical-middle">'
            html+='<a href="#" class="thumbnail">';
            html+='<img src="'+item.headimgurl+'" alt="暂无图片" class="avt">';
            html+='</a></div></div></li>';
            html+='<li style="width: 15%;overflow: hidden;word-wrap: break-word; word-break: normal;"><span>';
            html+=  item.nickname?item.nickname:'';
            html+='</span><br />';
            var username = '';
            if(item.user_name){
                username = item.user_name;
            }

            html+='<span>'+username+'</span><br />';
            var phone = '';
            if(item.phone_number){
                phone = item.phone_number;
            }
            html+='<span>'+phone+'</span>';
            html+='</li>';
            var isSubscribe;
            if(item.is_subscribe===0||item.is_subscribe==="0"){
                isSubscribe = '未关注客户';
            }else if(item.is_subscribe===1||item.is_subscribe==="1"){
                isSubscribe = '关注客户';
            }
            html+='<li>'+isSubscribe+'</li>';
            html+='<li><span>'+_date(item.subscribe_time)+'</span><br><span>'+_time(item.subscribe_time)+'</span></li>';

            html+='<li><span>2016-01-08</span><br><span>12:10:38</span></li>';
            html+='<li><span>2016-01-08</span><br><span>12:10:38</span></li>';
            //微操作
            //1.等级
            html+='<li class="btn-xlk" style="width: 15%;">';
            html+='<div ><span class="btn  btn-lightgray btn-sm" onclick="service.showDialog(this)" target="level"> 等级</span></div>';
            html+='<div source="dialog" class="btn-wcz hideD">';
            html+='<span class="out"></span><span class="iner"></span>';
            html+='<div class="star">';
            html+='<ul><li ><div class="radioBox u-rb">';
            html+='<input type="radio" name="star" id="radioBoxG'+index+'" value="" checked="">';
            html+='<label for="radioBoxG'+index+'"></label>';
            html+='</div>';
            html+='<label  for="radioBoxG'+index+'" class="f-mr-l">金牌</label></li>';
            html+='<li><div class="radioBox u-rb">';
            html+='<input type="radio" name="star" id="radioBoxS'+index+'" value="" checked="">';
            html+='<label for="radioBoxS'+index+'"></label>';
            html+='</div>';
            html+='<label for="radioBoxS'+index+'" class="f-mr-l">银牌</label></li>';
            html+='<li><div class="radioBox u-rb">';
            html+='<input type="radio" name="star" id="radioBoxC'+index+'" value="" checked="">';
            html+='<label for="radioBoxC'+index+'"></label>';
            html+='</div>';
            html+='<label for="radioBoxC'+index+'" class="f-mr-l">铜牌</label></li>';
            html+='</ul>';
            html+='<div class="btn  btn-lightorange btn-sm"> 保存</div>';
            html+='<div class="btn  btn-lightgray btn-sm " onclick="service.showDialog(this)"> 取消 </div>';
            html+='</div></div>';

            //2.分组
            html+='<div><span class="btn  btn-lightgray btn-sm" onclick="service.showDialog(this)"> 分组 </span></div>';
            html+='<div source="dialog" class="btn-fenzu hideD">';
            html+='<span class="out"></span>';
            html+='<span class="iner"></span>';
            html+='<div class="star">';
            html+='<ul>';
            html+='<li class="active">';
            html+='<div class="radioBox u-rb">';
            html+='<input type="radio" name="group" id="black'+index+'" value="" checked="">';
            html+='<label for="black'+index+'"></label></div>';
            html+='<label for="black" class="f-mr-l">加入黑名单</label>';
            html+='</li>';
            html+='<li> <div class="radioBox u-rb">';
            html+='<input type="radio" name="group" id="groupA'+index+'" value="" checked="">';
            html+='<label for="groupA'+index+'"></label>';
            html+='</div>';
            html+='<label for="radioBox2" class="f-mr-l">A组</label></li>';
            html+='</li>';
            html+='<li><div class="radioBox u-rb">';
            html+='<input type="radio" name="group" id="radioB'+index+'" value="" checked="">';
            html+='<label for="radioB'+index+'"></label>';
            html+='</div>';
            html+='<label for="radioB'+index+'" class="f-mr-l">B组</label>';
            html+='</li>';
            html+='</ul>';
            html+='<div class="btn  btn-lightorange btn-sm"> 保存</div>';
            html+='<div class="btn  btn-lightgray btn-sm " onclick="service.showDialog(this)"> 取消 </div>';
            html+='</div></div>';
            //3.备注
            html+='<div><span class="btn  btn-lightgray btn-sm " onclick="service.showDialog(this)"> 备注 </span></div>';
            html+='<div source="dialog" class="btn-bz hideD" >';
            html+='<span class="out"></span>';
            html+='<span class="iner"></span>';
            html+='<div class="form-group">';
            html+='<input type="text" name="remark" class="form-control">';
            html+='</div>';
            html+='<div class="btn  btn-lightorange btn-sm" onclick="service.remarkUpdate()"> 保存</div>';
            html+='<div class="btn  btn-lightgray btn-sm " onclick="service.showDialog(this)"> 取消 </div>';
            html+='</div></li>';
            // 回复 li
            html+='<li><img src="'+CONTEXT_PATH+'/css/pc/img/jmtt.png" height="46" width="53"></li>';
            html+='</ul>';
            html+='<div class="table-wxly">';
            html+='<img src="'+CONTEXT_PATH+'/css/pc/img/jmtts.png"/>';
            html+='<span>聚米留言</span>';
            html+='<span>身高163cm，l号的会不会太大？</span>';
            html+='</div>';
            $("#table-body").append(html);
            //var controller = $(html).find(".btn-xlk");
            //_bandEvent(controller);
        });
    };

    var _query = function(){

        var nickname = $("#nickname").val();
        //console.log(nickname);
        if(nickname&&nickname!==""){
            queryData.nikename = nickname;
        }else{
            queryData.nikename = '';
        }
        var starSubscribeTime = $("#starSubscribeTime").val();
        if(starSubscribeTime&&starSubscribeTime!==""){
            queryData.starSubscribeTime = starSubscribeTime;
        }else{
            queryData.starSubscribeTime = '';
        }
        var endSubscribeTime = $("#endSubscribeTime").val();
        if(endSubscribeTime&&endSubscribeTime!==""){
            queryData.endSubscribeTime = endSubscribeTime ;
        }else{
            queryData.endSubscribeTime = '';
        }
        var phoneNum = $("#phoneNum").val();
        if(phoneNum&&phoneNum!==""){
            queryData.phoneNum = phoneNum;
        }else{
            queryData.phoneNum = '';
        }
        var userName = $("#userName").val();
        if(userName&&userName!==""){
            queryData.name = userName;
        }else{
            queryData.name = '';
        }
        queryData.curPage = 0;
        //console.log(queryData);
        try{
            jumi.pagination('#Pagination',url,queryData,function(res,curPage){
                //$(".user-sum").html("用户总数："+res.data.count);
                console.log(res);
                if(res.code===0){
                    //判断是否第一页
                    var data = {
                        items:res.data.items,
                        basePath:CONTEXT_PATH,
                        staticPath:STATIC_URL,
                        ext:res.data.ext
                    };
                    if(curPage===0){
                        data.isFirstPage = 1;
                    }else{
                        data.isFirstPage = 0;
                    }
                    $("table-body").empty();
                    console.log(data);
                    jumi.template('online/service/service_reply_list',data,function(tpl){
                        queryData.curPage = curPage;
                        $('#table-body').empty();
                        $('#table-body').html(tpl);
                        $.each($('.icon-modified'),function(i,modifyBtn){
                             $(modifyBtn).click(function(){
                                 var input = $(this).parent().prev();
                                 $(input).removeAttr("disabled");
                                 $(input)[0].focus();
                                 $(input).blur(function(){
                                     $(this).attr("disabled","disabled");
                                     var groupName = $(this).val();
                                     var groupid = $(this).prev().find('input[type="radio"]').val();
                                     var url = CONTEXT_PATH+"/wx/group";
                                     var group = {
                                        name:groupName,
                                        groupid:groupid
                                     }
                                     $.ajaxJsonPut(url,group,function(res){
                                         console.log(res);
                                     })
                                 });
                             });
                         });
                        $.each($('.icon-delete'),function(i,removeBtn){
                            $(removeBtn).click(function(){
                                var input = $(this).parent().parent().find('input[type="radio"]');

                                var groupid = $(input).val();
                                var del_url = CONTEXT_PATH+'/wx/group/'+groupid;
                                $.ajaxJsonDel(del_url,{
                                    done:function(){
                                        _query();
                                    }
                                });
                            });
                        });
                    });
                }
            });
        }catch(e){
            console.log(e);
        }
    };

    var _labelInput = function(btn){
        var input = $(btn);
        input.find('input[name="groupid"]').prop('checked',true);
    }




    var returnObj = {
        init:_init,
        query:_query,
        timeFormat:_timeFomat,
        showDialog:_showCloseDialog,
        remarkUpdate:_remarkUpdate,
        saveLevel:_updateUserLevel,
        saveGroup:_groupUpdate,
        labelInput:_labelInput
    };

    return returnObj;
})();
//**************************分页 js********************************************
