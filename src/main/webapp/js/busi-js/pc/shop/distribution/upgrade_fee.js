CommonUtils.regNamespace("sales", "upgrade");

sales.upgrade=(function(){
    var feeType=1; //收费类型：1分销升级收费，2代理收费
    var opt={
        containerId:"upgradeBox",
        Project_Path:CONTEXT_PATH,
        getinfoAjax: CONTEXT_PATH+'/brokerage/config/'+feeType,
        addAjax: CONTEXT_PATH+'/brokerage/config/add',
        updateAjax: CONTEXT_PATH+'/brokerage/config/update',
        tplPath:'tpl/shop/distribution/brokerage/',
        brokerageConfig:{}
    }
    var _init=function () {
        _loadData().then(function () {
            _loadHtml();
        }).then(function () {
            bindEvent();
        })
    }
    var _loadData=function(){
        var dfdPlay = $.Deferred();
        var url=opt.getinfoAjax;

        $.ajaxJsonGet(url,null,{
           "done":function (res) {
               if(res.code==0){
                   opt.brokerageConfig=res.data;
               }
               dfdPlay.resolve();
            }
        });
        return dfdPlay;
    }

    var alertinfo = function (msg) {
        var dm = new dialogMessage({
            type:3,
            msg:msg,
            isAutoDisplay:true,
            time:1500
        });
        dm.render();
    }
    var _loadHtml=function () {
        opt.brokerageConfig.feeType=opt.brokerageConfig.feeType||feeType;
        opt.brokerageConfig.share=opt.brokerageConfig.share||'快来升级啊';
        console.log(opt.brokerageConfig);
        _loadViewWindow();
        _initEditWindow();
    }
    var _loadViewWindow=function () {
        var moneylist=_getMoneyList();
        var data={
            feeType:opt.brokerageConfig.feeType,
            imgUrl:opt.brokerageConfig.imgUrl||jumi.config.cssPath+'/css/pc/img/hyzx/hyzx-banner.png',
            instruction:opt.brokerageConfig.instruction||'升级说明描述',
            moneylist:moneylist
        }


        var html = jumi.templateHtml(opt.tplPath+'viewWindow.html',data);
        $(".viewWindow","#"+opt.containerId).html(html);
    }
    var  _getMoneyList=function () {
        var moneylist=[];
        if($("#settingArea [name='afee']")){
            moneylist.push($("#settingArea [name='afee']").val()||0);
        }
        if($("#settingArea [name='bfee']")){
            moneylist.push($("#settingArea [name='bfee']").val()||0);
        }
        if($("#settingArea [name='cfee']")){
            moneylist.push($("#settingArea [name='cfee']").val()||0);
        }
        return moneylist;
    }

    var _initEditWindow=function () {
        var html = jumi.templateHtml(opt.tplPath+'editWindow.html',opt.brokerageConfig);
        $(".editWindow","#"+opt.containerId).append(html);

    }
    var _save=function(){
        var brokerageConfig= opt.brokerageConfig;
        if(brokerageConfig.id){
            var url=opt.updateAjax;
            brokerageConfig=JSON.stringify(brokerageConfig);
            $.ajaxJsonPut(url,brokerageConfig,{
                "done":function (res) {
                    if(res.code==0){
                        alertinfo("设置成功");
                    }
                }
            })
        }else {
            var url=opt.addAjax;
           delete  brokerageConfig.id;
            brokerageConfig=JSON.stringify(brokerageConfig);
            $.ajaxJson(url,brokerageConfig,{
                "done":function (res) {
                    if(res.code==0){
                        opt.brokerageConfig=res.data;
                        alertinfo("设置成功");
                    }
                }
            })

        }

    }
    var bindEvent=function(){
        $(" .savebtn","#"+opt.containerId).click(function(){
            _save();
        })
        $(".imgUrl","#"+opt.containerId).click(function(){
            var d = new Dialog({
                context_path: opt.Project_Path,
                resType: 1,
                callback: function (url) {
                    if (!$.isEmptyObject(url)) {
                        var img= $(".imgUrl","#"+opt.containerId).find("img");
                        img.attr("src",url);
                        opt.brokerageConfig.imgUrl=url;
                        _loadViewWindow();
                    }
                }
            });
            d.render();
        });
        $(" .share","#"+opt.containerId).change(function(){
            var share=$.trim($(this).val())
            opt.brokerageConfig.share=share;
        });
        var ue =null;
        var richtextId= "instruction" +new Date().getTime();
        $(".editWindow","#"+opt.containerId).find(".richtextContainer").attr("id",richtextId);
        ue = UE.getEditor(richtextId, {
            enableAutoSave: false,
            initialFrameHeight: 500,
            imageScaleEnabled: false
        });
        ue.ready(function () {
            //设置编辑器的内容
            if (opt.brokerageConfig.instruction != null && opt.brokerageConfig.instruction != undefined) {
                ue.setContent(opt.brokerageConfig.instruction);
            }
        });
        ue.addListener('contentchange', function () {
            opt.brokerageConfig.instruction = ue.getContent();
            _loadViewWindow();
        })

    }
    return {
      init:_init,
      loadView: _loadViewWindow
    };
})();