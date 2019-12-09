layui.use("form", function() {
	var form = layui.form;
	var admin;
	form.on('submit(update_Pwd)', function(data) {
		var param = data.field;
		if(param.newPwd != param.yesPwd) {
			setMsg("两次密码不相同!", 7)
			return false;
		} else {
			$.ajax({
				type: "post",
				url: httpUrl() + "/back/user/updatePwd",
				async: false,
				headers: {
					'accessToken': getToken()
				},
				contentType: 'application/json',
				data: JSON.stringify(param),
				success: function(res) {
					if(res.code == '0010') {
						setMsg('修改成功', 2)
						layer.close(index);
					} else {
						setMsg(res.msg, 2)
						layer.close(index);
					}
				}
			});
			return false;
		}
	});
	form.verify({　　　　
		pwd: [/^((?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,12})$/, '密码必须为6-12位数字与字母混合']　　
	});
	getNode();
	//获取菜单节点
	function getNode() {
		var url = "/backRoleConsole/getAllStaticPage";
		$.ajax({
			type: "GET",
			url: httpUrl() + url,
			async: false,
			headers: {
				'accessToken': getToken()
			},
			success: function(res) {
				if(res.code == '0010') {
					GetMenu(res.data);
				} else if(res.code == '0031') {
					layui.notice.info("提示信息：权限不足");
				} else if(res.code == '0050') {
					//layui.notice.error("提示信息:错误!");
				}
			}
		});
	}
	//获取数据拼接菜单
	function GetMenu(menu) {
		var Nodes = [];
		for(var i = 0; i < menu.length; i++) {
			//把节点都获取出来
			Nodes.push(
				menu[i].nodeName
			)
		}
		$('#nav').empty();
		var content = '';
		var nodeList = norepeat(Nodes);
		nodeList.reverse();//数组反转
		for(var i = 0; i < nodeList.length; i++) {
			content += "<li>";
			content += '<a href="javascript:;">';
			content += '<i class="iconfont"></i>';
			content += '<cite>' + nodeList[i] + '</cite>';
			content += '<i class="iconfont nav_right">&#xe697;</i>';
			content += '</a>';
			content += '<ul class="sub-menu">';
			for(var j = 0; j < menu.length; j++) {
				if(nodeList[i] == menu[j].nodeName) {
					content += '<li>';
					content += '<a _href="pages' + menu[j].pageUrl + '">';
					content += '<i class="iconfont">&#xe6a7;</i>';
					content += '<cite>' + menu[j].pageName + '</cite>';
					content += '</a>';
					content += '</li>';
				}
			}
			content += '</ul>';
			content += "</li>";
		}
		$('#nav').append(content);
		var tab = {
			tabAdd: function(title, url, id) {
				element.tabAdd('xbs_tab', {
					title: title,
					content: '<iframe tab-id="' + id + '" frameborder="0" src="' + url + '" scrolling="yes" class="x-iframe"></iframe>',
					id: id
				})
			},
			tabDelete: function(othis) {
				element.tabDelete('xbs_tab', '44');
				othis.addClass('layui-btn-disabled');
			},
			tabChange: function(id) {
				element.tabChange('xbs_tab', id);
			}
		};
		
		$('.left-nav #nav li').click(function(event) {
			if($(this).children('.sub-menu').length) {
				if($(this).hasClass('open')) {
					$(this).removeClass('open');
					$(this).find('.nav_right').html('&#xe697;');
					$(this).children('.sub-menu').stop().slideUp();
					$(this).siblings().children('.sub-menu').slideUp();
				} else {
					$(this).addClass('open');
					$(this).children('a').find('.nav_right').html('&#xe6a6;');
					$(this).children('.sub-menu').stop().slideDown();
					$(this).siblings().children('.sub-menu').stop().slideUp();
					$(this).siblings().find('.nav_right').html('&#xe697;');
					$(this).siblings().removeClass('open');
				}
			} else {
				var url = $(this).children('a').attr('_href');
				var title = $(this).find('cite').html();
				var index = $('.left-nav #nav li').index($(this));
				for(var i = 0; i < $('.x-iframe').length; i++) {
					if($('.x-iframe').eq(i).attr('tab-id') == index + 1) {
						tab.tabChange(index + 1);
						event.stopPropagation();
						return;
					}
				};

				tab.tabAdd(title, url, index + 1);
				tab.tabChange(index + 1);
			}

			event.stopPropagation();
		})
	}
	

	//利用sort把多余的字段去除掉
	function norepeat(arr) {
		arr.sort();
		for(var i = 0; i < arr.length - 1; i++) {
			if(arr[i] == arr[i + 1]) {
				arr.splice(i, 1);
				i--;
			}
		}
		return arr;
	}

});