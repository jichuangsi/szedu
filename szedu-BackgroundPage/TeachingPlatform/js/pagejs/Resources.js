layui.use(['form', 'table'], function() {
	var form = layui.form,
		table = layui.table;

	var id = sessionStorage.getItem("teacherId");
	table.render({
		elem: '#demo',
		method: "get",
		async: false,
		url: httpUrl() + '/CourseWare/getCourseWareList/' + id,
		headers: {
			'accessToken': getToken()
		},
		cols: [
			[{
					field: 'id',
					title: '序号',
					type: 'numbers'
				}, {
					field: 'filename',
					title: '文件名称',
					align: 'center'
				}, {
					field: 'describes',
					title: '描述',
					align: 'center'
				},
				{
					field: 'isCheck',
					title: '审核状态',
					align: 'center',
					templet: function(d) {
						if(d.isCheck == 1) {
							return "待审核"
						} else if(d.isCheck == 2) {
							return "同意"
						}else if(d.isCheck == 3) {
							return "驳回"
						}
					}
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
			pageName: 'pageNum',
			limitName: "pageSize"
		},
		parseData: function(res) {
			var arr;
			var code = 0;
			var total = 0;
			if(res.code == "0010") {
				arr = res.data.content;
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
		$(document).on('click', '#del', function() {
			DelFile(param.id);
		});
		$(document).on('click', '#load', function() {
			loadFile(param.id);
		});
	});

	function DelFile(id) {
		layer.confirm('确认要删除吗？', function(index) {
			var url="/CourseWare/deleteAttachment?fileId="+id
			ajaxPOST(url)
			table.reload('demo');
			layer.close(index);
		})
	}

	function loadFile(id) {
		var a = document.createElement('a');
		var filename = '下载课件模板.ppt';
		a.href = httpUrl() + '/CourseWare/downLoadAttachment?fileId=' + id;
		a.download = filename;
		a.click();
		//		a.html('下载');
		window.URL.revokeObjectURL(url);
		$('#load').append(a)
	}
})