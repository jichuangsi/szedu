layui.use(['form', 'table', 'rate','element'], function() {
	var form = layui.form,
		rate = layui.rate,
		element=layui.element,
		table = layui.table;
	var cont = 5;
	$('.rate').each(function() {
		console.log(cont)
		rate.render({
			elem: this,
			length: cont,
			value: cont,
			half: true,
			readonly: true
		})
		cont--
	})
})