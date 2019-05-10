package edu.gdkm.weixin.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.gdkm.weixin.domain.ResponseError;
import edu.gdkm.weixin.domain.ResponseMessage;
import edu.gdkm.weixin.domain.ResponseToken;
import edu.gdkm.weixin.domain.User;


@Service// 把当前类对象加入Spring中管理
public class WeiXinProxy {
	
	private static final Logger LOG = LoggerFactory.getLogger(WeiXinProxy.class);
	
	@Autowired
	private AccessTokenManager accessTokenManager;
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	public User getUser(String account,String openId) {
		
		String accessToke = accessTokenManager.getToken(account);
		
		String url = "https://api.weixin.qq.com/cgi-bin/user/info"
				+ "?access_token=" + accessToke//
				+ "&openid=" +openId
				+ "&lang=zh_CN";
		
		HttpClient hc =HttpClient.newBuilder().build();
		
		HttpRequest request = HttpRequest.newBuilder(URI.create(url))
				.GET()
				.build();
		//发送请求
		//数据类型转换
		//ofString转换成String类型
		//Charset.fot UTF-8转换
		
		ResponseMessage msg;
		try {
			HttpResponse<String> response = hc.send(request, BodyHandlers.ofString(Charset.forName("UTF-8")));
			
			String body = response.body();
			
			LOG.trace("调用远程接口返回的内容:\n{}", body);
			
			if(!body.contains("errcode")) {
				
				//成功
				User user = objectMapper.readValue(body, User.class);
				return user;
			}
			
		} catch (Exception e) {
		// TODO Auto-generated catch block
			LOG.error("调用运程接口出现错误："+ e.getLocalizedMessage(), e);
		}
		return null;
	}

	public void sendText(String account, String openId,String string) {
		// TODO 发送文本信息给指定的用户
		
	} 

}
