layui.use(['form', 'upload', 'element', 'table'], function() {
	var form = layui.form,
		element = layui.element,
		upload = layui.upload;
	table = layui.table;
	table.render({
		elem: '#demo',
		method: "post",
		async: false,
		url: httpUrl() + '/UserInfoConsole/teacher/getTeacherByCondition',
		contentType: 'application/json',
		id: "teacher",
		//		headers: {
		//			'accessToken': getToken()
		//		},
		cols: [
			[{
					field: 'id',
					title: '序号',
					type: 'numbers'
				}, {
					field: 'account',
					title: '账户名',
					align: 'center'
				},
				{
					field: 'name',
					title: '用户名',
					align: 'center'
				},
				{
					field: 'sex',
					title: '性别',
					align: 'center'
				}, {
					field: 'subject',
					title: '科目',
					align: 'center'
				},
				{
					field: 'integral',
					title: '积分',
					align: 'center'
				},
				{
					field: 'schoolName',
					title: '任教学校',
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
		form.val('test', {
			"id": param.id,
			"name": param.name,
			"subject": param.subject,
			"schoolName": param.schoolName,
			"integral": param.integral
		});
		form.val('pwd', {
			"id": param.id
		});
		$(document).on('click', '#DelTeacher', function() {
			DelTeacher(param.id);
		});
	});
	//按条件查询
	form.on('submit(sreach)', function(data) {
		var param = data.field;
		table.reload('teacher', {
			where: {
				'userName': param.userName
			}
		});
	});
	//添加教师信息
	form.on('submit(formAddDemo)', function(data) {
		var param = data.field;
		var url = "/UserInfoConsole/saveTeacher";
		if(param.pwd != param.savePwd) {
			$("input[name=savePwd]").addClass("layui-form-danger");
			setMsg("两次密码不相同", 2);
		} else {
			$("input[name=savePwd]").removeClass("layui-form-danger")
			ajaxPOST(url, param);
			table.reload('teacher');
		}
	});
	//修改教师信息
	form.on('submit(formModifyDemo)', function(data) {
		var param = data.field;
		var url = "/UserInfoConsole/updateTeacher";
		ajaxPOST(url, param);
		table.reload('teacher');
		layer.close(index);
		return false;
	});
	//删除教师
	function DelTeacher(id) {
		layer.confirm('确认要删除吗？', function(index) {
			var url = "/UserInfoConsole/deleteUserById?id=" + id;
			ajaxPOST(url)
			table.reload('teacher');
			layer.close(index);
		})
	}

	//修改老师的密码
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
		url: httpUrl() + '/UserInfoConsole/excel/saveTeacherByExcel',
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
					layer.msg("导入成功！", {
						icon: 1,
						time: 1000,
						end: function() {
							table.reload('teacher');
						}
					})
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
				layer.msg("导入失败！", {
					icon: 2,
					time: 1000,
					end: function() {
						location.reload();
					}
				})
			}
		},
		error: function() {
			layer.closeAll('loading');
		}
	})
})