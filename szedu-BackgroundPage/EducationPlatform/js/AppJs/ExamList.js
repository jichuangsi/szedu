layui.use(['form', 'table'], function() {
	var form = layui.form,
		table = layui.table;
	getStudent();
	//获取学生的考试列表
	function getStudent() {
		var url = "/backExam/getExamByStudent?pageNum=1&pageSize=10";
		var arr = studentGetMethod(url);
		var data = arr.list;
		$('.studentExam').empty();
		var str = '';
		if(data.length > 0) {
			$.each(data, function(index, item) {
				str += '<div class="layui-col-xs6 layui-col-sm3">';
				str += '<div class="Exam-box">';
				str += '<div class="box-pig">';
				str += '<a href="#"><img src="../img/微信图片_20191113143904_WPS图片.png" /></a>';
				str += '</div>';
				str += '<div class="box-content">';
				str += '<div class="box-content-title"><a href="#">' + item.examName + '</a></div>';
				str += '<div class="box-content-des">';
				str += '<ul>';
				str += '<li><span>科目：</span>' + item.subjectName + '</li>';
				str += '<li><span>考试类型：</span>' + item.examType + '</li>';
				str += '<li><span>开考时间：</span>' + new Date(+new Date(item.startTime) + 8 * 3600 * 1000).toISOString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '') + '</li>';
				str += '<li><span>考试时长：</span>' + item.testTimeLength + '分钟</li>';
				if(item.status == 1) {
					str += '<li><span>考试状态：</span>正在考试</li>';
				} else if(item.status == 2) {
					str += '<li><span>考试状态：</span>准备考试</li>';
				} else if(item.status == 3) {
					str += '<li><span>考试状态：</span>正在考试</li>';
				} else if(item.status == 4||item.status == 5) {
					str += '<li><span>考试状态：</span>考试结束</li>';
				}
				str += '</ul>';
				str += '</div>';
				str += '<div class="box-content-btn">';
				if(item.status == 1) {} else if(item.status == 2) {
					str += '<div class="layui-btn layui-btn-disabled layui-btn-sm layui-btn-radius"  >进入考试</div>';
					str += '<div class="layui-btn layui-btn-disabled layui-btn-sm layui-btn-radius ">查看成绩</div>';
				} else if(item.status == 3) {
					str += '<input type="hidden"  name="testPaperId" value="' + item.testPaperId + '"/>';
					str += '<input type="hidden"  name="id" value="' + item.id + '"/>';
					str += '<div class="layui-btn layui-btn-normal layui-btn-sm layui-btn-radius" onclick="toExam(this)" >进入考试</div>';
					str += '<div class="layui-btn layui-btn-disabled layui-btn-sm layui-btn-radius ">查看成绩</div>';
				} else if(item.status == 4||item.status == 5) {
					str += '<div class="layui-btn layui-btn-disabled layui-btn-sm layui-btn-radius" >进入考试</div>';
					str += '<div class="layui-btn layui-btn-primary layui-btn-sm layui-btn-radius ">查看成绩</div>';
				}
				str += '</div>';
				str += '</div>';
				str += '</div>';
				str += '</div>';
			});
			$('.studentExam').append(str);
			form.render();
		} else {
			return layer.msg("暂无考试！");
		}
	}
	//加入考试
	window.toExam = function(obj) {
		var ids = {
			id: $(obj).parent().find('input[name=id]').val(),
			testPaperId: $(obj).parent().find('input[name=testPaperId]').val()
		}
		sessionStorage.setItem('ids',JSON.stringify(ids))
		location.href = "ExamContent.html";
	}
})