CommonUtils.regNamespace("market", "newfans");
market.newfans = (function(){
    var count = 3;
    var ajaxUrl = {
        url1:CONTEXT_PATH+'/customer',
        url2:CONTEXT_PATH+'/wx/level',
        url3:CONTEXT_PATH+'/wx//groups',
        url4:CONTEXT_PATH+'/system/session'
    };
    var _init = function(){
        _query();
        _sended_area_template();
    };

    //地区模板start
    var _sended_area_template=function(){
        jumi.template('customer/customer_area',function(tpl){
            $("#area_title").html("地区");
            $('#customer_content').html(tpl);
            $("#area_title").text("地区");
            $("#area_title").css("min-width","90px");
            $("#area_title").css("margin-right","5px");


        })
    }
    ////地区模板end


    var _query = function(){
        //查询等级列表
        var levels;
        $.ajaxHtmlGet(ajaxUrl.url2,null,{
            done:function(res) {
                levels=res.data;
            }
        });

        //查询分组列表
        var groups;
        $.ajaxHtmlGet(ajaxUrl.url3,null,{
            done:function(res) {
                groups=res.data;
            }
        });

        //获取用户session
        var userSession;
        $.ajaxHtmlGet(ajaxUrl.url4,null,{
            done:function(res) {
                userSession=res.data;
            }
        });

        var name = $("#name").val();
        var phoneNum = $("#phoneNum").val();
        var nikename = $("#nikename").val();
        var sex=$("#select3 option:selected").val();
        var starTime = $("#startTime").val();
        var endTime = $("#endTime").val();


        var reval="";
        $("#areaselectd div[class='u-sort active'] input[name='area']").each(function(){
            var val = $(this).attr("id");
            val = val.replace("chb","");
            reval = reval + val + ",";
        });

        var url = ajaxUrl.url1+'/findNewFans';
        var params = {
            pageSize:10,
            nikename:nikename,
            name:name,
            phoneNum:phoneNum,
            sex:sex,
            starSubscribeTime:starTime,
            endSubscribeTime:endTime,
            area:reval
        };
        jumi.pagination('#pageToolbar',url,params,function(res,curPage){
            if(res.code===0){
                //判断是否第一页
                var data = {
                    items:res.data.items,
                    basePath:CONTEXT_PATH,
                    STATIC_URL:STATIC_URL,
                    levels:levels,
                    groups:groups,
                    userSession:userSession
                };
                if(curPage===0){
                    data.isFirstPage = 1;
                }else{
                    data.isFirstPage = 0;
                }

                $("#userCount").text(res.data.count);
                jumi.template('active_marketing/new_fans_list',data,function(tpl){
                    $('#table-body').empty();
                    $('#table-body').html(tpl);
                })
            }
        });


    };


    /**
     * 上级用户弹出框
     */
    var _topUserPopMsg = function(userid){
        var url = CONTEXT_PATH+'/customer/topuser/'+userid;
        $.ajaxHtmlGet(url,null,{
            done:function(res) {
                $("#dialoginfo33").empty();
                $(res.data).appendTo("#dialoginfo33");
                popMsg("上级客户");
            }
        });
    }

    /**
     * 下级用户弹出框
     */
    var _lastUserPopMsg = function(userid){
        var url = CONTEXT_PATH+'/customer/laseuser/'+userid;
        $.ajaxHtmlGet(url,null,{
            done:function(res) {
                $("#dialoginfo33").empty();
                $(res.data).appendTo("#dialoginfo33");
                popMsg("下级客户");
            }
        });
    }

    function popMsg(title){
        var elem = $('#dialoginfo33');
        memudialog=dialog({
            id:'show-dialog',
            width: 880,
            height: 400,
            title: title,
            content: elem
        });
        memudialog.show();
    };
    var _labelInput = function(btn){
        var input = $(btn);
        input.find('input:radio').prop('checked',true);
    }
    return {
        init :_init,
        query:_query,
        lastUserPopMsg:_lastUserPopMsg,
        topUserPopMsg:_topUserPopMsg,
        labelInput:_labelInput
    };
})();

