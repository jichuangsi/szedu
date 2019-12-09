layui.use(['form', 'table', 'laydate','multiSelect'], function() {
	var form = layui.form,
	multiSelect=layui.multiSelect,
		laydate = layui.laydate,
		table = layui.table;

	$('.time').each(function() {
		laydate.render({
			elem: this,
			type: 'date'
		});
	})

	table.render({
		elem: '#demo',
		method: "get",
		async: false,
		id:'class',
		url: httpUrl() + '/Course/getAllCourse',
		headers: {
			'accessToken': getToken()
		},
		cols: [
			[{
					field: 'id',
					title: '序号',
					type: 'numbers'
				}, {
					field: 'courseTitle',
					title: '课堂名称',
					align: 'center'
				},
				{
					field: 'teacherName',
					title: '授课教师',
					align: 'center'
				},
				{
					field: 'courseTimeLength',
					title: '课程时长(分钟)',
					align: 'center'
				},
				{
					field: 'startTime',
					title: '上课时间',
					align: 'center',
					templet: function(d) {
						if(d.startTime != 0) {
							return new Date(+new Date(d.startTime) + 8 * 3600 * 1000).toISOString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '')
						} else {
							return "-"
						}
					}
				},
				{
					field: 'content',
					title: '课堂简介',
					align: 'center'
				},
				{
					field: 'certification',
					title: '操作',
					align: 'center',
					toolbar: '#operation'
				}
			]
		],
		toolbar: '#AddClass',
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
	table.on('row(demo)', function(data) {
		var param = data.data;
		form.val('class', {
			"id": param.id,
			"courseTimeLength": param.courseTimeLength,
			"courseTitle": param.courseTitle,
			"teacherName": param.teacherName,
			"content": param.content,
			"startTime":param.startTime
		});
		getTeacher(param.teacherId);
		$(document).on('click', '#DelClass', function() {
			DelClassRoom(param.id);
		})

	});
	//按条件查询
	form.on('submit(sreach)', function(data) {
		var param = data.field;
		table.reload('class', {
			where: {
				'name': param.name
			}
		});
	});
	//添加课堂信息
	form.on('submit(formAddDemo)', function(data) {
		var param = data.field;
		var url = "/Course/saveCourse";
		ajaxPOST(url, param);
		table.reload('class');
		layer.close(index);
	});
	//修改课堂信息
	form.on('submit(formModifyDemo)', function(data) {
		var param = data.field;
		var url = "/Course/updateCourse";
		ajaxPOST(url, param);
		table.reload('class');
		layer.close(index);
		return false;
	});
	//删除课堂
	function DelClassRoom(id) {
		layer.confirm('确认要删除吗？', function(index) {
			var url = "/Course/deleteCourse?id=" + id;
			ajaxPOST(url)
			table.reload('class');
			layer.close(index);
		})
	}
	getTeacher();
	//获取教师列表
	function getTeacher(id) {
		$('#teacher').empty();
		$('#modifyTeacher').empty();
		var options = '<option value="-1" selected="selected">' + "请选择教师" + '</option>';
		var arr = [];
		$.ajax({
			type: "post",
			url: httpUrl() + "/UserInfoConsole/teacher/getAllTeacher",
			async: false,
			headers: {
				'accessToken': getToken()
			},
			contentType: 'application/json',
			success: function(res) {
				if(res.code == '0010') {
					arr = res.data
					if(arr == null || arr == undefined) {
						options = '<option value="" selected="selected">暂无教师信息请先去添加教师</option>'
					} else {
						for(var i = 0; i < arr.length; i++) {
							options += '<option value="' + arr[i].id + '" >' + arr[i].name + '</option>'
						}
					}
				} else if(res.code == '0031') {
					layui.notice.info("提示信息：权限不足");
				} else if(res.code == '0050') {
					layui.notice.error("提示信息:错误!");
				}
			}
		});
		if(id != undefined) {
			$('#modifyTeacher').append(options);
			$("#modifyTeacher option[value=" + id + "]").prop("selected", true);
		}
		$('#teacher').append(options);
		form.render('select');
	}
})