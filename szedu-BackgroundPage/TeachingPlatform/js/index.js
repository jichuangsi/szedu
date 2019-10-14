layui.use("form", function() {
	var form = layui.form;
	var admin;
	form.on('submit(update_Pwd)', function(data) {
		var param = data.field;
		if(param.newPwd != param.yesPwd) {
			layer.msg("两次密码不相同!", {
				icon: 2,
				time: 1000,
				end: function() {
					layer.close(index);
				}
			});
			return false;
		} else {
			$.ajax({
				type: "post",
				url: httpUrl() + "/back/user/updatePwd",
				async: false,
				headers: {
					'accessToken': getToken()
				},
				contentType: 'application/json',
				data: JSON.stringify(param),
				success: function(res) {
					if(res.code == '0010') {
						layer.msg('修改成功！', {
							icon: 1,
							time: 1000,
							end: function() {
								layer.close(index);
							}
						});
					} else {
						layer.msg(res.msg, {
							icon: 2,
							time: 1000,
							end: function() {
								layer.close(index);
							}
						});
					}
				}
			});
			return false;
		}
	});
	form.verify({　　　　
		pwd: [/^((?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,12})$/, '密码必须为6-12位数字与字母混合']　　
	});

});