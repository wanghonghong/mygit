CommonUtils.regNamespace("repository","hintWin");
repository.hintWin = (function(){
    var _filter = {};

    var hintWin;

    var HintWin = function (count) {
        this.model = "z-hide";
        this.count = count;
    };
    HintWin.prototype.init = function () {
        var html = '<div id="hint" v-on:click="openWin" class="m-replymsg {{model}} "   v-bind:class="{hide:isHide,notreplymsg:offLine}" ><message-hint :count="msgCount"></message-hint> </div>';
        $('#webim_chat').after(html);
        Vue.component('message-hint', {
            props: ['count', 'model'],
            template: '<span><b>{{count}}</b>条</span>'
        });
        this.hint = new Vue({
            el: '#hint',
            data: {
                msgCount: this.count || 0,
                isHide: false,
                offLine: false,
                model: this.model
            },
            methods: {
                openWin: function () {
                    webim.openWin();
                }
            }
        });
        var body = $('body')[0];
        window.onhashchange = function () {
            var hash = window.location.hash;
            if (hintWin && _filter[hash]) {
                //console.log('hintWin:',_filter[hash]);
                _getHintWin().setting({
                    model: _filter[hash]
                });
            } else {
                _getHintWin().setting({
                    model: "z-hide"
                });
            }
        }
    };
    HintWin.prototype.hideWin = function () {
        //$('#hint').addClass('hide');
        this.hint.isHide = true;
    };
    HintWin.prototype.showWin = function () {
        //$('#hint').removeClass('hide');
        this.hint.isHide = false;
    };
    HintWin.prototype.clear = function () {
        this.count = 0;
        this.hint.msgCount = 0;
        historyHelper.cleanBuffer();
    };
    HintWin.prototype.plusMsg = function () {
        this.count++;
        this.hint.msgCount = this.count;
    };
    HintWin.prototype.offLine = function () {
        this.hint.offLine = true;
    };
    HintWin.prototype.onLine = function () {
        this.hint.offLine = false;
    };
    HintWin.prototype.setting = function (options) {
        this.hint.model = options.model || "z-hide";
        var hash = window.location.hash;
        _filter[hash] = this.hint.model;
    };
    var _getHintWin = function () {
        if(hintWin){
            return hintWin;
        }else{
            hintWin = new HintWin();
            hintWin.init();
            hintWin.setting({
                model:"default"
            });
            return hintWin;
        }
    };

    return {
        getHintWin:_getHintWin
    }

})();