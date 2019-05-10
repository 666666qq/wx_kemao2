package edu.gdkm.weixin;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import edu.gdkm.weixin.domain.InMessage;
import edu.gdkm.weixin.service.JsonRedisSerializer;

@SpringBootApplication 
@ComponentScan("edu.gdkm")
public class WeixinApplication implements CommonsConfig{
	
	 

	public static void main(String[] args) {
		SpringApplication.run(WeixinApplication.class, args);
	}

	
	
}
