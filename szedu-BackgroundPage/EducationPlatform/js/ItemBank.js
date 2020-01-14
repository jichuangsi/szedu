layui.use(['form', 'table', 'element', 'flow', 'upload'], function() {
	var form = layui.form,
		upload = layui.upload,
		flow = layui.flow,
		element = layui.element,
		table = layui.table;
	var user = JSON.parse(sessionStorage.getItem('user'))
	element.on('tab(docDemoTabBrief)', function(data) {
		var str = data.index
		if(str == 0) {
			$('.one').addClass('xs');
			$('.one').removeClass('yc');
			$('.one').siblings().removeClass('xs');
			$('.one').siblings().addClass('yc');
		} else if(str == 1) {
			getExam()
			$('.two').addClass('xs');
			$('.two').removeClass('yc');
			$('.two').siblings().removeClass('xs');
			$('.two').siblings().addClass('yc');
		} else if(str == 2) {
			getTeacherTest();
			$('.three').addClass('xs');
			$('.three').removeClass('yc');
			$('.three').siblings().removeClass('xs');
			$('.three').siblings().addClass('yc');
		}
	});

	function getExam() {
		table.render({
			elem: '#demo',
			method: "get",
			async: false,
			headers: {
				'accessToken': getToken()
			},
			url: httpUrl() + '/backExam/getTestPaperByTeacherId',
			cols: [
				[{
						field: 'id',
						title: '序号',
						type: 'numbers'
					},
					{
						field: 'testPaperName',
						title: '试卷名称',
						align: 'center'
					},
					{
						field: 'teacherName',
						title: '创建试卷人',
						align: 'center'
					}, {

						field: 'updateTime',
						title: '修改日期',
						align: 'center',
						templet: function(d) {
							if(d.updateTime != 0) {
								return new Date(+new Date(d.updateTime) + 8 * 3600 * 1000).toISOString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '')
							} else {
								return "-"
							}
						}
					},
					{
						field: 'grade',
						title: '适用年级',
						align: 'center'
					},
					{
						field: 'type',
						title: '考试类型',
						align: 'center'
					}, {
						field: 'subject',
						title: '操作',
						width: 250,
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
					arr = res.data;
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
	//查看试卷
	table.on('row(demo)', function(data) {
		var param = data.data;
		$(document).on('click', '', function() {

		});
	});
	//	getTypes();
	//获取题目类型
	function getTypes() {
		$.ajax({
			type: "get",
			url: httpUrl() + "/BackQuestions/getAllQuestionCategory",
			async: false,
			success: function(res) {
				console.log(res)
			}
		});
	}
	//删除题目
	function DelItem(questionId) {
		layer.confirm('确认要删除该题目吗？', function(index) {
			var url = '/BackQuestions/deleteSelfQuestion?questionId=' + questionId;
			ajaxPOST(url);
			layer.close(index);
		})
	}

	/*我的考试- 显示考试*/

	//获取科目

	//获取考试类型

	function getItem() {
		$.ajax({
			type: "get",
			url: httpUrl() + "/BackQuestions/getAllQuestions?pageSize=1&pageNum=10",
			async: false,
			success: function(res) {
				console.log(res)
			}
		});
	}
	//显示考试列表

	//根据老师获取试卷
	function getTeacherTest() {
		$('#box').empty();
		var str = '';
		$.ajax({
			type: "GET",
			url: httpUrl() + '/backExam/getExamByTeacher?pageNum=1&pageSize=10',
			async: false,
			headers: {
				'accessToken': getToken()
			},
			success: function(res) {
				if(res.code == '0010') {
					arr = res.data.content;
					/*拼接页面*/
					setExam(arr);
				} else if(res.code == '0050') {
					layui.notice.error("提示信息:获取试卷信息错误!");
				}
			}
		});
	}
	//	setExam();

	function setExam(arr) {
		if(arr == undefined) {
			return getTeacherTest();
		}
		$('#box').empty();
		var str = '';
		for(var i = 0; i < arr.length; i++) {
			str += '<div class="exam-list">';
			str += '<div class="box-pig">';
			if(arr[i] == null) {
				str += '<a href="ExaminationDetails .html?Id=' + arr[i].id + '"><img src="../img/微信图片_20191113143904_WPS图片.png" /></a>';
			} else { //没有图片就用默认图片
				str += '<a href="ExaminationDetails .html?Id=' + arr[i].id + '"><img src="../img/微信图片_20191113143904_WPS图片.png" /></a>';
			}
			str += '</div>';
			str += '<div class="box-content">';
			str += '<div class="box-content-title">';
			str += '<a href="ExaminationDetails .html?Id=' + arr[i].id + '">' + arr[i].examName + '</a>';
			str += '</div>';
			str += '<div class="box-content-des">';
			str += '<ul>';
			str += '<li><span>科目：</span>' + arr[i].subjectName + '</li>';
			str += '<li><span>考试类型：</span>' + arr[i].examType + '</li>';
			str += '<li><span>开考时间：</span>' + new Date(+new Date(arr[i].startTime) + 8 * 3600 * 1000).toISOString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '') + '</li>';
			str += '<li><span>考试时长：</span>' + arr[i].testTimeLength + '分钟</li>';
			if(arr[i].status == 1) {
				str += '<li><span>考试状态：</span>未发布</li>';
			} else if(arr[i].status == 2) {
				str += '<li><span>考试状态：</span>准备考试</li>';
			} else if(arr[i].status == 3) {
				str += '<li><span>考试状态：</span>正在考试</li>';
			} else if(arr[i].status == 4) {
				str += '<li><span>考试状态：</span>考试结束</li>';
			}

			str += '</ul>';
			str += '</div>';
			str += '<div class="box-content-btn">';
			str += '<input type="hidden" name="id" value="' + arr[i].id + '" />';
			if(arr[i].status == 1) {
				str += '<div class="layui-btn layui-btn-normal layui-btn-sm layui-btn-radius" onclick="release(this,2,"确定要发布该试卷吗？")">发布</div>';
				str += '<div class="layui-btn layui-btn-normal layui-btn-sm layui-btn-radius">预览</div>';
				str += '<div class="layui-btn layui-btn-normal layui-btn-sm layui-btn-radius">删除</div>';
			} else if(arr[i].status == 2) {
				str += '<div class="layui-btn layui-btn-normal layui-btn-sm layui-btn-radius "onclick="release(this,3,"确定要开始考试吗？")">开始考试</div>';
				str += '<div class="layui-btn layui-btn-normal layui-btn-sm layui-btn-radius">取消发布</div>';
			}else if( arr[i].status == 3){
				str += '<div class="layui-btn layui-btn-disabled layui-btn-sm layui-btn-radius ">查看成绩</div>';
				str += '<div class="layui-btn layui-btn-disabled layui-btn-sm layui-btn-radius ">公布答案</div>';
			} else if(arr[i].status == 4) {
				str += '<div class="layui-btn layui-btn-normal layui-btn-sm layui-btn-radius ">查看成绩</div>';
				str += '<div class="layui-btn layui-btn-normal layui-btn-sm layui-btn-radius ">公布答案</div>';
			}
			str += '</div>';
			str += '</div>';
			str += '</div>';
		}
		$('#box').append(str);
	}
	//发布试卷
	window.release = function(obj,status,msg) {
		layer.confirm(msg, function(index) {
			var id = $(obj).parent().find('input[name=id]').val();
			var url = '/backExam/publishingexamination?examId=' + id + '&status='+status;
			ajaxGET(url);
			setExam();
			layer.close(index);
		})
	}
	//查看考试详情
	//	$(document).on('click', '.exam-list', function() {
	//		var id=$('.exam-list').find('input[name=id]').val();
	//		location.href = 'ExaminationDetails .html?classId=' + id;
	//	})
	getQuestion('单选题');
	//查看老师的题目
	function getQuestion(type) {
		var id = user.teacherId;
		var num = 1;
		flow.load({
			elem: "#LAY_demo1",
			done: function(page, next) { //执行下一页的回调
				//模拟数据插入
				var lis = []
				var pageSize = 3
				$.ajax({
					type: "get",
					url: httpUrl() + '/BackQuestions/getQuestionsByTeacher?pageNum=' + page + '&pageSize=' + pageSize + '&questionType=' + type,
					async: false,
					headers: {
						'accessToken': getToken()
					},
					success: function(res) {
						var arr = res.data.content;
						var str = '';
						if(arr.length > 0) {
							for(var i = 0; i < arr.length; i++, num++) {
								str += '<div class="layui-row layui-form" style="margin-top: 30px;">';
								str += '<div checked style="float: left;">';
								str += '<input type="checkbox" name="" lay-skin="primary">';
								str += '</div>';
								str += '<div class="layui-row exam-rmb">';
								str += '<p class="exam-title">' + num + '.' + arr[i].content + '</p>';
								str += '</div>';
								str += '<div class="layui-row exam-rmb" style="margin:20px 0 0 40px;">';
								str += '<div class="layui-form-item exam-rmb">';
								str += '<input type="radio" name="ex" title="' + arr[i].aoption + '">';
								str += '</div>';
								str += '<div class="layui-form-item exam-rmb">';
								str += '<input type="radio" name="ex" title="' + arr[i].boption + '">';
								str += '</div>';
								str += '<div class="layui-form-item exam-rmb">';
								str += '<input type="radio" name="ex" title="' + arr[i].coption + '">';
								str += '</div>';
								str += '<div class="layui-form-item exam-rmb">';
								str += '<input type="radio" name="ex" title="' + arr[i].doption + '">';
								str += '</div>';
								str += '<div class="layui-form-item">';
								str += '<span style="font-size: 20px;">正确答案:</span><samp style="font-size: 20px;">' + arr[i].answer + '</samp>';
								str += '</div>';
								str += '<div class="layui-form-item">';
								if(arr[i].answerDetail == null) {
									arr[i].answerDetail = "暂无"
								}
								str += '<p style="font-size: 18px;">解析：' + arr[i].answerDetail + '</p>'
								str += '</div>';
								str += '<div class="layui-form-item" style="float: right;margin-right: 10%;">';
								str += '<span class="layui-btn layui-btn-radius layui-btn-primary">编辑</span>';
								str += '<span class="layui-btn layui-btn-radius layui-btn-primary">删除</span>';
								str += '</div>';
								str += '<hr />';
								str += '</div>'
							}
							//							$('.layui-flow-more').appendTo(str)
							if(page == 1) {
								$('#LAY_demo1').prepend(str)
							} else {
								$('#LAY_demo1').find('.layui-flow-more').before(str)
								//								$('.layui-flow-more').before(str)
							}

						} else {
							$('.layui-flow-more').find('i').remove('i')
							$('.layui-flow-more').html('暂无题目,快去添加题目吧')
							return layer.msg('暂无题目！');
						}
						next(lis.join(''), page < res.data.totalPages); //假设总页数为 10
						form.render();
					}
				});
			}
		});
	}

	function getMultipleQuestion(type) {
		var id = user.teacherId;
		var num = 1;
		flow.load({
			elem: ".LAY_demo2",
			done: function(page, next) { //执行下一页的回调
				console.log(page, next)
				//模拟数据插入
				var lis = []
				var pageSize = 3
				$.ajax({
					type: "get",
					url: httpUrl() + '/BackQuestions/getQuestionsByTeacher?pageNum=' + page + '&pageSize=' + pageSize + '&questionType=' + type,
					async: false,
					headers: {
						'accessToken': getToken()
					},
					success: function(res) {
						var arr = res.data.content;
						var str = '';
						if(arr.length > 0) {
							for(var i = 0; i < arr.length; i++, num++) {
								str += '<div class="layui-row layui-form" style="margin-top: 30px;">';
								str += '<div checked style="float: left;">';
								str += '<input type="checkbox" name="" lay-skin="primary">';
								str += '</div>';
								str += '<div class="layui-row exam-rmb">';
								str += '<p class="exam-title">' + num + '.' + arr[i].content + '</p>';
								str += '</div>';
								str += '<div class="layui-row exam-rmb" style="margin:20px 0 0 40px;">';
								str += '<div class="layui-form-item exam-rmb">';
								str += '<input type="checkbox" name="ex" lay-skin="primary" title="' + arr[i].aoption + '">';
								str += '</div>';
								str += '<div class="layui-form-item exam-rmb">';
								str += '<input type="checkbox" name="ex" lay-skin="primary" title="' + arr[i].boption + '">';
								str += '</div>';
								str += '<div class="layui-form-item exam-rmb">';
								str += '<input type="checkbox" name="ex" lay-skin="primary" title="' + arr[i].coption + '">';
								str += '</div>';
								str += '<div class="layui-form-item exam-rmb">';
								str += '<input type="checkbox" name="ex" lay-skin="primary" title="' + arr[i].doption + '">';
								str += '</div>';
								str += '<div class="layui-form-item">';
								str += '<span style="font-size: 20px;">正确答案:</span><samp style="font-size: 20px;">' + arr[i].answer + '</samp>';
								str += '</div>';
								str += '<div class="layui-form-item">';
								if(arr[i].answerDetail == null) {
									arr[i].answerDetail = "暂无"
								}
								str += '<p style="font-size: 18px;">解析：' + arr[i].answerDetail + '</p>'
								str += '</div>';
								str += '<div class="layui-form-item" style="float: right;margin-right: 10%;">';
								str += '<span class="layui-btn layui-btn-radius layui-btn-primary">编辑</span>';
								str += '<span class="layui-btn layui-btn-radius layui-btn-primary">删除</span>';
								str += '</div>';
								str += '<hr />';
								str += '</div>'
							}
							//							$('.layui-flow-more').appendTo(str)
							if(page == 1) {
								$('.LAY_demo2').prepend(str)
							} else {
								$('.LAY_demo2').find('.layui-flow-more').before(str)
								//								$('.layui-flow-more').before(str)
							}

						} else {
							$('.LAY_demo2').find('.layui-flow-more').find('i').remove('i')
							$('.LAY_demo2').find('.layui-flow-more').html('暂无题目,快去添加题目吧')
							return
						}
						next(lis.join(''), page < res.data.totalPages); //假设总页数为 10
						form.render();
					}
				});
			}
		});
	}
	element.on('tab(tabDemo)', function(data) {
		if(data.index == 0) {
			getQuestion('单选题');
		} else if(data.index == 1) {
			getMultipleQuestion('多选题');
		} else if(data.index == 2) {
			getQuestion('判断题');
		}

	});
	//表格上传--单选题
	upload.render({
		elem: '#load',
		url: httpUrl() + '/BackQuestions/excel/saveQuestionByExcel',
		headers: {
			'accessToken': getToken()
		},
		method: 'POST',
		accept: 'file',
		size: 10240,
		exts: 'xls/*',
		before: function(obj) {
			//layer.load(); //上传loading
		},
		done: function(res, index, upload) { //上传后的回调
			if(res.code == "0010") {
				if(res.data == '[]') {
					setMsg("导入成功！", 1)
				} else {
					var str = res.data;
					setMsg("导入成功！第" + str + "行导入失败", 1);
				}
			} else {
				setMsg("导入失败！", 2)
			}
		},
		error: function() {
			layer.closeAll('loading');
		}
	})

	//表格上传--多选题
	upload.render({
		elem: '#load2',
		url: httpUrl() + '/BackQuestions/excel/saveMultipleChoiceQuestionsByExcel',
		headers: {
			'accessToken': getToken()
		},
		method: 'POST',
		accept: 'file',
		size: 10240,
		exts: 'xls/*',
		before: function(obj) {
			//layer.load(); //上传loading
		},
		done: function(res, index, upload) { //上传后的回调
			if(res.code == "0010") {
				if(res.data == '[]') {
					setMsg("导入成功！", 1)
				} else {
					var str = res.data;
					setMsg("导入成功！第" + str + "行导入失败", 1);
				}
			} else {
				setMsg("导入失败！", 2)
			}
		},
		error: function() {
			layer.closeAll('loading');
		}
	})

	//表格上传--判断题
	upload.render({
		elem: '#load3',
		url: httpUrl() + '/BackQuestions/excel/saveJudgementQuestionByExcel',
		headers: {
			'accessToken': getToken()
		},
		method: 'POST',
		accept: 'file',
		size: 10240,
		exts: 'xls/*',
		before: function(obj) {
			//layer.load(); //上传loading
		},
		done: function(res, index, upload) { //上传后的回调
			if(res.code == "0010") {
				if(res.data == '[]') {
					setMsg("导入成功！", 1)
				} else {
					var str = res.data;
					setMsg("导入成功！第" + str + "行导入失败", 1);
				}
			} else {
				setMsg("导入失败！", 2)
			}
		},
		error: function() {
			layer.closeAll('loading');
		}
	})
})