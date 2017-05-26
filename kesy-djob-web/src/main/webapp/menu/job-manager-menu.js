var job_manager_menus = [
	{ name: " ", id:"2", childs:[
		{ name:"作业管理", id:"101", url:"job-manager/job-manager.html", icon:"statics/images/icons/menu/dark/settings.png"},
		{ name:"程序部署", id:"102", url:"job-manager/exec-deploy.html", icon:"statics/images/icons/menu/dark/settings.png"}
	]}
		
] ;


function initJobManagerMenu(){
	Index.menu.init(job_manager_menus) ;
};
	