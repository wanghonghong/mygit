var ActivityGoodsDialog = function(options, callback) {
    this.default={
        pageSize:10,
        curPage: 0,
        width:800,
        height:500,
        activityType:2,
        context_path:"",
        isSingle:false
    }
    this.options = $.extend({}, this.default, options);
    this.callback = this.options.callback || callback;
    this.data=null;
}
ActivityGoodsDialog.prototype = {
    render: function() {
        if (typeof this.callback !== 'function'  || typeof this.options.context_path === 'undefined' ) {
            throw new Error('传入参数有误');
        } else {
            this._showDialog();
        }
        //		this._onbindEvent();
    },
    ajaxConfig: {
        RenderMap: '/product/trad/trad_products'
    },
    _renderHtml:function(){
        this.$table=$("<div>",{
            "class":"jm-table goods-table clearfix",
        }).appendTo("#dialog_goods_div");
        this.$tablehander=$("<ul>",{
            "class":"table-hander",
        }).appendTo(this.$table);
        $("<li>",{
            "text":"商品图片",
        }).css({
            "padding":" 10px 0"
        }).appendTo(this.$tablehander);
        $("<li>",{
            "text":"商品名称",
        }).css({
            "padding":" 10px 0"
        }).appendTo(this.$tablehander);
        $("<li>",{
            "text":"价格",
        }).css({
            "padding":" 10px 0"
        }).appendTo(this.$tablehander);
        $("<li>",{
            "text":"活动价格",
        }).css({
            "padding":" 10px 0"
        }).appendTo(this.$tablehander);
        $("<li>",{
            "text":"选择",
        }).css({
            "padding":" 10px 0"
        }).appendTo(this.$tablehander);

        this.$tableBody=$("<div>",{
            "class":"table-body"
        }).height(450).appendTo(this.$table);
        this.$pageToolbarObj=$("<div>",{
            "class":"page-toolbar text-right margin-xs"
        }).appendTo(this.$table);

    },
    _loadData:function(sign){
        var self = this;
        var  url=this.options.context_path + this.ajaxConfig.RenderMap;
        var productQo={};
        productQo.type=this.options.activityType;
        productQo.pageSize=this.options.pageSize;
        productQo.curPage=this.options.curPage;
        var jsonStr = JSON.stringify(productQo);
        $.ajaxJson(url,jsonStr,{
            "done":function(res){
                var data=res.data;
                self._renderData(data,sign);
            }
        });
    },
    _renderData:function(data,sign){
        var self = this;
        var items=data.items;
        self.$tableBody.empty();
        $.each(items, function(i) {
            var tmpul=$("<ul>",{
                "class":"table-container",
            }).data(items[i]).appendTo(self.$tableBody);
            var imgli=$("<li>").css({"line-height":"124px"}).appendTo(tmpul);
            $("<img>",{
                "src":items[i].picSquare||"./css/img/pc/no_picture.png"
            }).css({
                "max-width":"70%"
            }).appendTo(imgli);
            $("<li>",{
                "text":items[i].name
            }).appendTo(tmpul);
            $("<li>",{
                "text":parseFloat(items[i].productPrice/100)
            }).appendTo(tmpul);
            $("<li>",{
                "text":parseFloat(items[i].price/100)
            }).appendTo(tmpul);
            var pidli=$("<li>" ).appendTo(tmpul);
            if(self.options.isSingle){
                var radioBox= $("<div>",{
                    "class":"radioBox"
                }).appendTo(pidli);
                $("<input>",{
                    "id":"pid"+items[i].pid,
                    "type":"radio",
                    "name":"chooseGoods",
                    "value":items[i].pid
                }).appendTo(radioBox);
                $("<label>",{
                    "for":"pid"+items[i].pid,
                    "click":function(){
                        self.data=$(this).data();
                    }
                }).data(items[i]).appendTo(radioBox);
                self.$tableBody.find("ul:first-child .radioBox label").click();
            }else{
                var checkBox= $("<div>",{
                    "class":"checkBox"
                }).appendTo(pidli);
                $("<input>",{
                    "id":"pid"+items[i].pid,
                    "type":"checkbox",
                    "name":"chooseGoods",
                    "value":items[i].pid
                }).appendTo(checkBox);
                $("<label>",{
                    "class":"iconfont icon-avoid",
                    "for":"pid"+items[i].pid,
                    "click":function(){
                        $(this).toggleClass("checked");
                    }
                }).data(items[i]).appendTo(checkBox);
            }
        });
        if(sign){
            var param={};
            param.pageSize=this.options.pageSize;
            param.curPage= this.options.curPage;
            param.count= data.count;
            this._initPagination(param);
        }
    },
    _initPagination:function(param){
        var self = this;
        $("<div>",{
            "class":"pagination"
        }).pagination(param.count, {
            num_edge_entries: 1,
            num_display_entries: 3,
            current_page:param.curpage,
            prev_text:"<li class='iconfont icon-left  '></li>",
            next_text:"<li class='iconfont icon-right  '></li>",
            callback: function(page_index, jq){
                self.options.curPage=page_index;
                self._loadData();
                return false;
            },
            items_per_page:param.pageSize
        }).appendTo( self.$pageToolbarObj);
    },
    _showDialog: function() {
        var width = this.options.width;
        var height = this.options.height;
        var self = this;
        this.DialogBox = dialog({
            content:"<div  id='dialog_goods_div'></div>",
            title: '商品列表',
            id: 'dialog_goods',
            okValue: '选择',
            ok: function() {
                if(self.options.isSingle){
                    self.callback(self.data);
                }else{
                    var goodslist=[];
                    $(".checkBox .checked",self.$tableBody).each(function(i){
                        goodslist.push($(this).data());
                    })
                    self.callback(goodslist);
                }
            },
        }).width(width).height(height).showModal();
        this.DialogBox.onclose = function () {
            self._dispose();
        };
        this._renderHtml();
        this._loadData(true);
    },
    _offbindEvent: function() {

    },
    _dispose: function() {
        if (typeof this.DialogBox !== 'undefined') {
            this.DialogBox = null;
        }
        this._offbindEvent();
    }

};