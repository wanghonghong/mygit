
CommonUtils.regNamespace("official","imglist");



official.imglist=(function(){

    var pageparam=[{
        url: CONTEXT_PATH+"/image_text/findAll/",//上架商品
        pageSize: 10,
        curPage: 0,
        countObj: "count",
        pageToolbarObj: "pageToolbar",
        tableBodyObj: "table-body",
        template:"/op/new/imageTextItem"
    }]
    var opt={
        baseAjaxUrl:CONTEXT_PATH+'/image_text',
        typeId:1
    }
    var _init=function(data){
        opt.typeId=data.typeId;
        _initPagination(pageparam[0]);
    }

    var  _initPagination=function (pageparam) {

        var data = {};
        data.pageSize=pageparam.pageSize;
        data.curPage=pageparam.curPage;
        var jsonStr = JSON.stringify(data);
        jumi.pagination("#"+pageparam.pageToolbarObj,pageparam.url+opt.typeId,data,function (res,curPage) {
            if(res.code===0){
                $("#"+pageparam.tableBodyObj).empty();
                pageparam.curPage=curPage;
                if(res.data.items && res.data.items.length>0){
                    var data = {
                        items:res.data.items
                    }

                    jumi.template(pageparam.template,data,function(tpl){
                        $("#"+pageparam.tableBodyObj).html(tpl);
                    });

                }else{
                    $("<div>",{
                        'class':'m-jm-err'
                    }).html(" <img src='"+THIRD_URL+"/css/pc/img/jm-nodb.png' >").appendTo("#"+pageparam.tableBodyObj);
                }
            }
        })


    }
     var _detailItem=function (el) {
         var tablecontainer= $(el).parent().parent();
         var tmpdata=tablecontainer.data('item');
         _newVersion(tmpdata);
     }



     var _newVersion=function(data){


         var data={
             id:data.id,
             typeId:opt.typeId,
             isEdit:data.isEdit,
             mainContainer:"image-text-details"
         }
         official.config.init(data)
     };






    var _del=function (el) {
        var tablecontainer= $(el).parent().parent();
        var tmpdata=tablecontainer.data('item');
        var delId = tmpdata.id;
        var args = {};
        var ids = [];
        ids.push(delId);
        args.fn1 = function(){
            var url = opt.baseAjaxUrl+'/'+ids.join(',');
            $.ajaxJsonDel(url,{
                "done":function (res) {
                    if(res.code===0){
                        var dm = new dialogMessage({
                            type:1,
                            fixed:true,
                            msg:'删除成功',
                            isAutoDisplay:true,
                            time:3000
                        });
                        dm.render();
                    }
                }
            });
            _initPagination(pageparam[0]);
        };
        //关闭的时候初始化方法
        args.fn2 = function(){

        };
        jumi.dialogSure('确定删除图文列表吗?',args);


    }
    var _changeState=function (el) {
        var tablecontainer= $(el).parent().parent();
        var tmpdata=tablecontainer.data('item');
        var p=(tmpdata.status===0)?1:0;
         var url=opt.baseAjaxUrl+"/id/"+tmpdata.id+'/status/'+p;
         var msg=(tmpdata.status===1)?'确定上架【'+tmpdata.imageTextTile+'】的活动吗?':'确定下架【'+tmpdata.imageTextTile+'】的活动吗?';

        var args = {};
        args.fn1 = function(){
            $.ajaxJsonPut(url,null,{
                "done":function (res) {
                    if(res.code===0){
                        var dm = new dialogMessage({
                            type:1,
                            fixed:true,
                            msg:res.data.msg,
                            isAutoDisplay:true,
                            time:3000
                        });
                        dm.render();
                        _initPagination(pageparam[0]);
                    }
                },
                "fail":function (res) {
                    var dm = new dialogMessage({
                        type:2,
                        fixed:true,
                        msg:res.data.msg,
                        isAutoDisplay:true,
                        time:3000
                    });
                    dm.render();
                }
            });
        };
        args.fn2 = function(){

        };
        jumi.dialogSure(msg,args);
    }
    var _querylist=function () {
        _initPagination(pageparam[0]);
    }    
   return {
        init:_init,
       detailItem:_detailItem,
        del:_del,
       changeState:_changeState,
       querylist:_querylist
   }
})();