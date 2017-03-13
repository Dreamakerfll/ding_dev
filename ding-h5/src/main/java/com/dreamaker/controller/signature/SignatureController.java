package com.dreamaker.controller.signature;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("signature")
public class SignatureController {
	
	Logger log = LoggerFactory.getLogger(getClass());
	
	@RequestMapping("getSignature")
	@ResponseBody
	public String getSignature(HttpServletRequest request){
		return null;
	}
	
	@Scheduled(cron="0 0/3 * * * *")
	private void process(){
		log.info("定时任务运行！！");
	}

}
