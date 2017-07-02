function actionPoster(){

}
actionPoster.prototype = {
	initialize:function(){

	},
	init:function(){
		this._onBindEvent();
	},

	_onBindEvent:function(){
		$(document).on('click','.radio-check',function(){
			var dom = $(this);
			dom.find('input[name="sexradio5"]').attr('checked',true);
			dom.find('span').addClass('group_word');
		})
		
	},
	_offBindEvent:function(){

	}
}
var ac = new actionPoster();
ac.init();