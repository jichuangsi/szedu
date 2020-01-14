layui.use(['form', 'table'], function() {
	var form = layui.form,
		table = layui.table;
	table.render({
		elem: '#demo',
		method: "get",
		async: false,
		url: '../json/historyScore.json',
		cols: [
			[{
					field: 'id',
					title: '序号',
					type: 'numbers'
				}, {
					field: 'name',
					title: '考试名称',
					align: 'center',
					width:'150'
				},
				{
					field: 'type',
					title: '考试类型',
					align: 'center',
					width:'100'
				},
				{
					field: 'Semester',
					title: '学期',
					align: 'center',
					width:'100'
				}, {
					field: 'createTime',
					title: '考试时间',
					align: 'center',
					width:'150'
				}, {
					field: 'subject',
					title: '科目',
					align: 'center',
					width:'100'
				}, {
					field: 'acc',
					title: '正确率',
					align: 'center',
					templet: function(d) {
						if(d.acc == null) {
							return "-"
						} else {
							return d.acc
						}
					},
					width:'100'
				}, {
					field: 'Average',
					title: '平均分',
					align: 'center',
					templet: function(d) {
						if(d.Average == null) {
							return "-"
						} else {
							return d.Average
						}
					}
					,width:'100'
				}, {
					field: 'rank',
					title: '等级',
					align: 'center',
					templet: function(d) {
						if(d.rank == null) {
							return "-"
						} else {
							return d.rank
						}
					}
				}, {
					field: 'subject',
					title: '查看',
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

})