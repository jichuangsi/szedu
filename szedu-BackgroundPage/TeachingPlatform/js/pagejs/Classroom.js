layui.use(['form', 'table'], function() {
	var form = layui.form,
		table = layui.table;
	
	table.render({
		elem: '#demo',
		method: "get",
		async: false,
		url: '../json/Knowledgge.json',
		headers: {
			'accessToken': getToken()
		},
		cols: [
			[{
					field: 'id',
					title: '序号',
					type: 'numbers'
				}, {
					field: 'className',
					title: '课堂名称',
					align: 'center'
				}, 
				 {
					field: 'tName',
					title: '授课教师',
					align: 'center'
				}, 
				 {
					field: 'time',
					title: '课程时长(分钟)',
					align: 'center'
				}, 
				 {
					field: 'start',
					title: '上课时间',
					align: 'center'
				}, 
				 {
					field: 'd',
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
		page:true,
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