layui.use(['form', 'table', 'laydate', 'upload'], function() {
	var form = layui.form,
		upload = layui.upload,
		laydate = layui.laydate,
		table = layui.table;
	$('.time').each(function() {
		laydate.render({
			elem: this,
			type: 'date'
		});
	})
	//获取老师个人中心
	getTeacherCenter();

	function getTeacherCenter() {
		var user = JSON.parse(sessionStorage.getItem('user'))
		var url = "/teacherInfo/teacherinfo?teacherId=" + user.teacherId;
		var data = ajaxGetData(url);
		var arr = data.teacherInfo;
		var deta = new Date(+new Date(arr.createTime) + 8 * 3600 * 1000).toISOString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '')

		$('input[name=name]').val(arr.name);
		$('input[name=birthday]').val(data.teacherBirthday);
		$('input[name=address]').val(arr.address);
		$('input[name=schoolName]').val(arr.schoolName);
		$('input[name=account]').val(arr.account);
		$('input[name=phone]').val(arr.phone);
		$('input[name=portrait]').val(arr.portrait);
		$('input[name=date]').val(new Date(+new Date(arr.createTime) + 8 * 3600 * 1000).toISOString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, ''));
		var className = '';
		$.each(data.classModels, function(index, item) {
			className += item.className + ','
		});
		$('input[name=className]').val(className);
	}
	form.on('submit(formDemo)', function(data) {
		var user = JSON.parse(sessionStorage.getItem('user'))
		var param = data.field;
		var url = '/teacherInfo/updateteacherinfo';
		param.id = user.teacherId;
		param.birthday = setTime(param.birthday);
		var arr = ajaxPostData(url, param);
		if(arr) {
			$('#Button1').hide()
			return layer.msg("修改成功！");
		} else {
			return layer.msg("修改失败！");
		}
	});
	$(document).on('click', '.pic-btn', function() {
		$('#Button1').show()
		$('input[name=name]').focus()
	});

	function setTime(data) {
		let d = new Date(data);
		return d.getTime(d);
	}
	uploadTeacherHeadPic();
	//上传老师头像
	function uploadTeacherHeadPic() {
		var user = JSON.parse(sessionStorage.getItem('user'))
		var uploadInst = upload.render({
			elem: '#upload',
			url: httpUrl() + '/teacherInfo/teacherinfoPic',
			before: function(obj) {
				//预读本地文件示例，不支持ie8
				obj.preview(function(index, file, result) {
					$('.demo1').attr('src', result); //图片链接（base64）
				});
			},
			data: {
				teacherId: function() {
					return user.teacherId;
				}
			},
			done: function(res) {
				//如果上传失败
				if(res.code =='0010') {
					return layer.msg('上传成功!');
				}else{
					return layer.msg('上传失败!');
				}
				//上传成功
			},
			error: function() {
				//演示失败状态，并实现重传
				var demoText = $('#demoText');
				demoText.html('<span style="color: #FF5722;">上传失败</span> <a class="layui-btn layui-btn-xs demo-reload">重试</a>');
				demoText.find('.demo-reload').on('click', function() {
					uploadInst.upload();
				});
			}
		});
	}
})