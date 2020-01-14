layui.use(['form', 'table', 'element', 'xmSelect'], function() {
	var form = layui.form,
		xmSelect = layui.xmSelect,
		element = layui.element,
		table = layui.table;
	element.on('tab(docDemoTabBrief)', function(data) {
		if(data.index == 0) {
			getTeacherMsg();
		} else {
			getTeacherSysMsg();
		}
	});
	//获取老师的系统消息
	//	getTeacherSysMsg();
	var user = JSON.parse(sessionStorage.getItem('user'))

	function getTeacherSysMsg() {
		var url = '/teacherInfo/getTeacherMessage?pageNum=1&pageSize=30';
		var arr = ajaxGetData(url);
		$('#teaccherSystemMessage').empty();
		var str = '';
		if(arr.length > 0) {
			$.each(arr, function(index, item) {
				str += '<div class="layui-card" onclick="read(this)">'
				str += '<div class="layui-card-header">';
				str += '<input type="hidden" name="id" value="' + item.id + '" />';

				if(item.alreadyRead!="false") {
					str += '<i class="layui-icon layui-icon-speaker"></i><span>来自</span><span>系统</span> <span>' + new Date(+new Date(item.time) + 8 * 3600 * 1000).toISOString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '') + '</span>';
					str += '<span class="alreadyRead">已读</span>'
				} else {
					str += '<i class="layui-icon layui-icon-speaker card-red"></i><span>来自</span><span>系统</span> <span>' + new Date(+new Date(item.time) + 8 * 3600 * 1000).toISOString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '') + '</span>';
					str += '<span class="alreadyRead">未读</span>'
				}
				str += '<span></span>';
				str += '</div>';
				str += '<div class="layui-card-body">' + item.message + '';
				str += '</div>';
				str += '</div>';
			});
			$('#teaccherSystemMessage').append(str);
		} else {
			$('#teaccherSystemMessage').html("<div style='color: darkgrey;margin-top: 10px;text-align: center;'>近期暂时没有系统信息</div>");
			return layer.msg('近期暂时没有系统信息');
		}
	}

	getTeacherMsg();

	function getTeacherMsg() {
		var url = '/teacherInfo/getTeacherInteractionMessage?pageNum=1&pageSize=30';
		var arr = ajaxGetData(url);
		$('#teacherMessage').empty();
		var str = '';
		if(arr.length > 0) {
			$.each(arr, function(index, item) {
				if(item.senderid == user.teacherId) {
					str += '<div class="layui-card" >'
				} else {
					if(item.alreadyRead == "false") {
						str += '<div class="layui-card" onclick="read(this)">'
					} else {
						str += '<div class="layui-card" >';
					}
				}

				str += '<div class="layui-card-header">';
				str += '<input type="hidden" name="id" value="' + item.id + '" />';
				if(item.senderid == user.teacherId) {
					str += '<i class="icon iconfont layui-icon-extendfasongyoujian"></i>';
					str += '<span>发送给</span><span>' + item.recipientName + '</span>'
					str += ' <span>' + new Date(+new Date(item.time) + 8 * 3600 * 1000).toISOString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '') + '</span>';
					str += '<input type="hidden" name="senderid" value="' + item.recipientId + '" />';
					str += '<input type="hidden" name="name" value="' + item.recipientName + '" />';
					if(item.send == 'Y') {
						str += '<span class="alreadyRead">已发送</span>'
					} else {
						str += '<span class="alreadyRead">未发送</span>'
					}
				} else {
					str += '<i class="icon iconfont layui-icon-extendreceive-mail"></i>';
					str += '<span>来自</span><span>' + item.senderName + '</span>'
					str += '<input type="hidden" name="senderid" value="' + item.senderid + '" />';
					str += '<input type="hidden" name="name" value="' + item.senderName + '" />';
					str += ' <span>' + new Date(+new Date(item.time) + 8 * 3600 * 1000).toISOString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '') + '</span>';
					if(item.alreadyRead != "false") {
						str += '<span class="alreadyRead">已读</span>'
					} else {
						str += '<span class="alreadyRead">未读</span>'
					}
				}
				str += '<span onclick="quickReply(this)">快捷回复</span>'
				str += '</div>';
				str += '<div class="layui-card-body">' + item.message + '';
				str += '</div>';
				str += '</div>';
			});
			$('#teacherMessage').append(str)
		} else {
			$('#teacherMessage').html("<div style='color: darkgrey;margin-top: 10px;text-align: center;'>暂无互动消息</div>");
			return layer.msg('暂无互动消息！');
		}
	}

	//发送信息
	form.on('submit(formShareDemo)', function(data) {
		var param = data.field;

		var arr = book.getValue();
		console.log(arr)
		recipient = [];
		$.each(arr, function(index, item) {
			recipient.push({
				id: item.value,
				name: item.name
			})
		});
		param.recipient = recipient;
		console.log(param);
		var url = '/teacherInfo/addInteractionMessage';
		var flag = ajaxPostData(url, param);
		if(flag) {
			getTeacherMsg();
			layer.close(index);
			return layer.msg('发送成功！');
		}
	});

	//获取联系人

	//确认已读
	window.read = function(obj) {
		var id = $(obj).find('input[name=id]').val();
		var url = '/teacherInfo/updateTeacherMessage?id=' + id;
		var arr = ajaxPostData(url);
		if(arr) {
			getMsg()
			$(obj).find('i').removeClass('card-red');
			$(obj).find('.alreadyRead').html('已读');
		}
	}
	//快捷回复
	window.quickReply = function(obj) {
		console.log()
		var name = $(obj).parent().find('input[name=name]').val();
		layer.prompt({
			formType: 2,
			value: '',
			title: '回复' + name + '',
			area: ['400px', '350px'] //自定义文本域宽高
		}, function(value, index, elem) {
			var sendId = $(obj).parent().find('input[name=senderid]').val();
			var url = "/teacherInfo/addInteractionMessage";
			var param = {};
			param.message = value;
			var recipient = [{
				id: sendId,
				name: name
			}]
			param.recipient = recipient;
			var flag = ajaxPostData(url, param);
			if(flag) {
				getTeacherMsg();
				layer.close(index);
				return layer.msg('发送成功！');
			}
		});
	}
	var classList = [];
	var Colleague = [];
	//先查询班级
	getTeacherClass();

	function getTeacherClass() {
		var url = '/teacherInfo/getClassByTeacherId';
		var arr = ajaxGetData(url);
		$.each(arr, function(index, item) {
			classList.push({
				name: item.className,
				value: item.classId,
				pid: 0,
				children: []
			})
		});
	}
	//查询老师所有同事
	getTeacherColleague();

	function getTeacherColleague() {
		var url = '/teacherInfo/getAllTeacher';
		var arr = ajaxGetData(url);
		$.each(arr, function(index, item) {
			Colleague.push({
				name: item.teacherName,
				value: item.teacherId,
				pid: 1
			})
		});
	}
	//编辑通讯录
	var book = xmSelect.render({
		el: '#book',
		autoRow: true,
		height: '200',
		direction: 'down',
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
						var list = getClassById(item.value)
						console.log(list)
						if(list.length == 0) {
							cb(getClassById(item.value))
							return layer.msg('该班级暂未添加学生！')
						} else {
							cb(getClassById(item.value))
						}

					}
				}, 500)
			}
		},
		height: 'auto',
		data() {
			return getTeacherAddressBook();
		}
	})

	function getTeacherAddressBook() {
		var data = [{
			name: '我的班级',
			value: -1,
			children: classList
		}, {
			name: '我的同事',
			value: -2,
			children: Colleague
		}];
		return data;
	}
	//根据班级id获取学生
	function getClassById(id) {
		var url = '/teacherInfo/getStudentByClassId?classId=' + id;
		var arr = ajaxGetData(url);
		console.log(arr);
		var data = [];
		$.each(arr, function(index, item) {
			data.push({
				name: item.name,
				value: item.id
			})
		});
		return data;
	}

})