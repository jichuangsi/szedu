layui.use(['form', 'table', 'element'], function() {
	var form = layui.form,
		element = layui.element,
		table = layui.table;

	getSpread();
	//获取成绩分布
	function getSpread() {
		table.render({
			elem: '#spread',
			method: "get",
			async: false,
			url: '../json/Spread.json',
			cols: [
				[{
						field: 'segment',
						title: '分数段',
						align: 'center'
					},
					{
						field: 'rank',
						title: '等级',
						align: 'center'
					},
					{
						field: 'Number',
						title: '人数',
						align: 'center'
					}, {
						field: 'Proportion',
						title: '比例',
						align: 'center'
					}
				]
			],
			skin: 'line',
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
	getScoues();

	function getScoues() {
		table.render({
			elem: '#scoues',
			method: "get",
			async: false,
			url: '../json/studentRank.json',
			cols: [
				[{
						field: 'id',
						title: '序号',
						type: 'numbers'
					}, {
						field: 'number',
						title: '学号',
						align: 'center'
					},
					{
						field: 'name',
						title: '姓名',
						align: 'center'
					},
					{
						field: 'class',
						title: '班级',
						align: 'center'
					},
					{
						field: 'mark',
						title: '分数',
						align: 'center',
						templet: function(d) {
							if(d.mark == null) {
								return "-"
							} else {
								return d.mark
							}
						}
					},
					{
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
					},
					{
						field: 'accuracy',
						title: '答题正确率',
						align: 'center',
						templet: function(d) {
							if(d.accuracy == null) {
								return "-"
							} else {
								return d.accuracy
							}
						}
					},
					{
						field: 'ranking',
						title: '排名',
						align: 'center',
						templet: function(d) {
							if(d.ranking == null) {
								return "-"
							} else {
								return d.ranking
							}
						}
					}, {
						field: 'Remarks',
						title: '备注',
						align: 'center',
						templet: function(d) {
							if(d.Remarks == null) {
								return "-"
							} else {
								return d.Remarks
							}
						}
					}
				]
			],
			skin: 'line',
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