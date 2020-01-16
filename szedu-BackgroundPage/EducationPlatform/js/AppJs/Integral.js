layui.use(['form', 'table'], function() {
	var form = layui.form,
		table = layui.table;
	getStudentRank();
	//获取学生积分排名
	function getStudentRank() {
		var url = '/studentInfo/integralRanking?pageNum=1&pageSize=11';
		var arr = studentGetMethod(url);
		console.log(arr);
		$('#rank').empty();
		var str = '';
		var count = 1
		$.each(arr, function(index, item) {

		});
		$.each(arr, function(index, item) {
			if(item.myrank == null) {
				str += '<li><div class="rmb-rank">';
				if(count == 1) {
					str += '<img src="../img/personal_ranking_icon_one@2x.png" />' + count;
				} else if(count == 2) {
					str += '<img src="../img/personal_ranking_icon_two@2x.png" />' + count;
				} else if(count == 3) {
					str += '<img src="../img/personal_ranking_icon_three@2x.png" />' + count;
				} else {
					str += '<img class="w" />' + count;
				}
				str += '<img src="../img/home_icon_headportrait_image.png" /> ';
				if(count < 4) {
					str += '<span class="stuName">' + item.studentName + '</span><span class="ph">' + item.integral + '</span>积分';
				} else {
					str += '<span class="stuName">' + item.studentName + '</span><span>' + item.integral + '</span>积分';
				}

				str += '</div>';
				str += '</li>';
				count++
			}else{
				$('#myrank').empty();
				var content='';
				content+='<img class="w" />'+item.myrank+'<img src="../img/home_icon_headportrait_image.png" /> <span class="stuName">'+item.studentName+'</span><span class="ph">'+ item.integral+'</span>积分';
				$('#myrank').append(content)
			}

		});
		$('#rank').append(str);
	}
	IntegralRule();
	//获取积分规则
	function IntegralRule() {
		var url = '/studentInfo/getintegralRule?pageNum=1&pageSize=10';
		var arr = studentGetMethod(url);
		$('#rule').empty();
		var str = '';
		var count = 1;
		$.each(arr.content, function(index, item) {
			str += '' + count + '.'
			str += '' + item.type + '<br/>';
			str += '' + item.content + "<br/>"
			count++;
		});
		$('#rule').append(str)
	}

})