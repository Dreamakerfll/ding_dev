<!DOCTYPE html>
<%@ page language="java" import="java.util.*" contentType="text/html;charset=utf-8"%>
<html>
<head>
<meta http-equiv=Content-Type content="text/html;charset=utf-8">
<meta charset="gbk">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">  
<meta content="yes" name="apple-mobile-web-app-capable"/>
<meta content="black" name="apple-mobile-web-app-status-bar-style">
<meta content="telephone=no" name="format-detection"/>
<meta content="yes" name="apple-touch-fullscreen"/>
<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=no" />

<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
<script src="http://g.alicdn.com/dingding/dinglogin/0.0.2/ddLogin.js"></script>
<title>内嵌扫码授权登录</title>

</head>

<body >

	<div id="login_container" style="margin:auto;"></div>
	<script type="text/javascript">

	var appid = "<%= com.dreamaker.ding.env.Env.APP_ID %>";
	
	var go = encodeURIComponent("https://oapi.dingtalk.com/connect/oauth2/sns_authorize?appid="+appid+"&response_type=code&scope=snsapi_login&state=STATE&redirect_uri="+window.location.href);
	var obj = DDLogin({
	    id:"login_container",//这里需要你在自己的页面定义一个HTML标签并设置id，例如<div id="login_container"></div>或<span id="login_container"></span>
	    goto: go,
	    style: "",
	    href: "",
	    width : "300px",
	    height: "300px"
	});
	
	var hanndleMessage = function (event) {
	    var loginTmpCode = event.data; //拿到loginTmpCode后就可以在这里构造跳转链接进行跳转了
	    var origin = event.origin;
	    window.location.href = "https://oapi.dingtalk.com/connect/oauth2/sns_authorize?appid="+appid+"&response_type=code&scope=snsapi_login&state=STATE&redirect_uri="+window.location.href+"&loginTmpCode="+loginTmpCode;
	    
	};
	if (typeof window.addEventListener != 'undefined') {
	    window.addEventListener('message', hanndleMessage, false);
	} else if (typeof window.attachEvent != 'undefined') {
	    window.attachEvent('onmessage', hanndleMessage);
	}
	
	var code ='<%=request.getParameter("code")%>';
	
	if(code != undefined && code != "" && code != "null" && code != null){
		//在此拿到权限验证配置所需要的信息
		<%
			String userInfo = "";
			String code = request.getParameter("code");
			if(code != null && code != "" && code != "null"){
				userInfo = com.dreamaker.controller.signature.SignatureController.getNormalAuth(request);
			}
		%>
		var _config = '<%= userInfo %>';
	}
	
</script>
	内部扫码登录，和企业无关
	用户信息：<div id="userinfo">请扫码</div>
	用户昵称：<div id="nickname">请扫码</div>
 	<script>
 		if(_config){
 			document.getElementById("userinfo").innerHTML = JSON.stringify(_config);
 	 		document.getElementById("nickname").innerHTML = _config.user_info.nick;
 		}
 		
 	</script>
 
</body>

</html>
