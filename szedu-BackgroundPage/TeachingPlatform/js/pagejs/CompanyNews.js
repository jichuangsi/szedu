layui.use(['form', 'table', 'layedit', 'laydate'], function() {
	var form = layui.form,
		layedit = layui.layedit,
		laydate = layui.laydate,
		table = layui.table;

	$('.time').each(function() {
		laydate.render({
			elem: this,
			type: 'date'
		});
	})
	layui.use('layedit', function() {
		var layedit = layui.layedit;
		layedit.build('demo2'); //建立编辑器
	});

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
					field: 'title',
					title: '标题',
					align: 'center'
				}, {
					field: 'account',
					title: '作者',
					align: 'center'
				},
				{
					field: 'fl',
					title: '文章分类',
					align: 'center'
				},
				{
					field: 'time',
					title: '日期',
					sort: true,
					align: 'center'
				},
				{
					field: 'd',
					title: '描述',
					align: 'center'
				},

				{
					field: 'account',
					title: '详情',
					align: 'center',
					toolbar: '#xq'
				},
				{
					field: 'certification',
					title: '操作',
					align: 'center',
					toolbar: '#Mnews'
				}
			]
		],
		toolbar: '#operation',
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