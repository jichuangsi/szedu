layui.use(['form', 'table','laydate'], function() {
	var form = layui.form,
	laydate=layui.laydate,
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
					field: 'Ename',
					title: '考试名称',
					align: 'center'
				},
				{
					field: 'sub',
					title: '课程',
					align: 'center'
				},
				{
					field: 'Etime',
					title: '考试时长(分钟)',
					align: 'center'
				},
				{
					field: 'start',
					title: '开始时间',
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
		toolbar: '#AddExam',
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