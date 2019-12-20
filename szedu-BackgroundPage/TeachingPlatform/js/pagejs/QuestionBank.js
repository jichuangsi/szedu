layui.use(['form', 'table', 'element', 'upload'], function() {
	var form = layui.form,
		element = layui.element,
		upload = layui.upload,
		table = layui.table;

	table.render({
		elem: '#demo',
		method: "get",
		async: false,
		url: httpUrl() + '/BackQuestions/getAllQuestions',
		headers: {
			'accessToken': getToken()
		},
		cols: [
			[{
					field: 'id',
					title: '序号',
					type: 'numbers'
				}, {
					field: 'content',
					title: '试题名称',
					align: 'center'
				}, {
					field: 'subject',
					title: '关联课程',
					align: 'center'
				},
				{
					field: 'type',
					title: '类型',
					align: 'center'
				},
				{
					field: 'jf',
					title: '积分',
					align: 'center'
				},
				{
					field: 'sc',
					title: '查看图片',
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
		getPicture(param.id);
		$(document).on('click', '#delQues', function() {
			delQuestion(param.id);
		});
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
			answerContent += '<input type="radio" name="answer" value="' + String.fromCharCode(i + 65) + '" title="' + String.fromCharCode(i + 65) + '">';
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
			answerContent += '<input type="checkbox" name="answer" value="' + String.fromCharCode(i + 65) + '" title="' + String.fromCharCode(i + 65) + '">';
		}
		$('#answerChecx').append(answerContent);
		form.render('checkbox');
	});

	//题目提交
	var option = ['A', 'B', 'C', 'D', 'E', 'F'];
	//单选题
	form.on('submit(formDemoSingle)', function(data) {
		var param = data.field;
		if($("input:radio[name=option]:checked").val() == undefined) {
			setMsg("请选择选项个数", 7);
			return false;
		}
		if($("input:radio[name=answer]:checked").val() == undefined) {
			setMsg("请选择答案", 7);
			return false;
		}
		if(param.subjectId==-1){
			setMsg("请选择科目", 7);
			return false;
		}
		var arr = option.slice(0, param.option);
		var options = {
			id: 0,
			questionId: 0
		};
		param.subject=$('select[name=subjectId] option:selected').text();
		for(var i = 0; i < arr.length; i++) {
			options["" + String.fromCharCode(i + 65).toLowerCase() + ""] = param["" + String.fromCharCode(i + 65) + ""]
		}
		//获取知识点
		var knowledges = [];
		var obj = param.select.split(',');
		for(var i = 0; i < obj.length; i++) {
			knowledges.push({
				knowledgesId: obj[i]
			})
		}
		param.options = options;
		param.knowledges = knowledges;
		param.type = "单选题";
		var url = "/BackQuestions/saveSelfQuestion";
		ajaxPOST(url, param);
		table.reload('demo');
		layer.close(index);
		return false;
	});
	//多选题
	form.on('submit(formDemoMultiple)', function(data) {
		var param = data.field;
		if($("input:radio[name=option]:checked").val() == undefined) {
			setMsg("请选择选项个数", 7);
			return false;
		}
		if($("input:checkbox[name=answer]:checked").val() == undefined) {
			setMsg("请选择答案", 7);
			return false;
		}
		if(param.subjectId==-1){
			setMsg("请选择科目", 7);
			return false;
		}
		var answerList = []
		var obj = $('input:checkbox[name="answer"]:checked').text();
		for(var i = 0; i < obj.length; i++) {
			answerList.push(obj[i].value);
		}
		var arr = option.slice(0, param.option);
		var options = {
			id: 0,
			questionId: 0
		};
		for(var i = 0; i < arr.length; i++) {
			options["" + String.fromCharCode(i + 65).toLowerCase() + ""] = param["" + String.fromCharCode(i + 65) + ""]
		}
		//获取科目
		param.subject=$('select[name=subjectId] option:selected');
		//获取知识点
		var knowledges = [];
		var obj = param.select.split(',');
		for(var i = 0; i < obj.length; i++) {
			knowledges.push({
				knowledgesId: obj[i]
			})
		}
		param.knowledges = knowledges;
		param.options = options; //答案集
		param.answerList = answerList; //正确答案集合
		var url = "/BackQuestions/saveSelfQuestion";
		param.type = "多选题";
		console.log(param)
		ajaxPOST(url, param);
		table.reload('demo');
		layer.close(index);
		return false;
	});
	//判断题
	form.on('submit(formDemoJudgement)', function(data) {
		var param = data.field;
		var url = "/BackQuestions/saveSelfQuestion";
		param.type = "判断题";
		//获取知识点
		var knowledges = [];
		var obj = param.select.split(',');
		for(var i = 0; i < obj.length; i++) {
			knowledges.push({
				knowledgesId: obj[i]
			})
		}
		param.knowledges = knowledges;
		ajaxPOST(url, param);
		table.reload('demo');
		layer.close(index);
		return false;
	});
	//获取科目或者说标签

	function getSubject() {
		$('.subject').empty();
		$('.judgeSubject').empty();
		$('.multipleChoice').empty();
		$('.multipleSelect').empty();
		var options = '<option value="-1" selected="selected">' + "请选择" + '</option>';
		var arr = [];
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
					arr = res.data
					if(arr == null || arr == undefined) {
						options = '<option value="">暂无标签信息请先去添加标签</option>'
					} else {
						for(var i = 0; i < arr.length; i++) {
							if(arr[i].pid == 0) {
								options += '<option value="' + arr[i].id + '" >' + arr[i].title + '</option>'
							}

						}
					}
				} else if(res.code == '0031') {
					layui.notice.info("提示信息：权限不足");
				} else if(res.code == '0050') {
					layui.notice.error("提示信息:错误!");
				}
			}
		});
		  $("select[name=subjectId]").append(options);
		form.render('select');
		updateName(arr)
		return arr;
	}
	var demo1 = xmSelect.render({
		el: '.multipleSelect1',
		data: getSubject()
	})

	var demo2 = xmSelect.render({
		el: '.multipleSelect2',
		data: getSubject()
	})
	var demo3 = xmSelect.render({
		el: '.multipleSelect3',
		data: getSubject()
	})

	function updateName(array) {
		var keyMap = {
			title: 'name',
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

	//上传图片
	upload.render({
		elem: '.load',
		url: httpUrl() + '/BackQuestions/uploadQuestionImg',
		accept: 'file',
		method: 'POST',
		size: 1024 * 10,
		done: function(res) {
			if(res.code == "0010") {
				setMsg('图片上传成功!', 6)
				form.val('singleChoice', {
					"id": res.data
				});
				form.val('multipleChoice', {
					"id": res.data
				});
				form.val('judge', {
					"id": res.data
				});
			} else {
				setMsg('图片上传失败!', 5)
			}
		},
		error: function() {
			setMsg('图片上传失败!', 5)
		}
	});
	//根据图片id获取图片
	function getPicture(id) {
		$('#pictureView').empty();
		var content = '';
		var url = '/BackQuestions/getQuestionImg?questionId=' + id
		$.ajax({
			type: "post",
			url: httpUrl() + url,
			async: false,
			headers: {
				'accessToken': getToken(),
				'content-type': 'application/x-www-form-urlencoded',
			},
			contentType: 'application/json',
			success: function(res) {
				if(res.code == '0010') {
					data = res.data;
					if(data==null){
						content='<h1>该题暂未上传图片</h1>'
					}else{
						content += '<img src="data:image/jpeg;base64,' + data + '" />'
					}
						$('#pictureView').append(content)
				} else if(res.code == '0031') {
					layui.notice.info("提示信息：权限不足");
				} else if(res.code == '0050') {
					layui.notice.error("提示信息:错误!");
				}
			}
		});
	}

	//删除试题	
	function delQuestion(id) {
		layer.confirm('确认要删除吗？', function(index) {
			var url = "/BackQuestions/deleteSelfQuestion?questionId=" + id;
			var param = [];
			ajaxPOST(url, param);
			table.reload('demo');
			layer.close(index);
		})

	}
})