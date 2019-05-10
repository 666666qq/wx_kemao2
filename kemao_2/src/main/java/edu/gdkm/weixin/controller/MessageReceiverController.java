package edu.gdkm.weixin.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import edu.gdkm.weixin.domain.InMessage;
import edu.gdkm.weixin.service.MessageConvertHelper;


//是满足他风格的一种控制器
@RestController

@RequestMapping("kemao_02chenzhipeng/message/receiver")
public class MessageReceiverController {
	
	private static final Logger LOG =LoggerFactory.getLogger(MessageReceiverController.class);
	
	@Autowired
	private XmlMapper xmlMapper;
	
	@Autowired
	private RedisTemplate<String, ?extends InMessage > inMessageTemplate;
	
	

	@GetMapping
	public String echo(//
	@RequestParam("signature") String signature,//
	@RequestParam("timestamp") String timestamp,//
	@RequestParam("nonce") String nonce,//
	@RequestParam("echostr") String echostr//
	
		){
		
	

		return echostr;
	}
	@PostMapping
	public String onMessage(
			@RequestParam("signature") String signature,//
			@RequestParam("timestamp") String timestamp,//
			@RequestParam("nonce") String nonce,//
			@RequestBody String xml) throws JsonParseException, JsonMappingException, IOException  {
		//收到消息
		//装换消息
		//把消息丢入消息队列
		LOG.trace("收到的消息原文:\n{}\n",xml);
		//
		 
		//InMessage inMessage = MessageConvertHelper.convert(xml);
		InMessage inMessage = convert(xml);
		
		if(inMessage == null) {
			LOG.error("消息无法转换！原文: \n{} \n",xml);
			return "success";
		}
		
		LOG.debug("转换后的消息对象\n{}\n",inMessage);
		
		//1完成对象的序列化
//		ByteArrayOutputStream bos = new ByteArrayOutputStream();
//		ObjectOutputStream out = new ObjectOutputStream(bos);
//		out.writeObject(inMessage);
//		
//		byte[] data = bos.toByteArray();
		
		
//	inMessageTemplate.execute(new RedisCallback<InMessage>() {

//			@Override
//			public InMessage doInRedis(RedisConnection connection) throws DataAccessException {
//				// TODO Auto-generated method stub
//				String channel = "czp" + inMessage.getMsgType();
//				connection.publish(channel.getBytes(), data);
//				return null;
//			}
//		});
			
			String channel = "czpkemao_2_" + inMessage.getMsgType();
			inMessageTemplate.convertAndSend(channel, inMessage);
		
		return "success";
		
	
	}

		private InMessage convert(String xml) throws JsonParseException, JsonMappingException, IOException {
			Class<? extends InMessage> c = MessageConvertHelper.getClass(xml);
			InMessage msg = xmlMapper.readValue(xml, c);
			return msg;
	}

	
}

