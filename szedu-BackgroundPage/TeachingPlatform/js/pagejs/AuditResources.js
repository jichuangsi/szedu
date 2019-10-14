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
					title: '上传教师',
					align:'center'
				}, {
					field: 'resources',
					title: '资源名称',
					align:'center'
				},
				{
					field: 'reslb',
					title: '资源类型',
					align:'center'
				},
				{
					field: 'certification',
					title: '更改上传积分',
					toolbar: '#integral',
					align:'center'
				},
				{
					field: 'account',
					title: '审核',
					toolbar: '#operation',
					align:'center'
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