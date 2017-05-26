var serverUrl = "../dataservice.dsr";
var pageSize = 15;

var jobManager = {
	result : "",
	dropJobs : function() {
		$.ajax({
			type : "post",
			url : serverUrl,
			contentType : "application/json;charset=UTF-8",
			dataType : "text",
			data : "{\"commandName\":\"jobManager.dropJobs\",\"params\":{\"jobIds\":\""
					+ jobManager.result 
					+ "\"}}",
			success : function(data) {
				var jsonObject = eval('(' + data + ')');
				if (jsonObject.resultValue == true) {
					alert("删除 成功！");
					searchJobMonitors(1);
				} else {
					alert("删除 失败！");
				}
			},
			error : function() {
				alert("error");
			}
		});
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
			if ("" === jobManager.result) {
				jobManager.result = jobManager.result + checks.value;
			} else {
				jobManager.result = jobManager.result + ","
						+ checks.value;
			}
		}
		if (checks.checked == false) {
			var i = jobManager.result.indexOf("," + checks.value);
			var j = i;
			if (i === -1) {
				j++;
			}
			jobManager.result = jobManager.result.substring(0, i)
					+ jobManager.result.substring(j + checks.value.length
							+ 1);
		}
	},
	selectAll : function() {
		var obj = document.getElementsByName("setJobName");
		jobManager.result = "";// 设空进行初始化
		if (document.getElementById("selAll").checked == false) {
			for (var i = 0; i < obj.length; i++) {
				obj[i].checked = false;
				jobManager.setJobName(obj[i]);
			}
		} else {
			for (var i = 0; i < obj.length; i++) {
				obj[i].checked = true;
				jobManager.setJobName(obj[i]);
			}
		}
	}
}

function openJobEditContent(id, pageIndex) {
	location.href = "job-edit.html?jobId=" + id + "&jobName=" + $('#jobName').val() + "&pageIndex=" + pageIndex;
}

function createJobMonitorView(data, pageIndex) {
	var jsonObject = eval('(' + data + ')');
	
	if (jsonObject.resultValue == null) {
		document.getElementById("jsonValue").innerHTML = "<center>没有作业</center>";
	} else {
		var inner = '<table class="hovertable table table-hover table-bordered">';
		inner = inner + '<tr><th colspan="8" style="padding:0;font-weight:normal;">';
		inner = inner
				+ '<form class="form-inline" style="text-align:right;margin: 0px 1px"><input class="btn btn-success" type="button" value="删除" onclick="jobManager.dropJobs()"/>';
		inner = inner
				+ '&nbsp;&nbsp;<input class="btn btn-success" type="button" value="新建" onclick="openJobEditContent()"/></form>';
		inner = inner
				+ '</th></tr><tr><th width="13px"><input type="checkbox" id="selAll" onclick="jobManager.selectAll()"/></th><th width="200px">名称</th><th width="80px">启动模式</th><th width="80px">运行状态</th><th width="100px">执行规则</th><th width="140px">创建时间</th><th width="140px">最后修改时间</th><th width="112px">操作</th></tr>';
		for (var i = 0; i < jsonObject.resultValue.length; i++) {
			inner = inner
					+ '<tr onmouseover="this.style.backgroundColor=#E3F5FF;" onmouseout="this.style.backgroundColor=#F4FAFE';
			inner = inner + ';"><td>';

			var j = jobManager.result
					.indexOf(jsonObject.resultValue[i].id);
			if (j >= 0) {
				inner = inner
						+ '<input type="checkbox" checked value='
						+ jsonObject.resultValue[i].id
						+ ' name="setJobName" id="setJobName" onclick="jobManager.setJobName(this)"/>';
			} else {
				inner = inner
						+ '<input type="checkbox" value='
						+ jsonObject.resultValue[i].id
						+ ' name="setJobName" id="setJobName" onclick="jobManager.setJobName(this)"/>';
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
			if (jsonObject.resultValue[i].runStatus != null) {
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
					+ '<div class="btn-group"><button style="height:25px;text-align:center;line-height:10px;" class="btn" onclick="dropJob(';
			inner = inner + "'";
			inner = inner + jsonObject.resultValue[i].id;
			inner = inner + "'";
			inner = inner + ')"><i class="icon-off"></i>删除</button><button style="height:25px;text-align:center;line-height:10px;" class="btn" onclick="openJobEditContent(';
			inner = inner + "'";
			inner = inner + jsonObject.resultValue[i].id;
			inner = inner + "',";
			inner = inner + pageIndex;
			inner = inner + ')"><i class="icon-list"></i>设置</button></div>';
			inner = inner + "</td></tr>";
		}
		inner = inner + "</table>";
		document.getElementById("jsonValue").innerHTML = inner;
	}
}

function dropJob(id) {
	$.ajax({
		type : "post",
		url : serverUrl,
		contentType : "application/json;charset=UTF-8",
		dataType : "text",
		data : "{\"commandName\":\"jobManager.dropJob\",\"params\":{\"jobId\":\""
				+ id + "\"}}",
		success : function(data) {
			var jsonObject = eval('(' + data + ')');
			if (jsonObject.resultValue == true) {
				alert("删除成功！");
			} else {
				alert("删除失败！");
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