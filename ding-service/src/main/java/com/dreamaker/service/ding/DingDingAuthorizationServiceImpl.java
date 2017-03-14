package com.dreamaker.service.ding;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dingtalk.open.client.ServiceFactory;
import com.dingtalk.open.client.api.model.corp.JsapiTicket;
import com.dingtalk.open.client.api.model.isv.CorpAuthToken;
import com.dingtalk.open.client.api.service.corp.JsapiService;
import com.dingtalk.open.client.api.service.isv.IsvService;
import com.dreamaker.service.redis.RedisService;

@Service
public class DingDingAuthorizationServiceImpl implements DingDingAuthorizationService{

	//定义相关Token的存活时间，定义2小时（7200） 为了解决服务器时间差异问题，本系统定义为1小时 40 分钟（6000）
	public static final long TOKEN_LIVE_TIME = 6000;
	
	//创建钉钉存放redis的key
	public static final String TICKET= "DING:TICKET:";
	public static final String PERMANENTCODE = "DING:PERMANENTCODE:";
	public static final String ISVACCESSTOKEN = "DING:ISVACCESSTOKEN:";
	public static final String ISVJSTICKET = "DING:ISVJSTICKET:";
	
	@Autowired
	private RedisService<?,?> redisService;
	
	public String createTicket(String corpId,String ticket) {
		String key = DingDingAuthorizationServiceImpl.TICKET + corpId;
		redisService.set(key, ticket);
		return ticket;
	}

	public String getTicket(String corpId) {
		String key = DingDingAuthorizationServiceImpl.TICKET + corpId;
		
		String ticket = redisService.get(key);
		if(StringUtils.isNotEmpty(ticket)){
			return ticket;
		}else{
			if(redisService.existsLock(key)){
				//等待其他线程完成
				redisService.waitForLock(key);
				
				//从Redis 缓存中获取 ticket
				ticket = redisService.get(key);
				
			}
		}
		
		return ticket;
	}

	@Override
	public String createPermanentCode(String corpId, String permantentCode) {
		String key = DingDingAuthorizationServiceImpl.PERMANENTCODE + corpId;
		redisService.set(key, permantentCode);
		return permantentCode;
	}

	@Override
	public String getPermanentCode(String corpId) {
		String key = DingDingAuthorizationServiceImpl.PERMANENTCODE + corpId;
		
		String permanentCode = redisService.get(key);
		if(StringUtils.isNotEmpty(permanentCode)){
			return permanentCode;
		}else{
			if(redisService.existsLock(key)){
				//等待其他线程完成
				redisService.waitForLock(key);
				
				//从Redis 缓存中获取 permanentCode
				permanentCode = redisService.get(key);
				
			}
		}
		
		return permanentCode;
	}

	@Override
	public String getIsvAccessToken(String suitCorpId,String corpId) {
		String key = DingDingAuthorizationServiceImpl.ISVACCESSTOKEN + corpId;
		
		String isvAccessToken = redisService.get(key);
		if(StringUtils.isNotEmpty(isvAccessToken)){
			return isvAccessToken;
		}else{
			if(redisService.existsLock(key)){
				//等待其他线程完成
				redisService.waitForLock(key);
				
				//从Redis 缓存中获取 isvAccessToken
				isvAccessToken = redisService.get(key);
				
			}else{
				//缓存过期，从服务器获取isvAccessToken 并存储到 redis 中
				isvAccessToken = getAndPutIsvAccessTokenToRedis(suitCorpId,corpId);
			}
			
			if(StringUtils.isEmpty(isvAccessToken)){
				isvAccessToken = getAndPutIsvAccessTokenToRedis(suitCorpId,corpId);
			}
		}
		
		return isvAccessToken;
	}
	
	public String getAndPutIsvAccessTokenToRedis(String suitCorpId,String corpId){
		String isvAccessToken = "";
		
		String key = DingDingAuthorizationServiceImpl.ISVACCESSTOKEN + corpId;
		
		//存放锁
		redisService.putLock(key);
		
		try {
			
			ServiceFactory serviceFactory = ServiceFactory.getInstance();

			IsvService isvService = serviceFactory.getOpenService(IsvService.class);
			String suiteToken = "";
			String permanentCode = "";
			String ticket = getTicket(suitCorpId);
			JSONObject ticketJson = JSONObject.parseObject(ticket);
			suiteToken = ticketJson.getString("suiteToken");
			String permanentCodeString = getPermanentCode(corpId);
			JSONObject permanentCodeJson = JSONObject.parseObject(permanentCodeString);
			
			permanentCode = permanentCodeJson.getString(corpId);
			CorpAuthToken corpAuthToken = isvService.getCorpToken(suiteToken, 
					corpId,permanentCode);
			
			if (corpAuthToken.getAccess_token() != null) {
				// save accessToken
				isvAccessToken = corpAuthToken.getAccess_token();

			} else {
				throw new RuntimeException("access_token");
			}
			
			
			if (StringUtils.isEmpty(isvAccessToken)) {
				throw new RuntimeException("get isvAccessToken business error:");
			}
			
		} catch (Exception e) {
			throw new RuntimeException("get isvAccessToken network error:"+e.getMessage());
		}
		
		redisService.set(key , isvAccessToken, DingDingAuthorizationServiceImpl.TOKEN_LIVE_TIME);
		
		redisService.cleanLock(key);
		
		getIsvJsTicket(suitCorpId,corpId);
		
		return isvAccessToken;
				
	}
	
	@Override
	public String getIsvJsTicket(String suitCorpId,String corpId) {
		String key = DingDingAuthorizationServiceImpl.ISVJSTICKET + corpId;
		
		String isvJsTicket = redisService.get(key);
		if(StringUtils.isNotEmpty(isvJsTicket)){
			return isvJsTicket;
		}else{
			if(redisService.existsLock(key)){
				//等待其他线程完成
				redisService.waitForLock(key);
				
				//从Redis 缓存中获取 isvAccessToken
				isvJsTicket = redisService.get(key);
				
			}else{
				//缓存过期，从服务器获取isvJsTicket 并存储到 redis 中
				isvJsTicket = getAndPutIsvJsTicketToRedis(suitCorpId,corpId);
			}
			
			if(StringUtils.isEmpty(isvJsTicket)){
				isvJsTicket = getAndPutIsvJsTicketToRedis(suitCorpId,corpId);
			}
		}
		
		return isvJsTicket;
	}
	
	
	public String getAndPutIsvJsTicketToRedis(String suitCorpId,String corpId){
		String isvJsTicket = "";
		
		String key = DingDingAuthorizationServiceImpl.ISVJSTICKET + corpId;
		
		//存放锁
		redisService.putLock(key);
		
		try {
			
			//获取isvAccessToken
			String isvAccessToken = getIsvAccessToken(suitCorpId,corpId);
			
			ServiceFactory serviceFactory = ServiceFactory.getInstance();

			JsapiService jsapiService = serviceFactory.getOpenService(JsapiService.class);

			JsapiTicket JsapiTicket = jsapiService.getJsapiTicket(isvAccessToken, "jsapi");
			
			if (JsapiTicket.getTicket() != null) {
				// save accessToken
				isvJsTicket = JsapiTicket.getTicket();

			} else {
				throw new RuntimeException("js_ticket");
			}
			
			
			if (StringUtils.isEmpty(isvJsTicket)) {
				throw new RuntimeException("get isvJsTicket business error:");
			}
			
		} catch (Exception e) {
			throw new RuntimeException("get isvJsTicket network error:"+e.getMessage());
		}
		
		redisService.set(key , isvJsTicket, DingDingAuthorizationServiceImpl.TOKEN_LIVE_TIME);
		
		redisService.cleanLock(key);
		return isvJsTicket;
				
	}


}
