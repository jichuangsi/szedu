layui.use(['form', 'table'], function() {
	var form = layui.form,
		table = layui.table;
	sessionStorage.clear();
	form.on('submit(login)', function(data) {
		var param = data.field;
		$.ajax({
			url: httpUrl() + '/studentInfo/studentLogin',
			type: "post",
			async: false,
			dataType: 'JSON',
			contentType: 'application/json',
			data: JSON.stringify(param),
			success: function(res) {
				if(res.code == "0010") {
					var user = res.data;
					console.log(user)
					sessionStorage.setItem('studentToken', res.data.accessToken);
					sessionStorage.setItem('student', JSON.stringify(user));
					location.href = 'index.html';
				} else {
					setMsg(res.msg, 7)
				}
			}
		});
		return false;
	});

})