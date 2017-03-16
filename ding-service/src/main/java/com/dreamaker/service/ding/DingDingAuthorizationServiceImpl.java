package com.dreamaker.service.ding;


import org.apache.commons.io.FileUtils;
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
import com.dreamaker.utils.HttpHelper;
import com.dreamaker.utils.OApiResultException;

@Service("dingDingAuthorizationService")
public class DingDingAuthorizationServiceImpl implements DingDingAuthorizationService{

	//定义相关Token的存活时间，定义2小时（7200） 为了解决服务器时间差异问题，本系统定义为1小时 40 分钟（6000）
	public static final long TOKEN_LIVE_TIME = 6000;
	
	//创建钉钉存放redis的key
	public static final String TICKET= "DING:TICKET:";
	public static final String PERMANENTCODE = "DING:PERMANENTCODE:";
	public static final String ISVACCESSTOKEN = "DING:ISVACCESSTOKEN:";
	public static final String ISVJSTICKET = "DING:ISVJSTICKET:";
	public static final String ISVSSOACCESSTOKEN = "DING:ISVSSOACCESSTOKEN:";
	public static final String ACCESSTOKEN = "DING:ACCESSTOKEN:";
	public static final String JSTICKET = "DING:JSTICKET:";
	public static final String SSOACCESSTOKEN = "DING:SSOACCESSTOKEN:";
	
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
	
	private String getAndPutIsvAccessTokenToRedis(String suitCorpId,String corpId){
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
				throw new OApiResultException("isv_access_token");
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
	
	
	private String getAndPutIsvJsTicketToRedis(String suitCorpId,String corpId){
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
				throw new OApiResultException("isv_js_ticket");
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

	@Override
	public String getIsvSsoAccessToken(String suitCorpId, String suitSsoSecret) {
		String key = DingDingAuthorizationServiceImpl.ISVSSOACCESSTOKEN + suitCorpId;
		
		String isvSsoAccessToken = redisService.get(key);
		if(StringUtils.isNotEmpty(isvSsoAccessToken)){
			return isvSsoAccessToken;
		}else{
			if(redisService.existsLock(key)){
				//等待其他线程完成
				redisService.waitForLock(key);
				
				//从Redis 缓存中获取 isvSsoAccessToken
				isvSsoAccessToken = redisService.get(key);
				
			}else{
				//缓存过期，从服务器获取isvSsoAccessToken 并存储到 redis 中
				isvSsoAccessToken = getAndPutIsvSsoAccessTokenToRedis(suitCorpId,suitSsoSecret);
			}
			
			if(StringUtils.isEmpty(isvSsoAccessToken)){
				isvSsoAccessToken = getAndPutIsvSsoAccessTokenToRedis(suitCorpId,suitSsoSecret);
			}
		}
		
		return isvSsoAccessToken;
	}

	private String getAndPutIsvSsoAccessTokenToRedis(String suitCorpId, String suitSsoSecret) {
		String isvSsoAccessToken = "";
		
		String key = DingDingAuthorizationServiceImpl.ISVSSOACCESSTOKEN + suitCorpId;
		
		//存放锁
		redisService.putLock(key);
		
		try {
			
			String url = "https://oapi.dingtalk.com/sso/gettoken?corpid=" + suitCorpId + "&corpsecret=" + suitSsoSecret;
			JSONObject response = HttpHelper.httpGet(url);
			if (response.containsKey("access_token")) {
				// save isvSsoAccessToken
				isvSsoAccessToken = response.getString("access_token");

			} else {
				throw new OApiResultException("isv_sso_access_token");
			}
			
			if (StringUtils.isEmpty(isvSsoAccessToken)) {
				throw new RuntimeException("get isvSsoAccessToken business error:");
			}
			
		} catch (Exception e) {
			throw new RuntimeException("get isvSsoAccessToken network error:"+e.getMessage());
		}
		
		redisService.set(key , isvSsoAccessToken, DingDingAuthorizationServiceImpl.TOKEN_LIVE_TIME);
		
		redisService.cleanLock(key);
		
		return isvSsoAccessToken;
	}

	@Override
	public String getAccessToken(String corpId, String corpSecret) {
		String key = DingDingAuthorizationServiceImpl.ACCESSTOKEN + corpId;
		
		String accessToken = redisService.get(key);
		if(StringUtils.isNotEmpty(accessToken)){
			return accessToken;
		}else{
			if(redisService.existsLock(key)){
				//等待其他线程完成
				redisService.waitForLock(key);
				
				//从Redis 缓存中获取 accessToken
				accessToken = redisService.get(key);
				
			}else{
				//缓存过期，从服务器获取accessToken 并存储到 redis 中
				accessToken = getAndPutAccessTokenToRedis(corpId,corpSecret);
			}
			
			if(StringUtils.isEmpty(accessToken)){
				accessToken = getAndPutAccessTokenToRedis(corpId,corpSecret);
			}
		}
		
		return accessToken;
	}

	private String getAndPutAccessTokenToRedis(String corpId, String corpSecret) {
		String accessToken = "";
		
		String key = DingDingAuthorizationServiceImpl.ACCESSTOKEN + corpId;
		
		//存放锁
		redisService.putLock(key);
		
		try {

			String url = "https://oapi.dingtalk.com/gettoken?corpid=" + corpId + "&corpsecret=" + corpSecret;
			JSONObject response = HttpHelper.httpGet(url);
			if (response.containsKey("access_token")) {
				// save accessToken
				accessToken = response.getString("access_token");
			} else {
				throw new OApiResultException("access_token");
			}
			
			if (StringUtils.isEmpty(accessToken)) {
				throw new RuntimeException("get accessToken business error:");
			}
			
		} catch (Exception e) {
			throw new RuntimeException("get accessToken network error:"+e.getMessage());
		}
		
		redisService.set(key , accessToken, DingDingAuthorizationServiceImpl.TOKEN_LIVE_TIME);
		
		redisService.cleanLock(key);
		
		getJsTicket(corpId,corpSecret);
		
		return accessToken;
	}

	@Override
	public String getJsTicket(String corpId, String corpSecret) {
		String key = DingDingAuthorizationServiceImpl.JSTICKET + corpId;
		
		String jsTicket = redisService.get(key);
		if(StringUtils.isNotEmpty(jsTicket)){
			return jsTicket;
		}else{
			if(redisService.existsLock(key)){
				//等待其他线程完成
				redisService.waitForLock(key);
				
				//从Redis 缓存中获取 jsTicket
				jsTicket = redisService.get(key);
				
			}else{
				//缓存过期，从服务器获取jsTicket 并存储到 redis 中
				jsTicket = getAndPutJsTicketToRedis(corpId,corpSecret);
			}
			
			if(StringUtils.isEmpty(jsTicket)){
				jsTicket = getAndPutJsTicketToRedis(corpId,corpSecret);
			}
		}
		
		return jsTicket;
	}

	private String getAndPutJsTicketToRedis(String corpId, String corpSecret) {
		String jsTicket = "";
		
		String key = DingDingAuthorizationServiceImpl.JSTICKET + corpId;
		
		//存放锁
		redisService.putLock(key);
		
		try {
			
			//获取accessToken
			String accessToken = getAccessToken(corpId,corpSecret);
			
			ServiceFactory serviceFactory;
			
			serviceFactory = ServiceFactory.getInstance();
			JsapiService jsapiService = serviceFactory.getOpenService(JsapiService.class);

			JsapiTicket JsapiTicket = jsapiService.getJsapiTicket(accessToken, "jsapi");
			jsTicket = JsapiTicket.getTicket();
			
			if (JsapiTicket.getTicket() != null) {
				// save jsTicket
				jsTicket = JsapiTicket.getTicket();

			} else {
				throw new OApiResultException("js_ticket");
			}
			
			
			if (StringUtils.isEmpty(jsTicket)) {
				throw new RuntimeException("get jsTicket business error:");
			}
			
		} catch (Exception e) {
			throw new RuntimeException("get jsTicket network error:"+e.getMessage());
		}
		
		redisService.set(key , jsTicket, DingDingAuthorizationServiceImpl.TOKEN_LIVE_TIME);
		
		redisService.cleanLock(key);
		return jsTicket;
	}

	@Override
	public String getSsoAccessToken(String corpId, String ssoSecret) {
		String key = DingDingAuthorizationServiceImpl.SSOACCESSTOKEN + corpId;
		
		String ssoAccessToken = redisService.get(key);
		if(StringUtils.isNotEmpty(ssoAccessToken)){
			return ssoAccessToken;
		}else{
			if(redisService.existsLock(key)){
				//等待其他线程完成
				redisService.waitForLock(key);
				
				//从Redis 缓存中获取 ssoAccessToken
				ssoAccessToken = redisService.get(key);
				
			}else{
				//缓存过期，从服务器获取ssoAccessToken 并存储到 redis 中
				ssoAccessToken = getAndPutSsoAccessTokenToRedis(corpId,ssoSecret);
			}
			
			if(StringUtils.isEmpty(ssoAccessToken)){
				ssoAccessToken = getAndPutSsoAccessTokenToRedis(corpId,ssoSecret);
			}
		}
		
		return ssoAccessToken;
	}

	private String getAndPutSsoAccessTokenToRedis(String corpId, String ssoSecret) {
		String ssoAccessToken = "";
		
		String key = DingDingAuthorizationServiceImpl.SSOACCESSTOKEN + corpId;
		
		//存放锁
		redisService.putLock(key);
		
		try {
			
			String url = "https://oapi.dingtalk.com/sso/gettoken?corpid=" + corpId + "&corpsecret=" + ssoSecret;
			JSONObject response = HttpHelper.httpGet(url);
			if (response.containsKey("access_token")) {
				// save ssoAccessToken
				ssoAccessToken = response.getString("access_token");

			} else {
				throw new OApiResultException("sso_access_token");
			}
			
			if (StringUtils.isEmpty(ssoAccessToken)) {
				throw new RuntimeException("get ssoAccessToken business error:");
			}
			
		} catch (Exception e) {
			throw new RuntimeException("get ssoAccessToken network error:"+e.getMessage());
		}
		
		redisService.set(key , ssoAccessToken, DingDingAuthorizationServiceImpl.TOKEN_LIVE_TIME);
		
		redisService.cleanLock(key);
		
		return ssoAccessToken;
	}

}
