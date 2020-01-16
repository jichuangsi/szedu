layui.use(['form', 'table', 'laydate'], function() {
	var form = layui.form,
		laydate = layui.laydate,
		table = layui.table;
	getStudentData();
	$('.time').each(function() {
		laydate.render({
			elem: this,
			type: 'date'
		});
	})

	function getStudentData() {
		var user = JSON.parse(sessionStorage.getItem('student'))
		var url = '/studentInfo/getstudentInfo?studentId=' + user.id;
		var arr = studentGetMethod(url);
		$('input[name=name]').val(arr.name);
		$('input[name=sex]').val(arr.sex);
		$('input[name=birthday]').val(arr.birthday);
		$('input[name=address]').val(arr.address);
		$('input[name=schoolName]').val(arr.schoolName);
		$('input[name=portrait]').val(arr.portrait);
		var classStr = '';
		$.each(arr.className, function(item, item) {
			classStr += item + ','
		});
		$('input[name=className]').val(classStr)
		if(arr.phone == null) {
			$('#phone').empty();
			str = '<div class="layui-btn layui-btn-primary layui-btn-radius layui-btn-sm">未绑定</div>'
			$('#phone').append(str)
		} else {
			$('#phone').empty();
			str = '<input type="text" class="layui-input" name="phone"  />'
			$('#phone').append(str)
			form.render();
			$('input[name=phone]').val(arr.phone);
		}
	}

	//学生修改
	form.on('submit(formDemo)', function(data) {
		var param = data.field;
		var user = JSON.parse(sessionStorage.getItem('student'))
		var url = '/studentInfo/updateStudent';
		param.id = user.id;
		var arr = StudentPostMethod(url, param);
		if(arr) {
			return layer.msg("修改成功！");
		} else {
			return layer.msg("修改失败！");
		}
	});
})