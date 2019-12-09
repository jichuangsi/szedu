layui.use(['form', 'table'], function() {
	var form = layui.form,
		table = layui.table;

	table.render({
		elem: '#demo',
		method: "get",
		async: false,
		url: httpUrl() + '/BackInformation/getAllArticleCategory',
		headers: {
			'accessToken': getToken()
		},
		cols: [
			[{
					field: 'id',
					title: '序号',
					type: 'numbers'
				}, {
					field: 'categoryName',
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
		loading: true,
		parseData: function(res) {
			var arr;
			var code;
			var total = 0;
			if(res.code == "0010") {
				arr = res.data;
				code = 0;
			}
			return {
				"code": code,
				"msg": res.msg,
				"data": arr
			};
		}
	});
	table.on('row(demo)', function(data) {
		var param = data.data;
		form.val('cate', {
			"id": param.id,
			"categoryName": param.categoryName
		});
		$(document).on('click', '#delCate', function() {
			DelCate(param.id);
		})

	});
	//添加课堂信息
	form.on('submit(formAddDemo)', function(data) {
		var param = data.field;
		var url = "/BackInformation/saveArticleCategory";
		ajaxPOST(url, param);
		table.reload('demo');
	});
	//修改信息
	form.on('submit(formModifyDemo)', function(data) {
		var param = data.field;
		var url = "/BackInformation/updateArticleCategory";
		ajaxPOST(url, param);
		table.reload('demo');
		layer.close(index);
		return false;
	});
	//删除课堂
	function DelCate(id) {
		layer.confirm('确认要删除吗？', function(index) {
			var url = "/BackInformation/deleteArticleCategory?id=" + id;
			ajaxPOST(url)
			table.reload('demo');
			layer.close(index);
		})
	}
})