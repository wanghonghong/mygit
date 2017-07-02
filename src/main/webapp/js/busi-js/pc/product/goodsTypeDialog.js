var GoodsTypeDialog = function(options, callback) {
	this.default = {
		pageSize: 10,
		curPage: 0,
		width: 800,
		height: 550,
		context_path: "/msa",
	}
	this.options = $.extend({}, this.default, options);
	this.callback = this.options.callback || callback;
	this.data = null;
}
GoodsTypeDialog.prototype = {
	render: function() {
		if (typeof this.callback !== 'function'  || typeof this.options.context_path === 'undefined') {
			throw new Error('传入参数有误');
		} else {
			this._showDialog();
		}
	},
    ajaxConfig: {
		RenderMap: '/product/group/groups'
	},
	_showDialog: function() {
		var width = this.options.width;
		var height = this.options.height;
		var self = this;
		this.DialogBox = dialog({
			content: "<div  id='dialog_goodsType_div'></div>",
			title: '商品类型列表',
			id: 'dialog_goods_type'
		}).width(width).height(height).showModal();

		this.DialogBox.onclose = function() {
			self._dispose();
		};
		this._renderHtml();
		this._loadData(true);
	},
	_renderHtml: function() {
		this.$table = $("<div>", {
			"class": "m-jm-table clearfix",
		}).appendTo("#dialog_goodsType_div");
		this.$tablehander = $("<ul>", {
			"class": "table-hander",
		}).appendTo(this.$table);
		$("<li>", {
			"text": "类型图片",
		}).appendTo(this.$tablehander);
		$("<li>", {
			"text": "类型名称",
		}).appendTo(this.$tablehander);
		$("<li>", {
			"text": "选择",
		}).appendTo(this.$tablehander);
		this.$tableBody = $("<div>", {
			"class": "table-body"
		}).height(450).appendTo(this.$table);
		this.$pageToolbarObj = $("<div>", {
			"class": "page-toolbar text-right margin-xs"
		}).appendTo(this.$table);
	},
	_loadData: function(sign) {
		var productVo = {};
	     productVo.pageSize = this.options.pageSize;
		productVo.curPage = this.options.curPage;
		var jsonStr = JSON.stringify(productVo);
		var self = this;
		var  url=this.options.context_path + this.ajaxConfig.RenderMap;
		$.ajaxJson(url, jsonStr, {
			"done": function(res) {
				var data = res.data;
				self._renderData(data, sign);
			}
		});
	},
	_renderData: function(data, sign) {
		var self = this;
		var items = data.items;
		self.$tableBody.empty();
		$.each(items, function(i) {
			var tmpul = $("<ul>", {
				"class": "table-container",
			}).data(items[i]).appendTo(self.$tableBody);
			var imgli = $("<li>").appendTo(tmpul);
			$("<img>", {
				"src": items[i].group_image_path || "./css/img/pc/no_picture.png"
			}).addClass("u-thumbnail-lg90").appendTo(imgli);
			$("<li>", {
				"text": items[i].group_name
			}).appendTo(tmpul);
			var pidli = $("<li>").appendTo(tmpul);
			var radioBox = $("<div>", {
				"class": "radioBox"
			}).appendTo(pidli);
			$("<input>", {
				"id": "group_id" + items[i].group_id,
				"type": "radio",
				"name": "chooseGoods",
				"value": items[i].group_id
			}).appendTo(radioBox);
			$("<label>", {
				"for": "group_id" + items[i].group_id,
				"click": function() {
					self.data = $(this).data();
					self.DialogBox.close().remove();
					self.callback(self.data);
				}
			}).data(items[i]).appendTo(radioBox);
		});
		if (sign) {
			var param = {};
			param.pageSize = this.options.pageSize;
			param.curPage = this.options.curPage;
			param.count = data.count;
			this._initPagination(param);
		}
	},
	_initPagination: function(param) {
		var self = this;
		$("<div>", {
			"class": "pagination"
		}).pagination(param.count, {
			num_edge_entries: 1,
			num_display_entries: 3,
			current_page: param.curpage,
			prev_text: "<li class='iconfont icon-left  '></li>",
			next_text: "<li class='iconfont icon-right  '></li>",
			callback: function(page_index, jq) {
				self.options.curPage = page_index;
				self._loadData();
				return false;
			},
			items_per_page: param.pageSize
		}).appendTo(self.$pageToolbarObj);
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