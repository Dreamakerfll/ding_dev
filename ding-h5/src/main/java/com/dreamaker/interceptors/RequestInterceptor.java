package com.dreamaker.interceptors;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class RequestInterceptor implements HandlerInterceptor{

	Logger log = LoggerFactory.getLogger(getClass());
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
//		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
//		CachedRequestWrapper cachedRequestWrapper = new CachedRequestWrapper(httpServletRequest);
		
		System.out.println("ID:"+request.getRequestedSessionId());
		System.out.println("METHOD:"+request.getMethod());
		System.out.println("remoteAddress:"+getIpAddress(request));
		System.out.println("URL:"+request.getRequestURL());
//		System.out.println("PRELOAD:"+cachedRequestWrapper.getBody());
		
		return true;
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("postHandle");
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("afterCompletion");
		
		Collection<String> e = response.getHeaderNames(); 
		Iterator<String> it = e.iterator();
        while(it.hasNext()){    
            String name = (String) it.next();    
            String value = response.getHeader(name);    
            log.info(name+" = "+value);    
                
        }
        
		System.out.println(response.getStatus());
	}
	
	
	/**
	 * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址;
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public final static String getIpAddress(HttpServletRequest request)
			throws IOException {
		// 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址
		String ip = request.getHeader("X-Forwarded-For");
		try {
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {

				if (ip == null || ip.length() == 0
						|| "unknown".equalsIgnoreCase(ip)) {
					ip = request.getHeader("Proxy-Client-IP");
				}

				if (ip == null || ip.length() == 0
						|| "unknown".equalsIgnoreCase(ip)) {
					ip = request.getHeader("WL-Proxy-Client-IP");
				}

				if (ip == null || ip.length() == 0
						|| "unknown".equalsIgnoreCase(ip)) {
					ip = request.getHeader("HTTP_CLIENT_IP");
				}

				if (ip == null || ip.length() == 0
						|| "unknown".equalsIgnoreCase(ip)) {
					ip = request.getHeader("HTTP_X_FORWARDED_FOR");
				}

				if (ip == null || ip.length() == 0
						|| "unknown".equalsIgnoreCase(ip)) {
					ip = request.getRemoteAddr();
				}

				if (ip != null && ip.length() > 15) {
					String[] ips = ip.split(",");
					for (int index = 0; index < ips.length; index++) {
						String strIp = (String) ips[index];
						if (!("unknown".equalsIgnoreCase(strIp))) {
							ip = strIp;
							break;
						}
					}
				}
			} 
			
			if (ip != null && ip.length() > 15) {
				String[] ips = ip.split(",");
				for (int index = 0; index < ips.length; index++) {
					String strIp = (String) ips[index];
					if (!("unknown".equalsIgnoreCase(strIp))) {
						ip = strIp;
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ip;
	}
	
	
	private class CachedRequestWrapper extends HttpServletRequestWrapper {
		private final String body;

		public CachedRequestWrapper(HttpServletRequest request) throws IOException {
			super(request);
			StringBuilder stringBuilder = new StringBuilder();
			BufferedReader bufferedReader = null;
			try {
				InputStream inputStream = request.getInputStream();
				if (inputStream != null) {
					bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
					char[] charBuffer = new char[128];
					int bytesRead = -1;
					while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
						stringBuilder.append(charBuffer, 0, bytesRead);
					}
				} else {
					stringBuilder.append("");
				}
			} catch (IOException ex) {
				throw ex;
			} finally {
				if (bufferedReader != null) {
					try {
						bufferedReader.close();
					} catch (IOException ex) {
						throw ex;
					}
				}
			}
			body = stringBuilder.toString();
		}
		
		public String getBody() {
			return this.body;
		}
		
		public ServletInputStream getInputStream() throws IOException {
			final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes());
			ServletInputStream servletInputStream = new ServletInputStream() {
				public int read() throws IOException {
					return byteArrayInputStream.read();
				}
			};
			return servletInputStream;
		}

		public BufferedReader getReader() throws IOException {
			return new BufferedReader(new InputStreamReader(this.getInputStream()));
		}
	}
	
	

}
