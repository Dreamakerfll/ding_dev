package com.dreamaker.controller.signature;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dreamaker.ding.env.Env;
import com.dreamaker.service.ding.DingDingAuthorizationService;
import com.dreamaker.util.UserHelper;
import com.dreamaker.utils.HttpHelper;
import com.dreamaker.utils.OApiException;
import com.dreamaker.utils.OApiResultException;

@Controller
@RequestMapping("signature")
public class SignatureController {
	
	Logger log = LoggerFactory.getLogger(getClass());
	
	public static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	
	public static DingDingAuthorizationService dingDingAuthorizationService;
	
	
	@Autowired
	public void setDingDingAuthorizationService(DingDingAuthorizationService dingDingAuthorizationService) {
		SignatureController.dingDingAuthorizationService = dingDingAuthorizationService;
	}

	@RequestMapping("getSignature")
	@ResponseBody
	public String getSignature(HttpServletRequest request){
		return null;
	}
	
	@Scheduled(cron="0 0/3 * * * *")
	private void process(){
		log.info("测试定时任务运行！！每3分钟执行，请直接无视！！！");
	}
	
	
	public static String getIsvMircoAppConfig(HttpServletRequest request,String url) {

		String corpId = request.getParameter("corpid");
		String appId = request.getParameter("appid");

		System.out.println(url);
		String nonceStr = "abcdefg";
		long timeStamp = System.currentTimeMillis() / 1000;
		String signedUrl = url;
		String accessToken = null;
		String ticket = null;
		String signature = null;
		String agentid = null;

		try {
			accessToken = dingDingAuthorizationService.getIsvAccessToken(Env.SUITE_CORP_ID,corpId);
			ticket = dingDingAuthorizationService.getIsvJsTicket(Env.SUITE_CORP_ID,corpId);
			signature = SignatureController.sign(ticket, nonceStr, timeStamp, signedUrl);
			agentid = SignatureController.getAgentId(Env.SUITE_CORP_ID,corpId, appId);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "{jsticket:'" + ticket + "',signature:'" + signature + "',nonceStr:'" + nonceStr + "',timeStamp:'"
				+ timeStamp + "',corpId:'" + corpId + "',agentid:'" + agentid+ "',appid:'" + appId + "'}";
	}
	
	public static String getIsvSsoMircoAppConfig(HttpServletRequest request,String url) {

		JSONObject userinfo = null;
		String accessToken = null;

		String code = request.getParameter("code");
		try {
			accessToken = dingDingAuthorizationService.getIsvSsoAccessToken(Env.SUITE_CORP_ID, Env.SUITE_SSO_SECRET);
			userinfo = UserHelper.getAgentUserInfo(accessToken,code);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return userinfo.toJSONString();
	}
	
	public static String getEnterpriseMircoAppConfig(HttpServletRequest request,String url) {

		System.out.println(url);
		String nonceStr = "abcdefg";
		long timeStamp = System.currentTimeMillis() / 1000;
		String signedUrl = url;
		String accessToken = null;
		String ticket = null;
		String signature = null;

		try {
			accessToken = dingDingAuthorizationService.getAccessToken(Env.CORP_ID, Env.SECRET);
			ticket = dingDingAuthorizationService.getJsTicket(Env.CORP_ID, Env.SECRET);
			signature = sign(ticket, nonceStr, timeStamp, signedUrl);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "{jsticket:'" + ticket + "',signature:'" + signature + "',nonceStr:'" + nonceStr + "',timeStamp:'"
				+ timeStamp + "',corpId:'" + Env.CORP_ID + "'}";
	}
	
	
	public static String getEnterpriseSsoMircoAppConfig(HttpServletRequest request,String url) {

		JSONObject userinfo = null;
		String accessToken = null;

		String code = request.getParameter("code");
		try {
			accessToken = dingDingAuthorizationService.getSsoAccessToken(Env.CORP_ID, Env.SSO_SECRET);
			userinfo = UserHelper.getAgentUserInfo(accessToken,code);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return userinfo.toJSONString();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static String sign(String ticket, String nonceStr, long timeStamp, String url) throws OApiException {
		String plain = "jsapi_ticket=" + ticket + "&noncestr=" + nonceStr + "&timestamp=" + String.valueOf(timeStamp)
				+ "&url=" + url;
		try {
			MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
			sha1.reset();
			sha1.update(plain.getBytes("UTF-8"));
			return bytesToHex(sha1.digest());
		} catch (NoSuchAlgorithmException e) {
			throw new OApiResultException(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			throw new OApiResultException(e.getMessage());
		}
	}
	
	private static String bytesToHex(byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}
	
	public static String getAgentId(String suitCorpId,String corpId, String appId) throws OApiException {
		String agentId = null;
		
		String ticket = dingDingAuthorizationService.getTicket(suitCorpId);
		JSONObject ticketJson = JSONObject.parseObject(ticket);
		String accessToken = ticketJson.getString("suiteToken");
		
		String permanentCodeString = dingDingAuthorizationService.getPermanentCode(corpId);
		JSONObject permanentCodeJson = JSONObject.parseObject(permanentCodeString);
		String permanentCode = permanentCodeJson.getString(corpId);
		
		String url = "https://oapi.dingtalk.com/service/get_auth_info?suite_access_token=" + accessToken;
		JSONObject args = new JSONObject();
		args.put("suite_key", Env.SUITE_KEY);
		args.put("auth_corpid", corpId);
		args.put("permanent_code", permanentCode);
		JSONObject response = HttpHelper.httpPost(url, args);

		if (response.containsKey("auth_info")) {
			JSONArray agents = (JSONArray) ((JSONObject) response.get("auth_info")).get("agent");

			for (int i = 0; i < agents.size(); i++) {

				if (((JSONObject) agents.get(i)).get("appid").toString().equals(appId)) {
					agentId = ((JSONObject) agents.get(i)).get("agentid").toString();
					break;
				}
			}
		} else {
			throw new OApiResultException("agentid");
		}
		return agentId;
	}
}
