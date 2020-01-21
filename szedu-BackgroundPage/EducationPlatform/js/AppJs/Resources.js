var subjectId = null;
var lessionType = null;
layui.use(['form', 'table', 'laypage'], function() {
	var form = layui.form,
		laypage = layui.laypage,
		table = layui.table;
	var param = {
		"lessionType": lessionType,
		"pageNum": 1,
		"pageSize": 12,
		"subjectId": subjectId,
		"time": null,
		"timeName": ""
	}
	getResources(param);

	function getResources(param) {
		var url = '/studentLesson/getStudy';

		var arr = StudentPostMethod(url, param);
		var data = arr.list;
		var str = '';
		$('#resources').empty();
		if (data.length > 0) {
			$.each(data, function(index, item) {
				str += '<div class="layui-col-xs6 layui-col-sm3">';
				str += '<div class="res-box">';
				str += '<div class="rmb-product">';
				str += '<a href="#"><img src="../img/微信图片_20191113143904_WPS图片.png" /></a>';
				str += '</div>';
				str += '<div class="rmb-product-title">';
				str +=
					'<span>'+item.filename+'</span>';
					//<i class="iconfont layui-icon-extendxin1" style="color: #999999;float: right;"></i>
				str += '</div>';
				str += '<div class="rmb-product-content">';
				str += '<ul>';
				str += '<li>推送者：'+item.teacherName+'</li>';
				str += '<li>资源类型：文档资源</li>';
				str += '<li>推送时间:2019-12-21 8:30</li>';
				str += '</ul>';
				str += '</div>';
				str += '</div>';
				str += '</div>';
				str += '</div>';
			});
			$('#resources').append(str);
			laypage.render({
				elem: 'test1',
				limit: param.pageSize, //页面多少条
				count: arr.totalElements, //总数
				curr: param.pageNum,
				theme: '#1E9FFF',
				jump: function(obj, first) {
					console.log(first)
					//首次不执行
					if (!first) {
						param = {
							"lessionType": lessionType,
							"pageNum": obj.curr,
							"pageSize": 12,
							"subjectId": subjectId,
							"time": null,
							"timeName": ""
						}
						getResources(param);
					}
				}
			});
		}else{
			$('#resources').addClass('box-n');
				$('#resources').html('暂无学习资料!');
		}
	}
	getSubject();
	//获取科目
	function getSubject() {
		var url = '/TreeMenu/getTreeMenuByPid?pid=0';
		var arr = studentGetMethod(url);
		var str = '<option value="-1">请选择</option>';
		str += '';
		$('#sub').empty();
		$.each(arr, function(index, item) {
			str += '<option value="' + item.id + '">' + item.title + '</option>';
		});
		$('#sub').append(str);
		form.render();
	}
	getType();
	//获取资源类型
	function getType() {
		var url = '/CourseWare/getResourcesRule';
		var arr = studentGetMethod(url);
		var str = '';
		str += '<option value="-1">请选择</option>';
		str += '';
		$('#type').empty();
		$.each(arr, function(index, item) {
			str += '<option value="' + item.id + '">' + item.typeName + '</option>';
		});
		$('#type').append(str);
		form.render();
	}
	//资源
	form.on('select(type)', function(data) {
		var param = data.value;
		lessionType = param
		if (lessionType != -1) {
			getResources(subjectId, lessionType);
		}
	});
	//科目
	form.on('select(subject)', function(data) {
		var param = data.value;
		subjectId = param
		if (subjectId != -1) {
			getResources(subjectId, lessionType)
		}
	});


	//分页

})
