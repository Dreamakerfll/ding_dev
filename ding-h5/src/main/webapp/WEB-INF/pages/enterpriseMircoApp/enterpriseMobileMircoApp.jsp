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

<title>企业移动端微应用</title>
<script type="text/javascript">
//拿到当前页面的url
<%
	String url = null;
	String urlString = request.getRequestURL().toString();
	String queryString = request.getQueryString();
	
	urlString = urlString.replaceAll("WEB-INF/pages/enterpriseMircoApp/enterpriseMobileMircoApp.jsp", "enterpriseMircoApp/mobile");
	
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
	这里是企业微应用移动端页面
<script type="text/javascript" src="../libs/zepto.min.js"></script>
<script type="text/javascript" src="http://g.alicdn.com/dingding/open-develop/1.0.0/dingtalk.js"></script>
<script type="text/javascript" src="../js/enterpriseMircoApp/enterpriseMobileMircoApp.js"></script>

<br>
<div style="padding-left:10px;">&nbsp;&nbsp;&nbsp;&nbsp;欢迎您：<div id="userName" style="display:inline-block;font-weight:bold"></div>&nbsp;成为钉钉开发者，您当前在钉钉的<code>userId</code>为：
	<div id="userId" style="display:inline-block;font-weight:bold"></div> 。</div>
<div style="padding-left:10px;">&nbsp;&nbsp;&nbsp;&nbsp;我们为您提供了文档＋<code>api</code>帮助您快速的开发微应用并接入钉钉。</div>
<div><a id = "openContant">打开企业通讯录</a></div>
<div><a id = "openMement">选择部门，人员</a></div>
<div><a id = "selectDepartment">选择部门</a></div>
<div><a id = "createChat">创建聊天</a></div>
<br>
 
 <script type="text/javascript">
window.addEventListener('load', function() {
	setTimeout(function(){
	}, 500);
});

$('#openContant').on('click',function(){
	dd.biz.contact.choose({
		  startWithDepartmentId: 0, //-1表示打开的通讯录从自己所在部门开始展示, 0表示从企业最上层开始，(其他数字表示从该部门开始:暂时不支持)
		  multiple: true, //是否多选： true多选 false单选； 默认true
		  users: [], //默认选中的用户列表，userid；成功回调中应包含该信息
		  disabledUsers:[],// 不能选中的用户列表，员工userid
		  corpId: _config.corpId, //企业id
		  max: 2, //人数限制，当multiple为true才生效，可选范围1-1500
		  limitTips:"你选的人数超限啦", //超过人数限制的提示语可以用这个字段自定义
		  isNeedSearch:true, // 是否需要搜索功能
		  title : "选择人员", // 如果你需要修改选人页面的title，可以在这里赋值 
		  local:"true", // 是否显示本地联系人，默认false
		  onSuccess: function(data) {
		  //onSuccess将在选人结束，点击确定按钮的时候被回调
		  /* data结构
		    [{
		      "name": "张三", //姓名
		      "avatar": "http://g.alicdn.com/avatar/zhangsan.png" //头像图片url，可能为空
		      "emplId": '0573', //userid
		     },
		     ...
		    ]
		  */
		  alert(JSON.stringify(data));
		  },
		  onFail : function(err) {}
		});
});

$('#openMement').on('click',function(){
	dd.biz.contact.complexPicker({
	    title:"选择部门",            //标题
	    corpId: _config.corpId,             //企业的corpId
	    multiple:true,            //是否多选
	    limitTips:"超出了",          //超过限定人数返回提示
	    maxUsers:3,            //最大可选人数
	    pickedUsers:[],            //已选用户
	    pickedDepartments:[],          //已选部门
	    disabledUsers:[],            //不可选用户
	    disabledDepartments:[],        //不可选部门
	    requiredUsers:[],            //必选用户（不可取消选中状态）
	    requiredDepartments:[],        //必选部门（不可取消选中状态）
	    permissionType:"GLOBAL",          //选人权限，目前只有GLOBAL这个参数
	    responseUserOnly:false,        //单纯返回人，或者返回人和部门
	    onSuccess: function(result) {
	        /**
	        {
	            selectedCount:1,                              //选择人数
	            users:[{"name":"","avatar":"","emplId":""}]，//返回选人的列表，列表中的对象包含name（用户名），avatar（用户头像），emplId（用户工号）三个字段
	            departments:[{"id":,"name":"","number":}]//返回已选部门列表，列表中每个对象包含id（部门id）、name（部门名称）、number（部门人数）
	        }
	        */
	    	 alert(JSON.stringify(result));
	    },
	   onFail : function(err) {}
	});
});

$('#selectDepartment').on('click',function(){
	dd.biz.contact.departmentsPicker({
	    title:"选择部门",            //标题
	    corpId: _config.corpId,              //企业的corpId
	    multiple:true,            //是否多选
	    limitTips:"超出了",          //超过限定人数返回提示
	    maxDepartments:100,            //最大选择部门数量
	    pickedDepartments:[],          //已选部门
	    disabledDepartments:[],        //不可选部门
	    requiredDepartments:[],        //必选部门（不可取消选中状态）
	    permissionType:"GLOBAL",          //选人权限，目前只有GLOBAL这个参数
	    onSuccess: function(result) {
	        /**
	        {
	            userCount:1,                              //选择人数
	            departmentsCount:1，//选择的部门数量
	            departments:[{"id":,"name":"","number":}]//返回已选部门列表，列表中每个对象包含id（部门id）、name（部门名称）、number（部门人数）
	        }
	        */
	    	alert(JSON.stringify(result));
	    },
	   onFail : function(err) {
		   alert(JSON.stringify(err));
	   }
	});
});

$('#createChat').on('click',function(){
	dd.biz.contact.createGroup({
	    corpId: '', //企业id，可选，配置该参数即在指定企业创建群聊天
	    users: [], //默认选中的用户工号列表，可选；使用此参数必须指定corpId
	    onSuccess: function(result) {
	        /*{
	            id: 123   //企业群id
	        }*/
	    },
	    onFail: function(err) {
	    }
	});
});
</script>
 
</body>

</html>
