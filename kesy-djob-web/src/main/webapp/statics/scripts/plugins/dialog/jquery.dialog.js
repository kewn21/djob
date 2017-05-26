(function(){	
	var HideScrollbar=true;//弹出Dialog时是否隐藏滚动条
	/*************************一些公用方法和属性****************************/
	
	var agt =   window.navigator.userAgent;
	var isIE = agt.toLowerCase().indexOf("msie") != -1;
	var isGecko = agt.toLowerCase().indexOf("gecko") != -1;
	var ieVer = isIE ? parseInt(agt.split(";")[1].replace(/(^\s*)|(\s*$)/g,"").split(" ")[1]) : 0;
	var isIE8 = !!window.XDomainRequest && !!document.documentMode;
	var isIE7 = ieVer==7 && !isIE8;
	var ielt7 = isIE && ieVer<7;
	
	window.$id = function (id) {
	    return typeof id == "string" ? document.getElementById(id) : id;
	};

	function stopEvent(evt){//阻止一切事件执行,包括浏览器默认的事件
		evt = window.event||evt;
		if(!evt){
			return;
		}
		if(isGecko){
			evt.preventDefault();
			evt.stopPropagation();
		}
		evt.cancelBubble = true
		evt.returnValue = false;
	}
	
	Array.prototype.remove = function (s, dust) { //如果dust为ture，则返回被删除的元素
	    if (dust) {
	        var dustArr = [];
	        for (var i = 0; i < this.length; i++) {
	            if (s == this[i]) {
	                dustArr.push(this.splice(i, 1)[0]);
	            }
	        }
	        return dustArr;
	    }
	    for (var i = 0; i < this.length; i++) {
	        if (s == this[i]) {
	            this.splice(i, 1);
	        }
	    }
	    return this;
	}
	if(!isIE&&HTMLElement){
		if(!HTMLElement.prototype.attachEvent){
			window.attachEvent = document.attachEvent = HTMLElement.prototype.attachEvent = function(evtName,func){
				evtName = evtName.substring(2);
				this.addEventListener(evtName,func,false);
			}
			window.detachEvent = document.detachEvent = HTMLElement.prototype.detachEvent = function(evtName,func){
				evtName = evtName.substring(2);
				this.removeEventListener(evtName,func,false);
			}
		}
	}else if(isIE&&ieVer<8){
		try{ document.execCommand('BackgroundImageCache',false,true); }catch(e){}
	}
	
	var dialogWindow = null ;
	window.$topWindow = function () {
	    var parentWin = window;
	    if( parentWin.Dialog ) dialogWindow = parentWin ;
	    while (parentWin != parentWin.parent) {
	    	try{
	        	if (parentWin.parent.document.getElementsByTagName("FRAMESET").length > 0) break;
	        	parentWin = parentWin.parent;
	        	if( parentWin.Dialog ) dialogWindow = parentWin ;
	    	}catch(e){
	    		return dialogWindow ;
	    	}
	    }

	    return dialogWindow;
	};
	
	var $bodyDimensions = function (win) {
	    win = win || window;
	    var doc = win.document;
	    
	    var cw = doc.compatMode == "BackCompat" ? doc.body.clientWidth : doc.documentElement.clientWidth;
	    var ch = doc.compatMode == "BackCompat" ? doc.body.clientHeight : doc.documentElement.clientHeight;
	    var sl = Math.max(doc.documentElement.scrollLeft, doc.body.scrollLeft);
	    var st = Math.max(doc.documentElement.scrollTop, doc.body.scrollTop);
	    var sw = Math.max(doc.documentElement.scrollWidth, doc.body.scrollWidth);
	    var sh = Math.max(doc.documentElement.scrollHeight, doc.body.scrollHeight);
		if(sh<ch)
			sh=ch; //IE下在页面内容很少时存在scrollHeight<clientHeight的情况
	    return {
	        "clientWidth": cw,
	        "clientHeight": ch,
	        "scrollLeft": sl,
	        "scrollTop": st,
	        "scrollWidth": sw,
	        "scrollHeight": sh
	    }
	};
	
	var fadeEffect = function(element, start, end, speed, callback){//透明度渐变：start:开始透明度 0-100；end:结束透明度 0-100；speed:速度1-100
		if(!element.effect)
			element.effect = {fade:0, move:0, size:0};
		clearInterval(element.effect.fade);
		start = 40 ;
		end = 40 ;
		var speed=100;//speed||20;
		element.effect.fade = setInterval(function(){
			start = start < end ? Math.min(start + speed, end) : Math.max(start - speed, end);
			element.style.opacity =  start / 100;
			element.style.filter = "alpha(opacity=" + start + ")";
			if(start == end){
				clearInterval(element.effect.fade);
				if(callback)
					callback.call(element);
			}
		}, 20);
	};
	
	/*************************弹出框类实现****************************/
	
	window._topWins = [window] ;
	
	//var topDoc ,topWin ;
	window.Dialog = function (params) {

		params = params||{} ;
		if( params.renderTop === false ){
			this.topWin = window ;
			this.topDoc = this.topWin.document ;
		}else{
			this.topWin = $topWindow();
			this.topDoc = this.topWin.document;
			window._topWins.push(this.topWin) ;
		}
	
	    /****以下属性以大写开始，可以在调用show()方法前设置值****/
	    this.ID = null;
	    this.Width = null;
	    this.Height = null;
	    this.URL = null;
		this.OnLoad=null;
	    this.InnerHtml = ""
	    this.InvokeElementId = ""
	    this.Top = "50%";
	    this.Left = "50%";
	    this.Title = "　";
	    this.OkButtonText = "确 定";
	    this.CancelButtonText = "取 消";
	    this.OKEvent = null; //点击确定后调用的方法
	    this.CancelEvent = null; //点击取消及关闭后调用的方法
	    this.ShowButtonRow = false;
	    this.MessageIcon = "window.gif";
	    this.MessageTitle = "";
	    this.Message = "";
	    this.ShowMessageRow = false;
	    this.Modal = true;
	    this.Drag = true;
	    this.AutoClose = null;
	    this.ShowCloseButton = true;
		this.Animator = !ielt7;
		this.MsgForESC = "";
		this.InnerFrameName = null;
		
		this.MinHeight  ;
		this.MinWidth   ;
		this.MaxHeight  ;
		this.MaxWidth   ;
		
	    /****以下属性以小写开始，不要自行改变****/
	    this.dialogDiv = null;
		this.bgDiv=null;
	    this.openerWindow = null;
	    this.openerDialog = null;
	    this.innerFrame = null;
	    this.innerWin = null;
	    this.innerDoc = null;
	    this.zindex = 900;
	    this.cancelButton = null;
	    this.okButton = null;
	    this.unauthorized = false;
	    
	    if(params){
	    	for(var o in params){
	    		this[o] = params[o] ;
	    	}
	    	this.ID = this.ID||params.ID||params.id ;
	    }
	    if (arguments.length > 0 && typeof(arguments[0]) == "string") { //兼容旧写法
	        this.ID = arguments[0];
	    } else if (arguments.length > 0 && typeof(arguments[0]) == "object") {
	        Dialog.setOptions(this, arguments[0])
	    }
		if(!this.ID){
			this.ID = this.topWin.Dialog._dialogArray.length + "";
		}
		
	};
	Dialog._dialogArray = [];
	Dialog._childDialogArray = [];
	Dialog.bgDiv = null;
	Dialog.setOptions = function (obj, optionsObj) {
	    if (!optionsObj) return;
	    for (var optionName in optionsObj) {
	        obj[optionName] = optionsObj[optionName];
	    }
	};
	
	Dialog.unload = function(){
		for(var i = 0 ;i<Dialog._childDialogArray.length ;i++){
			var d = Dialog._childDialogArray[i] ;
			d.close() ;
		}
		for(var i = 0 ;i<Dialog._dialogArray.length ;i++){
			var d = Dialog._dialogArray[i] ;
			d.close() ;
		}
	}
	
	Dialog.attachBehaviors = function () {
		document.attachEvent("onkeydown", Dialog.onKeyDown);
		window.attachEvent('onresize', Dialog.resetPosition);
		if(!HideScrollbar&&ielt7)
			window.attachEvent("onscroll", Dialog.resetPosition);
	};
	
	Dialog.prototype.getWinPosition = function(){
		return {
			width: this.topWin.$(this.topWin).width(),
			height:this.topWin.$(this.topWin).height()
		}
	};
	
	Dialog.prototype.attachBehaviors = function () {
		var self = this;
	    if (this.Drag && this.topWin.Drag){//注册拖拽方法
			this.topWin.Drag("_DialogDiv_" + this.ID) ;
		}
	};
	Dialog.prototype.displacePath = function () {
	    if (this.URL.substr(0, 7) == "http://" || this.URL.substr(0, 1) == "/" || this.URL.substr(0, 11) == "javascript:") {
	        return this.URL;
	    } else {
	        var thisPath = this.URL;
	        var locationPath = window.location.href;
	        locationPath = locationPath.substring(0, locationPath.lastIndexOf('/'));
	        while (thisPath.indexOf('../') >= 0) {
	            thisPath = thisPath.substring(3);
	            locationPath = locationPath.substring(0, locationPath.lastIndexOf('/'));
	        }
	        return locationPath + '/' + thisPath;
	    }
	};
	
	
	Dialog.setBgDivSize = function (top_win) {
	    var bd = $bodyDimensions(top_win);
		if(Dialog.bgDiv){
				Dialog.bgDiv.style.height = top_win.$(top_win.document.body).outerHeight(true) + "px";
			}
	};

	Dialog.resetPosition = function () {
		$(window._topWins).each(function(){
			var topWin = this ;
			Dialog.setBgDivSize(topWin);
		    if(topWin && topWin.Dialog && topWin.Dialog._dialogArray ){
		    	 for (var i = 0, len = topWin.Dialog._dialogArray.length; i < len; i++) {
		      	  	topWin.Dialog._dialogArray[i].setSize();
		    	}
		    }
		}) ;
	};
	
	Dialog.content = function(obj,container){
		 if (obj.InnerHtml){
		 	obj.isBlockRender = false ;
		 	return obj.InnerHtml;
		 }
		 if( obj.URL && (obj.iframe===true || typeof obj.iframe =='undefined' ) ){
		 	return '<iframe width="100%" height="100%" frameborder="0" style="border:none 0;" name="_DialogFrame_Name_' + obj.ID + '" id="_DialogFrame_' + obj.ID + '" ' + (obj.InnerFrameName?'name="'+obj.InnerFrameName+'"':'')+' src="' + obj.displacePath() + '"></iframe>';
		 }else if(obj.URL){
			// alert(1);
		 	var options = {
			  	url:obj.URL,
			  	cache: false,
			  	success: function(html){
			  		setTimeout(function(){
							if(obj.topWin.$.block)obj.topWin.$(container).unblock() ;
			  				obj.topWin.$(container).find(".dialog-content").empty().html(html) ;//[0].innerHTML = html ;// .empty()
			  				obj.OnLoad(container);
			  				obj.setSize() ;
			  		},10) ;
				},
				_error:function(xhr, textStatus, errorThrown,url){
					obj.close(false);
				}
			 } ;
			 
		 	 if(obj.requestType){
		 	 	options.type = obj.requestType ;
		 	 }
		 	 options.noblock = true ;
		 	 obj.topWin.$.request(options);
		 }else{ //load div
		 	obj.isBlockRender = false ;
		 }
		return "";
	}
	
	
	Dialog.prototype.create = function () {
		
	    var bd = $bodyDimensions(this.topWin);

	    if (typeof(this.OKEvent)== "function") this.ShowButtonRow = true;
	    if (this.MessageTitle || this.Message) this.ShowMessageRow = true;
	    
	    var div = this.topWin.$("#_DialogDiv_" + this.ID)[0];
	    
	    if (!div) {
	        div = this.topDoc.createElement("div");
	        div.id = "_DialogDiv_" + this.ID;
	        this.topDoc.getElementsByTagName("BODY")[0].appendChild(div);
	        div.className = "ui-dialog ui-widget ui-widget-content ui-corner-all" ;
	    }
	    
	    this.container = this.topWin.$("#_DialogDiv_" + this.ID)[0];
	    
	    var defaultPosition = "" ;
		 var _width = 0 ;
	     var _height = 0 ;
		 if(this.InnerHtml || !this.iframe){
	    	_width = "auto" ;
			 if(ielt7){
				defaultPosition = "min-height:100px;_height:100px;" ;
				_height = "100px" ;
			}else{
			//jiangb	
				_height = "auto" ;
				_width 	= (this.Width||Math.round(bd.clientWidth * 4 / 10))+"px";
				defaultPosition = "min-width:280px;min-height:100px;overflow:auto;" ;
			}
	    }else{
	    	_width 	= (this.Width||Math.round(bd.clientWidth * 4 / 10))+"px";
	    	_height = (this.Height||Math.round(this.Width / 2))+"px";
			defaultPosition="overflow:hidden;" ;
	    }

	    var html = '\
	    <div id="_DialogTable_{thisID}">\
			  <div  id="_Draghandle_{thisID}" style="-moz-user-select: -moz-none; ' + (this.Drag ? "cursor: move;" : "") + '" class=" drag_handler_target ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">\
				 <span class="ui-dialog-title"  id="_Title_{thisID}">' + this.Title + '</span>\
				 <a href="#" class="ui-dialog-titlebar-close ui-corner-all"  id="_ButtonClose_{thisID}" onclick="Dialog.getInstance(\'{thisID}\').cancelButton.onclick.apply(Dialog.getInstance(\'{thisID}\').cancelButton,[]);return false;" class="dialog_close" style="' + (this.ShowCloseButton ? "" : "display:none;") + '">\
					<span class="ui-icon ui-icon-closethick">X</span>\
				 </a>\
			  </div>\
			  <div  id="_Container_{thisID}" class="ui-dialog-content ui-widget-content dialog-content" style="position: relative; width:'+_width+'; height: ' + _height+ ';'+defaultPosition+';">\
				<div style="position: absolute; height: 100%; width: 100%; display: none; background-color:#fff; opacity: 0.5;" id="_Covering_{thisID}">&nbsp;</div>\
		          ' + (function (obj) {
	       				return Dialog.content(obj,div) ;
	    		  })(this) + '\
		      </div>\
			  <div id="_ButtonRow_{thisID}" style="' + (this.ShowButtonRow ? "" : "display:none") + '" class="ui-dialog-buttonpane ui-widget-content ui-helper-clearfix">\
				<div class="ui-dialog-buttonset">\
 					<button type="button"  id="_ButtonOK_{thisID}" class="btn btn-primary">\
						'+this.OkButtonText+'\
					</button>\
					<button type="button" onclick="Dialog.getInstance(\'{thisID}\').close(true);"  id="_ButtonCancel_{thisID}" class="btn">\
						'+this.CancelButtonText+'\
					</button>\
				</div>\
			</div>\
		</div>\
	';
	 
		html=html.replace(/\{thisID\}/gm,this.ID);

		 if(this.InnerHtml || !this.iframe){
	    	if(ielt7){
				div.style.width = "300px" ;
			}else{
				div.style.width = _width ;
			}
	    }else{
	    	div.style.width = _width ;
	    }

	   // div.style.height = DialogDivHeight+"px" ;
	    if(isIE&&this.topDoc.compatMode == "BackCompat"||ielt7){
	    	div.style.position ="absolute";
	    	this.fixedPosition = false;
	    }else{
	    	div.style.position ="fixed";
	    	this.fixedPosition = true;
	    }
	    div.style.left = "-9999px";
	    div.style.top = "-9999px";
	    div.innerHTML = html;

	    if (this.InvokeElementId) {
	        var element = $id(this.InvokeElementId);
	        element.style.position = "";
	        element.style.display = "";
	        if (isIE) {
	            var fragment = this.topDoc.createElement("div");
	            fragment.innerHTML = element.outerHTML;
	            element.outerHTML = "";
	            this.topWin.$id("_Covering_" + this.ID).parentNode.appendChild(fragment)
	        } else {
	            this.topWin.$id("_Covering_" + this.ID).parentNode.appendChild(element)
	        }
	    }

	    this.openerWindow = window;
	    if(window.ownerDialog)
	        this.openerDialog = window.ownerDialog;
	    if (this.URL && (this.iframe===true || typeof this.iframe =='undefined' ) ) {
	        if (this.topWin.$id("_DialogFrame_" + this.ID)) {
	            this.innerFrame = this.topWin.$id("_DialogFrame_" + this.ID);
	        };
	        var self = this;
	        this.innerFrameOnload = function () {
					self.innerWin = self.innerFrame.contentWindow ;
	            try {
					self.innerWin.ownerDialog = self;
	                self.innerDoc = self.innerWin.document;
	                if (self.Title=='　' && self.innerDoc && self.innerDoc.title) {
	                    if (self.innerDoc.title) this.topWin.$id("_Title_" + self.ID).innerHTML = self.innerDoc.title;
	                };
	            } catch(e) {
	                if (window.console && window.console.log) console.log("可能存在访问限制，不能获取到浮动窗口中的文档对象。");
	                self.unauthorized=true;
	            }
	            
	            if(self.topWin.$.block)self.topWin.$("#_DialogDiv_" + self.ID).unblock() 
	            
	            if (typeof(self.OnLoad)== "function")self.OnLoad();
	        };
	        if (!isGecko) {
	            this.innerFrame.attachEvent("onreadystatechange", function(){//在ie下可以给iframe绑定onreadystatechange事件
					if(self.innerFrame)
					if((/loaded|complete/).test(self.innerFrame.readyState)){
						self.innerFrameOnload();
					}
				});
	            //this.innerFrame.attachEvent("onload", self.innerFrameOnload);
	        } else {
				this.innerFrame.onload = self.innerFrameOnload;
	        };
	    };

	    this.topWin.$id("_DialogDiv_" + this.ID).dialogId = this.ID;
	    this.topWin.$id("_DialogDiv_" + this.ID).dialogInstance = this;
	    this.attachBehaviors();
	    this.okButton = this.topWin.$id("_ButtonOK_" + this.ID);
	    this.cancelButton = this.topWin.$id("_ButtonCancel_" + this.ID);

	    //jQuery button Render

		div=null;
	};
	Dialog.prototype.setSize = function (w, h) {
		var container = this.topWin.$("#_DialogDiv_" + this.ID) ;
		container.center(false);
		var height = Math.max(h||0,container.find(".dialog-content")[0].scrollHeight) ;
		var width  = Math.max(w||0,container.find(".dialog-content")[0].scrollWidth );
	
		var pos = this.getWinPosition() ;
		
		height = Math.max(this.MinHeight||this.minHeight||0,height) ;
		width  = Math.max(this.MinWidth||this.minWidth||0,width) ;
		
		var titleBarHeight = container.find(".ui-dialog-titlebar").is(":visible") ?container.find(".ui-dialog-titlebar").height():0 ;
		var btnPanelHeight = container.find(".ui-dialog-buttonpane").is(":visible") ?container.find(".ui-dialog-buttonpane").height():0 ;

		var contentHeight = pos.height -30-titleBarHeight-btnPanelHeight ;
		
		height = Math.min(this.MaxHeight||this.maxHeight||4000, Math.min(height ,contentHeight )) ;
		width  = Math.min(this.MaxWidth||this.maxWidth||4000, Math.min(width ,pos.width-50 )) ;


		if(this.InnerHtml || !this.iframe){
			 if(ielt7){
				container.width(width+20 ) ;
				container.find(".dialog-content").height(height);
			}else{
				width =  container.find(".dialog-content")[0].scrollWidth  > (pos.width-30) ?(pos.width-30):"auto" ;
				height = container.find(".dialog-content")[0].scrollHeight > (pos.height-30)?(pos.height-80):"auto" ;
				
				container.width( width ) ;
				container.find(".dialog-content").height( height ) ;
			}

			container.center(false);
	    }else{
	    	var container = this.topWin.$("#_DialogDiv_" + this.ID) ;
			container.width(width ) ;
			container.height(height +50) ;
	    	container.center(false);
	    }
	};
	
	Dialog.prototype.title = function(title,messageTitle,messageDesc){
		if(title)this.topWin.$id("_Title_" + this.ID).innerHTML = title ;
		if(messageTitle)this.topWin.$id("_MessageTitle_" + this.ID).innerHTML = title ;
		if(messageDesc)this.topWin.$id("_Message_" + this.ID).innerHTML = title ;
	}
	
	var log = function(text){
		alert(" show >> "+text);
	}
	
	Dialog.prototype.show = function () {
		
		this.topWin._dialogArguments = this.dialogArguments ||null ;

	    this.create();

	    var bgdiv = Dialog.getBgdiv(this.topWin),
			thisdialogDiv=this.getDialogDiv();
	    thisdialogDiv.style.zIndex = this.zindex = parseInt(Dialog.bgDiv.style.zIndex) + 1;
		if (this.topWin.Dialog._dialogArray.length > 0) {
	        thisdialogDiv.style.zIndex = this.zindex = this.topWin.Dialog._dialogArray[this.topWin.Dialog._dialogArray.length - 1].zindex + 2;
	    } else {
	        bgdiv.style.display = "none";
		  
			//if( HideScrollbar ){
			//	 this.topWin.$(this.topDoc).css("overflow","hidden");
			//}

	    	//if(HideScrollbar){
		    //    var topWinBody = this.topDoc.getElementsByTagName(this.topDoc.compatMode == "BackCompat" ? "BODY" : "HTML")[0];
		    //    topWinBody.style.overflow = "hidden";
	    	/*	if(window.navigator.userAgent.indexOf("Firefox/3.6") != -1){//在firefox3.6下改变overflow属性会导致scrollTop=0
	    			var topWinBodyScrollTop=topWinBody.scrollTop;
			        topWinBody.style.overflow = "hidden";
	    			topWinBody.scrollTop=topWinBodyScrollTop;
	    		}else{
		        	topWinBody.style.overflow = "hidden";
		        }
	        }*/
	    }

	    this.topWin.Dialog._dialogArray.push(this);
		Dialog._childDialogArray.push(this);
		if(Dialog._childDialogArray.length==1){
			if(window.ownerDialog){
				ownerDialog.hiddenCloseButton();
			}
		}

	    if (this.Modal) {
	        bgdiv.style.zIndex = this.topWin.Dialog._dialogArray[this.topWin.Dialog._dialogArray.length - 1].zindex - 1;
	
	        Dialog.setBgDivSize(this.topWin);
			if(bgdiv.style.display == "none"){
				var bgMask=this.topWin.$id("_DialogBGMask");
	        	bgMask.className="ui-widget-overlay" ;
	        	bgdiv.style.display = "";
				bgMask=null;
			}
	    }

	    this.topWin.$.block && this.isBlockRender !== false && this.topWin.$("#_DialogDiv_" + this.ID).block({timeout: 10000}) ;
	    this.setSize();

	    if (this.CancelEvent) {
	        this.cancelButton.onclick = this.CancelEvent;
	        if(this.ShowButtonRow)this.cancelButton.focus();
	    }
	    if (this.OKEvent) {
	        this.okButton.onclick = this.OKEvent;
	        if(this.ShowButtonRow)this.okButton.focus();
	    }
	    if (this.AutoClose && this.AutoClose > 0) this.autoClose();
	    this.opened = true;
		bgdiv=null;
		if( !this.isRendButton && this.buttons ){
			for(var i=0 ;i<this.buttons.length ;i++){
				var _b = this.buttons[i] ;
				this.addButton( _b.id,_b.label,_b.event,_b.icon ) ;
			}
			this.isRendButton = true ;
		}

		return this ;
	};
	Dialog.prototype.close = function (bool) {
	    if(this.unauthorized==false){
	    	if(this.innerWin&&this.innerWin.Dialog&&this.innerWin.Dialog._childDialogArray.length>0){
	    		return;
	    	}
	    }
	    if(bool && this.closeAction ){
	    	this.closeAction() ;
	    }
	    
		var thisdialogDiv=this.getDialogDiv();
		
		//清除iframe，释放内存
	   if(this.innerFrame){
	   	try{
	   	   jQuery.memory.iframe(this.innerFrame) ;
	   	}catch(e){}
	   }
		
	    if (this == this.topWin.Dialog._dialogArray[this.topWin.Dialog._dialogArray.length - 1]) {
	        var isTopDialog = this.topWin.Dialog._dialogArray.pop();
	    } else {
	        this.topWin.Dialog._dialogArray.remove(this);
	    }
		Dialog._childDialogArray.remove(this);
		if(Dialog._childDialogArray.length==0){
			if(window.ownerDialog){
				ownerDialog.showCloseButton();
			}
		}
	    if (this.InvokeElementId) {
	        var innerElement = this.topWin.$id(this.InvokeElementId);
	        innerElement.style.display = "none";
	        if (isIE) {
	            //ie下不能跨窗口拷贝元素，只能跨窗口拷贝html代码
	            var fragment = document.createElement("div");
	            fragment.innerHTML = innerElement.outerHTML;
	            innerElement.outerHTML = "";
	            document.getElementsByTagName("BODY")[0].appendChild(fragment)
	        } else {
	            document.getElementsByTagName("BODY")[0].appendChild(innerElement)
	        }
	
	    }
	    if (this.topWin.Dialog._dialogArray.length > 0) {
	        if (this.Modal && isTopDialog){
	        	var index=this.topWin.Dialog._dialogArray.length;
	        	var hiddenBgDiv=true;
	        	while(index){
	        		--index;
	        		if(this.topWin.Dialog._dialogArray[index].Modal){
			        	Dialog.bgDiv.style.zIndex = this.topWin.Dialog._dialogArray[index].zindex - 1;
			        	hiddenBgDiv=false;
			        	break;
			        }
	        	}
	        	if(hiddenBgDiv){
			       Dialog.bgDiv.style.display = "none";
	        	}
	        }
	    } else {
	    	
	        Dialog.bgDiv.style.zIndex = "900";
	        Dialog.bgDiv.style.display = "none";
	        if(HideScrollbar){
		        var topWinBody = this.topDoc.getElementsByTagName(this.topDoc.compatMode == "BackCompat" ? "BODY" : "HTML")[0];
		        if(topWinBody.styleOverflow != undefined)
		    		if(window.navigator.userAgent.indexOf("Firefox/3.6") != -1){//在firefox3.6下改变overflow属性会导致scrollTop=0
		    			var topWinBodyScrollTop=topWinBody.scrollTop;
				        topWinBody.style.overflow = topWinBody.styleOverflow;
		    			topWinBody.scrollTop=topWinBodyScrollTop;
		    		}else{
			        	topWinBody.style.overflow = topWinBody.styleOverflow;
			        }
	        }
	    }

	    this.openerWindow.focus();
		/*****释放引用，以便浏览器回收内存**/
	    if (isIE&&!isIE8) {
			thisdialogDiv.dialogInstance=null;
			if (this.CancelEvent){this.cancelButton.onclick = null;};
			if (this.OKEvent){this.okButton.onclick = null;};
			this.topWin.$id("_DialogDiv_" + this.ID).onDragStart=null;
			this.topWin.$id("_DialogDiv_" + this.ID).onDragEnd=null;
			this.topWin.$id("_Draghandle_" + this.ID).onmousedown=null;
			this.topWin.$id("_Draghandle_" + this.ID).root=null;
	        thisdialogDiv.outerHTML = "";
			CollectGarbage();
	    } else {
	        var RycDiv = this.topWin.$id("_RycDiv");
	        if (!RycDiv) {
	            RycDiv = this.topDoc.createElement("div");
	            RycDiv.id = "_RycDiv";
	        }
	        RycDiv.appendChild(thisdialogDiv);
	        RycDiv.innerHTML = "";
			RycDiv=null;
	    }
		this.innerFrame=null;
		this.bgDiv=null;
		thisdialogDiv=null;
	    this.closed = true;
	};
	Dialog.prototype.autoClose = function () {
	    if (this.closed) {
	        clearTimeout(this._closeTimeoutId);
	        return;
	    }
	    this.AutoClose -= 1;
	    this.topWin.$id("_Title_" + this.ID).innerHTML = this.AutoClose + " 秒后自动关闭";
	    if (this.AutoClose <= 0) {
	        this.close();
	    } else {
	        var self = this;
	        this._closeTimeoutId = setTimeout(function () {
	            self.autoClose();
	        },
	        1000);
	    }
	};
	Dialog.getInstance = function (id) {
		var instance = null ;
		$(window._topWins).each(function(){
			var topWin = this ;
			var dialogDiv = topWin.$id("_DialogDiv_" + id);
			if(dialogDiv){
				instance = dialogDiv ;
			}
		}) ;
		
		if (!instance) alert("没有取到对应ID的弹出框页面对象");
		try{
	    	return instance.dialogInstance;
		}finally{
			instance = null;
		}
	};
	Dialog.prototype.addButton = function (id, txt, func,icon) {
	    this.topWin.$id("_ButtonRow_" + this.ID).style.display = "";
	    this.ShowButtonRow = true;
	    var button = this.topDoc.createElement("input");
	    button.id = "_Button_" + this.ID + "_" + id;
	    button.type = "button";
	    button.className = 'dialog_button_align' ;//style.cssText = "margin-right:5px";
	    button.value = txt;
	    button.onclick = func ;

	    var input0 = this.topWin.$id("_DialogButtons_" + this.ID).getElementsByTagName("INPUT")[0];
	    input0.parentNode.insertBefore(button, input0);
	  	if( typeof this.topWin.jQuery != 'undefined' && this.topWin.jQuery.buttonInit ){
	  		var options = icon?{icon:icon}:{} ;
			this.topWin.jQuery(button).btn().init(this.topWin.jQuery(button),options) ;
		}
	    return button;
	};
	Dialog.prototype.removeButton = function (btn) {
	    var input0 = this.topWin.$id("_DialogButtons_" + this.ID).getElementsByTagName("INPUT")[0];
	    if( typeof this.topWin.jQuery != 'undefined' && this.topWin.jQuery.buttonInit ){
			 this.topWin.jQuery(input0).parents("table .z-btn").remove();
		}else{
			input0.parentNode.removeChild(btn);
		}
	    
	};
	Dialog.prototype.hiddenCloseButton = function (btn) {
	    var closebtn = this.topWin.$id("_ButtonClose_" + this.ID);
		if(closebtn)
			closebtn.style.display='none';
	};
	Dialog.prototype.showCloseButton = function (btn) {
	    var closebtn = this.topWin.$id("_ButtonClose_" + this.ID);
		if(closebtn)
			closebtn.style.display='';
	};
	Dialog.getBgdiv = function (top_win) {
		//alert( top_win.location );
	    //if (Dialog.bgDiv) return Dialog.bgDiv;
	    var bgdiv = top_win.$id("_DialogBGDiv");
	    if (!bgdiv) {
	        bgdiv = top_win.document.createElement("div");
	        bgdiv.id = "_DialogBGDiv";
	        bgdiv.className = 'ui-dialog-mask' ;
	        bgdiv.style.cssText = "position:absolute;left:0px;top:0px;width:100%;height:100%;z-index:900";
	        var bgIframeBox = '<div style="position:relative;width:100%;height:100%;">';//background-color:#333;opacity:0.4;filter:alpha(opacity=40);
			var bgIframeMask = '<div id="_DialogBGMask" style="position:absolute;width:100%;height:100%;"></div>';
			var bgIframe = ielt7?'<iframe src="about:blank" style="filter:alpha(opacity=0);" width="100%" height="100%"></iframe>':'';
			bgdiv.innerHTML=bgIframeBox+bgIframeMask+bgIframe+'</div>';
	        top_win.document.getElementsByTagName("BODY")[0].appendChild(bgdiv);
	        if (ielt7) {
	            var bgIframeDoc = bgdiv.getElementsByTagName("IFRAME")[0].contentWindow.document;
	            bgIframeDoc.open();
	            bgIframeDoc.write("<body style='background-color:#333;' oncontextmenu='return false;'></body>");
	            bgIframeDoc.close();
				bgIframeDoc=null;
	        }
	    }
	    Dialog.bgDiv = bgdiv;
		bgdiv=null;
	    return Dialog.bgDiv;
	};
	Dialog.prototype.getDialogDiv = function () {
		var dialogDiv=this.topWin.$id("_DialogDiv_" + this.ID)
		if(!dialogDiv)alert("获取弹出层页面对象出错！");
		try{
			return dialogDiv;
		}finally{
			dialogDiv = null;
		}
	};
	Dialog.onKeyDown = function (evt) {
		var evt=window.event||evt;
	    if ((evt.shiftKey && evt.keyCode == 9)
			 ||evt.keyCode == 8) { //shift键及tab键,或backspace键
			 
			$(window._topWins).each(function(){
				var topWin = this ;
				if (topWin.Dialog._dialogArray.length > 0) {
					var target = evt.srcElement||evt.target;
					if(target.tagName!='INPUT'&&target.tagName!='TEXTAREA'){//如果在不在输入状态中
						stopEvent(evt);
						return false;
					}
		        }
			}) ;
	    }
	    if (evt.keyCode == 27) { //ESC键
	        Dialog.close();
	    }
	};
	Dialog.close = function (id,top_win) {
	    if (top_win.Dialog._dialogArray.length > 0) {
	        var diag = top_win.Dialog._dialogArray[top_win.Dialog._dialogArray.length - 1];
	        if(diag.MsgForESC){
				Dialog.confirm(diag.MsgForESC,function(){diag.cancelButton.onclick.apply(diag.cancelButton, []);})
	        }else{
	        	diag.cancelButton.onclick.apply(diag.cancelButton, []);
	        }
	    }
	};
	
	Dialog.open = function (arg) {
	    var diag = new Dialog(arg);
	    diag.show();
	    return diag;
	};
	
	$(window).bind("load",Dialog.attachBehaviors) ;
	$(window).bind("unload",Dialog.unload) ;
	

	/**封装jQuery使用方法**/
	if( typeof(jQuery)!='undefined' ){
		jQuery.dialog = function(params){
			return new Dialog(params) ;
		} ;
	}
	
	jQuery.fn.center = function(f) {  
	    return this.each(function(){  
	        var p = f===false?document.body:this.parentNode;  
	        if ( p.nodeName.toLowerCase()!= "body" && jQuery.css(p,"position") == 'static' )  
	            p.style.position = 'relative';  
	        var s = this.style;  
	        s.position = 'absolute';  
	        if(p.nodeName.toLowerCase() == "body")  
	            var w=$(window);  
	        if(!f || f == "horizontal") {
	            s.left = "0px";  
	           
	            if(p.nodeName.toLowerCase() == "body") {  
	                var clientLeft = w.scrollLeft() - 3 + (w.width() - parseInt(jQuery.css(this,"width")))/2;  
	                s.left = Math.max(clientLeft,0) + "px";  
	            }else if(((parseInt(jQuery.css(p,"width")) - parseInt(jQuery.css(this,"width")))/2) > 0)  
	                s.left = ((parseInt(jQuery.css(p,"width")) - parseInt(jQuery.css(this,"width")))/2) + "px";  
	        }  
	        
	        if(!f || f == "vertical") {  
	            s.top = "0px";  
	            if(p.nodeName.toLowerCase() == "body") {  
	                var clientHeight = w.scrollTop() - 4 + (w.height() - parseInt(jQuery.css(this,"height")))/2;  
	                s.top = Math.max(clientHeight,0) + "px";  
	            }else if(((parseInt(jQuery.css(p,"height")) - parseInt(jQuery.css(this,"height")))/2) > 0)  
	                s.top = ((parseInt(jQuery.css(p,"height")) - parseInt(jQuery.css(this,"height")))/2) + "px";  
	        }  
	    });  
	};  
	
	/////////////////
	window.Drag= function (o,s)  
	{  
	    if (typeof o == "string") o = document.getElementById(o);  
	    o.orig_x = parseInt(o.style.left) - document.body.scrollLeft;  
	    o.orig_y = parseInt(o.style.top) - document.body.scrollTop;  
	    o.orig_index = o.style.zIndex; 
	
	    o.onmousedown = function(a)  
	    {   
	        if(!a)a=window.event;  
	        var tgt = a.srcElement||a.target ;
	        if(tgt.tagName == 'IMG'){
	        	this.style.cursor = "";
	        	return ;
	        }
	        
	        if( !$(tgt).hasClass("drag_handler_target") ){
	        	this.style.cursor = "";
	        	return ;
	        }
	        	
	        
	        this.style.cursor = "move";
	        var d=document;
	
	        var x = a.clientX+d.body.scrollLeft-o.offsetLeft;  
	        var y = a.clientY+d.body.scrollTop-o.offsetTop;  
	  
	        d.ondragstart = "return false;"  
	        d.onselectstart = "return false;"  
	        d.onselect = "document.selection.empty();"  
	        
	        if(o.setCapture)  
	            o.setCapture();  
	        else if(window.captureEvents)  
	            window.captureEvents(Event.MOUSEMOVE|Event.MOUSEUP);  
	
	        d.onmousemove = function(a){  
	            if(!a)a=window.event;  
	           
	            o.style.left = ( a.clientX+document.body.scrollLeft-x)+"px";//a.clientX+document.body.scrollLeft-x;  
	            o.style.top = (a.clientY+document.body.scrollTop-y)+"px";  
	            o.orig_x = parseInt(o.style.left) - document.body.scrollLeft;  
	            o.orig_y = parseInt(o.style.top) - document.body.scrollTop;  
	        }  
	
	        d.onmouseup = function(a)  
	        {  
	        	if(!a)a=window.event;
	            if(o.releaseCapture)  
	                o.releaseCapture();  
	            else if(window.captureEvents)  
	                window.captureEvents(Event.MOUSEMOVE|Event.MOUSEUP);  
	            d.onmousemove = null;  
	            d.onmouseup = null;  
	            d.ondragstart = null;  
	            d.onselectstart = null;  
	            d.onselect = null;  
	            o.style.cursor = "normal";  
	            //o.style.zIndex = o.orig_index;  
		        }  
		    }  
		      
		    if (s){  
		        var orig_scroll = window.onscroll?window.onscroll:function (){};  
		        window.onscroll = function(){
		            orig_scroll();  
		            o.style.left = o.orig_x + document.body.scrollLeft;  
		            o.style.top = o.orig_y + document.body.scrollTop;  
		        }  
		    }
		} 
		
})() ;