layui.use(['form', 'table', 'element'], function() {
	var form = layui.form,
		element = layui.element,
		table = layui.table;
	//获取班级管理数据
	function getClassList() {
		table.render({
			elem: '#demo',
			method: "get",
			async: false,
			url: httpUrl() + '/classConsole/getClassByTeacherId',
			headers: {
				'accessToken': getToken(),
			},
			cols: [
				[{
						field: 'id',
						title: '序号',
						type: 'numbers'
					},
					{
						field: 'specialty',
						title: '专业',
						align: 'center'
					},
					{
						field: 'enrollTime',
						title: '入学年份',
						align: 'center'
					}, {

						field: 'educationalSystem',
						title: '学制',
						align: 'center'
					},
					{
						field: 'className',
						title: '班级',
						align: 'center'
					},
					{
						field: 'founder',
						title: '创建人',
						align: 'center'
					}, {
						field: 'student',
						title: '班级人数',
						align: 'center'
					}, {
						field: 'status',
						title: '状态',
						align: 'center',
						templet: function(d) {
							if(d.status == 'A') {
								return '使用中'
							} else {
								return '暂停使用'
							}
						}
					}, {
						field: 'subject',
						title: '操作',
						width: 300,
						align: 'center',
						toolbar: '#operation'
					}
				]
			],
			skin: 'line',
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
	}

	function getMyClass() {
		table.render({
			elem: '#myClass',
			method: "get",
			async: false,
			url: httpUrl() + '/classConsole/getClassByTeacherId',
			headers: {
				'accessToken': getToken(),
			},
			cols: [
				[{
						field: 'id',
						title: '序号',
						type: 'numbers'
					},
					{
						field: 'specialty',
						title: '专业',
						align: 'center'
					},
					{
						field: 'enrollTime',
						title: '入学年份',
						align: 'center'
					}, {

						field: 'educationalSystem',
						title: '学制',
						align: 'center'
					},
					{
						field: 'className',
						title: '班级',
						align: 'center'
					},
					{
						field: 'founder',
						title: '创建人',
						align: 'center'
					}, {
						field: 'student',
						title: '班级人数',
						align: 'center'
					}, {
						field: 'status',
						title: '状态',
						align: 'center',
						templet: function(d) {
							if(d.status == 'A') {
								return '使用中'
							} else {
								return '暂停使用'
							}
						}
					}, {
						field: 'subject',
						title: '操作',
						width: 300,
						align: 'center',
						toolbar: '#myStudent'
					}
				]
			],
			skin: 'line',
			page: true,
			limit: 10,
			request: {
				pageName: 'pageNum',
				limitName: "pageSize"
			},
			loading: true,
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
	}
	//考勤
	function getCheck() {
		table.render({
			elem: '#Check',
			method: "post",
			async: false,
			url: httpUrl() + '/classConsole/getAttendance',
			cols: [
				[{
						field: 'id',
						title: '序号',
						type: 'numbers'
					}, {
						field: 'name',
						title: '课堂名称',
						align: 'center'
					},
					{
						field: 'date',
						title: '日期',
						align: 'center'
					},
					{
						field: 'number',
						title: '实到人数',
						align: 'center'
					},
					{
						field: 'nonumber',
						title: '缺勤人数',
						align: 'center'
					},
					{
						field: 'noName',
						title: '缺考姓名',
						align: 'center',
						width: '30%'
					}
				]
			],
			skin: 'line',
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
	}
	getMyClass();
	element.on('tab(docDemoTabBrief)', function(data) {
		var str = data.index
		if(str == 0) {
			getMyClass();
			$('.one').addClass('xs');
			$('.one').removeClass('yc');
			$('.one').siblings().removeClass('xs');
			$('.one').siblings().addClass('yc');
		} else if(str == 1) {
			getClassList()
			$('.two').addClass('xs');
			$('.two').removeClass('yc');
			$('.two').siblings().removeClass('xs');
			$('.two').siblings().addClass('yc');
		} else if(str == 2) {
			getCheck();
			$('.three').addClass('xs');
			$('.three').removeClass('yc');
			$('.three').siblings().removeClass('xs');
			$('.three').siblings().addClass('yc');
		}
	});
	//创建班级
	form.on('submit(formAddDemo)', function(data) {
		var param = data.field;
		var url = '/classConsole/addClass';
		ajaxPOST(url, param);
		table.reload('demo');
		layer.close(index);
		return false;
	})
	getClass();

	function getClass() {
		var url = '/classConsole/getAddClass';
		$('#new').empty();
		var strContent = '';
		$.ajax({
			type: "post",
			url: httpUrl() + url,
			async: true,
			headers: {
				'accessToken': getToken(),
			},
			contentType: 'application/json',
			success: function(res) {
				if(res.code == '0010') {
					var data = res.data;
					for(var i = 0; i < data.length; i++) {
						if(data[i].exist) {
							strContent += '<div class="layui-input-block">';
							strContent += '<input type="checkbox" name="classId" value="' + data[i].classId + '" title="' + data[i].className + '" lay-skin="primary" disabled>';
							strContent += '</div>'
						} else {
							strContent += '<div class="layui-input-block">';
							strContent += '<input type="checkbox"name="classId" value="' + data[i].classId + '"  title="' + data[i].className + '" lay-skin="primary">';
							strContent += '</div>'
						}
					}
					strContent += '<div class="layui-form-item">';
					strContent += '<div class="layui-input-block">';
					strContent += '<div class="layui-btn layui-btn-normal site-demo-active" lay-submit lay-filter="formNewDemo">确认</div>';
					strContent += '<button type="reset" class="layui-btn layui-btn-primary">重置</button>';
					strContent += '</div>';
					strContent += '</div>';
					$('#new').append(strContent);
					form.render();
				} else if(res.code == '0031') {
					layui.notice.info("提示信息：权限不足");
				} else if(res.code == '0050') {
					layui.notice.error("提示信息:错误!");
				}
			}
		});
	}
	//新增班级
	form.on('submit(formNewDemo)', function(data) {
		var param = [];
		$('input[type=checkbox]:checked').each(function() {
			param.push($(this).val());
		});
		var url = '/classConsole/addTeacherClass';
		ajaxPOST(url, param);
		table.reload('myClass');
		getClass();
		layer.close(index);
	})
	table.on('row(myClass)', function(data) {
		var param = data.data;
		$(document).on('click', '.update', function() {
			if(param.status == "A") {
				param.status = "B"
			} else {
				param.status = "A"
			}
			updateClassStatus(param.classId, param.status);
		});
		$(document).on('click', '.toStudent', function() {
			sessionStorage.setItem('classId', param.classId);
			location.href = 'StudentInfo.html?classId='+param.classId;
		})

	})
	//监听班级管理
	table.on('row(demo)', function(data) {
		var param = data.data;
		$(document).on('click', '.toClassInfo', function() {
//			sessionStorage.setItem('classId', param.classId);
			location.href = 'ClassManagement.html?classId='+param.classId;
		})
	})
	//停用班级或者启动班级
	function updateClassStatus(id, status) {
		layer.confirm('确认要停用改班级吗？', function(index) {
			var url = '/classConsole/updateClassStatus?classId=' + id + '&status=' + status
			ajaxGET(url);
			table.reload('myClass');
			layer.close(index);
		})
	}
})