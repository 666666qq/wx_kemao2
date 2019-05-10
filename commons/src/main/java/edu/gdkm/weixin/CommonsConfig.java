 package edu.gdkm.weixin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import edu.gdkm.weixin.domain.InMessage;
import edu.gdkm.weixin.json.JsonRedisSerializer;


public interface CommonsConfig extends 

			CommandLineRunner,
//spring销毁时自动调用此接口的方法
//用于发送停止通知
			DisposableBean {


	
	public static final Logger LOG = LoggerFactory.getLogger(CommonsConfig.class);
	
	
	@Bean
		public default XmlMapper xmlMapper() {
			XmlMapper mapper = new XmlMapper();
			return mapper;
		}
	
	
	@Bean 
	public default RedisTemplate<String, ? extends InMessage> inMessageTemplate(//
			@Autowired RedisConnectionFactory connectionFactory) {

		RedisTemplate<String, ? extends InMessage> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);
		// 使用序列化程序完成对象的序列化和反序列化，可以自定义
		template.setValueSerializer(new JsonRedisSerializer<InMessage>());

		return template;
	}
	
	

	
	public  final Object stopMonitor = new Object();
	@Override
	public default  void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		new Thread(() ->{
			synchronized (stopMonitor) {
				try {
					stopMonitor.wait();//无线等待
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					LOG.error("无法等待停止通知："+e.getLocalizedMessage(), e);
				}
			}
		
	}).start();
	}
	
	@Override
	public  default void  destroy() throws Exception {
		// TODO Auto-generated method stub
		synchronized (stopMonitor) {
			stopMonitor.notify();
		}
	}
}
