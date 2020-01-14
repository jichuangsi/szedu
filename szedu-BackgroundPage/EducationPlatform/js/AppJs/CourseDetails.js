layui.use(['form', 'table', 'rate', 'element'], function() {
	var form = layui.form,
		rate = layui.rate,
		element = layui.element,
		table = layui.table;
	var cont = 5;
	$('.rate').each(function() {
		console.log(cont)
		rate.render({
			elem: this,
			length: cont,
			value: cont,
			half: true,
			readonly: true
		})
		cont--
	})
	getCourseDetails()
	//获取课堂详情
	function getCourseDetails() {
		var id = UrlSearch();
		var url = '/studentLesson/getCourseDetail?courseId=' + id;
		var arr = StudentPostMethod(url);
		console.log(arr)
		var str = '';
		$('#details').empty();
		str += '<li><span>课堂名称：</span>' + arr.lessonName + '</li>';
		str += '<li><span>上课地点：</span>' + arr.teachAddress + '</li>';
		str += '<li><span>课堂类型：</span>' + arr.lessonTypeName + '</li>';
		str += '<li><span>任课老师：</span>' + arr.teacherName + '</li>';
		str += '<li><span>科目：</span>' + arr.subjectName + '</li>';
		str += '<li><span>章节：</span>' + arr.chapter + '</li>';
		str += '<li><span>开课时间：</span>' + new Date(+new Date(arr.teachTime) + 8 * 3600 * 1000).toISOString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '')  + '</li>';
		str += '<li><span>课堂时长：</span>' + arr.teachTimeLength + '</li>';
		str += '<li><span>课堂简介：</span>' + arr.lessonContent + '</li>';
		
		$('#details').append(str);
	}

	function UrlSearch() { //获取url里面的参数
		var name, value;
		var str = location.href; //取得整个地址栏
		var num = str.indexOf("?")
		str = str.substr(num + 1); //取得所有参数   stringvar.substr(start [, length ]
		var arr = str.split("="); //各个参数放到数组里
		return arr[1];
	}
	//获取课堂评分
	function getCourseScore(){
		
	}
})