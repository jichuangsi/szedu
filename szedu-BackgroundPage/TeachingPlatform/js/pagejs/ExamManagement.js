layui.use(['form', 'table', 'laydate'], function() {
	var form = layui.form,
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
		id: 'Exam',
		url: httpUrl() + '/backExam/getAllExam',
		headers: {
			'accessToken': getToken()
		},
		cols: [
			[{
					field: 'id',
					title: '序号',
					type: 'numbers'
				}, {
					field: 'examName',
					title: '考试名称',
					align: 'center'
				},
				{
					field: 'creatorName',
					title: '监考老师',
					align: 'center'
				},
				{
					field: 'course',
					title: '课程',
					align: 'center'
				},
				{
					field: 'testTimeLength',
					title: '考试时长(分钟)',
					align: 'center'
				},
				{
					field: 'startTime',
					title: '开始时间',
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
					field: 'certification',
					title: '操作',
					align: 'center',
					toolbar: '#operation'
				}
			]
		],
		toolbar: '#AddExam',
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
	//按条件查询
	form.on('submit(sreach)', function(data) {
		var param = data.field;
		table.reload('Exam', {
			where: {
				'name': param.name
			}
		});
	});
	table.on('row(demo)', function(data) {
		var param = data.data;
		form.val('Exam', {
			"id": param.id,
			"examName": param.examName,
			"testTimeLength": param.testTimeLength,
			"startTime": param.startTime
		});
		updateTeacher(param.creatorId);
		$(document).on('click', '#DelExam', function() {
			DelExam(param.id);
		})

	});
	form.on('submit(formDemo)', function(data) {
		var param = data.field;
		var url = "/backExam/saveExam";
		if(param.creatorId == -1) {
			setMsg("请选择教师", 5)
		} else {
			ajaxPOST(url, param);
			table.reload('Exam');
			layer.close(index);
			return false;
		}
	});
	//修改考试
	form.on('submit(formModifyDemo)', function(data) {
		var param = data.field;
		var url = "/backExam/saveExam";
		if(param.creatorId == -1) {
			setMsg("请选择教师", 5)
		} else {
			ajaxPOST(url, param);
			table.reload('Exam');
			layer.close(index);
			return false;
		}
	});
	getLabel();

	function getLabel() {
		$('#teacher').empty();
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

		$('#teacher').append(options);
		form.render('select');
	}
	//修改时候判断老师

	function updateTeacher(id) {
		$('#teacherList').empty();
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
		$('#teacherList').append(options);
		$("#teacherList option[value=" + id + "]").prop("selected", true);
		form.render('select');
	}

	//删除考试
	function DelExam(id) {
		layer.confirm('确认要删除吗？', function(index) {
			var url = "/backExam/deleteExam?id=" + id;
			ajaxPOST(url)
			table.reload('Exam');
			layer.close(index);
		})
	}
})