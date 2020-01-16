layui.use(['form', 'table', 'laydate', 'upload'], function() {
	var form = layui.form,
		upload = layui.upload,
		laydate = layui.laydate,
		table = layui.table;
	$('.time').each(function() {
		laydate.render({
			elem: this,
			type: 'datetime'
		});
	})
	var user = JSON.parse(sessionStorage.getItem('user'))
	$('.teacherName').html(user.teacherName);
	//新建课堂
	form.on('submit(formDemo)', function(data) {
		var param = data.field;
		console.log(param);
		if(param.select == '') {
			return setMsg('请选择上课班级', 7)
		}
		if(param.subjectId != -1) {
			if(number != 0 && param.chapter == -1) {
				return setMsg("请选择章节！", 7)
			}
		} else {
			return setMsg("请选择科目！", 7)
		}
		var arr = demo1.getValue();
		var classModelList=[]
		for(var i = 0; i < arr.length; i++) {
			classModelList.push({
				classId:arr[i].value,
				className:arr[i].name,
				schoolId:arr[i].schoolId,
				schoolName:arr[i].schoolName
			});
		}      
		param.courseWares=[];
		param.pushcourseWares=[]
		param.teachTime=new Date(param.teachTime).getTime();
		param.classModelList = classModelList;
		param.teacherId = user.teacherId
		param.teacherName = user.teacherName;
		var url ='/TeacherLesson/addLesson';
		ajaxPOST(url,param);
	});
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
	//获取班级
	var demo1 = xmSelect.render({
		el: '.ClassOption',
		data: getClass()
	})

	function getClass() {
		var arr;
		$.ajax({
			type: "post",
			url: httpUrl() + "/classConsole/getAllClass",
			async: false,
			headers: {
				'accessToken': getToken()
			},
			success: function(res) {
				console.log(res)
				if(res.code == '0010') {
					arr = res.data;
					updateName(arr);
				} else {
					return setMsg("班级信息查询异常！", 7)
				}
			}
		});
		return arr;
	}
	//更改名称，
	function updateName(array) {
		var keyMap = {
			className: 'name',
			id: 'value'
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
							options += '<option value="' + arr[i].id + '" >' + arr[i].title + '</option>'
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
	//多文件列表示例
	var demoListView = $('#demoList'),
		uploadListIns = upload.render({
			elem: '#testList',
			url: '/upload/',
			accept: 'file',
			multiple: true,
			auto: false,
			bindAction: '#testListAction',
			choose: function(obj) {
				var files = this.files = obj.pushFile(); //将每次选择的文件追加到文件队列
				//读取本地文件
				obj.preview(function(index, file, result) {
					var tr = $(['<tr id="upload-' + index + '">', '<td>' + file.name + '</td>', '<td>' + (file.size / 1014).toFixed(1) + 'kb</td>', '<td>等待上传</td>', '<td>', '<button class="layui-btn layui-btn-xs demo-reload layui-hide">重传</button>', '<button class="layui-btn layui-btn-xs layui-btn-danger demo-delete">删除</button>', '</td>', '</tr>'].join(''));

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
				if(res.code == 0) { //上传成功
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
})