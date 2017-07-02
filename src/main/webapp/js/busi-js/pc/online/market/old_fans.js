CommonUtils.regNamespace("oldFans");
oldFans = (function(){

    var url = CONTEXT_PATH+"/online/HX/oldFans";
    var userQo ={
        nikename:'',
        name:'',
        phoneNum:'',
        sex:'',
        startTime:'',
        endTime:'',
        pageSize:10,
        curPage:0
    };
    var initPage = function(){
        $("#sex").select2({
            theme: "jumi"
        });
        var dateConfig = {
            closeText:'确定',
            showSecond : true,
            timeFormat : 'hh:mm:ss',
            dateFormat : 'yy-mm-dd',
            stepHour : 1,
            stepMinute : 1,
            stepSecond : 1
        };
        $("#startTime").datepicker(dateConfig);
        $("#endTime").datepicker(dateConfig);
        _query();
        //$('.query').bind('click',_query);
    };


    var _query  = function(){
        userQo.name = $("#name").val()||'';
        userQo.nikename = $('#nickname').val()||'';
        userQo.phoneNum = $('#phoneNum').val()||'';
        userQo.sex = $('#sex').val()|| '';
        userQo.startTime = $('#startTime').val()||'';
        userQo.endTime = $('#endTime').val()||'';
        jumi.pagination('#Pagination1',url,userQo,function(res,curPage){
            if(res.code===0){
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
                $("#table-body").empty();
                jumi.template('online/market/old_fans_list',data,function(tpl){
                    $("#table-body").append(tpl);
                    $('#count').html(res.data.count);
                });
            }
        });
    };

    return{
        init:initPage,
        query:_query
    }
})();