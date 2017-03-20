<!DOCTYPE html>
<%@ page language="java" import="java.util.*,java.net.URLDecoder" contentType="text/html;charset=utf-8"%>
<html>
    <head>
        <title>新增任务</title>
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
            <div class="task-add" id="task-add">
                <div class="cells cells-form">
                   <div class="cell">
                            <div class="cell-hd"><label class="label">任务描述</label></div>
                            <div class="cell-bd cell-primary">
                                <input class="input" type="text" id="taskName" name="taskName" placeholder="请输入">
                            </div>
                        </div>
                    <div class="cell">
                            <div class="cell-hd"><label class="label">开始时间</label></div>
                            <div class="cell-bd cell-primary datepicker">
                                <div class="date" id="start-date"></div>
                            </div>
                        </div>
                    <div class="cell">
                            <div class="cell-hd"><label class="label">结束时间</label></div>
                            <div class="cell-bd cell-primary datepicker">
                                <div class="date" id="end-date"></div>
                            </div>
                        </div>     
                     <div class="cell">
                            <div class="cell-hd"><label class="label">优先级</label></div>
                            <div class="cell-bd cell-primary select-type">
                                <div class="select"></div>
                                <input type="hidden" name="taskType" id="taskType" />
                            </div>
                        </div>    
                </div>
            </div>
        </div>
        <script src="//g.alicdn.com/ilw/cdnjs/jquery/2.1.4/jquery.min.js"></script>
        <script src="//g.alicdn.com/ilw/ding/0.6.2/scripts/dingtalk.js"></script>
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
        <script src="./js/add.js"  async="async"></script>
    </body>
</html>
