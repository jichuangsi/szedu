layui.use(['form', 'table'], function() {
	var form = layui.form,
		table = layui.table;

	var classId = UrlSearch()

	table.render({
		elem: '#demo',
		method: "get",
		async: false,
		url: httpUrl() + '/classConsole/getStudentByClassId?classId=' + classId,
		headers: {
			'accessToken': getToken(),
		},
		cols: [
			[{
					field: 'id',
					title: '序号',
					type: 'numbers'
				},
				{
					field: 'name',
					title: '姓名',
					align: 'center'
				},
				{
					field: 'studentId',
					title: '学号',
					align: 'center'
				}, {

					field: 'account',
					title: '账户名',
					align: 'center'
				},
				{
					field: 'specialtity',
					title: '专业',
					align: 'center'
				},
				{
					field: 'name',
					title: '班级',
					align: 'center'
				}, {
					field: 'phone',
					title: '手机号',
					align: 'center'
				}, {
					field: 'subject',
					title: '操作',
					width: 250,
					align: 'center',
					toolbar: '#operation'
				}
			]
		],
		skin: 'line',
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
	//查看成绩
	table.on('row(demo)', function(data) {
		var param = data.data;
		$(document).on('click', '.toStudentScore', function() {
			location.href = 'StudentScore.html?id=' + param.id;
		})
	})
})