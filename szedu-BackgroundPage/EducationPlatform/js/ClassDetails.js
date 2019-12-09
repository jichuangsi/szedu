layui.use(['form', 'table'], function() {
	var form = layui.form,
		table = layui.table;
	getClass();
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
						title: '操作',
						align: 'center',
						toolbar: '#operation'
					}
				]
			],
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
})