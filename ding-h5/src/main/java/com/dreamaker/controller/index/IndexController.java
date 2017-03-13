package com.dreamaker.controller.index;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.dreamaker.domain.user.User;
import com.dreamaker.service.user.UserService;
import com.dreamaker.util.RedisService;

@Controller
@RequestMapping("index")
public class IndexController {
	
	Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RedisService<?,?> redisService;
	
	@RequestMapping("home")
	public String goToPage(){
		System.out.println("goToHome");
		return "index/home";
	}
	
	@RequestMapping("fileUpload")
	public String fileUpload(){
		System.out.println("fileUpload");
		return "index/fileUpload";
	}
	
	@RequestMapping("receiveFile")
	@ResponseBody
	public Map<String,Object> receiveFile(HttpServletRequest request){
		
		String url = request.getScheme() +"://" + request.getServerName()  
		
		                        + ":" +request.getServerPort() 
		
		                        +
		                        request.getServletPath();
		
		        if (request.getQueryString() != null){
		
		            url += "?" + request.getQueryString();
		
		        }

		
		System.out.println("receiveFile");
		// 转存在项目服务器temp文件夹下
				// 文件保存路径
				String filePath = request.getSession().getServletContext().getRealPath("/") + "temp/";

				File uploadFile = new File(filePath);
				if (!uploadFile.exists()) {
					uploadFile.mkdirs();
				}
				CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext()); 


		       //判断 request 是否有文件上传,即多部分请求  
		       if(multipartResolver.isMultipart(request)){  
		           //转换成多部分request    
		           MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;  
		           //取得request中的所有文件名  
		           Iterator<String> iter = multiRequest.getFileNames(); 
		           String openid = "";
		           Map<String, String[]> map = multiRequest.getParameterMap();
		           for(String str:map.get("filed1")){
		        	   openid = str; 
		            }
		           System.out.println(openid);
		           System.out.println(request.getParameter("filed1"));
		           while(iter.hasNext()){  
		               //记录上传过程起始时的时间，用来计算上传时间  
		               int pre = (int) System.currentTimeMillis();  
		               //取得上传文件  
		               MultipartFile file = multiRequest.getFile(iter.next()); 
		               
		               if(file != null){  
		                   //取得当前上传文件的文件名称  
		                   String myFileName = file.getOriginalFilename();  
		                   //如果名称不为“”,说明该文件存在，否则说明该文件不存在  
		                   if(myFileName.trim() !=""){  
		                       //重命名上传后的文件名  
		                       String fileName = "demoUpload" + file.getOriginalFilename();  
		                        
		                       File localFile = new File ( filePath ,  
		                       fileName ) ;  
		                       
		                       try {
								file.transferTo(localFile);
							} catch (IllegalStateException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}  
		                       
		                   
		               		
		                   }  
		               }  
		               //记录上传该文件后的时间  
		               int finaltime = (int) System.currentTimeMillis();  
		           }  
		             
		       }else{
		    	   
		       }
				
		       Map<String,Object> returnMap = new HashMap<String,Object>();
		       returnMap.put("code", 200);
		       returnMap.put("msg", "成功！");
				return returnMap;
	}
	
	@RequestMapping("selectUserByCondition")
	@ResponseBody
	public Map<String,Object> selectUserByCondition(HttpServletRequest request,User user){
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("code", 200);
		
//		request.getSession().setAttribute("session", "胖子未来式");
		List<User> userList = userService.selectUserByCondition(user);
		map.put("msg", "获取成功！");
		map.put("userList", userList);
		return map;
	}
	
	@RequestMapping("insertUser")
	@ResponseBody
	public String insertUser(HttpServletRequest request,User user){
		
		
		int row = userService.insertUser(user);
		System.out.println(row);
		return row+"";
	}
	
	@RequestMapping("updateUserByCondition")
	@ResponseBody
	public String updateUserByCondition(HttpServletRequest request,User user){
		
		
		int row = userService.updateUserByCondition(user);
		System.out.println(row);
		return row+"";
	}
	
	@RequestMapping("deleteUser")
	@ResponseBody
	public String deleteUser(HttpServletRequest request,User user){
		
		
		int row = userService.deleteUser(user);
		System.out.println(row);
		return row+"";
	}
	
	@RequestMapping("testRedis")
	@ResponseBody
	public String testRedis(HttpServletRequest request){
		
		RedisConnection conn = redisService.getConnection();
		redisService.set("key", "value", 300l);
		redisService.queuePush(conn, "queue", "haha");
		redisService.set("key2", "value2", 300l);
		
		
		//设置库存
		redisService.hashSetSync(conn, "shotes", "batch1_red", 100);
		
		//扣减库存
		redisService.hashincrSync(conn, "shotes", "batch1_red", 2);
		redisService.closeConnection(conn);
		
		return "";
	}
	
	@RequestMapping("goodAdd")
	@ResponseBody
	public String goodAdd(HttpServletRequest request){
		
		RedisConnection conn = redisService.getConnection();
		
		//设置库存
		redisService.hashSetSync(conn, request.getParameter("good_name_add"), request.getParameter("good_batch_add"), Integer.parseInt(request.getParameter("good_stock_add")));
		
		//扣减库存
//		redisService.closeConnection(conn);
		
		return "";
	}
	
	@RequestMapping("goodPlus")
	@ResponseBody
	public String goodPlus(HttpServletRequest request){
		
		RedisConnection conn = redisService.getConnection();
		
		//设置库存
		redisService.hashincrSync(conn, request.getParameter("good_name_plus"), request.getParameter("good_batch_plus"), Integer.parseInt(request.getParameter("good_stock_plus")));
		
		//扣减库存
		redisService.closeConnection(conn);
		
		return "";
	}
	
	@RequestMapping("goodMinus")
	@ResponseBody
	public String goodMinus(HttpServletRequest request){
		
		RedisConnection conn = redisService.getConnection();
		
		//设置库存
		redisService.hashincrSync(conn, request.getParameter("good_name_minus"), request.getParameter("good_batch_minus"), Integer.parseInt(request.getParameter("good_stock_minus")));
		
		//扣减库存
		redisService.closeConnection(conn);
		
		return "";
	}
	
	@RequestMapping("goodDel")
	@ResponseBody
	public String goodDel(HttpServletRequest request){
		
		RedisConnection conn = redisService.getConnection();
		
		//设置库存
		redisService.hashDelSync(conn, request.getParameter("good_name_del"), request.getParameter("good_batch_del"));
		
		//扣减库存
		redisService.closeConnection(conn);
		
		return "";
	}
	
	

}
