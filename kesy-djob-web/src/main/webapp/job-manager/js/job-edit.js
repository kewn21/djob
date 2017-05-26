var dataURL = '../dataservice.dsr';
var jobId = "";

function getUrlParam(name){
	var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
	var r = window.location.search.substr(1).match(reg);  //匹配目标参数
	if (r!=null) return unescape(r[2]); return null; //返回参数值
} 

function validate() {
	if (!(event.keyCode == 46) && !(event.keyCode == 8)
			&& !(event.keyCode == 37) && !(event.keyCode == 39))
		if (!((event.keyCode >= 48 && event.keyCode <= 57) || (event.keyCode >= 96 && event.keyCode <= 105)))
			event.resultValue = false;
}

function createJobInfo() {
	var tmpJobId = getUrlParam('jobId');
	if (tmpJobId == null || tmpJobId == "" 
		|| tmpJobId == undefined || tmpJobId == "undefined"){
		return;
	}
	
	jobId = tmpJobId;
	
	$.ajax({
		type : "post",
		url : dataURL,
		contentType : "application/json;charset=UTF-8",
		dataType : "text",
		data : "{\"commandName\":\"jobMonitor.findJob\",\"params\":{\"jobId\":\"" + jobId + "\"}}",
		success : function(data) {
			var jsonObject = eval('(' + data + ')');
			document.getElementById("job_name_info").innerHTML = "作业&nbsp;&nbsp;->&nbsp;&nbsp;" +  jsonObject.resultValue.name;
			
			$('#name').val(jsonObject.resultValue.name);
			$('#startMode').val(jsonObject.resultValue.startMode);
			$('#execStrategy').val(jsonObject.resultValue.execStrategy);
			$('#execName').val(jsonObject.resultValue.execName);
			$('#timeout').val(jsonObject.resultValue.timeout);
			$('#description').val(jsonObject.resultValue.description);
		},
		error : function() {
			alert("error");
		}
	});
}

function isNumberic(n) {  
	var r = /^([1-9]\d*|[0]{1,1})$/;//正整数 
	return r.test(n);
}

function trim(str){ //删除左右两端的空格
    return str.replace(/(^\s*)|(\s*$)/g, "");
}

function saveJob(){
	var name = $('#name').val();
	var startMode = $('#startMode').val();
	var execStrategy = $('#execStrategy').val();
	var execName = $('#execName').val();
	var timeout = $('#timeout').val();
	var description = $('#description').val();
	
	if (trim(name) == ""){
		alert("作业名不嫩为空！");
		return;
	}
	if (trim(execStrategy) == ""){
		alert("执行策略不嫩为空！");
		return;
	}
	if (trim(execName) == ""){
		alert("程序名不嫩为空！");
		return;
	}
	if (!isNumberic(timeout)){
		alert("超时时间必须是正整数！");
		return;
	}
	
	$.ajax({
		type : "post",
		url : dataURL,
		contentType : "application/json;charset=UTF-8",
		dataType : "text",
		data : "{\"commandName\":\"jobManager.saveJob\",\"params\":{" 
			+ "\"jobId\":\"" + jobId + "\"" 
			+ ",\"name\":\"" + name + "\"" 
			+ ",\"startMode\":\"" + startMode + "\"" 
			+ ",\"execStrategy\":\"" + execStrategy + "\"" 
			+ ",\"execName\":\"" + execName + "\"" 
			+ ",\"timeout\":\"" + timeout + "\"" 
			+ ",\"description\":\"" + description + "\""
			+ "}}",
		success : function(data) {
			var jsonObject = eval('(' + data + ')');
			if (jsonObject.resultValue) {
				alert("保存成功！");
				location.href = "job-manager.html";//"job-manager.html?jobId=" + id + "&jobName=" + $('#jobName').val() + "&pageIndex=" + pageIndex;
			} else {
				alert("保存失败！");
			}
		},
		error : function() {
			alert("error");
		}
	});
}

$(createJobInfo);