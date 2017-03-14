package com.dreamaker.controller.isvMircoApp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("isvMircoApp")
public class IsvMircoAppController {
	
	Logger log = LoggerFactory.getLogger(getClass());
	
	@RequestMapping("mobile")
	public String goToPage(){
		return "isvMircoApp/isvMobileMircoApp";
	}

}
