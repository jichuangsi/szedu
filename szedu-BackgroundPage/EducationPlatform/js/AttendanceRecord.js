layui.use(['form', 'table'], function() {
	var form = layui.form,
		table = layui.table;
	var id = UrlSearch();
	table.render({
		elem: '#demo',
		method: "post",
		async: false,
		url: httpUrl() + '/TeacherLesson/getAttendanceByCourse?couseId=' + id,
		cols: [
			[{
					field: 'id',
					title: '序号',
					type: 'numbers'
				},
				{
					field: 'studentName',
					title: '姓名',
					align: 'center'
				},
				{
					field: 'studentId',
					title: '学号',
					align: 'center'
				}, {

					field: 'className',
					title: '班级',
					align: 'center'
				},
				{
					field: 'xname',
					title: '到课情况',
					align: 'center'
				},
				{
					field: 'atime',
					title: '时间',
					align: 'center'
				}
			]
		],
		skin: 'line',
		page: true,
		limit: 10,
		loading: true,
		request: {
			pageName: 'pageNum',
			limitName: "PageSize"
		},
		parseData: function(res) {
			var arr;
			var code;
			var total = 0;
			if(res.code == "0010") {
				arr = res.data.list;
				total = res.data.total;
				code = 0;
				var str = '本次课堂 <span>应到人数'+arr[0].sum+'人</span><span>实到人数'+arr[0].trueto+'</span><span>缺课人数'+arr[0].dutystudent+'人</span>';
				$('.kq').prepend(str)
			}
			return {
				"code": code,
				"msg": res.msg,
				"count": total,
				"data": arr
			};
		}
	});
})