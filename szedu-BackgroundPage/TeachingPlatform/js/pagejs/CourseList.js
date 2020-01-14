layui.use(['form', 'table', 'upload', 'xmSelect'], function() {
	var form = layui.form,
		xmSelect = layui.xmSelect,
		upload = layui.upload,
		table = layui.table;
	//获取关联标签列表
	function getLabel() {
		$('#subject').empty();
		var str ='';
			str='<option value"-1">请选择</option>'
		var label = []
		$.ajax({
			type: "get",
			url: httpUrl() + "/TreeMenu/getTreeMenuByPid?pid=0", //获取科目
			async: false,
			headers: {
				'accessToken': getToken()
			},
			contentType: 'application/json',
			success: function(res) {
				if (res.code == '0010') {
					var arr = res.data;
					$.each(arr,function(index,item){
						str+='<option value="'+item.id+'">'+item.title+'</option>';
					});
					$('#subject').append(str);
					form.render();
				}
			}
		});
		return label;
	}

	form.on('submit(formCurriculum)', function(data) {
		var param = data.field;
		var url='/BackCurriculum/saveCurriculum';
		if(param.subjectId==-1){
			return layer.msg("请选择科目");
		}
		ajaxPOST(url, param);
	});
	//
	form.on('submit(formDemo)', function(data) {
		var param = data.field;
		var curriculumKnowledges = [];
		var obj = param.select.split(',');
		for (var i = 0; i < obj.length; i++) {
			curriculumKnowledges.push({
				knowledgesId: obj[i]
			})
		}
		param.curriculumKnowledges = curriculumKnowledges;
		var url = '/BackCurriculum/saveCurriculum';
		ajaxPOST(url, param);
	});
	var demo1 = xmSelect.render({
		el: '.multipleSelect0',
		data: getLabel()
	})
	element.on('tab(docDemoTabBrief)', function(data) {
		var str = 'multipleSelect' + data.index
		xmSelect.render({
			el: '.' + str + '',
			data: getLabel()
		})
	});

	//图片上传
	upload.render({
		elem: '.imgLoad',
		url: httpUrl() + '/BackCurriculum/uploadQuestionImg',
		headers: {
			'accessToken': getToken()
		},
		before: function(obj) {
			//预读本地文件示例，不支持ie8
			obj.preview(function(index, file, result) {
				$('.demo1').attr('src', result); //图片链接（base64）
			});
		},
		size: 1024 * 5,
		done: function(res) {
			//如果上传失败
			if (res.code == '0050') {
				return layer.msg('上传失败');
			} else {
				setMsg('图片上传成功!', 6);
				var str = $('.layui-field-box').find('ul').find('.layui-this').html();
				form.val('curriculum', {
					"id": res.data
				});
			}
		},
		error: function() {
			//演示失败状态，并实现重传
			var demoText = $('#demoText');
			demoText.html('<span style="color: #FF5722;">上传失败</span> <a class="layui-btn layui-btn-xs demo-reload">重试</a>');
			demoText.find('.demo-reload').on('click', function() {
				uploadInst.upload();
			});
		}
	});

})
