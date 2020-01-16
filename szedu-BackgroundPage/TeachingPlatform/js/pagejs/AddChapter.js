	var resourceIds=[];
layui.use(['form', 'table', 'upload'], function() {
	var form = layui.form,
		upload = layui.upload,
		table = layui.table;


	window.openUpload = function(type) {
		var extsType;
		var size = 10 * 1024;
		if (type == "PPT资源") {
			console.log(1)

			extsType = 'ppt';
			size = 100 * 1024;
		} else if (type == "实训资源") {
			console.log(2)
			extsType = 'doc|pdf|xls'
			size = 100 * 1024;
		} else if (type == "视频资源") {
			console.log(3)
			size = 300 * 1024;
			extsType = 'mp4|mpeg|mpg|rmvb|avi|flv|dmv|amv'
		} else if (type == "图片资源") {
			console.log(4)
			extsType = 'jpg|png|gif|bmp|jpeg';
			size = 5 * 1024;
		}
		index = layer.open({
			type: 1,
			area: ['80%', '500px'],
			anim: 2,
			title: '上传资源',
			maxmin: true,
			shadeClose: true, //点击遮罩关闭
			content: $('#uploadFile')
		});
	
		var demoListView = $('#demoList'),
			uploadListIns = upload.render({
				elem: '#testList',
				url: httpUrl() + '/BackCurriculum/localUploadCurriculumResources',
				headers: {
					'accessToken': getToken()
				},
				accept: 'file',
				exts: extsType,
				multiple: true,
				auto: false,
				bindAction: '#testListAction',
				choose: function(obj) {
					var files = this.files = obj.pushFile(); //将每次选择的文件追加到文件队列
					//读取本地文件
					obj.preview(function(index, file, result) {
						var tr = $(['<tr id="upload-' + index + '">', '<td>' + file.name + '</td>', '<td>' + (file.size / 1014).toFixed(
								1) + 'kb</td>', '<td>等待上传</td>', '<td>',
							'<button class="layui-btn layui-btn-xs demo-reload layui-hide">重传</button>',
							'<button class="layui-btn layui-btn-xs layui-btn-danger demo-delete">删除</button>', '</td>', '</tr>'
						].join(''));

						//单个重传
						tr.find('.demo-reload').on('click', function() {
							obj.upload(index, file);
						});

						//删除
						tr.find('.demo-delete').on('click', function() {
							delete files[index]; //删除对应的文件
							tr.remove();
							uploadListIns.config.elem.next()[0].value = ''; //清空 input file 值，以免删除后出现同名文件不可选
						});

						demoListView.append(tr);
					});
				},
				done: function(res, index, upload) {
					if (res.code == '0010') { //上传成功
					resourceIds.push(res.data[0])
						var tr = demoListView.find('tr#upload-' + index),
							tds = tr.children();
						tds.eq(2).html('<span style="color: #5FB878;">上传成功</span>');
						tds.eq(3).html(''); //清空操作
						return delete this.files[index]; //删除文件队列已经上传成功的文件
					}
					this.error(index, upload);
				},
				error: function(index, upload) {
					var tr = demoListView.find('tr#upload-' + index),
						tds = tr.children();
					tds.eq(2).html('<span style="color: #FF5722;">上传失败</span>');
					tds.eq(3).find('.demo-reload').removeClass('layui-hide'); //显示重传
				}
			});
	}
	getAllCouresList();
	//获取全部课程
	function getAllCouresList() {
		var url = '/BackCurriculum/getAllCurriculum';
		var arr = getAjaxPostData(url);
		console.log(arr);
		var str = '';
		$('#coures').empty();
		str += '<option value="-1">请选择</option>';
		$.each(arr.data, function(index, item) {
			str += '<option value="' + item.subjectId + '-'+item.id+'">' + item.curriculumName + '</option>';
		});
		$('#coures').append(str);
		form.render();
	}
	//根据选择的课程来获取章节

	form.on('select(coures)', function(data) {
		var param=data.value;
		console.log(isNaN(param))
		var ids=param.split('-');
		var url = '/TreeMenu/getTreeMenuByPid?pid=' + ids[0];
		var arr = getAjaxData(url);
		console.log(arr);
		$('#chapter').empty();
		var str = '';
		$.each(arr.data, function(index, item) {
			str += '<option value="' + item.id + '">' + item.title + '</option>';
		});
		$('#chapter').append(str);
		form.render();
	});
	//提交
	form.on('submit(formDemo)',function(data) {
		var param = data.field;
		var url = '/BackCurriculum/saveCurriculumResourceChapters';
		if(param.chapterId==-1){
			return layer.msg('请选择章节!');
		}
		var ids=param.subjectId.split('-');
		param.subjectId=ids[0];
		param.curriculumId=ids[1];
		param.resourceIds=resourceIds;
		console.log(param)
		ajaxPOST(url, param);
	});
	
});
