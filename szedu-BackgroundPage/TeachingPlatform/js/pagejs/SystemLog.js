layui.use(['form', 'table'], function() {
	var form = layui.form,
		table = layui.table;

	table.render({
		elem: '#demo',
		method: "get",
		async: false,
		url: httpUrl() + '/backuser/getOpLogByNameAndPage',
		headers: {
			'accessToken': getToken()
		},
		cols: [
			[{
					field: 'id',
					title: '序号',
					type: 'numbers'
				}, {
					field: 'operatorName',
					title: '执行者',
					align: 'center'
				}, {
					field: 'opAction',
					title: '行为名称',
					align: 'center'
				},
				{
					field: 'createdTime',
					title: '操作时间',
					align: 'center',
					templet: function(d) {
						if(d.createdTime != 0) {
							return new Date(+new Date(d.createdTime) + 8 * 3600 * 1000).toISOString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '')
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
				arr = res.data.content;
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
		form.val('pwd', {
			"id": param.id
		});
		$(document).on('click', '#DelLog', function() {
			DelLog(param.id);
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
	//删除日志
	function DelLog	(id) {
		layer.confirm('确认要删除吗？', function(index) {
			var url = "/backuser/deleteOpLog?opId=" + id;
			ajaxGET(url)
			table.reload('demo');
			layer.close(index);
		})
	}
})