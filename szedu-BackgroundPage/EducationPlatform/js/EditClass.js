layui.use(['form', 'table','laydate'], function() {
	var form = layui.form,
		laydate=layui.laydate,
		table = layui.table;

	$('.time').each(function() {
		laydate.render({
			elem: this,
			type: 'date'
		});
	})
})