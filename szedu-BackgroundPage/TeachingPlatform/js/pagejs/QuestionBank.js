layui.use(['form', 'table', 'element', 'upload'], function() {
	var form = layui.form,
		element = layui.element,
		upload = layui.upload,
		table = layui.table;

	table.render({
		elem: '#demo',
		method: "get",
		async: false,
		url: '../json/Bank.json',
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
					field: 'name',
					title: '试题名称',
					align: 'center'
				}, {
					field: 'ck',
					title: '关联课程',
					align: 'center'
				},
				{
					field: 'lx',
					title: '类型',
					align: 'center'
				}, {
					field: 'dq',
					title: '地区',
					align: 'center'
				},
				{
					field: 'jf',
					title: '积分',
					align: 'center'
				},
				{
					field: 'sc',
					title: '查看标签',
					align: 'center',
					toolbar: '#See'
				},
				{
					field: 'sc',
					title: '下载文件',
					align: 'center',
					toolbar: '#download'
				},
				{
					field: 'certification',
					title: '操作',
					align: 'center',
					toolbar: '#operation'
				}
			]
		],
		toolbar: '#AddBank',
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

	//监听选项个数单选框
	form.on('radio(sex)', function(data) {
		var param = data.value; //获取监听对象的value;
		var content = '';
		var answerContent = '';
		$('#xx').empty();
		$('#answer').empty();
		for(var i = 0; i < param; i++) {
			content += '<div class="layui-form-item">';
			content += '<label class="layui-form-label">选项' + String.fromCharCode(i + 65) + ':</label>';
			content += '<div class="layui-input-block">';
			content += '<input type="text" name="' + String.fromCharCode(i + 65) + '" required lay-verify="required" placeholder="' + String.fromCharCode(i + 65) + '" class="layui-input">';
			content += '</div>';
			content += '</div>';
		}

		for(var i = 0; i < param; i++) {
			answerContent += '<input type="radio" name="q" value="' + i + '" title="' + String.fromCharCode(i + 65) + '">';
		}
		$('#xx').append(content);
		$('#answer').append(answerContent);
		form.render('radio');
	});

	//监听复选框

	form.on('radio(sex2)', function(data) {
		var param = data.value; //获取监听对象的value;
		var content = '';
		var answerContent = '';
		$('#dx').empty();
		$('#answerChecx').empty();
		for(var i = 0; i < param; i++) {
			content += '<div class="layui-form-item">';
			content += '<label class="layui-form-label">选项' + String.fromCharCode(i + 65) + ':</label>';
			content += '<div class="layui-input-block">';
			content += '<input type="text" name="' + String.fromCharCode(i + 65) + '" required lay-verify="required" placeholder="' + String.fromCharCode(i + 65) + '" class="layui-input">';
			content += '</div>';
			content += '</div>';
		}
		$('#dx').append(content);
		for(var i = 0; i < param; i++) {
			answerContent += '<input type="checkbox" name="q2" value="' + i + '" title="' + String.fromCharCode(i + 65) + '">';
		}
		$('#answerChecx').append(answerContent);
		form.render('checkbox');
	});
})