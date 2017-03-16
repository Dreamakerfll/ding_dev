package com.dreamaker.service.ding;

public interface DingDingAuthorizationService {
	
	/**
	 * 创建Ticket
	 * @return
	 */
	public String createTicket(String suitCorpId,String ticket);
	
	public String getTicket(String suitCorpId);
	
	/**
	 * 创建permanentCode
	 * @param corpId
	 * @param permantentCode
	 * @return
	 */
	public String createPermanentCode(String corpId,String permantentCode);
	
	public String getPermanentCode(String corpId);
	
	public String getIsvAccessToken(String suitCorpId,String corpId);
	
	public String getIsvJsTicket(String suitCorpId,String corpId);
	
	public String getIsvSsoAccessToken(String suitCorpId,String suitSsoSecret);
	
	public String getAccessToken(String corpId,String corpSecret);
	
	public String getJsTicket(String corpId,String corpSecret);
	
	public String getSsoAccessToken(String corpId,String ssoSecret);
	
}
