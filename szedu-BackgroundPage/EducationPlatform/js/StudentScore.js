layui.use(['form', 'table'], function() {
	var form = layui.form,
		table = layui.table;
	var myChart = echarts.init(document.getElementById('main'));
	 var id =UrlSearch();
	// 指定图表的配置项和数据
	var option = {
		dataZoom: [{
				id: 'dataZoomX',
				type: 'slider',
				xAxisIndex: [0],
				filterMode: 'filter'
			},
			{
				id: 'dataZoomY',
				type: 'slider',
				yAxisIndex: [0],
				filterMode: 'empty'
			}
		],
		color: ['#3398DB'],
		title: {
			text: ''
		},
		tooltip: {},
		legend: {
			data: ['分数']
		},
		xAxis: {
			data: ["1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20(次)"],
			itemStyle: {
				normal: {
					label: {
						show: true,
					}
				}
			}
		},
		yAxis: {
			//					data:[10,20,30,40,50,60,70,80,90,100]	
		},
		series: [{
			name: '分数',
			type: 'bar',
			barWidth: '30%',
			data: [76, 65, 86, 88, 85, 85, 66, 87, 66, 78, 76, 65, 86, 88, 85, 85, 66, 87, 66, 78],
			itemStyle: {
				normal: {
					label: {
						show: true,
						position: 'top',
					}
				}
			}
		}]
	};
	// 使用刚指定的配置项和数据显示图表。
	myChart.setOption(option);

	table.render({
		elem: '#demo',
		method: "post",
		async: false,
		url: httpUrl()+'/classConsole/getStudentById?id='+id,
		cols: [
			[{
					field: 'id',
					title: '序号',
					type: 'numbers'
				},
				{
					field: 'name',
					title: '考试名称',
					align: 'center'
				},
				{
					field: 'name',
					title: '考试类型',
					align: 'center'
				}, {

					field: 'xz',
					title: '学期',
					align: 'center'
				},
				{
					field: 'class',
					title: '考试时间',
					align: 'center'
				},
				{
					field: 'name',
					title: '科目	',
					align: 'center'
				}, {
					field: 'number',
					title: '正确率',
					align: 'center'
				}, {
					field: 'number',
					title: '分数',
					align: 'center'
				},{
					field: 'number',
					title: '等级',
					align: 'center'
				},{
					field: 'subject',
					title: '班级成绩',
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
})