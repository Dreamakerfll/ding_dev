<!DOCTYPE html>
<%@ page language="java" import="java.util.*,java.net.URLDecoder" contentType="text/html;charset=utf-8"%>
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

<title>isv微应用后台</title>
<script type="text/javascript">
//拿到当前页面的url
<%
	String url = null;
	String urlString = request.getRequestURL().toString();
	String queryString = request.getQueryString();
	
	urlString = urlString.replaceAll("WEB-INF/pages/isvMircoApp/isvSsoMircoApp.jsp", "isvMircoApp/sso");
	
	String queryStringEncode = null;
	if (queryString != null) {
		queryStringEncode = URLDecoder.decode(queryString,"UTF-8");
		url = urlString + "?" + queryStringEncode;
	} else {
		url = urlString;
	}
%>
//在此拿到权限验证配置所需要的信息
var _config = <%= com.dreamaker.controller.signature.SignatureController.getIsvSsoMircoAppConfig(request,url) %>;

</script>

</head>

<body >
	isv微应用后台
	用户信息：<div id="userinfo"></div>
	用户头像：<img id="userimg" src="" width="120px" height="120px"/>
 	<script>
 	document.getElementById("userinfo").innerHTML = JSON.stringify(_config);
 	document.getElementById("userimg").src = _config.user_info.avatar;
 	</script>
</body>

</html>
