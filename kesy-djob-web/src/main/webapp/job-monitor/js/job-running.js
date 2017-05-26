var serverUrl = "../dataservice.dsr";
var pageSize = 15;

function createJobMonitorView(data, pageIndex) {
	var jsonObject = eval('(' + data + ')');
	
	if (jsonObject.resultValue == null) {
		document.getElementById("jsonValue").innerHTML = "<center>没有作业</center>";
	} else {
		var inner = '<table class="hovertable table table-hover table-bordered">';
		inner = inner + '<tr><th colspan="8" style="padding:0;font-weight:normal;">';
		inner = inner
				+ '</th></tr><tr><th width="200px">名称</th><th width="80px">启动模式</th><th width="100px">执行规则</th><th width="60px">状态</th><th width="140px">开始时间</th><th width="130px">节点</th></tr>';
		for (var i = 0; i < jsonObject.resultValue.length; i++) {
			inner = inner
					+ '<tr onmouseover="this.style.backgroundColor=#E3F5FF;" onmouseout="this.style.backgroundColor=#F4FAFE';
			inner = inner + ';"><td>';
			if (jsonObject.resultValue[i].jobName != null) {
				inner = inner + jsonObject.resultValue[i].jobName;
			}
			inner = inner + '</td><td>';
			if (jsonObject.resultValue[i].startMode != null) {
				inner = inner + jsonObject.resultValue[i].startMode;
			}
			inner = inner + '</td><td>';
			if (jsonObject.resultValue[i].execStrategy != null) {
				inner = inner + jsonObject.resultValue[i].execStrategy;
			}
			inner = inner + '</td><td>';
			if (jsonObject.resultValue[i].runStatus != null) {
				inner = inner + jsonObject.resultValue[i].runStatus;
			}
			inner = inner + '</td><td>';
			if (jsonObject.resultValue[i].startTime != null) {
				inner = inner + jsonObject.resultValue[i].startTime;
			}
			inner = inner + '</td><td>';
			if (jsonObject.resultValue[i].node != null) {
				inner = inner + jsonObject.resultValue[i].node;
			}
			inner = inner + '</td></tr>';
		}
		inner = inner + "</table>";
		document.getElementById("jsonValue").innerHTML = inner;
	}
}

function searchJobMonitors(pageIndex) {
	var jobName = $('#jobName').val();
	var runStatus = $('#runStatus').val();
	
	$.ajax({
		type : "post",
		url : serverUrl,
		contentType : "application/json;charset=UTF-8",
		dataType : "text",
		data : "{\"commandName\":\"jobMonitor.searchJobTaskMonitors\",\"params\":{" 
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
		data : "{\"commandName\":\"jobMonitor.searchJobTaskMonitorCount\",\"params\":{" 
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