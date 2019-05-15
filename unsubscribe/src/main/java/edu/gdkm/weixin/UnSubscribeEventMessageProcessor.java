package edu.gdkm.weixin;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.gdkm.weixin.domain.User;
import edu.gdkm.weixin.domain.event.EventinMessage;
import edu.gdkm.weixin.processors.EventMessageProcessor;
import edu.gdkm.weixin.repository.UserRepository;

@Service("unsubscribeMessageProcessor")
public class UnSubscribeEventMessageProcessor implements EventMessageProcessor{
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	@Transactional
	public void onMessage(EventinMessage msg) {
		if(msg.getEvent().equals("unsubscribe")) {
		System.out.println("取消关注消息处理器： "+ msg);
		//解除用户关注状态
		//一般不删除数据，标记已经取消关注
		User user = this.userRepository.findByOpenId(msg.getFromUserName());
		
		if(user != null) {
			
			user.setStatus(User.Status.IS_UNSUBSCRIBE);
			user.setUnsubTime(new Date());
		}
		
	}
	}
}