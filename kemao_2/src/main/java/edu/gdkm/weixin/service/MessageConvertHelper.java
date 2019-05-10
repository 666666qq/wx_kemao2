package edu.gdkm.weixin.service;

import java.io.StringReader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.JAXB;

import edu.gdkm.weixin.domain.InMessage;
import edu.gdkm.weixin.domain.event.EventinMessage;
import edu.gdkm.weixin.domain.image.ImageInMessage;
import edu.gdkm.weixin.domain.text.TextInMessage;

public class MessageConvertHelper {
	
	private static final Map<String, Class<? extends InMessage>> typeMap = new ConcurrentHashMap<>();      
	
	
	static {
		typeMap.put("text", TextInMessage.class);
		typeMap.put("image", ImageInMessage.class);

		// 其余的类型这样关联起来
		typeMap.put("vioce", TextInMessage.class);
		typeMap.put("video", TextInMessage.class);
		typeMap.put("location", TextInMessage.class);
		typeMap.put("event", EventinMessage.class);
		typeMap.put("link", TextInMessage.class);
		typeMap.put("shortvideo", TextInMessage.class);
	}
	
	public static Class<? extends InMessage> getClass(String xml) {
		// 获取类型
		String type = xml.substring(xml.indexOf("<MsgType><![CDATA[") + 18);
		type = type.substring(0, type.indexOf("]"));

		// 获取Java类
		Class<? extends InMessage> c = typeMap.get(type);
		return c;
	}

	// 2.提供一个静态的方法，可以传入XML，把XML转换为Java对象
	public static <T extends InMessage> T convert(String xml) {
		Class<? extends InMessage> c = getClass(xml);
		if (c == null) {
			return null;
		}
		//JAXB
		@SuppressWarnings("unchecked")
		T msg = (T) JAXB.unmarshal(new StringReader(xml),c);
		return msg;
		
	}
}
