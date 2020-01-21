var second = 0;
window.setInterval(function() {
	second++;
}, 1000);
var tjArr = localStorage.getItem("jsArr") ? localStorage.getItem("jsArr") : '[{}]';
$.cookie('tjRefer', getReferrer(), {
	expires: 1,
	path: '/'
});

window.onbeforeunload = function() {
	if ($.cookie('tjRefer') == '') {
		var tjT = eval('(' + localStorage.getItem("jsArr") + ')');
		if (tjT) {
			tjT[tjT.length - 1].time += second;
			var jsArr = JSON.stringify(tjT);
			localStorage.setItem("jsArr", jsArr);
		}
	} else {
		var tjArr = localStorage.getItem("jsArr") ? localStorage.getItem("jsArr") : '[{}]';
		var dataArr = {
			url: location.href,
			time: second,
			refer: getReferrer(),
			timeIn: Date.parse(new Date()),
			timeOut: Date.parse(new Date()) + (second * 1000)
		};
		tjArr = eval( tjArr);
		tjArr.push(dataArr);
		tjArr = JSON.stringify(tjArr);
		localStorage.setItem("jsArr", tjArr);
	}
};

function getReferrer() {
	var referrer = '';
	try {
		referrer = window.top.document.referrer;
	} catch (e) {
		if (window.parent) {
			try {
				referrer = window.parent.document.referrer;
			} catch (e2) {
				referrer = '';
			}
		}
	}
	if (referrer === '') {
		referrer = document.referrer;
	}
	return referrer;
}
getItem();
//获取时间
function getItem(){
	var data=JSON.parse(localStorage.getItem('jsArr'));
	delete data[0]
	var longTime=0;
	for(var i=0;i<data.length;i++){
		if(data[i]==null||data[i]=={}){
		}else{
			longTime=parseInt(data[i].time+longTime);
		}
	}
	console.log(longTime)
	//判断时间是否有10分钟
	if(longTime>=(60*10)){
		//执行添加积分的方法
		console.log('时间到了!');
	}
}