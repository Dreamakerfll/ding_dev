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

<title>普通用户授权登录</title>
<script type="text/javascript">
//在此拿到权限验证配置所需要的信息
var _config = <%= com.dreamaker.controller.signature.SignatureController.getNormalAuth(request) %>;
</script>


</head>

<body >
	普通用户授权登录，和企业无关
	用户信息：<div id="userinfo"></div>
	用户昵称：<div id="nickname"></div>
 	<script>
 	document.getElementById("userinfo").innerHTML = JSON.stringify(_config);
 	document.getElementById("nickname").innerHTML = _config.user_info.nick;
 	</script>
</body>

</html>
