var Index = {
	menu:{
		_cacheMenu:null,
		init:function(menus){
			Index.menu._cacheMenu = menus ;
			Index.menu.render() ;
		},
		render:function(){/*渲染菜单*/
			//自动发现
			if( $( "#topmenu_template" )[0] ){
				Index.menu._renderTop() ;
			}else{
				Index.menu._renderLeft() ;
			}
		},
		_findMenu:function(menus , id){
			for(var i=0 ;i<menus.length ;i++){
				if(menus[i].id == id ){
					return menus[i] ;
				}
				
				if(menus[i].childs){
					var _ = Index.menu._findMenu(menus[i].childs,id) ;
					if( _) return _ ;
				}
			}
			return null ;
		},
		_renderTop:function(){
			var menus = Index.menu._cacheMenu ;
			var container =  $( "#topmenu_template" ).attr("container") ;
			$(container).html(
				$( "#topmenu_template" ).render( Index.menu._cacheMenu )
			).find("a").click( function(e){
		
				$(container).find("li").removeClass("active") ;
				$(this).parents("li").addClass("active");
				var menuid = $(this).attr('menuid') ;
	
				//renderLeftMenu
				var menu = Index.menu._findMenu(menus,menuid) ;
				
				if( menu && menu.childs && menu.childs.length >0 ){
					$("#sidebar").show() ;
					$(".master-page").removeClass("nosidebar") ;
					Index.menu._renderLeft(menu.childs) ;
				}else{
					$("#sidebar").hide() ;
					$(".master-page").addClass("nosidebar");
					Index.layout.init() ;
				}
				
				//navigation
				var url = $(this).attr('href') ;
				if( url && url !="#" && url !="javascript:void(0)" ){
					Index.menu._navigate(menuid,url ,this ) ;
				}
				
				return false;
			} ) ;
			$(container).find("a").first().click();
		},
		_renderLeft:function(menus){
			menus = menus || Index.menu._cacheMenu ;
			var container =  $( "#leftmenu_template" ).attr("container") ;
			$(container).html(
				$( "#leftmenu_template" ).render( menus )
			) ;
			setTimeout(function(){
				//$(container).find("a").addClass('border1');
				$.each($(container).find("li a"),function(ind,val) {
					//alert('ind'+ind);
					var tmpIndex=ind%4+1;
					$(val).addClass('border'+tmpIndex);
				});
				
				$(container).find("a").click( function(e){
					$(container).find("li").removeClass("current") ;
					$(this).parents("li").addClass("current");
					var menuid = $(this).attr('menuid') ;
					
					//navigation
					var url = $(this).attr('href') ;
					if( url && url !="#" && url !="javascript:void(0)" ){
						Index.menu._navigate(menuid,url ,this ) ;
					}
					return false;
				} ) ;
			},100) ;
			
			Index.layout.init() ;
		},
		_navigate:function(id ,url , el){
			if(url != '#'){								
				$("#content iframe").attr("src",url) ;
			}
		}
	},
	layout:{
		init:function(){
			if( !$('#sidebar').length )return ;
			/** 
			 * Sidebar menus
			 **/
			var currentMenu = null; 
			var winHei = $(window).height(),
				headHei = $('#header').height(),
				$li = $('#sidebar>ul>li'),
				liHei = $li.height(),
				sideHei = $li.length * liHei ;
				
			var submenuHei = winHei - sideHei - headHei - 92;
		
			$('#sidebar>ul>li').each(function(){
				if($(this).find('li').length == 0){
					$(this).addClass('nosubmenu');
				}
			})
			$('#sidebar>ul>li:not([class*="current"])>ul').hide();
			
			 $('#sidebar>ul>li>ul.submenu').each(function(){
				var menuHei = $(this).height();
					if( menuHei > submenuHei ){ 
						$(this).height(submenuHei) 
						// fix ie6 不出现滚动条					
						$(this).css({'overflow-y':'auto'}).find('ul').css({'overflow':'hidden'});
				   }
			}) ;
			
			var htmlCollapse = $('#menucollapse').html(); 
			if($.cookie('isCollapsed') === 'true'){
			  $('body').addClass('collapsed'); 
			  $('#menucollapse').html('<span><img src="statics/images/icons/icon-collapse2.png" alt=""/></span>');
			} 
			
			// 动态设置右边内容高度
			var contentHei = $(window).height()- $("#header").height()-$("#footer").height();
			$("#sidebar").height(contentHei) ;
			$("#settings").height(contentHei)  ;
			$(".content").height(contentHei) ;
			
			/* 没有子菜单的点击 */
			$('#sidebar>ul>li[class*="nosubmenu"]>a').unbind("click.nosubmenu").bind("click.nosubmenu",function(){
				//$('#sidebar>ul>li:not([class*="nosubmenu"])>ul').slideUp();
			   $(this).parent().addClass('current')
							.siblings().removeClass('current');
					$("#sidebar a").removeClass('active');
					return false ;
			});
			
			/* 有子菜单的点击 */
			$('#sidebar>ul>li:not([class*="nosubmenu"])>a').unbind("click.nosubmenu").bind("click.nosubmenu",function(){
			   e = $(this).parent();
			   e.addClass('current').find('ul:first').slideToggle()
					.end().siblings().removeClass('current').find('ul:first').slideUp();
				
					$("#sidebar a").removeClass('active');
			});
			
			//子菜单里面的点击显示当前激活的状态			
			$("#sidebar .submenu a").unbind("click.submenu").bind("click.submenu",function(){
					$(this).addClass('active')
							.parent().siblings()
								.find('a').removeClass('active');
					var $secondParent = $(this).parent().parent().parent(),
						$thirdParent = $secondParent.parent(),
						$fourthParent = $thirdParent.parent();
					$secondParent.siblings().removeClass('current');
					$thirdParent.siblings().removeClass('current');
					$fourthParent.siblings().removeClass('current');
					return false ;
			});
			
			$('#menucollapse').unbind("click.menucollapse").bind("click.menucollapse",function(){
				  var body = $('body'); 
				  body.toggleClass('collapsed');
				  isCollapsed = body.hasClass('collapsed');
				  if(isCollapsed){
					$(this).html('<span><img src="statics/images/icons/icon-collapse2.png" alt=""/></span>');
				  }else{
					$(this).html(htmlCollapse); 
				  }
				  $.cookie('isCollapsed',isCollapsed); 
				  return false; 
			});	
			
			
			//窗口改变后动态计算
			$(window).unbind("resize").bind("resize",function(){	
				var conHei = $(window).height()- $("#header").height()-$("#footer").height();				
					$("#sidebar").height(conHei) ;
					$("#settings").height(conHei)  ;
					$(".content").height(conHei) ;
			})
		}
	}
}

//修复ie6下的问题
$.browserFix.register("ie","6","bootstrap",function( target ){
	var conHei = $(window).height()- $("#header").height()-$("#footer").height();
		$(".content>iframe")
			.height(conHei);

		$(".collapsed #sidebar>ul>li").live("mouseover",function(){
				var h = $(this).height() ;
				$(this).addClass("hover");
				$(this).find(">a").addClass("collapsed_sidebar_hover_a")  ;
				$(this).find(">ul").addClass("collapsed_sidebar_hover_ul") .show().css("margin-top",h+"px") ;
				$(this).height(h).css("margin-bottom","-3px") ;
		}).live("mouseout",function(){
				$(this).removeClass("hover");
				$(this).find(">a").removeClass("collapsed_sidebar_hover_a")  ;
				$(this).find(">ul").removeClass("collapsed_sidebar_hover_ul").hide().css("margin-top","0px") ;
				$(this).css("margin-bottom","0px") ;
		}) ;
} ) ;
