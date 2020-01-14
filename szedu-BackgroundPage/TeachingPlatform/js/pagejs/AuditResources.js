layui.use(['form', 'table'], function() {
	var form = layui.form,
		table = layui.table;

	table.render({
		elem: '#demo',
		method: "get",
		async: false,
		url: httpUrl() + '/CourseWare/getCourseWareList',
		headers: {
			'accessToken': getToken()
		},
		cols: [
			[{
					field: 'teacherId',
					title: '序号',
					type: 'numbers'
				}, {
					field: 'teacherName',
					title: '上传教师',
					align: 'center'
				}, {
					field: 'resourceName',
					title: '资源名称',
					align: 'center'
				},
				{
					field: 'describes',
					title: '资源描述',
					align: 'center'
				},
				{
					field: 'isCheck',
					title: '状态',
					align: 'center',
					templet: function(d) {
						if(d.isCheck == 1) {
							return "待审核"
						} else if(d.isCheck == 2) {
							return "已审核"
						} else if(d.isCheck == 3) {
							return "已审核"
						}
					}
				},
				{
					field: 'certification',
					title: '更改上传积分',
					align: 'center',
					templet: function(d) {
						if(d.isCheck == 1) {
							return '<span class="layui-btn-sm layui-btn layui-btn layui-btn-normal" onclick="load()">更改积分</span>'
						} else if(d.isCheck == 2) {
							return "审核通过获得" + d.integral + "积分"
						} else if(d.isCheck == 3) {
							return "审核驳回无法获得积分"
						}
					}
				},
				{
					field: 'isCheck',
					title: '操作状态',
					align: 'center',
					templet: function(d) {
						if(d.isCheck == 1) {
							return '<span class="layui-btn-sm layui-btn" id="Agree">同意</span><span class="layui-btn-sm layui-btn" id="Reject">驳回</span> '
						} else if(d.isCheck == 2) {
							return "同意"
						} else if(d.isCheck == 3) {
							return "驳回"
						}
					}
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
		form.val('load', {
			"resourceId": param.resourceId
		});
		$(document).on('click', '#DelTeacher', function() {
			DelTeacher(param.teacherId);
		});
		$(document).on('click', '#Agree', function() {
			param.isCheck = '2'; //
			AuditApplications("确认要同意该申请吗", param);
		});
		$(document).on('click', '#Reject', function() {
			param.isCheck = '3'; //不同意
			AuditApplications('确认要驳回该申请吗', param);
		});
	});
	//审核资源
	function AuditApplications(msg, param) {
		layer.confirm(msg, function(index) {
			var url = "/CourseWare/resourceCheck?status=" + param.isCheck + "&resourceId=" + param.resourceId + "&integral=" + param.integral;
			ajaxGET(url);
			table.reload('demo');
			layer.close(index);
			return false;
		})
	}
	//修改积分
	form.on('submit(formDemo)', function(data) {
		var param = data.field;
		if(param.integral < 0) {
			param.integral = 5;
		}
		var url = "/CourseWare/updateIntegral?integral=" + param.integral + "&resourceId=" + param.resourceId;
		ajaxGET(url);
		table.reload('demo');
		layer.close(index);
		return false;
	});

	table.render({
		elem: '#share',
		method: "get",
		async: false,
		url: httpUrl() + '/CourseWare/getShareCourseWareList',
		headers: {
			'accessToken': getToken()
		},
		cols: [
			[{
					field: 'teacherId',
					title: '序号',
					type: 'numbers'
				}, {
					field: 'teacherName',
					title: '上传教师',
					align: 'center'
				}, {
					field: 'resourceName',
					title: '资源名称',
					align: 'center'
				},
				{
					field: 'describes',
					title: '资源描述',
					align: 'center'
				},
				{
					field: 'isCheck',
					title: '状态',
					align: 'center',
					templet: function(d) {
						if(d.isShare == 1) {
							return "待审核"
						} else if(d.isShare == 2) {
							return "已审核"
						}else if(d.isShare == 3){
							return "用户取消分享"
						} else if(d.isShare == 4) {
							return "已审核"
						}
					}
				},
				{
					field: 'isCheck',
					title: '操作状态',
					align: 'center',
					templet: function(d) {
						if(d.isShare == 1) {
							return '<span class="layui-btn-sm layui-btn" id="shareAgree">同意</span><span class="layui-btn-sm layui-btn" id="shareReject">驳回</span> '
						} else if(d.isShare == 2) {
							return "同意"
						} else if(d.isShare == 4) {
							return "驳回"
						} else if(d.isShare == 3) {
							return "-"
						}
					}
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
	table.on('row(share)', function(data) {
		var param = data.data;
		form.val('load', {
			"resourceId": param.resourceId
		});
		$(document).on('click', '#shareAgree', function() {
			param.isCheck = '2'; //
			AuditApplicationsShare("确认要同意该申请吗", param);
		});
		$(document).on('click', '#shareReject', function() {
			param.isCheck = '4'; //不同意
			AuditApplicationsShare('确认要驳回该申请吗', param);
		});
	});
	//审核资源
	function AuditApplicationsShare(msg, param) {
		layer.confirm(msg, function(index) {
			var url = "/CourseWare/shareResourceCheck?status=" + param.isCheck + "&resourceId=" + param.resourceId + "&integral=" + param.integral;
			ajaxGET(url);
			table.reload('share');
			layer.close(index);
			return false;
		})
	}
})