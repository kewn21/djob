var dataURL = '../dataservice.dsr';
var pageSize = 10;
var jobId = null;
var taskInfos = null;
var dataFrom = '';
var dataTo = '';
var interval = '';

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
	jobId = getUrlParam('jobId');
	$.ajax({
		type : "post",
		url : dataURL,
		contentType : "application/json;charset=UTF-8",
		dataType : "text",
		data : "{\"commandName\":\"jobMonitor.findJob\",\"params\":{\"jobId\":\"" + jobId + "\"}}",
		success : function(data) {
			var jsonObject = eval('(' + data + ')');
			document.getElementById("job_name_info").innerHTML = "作业&nbsp;&nbsp;->&nbsp;&nbsp;" +  jsonObject.resultValue.name;
			
			getTaskMonitors(1);
			
			var inner = '<table class="detailtable table table-hover table-bordered"><tr><td width="80px" style="text-align:right;">运行状态：</td><td width="210px">';
			if (jsonObject.resultValue.runStatus != null) {
				inner += jsonObject.resultValue.runStatus;
			}
			inner += '</td><td width="80px" style="text-align:right;">启动方式：</td><td width="212px">';
			if (jsonObject.resultValue.startMode != null) {
				inner += jsonObject.resultValue.startMode;
			}
			inner += '</td><td width="80px" style="text-align:right;">执行规则：</td><td width="210px">';
			if (jsonObject.resultValue.execStrategy != null) {
				inner += jsonObject.resultValue.execStrategy;
			}
			inner += '</td></tr><tr><td width="80px" style="text-align:right;">超时时间：</td><td width="210px">';
			if (jsonObject.resultValue.timeout != null) {
				inner += jsonObject.resultValue.timeout != 0 ? jsonObject.resultValue.timeout : "";
			}
			inner += '</td><td width="80px" style="text-align:right;">负责人：</td><td width="210px">';
			if (jsonObject.resultValue.principal != null) {
				inner += jsonObject.resultValue.principal;
			}
			inner += '</td><td></td><td></td></tr><tr><td width="80px" style="text-align:right;">创建人：</td><td width="212px">';
			if (jsonObject.resultValue.creator != null) {
				inner += jsonObject.resultValue.creator;
			}
			inner += '</td><td width="80px" style="text-align:right;">创建时间：</td><td width="210px">';
			if (jsonObject.resultValue.createTime != null) {
				inner = inner + jsonObject.resultValue.createTime;
			}
			inner += '</td><td width="80px" style="text-align:right;">最后修改时间：</td><td width="210px">';
			if (jsonObject.resultValue.lastModifiedTime != null) {
				inner += jsonObject.resultValue.lastModifiedTime;
			}
			inner += '</td></tr></table>';
			document.getElementById("information").innerHTML = inner;
			
			if (jsonObject.resultValue.description != null && jsonObject.resultValue.description != "") {
				document.getElementById("description").innerHTML = jsonObject.resultValue.description;
			} else {
				document.getElementById("description").innerHTML = "没有描述！";
			}
			
			if (jsonObject.resultValue.exception != null && jsonObject.resultValue.exception != "") {
				document.getElementById("exception").innerHTML = jsonObject.resultValue.exception;
			} else {
				document.getElementById("exception").innerHTML = "没有异常！";
			}
		},
		error : function() {
			alert("error");
		}
	});
}

function runJob() {
	dataFrom = $('#dataFrom').val();
	dataTo = $('#dataTo').val();
	interval = $('#interval').val();
	
	if (dataFrom == "") {
		dataFrom = 0;
	}
	if (dataTo == "") {
		dataTo = 0;
	}
	if (interval == "") {
		interval = 0;
	}
	
	var msg = "数据开始时间：" + (dataFrom <= 0 ? "当天" : dataFrom) + " \n数据结束时间：" + (dataTo <= 0 ? "当天" : dataTo) + " \n时间间隔：" + (interval <= 0 ? "1" : interval) + " \n\n确定重跑？";
	if(window.confirm(msg)){
		$.ajax({
			type : "post",
			url : dataURL,
			contentType : "application/json;charset=UTF-8",
			dataType : "text",
			data : "{\"commandName\":\"jobMonitor.runJob\",\"params\":{\"jobId\":\""
					+ jobId
					+ "\",\"dataFrom\":"
					+ dataFrom
					+ ",\"dataTo\":" 
					+ dataTo 
					+ ",\"interval\":" 
					+ interval 
					+ "}}",
			success : function(data) {
				var jsonObject = eval('(' + data + ')');
				if (jsonObject.resultValue == true) {
					alert("运行成功！");
					getTaskMonitors(1);
				} else {
					alert("运行失败！");
				}
			},
			error : function() {
				alert("error");
			}
		});
	}
}

function createTaskInfoView(data, pageIndex) {
	var jsonObject = eval('(' + data + ')');
	taskInfos = jsonObject.resultValue;
	
	if (dataFrom == 0) {
		dataFrom = '';
	}
	if (dataTo == 0) {
		dataTo = '';
	}
	if (interval == 0) {
		interval = '';
	}
	
	if (jsonObject.resultValue == null || jsonObject.resultValue.length < 1) {
		var inner = '<table class="hovertable table table-hover table-bordered">';
		inner = inner + '<tr><th colspan="11" style="padding:0;font-weight:normal;">';
		inner = inner
				+ '<form class="form-inline" style="text-align:right;margin: 0px 1px">数据开始时间: <input style="width:130px;" type="text" onkeydown="validate();" id="dataFrom" value="' + dataFrom + '"/>&nbsp;&nbsp;&nbsp;&nbsp;数据结束时间: <input style="width:130px;" type="text" onkeydown="validate();" id="dataTo" value="' + dataTo + '"/>&nbsp;&nbsp;&nbsp;&nbsp;时间间隔: <input style="width:60px;" type="text" onkeydown="validate();" id="interval" value="' + interval + '">';
		inner = inner
				+ '&nbsp;<input class="btn btn-success" type="button" value="运行" onclick="runJob()"/></form></th></tr>';
		inner = inner + "</table>";
		inner = inner + '</br><p>没有任务执行历史</p></br>';
		document.getElementById("taskInfo").innerHTML = inner;
	} else {
		var inner = '<table class="hovertable table table-hover table-bordered">';
		inner = inner + '<tr><th colspan="12" style="padding:0;font-weight:normal;">';
		inner = inner
				+ '<form class="form-inline" style="text-align:right;margin: 0px 1px">数据开始时间: <input style="width:130px;" type="text" onkeydown="validate();" id="dataFrom" value="' + dataFrom + '"/>&nbsp;&nbsp;&nbsp;&nbsp;数据结束时间: <input style="width:130px;" type="text" onkeydown="validate();" id="dataTo" value="' + dataTo + '"/>&nbsp;&nbsp;&nbsp;&nbsp;时间间隔: <input style="width:60px;" type="text" onkeydown="validate();" id="interval" value="' + interval + '">';
		inner = inner
				+ '&nbsp;<input class="btn btn-success" type="button" value="运行" onclick="runJob()"/></form></th></tr>';
		inner = inner
				+ '<tr><th width="60px">状态</th><th width="70px">数据行数</th><th width="70px">拒绝行数</th><th width="90px">数据量</th><th width="140px">开始时间</th><th width="140px">完成时间</th><th width="100px">数据开始时间</th><th width="100px">数据结束时间</th><th width="130px">节点</th><th width="50px">是否超时</th><th width="200px">描述</th><th width="30px">异常</th></tr>';
		for (var i = 0; i < jsonObject.resultValue.length; i++) {
			inner = inner + '<tr><td>';

			if (jsonObject.resultValue[i].runStatus != null) {
				inner = inner + jsonObject.resultValue[i].runStatus;
			}
			inner = inner + '</td><td>';
			if (jsonObject.resultValue[i].dataRow != null) {
				inner = inner + jsonObject.resultValue[i].dataRow;
			}
			inner = inner + '</td><td>';
			if (jsonObject.resultValue[i].rejectedRow != null) {
				inner = inner + jsonObject.resultValue[i].rejectedRow;
			}
			inner = inner + '</td><td>';
			if (jsonObject.resultValue[i].dataQuantity != null) {
				inner = inner + jsonObject.resultValue[i].dataQuantity;
			}
			inner = inner + '</td><td>';
			if (jsonObject.resultValue[i].startTime != null) {
				inner = inner + jsonObject.resultValue[i].startTime;
			}
			inner = inner + '</td><td>';
			if (jsonObject.resultValue[i].finishTime != null) {
				inner = inner + jsonObject.resultValue[i].finishTime;
			}
			inner = inner + '</td><td>';
			if (jsonObject.resultValue[i].dataFrom != null) {
				inner = inner + jsonObject.resultValue[i].dataFrom;
			}
			inner = inner + '</td><td>';
			if (jsonObject.resultValue[i].dataTo != null) {
				inner = inner + jsonObject.resultValue[i].dataTo;
			}
			inner = inner + '</td><td>';
			if (jsonObject.resultValue[i].node != null) {
				inner = inner + jsonObject.resultValue[i].node;
			}
			inner = inner + '</td><td>';
			inner = inner + (jsonObject.resultValue[i].timeout ? "是" : "否");
			
			inner = inner + '</td><td>';
			if (jsonObject.resultValue[i].description != null) {
				inner = inner + jsonObject.resultValue[i].description;
			}
			
			inner = inner + '</td><td>';
			
			if (jsonObject.resultValue[i].exception != null && jsonObject.resultValue[i].exception != "") {
				inner = inner + '<a href="#" onClick="showExceptionDialog('
				inner = inner + "'";
				inner = inner + jsonObject.resultValue[i].id;
				inner = inner + "')";
				inner = inner + '"><img src="../statics/images/icons/exclamation.png"/></a>';
			}
			inner = inner + '</td></tr>';
		}
		inner = inner + "</table>";
		document.getElementById("taskInfo").innerHTML = inner;
		getTaskMonitorCount(pageIndex);
	}
}

function showExceptionDialog(taskId) {
	var exception = null;
	for (var i = 0; i < taskInfos.length; i++) {
		if (taskInfos[i].id == taskId){
			exception = taskInfos[i].exception;
			break;
		}
	}
	
	var diag = new Dialog();
	diag.Width = "400";
	diag.MinHeight = "300";
	//diag.MaxHeight = "600";
	diag.Title = "异常信息";
	diag.InnerHtml = '<div style="text-align:left;font-size:14px;">' + exception + '</div>';
	diag.show();
}

function getTaskMonitors(pageIndex) {
	$.ajax({
		type : "post",
		url : dataURL,
		contentType : "application/json;charset=UTF-8",
		dataType : "text",
		data : "{\"commandName\":\"jobMonitor.getTaskMonitors\",\"params\":{\"jobId\":\""
				+ jobId
				+ "\",\"pageIndex\":\""
				+ pageIndex
				+ "\",\"pageSize\":\"" + pageSize + "\"}}",
		success : function(data) {
			createTaskInfoView(data, pageIndex);
		},
		error : function() {
			alert("error");
		}
	});
}

// pageIndex是当前页
// pageSize:一页显示的行数
// pageCount:总页数
function getTaskMonitorCount(pageIndex) {
	curPageIndex = parseInt(pageIndex);
	$.ajax({
		type : "post",
		url : dataURL,
		contentType : "application/json;charset=UTF-8",
		dataType : "text",
		data : "{\"commandName\":\"jobMonitor.getTaskMonitorCount\",\"params\":{\"jobId\":\""
				+ jobId + "\"}}",
		success : function(data) {
			var jsonObject = eval('(' + data + ')');
			if (0 === jsonObject.resultValue % pageSize) {
				pageCount = parseInt(jsonObject.resultValue / pageSize);
			} else {
				pageCount = parseInt(jsonObject.resultValue / pageSize + 1);
			}
			var inner = "";
			if (1 === curPageIndex) {
				inner = inner
						+ '<li class="disabled"><a href="javascript:void(0)">&larr; 首页</a></li>';
				inner = inner
						+ '<li class="disabled"><a href="javascript:void(0)">&laquo;</a></li>';
			} else {
				inner = inner + '<li class="previous" onclick="getTaskMonitors(1)"><a href="javascript:void(0)">&larr; 首页</a></li>';
				inner = inner + '<li onclick="getTaskMonitors(' + (curPageIndex - 1) + ')"><a href="javascript:void(0)">&laquo;</a></li>';
			}
			if (pageCount < 13) {
				for (var i = 1; i < pageCount + 1; i++) {
					if (i === curPageIndex) {
						inner = inner
								+ '<li class="active"><a href="javascript:void(0)">'
								+ i + '</a></li>'
					} else {
						inner = inner + '<li onclick="getTaskMonitors(' + i + ')"><a href="javascript:void(0)">' + i
								+ '</a></li>';
					}
				}
			} else {
				if (curPageIndex < 7) {
					for (var j = 1; j < 13; j++) {
						if (j === curPageIndex) {
							inner = inner
									+ '<li class="active"><a href="javascript:void(0)">'
									+ j + '</a></li>'
						} else {
							inner = inner + '<li onclick="getTaskMonitors(' + j + ')"><a href="javascript:void(0)">' + j
									+ '</a></li>';
						}
					}
				} else {
					if (curPageIndex > pageCount - 6) {
						for (var k = pageCount - 11; k <= pageCount; k++) {
							if (k === curPageIndex) {
								inner = inner
										+ '<li class="active"><a href="javascript:void(0)">'
										+ k + '</a></li>'
							} else {
								inner = inner + '<li onclick="getTaskMonitors(' + k + ')"><a href="javascript:void(0)">'
										+ k + '</a></li>';
							}
						}
					} else {
						for (var l = curPageIndex - 5; l <= curPageIndex + 6; l++) {
							if (l === curPageIndex) {
								inner = inner
										+ '<li class="active"><a href="javascript:void(0)">'
										+ l + '</a></li>'
							} else {
								inner = inner + '<li onclick="getTaskMonitors(' + l + ')"><a href="javascript:void(0)">'
										+ l + '</a></li>';
							}
						}
					}
				}
			}
			if (pageCount === curPageIndex || 0 === pageCount) {
				inner = inner
						+ '<li class="disabled"><a href="javascript:void(0)">&raquo;</a></li>';
				inner = inner
						+ '<li class="disabled"><a href="javascript:void(0)">尾页 &rarr;</a></li>';
			} else {
				inner = inner + '<li onclick="getTaskMonitors(' + (curPageIndex + 1) + ')"><a href="javascript:void(0)">&raquo;</a></li>';
				inner = inner + '<li class="next" onclick="getTaskMonitors(' + pageCount + ')"><a href="javascript:void(0)">尾页 &rarr;</a></li>';
			}
			document.getElementById("pagelist").innerHTML = inner;
		},
		error : function() {
			alert("error");
		}
	});
}

$(createJobInfo);