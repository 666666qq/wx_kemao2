package edu.gdkm.weixin.service.impl;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import javax.management.RuntimeErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.gdkm.weixin.domain.ResponseError;
import edu.gdkm.weixin.domain.ResponseMessage;
import edu.gdkm.weixin.domain.ResponseToken;
import edu.gdkm.weixin.service.AccessTokenManager;
@Service
public class AccessTokenManagerSample implements AccessTokenManager{
	
	private static final Logger LOG= LoggerFactory.getLogger(AccessTokenManagerSample.class);
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	@Qualifier("redisTemplate")
	private RedisTemplate<String, ResponseToken> redisTemplate;
	
	@Override
	public String getToken(String account) throws RuntimeException {
		String key = "wx_access_token" ;
		ResponseToken token = redisTemplate.boundValueOps(key).get();
		if(token == null) {
			LOG.trace("数据可没有令牌，需要重新获取");
			
			Boolean locked = redisTemplate.boundValueOps(key + "_lock")//
			.setIfAbsent(new ResponseToken(), 1, TimeUnit.MINUTES);
			LOG.trace("获取事务锁结束：{ }" ,locked);
		if(locked != null && locked ==true) {
			try {	
				token = redisTemplate.boundValueOps(key).get();
				if(token == null) {
					LOG.trace("调用远程接口获取令牌");
					token = getResponseToken(account);
					
					 redisTemplate.boundValueOps(key).set(token,token.getExpiresIn(), TimeUnit.SECONDS);
				}
				
		}finally {
			redisTemplate.delete(key);
		}
	}else {
		throw new RuntimeException("没有获取事务锁，无法更新新令牌");
		}
	}
		return token.getToke();
		
	}
		
	private ResponseToken getResponseToken (String account) {
		

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
			LOG.trace("调用远程接口返回值：\n{}", body);
			
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
				
				return (ResponseToken) msg;
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


