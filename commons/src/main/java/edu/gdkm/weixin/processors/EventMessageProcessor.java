package edu.gdkm.weixin.processors;

import edu.gdkm.weixin.domain.event.EventinMessage;

public interface EventMessageProcessor {
	void onMessage(EventinMessage msg);

}
