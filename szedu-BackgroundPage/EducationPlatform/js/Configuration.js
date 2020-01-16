var url;
var token =null;
//修改路径
function httpUrl() {
		url = "http://192.168.31.84:8080"
	// url = "http://192.168.1.3:8080"
	return url;
}
//获取token
function getToken() {
	token = sessionStorage.getItem('token');
	if(token==null){
		location.href='../login.html'
	}
	return token
	
}
//获取学生token
function getStudentToken() {
	return token = sessionStorage.getItem('studentToken');
}

function getUser() {
	return user = JSON.parse(sessionStorage.getItem('user'))
}
layui.use(['form', 'element'],
	function() {
		layer = layui.layer;
		element = layui.element;
	});

function studentGetMethod(url) {
	var arr;
	$.ajax({
		type: "get",
		url: httpUrl() + url,
		async: false,
		headers: {
			'accessToken': getStudentToken()
		},
		success: function(res) {
			if(res.code == '0010') {
				arr = res.data;
			}
		}
	});
	return arr;
}

function StudentPostMethod(url, param) {
	var arr;
	$.ajax({
		type: "post",
		url: httpUrl() + url,
		async: false,
		headers: {
			'accessToken': getStudentToken()
		},
		contentType: 'application/json',
		data: JSON.stringify(param),
		success: function(res) {
			if(res.code == '0010') {
				arr = res.data;
			}
		}
	});
	return arr;
}

function ajaxGET(url) {
	var data;
	$.ajax({
		type: "GET",
		url: httpUrl() + url,
		async: false,
		headers: {
			'accessToken': getToken()
		},
		success: function(res) {
			if(res.code == '0010') {
				layui.notice.success("提示信息:成功!");
				data = res.data;
			} else if(res.code == '0031') {
				layui.notice.info("提示信息：权限不足");
			} else if(res.code == '0050') {
				layui.notice.error("提示信息:错误!");
			}
		}
	});
	return data
}

function ajaxPOST(url, param) {
	var data
	var DISABLED = 'layui-btn-disabled';
	// 增加样式
	$('.site-demo-active').addClass(DISABLED);
	// 增加属性
	$('.site-demo-active').attr('disabled', 'disabled');
	$.ajax({
		type: "post",
		url: httpUrl() + url,
		async: false,
		headers: {
			'accessToken': getToken(),
		},
		contentType: 'application/json',
		data: JSON.stringify(param),
		success: function(res) {
			if(res.code == '0010') {
				layui.notice.success("提示信息:成功!");
				data = res.data;
			} else if(res.code == '0031') {
				layui.notice.info("提示信息：权限不足");
			} else if(res.code == '0050') {
				layui.notice.error("提示信息:错误!");
			}
		}
	});

	$('.site-demo-active').removeClass(DISABLED)
	return data
	//	return false;
}

function ajaxGetData(url) {
	var arr;
	$.ajax({
		type: "get",
		url: httpUrl() + url,
		async: false,
		headers: {
			'accessToken': getToken()
		},
		success: function(res) {
			if(res.code == '0010') {
				arr = res.data;
			}
		}
	});
	return arr;
}

function ajaxPostData(url, param) {
	var arr;
	$.ajax({
		type: "post",
		url: httpUrl() + url,
		async: false,
		headers: {
			'accessToken': getToken()
		},
		contentType: 'application/json',
		data: JSON.stringify(param),
		success: function(res) {
			if(res.code == '0010') {
				arr = res.data;
			}
		}
	});
	return arr;
}

function setMsg(msg, icon) {
	layer.msg(msg, {
		icon: icon,
		time: 1000
	});
}
isSys()

function isSys() {
	var DEFAULT_VERSION = 8.0;
	var ua = navigator.userAgent.toLowerCase();
	var isIE = ua.indexOf("msie") > -1;
	var safariVersion;
	if(isIE) {
		safariVersion = ua.match(/msie ([\d.]+)/)[1];
	}
	if(safariVersion <= DEFAULT_VERSION) {
		alert('系统检测到您正在使用ie8以下内核的浏览器，不能实现完美体验，请更换或升级浏览器访问！')
	};
}
//setInterval("Online()", 60 * 1000);

function Online() {
	$.ajax({
		type: "get",
		url: httpUrl() + "/teacherInfo/getTeacherMessageCount",
		async: false,
		headers: {
			'accessToken': getToken()
		},
		success: function(res) {
			if(res.code == '0010') {

			}
		}
	});
}

//getMsg();
// setInterval("getMsg()", 30 * 1000);
function getMsg() {
	if(getToken()==undefined){
		return 
	}
	var url = '/teacherInfo/getTeacherMessageCount';
	var count = ajaxGetData(url);
	$('#msg').html('(' + count + ')');
}