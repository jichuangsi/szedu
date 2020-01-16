var time = 0;
layui.use(['form', 'table'], function() {
	var form = layui.form,
		table = layui.table;
	getExam();
	var ids = JSON.parse(sessionStorage.getItem('ids'))
	//根据id查询考试
	var Multiple;

	function getExam() {
		Multiple = [];
		var ids = JSON.parse(sessionStorage.getItem('ids'))
		var url = '/backExam/getTestPaperDetailByid?testPaperId=' + ids.testPaperId + '&examId=' + ids.id;
		var arr = studentGetMethod(url);
		var str = '';
		time = parseInt(arr.testTimeLength);
		var data = arr.questionsModels;
		$('.exam-name').html(arr.testPaperName);
		$('#exam').empty();
		var count = 1;
		if (data.length > 0) {
			$.each(data, function(index, item) {
				//
				str += '<div class="exam-content">';
				str += '' + count + ',' + item.content + '';
				str += '<div class="option layui-form">';
				if (item.type == "单选题") {
					str += '<div class="layui-form-item">';
					str += '<input type="radio" name="' + item.id + '" value="A" title="' + item.aoption + '" >';
					str += '</div>';
					str += '<div class="layui-form-item">';
					str += '<input type="radio" name="' + item.id + '" value="B" title="' + item.boption + '" >';
					str += '</div>';
					str += '<div class="layui-form-item">';
					str += '<input type="radio" name="' + item.id + '" value="C" title="' + item.coption + '" >';
					str += '</div>';
					str += '<div class="layui-form-item">';
					str += '<input type="radio" name="' + item.id + '" value="D" title="' + item.doption + '" >';
					str += '</div>';
					str += '<hr />';
					str += '</div>';
				} else if (item.type == "多选题") {
					Multiple.push(item.id)
					str += '<div class="layui-form-item">';
					str += '<input type="checkbox" name="' + item.id + '-A" lay-skin="primary" value="A" title="' + item.aoption +
						'" >';
					str += '</div>';
					str += '<div class="layui-form-item">';
					str += '<input type="checkbox" name="' + item.id + '-B"lay-skin="primary"  value="B" title="' + item.boption +
						'" >';
					str += '</div>';
					str += '<div class="layui-form-item">';
					str += '<input type="checkbox" name="' + item.id + '-C" lay-skin="primary" value="C" title="' + item.coption +
						'" >';
					str += '</div>';
					str += '<div class="layui-form-item">';
					str += '<input type="checkbox" name="' + item.id + '-D" lay-skin="primary" value="D" title="' + item.doption +
						'" >';
					str += '</div>';
					str += '<hr />';
				} else if (item.type == "判断题") {

				}
				str += '</div>';
				count++
			});
			str += '<div class="layui-form-item">';
			str += '<div class="layui-input-block">';
			str +=
				'<div id="Button1" class="layui-btn layui-btn-normal layui-btn-radius" lay-submit lay-filter="formDemo">立即提交</div>';
			str += '</div>';
			str += '</div>';
			$('#exam').append(str);
			form.render();
		} else {
			return layer.msg("获取考试内容错误！");
		}
	}
	timer(time);
	//定时器
	function timer(time) {
		var minute = 0,
			second = 0;
		minute = time;
		second = time * 60;
		var time1 = setInterval(function() {
			if (second == 0) {
				$('#timer').html("考试结束！");
				$('#timer').css("color", "red");
				clearInterval(time1)
				$("#Button1").click();
				return layer.msg('考试结束！');
			}
			second--;
			var m = Math.floor(second / 60);
			countTime = second;
			var s = second - (m * 60);
			$('#timer').html("倒计时:" + m + "分" + s + "秒")
		}, 1000)
	}
	//限制退出
	window.signOutExam = function() {
		layer.confirm('试卷还未完成,确定要离开考试吗？', function(index) {
			location.href = 'ExamList.html';
			layer.close(index);
		})
	}
	form.on('submit(formDemo)', function(data) {
		var param = data.field;
		var user = JSON.parse(sessionStorage.getItem('student'))
		var checkboxList = []
		$("input[type='checkbox']:checked").each(function(index, item) {
			checkboxList.push({
				value: $(this).val(),
				name: $(this).attr("name")
			})
		})
		//把多选题答案分离打包----凸(艹皿艹 )
		var checkAnswerList = [];
		for (var i = 0; i < Multiple.length; i++) {
			let str='';
			$.each(checkboxList, function(index, item) {
				var Answer = item.name.split('-');
				if(Answer[0]==Multiple[i]){
					str+=item.value+','
				}
			});
			checkAnswerList.push({
				answer: str,
				questionId:Multiple[i]
			})
		}
		var radioList = [];
		$("input[type='radio']:checked").each(function(index, item) {
			radioList.push({
				answer: $(this).val(),
				questionId: $(this).attr("name")
			})
		})
		param.judge=null;
		param.single=radioList;
		param.multiple=checkAnswerList;
		param.testPaperId = ids.testPaperId;
		param.examId = ids.id;
		console.log(param)
		var url='/backExam/submitTestPaper';
		var flag=StudentPostMethod(url,param);
		if(flag){
			return layer.msg('提交成功!');
		}
	});

})
