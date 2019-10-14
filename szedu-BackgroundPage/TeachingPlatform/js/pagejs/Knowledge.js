layui.use(['form', 'table', 'tree'], function() {
	var form = layui.form,
		tree = layui.tree,
		table = layui.table;

	table.render({
		elem: '#demo',
		method: "get",
		async: false,
		url: '../json/Knowledgge.json',
		headers: {
			'accessToken': getToken()
		},
		cols: [
			[{
					field: 'id',
					title: '序号',
					type: 'numbers'
				}, {
					field: 'know',
					title: '标签名',
					align: 'center'
				}, {
					field: 'subject',
					title: '关联课程',
					align: 'center'
				},
				{
					field: 'kao',
					title: '是否考点',
					align: 'center'
				},
				{
					field: 'certification',
					title: '操作',
					align: 'center',
					toolbar: '#operation'
				}
			]
		],
		page: true,
		limit: 10,
		loading: true,
		request: {
			pageName: 'pageIndex',
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

	var data = [{
		title: '课程',
		id: 1,
		children: [{
			title: '电动汽车',
			id: 1000,
			children: [{
				title: '电池',
				id: 10001
			}, {
				title: '驱动传动',
				id: 10002
			}]
		}, {
			title: '混合动力汽车',
			id: 1001
		}, {
			title: '传统汽车',
			id: 1002
		}]
	}, {
		title: '试题',
		id: 2,
		children: [{
			title: '传统汽车类',
			id: 2000
		}, {
			title: '新能源汽车类',
			id: 2001
		}]
	}, {
		title: '实习',
		id: 3,
		children: [{
			title: '实习单位介绍',
			id: 3000
		}, {
			title: ' 实习安全与注意事项',
			id: 3001
		}]
	}];
	tree.render({
		elem: '#tree',
		data: data,
		edit: ['add', 'update', 'del'] ,
		click: function(obj) {
			layer.msg(JSON.stringify(obj.data));
		},
		operate: function(obj) {
			var type = obj.type; //得到操作类型：add、edit、del
			var data = obj.data; //得到当前节点的数据
			var elem = obj.elem; //得到当前节点元素
			//Ajax 操作
			var id = data.id; //得到节点索引
			if(type === 'add') { //增加节点
				//返回 key 值
				console.log(data);
				console.log(123)
				return 123;
			} else if(type === 'update') { //修改节点
				console.log(data);
				console.log(elem)
				console.log(elem.find('.layui-tree-txt').html()); //得到修改后的内容
			} else if(type === 'del') { //删除节点
				console.log(data);

			};
		}
	});
})