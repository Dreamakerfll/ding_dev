package com.dreamaker.controller.isvMircoApp;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.dingtalk.open.client.api.model.corp.CorpUserDetail;
import com.dreamaker.ding.env.Env;
import com.dreamaker.service.ding.DingDingAuthorizationService;
import com.dreamaker.util.UserHelper;

@Controller
@RequestMapping("isvMircoApp")
public class IsvMircoAppController {
	
	Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	public DingDingAuthorizationService dingDingAuthorizationService;
	
	@RequestMapping("mobile")
	public String goToPageMobile(){
		return "isvMircoApp/isvMobileMircoApp";
	}
	
	@RequestMapping("pc")
	public String goToPagePc(){
		return "isvMircoApp/isvPcMircoApp";
	}
	
	@RequestMapping("userinfo")
	public void getUserinfo(HttpServletRequest request, HttpServletResponse response) throws IOException{
		// TODO Auto-generated method stub
		String code = request.getParameter("code");
		String corpId = request.getParameter("corpid");
		System.out.println("code:"+code+" corpid:"+corpId);

		try {
			response.setContentType("text/html; charset=utf-8"); 

			String accessToken = dingDingAuthorizationService.getIsvAccessToken(Env.SUITE_CORP_ID, corpId);
			System.out.println("access token:"+accessToken);
			
			CorpUserDetail user = (CorpUserDetail)UserHelper.getUser(accessToken, UserHelper.getUserInfo(accessToken, code).getUserid());
			String userJson = JSON.toJSONString(user);
			response.getWriter().append(userJson);
			System.out.println("userjson:"+userJson);
			
		} catch ( Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.getWriter().append(e.getMessage());
		}
	}

}
