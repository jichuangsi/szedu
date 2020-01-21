layui.use(['form', 'table', 'upload'], function() {
	var form = layui.form,
		upload = layui.upload,
		table = layui.table;
	var user = getUser();
	getSubject();
	//获取科目
	function getSubject() {
		$('#subject').empty();
		var options = '<option value="-1" selected="selected">' + "请选择" + '</option>';
		$.ajax({
			type: "get",
			url: httpUrl() + "/TreeMenu/getAllTreeMenu",
			async: true,
			headers: {
				'accessToken': getToken()
			},
			success: function(res) {
				if(res.code == '0010') {
					arr = res.data
					if(arr == null || arr == undefined) {
						options += '<option value="">暂无科目信息</option>'
					} else {
						for(var i = 0; i < arr.length; i++) {
							if(arr[i].pid == 0) {
								options += '<option value="' + arr[i].id + '" >' + arr[i].title + '</option>'
							}
						}
					}
					$('#subject').append(options);
					form.render('select');
				} else {
					layui.notice.error("提示信息:获取科目信息错误!");
				}
			}
		});
	}
	var number = 0;
	//根据选择的科目获取章节
	form.on('select(pid)', function(data) {
		var param = data.value;
		$('#chapter').empty();
		var options = '<option value="-1" selected="selected">' + "请选择" + '</option>';
		$.ajax({
			type: "get",
			url: httpUrl() + "/TreeMenu/getTreeMenuByPid?pid=" + param,
			async: true,
			headers: {
				'accessToken': getToken()
			},
			success: function(res) {
				if(res.code == '0010') {
					arr = res.data
					number = arr.length;
					if(arr == null || arr == undefined || arr.length == 0) {
						options += '<option value="0">暂无章节信息</option>'
					} else {
						for(var i = 0; i < arr.length; i++) {
							options += '<option value="' + arr[i].pid + '" >' + arr[i].title + '</option>'
						}
					}
					$('#chapter').append(options);
					form.render('select');
				} else {
					layui.notice.error("提示信息:获取章节信息错误!");
				}
			}
		});
		return number
	})
	//普通图片上传 单选题主图
	var uploadInst = upload.render({
		elem: '#demo1',
		url: httpUrl() + '/BackQuestions/uploadQuestionContentImg',
		headers: {
			'accessToken': getToken()
		},
		before: function(obj) {
			console.log(obj)
			//预读本地文件示例，不支持ie8
			obj.preview(function(index, file, result) {
				$('#demo1').attr('src', result); //图片链接（base64）
			});
		},
		done: function(res) {
			//如果上传失败
			if(res.code == '0010') {
				form.val('Single', {
					titlePic: res.data
				});
				return layer.msg('上传成功！');
			} else {
				return layer.msg('上传失败');
				//演示失败状态，并实现重传
				var demoText = $('#demoText');
				demoText.html('<span style="color: #FF5722;">失败</span> <a class="layui-btn layui-btn-xs demo-reload">重试</a>');
				demoText.find('.demo-reload').on('click', function() {
					uploadInst.upload();
				});
			}
			//上传成功
		}
	});
	var qId='';
	//添加试题-单选题
	form.on('submit(formDemo)', function(data) {
		var param = data.field;
		
		param.type = '单选题';
		param.teacherId = user.teacherId
		param.teacherName = user.teacherName;
		//判断是否选择科目章节
		if(param.subjectId != -1) {
			if(number != 0 && param.chapter == -1) {
				return setMsg("请选择章节！", 7)
			}
		} else {
			return setMsg("请选择科目！", 7)
		}
		/*判断选项，图片或者选项两个都没有选择进行提示*/
		if(param.title == "" && param.titlePic == "") {
			return setMsg("请选择输入题目或者上传题目图片！", 7)
		}
		if(param.a == "" && param.aoptionPic== "") {
			return setMsg("请选择输入选项A或者上传选项图片！", 7)
		}
		if(param.b == "" && param.boptionPic== "") {
			return setMsg("请选择输入选项B或者上传选项图片！", 7)
		}
		if(param.c == "" && param.coptionPic== "") {
			return setMsg("请选择输入选项C或者上传选项图片！", 7)
		}
		if(param.d == "" && param.doptionPic== "") {
			return setMsg("请选择输入选项D或者上传选项图片！", 7)
		}
		/*把选项放入对象*/
		var options = {
			"a": param.a,
			"aoptionPic": param.aoptionPic,
			"b": param.b,
			"boptionPic": param.boptionPic,
			"c": param.c,
			"coptionPic": param.coptionPic,
			"d": param.d,
			"doptionPic": param.doptionPic,
			"falseOption": "",
			"falseOptionPic": "",
			"foptionPic": "",
			"id":qId ,
			"questionId": 0,
			"tureOption": "",
			"tureOptionPic": ""
		};
		param.id=qId;
		param.options=options;
		
		var url='/BackQuestions/saveSelfQuestion';
		ajaxPOST(url,param)
	});
	
	//普通图片上传 单选题A
	var uploadInstA = upload.render({
		elem: '#demoA',
		url: httpUrl() + '/BackQuestions/uploadQuestionOptionImg',
		headers: {
			'accessToken': getToken()
		},
		before: function(obj) {
			console.log(obj)
			//预读本地文件示例，不支持ie8
			obj.preview(function(index, file, result) {
				$('#demoA').attr('src', result); //图片链接（base64）
			});
		},
		data: {
			options: function() {
				return "A";
			},
			questionId:function(){
				return qId
			}
		},
		done: function(res) {
			//如果上传失败
			if(res.code == '0010') {
				qId=res.data;
				form.val('Single', {
					aoptionPic: res.data
				});
				return layer.msg('上传成功！');
			} else {
				return layer.msg('上传失败');
				//演示失败状态，并实现重传
				var demoText = $('#demoTextA');
				demoText.html('<span style="color: #FF5722;">失败</span> <a class="layui-btn layui-btn-xs demo-reload">重试</a>');
				demoText.find('.demo-reload').on('click', function() {
					uploadInstA.upload();
				});
			}
			//上传成功
		}
	});
	//普通图片上传 单选题B
	var uploadInstB = upload.render({
		elem: '#demoB',
		url: httpUrl() + '/BackQuestions/uploadQuestionOptionImg',
		headers: {
			'accessToken': getToken()
		},
		before: function(obj) {
			console.log(obj)
			//预读本地文件示例，不支持ie8
			obj.preview(function(index, file, result) {
				$('#demoB').attr('src', result); //图片链接（base64）
			});
		},
		data: {
			options: function() {
				return "B";
			},
			questionId:function(){
				return qId
			}
		},
		done: function(res) {
			//如果上传失败
			if(res.code == '0010') {
				qId=res.data;
				form.val('Single', {
					boptionPic: res.data
				});
				return layer.msg('上传成功！');
			} else {
				return layer.msg('上传失败');
				//演示失败状态，并实现重传
				var demoText = $('#demoTextB');
				demoText.html('<span style="color: #FF5722;">失败</span> <a class="layui-btn layui-btn-xs demo-reload">重试</a>');
				demoText.find('.demo-reload').on('click', function() {
					uploadInstB.upload();
				});
			}
		}
	});
	//普通图片上传 单选题C
	var uploadInstC = upload.render({
		elem: '#demoC',
		url: httpUrl() + '/BackQuestions/uploadQuestionOptionImg',
		headers: {
			'accessToken': getToken()
		},
		before: function(obj) {
			console.log(obj)
			//预读本地文件示例，不支持ie8
			obj.preview(function(index, file, result) {
				$('#demoC').attr('src', result); //图片链接（base64）
			});
		},
		data: {
			options: function() {
				return "C";
			},
			questionId:function(){
				return qId
			}
		},
		done: function(res) {
			//如果上传失败
			if(res.code == '0010') {
				qId=res.data;
				form.val('Single', {
					coptionPic: res.data
				});
				return layer.msg('上传成功！');
			} else {
				return layer.msg('上传失败');
				//演示失败状态，并实现重传
				var demoText = $('#demoTextC');
				demoText.html('<span style="color: #FF5722;">失败</span> <a class="layui-btn layui-btn-xs demo-reload">重试</a>');
				demoText.find('.demo-reload').on('click', function() {
					uploadInstC.upload();
				});
			}
		}
	});
	//普通图片上传 单选题D
	var uploadInstD = upload.render({
		elem: '#demoD',
		url: httpUrl() + '/BackQuestions/uploadQuestionOptionImg',
		headers: {
			'accessToken': getToken()
		},
		before: function(obj) {
			console.log(obj)
			//预读本地文件示例，不支持ie8
			obj.preview(function(index, file, result) {
				$('#demoD').attr('src', result); //图片链接（base64）
			});
		},
		data: {
			options: function() {
				return "D";
			},
			questionId:function(){
				return qId
			}
		},
		done: function(res) {
			//如果上传失败
			if(res.code == '0010') {
				qId=res.data;
				form.val('Single', {
					doptionPic: res.data
				});
				return layer.msg('上传成功！');
			} else {
				return layer.msg('上传失败');
				//演示失败状态，并实现重传
				var demoText = $('#demoTextD');
				demoText.html('<span style="color: #FF5722;">失败</span> <a class="layui-btn layui-btn-xs demo-reload">重试</a>');
				demoText.find('.demo-reload').on('click', function() {
					uploadInstD.upload();
				});
			}
		}
	});

	
})