package edu.gdkm.weixin.service.impl;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.Charset;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.gdkm.weixin.domain.ResponseError;
import edu.gdkm.weixin.domain.ResponseMessage;
import edu.gdkm.weixin.domain.ResponseToken;
import edu.gdkm.weixin.service.AccessTokenManager;
@Service
public class AccessTokenManagerSample implements AccessTokenManager{
	
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	public String getToken(String account) throws RuntimeException {
		// TODO Auto-generated method stub
		
		String appid = "wx71fa6ec220145916";
		String appSecret = "b6376020f63e7cb0db23f5422a155c97";
		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential"//
				+ "&appid="+appid//
				+ "&secret="+appSecret;
		
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
			
			if(body.contains("errcode")) {
				
				msg = objectMapper.readValue(body, ResponseError.class);
				msg.setStatus(2);
				//错误
			}else {
				//成功
				msg = objectMapper.readValue(body, ResponseToken.class);
				msg.setStatus(1);
			}
			
			if(msg.getStatus()==1) {
				return ((ResponseToken) msg).getToke();
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("获取访问令牌出现问题：" +e.getLocalizedMessage(), e);
		}
		throw new RuntimeException("获取访问令牌出现问题,"
		+"错误代码="+((ResponseError) msg).getErrorCode()
		+ ",错误信息= "+((ResponseError) msg).getErrorMessage());
		
		
	
		
	}

}
