layui.use(['form', 'table', 'upload'], function() {
	var form = layui.form,

		table = layui.table;
	var upload = layui.upload;

	upload.render({
		elem: '#load',
		url: httpUrl() + '/CourseWare/saveAttachment',
		accept: 'file',
		method: 'POST',
		size: 1024 * 10,
		done: function(res) {
			if(res.code == "0010") {
				var arr = res.data;
				console.log(arr);
				setMsg('文件导入成功!', 6)
				form.val('load', {
					"filepath": arr.path,
					"filegroup": arr.group
				});
			} else {
				setMsg('文件导入失败!', 5)
			}
		},
		error: function() {
			setMsg('文件导入失败!', 5)
		}
	});

	table.render({
		elem: '#demo',
		method: "get",
		async: false,
		id:'teacher',
		url: httpUrl() + '/CourseWare/teacherResourseStatistics',
		headers: {
			'accessToken': getToken()
		},
		cols: [
			[{
					field: 'id',
					title: '序号',
					type: 'numbers'
				}, {
					field: 'userName',
					title: '用户名',
					align: 'center'
				}, {
					field: 'subject',
					title: '科目',
					align: 'center'
				},
				{
					field: 'school',
					title: '任教学校',
					align: 'center'
				},
				{
					field: 'integral',
					title: '积分',
					align: 'center'
				},
				{
					field: 'sc',
					title: '上传资源',
					align: 'center',
					toolbar: '#list'
				},
				{
					field: 'certification',
					title: '操作',
					align: 'center',
					toolbar: '#operation'
				},
				{
					field: 'certification',
					title: '删除',
					align: 'center',
					toolbar: '#del'
				}
			]
		],
		page: true,
		limit: 10,
		loading: true,
		request: {
			pageName: 'pageNum',
			limitName: "pageSize"
		},
		parseData: function(res) {
			var arr;
			var code = 0;
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
		form.val('load', {
			"teacherid": param.teacherId
		});
		$(document).on('click', '#DelTeacher', function() {
			DelTeacher(param.teacherId);
		});
		sessionStorage.setItem("teacherId", param.teacherId);
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
	//上传附件
	form.on('submit(formDemo)', function(data) {
		var param = data.field;
		var url = "/CourseWare/saveCourse";
		if(param.filegroup == "") {
			setMsg('请先上传附件',8)
			return false;
		} else if(param.filepath == "") {
			setMsg('请先上传附件',8)
			return false;
		} else {
			ajaxPOST(url, param);
			layer.close(index);
			return false;
		}
		return false;
	});
})