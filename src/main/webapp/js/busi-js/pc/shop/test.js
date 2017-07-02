var splitscreen = function(options){
    this.options = options || {};
}
splitscreen.prototype = {
    initialize:function(){
        this.data = this.options.config.data || {};
        this.screenNum = this.options.config.screenNum||1;
        this.columnNum = this.options.config.columnNum||3;
        this.isEmbediframe = this.options.config.isEmbediframe||false;
        this.srcs = this.options.config.srcs||[];
        this.EnableZoom =this.options.config.EnableZoom||false;
        this.el = this.options.el||'body';
        this.property = {};
    },
    render:function(){
        this.initialize();
        this._calculate();
        this._resize();
    },
    _calculate:function(){
        this._calculate_split();
    },
    _resize:function(){
        var self = this;
        $(window).resize(function() {
            self.dispose(); //移除组件
            self._calculate();
        })
    },
    _calculate_split:function(){
        var screenW = this._viewScreen().screenW;
        var screenH = this._viewScreen().screenH;
        var screenNum = this.screenNum; //小屏有几个
        var columnNum = this.columnNum; //一行几个
        var arithmetic = screenW / columnNum; //每个div的宽度
        var rows, divHeight,remainder;
        if (screenNum % columnNum === 0) {
            rows = screenNum / columnNum;
        } else {
            rows = parseInt(screenNum / columnNum, 10) + 1;
        }
        divHeight = screenH / rows;
        remainder = screenNum % columnNum;
        this.rows = rows;
        this.divHeight = divHeight;
        this.remainder = remainder;
        this.arithmetic = arithmetic;
        this.columnNum = columnNum;
        this._calculate_splitScreen();
    },
    _calculate_splitScreen:function(){
        var tpl = '',t,l,hg,w;
        var d = 0;
        var h = 0;
        var sn = this.screenNum;
        var cn = this.columnNum;
        for (var k = 0; k < sn; k++) {
            if (k % cn === 0 && k !== 0) {
                d = 0;
                h++;
                t = h * (this.divHeight);
                l = d*(this.arithmetic);
                w = this.arithmetic;
                hg = this.divHeight;
                tpl += '<div id="fp-module-'+k+'" class="fp-box" style="top:' +t+'px;height: '+hg+'px;left: '+l+'px;width: '+w+'px">'+
                       '</div>';
                d++;
            } else {
                t = h * (this.divHeight);
                l = d*(this.arithmetic);
                hg = this.divHeight;
                w = this.arithmetic;
                tpl += '<div id="fp-module-'+k+'"class="fp-box" style="top:' +t+'px;height: '+hg+'px;left: '+l+'px;width:'+w+'px">'+
                       '</div>';
                d++;
            }
        }
        $(this.el).append(tpl);
        if (this.isEmbediframe) {
            this._renderIframe();
            this._renderIframeSrc();
        }
        if(this.EnableZoom){
            this._enableZoom();
        }
    },
    _renderIframe:function(){
         $('div[id^="fp-module-"]').each(function(i) {
            $(this).append('<iframe border="0" frameborder="no" width="100%" height="100%" id="iframe_' + i + '"></iframe>');
        })
    },
    _isAccordHttp:function(http){
        var Expression=/http(s)?:\/\/([\w-]+\.)+[\w-]+(\/[\w- .\/?%&=]*)?/;
        if(!Expression.test(http)){
            throw new Error('输入的http格式不正确,请重新输入');
        }else{
            return Expression.test(http);
        }
    },
    _renderIframeSrc:function(){
        var srcs = this.srcs;
        var self = this;
        $('[id^="iframe_"]').each(function(i) {
            var parent = $(this).parent();
            if (srcs[i]) {
                var src = srcs[i];
                if(self._isAccordHttp(src)){
                    $(this).attr('src', src);
                } 
            }else{
                parent.find('iframe').remove();
            }
        })
    },
    _enableZoom:function(){
        var self = this;
        var element = $(this.el);
        $('div[id^="fp-module-"]').each(function(i) {
            $(this).append('<div class="bigicon"></div><div class="smallicon"></div>');
        })
        this._onObserver();
    },
    _viewScreen:function(){
        var element = $(this.el);
        var screenWidth = element.width();
        var screenHeight = element.height();
        return {
            screenW: screenWidth,
            screenH: screenHeight
        }
    },
    _enlarge:function(e){
        var elem = $(e.target);
        var self = e.data.self;
        var parent = elem.parent();
        var w = self._viewScreen().screenW;
        var h = self._viewScreen().screenH;
        self.property.l = parent.css('left');
        self.property.t = parent.css('top');
        self.property.w = parent.width();
        self.property.h = parent.height();
        parent.css({
            'zIndex':999
        }).stop().animate({
            'top': 0,
            'left': 0,
            'width': w,
            'height':h,
            'right': 0,
            'bottom': 0
        }, 300).attr('scaleMode', 'large');
        $(self.el).find('.fp-box:not([scaleMode="large"])').hide();
        elem.hide();
        parent.find('.smallicon').show();
        $('html,body').css({
            'overflow': 'hidden'
        });
    },
    _reduce:function(e){
        var elem = $(e.target);
        var self = e.data.self;
        var parent = elem.parent();
        parent.css({
            'zIndex': 999
        }).stop().animate({
            'top': self.property.t,
            'left': self.property.l,
            'width': self.property.w,
            'height':self.property.h
        }, 300).attr('scaleMode', 'large');
        parent.removeAttr('scaleMode');
        elem.hide();
        parent.find('.bigicon').show();
        $(self.el).find('.fp-box:not([scaleMode="large"])').show();
    },
    _onObserver: function() {
        $('.bigicon').on('click',{self:this},this._enlarge);
        $('.smallicon').on('click',{self:this},this._reduce);
    },
    _offObserver: function() {
        var self = this;
        $('.bigicon').off('click',this._enlarge);
        $('.smallicon').off('click',this._reduce);
    },
    dispose:function(){
        this._offObserver();
        $(this.el).children().remove();
    }
}