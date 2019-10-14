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
					field: 'resources',
					title: '资源名称',
					align: 'center'
				}, {
					field: 'account',
					title: '上传者',
					align: 'center'
				},
				{
					field: 'bq',
					title: '资源标签',
					align: 'center'
				},
				{
					field: 'downloadIntegral',
					title: '下载积分',
					align: 'center'
				},
				{
					field: 'reslb',
					title: '资源类别',
					align: 'center'
				},
				{
					field: 'describe',
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