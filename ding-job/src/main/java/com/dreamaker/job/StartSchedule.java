package com.dreamaker.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StartSchedule {

	private static Logger logger = LoggerFactory.getLogger(StartSchedule.class);

	
	public void getNewAccessToken() {
		
		logger.info("执行任务1");
	}
	
	public void checkAccessToken(){
		logger.info("执行任务2");
	}
}
