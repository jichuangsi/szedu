layui.use(['form', 'table', 'element'], function() {
	var form = layui.form,
		element = layui.element,
		table = layui.table;
	//获取班级管理数据
	function getClass() {
		table.render({
			elem: '#demo',
			method: "get",
			async: false,
			url: '../json/data.json',
			cols: [
				[{
						field: 'id',
						title: '序号',
						type: 'numbers'
					}, {
						field: 'name',
						title: '姓名',
						align: 'center'
					},
					{
						field: 'school',
						title: '学校',
						align: 'center'
					},
					{
						field: 'class',
						title: '班级',
						align: 'center'
					}, {
						field: 'subject',
						title: '查看',
						align: 'center',
						toolbar: '#operation'
					}
				]
			],
			skin: 'line',
			page: true,
			limit: 10,
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
			method: "get",
			async: false,
			url: '../json/kaoqin.json',
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

	element.on('tab(docDemoTabBrief)', function(data) {
		var str = data.index
		if(str == 0) {
			$('.one').addClass('xs');
			$('.one').removeClass('yc');
			$('.one').siblings().removeClass('xs');
			$('.one').siblings().addClass('yc');
		} else if(str == 1) {
			getClass()
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

})