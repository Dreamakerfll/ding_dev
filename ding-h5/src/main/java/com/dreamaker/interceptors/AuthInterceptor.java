package com.dreamaker.interceptors;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.dreamaker.ding.env.Env;

public class AuthInterceptor implements HandlerInterceptor{

	Logger log = LoggerFactory.getLogger(getClass());
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		String code = request.getParameter("code");
		String server = "http://dreamaker.tunnel.qydev.com";
		String contentPath = request.getRequestURI();
		
		if(StringUtils.isEmpty(code)||code =="null"){
			try {
				System.out.println(server+contentPath);
				String authType = request.getParameter("authType");
				if("scan".equals(authType)){
					response.sendRedirect("https://oapi.dingtalk.com/connect/qrconnect?appid="+Env.APP_ID+"&response_type=code&scope=snsapi_login&state=STATE&redirect_uri="+server+contentPath);
				}
				else{
					response.sendRedirect("https://oapi.dingtalk.com/connect/oauth2/sns_authorize?appid="+Env.APP_ID+"&response_type=code&scope=snsapi_login&state=STATE&redirect_uri="+server+contentPath);
				}
				return false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
	}
	

}
