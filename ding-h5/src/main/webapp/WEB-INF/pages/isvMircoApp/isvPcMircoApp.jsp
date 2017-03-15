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

<title>isvPc端微应用</title>
<script type="text/javascript">
//在此拿到权限验证配置所需要的信息
var _config = <%= com.dreamaker.controller.signature.SignatureController.getIsvPcMircoAppConfig(request) %>;
</script>


<script>
function openLink(url){
	dd.biz.util.openLink({
		url:url,
	    onSuccess : function(result) {
	    },
	    onFail : function(err) {
	    	alert(JSON.stringify(err));
	    }
	});
}

</script>

</head>

<body >
	
	<script type="text/javascript" src="../libs/zepto.min.js"></script>
<script type="text/javascript" src="http://g.alicdn.com/dingding/dingtalk-pc-api/2.3.1/index.js">
</script>
<script type="text/javascript" src="../js/isvMobileMircoApp/isvPcMircoApp.js">
 
</script>

<br>
<div style="padding-left:10px;">&nbsp;&nbsp;&nbsp;&nbsp;欢迎您：<div id="userName" style="display:inline-block;font-weight:bold"></div>&nbsp;成为钉钉开发者，您当前在钉钉的<code>userId</code>为：
	<div id="userId" style="display:inline-block;font-weight:bold"></div> 。</div>
<div style="padding-left:10px;">&nbsp;&nbsp;&nbsp;&nbsp;我们为您提供了文档＋<code>api</code>帮助您快速的开发微应用并接入钉钉。</div>
<br>
 
 <script type="text/javascript">
window.addEventListener('load', function() {
	setTimeout(function(){
	}, 500);
});

	
	
</script>
 
</body>

</html>
