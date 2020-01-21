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
			$('.two').addClass('xs');
			$('.two').removeClass('yc');
			$('.two').siblings().removeClass('xs');
			$('.two').siblings().addClass('yc');
		} else if(str == 2) {
			$('.three').addClass('xs');
			$('.three').removeClass('yc');
			$('.three').siblings().removeClass('xs');
			$('.three').siblings().addClass('yc');
			//公共资源
		} else if(str == 3) {
			getTeacherClassRoom();
			$('.four').addClass('xs');
			$('.four').removeClass('yc');
			$('.four').siblings().removeClass('xs');
			$('.four').siblings().addClass('yc');
		}
	});
	$(document).on('click', '.toaddClass', function() {
		location.href = 'AddClass.html'
	})
	//	getTeacherClassRoom();
	//获取我的课堂
	/*	function getTeacherClassRoom() {
			var param = {
				subjectId: '',
				lessionType: '',
				time: null,
				pageSize: 10,
				pageNum: 1
			}
			var arr
			var url = '/TeacherLesson/getAllLesson';
			$.ajax({
				type: "post",
				url: httpUrl() + url,
				async: false,
				headers: {
					'accessToken': getToken(),
				},
				contentType: 'application/json',
				data: JSON.stringify(param),
				success: function(res) {
					if(res.code == '0010') {
						arr = res.data.content;
					} else {
						layer.msg('获取课堂信息错误！');
					}
				}
			});

			str = '';
			$('.teacherClass').empty();
			for(var i = 0; i < arr.length; i++) {
				str += '<div class="rmb-box">';
				str += '<div class="rmb-product">';
				str += '<a href=""><img src="../img/12.jpg" /></a>';
				str += '</div>';
				str += '<div class="rmb-res-title">' + arr[i].courseTitle + '</div>';
				str += '<ul>';
				str += '<li>上传者:<span class="teacherName">' + arr[i].teacherName + '</span></li>';
				str += '<li>资源类型:<span class="teacherName"> 视频资源</span></li>';
				str += '<li>发布时间:<span class="date"> 2019/11/14</span></li>';
				str += '<li>购买量:<span class="cont"> 555526</span></li>';
				if(arr[i].status == "N") {
					str += '<li>课堂状态:<span style="color: red;"> 未发布</span></li>';
				} else if(arr[i].status == "P") {
					str += '<li>课堂状态:<span style="color: red;"> 已发布</span></li>';
				} else if(arr[i].status == "H") {
					str += '<li>课堂状态:<span style="color: red;"> 上课中</span></li>';
				} else if(arr[i].status == "F") {
					str += '<li>课堂状态:<span style="color: red;">已结束</span></li>';
				}
				//AttendanceRecord
				str += '</ul>';
				str += '<div class="box-content-btn">';
				str += '<input type="hidden" name="id" value="' + arr[i].id + '" />';
				if(arr[i].status == "N") {
					str += '<button class="layui-btn layui-btn-normal layui-btn-radius" onclick="release(this,1)">发布</button>';
					str += '<button class="layui-btn layui-btn-normal layui-btn-radius" onclick="CopyClassRoom(this)">复制</button>';
					str += '<button class="layui-btn layui-btn-normal layui-btn-radius" onclick="DeleteClassRoom(this)">删除</button>';
				} else if(arr[i].status == "P" || arr[i].status == "H") {
					str += '<button class="layui-btn layui-btn-normal layui-btn-radius" onclick="release(this,2)">上课</button>';
					str += '<button class="layui-btn layui-btn-disabled layui-btn-radius">到课情况</button>';
				}
				//			else if(arr[i].status == "H") {
				//				str += '<button class="layui-btn layui-btn-disabled layui-btn-radius">上课</button>';
				//				str += '<button class="layui-btn layui-btn-primary layui-btn-radius toAttendanceRecord"onclick="getByCourse(this)" >到课情况</button>';
				//			}
				else if(arr[i].status == "F") {
					str += '<button class="layui-btn layui-btn-disabled layui-btn-radius">上课</button>';
					str += '<button class="layui-btn layui-btn-primary layui-btn-radius toAttendanceRecord" onclick="getByCourse(this)">到课情况</button>';

				}
				str += '</div>';
				str += '</div>';
			}
			$('.teacherClass').append(str);
		}*/

	//改变课堂状态
	window.release = function(obj, num) {
		var staus;
		var msg;
		if(num == 1) {
			staus = 'P';
			msg = '确认要发布该课堂吗？'
		} else if(num == 2) {
			staus = 'H';
			msg = '确认要开始上课吗？';
		}
		layer.confirm(msg, function(index) {

			var id = $(obj).parent().find('input[name=id]').val();
			var url = '/TeacherLesson/updateCourseStatus?courseId=' + id + '&staus=' + staus;
			ajaxGET(url);
			getTeacherClassRoom();
			layer.close(index);
		})
	}
	//复制课堂
	window.CopyClassRoom = function(obj) {
		layer.confirm('确认要复制该课堂吗？', function(index) {
			var id = $(obj).parent().find('input[name=id]').val();
			var url = '/TeacherLesson/copyLession?id=' + id;
			ajaxPOST(url);
			getTeacherClassRoom();
			layer.close(index);
		})
	}
	//删除课堂
	window.DeleteClassRoom = function(obj) {
		layer.confirm('确认要删除该课堂吗？', function(index) {
			var id = $(obj).parent().find('input[name=id]').val();
			//			var id='402881836f3cdddb016f3cdfaadf0002';
			var url = '/TeacherLesson/deleteLesson?lessonId=' + id;
			ajaxPOST(url);
			getTeacherClassRoom();
			layer.close(index);
		})
	}
	//到课情况
	window.getByCourse = function(obj) {
		var id = $(obj).parent().find('input[name=id]').val();
		location.href = 'AttendanceRecord.html?lessonId=' + id
	}

	function getTeacherClassRoom() {
		flow.load({
			elem: ".teacherClass",
			done: function(page, next) { //执行下一页的回调
				//模拟数据插入
				var lis = []
				var param = {
					subjectId: '',
					lessionType: '',
					time: null,
					pageSize: 32,
					pageNum: page
				}
				$.ajax({
					type: "post",
					url: httpUrl() + '/TeacherLesson/getAllLesson',
					async: false,
					headers: {
						'accessToken': getToken()
					},
					contentType: 'application/json',
					data: JSON.stringify(param),
					success: function(res) {
						var arr = res.data.content;
						var str = '';
						if(arr.length > 0) {
							for(var i = 0; i < arr.length; i++) {
								str = '';
								//								$('.teacherClass').empty();
								for(var i = 0; i < arr.length; i++) {
									str += '<div class="rmb-box">';
									str += '<div class="rmb-product">';
									str += '<a href=""><img src="../img/12.jpg" /></a>';
									str += '</div>';
									str += '<div class="rmb-res-title">' + arr[i].courseTitle + '</div>';
									str += '<ul>';
									str += '<li>上传者:<span class="teacherName">' + arr[i].teacherName + '</span></li>';
									str += '<li>资源类型:<span class="teacherName"> 视频资源</span></li>';
									str += '<li>发布时间:<span class="date"> ' + new Date(+new Date(arr[i].startTime) + 8 * 3600 * 1000).toISOString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '') + '</span></li>';
									str += '<li>购买量:<span class="cont"> 555526</span></li>';
									if(arr[i].status == "N") {
										str += '<li>课堂状态:<span style="color: red;"> 未发布</span></li>';
									} else if(arr[i].status == "P") {
										str += '<li>课堂状态:<span style="color: red;"> 已发布</span></li>';
									} else if(arr[i].status == "H") {
										str += '<li>课堂状态:<span style="color: red;"> 上课中</span></li>';
									} else if(arr[i].status == "F") {
										str += '<li>课堂状态:<span style="color: red;">已结束</span></li>';
									}
									//AttendanceRecord
									str += '</ul>';
									str += '<div class="box-content-btn">';
									str += '<input type="hidden" name="id" value="' + arr[i].id + '" />';
									if(arr[i].status == "N") {
										str += '<button class="layui-btn layui-btn-normal layui-btn-radius" onclick="release(this,1)">发布</button>';
										str += '<button class="layui-btn layui-btn-normal layui-btn-radius" onclick="CopyClassRoom(this)">复制</button>';
										str += '<button class="layui-btn layui-btn-normal layui-btn-radius" onclick="DeleteClassRoom(this)">删除</button>';
									} else if(arr[i].status == "P" || arr[i].status == "H") {
										str += '<button class="layui-btn layui-btn-normal layui-btn-radius" onclick="release(this,2)">上课</button>';
										str += '<button class="layui-btn layui-btn-disabled layui-btn-radius">到课情况</button>';
									}
									//			else if(arr[i].status == "H") {
									//				str += '<button class="layui-btn layui-btn-disabled layui-btn-radius">上课</button>';
									//				str += '<button class="layui-btn layui-btn-primary layui-btn-radius toAttendanceRecord"onclick="getByCourse(this)" >到课情况</button>';
									//			}
									else if(arr[i].status == "F") {
										str += '<button class="layui-btn layui-btn-disabled layui-btn-radius">上课</button>';
										str += '<button class="layui-btn layui-btn-primary layui-btn-radius toAttendanceRecord" onclick="getByCourse(this)">到课情况</button>';

									}
									str += '</div>';
									str += '</div>';
								}
							}
							if(page == 1) {
								$('.teacherClass').find('.rmb-box').hide()
								$('.teacherClass').prepend(str)
							} else {
								$('.teacherClass').find('.layui-flow-more').before(str)
								//								$('.layui-flow-more').before(str)
							}

						} else {
							return
						}
						next(lis.join(''), page < res.data.totalPages); //假设总页数为 10
						form.render();
					}
				});
			}
		});
	}
	$('.layui-progress').hide();
	//上传本地文件
	var resourceId;
	form.on('select(res)', function(data) {
		var param = data.value;
		if(param != -1) {
			$('.load').attr('id', 'demo');
			upload.render({
				elem: '#demo',
				accept: 'file',
				size: 300 * 1024,
				url: httpUrl() + '/CourseWare/localUpload',
				headers: {
					'accessToken': getToken()
				},
				progress: function(value) { //上传进度回调 value进度值
					$('.layui-progress').show();
					element.progress('progressBar', value + '%') //设置页面进度条
				},
				data: {
					resourceType: function() {
						console.log(resourceId)
						return param;
					},
					resourceId: function() {
						if(resourceId != undefined) {
							return resourceId;
						}
						return '';
					}

				},
				done: function(res) {
					//如果上传失败
					if(res.code == '0010') {
						resourceId = res.data;
						form.val('test', {
							id: res.data
						});
						return layer.msg('上传成功！');
					} else {
						return layer.msg('上传失败,' + res.msg);
					}
				}
			});
		}
	})

	//上传封面
	upload.render({
		elem: '#Cover',
		size: 2 * 1024,
		exts: 'jpg|png|jpeg',
		url: httpUrl() + '/CourseWare/localUploadCover',
		headers: {
			'accessToken': getToken()
		},
		data: {
			resourceId: function() {
				if(resourceId != undefined) {
					return resourceId;
				}
				return '';
			}
		},
		done: function(res) {
			//如果上传失败
			if(res.code == '0010') {
				resourceId = res.data;
				form.val('test', {
					resourceId: res.data
				});
				return layer.msg('上传成功！');
			} else {
				return layer.msg('上传失败,' + res.msg);
			}
		}
	});
	/*我的资源-本地上传*/
	$(document).on('click', '.load', function() {
		var op = $("#res option:selected");
		if(op.val() == -1) {
			return setMsg('请先选择资源类型！', 7)
		}
	});
	form.on('submit(formModifyDemo)', function(data) {
		var param = data.field;
		var url = '/CourseWare/saveCourse';
		if(param.id == '' || param.id == null) {
			return setMsg("请先上传资源！", 7)
		}
		ajaxPOST(url, param);
		layer.close(index);
		setTimeout(function() {
			location.reload()
		}, 1000)

	});
	getresources();

	function getresources() {
		var url = httpUrl() + '/CourseWare/getResourcesRule';
		var headers = {
			'accessToken': getToken()
		}
		$.get(url, function(res) {
			if(res.code == '0010') {
				$('#res').empty();
				$('#qRes').empty()
				var str = '';
				str += '<option value="-1">上传标签</option>';
				$.each(res.data, function(index, item) {
					str += '<option value="' + item.id + '">' + item.typeName + '</option>'
				})
				$('#res').append(str);
				$('#qRes').append(str)
				form.render('select');
			}
		})
	}

	getSubject();
	//获取科目
	function getSubject() {
		$('#subject').empty();
		var options = '<option value="-1" selected="selected">' + "请选择" + '</option>';
		$.ajax({
			type: "get",
			url: httpUrl() + "/TreeMenu/getAllTreeMenu",
			async: true,
			headers: {
				'accessToken': getToken()
			},
			success: function(res) {
				if(res.code == '0010') {
					arr = res.data
					if(arr == null || arr == undefined) {
						options += '<option value="">暂无科目信息</option>'
					} else {
						for(var i = 0; i < arr.length; i++) {
							if(arr[i].pid == 0) {
								options += '<option value="' + arr[i].id + '" >' + arr[i].title + '</option>'
							}
						}
					}
					$('#subject').append(options);
					form.render('select');
				} else {
					layui.notice.error("提示信息:获取科目信息错误!");
				}
			}
		});
	}
	getLabe();

	function getLabe() {
		var url = '/CourseWare/getUploadLabel';
		var arr = ajaxGetData(url);
		$('#labe').empty()
		$('#qLabe').empty()
		var str = '';
		str += '<option value="-1">上传标签</option>';
		$.each(arr, function(index, item) {
			str += '<option value="' + item.id + '">' + item.name + '</option>'
		});
		$('#labe').append(str);
		$('#qLabe').append(str);
		form.render('select');
	}
	//删除本地资源
	window.deleteAttachment = function(obj) {
		layer.confirm('确认要删除该资源吗？', function(index) {
			var id = $(obj).parent().find('input[name=id]').val();
			var url = '/CourseWare/deleteResourcesByid?id=' + id;
			//			var url = '/CourseWare/localDownload?id=' + id
			ajaxPOST(url);
			getTeacherRes();
			layer.close(index);
		})
	}
	form.on('select(qLabe)', function(data) {
		var type = data.value;
		if(type != -1) {
			getTeacherRes(type, '');
		}
	})
	form.on('select(qRes)', function(data) {
		var label = data.value;
		if(label != -1) {
			getTeacherRes('', label);
		}
	})
	//获取老师 的资源-我的资源
	getTeacherRes();

	function getTeacherRes(label, type) {
		if(label == undefined) {
			label = '';
		}
		if(type == undefined) {
			type = '';
		}
		flow.load({
			elem: ".teacherResources",
			done: function(page, next) { //执行下一页的回调
				var lis = []
				var str = '';
				var url = "/CourseWare/getCourseWareListByTeacher?teacherId=" + user.teacherId + '&pageNum=' + page + '&pageSize=32&type=' + type + '&label=' + label;
				var arr = ajaxGetData(url);
				if(arr.content.length > 0) {
					$.each(arr.content, function(index, item) {
						str += '<div class="rmb-box">';
						str += '<div class="rmb-product">';
						str += '<a href=""><img src="../img/12.jpg" /></a>';
						str += '</div>';
						str += '<div class="rmb-res-title">' + item.filename + '</div>';
						str += '<ul>';
						str += '<li>上传者:<span class="teacherName">' + item.teacherName + '</span></li>';
						str += '<li>修改时间:<span class="date">' + new Date(+new Date(item.createTime) + 8 * 3600 * 1000).toISOString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '') + '</span></li>';
						if(item.isCheck == 1) {
							str += '<li>资源状态:<span style="color: red;">上传待审核</span></li>';
						} else if(item.isCheck == 2) {
							if(item.isShareCheck == 1) {
								str += '<li>资源状态:<span style="color: red;">分享待审核</span></li>';
							} else if(item.isShareCheck == 2) {
								str += '<li>资源状态:<span style="color: red;">分享审核通过</span></li>';
							} else if(item.isShareCheck == 4) {
								str += '<li>资源状态:<span style="color: red;">分享审核驳回</span></li>';
							} else {
								str += '<li>资源状态:<span style="color: red;">审核通过</span></li>';
							}
						} else if(item.isCheck == 3) {
							str += '<li>资源状态:<span style="color: red;">审核驳回</span></li>';
						}
						str += '</ul>';
						str += '<div class="box-content-btn-left">';
						if(item.isCheck == 1) {
							str += '<input type="hidden"  name="id" value="' + item.id + '"/>';
							str += '<div class="layui-btn layui-btn-normal layui-btn-radius" onclick="deleteAttachment(this)">删除</div>';
						} else if(item.isCheck == 2) {
							if(item.isShareCheck == 1) {
								str += '<input type="hidden"  name="id" value="' + item.id + '"/>';
								str += '<div class="layui-btn layui-btn-normal layui-btn-radius" onclick="cancelShare(this)">取消分享</div>'
								str += '<div class="layui-btn layui-btn-normal layui-btn-radius"onclick="getClassInfo(this)">推送</div>'
								str += '<div class="layui-btn layui-btn-normal layui-btn-radius" onclick="deleteAttachment(this)">删除</div>';
							} else if(item.isShareCheck == 2) {
								str += '<input type="hidden"  name="id" value="' + item.id + '"/>';
								str += '<div class="layui-btn layui-btn-normal layui-btn-radius" onclick="cancelShare(this)">取消分享</div>'
								str += '<div class="layui-btn layui-btn-normal layui-btn-radius"onclick="getClassInfo(this)">推送</div>'
								str += '<div class="layui-btn layui-btn-normal layui-btn-radius" onclick="deleteAttachment(this)">删除</div>';
							} else if(item.isShareCheck == 4) {
								str += '<input type="hidden"  name="id" value="' + item.id + '"/>';
								str += '<div class="layui-btn layui-btn-normal layui-btn-radius" onclick="share(this)">分享</div>'
								str += '<div class="layui-btn layui-btn-normal layui-btn-radius"onclick="getClassInfo(this)">推送</div>'
								str += '<div class="layui-btn layui-btn-normal layui-btn-radius" onclick="deleteAttachment(this)">删除</div>';
							} else {
								str += '<input type="hidden"  name="id" value="' + item.id + '"/>';
								str += '<div class="layui-btn layui-btn-normal layui-btn-radius" onclick="share(this)">分享</div>'
								str += '<div class="layui-btn layui-btn-normal layui-btn-radius"onclick="getClassInfo(this)">推送</div>'
								str += '<div class="layui-btn layui-btn-normal layui-btn-radius" onclick="deleteAttachment(this)">删除</div>';
							}
						} else if(item.isCheck == 3) {
							str += '<input type="hidden"  name="id" value="' + item.id + '"/>';
							str += '<div class="layui-btn layui-btn-normal layui-btn-radius" onclick="deleteAttachment(this)">删除</div>';
						}
						str += '</div>';
						str += '</div>';
					});

				} else {
					return layer.msg('暂时无资源，快去添加吧!');
				}
				if(page == 1) {
					$('.teacherResources').find('.rmb-box').hide()
					$('.teacherResources').prepend(str)
				} else {
					$('.teacherResources').find('.layui-flow-more').before(str)
				}
				next(lis.join(''), page < arr.totalPages); //假设总页数为 10
				form.render();
			}
		});
	}
	//分享资源
	window.share = function(obj) {
		layer.prompt({
			formType: 0,
			value: '',
			title: '请输入分享购买的积分数目1~100',
			area: ['400px', '350px'] //自定义文本域宽高
		}, function(value, index, elem) {
			var integral;
			if(!isNaN(value)) {
				integral = parseInt(value)
				if(integral>=100||integral<=0){
					return layer.msg('请输入正确的数值！');
				}
			}else{
				return layer.msg('请输入正确的积分数！');
			}
			var id = $(obj).parent().find('input[name=id]').val();
			var url = "/CourseWare/shareResource?resourceId=" + id + "&status=1&integral=" + integral;
			ajaxGET(url);
			getTeacherRes();
			layer.close(index);
		});
		//		layer.confirm('确认要分享资源吗？', function(index) {
		//			var id = $(obj).parent().find('input[name=id]').val();
		//			var url = "/CourseWare/shareResourceCheck?resourceId=" + id + "&status=1";
		//			ajaxGET(url);
		//			getTeacherRes();
		//			layer.close(index);
		//		})
	}

	//取消分享
	window.cancelShare = function(obj) {
		layer.confirm('确认要取消分享该资源吗？', function(index) {
			var id = $(obj).parent().find('input[name=id]').val();
			var url = "/CourseWare/shareResourceCheck?resourceId=" + id + "&status=3";
			ajaxGET(url);
			getTeacherRes();
			layer.close(index);
		})
	}
	var demo1 = xmSelect.render({
		el: '.ClassOption',
		prop: {
			name: 'className',
			value: 'id',
		},
		data: getClass()
	})
	//推送资源

	getClass();
	//获取班级信息
	function getClass() {
		var url = '/classConsole/getAllClass';
		var arr = ajaxPostData(url);
		return arr;
	}

	window.getClassInfo = function(obj) {
		var id = $(obj).parent().find('input[name=id]').val();
		form.val('shareClass', {
			"resourceId": id
		});
		index = layer.open({
			type: 1,
			area: ['600px', '350px'],
			anim: 2,
			title: '推送班级',
			maxmin: true,
			shadeClose: true, //点击遮罩关闭
			content: $('#shareClass')
		});
	}
	form.on('submit(formShareDemo)', function(data) {
		var param = data.field;
		param.classid = param.select.split(',');
		var url = '/CourseWare/pushResourceToClass';

		ajaxPOST(url, param);
		layer.close(index);
	});
	publicResource();

	function publicResource() {
		flow.load({
			elem: ".publicResource",
			done: function(page, next) { //执行下一页的回调
				//模拟数据插入
				var lis = [];
				var url = '/CourseWare/getPublicResource?pageNum=' + page + '&pageSize=32';
				var arr = ajaxGetData(url);
				var data = arr.list;
				if(data.length > 0) {
					var str = '';
					$.each(data, function(index, item) {
						str += '<div class="rmb-box">';
						str += '<div class="rmb-product">';
						str += '<a href=""><img src="../img/12.jpg" /></a>';
						str += '</div>';
						str += '<div class="rmb-icon-res"><span><i class="iconfont layui-icon-extendxin1"></i></span></div>';
						str += '<div class="rmb-res-title">' + item.resourceName + '</div>';
						str += '<ul>';
						str += '<li>上传者:<span class="teacherName">' + item.teacherName + '</span></li>';
						str += '<li>资源类型:<span class="teacherName">' + item.resourceType + '</span></li>';
						str += '<li>发布时间:<span class="date">' + new Date(+new Date(item.createTime) + 8 * 3600 * 1000).toISOString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '') + '</span></li>';
						str += '<li>购买量:<span class="cont"> 555526</span></li>';
						str += '</ul>';
						str += '<div class="rmb-res-buy">';
						str += '<span class="layui-badge layui-bg-orange">￥</span>';
						str += '<span>' + item.integral + '积分</span>';
						str += '<input type="hidden"  name="integral" value="' + item.integral + '"/>';
						str += '<input type="hidden"  name="id" value="' + item.resourceId + '"/>';
						if(item.isBuy == 1) {
							str += '<span class="layui-btn layui-btn-primary layui-btn-radius" >已购买</span>';
						} else {
							str += '<span class="layui-btn layui-btn-normal  layui-btn-radius" onclick="buyResource(this)">购买资源</span>';
						}
						str += '</div>';
						str += '</div>';
					});
					if(page == 1) {
						$('.publicResource').find('.rmb-box').hide()
						$('.publicResource').prepend(str)
						form.render();
					} else {
						$('.publicResource').find('.layui-flow-more').before(str)
					}
				} else {
					return
				}
				next(lis.join(''), page < arr.pages); //假设总页数为 10

			}
		});
	}
	//购买资源
	window.buyResource = function(obj) {
		layer.confirm('确认要购买该资源吗？', function(index) {
			var id = $(obj).parent().find('input[name=id]').val();
			var integral = $(obj).parent().find('input[name=integral]').val();
			var url = '/CourseWare/buyResourcesByid?id=' + id;
			if(integral > user.integral) {
				return setMsg("积分不足！", 5)
			} else {
				ajaxPOST(url);
				publicResource();
				//				user.integral = user.integral - integral;
				//				sessionStorage.setItem('user', JSON.stringify(user));
			}
			layer.close(index);
		})
	}

	//收藏资源
	
	//

})