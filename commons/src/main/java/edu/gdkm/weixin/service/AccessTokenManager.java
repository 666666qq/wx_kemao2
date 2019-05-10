package edu.gdkm.weixin.service;

public interface AccessTokenManager {
	/*
	 * 此方法返回一个合法的令牌，没有就异常抛出
	 * 
	 * RuntimeException 没有获得令牌，异常
	 */
	String getToken(String account) throws RuntimeException;
}
