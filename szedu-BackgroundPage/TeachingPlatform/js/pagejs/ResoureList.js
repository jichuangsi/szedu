layui.use(['form', 'table'], function() {
	var form = layui.form,
		table = layui.table;

	table.render({
		elem: '#demo',
		method: "get",
		async: false,
		id:"teacher",
		url: httpUrl() + '/CourseWare/getCourseWareList2',
		headers: {
			'accessToken': getToken()
		},
		cols: [
			[{
					field: 'id',
					title: '序号',
					type: 'numbers'
				}, {
					field: 'teacherName',
					title: '上传者',
					align: 'center'
				}, {
					field: 'resourceName',
					title: '资源名称',
					align: 'center'
				},
				{
					field: 'resourceType',
					title: '资源标签',
					align: 'center'
				},
				{
					field: 'integral',
					title: '下载积分',
					align: 'center'
				},
				{
					field: 'describes',
					title: '描述',
					align: 'center'
				},
				{
					field: 'download',
					title: '下载',
					toolbar: '#download',
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

	//监听表格
	table.on('row(demo)', function(data) {
		var param = data.data;
		$(document).on('click', '#delFile', function() {
			DelResoure(param.resourceId);
		});
	});
	//按条件查询
	form.on('submit(sreach)', function(data) {
		var param = data.field;
		table.reload('teacher', {
			where: {
				'name': param.userName
			}
		});
	});
	//删除
	function DelResoure(id) {
		layer.confirm('确认要删除吗？', function(index) {
			var url = "/CourseWare/deleteAttachment?fileId=" + id;
			ajaxPOST(url)
			table.reload('demo');
			layer.close(index);
		})
	}
})