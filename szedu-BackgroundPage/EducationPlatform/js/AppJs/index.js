layui.use(['form', 'table', 'element', 'carousel'], function() {
	var form = layui.form,
		table = layui.table,
		element = layui.element,
		carousel = layui.carousel;

	carousel.render({
		elem: '#test1',
		width: '100%',
		height: '16rem',
		arrow: 'always' //始终显示箭头
		//,anim: 'updown' //切换动画方式
	});

	//查询课堂
	function getClass() {
		var url = '/studentLesson/getAllLesson?pageNum=1&paggeSize=4';
		var arr = studentGetMethod(url);
		$('#MyClass').empty();
		var str = '';
		if (arr.length > 0) {
			$.each(arr, function(index, item) {
				str += '<li>';
				str += '<a href="#">' + item.name + '<span class="date">2019-10-12</span></a>'
				str += '<li>'
			});
		} else {
			str += '<li>近期暂无课堂</li>';
		}
		$('#MyClass').append(str);
	}
	getTest();
	//查询考试
	function getTest() {
		var url = '/backExam/getExamByStudent?pageNum=1&pageSize=4';
		var arr = studentGetMethod(url);
		var data=arr.list;
		$('#test').empty();
		var str = '';
		if (data.length > 0) {
			$.each(data, function(index, item) {
				var startTime=new Date(+new Date(item.startTime) + 8 * 3600 *
					1000).toISOString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '') ;
				 var 	time=startTime.split(' ');
				str += '<li>';
				str += '<a href="">' + item.examName + '<span class="date">' + time[0]+ '</span></a>'
				str += '<li>'
			});
		} else {
			str += '<li>近期暂无考试</li>';
		}
		$('#test').append(str);
	}

})
