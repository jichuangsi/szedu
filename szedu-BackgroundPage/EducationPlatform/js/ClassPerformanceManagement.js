layui.use(['form', 'table', 'laydate'], function() {
	var form = layui.form,
		laydate = layui.laydate,
		table = layui.table;
		
	$('.time').each(function() {
		laydate.render({
			elem: this,
			type: 'date'
		});
	})
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