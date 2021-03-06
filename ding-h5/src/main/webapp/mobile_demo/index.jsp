<!DOCTYPE html>
<%@ page language="java" import="java.util.*,java.net.URLDecoder" contentType="text/html;charset=utf-8"%>
<html>
    <head>
        <title>Demo</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
        <link rel="shortcut icon" type="image/x-icon" href=""/> 
        <meta name="keywords" content=""/>
        <meta name="description" content=""/>
        <meta name="apple-mobile-web-app-capable" content="yes">
        <meta name="apple-mobile-web-app-status-bar-style" content="black">
        <meta name="format-detection" content="telephone=no">
        <meta name="author" content="" />
        <meta name="copyright" content=""/>
        <link rel="stylesheet" href="./css/task.css"/>
    </head>
    <body>
        <div class="doc">
            <div class="task-list">
                <section class="itemlist" id="todolist">
                    <header class="hd">待完成任务<span></span></header>
                    <div class="bd">
                    </div>
                </section>
                <section class="itemlist" id="donelist">
                    <header class="hd done">已完成任务<span></span></header>
                    <div class="bd">
                    </div>
                </section>
            </div>
        </div>
        <script src="http://g.alicdn.com/ilw/cdnjs/jquery/2.1.4/jquery.min.js"></script>
        <script src="http://g.alicdn.com/ilw/ding/0.6.2/scripts/dingtalk.js"></script>
        <script src="./js/fastclick.js"></script>
        <script type="text/javascript">
			//拿到当前页面的url
			<%
				String url = null;
				String urlString = request.getRequestURL().toString();
				String queryString = request.getQueryString();
				
				//urlString = urlString.replaceAll("WEB-INF/pages/enterpriseMircoApp/enterpriseMobileMircoApp.jsp", "enterpriseMircoApp/mobile");
				
				String queryStringEncode = null;
				if (queryString != null) {
					queryStringEncode = URLDecoder.decode(queryString,"UTF-8");
					url = urlString + "?" + queryStringEncode;
				} else {
					url = urlString;
				}
			%>
			//在此拿到权限验证配置所需要的信息
			var _config = <%= com.dreamaker.controller.signature.SignatureController.getEnterpriseMircoAppConfig(request,url) %>;
			</script>
        <script type="text/javascript">
            //文档见http://open.dingtalk.com/doc/#权限验证配置-beta
        
             dd.config({
     			agentId : _config.agentid,
     			corpId : _config.corpId,
     			timeStamp : _config.timeStamp,
     			nonceStr : _config.nonceStr,
     			signature : _config.signature,
     			jsApiList: ['ui.pullToRefresh.enable','ui.pullToRefresh.stop','biz.util.openLink','biz.navigation.setLeft','biz.navigation.setTitle','biz.navigation.setRight'] // 必填，需要使用的jsapi列表
     		});
        </script>
        <script src="./js/index.js"  async="async"></script>
    </body>
</html>
