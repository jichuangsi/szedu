layui.use(['form', 'upload', 'element', 'table'], function() {
	var form = layui.form,
		element = layui.element,
		upload = layui.upload,
		table = layui.table;

	table.render({
		elem: '#demo',
		method: "post",
		async: false,
		id: "student",
		url: httpUrl() + '/UserInfoConsole/student/getStudentByCondition',
		contentType: 'application/json',
		headers: {
			'accessToken': getToken()
		},
		cols: [
			[{
					field: 'id',
					title: '序号',
					type: 'numbers'
				}, {
					field: 'name',
					title: '用户名',
					align: 'center'
				}, {
					field: 'phone',
					title: '手机号',
					align: 'center'
				}, {
					field: 'sex',
					title: '性别',
					align: 'center'
				},
				{
					field: 'schoolName',
					title: '就读学校',
					align: 'center'
				},
				{
					field: 'certification',
					title: '操作',
					align: 'center',
					toolbar: '#operation'
				}, {
					field: 'id',
					title: '修改密码',
					align: 'center',
					toolbar: '#updatePwd'
				}
			]
		],
		page: true,
		limit: 10,
		loading: true,
		request: {
			pageName: 'pageIndex',
			limitName: "pageSize"
		},
		parseData: function(res) {
			var arr;
			var code;
			var total = 0;
			if(res.code == "0010") {
				arr = res.data.list;
				total = res.data.total;
				code = 0;
			}
			return {
				"code": code,
				"msg": res.msg,
				"count": total,
				"data": arr
			};
		}
	});

	//监听表格
	table.on('row(demo)', function(data) {
		var param = data.data;
		$(document).on('click', '#DelStudent', function() {
			DelStudent(param.id);
		});
		form.val('pwd', {
			"id": param.id
		});
		form.val('test', {
			"id": param.id,
			"name": param.name,
			"phone": param.phone,
			"schoolName": param.schoolName
		});
	});
	//按条件查询
	form.on('submit(sreach)', function(data) {
		var param = data.field;
		table.reload('student', {
			where: {
				'userName': param.userName
			}
		});
	});
	//添加学生信息
	form.on('submit(formAddDemo)', function(data) {
		var param = data.field;
		var url = "/UserInfoConsole/saveStudent";
		if(param.pwd != param.savePwd) {
			$("input[name=savePwd]").addClass("layui-form-danger");
			setMsg("两次密码不相同", 2);
		} else {
			$("input[name=savePwd]").removeClass("layui-form-danger");
			ajaxPOST(url, param);
			table.reload('student');
			layer.close(index);
		}
	});
	//修改学生信息
	form.on('submit(formModifyDemo)', function(data) {
		var param = data.field;
		var url = "/UserInfoConsole/updateStudent";
		ajaxPOST(url, param);
		table.reload('student');
		layer.close(index);
		return false;
	});
	//删除学生
	function DelStudent(id) {
		layer.confirm('确认要删除吗？', function(index) {
			var url = "/UserInfoConsole/deleteUserById?id=" + id;
			ajaxPOST(url)
			table.reload('student');
			layer.close(index);
		})
	}
	//修改学生的密码
	form.on('submit(formModifyPwdDemo)', function(data) {
		var param = data.field;
		//判断两次密码是否相同
		if(param.pwd != param.oldPwd) {
			setMsg("两次密码不相同！", 7)
			return false;
		} else {
			var url = "/UserInfoConsole/updateUserPwdById?id=" + param.id + "&pwd=" + param.pwd + "&newPwd=" + param.oldPwd;
			ajaxGET(url);
			layer.close(index);
			return false;
		}
	});
	//表格上传 批量上传
	upload.render({
		elem: '#load',
		url: httpUrl() + '/UserInfoConsole/excel/saveStudentByExcel',
		//		headers: {
		//			'accessToken': getToken()
		//		},
		method: 'POST',
		accept: 'file',
		size: 1024 * 8,
		exts: 'xls/*',
		before: function(obj) {
			//layer.load(); //上传loading
		},
		done: function(res, index, upload) { //上传后的回调
			if(res.code == "0010") {
				if(res.data == '[]') {
					setMsg("导入成功!", 1);
					table.reload('student');
				} else {
					var str = res.data;
					layer.msg("第" + str + "行导入失败", {
						icon: 1,
						time: 3000,
						end: function() {
							$('#error').text("第" + str + "行导入失败");
							return false
						}
					})
				}

			} else {
				setMsg("导入失败!", 2);
			}
		},
		error: function() {
			layer.closeAll('loading');
		}
	})

	form.verify({
		username: function(value, item) { //value：表单的值、item：表单的DOM对象
			if(!new RegExp("^[a-zA-Z0-9_\u4e00-\u9fa5\\s·]+$").test(value)) {
				return '用户名不能有特殊字符';
			}
			if(/(^\_)|(\__)|(\_+$)/.test(value)) {
				return '用户名首尾不能出现下划线\'_\'';
			}
			if(/^\d+\d+\d$/.test(value)) {
				return '用户名不能全为数字';
			}
			//			if(!new RegExp("^[\\u4E00-\\u9FFF]+$", "g").test(value)) {
			//				return '用户名不能含中文';
			//			}
		}
	});
})