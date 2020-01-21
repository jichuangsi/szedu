var lessionType=null;
var subjectId=null;
layui.use(['form', 'table', 'rate', 'laypage'], function() {
	var form = layui.form,
		rate = layui.rate,
		laypage = layui.laypage,
		table = layui.table;

	var param = {
		"lessionType":lessionType,
		"pageNum": 1,
		"pageSize": 12,
		"subjectId": subjectId,
		"time": null,
		"timeName": ""
	}
	getStudentCoures(param);
	//获取学生课堂
	function getStudentCoures(param) {
		console.log(param)
		var url = '/studentLesson/getAllLesson';
		var arr = StudentPostMethod(url, param);
		var data=arr.content;
		var str = '';
		$('#studentCourse').empty();
		$.each(data, function(index, item) {
			str += '<div class="layui-col-xs6 layui-col-sm3">';
			str += '<div class="Exam-box">';
			str += '<div class="box-pig">';
			str += '<a href="#"><img src="../img/12.jpg" /></a></div>';
			str += '<div class="box-content">';
			str += '<div class="box-content-title">';
			str += '<a href="#">' + item.courseTitle + '</a>';
			str += '</div>';
			str += '<div class="box-content-des"><ul>';
			str += '<li><span>科目：</span>' + item.subject + '</li>';
			str += '<li><span>课类型类型：</span>' + item.lessonTypeName + '</li>';
			str += '<li><span>开课时间：</span>' + item.startTime + '</li>';
			str += '<li><span>课堂时长：</span>' + item.courseTimeLength + '</li>';
			if (item.status == "P") {
				str += '<li><span>课堂状态：</span>准备上课</li>';
			} else if (item.status == "H") {
				str += '<li><span>课堂状态：</span>正在上课</li>';
			} else if (item.status == "F") {
				str += '<li><span>课堂状态：</span>已结束</li>';
			}
			str += '</ul>';
			str += '</div>';
			str += '<div class="box-content-btn">';
			str += '<input type="hidden" name="id" value="' + item.id + '" />';
			str += '<input type="hidden" name="name" value="' + item.courseTitle + '" />';
			if (item.status == "P") {
				if (item.qiandao) {
					str +=
						'<div class="layui-btn layui-btn-normal layui-btn-sm layui-btn-radius "onclick="AttendanceInClass(this)">签到</div>';
					str += '<div class="layui-btn layui-btn-disabled layui-btn-sm layui-btn-radius ">评分</div>';
				} else {
					str += '<div class="layui-btn layui-btn-normal layui-btn-sm layui-btn-radius ">已签到</div>';
					str += '<div class="layui-btn layui-btn-disabled layui-btn-sm layui-btn-radius ">评分</div>';
				}
				str +=
					'<div class="layui-btn layui-btn-normal layui-btn-sm layui-btn-radius"onclick="toCourse(this)">进入课堂</div>';
			} else if (item.status == "H") {
				if (item.qiandao) {
					str +=
						'<div class="layui-btn layui-btn-normal layui-btn-sm layui-btn-radius "onclick="AttendanceInClass(this)">签到</div>';
					str += '<div class="layui-btn layui-btn-normal layui-btn-sm layui-btn-radius "onclick="score(this)">评分</div>';

				} else {
					str += '<div class="layui-btn layui-btn-normal layui-btn-sm layui-btn-radius ">已签到</div>';
					str += '<div class="layui-btn layui-btn-normal layui-btn-sm layui-btn-radius "onclick="score(this)">评分</div>';
				}
			} else if (item.status == "F") {
				str += '<div class="layui-btn layui-btn-disabled layui-btn-sm layui-btn-radius ">签到</div>';
				str += '<div class="layui-btn layui-btn-normal layui-btn-sm layui-btn-radius" onclick="score(this)">评分</div>';
			}
			str += '</div>';
			str += '</div>';
			str += '</div>';
			str += '</div>';
			str += '</div>';
		});
		$('#studentCourse').append(str);
		laypage.render({
			elem: 'page',
			limit:param.pageSize,//页面多少条
			count: arr.totalElements,//总数
			curr:param.pageNum,
			 theme: '#1E9FFF',
			jump: function(obj, first) {
				console.log(obj)
				//obj包含了当前分页的所有参数，比如：
				console.log(obj.curr); //得到当前页，以便向服务端请求对应页的数据。
				console.log(obj.limit); //得到每页显示的条数
				//首次不执行
				if (!first) {
					param = {
						"lessionType": "",
						"pageNum": obj.curr	,
						"pageSize":12,
						"subjectId": null,
						"time": null,
						"timeName": ""
					}
					getStudentCoures(param);
				}
			}
		});

	}
	
	form.on('select(subject)', function(data) {
		subjectId=data.value;
		var param = {
		"lessionType":lessionType,
		"pageNum": 1,
		"pageSize": 12,
		"subjectId": subjectId,
		"time": null,
		"timeName": ""
	}
		if(subjectId!=-1){
			getStudentCoures(param)
		}
	});
	getSubject();
	//获取科目
	function getSubject() {
		var url = '/TreeMenu/getTreeMenuByPid?pid=0';
		var arr = studentGetMethod(url);
		console.log(arr)
		var str = '';
		$('#subject').empty();
		str += '<option value="-1">科目</option>'
		if (arr.length > 0) {
			$.each(arr, function(index, item) {
				str += '<option value="' + item.id + '">' + item.title + '</option>'
			});
		} else {
			str += '<option value="-2">暂时无科目</option>'
		}
		$('#subject').append(str);
		form.render();
	}
	//课堂签到
	window.AttendanceInClass = function(obj) {
		var user = JSON.parse(sessionStorage.getItem('student'))
		var url = '/studentLesson/addAttendace';
		var id = $(obj).parent().find('input[name=id]').val();
		var name = $(obj).parent().find('input[name=name]').val();
		var param = {
			studentId: user.id,
			courseId: id,
			courseName: name
		}
		var arr = StudentPostMethod(url, param)
		if (arr) {
			return layer.msg('签到成功！');
		}
	}
	//进入课堂
	window.toCourse = function(obj) {
		var id = $(obj).parent().find('input[name=id]').val();
		location.href = "CourseDetails.html?id=" + id
	}
	//评分
	window.score = function(obj) {
		var id = $(obj).parent().find('input[name=id]').val();
		index = layer.open({
			type: 1,
			area: ['200px', '150px'],
			anim: 2,
			title: '评分',
			maxmin: true,
			shadeClose: true, //点击遮罩关闭
			content: $('#score')
		});
		var score;
		rate.render({
			elem: '#demo',
			text: true,
			setText: function(value) {
				score = value;
				this.span.text((value + "分"));
				if (score != 0) {
					var url = "/studentLesson/addscore?score=" + score + "&courseId=" + id
					var arr = StudentPostMethod(url);
					if (arr) {
						layer.close(index);
						return layer.msg("评分成功！");
					}
				}
			}
		})
	}


})
