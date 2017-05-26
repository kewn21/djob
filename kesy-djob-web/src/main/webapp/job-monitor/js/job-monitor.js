var serverUrl = "../dataservice.dsr";
var pageSize = 15;
var dataFrom = '';
var dataTo = '';
var interval = '';

var jobMonitor = {
	result : "",
	runJobs : function() {
		if (jobMonitor.result == '') {
			alert("请选择作业！");
			return;
		}
		
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
				url : serverUrl,
				contentType : "application/json;charset=UTF-8",
				dataType : "text",
				data : "{\"commandName\":\"jobMonitor.runJobs\",\"params\":{\"jobIds\":\""
						+ jobMonitor.result
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
						searchJobMonitors(1);
					} else {
						alert("运行失败！");
					}
				},
				error : function() {
					alert("error");
				}
			});
	     }
	},
	setJobName : function(checks) {
		var obj = document.getElementsByName("setJobName");
		var count = obj.length;
		var selectCount = 0;
		for (var i = 0; i < count; i++) {
			if (obj[i].checked == true) {
				selectCount++;
			}
		}
		if (count == selectCount) {
			document.all.selAll.checked = true;
		} else {
			document.all.selAll.checked = false;
		}

		if (checks.checked == true) {
			if ("" === jobMonitor.result) {
				jobMonitor.result = jobMonitor.result + checks.value;
			} else {
				jobMonitor.result = jobMonitor.result + ","
						+ checks.value;
			}
		}
		if (checks.checked == false) {
			var i = jobMonitor.result.indexOf("," + checks.value);
			var j = i;
			if (i === -1) {
				j++;
			}
			jobMonitor.result = jobMonitor.result.substring(0, i)
					+ jobMonitor.result.substring(j + checks.value.length
							+ 1);
		}
	},
	selectAll : function() {
		var obj = document.getElementsByName("setJobName");
		jobMonitor.result = "";// 设空进行初始化
		if (document.getElementById("selAll").checked == false) {
			for (var i = 0; i < obj.length; i++) {
				obj[i].checked = false;
				jobMonitor.setJobName(obj[i]);
			}
		} else {
			for (var i = 0; i < obj.length; i++) {
				obj[i].checked = true;
				jobMonitor.setJobName(obj[i]);
			}
		}
	}
}

function validate() {
	if (!(event.keyCode == 46) && !(event.keyCode == 8)
			&& !(event.keyCode == 37) && !(event.keyCode == 39))
		if (!((event.keyCode >= 48 && event.keyCode <= 57) || (event.keyCode >= 96 && event.keyCode <= 105)))
			event.resultValue = false;
}

function openTaskMonitorContent(id, pageIndex) {
	location.href = "job-detail.html?jobId=" + id + "&jobName=" + $('#jobName').val() + "&pageIndex=" + pageIndex;
}

function createJobMonitorView(data, pageIndex) {
	var jsonObject = eval('(' + data + ')');
	
	if (jsonObject.resultValue == null) {
		document.getElementById("jsonValue").innerHTML = "<center>没有作业</center>";
	} else {
		if (dataFrom == 0) {
			dataFrom = '';
		}
		if (dataTo == 0) {
			dataTo = '';
		}
		if (interval == 0) {
			interval = '';
		}
		
		var inner = '<table class="hovertable table table-hover table-bordered">';
		inner = inner + '<tr><th colspan="8" style="padding:0;font-weight:normal;">';
		inner = inner
				+ '<form class="form-inline" style="text-align:right;margin: 0px 1px">&nbsp;&nbsp;数据开始时间: <input type="text" onkeydown="validate();" id="dataFrom" style="width:130px;" value="' + dataFrom + '"/>&nbsp;&nbsp;&nbsp;&nbsp;数据结束时间: <input type="text" onkeydown="validate();" id="dataTo" style="width:130px;" value="' + dataTo + '"/>&nbsp;&nbsp;&nbsp;&nbsp;时间间隔: <input type="text" onkeydown="validate();" id="interval" style="width:60px;" value="' + interval + '">';
		inner = inner
				+ '&nbsp;<input class="btn btn-success" type="button" value="运行" onclick="jobMonitor.runJobs()"/></form>';
		inner = inner
				+ '</th></tr><tr><th width="13px"><input type="checkbox" id="selAll" onclick="jobMonitor.selectAll()"/></th><th width="200px">名称</th><th width="80px">启动模式</th><th width="80px">运行状态</th><th width="100px">执行规则</th><th width="140px">创建时间</th><th width="140px">最后修改时间</th><th width="185px">操作</th></tr>';
		for (var i = 0; i < jsonObject.resultValue.length; i++) {
			inner = inner
					+ '<tr onmouseover="this.style.backgroundColor=#E3F5FF;" onmouseout="this.style.backgroundColor=#F4FAFE';
			inner = inner + ';"><td>';

			var j = jobMonitor.result
					.indexOf(jsonObject.resultValue[i].id);
			if (j >= 0) {
				inner = inner
						+ '<input type="checkbox" checked value='
						+ jsonObject.resultValue[i].id
						+ ' name="setJobName" id="setJobName" onclick="jobMonitor.setJobName(this)"/>';
			} else {
				inner = inner
						+ '<input type="checkbox" value='
						+ jsonObject.resultValue[i].id
						+ ' name="setJobName" id="setJobName" onclick="jobMonitor.setJobName(this)"/>';
			}
			inner = inner + '</td><td>';
			if (jsonObject.resultValue[i].name != null) {
				inner = inner + jsonObject.resultValue[i].name;
			}
			inner = inner + '</td><td>';
			if (jsonObject.resultValue[i].startMode != null) {
				inner = inner + jsonObject.resultValue[i].startMode;
			}
			inner = inner + '</td><td>';
			if (jsonObject.resultValue[i].runStatus != null) {
				inner = inner + jsonObject.resultValue[i].runStatus;
			}
			inner = inner + '</td><td>';
			if (jsonObject.resultValue[i].execStrategy != null) {
				inner = inner + jsonObject.resultValue[i].execStrategy;
			}
			inner = inner + '</td><td>';
			if (jsonObject.resultValue[i].createTime != null) {
				inner = inner + jsonObject.resultValue[i].createTime;
			}
			inner = inner + '</td><td>';
			if (jsonObject.resultValue[i].lastModifiedTime != null) {
				inner = inner + jsonObject.resultValue[i].lastModifiedTime;
			}
			inner = inner + '</td><td>';
			inner = inner
					+ '<div class="btn-group"><button style="height:25px;text-align:center;line-height:10px;" class="btn" onclick="startJob(';
			inner = inner + "'";
			inner = inner + jsonObject.resultValue[i].id;
			inner = inner + "'";
			inner = inner + ')"><i class="icon-play"></i>启动</button><button style="height:25px;text-align:center;line-height:10px;" class="btn" onclick="shutdownJob(';
			inner = inner + "'";
			inner = inner + jsonObject.resultValue[i].id;
			inner = inner + "'";
			inner = inner + ')"><i class="icon-off"></i>关闭</button><button style="height:25px;text-align:center;line-height:10px;" class="btn" onclick="openTaskMonitorContent(';
			inner = inner + "'";
			inner = inner + jsonObject.resultValue[i].id;
			inner = inner + "',";
			inner = inner + pageIndex;
			inner = inner + ')"><i class="icon-list"></i>详细</button></div>';
			inner = inner + "</td></tr>";
		}
		inner = inner + "</table>";
		document.getElementById("jsonValue").innerHTML = inner;
	}
}

function startJob(id) {
	$.ajax({
		type : "post",
		url : serverUrl,
		contentType : "application/json;charset=UTF-8",
		dataType : "text",
		data : "{\"commandName\":\"jobMonitor.startJob\",\"params\":{\"jobId\":\""
				+ id + "\"}}",
		success : function(data) {
			var jsonObject = eval('(' + data + ')');
			if (jsonObject.resultValue == true) {
				alert("启动成功！");
			} else {
				alert("启动失败！");
			}
		},
		error : function() {
			alert("error");
		}
	});
}

function shutdownJob(id) {
	$.ajax({
		type : "post",
		url : serverUrl,
		contentType : "application/json;charset=UTF-8",
		dataType : "text",
		data : "{\"commandName\":\"jobMonitor.shutdownJob\",\"params\":{\"jobId\":\""
				+ id + "\"}}",
		success : function(data) {
			var jsonObject = eval('(' + data + ')');
			if (jsonObject.resultValue == true) {
				alert("关闭成功！");
			} else {
				alert("关闭失败！");
			}
		},
		error : function() {
			alert("error");
		}
	});
}

function searchJobMonitors(pageIndex) {
	var jobName = $('#jobName').val();
	var runStatus = $('#runStatus').val();
	
	$.ajax({
		type : "post",
		url : serverUrl,
		contentType : "application/json;charset=UTF-8",
		dataType : "text",
		data : "{\"commandName\":\"jobMonitor.searchJobMonitors\",\"params\":{" 
				+ "\"jobName\":\"" + jobName 
				+ "\",\"runStatus\":\"" + runStatus 
				+ "\",\"pageIndex\":" + pageIndex 
				+ ",\"pageSize\":" + pageSize + "}}",
		success : function(data) {
			createJobMonitorView(data, pageIndex);
			searchJobMonitorCount(pageIndex);
		},
		error : function() {
			alert("error");
		}
	});
}

function getAllJobMonitors(){
	searchJobMonitors(0);
}

// pageIndex是当前页
// pageSize:一页显示的行数
// pageCount:总页数
function searchJobMonitorCount(pageIndex) {
	var jobName = $('#jobName').val();
	var runStatus = $('#runStatus').val();
	
	var curPageIndex = parseInt(pageIndex);
	$.ajax({
		type : "post",
		url : serverUrl,
		contentType : "application/json;charset=UTF-8",
		dataType : "text",
		data : "{\"commandName\":\"jobMonitor.searchJobMonitorCount\",\"params\":{" 
				+ "\"jobName\":\"" + jobName 
				+ "\",\"runStatus\":\"" + runStatus + "\"}}",
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
				inner = inner
						+ '<li class="previous" onclick="searchJobMonitors(1)"><a href="javascript:void(0)">&larr; 首页</a></li>';
				inner = inner + '<li onclick="searchJobMonitors(' + (curPageIndex - 1)
						+ ')"><a href="javascript:void(0)">&laquo;</a></li>';
			}
			if (pageCount < 13) {
				for (var i = 1; i < pageCount + 1; i++) {
					if (i === curPageIndex) {
						inner = inner
								+ '<li class="active"><a href="javascript:void(0)">'
								+ i + '</a></li>'
					} else {
						inner = inner + '<li onclick="searchJobMonitors(' + i
								+ ')"><a href="javascript:void(0)">' + i
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
							inner = inner + '<li onclick="searchJobMonitors(' + j
									+ ')"><a href="javascript:void(0)">' + j
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
								inner = inner + '<li onclick="searchJobMonitors('
										+ k
										+ ')"><a href="javascript:void(0)">'
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
								inner = inner + '<li onclick="searchJobMonitors('
										+ l
										+ ')"><a href="javascript:void(0)">'
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
				inner = inner
				+ '<li class="next" onclick="getAllJobMonitors()"><a href="javascript:void(0)">全部</a></li>';
			} else {
				inner = inner + '<li onclick="searchJobMonitors(' + (curPageIndex + 1)
						+ ')"><a href="javascript:void(0)">&raquo;</a></li>';
				inner = inner + '<li class="next" onclick="searchJobMonitors('
						+ pageCount
						+ ')"><a href="javascript:void(0)">尾页 &rarr;</a></li>';
				inner = inner
						+ '<li class="next" onclick="getAllJobMonitors()"><a href="javascript:void(0)">全部</a></li>';
			}
			document.getElementById("pagelist").innerHTML = inner;
		},
		error : function() {
			alert("error");
		}
	});
}

function initPage() {
	searchJobMonitors(1);
}

$(initPage);