package com.dreamaker.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.util.PatternMatcher;
import com.alibaba.druid.util.ServletPathMatcher;
import com.dreamaker.util.BodyReaderHttpServletRequestWrapper;
import com.dreamaker.util.BodyWriteHttpServletResponseWrapper;
import com.dreamaker.util.RestResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AllRequestResponseFilter implements Filter {

	private Logger log = LoggerFactory.getLogger(getClass());
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
	protected PatternMatcher   pathMatcher                       = new ServletPathMatcher();
	
	
	public void doFilter(ServletRequest req, ServletResponse res,  
            FilterChain chain) throws IOException, ServletException {  
        HttpServletRequest hreq = (HttpServletRequest) req;  
        HttpServletResponse hres = (HttpServletResponse) res;  
        
        //排除所有的druid请求
        String requestURI = hreq.getRequestURI();

        String contextPath = hreq.getContextPath();
        
        if (contextPath != null && requestURI.startsWith(contextPath)) {
            requestURI = requestURI.substring(contextPath.length());
            if (!requestURI.startsWith("/")) {
                requestURI = "/" + requestURI;
            }
        }
        
        if (pathMatcher.matches("/druid/*", requestURI)) {
        	chain.doFilter(req, res);
            return;
        }
        
        String reqMethod = hreq.getMethod();
        if("POST".equals(reqMethod)){  
        	Enumeration<String> e = hreq.getHeaderNames()   ;    
            while(e.hasMoreElements()){    
                String name = (String) e.nextElement();    
                String value = hreq.getHeader(name);    
                log.info(name+" = "+value);    
                    
            }
            
           ServletRequest requestWrapper = new BodyReaderHttpServletRequestWrapper(hreq);  
           BodyWriteHttpServletResponseWrapper responseWrapper = new BodyWriteHttpServletResponseWrapper(hres);
           String body = getBodyString(requestWrapper);  
           log.info("preload = "+URLDecoder.decode(body,"UTF-8"));
           
           chain.doFilter(requestWrapper, responseWrapper);  
            
            String responseContent = new String(responseWrapper.getDataStream());

            log.info("response = "+ responseContent);
            log.info("responseStatus = "+ responseWrapper.getStatus());
            
//            //这里可以重新组装返回的值
//            RestResponse fullResponse = new RestResponse(responseContent);
//            byte[] responseToSend = restResponseBytes(fullResponse);

            res.getOutputStream().write(responseContent.getBytes());
            
            
        }else{  
            //get请求直接放行  
            chain.doFilter(req, res);  
        }   
    } 
    

	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}
	
	private byte[] restResponseBytes(RestResponse response) throws IOException {
	    String serialized = new ObjectMapper().writeValueAsString(response);
	    return serialized.getBytes();
	}
	
	
	/** 
     * 获取请求Body 
     * 
     * @param request 
     * @return 
     */  
    public static String getBodyString(ServletRequest request) {  
        StringBuilder sb = new StringBuilder();  
        InputStream inputStream = null;  
        BufferedReader reader = null;  
        try {  
            inputStream = request.getInputStream();  
            reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));  
            String line = "";  
            while ((line = reader.readLine()) != null) {  
                sb.append(line);  
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if (inputStream != null) {  
                try {  
                    inputStream.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
            if (reader != null) {  
                try {  
                    reader.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
        return sb.toString();  
    }
	
}
