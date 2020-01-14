layui.use(['form', 'table', 'element', 'carousel'], function() {
	var form = layui.form,
		table = layui.table,
		element = layui.element;

	CreateHeader()
	//创建头部
	
	function CreateHeader() {
		var user = JSON.parse(sessionStorage.getItem('student'))
		$('#nav').empty();
		var str = '';
		str += '<ul class="layui-nav" lay-filter="demo">';
		str += '<li class="layui-nav-item">';
		str += '<a href="#">信力达</a>';
		str += '</li>';
		str += '<li class="layui-nav-item">';
		str += '<a href="#">智能.互联.引领未来</a>';
		str += '</li>';
		str += '<li class="layui-nav-item" style="float: right;margin-right: 3%;">';
		str += '<i class="layui-icon layui-icon-shrink-right"></i>';
		str += '<dl class="layui-nav-child">';
//		str += '<dd><a href="">我的收藏</a></dd>';
		str += '<dd><a href="SendMsg.html">信息发送</a></dd>';
		str += '<dd><a href="Score.html">成绩分析</a></dd>';
		str += '<dd><a href="PersonalData.html">账号与设置</a></dd>';
		str += '<dd><a href="Feedback.html">帮助与反馈</a></dd>';
		str += '<dd><a href="Studentlogin.html">退出</a></dd>';
		str += '</dl>';
		str += '</li>';
		str += '<li class="layui-nav-item" style="float: right;">';
		str += '<a href="integral.html">积分：<span>'+user.integral+'</span></a>';
		str += '</li>';
		str += '<li class="layui-nav-item" style="float: right;">';
		str += '<a href="PersonalData.html">'+user.name+'<img src="//t.cn/RCzsdCq" class="layui-nav-img"></a>';
		str += '</li>';
		str += '</ul>';
		$('#nav').append(str)
		element.render(nav, 'demo');
	}

})

function UrlSearch() { //获取url里面的参数
	var name, value;
	var str = location.href; //取得整个地址栏
	var num = str.indexOf("?")
	str = str.substr(num + 1); //取得所有参数   stringvar.substr(start [, length ]
	var arr = str.split("="); //各个参数放到数组里
	return arr[1];
}