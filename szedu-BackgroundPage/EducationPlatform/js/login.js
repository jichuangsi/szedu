layui.use(['form', 'layer', 'element'], function() {
	sessionStorage.clear();
	var form = layui.form,
		layer = layui.layer,
		element = layui.element;
	form.on('submit(login)', function(data) {
		var param = data.field;
		$.ajax({
			url: httpUrl() + '/teacherInfo/teacherLogin',
			type: "post",
			async: false,
			dataType: 'JSON',
			contentType: 'application/json',
			data: JSON.stringify(param),
			success: function(res) {
				if(res.code == "0010") {
					var user=res.data;
					console.log(user)
					sessionStorage.setItem('token', res.data.accessToken);
					sessionStorage.setItem('user',JSON.stringify(user));
					location.href = 'index.html';
				} else {
					setMsg(res.msg, 7)
				}
			}
		});
		return false;
	});
});