var job_monitor_menus = [
	{ name: " ", id:"1", childs:[
		{ name:"作业监控", id:"101", url:"job-monitor/job-monitor.html", icon:"statics/images/icons/menu/dark/chart.png"},
		{ name:"今天完成", id:"102", url:"job-monitor/job-today.html", icon:"statics/images/icons/menu/dark/chart.png"},
		{ name:"正在运行", id:"103", url:"job-monitor/job-running.html", icon:"statics/images/icons/menu/dark/chart.png"}
	]}
] ;


function initJobMonitorMenu(){
	Index.menu.init(job_monitor_menus) ;
};

$.pageLoad.register("before", function(){
	Index.menu.init(job_monitor_menus) ;
})
	