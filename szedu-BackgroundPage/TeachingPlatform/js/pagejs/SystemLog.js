layui.use(['form', 'table'], function() {
	var form = layui.form,
		table = layui.table;
	
		table.render({
		elem: '#demo',
		method: "get",
		async: false,
		url: '../json/data.json',
		headers: {
			'accessToken': getToken()
		},
		cols: [
			[{
					field: 'id',
					title: '序号',
					type: 'numbers'
				}, {
					field: 'account',
					title: '执行者',
					align:'center'
				}, {
					field: 'cz',
					title: '行为名称',
					align:'center'
				},
				{
					field: 'time',
					title: '操作时间',
					align:'center'
				},
				{
					field: 'certification',
					title: '操作',
					align:'center',
					toolbar: '#operation'
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
})