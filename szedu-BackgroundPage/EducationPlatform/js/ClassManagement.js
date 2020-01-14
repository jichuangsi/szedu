layui.use(['form', 'table', 'upload'], function() {
	var form = layui.form,
		upload = layui.upload;
	table = layui.table;
	var classId =UrlSearch();
	table.render({
		elem: '#demo',
		method: "get",
		async: false,
		url: httpUrl() + '/classConsole/getStudentByClassId?classId=' + classId,
		headers: {
			'accessToken': getToken(),
		},
		cols: [
			[{
					field: 'id',
					title: '序号',
					type: 'numbers'
				},
				{
					field: 'name',
					title: '姓名',
					align: 'center'
				},
				{
					field: 'studentId',
					title: '学号',
					align: 'center'
				}, {

					field: 'account',
					title: '账户名',
					align: 'center'
				},
				{
					field: 'specialtity',
					title: '专业',
					align: 'center'
				},
				{
					field: 'name',
					title: '班级',
					align: 'center'
				}, {
					field: 'phone',
					title: '手机号',
					align: 'center'
				}, {
					field: 'subject',
					title: '操作',
					width: 250,
					align: 'center',
					toolbar: '#operation'
				}
			]
		],
		skin: 'line',
		page: true,
		limit: 10,
		loading: true,
		request: {
			pageName: 'pageNum',
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
	form.on('submit(formAddStudentDemo)', function(data) {
		var param = data.field;
		var url = '/classConsole/addStudent';
		param.classId = classId;
		ajaxPOST(url, param);
		table.reload('demo');
		layer.close(index);
	});
	//表格上传 批量上传
	upload.render({
		elem: '#load',
		url: httpUrl() + '/excel/saveStudentByExcel',
		headers: {
			'accessToken': getToken()
		},
		method: 'POST',
		accept: 'file',
		size: 1024 * 8,
		exts: 'xls/*',
		before: function(obj) {
			layer.load(); //上传loading
		},
		done: function(res, index, upload) { //上传后的回调
			if(res.code == "0010") {
				if(res.data == '[]') {
					setMsg("导入成功！", 1)
					table.reload('demo');
				} else {
					var str = res.data;
					setMsg("第" + str + "行导入失败", 1);
					$('#error').text("第" + str + "行导入失败");
				}

			} else {
				setMsg("导入失败！", 2);
				location.reload();
			}
		},
		error: function() {
			layer.closeAll('loading');
		}
	})
	//监听
	table.on('row(demo)', function(data) {
		var param = data.data;
		form.val('modPwd', {
			studentId: param.id
		});
	})
	//修改学生
	$(document).on('click', '#updateStudent', function() {

	})

	//修改班级
	$(document).on('click', '#updateClass', function() {
		
	})
	//重置学生密码
	form.on('submit(formModifyPwd)', function(data) {
		var param = data.field;
		if(param.pwd == param.newPwd) {
			var url = '/classConsole/updateStudentPwd';
			delete param.newPwd
			ajaxPOST(url, param);
			layer.close(index);
		} else {
			return setMsg('两次密码不相同！', 7);
		}

	});
})