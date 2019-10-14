var url;
var token;
//修改路径
function httpUrl() {
	url = "http://192.168.31.83:8080"
	return url;
}
//获取token
function getToken() {
	return token = sessionStorage.getItem('token');
}

function ajaxGET(url) {
	$.ajax({
		type: "get",
		url: httpUrl() + url,
		async: false,
//		headers: {
//			'accessToken': getToken()
//		},
		success: function(res) {
			if(res.code == '0010') {
				layui.notice.success("提示信息:成功!");
				return res.data;
			} else if(res.code == '0031') {
				layui.notice.info("提示信息：权限不足");
			} else if(res.code == '0050') {
				layui.notice.error("提示信息:错误!");
			}
		}
	});
}

function ajaxPOST(url, param) {
	$.ajax({
		type: "post",
		url: httpUrl() + url,
		async: false,
		//		headers: {
		//			'accessToken': getToken()
		//		},
		contentType: 'application/json',
		data: JSON.stringify(param),
		success: function(res) {
			if(res.code == '0010') {
				layui.notice.success("提示信息:成功!");
				return res.data;
			} else if(res.code == '0031') {
				layui.notice.info("提示信息：权限不足");
			} else if(res.code == '0050') {
				layui.notice.error("提示信息:错误!");
			}
		}
	});
	return false;
}
function setMsg(msg, icon) {
	layer.msg(msg, {
		icon: icon,
		time: 1000
	});
}


layui.config({
	base: '../js/modules/'
}).extend({
	notice: 'dist/notice'
}).use(['layer', 'laypage', 'notice'], function(exports) {
	var notice = layui.notice;
	notice.options = {
		closeButton: true, //显示关闭按钮
		debug: false, //启用debug
		positionClass: "toast-top-right", //弹出的位置,
		showDuration: "2000", //显示的时间
		hideDuration: "2000", //消失的时间
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
})