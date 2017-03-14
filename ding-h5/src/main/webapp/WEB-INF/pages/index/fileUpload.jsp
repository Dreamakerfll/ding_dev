<!DOCTYPE html>
<%@ page language="java" import="java.util.*" contentType="text/html;charset=utf-8"%>
<html>
<head>
<meta charset="UTF-8">
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport" />
<title>测试页面</title>
<script type="text/javascript" src="../libs/jquery-3.0.0.min.js"></script>
</head>
<body>
    <form action="" enctype="multipart/form-data">
        <input type="text" name="filed1"/>
        <input type="text" name="filed2"/>
        <input type="text" name="filed3"/>
        <input type="text" name="filed4"/>
        <input type="file" id="file1" name="file1"/>
    </form>
    <button id="submit">提交文件</button>
    
    <button id="updatePerson">更新数据</button>
    
    <div>
    	<div>查询条件</div>
    	id<input id="id_search"/>
    	name<input id="name_search"/>
    	birthday<input id="birthday_search" type="datetime"/>
    	area<input id="area_search"/>
    	<button id="searchUser">查询用户</button>
    </div>
    
    <div>
    	<div>新增用户</div>
    	name<input id="name_insert"/>
    	birthday<input id="birthday_insert" type="datetime"/>
    	area<input id="area_insert"/>
    	<button id="insertUser">新增用户</button>
    </div>
    
    <div>
    	<div>更新用户</div>
    	id<input id="id_update"/>
    	name<input id="name_update"/>
    	birthday<input id="birthday_update" type="datetime"/>
    	area<input id="area_update"/>
    	<button id="updateUser">更新用户</button>
    </div>
    
    <div>
    	<div>删除用户</div>
    	id<input id="id_delete"/>
    	<button id="deleteUser">删除用户</button>
    </div>
    
    <div>
    	<button id="testRedis">测试redis相关功能</button>
    </div>
    
    <div>
    	<div>设置库存</div>
    	商品<input id="good_name_add"/>
    	批次<input id="good_batch_add"/>
    	初始库存<input id="good_stock_add"/>
    	<button id="goodAdd">添加商品</button>
    </div>
    
    <div>
    	<div>增加库存</div>
    	商品<input id="good_name_plus"/>
    	批次<input id="good_batch_plus"/>
    	增加库存<input id="good_stock_plus"/>
    	<button id="goodPlus">增加库存</button>
    </div>
    <div>
    	<div>扣减库存</div>
    	商品<input id="good_name_minus"/>
    	批次<input id="good_batch_minus"/>
    	扣减库存<input id="good_stock_minus"/>
    	<button id="goodMinus">扣减商品</button>
    </div>
    
    <div>
    	<div>删除库存</div>
    	商品<input id="good_name_del"/>
    	批次<input id="good_batch_del"/>
    	<button id="goodDel">删除库存</button>
    </div>
    
    <script>
    $(function(){
    	$('#submit').on('click',function(){
//          var formElement = document.querySelector("form");
//          var request = new XMLHttpRequest();
//          request.open("POST", "http://127.0.0.1/ding-h5/index/receiveFile");
//          request.send(new FormData(formElement));

          var formData = new FormData();

          formData.append("filed1", "就是你");
          formData.append("accountnum", 123456); // number 123456 is immediately converted to a string "123456"

          // HTML file input, chosen by user
          formData.append("userfile", $('#file1')[0].files[0]);

          // JavaScript file-like object
          var content = '<a id="a"><b id="b">hey!</b></a>'; // the body of the new file...
          var blob = new Blob([content], { type: "text/xml"});

          formData.append("webmasterfile", blob);

//          var request = new XMLHttpRequest();
//          request.open("POST", "http://127.0.0.1/ding-h5/index/receiveFile");
//          request.send(formData);

          $.ajax({
              url: 'http://127.0.0.1/ding-h5/index/receiveFile',
              method: 'post',
              processData: false,
              contentType: false,
              data: formData,
              success:function(data){
                  console.log(data);
                  console.log(data.code);
                  console.log(data.msg);
              }
              ,error:function(data){
                  console.log(data);
              }

          });
      });
    	
    	$('#searchUser').on('click',function(){

    		//获取页面的用户数据
    		var id = $('#id_search').val();
    		var name = $('#name_search').val();
    		var birthday = $('#birthday_search').val();
    		var area = $('#area_search').val();
    		
          $.ajax({
              url: 'http://127.0.0.1/ding-h5/index/selectUserByCondition',
              type: 'post',
              data: {"id":id,"name":name,"birthday":birthday,"area":area},
              dataType : 'json',
              success:function(data){
                  console.log(data);
              }
              ,error:function(data){
                  console.log(data);
              }

          });
      });
    	
    	$('#insertUser').on('click',function(){

    		//获取页面的用户数据
    		var name = $('#name_insert').val();
    		var birthday = $('#birthday_insert').val();
    		var area = $('#area_insert').val();
    		
          $.ajax({
              url: 'http://127.0.0.1/ding-h5/index/insertUser',
              type: 'post',
              data: {"name":name,"birthday":birthday,"area":area},
              dataType : 'json',
              success:function(data){
                  console.log(data);
              }
              ,error:function(data){
                  console.log(data);
              }

          });
      });
    	
    	$('#updateUser').on('click',function(){

    		//获取页面的用户数据
    		var id = $('#id_update').val();
    		var name = $('#name_update').val();
    		var birthday = $('#birthday_update').val();
    		var area = $('#area_update').val();
    		
          $.ajax({
              url: 'http://127.0.0.1/ding-h5/index/updateUserByCondition',
              type: 'post',
              data: {"id":id,"name":name,"birthday":birthday,"area":area},
              dataType : 'json',
              success:function(data){
                  console.log(data);
              }
              ,error:function(data){
                  console.log(data);
              }

          });
      });
    	
    	$('#deleteUser').on('click',function(){

    		//获取页面的用户数据
    		var id = $('#id_delete').val();
    		
          $.ajax({
              url: 'http://127.0.0.1/ding-h5/index/deleteUser',
              type: 'post',
              data: {"id":id},
              dataType : 'json',
              success:function(data){
                  console.log(data);
              }
              ,error:function(data){
                  console.log(data);
              }

          });
      });
    	
    	$('#updatePerson').on('click',function(){


            $.ajax({
                url: 'http://127.0.0.1/ding-h5/index/updatePerson',
                type: 'post',
                dataType : 'json',
                success:function(data){
                    console.log(data);
                }
                ,error:function(data){
                    console.log(data);
                }

            });
        });
    	
    	$('#testRedis').on('click',function(){


            $.ajax({
                url: 'http://127.0.0.1/ding-h5/index/testRedis',
                type: 'post',
                dataType : 'json',
                success:function(data){
                    console.log(data);
                }
                ,error:function(data){
                    console.log(data);
                }

            });
        });
    	
    	$('#goodAdd').on('click',function(){

    		var good_name = $('#good_name_add').val();
    		var good_batch = $('#good_batch_add').val();
    		var good_stock = $('#good_stock_add').val();
    		var data = {'good_name_add':good_name,'good_batch_add':good_batch,'good_stock_add':good_stock};

            $.ajax({
                url: 'http://127.0.0.1/ding-h5/index/goodAdd',
                type: 'post',
                dataType : 'json',
                data:data,
                success:function(data){
                    console.log(data);
                }
                ,error:function(data){
                    console.log(data);
                }

            });
        });
    	
    	$('#goodPlus').on('click',function(){

    		var good_name = $('#good_name_plus').val();
    		var good_batch = $('#good_batch_plus').val();
    		var good_stock = $('#good_stock_plus').val();
    		var data = {'good_name_plus':good_name,'good_batch_plus':good_batch,'good_stock_plus':good_stock};

            $.ajax({
                url: 'http://127.0.0.1/ding-h5/index/goodPlus',
                type: 'post',
                dataType : 'json',
                data:data,
                success:function(data){
                    console.log(data);
                }
                ,error:function(data){
                    console.log(data);
                }

            });
        });
    	
    	$('#goodMinus').on('click',function(){

    		var good_name = $('#good_name_minus').val();
    		var good_batch = $('#good_batch_minus').val();
    		var good_stock = $('#good_stock_minus').val();
    		var data = {'good_name_minus':good_name,'good_batch_minus':good_batch,'good_stock_minus':good_stock};

            $.ajax({
                url: 'http://127.0.0.1/ding-h5/index/goodMinus',
                type: 'post',
                dataType : 'json',
                data:data,
                success:function(data){
                    console.log(data);
                }
                ,error:function(data){
                    console.log(data);
                }

            });
        });
    	
    	$('#goodDel').on('click',function(){


    		var good_name = $('#good_name_del').val();
    		var good_batch = $('#good_batch_del').val();
    		var data = {'good_name_del':good_name,'good_batch_del':good_batch};
    		
            $.ajax({
                url: 'http://127.0.0.1/ding-h5/index/goodDel',
                type: 'post',
                dataType : 'json',
                data:data,
                success:function(data){
                    console.log(data);
                }
                ,error:function(data){
                    console.log(data);
                }

            });
        });
    });
    

    </script>
</body>
</html>