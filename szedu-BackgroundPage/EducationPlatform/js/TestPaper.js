layui.use(['form', 'table', 'xmSelect', 'element'], function() {
	var form = layui.form,
		element = layui.element,
		xmSelect = layui.xmSelect,
		table = layui.table;
	var teacherSubList = []; //老师的全部科目
	var BoutiqueSubList = []; //精品题库的
	form.on('submit(formDemo)', function(data) {
		var param = data.field;
		var subList = [];
		var chapter = [];
		//判断选择那个题库
		if(param.type == -1) {
			return setMsg("请选择考试类型", 7)
		}
		if(param.way == 'jp') { //选择精品题库
			param.way = 1;
			if(param.bankitem == "all") {
				
			} else if(param.bankitem == "km") {
				var num = $("input:checkbox[name=tQuestion]:checked").length;
				if(num <= 0) {
					return layer.msg('请按科目选择题目！');
				}
				$("input:checkbox[name=tQuestion]:checked").each(function() {
					subList.push($(this).val())
				})
				param.subList = subList;
				console.log(subList);
			} else if(param.bankitem == "zj") {
				var chapterList = demo1.getValue();
				if(chapterList.length <= 0) {
					return layer.msg('请按章节选择题目！')
				}
				$.each(chapterList, function(index, item) {
					chapter.push(item.id)
				});
				param.chapter = chapter;
			}
		} else if(param.way == "wd") { //选择我的题库
			param.way = 2;
			if(param.er == "zj") {
				var chapterList = demo2.getValue();
				if(chapterList.length <= 0) {
					return layer.msg('请按章节选择题目！')
				}
				$.each(chapterList, function(index, item) {
					chapter.push(item.id)
				});
				param.chapter = chapter;
			} else if(param.er == 'km') {
				var num = $("input:checkbox[name=tQuestion]:checked").length;
				if(num <= 0) {
					return layer.msg('请按科目选择题目！');
				}
				$("input:checkbox[name=tQuestion]:checked").each(function() {
					subList.push($(this).val())
				})
				param.subList = subList;
				console.log(subList);
			} else if(param.er == 'all') {
				param.subList = teacherSubList;
			}
		}
		console.log(param)
		var url = "/backExam/addTestpaper";
		ajaxPOST(url, param);
	});

	$("input:radio[name=bank]").change(function() {
		console.log(1111)
	})

	//获取选择树状图的数据

	function getData(pid) {
		var url;
		if(pid == undefined) {
			url = '/TreeMenu/getTreeMenuByPid?pid=0';
		} else {
			url = '/TreeMenu/getTreeMenuByPid?pid=' + pid;
		}
		var arr = ajaxGetData(url);
		var data = [];
		$.each(arr, function(index, item) {
			if(item.title != "无" && item.title != "其他" && item.pid == 0) {
				item.children = []
			}
			data.push(item)
		});
		return data
	}
	//构建选择科目的树状图
	var demo1 = xmSelect.render({
		el: '#demo1',
		autoRow: true,
		height: '200',
		direction: 'down',
		prop: {
			name: 'title',
			value: 'id',
		},
		tree: {
			show: true,
			showFolderIcon: true,
			showLine: true,
			indent: 40,
			expandedKeys: [-1],
			lazy: true,
			strict: true,
			load: function(item, cb) {
				setTimeout(function() {
					if(item.pid == 0) {
						cb(getData(item.id))
					}
				}, 500)
			}
		},
		height: 'auto',
		data() {
			return getData();
		}
	})
	//我的题库
	var demo2 = xmSelect.render({
		el: '#demo2',
		autoRow: true,
		height: '200',
		direction: 'down',
		prop: {
			name: 'title',
			value: 'id',
		},
		tree: {
			show: true,
			showFolderIcon: true,
			showLine: true,
			indent: 40,
			expandedKeys: [-1],
			lazy: true,
			strict: true,
			load: function(item, cb) {
				setTimeout(function() {
					if(item.pid == 0) {
						cb(getData(item.id))
					}
				}, 500)
			}
		},
		height: 'auto',
		data() {
			return getData();
		}
	})
	getTeacherQuestion();
	//获取老师的题目数量
	function getTeacherQuestion() {
		var url = '/BackQuestions/getAllQuestionsNumBySubjectAndTeacher';
		var arr = ajaxGetData(url);
		console.log(arr)
		$.each(arr, function(index, item) {
			teacherSubList.push(item.id);
		});
		var str = '';
		$('#teacherQuertion').empty();
		$.each(arr, function(index, item) {
			str += '<div>';
			str += '<input type="checkbox" name="tQuestion" value="' + item.id + '" lay-skin="primary" title="' + item.name + '">';
			str += '<div class="chec-span">';
			str += '<span>单选题：' + item.singleChoice + ',</span><span>多选题：' + item.multipleChoice + ',</span><span>判断题：' + item.judgement + '</span>';
			str += '</div>';
			str += '</div>';
		});
		$('#teacherQuertion').append(str)
		form.render();
	}

})