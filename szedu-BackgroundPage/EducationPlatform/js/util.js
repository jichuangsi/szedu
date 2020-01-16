layui.config({
	base: '../js/modules/dist/'
}).extend({
	xmSelect: 'xm-select'
}).use(['layer', 'laypage', 'notice', 'multiSelect', 'xmSelect', 'form'], function(exports) {
	var notice = layui.notice,
		form = layui.form,
		xmSelect = layui.xmSelect,
		multiSelect = layui.multiSelect;
	notice.options = {
		closeButton: true, //显示关闭按钮
		debug: false, //启用debug
		positionClass: "toast-top-right", //弹出的位置,
		showDuration: "2000", //显示的时间
		hideDuration: "1000", //消失的时间
		timeOut: "2000", //停留的时间
		extendedTimeOut: "1000", //控制时间
		showEasing: "swing", //显示时的动画缓冲方式
		hideEasing: "linear", //消失时的动画缓冲方式
		iconClass: 'toast-info', // 自定义图标，有内置，如不需要则传空 支持layui内置图标/自定义iconfont类名
		onclick: null // 点击关闭回调

		//notice.warning("提示信息:警告警告!");
		//notice.info("提示信息:显示!");
		//notice.error("提示信息,报错啦!");
		//notice.success("提示信息,成功!");

	};
	//正则表达式
	form.verify({
		username: function(value, item) { //value：表单的值、item：表单的DOM对象
			if(!new RegExp("^[a-zA-Z0-9_\u4e00-\u9fa5\\s·]+$").test(value)) {
				return '用户名不能有特殊字符';
			}
			if(/(^\_)|(\__)|(\_+$)/.test(value)) {
				return '用户名首尾不能出现下划线\'_\'';
			}
			if(/^\d+\d+\d$/.test(value)) {
				return '用户名不能全为数字';
			}
		},
		studentInfo: function(value, item) {
			if(!new RegExp("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,10}$").test(value)) {
				return "不能含有其他字符!"
			}
		},
		pwd: function(value, item) {
			if(!new RegExp("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,10}$").test(value)) {
				return "密码由6-10的数字与字母组成!"
			}
		}
	});

})

function UrlSearch() { //获取url里面的参数
	var name, value;
	var str = location.href; //取得整个地址栏
	var num = str.indexOf("?")
	str = str.substr(num + 1); //取得所有参数   stringvar.substr(start [, length ]
	var arr = str.split("="); //各个参数放到数组里
	return arr[1];
}