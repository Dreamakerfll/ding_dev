package com.dreamaker.controller.authLogin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dreamaker.service.ding.DingDingAuthorizationService;

@Controller
@RequestMapping("authLogin")
public class AuthLoginController {
	
	Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	public DingDingAuthorizationService dingDingAuthorizationService;
	
	@RequestMapping("normalAuth")
	public String normalAuth(){
		return "authLogin/normalAuth";
	}
	
	@RequestMapping("scanAuth")
	public String scanAuth(){
		return "authLogin/scanAuth";
	}
	
	@RequestMapping("innerScanAuth")
	public String innerScanAuth(){
		return "authLogin/innerScanAuth";
	}
	
	

}
