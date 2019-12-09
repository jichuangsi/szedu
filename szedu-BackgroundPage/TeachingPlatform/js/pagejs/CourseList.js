layui.use(['form', 'table', 'upload', 'xmSelect'], function() {
	var form = layui.form,
		xmSelect = layui.xmSelect,
		upload = layui.upload,
		table = layui.table;
	//获取关联标签列表
	function getLabel() {
		var label = []
		$.ajax({
			type: "get",
			url: httpUrl() + "/TreeMenu/getAllTreeMenu",
			async: false,
			headers: {
				'accessToken': getToken()
			},
			contentType: 'application/json',
			success: function(res) {
				if(res.code == '0010') {
					var arr = res.data;
					for(var i = 0; i < arr.length; i++) {
						label.push({
							name: arr[i].title,
							value: arr[i].id
						});
					}
				}
			}
		});
		return label;
	}

	function updateName(array) {
		var keyMap = {
			tname: 'name',
			type: 'children'
		}
		for(var i = 0; i < array.length; i++) {
			var obj = array[i];
			for(var key in obj) {
				var newKey = keyMap[key];
				if(newKey) {
					obj[newKey] = obj[key];
					delete obj[key];
				}
			}
		}
	}
	form.on('submit(formCurriculum)', function(data) {
		var param = data.field;
		var curriculumKnowledges = [];
		var obj = param.select.split(',');
		for(var i = 0; i < obj.length; i++) {
			curriculumKnowledges.push({
				knowledgesId: obj[i]
			})
		}
		param.curriculumKnowledges=curriculumKnowledges;
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
			if(res.code == '0050') {
				return layer.msg('上传失败');
			} else {
				setMsg('图片上传成功!', 6);
				var str = $('.layui-field-box').find('ul').find('.layui-this').html();
				if(str == '课程') {
					form.val('curriculum', {
						"id": res.data
					});
				} else if(str == '视频') {
					form.val('couresVideo', {
						"id": res.data
					});
				} else if(str == '实训') {
					form.val('training', {
						"id": res.data
					});
				} else if(str == '图片') {
					form.val('couresPic', {
						"id": res.data
					});
				}
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

	//*上传课程文件*/
	upload.render({
		elem: '.uploadFile',
		url: httpUrl() + '/BackCurriculum/uploadCurriculumResource',
		accept: 'file',
		size: 1024 * 10,
		done: function(res) {
			if(res.code == '0010') {
				setMsg('上传成功!', 6);
					form.val('curriculum', {
					"fullPath": res.data.fullPath,
					"filegroup": res.data.group,
					"filepath": res.data.path
				});
				form.val('training', {
					"fullPath": res.data.fullPath,
					"filegroup": res.data.group,
					"filepath": res.data.path
				});
				form.val('couresPic', {
					"fullPath": res.data.fullPath,
					"filegroup": res.data.group,
					"filepath": res.data.path
				});
			}
		}
	});
	//上传视频
	upload.render({
		elem: '.uploadVideo',
		url: httpUrl() + '/BackCurriculum/uploadCurriculumResource',
		accept: 'file',
		accept: 'audio',
		done: function(res) {
			if(res.code == '0010') {
				setMsg('上传成功!', 6);
				form.val('couresVideo', {
					"fullPath": res.data.fullPath,
					"group": res.data.group,
					"path": res.data.path
				});
			} else {
				setMsg('上传失败!', 5);
			}
		}
	});

})