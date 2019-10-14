layui.use(['form', 'table'], function() {
	var form = layui.form,
		table = layui.table;
	table.render({
		elem: '#demo',
		method: "get",
		async: false,
		url: '../json/data.json',
		contentType: 'application/json',
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
					title: '用户名',
					align:'center'
				}, {
					field: 'subject',
					title: '科目',
					align:'center'
				},
				{
					field: 'school',
					title: '任教学校',
					align:'center'
				}, {
					field: 'region',
					title: '地区',
					align:'center'
				},
				{
					field: 'integral',
					title: '积分',
					align:'center'
				},
				{
					field: 'sc',
					title: '上传资源(次)',
					align:'center'
				},
				{
					field: 'xz',
					title: '下载资源(次)',
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