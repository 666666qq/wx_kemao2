package edu.gdkm.weixin.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseToken extends ResponseMessage{
	@JsonProperty("access_token")
	private String toke;
	@JsonProperty("expires_in")
	private long expiresIn;
	public String getToke() {
		return toke;
	}
	public void setToke(String toke) {
		this.toke = toke;
	}
	public long getExpiresIn() {
		return expiresIn;
	}
	public void setExpiresIn(long expiresIn) {
		this.expiresIn = expiresIn;
	}

}
