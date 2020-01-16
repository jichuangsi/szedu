layui.use(['form', 'table', 'laydate', 'xmSelect'], function() {
	var form = layui.form,
		xmSelect = layui.xmSelect,
		laydate = layui.laydate,
		table = layui.table;

	var user = JSON.parse(sessionStorage.getItem('user'))
	$('#name').append(user.teacherName)
	$('.time').each(function() {
		laydate.render({
			elem: this,
			type: 'datetime'
		});
	})
	//新建试卷
	form.on('submit(formDemo)', function(data) {
		var param = data.field;
		if(param.term == -1) {
			return setMsg('请选择学期！', 7);
		}
		if(param.subjectId == -1) {
			return setMsg('请选择科目', 7);
		}
		if(param.select == '') {
			return setMsg('请选择考试班级', 7)
		}
		var models = [];
		if(param.write == 1) {
			if(param.m == "") {
				param.m = "0"
			}
			if(parseInt(param.m) > 5) {
				return setMsg('提前阅卷不能超过5分钟！', 7)
			}
			if(param.time == '') {
				return setMsg("请选择考试时间", 7)
			}
			//把日期获取的放到数组
			models.push({
				timeLength: param.minute1,
				tiqian: param.m,
				id: param.write,
				startTime: setTime(param.time),
				endTime: ''
			});
		} else {
			if(param.star == '') {
				return setMsg("请选择开始答题时间", 7)
			}
			if(param.end == '') {
				return setMsg("请选择结束答题时间", 7)
			}
			//把日期获取的放到数组
			models.push({
				timeLength: param.minute,
				tiqian: '',
				id: param.write,
				startTime: setTime(param.star),
				endTime: setTime(param.end)
			});
		}
		//选择时间，根据选择选项来判断获取那个数组
		//创建人 直接获取用户信息
		param.creatorId = user.teacherId;
		param.creatorName = user.teacherName;
		
		param.status=1;
		param.isOpenAnswer=1;
		param.models = models;
		param.subjectName = $('select[name=subjectId] option:selected').text();
		var classId = param.select.split(',')
		param.classId = classId;
		var examType = $("input:radio[name=examType]:checked").attr('title');
		param.examType = examType
		console.log(param)

		var url = '/backExam/saveExam';
		ajaxPOST(url, param);
	});

	function setTime(data) {
		let d = new Date(data);
		return d.getTime(d);
	}
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
	var demo1 = xmSelect.render({
		el: '.ClassOption',
		data: getClass()
	})

	var Semester;
	//获取班级，获取学期
	function getClass() {
		var arr;
		Semester = []
		$.ajax({
			type: "post",
			url: httpUrl()+"/classConsole/getAllClass",
			async: false,
			headers: {
				'accessToken': getToken()
			},
			success: function(res) {
				console.log(res)
				if(res.code == '0010') {
					arr = res.data;
					updateName(arr);
					for(var i = 0; i < arr.length; i++) {
						Semester.push(arr[i].ruTime); //获取各个班级的下拉框
					}
					setRutime(Semester);
				} else {
					return setMsg("班级信息查询异常！", 7)
				}
			}
		});
		return arr;
	}

	function setRutime(arr) {
		//把学期去重
		var data = Array.from(new Set(arr));
		//把数据放入下拉框
		$('#Semester').empty();
		var options = '<option value="-1" >' + "请选择" + '</option>';
		for(var i = 0; i < data.length; i++) {
			options += '<option value="' + data[i] + '" >' + data[i] + '</option>'
		}
		$('#Semester').append(options);
		form.render('select');
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
	
	//根据老师查询试卷
	function getTeacherExam(){
		var url="/backExam/getTestPaperByTeacherId";
		var arr=ajaxGetData(url);
		var str='';
		if(arr.length>0){
			str+='';
		}else{
			return setMsg('您暂未添加试卷，请先添加试卷在添加考试！',7);
		}
	}

	
})