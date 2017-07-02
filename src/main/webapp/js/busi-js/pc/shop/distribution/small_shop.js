/**
 * 我的小店
 * @class smallshop
 * @author zww
 * @date 2016/7/1
 */
CommonUtils.regNamespace("distribution","smallshop");
distribution.smallshop = (function(){
    var ajaxUrl = {
        url1:CONTEXT_PATH+'/brokerage/small_shop',//我的小店获取
        url2:CONTEXT_PATH+'/brokerage/small_shop/add',//我的小店初创建
        url3:CONTEXT_PATH+'/brokerage/small_shop/update',//我的小店修改
    };
    //初始化数据
    var _init = function(){
        _bind();
        _getdata(0);

    };
    var _bind = function () {
        _tabclick();
        _imgchoose();
    };
    //点击tab切换
    var _tabclick = function(){
        var tabli = $("#xd-tab ul li");
        tabli.click(function(){
            var index=$(this).index();
            $(this).addClass("z-sel").siblings().removeClass("z-sel");
            _getdata(index);
        })
    };
    //请求数据
    var _getdata = function(index){
        if(index==0){
            var url = ajaxUrl.url1;
        }else{
            _tplload(index,'0');
            return;
        }
        $.ajaxJsonGet(url,null,{
            done:function (res) {
                if(res.code==0){
                    _tplload(index,res);
                    _validateset();
                }
            }
        })
    };
    //加载模板和数据
    var _tplload = function (index,res){
        var data=res;
        if(index==0){
            jumi.template('shop/distribution/small_shop/xd_manager',data,function(tpl){
                $('#smallshop-tpl').html(tpl);
            })
            _dataload(data);
        }else{
            jumi.template('shop/distribution/small_shop/xd_add',function(tpl){
                $('#smallshop-tpl').html(tpl);
            })
        }
    };
    //小店管理数据回填
    var  _dataload = function (data){
        var bgstyle=data.data.backStyle;
        var preStyle=data.data.preStyle;
        var mode=data.data.mode;
        if(bgstyle==0||bgstyle==1){
            $("#bgstyle #radioBox1").prop('checked',true);
            $("#bgstyle #presettpl").show();
        }else{
            $("#bgstyle #radioBox2").prop('checked',true);
            $("#bgstyle #diytpl").show();
        };
        if(mode==0||mode==1){
            $("#modeset #radioBox3").prop('checked',true);
            $("#money").prop("readonly",true);
            $("#m-tip1").show();
        }else{
            $("#modeset #radioBox4").prop('checked',true);
            $("#m-tip2").show();
        };
        //无论是否是选中预置模板，勾选的与之模板初始化的时候都会去匹配。
        if(preStyle==1||preStyle==0){
            $("#bgstyle #preset1").addClass("active");
            $("#bgstyle #preset1 input").prop('checked',true);
        }
        else if(preStyle==2){
            $("#bgstyle #preset2").addClass("active");
            $("#bgstyle #preset2 input").prop('checked',true);
        }else{
            $("#bgstyle #preset3").addClass("active");
            $("#bgstyle #preset3 input").prop('checked',true);
        };
    };
    //收费模式输入框的禁用与可输入及提示语显隐
    var _import = function(sign){
        if(sign==1){
            $("#money").prop("readonly",true);
            $("#m-tip1").slideDown(300);
            $("#m-tip2").slideUp(300);
            $("#money").attr("min",0).val(0);
        }else{
            $("#money").prop("readonly",false);
            $("#money").attr("min",1);
            $("#m-tip2").slideDown(300);
            $("#m-tip1").slideUp(300);
        }
    };
    //点击显示隐藏
    var _show = function(sign){
        switch (sign){
            case 1 :
                $("#bgstyle #radioBox1").prop('checked',true);
                $("#bgstyle #presettpl").slideDown(500);
                $("#bgstyle #diytpl").slideUp(500);
                break;
            case 2 :
                $("#bgstyle #radioBox2").prop('checked',true);
                $("#bgstyle #presettpl").slideUp(500);
                $("#bgstyle #diytpl").slideDown(500);
                break;
            default:
        }
    };
    //预置模板勾选
    var _imgchoose = function (){
         $("#smallshop-tpl ").on("click","#presettpl div",function(){
             $(this).addClass("active").siblings().removeClass("active");
             $(this).find("input[type=checkbox]").prop('checked',true);
             $(this).siblings().find("input[type=checkbox]").prop('checked',false);
         })
    };
    //自定义图片选择
    var _diyimgchoose = function(){
        var d = new Dialog({
            context_path:CONTEXT_PATH, //请求路径,  必填
            resType:1 ,//图片1，视频2，语音3  头像身份证5 必填
            multifile:false,//多选文件
            width:'',//自定义弹出框宽度
            height:'',//自定义弹出框高度,
            compress:0,//压缩比例
            callback:function(url){
                $("#diytpl img").attr("src",url);
            }
        });
        d.render();
    };
    //from表单 整数验证
    var _validateset = function(){
        $('#xdcharge').validate({
            rules: {
                xdchargename:{
                    required: true,
                    digits:true
                }
            },
            messages:{
                xdchargename:{
                    required: "请输入收费",
                    min:'收费必须大于1',
                    digits:'收费必须是整数'
                }
            }
        })
    };
    //创建或修改小店数据
    var _setdata = function (){
         var form = $('#xdcharge');
         var id = $("#smallshopid").val();
         //对象不需要和接口给的一致  只要是对象 且传值正确就ok。
         // var SmallShopCo={};
         // var SmallShopUo={};
         var data={};
         var backstyle = $("#bgstyle input[type=radio]:checked").attr("backstyle");//背景风格1：预置 2：自定义
         var prestyle =  $("#bgstyle input[type=checkbox]:checked").attr("prestyle");//预置背景风格图类型三选一
         var backurl =   $("#diytpl img").attr("src");//自定义背景风格图片src地址
         var mode = $("#modeset input[type=radio]:checked").attr("mode");//功能类型： 1：免费 2：收费
         var money = $("#money").val(); //收费类型所设置金额
         data.backStyle = backstyle;
         data.preStyle = prestyle;
         data.backUrl = backurl;
         data.mode = mode;
         data.money = money*100;
         var jsonStr=JSON.stringify(data);
         if(!form.valid()){
                return;
            }
         if(!id){
             var url = ajaxUrl.url2;
             $.ajaxJson(url,jsonStr,{
                 done:function (res) {
                     if(res.code==0){
                         var dm = new dialogMessage({
                             type:1,
                             fixed:true,
                             msg:'操作成功',
                             isAutoDisplay:true,
                             time:1500
                         });
                         dm.render();
                         _getdata(0);
                     }
                 }
             })
         }else{
            var url = ajaxUrl.url3;
             data.id = id;
             jsonStr = JSON.stringify(data);
             $.ajaxJsonPut(url,jsonStr,{
                 done:function (res) {
                     if(res.code==0){
                         var dm = new dialogMessage({
                             type:1,
                             fixed:true,
                             msg:'操作成功',
                             isAutoDisplay:true,
                             time:1500
                         });
                         dm.render();
                         _getdata(0);
                     }
                 }
             })
         }
    };
    //添加
    var _add = function(){
        
    };
    //删除
    var _del = function(){

    };
    //修改
    var _update = function(){

    };
    //查询
    var _query = function(){

    };
    //获取
    var _get = function(){

    };
    return {
        init :_init, //初始化
        add:_add,  //添加
        del:_del, //删除
        update:_update,
        query:_query,
        get:_get,
        show:_show,
        diyimgchoose:_diyimgchoose,
        setdata:_setdata,
        import:_import
    };
})();
