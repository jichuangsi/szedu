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
		pid: 0,
		children: [{
			title: '电动汽车',
			id: 1000,
			pid: 1,
			children: [{
				title: '电池',
				id: 1002,
				pid: 1000
			}, {
				title: '驱动传动',
				id: 10003,
				pid: 1000
			}]
		}, {
			title: '混合动力汽车',
			id: 1004,
			pid: 1
		}, {
			title: '传统汽车',
			id: 1005,
			pid: 1
		}]
	}, {
		title: '试题',
		id: 2,
		pid: 0,
		children: [{
			title: '传统汽车类',
			id: 2000,
			pid: 2
		}, {
			title: '新能源汽车类',
			id: 2001,
			pid: 2
		}]
	}, {
		title: '实习',
		id: 3,
		pid: 0,
		children: [{
			title: '实习单位介绍',
			id: 3000,
			pid: 3
		}, {
			title: ' 实习安全与注意事项',
			id: 3001,
			pid: 3
		}]
	}];

	getNode();
	var NodeData = [];
	//获取菜单节点
	function getNode() {
		var url = "/TreeMenu/getAllTreeMenu";
		$.ajax({
			type: "GET",
			url: httpUrl() + url,
			async: false,
			headers: {
				'accessToken': getToken()
			},
			success: function(res) {
				if(res.code == '0010') {
					NodeData = treeData(res.data);
					setTree(NodeData);
				} else if(res.code == '0031') {
					//	layui.notice.info("提示信息：权限不足");
				} else if(res.code == '0050') {
					layui.notice.error("提示信息:错误!");
				}
			}
		});
	}
	//把数据整理成树状图
	function treeData(source) {
		return source.filter(father => {
			var branchArr = source.filter(child => father.id == child.pid);
			branchArr.length > 0 ? father.children = branchArr : ''
			console.log(father)
			return father.pid == 0
		})
	}
	//
	//创建树状图
	function setTree(NodeData) {
		tree.render({
			elem: '#tree',
			data: NodeData,
			edit: ['add', 'update', 'del'],
			click: function(obj) {},
			operate: function(obj) {
				var type = obj.type; //得到操作类型：add、edit、del
				var data = obj.data; //得到当前节点的数据
				var elem = obj.elem; //得到当前节点元素
				//Ajax 操作
				var id = data.id; //得到节点索引
				if(type === 'add') { //增加节点
					var url = "/TreeMenu/saveTreeNode";
					var param = {
						title: '未命名',
						pid: data.id
					}
					ajaxPOST(url, param);
					getNode();
				} else if(type === 'update') { //修改节点
					console.log(data);
					var url = "/TreeMenu/updateTreeNode?id=" + data.id + "&title=" + elem.find('.layui-tree-txt').html();
					ajaxGET(url);
					getNode();
				} else if(type === 'del') { //删除节点
					var url = "/TreeMenu/deleteTreeNode?id=" + data.id
					ajaxPOST(url)
				};
			}
		});
	}

	form.on('submit(formDemo)', function(data) {
		var param = data.field;
		var url = "/TreeMenu/saveTreeNode";
		ajaxPOST(url, param);
		getNode();
		return false;
	});
	//	var data = [{
	//		title: '目录',
	//		id: 1,
	//		pid: 0,
	//		children: []
	//	}];

})