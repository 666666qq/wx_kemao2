package edu.gdkm.weixin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import edu.gdkm.weixin.domain.InMessage;
import edu.gdkm.weixin.domain.event.EventinMessage;
import edu.gdkm.weixin.processors.EventMessageProcessor;

@SpringBootApplication 
@ComponentScan("edu.gdkm")
public class UnsubscribeApplication implements //
	//命令行运行，需要重新run
	//线程等待程序停止通知
	CommandLineRunner,
	//spring销毁时自动调用此接口的方法
	//用于发送停止通知
	DisposableBean,
	ApplicationContextAware,
	CommonsConfig
	
	{

	
	private ApplicationContext ctx;
	
	private static final Logger LOG = LoggerFactory.getLogger(UnsubscribeApplication.class);
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// TODO Auto-generated method stub
		ctx = applicationContext;
		
	}
	
	@Bean
	public MessageListener messageListener(//
			@Autowired//
			@Qualifier("inMessageTemplate")//
			RedisTemplate<String, ? extends InMessage> inMessageTemplate) {
		
		MessageListenerAdapter adapter = new MessageListenerAdapter(this,"handle");
		adapter.setSerializer(inMessageTemplate.getValueSerializer());
		return adapter;
		
	};
	//正常处理信息
	public void handle(EventinMessage msg) {
		
		LOG.trace("处理消息{}",msg);
		//1获取事件类型
		String id = msg.getEvent().toLowerCase() + "MessageProcessor";
		try {
		EventMessageProcessor mp = (EventMessageProcessor) ctx.getBean(id);
		
		if(mp != null) {
			
			mp.onMessage(msg);
		}else {
			LOG.error("利用Bean的ID{}不能找到一个消息事件的处理器！",id);
		}
	}catch (NoSuchBeanDefinitionException e) {
		LOG.trace("当前模块不适合处理{} 消息，没有对应的处理器实现：",msg.getEvent());
		
	}catch (Exception e) {
		LOG.error("无法处理事件："+e.getLocalizedMessage(), e);
	}
}
	
	@Bean
	 public RedisMessageListenerContainer messageListenerContainer (
			 @Autowired RedisConnectionFactory connectionFactory,
			 @Autowired
			 MessageListener messageListener) {
		RedisMessageListenerContainer c = new RedisMessageListenerContainer();
		c.setConnectionFactory(connectionFactory);
		//添加消息监听器
		
		Topic topic = new ChannelTopic("czpkemao_2_event");
		
		c.addMessageListener(messageListener, topic);
		
		return c;
		
	}
	 
 
	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(UnsubscribeApplication.class, args);
		
//		CountDownLatch countDownLatch = new CountDownLatch(1);
//		countDownLatch.await();
	}
	
	
	

}
