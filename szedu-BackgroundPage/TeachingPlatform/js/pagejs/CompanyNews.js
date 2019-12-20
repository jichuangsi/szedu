layui.use(['form', 'table', 'layedit', 'laydate'], function() {
	var form = layui.form,
		laydate = layui.laydate,
		table = layui.table;
	var layedit = layui.layedit;
	$('.time').each(function() {
		laydate.render({
			elem: this,
			type: 'date'
		});
	})
	var index = layedit.build('demo2', {
		tool: [
			'strong' //加粗
			, 'italic' //斜体
			, 'underline' //下划线
			, 'del' //删除线
			, '|' //分割线
			, 'left' //左对齐
			, 'center' //居中对齐
			, 'right' //右对齐
			, 'link' //超链接
			, 'unlink' //清除链接
			, 'face' //表情
			//,'help' //帮助
		]
	});
	table.render({
		elem: '#demo',
		method: "get",
		async: false,
		url: httpUrl() + '/BackInformation/getAllArticle',
		headers: {
			'accessToken': getToken()
		},
		cols: [
			[{
					field: 'id',
					title: '序号',
					type: 'numbers'
				}, {
					field: 'articleTitle',
					title: '标题',
					align: 'center'
				}, {
					field: 'authorName',
					title: '作者',
					align: 'center'
				},
				{
					field: 'category',
					title: '文章分类',
					align: 'center'
				},
				{
					field: 'publishTime',
					title: '日期',
					sort: true,
					align: 'center',
					templet: function(d) {
						if(d.publishTime != 0) {
							return new Date(+new Date(d.publishTime) + 8 * 3600 * 1000).toISOString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '')
						} else {
							return "-"
						}
					}
				},
				{
					field: 'content',
					title: '内容',
					align: 'center'
					//					toolbar: '#xq'
				},
				{
					field: 'certification',
					title: '操作',
					align: 'center',
					toolbar: '#Mnews'
				}
			]
		],
		//		toolbar: '#operation',
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
	table.on('row(demo)', function(data) {
		var param = data.data;
		form.val('Exam', {
			"id": param.id,
			"articleTitle": param.articleTitle,
			"publishTime": param.publishTime,
			"authorName": param.authorName,
			"content":param.content
		});
		getLabel(param.categoryId);
		$(document).on('click', '#delNew', function() {
			DelExam(param.id);
		})
//		setValue(param.content)
	});

	function setValue(value) {
		layedit.setContent(index, value);
	}
	//修改
	form.on('submit(formModifyDemo)', function(data) {
		var param = data.field;
		var url = "/BackInformation/updateArticle";
		param.content = layedit.getContent(index);
		var category;
		$('select[name=categoryId] option:selected').each(function() {
			category = $(this).text()
		})
		param.category = category;
		ajaxPOST(url, param);
		table.reload('demo');
		layer.close(index);
		return false;
	});

	//删除信息
	function DelExam(id) {
		layer.confirm('确认要删除吗？', function(index) {
			var url = "/BackInformation/deleteArticle?id=" + id;
			ajaxPOST(url)
			table.reload('demo');
			layer.close(index);
		})
	}

	getLabel();

	function getLabel(id) {
		$('#teacher').empty();
		var options = '<option value="-1" selected="selected">' + "请选择" + '</option>';
		var arr = [];
		$.ajax({
			type: "get",
			url: httpUrl() + "/BackInformation/getAllArticleCategory",
			async: false,
			headers: {
				'accessToken': getToken()
			},
			success: function(res) {
				if(res.code == '0010') {
					arr = res.data
					if(arr == null || arr == undefined) {
						options = '<option value="" selected="selected">暂无信息请先去添加</option>'
					} else {
						for(var i = 0; i < arr.length; i++) {
							options += '<option value="' + arr[i].id + '" >' + arr[i].categoryName + '</option>'
						}
					}
				} else if(res.code == '0031') {
					layui.notice.info("提示信息：权限不足");
				} else if(res.code == '0050') {
					layui.notice.error("提示信息:错误!");
				}
			}
		});
		$('#teacher').append(options);
		if(id != undefined) {
			$("#teacher option[value=" + id + "]").prop("selected", true);
		}
		form.render('select');
	}
})