layui.use(['form', 'table', 'laydate', 'layedit'], function() {
	var form = layui.form,
		laydate = layui.laydate,
		layedit = layui.layedit,
		table = layui.table;

	$('.time').each(function() {
		laydate.render({
			elem: this,
			type: 'date'
		});
	})
	//富文本图片上传接口
	layedit.set({
	  uploadImage: {
		url: httpUrl() + "/back/school/sendMessageImage", //接口url
		type: 'post', //默认post
		headers: {
			'accessToken': getToken()
		},
		size: 5*1024,
		acceptMime: 'images',
		accept: 'images'
	  }
	});
	var index = layedit.build('demo', {
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
			,'face' //表情
			//,'help' //帮助
		]
	});
	form.on('submit(formDemo)', function(data) {
		var param = data.field;
		//获取富文本的内容		
		var str = layedit.getContent(index);
		param.content=str;
		var category;
		$('select[name=categoryId] option:selected').each(function() {
			category= $(this).text()
		})
		param.category=category;
		var url='/BackInformation/saveArticle'
		ajaxPOST(url,param)
		return false;
	});
	//获取分类
	getLabel();
	function getLabel() {
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
		form.render('select');
	}
})