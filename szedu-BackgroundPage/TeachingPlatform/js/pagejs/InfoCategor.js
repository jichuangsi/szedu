layui.use(['form', 'table'], function() {
	var form = layui.form,
		table = layui.table;
	
	table.render({
		elem: '#demo',
		method: "get",
		async: false,
		url: '../json/new.json',
		headers: {
			'accessToken': getToken()
		},
		cols: [
			[{
					field: 'id',
					title: '序号',
					type: 'numbers'
				}, {
					field: 'sub',
					title: '分类',
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
		limit: 10,
		loading: true,
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