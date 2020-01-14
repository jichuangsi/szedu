layui.use(['form', 'table'], function() {
	var form = layui.form,
		table = layui.table;
	var user = JSON.parse(sessionStorage.getItem('user'))
	index()

	function index() {
		if (getToken() == undefined) {
			$('.frame').hide()
		} else {
			$('.countResource').html(user.countResource)
			$('.integral').html(user.integral);
			if (user.signin) {
				$('#sing').html("签到");
			} else {
				$('#sing').html("已签到");
				$('#sing').addClass('layui-btn-disabled');
				$('#sing').removeClass('layui-btn-normal');
			}

		}
	}

	window.SignIn = function() {
		if (user.signin) {
			$.ajax({
				type: "post",
				url: httpUrl() + "/teacherInfo/signin",
				async: false,
				headers: {
					'accessToken': getToken()
				},
				success: function(res) {
					if (res.code == '0010') {
						console.log(res);
						$('#sing').html("已签到");
						$('#sing').addClass('layui-btn-disabled');
						$('#sing').removeClass('layui-btn-normal');
						user.integral = res.data;
						user.signin = false;
						sessionStorage.setItem('user', JSON.stringify(user));
						index()
						layer.msg('签到成功！');
					} else {
						layer.msg('签到失败！');
					}
				}
			});
		} else {
			layer.msg('已签到，明天再来吧！');
		}

	}
	getResource();
	//获取近期资源
	function getResource() {
		var url = '/CourseWare/getPublicResourceByTime?pageNum=1&pageSize=8';
		var arr = ajaxGetData(url);
		var data=arr.list;
		console.log(arr);
		var str = '';
		$('#dateResource').empty();
		if (data.length > 0) {
			$.each(data, function(index, item) {
				str += '<div class="rmb-box">';
				str += '<div class="rmb-product">';
				str += '<a href=""><img src="./img/12.jpg" /></a>';
				str += '</div>';
				str += '<div class="rmb-icon-res"><span><i class="iconfont layui-icon-extendxin1"></i></span></div>';
				str += '<div class="rmb-res-title">' + item.resourceName + '</div>';
				str += '<ul>';
				str += '<li>上传者:<span class="teacherName">' + item.teacherName + '</span></li>';
				str += '<li>资源类型:<span class="teacherName"> ' + item.resourceType + '</span></li>';
				str += '<li>发布时间:<span class="date">' + item.createTime + '</span></li>';
				str += '<li>购买量:<span class="cont">' + item.buy + '</span></li>';
				str += '</ul>';
				str += '<div class="rmb-res-buy">';
				str += '<span class="layui-badge layui-bg-orange">￥</span>';
				str += '<span>' + item.integral + '积分</span>';
				str += '<span class="layui-btn layui-btn-normal  layui-btn-radius">购买资源</span>';
				str += '</div>';
				str += '</div>';
			});
		}
			$('#dateResource').append(str);
	}
	getHoldResource();
	//获取热门资源
	function getHoldResource() {
		var url = '/CourseWare/getPublicResource?pageNum=1&pageSize=8';
		var arr = ajaxGetData(url);
		var str = '';
		var data = arr.list;
		$('#countResource').empty();
		if (data.length > 0) {
			$.each(data, function(index, item) {
				str += '<div class="rmb-box">';
				str += '<div class="rmb-product">';
				str += '<a href=""><img src="./img/12.jpg" /></a>';
				str += '</div>';
				str += '<div class="rmb-icon-res"><span><i class="iconfont layui-icon-extendxin1"></i></span></div>';
				str += '<div class="rmb-res-title">' + item.resourceName + '</div>';
				str += '<ul>';
				str += '<li>上传者:<span class="teacherName">' + item.teacherName + '</span></li>';
				str += '<li>资源类型:<span class="teacherName"> ' + item.resourceType + '</span></li>';
				str += '<li>发布时间:<span class="date">' + item.createTime + '</span></li>';
				str += '<li>购买量:<span class="cont">' + item.buy + '</span></li>';
				str += '</ul>';
				str += '<div class="rmb-res-buy">';
				str += '<span class="layui-badge layui-bg-orange">￥</span>';
				str += '<span>' + item.integral + '积分</span>';
				str += '<span class="layui-btn layui-btn-normal  layui-btn-radius">购买资源</span>';
				str += '</div>';
				str += '</div>';
			});
		}
		$('#countResource').append(str);
	}
	getCourse();
	//获取精品课程
	function getCourse() {
		var url = '/TeacherCourse/topCourse';
		var arr = ajaxPostData(url);
		var str = '';
		console.log(arr)
		$('#coures').empty();
		if (arr.length > 0) {
			$.each(arr, function(index, item) {
				str += '<div class="rmb-box">';
				str += '<div class="rmb-product">';
				str += '<a href=""><img src="./img/12.jpg" /></a>';
				str += '</div>';
				str += '<div class="rmb-icon-product"><i class="iconfont layui-icon-extendsuo"></i></div>';
				str += '<div class="rmb-product-title"><a href="">电动汽车电池管理系统</a></div>';
				str += '<div class="rmb-product-res"><span>151</span>课时 <span>150</span>个资源</div>';
				str += '<div class="rmb-product-desc"><a href="">本课程将详细介绍电动车电理系统的基础原理，主要包括</a></div>';
				str += '</div>';
			});
		}
			$('#coures').append(str);
	}


});
