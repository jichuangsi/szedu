layui.use(['form', 'table', 'laydate', 'xmSelect'], function() {
	var form = layui.form,
		laydate = layui.laydate,
		xmSelect = layui.xmSelect,
		table = layui.table;
	$('.time').each(function() {
		laydate.render({
			elem: this,
			type: 'date'
		});
	})
	//添加学校
	form.on('submit(formDemo)', function(data) {
		var param = data.field;
		//地址 +详细地址
		param.address += param.fullAddress;
		var schoolCurriculumRelations=[];
		var obj = param.select.split(',');
		for(var i = 0; i < obj.length; i++) {
			schoolCurriculumRelations.push({
				curriculumId: obj[i]
			})
		}
		param.schoolCurriculumRelations=schoolCurriculumRelations
		//获取课程
		var url="/BackSchoolInfo/saveSchoolInfo";
		ajaxPOST(url,param)
	})

	//获取课程选项
	function getCur() {
		var data = [];
		$.ajax({
			type: "post",
			url: httpUrl() + "/BackCurriculum/getAllCurriculum",
			async: false,
			headers: {
				'accessToken': getToken()
			},
			success: function(res) {
				if(res.code == '0010') {
					updateName(res.data)
					data = res.data;
				}
			}
		});
		return data
	}

	function updateName(array) {
		var keyMap = {
			curriculumName: 'name',
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
	//课程权限
	var demo1 = xmSelect.render({
		el: '.xmSelect',
		data: getCur()
	})
	//教师改
	table.render({
		elem: '#demo',
		method: "get",
		async: false,
		url: httpUrl() + '/backuser/getOpLogByNameAndPage',
		headers: {
			'accessToken': getToken()
		},
		cols: [
			[{
				field: 'id',
				title: '序号',
				type: 'numbers'
			}, {
				field: 'operatorName',
				title: '账号名',
				align: 'center'
			}, {
				field: 'operatorName',
				title: '姓名',
				align: 'center'
			}, {
				field: 'operatorName',
				title: '工号',
				align: 'center'
			}, {
				field: 'operatorName',
				title: '管理员权限',
				align: 'center'
			}, {
				field: 'operatorName',
				title: '状态',
				align: 'center'
			}, {
				field: 'operatorName',
				title: '查看信息',
				align: 'center'
			}, {
				field: 'opAction',
				title: '修改',
				align: 'center'
			}]
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
			var code;
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
})