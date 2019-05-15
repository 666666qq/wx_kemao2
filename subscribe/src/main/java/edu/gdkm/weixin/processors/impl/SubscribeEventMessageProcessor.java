package edu.gdkm.weixin.processors.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.gdkm.weixin.domain.User;
import edu.gdkm.weixin.domain.event.EventinMessage;
import edu.gdkm.weixin.processors.EventMessageProcessor;
import edu.gdkm.weixin.repository.UserRepository;
import edu.gdkm.weixin.service.AccessTokenManager;
import edu.gdkm.weixin.service.WeiXinProxy;

@Service("subscribeMessageProcessor")
public class SubscribeEventMessageProcessor implements EventMessageProcessor{
	
	private static final Logger LOG = LoggerFactory.getLogger(SubscribeEventMessageProcessor.class);
//	@Autowired
//	private AccessTokenManager accessTokenManager;
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private WeiXinProxy weiXinProxy;
	
	@Override
	public void onMessage(EventinMessage msg) {
		LOG.trace("关注消息处理器： "+ msg);
		String account = msg.getToUserName();
		String openId = msg.getFromUserName();
		// 检查是否有关注，如果有已关注，
		User user = this.userRepository.findByOpenId(openId);
		//如果没关注，就需要获取用户资料
		if(user == null||user.getStatus() != User.Status.IS_SUBSCRIBE) {
			//先获取访问令牌
			User wxUser = weiXinProxy.getUser(null, openId);
			if(wxUser == null) {
				return;
			}
			
			//把信息保存到数据库
			if(user !=null) {
				wxUser.setId(user.getId());
				wxUser.setSubTime(user.getSubTime());
				
			}
			wxUser.setStatus(User.Status.IS_SUBSCRIBE);
			this.userRepository.save(wxUser);
			this.weiXinProxy.sendText(account,openId ,"终于等到你了，进来玩吧，既然来了就别走了");
		}
		 
		
	
		//调用远程接口
		//信息保存数据库
		
		
	//	String token = accessTokenManager.getToken("null");
	//	LOG.trace("访问令牌: "+ token);
		
		
	}

}
