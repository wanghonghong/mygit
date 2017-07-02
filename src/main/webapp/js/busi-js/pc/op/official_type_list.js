/**
 * Created by Administrator on 2017/2/7.
 */
CommonUtils.regNamespace("official","list");

official.list=(function(){

    var pageparam=[{
        url: CONTEXT_PATH+"/image_text_type/list/type_id/",//上架商品
        pageSize: 10,
        curPage: 0,
        countObj: "count",
        pageToolbarObj: "pageTypeToolbar",
        tableBodyObj: "m_type_list",
        template:"op/new/imageTextTypeList"
    }]
    var opt={
        baseAjaxUrl:CONTEXT_PATH+'/image_text_type',
        typeId:1
    }
    var _init=function (data) {
        opt.typeId=data.typeId;
        //初始化方法
        _initPagination(pageparam[0]);
        _bindEvent();
    }
    var _initPagination = function(pageparam){
        var data = {};
        data.pageSize=pageparam.pageSize;
        data.curPage=pageparam.curPage;
        var jsonStr = JSON.stringify(data);
        jumi.pagination("#"+pageparam.pageToolbarObj,pageparam.url+opt.typeId,data,function (res,curPage) {
            if(res.code===0){
                //判断是否第一页
                var showFormat = res.data.showFormat;
                $("input[name=image_showFormat][value='"+showFormat+"']").prop("checked",true);
                var data = {
                    items:res.data.imageTextTypes.items
                };
                jumi.template('op/new/imageTextTypeItem',data,function(tpl){
                    $('#m_type_list').html(tpl);
                })
            }
        })
    }
    var _bindEvent=function () {
        $('input[name="image_showFormat"]').click(function(){
            var formatDiv = $(this);
            var value = formatDiv.val();
            var url = opt.baseAjaxUrl+'/saveShowFormat/'+value+'/type_id/'+opt.typeId;
            $.ajaxJsonPut(url,'',{
                "done":function (res) {
                    if(res.code===0){
                        var dm = new dialogMessage({
                            type:1,
                            fixed:true,
                            msg:'设置成功',
                            isAutoDisplay:true,
                            time:3000
                        });
                        dm.render();
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
        });
        $("#op_add_type").click(function () {
            var data={ };
            data.typeId=opt.typeId;
            openWindow(data);
        })


    }
    var _del=function (id) {

        var args={};
        if(id){
            var checkUrl = opt.baseAjaxUrl+'/checkIsDel/'+id;
            var url = opt.baseAjaxUrl+'/'+id;
            $.ajaxJsonGet(checkUrl,{
                "done":function (res) {
                    jumi.dialogSure(res.data.msg,args);
                }
            });
            args.fn1 = function(){
                $.ajaxJsonDel(url,{
                    "done":function (res) {
                        var dm = new dialogMessage({
                            type:1,
                            fixed:true,
                            msg:'删除成功',
                            isAutoDisplay:true,
                            time:3000
                        });
                        dm.render();
                        _initPagination(pageparam[0]);
                    },
                    "fail":function(){
                        _initPagination(pageparam[0]);
                    }
                });
            };
            args.fn2 = function(){

            }

        }
    }

    var _edit=function(id){

        var url=opt.baseAjaxUrl+"/"+id;
        $.ajaxJsonGet(url,{
            "done":function (res) {
                openWindow(res.data);
            }
        });
    }

    var  openWindow=function(data){
        data.typeId=opt.typeId;
        var html = jumi.templateHtml('/tpl/op/new/typeItem.html',data);

        var titleStr="修改分类";
        var tag='update';
        if($.isEmptyObject(data)){
            titleStr="添加分类";
            tag='add';
        }
        data.type=opt.typeId;
        dialog({
            content: html,
            title: titleStr,
            okValue: '确定',
            ok: function() {
                var isEdit = $('input[name="isEdit"]').val();
                var form = $('#typeForm');
                if(!form.valid()){
                    return false;
                }else{
                    _dosave();
                }

            },
            onshow:function(){
                var isEdit = $('input[name="isEdit"]').val();
                if(isEdit){
                    _validateset();
                }else{
                    _validate();
                }
            },
            cancelValue:'取消',
            cancel:function () {
            }
        }).width(500).height(315).showModal();


        $("#m_op_img").click(function () {

            var _this=$(this);
            var d = new Dialog({
                context_path:CONTEXT_PATH, //请求路径,  必填
                resType:1 ,//图片1，视频2，语音3   必填
                callback:function(url){
                    if(url){
                        $(_this).attr("src",url);
                        $("#typeForm [name='imageUrl']").val(url);
                    }
                }
            });
            d.render();

        })


    }
    var _validate= function(){
        $('#typeForm').validate({
            rules: {
                typeName:{
                    required: true,
                    byteRangeLength:[0,10]
                }
            },
            messages:{
                typeName:{
                    required: "请输入图文分类名称",
                    byteRangeLength:'长度必须介于{0}-{1}之间的字符串'
                }
            }
        })
    }
    var _validateset = function(){
        $('#typeForm').validate({
            rules: {
                typeName:{
                    required: true,
                    byteRangeLength:[0,6]
                }
            },
            messages:{
                typeName:{
                    required: "请输入图文分类名称",
                    byteRangeLength:'长度必须介于{0}-{1}之间的字符串'
                }
            }
        })
    }
    var _dosave=function () {
        var data={};
        $(" #typeForm input:text,#typeForm input:hidden,#typeForm input[name='sort'], #typeForm textarea[name='shareText']").each(function(){
            data[$(this).prop("name")]=$(this).val();
        })

        var jsonData = JSON.stringify(data);
        if(data.id=="0"){
            $.ajaxJson(opt.baseAjaxUrl,jsonData,{
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

                }
            });
        }else{
            $.ajaxJsonPut(opt.baseAjaxUrl,jsonData,{
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

                }
            });

        }

    }



    return{
        init:_init,
        del:_del,
        edit:_edit

    }

})();