var buffer_menus = [
	{ name: " ", id:"3", childs:[
		{ name:"缓冲池监控", id:"301", url:"", icon:"statics/images/icons/menu/dark/chart.png", childs:[
		     { name:"缓冲池监控", id:"30101", url:"job-monitor/job-monitor.html" }
		]}
	]}
		
] ;


function initBufferMenu(){
	Index.menu.init(buffer_menus) ;
};
	