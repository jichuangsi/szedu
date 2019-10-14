layui.use(['form', 'table', 'laydate', 'layedit'], function() {
	var form = layui.form,
		laydate = layui.laydate,
		layedit = layui.layedit,
		table = layui.table;

	$('.time').each(function() {
		laydate.render({
			elem: this,
			type: 'date'
		});
	})
	//富文本图片上传接口
	layedit.set({
	  uploadImage: {
		url: httpUrl() + "/back/school/sendMessageImage", //接口url
		type: 'post', //默认post
		headers: {
			'accessToken': getToken()
		},
		size: 5*1024,
		acceptMime: 'images',
		accept: 'images'
	  }
	});
	var index = layedit.build('demo', {
		tool: [
			'strong' //加粗
			, 'italic' //斜体
			, 'underline' //下划线
			, 'del' //删除线
			, '|' //分割线
			, 'left' //左对齐
			, 'center' //居中对齐
			, 'right' //右对齐
			, 'link' //超链接
			, 'unlink' //清除链接
			,'face' //表情
			,'image' //插入图片
			//,'help' //帮助
		]
	});
	form.on('submit(formDemo)', function(data) {
		var param = data.field;
		//富文本的内容		
		$.ajax({
			type: "post",
			url: httpUrl() + "",
			headers: {
				'accessToken': getToken()
			},
			async: false,
			contentType: 'application/json',
			data: JSON.stringify(model),
			success: function(res) {
				if(res.code == '0010') {
					layer.msg('发布成功！', {
						icon: 1,
						time: 1000,
						end: function() {
							table.reload('Info');
						}
					});
				} else {
					layer.msg(res.msg, {
						icon: 2,
						time: 1000,
						end: function() {
							table.reload('Info');
						}
					});
				}
			}
		});
		return false;

	});
})